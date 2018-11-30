/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.b.security;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toStrings;
import static com.feilong.core.util.MapUtil.newConcurrentHashMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.feilong.servlet.http.RequestUtil;

/**
 * 需要 requireCsrfProtectionMatcher 保护的.
 * 
 * <p>
 * 用户 {@link org.springframework.security.web.csrf.CsrfFilter}
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class CsrfSecurityRequestMatcher implements RequestMatcher{

    /** The Constant log. */
    private static final Logger         LOGGER                        = LoggerFactory.getLogger(CsrfSecurityRequestMatcher.class);

    //---------------------------------------------------------------

    /** 忽略的url白名单，支持正则匹配. */
    private List<String>                whiteList;

    /**
     * 忽略的请求method.
     * 
     * @since bluebox 1B
     */
    private String[]                    ignoredRequestMethods         = toStrings("GET,HEAD,TRACE,OPTIONS");

    /**
     * 请求的url地址以及是否在白名单的map mapping.
     *
     * @since bluebox 1B
     */
    private static Map<String, Boolean> requestURIAndIsInWhiteListMap = newConcurrentHashMap();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.web.util.matcher.RequestMatcher#matches(javax.servlet.http.HttpServletRequest)
     */
    @Override
    //如果不需要保护, 返回false
    public boolean matches(HttpServletRequest request){
        String requestURI = request.getRequestURI();
        //判断 method
        String method = request.getMethod();
        if (RequestUtil.isSupportMethod(ignoredRequestMethods, method)){
            LOGGER.debug("requestURI:[{}],method is [{}],should not csrf", requestURI, method);
            return false;
        }

        //---------------------------------------------------------------
        if (RequestUtil.isStaticResource(requestURI)){
            LOGGER.debug("requestURI:[{}],isStaticResource,should not csrf", requestURI);
            return false;
        }

        //---------------------------------------------------------------
        return isNeedCsrfUrl(requestURI, request);
    }

    //---------------------------------------------------------------

    /**
     * 请求的url是否需要csrf.
     *
     * @param requestURI
     *            the request URI
     * @param request
     *            the request
     * @return true, if is need csrf url
     * @since bluebox 1B
     */
    private boolean isNeedCsrfUrl(String requestURI,HttpServletRequest request){
        if (isNullOrEmpty(whiteList)){
            return true;
        }

        //---------------------------------------------------------------
        //如果在缓存里面，那么直接返回
        Boolean isInWhiteList = requestURIAndIsInWhiteListMap.get(requestURI);
        if (null != isInWhiteList){
            LOGGER.debug("requestURI:[{}],in requestURIAndIsInWhiteListMap,value is :[{}]", requestURI, isInWhiteList);
            return isInWhiteList;
        }

        //---------------------------------------------------------------
        boolean needCsrfUrl = isNeedCsrfUrl(requestURI, whiteList, request);
        requestURIAndIsInWhiteListMap.put(requestURI, needCsrfUrl);
        //default is true need csrf
        return needCsrfUrl;
    }

    //---------------------------------------------------------------

    /**
     * Checks if is need csrf url.
     *
     * @param requestURI
     *            the request URI
     * @param whiteList
     *            the white list
     * @param request
     *            the request
     * @return true, if is need csrf url
     * @since bluebox 1B
     */
    private static boolean isNeedCsrfUrl(String requestURI,List<String> whiteList,HttpServletRequest request){
        for (String whiteUrl : whiteList){
            RequestMatcher requestMatcher = new AntPathRequestMatcher(whiteUrl);
            boolean matches = requestMatcher.matches(request);
            if (matches){
                LOGGER.info("requestURI:[{}],in whiteList,should not csrf", requestURI);
                return false;
            }
        }
        return true;
    }

    //---------------------------------------------------------------

    /**
     * 设置 忽略的url白名单，支持正则匹配.
     *
     * @param whiteList
     *            the new 忽略的url白名单，支持正则匹配
     */
    public void setWhiteList(List<String> whiteList){
        this.whiteList = whiteList;
    }

    /**
     * 设置 忽略的请求method.
     *
     * @param ignoredRequestMethods
     *            the ignoredRequestMethods to set
     */
    public void setIgnoredRequestMethods(String[] ignoredRequestMethods){
        this.ignoredRequestMethods = ignoredRequestMethods;
    }
}
