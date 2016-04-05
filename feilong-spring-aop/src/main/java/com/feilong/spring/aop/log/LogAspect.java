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
package com.feilong.spring.aop.log;

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.feilong.core.date.DateExtensionUtil;
import com.feilong.core.lang.StringUtil;
import com.feilong.spring.aop.AbstractAspect;
import com.feilong.spring.aop.JoinPointUtil;

/**
 * 日志切面 aspect,作用于 所有使用 {@link com.feilong.spring.aop.log.Log}标注 的方法.
 * 
 * @author feilong
 * @version 1.0.2 2012-3-30 上午2:49:12
 * @version 1.5.0 2016年1月5日 下午3:46:17
 * @since 1.5.0
 */
@Aspect
public class LogAspect extends AbstractAspect{

    // log4j
    /** The LOGGER. */
    private static final Logger LOGGER = Logger.getLogger(LogAspect.class);

    /** The _LOGGER. */
    private Log                 log;

    /**
     * Pointcut.
     */
    @Pointcut("@annotation(com.feilong.spring.aop.log.Log)")
    private void pointcut(){
        //only pointcut
    }

    // com.feilong.spring.aspects.UserManager
    /**
     * Around.
     *
     * @param proceedingJoinPoint
     *            the join point
     * @return the object
     * @throws Throwable
     *             the throwable
     */
    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        // LoggerFactory.
        // LOGGER.

        Date begin = new Date();

        // 通过反射执行目标对象的连接点处的方法
        Object result = proceedingJoinPoint.proceed();
        // 在来得到方法名吧,就是通知所要织入目标对象中的方法名称

        Date end = new Date();

        log = JoinPointUtil.findAnnotation(proceedingJoinPoint, Log.class);

        String level = log.level();
        // 输出的日志 怪怪的 02:13:10 INFO (NativeMethodAccessorImpl.java:?) [invoke0()] method:addUser([1018, Jummy]),耗时:0
        // ReflectUtil.invokeMethod(log, level, "method:{}({}),耗时:{}", objects);

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();

        String format = "method:%s(%s),耗时:%s";
        Object[] objects = { method.getName(), proceedingJoinPoint.getArgs(), DateExtensionUtil.getIntervalForView(begin, end) };

        Object message = StringUtil.format(format, objects);
        LOGGER.log(Level.toLevel(level), message);

        return result;
    }
}
