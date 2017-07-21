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
package com.feilong.spring.web.event.builder;

import static com.feilong.core.bean.ConvertUtil.toLong;

import java.util.LinkedHashMap;
import java.util.Map;

import com.feilong.accessor.cookie.CookieAccessor;
import com.feilong.core.date.DateExtensionUtil;
import com.feilong.servlet.http.entity.CookieEntity;

/**
 * The Class CookieAccessorBeanToMapBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.5
 */
public class CookieAccessorBeanToMapBuilder implements BeanToMapBuilder<CookieAccessor>{

    /** Static instance. */
    // the static instance works for all types
    public static final BeanToMapBuilder<CookieAccessor> INSTANCE = new CookieAccessorBeanToMapBuilder();

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.event.builder.BeanToMapBuilder#build(java.lang.String, java.lang.Object)
     */
    @Override
    public Map<String, Object> build(String beanName,CookieAccessor cookieAccessor){
        CookieEntity cookieEntity = cookieAccessor.getCookieEntity();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("beanName", beanName);
        map.put("name", cookieEntity.getName());
        map.put("httpOnly", cookieEntity.getHttpOnly());
        map.put("path", cookieEntity.getPath());

        int maxAge = cookieEntity.getMaxAge();

        map.put("maxAge", toShowMaxAge(maxAge));

        map.put("domain", cookieEntity.getDomain());
        map.put("secure", cookieEntity.getSecure());
        map.put("version", cookieEntity.getVersion());
        map.put("isValueEncoding", cookieAccessor.getIsValueEncoding());

        return map;
    }

    /**
     * To show max age.
     *
     * @param maxAge
     *            单位秒
     * @return the string
     */
    private String toShowMaxAge(int maxAge){
        if (maxAge <= 0){
            return String.valueOf(maxAge);
        }
        return DateExtensionUtil.formatDuration(toLong(maxAge) * 1000);
    }
}
