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
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;

import com.feilong.core.util.SortUtil;
import com.feilong.formatter.entity.BeanFormatterConfig;
import com.feilong.spring.context.ApplicationContextUtil;
import com.feilong.spring.event.builder.DefaultMapBeanToMapListBuilder;
import com.feilong.spring.event.builder.MapBeanToMapListBuilder;

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
 * @since 4.0.0 move from feilong-spring-web
 */
public final class ContextRefreshedBeanLoggingEventListener<T> extends AbstractContextRefreshedEventListener{

    /** The Constant LOGGER. */
    private static final Logger        LOGGER                  = LoggerFactory.getLogger(ContextRefreshedBeanLoggingEventListener.class);

    //---------------------------------------------------------------

    /** The bean class. */
    private Class<T>                   beanClass;

    /**
     * list 排序,属性和order 配置.
     * 
     * @since 1.12.9
     */
    private String[]                   listSortPropertyNameAndOrders;

    /**
     * 提供格式化的时候,相关参数控制.
     * 
     * @since 1.12.9
     */
    private BeanFormatterConfig        beanFormatterConfig;

    //---------------------------------------------------------------

    /**
     * 将 {@code Map<String, T> beanNameAndBeanMap} 转成 {@code List<Map<String, Object>>} 的构造器.
     * 
     * @since 4.0.0
     */
    private MapBeanToMapListBuilder<T> mapBeanToMapListBuilder = new DefaultMapBeanToMapListBuilder<T>();

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
        Map<String, T> beanNameAndBeanMap = ApplicationContextUtil
                        .getBeanNameAndBeanMap(contextRefreshedEvent.getApplicationContext(), beanClass);
        if (isNullOrEmpty(beanNameAndBeanMap)){
            LOGGER.info("can't find [{}] bean", beanClass.getName());
            return;
        }
        //---------------------------------------------------------------

        List<Map<String, Object>> list = mapBeanToMapListBuilder.build(beanNameAndBeanMap);

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
    private void doLog(List<Map<String, Object>> list){
        if (LOGGER.isInfoEnabled()){
            if (isNotNullOrEmpty(listSortPropertyNameAndOrders)){
                list = SortUtil.sortListByPropertyNamesValue(list, listSortPropertyNameAndOrders);
            }
            LOGGER.info("[{}] list size:[{}], Info:{}", beanClass, list.size(), formatToSimpleTable(list, beanFormatterConfig));
        }
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
     * 将 {@code Map<String, T> beanNameAndBeanMap} 转成 {@code List<Map<String, Object>>} 的构造器.
     *
     * @param mapBeanToMapListBuilder
     *            the mapBeanToMapListBuilder to set
     * @since 4.0.0
     */
    public void setMapBeanToMapListBuilder(MapBeanToMapListBuilder<T> mapBeanToMapListBuilder){
        this.mapBeanToMapListBuilder = mapBeanToMapListBuilder;
    }

}
