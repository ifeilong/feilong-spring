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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
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
 * {@link RequestMatcher} implementation that returns the opposite of the decorated {@link RequestMatcher}.
 * 
 * <p>
 * 参考了 {@link org.apache.commons.collections4.functors.NotPredicate}
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.security.web.csrf.CsrfFilter#setRequireCsrfProtectionMatcher(RequestMatcher)
 * @see AndRequestMatcher
 * @see AnyRequestMatcher
 * @see OrRequestMatcher
 * @see AntPathRequestMatcher
 * @see RegexRequestMatcher
 * @see ELRequestMatcher
 * @see IpAddressMatcher
 * @see MediaTypeRequestMatcher
 * @see NegatedRequestMatcher
 * @see RequestHeaderRequestMatcher
 * @see RequestMatcherEditor
 * @see org.apache.commons.collections4.functors.NotPredicate
 * @since 4.0.2
 */
public class NotRequestMatcher implements RequestMatcher{

    /** The RequestMatcher to decorate. */
    private final RequestMatcher requestMatcher;

    //---------------------------------------------------------------

    /**
     * Factory to create the not RequestMatcher.
     *
     * @param requestMatcher
     *            the RequestMatcher to decorate, not null
     * @return the RequestMatcher
     * @throws NullPointerException
     *             if the RequestMatcher is null
     */
    public static RequestMatcher notRequestMatcher(final RequestMatcher requestMatcher){
        Validate.notNull(requestMatcher, "requestMatcher can't be null!");
        return new NotRequestMatcher(requestMatcher);
    }

    //---------------------------------------------------------------

    /**
     * Constructor that performs no validation.
     * 
     * Use {@link #notRequestMatcher(RequestMatcher)} if you want that.
     *
     * @param requestMatcher
     *            the request matcher
     */
    public NotRequestMatcher(RequestMatcher requestMatcher){
        super();
        this.requestMatcher = requestMatcher;
    }

    //---------------------------------------------------------------

    /**
     * Evaluates the requestMatcher returning the opposite to the stored RequestMatcher.
     *
     * @param request
     *            the request
     * @return true if RequestMatcher returns false
     */
    @Override
    public boolean matches(HttpServletRequest request){
        return !requestMatcher.matches(request);
    }

}
