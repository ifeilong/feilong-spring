/*
 * Copyright (C) 2008 feilong (venusdrogon@163.com)
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
package com.feilong.spring.web.servlet.interceptor.browsingHistory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.feilong.spring.web.servlet.interceptor.AbstractHandlerInterceptorAdapter;
import com.feilong.spring.web.servlet.interceptor.browsingHistory.command.BrowsingHistoryCommand;

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

        if (handler instanceof HandlerMethod){
            BrowsingHistoryCommand browsingHistoryCommand = constructBrowsingHistoryCommand(request, response, handler, modelAndView);
            browsingHistoryResolver.resolveBrowsingHistory(request, response, browsingHistoryCommand);
        }
    }

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
    //TODO 暂没有默认实现
    //protected 支持子类重写
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
