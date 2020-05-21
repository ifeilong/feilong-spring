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

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Test;

import com.feilong.test.AbstractTest;

/**
 * The Class HtmlUtilsTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class HtmlUtilsTest extends AbstractTest{

    /** The a. */
    String a = "m&eacute;n&nbsp;";

    /**
     * Test html utils.
     */
    @Test
    public void testHtmlUtils(){
        String specialStr = "<div id=\"testDiv\">test1;test2</div>";
        String str1 = HtmlUtils.htmlEscape(specialStr); // ①将 HTML 特殊字符转义为 HTML 通用转义序列;
        LOGGER.debug(str1);

        String str2 = HtmlUtils.htmlEscapeDecimal(specialStr);// 将 HTML 特殊字符转义为带 # 的十进制数据转义序列;
        LOGGER.debug(str2);

        String str3 = HtmlUtils.htmlEscapeHex(specialStr);// 将 HTML 特殊字符转义为带 # 的十六进制数据转义序列;
        LOGGER.debug(str3);

        // ④下面对转义后字符串进行反向操作
        LOGGER.debug(HtmlUtils.htmlUnescape(str1));
        LOGGER.debug(HtmlUtils.htmlUnescape(str2));
        LOGGER.debug(HtmlUtils.htmlUnescape(str3));

        LOGGER.debug(StringEscapeUtils.unescapeHtml4(str1));
        LOGGER.debug(StringEscapeUtils.unescapeHtml4(str2));
        LOGGER.debug(StringEscapeUtils.unescapeHtml4(str3));
    }

    /**
     * Html unescape.
     */
    @Test
    public void htmlUnescape(){
        LOGGER.debug(HtmlUtils.htmlUnescape(a));
    }

    /**
     * String escape utils.
     */
    @Test
    public void stringEscapeUtils(){
        LOGGER.debug(StringEscapeUtils.unescapeHtml4(a));
    }

    /**
     * String escape utils2.
     */
    @Test
    public void stringEscapeUtils2(){
        LOGGER.debug(StringEscapeUtils.unescapeHtml4(a));
    }
}
