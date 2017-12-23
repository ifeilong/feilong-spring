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

import static com.feilong.core.bean.ConvertUtil.toMapUseEntrys;
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

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.TransactionAttribute;

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
     * <h3>我们首先说并发中可能发生的3中不讨人喜欢的事情:</h3>
     * 
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * 
     * <tr style="background-color:#ccccff">
     * <th align="left">字段</th>
     * <th align="left">说明</th>
     * </tr>
     * 
     * <tr valign="top">
     * <td>Dirty reads<br>
     * 读脏数据</td>
     * <td>比如事务A的未提交(还依然缓存)的数据被事务B读走,如果事务A失败回滚,会导致事务B所读取的的数据是错误的。</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>non-repeatable reads<br>
     * 数据不可重复读</td>
     * <td>比如事务A中两处读取数据-total-的值。在第一读的时候,total是100,<br>
     * 然后事务B就把total的数据改成200,事务A再读一次,结果就发现,total竟然就变成200了,造成事务A数据混乱</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>phantom reads<br>
     * 幻象读数据</td>
     * 
     * <td>这个和non-repeatable reads相似,也是同一个事务中多次读不一致的问题。<br>
     * 但是non-repeatable reads的不一致是因为他所要取的数据集被改变了(比如total的数据),<br>
     * 但是phantom reads所要读的数据的不一致却不是他所要读的数据集改变,而是他的条件数据集改变。<br>
     * 比如Select account.id where account.name="ppgogo*",<br>
     * 第一次读去了6个符合条件的id,<br>
     * 第二次读取的时候,由于事务b把一个帐号的名字由"dd"改 成"ppgogo1",结果取出来了7个数据</td>
     * </tr>
     * 
     * </table>
     * </blockquote>
     * 
     * <h3>数据库事务的隔离级别有4个</h3>
     * 
     * <blockquote>
     * <p>
     * 由低到高依次为Read uncommitted 、Read committed 、Repeatable read 、Serializable ,这四个级别可以逐个解决脏读 、不可重复读 、幻读这几类问题:
     * </p>
     * 
     * 
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * 
     * <tr style="background-color:#ccccff">
     * <th align="left"></th>
     * <th align="left">Dirty reads</th>
     * <th align="left">non-repeatable reads</th>
     * <th align="left">phantom reads</th>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link #ISOLATION_READ_UNCOMMITTED}</td>
     * <td>会</td>
     * <td>会</td>
     * <td>会</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link #ISOLATION_READ_COMMITTED}</td>
     * <td>不会</td>
     * <td>会</td>
     * <td>会</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>{@link #ISOLATION_REPEATABLE_READ}</td>
     * <td>不会</td>
     * <td>不会</td>
     * <td>会</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link #ISOLATION_SERIALIZABLE}</td>
     * <td>不会</td>
     * <td>不会</td>
     * <td>不会</td>
     * </tr>
     * </table>
     * </blockquote>
     * 
     * <h3>不同数据库默认的隔离级别:</h3>
     * 
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * 
     * <tr style="background-color:#ccccff">
     * <th align="left">数据库</th>
     * <th align="left">默认隔离级别</th>
     * <th align="left">备注</th>
     * </tr>
     * 
     * <tr valign="top">
     * <td>pgsql</td>
     * <td>{@link TransactionDefinition#ISOLATION_READ_COMMITTED ISOLATION_READ_COMMITTED}</td>
     * <td>参见 postgresql.conf<br>
     * #default_transaction_isolation = 'read committed'</td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>MySQL</td>
     * <td>{@link TransactionDefinition#ISOLATION_REPEATABLE_READ ISOLATION_REPEATABLE_READ}</td>
     * <td></td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>Oracle</td>
     * <td>{@link TransactionDefinition#ISOLATION_READ_COMMITTED ISOLATION_READ_COMMITTED}</td>
     * <td></td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>Sql Server</td>
     * <td>{@link TransactionDefinition#ISOLATION_READ_UNCOMMITTED ISOLATION_READ_UNCOMMITTED}</td>
     * <td></td>
     * </tr>
     * 
     * </table>
     * </blockquote>
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
    private static final Map<Integer, String> ISOLATION_LEVEL_AND_NAME_MAP      = toMapUseEntrys(
                    of(ISOLATION_DEFAULT, "default"),
                    //大多数主流数据库的默认事务等级,保证了一个事务不会读到另一个并行事务已修改但未提交的数据,避免了“脏读取”。该级别适用于大多数系统。
                    of(ISOLATION_READ_COMMITTED, "read_committed"),
                    //保证了读取过程中不会读取到非法数据。隔离级别在于处理多事务的并发问题。
                    of(ISOLATION_READ_UNCOMMITTED, "read_uncommitted"),
                    //保证了一个事务不会修改已经由另一个事务读取但未提交(回滚)的数据。避免了“脏读取”和“不可重复读取”的情况,但是带来了更多的性能损失。
                    of(ISOLATION_REPEATABLE_READ, "repeatable_read"),
                    //最严格的级别,事务串行执行,资源消耗最大
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
    private static final Map<Integer, String> PROPAGATION_BEHAVIOR_AND_NAME_MAP = toMapUseEntrys(
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
                    of(PROPAGATION_NESTED, "nested"));

    //---------------------------------------------------------------
    /** Don't let anyone instantiate this class. */
    private TransactionAttributeUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

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

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("getPropagationBehavior", propagationBehaviorString + "(" + propagationBehavior + ")");
        map.put("getIsolationLevel", isolationLevelString + "(" + isolationLevel + ")");
        map.put("isReadOnly", transactionAttribute.isReadOnly());
        map.put("getName", transactionAttribute.getName());
        map.put("getQualifier", transactionAttribute.getQualifier());
        map.put("getTimeout", transactionAttribute.getTimeout());

        return map;
    }
}
