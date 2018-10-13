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
package com.feilong.spring.web.servlet.view.printwriter;

import org.springframework.web.servlet.View;

import com.feilong.spring.web.servlet.view.AbstractPrefixViewResolver;

/**
 * The Class PrintWriterViewResolver.
 * 
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * 
 * <b>xml 配置:</b>
 * 
 * <pre class="code">
 * {@code
    <bean class="com.feilong.spring.web.servlet.view.printwriter.PrintWriterViewResolver">
        <property name="order" value="1" />
        <property name="prefix" value="printwrite:" />
    </bean>
 * }
 * </pre>
 * 
 * <b>项目使用:</b>
 * 
 * <pre class="code">
 * {@code
 * &#64;RequestMapping(value = { "/printwrite" })
    public String show(@RequestParam(name = "code",defaultValue = "") String code,HttpServletResponse response){
        return "printwrite:helloword" + code;
    }
    }
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * 
 * @since 4.0.3
 */
public class PrintWriterViewResolver extends AbstractPrefixViewResolver{

    /** The Constant PRINT_WRITER_PREFIX. */
    public static final String PRINT_WRITER_PREFIX = "printwrite:";

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.view.AbstractPrefixViewResolver#buildView(java.lang.String)
     */
    @Override
    protected View buildView(String content){
        return new PrintWriterView(content);
    }

}