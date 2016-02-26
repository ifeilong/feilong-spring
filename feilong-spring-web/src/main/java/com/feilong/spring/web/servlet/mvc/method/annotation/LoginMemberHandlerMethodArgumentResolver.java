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

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.lang.reflect.FieldUtil;
import com.feilong.core.tools.jsonlib.JsonUtil;
import com.feilong.spring.web.bind.annotation.LoginMember;

/**
 * 支持spring mvc requestMapping 方法支持 {@link LoginMember} 注解 特殊参数.
 * 
 * <ul>
 * <li>如果标识的是session对象本身 {@link #sessionMemberClass},那么直接获取返回;</li>
 * <li>如果标识的是session对象中的某个字段 {@link #sessionMemberIdName},那么获取该字段返回.</li>
 * </ul>
 * 
 * <h3>配置示例:</h3> <blockquote>
 * 
 * <pre>
 * {@code
 *             <bean class="com.feilong.spring.web.servlet.mvc.method.annotation.LoginMemberHandlerMethodArgumentResolver">
 *                 <!-- 在session中保存的标识用户session对象的key -->
 *                 <property name="sessionKey">
 *                     <util:constant static-field="com.baozun.nebula.web.constants.SessionKeyConstants.MEMBER_CONTEXT" />
 *                 </property>
 * 
 *                 <!-- session 里面存放的 对象类型 -->
 *                 <property name="sessionMemberClass" value="com.baozun.nebula.web.MemberDetails" />
 * 
 *                 <!-- 对象的主键名称 -->
 *                 <property name="sessionMemberIdName" value="memberId" />
 *             </bean>
 * }
 * </pre>
 * 
 * </blockquote>
 *
 * @author feilong
 * @version 5.0.0 2016年2月26日 下午6:49:44
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#getDefaultArgumentResolvers()
 * @since 5.0.0
 */
public class LoginMemberHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginMemberHandlerMethodArgumentResolver.class);

    /** 在session中保存的标识用户session对象的key. */
    private String              sessionKey;

    /** session 里面存放的 用户对象类型. */
    private Class<?>            sessionMemberClass;

    /** 对象的主键名称. */
    private String              sessionMemberIdName;

    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct(){
        if (LOGGER.isInfoEnabled()){
            Map<String, Object> map = FieldUtil.getAllFieldNameAndValueMap(this);
            LOGGER.info("\n[{}] fieldValueMap: \n[{}]", getClass().getCanonicalName(), JsonUtil.format(map));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter){
        if (parameter.hasParameterAnnotation(LoginMember.class)){
            return true;
        }
        return false;
    }

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

        Object sessionMember = WebUtils.getRequiredSessionAttribute(request, sessionKey);

        if (null == sessionMember){
            return null;
        }

        Class<?> klass = parameter.getParameterType();
        if (klass.isAssignableFrom(sessionMemberClass)){
            return sessionMember;
        }

        return PropertyUtil.getProperty(sessionMember, sessionMemberIdName);
    }

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

}
