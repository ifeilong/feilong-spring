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
package com.feilong.spring.transaction.interceptor;

import static org.apache.commons.lang3.tuple.Pair.of;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_DEFAULT;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_READ_COMMITTED;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_READ_UNCOMMITTED;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_REPEATABLE_READ;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_SERIALIZABLE;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_MANDATORY;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NEVER;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NOT_SUPPORTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_SUPPORTS;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.transaction.interceptor.TransactionAttribute;

import static com.feilong.core.bean.ConvertUtil.toMap;

/**
 * The Class TransactionAttributeUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.transaction.interceptor.TransactionAttribute
 * @see org.springframework.transaction.TransactionDefinition
 * @since 1.1.1
 */
public class TransactionAttributeUtil{

    /**
     * 隔离性级别.
     * 
     * @see org.springframework.transaction.TransactionDefinition#ISOLATION_DEFAULT
     * @see org.springframework.transaction.TransactionDefinition#ISOLATION_READ_COMMITTED
     * @see org.springframework.transaction.TransactionDefinition#ISOLATION_READ_UNCOMMITTED
     * @see org.springframework.transaction.TransactionDefinition#ISOLATION_REPEATABLE_READ
     * @see org.springframework.transaction.TransactionDefinition#ISOLATION_SERIALIZABLE
     * 
     * @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED
     * @see java.sql.Connection#TRANSACTION_READ_COMMITTED
     * @see java.sql.Connection#TRANSACTION_REPEATABLE_READ
     * @see java.sql.Connection#TRANSACTION_SERIALIZABLE
     */
    private static final Map<Integer, String> ISOLATION_LEVEL_AND_NAME_MAP      = toMap(
                    of(ISOLATION_DEFAULT, "default"),
                    of(ISOLATION_READ_COMMITTED, "read_committed"),
                    of(ISOLATION_READ_UNCOMMITTED, "read_uncommitted"),
                    of(ISOLATION_REPEATABLE_READ, "repeatable_read"),
                    of(ISOLATION_SERIALIZABLE, "serializable"));

    /**
     * 事务传播行为.
     * 
     * @see org.springframework.transaction.TransactionDefinition#PROPAGATION_MANDATORY
     * @see org.springframework.transaction.TransactionDefinition#PROPAGATION_NESTED
     * @see org.springframework.transaction.TransactionDefinition#PROPAGATION_NEVER
     * @see org.springframework.transaction.TransactionDefinition#PROPAGATION_NOT_SUPPORTED
     * @see org.springframework.transaction.TransactionDefinition#PROPAGATION_REQUIRED
     * @see org.springframework.transaction.TransactionDefinition#PROPAGATION_REQUIRES_NEW
     * @see org.springframework.transaction.TransactionDefinition#PROPAGATION_SUPPORTS
     */
    private static final Map<Integer, String> PROPAGATION_BEHAVIOR_AND_NAME_MAP = toMap(
                    //默认的事务传播行为,表示必须有逻辑事务,否则新建一个事务
                    of(PROPAGATION_REQUIRED, "required"),
                    //创建新的逻辑事务,表示每次都创建新的逻辑事务(物理事务也是不同的),因此外部事务可以不受内部事务回滚状态的影响独立提交或者回滚.
                    of(PROPAGATION_REQUIRES_NEW, "requires_new"),
                    //指如果当前存在逻辑事务,就加入到该逻辑事务, 如果当前没有逻辑事务,就以非事务方式执行.
                    of(PROPAGATION_SUPPORTS, "supports"),
                    //不支持事务,如果当前存在事务则暂停该事务,如果当前存在逻辑事务,就把当前事务暂停,以非事务方式执行
                    of(PROPAGATION_NOT_SUPPORTED, "not_supported"),
                    //如果当前有事务,使用当前事务执行,如果当前没有事务,则抛出异常(IllegalTransactionStateException)
                    of(PROPAGATION_MANDATORY, "mandatory"),
                    //不支持事务,如果当前存在是事务则抛出IllegalTransactionStateException异常,
                    of(PROPAGATION_NEVER, "never"),
                    //嵌套事务支持
                    of(PROPAGATION_NESTED, "mandatory"));

    //****************************************************************************************************
    /** Don't let anyone instantiate this class. */
    private TransactionAttributeUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //****************************************************************************************************

    /**
     * 获得 map for LOGGER.
     *
     * @param transactionAttribute
     *            the transaction attribute
     * @return the map for log
     */
    public static final Map<String, Object> getMapForLog(TransactionAttribute transactionAttribute){
        if (null == transactionAttribute){
            return null;
        }

        int isolationLevel = transactionAttribute.getIsolationLevel();
        String isolationLevelString = ISOLATION_LEVEL_AND_NAME_MAP.get(isolationLevel);

        int propagationBehavior = transactionAttribute.getPropagationBehavior();
        String propagationBehaviorString = PROPAGATION_BEHAVIOR_AND_NAME_MAP.get(propagationBehavior);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("getPropagationBehavior", propagationBehaviorString + "(" + propagationBehavior + ")");
        map.put("getIsolationLevel", isolationLevelString + "(" + isolationLevel + ")");
        map.put("isReadOnly", transactionAttribute.isReadOnly());
        map.put("getName", transactionAttribute.getName());
        map.put("getQualifier", transactionAttribute.getQualifier());
        map.put("getTimeout", transactionAttribute.getTimeout());

        return map;
    }
}
