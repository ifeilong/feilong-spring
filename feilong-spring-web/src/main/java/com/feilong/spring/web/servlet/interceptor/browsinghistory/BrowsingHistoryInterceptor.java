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
package com.feilong.spring.web.servlet.interceptor.browsinghistory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.feilong.servlet.http.RequestUtil;
import com.feilong.servlet.http.entity.RequestLogSwitch;
import com.feilong.spring.web.servlet.interceptor.AbstractHandlerInterceptorAdapter;
import com.feilong.spring.web.servlet.interceptor.browsinghistory.command.BrowsingHistoryCommand;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * 商品浏览历史记录.
 * 
 * <h3>cookie里面放什么?</h3>
 * 
 * <blockquote>
 * <p>
 * itemid join--->aes hex 加密格式字符串
 * </p>
 * </blockquote>
 * 
 * <h3>why 不放以 name price image等组成的对象?</h3>
 * 
 * <blockquote>
 * 
 * <ul>
 * <li>太大了,cookie的length 也大, 影响性能</li>
 * <li>如果要做下架的商品不显示等业务控制,还需要再次解析</li>
 * </ul>
 * </blockquote>
 * 
 * 
 * <h3>如果只放itemId 一个值,渲染页面不是还要构造对象吗?</h3>
 * 
 * <blockquote>
 * <p>
 * 渲染可以通过 cache 提高性能
 * </p>
 * </blockquote>
 *
 * @author feilong
 * @version 1.2.2 2015年7月19日 下午7:22:00
 * @since 1.2.2
 */
public abstract class BrowsingHistoryInterceptor extends AbstractHandlerInterceptorAdapter{

    /** The Constant log. */
    private static final Logger     LOGGER = LoggerFactory.getLogger(BrowsingHistoryInterceptor.class);

    /** The browsing history resolver. */
    private BrowsingHistoryResolver browsingHistoryResolver;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView)
                    throws Exception{
        //是否支持解析,有可能在xml里面配置的一些不相关的路径透过到了这个拦截器
        //比如 配置的 mapping path 是 item/* 但是有一些url地址是 item/wishlist 诸如此类的也到了该拦截器
        boolean isSupport = isSupport(request, handler, modelAndView);
        if (isSupport){
            BrowsingHistoryCommand browsingHistoryCommand = constructBrowsingHistoryCommand(request, response, handler, modelAndView);

            if (null != browsingHistoryCommand){
                browsingHistoryResolver.add(browsingHistoryCommand, request, response);
            }else{
                LOGGER.debug(
                                "browsingHistoryCommand is null,don't add browsingHistory,request info:{}",
                                JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request, RequestLogSwitch.NORMAL)));
            }
        }else{
            if (LOGGER.isInfoEnabled()){
                LOGGER.info(
                                "current request:[{}] not support this BrowsingHistoryInterceptor,maybe you can config path in spring config 'mvc:exclude-mapping' node!",
                                JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request)));
            }
        }
    }

    /**
     * 当前请求,是否支持解析.
     *
     * @param request
     *            the request
     * @param handler
     *            the handler
     * @param modelAndView
     *            the model and view
     * @return true, if checks if is support
     * @since 1.5.3
     */
    protected abstract boolean isSupport(HttpServletRequest request,Object handler,ModelAndView modelAndView);

    /**
     * Construct browsing history command.
     * <p>
     * 如果返回 null or empty 那么不会操作cookie
     * </p>
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handler
     *            the handler
     * @param modelAndView
     *            the model and view
     * @return the browsing history command
     */
    protected abstract BrowsingHistoryCommand constructBrowsingHistoryCommand(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Object handler,
                    ModelAndView modelAndView);

    /**
     * 设置 the browsing history resolver.
     *
     * @param browsingHistoryResolver
     *            the browsingHistoryResolver to set
     */
    public void setBrowsingHistoryResolver(BrowsingHistoryResolver browsingHistoryResolver){
        this.browsingHistoryResolver = browsingHistoryResolver;
    }

}
