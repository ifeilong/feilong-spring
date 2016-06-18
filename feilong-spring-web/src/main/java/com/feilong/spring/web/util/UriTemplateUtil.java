/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.spring.web.util;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.UriTemplate;
import org.springframework.web.util.UrlPathHelper;

import com.feilong.core.Validator;
import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.util.MapUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * {@link org.springframework.web.util.UriTemplate},此类是 单值 expend.
 * 
 * <h3>about {@link org.springframework.web.servlet.HandlerMapping#URI_TEMPLATE_VARIABLES_ATTRIBUTE URI_TEMPLATE_VARIABLES_ATTRIBUTE}</h3>
 * 
 * <blockquote>
 * <p>
 * {@link org.springframework.web.servlet.handler.AbstractUrlHandlerMapping#exposeUriTemplateVariables(Map, HttpServletRequest)}
 * </p>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.util.AntPathMatcher
 * @see org.springframework.web.servlet.HandlerMapping#URI_TEMPLATE_VARIABLES_ATTRIBUTE
 * @see org.springframework.web.servlet.handler.AbstractUrlHandlerMapping#exposeUriTemplateVariables(Map, HttpServletRequest)
 * @see org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping#handleMatch(RequestMappingInfo, String,
 *      HttpServletRequest)
 * @since 1.0.4
 */
public class UriTemplateUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UriTemplateUtil.class);

    /** Don't let anyone instantiate this class. */
    private UriTemplateUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 获得当前uri template 变量值.
     * 
     * @param request
     *            the request
     * @param pathVarName
     *            the path var name
     * @return the uri template variable value
     * @see #getUriTemplateVariables(HttpServletRequest)
     */
    public static String getUriTemplateVariableValue(HttpServletRequest request,String pathVarName){
        Map<String, String> uriTemplateVariables = getUriTemplateVariables(request);
        if (uriTemplateVariables == null || !uriTemplateVariables.containsKey(pathVarName)){
            throw new IllegalStateException("Could not find @PathVariable [" + pathVarName + "] in @RequestMapping");
        }
        return uriTemplateVariables.get(pathVarName);
    }

    /**
     * 判断模板请求里面是否有指定的变量名称.
     * 
     * @param request
     *            the request
     * @param pathVarName
     *            指定的变量名称
     * @return true, if successful
     * @see #getUriTemplateVariables(HttpServletRequest)
     */
    public static boolean hasPathVarName(HttpServletRequest request,String pathVarName){
        Map<String, String> uriTemplateVariables = getUriTemplateVariables(request);
        return null != uriTemplateVariables && uriTemplateVariables.containsKey(pathVarName);
    }

    /**
     * Gets the uri template variables.
     * 
     * <p>
     * 返回值是 {@code Map<String, String>} 需要自行转换
     * </p>
     * 
     * @param request
     *            the request
     * @return request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)
     * @see org.springframework.web.servlet.HandlerMapping#URI_TEMPLATE_VARIABLES_ATTRIBUTE
     * @see com.feilong.servlet.http.RequestUtil#getAttribute(HttpServletRequest, String)
     */
    public static Map<String, String> getUriTemplateVariables(HttpServletRequest request){
        return RequestUtil.getAttribute(request, HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }

    /**
     * 获得最佳的HandlerMapping映射内的匹配模式.
     * 
     * @param request
     *            the request
     * @return the best matching pattern
     * @see org.springframework.web.servlet.HandlerMapping#BEST_MATCHING_PATTERN_ATTRIBUTE
     * @see org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping#handleMatch(RequestMappingInfo, String,
     *      HttpServletRequest)
     */
    public static String getBestMatchingPattern(HttpServletRequest request){
        return RequestUtil.getAttribute(request, HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    }

    /**
     * 自动寻找matchingPatternPath 扩充模板值.
     * 
     * <p>
     * urlPathHelper.getOriginatingContextPath(request) + expandUrl + (Validator.isNotNull(queryString) ? "?" + queryString : "");
     * </p>
     * 
     * @param request
     *            the request
     * @param variableName
     *            模板变量
     * @param value
     *            the value
     * @return 获得一个新的url,参数部分会被原样返回<br>
     *         urlPathHelper.getOriginatingContextPath(request) + expandUrl + (Validator.isNotNull(queryString) ? "?" + queryString : "");
     *
     * @see #getBestMatchingPattern(HttpServletRequest)
     * @see RequestUtil#getOriginatingServletPath(HttpServletRequest)
     */
    public static String expandBestMatchingPattern(HttpServletRequest request,String variableName,String value){
        String requestPath = RequestUtil.getOriginatingServletPath(request);
        String matchingPatternPath = getBestMatchingPattern(request);// 这种方法可能不太好 可能被覆盖

        String expandUrl = expandWithVariable(requestPath, matchingPatternPath, variableName, value);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.format(UrlPathHelperUtil.getUrlPathHelperMapForLog(request)));
        }

        String queryString = request.getQueryString();
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        return urlPathHelper.getOriginatingContextPath(request) + expandUrl
                        + (Validator.isNullOrEmpty(queryString) ? "?" + queryString : "");
    }

    /**
     * 扩充模板值.
     * 
     * <pre class="code">
     * String requestPath = &quot;/s/c-m-c-s-k-s-o.htm&quot; 
     * String matchingPatternPath = &quot;/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm&quot;
     * String variableName="color";
     * String value="100";
     * expandWithVariable(requestPath,matchingPatternPath,variableName,value);
     * 
     * return /s/c-m-c<span style="color:red">100</span>-s-k-s-o.htm?keyword=鞋;
     * </pre>
     * 
     * @param requestPath
     *            the request path
     * @param matchingPatternPath
     *            the matching pattern path
     * @param variableName
     *            模板变量名称
     * @param value
     *            指定值
     * @return 获得一个新的url<br>
     *         参数部分 需要自己添加
     */
    public static String expandWithVariable(String requestPath,String matchingPatternPath,String variableName,String value){
        Map<String, String> map = extractUriTemplateVariables(requestPath, matchingPatternPath);
        map.put(variableName, value);
        return expand(matchingPatternPath, map);
    }

    /**
     * Given a pattern and a full path, extract the URI template variables.
     * <p>
     * URI template variables are expressed through curly brackets ('{' and '}'). <br>
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * For pattern "/hotels/{hotel}" and path "/hotels/1"
     * </pre>
     * 
     * 返回:
     * 
     * <pre class="code">
     * a map containing "hotel"->"1".
     * </pre>
     * 
     * </blockquote>
     * 
     * @param requestPath
     *            the request path
     * @param matchingPatternPath
     *            the matching pattern path
     * @return the map
     * @see org.springframework.util.AntPathMatcher
     * @see org.springframework.util.PathMatcher#extractUriTemplateVariables(String, String)
     */
    public static Map<String, String> extractUriTemplateVariables(String requestPath,String matchingPatternPath){
        LOGGER.debug("requestPath:[{}],matchingPatternPath:[{}]", requestPath, matchingPatternPath);
        PathMatcher matcher = new AntPathMatcher();
        return matcher.extractUriTemplateVariables(matchingPatternPath, requestPath);
    }

    //**************************************************************************************

    /**
     * 扩充模板值.
     * 
     * <pre class="code">
     * String matchingPatternPath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
     * String variableName = "color";
     * String value = "100";
     * expandWithVariable(matchingPatternPath, variableName, value);
     * 
     * return /s/c-m-c<span style="color:red">100</span>-s-k-s-o.htm
     * </pre>
     * 
     * @param matchingPatternPath
     *            the matching pattern path
     * @param variableName
     *            the variable name
     * @param value
     *            the value
     * @return the string
     * @see #expand(String, Map)
     */
    public static String expandWithVariable(String matchingPatternPath,String variableName,String value){
        return expand(matchingPatternPath, ConvertUtil.toMap(variableName, value));
    }

    /**
     * 清除 基于 variableNames 变量名称的值.
     * 
     * <pre class="code">
     * 
     * String requestPath = &quot;/s/c500-m60-cred-s-k-s100-o6.htm&quot;;
     * String matchingPatternPath = &quot;/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm&quot;;
     * String[] variableNames = { &quot;color&quot;, &quot;style&quot; };
     * LOGGER.info(UriTemplateUtil.clearVariablesValue(requestPath, matchingPatternPath, variableNames));
     * 
     * return /s/c500-m60-c-s-k-s-o6.htm
     * </pre>
     * 
     * @param requestPath
     *            请求路径
     * @param matchingPatternPath
     *            匹配模式
     * @param variableNames
     *            变量数组
     * @return the string
     * @see #extractUriTemplateVariables(String, String)
     * @see #expand(String, Map)
     */
    public static String clearVariablesValue(String requestPath,String matchingPatternPath,String[] variableNames){
        Map<String, String> map = extractUriTemplateVariables(requestPath, matchingPatternPath);
        if (Validator.isNotNullOrEmpty(variableNames)){
            for (String variableName : variableNames){
                map.put(variableName, "");// 将这些变量的值 设为""
            }
        }
        return expand(matchingPatternPath, map);
    }

    /**
     * 仅仅保留这些参数的值,和 clearVariablesValue相反.
     * 
     * <pre class="code">
     * 
     * String requestPath = &quot;/s/c500-m60-cred-s-k-s100-o6.htm&quot;;
     * String matchingPatternPath = &quot;/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm&quot;;
     * String[] variableNames = { &quot;color&quot;, &quot;style&quot; };
     * LOGGER.info(UriTemplateUtil.clearVariablesValue(requestPath, matchingPatternPath, variableNames));
     * 
     * return /s/c-m-cred-s-k-s100-o.htm
     * </pre>
     * 
     * @param requestPath
     *            请求路径
     * @param matchingPatternPath
     *            匹配模式
     * @param variableNames
     *            变量数组
     * @return the string
     * @see #clearVariablesValue(String, String, String[])
     * @see #extractUriTemplateVariables(String, String)
     * @see #expand(String, Map)
     */
    public static String retainVariablesValue(String requestPath,String matchingPatternPath,String[] variableNames){
        Map<String, String> map = new HashMap<String, String>();

        if (Validator.isNotNullOrEmpty(variableNames)){
            Map<String, String> opMap = extractUriTemplateVariables(requestPath, matchingPatternPath);
            for (String variableName : variableNames){
                map.put(variableName, opMap.get(variableName));
            }
        }
        return expand(matchingPatternPath, map);
    }

    /**
     * 扩充模板值.
     * 
     * <pre class="code">
     *  String uriTemplatePath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
     *  Map<String, String> map = new HashMap<String, String>();
     *  map.put("color", "100");
     *  map.put("size", "L");
     *   
     * return /s/c-m-c<span style="color:red">100</span>-s<span style="color:red">L</span>-k-s-o.htm
     * </pre>
     * 
     * @param uriTemplatePath
     *            模板
     * @param map
     *            变量-值 map <br>
     *            如果 传入的map 中的key 不在模板中,自动忽略<br>
     *            即只处理 模板中有的key
     * @return the string
     * @see #getVariableNames(String)
     * @see org.springframework.web.util.UriTemplate
     * @see org.springframework.web.util.UriTemplate#expand(Map)
     */
    public static String expand(String uriTemplatePath,Map<String, String> map){
        // 所有的变量
        List<String> variableNames = getVariableNames(uriTemplatePath);

        Map<String, String> opMap = new LinkedHashMap<String, String>();
        // 基于变量 生成对应的 值空map
        for (String variableName : variableNames){
            opMap.put(variableName, null);
        }

        MapUtil.putAllIfNotNull(opMap, map);

        UriTemplate uriTemplate = new UriTemplate(uriTemplatePath);
        URI uri = uriTemplate.expand(opMap);
        return uri.toString();
    }

    /**
     * 变量名称 Return the names of the variables in the template, in order.
     * 
     * @param uriTemplatePath
     *            the uri template path
     * @return the variable names
     * @see org.springframework.web.util.UriTemplate
     * @see org.springframework.web.util.UriTemplate#getVariableNames()
     */
    public static List<String> getVariableNames(String uriTemplatePath){
        UriTemplate uriTemplate = new UriTemplate(uriTemplatePath);
        return uriTemplate.getVariableNames();
    }
}
