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

import static com.feilong.core.Validator.isNullOrEmpty;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.feilong.spring.web.servlet.interceptor.AbstractHandlerMethodInterceptorAdapter;

/**
 * 支持语言的 change 拦截器.
 * 
 * <p>
 * 如果直接使用 {@link org.springframework.web.servlet.i18n.LocaleChangeInterceptor},而参数中传入了<code>不存在的/不支持的 locale</code> 调用
 * {@link org.springframework.util.StringUtils#parseLocaleString(String)}会报错
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see LocaleChangeInterceptor
 * @since 1.0.9
 */
public class SupportLocaleChangeInterceptor extends AbstractHandlerMethodInterceptorAdapter{

    /** The Constant log. */
    private static final Logger LOGGER             = LoggerFactory.getLogger(SupportLocaleChangeInterceptor.class);

    //---------------------------------------------------------------

    /**
     * Default name of the locale specification parameter: "locale".
     * 
     * @since 1.12.7
     */
    public static final String  DEFAULT_PARAM_NAME = "locale";

    //---------------------------------------------------------------

    /** 支持的语言. */
    private List<String>        supportLocales;

    /**
     * The param name.
     * 
     * @since 1.12.7
     */
    private String              paramName          = DEFAULT_PARAM_NAME;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.i18n.LocaleChangeInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean doPreHandle(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod){
        boolean canHandle = isSupport(request);
        if (canHandle){
            handler(request, response);
        }
        //不管支不支持  都return true
        return true;
    }

    //---------------------------------------------------------------

    /**
     * copy from {@link LocaleChangeInterceptor}.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @return true, if successful
     * @since 1.12.7
     */
    private boolean handler(HttpServletRequest request,HttpServletResponse response){
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver == null){
            throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
        }
        String newLocale = request.getParameter(this.paramName);
        localeResolver.setLocale(request, response, StringUtils.parseLocaleString(newLocale));
        // Proceed in any case.
        return true;
    }

    //---------------------------------------------------------------

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
            LOGGER.warn("SupportLocaleChangeInterceptor's supportLocales isNullOrEmpty,you maybe can direct use LocaleChangeInterceptor");
            return true; //如果isNotNullOrEmpty supportLocales,那么就是个普通的  LocaleChangeInterceptor
        }

        //---------------------------------------------------------------
        String newLocaleValue = request.getParameter(paramName);
        //since 1.12.6
        if (isNullOrEmpty(newLocaleValue)){
            return true;
        }

        //---------------------------------------------------------------
        boolean contains = supportLocales.contains(newLocaleValue);
        if (!contains){
            LOGGER.warn("SupportLocaleChangeInterceptor's supportLocales:[{}] not contains [{}]", supportLocales, newLocaleValue);
        }
        return contains; //是否属于支持的locale
    }

    //---------------------------------------------------------------

    /**
     * 设置 support locales.
     *
     * @param supportLocales
     *            the supportLocales to set
     */
    public void setSupportLocales(List<String> supportLocales){
        this.supportLocales = supportLocales;
    }

    //---------------------------------------------------------------

    /**
     * Set the name of the parameter that contains a locale specification in a locale change request.
     * 
     * <p>
     * Default is "locale".
     * </p>
     *
     * @param paramName
     *            the new param name
     * @since 1.12.7
     */
    public void setParamName(String paramName){
        this.paramName = paramName;
    }

}