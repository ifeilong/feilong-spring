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

import static com.feilong.servlet.http.HttpHeaders.X_REQUESTED_WITH;
import static com.feilong.servlet.http.HttpHeaders.X_REQUESTED_WITH_VALUE_AJAX;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.FORWARD_URL_PREFIX;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.feilong.spring.web.servlet.LocaleUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import static com.feilong.core.DatePattern.COMMON_DATE;

/**
 * 通用的 父类 AbstractController.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.3
 */
public abstract class AbstractController{

    /** The Constant header_with_ajax_springmvc. */
    public static final String   HEADER_WITH_AJAX_SPRINGMVC = X_REQUESTED_WITH + "=" + X_REQUESTED_WITH_VALUE_AJAX;

    /** The context. */
    @Resource
    protected ApplicationContext applicationContext;

    //***********************************************************************************

    /**
     * Inits the binder.
     * 
     * @param webDataBinder
     *            the web data binder
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(COMMON_DATE);
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

    //***********************************************************************************

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
        return REDIRECT_URL_PREFIX + targetUrl;
    }

    /**
     * 生成 spring Forward 路径.
     * 
     * @param forwardUrl
     *            the forward url
     * @return the spring forward path
     */
    protected String forward(String forwardUrl){
        return FORWARD_URL_PREFIX + forwardUrl;
    }

    // **********************************************************************************************

    /**
     * 获得消息信息.
     * 
     * @param key
     *            code
     * @param args
     *            args
     * @return the message
     */
    protected String getMessage(String key,Object...args){
        if (isNotNullOrEmpty(key)){
            return applicationContext.getMessage(key, args, LocaleUtil.getLocale());
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
        return applicationContext.getMessage(messageSourceResolvable, LocaleUtil.getLocale());
    }
}
