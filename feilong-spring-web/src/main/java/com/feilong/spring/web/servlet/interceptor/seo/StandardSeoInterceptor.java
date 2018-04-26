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

import static com.feilong.core.Validator.isNotNullOrEmpty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 专门处理每个页面的seo信息,在 {@link HandlerInterceptorAdapter#preHandle(HttpServletRequest, HttpServletResponse, Object)}流程中,查找 request作用域中的数据.
 * 
 * <h3>配置方式:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 提供了两种类型的配置方式, 第一种,配置默认的 {@link #defaultSeoViewCommand}对象,比如:
 * </p>
 * 
 * <pre class="code">
 * {@code 
 * 
    <util:properties id="p_seo" location="classpath:config/seo.properties"></util:properties>

    <mvc:interceptors>

        <mvc:interceptor>

            <!-- 排除掉 json请求 -->
            <mvc:mapping path="/**" />
            <mvc:exclude-mapping path="/**}/*{@code .json" />

            <bean class="com.baozun.store.web.interceptor.StoreSeoInterceptor">
                <!-- seoViewCommand在 作用域里面的名称,默认是 seoViewCommand,不建议修改 -->
                <property name="seoViewCommandRequestAttributeName" value="seoViewCommand" />

                <property name="defaultSeoViewCommand">
                    <bean class="com.feilong.spring.web.servlet.interceptor.seo.DefaultSeoViewCommand">
                        <property name="seoTitle" value="}#${p_seo['seo.defaultSeoTitle']}{@code" />
                        <property name="seoKeywords" value="}#${p_seo['seo.defaultSeoKeywords']}{@code" />
                        <property name="seoDescription" value="}#${p_seo['seo.defaultSeoDescription']}{@code" />
                    </bean>
                </property>
            </bean>
        </mvc:interceptor>
    </mvc:interceptors>
 * }
 * </pre>
 * 
 * <p>
 * 第二种,配置 {@link #keyNameSeoTitle},{@link #keyNameSeoKeywords},{@link #keyNameSeoDescription},比如:
 * </p>
 * 
 * <pre class="code">
 * {@code 
 *  <mvc:interceptors>

        <mvc:interceptor>

            <!-- 排除掉 json请求 -->
            <mvc:mapping path="/**" />
            <mvc:exclude-mapping path="/**}/*{@code .json" />

            <bean class="com.feilong.spring.web.servlet.interceptor.seo.StandardSeoInterceptor">
                <!-- seoViewCommand在 作用域里面的名称,默认是 seoViewCommand,不建议修改 -->
                <property name="seoViewCommandRequestAttributeName" value="seoViewCommand" />
                <property name="keyNameSeoTitle" value="seo.defaultSeoTitle" />
                <property name="keyNameSeoKeywords" value="seo.defaultSeoKeywords" />
                <property name="keyNameSeoDescription" value="seo.defaultSeoDescription" />

            </bean>
        </mvc:interceptor>
    </mvc:interceptors>
 * }
 * </pre>
 * 
 * <p>
 * 两种方式中,以 {@link #keyNameSeoTitle},{@link #keyNameSeoKeywords},{@link #keyNameSeoDescription}优先:
 * </p>
 * 
 * </blockquote>
 * 
 * <h3>使用指南:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>Model1:什么都不设置,那么使用默认配置的 {@link #defaultSeoViewCommand},如果这些参数也没有设置,那么页面相关地方会输出空</li>
 * <li>Model2:可以在controller {@link RequestMapping} 方法体里面,使用
 * 
 * <pre class="code">
 * SeoViewCommand defaultSeoViewCommand = new DefaultSeoViewCommand();
 * defaultSeoViewCommand.setSeoDescription(xxx);
 * defaultSeoViewCommand.setSeoKeywords(xxx);
 * defaultSeoViewCommand.setSeoTitle(xxx);
 * </pre>
 * 
 * 自定义设置一个SeoViewCommand对象, 然后,将此对象 设置到 request/model中</li>
 * 
 * </ol>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.0
 */
public class StandardSeoInterceptor extends AbstractSeoInterceptor{

    //---------------------------------------------------------------
    //
    //keyNameSeoTitle  keyNameSeoKeywords keyNameSeoDescription 优先级 高于 defaultSeoViewCommand
    //
    //---------------------------------------------------------------

    /** 资源文件中的 seo title key. */
    private String             keyNameSeoTitle;

    /** 资源文件中的 seo Keywords key. */
    private String             keyNameSeoKeywords;

    /** 资源文件中的 seo Description key. */
    private String             keyNameSeoDescription;

    //---------------------------------------------------------------

    /** The default seo view command. */
    private SeoViewCommand     defaultSeoViewCommand;

    //---------------------------------------------------------------

    /** The application context. */
    @Autowired
    private ApplicationContext applicationContext;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.seo.AbstractSeoInterceptor#buildDefaultSeoViewCommand(javax.servlet.http.
     * HttpServletRequest)
     */
    @Override
    protected SeoViewCommand buildDefaultSeoViewCommand(HttpServletRequest request){
        //这里有国际化
        boolean isMessageConfig = isNotNullOrEmpty(keyNameSeoTitle) && isNotNullOrEmpty(keyNameSeoKeywords)
                        && isNotNullOrEmpty(keyNameSeoDescription);
        if (isMessageConfig){
            return SeoViewCommandFromMessageBuilder.build(applicationContext, keyNameSeoTitle, keyNameSeoKeywords, keyNameSeoDescription);
        }

        //---------------------------------------------------------------
        return defaultSeoViewCommand;
    }

    //---------------------------------------------------------------

    /**
     * 设置 default seo view command.
     *
     * @param defaultSeoViewCommand
     *            the defaultSeoViewCommand to set
     */
    public void setDefaultSeoViewCommand(SeoViewCommand defaultSeoViewCommand){
        this.defaultSeoViewCommand = defaultSeoViewCommand;
    }

    /**
     * 设置 资源文件中的 seo title key.
     *
     * @param keyNameSeoTitle
     *            the keyNameSeoTitle to set
     */
    public void setKeyNameSeoTitle(String keyNameSeoTitle){
        this.keyNameSeoTitle = keyNameSeoTitle;
    }

    /**
     * 设置 资源文件中的 seo Keywords key.
     *
     * @param keyNameSeoKeywords
     *            the keyNameSeoKeywords to set
     */
    public void setKeyNameSeoKeywords(String keyNameSeoKeywords){
        this.keyNameSeoKeywords = keyNameSeoKeywords;
    }

    /**
     * 设置 资源文件中的 seo Description key.
     *
     * @param keyNameSeoDescription
     *            the keyNameSeoDescription to set
     */
    public void setKeyNameSeoDescription(String keyNameSeoDescription){
        this.keyNameSeoDescription = keyNameSeoDescription;
    }

}