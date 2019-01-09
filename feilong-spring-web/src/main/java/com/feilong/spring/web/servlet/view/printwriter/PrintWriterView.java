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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import com.feilong.servlet.http.ResponseUtil;

/**
 * 直接输出的 view.
 * 
 * <p>
 * 实现原理,调用 {@link com.feilong.servlet.http.ResponseUtil#writeText(HttpServletResponse, Object)} 方法
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.3
 */
public class PrintWriterView extends AbstractView{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintWriterView.class);

    //---------------------------------------------------------------

    /** 待输出的内容. */
    private String              content;

    //---------------------------------------------------------------

    /**
     * Instantiates a new 打印 writer view.
     *
     * @param content
     *            the content
     */
    public PrintWriterView(String content){
        super();
        this.content = content;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,HttpServletRequest request,HttpServletResponse response)
                    throws Exception{
        //since 4.0.4 add log
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("[{}] will write:[{}]", request.getRequestURI(), content);
        }
        //---------------------------------------------------------------
        ResponseUtil.writeText(response, content);
    }

    //---------------------------------------------------------------

    /**
     * 获得 待输出的内容.
     *
     * @return the 待输出的内容
     */

    public String getContent(){
        return content;
    }

    /**
     * 设置 待输出的内容.
     *
     * @param content
     *            the new 待输出的内容
     */
    public void setContent(String content){
        this.content = content;
    }
}
