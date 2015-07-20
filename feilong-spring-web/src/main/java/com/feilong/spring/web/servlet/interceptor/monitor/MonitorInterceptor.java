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
import java.util.Map;
import java.util.TreeSet;

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
import com.feilong.core.tools.jsonlib.JsonUtil;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorInterceptor.class);

    /** The stop watch. */
    private StopWatch           stopWatch;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
        stopWatch = new StopWatch();
        stopWatch.start();

        if (LOGGER.isDebugEnabled()){

            Map<String, Object> attributeMap = RequestUtil.getAttributeMap(request);

            RequestLogSwitch requestLogSwitch = new RequestLogSwitch();
            requestLogSwitch.setShowForwardInfos(true);
            //requestLogSwitch.setShowIncludeInfos(true);
            //requestLogSwitch.setShowURLs(true);
            Map<String, Object> requestInfoMapForLog = RequestUtil.getRequestInfoMapForLog(request, requestLogSwitch);
            LOGGER.debug(
                            "RequestInfoMapForLog:{},request attribute keys:{},start StopWatch",
                            JsonUtil.format(requestInfoMapForLog),
                            JsonUtil.format(new TreeSet(attributeMap.keySet())));
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

            Map<String, Object> model = modelAndView.getModel();
            Map<String, Object> attributeMap = RequestUtil.getAttributeMap(request);

            RequestLogSwitch requestLogSwitch = new RequestLogSwitch();
            requestLogSwitch.setShowForwardInfos(true);
            //requestLogSwitch.setShowIncludeInfos(true);
            //requestLogSwitch.setShowURLs(true);
            Map<String, Object> requestInfoMapForLog = RequestUtil.getRequestInfoMapForLog(request, requestLogSwitch);
            if (LOGGER.isInfoEnabled()){
                stopWatch.split();
                long splitTime = stopWatch.getSplitTime();

                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();

                LOGGER.info(
                                "RequestInfoMapForLog:{},request attribute keys:{},model keys:[{}],postHandle [{}.{}()], use time:[{}]",
                                JsonUtil.format(requestInfoMapForLog),
                                JsonUtil.format(new TreeSet(attributeMap.keySet())),
                                JsonUtil.format(new TreeSet(model.keySet())),
                                method.getDeclaringClass().getSimpleName(),
                                method.getName(),
                                DateExtensionUtil.getIntervalForView(splitTime));
            }
        }
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
            stopWatch.split();
            long splitTime = stopWatch.getSplitTime();

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LOGGER.info(
                            "afterConcurrentHandlingStarted [{}.{}()], use time:[{}]",
                            method.getDeclaringClass().getSimpleName(),
                            method.getName(),
                            DateExtensionUtil.getIntervalForView(splitTime));
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
            stopWatch.split();
            long splitTime = stopWatch.getSplitTime();

            stopWatch.stop();
            long time = stopWatch.getTime();

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