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
package com.feilong.spring.web.servlet;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.support.AbstractFlashMapManager;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

/**
 * The Class FlashMapUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.web.servlet.support.SessionFlashMapManager
 * @see RequestMappingHandlerAdapter#getModelAndView(ModelAndViewContainer, ModelFactory, NativeWebRequest)
 * @see AbstractFlashMapManager#isFlashMapForRequest(FlashMap, HttpServletRequest)
 * @see RedirectView#renderMergedOutputModel(Map, HttpServletRequest, HttpServletResponse)
 * @since 1.5.3
 */
public class FlashMapUtil{

    /** Don't let anyone instantiate this class. */
    private FlashMapUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Put.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     * @param targetRequestPath
     *            the target request path
     * @param request
     *            the request
     * @return the flash map
     */
    public static FlashMap put(String name,String value,String targetRequestPath,HttpServletRequest request){
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put(name, value);
        flashMap.setTargetRequestPath(targetRequestPath);
        flashMap.addTargetRequestParam(name, value);

        return flashMap;
    }

    /**
     * Save output flash map.
     *
     * @param flashMap
     *            the flash map
     * @param request
     *            the request
     * @param response
     *            the response
     */
    public static void saveOutputFlashMap(FlashMap flashMap,HttpServletRequest request,HttpServletResponse response){
        FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
        flashMapManager.saveOutputFlashMap(flashMap, request, response);
    }
}
