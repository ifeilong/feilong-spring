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
package com.feilong.spring.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.feilong.core.TimeInterval;
import com.feilong.coreextension.lang.ThreadUtil;
import com.feilong.spring.manager.java.MemberManager;
import com.feilong.spring.manager.java.SalesOrderManager;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * The Class MemberManagerTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.1.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring.xml" })
public class MultiThreadMemberManagerTest //extends AbstractJUnit4SpringContextTests
{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadMemberManagerTest.class);

    /** The member manager. */
    @Autowired
    private MemberManager       memberManager;

    /** The sales order manager. */
    @Autowired
    private SalesOrderManager   salesOrderManager;

    /**
     * Test add user2.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testAddUser2() throws InterruptedException{
        for (int i = 0; i < 1; ++i){
            Thread thread = new Thread(new TestRunnable());
            thread.start();
        }
        Thread.sleep((long) (TimeInterval.SECONDS_PER_MINUTE * 1000 * 0.2));
    }

    /**
     * The Class TestRunnable.
     */
    class TestRunnable implements Runnable{

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run(){
            LOGGER.info("run thread,thread info:{}", JsonUtil.format(ThreadUtil.getCurrentThreadMapForLog()));
            // memberManager.addUser("feilong");
            salesOrderManager.addUser("feilong");
            // memberManager.getUser("feilong");
        }
    }
}
