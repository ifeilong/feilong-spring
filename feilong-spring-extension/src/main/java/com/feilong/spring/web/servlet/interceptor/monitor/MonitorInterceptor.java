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
package com.feilong.spring.web.servlet.interceptor.monitor;

import static com.feilong.core.date.DateUtil.formatDuration;
import static com.feilong.servlet.http.RequestUtil.getRequestInfoMapForLog;
import static com.feilong.spring.web.method.HandlerMethodUtil.getDeclaringClassSimpleName;
import static com.feilong.spring.web.method.HandlerMethodUtil.getHandlerMethodName;
import static com.feilong.spring.web.servlet.interceptor.monitor.MonitorMessageBuilder.buildPostMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.time.StopWatch;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.spring.web.method.HandlerMethodUtil;
import com.feilong.spring.web.servlet.interceptor.AbstractHandlerMethodInterceptorAdapter;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 监控每个 {@link HandlerMethod}执行的时间, 输出log到日志,这些日志级别可以单独开启到专门的日志文件.
 * 
 * <p>
 * 该Interceptor本身对性能的影响几乎为0
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.2.2
 */
public class MonitorInterceptor extends AbstractHandlerMethodInterceptorAdapter{

    /** The Constant LOGGER. */
    private static final Logger  LOGGER              = LoggerFactory.getLogger(MonitorInterceptor.class);

    /** The Constant STOPWATCH_ATTRIBUTE. */
    public static final String   STOPWATCH_ATTRIBUTE = MonitorInterceptor.class.getName() + ".stopWatch";

    //---------------------------------------------------------------
    /**
     * MonitorMessageEntity.
     * 
     * @since 1.10.6
     */
    private MonitorMessageEntity monitorMessageEntity;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.feilong.spring.web.servlet.interceptor.AbstractHandlerMethodInterceptorAdapter#doPreHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean doPreHandle(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        //---------------------------------------------------------------

        request.setAttribute(STOPWATCH_ATTRIBUTE, stopWatch);

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(MonitorMessageBuilder.buildPreMessage(monitorMessageEntity, request));
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.AbstractHandlerMethodInterceptorAdapter#doPostHandle(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.method.HandlerMethod,
     * org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void doPostHandle(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod,ModelAndView modelAndView){
        if (!monitorMessageEntity.getIsShowPostHandleLog()){
            return;
        }

        //---------------------------------------------------------------
        StopWatch stopWatch = getStopWatch(request);
        stopWatch.split();
        long useTime = stopWatch.getSplitTime();

        boolean isMoreThanPerformanceThreshold = useTime > monitorMessageEntity.getPerformanceThreshold();

        //---------------------------------------------------------------

        try{
            //如果超过阀值, 那么以error的形式记录
            if (isMoreThanPerformanceThreshold){
                LOGGER.error(buildPostMessage(handlerMethod, modelAndView, useTime, monitorMessageEntity, request));

                //TODO 这里的request.getSession() 可能会报错 Cannot create a session after the response has been committed 
                //ServletContext servletContext = request.getSession().getServletContext();
            }else{
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug(buildPostMessage(handlerMethod, modelAndView, useTime, monitorMessageEntity, request));
                }
            }
        }catch (Exception e){//可能有异常,比如  往request/model里面设置了 不能被json处理的对象或者字段
            String pattern = "postHandle [{}.{}()] occur exception,but we need goon!,just log it,request info:[{}]";
            LOGGER.error(
                            Slf4jUtil.format(
                                            pattern,
                                            getDeclaringClassSimpleName(handlerMethod),
                                            getHandlerMethodName(handlerMethod),
                                            JsonUtil.toString(
                                                            getRequestInfoMapForLog(request, monitorMessageEntity.getRequestLogSwitch()))),
                            e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.feilong.spring.web.servlet.interceptor.AbstractHandlerMethodInterceptorAdapter#doAfterConcurrentHandlingStarted(javax.servlet.
     * http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.method.HandlerMethod)
     */
    @Override
    public void doAfterConcurrentHandlingStarted(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod){
        if (!monitorMessageEntity.getIsShowAfterConcurrentHandlingStartedLog()){
            return;
        }

        //---------------------------------------------------------------

        StopWatch stopWatch = getStopWatch(request);
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "afterConcurrentHandlingStarted [{}.{}()], use time:[{}]",
                            HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
                            HandlerMethodUtil.getHandlerMethodName(handlerMethod),
                            formatDuration(splitTime));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.AbstractHandlerMethodInterceptorAdapter#doAfterCompletion(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.method.HandlerMethod, java.lang.Exception)
     */
    @Override
    public void doAfterCompletion(HttpServletRequest request,HttpServletResponse response,HandlerMethod handlerMethod,Exception ex){
        if (!monitorMessageEntity.getIsShowAfterCompletionLog()){
            return;
        }

        //---------------------------------------------------------------

        StopWatch stopWatch = getStopWatch(request);

        if (null == stopWatch || stopWatch.isStopped()){
            LOGGER.error("stopWatch is null or stopWatch isStopped!!,request info is:{}", RequestUtil.getRequestInfoMapForLog(request));
            return;
        }

        //---------------------------------------------------------------

        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();

        stopWatch.stop();
        long time = stopWatch.getTime();

        //---------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "afterCompletion [{}.{}()], use time:[{}],total time:[{}]",
                            HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
                            HandlerMethodUtil.getHandlerMethodName(handlerMethod),
                            formatDuration(splitTime),
                            formatDuration(time));
        }
    }

    //---------------------------------------------------------------

    /**
     * 获得 stop watch.
     *
     * @param request
     *            the request
     * @return the stop watch
     * @since 1.4.0
     */
    private static StopWatch getStopWatch(HttpServletRequest request){
        return (StopWatch) request.getAttribute(STOPWATCH_ATTRIBUTE);
    }

    //---------------------------------------------------------------

    /**
     * 设置 monitorMessageEntity.
     *
     * @param monitorMessageEntity
     *            the monitorMessageEntity to set
     */
    public void setMonitorMessageEntity(MonitorMessageEntity monitorMessageEntity){
        this.monitorMessageEntity = monitorMessageEntity;
    }
}