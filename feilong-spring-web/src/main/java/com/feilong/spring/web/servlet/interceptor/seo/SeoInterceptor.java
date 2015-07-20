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
package com.feilong.spring.web.servlet.interceptor.seo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.feilong.core.date.DateExtensionUtil;
import com.feilong.core.lang.ClassUtil;
import com.feilong.core.tools.jsonlib.JsonUtil;
import com.feilong.servlet.http.RequestUtil;

/**
 * 专门处理每个页面的seo信息,在 {@link HandlerInterceptorAdapter#preHandle(HttpServletRequest, HttpServletResponse, Object)}流程中,查找 request作用域中的数据.
 * 
 * <h3>使用指南:</h3>
 * 
 * <blockquote>
 * <ol>
 * 
 * <li>
 * Model1:什么都不设置,那么使用默认配置的 {@link #defaultSeoTitle},{@link #defaultSeoKeywords},{@link #defaultSeoDescription},如果这些参数也没有设置,那么页面相关地方会输出空</li>
 * 
 * <li>
 * Model2:可以在controller {@link RequestMapping} 方法体里面,使用
 * 
 * <pre>
 * SeoViewCommand defaultSeoViewCommand = new DefaultSeoViewCommand();
 * defaultSeoViewCommand.setSeoDescription(xxx);
 * defaultSeoViewCommand.setSeoKeywords(xxx);
 * defaultSeoViewCommand.setSeoTitle(xxx);
 * </pre>
 * 
 * 自定义设置一个SeoViewCommand对象, 然后,将此对象 设置到 request/model中
 * <li>
 * 
 * <li>
 * Model3:如果使用了 {@link ViewCommand}作为整体数据返回,那么只需要让 您自己的{@link ViewCommand} 实现 {@link SeoViewCommand}接口,实现里面的方法即可</li>
 * 
 * <li>
 * Model4:如果使用了 {@link ViewCommand}作为整体数据返回,并且也使用了 {@link SubViewCommand},并且想将参数设置到该{@link SubViewCommand}内,你可以让 您自己的{@link SubViewCommand}
 * 实现 {@link SeoViewCommand}接口,实现里面的方法即可</li>
 * </ol>
 * </blockquote>
 *
 * @author feilong
 * @version 1.2.2 2015年7月14日 下午8:30:14
 * @since 1.2.2
 */
public class SeoInterceptor extends HandlerInterceptorAdapter{

    /** The Constant LOGGER. */
    private static final Logger LOGGER                             = LoggerFactory.getLogger(SeoInterceptor.class);

    /** request作用域,关于 SEOVIEWCOMMAND 属性的里面的值. */
    private static final String REQUEST_ATTRIBUTE_SEOVIEWCOMMAND   = "seoViewCommand";

    /** 您可以修改seoViewCommand在 作用域里面的名称,默认是 {@link #REQUEST_ATTRIBUTE_SEOVIEWCOMMAND}. */
    private String              seoViewCommandRequestAttributeName = REQUEST_ATTRIBUTE_SEOVIEWCOMMAND;

    /** The default seo title. */
    private String              defaultSeoTitle;

    /** 默认的 SeoKeywords. */
    private String              defaultSeoKeywords;

    /** 默认的 SeoDescription. */
    private String              defaultSeoDescription;

    //TODO map cache 
    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView)
                    throws Exception{

        Date beginDate = new Date();

        Map<String, Object> dataMap = getDataMap(request, modelAndView);

        boolean isFindSeoViewCommand = false;
        for (Map.Entry<String, Object> entry : dataMap.entrySet()){
            String requestAttributeName = entry.getKey();
            Object requestAttributeValue = entry.getValue();
            //XXX seoViewCommandRequestAttributeName 优先
            //*************************如果有 seoViewCommandRequestAttributeName变量,那么log 并且直接跳出*****************************************
            if (requestAttributeName.equals(seoViewCommandRequestAttributeName)){
                if (LOGGER.isInfoEnabled()){
                    LOGGER.info(
                                    "find attributeName:[{}] in map,value is:{},break and go-on",
                                    seoViewCommandRequestAttributeName,
                                    JsonUtil.format(requestAttributeValue));
                }
                break;
            }

            ///********************findSeoViewCommand*********************************
            SeoViewCommand seoViewCommand = findSeoViewCommand(requestAttributeName, requestAttributeValue);

            if (null != seoViewCommand){
                request.setAttribute(seoViewCommandRequestAttributeName, seoViewCommand);

                if (LOGGER.isInfoEnabled()){
                    LOGGER.info(
                                    "set seoViewCommand to request,attributeName is:[{}],value is:{}",
                                    seoViewCommandRequestAttributeName,
                                    JsonUtil.format(seoViewCommand));
                }

                isFindSeoViewCommand = true;
                break;
            }
        }

        if (!isFindSeoViewCommand){
            LOGGER.debug("can not find SeoViewCommand object in total request attribute");

            SeoViewCommand defaultSeoViewCommand = constructDefaultSeoViewCommand();

            request.setAttribute(seoViewCommandRequestAttributeName, defaultSeoViewCommand);

            if (LOGGER.isInfoEnabled()){
                LOGGER.info(
                                "set defaultSeoViewCommand to request,attributeName is:[{}],value is:{}",
                                seoViewCommandRequestAttributeName,
                                JsonUtil.format(defaultSeoViewCommand));
            }
        }

        Date endDate = new Date();
        LOGGER.info("use time:{}", DateExtensionUtil.getIntervalForView(beginDate, endDate));
    }

    /**
     * 获得 data map.
     *
     * @param request
     *            the request
     * @param modelAndView
     *            the ModelAndView that the handler returned (can also be null) <br>
     *            with the name of the view and the required model data, or null if the request has been handled directly
     * @return the data map
     */
    private Map<String, Object> getDataMap(HttpServletRequest request,ModelAndView modelAndView){
        if (null == modelAndView){
            LOGGER.warn("modelAndView is null,request info:[{}]", JsonUtil.format(RequestUtil.getRequestInfoMapForLog(request)));
        }
        Map<String, Object> model = (null == modelAndView) ? null : modelAndView.getModel();
        Map<String, Object> attributeMap = RequestUtil.getAttributeMap(request);

        //新创建个map对象, 这样操作不会影响原始数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.putAll(model);
        map.putAll(attributeMap);

        return map;
    }

    /**
     * Constract default seo view command.
     *
     * @return the seo view command
     */
    private SeoViewCommand constructDefaultSeoViewCommand(){
        //XXX 可以抽取 提高性能
        SeoViewCommand defaultSeoViewCommand = new DefaultSeoViewCommand();
        defaultSeoViewCommand.setSeoDescription(defaultSeoDescription);
        defaultSeoViewCommand.setSeoKeywords(defaultSeoKeywords);
        defaultSeoViewCommand.setSeoTitle(defaultSeoTitle);
        return defaultSeoViewCommand;
    }

    /**
     * Find seo view command.
     *
     * @param requestAttributeName
     *            the request attribute name
     * @param requestAttributeValue
     *            the request attribute value
     * @return the seo view command
     */
    private SeoViewCommand findSeoViewCommand(String requestAttributeName,Object requestAttributeValue){
        if (ClassUtil.isInstance(requestAttributeValue, SeoViewCommand.class)){
            return (SeoViewCommand) requestAttributeValue;
        }
        return consttructSeoViewCommand(requestAttributeName, requestAttributeValue);
    }

    /**
     * Consttruct seo view command.
     *
     * @param requestAttributeName
     *            the request attribute name
     * @param requestAttributeValue
     *            the request attribute value
     * @return the seo view command
     * @since 1.2.2
     */
    protected SeoViewCommand consttructSeoViewCommand(String requestAttributeName,Object requestAttributeValue){
        return null;
    }

    /**
     * 设置 您可以修改seoViewCommand在 作用域里面的名称,默认是 {@link #REQUEST_ATTRIBUTE_SEOVIEWCOMMAND}.
     *
     * @param seoViewCommandRequestAttributeName
     *            the seoViewCommandRequestAttributeName to set
     */
    public void setSeoViewCommandRequestAttributeName(String seoViewCommandRequestAttributeName){
        this.seoViewCommandRequestAttributeName = seoViewCommandRequestAttributeName;
    }

    /**
     * 设置 default seo title.
     *
     * @param defaultSeoTitle
     *            the defaultSeoTitle to set
     */
    public void setDefaultSeoTitle(String defaultSeoTitle){
        this.defaultSeoTitle = defaultSeoTitle;
    }

    /**
     * 设置 默认的 SeoKeywords.
     *
     * @param defaultSeoKeywords
     *            the defaultSeoKeywords to set
     */
    public void setDefaultSeoKeywords(String defaultSeoKeywords){
        this.defaultSeoKeywords = defaultSeoKeywords;
    }

    /**
     * 设置 默认的 SeoDescription.
     *
     * @param defaultSeoDescription
     *            the defaultSeoDescription to set
     */
    public void setDefaultSeoDescription(String defaultSeoDescription){
        this.defaultSeoDescription = defaultSeoDescription;
    }
}