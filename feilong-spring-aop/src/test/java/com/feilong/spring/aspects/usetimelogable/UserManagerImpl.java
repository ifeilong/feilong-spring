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
package com.feilong.spring.aspects.usetimelogable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.spring.aop.log.Log;
import com.feilong.spring.aop.log.UseTimeLogable;

/**
 * The Class UserManagerImpl.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class UserManagerImpl implements UserManager,UseTimeLogable{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserManagerImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.aspects.UserManager#addUser(java.lang.String, java.lang.String)
     */
    @Override
    @Log(level = "debug")
    public void addUser(String id,String name){
        LOGGER.info("---UsreManagerImpl中的addUser方法的实现-----");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.aspects.UserManager#delUser(int)
     */
    @Override
    public void delUser(int id){
        LOGGER.info("-----UserManagerImpl delUser方法的实现-----");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.aspects.UserManager#modifyUser(int, java.lang.String, int)
     */
    @Override
    public void modifyUser(int id,String name,int age){
        LOGGER.info("----UserManagerImpl modifyUser方法的实现-----");
    }
}
