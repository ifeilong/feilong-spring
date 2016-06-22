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
package com.feilong.spring.web.servlet;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 国际化.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.web.servlet.LocaleResolver
 * @since 1.5.0
 */
public final class LocaleUtil{

    /** Don't let anyone instantiate this class. */
    private LocaleUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 获取当前国际化内容语言.
     *
     * @return the locale
     * @see org.springframework.web.context.request.RequestContextHolder#getRequestAttributes()
     * @see org.springframework.web.servlet.support.RequestContextUtils#getLocaleResolver(HttpServletRequest)
     * @see org.springframework.web.servlet.LocaleResolver#resolveLocale(HttpServletRequest)
     */
    public static Locale getLocale(){
        // LocaleContextHolder.getLocale()
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        return RequestContextUtils.getLocale(request);
    }
}
