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
package com.feilong.spring.web.event;

import static com.feilong.core.bean.ToStringConfig.DEFAULT_CONFIG;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.lang.annotation.AnnotationToStringBuilder;

/**
 * HandlerMethodInfo 信息提取.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
class HandlerMethodInfoExtractor{

    /**
     * 提取.
     *
     * @param requestMappingInfo
     *            the request mapping info
     * @param handlerMethod
     *            the handler method
     * @param annotationAndAnnotationToStringBuilderMap
     *            the annotation and annotation to string builder map
     * @return the map
     */
    static Map<String, Object> extract(
                    RequestMappingInfo requestMappingInfo,
                    HandlerMethod handlerMethod,
                    Map<Class<Annotation>, AnnotationToStringBuilder<Annotation>> annotationAndAnnotationToStringBuilderMap){
        Map<String, Object> keyAndValueMap = new LinkedHashMap<>();

        //塞RequestMappingInfo 本身的信息,比如 url ,method,header 等信息进去.
        putRequestMappingInfo(keyAndValueMap, requestMappingInfo);

        //塞配置的要提取的annotaion信息进去.
        putAnnotationInfo(keyAndValueMap, handlerMethod, annotationAndAnnotationToStringBuilderMap);

        return keyAndValueMap;
    }

    /**
     * 塞RequestMappingInfo 本身的信息,比如 url ,method,header 等信息进去.
     *
     * @param keyAndValueMap
     *            the key and value map
     * @param requestMappingInfo
     *            the request mapping info
     */
    private static void putRequestMappingInfo(Map<String, Object> keyAndValueMap,RequestMappingInfo requestMappingInfo){
        PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
        RequestMethodsRequestCondition requestMethodsRequestCondition = requestMappingInfo.getMethodsCondition();
        HeadersRequestCondition headersRequestCondition = requestMappingInfo.getHeadersCondition();

        keyAndValueMap.put("url", ConvertUtil.toString(patternsRequestCondition.getPatterns(), DEFAULT_CONFIG));
        keyAndValueMap.put("method", ConvertUtil.toString(requestMethodsRequestCondition.getMethods(), DEFAULT_CONFIG));
        keyAndValueMap.put("header", ConvertUtil.toString(headersRequestCondition.getExpressions(), DEFAULT_CONFIG));
    }

    /**
     * 塞配置的要提取的annotaion信息进去.
     *
     * @param keyAndValueMap
     *            the key and value map
     * @param handlerMethod
     *            the handler method
     * @param annotationAndAnnotationToStringBuilderMap
     *            the annotation and annotation to string builder map
     */
    private static void putAnnotationInfo(
                    Map<String, Object> keyAndValueMap,
                    HandlerMethod handlerMethod,
                    Map<Class<Annotation>, AnnotationToStringBuilder<Annotation>> annotationAndAnnotationToStringBuilderMap){

        for (Map.Entry<Class<Annotation>, AnnotationToStringBuilder<Annotation>> annotationEntry : annotationAndAnnotationToStringBuilderMap
                        .entrySet()){
            Class<Annotation> annotationClass = annotationEntry.getKey();
            AnnotationToStringBuilder<Annotation> annotationToStringBuilder = annotationEntry.getValue();

            Annotation annotation = handlerMethod.getMethodAnnotation(annotationClass);

            keyAndValueMap.put(annotationClass.getSimpleName(), annotationToStringBuilder.build(annotation));
        }
    }
}
