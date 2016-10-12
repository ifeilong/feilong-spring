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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.tools.jsonlib.JsonUtil;

/**
 * The Class UriTemplateUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class UriTemplateUtilTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UriTemplateUtilTest.class);

    @Test
    public void testGetVariableNames(){
        List<String> list = UriTemplateUtil.getVariableNames("/c{categoryCode}/m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm");
        LOGGER.debug("list:{}", JsonUtil.format(list));
    }

    @Test
    public void testExpandWithVariable(){
        String result = UriTemplateUtil
                        .expandWithVariable("/c{categoryCode}/m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm", "color", "a");
        LOGGER.debug(result);
    }

    @Test
    public void testExpandWithVariable3(){
        String requestPath = "/s/c-m-c-s-k-s100-o.htm";
        String matchingPatternPath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
        String variableName = "color";
        String value = "100";
        LOGGER.debug(UriTemplateUtil.expandWithVariable(requestPath, matchingPatternPath, variableName, value));
    }

    @Test
    public void clearVariablesValue(){
        String requestPath = "/s/c500-m60-cred-s-k-s100-o6.htm";
        String matchingPatternPath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
        String[] variableNames = { "color", "style" };

        assertEquals("/s/c500-m60-c-s-k-s-o6.htm", UriTemplateUtil.clearVariablesValue(requestPath, matchingPatternPath, variableNames));
    }

    @Test
    public void retainVariablesValue(){
        String requestPath = "/s/c500-m60-cred-s-k-s100-o6.htm";
        String matchingPatternPath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
        String[] variableNames = { "color", "style" };
        LOGGER.debug(UriTemplateUtil.retainVariablesValue(requestPath, matchingPatternPath, variableNames));
        LOGGER.debug(UriTemplateUtil.retainVariablesValue(requestPath, matchingPatternPath, null));
    }

    @Test
    public void testExtractUriTemplateVariables(){
        String requestPath = "/c/m-caaa-s-k-s-o.htm";
        String matchingPatternPath = "/c{categoryCode}/m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
        Map<String, String> map = UriTemplateUtil.extractUriTemplateVariables(requestPath, matchingPatternPath);
        LOGGER.debug("map:{}", JsonUtil.format(map));
    }

    @Test
    public void testExpandWithVariable2(){
        String matchingPatternPath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";
        String variableName = "color";
        String value = "100";
        LOGGER.debug(UriTemplateUtil.expandWithVariable(matchingPatternPath, variableName, value));
    }

    @Test
    public void testExpand(){
        String uriTemplatePath = "/s/c{categoryCode}-m{material}-c{color}-s{size}-k{kind}-s{style}-o{order}.htm";

        Map<String, String> map = new HashMap<>();
        map.put("color", "100");
        map.put("size", "L");
        map.put("K", "aaaa");

        LOGGER.debug(UriTemplateUtil.expand(uriTemplatePath, map));
    }
}
