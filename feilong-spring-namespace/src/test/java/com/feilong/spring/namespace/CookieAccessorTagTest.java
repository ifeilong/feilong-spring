package com.feilong.spring.namespace;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.accessor.cookie.CookieAccessor;
import com.feilong.json.jsonlib.JsonUtil;

@ContextConfiguration(locations = { "classpath*:applicationContext-CookieAccessor.xml" })
public class CookieAccessorTagTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("cookieAccessor")
    private CookieAccessor cookieAccessor;

    //---------------------------------------------------------------

    @Test
    public void test(){
        System.out.println(JsonUtil.format(cookieAccessor));
    }
}