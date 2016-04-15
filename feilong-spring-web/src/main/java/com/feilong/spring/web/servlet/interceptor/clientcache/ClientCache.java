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
package com.feilong.spring.web.servlet.interceptor.clientcache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.feilong.core.TimeInterval;

/**
 * (为了提高速度一些浏览器会缓存浏览者浏览过的页面,通过下面的相关参数的定义,浏览器一般不会缓存页面,而且浏览器无法脱机浏览.
 * 
 * <p>
 * 借鉴了 胡总的 {@link "NoClientCache"}
 * </p>
 * 
 * @author feilong
 * @version 1.0.9 2015年3月30日 下午4:25:10
 * @since 1.0.9
 * 
 */
//表示产生文档,比如通过javadoc产生文档, 将此注解包含在 javadoc 中, 这个Annotation可以被写入javadoc
//在默认情况下,注释 不包括在 Javadoc 中
@Documented
//在jvm加载class时候有效, VM将在运行期也保留注释,因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)

//仅用于 Method 
@Target({ ElementType.METHOD })
public @interface ClientCache{

    /**
     * 过期时间 = max-age 属性,单位<span style="color:red">秒</span>.
     * 
     * <p>
     * 举例:
     * Cache-Control: max-age=3600
     * 
     * 只需要设置 <code>@ClientCache(TimeInterval.SECONDS_PER_HOUR)</code>
     * </p>
     * <p>
     * if value <=0 表示不缓存<br>
     * 默认:0 不缓存
     * </p>
     * 
     * 设置为int类型,int 最大值是{@link Integer#MAX_VALUE} 为 68.096259734906,参见 {@link TimeInterval} ,绝对够用了
     *
     * @return the int
     * @since 1.0.9
     */
    int value() default 0;
}
