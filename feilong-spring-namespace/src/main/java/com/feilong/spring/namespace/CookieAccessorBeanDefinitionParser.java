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

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.feilong.accessor.cookie.CookieAccessor;
import com.feilong.servlet.http.entity.CookieEntity;

/**
 * CookieAccessorBeanDefinitionParser.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.5
 */
public class CookieAccessorBeanDefinitionParser extends org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#getBeanClass(org.w3c.dom.Element)
     */
    @Override
    protected Class<?> getBeanClass(Element element){
        return CookieAccessor.class;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#doParse(org.w3c.dom.Element,
     * org.springframework.beans.factory.xml.ParserContext, org.springframework.beans.factory.support.BeanDefinitionBuilder)
     */
    @Override
    protected void doParse(Element element,ParserContext parserContext,BeanDefinitionBuilder beanDefinitionBuilder){
        CookieEntity cookieEntity = CookieEntityBuilder.build(element);
        beanDefinitionBuilder.addPropertyValue("cookieEntity", cookieEntity);

        //---------------------------------------------------------------

        String isValueEncoding = element.getAttribute("isValueEncoding");
        if (isNotNullOrEmpty(isValueEncoding)){
            beanDefinitionBuilder.addPropertyValue("isValueEncoding", toBoolean(isValueEncoding));
        }
    }

}