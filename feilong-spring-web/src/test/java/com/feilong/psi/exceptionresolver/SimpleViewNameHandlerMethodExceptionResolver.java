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
package com.feilong.psi.exceptionresolver;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

import com.feilong.spring.web.servlet.handler.ExceptionViewNameBuilder;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.0
 * @deprecated 可能不需要, 使用 {@link org.springframework.web.servlet.handler.SimpleMappingExceptionResolver} 即可
 */
@Deprecated
public class SimpleViewNameHandlerMethodExceptionResolver extends AbstractHandlerMethodExceptionResolver{

    /** The exception view name builder. */
    private ExceptionViewNameBuilder exceptionViewNameBuilder;

    //使用 父类  mappedHandlerClasses 属性

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver#doResolveHandlerMethodException(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.web.method.HandlerMethod, java.lang.Exception)
     */
    @Override
    protected ModelAndView doResolveHandlerMethodException(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    HandlerMethod handlerMethod,
                    Exception ex){

        //---------------------------------------------------------------
        String viewName = exceptionViewNameBuilder.build(handlerMethod, ex, request, response);
        if (isNotNullOrEmpty(viewName)){
            return new ModelAndView(viewName);
        }

        //---------------------------------------------------------------
        return null;
    }

    //---------------------------------------------------------------
    /**
     * Sets the exception view name builder.
     *
     * @param exceptionViewNameBuilder
     *            the exceptionViewNameBuilder to set
     */
    public void setExceptionViewNameBuilder(ExceptionViewNameBuilder exceptionViewNameBuilder){
        this.exceptionViewNameBuilder = exceptionViewNameBuilder;
    }

}
