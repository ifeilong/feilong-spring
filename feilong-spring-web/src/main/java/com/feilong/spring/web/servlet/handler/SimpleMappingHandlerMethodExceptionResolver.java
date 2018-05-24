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
package com.feilong.spring.web.servlet.handler;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;

import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * 借鉴了 {@link SimpleMappingExceptionResolver}, 但是适用于 HandlerMethodExceptionResolver.
 * 
 * <p>
 * 优先级 <code>exceptionAndExceptionViewNameBuilderMap</code> {@code >} <code>exceptionMappings</code>
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see SimpleMappingExceptionResolver
 * @since 1.11.4
 * @since spring 3.1
 */
public class SimpleMappingHandlerMethodExceptionResolver extends AbstractHandlerMethodExceptionResolver{

    /** The Constant log. */
    private static final Logger                   LOGGER             = LoggerFactory
                    .getLogger(SimpleMappingHandlerMethodExceptionResolver.class);

    //---------------------------------------------------------------

    //优先级  exceptionAndExceptionViewNameBuilderMap  >  exceptionMappings

    /** 异常名字和 路径的隐射. */
    private Properties                            exceptionMappings;

    /**
     * 异常的名字和自定义视图的构造器对应的map.
     * 
     * @since 1.12.1
     */
    private Map<String, ExceptionViewNameBuilder> exceptionAndExceptionViewNameBuilderMap;

    //---------------------------------------------------------------

    /** 把对应的异常放到哪个model 名字中, 方便在页面获取. */
    private String                                exceptionAttribute = SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver#doResolveHandlerMethodException(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.method.HandlerMethod, java.lang.Exception)
     */
    @Override
    protected ModelAndView doResolveHandlerMethodException(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    HandlerMethod handlerMethod,
                    Exception exception){
        String viewName = buildViewName(handlerMethod, exception, request, response);

        return isNotNullOrEmpty(viewName) ? packModelAndView(viewName, exception) : null;
    }

    //---------------------------------------------------------------

    /**
     * Builds the view name.
     * 
     * @param handlerMethod
     *            the handler method
     * @param exception
     *            the exception
     * @param request
     *            the request
     * @param response
     *            the response
     *
     * @return the string
     * @since 1.12.1
     */
    private String buildViewName(HandlerMethod handlerMethod,Exception exception,HttpServletRequest request,HttpServletResponse response){
        String customerViewName = customerViewName(handlerMethod, exception, request, response);

        if (isNotNullOrEmpty(customerViewName)){
            return customerViewName;
        }

        String viewName = determineViewName(exception, request, response);
        if (isNotNullOrEmpty(viewName)){
            return viewName;
        }

        return null;
    }

    //---------------------------------------------------------------

    /**
     * Customer view name.
     * 
     * @param handlerMethod
     *            the handler method
     * @param exception
     *            the exception
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the string
     * @since 1.12.1
     */
    protected String customerViewName(
                    HandlerMethod handlerMethod,
                    Exception exception,
                    HttpServletRequest request,
                    HttpServletResponse response){
        if (isNullOrEmpty(exceptionAndExceptionViewNameBuilderMap)){
            return null;
        }

        //---------------------------------------------------------------
        ExceptionViewNameBuilder exceptionViewNameBuilder = exceptionAndExceptionViewNameBuilderMap.get(exception.getClass().getName());
        if (null == exceptionViewNameBuilder){
            return null;
        }

        return exceptionViewNameBuilder.build(handlerMethod, exception, request, response);
    }

    //---------------------------------------------------------------

    /**
     * 封装 {@link ModelAndView}.
     *
     * @param viewName
     *            the view name
     * @param exception
     *            the exception
     * @return the model and view
     * @since 1.12.1
     */
    protected ModelAndView packModelAndView(String viewName,Exception exception){
        ModelAndView modelAndView = new ModelAndView(viewName);

        //---------------------------------------------------------------
        if (isNotNullOrEmpty(exceptionAttribute)){
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("Exposing Exception as model attribute '[{}]'", exceptionAttribute);
            }
            modelAndView.addObject(exceptionAttribute, exception);
        }
        return modelAndView;
    }

    //---------------------------------------------------------------

    //下面借鉴了 org.springframework.web.servlet.handler.SimpleMappingExceptionResolver

    /**
     * Determine the view name for the given exception, first checking against the
     * {@link #setExcludedExceptions(Class[]) "excludedExecptions"}, then searching the
     * {@link #setExceptionMappings "exceptionMappings"}, and finally using the
     * {@link #setDefaultErrorView "defaultErrorView"} as a fallback.
     *
     * @param exception
     *            the exception that got thrown during handler execution
     * @param request
     *            current HTTP request (useful for obtaining metadata)
     * @param response
     *            the response
     * @return the resolved view name, or {@code null} if excluded or none found
     */
    protected String determineViewName(
                    Exception exception,
                    @SuppressWarnings("unused") HttpServletRequest request,
                    @SuppressWarnings("unused") HttpServletResponse response){
        String viewName = null;
        //        if (this.excludedExceptions != null) {
        //            for (Class<?> excludedEx : this.excludedExceptions) {
        //                if (excludedEx.equals(ex.getClass())) {
        //                    return null;
        //                }
        //            }
        //        }
        // Check for specific exception mappings.
        if (this.exceptionMappings != null){
            viewName = MatchingViewNameFinder.find(this.exceptionMappings, exception);
        }
        // Return default error view else, if defined.
        //        if (viewName == null && this.defaultErrorView != null) {
        //            if (logger.isDebugEnabled()) {
        //                logger.debug("Resolving to default view '" + this.defaultErrorView + "' for exception of type [" +
        //                        ex.getClass().getName() + "]");
        //            }
        //            viewName = this.defaultErrorView;
        //        }
        return viewName;
    }

    //---------------------------------------------------------------

    /**
     * 设置 异常名字和 路径的隐射.
     *
     * @param exceptionMappings
     *            the exceptionMappings to set
     */
    public void setExceptionMappings(Properties exceptionMappings){
        this.exceptionMappings = exceptionMappings;
    }

    /**
     * 设置 把对应的异常放到哪个model 名字中, 方便在页面获取.
     *
     * @param exceptionAttribute
     *            the exceptionAttribute to set
     */
    public void setExceptionAttribute(String exceptionAttribute){
        this.exceptionAttribute = exceptionAttribute;
    }

    /**
     * 设置 异常的名字和自定义视图的构造器对应的map.
     *
     * @param exceptionAndExceptionViewNameBuilderMap
     *            the exceptionAndExceptionViewNameBuilderMap to set
     */
    public void setExceptionAndExceptionViewNameBuilderMap(Map<String, ExceptionViewNameBuilder> exceptionAndExceptionViewNameBuilderMap){
        this.exceptionAndExceptionViewNameBuilderMap = exceptionAndExceptionViewNameBuilderMap;
    }
}
