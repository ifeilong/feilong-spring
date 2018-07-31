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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.context.ReturnResult;
import com.feilong.core.lang.AbstractPartitionThreadExecutor;
import com.feilong.core.lang.PartitionRunnableBuilder;

/**
 * The Class AsyncTaskExecutorPartitionThreadExecutor.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class AsyncTaskExecutorPartitionThreadExecutor extends AbstractPartitionThreadExecutor{

    /** The Constant log. */
    private static final Logger                LOGGER                             = LoggerFactory
                    .getLogger(AsyncTaskExecutorPartitionThreadExecutor.class);

    //---------------------------------------------------------------

    /** The task list builder. */
    private FutureListBuilder                  futureListBuilder;

    /** The partition thread callable list builder. */
    private PartitionThreadCallableListBuilder partitionThreadCallableListBuilder = new DefaultPartitionThreadCallableListBuilder();

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.core.lang.AbstractPartitionThreadExecutor#actualExcute(java.util.List, int, java.util.Map,
     * com.feilong.core.lang.PartitionRunnableBuilder)
     */
    @Override
    protected <T> void actualExcute(
                    List<T> list,
                    int eachSize,
                    Map<String, ?> paramsMap,
                    PartitionRunnableBuilder<T> partitionRunnableBuilder){
        List<PartitionThreadCallable<T>> partitionThreadCallableList = partitionThreadCallableListBuilder
                        .build(list, eachSize, paramsMap, partitionRunnableBuilder);

        //---------------------------------------------------------------

        LOGGER.info("拆成 [{}] 个任务并发执行", partitionThreadCallableList.size());

        List<Future<ReturnResult>> futureList = futureListBuilder.build(partitionThreadCallableList);

        //---------------------------------------------------------------
        for (int i = 0; i < futureList.size(); i++){
            Future<ReturnResult> future = futureList.get(i);

            try{
                //如有必要，等待计算完成，然后获取其结果。
                ReturnResult returnResult = future.get();
            }catch (InterruptedException | ExecutionException e){
                //LOGGER.error("task:[" + partitionThreadCallable.getTaskId() + "]execut error", e);
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置 task list builder.
     *
     * @param futureListBuilder
     *            the futureListBuilder to set
     */
    public void setFutureListBuilder(FutureListBuilder futureListBuilder){
        this.futureListBuilder = futureListBuilder;
    }

    /**
     * Sets the partition thread callable list builder.
     *
     * @param partitionThreadCallableListBuilder
     *            the partitionThreadCallableListBuilder to set
     */
    public void setPartitionThreadCallableListBuilder(PartitionThreadCallableListBuilder partitionThreadCallableListBuilder){
        this.partitionThreadCallableListBuilder = partitionThreadCallableListBuilder;
    }

}
