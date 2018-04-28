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
package com.feilong.spring.messagesource;

import org.apache.commons.lang3.Validate;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * The Class MessageSourceUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.1
 */
public class MessageSourceUtil{

    /** Don't let anyone instantiate this class. */
    private MessageSourceUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得消息信息.
     *
     * @param messageSource
     *            the message source
     * @param code
     *            the code
     * @param args
     *            the args
     * @return 如果 <code>messageSource</code> 是null,抛出 {@link NullPointerException}<br>
     */
    public static String getMessage(MessageSource messageSource,String code,Object...args){
        Validate.notNull(messageSource, "messageSource can't be null!");
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
