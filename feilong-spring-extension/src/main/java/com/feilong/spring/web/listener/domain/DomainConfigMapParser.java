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
package com.feilong.spring.web.listener.domain;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;
import java.util.Properties;

/**
 * The Class DomainConfigMapParser.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.3
 */
class DomainConfigMapParser{

    /** Don't let anyone instantiate this class. */
    private DomainConfigMapParser(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Properties to map 将全部的配置转成map key就是properties中的key.
     *
     * @param properties
     *            the properties
     * @param defaultValue
     *            如果 配置的value是空,则使用 contextPath
     * @return the map< string, domain config>
     * @since 1.0.9
     */
    static Map<String, DomainConfig> parse(Properties properties,String defaultValue){
        Map<String, String> map = toMap(properties);

        Map<String, DomainConfig> domainConfigMap = newLinkedHashMap(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();

            domainConfigMap.put(key, DomainConfigBuilder.build(key, value, defaultValue));
        }
        return domainConfigMap;
    }
}
