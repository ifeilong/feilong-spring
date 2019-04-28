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

import static com.feilong.core.bean.ConvertUtil.toMapUseEntrys;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * The Class FeilongNamespaceHandler.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.5
 */
public class FeilongNamespaceHandler extends NamespaceHandlerSupport{

    /** The Constant map. */
    private static final Map<String, BeanDefinitionParser> map = toMapUseEntrys(//
                    // Pair.of("httpResponseStringBuilder", (BeanDefinitionParser) new HttpResponseStringBuilderBeanDefinitionParser()),
                    Pair.of("accessor-session", (BeanDefinitionParser) new SessionAccessorBeanDefinitionParser()),
                    Pair.of("accessor-sessionkey", (BeanDefinitionParser) new SessionKeyAccessorBeanDefinitionParser()),
                    Pair.of("accessor-cookie", (BeanDefinitionParser) new CookieAccessorBeanDefinitionParser())
    //
    );

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    @Override
    public void init(){
        for (Map.Entry<String, BeanDefinitionParser> entry : map.entrySet()){
            registerBeanDefinitionParser(entry.getKey(), entry.getValue());
        }
    }
}