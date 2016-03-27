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
package com.feilong.spring.web.servlet.interceptor.clientcache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.feilong.servlet.http.HttpHeaders;
import com.feilong.servlet.http.ResponseUtil;
import com.feilong.spring.web.method.HandlerMethodUtil;
import com.feilong.spring.web.servlet.interceptor.AbstractHandlerInterceptorAdapter;

/**
 * 用来拦截所有 标识有 {@link ClientCache}的 请求方法.
 * 
 * <h3>作用及原理:</h3>
 * <blockquote>
 * 
 * <ol>
 * <li>如果没有标识{@link ClientCache}，那么自动通过拦截器，不进行任何处理</li>
 * <li>如果标识的{@link ClientCache}，{@link ClientCache#value()} <=0,那么标识不设置缓存，参见 {@link ResponseUtil#setNoCacheHeader(HttpServletResponse)}</li>
 * <li>否则，会调用 {@link HttpServletResponse#setHeader(String, String)},添加 {@link HttpHeaders#CACHE_CONTROL}头，value值为 {@code "max-age=" + value}
 * </li>
 * </ol>
 * </blockquote>
 * 
 * <h3>使用方式:</h3>
 * 
 * <blockquote>
 * Example 1:标识请求不需要 浏览器端缓存
 * <p>
 * <code>@ClientCache(0)</code>
 * </p>
 * 
 * Example 2:标识请求需要5分钟 浏览器端缓存
 * <p>
 * <code>@ClientCache(value = TimeInterval.SECONDS_PER_MINUTE * 5)</code>
 * </p>
 * </blockquote>
 *
 * @author feilong
 * @version 1.2.2 2015年7月17日 上午12:45:06
 * @see ResponseUtil#setNoCacheHeader(HttpServletResponse)
 * @see javax.servlet.http.HttpServletResponse#setHeader(String, String)
 * @since 1.2.2
 */
public class ClientCacheInterceptor extends AbstractHandlerInterceptorAdapter{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCacheInterceptor.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView)
                    throws Exception{
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            ClientCache clientCache = handlerMethod.getMethodAnnotation(ClientCache.class);

            //如果没有标识{@link ClientCache}，那么自动通过拦截器，不进行任何处理
            if (clientCache != null){
                int value = clientCache.value();

                //如果标识的{@link ClientCache}，{@link ClientCache#value()} <=0,那么标识不设置缓存，参见 {@link ResponseUtil#setNoCacheHeader(HttpServletResponse)}
                if (value <= 0){
                    ResponseUtil.setNoCacheHeader(response);

                    LOGGER.debug(
                                    "[{}.{}()],setNoCacheHeader",
                                    HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
                                    HandlerMethodUtil.getHandlerMethodName(handlerMethod));
                }
                //否则，会调用 {@link HttpServletResponse#setHeader(String, String)},添加 {@link HttpHeaders#CACHE_CONTROL}头，value值为 {@code "max-age=" + value}
                else{
                    String cacheControlValue = "max-age=" + value;
                    response.setHeader(HttpHeaders.CACHE_CONTROL, cacheControlValue);

                    LOGGER.debug(
                                    "[{}.{}()],set response setHeader:[{}],value is :[{}]",
                                    HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
                                    HandlerMethodUtil.getHandlerMethodName(handlerMethod),
                                    HttpHeaders.CACHE_CONTROL,
                                    cacheControlValue);
                }
            }
        }
    }
}