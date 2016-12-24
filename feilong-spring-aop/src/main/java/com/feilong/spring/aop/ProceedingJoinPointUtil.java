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

import java.util.LinkedHashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * focus on {@link org.aspectj.lang.ProceedingJoinPoint}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.1.1
 */
public class ProceedingJoinPointUtil{

    /** Don't let anyone instantiate this class. */
    private ProceedingJoinPointUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 获得 map for LOGGER.
     *
     * @param proceedingJoinPoint
     *            the proceeding join point
     * @return the map for log
     */
    public static final Map<String, Object> getMapForLog(ProceedingJoinPoint proceedingJoinPoint){
        MethodSignature methodSignature = getMethodSignature(proceedingJoinPoint);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("target", proceedingJoinPoint.getTarget().getClass().getCanonicalName());
        map.put("method", methodSignature.getMethod().getName());
        map.put("args", proceedingJoinPoint.getArgs());
        return map;
    }

    /**
     * Gets the method signature.
     *
     * @param proceedingJoinPoint
     *            the proceeding join point
     * @return the method signature
     * @since 1.8.3
     */
    private static MethodSignature getMethodSignature(ProceedingJoinPoint proceedingJoinPoint){
        Signature signature = proceedingJoinPoint.getSignature();
        return (MethodSignature) signature;
    }
}
