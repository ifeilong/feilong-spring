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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.DateExtensionUtil.formatDuration;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.feilong.core.util.MapUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.spring.web.method.HandlerMethodUtil;
import com.feilong.spring.web.servlet.ModelAndViewUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 构造监控显示的message.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class MonitorMessageBuilder{

    /** Don't let anyone instantiate this class. */
    private MonitorMessageBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the pre message.
     *
     * @param monitorMessageEntity
     *            the monitor message entity
     * @param request
     *            the request
     * @return the string
     */
    static String buildPreMessage(MonitorMessageEntity monitorMessageEntity,HttpServletRequest request){
        String requestBaseInfo = JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request, monitorMessageEntity.getRequestLogSwitch()));

        if (monitorMessageEntity.getIsShowRequestAttribute()){
            return Slf4jUtil.format(
                            "[Request] base info:{},[request] attribute:{} ,start StopWatch",
                            requestBaseInfo,
                            getRequestAttributeMap(monitorMessageEntity, request));
        }
        return Slf4jUtil.format("[Request] base info:{} ,start StopWatch", requestBaseInfo);
    }

    /**
     * 获得 post handle log message.
     *
     * @param handlerMethod
     *            the handler method
     * @param modelAndView
     *            the model and view
     * @param useTime
     *            the use time
     * @param monitorMessageEntity
     *            the monitor message entity
     * @param request
     *            the request
     * @return the log message
     */
    static String buildPostMessage(
                    HandlerMethod handlerMethod,
                    ModelAndView modelAndView,
                    long useTime,
                    MonitorMessageEntity monitorMessageEntity,
                    HttpServletRequest request){
        long performanceThreshold = monitorMessageEntity.getPerformanceThreshold();
        String logicOperator = useTime > performanceThreshold ? ">" : (useTime == performanceThreshold ? "=" : "<");

        //一条日志输出, 这样的话,在并发的情况, 日志还是有上下文的
        return Slf4jUtil.format(
                        "postHandle [{}.{}()],RequestInfoMapForLog:{},[request] attribute:{},modelAndView info:[{}],use time:[{}],[{}] performanceThreshold:[{}]",
                        HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
                        HandlerMethodUtil.getHandlerMethodName(handlerMethod),
                        JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request, monitorMessageEntity.getRequestLogSwitch())),
                        getRequestAttributeMap(monitorMessageEntity, request),
                        getModelAndViewLogInfo(monitorMessageEntity, modelAndView),
                        formatDuration(useTime),
                        logicOperator,
                        performanceThreshold);
    }

    /**
     * 获得 model and view log info.
     *
     * @param monitorMessageEntity
     *            the monitor message entity
     * @param modelAndView
     *            the model and view
     * @return the model and view log info
     * @since 1.4.0
     */
    private static String getModelAndViewLogInfo(MonitorMessageEntity monitorMessageEntity,ModelAndView modelAndView){
        if (isNullOrEmpty(modelAndView)){
            return "modelAndView is null!!";
        }

        Map<String, Object> model = modelAndView.getModel();
        String viewName = ModelAndViewUtil.getViewName(modelAndView);

        return Slf4jUtil.format(
                        "model:[{}],view:[{}]",
                        JsonUtil.formatSimpleMap(model, monitorMessageEntity.getAllowFormatClassTypes()),
                        viewName);
    }

    /**
     * 获得 request attribute map.
     *
     * @param monitorMessageEntity
     *            the monitor message entity
     * @param request
     *            the request
     * @return the request attribute map
     */
    private static String getRequestAttributeMap(MonitorMessageEntity monitorMessageEntity,HttpServletRequest request){
        Map<String, Object> attributeMap = RequestUtil.getAttributeMap(request);

        //剔除掉不常用的属性 不需要care 避免输出过多的日志
        MapUtil.removeKeys(
                        attributeMap,
                        MonitorInterceptor.STOPWATCH_ATTRIBUTE,
                        org.springframework.web.servlet.DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                        org.springframework.web.context.request.RequestContextListener.class.getName() + ".REQUEST_ATTRIBUTES",
                        org.springframework.web.context.request.async.WebAsyncUtils.WEB_ASYNC_MANAGER_ATTRIBUTE,
                        org.springframework.web.servlet.DispatcherServlet.THEME_RESOLVER_ATTRIBUTE,
                        org.springframework.web.servlet.DispatcherServlet.THEME_SOURCE_ATTRIBUTE);

        return JsonUtil.formatSimpleMap(attributeMap, monitorMessageEntity.getAllowFormatClassTypes());
    }
}
