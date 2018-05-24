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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;

/**
 * 基于异常来构造相关视图.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.1
 */
public interface ExceptionViewNameBuilder{

    /**
     * Builds the.
     *
     * @param handlerMethod
     *            the handler method
     * @param exception
     *            the exception
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the string
     */
    String build(HandlerMethod handlerMethod,Exception exception,HttpServletRequest request,HttpServletResponse response);
}
