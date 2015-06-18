/*
 * Copyright (C) 2008 feilong (venusdrogon@163.com)
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
package com.feilong.spring.web.servlet.mvc.method;

/**
 *
 * @author <a href="mailto:venusdrogon@163.com">feilong</a>
 * @version 1.2.1 2015年6月14日 下午11:03:07
 * @since 1.2.1
 */
public class RequestMappingConfig{

    private String[] requestPatterns;

    private String[] requestMethods;

    private String   targetClass;

    private String   targetMethod;

    //    private RequestMappingInfo  mappingInfo;
    /**
     * @return the requestPatterns
     */
    public String[] getRequestPatterns(){
        return requestPatterns;
    }

    /**
     * @param requestPatterns
     *            the requestPatterns to set
     */
    public void setRequestPatterns(String[] requestPatterns){
        this.requestPatterns = requestPatterns;
    }

    /**
     * @return the requestMethods
     */
    public String[] getRequestMethods(){
        return requestMethods;
    }

    /**
     * @param requestMethods
     *            the requestMethods to set
     */
    public void setRequestMethods(String[] requestMethods){
        this.requestMethods = requestMethods;
    }

    /**
     * @return the targetClass
     */
    public String getTargetClass(){
        return targetClass;
    }

    /**
     * @param targetClass
     *            the targetClass to set
     */
    public void setTargetClass(String targetClass){
        this.targetClass = targetClass;
    }

    /**
     * @return the targetMethod
     */
    public String getTargetMethod(){
        return targetMethod;
    }

    /**
     * @param targetMethod
     *            the targetMethod to set
     */
    public void setTargetMethod(String targetMethod){
        this.targetMethod = targetMethod;
    }
}
