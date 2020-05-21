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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.JsonUtil;

/**
 * {@link DomainConfig} 构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.3
 */
class DomainConfigBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainConfigBuilder.class);

    /** Don't let anyone instantiate this class. */
    private DomainConfigBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the domain config.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @param defaultValue
     *            the default value
     * @return the domain config
     */
    static DomainConfig build(String key,String value,String defaultValue){
        DomainConfig domainConfig = new DomainConfig();
        domainConfig.setKey(key);

        if (isNotNullOrEmpty(value)){
            if (value.startsWith("{")){//json
                domainConfig = JsonUtil.toBean(value, DomainConfig.class);
            }else{
                domainConfig.setValue(value); //不是 json 那么直接设置值
            }
        }else{
            //nothing to do
        }

        //---------------------------------------------------------------

        reworkValue(key, defaultValue, domainConfig);
        return domainConfig;
    }

    //---------------------------------------------------------------
    /**
     * Rework value.
     *
     * @param key
     *            the key
     * @param defaultValue
     *            the default value
     * @param domainConfig
     *            the domain config
     */
    private static void reworkValue(String key,String defaultValue,DomainConfig domainConfig){
        if (isNullOrEmpty(domainConfig.getValue())){
            LOGGER.debug("key:[{}] 's value is mullOrEmpty,use ContextPath:[{}]", key, defaultValue);
            domainConfig.setValue(defaultValue);
        }
    }
}
