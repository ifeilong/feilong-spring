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
package com.feilong.spring.web.filter;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.date.DateExtensionUtil.formatDuration;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.OncePerRequestFilter;

import com.feilong.servlet.http.RequestUtil;
import com.feilong.spring.BeanLogMessageBuilder;

/**
 * 可以配置条件的 OncePerRequestFilter.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.8
 */
public abstract class ConfigurableConditionOncePerRequestFilter extends OncePerRequestFilter{

    /** The Constant log. */
    private static final Logger LOGGER                    = LoggerFactory.getLogger(ConfigurableConditionOncePerRequestFilter.class);

    //---------------------------------------------------------------

    /** 指定哪些指定的扩展名不需要处理. */
    private List<String>        notFilterRequestURISuffixList;

    //---------------------------------------------------------------

    /** 是否不过滤静态资源. */
    private boolean             isNotFilterStaticResource = true;

    /** 是否不过滤ajax. */
    private boolean             isNotFilterAjax           = false;

    //---------------------------------------------------------------
    /** 支持过滤的 http 请求 method. */
    private String[]            filterHttpMethods         = toArray("get", "post");

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain)
                    throws ServletException,IOException{
        Date beginDate = new Date();

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("begin [{}] [{}] doFilterInternal", this.getClass().getSimpleName(), RequestUtil.getRequestFullURL(request, UTF8));
        }

        //---------------------------------------------------------------
        doWithFilterInternal(request, response, filterChain);

        //---------------------------------------------------------------

        if (LOGGER.isInfoEnabled()){
            String pattern = "end [{}] [{}] doFilterInternal,use time: [{}]";
            LOGGER.info(pattern, this.getClass().getSimpleName(), RequestUtil.getRequestFullURL(request, UTF8), formatDuration(beginDate));
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.OncePerRequestFilter#shouldNotFilter(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected final boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
        String requestURI = request.getRequestURI();
        boolean shouldNotFilterSuffix = isShouldNotFilterSuffix(requestURI);
        if (shouldNotFilterSuffix){
            LOGGER.debug("[{}],requestURI:[{}],suffix should not filter", this.getClass().getSimpleName(), requestURI);
            return true;
        }

        //---------------------------------------------------------------
        String method = request.getMethod();
        boolean shouldNotFilterMethod = isShouldNotFilterMethod(method);
        if (shouldNotFilterMethod){
            LOGGER.debug("[{}],requestURI:[{}],method:[{}] ,should not filter", this.getClass().getSimpleName(), requestURI, method);
            return true;
        }

        //---------------------------------------------------------------

        if (isNotFilterAjax){
            if (RequestUtil.isAjaxRequest(request)){
                LOGGER.debug("[{}],requestURI:[{}],is ajax request,should not filter", this.getClass().getSimpleName(), requestURI);
                return true;
            }
        }

        //---------------------------------------------------------------
        return false;
    }

    //---------------------------------------------------------------

    /**
     * 是否是不过滤的方法.
     *
     * @param method
     *            the method
     * @return true, if is should not filter method
     */
    private boolean isShouldNotFilterMethod(String method){
        if (isNotNullOrEmpty(filterHttpMethods)){
            for (String filterHttpMethod : filterHttpMethods){
                if (StringUtils.equalsIgnoreCase(filterHttpMethod, method)){
                    return true;
                }
            }
            //TODO 忽视大小写
            //return ArrayUtils.contains(filterHttpMethods, method);
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>
     * Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param filterChain
     *            the filter chain
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected abstract void doWithFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain)
                    throws ServletException,IOException;

    //---------------------------------------------------------------

    /**
     * 判断一个 uri 不需要过滤.
     *
     * @param requestURI
     *            the request URI
     * @return true, if is should not filter suffix
     */
    protected boolean isShouldNotFilterSuffix(String requestURI){
        if (isNotFilterStaticResource){
            if (ConfigurableConditionHelper.isStaticResource(requestURI)){
                return true;
            }
        }

        //---------------------------------------------------------------
        if (isNotNullOrEmpty(notFilterRequestURISuffixList)){
            for (String uriSuffix : notFilterRequestURISuffixList){
                //TODO 性能
                if (StringUtils.endsWithIgnoreCase(requestURI, uriSuffix)){
                    return true;
                }
            }
        }
        return false;

    }

    //---------------------------------------------------------------

    //Dispatch

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.OncePerRequestFilter#isAsyncDispatch(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected boolean isAsyncDispatch(HttpServletRequest request){
        return super.isAsyncDispatch(request);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.OncePerRequestFilter#isAsyncStarted(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected boolean isAsyncStarted(HttpServletRequest request){
        return super.isAsyncStarted(request);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.OncePerRequestFilter#getAlreadyFilteredAttributeName()
     */
    @Override
    protected String getAlreadyFilteredAttributeName(){
        return super.getAlreadyFilteredAttributeName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.OncePerRequestFilter#shouldNotFilterAsyncDispatch()
     */
    @Override
    protected boolean shouldNotFilterAsyncDispatch(){
        return super.shouldNotFilterAsyncDispatch();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.OncePerRequestFilter#shouldNotFilterErrorDispatch()
     */
    @Override
    protected boolean shouldNotFilterErrorDispatch(){
        return super.shouldNotFilterErrorDispatch();
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.GenericFilterBean#setEnvironment(org.springframework.core.env.Environment)
     */
    @Override
    public void setEnvironment(Environment environment){
        super.setEnvironment(environment);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.GenericFilterBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws ServletException{
        super.afterPropertiesSet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.GenericFilterBean#initBeanWrapper(org.springframework.beans.BeanWrapper)
     */
    @Override
    protected void initBeanWrapper(BeanWrapper bw) throws BeansException{
        super.initBeanWrapper(bw);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.GenericFilterBean#initFilterBean()
     */
    @Override
    protected void initFilterBean() throws ServletException{
        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("[{}],propertty infos:{}", this.getClass().getName(), BeanLogMessageBuilder.buildFieldsSimpleMessage(this));
        }

        //---------------------------------------------------------------

        super.initFilterBean();
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.filter.GenericFilterBean#destroy()
     */
    @Override
    public void destroy(){
        super.destroy();
    }

    //---------------------------------------------------------------

    /**
     * 设置 指定哪些指定的扩展名不需要处理.
     *
     * @param notFilterRequestURISuffixList
     *            the notFilterRequestURISuffixList to set
     */
    public void setNotFilterRequestURISuffixList(List<String> notFilterRequestURISuffixList){
        this.notFilterRequestURISuffixList = notFilterRequestURISuffixList;
    }

    /**
     * 设置 是否不过滤静态资源.
     *
     * @param isNotFilterStaticResource
     *            the isNotFilterStaticResource to set
     */
    public void setIsNotFilterStaticResource(boolean isNotFilterStaticResource){
        this.isNotFilterStaticResource = isNotFilterStaticResource;
    }

    /**
     * 设置 支持过滤的 http 请求 method.
     *
     * @param filterHttpMethods
     *            the filterHttpMethods to set
     */
    public void setFilterHttpMethods(String[] filterHttpMethods){
        this.filterHttpMethods = filterHttpMethods;
    }

    /**
     * 设置 是否不过滤ajax.
     *
     * @param isNotFilterAjax
     *            the isNotFilterAjax to set
     */
    public void setIsNotFilterAjax(boolean isNotFilterAjax){
        this.isNotFilterAjax = isNotFilterAjax;
    }

}
