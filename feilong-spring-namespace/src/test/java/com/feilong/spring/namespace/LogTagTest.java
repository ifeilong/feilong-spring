package com.feilong.spring.namespace;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.StringUtils;

@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
public class LogTagTest extends AbstractJUnit4SpringContextTests{

    @Test
    public void test(){
        String string = "myLog";
        // string = "log";

        LogBean logBean = (LogBean) this.applicationContext.getBean(string);
        assertTrue(StringUtils.hasText(logBean.getCompany()));
        assertTrue(logBean.isPrintTiem());
        logBean.print("is fun!");
    }
}