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
package com.feilong.spring.messagesource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import com.feilong.json.JsonUtil;

/**
 * 支持通配符配置的 {@link ReloadableResourceBundleMessageSource}.
 * 
 * <h3>配置示例:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * {@code
 *     <bean id="messageSource" class="com.feilong.spring.messagesource.PathMatchingReloadableResourceBundleMessageSource">
 *         <property name="basenames">
 *             <list>
 *                 <value>classpath*:i18n/**}{@code /*.properties</value>
 *             </list>
 *         </property>
 *         <property name="useCodeAsDefaultMessage" value="true" />
 *         <property name="defaultEncoding" value="UTF-8" />
 * 
 *         <property name="cacheSeconds">
 *             <value>60</value>
 *         </property>
 *     </bean>
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>注意事项:</h3>
 * <blockquote>
 * <ol>
 * <li>由于使用正则表达式来截取扫描的文件,<span style="color:red">如果文件名称出现非语言的下划线_,可能会出现文件名称不准确的情况</span></li>
 * <li>如果重复的配置,将会去重</li>
 * <li>也支持不带通配符形式的配置 ,如
 * 
 * <pre class="code">
 * {@code
 * <value>classpath:i18n/list</value>
 * }
 * </pre>
 * 
 * </li>
 * <li></li>
 * </ol>
 * </blockquote>
 * 
 * 
 * <h3>解析原理:</h3>
 * 
 * <blockquote>
 * <p>
 * 将传入的 <code>basenames</code>参数,使用 {@link ResourcePatternResolver}进行解析,再提取转成 {@link ReloadableResourceBundleMessageSource}认知的
 * <code>basenames</code>格式
 * </p>
 * </blockquote>
 * 
 * <h3>关于Spring自带的 {@link ReloadableResourceBundleMessageSource}:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>内部使用 {@link DefaultResourceLoader}来读取</li>
 * <li>支持 {@link ResourceUtils#CLASSPATH_URL_PREFIX},但是不支持{@link ResourcePatternResolver#CLASSPATH_ALL_URL_PREFIX},具体参见
 * {@link DefaultResourceLoader#getResource(String)}</li>
 * <li>不支持通配符 比如 i18n/*</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://searchcode.com/codesearch/view/17495983/#">https://searchcode.com/codesearch/view/17495983/#</a>
 * @since 1.5.0
 */
public class PathMatchingReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource{

    /** The Constant LOGGER. */
    private static final Logger     LOGGER                  = LoggerFactory
                    .getLogger(PathMatchingReloadableResourceBundleMessageSource.class);

    /** The resource pattern resolver. */
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.context.support.ReloadableResourceBundleMessageSource#setBasenames(java.lang.String[])
     */
    @Override
    public void setBasenames(String...basenames){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("begin parse input basenames:{}", JsonUtil.toString(basenames));
        }

        //---------------------------------------------------------------
        super.setBasenames(BasenamesResolver.resolver(resourcePatternResolver, basenames));
    }

    //---------------------------------------------------------------

    /**
     * 设置 resource pattern resolver.
     *
     * @param resourcePatternResolver
     *            the resourcePatternResolver to set
     */
    public void setResourcePatternResolver(ResourcePatternResolver resourcePatternResolver){
        this.resourcePatternResolver = resourcePatternResolver;
    }
}
