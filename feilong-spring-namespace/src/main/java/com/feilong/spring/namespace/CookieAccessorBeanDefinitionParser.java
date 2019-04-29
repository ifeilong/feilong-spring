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

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.feilong.accessor.cookie.CookieAccessor;

/**
 * CookieAccessorBeanDefinitionParser.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.web.servlet.config.AnnotationDrivenBeanDefinitionParser
 * @since 4.0.5
 */
public class CookieAccessorBeanDefinitionParser extends AbstractBeanDefinitionParser{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.xml.AbstractBeanDefinitionParser#parseInternal(org.w3c.dom.Element,
     * org.springframework.beans.factory.xml.ParserContext)
     */
    @Override
    protected AbstractBeanDefinition parseInternal(Element element,ParserContext parserContext){
        // create a RootBeanDefinition that will serve as configuration holder for the 'pattern' attribute and the 'lenient' attribute  
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(CookieAccessor.class);

        //---------------------------------------------------------------

        rootBeanDefinition.getPropertyValues().addPropertyValue("cookieEntity", buildRuntimeBeanReference(element, parserContext));
        addPropertyValue(element, rootBeanDefinition, "isValueEncoding", false);

        return rootBeanDefinition;
    }

    private RuntimeBeanReference buildRuntimeBeanReference(Element element,ParserContext parserContext){
        BeanDefinitionBuilder beanDefinitionBuilder = buildBeanDefinitionBuilder(element);

        AbstractBeanDefinition rawBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        rawBeanDefinition.setSource(parserContext.extractSource(element));

        String generatedName = parserContext.getReaderContext().generateBeanName(rawBeanDefinition);
        parserContext.registerBeanComponent(new BeanComponentDefinition(beanDefinitionBuilder.getBeanDefinition(), generatedName));

        return new RuntimeBeanReference(generatedName);
    }

    private BeanDefinitionBuilder buildBeanDefinitionBuilder(Element element){
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                        .genericBeanDefinition("com.feilong.servlet.http.entity.CookieEntity");
        addPropertyValue(element, beanDefinitionBuilder, "name", true);
        addPropertyValue(element, beanDefinitionBuilder, "version", "httpOnly", "secure", "path", "domain", "comment", "maxAge");
        return beanDefinitionBuilder;
    }

    //---------------------------------------------------------------

    private void add(Element element,BeanDefinitionBuilder beanDefinitionBuilder,String attributeName){
        addPropertyValue(element, beanDefinitionBuilder, attributeName, false);
    }

    private void addPropertyValue(Element element,BeanDefinitionBuilder beanDefinitionBuilder,String attributeName,boolean required){
        Validate.notBlank(attributeName, "name can't be blank!,element:[%s]", element.toString());

        //---------------------------------------------------------------
        //没有属性
        if (!element.hasAttribute(attributeName)){
            if (required){
                throw new IllegalArgumentException("must config attributeName:[" + attributeName + "]");
            }
            return;
        }

        //---------------------------------------------------------------
        //有元素
        String value = element.getAttribute(attributeName);
        if (required){
            Validate.notBlank(value, "when name is :[%s],value can't be blank!", attributeName);
        }

        //---------------------------------------------------------------
        if (isNotNullOrEmpty(value)){
            beanDefinitionBuilder.addPropertyValue(attributeName, value);
        }
    }

    private void addPropertyValue(Element element,RootBeanDefinition rootBeanDefinition,String attributeName,boolean required){
        Validate.notBlank(attributeName, "name can't be blank!,element:[%s]", element.toString());

        //没有属性
        if (!element.hasAttribute(attributeName)){
            if (required){
                throw new IllegalArgumentException("must config attributeName:[" + attributeName + "]");
            }
            return;
        }

        //---------------------------------------------------------------
        //有元素
        String value = element.getAttribute(attributeName);
        if (required){
            Validate.notBlank(value, "when name is :[%s],value can't be blank!", attributeName);
        }

        //---------------------------------------------------------------
        if (isNotNullOrEmpty(value)){
            rootBeanDefinition.getPropertyValues().addPropertyValue(attributeName, value);
        }
    }

    private void addPropertyValue(Element element,BeanDefinitionBuilder beanDefinitionBuilder,String...attributeNameList){
        for (String attributeName : attributeNameList){
            add(element, beanDefinitionBuilder, attributeName);
        }
    }

    private void add(Element element,BeanDefinitionBuilder beanDefinitionBuilder,List<String> attributeNameList,boolean required){
        for (String attributeName : attributeNameList){
            addPropertyValue(element, beanDefinitionBuilder, attributeName, required);
        }
    }

}