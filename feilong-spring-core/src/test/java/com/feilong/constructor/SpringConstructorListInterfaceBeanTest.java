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

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.feilong.entity.ConstructorListInterfaceBeanEntity;
import com.feilong.entity.InterfaceUser;

@ContextConfiguration(locations = { "classpath:spring-DI-constructor-list-interface-bean.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringConstructorListInterfaceBeanTest{

    @Autowired
    private ConstructorListInterfaceBeanEntity constructorListInterfaceBeanEntity;

    //---------------------------------------------------------------

    @Autowired
    @Qualifier("user1")
    private InterfaceUser                      user1;

    @Autowired
    @Qualifier("user2")
    private InterfaceUser                      user2;

    //---------------------------------------------------------------
    @Test
    public void test(){
        InterfaceUser[] users = constructorListInterfaceBeanEntity.getUsers();
        assertThat(users, allOf(hasItemInArray(user1), hasItemInArray(user2)));
    }
}
