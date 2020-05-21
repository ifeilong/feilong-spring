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
package com.feilong.spring.web.event;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;
import static java.util.Collections.emptyMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.feilong.csv.CsvWrite;
import com.feilong.csv.DefaultCsvWrite;
import com.feilong.spring.event.AbstractContextRefreshedEventListener;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * {@link ApplicationContext} 初始化或刷新完成后触发的事件,用来分析 {@link HandlerMethod} 信息的父类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.springframework.context.event.SmartApplicationListener
 * @since 1.10.5
 */
public abstract class AbstractContextRefreshedHandlerMethodLogginEventListener extends AbstractContextRefreshedEventListener{

    /** The Constant LOGGER. */
    private static final Logger LOGGER                      = LoggerFactory
                    .getLogger(AbstractContextRefreshedHandlerMethodLogginEventListener.class);

    //---------------------------------------------------------------

    /**
     * The default write cvs file path.
     * 
     * @see <a href="https://github.com/venusdrogon/feilong-spring/issues/173">新增生成 CVS 文件支持</a>
     * @since 4.0.6
     */
    private final String        DEFAULT_WRITE_CVS_FILE_PATH = Slf4jUtil
                    .format("{}/{}.csv", SystemUtils.getUserDir().getAbsolutePath(), getClass().getName());

    //---------------------------------------------------------------
    /**
     * 是否输出 cvs.
     *
     * @see <a href="https://github.com/venusdrogon/feilong-spring/issues/173">新增生成 CVS 文件支持</a>
     * @since 4.0.6
     */
    private boolean             writeCvs                    = true;

    /**
     * CVS 文件路径.
     * 
     * <p>
     * 如果 writeCvs 是 true,但是writeCvsFilePath没有设置,那么将使用 默认的
     * </p>
     * 
     * @see <a href="https://github.com/venusdrogon/feilong-spring/issues/173">新增生成 CVS 文件支持</a>
     * @since 4.0.6
     */
    private String              writeCvsFilePath            = DEFAULT_WRITE_CVS_FILE_PATH;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void doOnApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        if (!LOGGER.isInfoEnabled()){
            return;
        }

        //---------------------------------------------------------------
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();

        //RequestMappingInfo 和  HandlerMethod Map
        Map<RequestMappingInfo, HandlerMethod> requestMappingInfoAndHandlerMethodMap = buildHandlerMethods(applicationContext);

        if (isNullOrEmpty(requestMappingInfoAndHandlerMethodMap)){
            LOGGER.info("requestMappingInfo And HandlerMethod Map is null or empty!!");
            return;
        }
        //---------------------------------------------------------------
        doLogging(requestMappingInfoAndHandlerMethodMap);
    }

    //---------------------------------------------------------------

    /**
     * Do logging.
     *
     * @param requestMappingInfoAndHandlerMethodMap
     *            the request mapping info and handler method map
     * @since 1.10.5
     */
    protected void doLogging(Map<RequestMappingInfo, HandlerMethod> requestMappingInfoAndHandlerMethodMap){
        List<Map<String, Object>> list = buildList(requestMappingInfoAndHandlerMethodMap);
        if (isNullOrEmpty(list)){
            LOGGER.info("list is null or empty");
            return;
        }
        render(list);
    }

    //---------------------------------------------------------------

    /**
     * Render.
     *
     * @param list
     *            the list
     */
    @SuppressWarnings("static-method")
    protected void render(List<Map<String, Object>> list){
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("handler method ,size:[{}],info:{}", list.size(), formatToSimpleTable(list));
        }

        //---------------------------------------------------------------

        //since 4.0.6
        if (writeCvs){
            write(defaultIfNullOrEmpty(writeCvsFilePath, DEFAULT_WRITE_CVS_FILE_PATH), list);
        }
    }

    //---------------------------------------------------------------

    /**
     * Write.
     *
     * @param fileName
     *            the file name
     * @param list
     *            the list
     * @since 4.0.6
     */
    private static void write(String fileName,List<Map<String, Object>> list){
        CsvWrite csvWrite = new DefaultCsvWrite();
        String[] columnTitles = toArray(list.get(0).keySet(), String.class);

        List<Object[]> dataList = toArrayList(list);
        csvWrite.write(fileName, columnTitles, dataList);
    }

    //---------------------------------------------------------------

    /**
     * To list 1.
     *
     * @param list
     *            the list
     * @return the list
     * @since 4.0.6
     */
    private static List<Object[]> toArrayList(List<Map<String, Object>> list){
        List<Object[]> list2 = newArrayList();
        for (Map<String, Object> map : list){
            list2.add(toArray(map.values(), Object.class));
        }
        return list2;
    }

    /**
     * 构造数据.
     *
     * @param handlerMethods
     *            the handler methods
     * @return the list
     */
    @SuppressWarnings("static-method")
    protected List<Map<String, Object>> buildList(@SuppressWarnings("unused") Map<RequestMappingInfo, HandlerMethod> handlerMethods){
        return null;
    }

    //---------------------------------------------------------------

    /**
     * Builds the handler methods.
     *
     * @param applicationContext
     *            the application context
     * @return 如果取不到 <code>RequestMappingHandlerMapping</code>,返回 {@link Collections#emptyMap()}<br>
     * @throws BeansException
     *             the beans exception
     */
    private static Map<RequestMappingInfo, HandlerMethod> buildHandlerMethods(ApplicationContext applicationContext){
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);//通过上下文对象获取RequestMappingHandlerMapping实例对象  

        if (null == requestMappingHandlerMapping){
            return emptyMap();
        }

        return requestMappingHandlerMapping.getHandlerMethods();
    }

    //---------------------------------------------------------------

    /**
     * 设置 是否输出 cvs.
     *
     * @param writeCvs
     *            the writeCvs to set
     * @since 4.0.6
     */
    public void setWriteCvs(boolean writeCvs){
        this.writeCvs = writeCvs;
    }

    /**
     * 设置 cVS 文件路径.
     *
     * @param writeCvsFilePath
     *            the writeCvsFilePath to set
     */
    public void setWriteCvsFilePath(String writeCvsFilePath){
        this.writeCvsFilePath = writeCvsFilePath;
    }

}
