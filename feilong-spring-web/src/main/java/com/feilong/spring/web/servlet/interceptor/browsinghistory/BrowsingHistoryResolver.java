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

import java.io.Serializable;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feilong.spring.web.servlet.interceptor.browsinghistory.command.BrowsingHistoryCommand;

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
 * <pre class="code">
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
 * @see com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryInterceptor
 * @since 1.2.2
 */
public interface BrowsingHistoryResolver{

    /**
     * 添加历史浏览记录.
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
     * 获得所有存储的历史浏览记录.
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

    /**
     * 获得历史浏览的历史记录,剔除掉指定的id.
     * 
     * <p>
     * 该方法适用于PDP页面, 从历史浏览记录中,把自己去掉显示
     * </p>
     *
     * @param <T>
     *            the generic type
     * @param request
     *            the request
     * @param excludeId
     *            指定去掉的id
     * @param klass
     *            the klass
     * @return 如果没有历史浏览记录,返回 null;<br>
     *         如果历史记录中有 {@code excludeId},那么返回剔除之后的数据;<br>
     *         如果历史记录中没有 {@code excludeId},那么返回原样的历史浏览数据;
     * @since 1.5.4
     */
    <T extends Serializable> LinkedList<T> getBrowsingHistoryExcludeId(HttpServletRequest request,Serializable excludeId,Class<T> klass);

    /**
     * 清空浏览的历史记录.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @since 1.5.4
     */
    void clear(HttpServletRequest request,HttpServletResponse response);
}
