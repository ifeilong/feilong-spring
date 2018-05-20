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

import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.SortUtil.sortListByPropertyNamesValue;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.feilong.core.lang.annotation.AnnotationToStringBuilder;
import com.feilong.spring.web.event.builder.HandlerMethodInfoExtractor;

/**
 * 启动的时候显示 handler method 信息.
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
14:28:16 INFO  (AbstractContextRefreshedHandlerMethodLogginEventListener.java:137) render() - handler method ,size:[13],info:
url                                                                method   header ClientCache 
------------------------------------------------------------------ -------- ------ ----------- 
/c{categoryCode}-{name}                                                                        
/c{categoryCode}/m{material}-c{color}-s{size}-k{kind}-s{style}.htm                             
/errors/404400                                                                                 
/errors/500                                                                                    
/exceptiontest                                                                                 
/index,/,/index.htm                                                GET                         
/item/{itemid}                                                     GET             5分钟         
/jsp/{category}/{page}                                             GET,POST                    
/jsp/{category}/{page}/{small}                                     GET,POST                    
/requestbodytest                                                                               
/{c1}-{c2}-{c3}-{c4}-{label1}-{label2}-{label3}.htm                                            
/{c1}-{c2}-{c3}-{c4}-{label}.htm                                                               
/{c1}-{c2}-{c3}-{c4}.htm
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
    <!-- 启动的时候,显示 路径 method等 信息 -->
    <bean id="contextRefreshedHandlerMethodInfoEventListener" class=
"com.feilong.spring.web.event.ContextRefreshedHandlerMethodInfoEventListener">
        <property name="annotationAndAnnotationToStringBuilderMap">
            <map>
                <entry key="com.feilong.spring.web.servlet.interceptor.clientcache.ClientCache">
                    <bean class="com.feilong.spring.web.servlet.interceptor.clientcache.ClientCacheToStringBuilder" />
                </entry>
            </map>
        </property>
    </bean>
}
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.context.event.SmartApplicationListener
 * @since 1.10.4
 */
public class ContextRefreshedHandlerMethodInfoEventListener extends AbstractContextRefreshedHandlerMethodLogginEventListener{

    /** The annotation and annotation to string builder map. */
    private Map<Class<Annotation>, AnnotationToStringBuilder<Annotation>> annotationAndAnnotationToStringBuilderMap;

    //---------------------------------------------------------------

    /**
     * Builds the list.
     *
     * @param handlerMethods
     *            the handler methods
     * @return the list
     */
    @Override
    protected List<Map<String, Object>> buildList(Map<RequestMappingInfo, HandlerMethod> handlerMethods){
        List<Map<String, Object>> list = newArrayList();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()){
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();

            list.add(HandlerMethodInfoExtractor.extract(requestMappingInfo, handlerMethod, annotationAndAnnotationToStringBuilderMap));
        }
        return sortDataList(list);
    }

    //---------------------------------------------------------------

    /**
     * 对 list map 进行排序.
     *
     * @param list
     *            the list
     * @return the list
     * @since 1.10.5
     */
    private static List<Map<String, Object>> sortDataList(List<Map<String, Object>> list){
        return sortListByPropertyNamesValue(list, "url");
    }

    //---------------------------------------------------------------

    /**
     * 设置 annotation and annotation to string builder map.
     *
     * @param annotationAndAnnotationToStringBuilderMap
     *            the annotationAndAnnotationToStringBuilderMap to set
     */
    public void setAnnotationAndAnnotationToStringBuilderMap(
                    Map<Class<Annotation>, AnnotationToStringBuilder<Annotation>> annotationAndAnnotationToStringBuilderMap){
        this.annotationAndAnnotationToStringBuilderMap = annotationAndAnnotationToStringBuilderMap;
    }
}
