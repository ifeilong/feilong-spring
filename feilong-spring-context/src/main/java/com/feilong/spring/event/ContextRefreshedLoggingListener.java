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
package com.feilong.spring.event;

import static com.feilong.spring.context.ApplicationContextUtil.getApplicationContextInfoMapForLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import com.feilong.json.JsonUtil;

/**
 * 可以在控制台显示 {@link ApplicationContext} 信息.
 *
 * <h3>作用:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 可以在日志文件或者控制台输出如下信息:
 * </p>
 * 
 * <pre class="code">
14:39:39 DEBUG (ContextRefreshedLoggingListener.java:95) onApplicationEvent() - applicationContext info:    {
        "beanDefinitionCount": 71,
        "startupDate": 1526798376957,
        "applicationName": "",
        "displayName": "WebApplicationContext for namespace 'springmvc-servlet'",
        "id": "org.springframework.web.context.WebApplicationContext:/springmvc",
        "beanDefinitionNamesAndClassMap":         {
            "browsingHistoryResolver": "com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryCookieResolver[Singleton]",
            "contextRefreshedBeanLoggingEventListener": "com.feilong.spring.web.event.ContextRefreshedBeanLoggingEventListener[Singleton]",
            "contextRefreshedCookieAccessorEventListener": "com.feilong.spring.web.event.ContextRefreshedCookieAccessorEventListener[Singleton]",
            "contextRefreshedEventClientCacheListener": "com.feilong.spring.web.servlet.interceptor.clientcache.ContextRefreshedClientCacheInfoEventListener[Singleton]",
            "contextRefreshedHandlerExceptionResolverEventListener": "com.feilong.spring.web.event.ContextRefreshedBeanLoggingEventListener[Singleton]",
            "contextRefreshedHandlerMethodInfoEventListener": "com.feilong.spring.web.event.ContextRefreshedHandlerMethodInfoEventListener[Singleton]",
            "contextRefreshedLoggingListener": "com.feilong.spring.event.ContextRefreshedLoggingListener[Singleton]",
            "defaultHandlerExceptionResolver": "org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver[Singleton]",
            "errorController": "com.feilong.controller.ErrorController[Singleton]",
            "exceptionTestController": "com.feilong.controller.ExceptionTestController[Singleton]",
            "immediatelyBuyAutoKeyAccessor": "com.feilong.accessor.session.SessionAutoKeyAccessor[Singleton]",
            "indexController": "com.feilong.controller.IndexController[Singleton]",
            "jspController": "com.feilong.controller.JspController[Singleton]",
            "localeResolver": "org.springframework.web.servlet.i18n.CookieLocaleResolver[Singleton]",
            "loginBindCookieAccessor": "com.feilong.accessor.cookie.CookieAccessor[Singleton]",
            "loginBindSessionKeyAccessor": "com.feilong.accessor.session.SessionKeyAccessor[Singleton]",
            "messageSource": "com.feilong.spring.messagesource.PathMatchingReloadableResourceBundleMessageSource[Singleton]",
            "mvcContentNegotiationManager": "org.springframework.web.accept.ContentNegotiationManager[Singleton]",
            "mvcPathMatcher": "org.springframework.util.AntPathMatcher[Singleton]",
            "mvcUrlPathHelper": "org.springframework.web.util.UrlPathHelper[Singleton]",
            "nickNameCookieAccessor": "com.feilong.accessor.cookie.CookieAccessor[Singleton]",
            "org.springframework.context.annotation.ConfigurationClassPostProcessor.importAwareProcessor": "org.springframework.context.annotation.ConfigurationClassPostProcessor.ImportAwareBeanPostProcessor[Singleton]",
            "org.springframework.context.annotation.internalAutowiredAnnotationProcessor": "org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor[Singleton]",
            "org.springframework.context.annotation.internalCommonAnnotationProcessor": "org.springframework.context.annotation.CommonAnnotationBeanPostProcessor[Singleton]",
            "org.springframework.context.annotation.internalConfigurationAnnotationProcessor": "org.springframework.context.annotation.ConfigurationClassPostProcessor[Singleton]",
            "org.springframework.context.annotation.internalRequiredAnnotationProcessor": "org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor[Singleton]",
            "org.springframework.format.support.FormattingConversionServiceFactoryBean#0": "org.springframework.format.support.DefaultFormattingConversionService[Singleton]",
            "org.springframework.web.servlet.config.viewControllerHandlerMapping": "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping[Singleton]",
            "org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping": "org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping[Singleton]",
            "org.springframework.web.servlet.handler.MappedInterceptor#0": "org.springframework.web.servlet.handler.MappedInterceptor[Singleton]",
            "org.springframework.web.servlet.handler.MappedInterceptor#1": "org.springframework.web.servlet.handler.MappedInterceptor[Singleton]",
            "org.springframework.web.servlet.handler.MappedInterceptor#2": "org.springframework.web.servlet.handler.MappedInterceptor[Singleton]",
            "org.springframework.web.servlet.handler.MappedInterceptor#3": "org.springframework.web.servlet.handler.MappedInterceptor[Singleton]",
            "org.springframework.web.servlet.handler.MappedInterceptor#4": "org.springframework.web.servlet.handler.MappedInterceptor[Singleton]",
            "org.springframework.web.servlet.handler.MappedInterceptor#5": "org.springframework.web.servlet.handler.MappedInterceptor[Singleton]",
            "org.springframework.web.servlet.handler.MappedInterceptor#6": "org.springframework.web.servlet.handler.MappedInterceptor[Singleton]",
            "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping#0": "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping[Singleton]",
            "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping#1": "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping[Singleton]",
            "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping#2": "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping[Singleton]",
            "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping#3": "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping[Singleton]",
            "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping#4": "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping[Singleton]",
            "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping#5": "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping[Singleton]",
            "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping#6": "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping[Singleton]",
            "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping#7": "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping[Singleton]",
            "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping#8": "org.springframework.web.servlet.handler.SimpleUrlHandlerMapping[Singleton]",
            "org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter": "org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter[Singleton]",
            "org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter": "org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter[Singleton]",
            "org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver#0": "org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver[Singleton]",
            "org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver#0": "org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver[Singleton]",
            "org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#0": "org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter[Singleton]",
            "org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#0": "org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping[Singleton]",
            "org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver#0": "org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver[Singleton]",
            "org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler#0": "org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler[Singleton]",
            "org.springframework.web.servlet.resource.ResourceHttpRequestHandler#0": "org.springframework.web.servlet.resource.ResourceHttpRequestHandler[Singleton]",
            "org.springframework.web.servlet.resource.ResourceHttpRequestHandler#1": "org.springframework.web.servlet.resource.ResourceHttpRequestHandler[Singleton]",
            "org.springframework.web.servlet.resource.ResourceHttpRequestHandler#2": "org.springframework.web.servlet.resource.ResourceHttpRequestHandler[Singleton]",
            "org.springframework.web.servlet.resource.ResourceHttpRequestHandler#3": "org.springframework.web.servlet.resource.ResourceHttpRequestHandler[Singleton]",
            "org.springframework.web.servlet.resource.ResourceHttpRequestHandler#4": "org.springframework.web.servlet.resource.ResourceHttpRequestHandler[Singleton]",
            "org.springframework.web.servlet.resource.ResourceHttpRequestHandler#5": "org.springframework.web.servlet.resource.ResourceHttpRequestHandler[Singleton]",
            "org.springframework.web.servlet.resource.ResourceHttpRequestHandler#6": "org.springframework.web.servlet.resource.ResourceHttpRequestHandler[Singleton]",
            "org.springframework.web.servlet.resource.ResourceHttpRequestHandler#7": "org.springframework.web.servlet.resource.ResourceHttpRequestHandler[Singleton]",
            "org.springframework.web.servlet.view.BeanNameViewResolver#0": "org.springframework.web.servlet.view.BeanNameViewResolver[Singleton]",
            "org.springframework.web.servlet.view.InternalResourceViewResolver#0": "org.springframework.web.servlet.view.InternalResourceViewResolver[Singleton]",
            "pathVariableController": "com.feilong.controller.PathVariableController[Singleton]",
            "pathVariableController1": "com.feilong.controller.PathVariableController1[Singleton]",
            "paymentSuccessTokenKeyAccessor": "com.feilong.accessor.session.SessionKeyAccessor[Singleton]",
            "requestBodyController": "com.feilong.controller.RequestBodyController[Singleton]",
            "simpleMappingHandlerMethodExceptionResolver": "com.feilong.spring.web.servlet.handler.SimpleMappingHandlerMethodExceptionResolver[Singleton]",
            "stdPDPController": "com.feilong.controller.pdp.StdPDPController[Singleton]",
            "tilesConfigurer": "org.springframework.web.servlet.view.tiles3.TilesConfigurer[Singleton]",
            "tilesViewResolver": "org.springframework.web.servlet.view.tiles3.TilesViewResolver[Singleton]"
        },
        "ApplicationContext.CLASSPATH_ALL_URL_PREFIX": "classpath*:",
        "ApplicationContext.CLASSPATH_URL_PREFIX": "classpath:",
        "ApplicationContext.FACTORY_BEAN_PREFIX": "&",
        "parent info":         {
            "beanDefinitionCount": 0,
            "startupDate": 1526798376498,
            "applicationName": "",
            "displayName": "Root WebApplicationContext",
            "id": "org.springframework.web.context.WebApplicationContext:",
            "beanDefinitionNamesAndClassMap": {},
            "ApplicationContext.CLASSPATH_ALL_URL_PREFIX": "classpath*:",
            "ApplicationContext.CLASSPATH_URL_PREFIX": "classpath:",
            "ApplicationContext.FACTORY_BEAN_PREFIX": "&",
            "parent info": null
        }
    }
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>参考配置:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
{@code 
    <bean id="contextRefreshedLoggingListener" class="com.feilong.spring.event.ContextRefreshedLoggingListener" />
}
 * </pre>
 * 
 * 如果加载 parent bean 时间比较长, 你还可以使用 <b>isShowParentInfo</b> 参数
 * 
 * <pre class="code">
{@code 
    <bean id="contextRefreshedLoggingListener" class="com.feilong.spring.event.ContextRefreshedLoggingListener" >
         <property name="isShowParentInfo" value="false" />
    </bean>
}
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.context.event.SmartApplicationListener
 * @since 1.10.4
 */
public class ContextRefreshedLoggingListener extends AbstractContextRefreshedEventListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(ContextRefreshedLoggingListener.class);

    /**
     * 是否显示父容器信息.
     * 
     * @since 1.12.7
     */
    private boolean             isShowParentInfo = true;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.event.AbstractContextRefreshedEventListener#doOnApplicationEvent(org.springframework.context.event.
     * ContextRefreshedEvent)
     */
    @Override
    public void doOnApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        if (LOGGER.isDebugEnabled()){
            ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
            LOGGER.debug(
                            "applicationContext info:{}",
                            JsonUtil.format(getApplicationContextInfoMapForLog(applicationContext, isShowParentInfo)));
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置 是否显示父容器信息.
     *
     * @param isShowParentInfo
     *            the isShowParentInfo to set
     * @since 1.12.7
     */
    public void setIsShowParentInfo(boolean isShowParentInfo){
        this.isShowParentInfo = isShowParentInfo;
    }
}
