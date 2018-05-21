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
package com.feilong.spring;

import java.util.Map;

import org.apache.commons.lang3.Validate;

import com.feilong.core.lang.reflect.FieldUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * bean message 信息构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.4
 */
public final class BeanLogMessageBuilder{

    /** Don't let anyone instantiate this class. */
    private BeanLogMessageBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 生成对象字段相关日志信息.
     *
     * @param obj
     *            the obj
     * @return 如果 <code>obj</code> 是null,抛出 {@link NullPointerException}<br>
     */
    public static String buildFieldsMessage(Object obj){
        Validate.notNull(obj, "obj can't be null!");

        Map<String, Object> map = FieldUtil.getAllFieldNameAndValueMap(obj);
        return Slf4jUtil.format("[{}] field's value map:\n[{}]", obj.getClass().getCanonicalName(), JsonUtil.format(map));
    }
}
