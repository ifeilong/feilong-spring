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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.HttpMethodType;

/**
 * The Class UserTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
@ContextConfiguration("classpath:spring-DI-enum.xml")
public class SpringDIEnumTest extends AbstractJUnit4SpringContextTests{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDIEnumTest.class);

    /** The platform. */
    @Autowired
    private HttpMethodType      httpMethodType;

    /**
     * Test platform.
     */
    @Test
    public void testPlatform(){
        // String aString = "china".equals("china") ? "" : ("china".equals("china") ? "" : "");
        LOGGER.debug(JsonUtil.format(httpMethodType));
    }
}