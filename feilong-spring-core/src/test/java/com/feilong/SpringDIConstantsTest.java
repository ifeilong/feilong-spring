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

import com.feilong.coreextension.entity.Platform;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * The Class UserTest.
 * 
 * @author feilong
 * @version 1.0.7 2014-6-16 1:09:06
 */
@ContextConfiguration("classpath:spring-DI-constants.xml")
public class SpringDIConstantsTest extends AbstractJUnit4SpringContextTests{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDIConstantsTest.class);

    /** The platform. */
    @Autowired
    private Platform            platform;

    /**
     * Test platform.
     */
    @Test
    public void testPlatform(){
        LOGGER.info(JsonUtil.format(platform));
    }
}