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
package com.feilong.spring.web.servlet.interceptor;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.servlet.http.RequestUtil.getRequestFullURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 所有 HandlerMethodInterceptor 的父类.
 * 
 * <p>
 * 如果不是 HandlerMethod,将会以 warn级别日志输出
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public abstract class AbstractHandlerMethodInterceptorAdapter extends AbstractHandlerInterceptorAdapter{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandlerMethodInterceptorAdapter.class);

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
        if (!(handler instanceof HandlerMethod)){
            logNoHandlerMethod(request, handler);
            return true;//容错
        }

        return doPreHandle(request, response, (HandlerMethod) handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView)
                    throws Exception{
        if (!(handler instanceof HandlerMethod)){
            logNoHandlerMethod(request, handler);
            return;
        }

        doPostHandle(request, response, (HandlerMethod) handler, modelAndView);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) throws Exception{
        if (!(handler instanceof HandlerMethod)){
            logNoHandlerMethod(request, handler);
            return;
        }
        doAfterCompletion(request, response, (HandlerMethod) handler, ex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterConcurrentHandlingStarted(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
        if (!(handler instanceof HandlerMethod)){
            logNoHandlerMethod(request, handler);
            return;
        }
        doAfterConcurrentHandlingStarted(request, response, (HandlerMethod) handler);
    }

    //---------------------------------------------------------------

    /**
     * Log no handler method.
     *
     * @param request
     *            the request
     * @param handler
     *            the handler
     */
    private static void logNoHandlerMethod(HttpServletRequest request,Object handler){
        if (LOGGER.isWarnEnabled()){
            LOGGER.warn(
                            "request info:[{}],not [HandlerMethod],handler is [{}],What ghost~~,",
                            getRequestFullURL(request, UTF8),
                            handler.getClass().getName());
        }
    }
    //---------------------------------------------------------------

    /**
     * Do pre handle.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handlerMethod
     *            the handler method
     * @return true, if successful
     */
    @SuppressWarnings({ "static-method", "unused" })
    public boolean doPreHandle(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod){
        return true;
    }

    /**
     * Do post handle.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handlerMethod
     *            the handler method
     * @param modelAndView
     *            the model and view
     */
    @SuppressWarnings("unused")
    public void doPostHandle(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod,ModelAndView modelAndView){

    }

    /**
     * Do after completion.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handlerMethod
     *            the handler method
     * @param ex
     *            the ex
     */
    @SuppressWarnings("unused")
    public void doAfterCompletion(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod,Exception ex){

    }

    /**
     * Do after concurrent handling started.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handlerMethod
     *            the handler method
     */
    @SuppressWarnings("unused")
    public void doAfterConcurrentHandlingStarted(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod){

    }

}