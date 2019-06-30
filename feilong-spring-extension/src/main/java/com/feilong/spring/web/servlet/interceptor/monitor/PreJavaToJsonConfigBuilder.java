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
package com.feilong.spring.web.servlet.interceptor.monitor;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JavaToJsonConfig;
import com.feilong.json.jsonlib.processor.SensitiveWordsJsonValueProcessor;

import net.sf.json.processors.JsonValueProcessor;

/**
 * The Class PreJavaToJsonConfigBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.7
 */
public class PreJavaToJsonConfigBuilder{

    /** The Constant log. */
    private static final Logger     LOGGER           = LoggerFactory.getLogger(PreJavaToJsonConfigBuilder.class);

    //---------------------------------------------------------------

    /** The javatojsonconfig. */
    private static JavaToJsonConfig JAVATOJSONCONFIG = null;

    //---------------------------------------------------------------

    /**
     * Builds the java to json config.
     *
     * @param monitorMessageEntity
     *            the monitor message entity
     * @return the java to json config
     */
    public static JavaToJsonConfig build(MonitorMessageEntity monitorMessageEntity){
        if (null != JAVATOJSONCONFIG){
            return JAVATOJSONCONFIG;
        }

        //TODO lock

        JAVATOJSONCONFIG = buildExcute(monitorMessageEntity);
        return JAVATOJSONCONFIG;
    }

    //---------------------------------------------------------------

    /**
     * Builds the excute.
     *
     * @param monitorMessageEntity
     *            the monitor message entity
     * @return the java to json config
     */
    private static JavaToJsonConfig buildExcute(MonitorMessageEntity monitorMessageEntity){
        List<String> sensitiveRequestParamNameList = monitorMessageEntity.getSensitiveRequestParamNameList();
        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("will build JavaToJsonConfig,use sensitiveRequestParamNameList:{}", sensitiveRequestParamNameList);
        }

        String[] ignoreRequestParamNames = monitorMessageEntity.getIgnoreRequestParamNames();

        //---------------------------------------------------------------
        //都是 NullOrEmpty
        if (isNullOrEmpty(sensitiveRequestParamNameList) && isNullOrEmpty(ignoreRequestParamNames)){
            return new JavaToJsonConfig();
        }

        //---------------------------------------------------------------
        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
        javaToJsonConfig.setPropertyNameAndJsonValueProcessorMap(buildPropertyNameAndJsonValueProcessorMap(sensitiveRequestParamNameList));
        javaToJsonConfig.setExcludes(ignoreRequestParamNames);
        return javaToJsonConfig;
    }

    //---------------------------------------------------------------

    /**
     * Builds the property name and json value processor map.
     *
     * @param sensitiveRequestParamNameList
     *            the sensitive request param name list
     * @return the map
     * @since 4.0.6
     */
    static Map<String, JsonValueProcessor> buildPropertyNameAndJsonValueProcessorMap(List<String> sensitiveRequestParamNameList){
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = newHashMap(sensitiveRequestParamNameList.size());
        for (String sensitiveRequestParamName : sensitiveRequestParamNameList){
            propertyNameAndJsonValueProcessorMap.put(sensitiveRequestParamName, SensitiveWordsJsonValueProcessor.INSTANCE);
        }
        return propertyNameAndJsonValueProcessorMap;
    }

}
