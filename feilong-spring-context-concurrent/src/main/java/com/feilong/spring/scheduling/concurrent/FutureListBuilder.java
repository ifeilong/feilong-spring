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
package com.feilong.spring.scheduling.concurrent;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * The Interface FutureListBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public interface FutureListBuilder{

    /**
     * Builds the.
     *
     * @param <V>
     *            the value type
     * @param callableList
     *            the callable list
     * @return 如果 <code>callableList</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>callableList</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     */
    <V> List<Future<V>> build(List<? extends Callable<V>> callableList);
}
