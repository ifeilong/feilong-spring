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
package com.feilong.spring.web.servlet.tags;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.feilong.spring.web.util.WebSpringUtil;

/**
 * 需要和spring控制的业务层交互,且仅仅设置作用域,请使用这个基类.
 * 
 * <pre class="code">
 * 只需要重写doExecute()方法即可
 * </pre>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.web.servlet.tags.RequestContextAwareTag
 * @since 1.0.0
 * @deprecated 建议不直接使用 {@link RequestContextAwareTag},使用feilong 原生的 {@link "BaseTag"},如果要想在tag里面得到 spring相关的bean ,可以单独使用
 *             {@link WebSpringUtil#getBean(HttpServletRequest, Class)}等方法
 */
@Deprecated
public abstract class AbstractSpringScopeTag extends BaseSpringTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -9131054886965798341L;

    /** 设置作用域名称 类似于bean:define标签. */
    @SuppressWarnings("hiding")
    protected String          id;

    /**
     * 标签开始.
     *
     * @return the int
     */
    @Override
    public int doStartTagInternal(){
        // 重写该方法
        doExecute();
        return SKIP_BODY; // 表示不用处理标签体,直接调用doEndTag()方法
    }

    /**
     * 执行方法,重写该方法.
     */
    protected abstract void doExecute();

    /**
     * 设置 设置作用域名称 类似于bean:define标签.
     *
     * @param id
     *            the id to set
     */
    @Override
    public void setId(String id){
        this.id = id;
    }
}