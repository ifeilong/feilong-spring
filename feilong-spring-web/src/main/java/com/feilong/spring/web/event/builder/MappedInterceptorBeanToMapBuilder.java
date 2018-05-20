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

import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;

/**
 * {@link HandlerInterceptor} 相关信息转成map.
 * 
 * <h3>作用:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 可以在日志文件或者控制台输出如下信息:
 * </p>
 * 
 * <pre class="code">

14:28:16 INFO  (ContextRefreshedBeanLoggingEventListener.java:134) doLog() - [class org.springframework.web.servlet.handler.MappedInterceptor] list size:[7], Info:
handlerInterceptor                                                             includePatterns excludePatterns beanName                                                    
------------------------------------------------------------------------------ --------------- --------------- ----------------------------------------------------------- 
com.feilong.spring.web.servlet.interceptor.monitor.MonitorInterceptor          /**                             org.springframework.web.servlet.handler.MappedInterceptor#0 
org.springframework.mobile.device.DeviceResolverHandlerInterceptor                                             org.springframework.web.servlet.handler.MappedInterceptor#1 
org.springframework.mobile.device.site.SitePreferenceHandlerInterceptor                                        org.springframework.web.servlet.handler.MappedInterceptor#2 
org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor                                   org.springframework.web.servlet.handler.MappedInterceptor#3 
com.baozun.store.web.interceptor.StoreItemBrowsingHistoryInterceptor           /item/*                         org.springframework.web.servlet.handler.MappedInterceptor#4 
com.feilong.spring.web.servlet.interceptor.i18n.SupportLocaleChangeInterceptor                                 org.springframework.web.servlet.handler.MappedInterceptor#5 
com.feilong.spring.web.servlet.interceptor.clientcache.ClientCacheInterceptor  /**                             org.springframework.web.servlet.handler.MappedInterceptor#6
 * 
 * 
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>参考配置:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
{@code 
    <bean id="contextRefreshedMappedInterceptorBeanLoggingEventListener" class=
"com.feilong.spring.web.event.ContextRefreshedBeanLoggingEventListener">
        <property name="beanClass" value="org.springframework.web.servlet.handler.MappedInterceptor" />
        <property name="beanToMapBuilder">
            <bean class="com.feilong.spring.web.event.builder.MappedInterceptorBeanToMapBuilder" />
        </property>
    </bean>
}
 * </pre>
 * 
 * </blockquote>
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
        Map<String, Object> map = newLinkedHashMap();
        map.put("handlerInterceptor", handlerInterceptor.getClass().getName());
        map.put("includePatterns", pathPatterns);
        map.put("excludePatterns", readField);
        map.put("beanName", beanName);
        return map;
    }

    //---------------------------------------------------------------

    /**
     * Gets the exclude patterns.
     *
     * @param mappedInterceptor
     *            the mapped interceptor
     * @return the exclude patterns
     */
    private static Object getExcludePatterns(MappedInterceptor mappedInterceptor){
        try{
            return FieldUtils.readField(mappedInterceptor, "excludePatterns", true);
        }catch (IllegalAccessException e){
            LOGGER.error("", e);
        }
        return null;
    }
}
