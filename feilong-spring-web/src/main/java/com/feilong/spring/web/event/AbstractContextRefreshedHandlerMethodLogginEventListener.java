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
package com.feilong.spring.web.event;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;
import static java.util.Collections.emptyMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.feilong.spring.event.AbstractContextRefreshedEventListener;

/**
 * ApplicationContext 初始化或刷新完成后触发的事件,用来分析 HandlerMethod信息的父类.
 * 
 * <p>
 * 
 * 只有一个ApplicationContextEvent,表示ApplicationContext容器事件,且其又有如下实现：
 * </p>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * <th align="left">备注</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>ContextStartedEvent</td>
 * <td>ApplicationContext启动后触发的事件</td>
 * <td>目前版本没有任何作用</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>ContextStoppedEvent</td>
 * <td>ApplicationContext停止后触发的事件</td>
 * <td>目前版本没有任何作用</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>ContextRefreshedEvent</td>
 * <td>ApplicationContext初始化或刷新完成后触发的事件</td>
 * <td>容器初始化完成后调用</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>ContextClosedEvent</td>
 * <td>ApplicationContext关闭后触发的事件；</td>
 * <td>（如web容器关闭时自动会触发spring容器的关闭,如果是普通java应用,需要调用ctx.registerShutdownHook();注册虚拟机关闭时的钩子才行）</td>
 * </tr>
 * 
 * </table>
 * 注: {@link org.springframework.context.support.AbstractApplicationContext}
 * 抽象类实现了LifeCycle的start和stop回调并发布ContextStartedEvent和ContextStoppedEvent事件；但是无任何实现调用它,所以目前无任何作用。
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.context.event.SmartApplicationListener
 * @since 1.10.5
 */
public abstract class AbstractContextRefreshedHandlerMethodLogginEventListener extends AbstractContextRefreshedEventListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractContextRefreshedHandlerMethodLogginEventListener.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        if (!LOGGER.isInfoEnabled()){
            return;
        }

        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<RequestMappingInfo, HandlerMethod> requestMappingInfoAndHandlerMethodMap = buildHandlerMethods(applicationContext);

        if (isNullOrEmpty(requestMappingInfoAndHandlerMethodMap)){
            LOGGER.info("requestMappingInfo And HandlerMethod Map is null or empty!!");
            return;
        }
        //---------------------------------------------------------------
        doLogging(requestMappingInfoAndHandlerMethodMap);
    }

    /**
     * @param requestMappingInfoAndHandlerMethodMap
     * @since 1.10.5
     */
    protected void doLogging(Map<RequestMappingInfo, HandlerMethod> requestMappingInfoAndHandlerMethodMap){
        List<Map<String, Object>> list = buildList(requestMappingInfoAndHandlerMethodMap);

        if (isNullOrEmpty(list)){
            LOGGER.info("list is null or empty");
            return;
        }

        render(list);
    }

    //---------------------------------------------------------------

    /**
     * Render.
     *
     * @param list
     *            the list
     */
    protected void render(List<Map<String, Object>> list){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("handler method ,size:[{}],info:{}", list.size(), formatToSimpleTable(list));
        }
    }

    /**
     * 构造数据.
     *
     * @param handlerMethods
     *            the handler methods
     * @return the list
     */
    protected List<Map<String, Object>> buildList(Map<RequestMappingInfo, HandlerMethod> handlerMethods){
        return null;
    }

    //---------------------------------------------------------------

    /**
     * Builds the handler methods.
     *
     * @param applicationContext
     *            the application context
     * @return 如果取不到 <code>RequestMappingHandlerMapping</code>,返回 {@link Collections#emptyMap()}<br>
     * @throws BeansException
     *             the beans exception
     */
    private static Map<RequestMappingInfo, HandlerMethod> buildHandlerMethods(ApplicationContext applicationContext){
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);//通过上下文对象获取RequestMappingHandlerMapping实例对象  

        if (null == requestMappingHandlerMapping){
            return emptyMap();
        }

        return requestMappingHandlerMapping.getHandlerMethods();
    }

}
