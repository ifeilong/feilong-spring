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
package com.feilong.spring.web.servlet;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.MapUtil.newHashMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.AsyncTaskMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.CallableMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.DeferredResultMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.feilong.core.util.MapUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.servlet.http.RequestUtil;

/**
 * The Class ModelAndViewUtil.
 * 
 * <h3>关于 {@link ModelAndView}是 null的情况</h3>
 * 
 * <blockquote>
 * 在以下的处理方式中, {@link ModelAndView}或者 model是null
 * 
 * <ul>
 * <li>
 * {@link AsyncTaskMethodReturnValueHandler#handleReturnValue(Object, MethodParameter, ModelAndViewContainer, NativeWebRequest)}</li>
 * <li>
 * {@link CallableMethodReturnValueHandler#handleReturnValue(Object, MethodParameter, ModelAndViewContainer, NativeWebRequest)}</li>
 * <li>
 * {@link DeferredResultMethodReturnValueHandler#handleReturnValue(Object, MethodParameter, ModelAndViewContainer, NativeWebRequest)}</li>
 * <li>{@link HttpEntityMethodProcessor#handleReturnValue(Object, MethodParameter, ModelAndViewContainer, NativeWebRequest)}</li>
 * <li>{@link RequestResponseBodyMethodProcessor#handleReturnValue(Object, MethodParameter, ModelAndViewContainer, NativeWebRequest)}</li>
 * <li>{@link ServletInvocableHandlerMethod#invokeAndHandle(ServletWebRequest, ModelAndViewContainer, Object...)}</li>
 * </ul>
 * 
 * <p>
 * 一旦 以前调用了 {@link ModelAndViewContainer#setRequestHandled(boolean)} ,那么在构建 {@link ModelAndView} 的时候就会跳过去 ,并且直接返回 <span
 * style="color:red">null</span> ,参见
 * {@link ModelAndViewMethodReturnValueHandler#handleReturnValue(Object, MethodParameter, ModelAndViewContainer, NativeWebRequest)} 以及
 * {@link RequestMappingHandlerAdapter#getModelAndView(ModelAndViewContainer, ModelFactory, NativeWebRequest)}
 * </p>
 * 
 * <p>
 * 这样在,{@link HandlerInterceptor#postHandle(HttpServletRequest, HttpServletResponse, Object, ModelAndView)} 流程,以及
 * {@link View#render(Map, HttpServletRequest, HttpServletResponse)} 里面要加以判断;
 * </p>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.4.0
 */
public final class ModelAndViewUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelAndViewUtil.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private ModelAndViewUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得 {@link HttpServletRequest} 以及 {@link ModelAndView}.
     *
     * @param request
     *            the request
     * @param modelAndView
     *            the ModelAndView that the handler returned <span style="color:red">(can also be null)</span> <br>
     *            with the name of the view and the required model data, <span style="color:red"> or null if the request has been handled
     *            directly </span>
     * @return the data map
     */
    public static Map<String, Object> getRequestAndModelAttributeMap(HttpServletRequest request,ModelAndView modelAndView){
        if (null == modelAndView){
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("modelAndView is null,request info:[{}]", JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request)));
            }
        }
        //---------------------------------------------------------------
        Map<String, Object> model = (null == modelAndView) ? null : modelAndView.getModel();
        return getRequestAndModelAttributeMap(request, model);
    }

    //---------------------------------------------------------------

    /**
     * 获得 request and model attribute map.
     *
     * @param request
     *            the request
     * @param model
     *            the model
     * @return the request and model attribute map
     */
    public static Map<String, Object> getRequestAndModelAttributeMap(HttpServletRequest request,Map<String, Object> model){
        Map<String, Object> requestAttributeMap = RequestUtil.getAttributeMap(request);

        //新创建个map对象, 这样操作不会影响原始数据
        Map<String, Object> map = newHashMap();
        MapUtil.putAllIfNotNull(map, requestAttributeMap);
        MapUtil.putAllIfNotNull(map, model);
        return map;
    }

    //---------------------------------------------------------------

    /**
     * 获得 view name.
     *
     * @param modelAndView
     *            the model and view
     * @return the view name
     */
    public static String getViewName(ModelAndView modelAndView){
        if (isNullOrEmpty(modelAndView)){
            return EMPTY;
        }

        //---------------------------------------------------------------

        View view = modelAndView.getView();
        return isNullOrEmpty(view) ? modelAndView.getViewName() : view.toString();
    }
}
