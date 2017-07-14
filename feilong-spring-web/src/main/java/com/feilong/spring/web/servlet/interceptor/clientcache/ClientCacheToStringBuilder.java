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
package com.feilong.spring.web.servlet.interceptor.clientcache;

import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.feilong.core.lang.annotation.AnnotationToStringBuilder;

/**
 * {@link ClientCache} to string.
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">

16:46:11 INFO  (ContextRefreshedEventClientCacheListener.java:117) onApplicationEvent() - url And ClientCache info:    {
        "/clientcache": "20秒",
        "/item/{itemid}": "5分钟",
        "/noclientcache": "0"
    }
 * 
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public class ClientCacheToStringBuilder implements AnnotationToStringBuilder<ClientCache>{

    /** Static instance. */
    // the static instance works for all types
    public static final ClientCacheToStringBuilder INSTANCE = new ClientCacheToStringBuilder();

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.method.AnnotationToStringBuilder#build(java.lang.annotation.Annotation)
     */
    @Override
    public String build(ClientCache clientCache){
        if (null == clientCache){
            return EMPTY;
        }
        return formatDuration(clientCache.value() * 1000);
    }
}
