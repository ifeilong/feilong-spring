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
package com.feilong;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.spring.context.ApplicationContextUtil;
import com.feilong.store.member.UserInfo;

/**
 * The Class UserTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */

@ContextConfiguration(locations = { "classpath:spring-DI-innerBean.xml" })
public class SpringDIInnerBeanTest extends AbstractJUnit4SpringContextTests{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDIInnerBeanTest.class);

    /**
     * Test.
     */
    @Test
    public void test(){
        //UserInfo bean = applicationContext.getBean(UserInfo.class);
        Map<String, UserInfo> beansOfType = ApplicationContextUtil.getBeanNameAndBeanMap(applicationContext, UserInfo.class);

        //        getBeansOfType(Class),获取某一类的所有的bean。
        //        getBeansOfType(Class,boolean,boolean)，后面两个布尔值，
        //第一代表是否也包含原型（Class祖先）bean或者或者只是singletons（包含FactoryBean生成的），
        //第二个表示是否立即实例化懒加载或者由FactoryBean生成的Bean以保证依赖关系。

        LOGGER.debug(JsonUtil.format(beansOfType));

        //https://stackoverflow.com/questions/4097845/spring-get-inline-bean-by-name
        BeanWrapper beanWrapper = new BeanWrapperImpl(applicationContext.getBean("user"));
        Object innerBean = beanWrapper.getPropertyValue("userInfo");
        LOGGER.debug(JsonUtil.format(innerBean));
    }
}