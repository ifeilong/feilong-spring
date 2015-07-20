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

import java.io.Serializable;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feilong.spring.web.servlet.interceptor.browsingHistory.command.BrowsingHistoryCommand;

/**
 * The Interface BrowsingHistory.
 * 
 * <h3>为什么要单独建个 {@link BrowsingHistoryResolver}接口, 逻辑不能直接 在 {@link BrowsingHistoryInterceptor}中处理吗?</h3>
 * 
 * <blockquote>
 * <p>
 * 原因在于, 我需要在controller 里面拿到历史记录, 如果所有的逻辑都在 {@link BrowsingHistoryInterceptor}中, 那么我没有办法直接在controller中拿到历史记录
 * </p>
 * <p>
 * 现在, 你只需要这么做
 * </p>
 * 
 * <pre>
 * &#64;Controller
 * public class StdRecommendationEngineController {
 * 
 *    &#64;Autowired
 *    private BrowsingHistoryResolver browsingHistoryResolver;
 * 
 * &#64;RequestMapping("******")
 *    public ModelAndView doHandler(HttpServletRequest request,HttpServletResponse response){
 *        LinkedList<Serializable> browsingHistory = browsingHistoryResolver.getBrowsingHistory(request);
 *       ......
 * </pre>
 * 
 * 就可以很方便的得到历史浏览记录了
 * 
 * </blockquote>
 *
 * @author feilong
 * @version 1.2.2 2015年7月20日 下午6:44:27
 * @see com.feilong.spring.web.servlet.interceptor.browsingHistory.BrowsingHistoryInterceptor
 * @since 1.2.2
 */
public interface BrowsingHistoryResolver{

    /**
     * 处理历史记录.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param browsingHistoryCommand
     *            the browsing history command
     */
    void resolveBrowsingHistory(HttpServletRequest request,HttpServletResponse response,BrowsingHistoryCommand browsingHistoryCommand);

    /**
     * 获得 browsing history.
     *
     * @param <T>
     *            the generic type
     * @param request
     *            the request
     * @param klass
     *            the klass
     * @return the browsing history
     */
    //TODO 设置成  BrowsingHistoryCommand
    <T extends Serializable> LinkedList<T> getBrowsingHistory(HttpServletRequest request,Class<T> klass);

}
