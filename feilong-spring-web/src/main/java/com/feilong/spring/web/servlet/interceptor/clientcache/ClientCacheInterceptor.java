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

import static com.feilong.servlet.http.HttpHeaders.CACHE_CONTROL;
import static com.feilong.spring.web.method.HandlerMethodUtil.getDeclaringClassSimpleName;
import static com.feilong.spring.web.method.HandlerMethodUtil.getHandlerMethodName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.feilong.servlet.http.HttpHeaders;
import com.feilong.servlet.http.ResponseUtil;
import com.feilong.spring.web.servlet.interceptor.AbstractHandlerMethodInterceptorAdapter;

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
public class ClientCacheInterceptor extends AbstractHandlerMethodInterceptorAdapter{

    /** The Constant LOGGER. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(ClientCacheInterceptor.class);

    //---------------------------------------------------------------

    /**
     * 是否默认不缓存.
     * 
     * @since 1.10.4
     */
    private boolean             isDefaultNoCache = false;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.AbstractHandlerMethodInterceptorAdapter#doPostHandle(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    //---------------------------------------------------------------
    @Override
    public void doPostHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView){
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //如果没有标识{@link ClientCache}
        ClientCache clientCache = handlerMethod.getMethodAnnotation(ClientCache.class);

        //---------------------------------------------------------------
        if (clientCache == null){
            //默认 nocache
            if (isDefaultNoCache){
                setNoCacheAndLog(response, handlerMethod, "isDefaultNoCache:[" + isDefaultNoCache + "]");
                return;
            }
            //那么自动通过拦截器,不进行任何处理
            return;
        }

        //---------------------------------------------------------------
        int value = clientCache.value();

        //如果标识的{@link ClientCache}且{@link ClientCache#value()} <=0,那么标识不设置缓存
        if (value <= 0){
            setNoCacheAndLog(response, handlerMethod, "value:[" + value + "] <=0");
        }
        //否则,会调用 {@link HttpServletResponse#setHeader(String, String)},添加 {@link HttpHeaders#CACHE_CONTROL}头,value值为 {@code "max-age=" + value}
        else{
            setCacheAndLog(response, handlerMethod, value);
        }
    }

    /**
     * 设置 cache and log.
     *
     * @param response
     *            the response
     * @param handlerMethod
     *            the handler method
     * @param value
     *            the value
     * @since 1.10.4
     */
    private static void setCacheAndLog(HttpServletResponse response,HandlerMethod handlerMethod,int value){
        ResponseUtil.setCacheHeader(response, value);

        if (LOGGER.isDebugEnabled()){
            String pattern = "[{}.{}()],set response setHeader:[{}],value is :[{}]";
            LOGGER.debug(pattern, getDeclaringClassSimpleName(handlerMethod), getHandlerMethodName(handlerMethod), CACHE_CONTROL, value);
        }
    }

    /**
     * 设置 no cache and log.
     *
     * @param response
     *            the response
     * @param handlerMethod
     *            the handler method
     * @param reason
     *            the reason
     * @since 1.10.4
     */
    private static void setNoCacheAndLog(HttpServletResponse response,HandlerMethod handlerMethod,String reason){
        ResponseUtil.setNoCacheHeader(response);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "[{}.{}()],setNoCacheHeader,cause by: [{}]",
                            getDeclaringClassSimpleName(handlerMethod),
                            getHandlerMethodName(handlerMethod),
                            reason);
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置 是否默认不缓存.
     *
     * @param isDefaultNoCache
     *            the isDefaultNoCache to set
     * @since 1.10.4
     */
    public void setIsDefaultNoCache(boolean isDefaultNoCache){
        this.isDefaultNoCache = isDefaultNoCache;
    }
}