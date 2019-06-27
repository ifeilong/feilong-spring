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
package com.feilong.spring.web.event.builder;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ToStringConfig.DEFAULT_CONFIG;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.lang.annotation.AnnotationToStringBuilder;
import com.feilong.spring.web.method.HandlerMethodUtil;
import com.feilong.spring.web.servlet.mvc.ControllerUtil;

/**
 * {@link RequestMappingInfo} 信息提取.
 * 
 * <h3>作用:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 可以在日志文件或者控制台输出如下信息:
 * </p>
 * 
 * <pre class="code">
14:28:16 INFO  (AbstractContextRefreshedHandlerMethodLogginEventListener.java:137) render() - handler method ,size:[13],info:
url                                                                method   header ClientCache 
------------------------------------------------------------------ -------- ------ ----------- 
/c{categoryCode}-{name}                                                                        
/c{categoryCode}/m{material}-c{color}-s{size}-k{kind}-s{style}.htm                             
/errors/404400                                                                                 
/errors/500                                                                                    
/exceptiontest                                                                                 
/index,/,/index.htm                                                GET                         
/item/{itemid}                                                     GET             5分钟         
/jsp/{category}/{page}                                             GET,POST                    
/jsp/{category}/{page}/{small}                                     GET,POST                    
/requestbodytest                                                                               
/{c1}-{c2}-{c3}-{c4}-{label1}-{label2}-{label3}.htm                                            
/{c1}-{c2}-{c3}-{c4}-{label}.htm                                                               
/{c1}-{c2}-{c3}-{c4}.htm
 * 
 * 
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>参考配置:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
{@code 
    <!-- 启动的时候,显示 路径 method等 信息 -->
    <bean id="contextRefreshedHandlerMethodInfoEventListener" class=
"com.feilong.spring.web.event.ContextRefreshedHandlerMethodInfoEventListener">
        <property name="annotationAndAnnotationToStringBuilderMap">
            <map>
                <entry key="com.feilong.spring.web.servlet.interceptor.clientcache.ClientCache">
                    <bean class="com.feilong.spring.web.servlet.interceptor.clientcache.ClientCacheToStringBuilder" />
                </entry>
            </map>
        </property>
    </bean>
}
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public class HandlerMethodInfoExtractor{

    /** Don't let anyone instantiate this class. */
    private HandlerMethodInfoExtractor(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 提取.
     *
     * @param requestMappingInfo
     *            the request mapping info
     * @param handlerMethod
     *            the handler method
     * @param annotationAndAnnotationToStringBuilderMap
     *            the annotation and annotation to string builder map
     * @return the map
     */
    public static Map<String, Object> extract(
                    RequestMappingInfo requestMappingInfo,
                    HandlerMethod handlerMethod,
                    Map<Class<Annotation>, AnnotationToStringBuilder<Annotation>> annotationAndAnnotationToStringBuilderMap){
        Map<String, Object> keyAndValueMap = newLinkedHashMap();

        //塞RequestMappingInfo 本身的信息,比如 url ,method,header 等信息进去.
        putRequestMappingInfo(keyAndValueMap, requestMappingInfo);

        //塞handlerMethod 本身的信息,比如 class类等信息进去.
        //4.0.1
        putHandlerMethod(keyAndValueMap, handlerMethod);

        //塞配置的要提取的annotaion信息进去.
        putAnnotationInfo(keyAndValueMap, handlerMethod, annotationAndAnnotationToStringBuilderMap);

        return keyAndValueMap;
    }

    //---------------------------------------------------------------

    /**
     * Put handler method.
     *
     * @param keyAndValueMap
     *            the key and value map
     * @param handlerMethod
     *            the handler method
     * @since 4.0.1
     */
    private static void putHandlerMethod(Map<String, Object> keyAndValueMap,HandlerMethod handlerMethod){
        keyAndValueMap.put("Controller", HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod));
        keyAndValueMap.put("Method", HandlerMethodUtil.getHandlerMethodName(handlerMethod));
    }

    //---------------------------------------------------------------

    /**
     * 塞RequestMappingInfo 本身的信息,比如 url ,method,header 等信息进去.
     *
     * @param keyAndValueMap
     *            the key and value map
     * @param requestMappingInfo
     *            the request mapping info
     */
    private static void putRequestMappingInfo(Map<String, Object> keyAndValueMap,RequestMappingInfo requestMappingInfo){
        PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
        RequestMethodsRequestCondition requestMethodsRequestCondition = requestMappingInfo.getMethodsCondition();
        HeadersRequestCondition headersRequestCondition = requestMappingInfo.getHeadersCondition();

        Set<RequestMethod> methods = requestMethodsRequestCondition.getMethods();

        keyAndValueMap.put("url", ConvertUtil.toString(patternsRequestCondition.getPatterns(), DEFAULT_CONFIG));

        //---------------------------------------------------------------
        //since 4.0.6
        keyAndValueMap.put("get", contains(methods, RequestMethod.GET));
        keyAndValueMap.put("post", contains(methods, RequestMethod.POST));
        keyAndValueMap.put("put", contains(methods, RequestMethod.PUT));
        keyAndValueMap.put("head", contains(methods, RequestMethod.HEAD));
        keyAndValueMap.put("patch", contains(methods, RequestMethod.PATCH));
        keyAndValueMap.put("delete", contains(methods, RequestMethod.DELETE));
        keyAndValueMap.put("options", contains(methods, RequestMethod.OPTIONS));
        keyAndValueMap.put("trace", contains(methods, RequestMethod.TRACE));

        //---------------------------------------------------------------
        Set<NameValueExpression<String>> expressions = headersRequestCondition.getExpressions();
        //since 4.0.6
        keyAndValueMap.put("isAjax", isAjax(expressions));
        keyAndValueMap.put("header", ConvertUtil.toString(expressions, DEFAULT_CONFIG));
    }

    //---------------------------------------------------------------

    /**
     * Checks if is ajax.
     *
     * @param expressions
     *            the expressions
     * @return true, if is ajax
     * @since 4.0.6
     */
    private static boolean isAjax(Set<NameValueExpression<String>> expressions){
        if (isNullOrEmpty(expressions)){
            return false;
        }

        //---------------------------------------------------------------
        for (NameValueExpression<String> nameValueExpression : expressions){
            if (null == nameValueExpression){
                continue;
            }

            //---------------------------------------------------------------
            if (ControllerUtil.HEADER_WITH_AJAX_SPRINGMVC.equals(nameValueExpression.toString())){
                return true;
            }
        }
        return false;
    }

    /**
     * Contains.
     *
     * @param methods
     *            the methods
     * @param requestMethod
     *            the request method
     * @return true, if successful
     * @since 4.0.6
     */
    private static boolean contains(Set<RequestMethod> methods,RequestMethod requestMethod){
        if (isNullOrEmpty(methods)){
            return true;
        }
        return methods.contains(requestMethod);
    }

    //---------------------------------------------------------------

    /**
     * 塞配置的要提取的annotaion信息进去.
     *
     * @param keyAndValueMap
     *            the key and value map
     * @param handlerMethod
     *            the handler method
     * @param annotationAndAnnotationToStringBuilderMap
     *            the annotation and annotation to string builder map
     */
    private static void putAnnotationInfo(
                    Map<String, Object> keyAndValueMap,
                    HandlerMethod handlerMethod,
                    Map<Class<Annotation>, AnnotationToStringBuilder<Annotation>> annotationAndAnnotationToStringBuilderMap){

        for (Map.Entry<Class<Annotation>, AnnotationToStringBuilder<Annotation>> annotationEntry : annotationAndAnnotationToStringBuilderMap
                        .entrySet()){
            Class<Annotation> annotationClass = annotationEntry.getKey();
            AnnotationToStringBuilder<Annotation> annotationToStringBuilder = annotationEntry.getValue();

            Annotation annotation = handlerMethod.getMethodAnnotation(annotationClass);

            keyAndValueMap.put(annotationClass.getSimpleName(), annotationToStringBuilder.build(annotation));
        }
    }
}
