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
package com.feilong.spring.monitor.controller;

import static com.feilong.core.bean.ConvertUtil.toMap;

import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.json.JsonUtil;

/**
 * 用来给运维平台健康扫描用的.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.4
 */
@Controller
public class HealthController{

    /**
     * 和后端保持 url 一致.
     *
     * @return the string
     */
    @RequestMapping("/customize_health")
    @ResponseBody
    public String invoke(){
        return JsonUtil.format(buildMapData(), 0, 0);
    }

    /**
     * Builds the map data.
     *
     * @return the map< string,? extends object>
     */
    private static Map<String, ? extends Object> buildMapData(){
        //git.properties
        String name = "git";
        String key = "git.commit.id";

        //---------------------------------------------------------------

        try{
            ResourceBundle resourceBundle = ResourceBundleUtil.getResourceBundle(name);
            String commitId = ResourceBundleUtil.getValue(resourceBundle, key);
            return toMap("status", 200, "codeVersion", commitId);
        }catch (Exception e){
            return toMap("status", 501, "codeVersion", "Exception " + e.getMessage());
        }
    }

}
