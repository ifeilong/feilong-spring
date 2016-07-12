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
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.feilong.servlet.http.HttpHeaders;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.servlet.http.ResponseUtil;
import com.feilong.spring.web.method.HandlerMethodUtil;
import com.feilong.spring.web.servlet.interceptor.AbstractHandlerInterceptorAdapter;

import static com.feilong.core.CharsetType.UTF8;

/**
 * 用来拦截所有 标识有 {@link ClientCache}的 请求方法.
 * 
 * <h3>什么是客户端缓存?</h3>
 * 
 * <blockquote>
 * <p>
 * The mozilla cache holds all documents downloaded by the user. At first this may seem odd; however, this is done to make visited documents
 * available for back/forward, saving, viewing-as-source, etc. 不需要再向服务器端发送请求. 并且可以用于离线浏览
 * </p>
 * </blockquote>
 * 
 * <h3>浏览器缓存的整个机制流程:</h3>
 * 
 * <blockquote>
 * <p>
 * <img src="http://venusdrogon.github.io/feilong-platform/mysource/client-cache.png"/>
 * </p>
 * </blockquote>
 * 
 * <h3>作用及原理:</h3>
 * <blockquote>
 * 
 * <ol>
 * <li>如果没有标识{@link ClientCache},那么自动通过拦截器,不进行任何处理</li>
 * <li>如果标识的{@link ClientCache},{@link ClientCache#value()} <=0,那么标识不设置缓存,参见 {@link ResponseUtil#setNoCacheHeader(HttpServletResponse)}</li>
 * <li>否则,会调用 {@link HttpServletResponse#setHeader(String, String)},添加 {@link HttpHeaders#CACHE_CONTROL}头,value值为 {@code "max-age=" + value}
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
 * <code>@ClientCache(value = SECONDS_PER_MINUTE * 5)</code>
 * </p>
 * </blockquote>
 * 
 * <h3>关于springmvc cache:</h3>
 * 
 * <blockquote>
 * <p>
 * springmvc cache 参见 {@link org.springframework.web.servlet.support.WebContentGenerator},此外可以参考 {@link ShallowEtagHeaderFilter}
 * </p>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see ResponseUtil#setNoCacheHeader(HttpServletResponse)
 * @see ResponseUtil#setCacheHeader(HttpServletResponse, int)
 * @see org.springframework.web.servlet.support.WebContentGenerator
 * @see ShallowEtagHeaderFilter
 * @see <a href="http://www-archive.mozilla.org/projects/netlib/http/http-caching-faq.html">http-caching-faq</a>
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
        if (!(handler instanceof HandlerMethod)){
            LOGGER.warn(
                            "request info:[{}],not [HandlerMethod],handler is [{}],What ghost~~,",
                            RequestUtil.getRequestFullURL(request, UTF8),
                            handler.getClass().getName());
            return;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //如果没有标识{@link ClientCache},那么自动通过拦截器,不进行任何处理
        ClientCache clientCache = handlerMethod.getMethodAnnotation(ClientCache.class);
        if (clientCache == null){
            return;
        }

        int value = clientCache.value();

        //如果标识的{@link ClientCache},{@link ClientCache#value()} <=0,那么标识不设置缓存,参见 {@link ResponseUtil#setNoCacheHeader(HttpServletResponse)}
        if (value <= 0){
            ResponseUtil.setNoCacheHeader(response);
            LOGGER.debug(
                            "[{}.{}()],setNoCacheHeader",
                            HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
                            HandlerMethodUtil.getHandlerMethodName(handlerMethod));
        }
        //否则,会调用 {@link HttpServletResponse#setHeader(String, String)},添加 {@link HttpHeaders#CACHE_CONTROL}头,value值为 {@code "max-age=" + value}
        else{
            ResponseUtil.setCacheHeader(response, value);

            LOGGER.debug(
                            "[{}.{}()],set response setHeader:[{}],value is :[{}]",
                            HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
                            HandlerMethodUtil.getHandlerMethodName(handlerMethod),
                            HttpHeaders.CACHE_CONTROL,
                            value);
        }
    }
}