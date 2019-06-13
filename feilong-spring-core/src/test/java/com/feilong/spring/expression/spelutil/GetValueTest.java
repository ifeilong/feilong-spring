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

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import com.feilong.spring.expression.SpelUtil;

public class GetValueTest{

    /**
     * 获得 value.
     */
    @Test
    public void getValue(){
        String ex = "'Hello,World'";

        assertEquals("Hello,World", SpelUtil.getValue(ex));
        assertEquals(11, SpelUtil.getValue(ex + ".length()"));
        assertEquals("Hello,World!", SpelUtil.getValue(ex + ".concat('!')"));
        assertEquals(String.class, SpelUtil.getValue(ex + ".class"));
        assertEquals(11, SpelUtil.getValue(ex + ".bytes.length"));
        assertEquals("HELLO,WORLD", SpelUtil.getValue("new String(" + ex + ").toUpperCase()"));
    }

    //---------------------------------------------------------------

    @Test
    @Ignore
    public void getValue1(){
        String value = "hongkong";
        System.setProperty("feilong.site", value);

        assertEquals(value, SpelUtil.getValue("#systemProperties['feilong.site']"));
        //assertEquals("" + SpelUtil.getValue("${feilong.site}=='china'?'CHINA':(${feilong.site}=='hongkong'?'HONGKONG':'TAIWAN')"));
    }

    @Test
    public void getValue2(){
        String expressionString = "T(com.feilong.core.lang.StringUtil).tokenizeToStringArray('xin,jin',',')";

        String[] values = SpelUtil.getValue(expressionString);
        assertThat(toList(values), allOf(hasItem("xin"), hasItem("jin")));
    }

    //---------------------------------------------------------------

}
