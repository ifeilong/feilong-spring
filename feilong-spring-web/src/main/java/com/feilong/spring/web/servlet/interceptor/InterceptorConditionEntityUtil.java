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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.servlet.http.RequestUtil;

/**
 * The Class InterceptorConditionEntityUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.10
 */
public class InterceptorConditionEntityUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorConditionEntityUtil.class);

    /**
     * 是否不拦截处理.
     *
     * @param request
     *            the request
     * @param interceptorConditionEntity
     *            the interceptor condition entity
     * @return true, if is not intercept
     * @since 1.12.10
     */
    public static boolean isIntercept(HttpServletRequest request,InterceptorConditionEntity interceptorConditionEntity){
        //interceptorConditionEntity 没有, 那么表示支持
        if (null == interceptorConditionEntity){
            return true;
        }

        //---------------------------------------------------------------
        //method
        String[] interceptHttpMethods = interceptorConditionEntity.getInterceptHttpMethods();
        String method = request.getMethod();
        if (!RequestUtil.isSupportMethod(interceptHttpMethods, method)){
            if (LOGGER.isDebugEnabled()){
                String message = "request info:[{}],method [{}],not in config [{}],skip~";
                LOGGER.debug(message, getRequestFullURL(request, UTF8), method, ConvertUtil.toString(interceptHttpMethods, ","));
            }
            return false;
        }

        //---------------------------------------------------------------
        boolean isInterceptAjax = interceptorConditionEntity.getIsInterceptAjax();
        if (!isInterceptAjax){
            //ajax 请求
            if (RequestUtil.isAjaxRequest(request)){
                if (LOGGER.isDebugEnabled()){
                    String message = "request info:[{}],is ajax request,but config [isInterceptAjax] is false,skip~";
                    LOGGER.debug(message, getRequestFullURL(request, UTF8));
                }
                return false;
            }
        }

        //---------------------------------------------------------------
        return true;
    }
}
