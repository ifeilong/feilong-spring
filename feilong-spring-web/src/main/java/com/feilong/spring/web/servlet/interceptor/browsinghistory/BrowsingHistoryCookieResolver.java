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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.selectRejected;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.accessor.cookie.CookieAccessor;
import com.feilong.core.CharsetType;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.security.symmetric.SymmetricEncryption;
import com.feilong.security.symmetric.SymmetricType;
import com.feilong.spring.web.servlet.interceptor.browsinghistory.command.BrowsingHistoryCommand;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * The Class DefaultBrowsingHistory.
 * 
 * <h3>可配置参数:</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * <th align="left">必须</th>
 * <th align="left">默认</th>
 * </tr>
 * <tr valign="top">
 * <td>symmetricEncryption</td>
 * <td>加密算法</td>
 * <td>
 * <span style="color:red">true</span>,推荐使用 {@link SymmetricType#AES}</td>
 * <td>无</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>symmetricEncryptionCharsetName</td>
 * <td>加密时候的编码</td>
 * <td>true</td>
 * <td>{@link CharsetType#UTF8}</td>
 * </tr>
 * <tr valign="top">
 * <td>cookieAccessor</td>
 * <td>cookie寄存器</td>
 * <td>true</td>
 * <td>无</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>maxCount</td>
 * <td>最大记录数量,超过的记录将被去掉</td>
 * <td>true</td>
 * <td>10</td>
 * </tr>
 * </table>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.2.2
 */
public class BrowsingHistoryCookieResolver extends AbstractBrowsingHistoryResolver{

    /** The Constant log. */
    private static final Logger                           LOGGER                         = LoggerFactory
                    .getLogger(BrowsingHistoryCookieResolver.class);

    /** cookie寄存器. */
    private CookieAccessor                                cookieAccessor;

    /** 加密算法. */
    private SymmetricEncryption                           symmetricEncryption;

    /** 对称加密的编码. */
    private String                                        symmetricEncryptionCharsetName = UTF8;

    //---------------------------------------------------------------
    /** The bean class. */
    private final Class<? extends BrowsingHistoryCommand> beanClass;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     *
     * @param beanClass
     *            the bean class
     */
    public BrowsingHistoryCookieResolver(Class<? extends BrowsingHistoryCommand> beanClass){
        super();
        this.beanClass = beanClass;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryResolver#add(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, com.feilong.spring.web.servlet.interceptor.browsinghistory.command.BrowsingHistoryCommand)
     */
    @Override
    public void add(BrowsingHistoryCommand browsingHistoryCommand,HttpServletRequest request,HttpServletResponse response){
        Validate.notNull(browsingHistoryCommand, "browsingHistoryCommand can't be null!");

        try{
            List<BrowsingHistoryCommand> list = getBrowsingHistory(request, response);
            List<BrowsingHistoryCommand> browsingHistoryList = buildBrowsingHistoryList(list, browsingHistoryCommand);

            if (isNullOrEmpty(browsingHistoryList)){
                return;
            }
            saveToCookie(browsingHistoryList, response);
        }catch (Exception e){
            LOGGER.error("constructItemBrowsingHistory Exception,but we need code go-on!", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryResolver#getBrowsingHistory(javax.servlet.http.
     * HttpServletRequest)
     */
    @Override
    public List<BrowsingHistoryCommand> getBrowsingHistory(HttpServletRequest request,HttpServletResponse response){
        String value = cookieAccessor.get(request);
        return parseValue(request, response, value);
    }

    /**
     * Resolver value.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param value
     *            the value
     * @return 如果 value是 null 或者 Empty返回 {@link Collections#emptyList()}<br>
     *         在解析的过程中出现任何的异常,也会 返回 {@link Collections#emptyList()}
     * @since 1.5.5
     */
    private List<BrowsingHistoryCommand> parseValue(HttpServletRequest request,HttpServletResponse response,String value){
        if (isNullOrEmpty(value)){
            return emptyList();
        }

        try{
            //解密之后的明码
            String cookiePlainValue = symmetricEncryption.decryptHex(value, symmetricEncryptionCharsetName);
            LOGGER.debug("value:[{}],cookiePlainValue:[{}]", value, cookiePlainValue);

            return new ArrayList<>(JsonUtil.toList(cookiePlainValue, beanClass));//TODO
        }catch (Exception e){
            String pattern = "getBrowsingHistory cookie value:[{}] error,charset:[{}],will clear";
            LOGGER.error(Slf4jUtil.format(pattern, value, symmetricEncryptionCharsetName), e);
            clear(request, response); //如果出错了,那么就将cookie删掉
        }
        return emptyList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryResolver#remove(java.io.Serializable,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void remove(Long id,HttpServletRequest request,HttpServletResponse response){
        //① 获得所有的历史浏览记录
        List<BrowsingHistoryCommand> browsingHistoryList = getBrowsingHistory(request, response);

        //② 删除指定的 一条历史浏览记录
        List<BrowsingHistoryCommand> buildBrowsingHistoryList = selectRejected(browsingHistoryList, "id", id);

        //③ 设置整理后的记录到 cookie中
        saveToCookie(buildBrowsingHistoryList, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryResolver#clear(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void clear(HttpServletRequest request,HttpServletResponse response){
        cookieAccessor.remove(response);
    }

    /**
     * Save to cookie.
     *
     * @param browsingHistoryList
     *            如果是 null或者 empty 那么什么都不做
     * @param response
     *            the response
     * @since 1.5.5
     */
    private void saveToCookie(List<BrowsingHistoryCommand> browsingHistoryList,HttpServletResponse response){
        if (isNullOrEmpty(browsingHistoryList)){
            LOGGER.debug("browsingHistoryList isNullOrEmpty,nothing to do~~");
            return;
        }

        String original = JsonUtil.format(browsingHistoryList, 0, 0);
        String cookieValue = symmetricEncryption.encryptHex(original, symmetricEncryptionCharsetName);

        LOGGER.debug("will add to cookie,original:[{}],encryptHex:[{}]", original, cookieValue);
        cookieAccessor.save(cookieValue, response);
    }

    //---------------------------------------------------------------

    /**
     * 设置 cookie寄存器.
     *
     * @param cookieAccessor
     *            the cookieAccessor to set
     */
    public void setCookieAccessor(CookieAccessor cookieAccessor){
        this.cookieAccessor = cookieAccessor;
    }

    /**
     * 设置 加密算法.
     *
     * @param symmetricEncryption
     *            the symmetricEncryption to set
     */
    public void setSymmetricEncryption(SymmetricEncryption symmetricEncryption){
        this.symmetricEncryption = symmetricEncryption;
    }

    /**
     * 设置 对称加密的编码.
     *
     * @param symmetricEncryptionCharsetName
     *            the symmetricEncryptionCharsetName to set
     */
    public void setSymmetricEncryptionCharsetName(String symmetricEncryptionCharsetName){
        this.symmetricEncryptionCharsetName = symmetricEncryptionCharsetName;
    }
}
