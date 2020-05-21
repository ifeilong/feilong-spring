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
package com.feilong.spring.web.util;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.bean.ConvertUtil.toMap;

import java.util.Map;

import org.junit.Test;

import com.feilong.core.net.URIUtil;
import com.feilong.test.AbstractTest;

/**
 * The Class MultiUriTemplateUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class MultiUriTemplateUtilTest extends AbstractTest{

    @Test
    public void testExpandWithMultiVariable(){
        String requestPath = "/s/c-m-c-s-k-s100-o.htm";
        String matchingPatternPath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
        String variableName = "style";
        String value = "Lifestyle / Graphic";
        String valueSeparator = ",";
        LOGGER.debug(MultiUriTemplateUtil.expandWithMultiVariable(requestPath, matchingPatternPath, variableName, value, valueSeparator));
    }

    @Test
    public void expandWithMultiVariableMap(){
        String matchingPatternPath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
        Map<String, String> map = toMap("categoryCode", "2541", "style", "100");

        String variableName = "style";
        String value = URIUtil.encode("Lifestyle / Graphic", UTF8);

        String valueSeparator = ",";
        LOGGER.debug(MultiUriTemplateUtil.expandWithMultiVariable(matchingPatternPath, map, variableName, value, valueSeparator));
    }

    @Test
    public void testExpandWithMultiVariable2(){
        String requestPath = "/s/c-m-c-s-k-s100,200-o.htm";
        String matchingPatternPath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
        String variableName = "style";
        String value = "Lifestyle / Graphic";
        String valueSeparator = "@";
        LOGGER.debug(MultiUriTemplateUtil.expandWithMultiVariable(requestPath, matchingPatternPath, variableName, value, valueSeparator));
    }

    @Test
    public void removeMultiVariableValue(){
        String requestPath = "/s/c-m-c-s-k-s500,100,200,9000-o.htm";
        String matchingPatternPath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
        String variableName = "style";
        String value = "200";
        String valueSeparator = ",";
        LOGGER.debug(MultiUriTemplateUtil.removeMultiVariableValue(requestPath, matchingPatternPath, variableName, value, valueSeparator));
    }

    @Test
    public void removeMultiVariableValue222(){
        String requestPath = "/s/c-m-c-s-k-s-o.htm";
        String matchingPatternPath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
        String variableName = "style";
        String value = "200";
        String valueSeparator = ",";
        LOGGER.debug(MultiUriTemplateUtil.removeMultiVariableValue(requestPath, matchingPatternPath, variableName, value, valueSeparator));
    }

    @Test
    public void removeMultiVariableValue2222(){
        String requestPath = "/s/c-m-c-s-k-s500,100,200,9000-o.htm";
        String matchingPatternPath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
        String variableName = "style";
        String value = "20000";
        String valueSeparator = ",";
        LOGGER.debug(MultiUriTemplateUtil.removeMultiVariableValue(requestPath, matchingPatternPath, variableName, value, valueSeparator));
    }
}
