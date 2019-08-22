package org.quartz;

import static com.feilong.core.date.DateUtil.nowString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.DatePattern;

public class SimpleJob implements Job{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJob.class);

    //---------------------------------------------------------------

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        Trigger trigger = context.getTrigger();
        JobDetail jobDetail = context.getJobDetail();
        TriggerKey key = trigger.getKey();

        JobKey jobKey = jobDetail.getKey();
        LOGGER.info(
                        nowString(DatePattern.COMMON_DATE_AND_TIME)
                                        + ",triggerName:[{}],triggerGroup:[{}],jobName:[{}],jobGroup:[{}],jobMap:[{}]",
                        key.getName(),
                        key.getName(),

                        jobKey.getName(),
                        jobKey.getGroup(),
                        trigger.getJobDataMap().getWrappedMap());

    }
}