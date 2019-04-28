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
package com.feilong.spring.namespace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.feilong.context.beanproperty.SimpleHttpTypeBeanProperty;
import com.feilong.context.invoker.http.DefaultHttpRequestBuilder;
import com.feilong.context.invoker.http.HttpRequestBuilder;

/**
 * The Class HttpRequestBuilderParserUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.5
 */
public class HttpRequestBuilderParserUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestBuilderParserUtil.class);

    //        <bean class="com.feilong.context.invoker.http.DefaultHttpRequestBuilder">
    //
    //            <property name="httpTypeBeanProperty">
    //                <bean class="com.feilong.context.beanproperty.SimpleHttpTypeBeanProperty" p:uri="http://hubforeign-test.mapemall.com/toms/inter/tryCaculateForOrder/${routingRequest.routingConstants.routingSecret}"
    //                    p:method="post" />
    //            </property>
    //
    //            <property name="requestBodyBuilder">
    //                <bean class="com.baozun.store.order.RoutingRequestBodyBuilder" />
    //            </property>
    //
    //            <property name="httpRequestRebuilder">
    //                <bean class="com.baozun.store.order.RoutingHttpRequestRebuilder" />
    //            </property>
    //
    //            <property name="requestHeaderBuilder">
    //                <bean class="com.feilong.context.invoker.http.SimpleMapRequestHeaderBuilder">
    //                        <property name="headerMap">
    //                            <map>
    //                                <entry key="Content-Type" value="application/json" />
    //                            </map>
    //                        </property>
    //                </bean>
    //            </property>
    //
    //        </bean>
    //    </property>

    /**
     * Builds the.
     *
     * @param element
     *            the element
     * @return the http request builder
     * @since 4.0.5
     */
    public static HttpRequestBuilder build(Element element){

        String uri = element.getAttribute("uri");
        String method = element.getAttribute("method");

        //---------------------------------------------------------------

        DefaultHttpRequestBuilder httpRequestBuilder = new DefaultHttpRequestBuilder();

        httpRequestBuilder.setHttpTypeBeanProperty(new SimpleHttpTypeBeanProperty(uri, method));

        //        httpRequestBuilder.setRequestParamsBuilder(requestParamsBuilder);
        //        httpRequestBuilder.setRequestBodyBuilder(rebuilder);
        //        httpRequestBuilder.setRequestHeaderBuilder(rebuilder);

        //        httpRequestBuilder.setHttpRequestRebuilder(rebuilder);

        //---------------------------------------------------------------
        return httpRequestBuilder;
    }

}
