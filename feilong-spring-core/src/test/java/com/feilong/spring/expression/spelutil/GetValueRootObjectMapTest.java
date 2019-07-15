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
package com.feilong.spring.expression.spelutil;

import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.spring.expression.SpelUtil;

public class GetValueRootObjectMapTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(GetValueRootObjectMapTest.class);

    @Test
    public void getValueMap(){
        Map<String, Object> map = newLinkedHashMap();
        map.put("logisticsStatus", 10);

        //---------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("" + SpelUtil.getValue("logisticsStatus", map));
        }

    }
}
