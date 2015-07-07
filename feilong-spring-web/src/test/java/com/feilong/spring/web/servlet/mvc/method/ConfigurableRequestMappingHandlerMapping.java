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
package com.feilong.spring.web.servlet.mvc.method;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 *
 * @author feilong
 * @version 1.2.1 2015年6月14日 下午8:14:04
 * @since 1.2.1
 */
public class ConfigurableRequestMappingHandlerMapping extends RequestMappingHandlerMapping{

    private static final Logger        LOGGER = LoggerFactory.getLogger(ConfigurableRequestMappingHandlerMapping.class);

    private List<RequestMappingConfig> requestMappingConfigList;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#initHandlerMethods()
     */
    @Override
    protected void initHandlerMethods(){
        super.initHandlerMethods();

        // getApplicationContext().getBean(name)

        //        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(serviceClass);
        //        beanDefinitionBuilder.addPropertyValue("servicename", service.getServiceName());
        //
        //        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        //        BeanDefinitionRegistry beanDefinitonRegistry = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
        //        beanDefinitonRegistry.registerBeanDefinition("", beanDefinitionBuilder.getRawBeanDefinition());
        //
        //        //
        //        Class<?> handlerType = (handler instanceof String ? getApplicationContext().getType((String) handler) : handler.getClass());
        //
        //        // Avoid repeated calls to getMappingForMethod which would rebuild RequestMatchingInfo instances
        //        final Map<Method, RequestMappingInfo> mappings = new IdentityHashMap<Method, RequestMappingInfo>();
        //        final Class<?> userType = ClassUtils.getUserClass(handlerType);
        //
        //        Set<Method> methods = HandlerMethodSelector.selectMethods(userType, new MethodFilter(){
        //
        //            @Override
        //            public boolean matches(Method method){
        //                RequestMappingInfo mapping = getMappingForMethod(method, userType);
        //                if (mapping != null){
        //                    mappings.put(method, mapping);
        //                    return true;
        //                }else{
        //                    return false;
        //                }
        //            }
        //        });
        //
        //        for (Method method : methods){
        //            registerHandlerMethod(handler, method, mappings.get(method));
        //        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#isHandler(java.lang.Class)
     */
    @Override
    protected boolean isHandler(Class<?> beanType){
        boolean handler = super.isHandler(beanType);
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("beanType:[{}],isHandler:[{}]", beanType.getCanonicalName(), handler);
        }
        return handler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#registerHandlerMethod(java.lang.Object,
     * java.lang.reflect.Method, java.lang.Object)
     */
    @Override
    protected void registerHandlerMethod(Object handler,Method method,RequestMappingInfo mapping){
        super.registerHandlerMethod(handler, method, mapping);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#detectHandlerMethods(java.lang.Object)
     */
    @Override
    protected void detectHandlerMethods(Object handler){
        super.detectHandlerMethods(handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#getMappingForMethod(java.lang.reflect.Method,
     * java.lang.Class)
     */
    @Override
    protected RequestMappingInfo getMappingForMethod(Method method,Class<?> handlerType){
        return super.getMappingForMethod(method, handlerType);
    }

    /**
     * @param requestMappingConfigList
     *            the requestMappingConfigList to set
     */
    public void setRequestMappingConfigList(List<RequestMappingConfig> requestMappingConfigList){
        this.requestMappingConfigList = requestMappingConfigList;
    }
}
