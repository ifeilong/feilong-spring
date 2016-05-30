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
package com.feilong.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 数组
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.8
 */
public abstract class BaseDIUser implements InitializingBean,DisposableBean{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDIUser.class);

    /**
     * Inits the.
     */
    public void init(){
        LOGGER.info("init.....:[{}]", this.getClass().getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception{
        LOGGER.info("afterPropertiesSet.....:[{}]", this.getClass().getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception{
        LOGGER.info("destroy.....:[{}]", this.getClass().getName());
    }
}
