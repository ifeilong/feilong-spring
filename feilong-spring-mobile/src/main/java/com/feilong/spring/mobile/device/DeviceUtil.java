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
package com.feilong.spring.mobile.device;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.mobile.device.LiteDeviceResolver;

/**
 * The Class DeviceUtil.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.mobile.device.DeviceUtils
 * @since 1.6.0
 */
public final class DeviceUtil{

    /**
     * The Constant DEVICE_RESOLVER.
     * 
     * @see org.springframework.mobile.device.LiteDeviceResolver#LiteDeviceResolver()
     */
    private static final DeviceResolver DEVICE_RESOLVER = new LiteDeviceResolver();

    /** Don't let anyone instantiate this class. */
    private DeviceUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 通过 {@link HttpServletRequest} 解析获得 {@link Device},以便区分 {@link Device#isMobile()},{@link Device#isNormal()},{@link Device#isTablet()}.
     * 
     * <p>
     * 此方法适用于没有配置 {@link DeviceResolverHandlerInterceptor}或者 {@link DeviceResolverRequestFilter}或者
     * {@link DeviceHandlerMethodArgumentResolver} 的场景,如果你所在项目的环境配置了上述的参数,可以直接使用
     * {@link org.springframework.mobile.device.DeviceUtils#getCurrentDevice(HttpServletRequest)}
     * </p>
     * 
     * <p>
     * 以往的实现方式是通过配置 {@link DeviceResolverHandlerInterceptor}或者 {@link DeviceResolverRequestFilter}或者
     * {@link DeviceHandlerMethodArgumentResolver},<br>
     * 目前的实现方式是:按需加载,当有需要的时候调用该方法
     * </p>
     * 
     * <p>
     * 目前 spring mobile的默认实现是 {@link LiteDeviceResolver},参见
     * {@link org.springframework.mobile.device.DeviceResolverHandlerInterceptor#DeviceResolverHandlerInterceptor()}
     * </p>
     *
     * @param request
     *            request
     * @return {@link Device}
     * @see org.springframework.mobile.device.DeviceResolverHandlerInterceptor#DeviceResolverHandlerInterceptor()
     * @see org.springframework.mobile.device.LiteDeviceResolver#resolveDevice(HttpServletRequest)
     * @see org.springframework.mobile.device.DeviceUtils#getCurrentDevice(HttpServletRequest)
     */
    public static Device getDevice(HttpServletRequest request){
        return DEVICE_RESOLVER.resolveDevice(request);
    }
}
