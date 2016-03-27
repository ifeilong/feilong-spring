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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.entity.DIUserArray;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * The Class SpringDIArrayTest.
 *
 * @author feilong
 * @version 1.0.8 2014年10月8日 下午4:03:31
 * @since 1.0.8
 */
@ContextConfiguration(locations = { "classpath:spring-DI-Array.xml" })
public class SpringDIArrayTest extends AbstractJUnit4SpringContextTests{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDIArrayTest.class);

    /**
     * Test.
     */
    @Test
    public void testDIUserArray(){
        DIUserArray diUserArray = applicationContext.getBean(DIUserArray.class);
        LOGGER.info(JsonUtil.format(diUserArray));
    }
}
