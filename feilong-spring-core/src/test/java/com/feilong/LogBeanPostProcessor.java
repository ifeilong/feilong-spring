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
package com.feilong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class LogBeanPostProcessor implements BeanPostProcessor{

    private static final Logger LOGGER = LoggerFactory.getLogger(LogBeanPostProcessor.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean,String beanName) throws BeansException{
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("postProcessBeforeInitialization beanName:[{}],bean:[{}]", beanName, bean.getClass().getName());
        }
        return bean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean,String beanName) throws BeansException{
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("postProcessAfterInitialization beanName:[{}],bean:[{}]", beanName, bean.getClass().getName());
        }
        return bean;
    }
}
