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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.feilong.core.date.DateExtensionUtil;
import com.feilong.core.date.TimeInterval;
import com.feilong.core.log.Slf4jUtil;
import com.feilong.core.tools.jsonlib.JsonUtil;
import com.feilong.core.util.Validator;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.servlet.http.entity.RequestLogSwitch;

/**
 * 监控每个 {@link HandlerMethod}执行的时间, 输出log到日志,这些日志级别可以单独开启到专门的日志文件.
 * 
 * <h3>如果不想用</h3>
 * 
 * <blockquote>
 * 该Interceptor本身对性能的影响几乎为0, 如果不想使用, 可以在spring Interceptor配置文件中去掉该配置
 * </blockquote>
 * 
 * @author feilong
 * @version 1.2.2 2015年7月18日 下午2:22:36
 * @since 1.2.2
 */
public class MonitorInterceptor extends HandlerInterceptorAdapter implements Ordered{

    /** The Constant LOGGER. */
    private static final Logger  LOGGER                = LoggerFactory.getLogger(MonitorInterceptor.class);

    /** 性能阀值<code>{@value}</code>,目前初步设置为1.5s,如果构建command 超过1.5s,会有 error log 记录. */
    private static final Integer PERFORMANCE_THRESHOLD = (int) (TimeInterval.MILLISECOND_PER_SECONDS * 1.5);

    /** The Constant STOPWATCH_ATTRIBUTE. */
    private static final String  STOPWATCH_ATTRIBUTE   = MonitorInterceptor.class.getName() + ".stopWatch";

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        request.setAttribute(STOPWATCH_ATTRIBUTE, stopWatch);

        if (LOGGER.isDebugEnabled()){

            Map<String, Object> attributeMap = RequestUtil.getAttributeMap(request);

            RequestLogSwitch requestLogSwitch = new RequestLogSwitch();
            requestLogSwitch.setShowForwardInfos(true);

            Map<String, Object> requestInfoMapForLog = RequestUtil.getRequestInfoMapForLog(request, requestLogSwitch);

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug(
                                "RequestInfoMapForLog:{},request attribute keys:{},start StopWatch",
                                JsonUtil.format(requestInfoMapForLog),
                                JsonUtil.format(new TreeSet(attributeMap.keySet())));
            }
        }

        return super.preHandle(request, response, handler);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView)
                    throws Exception{

        if (handler instanceof HandlerMethod){
            RequestLogSwitch requestLogSwitch = new RequestLogSwitch();
            requestLogSwitch.setShowForwardInfos(true);
            //requestLogSwitch.setShowIncludeInfos(true);
            //requestLogSwitch.setShowURLs(true);
            Map<String, Object> requestInfoMapForLog = RequestUtil.getRequestInfoMapForLog(request, requestLogSwitch);

            StopWatch stopWatch = (StopWatch) request.getAttribute(STOPWATCH_ATTRIBUTE);
            stopWatch.split();
            long useTime = stopWatch.getSplitTime();

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            String methodName = method.getName();
            String className = method.getDeclaringClass().getSimpleName();

            if (LOGGER.isInfoEnabled()){
                String customerLog = "";
                if (Validator.isNotNullOrEmpty(modelAndView)){
                    String modelMapKeys = null == modelAndView.getModel() ? null : JsonUtil.format(new TreeSet(modelAndView.getModel()
                                    .keySet()));
                    String viewName = Validator.isNullOrEmpty(modelAndView.getView()) ? modelAndView.getViewName() : modelAndView.getView()
                                    .toString();
                    customerLog = Slf4jUtil.formatMessage("model keys:[{}],view:[{}]", modelMapKeys, viewName);
                }else{
                    customerLog = "modelAndView is null!!";
                }

                LOGGER.info(
                                "RequestInfoMapForLog:{},request attribute keys:{},customerLog:[{}],\n postHandle [{}.{}()] ",
                                JsonUtil.format(requestInfoMapForLog),
                                JsonUtil.format(new TreeSet(RequestUtil.getAttributeMap(request).keySet())),
                                customerLog,
                                className,
                                methodName);
            }

            //**********************************************************************************
            boolean isMoreThanPerformanceThreshold = useTime > PERFORMANCE_THRESHOLD;
            String message = Slf4jUtil.formatMessage(
                            " [{}.{}()], use time:[{}],[{}] PERFORMANCE_THRESHOLD:[{}]",
                            className,
                            methodName,
                            DateExtensionUtil.getIntervalForView(useTime),
                            isMoreThanPerformanceThreshold ? ">" : "<=",
                            PERFORMANCE_THRESHOLD);

            //如果超过阀值, 那么以error的形式记录
            if (isMoreThanPerformanceThreshold){
                ServletContext servletContext = request.getSession().getServletContext();
                servletContext.log(message);
                LOGGER.error(message);
            }else{
                LOGGER.info(message);
            }
        }
    }

    /**
     * 获得 data map.
     *
     * @param request
     *            the request
     * @param modelAndView
     *            the ModelAndView that the handler returned (can also be null)
     * @return the data map
     */
    private Map<String, Object> getDataMap(HttpServletRequest request,ModelAndView modelAndView){
        Map<String, Object> model = (null == modelAndView) ? null : modelAndView.getModel();
        Map<String, Object> attributeMap = RequestUtil.getAttributeMap(request);

        //新创建个map对象, 这样操作不会影响原始数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.putAll(model);
        map.putAll(attributeMap);
        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterConcurrentHandlingStarted(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
        if (handler instanceof HandlerMethod){
            StopWatch stopWatch = (StopWatch) request.getAttribute(STOPWATCH_ATTRIBUTE);
            stopWatch.split();
            long splitTime = stopWatch.getSplitTime();

            if (LOGGER.isInfoEnabled()){
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                LOGGER.info(
                                "afterConcurrentHandlingStarted [{}.{}()], use time:[{}]",
                                method.getDeclaringClass().getSimpleName(),
                                method.getName(),
                                DateExtensionUtil.getIntervalForView(splitTime));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) throws Exception{
        if (handler instanceof HandlerMethod){
            StopWatch stopWatch = (StopWatch) request.getAttribute(STOPWATCH_ATTRIBUTE);

            if (null == stopWatch || stopWatch.isStopped()){
                String message = Slf4jUtil.formatMessage(
                                "stopWatch is null or stopWatch isStopped!!,request info is:{}",
                                RequestUtil.getRequestInfoMapForLog(request));
                LOGGER.error(message);
                request.getSession().getServletContext().log("[" + MonitorInterceptor.class.getSimpleName() + "]," + message);
            }else{
                stopWatch.split();
                long splitTime = stopWatch.getSplitTime();

                stopWatch.stop();
                long time = stopWatch.getTime();

                if (LOGGER.isInfoEnabled()){
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    Method method = handlerMethod.getMethod();
                    LOGGER.info(
                                    "afterCompletion [{}.{}()], use time:[{}],total time:[{}]",
                                    method.getDeclaringClass().getSimpleName(),
                                    method.getName(),
                                    DateExtensionUtil.getIntervalForView(splitTime),
                                    DateExtensionUtil.getIntervalForView(time));
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.core.Ordered#getOrder()
     */
    @Override
    public int getOrder(){
        return 99999;
    }

}