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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.List;
import java.util.Map;

import org.springframework.core.Ordered;

import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.lang.ClassUtil;

/**
 * 简单的 {@link BeanToMapBuilder}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.12.7
 */
public class SimpleBeanToMapBuilder<T> implements BeanToMapBuilder<T>{

    /**
     * 自动提取属性的名字.
     * 
     * @since 1.12.8
     */
    private List<String> autoExtractPropertyNameList;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.event.builder.BeanToMapBuilder#build(java.lang.String, java.lang.Object)
     */
    @Override
    public Map<String, Object> build(String beanName,T bean){
        Map<String, Object> map = newLinkedHashMap();

        map.put("beanName", beanName);
        map.put("name", buildName(bean));

        //---------------------------------------------------------------

        //since 1.12.8
        if (isNotNullOrEmpty(autoExtractPropertyNameList)){
            for (String autoExtractPropertyName : autoExtractPropertyNameList){
                map.put(autoExtractPropertyName, PropertyUtil.getProperty(bean, autoExtractPropertyName));
            }
        }

        //---------------------------------------------------------------
        map.put("order", ClassUtil.isInstance(bean, Ordered.class) ? "" + ((Ordered) bean).getOrder() : "-");

        packExtentionInfo(map);

        return map;
    }

    //---------------------------------------------------------------

    /**
     * Pack extention info.
     *
     * @param map
     *            the map
     */
    @SuppressWarnings("unused")
    protected void packExtentionInfo(Map<String, Object> map){

    }

    //---------------------------------------------------------------

    /**
     * Builds the name.
     *
     * @param filter
     *            the filter
     * @return the string
     */
    protected String buildName(T filter){
        return filter.getClass().getName();
    }

    //---------------------------------------------------------------

    /**
     * 设置 自动提取属性的名字.
     *
     * @param autoExtractPropertyNameList
     *            the autoExtractPropertyNameList to set
     * @since 1.12.8
     */
    public void setAutoExtractPropertyNameList(List<String> autoExtractPropertyNameList){
        this.autoExtractPropertyNameList = autoExtractPropertyNameList;
    }

}
