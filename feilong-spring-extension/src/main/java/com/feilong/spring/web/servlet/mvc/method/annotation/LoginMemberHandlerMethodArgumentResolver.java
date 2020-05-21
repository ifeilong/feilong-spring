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
package com.feilong.spring.web.servlet.mvc.method.annotation;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.ObjectUtil.defaultIfNull;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.feilong.context.BeanLogMessageBuilder;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.servlet.http.SessionUtil;
import com.feilong.spring.web.bind.annotation.LoginMember;

/**
 * 支持spring mvc requestMapping 方法支持 {@link LoginMember} 注解特殊参数.
 * 
 * <h3>代码流程:</h3>
 * <blockquote>
 * <ol>
 * <li>如果session中的 <code>sessionKey</code>是 <code>null</code>,那么直接返回<code>null</code>;</li>
 * <li>如果标识的是session对象本身 {@link #sessionMemberClass},那么直接获取返回;</li>
 * <li>如果标识的是session对象中的某个字段 {@link #sessionMemberIdName},那么提取该字段返回;</li>
 * <li>如果没有配置 {@link #sessionMemberIdName},那么返回null.</li>
 * </ol>
 * </blockquote>
 * 
 * <h3>配置示例:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * {@code
 *             <bean class="com.feilong.spring.web.servlet.mvc.method.annotation.LoginMemberHandlerMethodArgumentResolver">
 *                 <span style="color:green"><!-- 在session中保存的标识用户session对象的key --></span>
 *                 <property name="sessionKey">
 *                     <util:constant static-field="com.feilong.web.constants.SessionKeyConstants.MEMBER_CONTEXT" />
 *                 </property>
 * 
 *                 <span style="color:green"><!-- session 里面存放的 对象类型 --></span>
 *                 <property name="sessionMemberClass" value="com.feilong.web.MemberDetails" />
 * 
 *                 <span style="color:green"><!-- 对象的主键名称 --></span>
 *                 <property name="sessionMemberIdName" value="memberId" />
 *             </bean>
 * }
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#getDefaultArgumentResolvers()
 * @since 1.5.0
 * @since spring 3.1
 */
public class LoginMemberHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginMemberHandlerMethodArgumentResolver.class);

    //---------------------------------------------------------------

    /** 在session中保存的标识用户session对象的key. */
    private String              sessionKey;

    /** session 里面存放的 用户对象类型. */
    private Class<?>            sessionMemberClass;

    //---------------------------------------------------------------
    /** 对象的主键名称. */
    private String              sessionMemberIdName;

    /**
     * 对象的主键默认值(比如可以设定游客的值,某些商城需要设置为 1).
     * 
     * @since 1.12.5
     */
    private Object              sessionMemberIdDefaultValue;

    //---------------------------------------------------------------

    /** Post construct. */
    @PostConstruct
    protected void postConstruct(){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(BeanLogMessageBuilder.buildFieldsMessage(this));
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter){
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter,
     * org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest,
     * org.springframework.web.bind.support.WebDataBinderFactory)
     */
    @Override
    public Object resolveArgument(
                    MethodParameter parameter,
                    ModelAndViewContainer mavContainer,
                    NativeWebRequest webRequest,
                    WebDataBinderFactory binderFactory) throws Exception{
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Object sessionMember = SessionUtil.getAttribute(request, sessionKey);

        //1.如果session中的  sessionKey 是 null ,那么直接返回 null 
        if (null == sessionMember){
            return null;
        }

        //---------------------------------------------------------------

        //2.如果标识的是session对象本身是配置的类型,那么直接获取返回;
        Class<?> klass = parameter.getParameterType();
        if (klass.isAssignableFrom(sessionMemberClass)){
            return sessionMember;
        }

        //---------------------------------------------------------------

        //3.如果没有配置#sessionMemberIdName,那么返回null
        if (isNullOrEmpty(sessionMemberIdName)){
            return null;
        }

        //---------------------------------------------------------------

        //4.如果标识的是session对象中的某个字段#sessionMemberIdName,那么提取该字段返回;
        Object id = PropertyUtil.getProperty(sessionMember, sessionMemberIdName);

        return defaultIfNull(id, sessionMemberIdDefaultValue);
    }

    //---------------------------------------------------------------

    /**
     * 设置 在session中保存的标识用户session对象的key.
     *
     * @param sessionKey
     *            the sessionKey to set
     */
    public void setSessionKey(String sessionKey){
        this.sessionKey = sessionKey;
    }

    /**
     * 设置 session 里面存放的 用户对象类型.
     *
     * @param sessionMemberClass
     *            the sessionMemberClass to set
     */
    public void setSessionMemberClass(Class<?> sessionMemberClass){
        this.sessionMemberClass = sessionMemberClass;
    }

    /**
     * 设置 对象的主键名称.
     *
     * @param sessionMemberIdName
     *            the sessionMemberIdName to set
     */
    public void setSessionMemberIdName(String sessionMemberIdName){
        this.sessionMemberIdName = sessionMemberIdName;
    }

    /**
     * 设置 对象的主键默认值(比如可以设定游客的值,某些商城需要设置为 1).
     *
     * @param sessionMemberIdDefaultValue
     *            the sessionMemberIdDefaultValue to set
     * @since 1.12.5
     */
    public void setSessionMemberIdDefaultValue(Object sessionMemberIdDefaultValue){
        this.sessionMemberIdDefaultValue = sessionMemberIdDefaultValue;
    }

}
