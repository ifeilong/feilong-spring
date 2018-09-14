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
package com.feilong.spring.security.web.util.matcher;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.MapUtil.newConcurrentHashMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.ELRequestMatcher;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEditor;

/**
 * 简单的 基于 {@link AntPathRequestMatcher} 的 patternList 的 {@link RequestMatcher} 实现.
 * 
 * <p>
 * 有缓存的实现，效率高
 * </p>
 * 
 * <p>
 * 可用于 {@link org.springframework.security.web.csrf.CsrfFilter#setRequireCsrfProtectionMatcher(RequestMatcher)} 中的 ，CsrfFilter
 * 的requireCsrfProtectionMatcher， 如果request 匹配 requireCsrfProtectionMatcher，那么会进行csrf处理
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.security.web.csrf.CsrfFilter#setRequireCsrfProtectionMatcher(RequestMatcher)
 * 
 * @see AndRequestMatcher
 * @see AnyRequestMatcher
 * @see OrRequestMatcher
 * 
 * @see AntPathRequestMatcher
 * @see RegexRequestMatcher
 * @see ELRequestMatcher
 * 
 * @see IpAddressMatcher
 * @see MediaTypeRequestMatcher
 * @see NegatedRequestMatcher
 * @see RequestHeaderRequestMatcher
 * 
 * @see RequestMatcherEditor
 * @since 4.0.2
 */
public class SimpleAntPathListRequestMatcher implements RequestMatcher{

    /** The Constant log. */
    private static final Logger               LOGGER        = LoggerFactory.getLogger(SimpleAntPathListRequestMatcher.class);

    //---------------------------------------------------------------

    /** 模式list. */
    private List<String>                      patternList;

    /** 是否忽略大小写,默认是true. */
    private boolean                           caseSensitive = true;

    //---------------------------------------------------------------

    /** 请求的url地址以及是否在匹配的map mapping. */
    private static final Map<String, Boolean> CACHE         = newConcurrentHashMap();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.web.util.matcher.RequestMatcher#matches(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public boolean matches(HttpServletRequest request){
        //如果没有设置 patternList，返回false 不匹配
        if (isNullOrEmpty(patternList)){
            return false;
        }

        //---------------------------------------------------------------
        String requestURI = request.getRequestURI();
        //如果在缓存里面，那么直接返回
        Boolean isMatch = CACHE.get(requestURI);

        if (null != isMatch){
            LOGGER.debug("requestURI:[{}],in cache,value is:[{}],will return it", requestURI, isMatch);
            return isMatch;
        }

        //---------------------------------------------------------------
        boolean isMatchValue = matches(patternList, caseSensitive, request);

        CACHE.put(requestURI, isMatchValue);

        return isMatchValue;
    }

    //---------------------------------------------------------------

    /**
     * Matches.
     *
     * @param whiteList
     *            the white list
     * @param caseSensitive
     *            the case sensitive
     * @param request
     *            the request
     * @return true, if successful
     */
    private static boolean matches(List<String> whiteList,boolean caseSensitive,HttpServletRequest request){
        for (String whiteUrl : whiteList){
            RequestMatcher requestMatcher = new AntPathRequestMatcher(whiteUrl, null, caseSensitive);
            if (requestMatcher.matches(request)){
                LOGGER.info("requestURI:[{}],in whiteList,should not csrf", request.getRequestURI());
                return true;
            }
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * 设置 模式list.
     *
     * @param patternList
     *            the patternList to set
     */
    public void setPatternList(List<String> patternList){
        this.patternList = patternList;
    }

    /**
     * 设置 是否忽略大小写.
     *
     * @param caseSensitive
     *            the caseSensitive to set
     */
    public void setCaseSensitive(boolean caseSensitive){
        this.caseSensitive = caseSensitive;
    }

    /**
     * 获得 模式list.
     *
     * @return the patternList
     */
    public List<String> getPatternList(){
        return patternList;
    }

    /**
     * 获得 是否忽略大小写,默认是true.
     *
     * @return the caseSensitive
     */
    public boolean getCaseSensitive(){
        return caseSensitive;
    }

}
