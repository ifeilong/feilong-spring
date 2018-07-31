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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.apache.commons.lang3.Validate;
import org.springframework.core.task.AsyncTaskExecutor;

/**
 * The Class DefaultFutureListBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class DefaultFutureListBuilder implements FutureListBuilder{

    /** The async task executor. */
    private AsyncTaskExecutor asyncTaskExecutor;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.scheduling.concurrent.FutureListBuilder#build(java.util.List)
     */
    @Override
    public <V> List<Future<V>> build(List<? extends Callable<V>> callableList){
        Validate.notEmpty(callableList, "callableList can't be null/empty!");

        //Future 表示异步计算的结果。它提供了检查计算是否完成的方法，以等待计算的完成，并获取计算的结果
        List<Future<V>> futureList = new ArrayList<>(callableList.size());
        for (Callable<V> callable : callableList){
            Future<V> future = asyncTaskExecutor.submit(callable);
            futureList.add(future);
        }
        return futureList;
    }

    //---------------------------------------------------------------
    /**
     * Sets the async task executor.
     *
     * @param asyncTaskExecutor
     *            the asyncTaskExecutor to set
     */
    public void setAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor){
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

}
