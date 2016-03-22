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
package com.feilong.spring.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.feilong.spring.web.servlet.mvc.method.annotation.LoginMemberHandlerMethodArgumentResolver;

/**
 * 标识spring mvc controller方法参数支持自动注入 用户session信息 ,参见 {@link LoginMemberHandlerMethodArgumentResolver}
 *
 * @author feilong
 * @version 1.5.0 2016年2月26日 下午7:36:41
 * @since 1.5.0
 * 
 * @see LoginMemberHandlerMethodArgumentResolver
 */
//表示产生文档,比如通过javadoc产生文档, 将此注解包含在 javadoc 中, 这个Annotation可以被写入javadoc
//在默认情况下，注释 不包括在 Javadoc 中
@Documented
//在jvm加载class时候有效, VM将在运行期也保留注释,因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)

//仅用于 Method parameter
@Target({ ElementType.PARAMETER })
public @interface LoginMember{

}
