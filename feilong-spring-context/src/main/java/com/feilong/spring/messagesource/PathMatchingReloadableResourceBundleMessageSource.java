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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.tools.jsonlib.JsonUtil;

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
 * <p>
 * 也支持 不带通配符形式的配置 ,如
 * 
 * <pre class="code">
 * {@code
 * <value>classpath:i18n/list</value>
 * }
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * 如果重复的配置,将会去重
 * </p>
 * 
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
 * <h3>关于 {@link ReloadableResourceBundleMessageSource}:</h3>
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
 * @author feilong
 * @version 1.5.0 2016年2月24日 下午1:31:07
 * @see <a href="https://searchcode.com/codesearch/view/17495983/#">https://searchcode.com/codesearch/view/17495983/#</a>
 * @since 1.5.0
 */
public class PathMatchingReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource{

    /** The Constant LOGGER. */
    private static final Logger     LOGGER                  = LoggerFactory
                    .getLogger(PathMatchingReloadableResourceBundleMessageSource.class);

    /** The resource pattern resolver. */
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.context.support.ReloadableResourceBundleMessageSource#setBasenames(java.lang.String[])
     */
    @Override
    public void setBasenames(String...basenames){
        LOGGER.info("input basenames:{}", JsonUtil.format(basenames));

        List<String> basenameList = new ArrayList<String>();
        for (String basename : basenames){
            if (basename.contains("*")){//如果带有通配符
                basenameList.addAll(resolverWildcardConfig(basename));
            }else{
                basenameList.add(basename);
            }
        }

        //去重
        List<String> removeDuplicateBasenameList = CollectionsUtil.removeDuplicate(basenameList);
        String[] finalBaseNames = ConvertUtil.toArray(removeDuplicateBasenameList, String.class);

        LOGGER.info("finalBaseNames:{}", JsonUtil.format(finalBaseNames));
        super.setBasenames(finalBaseNames);
    }

    /**
     * Resolver wildcard config.
     *
     * @param basename
     *            the basename
     * @return the list< string>
     * @since 1.5.0
     */
    private List<String> resolverWildcardConfig(String basename){
        List<String> list = new ArrayList<String>();

        try{
            Resource[] resources = resourcePatternResolver.getResources(basename);
            for (Resource resource : resources){
                list.add(resolverBaseName(resource));
            }
        }catch (IOException e){
            LOGGER.error("", e);
        }

        return list;
    }

    /**
     * Resolver base name.
     *
     * @param resource
     *            the resource
     * @return the string
     * @throws IOException
     *             the IO exception
     * 
     * @see <a href="https://searchcode.com/codesearch/view/17495983/#">https://searchcode.com/codesearch/view/17495983/#</a>
     */
    private static String resolverBaseName(Resource resource) throws IOException{
        URL url = resource.getURL();
        String file = url.getFile();

        boolean isJar = ResourceUtils.isJarURL(url);

        String baseName = ResourceUtils.CLASSPATH_URL_PREFIX + file
                        .replaceFirst(isJar ? "^.*" + ResourceUtils.JAR_URL_SEPARATOR : "^(.*/test-classes|.*/classes|.*/resources)/", "")
                        .replaceAll("(_\\w+){0,3}\\.(properties|xml)", "");

        LOGGER.debug("" + resource.getURL() + "(====>)" + baseName);
        return baseName;
    }

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
