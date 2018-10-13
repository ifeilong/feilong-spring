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

import org.springframework.web.servlet.view.AbstractView;

import com.feilong.servlet.http.ResponseUtil;

/**
 * The Class PrintWriterView.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.3
 */
public class PrintWriterView extends AbstractView{

    /** The content. */
    private String content;

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

        ResponseUtil.writeText(response, content);
    }

    //---------------------------------------------------------------

    /**
     * Gets the content.
     *
     * @return the content
     */
    public String getContent(){
        return content;
    }

    /**
     * Sets the content.
     *
     * @param content
     *            the content to set
     */
    public void setContent(String content){
        this.content = content;
    }
}
