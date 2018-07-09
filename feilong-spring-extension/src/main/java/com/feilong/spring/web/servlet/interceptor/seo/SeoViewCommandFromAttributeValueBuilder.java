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

/**
 * 提供从 <code>request/model attributeValue</code>中提取/构造 {@link SeoViewCommand}的扩展点.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.2
 */
public interface SeoViewCommandFromAttributeValueBuilder{

    /**
     * 提供从 <code>request/model attributeValue</code>中提取/构造 {@link SeoViewCommand}的扩展点.
     * 
     * <p>
     * 如果结果不是null,那么使用此结果<br>
     * 如果结果是null,那么将处理下一个 attributeName
     * </p>
     *
     * @param attributeName
     *            request/model 作用域 属性名称
     * @param attributeValue
     *            request/model 作用域 属性值
     * @return the seo view command
     */
    SeoViewCommand build(String attributeName,Object attributeValue);

}
