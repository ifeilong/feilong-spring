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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.spring.expression.SpelUtil;
import com.feilong.store.member.Member;
import com.feilong.store.order.SalesOrder;

public class GetValueRootObjectTest{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(GetValueRootObjectTest.class);

    //---------------------------------------------------------------
    @Test
    public void getValue4(){
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setLogisticsStatus(10);

        //---------------------------------------------------------------
        assertEquals(10, SpelUtil.getValue("logisticsStatus", salesOrder));
    }

    @Test
    public void getValue44(){
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setLogisticsStatus(10);

        assertEquals("系统超时取消", SpelUtil.getValue("logisticsStatus==10?\"系统超时取消\":\"用户主动取消\"", salesOrder));
    }

    //---------------------------------------------------------------

    @Test
    public void getValue2(){
        String expressionString = "T(com.feilong.core.lang.StringUtil).tokenizeToStringArray('xin,jin',',')";

        String[] values = SpelUtil.getValue(expressionString, null);
        assertThat(toList(values), allOf(hasItem("xin"), hasItem("jin")));
    }

    @Test
    public void getValue222(){
        String expressionString = "T(com.feilong.core.lang.StringUtil).tokenizeToStringArray('xin,jin',',')";

        String[] values = SpelUtil.getValue(expressionString, new Member());
        assertThat(toList(values), allOf(hasItem("xin"), hasItem("jin")));
    }
}
