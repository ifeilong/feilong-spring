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
package com.feilong.spring.web.servlet.mvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.feilong.core.DatePattern;
import com.feilong.core.Validator;
import com.feilong.servlet.http.HttpHeaders;
import com.feilong.spring.web.servlet.LocaleResolverUtil;

/**
 * 通用的 父类 AbstractController.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.3
 */
public abstract class AbstractController{

    /** The Constant header_with_ajax_springmvc. */
    public static final String   HEADER_WITH_AJAX_SPRINGMVC = HttpHeaders.X_REQUESTED_WITH + "=" + HttpHeaders.X_REQUESTED_WITH_VALUE_AJAX;

    /** The context. */
    @Resource
    protected ApplicationContext context;

    // @Autowired
    // protected ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

    /**
     * Inits the binder.
     * 
     * @param webDataBinder
     *            the web data binder
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.COMMON_DATE);
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));

        // can be override to add new binding
        initBinderInternal(webDataBinder);
    }

    /**
     * can be override to add new binding.
     * 
     * @param webDataBinder
     *            webDataBinder
     */
    protected void initBinderInternal(@SuppressWarnings("unused") WebDataBinder webDataBinder){
    }

    /**
     * 生成spring 的跳转路径<br>
     * e.g. getSpringRedirectPath("/shoppingcart") <br>
     * 注:不需要你手工的 拼接request.getContextPath()
     * 
     * @param targetUrl
     *            如果是相对根目录路径 只需要传递 如"/shoppingcart" spring会自动添加request.getContextPath() <br>
     *            也可以传入绝对路径 如:http://www.baidu.com
     * @return the spring redirect path
     */
    protected String redirect(String targetUrl){
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + targetUrl;
    }

    /**
     * 生成 spring Forward 路径.
     * 
     * @param forwardUrl
     *            the forward url
     * @return the spring forward path
     */
    protected String forward(String forwardUrl){
        return UrlBasedViewResolver.FORWARD_URL_PREFIX + forwardUrl;
    }

    // **********************************************************************************************

    /**
     * 获得消息信息.
     * 
     * @param errorCode
     *            the error code
     * @return the message
     */
    protected String getMessage(Integer errorCode){
        Object[] args = null;
        return getMessage(errorCode, args);
    }

    /**
     * 获得消息信息.
     * 
     * @param errorCode
     *            the error code
     * @param args
     *            args
     * @return the message
     */
    protected String getMessage(Integer errorCode,Object...args){
        if (Validator.isNotNullOrEmpty(errorCode)){
            String prefix = "ExceptionResolver.BUSINESS_EXCEPTION_PREFIX";
            return getMessage(prefix + errorCode, args);
        }
        return null;
    }

    /**
     * 获得消息信息.
     * 
     * @param code
     *            code
     * @param args
     *            args
     * @return the message
     */
    protected String getMessage(String code,Object...args){
        if (Validator.isNotNullOrEmpty(code)){
            return context.getMessage(code, args, LocaleResolverUtil.getLocale());
        }
        return null;
    }

    /**
     * 获得消息信息.
     * 
     * @param messageSourceResolvable
     *            适用于 ObjectError 以及 FieldError
     * @return the message
     */
    protected String getMessage(MessageSourceResolvable messageSourceResolvable){
        return context.getMessage(messageSourceResolvable, LocaleResolverUtil.getLocale());
    }

}
