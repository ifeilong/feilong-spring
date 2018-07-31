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

import static com.feilong.core.bean.ConvertUtil.toList;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.feilong.core.lang.PartitionRunnableBuilder;
import com.feilong.core.lang.PartitionThreadEntity;
import com.feilong.core.lang.PartitionThreadExecutor;

@ContextConfiguration(locations = { "classpath:spring-taskExecutor.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class ThreadPoolTaskExecutorUtilTest{

    /** The Constant log. */
    private static final Logger     LOGGER = LoggerFactory.getLogger(ThreadPoolTaskExecutorUtilTest.class);

    //---------------------------------------------------------------
    @Autowired
    private PartitionThreadExecutor partitionThreadExecutor;

    //---------------------------------------------------------------

    @Test
    public void test(){
        final List<Integer> list = toList(1, 2, 3, 4, 5);

        partitionThreadExecutor.excute(list, 2, new PartitionRunnableBuilder<Integer>(){

            @Override
            public Runnable build(List<Integer> perBatchList,final PartitionThreadEntity partitionThreadEntity,Map<String, ?> paramsMap){
                return new Runnable(){

                    @Override
                    public void run(){
                        for (Integer integer : list){
                            LOGGER.debug("{}-{}", partitionThreadEntity.getBatchNumber(), integer);
                        }
                    }
                };
            }
        });
    }

}
