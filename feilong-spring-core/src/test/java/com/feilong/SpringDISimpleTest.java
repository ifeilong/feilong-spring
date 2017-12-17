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

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.feilong.entity.DIUser;
import com.feilong.json.jsonlib.JsonUtil;

/**
 * The Class UserTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class SpringDISimpleTest{

    /** The Constant LOGGER. */
    private static final Logger       LOGGER = LoggerFactory.getLogger(SpringDISimpleTest.class);

    /** The file system context. */
    private static ApplicationContext fileSystemContext;

    /**
     * Before class.
     */
    @BeforeClass
    public static void beforeClass(){
        fileSystemContext = new FileSystemXmlApplicationContext("classpath:spring-DI-Array.xml", "classpath:spring-DI-simple.xml");
    }

    /**
     * Test.
     */
    @Test
    public void testUser(){
        DIUser diUser = (DIUser) fileSystemContext.getBean("spring-DI-simple");
        LOGGER.debug(JsonUtil.format(diUser));
    }
}