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

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.json.JsonUtil;

@ContextConfiguration("classpath:spring-DI-util-list.xml")
public class SpringDIUtilListResourceTest extends AbstractJUnit4SpringContextTests{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDIUtilListResourceTest.class);

    //https://blog.csdn.net/qianlibie/article/details/44079647
    @Resource(name = "utilTestList")
    private List<String>        utilTestList1;

    @Resource(name = "utilTestList2")
    private List<String>        utilTestList2;

    //---------------------------------------------------------------

    @Test
    public void test(){
        LOGGER.debug(JsonUtil.format(utilTestList1));
        LOGGER.debug(JsonUtil.format(utilTestList2));
    }
}