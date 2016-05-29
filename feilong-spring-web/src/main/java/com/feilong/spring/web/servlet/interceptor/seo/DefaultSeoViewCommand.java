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

/**
 * 默认的.
 *
 * @author feilong
 * @since 1.2.2
 */
public class DefaultSeoViewCommand implements SeoViewCommand{

    /** seo搜索描述. */
    private String seoDescription;

    /** seo搜索关键字. */
    private String seoKeywords;

    /** seoTitle. */
    private String seoTitle;

    /**
     * 获得 seo搜索描述.
     *
     * @return the seoDescription
     */
    @Override
    public String getSeoDescription(){
        return seoDescription;
    }

    /**
     * 设置 seo搜索描述.
     *
     * @param seoDescription
     *            the seoDescription to set
     */
    @Override
    public void setSeoDescription(String seoDescription){
        this.seoDescription = seoDescription;
    }

    /**
     * 获得 seo搜索关键字.
     *
     * @return the seoKeywords
     */
    @Override
    public String getSeoKeywords(){
        return seoKeywords;
    }

    /**
     * 设置 seo搜索关键字.
     *
     * @param seoKeywords
     *            the seoKeywords to set
     */
    @Override
    public void setSeoKeywords(String seoKeywords){
        this.seoKeywords = seoKeywords;
    }

    /**
     * 获得 seoTitle.
     *
     * @return the seoTitle
     */
    @Override
    public String getSeoTitle(){
        return seoTitle;
    }

    /**
     * 设置 seoTitle.
     *
     * @param seoTitle
     *            the seoTitle to set
     */
    @Override
    public void setSeoTitle(String seoTitle){
        this.seoTitle = seoTitle;
    }
}
