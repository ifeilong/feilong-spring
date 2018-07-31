package com.baozun.nebula.manager;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service("schedulerManager")
public class SchedulerManagerImpl implements SchedulerManager{

    /** The Constant log. */
    private static final Logger  LOGGER = LoggerFactory.getLogger(SchedulerManagerImpl.class);

    //---------------------------------------------------------------

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    //---------------------------------------------------------------

    @Override
    public void addTask(Object taskInstance,String taskMethod,Date date,String jobName){

        SimpleDateFormat sdf = new SimpleDateFormat("s m H d M ? yyyy");
        String strDate = sdf.format(date);
        addTask(taskInstance, taskMethod, strDate, jobName);
    }

    @Override
    public void removeTask(String jobName){
        //schedulerFactoryBean.getScheduler().deleteJob(jobName, schedulerFactoryBean.DEFAULT_GROUP);
    }

    @Override
    public void timerClean(){
        LOGGER.debug("1");
        //        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //
        //        String[] triggerNames = scheduler.getTriggerNames(schedulerFactoryBean.DEFAULT_GROUP);

        //        for (String name : triggerNames){
        //        Trigger trigger = scheduler.getTrigger(name, schedulerFactoryBean.DEFAULT_GROUP);

        //            //如果下次触发时间小于当前时间，代表此任务已经没有用处，清理掉(多加5秒安全期，预防并发问题)
        //            if (trigger.getNextFireTime() == null || trigger.getNextFireTime().getTime() < System.currentTimeMillis() - 5000){
        //                removeTask(trigger.getJobName());
        //            }
        //        }
    }

    @Override
    public void addTask(Object taskInstance,String taskMethod,String timeExp,String jobName){
        //        MethodInvokingJobDetailFactoryBean mifb = new MethodInvokingJobDetailFactoryBean();
        //
        //        mifb.setTargetObject(taskInstance);
        //        mifb.setTargetMethod(taskMethod);
        //        mifb.setConcurrent(false);
        //        mifb.setName(jobName);
        //        mifb.afterPropertiesSet();
        //
        //        CronTriggerBean ctb = new CronTriggerBean();
        //        ctb.setJobDetail(mifb.getObject());
        //        ctb.setCronExpression(timeExp);
        //        //		ctb.setCronExpression("0 59 18 9 4 ?");
        //
        //        ctb.setName(jobName + "trigger");
        //        //		ctb.getNextFireTime();
        //
        //        schedulerFactoryBean.getScheduler().scheduleJob(mifb.getObject(), ctb);
    }

}
