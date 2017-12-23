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

import static com.feilong.core.DatePattern.COMMON_DATE;
import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.feilong.spring.web.servlet.LocaleUtil;

/**
 * 通用的 父类 AbstractController.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.3
 */
public abstract class AbstractController{

    /** The context. */
    @Resource
    protected ApplicationContext applicationContext;

    //---------------------------------------------------------------

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

    //---------------------------------------------------------------

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
