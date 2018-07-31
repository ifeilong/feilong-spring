package com.baozun.nebula.manager;

import java.util.Date;

public interface SchedulerManager{

    /**
     * 添加定时任务
     * 
     * @param taskInstance
     *            任务的实例
     * @param taskMethod
     *            调用任务的方法
     * @param date
     *            在什么时间执行
     * @param jobName
     *            任务名称(不能重复)，一般按照业务逻辑再加上一定的参数进行命名
     */
    void addTask(Object taskInstance,String taskMethod,Date date,String jobName);

    /**
     * 添加定时任务
     * 
     * @param taskInstance
     *            任务的实例
     * @param taskMethod
     *            调用任务的方法
     * @param timeExp
     *            执行时间表达式
     * @param jobName
     *            任务名称(不能重复)，一般按照业务逻辑再加上一定的参数进行命名
     */
    void addTask(Object taskInstance,String taskMethod,String timeExp,String jobName);

    /**
     * 删除定时任务
     * 
     * @param jobName
     */
    void removeTask(String jobName);

    /**
     * 定时清理任务
     * 
     */
    void timerClean();
}
