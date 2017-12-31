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
package com.feilong.spring.mobile.device.view;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.mobile.device.util.ResolverUtils;
import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ViewResolver;

/**
 * Integration of Tiles with Spring Moblie
 *
 * Dynamically loads the the location of JSP using Tiles Expression Language.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.6.0
 */
public class TilesSupportLiteDeviceDelegatingViewResolver extends LiteDeviceDelegatingViewResolver{

    /** The device type. */
    private String deviceType;

    /**
     * Instantiates a new tiles support lite device delegating view resolver.
     *
     * @param resolver
     *            the resolver
     */
    public TilesSupportLiteDeviceDelegatingViewResolver(ViewResolver resolver){
        super(resolver);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver#getDeviceViewNameInternal(java.lang.String)
     */
    @Override
    protected String getDeviceViewNameInternal(String viewName){
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
        HttpServletRequest request = ((ServletRequestAttributes) attrs).getRequest();
        Device device = DeviceUtils.getCurrentDevice(request);
        SitePreference sitePreference = SitePreferenceUtils.getCurrentSitePreference(request);

        //---------------------------------------------------------------

        String resolvedViewName = viewName;

        String attributeValue = null;
        if (ResolverUtils.isNormal(device, sitePreference)){
            resolvedViewName = getNormalPrefix() + viewName + getNormalSuffix();
            attributeValue = getNormalPrefix();
        }else if (ResolverUtils.isMobile(device, sitePreference)){
            resolvedViewName = getMobilePrefix() + viewName + getMobileSuffix();
            attributeValue = getMobilePrefix();
        }else if (ResolverUtils.isTablet(device, sitePreference)){
            resolvedViewName = getTabletPrefix() + viewName + getTabletSuffix();
            attributeValue = getTabletPrefix();
        }

        //---------------------------------------------------------------
        request.setAttribute(deviceType, attributeValue);
        // MOBILE-63 "redirect:/" and "forward:/" can result in the view name containing multiple trailing slashes
        return stripTrailingSlash(resolvedViewName);
    }

    //---------------------------------------------------------------

    /**
     * Strip trailing slash.
     *
     * @param viewName
     *            the view name
     * @return the string
     */
    private String stripTrailingSlash(String viewName){
        if (viewName.endsWith("//")){
            return viewName.substring(0, viewName.length() - 1);
        }
        return viewName;
    }

    //---------------------------------------------------------------

    /**
     * Sets the device type.
     *
     * @param deviceType
     *            the deviceType to set
     */
    public void setDeviceType(String deviceType){
        this.deviceType = deviceType;
    }
}
