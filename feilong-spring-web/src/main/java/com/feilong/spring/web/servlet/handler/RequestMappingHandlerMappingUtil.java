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
package com.feilong.spring.web.servlet.handler;

import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static java.util.Collections.emptyMap;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.feilong.core.util.MapUtil;

/**
 * RequestMappingHandlerMapping工具类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.1.0
 */
public class RequestMappingHandlerMappingUtil{

    /**
     * 获取指定的RequestMappingHandlerMapping.
     * 
     * @param applicationContext
     * @return
     */
    public static RequestMappingHandlerMapping getRequestMappingHandlerMapping(ApplicationContext applicationContext){
        //see https://github.com/ifeilong/feilong-spring/issues/229
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
                        .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);//通过上下文对象获取RequestMappingHandlerMapping实例对象  
        return requestMappingHandlerMapping;
    }

    /**
     * 支持多个.
     * 
     * <p>
     * Field requestMappingInfoHandlerMapping in com.example.getallcontrollerpath.GetAllControllerPathApplication required a single bean,
     * but 4 were found:
     * </p>
     * 
     * - requestMappingHandlerMapping: defined by method 'requestMappingHandlerMapping' in class path resource
     * [org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$EnableWebMvcConfiguration.class] <br>
     * <br>
     * 
     * - healthEndpointWebMvcHandlerMapping: defined by method 'healthEndpointWebMvcHandlerMapping' in class path resource
     * [org/springframework/boot/actuate/autoconfigure/health/HealthEndpointWebExtensionConfiguration$MvcAdditionalHealthEndpointPathsConfiguration.class]<br>
     * <br>
     * 
     * - webEndpointServletHandlerMapping: defined by method 'webEndpointServletHandlerMapping' in class path resource
     * [org/springframework/boot/actuate/autoconfigure/endpoint/web/servlet/WebMvcEndpointManagementContextConfiguration.class]<br>
     * <br>
     * 
     * - controllerEndpointHandlerMapping: defined by method 'controllerEndpointHandlerMapping' in class path resource
     * [org/springframework/boot/actuate/autoconfigure/endpoint/web/servlet/WebMvcEndpointManagementContextConfiguration.class]
     * 
     * 
     * @param applicationContext
     * @return
     */
    public static Map<String, RequestMappingHandlerMapping> getRequestMappingHandlerMappingMap(ApplicationContext applicationContext){
        Map<String, RequestMappingHandlerMapping> map = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);
        return map;
    }

    /**
     * Builds the handler methods.
     *
     * @param applicationContext
     *            the application context
     * @return 如果取不到 <code>RequestMappingHandlerMapping</code>,返回 {@link Collections#emptyMap()}<br>
     * @throws BeansException
     *             the beans exception
     */
    public static Map<RequestMappingInfo, HandlerMethod> buildHandlerMethods(ApplicationContext applicationContext){
        Map<String, RequestMappingHandlerMapping> requestMappingHandlerMappingMap = getRequestMappingHandlerMappingMap(applicationContext);
        if (null == requestMappingHandlerMappingMap){
            return emptyMap();
        }

        //---------------------------------------------------------------
        Map<RequestMappingInfo, HandlerMethod> returnMap = newLinkedHashMap();

        for (Map.Entry<String, RequestMappingHandlerMapping> entry : requestMappingHandlerMappingMap.entrySet()){
            RequestMappingHandlerMapping requestMappingHandlerMapping = entry.getValue();
            MapUtil.putAllIfNotNull(returnMap, requestMappingHandlerMapping.getHandlerMethods());
        }
        return returnMap;
    }
}
