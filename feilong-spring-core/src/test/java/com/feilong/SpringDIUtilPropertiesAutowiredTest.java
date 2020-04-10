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
package com.feilong;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

import java.util.Properties;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration("classpath:spring-DI-util-properties.xml")
public class SpringDIUtilPropertiesAutowiredTest extends AbstractJUnit4SpringContextTests{

    @Autowired(required = false)
    @Qualifier("p_AlipayAdvance")
    private Properties properties;

    @Autowired(required = false)
    @Qualifier("p_AlipayAdvance_Append")
    private Properties propertiesAppend;

    @Autowired(required = false)
    @Qualifier("p_AlipayAdvance_PropertiesFactoryBean")
    private Properties propertiesPropertiesFactoryBean;

    //---------------------------------------------------------------

    @Test
    public void test1222(){
        assertThat(
                        toMap(propertiesPropertiesFactoryBean, String.class, String.class),
                        allOf(//
                                        hasEntry(
                                                        "solr.solrServerUrl",
                                                        "http://10.12.38.41:8080/solr4/adidas_pro http://10.12.38.42:8080/solr4/adidas_pro , http://10.12.38.43:8080/solr4/adidas_pro; ;http://10.12.38.43:8080/solr4/adidas_pro"),
                                        hasEntry("skills", "豪杰,远射;神速 水练"),
                                        hasEntry("name", "关羽")

                        )

        );
    }

    @Test
    public void test1(){
        assertThat(
                        toMap(propertiesAppend, String.class, String.class),
                        allOf(
                                        hasEntry("service.close_trade", "close_trade"),
                                        hasEntry("_input_charset", "UTF-8"),
                                        hasEntry("service.single_trade_query", "single_trade_query")));
    }

    @Test
    public void test(){
        assertThat(
                        toMap(properties, String.class, String.class),
                        allOf(
                                        hasEntry("service.close_trade", "close_trade"),
                                        hasEntry("_input_charset", "UTF-8"),
                                        hasEntry("service.single_trade_query", "single_trade_query")));
    }
}