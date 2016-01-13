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
import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import com.feilong.core.util.Validator;

/**
 * The Class JoinPointUtil.
 *
 * @author feilong
 * @version 1.5.0 2016年1月5日 下午3:53:42
 * @since 1.5.0
 */
public class JoinPointUtil{

    /**
     * 获得运行的annotaion.
     * 
     * <h3>代码流程:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>先基于 {@link org.aspectj.lang.reflect.MethodSignature},获得其 method,然后继续这个method 调用
     * {@link org.springframework.core.annotation.AnnotationUtils#findAnnotation(Method, Class)}解析</li>
     * <li>如果第一步找不到相应的annotation,那么会通过 {@link org.aspectj.lang.JoinPoint#getTarget()} 构建target method,并解析</li>
     * </ol>
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param joinPoint
     *            the join point
     * @param annotationClass
     *            the annotation class
     * @return the annotation
     */
    public static <T extends Annotation> T findAnnotation(JoinPoint joinPoint,Class<T> annotationClass){
        if (Validator.isNullOrEmpty(joinPoint)){
            throw new NullPointerException("joinPoint can't be null/empty!");
        }
        if (Validator.isNullOrEmpty(annotationClass)){
            throw new NullPointerException("annotationClass can't be null/empty!");
        }

        //**************************************************************************

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        T annotation = AnnotationUtils.findAnnotation(method, annotationClass);
        if (null != annotation){
            return annotation;
        }

        //**************************************************************************

        Method targetMethod = MethodUtils.getAccessibleMethod(
                        joinPoint.getTarget().getClass(),
                        method.getName(),
                        method.getParameterTypes());
        annotation = AnnotationUtils.findAnnotation(targetMethod, annotationClass);
        if (null != annotation){
            return annotation;
        }
        return null;
    }
}