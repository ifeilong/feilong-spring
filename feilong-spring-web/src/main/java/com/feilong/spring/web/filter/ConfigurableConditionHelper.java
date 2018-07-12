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
package com.feilong.spring.web.filter;

import static com.feilong.core.bean.ConvertUtil.toArray;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class ConfigurableConditionHelper.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.8
 */
public class ConfigurableConditionHelper{

    /** <code>{@value}</code>. */
    private static final String[] STATIC_RESOURCE_SUFFIX = toArray(
                    ".jpg",
                    ".png",
                    ".gif",
                    ".ico",
                    ".js",
                    ".css",
                    ".html",
                    ".xml",
                    ".swf",
                    ".woff",
                    ".woff2",
                    ".ttf",
                    ".mp3");

    //---------------------------------------------------------------

    /**
     * Checks if is static resource.
     *
     * @param requestURI
     *            the request URI
     * @return true, if is static resource
     */
    public static boolean isStaticResource(String requestURI){
        for (String uriSuffix : STATIC_RESOURCE_SUFFIX){
            //TODO 性能
            if (StringUtils.endsWithIgnoreCase(requestURI, uriSuffix)){
                return true;
            }
        }

        return false;
    }
}
