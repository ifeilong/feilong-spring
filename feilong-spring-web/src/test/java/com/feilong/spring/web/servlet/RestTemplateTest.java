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
package com.feilong.spring.web.servlet;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.feilong.json.JsonUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.2.0
 */
public class RestTemplateTest{

    private static final Logger LOGGER       = LoggerFactory.getLogger(RestTemplateTest.class);

    private final RestTemplate  restTemplate = new RestTemplate();

    private final String        picUrl       = "https://scm-pim.oss-cn-shanghai.aliyuncs.com/image/"
                    + "genimg/gap_csv_photobase/DIPV1-g2cc08700onqta5inkrb/主图/官网/577535-主图-1-577535TRUE BLACK V2 2.jpg?etag=1592359501916";

    private final String        posturl      = "http://127.0.0.1:8087/upload/image";

    @Test
    public void testRestTemplateTest() throws MalformedURLException{
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        URL url = new URL(picUrl);
        form.add("file", new FileUrlResource(url));

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(form, requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(posturl, HttpMethod.POST, requestEntity, String.class);

        LOGGER.debug(JsonUtil.format(responseEntity));

    }

    @Test
    public void testRestTemplateTest222() throws MalformedURLException{
        FileUrlResource fileUrlResource = new FileUrlResource(new URL(picUrl));
        String filename = fileUrlResource.getFilename();
        LOGGER.debug(filename);

        //        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        //        formHttpMessageConverter.

        //        MimeUtility.encodeText(filename, UTF8, null);
        //        MimeDelegate.encode(filename, this.multipartCharset.name());
    }
}
