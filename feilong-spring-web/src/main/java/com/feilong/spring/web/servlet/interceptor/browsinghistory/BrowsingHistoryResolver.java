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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.feilong.spring.web.servlet.interceptor.browsinghistory.command.BrowsingHistoryCommand;

/**
 * The Interface BrowsingHistory.
 * 
 * <h3>为什么要单独建个{@link BrowsingHistoryResolver}接口,逻辑不能直接 在 {@link BrowsingHistoryInterceptor}中处理吗?</h3>
 * 
 * <blockquote>
 * <p>
 * 原因在于, 我需要在controller 里面拿到历史记录, 如果所有的逻辑都在 {@link BrowsingHistoryInterceptor}中,那么我没有办法直接在controller中拿到历史记录
 * </p>
 * <p>
 * 现在, 你只需要这么做
 * </p>
 * 
 * <pre class="code">
 * &#64;Controller
 * public class RecommendationEngineController extends BaseController{
 * 
 *     &#64;Autowired
 *     private BrowsingHistoryResolver browsingHistoryResolver;
 * 
 *     &#64;ResponseBody
 *     &#64;ClientCache(value = SECONDS_PER_MINUTE * 5)
 *     &#64;RequestMapping(value = { "/item/{itemId}/recommendation.json" },method = RequestMethod.POST,headers = HEADER_WITH_AJAX_SPRINGMVC)
 *     public Map<RecommendationEngineType, List<RecommendationEngineCommand>> doHandler(
 *                     HttpServletRequest request,
 *                     HttpServletResponse response,
 *                     &#64;PathVariable("itemId") Long itemId){
 * 
 *         List{@code <Long>} browsingHistoryItemIds = browsingHistoryResolver.getBrowsingHistoryIdListExcludeId(request, response, itemId);
 *         //***
 *     }
 * }
 * </pre>
 * 
 * 就可以很方便的得到历史浏览记录了
 * 
 * </blockquote>
 * 
 * <h3>关于 打开/关闭浏览历史记录</h3>
 * 
 * <blockquote>
 * <p>
 * 打开/关闭浏览历史记录,参见 亚马逊,这个功能不在当前接口实现, 需要在拦截器里面搭配会员在DB存储的开关值,如果开启 ,此时
 * {@link #add(BrowsingHistoryCommand, HttpServletRequest, HttpServletResponse)} 可以将数据添加到历史记录中;<br>
 * false 的话,不管浏览什么商品将不会添加,但是不会影响其他的操作,比如 {@link #clear(HttpServletRequest, HttpServletResponse) clear},
 * {@link #getBrowsingHistory(HttpServletRequest, HttpServletResponse) getBrowsingHistory},
 * {@link #getBrowsingHistoryIdListExcludeId(Long, HttpServletRequest, HttpServletResponse) getBrowsingHistoryIdListExcludeId},
 * {@link #remove(Long, HttpServletRequest, HttpServletResponse) remove}
 * </p>
 * 
 * <p>
 * 通过该参数 来控制 {@link com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryInterceptor#isSupport(HttpServletRequest,
 * Object, ModelAndView) BrowsingHistoryInterceptor.isSupport} 方法
 * </p>
 * </blockquote>
 * 
 * <h3>关于 历史记录中可以保存哪些东西</h3>
 * 
 * <blockquote>
 * <p>
 * 目前默认的 {@link com.feilong.spring.web.servlet.interceptor.browsinghistory.command.DefaultBrowsingHistoryCommand
 * DefaultBrowsingHistoryCommand} 只是保存的 id,如果有特殊的需求,比如 //TODO 天猫的 我看过的 会记录时间, 可以自定义类 继承自
 * {@link com.feilong.spring.web.servlet.interceptor.browsinghistory.command.DefaultBrowsingHistoryCommand
 * DefaultBrowsingHistoryCommand} ,扩充需要的属性和字段
 * </p>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryInterceptor
 * @since 1.2.2
 */
//XXX 天猫的 我看过的 页面tag切换 都会自动更新
public interface BrowsingHistoryResolver{

    /**
     * 添加历史浏览记录.
     * 
     * <h3>流程</h3>
     * 
     * <blockquote>
     * <p>
     * <img src="http://venusdrogon.github.io/feilong-platform/mysource/store/添加浏览的历史记录.png"/>
     * </p>
     * </blockquote>
     * 
     * <p>
     * 拿到list之后,通常会加密处理,然后保存
     * </p>
     * 
     * @param browsingHistoryCommand
     *            the browsing history command,如果是null,则抛出异常
     * @param request
     *            the request
     * @param response
     *            the response
     *
     * @since 1.5.5
     */
    void add(BrowsingHistoryCommand browsingHistoryCommand,HttpServletRequest request,HttpServletResponse response);

    //****************************************************************************************************
    /**
     * 获得所有存储的历史浏览记录.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the browsing history
     * @since 1.5.5
     */
    List<BrowsingHistoryCommand> getBrowsingHistory(HttpServletRequest request,HttpServletResponse response);

    /**
     * 获得历史浏览的历史记录,剔除掉指定的id.
     * 
     * <p>
     * 该方法适用于PDP页面,从历史浏览记录中,需要把自己去掉显示
     * </p>
     * 
     * @param excludeId
     *            指定去掉的id
     * @param request
     *            the request
     * @param response
     *            the response
     *
     * @return 如果没有历史浏览记录,返回 null;<br>
     *         如果历史记录中有 {@code excludeId},那么返回剔除之后的数据;<br>
     *         如果历史记录中没有 {@code excludeId},那么返回原样的历史浏览数据;
     * @since 1.5.5
     */
    List<BrowsingHistoryCommand> getBrowsingHistoryExcludeId(Long excludeId,HttpServletRequest request,HttpServletResponse response);

    /**
     * 获得所有存储的历史浏览记录的 id list(适合于只care id的操作).
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the browsing history
     */
    List<Long> getBrowsingHistoryIdList(HttpServletRequest request,HttpServletResponse response);

    /**
     * 获得历史浏览的历史记录,剔除掉指定的id.
     * 
     * <p>
     * 该方法适用于PDP页面, 从历史浏览记录中,把自己去掉显示
     * </p>
     * 
     * @param excludeId
     *            指定去掉的id
     * @param request
     *            the request
     * @param response
     *            the response
     *
     * @return 如果没有历史浏览记录,返回 null;<br>
     *         如果历史记录中有 {@code excludeId},那么返回剔除之后的数据;<br>
     *         如果历史记录中没有 {@code excludeId},那么返回原样的历史浏览数据;
     * @since 1.5.5
     */
    List<Long> getBrowsingHistoryIdListExcludeId(Long excludeId,HttpServletRequest request,HttpServletResponse response);

    //************************************************************************************************
    /**
     * 根据id删除某个指定的历史浏览记录.
     * 
     * <p>
     * 亚马逊支持这种模式
     * </p>
     *
     * @param id
     *            the id
     * @param request
     *            the request
     * @param response
     *            the response
     * @since 1.5.5
     */
    void remove(Long id,HttpServletRequest request,HttpServletResponse response);

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
