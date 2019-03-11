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

import static com.feilong.core.util.CollectionsUtil.addAllIgnoreNull;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.lang.ClassUtil;
import com.feilong.spring.event.builder.MapBeanToMapListBuilder;

/**
 * 将 {@code Map<String, SchedulerFactoryBean> beanNameAndBeanMap} 转成 {@code List<Map<String, Object>>} 的构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.0
 */
public class SchedulerFactoryMapBeanToMapListBuilder implements MapBeanToMapListBuilder<SchedulerFactoryBean>{

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.event.builder.MapListBuilder#buildList(java.util.Map)
     */
    @Override
    public List<Map<String, Object>> build(Map<String, SchedulerFactoryBean> beanNameAndBeanMap){
        List<Map<String, Object>> list = newArrayList();

        //---------------------------------------------------------------
        for (Map.Entry<String, SchedulerFactoryBean> entry : beanNameAndBeanMap.entrySet()){
            addAllIgnoreNull(list, build(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param key
     *            the key
     * @param schedulerFactoryBean
     *            the scheduler factory bean
     * @return the map
     * @since 4.0.0
     */
    private static List<Map<String, Object>> build(String key,SchedulerFactoryBean schedulerFactoryBean){
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        //---------------------------------------------------------------
        try{
            return get(scheduler);
        }catch (SchedulerException e){
            throw new DefaultRuntimeException(e);
        }
    }

    /**
     * 获得.
     *
     * @param scheduler
     *            the scheduler
     * @return the list
     * @throws SchedulerException
     *             the scheduler exception
     * @since 4.0.0
     */
    public final static List<Map<String, Object>> get(Scheduler scheduler) throws SchedulerException{
        List<Map<String, Object>> list = newArrayList();

        //---------------------------------------------------------------

        for (String groupName : scheduler.getJobGroupNames()){
            GroupMatcher<JobKey> group = GroupMatcher.jobGroupEquals(groupName);
            Set<JobKey> jobKeys = scheduler.getJobKeys(group);

            for (JobKey jobKey : jobKeys){
                list.add(buildMap(scheduler, jobKey));
            }
        }

        //---------------------------------------------------------------

        return list;
    }

    //---------------------------------------------------------------

    /**
     * Builds the map.
     *
     * @param scheduler
     *            the scheduler
     * @param jobKey
     *            the job key
     * @return the map
     * @throws SchedulerException
     *             the scheduler exception
     */
    private static Map<String, Object> buildMap(Scheduler scheduler,JobKey jobKey) throws SchedulerException{
        //get job's trigger
        List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);

        Trigger trigger = triggers.get(0);

        //JobDetail jobDetail = scheduler.getJobDetail(jobKey);

        //---------------------------------------------------------------

        Map<String, Object> map = newLinkedHashMap();
        map.put("job group", jobKey.getGroup());
        map.put("jobKey name", jobKey.getName());

        //---------------------------------------------------------------
        boolean isCronTrigger = ClassUtil.isInstance(trigger, CronTrigger.class);
        map.put("isCronTrigger", isCronTrigger);

        if (isCronTrigger){
            CronTrigger cronTrigger = (CronTrigger) trigger;

            map.put("cronExpression", cronTrigger.getCronExpression());

            //            seconds: 0
            //            minutes: 0,5,10,15,20,25,30,35,40,45,50,55
            //            hours: *
            //            daysOfMonth: *
            //            months: *
            //            daysOfWeek: ?
            //            lastdayOfWeek: false
            //            nearestWeekday: false
            //            NthDayOfWeek: 0
            //            lastdayOfMonth: false
            //            years: *
            //map.put("getExpressionSummary", cronTrigger.getExpressionSummary());
            //map.put("getTimeZone", cronTrigger.getTimeZone());
        }

        //---------------------------------------------------------------
        map.put("trigger Priority", trigger.getPriority());
        map.put("trigger StartTime", trigger.getStartTime());
        map.put("trigger EndTime", trigger.getEndTime());
        map.put("trigger PreviousFireTime", trigger.getPreviousFireTime());
        map.put("trigger NextFireTime", trigger.getNextFireTime());
        map.put("trigger CalendarName", trigger.getCalendarName());
        map.put("trigger Description", trigger.getDescription());
        map.put("trigger FinalFireTime", trigger.getFinalFireTime());

        //---------------------------------------------------------------
        return map;
    }
}
