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

import static com.feilong.core.util.SortUtil.sortArray;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * The Class ApplicationContextUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.8
 */
public final class ApplicationContextUtil{

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
    public static Map<String, Object> getApplicationContextForLogMap(ApplicationContext applicationContext){
        Validate.notNull(applicationContext, "applicationContext can't be null!");

        //---------------------------------------------------------------

        Map<String, Object> map = new LinkedHashMap<>();

        map.put("beanDefinitionCount", applicationContext.getBeanDefinitionCount());
        map.put("startupDate", applicationContext.getStartupDate());

        map.put("applicationName", applicationContext.getApplicationName());
        map.put("displayName", applicationContext.getDisplayName());

        map.put("class", applicationContext.getClass());

        map.put("id", applicationContext.getId());

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        sortArray(beanDefinitionNames);

        //map.put("beanDefinitionNames", beanDefinitionNames);

        Map<String, Object> beanDefinitionNamesAndClassMap = buildBeanDefinitionNamesAndClassMap(applicationContext, beanDefinitionNames);

        map.put("beanDefinitionNamesAndClassMap", beanDefinitionNamesAndClassMap);

        Environment environment = applicationContext.getEnvironment();
        map.put("environment", environment);

        map.put("ApplicationContext.CLASSPATH_ALL_URL_PREFIX", ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX);
        map.put("ApplicationContext.CLASSPATH_URL_PREFIX", ResourceLoader.CLASSPATH_URL_PREFIX);
        map.put("ApplicationContext.FACTORY_BEAN_PREFIX", BeanFactory.FACTORY_BEAN_PREFIX);

        ApplicationContext parent = applicationContext.getParent();
        map.put("parent info", null == parent ? null : getApplicationContextForLogMap(parent));
        return map;
    }

    /**
     * @param applicationContext
     * @param beanDefinitionNames
     * @return
     * @since 1.10.4
     */
    private static Map<String, Object> buildBeanDefinitionNamesAndClassMap(
                    ApplicationContext applicationContext,
                    String[] beanDefinitionNames){
        Map<String, Object> beanDefinitionNamesAndClassMap = new TreeMap<>();
        for (String beanDefinitionName : beanDefinitionNames){
            try{
                Object bean = applicationContext.getBean(beanDefinitionName);
                String canonicalName = bean.getClass().getCanonicalName();

                Object vObject = canonicalName + (applicationContext.isPrototype(beanDefinitionName) ? "[Prototype]"
                                : (applicationContext.isSingleton(beanDefinitionName) ? "[Singleton]" : ""));

                beanDefinitionNamesAndClassMap.put(beanDefinitionName, vObject);
            }catch (BeansException e){
                beanDefinitionNamesAndClassMap.put(beanDefinitionName, e.getMessage());
            }
        }
        return beanDefinitionNamesAndClassMap;
    }
}
