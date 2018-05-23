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
package com.feilong.spring.web.servlet.interceptor.browsinghistory.command;

/**
 * 目前只支持主键,以后视情况而定吧看看要不要把价格等信息放这里.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.5
 */
public class DefaultBrowsingHistoryCommand implements BrowsingHistoryCommand{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2860276539206053398L;

    /** The id. */
    private Long              id;

    /**
     * 获得 the id.
     *
     * @return the id
     */
    @Override
    public Long getId(){
        return id;
    }

    /**
     * 设置 the id.
     *
     * @param id
     *            the id to set
     */
    @Override
    public void setId(Long id){
        this.id = id;
    }

}