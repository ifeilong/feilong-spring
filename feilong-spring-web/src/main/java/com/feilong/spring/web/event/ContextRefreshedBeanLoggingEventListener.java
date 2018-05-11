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
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;
import static java.util.Collections.emptyMap;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import com.feilong.spring.event.AbstractContextRefreshedEventListener;
import com.feilong.spring.web.event.builder.BeanToMapBuilder;

/**
 * The listener interface for receiving contextStartedLogging events.
 * The class that is interested in processing a contextStartedLogging
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addContextStartedLoggingListener</code> method. When
 * the contextStartedLogging event occurs, that object's appropriate
 * method is invoked.
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
 * @param <T>
 *            the generic type
 * @see org.springframework.context.event.SmartApplicationListener
 * @since 1.10.4
 */
public class ContextRefreshedBeanLoggingEventListener<T> extends AbstractContextRefreshedEventListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextRefreshedBeanLoggingEventListener.class);

    /** The bean class. */
    private Class<T>            beanClass;

    /** The bean to map builder. */
    private BeanToMapBuilder<T> beanToMapBuilder;

    //---------------------------------------------------------------

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

        //---------------------------------------------------------------
        Map<String, T> buildBeanNameAndBeanMap = buildBeanNameAndBeanMap(contextRefreshedEvent.getApplicationContext(), beanClass);

        if (isNullOrEmpty(buildBeanNameAndBeanMap)){
            LOGGER.info("can not find [{}] bean", beanClass.getName());
            return;
        }
        //---------------------------------------------------------------
        List<Map<String, Object>> list = buildList(buildBeanNameAndBeanMap);
        LOGGER.info("list size:[{}], Info:{}", list.size(), formatToSimpleTable(list));
    }

    //---------------------------------------------------------------

    /**
     * Builds the list.
     *
     * @param buildBeanNameAndBeanMap
     *            the build bean name and bean map
     * @return the list
     */
    private List<Map<String, Object>> buildList(Map<String, T> buildBeanNameAndBeanMap){
        List<Map<String, Object>> list = newArrayList();

        for (Map.Entry<String, T> entry : buildBeanNameAndBeanMap.entrySet()){
            list.add(beanToMapBuilder.build(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    /**
     * Builds the bean name and bean map.
     *
     * @param <T>
     *            the generic type
     * @param applicationContext
     *            the application context
     * @param klass
     *            the klass
     * @return the map
     */
    private static <T> Map<String, T> buildBeanNameAndBeanMap(ApplicationContext applicationContext,Class<T> klass){
        Map<String, T> beanNameAndBeanMap = applicationContext.getBeansOfType(klass);

        if (null == beanNameAndBeanMap){
            return emptyMap();
        }

        return beanNameAndBeanMap;
    }

    //---------------------------------------------------------------

    /**
     * Gets the bean class.
     *
     * @return the beanClass
     */
    public Class<T> getBeanClass(){
        return beanClass;
    }

    /**
     * Sets the bean class.
     *
     * @param beanClass
     *            the beanClass to set
     */
    public void setBeanClass(Class<T> beanClass){
        this.beanClass = beanClass;
    }

    /**
     * Gets the bean to map builder.
     *
     * @return the beanToMapBuilder
     */
    public BeanToMapBuilder<T> getBeanToMapBuilder(){
        return beanToMapBuilder;
    }

    /**
     * Sets the bean to map builder.
     *
     * @param beanToMapBuilder
     *            the beanToMapBuilder to set
     */
    public void setBeanToMapBuilder(BeanToMapBuilder<T> beanToMapBuilder){
        this.beanToMapBuilder = beanToMapBuilder;
    }

}
