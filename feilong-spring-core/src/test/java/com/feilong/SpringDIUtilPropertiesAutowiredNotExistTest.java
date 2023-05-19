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

import java.util.Properties;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.json.JsonUtil;

@ContextConfiguration("classpath:spring-DI-util-properties-notexist.xml")
public class SpringDIUtilPropertiesAutowiredNotExistTest extends AbstractJUnit4SpringContextTests{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDIUtilPropertiesAutowiredNotExistTest.class);

    @Autowired(required = false)
    @Qualifier("p_AlipayAdvanceNotExist")
    private Properties          propertiesAppend;

    //---------------------------------------------------------------

    @Test
    public void test1(){
        LOGGER.debug(JsonUtil.format(propertiesAppend));
    }

}