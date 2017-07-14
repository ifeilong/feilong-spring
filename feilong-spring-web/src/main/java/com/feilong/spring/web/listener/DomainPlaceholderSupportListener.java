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
package com.feilong.spring.web.listener;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.WebApplicationContext;

import com.feilong.core.UncheckedIOException;
import com.feilong.spring.web.util.WebSpringUtil;
import com.feilong.tools.slf4j.Slf4jUtil;
import com.feilong.web.domain.AbstractDomainListener;

/**
 * 初始化域名配置监听器.
 * 
 * <p>
 * 注意: 此 {@link DomainPlaceholderSupportListener}由于需要使用到 {@link WebApplicationContext} ,需要配置在
 * 
 * <pre class="code">
 * {@code
 * <listener>
 *      <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class> 
 * </listener>
 * }
 * </pre>
 * 
 * 后面
 * </p>
 * 
 * <h3>关于配置文件</h3>
 * 
 * <blockquote>
 * <p>
 * <ol>
 * <li>读取 web.xml中配置的 <code>domainConfigLocation</code> context-param参数,比如:
 * 
 * <pre class="code">
 * {@code
 *     <context-param>
 *         <param-name>domainConfigLocation</param-name>
 *         <param-value>classpath:config/}${{@code spring.profiles.active}}{@code /domain.properties</param-value>
 *     </context-param>
 * }
 * </pre>
 * 
 * <ul>
 * <li>配置的路径支持占位符 {@code Placeholder},比如 <code>config/${spring.profiles.active}/domain.properties</code>,使用
 * {@link org.springframework.core.env.PropertyResolver#resolvePlaceholders(String)} 来解析</li>
 * <li>支持XML形式的properties<br>
 * 使用的是 {@link org.springframework.core.io.support.PropertiesLoaderUtils#loadProperties(Resource)} 来加载资源,关于XML形式的properties,请参考
 * {@link java.util.Properties}</li>
 * </ul>
 * </li>
 * <li>如果没有读取到文件,那么默认读取 domain.properties 默认地址</li>
 * </ol>
 * </p>
 * </blockquote>
 * 
 * 
 * <h3>关于 domain.properties</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * domain.css={"variableName":"domainCSS","value":"http://rs.feilong.com:8888"}
 * domain.js={"variableName":"domainJS","value":"http://rs.feilong.com:8888"}
 * domain.image={"variableName":"domainImage","value":"http://rs.feilong.com:8888"}
 * domain.resource={"variableName":"domainResource","value":"http://127.0.0.1:6666"}
 * </pre>
 * 
 * 值,如果是 json格式,会自动转换,variableName参数会自动设置到 servletContext作用域中,值是value参数<br>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.feilong.web.domain.AbstractDomainListener#CONFIG_LOCATION_PARAM
 * @see com.feilong.spring.web.util.WebSpringUtil#getWebApplicationContext(ServletContext)
 * @see org.springframework.core.env.EnvironmentCapable#getEnvironment()
 * @see org.springframework.core.io.ResourceLoader#getResource(String)
 * @see org.springframework.core.env.PropertyResolver#resolvePlaceholders(String)
 * @see org.springframework.core.io.support.PropertiesLoaderUtils#loadProperties(Resource)
 * @since 1.1.1
 */
public class DomainPlaceholderSupportListener extends AbstractDomainListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainPlaceholderSupportListener.class);
    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.web.domain.AbstractDomainListener#loadDomainProperties(javax.servlet.ServletContext)
     */
    @Override
    protected Properties loadDomainProperties(ServletContext servletContext){
        String domainConfigLocation = resolverDomainConfigLocation(servletContext);

        //org.springframework.web.context.support.XmlWebApplicationContext
        WebApplicationContext webApplicationContext = WebSpringUtil.getRequiredWebApplicationContext(servletContext);

        //org.springframework.web.context.support.ServletContextResource
        Resource resource = webApplicationContext.getResource(domainConfigLocation);

        Validate.isTrue(resource.exists(), "domainConfigLocation:[%s] not exists", domainConfigLocation);
        try{
            return PropertiesLoaderUtils.loadProperties(resource);
        }catch (IOException e){
            String messagePattern = "load domainConfigLocation:[{}] exception";
            LOGGER.error(Slf4jUtil.format(messagePattern, domainConfigLocation), e);
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Resolver domain config location.
     *
     * @param servletContext
     *            the servlet context
     * @return the string
     */
    private String resolverDomainConfigLocation(ServletContext servletContext){
        String domainConfigLocation = getConfigLocationParamValue(servletContext);

        WebApplicationContext webApplicationContext = WebSpringUtil.getRequiredWebApplicationContext(servletContext);
        Environment environment = webApplicationContext.getEnvironment();
        return environment.resolvePlaceholders(domainConfigLocation);
    }
}