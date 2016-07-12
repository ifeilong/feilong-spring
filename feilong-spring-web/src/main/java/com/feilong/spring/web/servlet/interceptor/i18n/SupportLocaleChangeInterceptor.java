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
package com.feilong.spring.web.servlet.interceptor.i18n;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.feilong.servlet.http.RequestUtil;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNullOrEmpty;

/**
 * 如果直接使用 {@link org.springframework.web.servlet.i18n.LocaleChangeInterceptor} ,而参数中传入了 不存在的/不支持的 locale 调用
 * {@link org.springframework.util.StringUtils#parseLocaleString(String)}会报错
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.9
 */
public class SupportLocaleChangeInterceptor extends LocaleChangeInterceptor{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SupportLocaleChangeInterceptor.class);

    /** The support locales. */
    private List<String>        supportLocales;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.i18n.LocaleChangeInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws ServletException{
        if (!(handler instanceof HandlerMethod)){
            LOGGER.warn(
                            "request info:[{}],not [HandlerMethod],handler is [{}],What ghost~~,",
                            RequestUtil.getRequestFullURL(request, UTF8),
                            handler.getClass().getName());
            return true;
        }
        boolean canHandle = isSupport(request);
        if (canHandle){
            super.preHandle(request, response, handler);
        }
        //不管支不支持  都return true
        return true;
    }

    /**
     * Checks if is support.
     *
     * @param request
     *            the request
     * @return true, if checks if is support
     * @since 1.0.9
     */
    private boolean isSupport(HttpServletRequest request){
        if (isNullOrEmpty(supportLocales)){
            LOGGER.warn("SupportLocaleChangeInterceptor's supportLocales is isNullOrEmpty,you can direct use LocaleChangeInterceptor");
            return true; //如果isNotNullOrEmpty supportLocales,那么 就是个普通的  LocaleChangeInterceptor
        }

        String newLocaleValue = request.getParameter(getParamName());
        boolean contains = supportLocales.contains(newLocaleValue);
        if (!contains){
            LOGGER.warn("SupportLocaleChangeInterceptor's supportLocales:[{}] not contains [{}]", supportLocales, newLocaleValue);
        }
        return contains; //是否属于支持的locale
    }

    /**
     * 设置 support locales.
     *
     * @param supportLocales
     *            the supportLocales to set
     */
    public void setSupportLocales(List<String> supportLocales){
        this.supportLocales = supportLocales;
    }
}