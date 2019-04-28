package com.feilong.spring.namespace;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.accessor.session.SessionAccessor;

@ContextConfiguration(locations = { "classpath*:applicationContext-SessionAccessor.xml" })
public class SessionAccessorTagTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("memeberResetPasswordMobileSessionAccessor")
    private SessionAccessor sessionAccessor;

    @Autowired
    @Qualifier("aSessionAccessor")
    private SessionAccessor aSessionAccessor;

    //---------------------------------------------------------------

    @Test
    public void test(){
        System.out.println(sessionAccessor.getKey());
        System.out.println(aSessionAccessor.getKey());
    }
}