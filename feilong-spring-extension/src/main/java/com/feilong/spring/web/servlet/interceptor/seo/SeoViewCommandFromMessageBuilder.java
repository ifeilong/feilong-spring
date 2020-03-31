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

import org.springframework.context.ApplicationContext;

import com.feilong.spring.messagesource.MessageSourceUtil;

/**
 * 构造 {@link SeoViewCommand}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.1
 */
class SeoViewCommandFromMessageBuilder{

    /** Don't let anyone instantiate this class. */
    private SeoViewCommandFromMessageBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param applicationContext
     *            the application context
     * @param keyNameSeoTitle
     *            the key name seo title
     * @param keyNameSeoKeywords
     *            the key name seo keywords
     * @param keyNameSeoDescription
     *            the key name seo description
     * @return the seo view command
     */
    static SeoViewCommand build(
                    ApplicationContext applicationContext,
                    String keyNameSeoTitle,
                    String keyNameSeoKeywords,
                    String keyNameSeoDescription){

        //Locale locale = LocaleUtil.getLocale();

        SeoViewCommand seoViewCommand = new DefaultSeoViewCommand();
        seoViewCommand.setSeoDescription(MessageSourceUtil.getMessage(applicationContext, keyNameSeoDescription));
        seoViewCommand.setSeoKeywords(MessageSourceUtil.getMessage(applicationContext, keyNameSeoKeywords));
        seoViewCommand.setSeoTitle(MessageSourceUtil.getMessage(applicationContext, keyNameSeoTitle));

        return seoViewCommand;
    }
}
