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

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.6
 */
public class TestTriggerListener implements TriggerListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(TestTriggerListener.class);

    @Override
    public String getName(){
        return this.getClass().getName();
    }

    //---------------------------------------------------------------

    @Override
    public void triggerFired(Trigger trigger,JobExecutionContext context){
        LOGGER.debug("triggerFired:[{}],trigger:[{}]", this.getClass().getName(), trigger.getNextFireTime());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.quartz.TriggerListener#vetoJobExecution(org.quartz.Trigger, org.quartz.JobExecutionContext)
     */
    @Override
    public boolean vetoJobExecution(Trigger trigger,JobExecutionContext context){
        LOGGER.debug("vetoJobExecution:[{}],trigger:[{}]", this.getClass().getName(), trigger.getNextFireTime());
        return false;
    }

    //---------------------------------------------------------------

    @Override
    public void triggerMisfired(Trigger trigger){
        LOGGER.debug("triggerMisfired:{}", this.getClass().getName());
    }

    //---------------------------------------------------------------

    @Override
    public void triggerComplete(Trigger trigger,JobExecutionContext context,CompletedExecutionInstruction triggerInstructionCode){
        LOGGER.debug("triggerComplete:[{}],trigger:[{}]", this.getClass().getName(), trigger.getNextFireTime());
    }
}
