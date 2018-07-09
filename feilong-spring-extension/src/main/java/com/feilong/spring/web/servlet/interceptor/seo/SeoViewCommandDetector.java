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

import static com.feilong.core.Validator.isNullOrEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link SeoViewCommand} 检测器.
 * 
 * <p>
 * 主要作用是容错,当发现某个属性没有值,那么以默认值来替代
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.1
 */
class SeoViewCommandDetector{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SeoViewCommandDetector.class);

    //---------------------------------------------------------------
    /**
     * 检测以下信息.
     * 
     * <ul>
     * <li>如果 null==seoViewCommand,返回 <code>defaultSeoViewCommand</code></li>
     * <li>如果isNullOrEmpty(seoViewCommand.getSeoDescription()),将设置<code>defaultSeoViewCommand</code>的Description</li>
     * <li>如果isNullOrEmpty(seoViewCommand.getSeoKeywords()),将设置 <code>defaultSeoViewCommand</code>的SeoKeywords</li>
     * <li>如果isNullOrEmpty(seoViewCommand.getSeoTitle()),将设置 <code>defaultSeoViewCommand</code>的SeoTitle</li>
     * </ul>
     *
     * @param seoViewCommand
     *            the seo view command
     * @param defaultSeoViewCommand
     *            the default seo view command
     * @return the seo view command
     */
    static SeoViewCommand detect(SeoViewCommand seoViewCommand,SeoViewCommand defaultSeoViewCommand){
        if (null == seoViewCommand){
            LOGGER.debug("can't find [SeoViewCommand] in Request or ModelAndView attribute,use [defaultSeoViewCommand].");
            return defaultSeoViewCommand;
        }

        //---------------------------------------------------------------

        //SeoDescription
        if (isNullOrEmpty(seoViewCommand.getSeoDescription())){
            seoViewCommand.setSeoDescription(defaultSeoViewCommand.getSeoDescription());
        }
        //SeoKeywords
        if (isNullOrEmpty(seoViewCommand.getSeoKeywords())){
            seoViewCommand.setSeoKeywords(defaultSeoViewCommand.getSeoKeywords());
        }
        //SeoTitle
        if (isNullOrEmpty(seoViewCommand.getSeoTitle())){
            seoViewCommand.setSeoTitle(defaultSeoViewCommand.getSeoTitle());
        }
        return seoViewCommand;
    }

}
