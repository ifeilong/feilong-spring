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
package com.feilong.spring.web.event;

import static com.feilong.core.util.SortUtil.sortListByPropertyNamesValue;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.accessor.cookie.CookieAccessor;
import com.feilong.spring.web.event.builder.CookieAccessorBeanToMapBuilder;

/**
 * 启动的时候,显示 cookie 信息.
 * 
 * 
 * <h3>作用:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 可以在日志文件或者控制台输出如下信息:
 * </p>
 * 
 * <pre class="code">
 * 
14:39:39 INFO  (ContextRefreshedCookieAccessorEventListener.java:118) doLog() - CookieAccessor size:[2], Info:
beanName                name     httpOnly path maxAge domain secure version isValueEncoding 
----------------------- -------- -------- ---- ------ ------ ------ ------- --------------- 
loginBindCookieAccessor l_b_s    true     /    -1            false  0       false           
nickNameCookieAccessor  nickName false    /    365天          false  0       false
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>参考配置:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
{@code 

    <!-- 启动的时候,显示 cookie 信息 -->
    <bean id="contextRefreshedCookieAccessorEventListener" class=
"com.feilong.spring.web.event.ContextRefreshedCookieAccessorEventListener" />

}
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.context.event.SmartApplicationListener
 * @since 1.10.4
 * @deprecated since 1.11.4 ,you can use ContextRefreshedBeanLoggingEventListener
 */
@Deprecated
public class ContextRefreshedCookieAccessorEventListener extends ContextRefreshedBeanLoggingEventListener<CookieAccessor>{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextRefreshedCookieAccessorEventListener.class);

    //---------------------------------------------------------------

    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct1(){
        super.setBeanClass(CookieAccessor.class);
        super.setBeanToMapBuilder(CookieAccessorBeanToMapBuilder.INSTANCE);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.event.ContextRefreshedBeanLoggingEventListener#doLog(java.util.List)
     */
    @Override
    protected void doLog(List<Map<String, Object>> list){
        String name = "CookieAccessor";
        LOGGER.info("{} size:[{}], Info:{}", name, list.size(), formatToSimpleTable(sortListByPropertyNamesValue(list, "name")));
    }
}
