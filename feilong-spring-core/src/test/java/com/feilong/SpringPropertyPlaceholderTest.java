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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.json.JsonUtil;

/**
 * The Class SpringPropertyPlaceholderTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.8
 */
@ContextConfiguration(locations = { "classpath:spring-property-placeholder.xml" })
public class SpringPropertyPlaceholderTest extends AbstractJUnit4SpringContextTests{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringPropertyPlaceholderTest.class);

    //	@Value("#{p_testProperties['name']}")
    /** The name. */
    private String              name;

    //	@Value("#{p_testProperties['skills']}")
    /** The skills. */
    private String              skills;

    /** The skills array. */
    @Value("#{p_testProperties['skills']}")
    private String[]            skillsArray;

    /** The skills array2. */
    @Value("${skills}")
    private String[]            skillsArray2;

    //---------------------------------------------------------------

    /**
     * Test.
     */
    @Test
    public void test(){
        LOGGER.debug(name);
        LOGGER.debug(skills);
        LOGGER.debug(JsonUtil.format(skillsArray));
        LOGGER.debug(JsonUtil.format(skillsArray2));
    }
}
