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

import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.json.JsonUtil;

@SuppressWarnings("static-method")
@Controller
public class GitInfoController{

    @RequestMapping("/monitor/gitinfo")
    @ResponseBody
    public String showGitInfo(){
        //git.properties
        String baseName = "git";

        ResourceBundle resourceBundle = ResourceBundleUtil.getResourceBundle(baseName);
        Map<String, String> map = ResourceBundleUtil.toMap(resourceBundle);
        return JsonUtil.toString(map);
    }

}
