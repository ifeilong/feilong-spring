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
package com.feilong.spring.web.listener.domain;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.date.DateExtensionUtil.formatDuration;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;

/**
 * 初始化配置监听器.
 * 
 * <h3>说明(仅支持固定地址配置路径形式):</h3>
 * 
 * <blockquote>
 * <p style="color:red">
 * 本Listener解析的是 固定文件 {@link #DEFAULT_CONFIGURATION_FILE},或者 web.xml 配置的参数 context-param {@link #CONFIG_LOCATION_PARAM} (固定地址),<br>
 * 不支持解析配置参数地址中含变量形式的地址.
 * </p>
 * 
 * <p>
 * 如果有支持动态参}数的需求, 比如
 * </p>
 * 
 * <pre class="code">
 * 
 *     {@code <context-param>}
 *         {@code <param-name>domainConfigLocation</param-name>}
 *         {@code <param-value>}classpath:config/${spring.profiles.active}/domain.properties{@code </param-value>}
 *     {@code </context-param>}
 * 
 * </pre>
 * 
 * <p>
 * 请使用 feilong-spring-web 项目的 {@link "com.feilong.spring.web.listener.DomainPlaceholderSupportListener"}
 * </p>
 * 
 * </blockquote>
 * 
 * 
 * <h3>关于配置文件</h3>
 * 
 * <blockquote>
 * <p>
 * <ol>
 * <li>读取 web.xml中配置的 domainConfigLocation context-param参数</li>
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
 * @since 1.7.3
 */
public abstract class AbstractDomainListener implements ServletContextListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER                = LoggerFactory.getLogger(AbstractDomainListener.class);

    /** <code>{@value}</code>. */
    private static final String CONFIG_LOCATION_PARAM = "domainConfigLocation";

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent){
        LOGGER.info("begin initialized [{}]~~", getClass().getName());

        Date beginDate = new Date();

        initDomain(servletContextEvent.getServletContext());

        LOGGER.info("[{}]:initialization completed,use time:[{}]", getClass().getSimpleName(), formatDuration(beginDate));
    }

    //---------------------------------------------------------------

    /**
     * 初始化二级域名.
     * 
     * @param servletContext
     *            the servlet context
     */
    private void initDomain(ServletContext servletContext){
        Properties properties = loadDomainProperties(servletContext);
        Map<String, DomainConfig> domainConfigMap = DomainConfigMapParser.parse(properties, servletContext.getContextPath());

        setServletContextAttribute(servletContext, domainConfigMap);

        LOGGER.info("domain config info:[{}]", JsonUtil.format(domainConfigMap));
    }

    //---------------------------------------------------------------

    /**
     * 获得 domain 配置文件.
     *
     * @param servletContext
     *            the servlet context
     * @param domainConfigLocation
     *            the domain config location
     * @return the domain properties
     * @since 1.0.9
     */
    protected abstract Properties loadDomainProperties(ServletContext servletContext);

    //---------------------------------------------------------------

    /**
     * Gets the config location param value.
     *
     * @param servletContext
     *            the servlet context
     * @return the config location param value
     * @since 1.7.3
     */
    @SuppressWarnings("static-method")
    protected String getConfigLocationParamValue(ServletContext servletContext){
        return servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
    }

    //---------------------------------------------------------------

    /**
     * 设置 servlet context attribute.
     *
     * @param servletContext
     *            the servlet context
     * @param domainConfigMap
     *            the domain config map
     * @since 1.1.1
     */
    private static void setServletContextAttribute(ServletContext servletContext,Map<String, DomainConfig> domainConfigMap){
        for (Map.Entry<String, DomainConfig> entry : domainConfigMap.entrySet()){
            DomainConfig domainConfig = entry.getValue();

            String variableName = domainConfig.getVariableName();
            if (isNotNullOrEmpty(variableName)){
                servletContext.setAttribute(variableName, domainConfig.getValue());
            }
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce){
    }
}
