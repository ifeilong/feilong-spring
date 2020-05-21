package com.b.nebula.manager;

import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;

import java.util.Map;

import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.json.JsonUtil;
import com.feilong.spring.scheduling.quartz.SchedulerFactoryMapBeanToMapListBuilder;

@ContextConfiguration(locations = { "classpath:spring4-Scheduler-test.xml", "classpath:spring-EventListener.xml" })
public class SchedulerManagerImplTest extends AbstractJUnit4SpringContextTests{

    /** The Constant log. */
    private static final Logger                           LOGGER                                  = LoggerFactory
                    .getLogger(SchedulerManagerImplTest.class);

    //---------------------------------------------------------------

    @Autowired
    private SchedulerManager                              schedulerManager;

    @Autowired
    private SchedulerFactoryBean                          schedulerFactoryBean;

    private final SchedulerFactoryMapBeanToMapListBuilder schedulerFactoryMapBeanToMapListBuilder = new SchedulerFactoryMapBeanToMapListBuilder();
    //---------------------------------------------------------------

    @Test
    public void test1() throws SchedulerException{
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        LOGGER.debug(JsonUtil.format(getMapForLog(scheduler)));
        LOGGER.debug("{}", formatToSimpleTable(schedulerFactoryMapBeanToMapListBuilder.get(scheduler)));
    }

    //---------------------------------------------------------------

    public final static Map<String, Object> getMapForLog(Scheduler scheduler){
        Map<String, Object> map = newLinkedHashMap();

        //     String[] triggerNames = scheduler.getTriggerNames(schedulerFactoryBean.DEFAULT_GROUP);
        try{
            map.put("scheduler.getSchedulerInstanceId()", scheduler.getSchedulerInstanceId());
            map.put("scheduler.getSchedulerName()", scheduler.getSchedulerName());
            map.put("scheduler.getCalendarNames()", scheduler.getCalendarNames());
            map.put("scheduler.getJobGroupNames()", scheduler.getJobGroupNames());

            //---------------------------------------------------------------
            map.put("scheduler.isInStandbyMode()", scheduler.isInStandbyMode());
            map.put("scheduler.isStarted()", scheduler.isStarted());
            map.put("scheduler.isShutdown()", scheduler.isShutdown());

            //---------------------------------------------------------------

            TriggerKey triggerKey = new TriggerKey("", "");

            Trigger trigger = scheduler.getTrigger(triggerKey);

            map.put("trigger()", trigger);

            return map;
        }catch (SchedulerException e){
            throw new DefaultRuntimeException(e);
        }
    }

    @Test
    public void test(){
        schedulerManager.timerClean();
    }

}
