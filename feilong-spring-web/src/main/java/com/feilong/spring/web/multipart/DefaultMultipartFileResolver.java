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
package com.feilong.spring.web.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.feilong.io.IOWriteUtil;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.Validate;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 需要在spring相关xml里面配置
 *
 * <pre class="code">
{@code
    <bean id="multipartFileResolver" class="com.feilong.spring.web.multipart.DefaultMultipartFileResolver" />
}
 * </pre>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 * @since 1.12.0 rename
 */
public class DefaultMultipartFileResolver implements MultipartFileResolver{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMultipartFileResolver.class);

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.controller.MultipartFileResolver#upload(org.springframework.web.multipart.MultipartFile, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void upload(MultipartFile multipartFile,String directoryName,String fileName){
        Validate.notNull(multipartFile, "multipartFile can't be null!");

        if (multipartFile.isEmpty()){
            LOGGER.warn("multipartFile is empty,but you want to save to directoryName:[{}],fileName:[{}]", directoryName, fileName);
            return;
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            Map<String, Object> map = MultipartFileUtil.getMultipartFileInfoMapForLogMap(multipartFile);
            LOGGER.debug(JsonUtil.format(map));
        }

        try (InputStream inputStream = multipartFile.getInputStream()){
            IOWriteUtil.write(inputStream, directoryName, fileName);
        }catch (IOException e){
            throw new UncheckedIOException(Slf4jUtil.format("directoryName:[{}],fileName:[{}]", directoryName, fileName), e);
        }
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.multipart.MultipartFileResolver#upload(org.springframework.web.multipart.MultipartFile[],
     * java.lang.String, java.lang.String[])
     */
    @Override
    public void upload(MultipartFile[] multipartFiles,String directoryName,String[] fileNames){
        Validate.notNull(multipartFiles, "multipartFiles can't be null!");

        for (int i = 0; i < multipartFiles.length; ++i){
            MultipartFile multipartFile = multipartFiles[i];
            if (multipartFile.isEmpty()){
                continue;
            }
            String fileName = fileNames[i];
            Validate.notEmpty(fileName, "fileName can't be null/empty!");

            upload(multipartFile, directoryName, fileName);
        }
    }
}
