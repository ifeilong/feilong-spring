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
import java.util.Map;

import org.apache.commons.collections4.ListUtils;

import com.feilong.core.lang.PartitionRunnableBuilder;

/**
 * The Class DefaultPartitionThreadCallableListBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class DefaultPartitionThreadCallableListBuilder implements PartitionThreadCallableListBuilder{

    /** The Constant BATCH_SIZE. */
    private static final int BATCH_SIZE = 2;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.scheduling.concurrent.PartitionThreadCallableListBuilder#build(java.util.List, int, java.util.Map,
     * com.feilong.core.lang.PartitionRunnableBuilder)
     */
    @Override
    public <T> List<PartitionThreadCallable<T>> build(
                    final List<T> list,
                    final int eachSize,
                    final Map<String, ?> paramsMap,
                    final PartitionRunnableBuilder<T> partitionRunnableBuilder){
        int end = Math.min(BATCH_SIZE, list.size());

        //将 list 分成 N 份
        List<List<T>> groupList = ListUtils.partition(list, end);

        //---------------------------------------------------------------
        List<PartitionThreadCallable<T>> partitionThreadCallableList = new ArrayList<>(groupList.size());
        for (int i = 0, j = groupList.size(); i < j; ++i){
            PartitionThreadCallable<T> partitionThreadCallable = new PartitionThreadCallable<T>(
                            list,
                            eachSize,
                            paramsMap,
                            partitionRunnableBuilder,
                            i,
                            groupList.get(i));
            partitionThreadCallableList.add(partitionThreadCallable);
        }

        //---------------------------------------------------------------
        return partitionThreadCallableList;
    }

}
