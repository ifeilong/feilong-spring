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

import org.springframework.web.multipart.MultipartFile;

/**
 * The Interface MultipartFileResolver.
 *
 * @author feilong
 * @version 1.5.4 2016年5月2日 下午10:05:05
 * @since 1.5.4
 */
public interface MultipartFileResolver{

    /**
     * 将<code>MultipartFile</code>上传到指定目录<code>directoryName</code>下面重命名指定的文件名称<code>fileName</code>.
     *
     * @param multipartFile
     *            the multipart file
     * @param directoryName
     *            the directory name
     * @param fileName
     *            the file name
     */
    void upload(MultipartFile multipartFile,String directoryName,String fileName);
}
