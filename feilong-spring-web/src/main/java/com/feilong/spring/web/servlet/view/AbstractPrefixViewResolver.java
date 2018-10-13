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
package com.feilong.spring.web.servlet.view;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

/**
 * 抽象的前缀 view resolver.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.3
 */
public abstract class AbstractPrefixViewResolver extends AbstractCachingViewResolver implements Ordered{

    /** The prefix. */
    private String prefix;

    /** The order. */
    private int    order = Ordered.LOWEST_PRECEDENCE; // default: same as non-Ordered

    //---------------------------------------------------------------

    /** Post construct. */
    @PostConstruct
    protected void postConstruct(){
        Validate.notBlank(prefix, "prefix can't be blank!");
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.view.AbstractCachingViewResolver#loadView(java.lang.String, java.util.Locale)
     */
    @Override
    protected View loadView(String viewName,Locale locale) throws Exception{
        if (!StringUtils.startsWith(viewName, prefix)){
            return null;
        }

        //---------------------------------------------------------------
        String content = viewName.substring(prefix.length());
        return buildView(content);
    }

    //---------------------------------------------------------------

    /**
     * Builds the view.
     *
     * @param content
     *            the content
     * @return the view
     * @since 4.0.3
     */
    protected abstract View buildView(String content);

    //---------------------------------------------------------------

    /**
     * 获得 prefix.
     *
     * @return the prefix
     */
    public String getPrefix(){
        return prefix;
    }

    /**
     * 设置 prefix.
     *
     * @param prefix
     *            the prefix to set
     */
    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    /**
     * 获得 order.
     *
     * @return the order
     */
    @Override
    public int getOrder(){
        return order;
    }

    /**
     * 设置 order.
     *
     * @param order
     *            the order to set
     */
    public void setOrder(int order){
        this.order = order;
    }

}