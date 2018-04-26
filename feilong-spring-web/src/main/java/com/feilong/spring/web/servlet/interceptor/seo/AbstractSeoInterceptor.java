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

import static com.feilong.core.date.DateExtensionUtil.formatDuration;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.feilong.core.lang.ClassUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.spring.web.servlet.LocaleUtil;
import com.feilong.spring.web.servlet.ModelAndViewUtil;
import com.feilong.spring.web.servlet.interceptor.AbstractHandlerMethodInterceptorAdapter;

/**
 * Seo 的核心实现.
 * 
 * <h3>实现原理:</h3>
 * <blockquote>
 * <ol>
 * 
 * <li>先判断自定义的 {@link SeoViewCommandBuilder}, 如果注入了自定义的 {@link SeoViewCommandBuilder}, 并且返回值不是null, 那么将优先使用该结果</li>
 * <li>{@link #findSeoViewCommandFromRequestAndModelAttributeMap(HttpServletRequest, ModelAndView)} 从request 以及 model中提取
 * 
 * <p>
 * 步骤:
 * <ol>
 * <li>如果发现属性名字 是 {@link #seoViewCommandRequestAttributeName},那么将 <code>attributeValue</code>强转 {@link SeoViewCommand},并返回</li>
 * <li>如果发现属性值 是 {@link SeoViewCommand}类型,那么转成 {@link SeoViewCommand},并返回</li>
 * <li>支持 通过实现 {@link #buildSeoViewCommandFromRequestAttributeValue(String, Object)}扩展,自己来查找/构建{@link SeoViewCommand}</li>
 * </ol>
 * </li>
 * 
 * <li>{@link #detectSeoViewCommand(SeoViewCommand, HttpServletRequest)} 检测上面得到的 {@link SeoViewCommand},如果发现上面结果是null,则返回默认的
 * {@link #buildDefaultSeoViewCommand(HttpServletRequest)},如果上面返回的对象,有属性参数是null或者empty,则自动填充默认的数据</li>
 * 
 * <li>将整理之后的{@link SeoViewCommand} 放入 request作用域</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.0
 */
public abstract class AbstractSeoInterceptor extends AbstractHandlerMethodInterceptorAdapter{

    /** The Constant LOGGER. */
    private static final Logger   LOGGER                             = LoggerFactory.getLogger(AbstractSeoInterceptor.class);

    //---------------------------------------------------------------

    /** request作用域,关于 SEOVIEWCOMMAND 属性的里面的值. */
    private static final String   REQUEST_ATTRIBUTE_SEOVIEWCOMMAND   = "seoViewCommand";

    /** 您可以修改seoViewCommand在 作用域里面的名称,默认是 {@link #REQUEST_ATTRIBUTE_SEOVIEWCOMMAND}. */
    private String                seoViewCommandRequestAttributeName = REQUEST_ATTRIBUTE_SEOVIEWCOMMAND;

    //---------------------------------------------------------------
    /**
     * 自定义的 SeoViewCommandBuilder,如果配置了有限使用.
     * 
     * <p>
     * 比如 bluebox 需要不同的店铺配置不同的seo信息
     * </p>
     * 
     * @since 1.11.1
     */
    private SeoViewCommandBuilder seoViewCommandBuilder;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.AbstractHandlerMethodInterceptorAdapter#doPostHandle(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void doPostHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView){
        Date beginDate = new Date();

        //---------------------------------------------------------------

        SeoViewCommand seoViewCommand = build(request, modelAndView);

        seoViewCommand = detect(seoViewCommand, request);

        if (LOGGER.isTraceEnabled()){
            LOGGER.trace(
                            "set seoViewCommand to request,attributeName:[{}],value :{}",
                            seoViewCommandRequestAttributeName,
                            JsonUtil.format(seoViewCommand));
        }

        //---------------------------------------------------------------

        request.setAttribute(seoViewCommandRequestAttributeName, seoViewCommand);

        //---------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("use time:[{}]", formatDuration(beginDate));
        }
    }

    //---------------------------------------------------------------

    /**
     * Detect.
     *
     * @param seoViewCommand
     *            the seo view command
     * @param request
     *            the request
     * @return the seo view command
     * @since 1.11.1
     */
    private SeoViewCommand detect(SeoViewCommand seoViewCommand,HttpServletRequest request){
        //解析,如果有些参数没有值,将采用默认的 
        SeoViewCommand defaultSeoViewCommand = buildDefaultSeoViewCommand(request);
        return SeoViewCommandDetector.detect(seoViewCommand, defaultSeoViewCommand);
    }

    //---------------------------------------------------------------

    /**
     * seoViewCommandBuilder > Request And Model attribute> default.
     *
     * @param request
     *            the request
     * @param modelAndView
     *            the model and view
     * @return the seo view command
     * @since 1.11.1
     */
    private SeoViewCommand build(HttpServletRequest request,ModelAndView modelAndView){
        //since 1.11.1
        if (null != seoViewCommandBuilder){
            return seoViewCommandBuilder.build(LocaleUtil.getLocale(), request);
        }

        //查找
        return findSeoViewCommandFromRequestAndModelAttributeMap(request, modelAndView);
    }

    //---------------------------------------------------------------

    /**
     * Find seo view command from request and model and view.
     *
     * @param request
     *            the request
     * @param modelAndView
     *            the model and view
     * @return the seo view command
     * @since 1.11.1 rename
     */
    private SeoViewCommand findSeoViewCommandFromRequestAndModelAttributeMap(HttpServletRequest request,ModelAndView modelAndView){
        Map<String, Object> requestAndModelAttributeMap = ModelAndViewUtil.getRequestAndModelAttributeMap(request, modelAndView);

        //---------------------------------------------------------------

        for (Map.Entry<String, Object> entry : requestAndModelAttributeMap.entrySet()){
            String attributeName = entry.getKey();
            Object attributeValue = entry.getValue();

            //---------------------如果有 seoViewCommandRequestAttributeName变量,那么log 并且直接跳出-----------------------------------
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

            //------------------------findSeoViewCommand---------------------------------------

            if (ClassUtil.isInstance(attributeValue, SeoViewCommand.class)){
                return (SeoViewCommand) attributeValue;
            }

            //---------------------------------------------------------------

            SeoViewCommand seoViewCommand = buildSeoViewCommandFromRequestAttributeValue(attributeName, attributeValue);
            if (null != seoViewCommand){
                return seoViewCommand;
            }
        }
        return null;
    }

    //---------------------------------------------------------------

    /**
     * 构造默认的DefaultSeoViewCommand.
     *
     * @param request
     *            the request
     * @return the seo view command
     */
    protected abstract SeoViewCommand buildDefaultSeoViewCommand(HttpServletRequest request);

    //---------------------------------------------------------------

    /**
     * 提供从 <code>requestAttributeValue</code>中提取/构造 {@link SeoViewCommand}的扩展点.
     * 
     * <p>
     * 如果结果不是null,那么使用当前的结果<br>
     * 如果结果是null,那么将处理下一个 requestAttributeName
     * </p>
     *
     * @param requestAttributeName
     *            request 作用域 属性名称
     * @param requestAttributeValue
     *            request 作用域 属性值
     * @return the seo view command
     */
    protected SeoViewCommand buildSeoViewCommandFromRequestAttributeValue(String requestAttributeName,Object requestAttributeValue){
        return null;
    }

    //---------------------------------------------------------------

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
     * 设置 自定义的 SeoViewCommandBuilder,如果配置了有限使用.
     *
     * @param seoViewCommandBuilder
     *            the seoViewCommandBuilder to set
     * @since 1.11.1
     */
    public void setSeoViewCommandBuilder(SeoViewCommandBuilder seoViewCommandBuilder){
        this.seoViewCommandBuilder = seoViewCommandBuilder;
    }

}