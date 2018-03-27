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
import java.util.concurrent.Callable;

import com.feilong.core.ReturnResult;
import com.feilong.core.lang.PartitionRunnableBuilder;
import com.feilong.core.lang.PartitionThreadEntity;

/**
 * Callable 的实现.
 * 
 * <h3>{@link Callable}和{@link Runnable}的区别如下:</h3>
 * <blockquote>
 * <ol>
 * <li>{@link Callable}定义的方法是{@link Callable#call()},而{@link Runnable}定义的方法是{@link Runnable#run()}.</li>
 * <li>{@link Callable}的{@link Callable#call()}方法有返回值,而{@link Runnable}的{@link Runnable#run()}方法不能有返回值</li>
 * <li>{@link Callable}的{@link Callable#call()}方法可抛出异常,而{@link Runnable}的{@link Runnable#run()}方法不能抛出异常</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.11.0
 */
public class PartitionThreadCallable<T> implements Callable<ReturnResult>{

    /** The list. */
    private final List<T>                     list;

    /** The each size. */
    private final int                         eachSize;

    /** The params map. */
    private final Map<String, ?>              paramsMap;

    /** The partition runnable builder. */
    private final PartitionRunnableBuilder<T> partitionRunnableBuilder;

    /** The i. */
    private final int                         i;

    /** The per batch list. */
    private final List<T>                     perBatchList;

    //---------------------------------------------------------------

    /**
     * Instantiates a new partition thread callable.
     *
     * @param list
     *            the list
     * @param eachSize
     *            the each size
     * @param paramsMap
     *            the params map
     * @param partitionRunnableBuilder
     *            the partition runnable builder
     * @param i
     *            the i
     * @param perBatchList
     *            the per batch list
     */
    public PartitionThreadCallable(List<T> list, int eachSize, Map<String, ?> paramsMap,
                    PartitionRunnableBuilder<T> partitionRunnableBuilder, int i, List<T> perBatchList){
        this.list = list;
        this.eachSize = eachSize;
        this.paramsMap = paramsMap;
        this.partitionRunnableBuilder = partitionRunnableBuilder;
        this.i = i;
        this.perBatchList = perBatchList;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public ReturnResult call() throws Exception{
        String taskId = perBatchList.toString();
        PartitionThreadEntity partitionThreadEntity = new PartitionThreadEntity("task" + i, list.size(), eachSize, i, perBatchList.size());
        Runnable runnable = partitionRunnableBuilder.build(perBatchList, partitionThreadEntity, paramsMap);
        runnable.run();

        return new ReturnResult(true, taskId);
    }

}
