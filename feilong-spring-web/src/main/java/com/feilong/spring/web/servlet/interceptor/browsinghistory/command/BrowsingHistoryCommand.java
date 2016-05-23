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

import java.io.Serializable;

/**
 * 目前只支持主键,以后视情况而定吧看看要不要把价格等信息放这里.
 *
 * @author feilong
 * @version 1.2.2 2015年7月19日 下午11:29:32
 * @version 1.5.5 2016年5月22日 03:21:14
 * @since 1.2.2
 */
public interface BrowsingHistoryCommand extends Serializable{

    /**
     * 获得 id.
     *
     * @return the id
     */
    Long getId();

    /**
     * 设置 id.
     *
     * @param id
     *            the id
     */
    void setId(Long id);
}