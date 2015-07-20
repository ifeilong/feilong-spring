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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.feilong.core.date.TimeInterval;
import com.feilong.core.io.CharsetType;
import com.feilong.core.log.Slf4jUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.StringUtil;
import com.feilong.core.util.ToStringConfig;
import com.feilong.core.util.Validator;
import com.feilong.servlet.http.CookieUtil;
import com.feilong.servlet.http.entity.CookieEntity;
import com.feilong.tools.security.EncryptionException;
import com.feilong.tools.security.symmetric.SymmetricEncryption;

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
public abstract class BrowsingHistoryInterceptor extends HandlerInterceptorAdapter{

    /** The Constant log. */
    private static final Logger LOGGER             = LoggerFactory.getLogger(BrowsingHistoryInterceptor.class);

    /** The Constant DEFAULT_CONNECTOR. */
    private static final String DEFAULT_CONNECTOR  = "_";

    /** 默认的cookie名称. */
    private static final String DEFAULT_COOKIENAME = "f_b_h";

    /** The cookie name. */
    private String              cookieName         = DEFAULT_COOKIENAME;

    /** 单位秒 默认 3个月. */
    private int                 cookieMaxAge       = TimeInterval.SECONDS_PER_MONTH * 3;

    /** cookie编码. */
    private String              cookieCharsetName  = CharsetType.UTF8;

    /** 最大记录数量,超过的记录将被去掉. */
    private Integer             maxCount           = 5;

    /** 加密算法. */
    private SymmetricEncryption symmetricEncryption;

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

            if (Validator.isNotNullOrEmpty(browsingHistoryCommand)){
                try{
                    String cookieValue = constructItemBrowsingHistoryCookieValue(request, response, browsingHistoryCommand);

                    if (Validator.isNotNullOrEmpty(cookieValue)){
                        CookieEntity cookieEntity = new CookieEntity(cookieName, cookieValue, cookieMaxAge);
                        cookieEntity.setHttpOnly(true);
                        CookieUtil.addCookie(cookieEntity, response);
                    }
                }catch (Exception e){
                    LOGGER.error("constructItemBrowsingHistory Exception,but we need code go-on!", e);
                }
            }else{
                LOGGER.warn("browsingHistoryCommand is null or empty");
            }
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
     * Construct item browsing history cookie value.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param browsingHistoryCommand
     *            the browsing history command
     * @return if nothing to do ,then return null, such as in the cookie, first item is current item
     * @since 1.2.2
     */
    private String constructItemBrowsingHistoryCookieValue(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    BrowsingHistoryCommand browsingHistoryCommand){

        if (Validator.isNullOrEmpty(browsingHistoryCommand)){
            return null;
        }

        Serializable id = browsingHistoryCommand.getId();

        LinkedList<Serializable> linkedList = new LinkedList<Serializable>();

        Cookie cookie = CookieUtil.getCookie(request, cookieName);

        //如果cookie没有,表示第一次访问PDP页面 ,这时逻辑是构建一个往cookie 里加入
        if (Validator.isNullOrEmpty(cookie)){
            //如果没有 添加一个
            linkedList.add(id);
        }else{
            String value = cookie.getValue();
            try{
                String decryptHex = symmetricEncryption.decryptHex(value, cookieCharsetName);
                String[] tokenizeToStringArray = StringUtil.tokenizeToStringArray(decryptHex, DEFAULT_CONNECTOR);
                for (String string : tokenizeToStringArray){
                    linkedList.add(Long.parseLong(string));
                }

                Serializable first = linkedList.getFirst();
                //如果 list 里面的数据 第一个是当前item  那么一般表示刷新页面 或者重新打开新窗口
                //这种case 没有必要操作 cookie
                if (first.equals(id)){
                    LOGGER.info("in cookie, decryptHex:[{}] first pk is:[{}],current pk:[{}], nothing to do", decryptHex, first, id);
                    return null;
                }

                //如果有当前商品,那么删除掉 并将 当前的item id 塞第一个
                if (linkedList.contains(id)){
                    linkedList.remove(id);
                }
                linkedList.addFirst(id);

                //如果超长了 ,截取
                if (linkedList.size() > maxCount){
                    linkedList.subList(0, maxCount);
                }
            }catch (EncryptionException e){
                LOGGER.error(Slf4jUtil.formatMessage("decryptHex cookie error,value:{},cookieCharsetName:{}", value, cookieCharsetName), e);

                //如果出错了,那么就将cookie删掉
                CookieUtil.deleteCookie(cookieName, response);
                linkedList.add(id);
            }
        }

        //****************************************************************************
        //cookie value 是  itemid join--->aes hex 加密格式字符串
        ToStringConfig toStringConfig = new ToStringConfig(DEFAULT_CONNECTOR);
        String join = CollectionsUtil.toString(linkedList, toStringConfig);

        //如果cookie没有,表示第一次访问PDP页面 ,这时逻辑是构建一个往cookie 里加入
        String encryptHex = symmetricEncryption.encryptHex(join, cookieCharsetName);
        return encryptHex;
    }

    /**
     * 设置 the cookie name.
     *
     * @param cookieName
     *            the cookieName to set
     */
    public void setCookieName(String cookieName){
        this.cookieName = cookieName;
    }

    /**
     * 设置 单位秒 默认 3个月.
     *
     * @param cookieMaxAge
     *            the cookieMaxAge to set
     */
    public void setCookieMaxAge(int cookieMaxAge){
        this.cookieMaxAge = cookieMaxAge;
    }

    /**
     * 设置 cookie编码.
     *
     * @param cookieCharsetName
     *            the cookieCharsetName to set
     */
    public void setCookieCharsetName(String cookieCharsetName){
        this.cookieCharsetName = cookieCharsetName;
    }

    /**
     * 设置 最大记录数量,超过的记录将被去掉.
     *
     * @param maxCount
     *            the maxCount to set
     */
    public void setMaxCount(Integer maxCount){
        this.maxCount = maxCount;
    }

    /**
     * 设置 加密算法.
     *
     * @param symmetricEncryption
     *            the symmetricEncryption to set
     */
    public void setSymmetricEncryption(SymmetricEncryption symmetricEncryption){
        this.symmetricEncryption = symmetricEncryption;
    }
}
