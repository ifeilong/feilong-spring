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
package com.feilong.spring.web.event.builder;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;

/**
 * The Class MappedInterceptorBeanToMapBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.5
 */
public class MappedInterceptorBeanToMapBuilder implements BeanToMapBuilder<MappedInterceptor>{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MappedInterceptorBeanToMapBuilder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.event.builder.EntryToMapBuilder#build(java.lang.String, java.lang.Object)
     */
    @Override
    public Map<String, Object> build(String beanName,MappedInterceptor mappedInterceptor){
        HandlerInterceptor handlerInterceptor = mappedInterceptor.getInterceptor();
        String[] pathPatterns = mappedInterceptor.getPathPatterns();

        Object readField = getExcludePatterns(mappedInterceptor);

        //---------------------------------------------------------------
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("handlerInterceptor", handlerInterceptor.getClass().getName());
        map.put("includePatterns", pathPatterns);
        map.put("excludePatterns", readField);
        map.put("beanName", beanName);
        return map;
    }

    /**
     * Gets the exclude patterns.
     *
     * @param mappedInterceptor
     *            the mapped interceptor
     * @return the exclude patterns
     */
    private Object getExcludePatterns(MappedInterceptor mappedInterceptor){
        try{
            return FieldUtils.readField(mappedInterceptor, "excludePatterns", true);
        }catch (IllegalAccessException e){
            LOGGER.error("", e);
        }
        return null;
    }
}
