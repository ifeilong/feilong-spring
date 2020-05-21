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
package com.feilong.spring.web.servlet.interceptor.browsinghistory;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.JsonUtil;
import com.feilong.spring.web.servlet.interceptor.browsinghistory.command.BrowsingHistoryCommand;
import com.feilong.spring.web.servlet.interceptor.browsinghistory.command.DefaultBrowsingHistoryCommand;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.5
 */
public class BrowsingHistoryResolverTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(BrowsingHistoryResolverTest.class);

    @Test
    public final void test(){
        List<BrowsingHistoryCommand> list = new LinkedList<>();

        BrowsingHistoryCommand browsingHistoryCommand = new DefaultBrowsingHistoryCommand();
        browsingHistoryCommand.setId(5L);
        list.add(browsingHistoryCommand);

        LOGGER.debug(JsonUtil.format(list));
        JsonUtil.toList(JsonUtil.format(list), DefaultBrowsingHistoryCommand.class);
    }

}
