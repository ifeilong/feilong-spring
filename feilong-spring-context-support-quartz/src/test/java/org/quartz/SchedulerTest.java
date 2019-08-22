package org.quartz;

import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.StdSchedulerFactory;

/**
 * quartz的类写的很优雅，类的方法能进行连写，和jQuery很像,好处很明显，优雅的同时也减少了代码，提高了创建类对象的使用
 * 
 * quartz包含job和trigger两个概念，job就是你要做的事情，trigger当然是触发器，
 * 
 * 他们都是有名称和组的，并且还有参数，
 * 
 * 这个参数呢其实就是一个quartz自己搞的一个继承了java.util.Map的类org.quartz.JobDataMap。
 * 
 * 基本上org.quartz.*包能完成大部分的定时器的任务，因为初学这个所以今天也就只看了这个包的内容，感觉还是非常容易上手的。
 * 
 * 总结：org.quartz.*包下基本上搞懂job、trigger、scheduler这3个相关的东西就行了，因为定时相关的就是围绕这3个东西展开的。
 */
public class SchedulerTest{

    public static void main(String[] args) throws SchedulerException{
        SchedulerTest schedulerTest = new SchedulerTest();

        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDetail jobDetail = schedulerTest.buildJobDetail();
        scheduler.scheduleJob(jobDetail, schedulerTest.buildTrigger1());
        scheduler.scheduleJob(schedulerTest.buildTrigger2(jobDetail));//同一个job添加多个trigger  

        scheduler.start();
    }

    //---------------------------------------------------------------

    private JobDetail buildJobDetail(){
        return JobBuilder.newJob(SimpleJob.class)//
                        .withIdentity(new JobKey("job名称", "job组名称")).//
                        build();
    }

    private Trigger buildTrigger1(){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("key1", "value1");
        jobDataMap.put("key2", "value2");

        return TriggerBuilder.newTrigger()//
                        // 添加身份标识，可以添加或不添加  
                        .withIdentity(new TriggerKey("trigger名称", "trigger组名称"))
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever())
                        //                  .withSchedule(CronScheduleBuilder.cronSchedule(""))  
                        .usingJobData(jobDataMap)//
                        .startAt(
                                        // future将来的意思，futureDate就是将来的某个时间执行  
                                        DateBuilder.futureDate(2, IntervalUnit.SECOND))
                        .build();
    }

    //---------------------------------------------------------------

    private Trigger buildTrigger2(JobDetail jobDetail){
        return TriggerBuilder.newTrigger()
                        // 添加身份标识，可以添加或不添加  
                        .withIdentity(new TriggerKey("trigger名称2", "trigger组名称2"))
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever()).startAt(
                                        // future将来的意思，futureDate就是将来的某个时间执行  
                                        DateBuilder.futureDate(5, IntervalUnit.SECOND))
                        .forJob(jobDetail)//
                        .build();
    }
}