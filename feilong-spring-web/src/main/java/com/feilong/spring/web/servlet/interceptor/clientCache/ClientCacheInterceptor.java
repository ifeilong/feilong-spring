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
package com.feilong.spring.web.servlet.interceptor.clientCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.feilong.spring.web.method.HandlerMethodUtil;
import com.feilong.spring.web.servlet.interceptor.AbstractHandlerInterceptorAdapter;

/**
 * ClientCacheInterceptor.
 *
 * @author feilong
 * @version 1.2.2 2015年7月17日 上午12:45:06
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

            //TODO 目前仅支持方法体上面,将来支持类上面
            ClientCache clientCache = handlerMethod.getMethodAnnotation(ClientCache.class);

            if (clientCache != null){
                long value = clientCache.value();

                if (value <= 0){
                    response.addHeader("Pragma", "no-cache");
                    response.setHeader("Cache-Control", "no-cache");
                    response.setDateHeader("Expires", 0);
                    //TODO log
                }else{
                    String cacheControlValue = "max-age=" + value;
                    response.setHeader("Cache-Control", cacheControlValue);
                    LOGGER.debug(
                                    "[{}.{}()],set response setHeader:[Cache-Control],value is :[{}]",
                                    HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
                                    HandlerMethodUtil.getHandlerMethodName(handlerMethod),
                                    cacheControlValue);
                }
            }
        }
    }
}