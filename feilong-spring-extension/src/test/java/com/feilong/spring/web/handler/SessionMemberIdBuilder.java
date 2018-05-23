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
package com.feilong.spring.web.handler;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 * @since 1.11.4
 */
public class SessionMemberIdBuilder<T> implements MemberIdBuilder<T>{

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionMemberIdBuilder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.handler.LoginMemberIdBuilder#build(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public T build(HttpServletRequest request){
        // TODO Auto-generated method stub
        return null;
    }
}
