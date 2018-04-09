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
package com.feilong.spring.aop;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class JoinPointUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.0
 */
public class JoinPointUtilTemp{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JoinPointUtil.class);

    /**
     * 获得 method.
     * 
     * @param joinPoint
     *            the join point
     * @param klass
     *            the klass
     * @return the method
     * @deprecated 目前作用不大,将来会重构
     */
    @Deprecated
    protected Method getMethod(JoinPoint joinPoint,Class<? extends Annotation> klass){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(klass)){
            return method;
        }
        Target annotation = klass.getAnnotation(Target.class);
        ElementType[] value = annotation.value();
        try{
            Object target = joinPoint.getTarget();
            Class<? extends Object> targetClass = target.getClass();
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            Method m1 = targetClass.getMethod(methodName, parameterTypes);
            if (m1.isAnnotationPresent(klass)){
                return m1;
            }
        }catch (Exception e){
            LOGGER.error(e.getClass().getName(), e);
        }
        throw new RuntimeException("No Proper annotation found.");
    }

    /**
     * Checks if is annotation present.
     * 
     * @param joinPoint
     *            the join point
     * @param klass
     *            the klass
     * @return true, if checks if is annotation present
     * @deprecated 目前作用不大,将来会重构
     */
    @Deprecated
    protected boolean isAnnotationPresent(JoinPoint joinPoint,Class<? extends Annotation> klass){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(klass)){
            return true;
        }
        Target annotation = klass.getAnnotation(Target.class);
        ElementType[] value = annotation.value();
        try{
            Object target = joinPoint.getTarget();
            Class<? extends Object> targetClass = target.getClass();
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            Method m1 = targetClass.getMethod(methodName, parameterTypes);
            if (m1.isAnnotationPresent(klass)){
                return true;
            }
        }catch (Exception e){
            LOGGER.error(e.getClass().getName(), e);
        }
        return false;
    }
}
