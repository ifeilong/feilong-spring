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
package com.feilong.entity;

import com.feilong.store.member.User;

public class ConstructorListBeanEntity{

    /** The name. */
    private User[] users;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     *
     * @param users
     *            the users
     */
    public ConstructorListBeanEntity(User...users){
        this.users = users;
    }

    //---------------------------------------------------------------
    /**
     * 获得 name.
     *
     * @return the users
     */
    public User[] getUsers(){
        return users;
    }

    /**
     * 设置 name.
     *
     * @param users
     *            the users to set
     */
    public void setUsers(User[] users){
        this.users = users;
    }

}
