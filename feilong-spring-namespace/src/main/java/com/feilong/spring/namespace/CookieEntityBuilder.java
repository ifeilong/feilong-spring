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
package com.feilong.spring.namespace;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toBoolean;
import static com.feilong.core.bean.ConvertUtil.toInteger;

import org.apache.commons.lang3.Validate;
import org.w3c.dom.Element;

import com.feilong.servlet.http.entity.CookieEntity;

/**
 * The Class CookieEntityBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.5
 */
public class CookieEntityBuilder{

    /**
     * Builds the.
     *
     * @param element
     *            the element
     * @return the cookie entity
     */
    public static CookieEntity build(Element element){
        CookieEntity cookieEntity = new CookieEntity();

        //---------------------------------------------------------------

        //name名称,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号. 
        String name = element.getAttribute("name");
        Validate.notBlank(name, "name can't be blank!");
        cookieEntity.setName(name);

        //---------------------------------------------------------------
        //设置存活时间,单位秒.
        String maxAge = element.getAttribute("maxAge");
        if (isNotNullOrEmpty(maxAge)){
            cookieEntity.setMaxAge(toInteger(maxAge));
        }

        //---------------------------------------------------------------

        //;Comment=VALUE ... describes cookie's use ;Discard ... implied by maxAge {@code <} 0
        String comment = element.getAttribute("comment");
        if (isNotNullOrEmpty(comment)){
            cookieEntity.setComment(comment);
        }

        // ;Domain=VALUE ... domain that sees cookie.
        String domain = element.getAttribute("domain");
        if (isNotNullOrEmpty(domain)){
            cookieEntity.setDomain(domain);
        }

        // ;Path=VALUE ... URLs that see the cookie
        String path = element.getAttribute("path");
        if (isNotNullOrEmpty(path)){
            cookieEntity.setPath(path);
        }

        // ;Secure ... e.g. use SSL.
        String secure = element.getAttribute("secure");
        if (isNotNullOrEmpty(secure)){
            cookieEntity.setSecure(toBoolean(secure));
        }

        //supports both the <b>Version 0 (by Netscape)</b> and <b>Version 1 (by RFC 2109)</b> cookie specifications.
        String version = element.getAttribute("version");
        if (isNotNullOrEmpty(version)){
            cookieEntity.setVersion(toInteger(version));
        }

        // Not in cookie specs, but supported by browsers.
        String httpOnly = element.getAttribute("httpOnly");
        if (isNotNullOrEmpty(httpOnly)){
            cookieEntity.setSecure(toBoolean(httpOnly));
        }

        return cookieEntity;
    }
}
