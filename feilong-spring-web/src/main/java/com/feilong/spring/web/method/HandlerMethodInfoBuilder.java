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
package com.feilong.spring.web.method;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.bean.ToStringConfig;
import com.feilong.core.lang.annotation.AnnotationToStringBuilder;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
class HandlerMethodInfoBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerMethodInfoBuilder.class);

    static Map<String, Object> build(
                    RequestMappingInfo requestMappingInfo,
                    HandlerMethod handlerMethod,
                    Map<Class<Annotation>, AnnotationToStringBuilder<Annotation>> annotationAndAnnotationToStringBuilderMap){
        Map<String, Object> keyAndValueMap = new LinkedHashMap<>();

        putRequestMappingInfo(keyAndValueMap, requestMappingInfo);
        putAnnotationInfo(keyAndValueMap, handlerMethod, annotationAndAnnotationToStringBuilderMap);

        return keyAndValueMap;
    }

    /**
     * @param keyAndValueMap
     * @param requestMappingInfo
     */
    private static void putRequestMappingInfo(Map<String, Object> keyAndValueMap,RequestMappingInfo requestMappingInfo){
        PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
        RequestMethodsRequestCondition requestMethodsRequestCondition = requestMappingInfo.getMethodsCondition();
        HeadersRequestCondition headersRequestCondition = requestMappingInfo.getHeadersCondition();

        keyAndValueMap.put("url", ConvertUtil.toString(patternsRequestCondition.getPatterns(), ToStringConfig.DEFAULT_CONFIG));
        keyAndValueMap.put("method", ConvertUtil.toString(requestMethodsRequestCondition.getMethods(), ToStringConfig.DEFAULT_CONFIG));
        keyAndValueMap.put("header", ConvertUtil.toString(headersRequestCondition.getExpressions(), ToStringConfig.DEFAULT_CONFIG));
    }

    /**
     * @param keyAndValueMap
     * @param handlerMethod
     * @param annotationAndAnnotationToStringBuilderMap
     */
    private static void putAnnotationInfo(
                    Map<String, Object> keyAndValueMap,
                    HandlerMethod handlerMethod,
                    Map<Class<Annotation>, AnnotationToStringBuilder<Annotation>> annotationAndAnnotationToStringBuilderMap){

        for (Map.Entry<Class<Annotation>, AnnotationToStringBuilder<Annotation>> annotationEntry : annotationAndAnnotationToStringBuilderMap
                        .entrySet()){
            Class<Annotation> annotationClass = annotationEntry.getKey();
            AnnotationToStringBuilder<Annotation> annotationToStringBuilder = annotationEntry.getValue();

            //如果没有标识{@link ClientCache}
            Annotation annotation = handlerMethod.getMethodAnnotation(annotationClass);

            keyAndValueMap.put(annotationClass.getSimpleName(), annotationToStringBuilder.build(annotation));
        }
    }
}
