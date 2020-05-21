package com.feilong.spring.web.servlet.interceptor.loginsessionbind;

import java.io.Serializable;

import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

/**
 * 保存用户请求客户端信息.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
public class ClientInfo implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 288232184048495608L;

    /** ua. */
    private String            userAgent;

    /** 客户端识别码. */
    private String            fingerPrint;

    //---------------------------------------------------------------

    /**
     * Instantiates a new client info.
     */
    public ClientInfo(){
        super();
    }

    /**
     * Instantiates a new client info.
     *
     * @param userAgent
     *            the user agent
     * @param fingerPrint
     *            the finger print
     */
    public ClientInfo(String userAgent, String fingerPrint){
        super();
        this.userAgent = userAgent;
        this.fingerPrint = fingerPrint;
    }

    //---------------------------------------------------------------

    /**
     * 获得 ua.
     *
     * @return the userAgent
     */
    public String getUserAgent(){
        return userAgent;
    }

    /**
     * 设置 ua.
     *
     * @param userAgent
     *            the userAgent to set
     */
    public void setUserAgent(String userAgent){
        this.userAgent = userAgent;
    }

    /**
     * 获得 客户端识别码.
     *
     * @return the fingerPrint
     */
    public String getFingerPrint(){
        return fingerPrint;
    }

    /**
     * 设置 客户端识别码.
     *
     * @param fingerPrint
     *            the fingerPrint to set
     */
    public void setFingerPrint(String fingerPrint){
        this.fingerPrint = fingerPrint;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
