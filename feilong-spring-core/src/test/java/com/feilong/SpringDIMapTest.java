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

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.feilong.entity.DIUser;

public class SpringDIMapTest{

    /** The file system context. */
    private static ApplicationContext fileSystemContext;

    /**
     * Before class.
     */
    @BeforeClass
    public static void beforeClass(){
        fileSystemContext = new FileSystemXmlApplicationContext("classpath:spring-DI-Map.xml");
    }

    /**
     * Test.
     */
    @Test
    public void test(){
        DIUser diUser = (DIUser) fileSystemContext.getBean("feitian@");

        Map<String, Object> map = diUser.getMap();
        assertThat(map.get("五子良将"), allOf(hasProperty("code", is("false")), hasProperty("loginName", is("哈哈哈"))));
        assertThat(map.get("八虎骑"), allOf(hasProperty("code", is("true")), hasProperty("loginName", is("呵呵呵"))));
    }
}