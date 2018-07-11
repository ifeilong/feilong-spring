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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.SortUtil.sortMapByKeyAsc;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.spring.web.event.AbstractContextRefreshedHandlerMethodLogginEventListener;
import com.feilong.spring.web.servlet.handler.HandlerMappingUtil;

/**
 * 显示{@link ClientCache} 信息.
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

16:46:11 INFO  (ContextRefreshedEventClientCacheListener.java:117) onApplicationEvent() - url And ClientCache info:    {
        "/clientcache": "20秒",
        "/item/{itemid}": "5分钟",
        "/noclientcache": "0"
    }
 * 
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
    <bean id="contextRefreshedEventClientCacheListener" class=
"com.feilong.spring.web.servlet.interceptor.clientcache.ContextRefreshedClientCacheInfoEventListener" />
}
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.context.event.SmartApplicationListener
 * @since 1.10.4
 */
public class ContextRefreshedClientCacheInfoEventListener extends AbstractContextRefreshedHandlerMethodLogginEventListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextRefreshedClientCacheInfoEventListener.class);

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.event.AbstractContextRefreshedHandlerMethodLogginEventListener#doLogging(java.util.Map)
     */
    @Override
    protected void doLogging(Map<RequestMappingInfo, HandlerMethod> requestMappingInfoAndHandlerMethodMap){
        Map<String, String> urlAndClientCacheMap = HandlerMappingUtil.buildUrlAndAnnotationStringMap(
                        requestMappingInfoAndHandlerMethodMap,
                        ClientCache.class,
                        ClientCacheToStringBuilder.INSTANCE);

        //---------------------------------------------------------------
        if (isNullOrEmpty(urlAndClientCacheMap)){
            LOGGER.info("no @RequestMapping method has [@ClientCache] annotation,do nothing~");
            return;
        }

        //---------------------------------------------------------------
        LOGGER.info(
                        "url And ClientCache,size:[{}], info:{}",
                        urlAndClientCacheMap.size(),
                        JsonUtil.format(sortMapByKeyAsc(urlAndClientCacheMap)));
    }

}
