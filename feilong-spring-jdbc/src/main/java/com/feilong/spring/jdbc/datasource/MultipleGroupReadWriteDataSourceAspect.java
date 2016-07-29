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

import static com.feilong.coreextension.lang.ThreadUtil.getCurrentThreadMapForLog;
import static loxia.dao.ReadWriteSupport.READ;
import static loxia.dao.ReadWriteSupport.WRITE;
import static org.apache.commons.lang3.tuple.Pair.of;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_MANDATORY;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NEVER;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NOT_SUPPORTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_SUPPORTS;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import com.feilong.coreextension.lang.ThreadUtil;
import com.feilong.spring.aop.AbstractAspect;
import com.feilong.spring.aop.JoinPointUtil;
import com.feilong.spring.aop.ProceedingJoinPointUtil;
import com.feilong.spring.transaction.interceptor.TransactionAttributeUtil;
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.date.DateExtensionUtil.getIntervalForView;

import net.sf.json.JSONException;

/**
 * 使用拦截器,确定使用那个组/类型数据库,以及那种(读还是写)的数据库.
 * 
 * <h3>可能的方式:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>
 * controller调用 AManager.a(); A是 adatabase</li>
 * <li></li>
 * <li></li>
 * <li>
 * 
 * <pre class="code">
 * 可能方法 套方法  AManager.a(){ 调用 BManager.b();}  ,
 * A是 adatabase
 * B是 bdatabase
 * controller 调用A 的前, MultipleGroupReadWriteStatusHolder.setMultipleDataSourceGroupName(adatabase);
 * 当 A.a(){ 调用 B.b();}的时候
 * 调完之后,需要回去
 * </pre>
 * 
 * </li>
 * </ol>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.feilong.spring.aop.AbstractAspect
 * @see org.aspectj.lang.annotation.Aspect
 * @see org.aspectj.lang.annotation.Around
 * @see org.springframework.transaction.interceptor.TransactionAttributeSource
 * @see org.springframework.core.Ordered
 * @see "loxia.aspect.ReadWriteDataSourceAspect"
 * @see org.springframework.transaction.annotation.Transactional
 * @since 1.1.1
 */
@Aspect
public class MultipleGroupReadWriteDataSourceAspect extends AbstractAspect{

    /** The Constant logger. */
    private static final Logger                LOGGER                                  = getLogger(
                    MultipleGroupReadWriteDataSourceAspect.class);

    /** The transaction attribute souce. */
    @Autowired(required = false)
    private TransactionAttributeSource         transactionAttributeSouce;

    /** 传播行为和是否必须是write的对照map. */
    private static final Map<Integer, Boolean> PROPAGATION_BEHAVIOR_AND_MUST_WRITE_MAP = toMap(
                    //默认的事务传播行为,表示必须有逻辑事务,否则新建一个事务
                    of(PROPAGATION_REQUIRED, false),
                    //创建新的逻辑事务,表示每次都创建新的逻辑事务(物理事务也是不同的),因此外部事务可以不受内部事务回滚状态的影响独立提交或者回滚.
                    of(PROPAGATION_REQUIRES_NEW, true),
                    //指如果当前存在逻辑事务,就加入到该逻辑事务, 如果当前没有逻辑事务,就以非事务方式执行.
                    of(PROPAGATION_SUPPORTS, false),
                    //不支持事务,如果当前存在事务则暂停该事务,如果当前存在逻辑事务,就把当前事务暂停,以非事务方式执行
                    of(PROPAGATION_NOT_SUPPORTED, false),
                    //如果当前有事务,使用当前事务执行,如果当前没有事务,则抛出异常(IllegalTransactionStateException)
                    of(PROPAGATION_MANDATORY, false),
                    //不支持事务,如果当前存在是事务则抛出IllegalTransactionStateException异常,
                    of(PROPAGATION_NEVER, false),
                    //嵌套事务支持
                    of(PROPAGATION_NESTED, false));

    /**
     * Point.
     *
     * @param proceedingJoinPoint
     *            the proceeding join point
     * @return the object
     * @throws Throwable
     *             the throwable
     */
    @Around("this(loxia.dao.ReadWriteSupport)")
    public Object point(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        TransactionAttribute transactionAttribute = null;
        if (null != transactionAttributeSouce){
            transactionAttribute = transactionAttributeSouce
                            .getTransactionAttribute(methodSignature.getMethod(), proceedingJoinPoint.getTarget().getClass());
        }

        String groupName = getGroupName(proceedingJoinPoint);
        return proceed(proceedingJoinPoint, transactionAttribute, groupName);
    }

    /**
     * Gets the group name.
     *
     * @param proceedingJoinPoint
     *            the proceeding join point
     * @return the group name
     * @since 1.8.3
     */
    private static String getGroupName(ProceedingJoinPoint proceedingJoinPoint){
        MultipleGroupDataSource multipleGroupDataSource = JoinPointUtil.findAnnotation(proceedingJoinPoint, MultipleGroupDataSource.class);
        //组名
        //没有配置multipleGroupDataSourceAnnotation
        //没有配置 当然延续原来的 风格
        if (null == multipleGroupDataSource || isNullOrEmpty(multipleGroupDataSource.value())){
            //nothing to do
            return null;
        }
        return multipleGroupDataSource.value();
    }

    /**
     * Proceed.
     *
     * @param proceedingJoinPoint
     *            the proceeding join point
     * @param transactionAttribute
     *            the transaction attribute
     * @param groupName
     *            the group name
     * @return the object
     * @throws Throwable
     *             the throwable
     * @since 1.1.1
     */
    private static Object proceed(ProceedingJoinPoint proceedingJoinPoint,TransactionAttribute transactionAttribute,String groupName)
                    throws Throwable{
        //当前的holder
        String previousDataSourceNameHolder = MultipleGroupReadWriteStatusHolder.getMultipleDataSourceGroupName();

        String currentThreadInfo = JsonUtil.format(ThreadUtil.getCurrentThreadMapForLog());
        if (LOGGER.isInfoEnabled()){
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("groupName", groupName);
            map.put("previousDataSourceNameHolder", previousDataSourceNameHolder);
            map.put("transactionAttribute:", TransactionAttributeUtil.getMapForLog(transactionAttribute));

            String pattern = "before determine datasource :[{}],proceedingJoinPoint info:[{}],current thread info:[{}]";
            LOGGER.info(
                            pattern,
                            JsonUtil.format(map),
                            getProceedingJoinPointJsonInfoExcludeJsonException(proceedingJoinPoint),
                            currentThreadInfo);
        }

        boolean isSetHolder = isSetHolder(transactionAttribute, groupName);

        //***************************************************************************
        if (isSetHolder){
            //read or write
            String readWriteSupport = getReadWriteSupport(transactionAttribute);

            String targetDataSourcesKey = MultipleGroupReadWriteUtil.getTargetDataSourcesKey(groupName, readWriteSupport);
            LOGGER.info("set targetDataSourcesKey:[{}],current thread info:[{}]", targetDataSourcesKey, currentThreadInfo);
            MultipleGroupReadWriteStatusHolder.setMultipleDataSourceGroupName(targetDataSourcesKey);
        }

        try{
            return proceed(proceedingJoinPoint);
        }catch (Throwable e){
            throw e;
        }finally{
            if (isNotNullOrEmpty(previousDataSourceNameHolder)){
                String pattern = "Back to previous Read/Write Status:[{}],current thread info:[{}]";
                LOGGER.info(pattern, previousDataSourceNameHolder, currentThreadInfo);
                //神来之笔,这样才能兼容嵌套
                MultipleGroupReadWriteStatusHolder.setMultipleDataSourceGroupName(previousDataSourceNameHolder);
            }

            //XXX 可能还可以优化 现规则和loxia相同
            //不存在previousDataSourceNameHolder,则清空
            else{
                String pattern = "previousDataSourceNameHolder is NullOrEmpty,Clear Read/Write Status:[{}],current thread info:[{}]";
                LOGGER.info(pattern, MultipleGroupReadWriteStatusHolder.getMultipleDataSourceGroupName(), currentThreadInfo);
                MultipleGroupReadWriteStatusHolder.clearMultipleDataSourceGroupName();
            }
        }
    }

    /**
     * 判断是否要设置钩子.
     *
     * @param transactionAttribute
     *            the transaction attribute
     * @param groupName
     *            the group name
     * @return true, if checks if is set holder
     * @since 1.1.1
     */
    private static boolean isSetHolder(TransactionAttribute transactionAttribute,String groupName){
        if (isNotNullOrEmpty(groupName)){
            return true;
        }

        if (null == transactionAttribute){
            return true;
        }

        //可能还可以优化 现规则和loxia相同
        return transactionAttribute.getPropagationBehavior() != PROPAGATION_REQUIRES_NEW;
    }

    /**
     * 判断数据库读写类型,<br>
     * 通过 {@link org.springframework.transaction.interceptor.TransactionAttribute}判断这次请求该操作读库还是写库.
     *
     * @param transactionAttribute
     *            the transaction attribute
     * @return the read write support
     * @see "org.postgresql.jdbc2.AbstractJdbc2Connection#setReadOnly(boolean)"
     * @since 1.1.1
     */
    private static String getReadWriteSupport(TransactionAttribute transactionAttribute){
        if (null == transactionAttribute){
            return READ;
        }

        boolean mustWrite = PROPAGATION_BEHAVIOR_AND_MUST_WRITE_MAP.get(transactionAttribute.getPropagationBehavior());
        if (mustWrite){
            LOGGER.info("New writable connection is required for new transaction.");
            return WRITE;
        }

        //see "org.postgresql.jdbc2.AbstractJdbc2Connection#setReadOnly(boolean)"
        boolean readOnly = transactionAttribute.isReadOnly();
        return readOnly ? READ : WRITE;
    }

    /**
     * Proceed.
     *
     * @param proceedingJoinPoint
     *            the proceeding join point
     * @return the object
     * @throws Throwable
     *             the throwable
     * @since 1.1.1
     */
    private static Object proceed(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Object[] args = proceedingJoinPoint.getArgs();
        String format = getProceedingJoinPointJsonInfoExcludeJsonException(proceedingJoinPoint);

        if (LOGGER.isInfoEnabled()){
            String pattern = "begin proceed ,ProceedingJoinPoint info:[{}],Thread info:{}";
            LOGGER.info(pattern, format, JsonUtil.format(getCurrentThreadMapForLog()));
        }
        Date beginDate = new Date();

        //***********************************************************
        Object returnValue = proceedingJoinPoint.proceed(args);

        //***********************************************************
        if (LOGGER.isInfoEnabled()){
            String pattern = "end proceed:[{}],thread info:[{}],time:[{}],return:[{}]";
            LOGGER.info(pattern, format, JsonUtil.format(getCurrentThreadMapForLog()), getIntervalForView(beginDate), returnValue);
        }
        return returnValue;
    }

    /**
     * 有些业务类可能不规范,把request这样的不能转成json的对象 当作参数传递, 如果不处理的话, 就会抛异常.
     *
     * @param proceedingJoinPoint
     *            the proceeding join point
     * @return the proceeding join point json info exclude json exception
     * @since 1.1.1
     */
    private static String getProceedingJoinPointJsonInfoExcludeJsonException(ProceedingJoinPoint proceedingJoinPoint){
        try{
            return JsonUtil.format(ProceedingJoinPointUtil.getMapForLog(proceedingJoinPoint));
        }catch (JSONException e){
            LOGGER.error("", e);
            return e.getMessage();
        }
    }
}