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
package org.springframework.web.util;

import static com.feilong.core.CharsetType.UTF8;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.feilong.test.AbstractTest;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 */
public class UriUtilsTest extends AbstractTest{

    /**
     * TestUriUtilsTest.
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testUriUtilsTest() throws UnsupportedEncodingException{
        LOGGER.debug(UriUtils.decode("%", UTF8));
    }
}
