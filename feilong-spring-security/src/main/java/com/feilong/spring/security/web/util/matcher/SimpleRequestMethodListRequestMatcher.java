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
package com.feilong.spring.security.web.util.matcher;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ToStringConfig.DEFAULT_CONNECTOR;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.ELRequestMatcher;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEditor;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;
import com.feilong.servlet.http.RequestUtil;

/**
 * 简单的 基于 {@link AntPathRequestMatcher} 的 methods 的 {@link RequestMatcher} 实现.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.security.web.csrf.CsrfFilter#setRequireCsrfProtectionMatcher(RequestMatcher)
 * 
 * @see AndRequestMatcher
 * @see AnyRequestMatcher
 * @see OrRequestMatcher
 * 
 * @see AntPathRequestMatcher
 * @see RegexRequestMatcher
 * @see ELRequestMatcher
 * 
 * @see IpAddressMatcher
 * @see MediaTypeRequestMatcher
 * @see NegatedRequestMatcher
 * @see RequestHeaderRequestMatcher
 * 
 * @see RequestMatcherEditor
 * @since 4.0.2
 */
public class SimpleRequestMethodListRequestMatcher implements RequestMatcher{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRequestMethodListRequestMatcher.class);

    //---------------------------------------------------------------
    /** 需要匹配的请求method. */
    private String[]            methods;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.web.util.matcher.RequestMatcher#matches(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean matches(HttpServletRequest request){
        //如果没有设置 methodList，返回false 不匹配
        if (isNullOrEmpty(methods)){
            return false;
        }

        //---------------------------------------------------------------
        //判断 method
        String method = request.getMethod();

        String requestURI = request.getRequestURI();

        String methodsValue = ConvertUtil.toString(methods, DEFAULT_CONNECTOR);
        if (RequestUtil.isSupportMethod(methods, method)){
            LOGGER.debug("requestURI:[{}],method is: [{}],in methods:[{}]", requestURI, method, methodsValue);
            return true;
        }

        //---------------------------------------------------------------
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("requestURI:[{}],method is: [{}],not in methods:[{}]", requestURI, method, methodsValue);
        }

        return false;
    }

    //---------------------------------------------------------------

    /**
     * 获得 需要匹配的请求method.
     *
     * @return the methods
     */
    public String[] getMethods(){
        return methods;
    }

    /**
     * 设置 需要匹配的请求method.
     *
     * @param methods
     *            the methods to set
     */
    public void setMethods(String[] methods){
        this.methods = methods;
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
