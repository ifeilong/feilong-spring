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
package com.feilong.spring.web.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * {@link WebApplicationContextUtils} 工具类.
 * 
 * <p>
 * 当 Web应用集成 Spring容器后,代表 Spring 容器的 {@link WebApplicationContext} 对象将以{@link WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE}
 * 为键存放在 {@link ServletContext} 属性列表中,具体参见 {@link org.springframework.web.context.ContextLoader#initWebApplicationContext(ServletContext)}
 * </p>
 * 
 * <h3>{@link WebApplicationContextUtils#getWebApplicationContext(ServletContext) getWebApplicationContext}VS
 * {@link WebApplicationContextUtils#getRequiredWebApplicationContext(ServletContext) getRequiredWebApplicationContext}:</h3>
 * <blockquote>
 * <p>
 * 当 ServletContext 属性列表中不存在 WebApplicationContext时:
 * <ol>
 * <li>{@link WebApplicationContextUtils#getWebApplicationContext(ServletContext)}方法不会抛出异常,它简单地返回 null, 如果后续代码直接访问返回的结果将引发一个
 * NullPointerException 异常.</li>
 * <li>而{@link WebApplicationContextUtils#getRequiredWebApplicationContext(ServletContext)}方法要求 ServletContext属性列表中一定要包含一个有效的
 * WebApplicationContext对象,否则马上抛出一个 异常 {@link java.lang.IllegalStateException}.</li>
 * </ol>
 * 我们推荐使用后者,因为它能提前发现错误的时间,强制开发者搭建好必备的基础设施。
 * </p>
 * </blockquote>
 * 
 * @author feilong
 * @version 1.0 2011-3-31 下午06:08:20
 * @see org.springframework.web.context.WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
 * @see org.springframework.web.context.support.WebApplicationContextUtils#getWebApplicationContext(ServletContext)
 */
public final class WebSpringUtil{

    /** Don't let anyone instantiate this class. */
    private WebSpringUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //  /**
    //   * 获得消息信息
    //   * 
    //   * @param request
    //   * @param messageSourceResolvable
    //   *            适用于 ObjectError 以及 FieldError
    //   * @return
    //   */
    //  public static String getMessage(MessageSourceResolvable messageSourceResolvable,HttpServletRequest request){
    //      HttpSession session = request.getSession();
    //      ServletContext servletContext = session.getServletContext();
    //      WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    //      return webApplicationContext.getMessage(messageSourceResolvable, request.getLocale());
    //  }

    //********************************************************************************
    /**
     * 普通类获得spring 注入的类方法.
     * 
     * @param <T>
     *            the generic type
     * @param request
     *            request
     * @param beanName
     *            xml文件中配置的bean beanName
     * @return 注入的bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(HttpServletRequest request,String beanName){
        HttpSession session = request.getSession();
        return (T) getBean(session, beanName);
    }

    /**
     * Gets the bean.
     * 
     * @param <T>
     *            the generic type
     * @param request
     *            the request
     * @param requiredType
     *            the required type
     * @return the bean
     */
    public static <T> T getBean(HttpServletRequest request,Class<T> requiredType){
        HttpSession session = request.getSession();
        return getBean(session, requiredType);
    }

    /**
     * 普通类获得spring 注入的类方法<br>
     * 注意:<b>(如果找不到bean,返回null)</b>.
     * 
     * @param <T>
     *            the generic type
     * @param session
     *            session
     * @param beanName
     *            xml文件中配置的bean beanName
     * @return 注入的bean
     */
    public static <T> T getBean(HttpSession session,String beanName){
        ServletContext servletContext = session.getServletContext();
        return getBean(servletContext, beanName);
    }

    /**
     * Gets the bean.
     * 
     * @param <T>
     *            the generic type
     * @param session
     *            the session
     * @param requiredType
     *            the required type
     * @return the bean
     */
    public static <T> T getBean(HttpSession session,Class<T> requiredType){
        ServletContext servletContext = session.getServletContext();
        return getBean(servletContext, requiredType);
    }

    //********************************************************************************************
    /**
     * 普通类获得spring 注入的类方法<br>
     * 注意:<b>(如果找不到bean,返回null)</b>.
     * 
     * @param <T>
     *            the generic type
     * @param servletContext
     *            servletContext
     * @param beanName
     *            xml文件中配置的bean beanName
     * @return 注入的bean
     * @see #getWebApplicationContext(ServletContext)
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(ServletContext servletContext,String beanName){
        WebApplicationContext webApplicationContext = getWebApplicationContext(servletContext);
        return (T) getBean(webApplicationContext, beanName);
    }

    /**
     * Gets the bean.
     * 
     * @param <T>
     *            the generic type
     * @param servletContext
     *            the servlet context
     * @param requiredType
     *            the required type
     * @return the bean
     */
    public static <T> T getBean(ServletContext servletContext,Class<T> requiredType){
        WebApplicationContext webApplicationContext = getWebApplicationContext(servletContext);
        return getBean(webApplicationContext, requiredType);
    }

    //********************************************************************************************

    /**
     * 普通类获得spring 注入的类方法<br>
     * 注意:<b>(如果找不到bean会抛出异常)</b>.
     * 
     * @param <T>
     *            the generic type
     * @param request
     *            request
     * @param beanName
     *            xml文件中配置的bean beanName
     * @return 注入的bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getRequiredBean(HttpServletRequest request,String beanName){
        HttpSession session = request.getSession();
        return (T) getRequiredBean(session, beanName);
    }

    /**
     * Gets the required bean.
     * 
     * @param <T>
     *            the generic type
     * @param request
     *            the request
     * @param requiredType
     *            the required type
     * @return the required bean
     */
    public static <T> T getRequiredBean(HttpServletRequest request,Class<T> requiredType){
        HttpSession session = request.getSession();
        return getRequiredBean(session, requiredType);
    }

    /**
     * Gets the required bean.
     * 
     * @param <T>
     *            the generic type
     * @param session
     *            the session
     * @param beanName
     *            the bean name
     * @return the required bean
     */
    public static <T> T getRequiredBean(HttpSession session,String beanName){
        ServletContext servletContext = session.getServletContext();
        return getRequiredBean(servletContext, beanName);
    }

    /**
     * Gets the required bean.
     * 
     * @param <T>
     *            the generic type
     * @param session
     *            the session
     * @param requiredType
     *            the required type
     * @return the required bean
     */
    public static <T> T getRequiredBean(HttpSession session,Class<T> requiredType){
        ServletContext servletContext = session.getServletContext();
        return getRequiredBean(servletContext, requiredType);
    }

    /**
     * Gets the required bean.
     * 
     * @param <T>
     *            the generic type
     * @param servletContext
     *            the servlet context
     * @param beanName
     *            the bean name
     * @return the required bean
     * @see #getRequiredWebApplicationContext(ServletContext)
     */
    public static <T> T getRequiredBean(ServletContext servletContext,String beanName){
        WebApplicationContext webApplicationContext = getRequiredWebApplicationContext(servletContext);
        return getBean(webApplicationContext, beanName);
    }

    /**
     * Gets the required bean.
     * 
     * @param <T>
     *            the generic type
     * @param servletContext
     *            the servlet context
     * @param requiredType
     *            the required type
     * @return the required bean
     * @see #getRequiredWebApplicationContext(ServletContext)
     */
    public static <T> T getRequiredBean(ServletContext servletContext,Class<T> requiredType){
        WebApplicationContext webApplicationContext = getRequiredWebApplicationContext(servletContext);
        return getBean(webApplicationContext, requiredType);
    }

    //*******************************************************************************************
    /**
     * Gets the bean.
     *
     * @param <T>
     *            the generic type
     * @param applicationContext
     *            the application context
     * @param beanName
     *            the bean name
     * @return the bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(ApplicationContext applicationContext,String beanName){
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * Gets the bean.
     *
     * @param <T>
     *            the generic type
     * @param applicationContext
     *            the application context
     * @param requiredType
     *            the required type
     * @return the bean
     */
    public static <T> T getBean(ApplicationContext applicationContext,Class<T> requiredType){
        return applicationContext.getBean(requiredType);
    }

    //*******************************************************************************************

    /**
     * 获得 web application context.
     *
     * @param servletContext
     *            the servlet context
     * @return the web application context
     * @see org.springframework.web.context.WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
     * @see org.springframework.web.context.support.WebApplicationContextUtils#getWebApplicationContext(ServletContext)
     * @since 1.1.1
     */
    public static WebApplicationContext getWebApplicationContext(ServletContext servletContext){
        return WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

    /**
     * 获得 web application context.
     *
     * @param servletContext
     *            the servlet context
     * @return the web application context
     * @see org.springframework.web.context.WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
     * @see org.springframework.web.context.support.WebApplicationContextUtils#getRequiredWebApplicationContext(ServletContext)
     * @since 1.2.0
     */
    public static WebApplicationContext getRequiredWebApplicationContext(ServletContext servletContext){
        return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    }
}
