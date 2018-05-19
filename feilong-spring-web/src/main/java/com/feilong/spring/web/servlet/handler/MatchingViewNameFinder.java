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
package com.feilong.spring.web.servlet.handler;

import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 下面借鉴了 org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.4
 */
//下面借鉴了 org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
class MatchingViewNameFinder{

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingViewNameFinder.class);

    /** Don't let anyone instantiate this class. */
    private MatchingViewNameFinder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Find a matching view name in the given exception mappings.
     * 
     * @param exceptionMappings
     *            mappings between exception class names and error view names
     * @param exception
     *            the exception that got thrown during handler execution
     * @return the view name, or {@code null} if none found
     * @see #setExceptionMappings
     */
    static String find(Properties exceptionMappings,Exception exception){
        String viewName = null;
        String dominantMapping = null;
        //---------------------------------------------------------------
        int deepest = Integer.MAX_VALUE;

        for (Enumeration<?> names = exceptionMappings.propertyNames(); names.hasMoreElements();){
            String exceptionMapping = (String) names.nextElement();
            int depth = getDepth(exceptionMapping, exception);
            if (depth >= 0 && (depth < deepest
                            || (depth == deepest && dominantMapping != null && exceptionMapping.length() > dominantMapping.length()))){
                deepest = depth;
                dominantMapping = exceptionMapping;
                viewName = exceptionMappings.getProperty(exceptionMapping);
            }
        }

        //---------------------------------------------------------------
        if (viewName != null && LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "Resolving to view '{}' for exception of type [{}], based on exception mapping [{}]",
                            viewName,
                            exception.getClass().getName(),
                            dominantMapping);
        }

        //---------------------------------------------------------------
        return viewName;
    }

    //---------------------------------------------------------------

    /**
     * Return the depth to the superclass matching.
     * <p>
     * 0 means ex matches exactly. Returns -1 if there's no match.
     * Otherwise, returns depth. Lowest depth wins.
     */
    private static int getDepth(String exceptionMapping,Exception ex){
        return getDepth(exceptionMapping, ex.getClass(), 0);
    }

    private static int getDepth(String exceptionMapping,Class<?> exceptionClass,int depth){
        if (exceptionClass.getName().contains(exceptionMapping)){
            // Found it!
            return depth;
        }

        //---------------------------------------------------------------
        // If we've gone as far as we can go and haven't found it...
        if (exceptionClass.equals(Throwable.class)){
            return -1;
        }

        //---------------------------------------------------------------
        return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
    }
}
