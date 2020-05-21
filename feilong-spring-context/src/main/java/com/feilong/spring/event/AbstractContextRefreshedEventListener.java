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
package com.feilong.spring.event;

import static com.feilong.core.date.DateUtil.formatDuration;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.feilong.context.BeanLogMessageBuilder;

/**
 * The listener interface for receiving contextStartedLogging events.
 * 
 * <p>
 * The class that is interested in processing a contextStartedLogging event implements this interface, and the object created
 * with that class is registered with a component using the component's <code>addContextStartedLoggingListener<code> method.<br>
 * 
 * When the contextStartedLogging event occurs, that object's appropriate method is invoked.
 * </p>
 * 
 * <p>
 * 只有一个ApplicationContextEvent,表示ApplicationContext容器事件,且其又有如下实现：
 * </p>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * <th align="left">备注</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>ContextStartedEvent</td>
 * <td>ApplicationContext启动后触发的事件</td>
 * <td>目前版本没有任何作用</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>ContextStoppedEvent</td>
 * <td>ApplicationContext停止后触发的事件</td>
 * <td>目前版本没有任何作用</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>ContextRefreshedEvent</td>
 * <td>ApplicationContext初始化或刷新完成后触发的事件</td>
 * <td>容器初始化完成后调用</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>ContextClosedEvent</td>
 * <td>ApplicationContext关闭后触发的事件；</td>
 * <td>（如web容器关闭时自动会触发spring容器的关闭,如果是普通java应用,需要调用ctx.registerShutdownHook();注册虚拟机关闭时的钩子才行）</td>
 * </tr>
 * 
 * </table>
 * 
 * <p>
 * 注: {@link org.springframework.context.support.AbstractApplicationContext}
 * 抽象类实现了LifeCycle的start和stop回调并发布ContextStartedEvent和ContextStoppedEvent事件；但是无任何实现调用它,所以目前无任何作用。
 * </p>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.context.event.SmartApplicationListener
 * @since 1.10.4
 */
public abstract class AbstractContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent>{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractContextRefreshedEventListener.class);

    //---------------------------------------------------------------

    /** Post construct. */
    @PostConstruct
    protected void postConstruct(){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(BeanLogMessageBuilder.buildFieldsMessage(this));
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        Date beginDate = new Date();

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("begin [{}] 's onApplicationEvent", buildKeyMessage());
        }
        //---------------------------------------------------------------

        doOnApplicationEvent(contextRefreshedEvent);

        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("end [{}] 's onApplicationEvent,use time: [{}]", buildKeyMessage(), formatDuration(beginDate));
        }
    }

    //---------------------------------------------------------------

    /**
     * @return
     * @since 1.12.7
     */
    protected String buildKeyMessage(){
        return this.getClass().getName();
    }

    //---------------------------------------------------------------

    /**
     * Do on application event.
     *
     * @param contextRefreshedEvent
     *            the context refreshed event
     * @since 1.12.7
     */
    public abstract void doOnApplicationEvent(ContextRefreshedEvent contextRefreshedEvent);
}
