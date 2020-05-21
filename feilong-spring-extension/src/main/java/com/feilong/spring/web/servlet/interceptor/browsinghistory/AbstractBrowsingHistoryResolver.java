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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;
import static com.feilong.core.util.CollectionsUtil.selectRejected;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.util.CollectionsUtil;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.core.Validate;
import com.feilong.spring.web.servlet.interceptor.browsinghistory.command.BrowsingHistoryCommand;

/**
 * The Class AbstractBrowsingHistoryResolver.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 */
public abstract class AbstractBrowsingHistoryResolver implements BrowsingHistoryResolver{

    /** The Constant log. */
    private static final Logger LOGGER   = LoggerFactory.getLogger(AbstractBrowsingHistoryResolver.class);

    /** 最大记录数量,超过的记录将被去掉. */
    protected Integer           maxCount = 10;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryResolver#getBrowsingHistoryIdListExcludeId(javax.servlet.
     * http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Long)
     */
    @Override
    public List<Long> getBrowsingHistoryIdListExcludeId(Long excludeId,HttpServletRequest request,HttpServletResponse response){
        List<Long> browsingHistoryItemIds = getBrowsingHistoryIdList(request, response);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("browsingHistoryItemIds:[{}],excludeId:[{}]", JsonUtil.format(browsingHistoryItemIds), excludeId);
        }

        if (isNullOrEmpty(browsingHistoryItemIds)){
            return Collections.emptyList();
        }

        //---------------------------------------------------------------

        browsingHistoryItemIds.remove(excludeId);
        return browsingHistoryItemIds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryResolver#getBrowsingHistoryExcludeId(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Long)
     */
    @Override
    public List<BrowsingHistoryCommand> getBrowsingHistoryExcludeId(Long excludeId,HttpServletRequest request,HttpServletResponse response){
        List<BrowsingHistoryCommand> browsingHistory = getBrowsingHistory(request, response);
        return selectRejected(browsingHistory, "id", excludeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryResolver#getBrowsingHistoryIdList(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public List<Long> getBrowsingHistoryIdList(HttpServletRequest request,HttpServletResponse response){
        List<BrowsingHistoryCommand> browsingHistory = getBrowsingHistory(request, response);
        return getPropertyValueList(browsingHistory, "id");
    }

    //---------------------------------------------------------------

    /**
     * 构造需要被存储的历史记录.
     * 
     * <p>
     * 如果null或者 empty 表示 不需要保存
     * </p>
     *
     * @param list
     *            the list
     * @param browsingHistoryCommand
     *            the o
     * @return the list< browsing history command>
     * @since 1.5.5
     */
    protected List<BrowsingHistoryCommand> buildBrowsingHistoryList(
                    List<BrowsingHistoryCommand> list,
                    BrowsingHistoryCommand browsingHistoryCommand){
        //**********如果cookie没有,表示第一次访问PDP页面 ,这时逻辑是构建一个往cookie里加入********************
        if (isNullOrEmpty(list)){
            LOGGER.debug("list is null or empty,will construct a new list and put element into", list);
            return buildNewBrowsingHistoryList(browsingHistoryCommand);
        }

        //---------------------------------------------------------------
        Long id = browsingHistoryCommand.getId();
        Validate.notNull(id, "id can't be null!");

        int index = CollectionsUtil.indexOf(list, "id", id);
        if (StringUtils.INDEX_NOT_FOUND != index){//list中存在相同id的对象
            boolean isFirst = 0 == index;
            if (isFirst){//如果 list里面的数据第一个是当前item 那么一般表示刷新页面 或者重新打开新窗口
                LOGGER.debug("in cookie,first pk same as current pk:[{}],nothing to do", id);
                return Collections.emptyList();//这种case没有必要操作 cookie
            }

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug(
                                "in cookie,list:[{}],contains:[{}],remove it~~",
                                JsonUtil.format(list, 0, 0),
                                JsonUtil.format(browsingHistoryCommand, 0, 0));
            }
            list.remove(index);//如果有当前商品,那么删除掉
        }

        //****将当前的item id塞第一个*************************************
        list.add(0, browsingHistoryCommand);
        LOGGER.debug("insert browsingHistoryCommand to list first position");

        return judgeSize(list);
    }

    //---------------------------------------------------------------

    /**
     * New list.
     *
     * @param browsingHistoryCommand
     *            the browsing history command
     * @return the list< browsing history command>
     * @since 1.5.5
     */
    private static List<BrowsingHistoryCommand> buildNewBrowsingHistoryList(BrowsingHistoryCommand browsingHistoryCommand){
        return toList(browsingHistoryCommand);
    }

    /**
     * 判断长度,如果超长,那么截取返回.
     *
     * @param list
     *            the list
     * @return the list< o>
     * @since 1.5.5
     */
    private List<BrowsingHistoryCommand> judgeSize(List<BrowsingHistoryCommand> list){
        int size = list.size();
        if (size <= maxCount){
            LOGGER.debug("list size:{},<=maxCount:[{}],direct return list", size, maxCount);
            return list;
        }

        //如果超长了 ,截取
        LOGGER.debug("linkedList size:[{}] > maxCount[{}],list:[{}],will sub subList", size, maxCount, list);
        return new ArrayList<>(list.subList(0, maxCount)); //linkedList=(LinkedList<Serializable>) subList;//java.util.SubList cannot be cast to java.util.LinkedList
    }

    //---------------------------------------------------------------

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
