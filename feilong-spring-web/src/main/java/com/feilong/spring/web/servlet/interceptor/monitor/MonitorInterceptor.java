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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.feilong.core.CharsetType;
import com.feilong.core.TimeInterval;
import com.feilong.core.Validator;
import com.feilong.core.date.DateExtensionUtil;
import com.feilong.core.util.MapUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.servlet.http.entity.RequestLogSwitch;
import com.feilong.spring.web.method.HandlerMethodUtil;
import com.feilong.spring.web.servlet.ModelAndViewUtil;
import com.feilong.spring.web.servlet.interceptor.AbstractHandlerInterceptorAdapter;
import com.feilong.tools.jsonlib.JsonUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 监控每个 {@link HandlerMethod}执行的时间, 输出log到日志,这些日志级别可以单独开启到专门的日志文件.
 * 
 * <h3>如果不想用</h3>
 * 
 * <blockquote>
 * 该Interceptor本身对性能的影响几乎为0, 如果不想使用, 可以在spring Interceptor配置文件中去掉该配置
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.2.2
 */
public class MonitorInterceptor extends AbstractHandlerInterceptorAdapter{

    /** The Constant LOGGER. */
    private static final Logger  LOGGER                = LoggerFactory.getLogger(MonitorInterceptor.class);

    /** 性能阀值<code>{@value}</code>,目前初步设置为1.5s,如果构建command 超过1.5s,会有 error log 记录. */
    private static final Integer PERFORMANCE_THRESHOLD = (int) (TimeInterval.MILLISECOND_PER_SECONDS * 1.5);

    /** The Constant STOPWATCH_ATTRIBUTE. */
    private static final String  STOPWATCH_ATTRIBUTE   = MonitorInterceptor.class.getName() + ".stopWatch";

    /**
     * 性能阀值,单位毫秒,默认 {@link #PERFORMANCE_THRESHOLD}.
     * 
     * @since 1.4.0
     */
    private Integer              performanceThreshold  = PERFORMANCE_THRESHOLD;

    /**
     * 允许被json log输出的 request {@code &&} model 对象类型, 默认只有基本类型以及数组才会被输出.
     * 
     * @see JsonUtil#formatSimpleMap(Map, Class...)
     * @since 1.4.0
     */
    private Class<?>[]           allowFormatClassTypes;

    /** The request log switch. */
    private RequestLogSwitch     requestLogSwitch;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
        if (!(handler instanceof HandlerMethod)){
            LOGGER.warn(
                            "request info:[{}],not [HandlerMethod],handler is [{}],What ghost~~,",
                            RequestUtil.getRequestFullURL(request, CharsetType.UTF8),
                            handler.getClass().getName());
            return true;
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        request.setAttribute(STOPWATCH_ATTRIBUTE, stopWatch);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "RequestInfoMapForLog:{},request attribute:{},start StopWatch",
                            JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request, requestLogSwitch)),
                            getRequestAttributeMap(request));
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

        if (!(handler instanceof HandlerMethod)){
            LOGGER.warn(
                            "request info:[{}],not [HandlerMethod],handler is [{}],What ghost~~,",
                            RequestUtil.getRequestFullURL(request, CharsetType.UTF8),
                            handler.getClass().getName());
            return;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        StopWatch stopWatch = getStopWatch(request);
        stopWatch.split();
        long useTime = stopWatch.getSplitTime();

        boolean isMoreThanPerformanceThreshold = useTime > performanceThreshold;

        try{
            //如果超过阀值, 那么以error的形式记录
            if (isMoreThanPerformanceThreshold){
                LOGGER.error(getPostHandleLogMessage(request, handlerMethod, modelAndView, useTime));

                //这里的request.getSession() 可能会报错 Cannot create a session after the response has been committed 
                //ServletContext servletContext = request.getSession().getServletContext();
            }else{
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug(getPostHandleLogMessage(request, handlerMethod, modelAndView, useTime));
                }
            }
        }catch (Exception e){//可能有异常,比如  往request/model里面设置了 不能被json处理的对象或者字段
            LOGGER.error(
                            Slf4jUtil.format(
                                            "postHandle [{}.{}()] occur exception,but we need goon!,just log it,request info:[{}]",
                                            HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
                                            HandlerMethodUtil.getHandlerMethodName(handlerMethod),
                                            JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request, requestLogSwitch))),
                            e);
        }
    }

    /**
     * 获得 post handle log message.
     *
     * @param request
     *            the request
     * @param handlerMethod
     *            the handler method
     * @param modelAndView
     *            the model and view
     * @param useTime
     *            the use time
     * @return the log message
     * @since 1.4.0
     */
    private String getPostHandleLogMessage(HttpServletRequest request,HandlerMethod handlerMethod,ModelAndView modelAndView,long useTime){
        String logicOperator = useTime > performanceThreshold ? ">" : useTime == performanceThreshold ? "=" : "<";

        //一条日志输出, 这样的话,在并发的情况, 日志还是有上下文的
        return Slf4jUtil.format(
                        "postHandle [{}.{}()],RequestInfoMapForLog:{},request attribute:{},modelAndView info:[{}],use time:[{}],[{}] performanceThreshold:[{}]",
                        HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
                        HandlerMethodUtil.getHandlerMethodName(handlerMethod),
                        JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request, requestLogSwitch)),
                        getRequestAttributeMap(request),
                        getModelAndViewLogInfo(modelAndView),
                        DateExtensionUtil.getIntervalForView(useTime),
                        logicOperator,
                        performanceThreshold);
    }

    /**
     * 获得 model and view log info.
     *
     * @param modelAndView
     *            the model and view
     * @return the model and view log info
     * @since 1.4.0
     */
    private String getModelAndViewLogInfo(ModelAndView modelAndView){
        if (Validator.isNullOrEmpty(modelAndView)){
            return "modelAndView is null!!";
        }

        Map<String, Object> model = modelAndView.getModel();
        String viewName = ModelAndViewUtil.getViewName(modelAndView);

        return Slf4jUtil.format("model:[{}],view:[{}]", JsonUtil.formatSimpleMap(model, allowFormatClassTypes), viewName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterConcurrentHandlingStarted(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{

        StopWatch stopWatch = getStopWatch(request);
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();

        if (LOGGER.isDebugEnabled()){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            LOGGER.debug(
                            "afterConcurrentHandlingStarted [{}.{}()], use time:[{}]",
                            HandlerMethodUtil.getDeclaringClassSimpleName(handlerMethod),
                            HandlerMethodUtil.getHandlerMethodName(handlerMethod),
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
        StopWatch stopWatch = getStopWatch(request);

        if (null == stopWatch || stopWatch.isStopped()){
            LOGGER.error("stopWatch is null or stopWatch isStopped!!,request info is:{}", RequestUtil.getRequestInfoMapForLog(request));
            return;
        }

        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();

        stopWatch.stop();
        long time = stopWatch.getTime();

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "afterCompletion [{}.{}()], use time:[{}],total time:[{}]",
                            HandlerMethodUtil.getDeclaringClassSimpleName((HandlerMethod) handler),
                            HandlerMethodUtil.getHandlerMethodName((HandlerMethod) handler),
                            DateExtensionUtil.getIntervalForView(splitTime),
                            DateExtensionUtil.getIntervalForView(time));
        }
    }

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

    /**
     * 获得 request attribute map.
     *
     * @param request
     *            the request
     * @return the request attribute map
     * @since 1.5.4
     */
    private String getRequestAttributeMap(HttpServletRequest request){
        Map<String, Object> attributeMap = RequestUtil.getAttributeMap(request);

        //剔除掉不常用的属性 不需要care 避免输出过多的日志
        MapUtil.removeKeys(
                        attributeMap,
                        STOPWATCH_ATTRIBUTE,
                        org.springframework.web.servlet.DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                        org.springframework.web.context.request.RequestContextListener.class.getName() + ".REQUEST_ATTRIBUTES",
                        org.springframework.web.context.request.async.WebAsyncUtils.WEB_ASYNC_MANAGER_ATTRIBUTE,
                        org.springframework.web.servlet.DispatcherServlet.THEME_RESOLVER_ATTRIBUTE,
                        org.springframework.web.servlet.DispatcherServlet.THEME_SOURCE_ATTRIBUTE);

        return JsonUtil.formatSimpleMap(attributeMap, allowFormatClassTypes);
    }

    //************************************************************************************************

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.core.Ordered#getOrder()
     */
    @Override
    public int getOrder(){
        return 99999;
    }

    /**
     * 允许被json log输出的 request {@code &&} model 对象类型, 默认只有基本类型以及数组才会被输出.
     *
     * @param allowFormatClassTypes
     *            the allowFormatClassTypes to set
     * @see JsonUtil#formatSimpleMap(Map, Class...)
     * @see JsonUtil#formatSimpleMap(Map, Class...)
     * @since 1.4.0
     */
    public void setAllowFormatClassTypes(Class<?>[] allowFormatClassTypes){
        this.allowFormatClassTypes = allowFormatClassTypes;
    }

    /**
     * 设置 性能阀值,单位毫秒,默认 {@link #PERFORMANCE_THRESHOLD}.
     *
     * @param performanceThreshold
     *            the performanceThreshold to set
     * @since 1.4.0
     */
    public void setPerformanceThreshold(Integer performanceThreshold){
        this.performanceThreshold = performanceThreshold;
    }

    /**
     * 设置 request log switch.
     *
     * @param requestLogSwitch
     *            the requestLogSwitch to set
     */
    public void setRequestLogSwitch(RequestLogSwitch requestLogSwitch){
        this.requestLogSwitch = requestLogSwitch;
    }
}