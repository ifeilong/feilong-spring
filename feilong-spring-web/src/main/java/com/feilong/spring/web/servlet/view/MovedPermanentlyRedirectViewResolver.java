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

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 301 永久跳转.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.web.servlet.view.UrlBasedViewResolver#createView(String, Locale)
 * @see org.springframework.http.HttpStatus#MOVED_PERMANENTLY
 * @see <a href="https://gist.github.com/code4craft/144cdc94464a2f5228219b5fe2864232">RedirectViewResolver</a>
 * @see <a href="https://blog.csdn.net/hj7jay/article/details/51372467">用Spring MVC优雅的实现301跳转</a>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.2
 */
public class MovedPermanentlyRedirectViewResolver extends AbstractPrefixViewResolver{

    /** The Constant REDIRECT_PERMANENT_PREFIX. */
    public static final String REDIRECT_PERMANENT_PREFIX = "redirectPermanent:";

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.view.AbstractPrefixViewResolver#buildView(java.lang.String)
     */
    @Override
    protected View buildView(String redirectUrl){
        RedirectView redirectView = new RedirectView(redirectUrl);
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);

        return redirectView;
    }

}