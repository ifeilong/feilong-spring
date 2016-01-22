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
package com.feilong.spring.expression;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SpelUtilTest.
 *
 * @author feilong
 * @version 1.0.8 2014年10月8日 上午11:21:00
 * @since 1.0.8
 */
public class SpelUtilTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpelUtilTest.class);

    /**
     * 获得 value.
     */
    @Test
    public void getValue(){
        String ex = "'Hello,World'";
        LOGGER.info("" + SpelUtil.getValue(ex));
        LOGGER.info("" + SpelUtil.getValue(ex + ".length()"));
        LOGGER.info("" + SpelUtil.getValue(ex + ".concat('!')"));
        LOGGER.info("" + SpelUtil.getValue(ex + ".class"));
        LOGGER.info("" + SpelUtil.getValue(ex + ".bytes.length"));
        LOGGER.info("" + SpelUtil.getValue("new String(" + ex + ").toUpperCase()"));
    }

    @Test
    public void getValue1(){
        System.setProperty("feilong.site", "hongkong");
        LOGGER.info("" + SpelUtil.getValue("#systemProperties['feilong.site']"));
        //LOGGER.info("" + SpelUtil.getValue("${feilong.site}=='china'?'CHINA':(${feilong.site}=='hongkong'?'HONGKONG':'TAIWANG')"));
    }
}
