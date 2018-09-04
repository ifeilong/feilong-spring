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
package org.broadleafcommerce.profile.web.core.security;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.token.Sha512DigestUtils;

public class Sha512DigestUtilsTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(Sha512DigestUtilsTest.class);

    @Test
    public void test(){
        //l_b_s 2284208963  10.88.54.164    /   会话  15 B    ✓

        LOGGER.debug(Sha512DigestUtils.shaHex("2284208963"));
    }
}
