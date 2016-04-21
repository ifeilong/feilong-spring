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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validator;
import com.feilong.spring.web.servlet.interceptor.browsinghistory.command.BrowsingHistoryCommand;

/**
 * The Class AbstractBrowsingHistoryResolver.
 *
 * @author feilong
 * @version 1.5.4 2016年4月21日 下午3:26:43
 * @since 1.5.4
 */
public abstract class AbstractBrowsingHistoryResolver implements BrowsingHistoryResolver{

    /** The Constant log. */
    private static final Logger LOGGER   = LoggerFactory.getLogger(AbstractBrowsingHistoryResolver.class);

    /** 最大记录数量,超过的记录将被去掉. */
    protected Integer           maxCount = 5;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryResolver#getBrowsingHistoryExcludeId(javax.servlet.http.
     * HttpServletRequest, java.io.Serializable, java.lang.Class)
     */
    @Override
    public <T extends Serializable> LinkedList<T> getBrowsingHistoryExcludeId(
                    HttpServletRequest request,
                    Serializable excludeId,
                    Class<T> klass){
        LinkedList<T> browsingHistoryItemIds = getBrowsingHistory(request, klass);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("browsingHistoryItemIds:[{}],excludeId:[{}]", browsingHistoryItemIds, excludeId);
        }

        if (Validator.isNullOrEmpty(browsingHistoryItemIds)){
            return null;
        }
        //去掉当前的
        browsingHistoryItemIds.remove(excludeId);
        return browsingHistoryItemIds;
    }

    /**
     * 构造历史浏览记录.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param browsingHistoryCommand
     *            the browsing history command
     * @return the linked list< string>
     */
    protected LinkedList<String> constructBrowsingHistoryList(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    BrowsingHistoryCommand browsingHistoryCommand){

        LinkedList<String> linkedList = null;
        try{
            linkedList = getBrowsingHistory(request, String.class);
        }catch (Exception e){
            //如果出错了,那么就将cookie删掉
            clear(request, response);
        }

        //*****************************************************************************

        Serializable id = browsingHistoryCommand.getId();
        //如果cookie没有,表示第一次访问PDP页面 ,这时逻辑是构建一个往cookie 里加入
        String idStr = id.toString();
        if (Validator.isNullOrEmpty(linkedList)){
            linkedList = new LinkedList<String>();
            //如果没有 添加一个
            linkedList.add(idStr);

            return linkedList;
        }

        //*****************************************************************************
        @SuppressWarnings("null")
        String first = linkedList.getFirst();
        //如果 list 里面的数据 第一个是当前item 那么一般表示刷新页面 或者重新打开新窗口
        //这种case 没有必要操作 cookie
        if (first.equals(idStr)){
            LOGGER.info("in cookie,first pk is:[{}],current pk:[{}], nothing to do", first, id);
            return null;
        }

        //*************************************************************************************************
        //如果有当前商品,那么删除掉并将当前的item id塞第一个
        if (linkedList.contains(idStr)){
            LOGGER.info("in cookie,linkedList:[{}],contains:[{}],remove it~", linkedList, idStr);
            linkedList.remove(idStr);
        }
        linkedList.addFirst(idStr);

        //*************************************************************************************************
        //如果超长了 ,截取
        int size = linkedList.size();
        if (size > maxCount){
            LOGGER.debug("linkedList size:[{}] > maxCount[{}],linkedList:[{}],will sub subList", size, maxCount, linkedList);

            // so non-structural changes in the returned list
            List<String> subList = linkedList.subList(0, maxCount);
            //linkedList = (LinkedList<Serializable>) subList;  //java.util.SubList cannot be cast to java.util.LinkedList
            linkedList = new LinkedList<String>(subList);
        }
        return linkedList;
    }

    /**
     * 设置 最大记录数量,超过的记录将被去掉.
     *
     * @param maxCount
     *            the maxCount to set
     */
    public void setMaxCount(Integer maxCount){
        this.maxCount = maxCount;
    }
}
