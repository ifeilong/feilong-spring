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
package org.quartz;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.springframework.scheduling.support.CronSequenceGenerator;

import com.feilong.core.DatePattern;
import com.feilong.core.date.DateUtil;
import com.feilong.test.AbstractTest;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.4
 */
public class CronSequenceGeneratorTest extends AbstractTest{

    @Test
    public void test() throws ParseException{

        //      CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator("*/10 * * * * *");
        //https://en.wikipedia.org/wiki/Cron#Special_characters
        //---------------------------------------------------------------

        LOGGER.debug("{}", CronSequenceGenerator.isValidExpression("*/10 * * * * *"));
        LOGGER.debug("{}", CronSequenceGenerator.isValidExpression("0 0 10 L-6 * ?"));
        LOGGER.debug("{}", CronSequenceGenerator.isValidExpression("0 10 10 L * ?"));
        LOGGER.debug("{}", CronSequenceGenerator.isValidExpression("0 15 10 ? * 6#3"));
        LOGGER.debug("{}", CronSequenceGenerator.isValidExpression("0 0 10 L-7 * ?"));

    }

    /**
     * TestCronSequenceGeneratorTest.
     */
    @Test
    public void testCronSequenceGeneratorTest() throws ParseException{
        CronExpression cronExpression = new CronExpression("0 0 10 L-7 * ?");
        Date nextInvalidTimeAfter = cronExpression.getNextValidTimeAfter(new Date());

        LOGGER.debug("{}", DateUtil.toString(nextInvalidTimeAfter, DatePattern.CHINESE_DATE_AND_TIME));
        LOGGER.debug("{}", CronExpression.isValidExpression("0 0 10 L-7 * ?"));
    }
}
