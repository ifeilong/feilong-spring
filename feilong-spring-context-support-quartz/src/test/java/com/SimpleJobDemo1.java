package com;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SimpleJobDemo1 implements Job{

    private final Calendar calendar = new GregorianCalendar();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mi = calendar.get(Calendar.MINUTE);
        int ss = calendar.get(Calendar.SECOND);
        String s = year + "-" + month + "-" + day + " " + hh + ":" + mi + ":" + ss;
        System.out.println(
                        s + ",triggerName:" + context.getTrigger().getKey().getName() + ",triggerGroup:"
                                        + context.getTrigger().getKey().getName() + ",jobName:" + context.getJobDetail().getKey().getName()
                                        + ",jobGroup:" + context.getJobDetail().getKey().getGroup() + ",jobMap:"
                                        + context.getTrigger().getJobDataMap().getWrappedMap());
    }
}