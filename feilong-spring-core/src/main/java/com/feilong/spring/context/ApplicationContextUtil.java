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
package com.feilong.spring.context;

import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static com.feilong.core.util.SortUtil.sortArray;
import static java.util.Collections.emptyMap;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * {@link ApplicationContext} 工具类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.8
 */
public final class ApplicationContextUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextUtil.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private ApplicationContextUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    /**
     * 获得 application context for log map.
     *
     * @param applicationContext
     *            the application context
     * @return 如果 <code>applicationContext</code> 是null,抛出 {@link NullPointerException}<br>
     */
    public static Map<String, Object> getApplicationContextInfoMapForLog(ApplicationContext applicationContext){
        return getApplicationContextInfoMapForLog(applicationContext, true);
    }

    //---------------------------------------------------------------
    /**
     * 获得 application context for log map.
     *
     * @param applicationContext
     *            the application context
     * @param loadParent
     *            the load parent
     * @return 如果 <code>applicationContext</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 1.12.7
     */
    public static Map<String, Object> getApplicationContextInfoMapForLog(ApplicationContext applicationContext,boolean loadParent){
        Validate.notNull(applicationContext, "applicationContext can't be null!");

        Date beginDate = new Date();
        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("begin read [{}] info", buildKeyMessage(applicationContext));
        }
        //---------------------------------------------------------------
        Map<String, Object> map = newLinkedHashMap();

        map.put("id", buildKeyMessage(applicationContext));
        map.put("class", applicationContext.getClass());
        map.put("displayName", applicationContext.getDisplayName());
        map.put("beanDefinitionCount", applicationContext.getBeanDefinitionCount());
        map.put("startupDate", applicationContext.getStartupDate());

        map.put("applicationName", applicationContext.getApplicationName());

        //---------------------------------------------------------------

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        sortArray(beanDefinitionNames);

        map.put("beanDefinitionNamesAndClassMap", buildBeanDefinitionNameAndClassMap(applicationContext, beanDefinitionNames));

        //Environment environment = applicationContext.getEnvironment();
        //map.put("environment", environment);

        map.put("ApplicationContext.CLASSPATH_ALL_URL_PREFIX", ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX);
        map.put("ApplicationContext.CLASSPATH_URL_PREFIX", ResourceLoader.CLASSPATH_URL_PREFIX);
        map.put("ApplicationContext.FACTORY_BEAN_PREFIX", BeanFactory.FACTORY_BEAN_PREFIX);

        if (loadParent){
            ApplicationContext parent = applicationContext.getParent();
            map.put("parent info", null == parent ? null : getApplicationContextInfoMapForLog(parent));
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("end read [{}] info,use time: [{}]", buildKeyMessage(applicationContext), formatDuration(beginDate));
        }
        return map;
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
     * @return 如果 applicationContext 中没有指定的klass ,返回 {@link Collections#emptyMap()}<br>
     * @see org.springframework.beans.factory.BeanFactoryUtils#beansOfTypeIncludingAncestors(org.springframework.beans.factory.ListableBeanFactory,
     *      Class, boolean, boolean)
     * @see org.springframework.beans.factory.ListableBeanFactory#getBeansOfType(Class)
     * @see org.springframework.beans.factory.ListableBeanFactory#getBeansOfType(Class, boolean, boolean)
     * @since 4.0.0
     */
    public static <T> Map<String, T> getBeanNameAndBeanMap(ApplicationContext applicationContext,Class<T> klass){
        //LinkedHashMap
        Map<String, T> beanNameAndBeanMap = applicationContext.getBeansOfType(klass);
        if (null == beanNameAndBeanMap){
            return emptyMap();
        }
        return beanNameAndBeanMap;
    }

    //---------------------------------------------------------------

    /**
     * Builds the bean definition names and class map.
     *
     * @param applicationContext
     *            the application context
     * @param beanDefinitionNames
     *            the bean definition names
     * @return the map
     * @since 1.10.4
     */
    private static Map<String, String> buildBeanDefinitionNameAndClassMap(
                    ApplicationContext applicationContext,
                    String[] beanDefinitionNames){
        Map<String, String> map = new TreeMap<>();

        Date beginDate = new Date();
        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("begin build [{}] BeanDefinitionNameAndClassMap", buildKeyMessage(applicationContext));
        }

        //---------------------------------------------------------------
        for (String beanDefinitionName : beanDefinitionNames){
            map.put(beanDefinitionName, buildValue(applicationContext, beanDefinitionName));
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "end build [{}] BeanDefinitionNameAndClassMap,use time: [{}]",
                            buildKeyMessage(applicationContext),
                            formatDuration(beginDate));
        }
        return map;
    }

    //---------------------------------------------------------------

    /**
     * Builds the key message.
     *
     * @param applicationContext
     *            the application context
     * @return the string
     * @since 1.12.7
     */
    private static String buildKeyMessage(ApplicationContext applicationContext){
        return applicationContext.getDisplayName();
    }

    //---------------------------------------------------------------

    /**
     * Builds the value.
     *
     * @param applicationContext
     *            the application context
     * @param beanDefinitionName
     *            the bean definition name
     * @return the string
     * @since 1.12.7
     */
    private static String buildValue(ApplicationContext applicationContext,String beanDefinitionName){
        try{
            Object bean = applicationContext.getBean(beanDefinitionName);
            return buildValue(applicationContext, beanDefinitionName, bean);
        }catch (Exception e){
            return e.getMessage();
        }
    }

    //---------------------------------------------------------------
    /**
     * Builds the value.
     *
     * @param applicationContext
     *            the application context
     * @param beanDefinitionName
     *            the bean definition name
     * @param bean
     *            the bean
     * @return the string
     * @throws NoSuchBeanDefinitionException
     *             the no such bean definition exception
     * @since 1.12.6
     */
    private static String buildValue(ApplicationContext applicationContext,String beanDefinitionName,Object bean)
                    throws NoSuchBeanDefinitionException{
        String canonicalName = bean.getClass().getCanonicalName();
        return canonicalName + (applicationContext.isPrototype(beanDefinitionName) ? "[Prototype]"
                        : (applicationContext.isSingleton(beanDefinitionName) ? "[Singleton]" : ""));
    }
}
