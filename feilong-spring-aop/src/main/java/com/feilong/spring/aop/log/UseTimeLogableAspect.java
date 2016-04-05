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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.date.DateExtensionUtil;
import com.feilong.spring.aop.AbstractAspect;

/**
 * 用来给所有实现{@link UseTimeLogable}接口的方法输出耗时日志用的.
 * 
 * @author feilong
 * @version 1.5.0 2016年1月5日 下午2:32:35
 * @since 1.5.0
 */
@Aspect
public class UseTimeLogableAspect extends AbstractAspect{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UseTimeLogableAspect.class);

    /**
     * Pointcut.
     */
    @Pointcut("this(com.feilong.spring.aop.log.UseTimeLogable)")
    private void pointcut(){
        //only pointcut
    }

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
        Date beginDate = new Date();

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();

        String targetClassSimpleName = proceedingJoinPoint.getTarget().getClass().getSimpleName();

        LOGGER.info("begin [{}.{}] ~~~~", targetClassSimpleName, methodName);

        // 通过反射执行目标对象的连接点处的方法
        Object result = proceedingJoinPoint.proceed();
        // 在来得到方法名吧,就是通知所要织入目标对象中的方法名称

        Date endDate = new Date();
        LOGGER.info("end [{}.{}],use time:{}", targetClassSimpleName, methodName, DateExtensionUtil.getIntervalForView(beginDate, endDate));

        return result;
    }
}
