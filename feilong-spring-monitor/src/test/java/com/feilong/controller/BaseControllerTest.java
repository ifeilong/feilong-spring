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
package com.feilong.controller;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.json.JsonUtil;
import com.feilong.spring.context.ApplicationContextUtil;
import com.feilong.test.AbstractTest;

//Spring-mvc-test does not read the web.xml file, but you can configure the filters this way:
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:springmvc/springmvc-servlet.xml" })
//测试环境使用,用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的;value指定web应用的根;
@WebAppConfiguration(value = "src/main/webapp")
public class BaseControllerTest extends AbstractTest{

    /** The mock mvc. */
    protected MockMvc                 mockMvc;

    //---------------------------------------------------------------

    /** The request. */
    protected MockHttpServletRequest  request;

    /** The response. */
    protected MockHttpServletResponse response;

    //---------------------------------------------------------------

    /** The context. */
    @Autowired
    protected WebApplicationContext   webApplicationContext;

    //---------------------------------------------------------------

    @Before
    public void before(){
        DefaultMockMvcBuilder defaultMockMvcBuilder = MockMvcBuilders.webAppContextSetup(webApplicationContext);
        this.mockMvc = defaultMockMvcBuilder.build();
    }

    //---------------------------------------------------------------

    @Before
    public void setUp() throws Exception{
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
    }

    //---------------------------------------------------------------
    /**
     * Perform.
     *
     * @param urlTemplate
     *            the url template
     * @param urlVariables
     *            the url variables
     * @since 1.0.9
     */
    protected void perform(String urlTemplate,Object...urlVariables){
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(urlTemplate, urlVariables);
        ResultHandler resultHandler = MockMvcResultHandlers.print();
        try{
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            ResultActions andDo = resultActions.andDo(resultHandler);
        }catch (Exception e){
            throw new DefaultRuntimeException(e);
        }
    }

    protected ModelAndView handle(HttpServletRequest request){
        LOGGER.debug(JsonUtil.format(ApplicationContextUtil.getApplicationContextInfoMapForLog(webApplicationContext)));

        HandlerMapping handlerMapping = webApplicationContext.getBean(HandlerMapping.class);

        try{
            HandlerExecutionChain handler = handlerMapping.getHandler(request);

            // assertNotNull("No handler found for request, check you request mapping", handler);
            Object controller = handler.getHandler();

            // if you want to override any injected attributes do it here

            //Deprecated. in Spring 3.2 in favor of RequestMappingHandlerAdapter
            //HandlerAdapter handlerAdapter = new AnnotationMethodHandlerAdapter();

            HandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();

            ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

            LOGGER.debug(JsonUtil.format(modelAndView));
            return modelAndView;
        }catch (Exception e){
            throw new DefaultRuntimeException(e);
        }
    }
}
