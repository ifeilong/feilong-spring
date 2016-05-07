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
 * 设置浏览器客户端缓存.
 * 
 * <p>
 * 此注解,用于 {@link ClientCacheInterceptor},如果方法没有使用 <code>@ClientCache</code>,那么拦截器什么都不做
 * </p>
 * 
 * <h3>step1:springmvc XML配置</h3>
 * <blockquote>
 * 
 * <pre class="code">
{@code
    <mvc:interceptors>

        <mvc:interceptor>

            <!-- 默认所有 -->
            <!-- json请求不排除, 反正这个cache 可以通过程序控制 -->
            <mvc:mapping path="/**" />
            <bean class="com.feilong.spring.web.servlet.interceptor.clientcache.ClientCacheInterceptor">
            </bean>
        </mvc:interceptor>
    </mvc:interceptors>
}
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>step2: {@link org.springframework.web.bind.annotation.RequestMapping} 方法上使用</h3>
 * <blockquote>
 * 
 * <p>
 * 1.如果我的页面,需要设置5分钟浏览器缓存,你可以这么设置:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * 
 * &#064;Controller
 * public class StdPDPController extends AbstractStandardController{
 * 
 *  &#064;ClientCache(value = TimeInterval.SECONDS_PER_MINUTE * 5)
 *  &#064;RequestMapping(value = "/pdp/{code}",method = RequestMethod.GET)
 *  public ModelAndView doPDPHandler(HttpServletRequest request,HttpServletResponse response){
 *      .........
 *  }
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * 2.如果我的页面,<b>不能有</b>浏览器缓存,你可以这么设置:
 * </p>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * 
 * &#064;Controller
 * public class StdMemberCouponController extends BaseController{
 * 
 * &#064;ClientCache(0)
 * &#064;RequestMapping(value = "/myaccount/couponList.htm",method = RequestMethod.GET)
 * public String myCouponList(HttpServletRequest request,Model model){
 * ....
 * }
 * </pre>
 * 
 * </blockquote>
 * </blockquote>
 * 
 * <h3>不使用<code>@ClientCache</code>和<code>@ClientCache(0)</code>的区别:</h3>
 * 
 * <blockquote>
 * <p>
 * 不使用<code>@ClientCache</code>,由于拦截器不做任何处理,有些浏览器(如chrome)对get请求会自动cache,比如购物车页面,当你删除了一个商品(ajax),此时查看其他页面,再按后退按钮返回的时候,会发现原来的商品还在;<br>
 * 而当你设置了 <code>@ClientCache(0)</code>,那么会强制浏览器不设置cache,重新向服务器发送请求
 * </p>
 * </blockquote>
 *
 * @author feilong
 * @version 1.0.9 2015年3月30日 下午4:25:10
 * @see ClientCacheInterceptor
 * @since 1.0.9
 */
//表示产生文档,比如通过javadoc产生文档, 将此注解包含在 javadoc中,这个Annotation可以被写入javadoc
@Documented //在默认情况下,注释不包括在 Javadoc 中
@Retention(RetentionPolicy.RUNTIME) //在jvm加载class时候有效,VM将在运行期也保留注释,因此可以通过反射机制读取注解的信息
@Target({ ElementType.METHOD }) //仅用于 Method 
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
     * if value <=0表示不缓存<br>
     * 默认:0 不缓存
     * </p>
     * 
     * 设置为int类型,int最大值是{@link Integer#MAX_VALUE}为 68.096259734906,参见 {@link TimeInterval},绝对够用了
     *
     * @return the int
     * @since 1.0.9
     */
    int value() default 0;
}
