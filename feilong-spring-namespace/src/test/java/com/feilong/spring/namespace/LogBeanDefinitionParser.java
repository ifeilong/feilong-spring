package com.feilong.spring.namespace;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class LogBeanDefinitionParser implements BeanDefinitionParser{

    @Override
    public BeanDefinition parse(Element element,ParserContext parserContext){

        // create a RootBeanDefinition that will serve as configuration  
        // holder for the 'pattern' attribute and the 'lenient' attribute  
        RootBeanDefinition beanDef = new RootBeanDefinition();
        beanDef.setBeanClass(LogBean.class);

        // never null since the schema requires it  
        String pattern = element.getAttribute("isPrintTime");
        beanDef.getConstructorArgumentValues().addGenericArgumentValue(pattern);
        String company = element.getAttribute("company");
        if (StringUtils.hasText(company)){
            beanDef.getPropertyValues().addPropertyValue("company", company);
        }

        // retrieve the ID attribute that will serve as the bean identifier in  
        // the context  
        String id = element.getAttribute("id");

        // create a bean definition holder to be able to register the  
        // bean definition with the bean definition registry  
        // (obtained through the ParserContext)  
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDef, id);

        // register the BeanDefinitionHolder (which contains the bean  
        // definition)  
        // with the BeanDefinitionRegistry  
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());

        return beanDef;

    }

}