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
package com.feilong.spring.web.servlet.interceptor.seo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.feilong.core.bean.PropertyUtil;
import com.feilong.web.command.SubViewCommand;
import com.feilong.web.command.ViewCommand;

/**
 * 专门处理每个页面的seo信息,在 {@link HandlerInterceptorAdapter#preHandle(HttpServletRequest, HttpServletResponse, Object)}流程中,查找 request作用域中的数据.
 * 
 * <h3>使用指南:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>Model1:什么都不设置,那么使用默认配置的 {@link #defaultSeoViewCommand},如果这些参数也没有设置,那么页面相关地方会输出空</li>
 * <li>Model2:可以在controller {@link RequestMapping} 方法体里面,使用
 * 
 * <pre>
 * SeoViewCommand defaultSeoViewCommand = new DefaultSeoViewCommand();
 * defaultSeoViewCommand.setSeoDescription(xxx);
 * defaultSeoViewCommand.setSeoKeywords(xxx);
 * defaultSeoViewCommand.setSeoTitle(xxx);
 * </pre>
 * 
 * 自定义设置一个SeoViewCommand对象, 然后,将此对象 设置到 request/model中</li>
 * 
 * <li>Model3:如果使用了 {@link ViewCommand}作为整体数据返回,那么只需要让 您自己的{@link ViewCommand} 实现 {@link SeoViewCommand}接口,实现里面的方法即可</li>
 * 
 * <li>Model4:如果使用了 {@link ViewCommand}作为整体数据返回,并且也使用了 {@link SubViewCommand},并且想将参数设置到该{@link SubViewCommand}内,你可以让 您自己的
 * {@link SubViewCommand} 实现 {@link SeoViewCommand}接口,实现里面的方法即可</li>
 * </ol>
 * </blockquote>
 * 
 * <p>
 * 更多说明,请参考 {@link StandardSeoInterceptor}
 * </p>
 *
 * @author feilong
 * @version 5.0.0 2015年7月14日 下午8:30:14
 * @see StandardSeoInterceptor
 * @since 5.0.0
 */
public class ViewCommandFoundSeoInterceptor extends StandardSeoInterceptor{

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.seo.AbstractSeoInterceptor#constructSeoViewCommand(java.lang.String,
     * java.lang.Object)
     */
    @Override
    protected SeoViewCommand buildSeoViewCommandFromRequestAttributeValue(String requestAttributeName,Object requestAttributeValue){
        if (requestAttributeValue instanceof ViewCommand){
            //级联查询
            SeoViewCommand seoViewCommand = PropertyUtil.findValueOfType(requestAttributeValue, SeoViewCommand.class);
            if (null != seoViewCommand){
                return seoViewCommand;
            }
        }
        return null;
    }

}