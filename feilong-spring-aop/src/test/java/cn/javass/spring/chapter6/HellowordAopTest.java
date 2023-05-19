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
package cn.javass.spring.chapter6;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.javass.spring.chapter6.service.IHelloWorldService;

@SuppressWarnings("static-method")
public class HellowordAopTest{

    @Test
    public void test(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("test-helloword.xml");

        IHelloWorldService helloworldService = applicationContext.getBean("helloWorldService", IHelloWorldService.class);
        helloworldService.sayHello();
    }
}
