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

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * 借鉴了 {@link SimpleMappingExceptionResolver}, 但是适用于 HandlerMethodExceptionResolver.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see SimpleMappingExceptionResolver
 * @since 1.11.4
 */
public class SimpleMappingHandlerMethodExceptionResolver extends AbstractHandlerMethodExceptionResolver{

    private Properties exceptionMappings;

    private String     exceptionAttribute = SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE;

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
        // Expose ModelAndView for chosen error view.
        String viewName = determineViewName(exception, request, response);
        if (viewName != null){
            // Apply HTTP status code for error views, if specified.
            // Only apply it if we're processing a top-level request.
            //            Integer statusCode = determineStatusCode(request, viewName);
            //            if (statusCode != null) {
            //                applyStatusCodeIfPossible(request, response, statusCode);
            //            }

            //Return a ModelAndView for the given request, view name and exception.
            //The default implementation delegates to {@link #getModelAndView(String, Exception)}.
            ModelAndView modelAndView = new ModelAndView(viewName);
            if (this.exceptionAttribute != null){
                if (logger.isDebugEnabled()){
                    logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
                }
                modelAndView.addObject(this.exceptionAttribute, exception);
            }
            return modelAndView;
        }
        return null;
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
     * @return the resolved view name, or {@code null} if excluded or none found
     */
    protected String determineViewName(Exception exception,HttpServletRequest request,HttpServletResponse response){
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
     * @param exceptionMappings
     *            the exceptionMappings to set
     */
    public void setExceptionMappings(Properties exceptionMappings){
        this.exceptionMappings = exceptionMappings;
    }

    /**
     * @param exceptionAttribute
     *            the exceptionAttribute to set
     */
    public void setExceptionAttribute(String exceptionAttribute){
        this.exceptionAttribute = exceptionAttribute;
    }
}
