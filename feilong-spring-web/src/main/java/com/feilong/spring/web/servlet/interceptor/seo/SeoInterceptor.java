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
package com.feilong.spring.web.servlet.interceptor.seo;

import java.util.Date;
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
import com.feilong.core.util.Validator;
import com.feilong.spring.web.servlet.ModelAndViewUtil;
import com.feilong.spring.web.servlet.interceptor.AbstractHandlerInterceptorAdapter;
import com.feilong.web.command.SubViewCommand;
import com.feilong.web.command.ViewCommand;

/**
 * 专门处理每个页面的seo信息,在 {@link HandlerInterceptorAdapter#preHandle(HttpServletRequest, HttpServletResponse, Object)}流程中,查找 request作用域中的数据.
 * 
 * <h3>使用指南:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>Model1:什么都不设置,那么使用默认配置的 {@link #defaultSeoViewCommand},如果这些参数也没有设置,那么页面相关地方会输出空</li>
 * <li>Model2:可以在controller {@link RequestMapping} 方法体里面,使用
 * 
 * <pre>
 * SeoViewCommand defaultSeoViewCommand = new DefaultSeoViewCommand();
 * defaultSeoViewCommand.setSeoDescription(xxx);
 * defaultSeoViewCommand.setSeoKeywords(xxx);
 * defaultSeoViewCommand.setSeoTitle(xxx);
 * </pre>
 * 
 * 自定义设置一个SeoViewCommand对象, 然后,将此对象 设置到 request/model中</li>
 * 
 * <li>Model3:如果使用了 {@link ViewCommand}作为整体数据返回,那么只需要让 您自己的{@link ViewCommand} 实现 {@link SeoViewCommand}接口,实现里面的方法即可</li>
 * 
 * <li>Model4:如果使用了 {@link ViewCommand}作为整体数据返回,并且也使用了 {@link SubViewCommand},并且想将参数设置到该{@link SubViewCommand}内,你可以让 您自己的
 * {@link SubViewCommand} 实现 {@link SeoViewCommand}接口,实现里面的方法即可</li>
 * </ol>
 * </blockquote>
 *
 * @author feilong
 * @version 1.2.2 2015年7月14日 下午8:30:14
 * @since 1.2.2
 */
public class SeoInterceptor extends AbstractHandlerInterceptorAdapter{

    /** The Constant LOGGER. */
    private static final Logger LOGGER                             = LoggerFactory.getLogger(SeoInterceptor.class);

    /** request作用域,关于 SEOVIEWCOMMAND 属性的里面的值. */
    private static final String REQUEST_ATTRIBUTE_SEOVIEWCOMMAND   = "seoViewCommand";

    /** 您可以修改seoViewCommand在 作用域里面的名称,默认是 {@link #REQUEST_ATTRIBUTE_SEOVIEWCOMMAND}. */
    private String              seoViewCommandRequestAttributeName = REQUEST_ATTRIBUTE_SEOVIEWCOMMAND;

    /** The default seo view command. */
    private SeoViewCommand      defaultSeoViewCommand;

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

        //查找
        SeoViewCommand seoViewCommand = findSeoViewCommandFromRequestAndModelAndViewAttributeMap(request, modelAndView);

        //解析,如果有些参数没有值,将采用默认的 
        seoViewCommand = detectSeoViewCommand(seoViewCommand);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "set seoViewCommand to request,attributeName is:[{}],value is:{}",
                            seoViewCommandRequestAttributeName,
                            JsonUtil.format(seoViewCommand));
        }

        request.setAttribute(seoViewCommandRequestAttributeName, seoViewCommand);

        Date endDate = new Date();
        LOGGER.info("use time:{}", DateExtensionUtil.getIntervalForView(beginDate, endDate));
    }

    /**
     * 检测以下信息.
     * <ul>
     * <li>如果 null==seoViewCommand,return <code>defaultSeoViewCommand</code></li>
     * <li>如果Validator.isNullOrEmpty(seoViewCommand.getSeoDescription()),将设置<code>defaultSeoViewCommand</code>的Description</li>
     * <li>如果Validator.isNullOrEmpty(seoViewCommand.getSeoKeywords()),将设置 <code>defaultSeoViewCommand</code>的SeoKeywords</li>
     * <li>如果Validator.isNullOrEmpty(seoViewCommand.getSeoTitle()),将设置 <code>defaultSeoViewCommand</code>的SeoTitle</li>
     * </ul>
     *
     * @param seoViewCommand
     *            the seo view command
     * @return the seo view command
     * @since 1.4.0
     */
    private SeoViewCommand detectSeoViewCommand(SeoViewCommand seoViewCommand){
        if (null == seoViewCommand){
            LOGGER.debug("can not find SeoViewCommand object in Request And ModelAndView attribute,use defaultSeoViewCommand.");
            return defaultSeoViewCommand;
        }
        //SeoDescription
        if (Validator.isNullOrEmpty(seoViewCommand.getSeoDescription())){
            seoViewCommand.setSeoDescription(defaultSeoViewCommand.getSeoDescription());
        }
        //SeoKeywords
        if (Validator.isNullOrEmpty(seoViewCommand.getSeoKeywords())){
            seoViewCommand.setSeoKeywords(defaultSeoViewCommand.getSeoKeywords());
        }
        //SeoTitle
        if (Validator.isNullOrEmpty(seoViewCommand.getSeoTitle())){
            seoViewCommand.setSeoTitle(defaultSeoViewCommand.getSeoTitle());
        }
        return seoViewCommand;
    }

    /**
     * Find seo view command from request and model and view.
     *
     * @param request
     *            the request
     * @param modelAndView
     *            the model and view
     * @return the seo view command
     * @since 1.4.0
     */
    private SeoViewCommand findSeoViewCommandFromRequestAndModelAndViewAttributeMap(HttpServletRequest request,ModelAndView modelAndView){
        Map<String, Object> requestAndModelAttributeMap = ModelAndViewUtil.getRequestAndModelAttributeMap(request, modelAndView);

        for (Map.Entry<String, Object> entry : requestAndModelAttributeMap.entrySet()){
            String attributeName = entry.getKey();
            Object attributeValue = entry.getValue();

            //*************************如果有 seoViewCommandRequestAttributeName变量,那么log 并且直接跳出*****************************************
            if (attributeName.equals(seoViewCommandRequestAttributeName)){
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug(
                                    "find attributeName:[{}] in map,value is:{},break and go-on",
                                    seoViewCommandRequestAttributeName,
                                    JsonUtil.format(attributeValue));
                }

                // may be case exception,if somebody cover the seoViewCommandRequestAttributeName
                return (SeoViewCommand) attributeValue;
            }

            ///********************findSeoViewCommand*********************************
            if (ClassUtil.isInstance(attributeValue, SeoViewCommand.class)){
                return (SeoViewCommand) attributeValue;
            }

            SeoViewCommand seoViewCommand = constructSeoViewCommand(attributeName, attributeValue);
            if (null != seoViewCommand){
                return seoViewCommand;
            }
        }
        return null;
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
    protected SeoViewCommand constructSeoViewCommand(
                    @SuppressWarnings("unused") String requestAttributeName,
                    @SuppressWarnings("unused") Object requestAttributeValue){
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
     * 设置 default seo view command.
     *
     * @param defaultSeoViewCommand
     *            the defaultSeoViewCommand to set
     */
    public void setDefaultSeoViewCommand(SeoViewCommand defaultSeoViewCommand){
        this.defaultSeoViewCommand = defaultSeoViewCommand;
    }
}