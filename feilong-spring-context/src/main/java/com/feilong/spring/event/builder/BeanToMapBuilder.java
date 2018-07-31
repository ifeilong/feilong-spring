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
package com.feilong.spring.event.builder;

import java.util.Map;

/**
 * 将bean信息转成map 数据.
 * 
 * <p>
 * 方便使用 {@link com.feilong.formatter.FormatterUtil#formatToSimpleTable(Iterable)} 来渲染输出数据
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.10.5
 * @since 4.0.0 move from feilong-spring-web
 */
public interface BeanToMapBuilder<T> {

    /**
     * 构造.
     *
     * @param beanName
     *            bean名字
     * @param bean
     *            bean对象
     * @return the map
     */
    Map<String, Object> build(String beanName,T bean);

}
