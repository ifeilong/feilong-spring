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
package com.feilong.constructor;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.feilong.entity.ConstructorArgsEntity;

@ContextConfiguration(locations = { "classpath:spring-DI-ConstructorArgs.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringConstructorArgsTest{

    @Autowired
    private ConstructorArgsEntity constructorArgsEntity;

    @Test
    public void test(){
        String[] names = constructorArgsEntity.getNames();
        assertThat(
                        toList(names),
                        allOf(
                                        hasItem("http://10.12.38.41:8080/solr4/adidas_pro"),
                                        hasItem("http://10.12.38.42:8080/solr4/adidas_pro"),
                                        hasItem("http://10.12.38.43:8080/solr4/adidas_pro"),
                                        hasItem("http://10.12.38.43:8080/solr4/adidas_pro")));
    }
}
