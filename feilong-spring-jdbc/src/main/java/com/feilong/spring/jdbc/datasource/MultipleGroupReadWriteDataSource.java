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
package com.feilong.spring.jdbc.datasource;

import static com.feilong.spring.jdbc.datasource.MultipleGroupReadWriteUtil.DEFAULT_GROUP_NAME;
import static loxia.dao.ReadWriteSupport.READ;
import static loxia.dao.ReadWriteSupport.WRITE;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.feilong.coreextension.lang.ThreadUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * 多数据源,带分组的概念 {@link #readWriteDataSourceCommandMap}
 * <p>
 * 此map中,key 是 dataSource group 名字(group 可以理解为不同类型/不同的数据库,比如 金宝贝商城DB,金宝贝O2O DB);<br>
 * value,是这个组的读写数据源
 * </p>
 * 
 * <h3>主要做两件事情:</h3>
 * 
 * <blockquote>
 * <p>
 * <ol>
 * <li>{@link #determineCurrentLookupKey()}</li>
 * <li>{@link #afterPropertiesSet()}</li>
 * </ol>
 * </p>
 * </blockquote>
 * 
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
 * @since 1.1.1
 */
public class MultipleGroupReadWriteDataSource extends AbstractRoutingDataSource{

    /** The Constant LOGGER. */
    private static final Logger                     LOGGER = LoggerFactory.getLogger(MultipleGroupReadWriteDataSource.class);

    /** key 是 dataSource group 名字;. */
    private Map<String, ReadWriteDataSourceCommand> readWriteDataSourceCommandMap;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet(){
        Validate.notNull(readWriteDataSourceCommandMap, "readWriteDataSourceCommandMap can't be null!");

        Object defaultTargetDataSource = null;
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();

        for (Map.Entry<String, ReadWriteDataSourceCommand> entry : readWriteDataSourceCommandMap.entrySet()){
            String groupName = entry.getKey();
            ReadWriteDataSourceCommand readWriteDataSourceCommand = entry.getValue();

            DataSource readDataSource = readWriteDataSourceCommand.getReadDataSource();
            DataSource writeDataSource = readWriteDataSourceCommand.getWriteDataSource();

            // 默认读的是 default 组的 写库
            if (DEFAULT_GROUP_NAME.equals(groupName)){
                defaultTargetDataSource = writeDataSource;
            }

            targetDataSources.put(MultipleGroupReadWriteUtil.getTargetDataSourcesKey(groupName, WRITE), writeDataSource);
            targetDataSources.put(MultipleGroupReadWriteUtil.getTargetDataSourcesKey(groupName, READ), readDataSource);
        }

        this.setTargetDataSources(targetDataSources);
        this.setDefaultTargetDataSource(defaultTargetDataSource);
        // this.setDataSourceLookup(dataSourceLookup);
        // this.setLenientFallback(defaultTargetDataSource);
        // this.setLoginTimeout(timeout);
        // this.setLogWriter(pw);
        super.afterPropertiesSet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
     */
    @Override
    protected Object determineCurrentLookupKey(){
        String dataSourceName = MultipleGroupReadWriteStatusHolder.getMultipleDataSourceGroupName();
        LOGGER.info("Current LookupKey:[{}],thread info:{}", dataSourceName, JsonUtil.format(ThreadUtil.getCurrentThreadMapForLog()));
        return dataSourceName;
    }

    /**
     * Sets the key 是 dataSource group 名字;.
     * 
     * @param readWriteDataSourceCommandMap
     *            the readWriteDataSourceCommandMap to set
     */
    public void setReadWriteDataSourceCommandMap(Map<String, ReadWriteDataSourceCommand> readWriteDataSourceCommandMap){
        this.readWriteDataSourceCommandMap = readWriteDataSourceCommandMap;
    }
}
