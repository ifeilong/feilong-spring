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
package com.feilong.spring.event.builder;

import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.List;
import java.util.Map;

/**
 * 默认的.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 4.0.0
 */
public class DefaultMapBeanToMapListBuilder<T> implements MapBeanToMapListBuilder<T>{

    /** 提取bean信息到map. */
    private BeanToMapBuilder<T> beanToMapBuilder = new SimpleBeanToMapBuilder<>();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.event.builder.MapListBuilder#buildList(java.util.Map)
     */
    @Override
    public List<Map<String, Object>> build(Map<String, T> beanNameAndBeanMap){
        List<Map<String, Object>> list = newArrayList();
        for (Map.Entry<String, T> entry : beanNameAndBeanMap.entrySet()){
            Map<String, Object> map = beanToMapBuilder.build(entry.getKey(), entry.getValue());
            list.add(map);
        }
        return list;
    }

    //---------------------------------------------------------------

    /**
     * 获得 提取bean信息到map.
     *
     * @return the beanToMapBuilder
     */
    public BeanToMapBuilder<T> getBeanToMapBuilder(){
        return beanToMapBuilder;
    }

    /**
     * 设置 提取bean信息到map.
     *
     * @param beanToMapBuilder
     *            the beanToMapBuilder to set
     */
    public void setBeanToMapBuilder(BeanToMapBuilder<T> beanToMapBuilder){
        this.beanToMapBuilder = beanToMapBuilder;
    }
}
