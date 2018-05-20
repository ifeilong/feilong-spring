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

import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * {@link HandlerExceptionResolver} 信息提取.
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
14:28:16 INFO  (ContextRefreshedBeanLoggingEventListener.java:134) doLog() - [interface org.springframework.web.servlet.HandlerExceptionResolver] list size:[5], Info:
beanName                                                                                  name                                                                                    order      
----------------------------------------------------------------------------------------- --------------------------------------------------------------------------------------- ---------- 
defaultHandlerExceptionResolver                                                           org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver             2147483647 
org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver#0          org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver          1          
org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver#0 org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver 0          
org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver#0             org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver             2          
simpleMappingHandlerMethodExceptionResolver                                               com.feilong.spring.web.servlet.handler.SimpleMappingHandlerMethodExceptionResolver      1
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
    <!-- 启动的时候,显示 HandlerExceptionResolver 信息 -->
    <bean id="contextRefreshedHandlerExceptionResolverEventListener" class=
"com.feilong.spring.web.event.ContextRefreshedBeanLoggingEventListener">
        <property name="beanClass" value="org.springframework.web.servlet.HandlerExceptionResolver" />
        <property name="beanToMapBuilder">
            <bean class="com.feilong.spring.web.event.builder.HandlerExceptionResolverBeanToMapBuilder" />
        </property>
    </bean>
}
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.4
 */
public class HandlerExceptionResolverBeanToMapBuilder implements BeanToMapBuilder<HandlerExceptionResolver>{

    /** Static instance. */
    // the static instance works for all types
    public static final BeanToMapBuilder<HandlerExceptionResolver> INSTANCE = new HandlerExceptionResolverBeanToMapBuilder();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.event.builder.BeanToMapBuilder#build(java.lang.String, java.lang.Object)
     */
    @Override
    public Map<String, Object> build(String beanName,HandlerExceptionResolver handlerExceptionResolver){

        Map<String, Object> map = newLinkedHashMap();
        map.put("beanName", beanName);
        map.put("name", handlerExceptionResolver.getClass().getName());
        map.put(
                        "order",
                        handlerExceptionResolver instanceof Ordered ? ((Ordered) handlerExceptionResolver).getOrder()
                                        : Ordered.LOWEST_PRECEDENCE);

        return map;
    }
}
