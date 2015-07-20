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
 * The Class SeoViewCommand.
 *
 * @author feilong
 * @version 1.2.2 2015年7月14日 下午8:30:41
 * @since 1.2.2
 */
public interface SeoViewCommand{

    /**
     * 获得 seo搜索描述.
     *
     * @return the seoDescription
     */
    String getSeoDescription();

    /**
     * 设置 seo搜索描述.
     *
     * @param seoDescription
     *            the seoDescription to set
     */
    void setSeoDescription(String seoDescription);

    /**
     * 获得 seo搜索关键字.
     *
     * @return the seoKeywords
     */
    String getSeoKeywords();

    /**
     * 设置 seo搜索关键字.
     *
     * @param seoKeywords
     *            the seoKeywords to set
     */
    void setSeoKeywords(String seoKeywords);

    /**
     * 获得 seoTitle.
     *
     * @return the seoTitle
     */
    String getSeoTitle();

    /**
     * 设置 seoTitle.
     *
     * @param seoTitle
     *            the seoTitle to set
     */
    void setSeoTitle(String seoTitle);
}
