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

import static com.feilong.core.bean.ConvertUtil.toArray;

import java.io.Serializable;

import org.springframework.util.AntPathMatcher;

/**
 * 拦截器的条件.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see AntPathMatcher
 * @since 1.12.9
 */
public class InterceptorConditionEntity implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID     = 9049089122463144605L;

    /** 是否过滤ajax. */
    private boolean           isInterceptAjax      = true;

    //---------------------------------------------------------------
    /** 支持过滤的 http 请求 method. */
    private String[]          interceptHttpMethods = toArray("get", "post");

    //---------------------------------------------------------------

    /**
     * 获得 支持过滤的 http 请求 method.
     *
     * @return the interceptHttpMethods
     */
    public String[] getInterceptHttpMethods(){
        return interceptHttpMethods;
    }

    /**
     * 设置 支持过滤的 http 请求 method.
     *
     * @param interceptHttpMethods
     *            the interceptHttpMethods to set
     */
    public void setInterceptHttpMethods(String[] interceptHttpMethods){
        this.interceptHttpMethods = interceptHttpMethods;
    }

    /**
     * 设置 是否过滤ajax.
     *
     * @param isInterceptAjax
     *            the isInterceptAjax to set
     */
    public void setIsInterceptAjax(boolean isInterceptAjax){
        this.isInterceptAjax = isInterceptAjax;
    }

    /**
     * 获得 是否过滤ajax.
     *
     * @return the isInterceptAjax
     */
    public boolean getIsInterceptAjax(){
        return isInterceptAjax;
    }

}
