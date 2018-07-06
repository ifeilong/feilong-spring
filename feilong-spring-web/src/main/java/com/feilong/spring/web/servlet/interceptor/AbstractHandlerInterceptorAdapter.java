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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.feilong.spring.BeanLogMessageBuilder;

/**
 * 抽象的 HandlerInterceptor 拦截器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.4.0
 */
public abstract class AbstractHandlerInterceptorAdapter extends HandlerInterceptorAdapter implements Ordered{

    /** The logger. */
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    //---------------------------------------------------------------

    /** Post construct. */
    @PostConstruct
    protected void postConstruct(){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(BeanLogMessageBuilder.buildFieldsSimpleMessage(this));
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.core.Ordered#getOrder()
     */
    @Override
    public int getOrder(){
        return 0;
    }
}