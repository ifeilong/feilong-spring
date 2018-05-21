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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.spring.BeanLogMessageBuilder;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class DiPropertiesManager extends AbstractManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(DiPropertiesManager.class);

    private Properties          properties;

    //    @PostConstruct
    //    protected void postConstruct(){
    //        if (LOGGER.isInfoEnabled()){
    //            Map<String, Object> map = FieldUtil.getAllFieldNameAndValueMap(this);
    //            LOGGER.info("\n[{}] fieldValueMap: \n[{}]", getClass().getCanonicalName(), JsonUtil.format(map));
    //        }
    //    }

    /** Post construct. */
    @PostConstruct
    protected void postConstruct1(){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(BeanLogMessageBuilder.buildFieldsMessage(this));
        }
    }

    public void print(){
        //LOGGER.debug(JsonUtil.format(properties));
    }

    /**
     * @param properties
     *            the properties to set
     */
    public void setProperties(Properties properties){
        this.properties = properties;
    }

}
