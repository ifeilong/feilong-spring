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
package com.feilong.spring.scheduling.quartz.listener;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.6
 */
public class TestSchedulerListener implements SchedulerListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSchedulerListener.class);

    @Override
    public void jobScheduled(Trigger trigger){
        LOGGER.debug("jobScheduled:{}", this.getClass().getName());

    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey){
        LOGGER.debug("jobUnscheduled:{}", this.getClass().getName());
    }

    @Override
    public void triggerFinalized(Trigger trigger){
        LOGGER.debug("triggerFinalized:{}", this.getClass().getName());
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey){
        LOGGER.debug("triggerPaused:{}", this.getClass().getName());
    }

    @Override
    public void triggersPaused(String triggerGroup){
        LOGGER.debug("triggersPaused:{}", this.getClass().getName());
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey){
        LOGGER.debug("triggerResumed:{}", this.getClass().getName());
    }

    @Override
    public void triggersResumed(String triggerGroup){
        LOGGER.debug("triggersResumed:{}", this.getClass().getName());

    }

    @Override
    public void jobAdded(JobDetail jobDetail){
        LOGGER.debug("jobAdded:[{}]", this.getClass().getName());
    }

    @Override
    public void jobDeleted(JobKey jobKey){
        LOGGER.debug("jobDeleted:{}", this.getClass().getName());
    }

    @Override
    public void jobPaused(JobKey jobKey){
        LOGGER.debug("jobPaused:{}", this.getClass().getName());
    }

    @Override
    public void jobsPaused(String jobGroup){
        LOGGER.debug("jobsPaused:{}", this.getClass().getName());
    }

    @Override
    public void jobResumed(JobKey jobKey){
        LOGGER.debug("jobResumed:{}", this.getClass().getName());
    }

    @Override
    public void jobsResumed(String jobGroup){
        LOGGER.debug("jobsResumed:{}", this.getClass().getName());
    }

    @Override
    public void schedulerError(String msg,SchedulerException cause){
        LOGGER.debug("schedulerError:{}", this.getClass().getName());
    }

    @Override
    public void schedulerInStandbyMode(){
        LOGGER.debug("schedulerInStandbyMode:{}", this.getClass().getName());
    }

    //---------------------------------------------------------------

    @Override
    public void schedulerStarting(){
        LOGGER.debug("schedulerStarting:[{}]", this.getClass().getName());
    }

    @Override
    public void schedulerStarted(){
        LOGGER.debug("schedulerStarted:[{}]", this.getClass().getName());
    }

    @Override
    public void schedulerShuttingdown(){
        LOGGER.debug("schedulerShuttingdown:{}", this.getClass().getName());
    }

    @Override
    public void schedulerShutdown(){
        LOGGER.debug("schedulerShutdown:{}", this.getClass().getName());
    }

    //---------------------------------------------------------------

    @Override
    public void schedulingDataCleared(){
        LOGGER.debug("schedulingDataCleared:{}", this.getClass().getName());
    }
}
