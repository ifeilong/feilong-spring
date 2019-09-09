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
import java.util.Map;

import com.feilong.core.lang.thread.PartitionRunnableBuilder;

/**
 * 专门用来构造 PartitionThreadCallable list的.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public interface PartitionThreadCallableListBuilder{

    /**
     * 构建批量执行的任务
     * 按照BATCH_SIZE的大小拆分成多组进行执行.
     *
     * @param <T>
     *            the generic type
     * @param list
     *            执行解析的list
     * 
     *            <p>
     *            比如 100000个 User
     *            </p>
     * @param eachSize
     *            每个线程执行多少个对象
     * 
     *            <p>
     *            比如 一个线程解析 1000个 User, 那么程序内部 会自动创建 100000/1000个 线程去解析;
     *            </p>
     * @param paramsMap
     *            自定义的相关参数
     *            <p>
     *            自定义的 <code>partitionRunnableBuilder</code>中使用,可能为null
     *            </p>
     * @param partitionRunnableBuilder
     *            每个线程做的事情
     * @return the list
     */
    <T> List<PartitionThreadCallable<T>> build(
                    final List<T> list,
                    final int eachSize,
                    final Map<String, ?> paramsMap,
                    final PartitionRunnableBuilder<T> partitionRunnableBuilder);
}
