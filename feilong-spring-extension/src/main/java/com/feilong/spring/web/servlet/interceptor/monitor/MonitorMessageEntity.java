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
package com.feilong.spring.web.servlet.interceptor.monitor;

import static com.feilong.core.TimeInterval.MILLISECOND_PER_SECONDS;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.servlet.http.entity.RequestLogSwitch;

/**
 * MonitorMessage Entity.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class MonitorMessageEntity implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID                        = 375692195684960908L;

    //---------------------------------------------------------------

    /** 性能阀值<code>{@value}</code>,目前初步设置为1.5s,如果构建command 超过1.5s,会有 error log 记录. */
    public static final Integer PERFORMANCE_THRESHOLD                   = (int) (MILLISECOND_PER_SECONDS * 1.5);

    //---------------------------------------------------------------

    /** The request log switch. */
    private RequestLogSwitch    requestLogSwitch;

    /**
     * 是否显示 request attribute.
     */
    private boolean             isShowRequestAttribute                  = true;

    /**
     * 允许被json log输出的 request {@code &&} model 对象类型, 默认只有基本类型以及数组才会被输出.
     * 
     * @see JsonUtil#formatSimpleMap(Map, Class...)
     */
    private Class<?>[]          allowFormatClassTypes;

    /**
     * 性能阀值,单位毫秒,默认 {@link #PERFORMANCE_THRESHOLD}.
     */
    private Integer             performanceThreshold                    = PERFORMANCE_THRESHOLD;

    //---------------------------------------------------------------
    /**
     * 是否显示PostHandle 方法日志.
     */
    private boolean             isShowPostHandleLog                     = true;

    /**
     * 是否显示AfterCompletion 方法日志.
     * 
     */
    private boolean             isShowAfterCompletionLog                = true;

    /**
     * 是否显示AfterConcurrentHandlingStarted 方法日志.
     */
    private boolean             isShowAfterConcurrentHandlingStartedLog = true;

    //---------------------------------------------------------------

    /**
     * 敏感参数的名字.
     * 
     * <p>
     * 在这list中的参数 值将会以*** 的形式,输出在日志里面
     * </p>
     *
     * @since 1.12.7
     */
    private List<String>        sensitiveRequestParamNameList;

    //---------------------------------------------------------------

    /**
     * Instantiates a new monitor message entity.
     */
    public MonitorMessageEntity(){
        super();
    }

    /**
     * Instantiates a new monitor message entity.
     *
     * @param requestLogSwitch
     *            the request log switch
     * @param isShowRequestAttribute
     *            the is show request attribute
     */
    public MonitorMessageEntity(RequestLogSwitch requestLogSwitch, boolean isShowRequestAttribute){
        super();
        this.requestLogSwitch = requestLogSwitch;
        this.isShowRequestAttribute = isShowRequestAttribute;
    }

    //---------------------------------------------------------------

    /**
     * 获得 request log switch.
     *
     * @return the requestLogSwitch
     */
    public RequestLogSwitch getRequestLogSwitch(){
        return requestLogSwitch;
    }

    /**
     * 设置 request log switch.
     *
     * @param requestLogSwitch
     *            the requestLogSwitch to set
     */
    public void setRequestLogSwitch(RequestLogSwitch requestLogSwitch){
        this.requestLogSwitch = requestLogSwitch;
    }

    /**
     * 获得 是否显示 request attribute.
     *
     * @return the isShowRequestAttribute
     */
    public boolean getIsShowRequestAttribute(){
        return isShowRequestAttribute;
    }

    /**
     * 设置 是否显示 request attribute.
     *
     * @param isShowRequestAttribute
     *            the isShowRequestAttribute to set
     */
    public void setIsShowRequestAttribute(boolean isShowRequestAttribute){
        this.isShowRequestAttribute = isShowRequestAttribute;
    }

    /**
     * 获得 允许被json log输出的 request {@code &&} model 对象类型, 默认只有基本类型以及数组才会被输出.
     *
     * @return the allowFormatClassTypes
     */
    public Class<?>[] getAllowFormatClassTypes(){
        return allowFormatClassTypes;
    }

    /**
     * 设置 允许被json log输出的 request {@code &&} model 对象类型, 默认只有基本类型以及数组才会被输出.
     *
     * @param allowFormatClassTypes
     *            the allowFormatClassTypes to set
     */
    public void setAllowFormatClassTypes(Class<?>[] allowFormatClassTypes){
        this.allowFormatClassTypes = allowFormatClassTypes;
    }

    /**
     * 获得 性能阀值,单位毫秒,默认 {@link #PERFORMANCE_THRESHOLD}.
     *
     * @return the performanceThreshold
     */
    public Integer getPerformanceThreshold(){
        return performanceThreshold;
    }

    /**
     * 设置 性能阀值,单位毫秒,默认 {@link #PERFORMANCE_THRESHOLD}.
     *
     * @param performanceThreshold
     *            the performanceThreshold to set
     */
    public void setPerformanceThreshold(Integer performanceThreshold){
        this.performanceThreshold = performanceThreshold;
    }

    /**
     * 获得 是否显示AfterCompletion 方法日志.
     *
     * @return the isShowAfterCompletionLog
     */
    public boolean getIsShowAfterCompletionLog(){
        return isShowAfterCompletionLog;
    }

    /**
     * 设置 是否显示AfterCompletion 方法日志.
     *
     * @param isShowAfterCompletionLog
     *            the isShowAfterCompletionLog to set
     */
    public void setIsShowAfterCompletionLog(boolean isShowAfterCompletionLog){
        this.isShowAfterCompletionLog = isShowAfterCompletionLog;
    }

    /**
     * 获得 是否显示AfterConcurrentHandlingStarted 方法日志.
     *
     * @return the isShowAfterConcurrentHandlingStartedLog
     */
    public boolean getIsShowAfterConcurrentHandlingStartedLog(){
        return isShowAfterConcurrentHandlingStartedLog;
    }

    /**
     * 设置 是否显示AfterConcurrentHandlingStarted 方法日志.
     *
     * @param isShowAfterConcurrentHandlingStartedLog
     *            the isShowAfterConcurrentHandlingStartedLog to set
     */
    public void setIsShowAfterConcurrentHandlingStartedLog(boolean isShowAfterConcurrentHandlingStartedLog){
        this.isShowAfterConcurrentHandlingStartedLog = isShowAfterConcurrentHandlingStartedLog;
    }

    /**
     * 获得 是否显示PostHandle 方法日志.
     *
     * @return the isShowPostHandleLog
     */
    public boolean getIsShowPostHandleLog(){
        return isShowPostHandleLog;
    }

    /**
     * 设置 是否显示PostHandle 方法日志.
     *
     * @param isShowPostHandleLog
     *            the isShowPostHandleLog to set
     */
    public void setIsShowPostHandleLog(boolean isShowPostHandleLog){
        this.isShowPostHandleLog = isShowPostHandleLog;
    }

    /**
     * 获得 敏感参数的名字.
     * 
     * <p>
     * 在这list中的参数 值将会以*** 的形式,输出在日志里面
     * </p>
     * 
     * @return the sensitiveRequestParamNameList
     * 
     * @since 1.12.7
     */
    public List<String> getSensitiveRequestParamNameList(){
        return sensitiveRequestParamNameList;
    }

    /**
     * 设置 敏感参数的名字.
     * 
     * <p>
     * 在这list中的参数 值将会以*** 的形式,输出在日志里面
     * </p>
     * 
     * @param sensitiveRequestParamNameList
     *            the sensitiveRequestParamNameList to set
     * 
     * @since 1.12.7
     */
    public void setSensitiveRequestParamNameList(List<String> sensitiveRequestParamNameList){
        this.sensitiveRequestParamNameList = sensitiveRequestParamNameList;
    }

}
