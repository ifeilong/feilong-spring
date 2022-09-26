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
package com.feilong.spring.web.servlet.interceptor.seo;

import com.feilong.core.bean.PropertyUtil;
import com.feilong.spring.web.servlet.ViewCommand;

/**
 * 提供从 {@link ViewCommand} 中提取/构造 {@link SeoViewCommand}的扩展点.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see StandardSeoInterceptor
 * @since 1.5.1
 * @since 1.11.2 change extends to interface
 */
public class SeoViewCommandFromAttributeValueViewCommandBuilder implements SeoViewCommandFromAttributeValueBuilder{

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.seo.SeoViewCommandFromAttributeValueBuilder#build(java.lang.String, java.lang.Object)
     */
    @Override
    public SeoViewCommand build(String attributeName,Object attributeValue){
        if (attributeValue instanceof ViewCommand){
            //级联查询
            SeoViewCommand seoViewCommand = PropertyUtil.findValueOfType(attributeValue, SeoViewCommand.class);
            if (null != seoViewCommand){
                return seoViewCommand;
            }
        }
        return null;
    }
}
