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
package com.feilong.spring.scheduling.quartz;

import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;

import java.util.List;
import java.util.Map;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.feilong.context.Task;

public class ShowTask implements Task<Void>{

    private static final Logger  LOGGER = LoggerFactory.getLogger(ShowTask.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    //    private final SchedulerFactoryMapBeanToMapListBuilder schedulerFactoryMapBeanToMapListBuilder = new SchedulerFactoryMapBeanToMapListBuilder();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.scheduling.quartz.Job#excute()
     */
    @Override
    public Void run(){
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        try{
            List<Map<String, Object>> iterable = SchedulerFactoryMapBeanToMapListBuilder.get(scheduler);
            LOGGER.debug("{}", formatToSimpleTable(iterable));
            return null;
        }catch (SchedulerException e){
            LOGGER.error("", e);
            throw new RuntimeException(e);
        }
    }
}
