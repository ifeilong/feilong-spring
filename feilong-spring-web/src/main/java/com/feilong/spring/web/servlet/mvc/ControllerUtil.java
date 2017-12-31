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
package com.feilong.spring.web.servlet.mvc;

import static com.feilong.servlet.http.HttpHeaders.X_REQUESTED_WITH;
import static com.feilong.servlet.http.HttpHeaders.X_REQUESTED_WITH_VALUE_AJAX;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.FORWARD_URL_PREFIX;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

import org.apache.commons.lang3.Validate;

/**
 * Controller 相关常用工具方法.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.stereotype.Controller
 * @see org.springframework.web.servlet.mvc.Controller
 * @since 1.10.6
 */
public final class ControllerUtil{

    /** ajax 头. */
    public static final String HEADER_WITH_AJAX_SPRINGMVC = X_REQUESTED_WITH + "=" + X_REQUESTED_WITH_VALUE_AJAX;

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private ControllerUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 生成spring 的跳转路径.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * e.g. redirect("/shoppingcart")= "redirect:/shoppingcart";
     * 
     * <br>
     * 
     * 注:不需要你手工的拼接request.getContextPath()
     * </blockquote>
     * 
     * @param targetUrl
     *            如果是相对根目录路径,只需要传递 如"/shoppingcart" spring会自动添加request.getContextPath() <br>
     *            也可以传入绝对路径 如:http://www.baidu.com
     * @return 如果 <code>targetUrl</code> 是null,抛出 {@link NullPointerException}<br>
     */
    public static String redirect(String targetUrl){
        Validate.notNull(targetUrl, "targetUrl can't be null!");

        return REDIRECT_URL_PREFIX + targetUrl;
    }

    /**
     * 生成 spring Forward 路径.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * e.g. forward("/shoppingcart")= "forward:/shoppingcart";
     * 
     * <br>
     * 
     * 注:不需要你手工的拼接request.getContextPath()
     * </blockquote>
     * 
     * @param forwardUrl
     *            the forward url
     * @return 如果 <code>forwardUrl</code> 是null,抛出 {@link NullPointerException}<br>
     */
    public static String forward(String forwardUrl){
        Validate.notNull(forwardUrl, "forwardUrl can't be null!");

        return FORWARD_URL_PREFIX + forwardUrl;
    }

}
