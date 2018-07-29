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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;
import static java.util.Collections.emptyMap;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import com.feilong.core.util.SortUtil;
import com.feilong.formatter.entity.BeanFormatterConfig;
import com.feilong.spring.event.builder.BeanToMapBuilder;
import com.feilong.spring.event.builder.SimpleBeanToMapBuilder;

/**
 * 启动的时候显示相关bean的日志信息.
 * 
 * <p>
 * 详细使用方法, 参见
 * {@link "com.feilong.spring.web.event.builder.HandlerExceptionResolverBeanToMapBuilder"},{@link "com.feilong.spring.web.event.builder.MappedInterceptorBeanToMapBuilder"}
 * 等类上注释
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @see org.springframework.context.event.SmartApplicationListener
 * @see "com.feilong.spring.web.event.builder.HandlerExceptionResolverBeanToMapBuilder"
 * @see "com.feilong.spring.web.event.builder.MappedInterceptorBeanToMapBuilder"
 * @since 1.10.4
 * @since 1.13.0 move from feilong-spring-web
 */
public class ContextRefreshedBeanLoggingEventListener<T> extends AbstractContextRefreshedEventListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(ContextRefreshedBeanLoggingEventListener.class);

    //---------------------------------------------------------------

    /** The bean class. */
    private Class<T>            beanClass;

    /**
     * list 排序,属性和order 配置.
     * 
     * @since 1.12.9
     */
    private String[]            listSortPropertyNameAndOrders;

    /**
     * 提供格式化的时候,相关参数控制.
     * 
     * @since 1.12.9
     */
    private BeanFormatterConfig beanFormatterConfig;

    /** 提取bean信息到map. */
    private BeanToMapBuilder<T> beanToMapBuilder = new SimpleBeanToMapBuilder<T>();

    //---------------------------------------------------------------

    /** Post construct. */
    @PostConstruct
    protected void postConstructCurrent(){
        Validate.notNull(beanClass, "beanClass can't be null!");
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.event.AbstractContextRefreshedEventListener#doOnApplicationEvent(org.springframework.context.event.
     * ContextRefreshedEvent)
     */
    @Override
    public void doOnApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        if (!LOGGER.isInfoEnabled()){
            return;
        }

        //---------------------------------------------------------------
        Map<String, T> beanNameAndBeanMap = buildBeanNameAndBeanMap(contextRefreshedEvent.getApplicationContext(), beanClass);
        if (isNullOrEmpty(beanNameAndBeanMap)){
            LOGGER.info("can't find [{}] bean", beanClass.getName());
            return;
        }
        //---------------------------------------------------------------
        List<Map<String, Object>> list = buildList(beanNameAndBeanMap);
        doLog(list);
    }

    //---------------------------------------------------------------

    /**
     * Do log.
     *
     * @param list
     *            the list
     * @since 1.11.4
     */
    protected void doLog(List<Map<String, Object>> list){
        if (LOGGER.isInfoEnabled()){
            if (isNotNullOrEmpty(listSortPropertyNameAndOrders)){
                list = SortUtil.sortListByPropertyNamesValue(list, listSortPropertyNameAndOrders);
            }
            LOGGER.info("[{}] list size:[{}], Info:{}", beanClass, list.size(), formatToSimpleTable(list, beanFormatterConfig));
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the list.
     *
     * @param beanNameAndBeanMap
     *            the build bean name and bean map
     * @return the list
     */
    protected List<Map<String, Object>> buildList(Map<String, T> beanNameAndBeanMap){
        List<Map<String, Object>> list = newArrayList();
        for (Map.Entry<String, T> entry : beanNameAndBeanMap.entrySet()){
            list.add(beanToMapBuilder.build(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    //---------------------------------------------------------------

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
     * @see org.springframework.beans.factory.BeanFactoryUtils#beansOfTypeIncludingAncestors(org.springframework.beans.factory.ListableBeanFactory,
     *      Class, boolean, boolean)
     * @see org.springframework.beans.factory.ListableBeanFactory#getBeansOfType(Class)
     * @see org.springframework.beans.factory.ListableBeanFactory#getBeansOfType(Class, boolean, boolean)
     */
    protected static <T> Map<String, T> buildBeanNameAndBeanMap(ApplicationContext applicationContext,Class<T> klass){
        //LinkedHashMap
        Map<String, T> beanNameAndBeanMap = applicationContext.getBeansOfType(klass);
        if (null == beanNameAndBeanMap){
            return emptyMap();
        }
        return beanNameAndBeanMap;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.event.AbstractContextRefreshedEventListener#keyMessage()
     */
    @Override
    protected String buildKeyMessage(){
        return "ContextRefreshedBeanLoggingEventListener [" + beanClass.getSimpleName() + "]";
    }

    //---------------------------------------------------------------
    /**
     * 获得 bean class.
     *
     * @return the beanClass
     */
    public Class<T> getBeanClass(){
        return beanClass;
    }

    /**
     * 设置 bean class.
     *
     * @param beanClass
     *            the beanClass to set
     */
    public void setBeanClass(Class<T> beanClass){
        this.beanClass = beanClass;
    }

    /**
     * 获得 list 排序,属性和order 配置.
     *
     * @return the listSortPropertyNameAndOrders
     * @since 1.12.9
     */
    public String[] getListSortPropertyNameAndOrders(){
        return listSortPropertyNameAndOrders;
    }

    /**
     * 设置 list 排序,属性和order 配置.
     *
     * @param listSortPropertyNameAndOrders
     *            the listSortPropertyNameAndOrders to set
     * @since 1.12.9
     */
    public void setListSortPropertyNameAndOrders(String[] listSortPropertyNameAndOrders){
        this.listSortPropertyNameAndOrders = listSortPropertyNameAndOrders;
    }

    /**
     * 获得 提供格式化的时候,相关参数控制.
     *
     * @return the beanFormatterConfig
     * @since 1.12.9
     */
    public BeanFormatterConfig getBeanFormatterConfig(){
        return beanFormatterConfig;
    }

    /**
     * 设置 提供格式化的时候,相关参数控制.
     *
     * @param beanFormatterConfig
     *            the beanFormatterConfig to set
     * @since 1.12.9
     */
    public void setBeanFormatterConfig(BeanFormatterConfig beanFormatterConfig){
        this.beanFormatterConfig = beanFormatterConfig;
    }

    /**
     * 获得 提取bean信息到map.
     *
     * @return the beanToMapBuilder
     */
    public BeanToMapBuilder<T> getBeanToMapBuilder(){
        return beanToMapBuilder;
    }

    /**
     * 设置 提取bean信息到map.
     *
     * @param beanToMapBuilder
     *            the beanToMapBuilder to set
     */
    public void setBeanToMapBuilder(BeanToMapBuilder<T> beanToMapBuilder){
        this.beanToMapBuilder = beanToMapBuilder;
    }

}
