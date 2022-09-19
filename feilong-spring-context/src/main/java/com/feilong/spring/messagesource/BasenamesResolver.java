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
package com.feilong.spring.messagesource;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.CollectionsUtil.removeDuplicate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.feilong.json.JsonUtil;

/**
 * The Class BasenamesResolver.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.3
 */
class BasenamesResolver{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BasenamesResolver.class);

    //---------------------------------------------------------------
    /** Don't let anyone instantiate this class. */
    private BasenamesResolver(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Resolver.
     *
     * @param resourcePatternResolver
     *            the resource pattern resolver
     * @param basenames
     *            the basenames
     * @return the string[]
     */
    static String[] resolver(ResourcePatternResolver resourcePatternResolver,String...basenames){
        List<String> basenameList = resolverList(resourcePatternResolver, basenames);

        //去重
        List<String> removeDuplicateBasenameList = removeDuplicate(basenameList);
        String[] finalBaseNames = toArray(removeDuplicateBasenameList, String.class);

        //---------------------------------------------------------------

        if (LOGGER.isInfoEnabled()){
            LOGGER.info("resolver finalBaseNames:{}", JsonUtil.toString(finalBaseNames));
        }

        return finalBaseNames;
    }

    /**
     * Resolver basename list.
     *
     * @param resourcePatternResolver
     *            the resource pattern resolver
     * @param basenames
     *            the basenames
     * @return the list
     * @since 1.8.2
     * @since 1.11.3
     */
    private static List<String> resolverList(ResourcePatternResolver resourcePatternResolver,String...basenames){
        List<String> basenameList = newArrayList();
        for (String basename : basenames){
            if (isNullOrEmpty(basename)){
                LOGGER.warn("basename:[{}] is null or empty", basename);
                continue;
            }

            //---------------------------------------------------------------

            if (basename.contains("*")){//如果带有通配符
                basenameList.addAll(resolverWildcardConfig(resourcePatternResolver, basename));
            }else{
                basenameList.add(basename);
            }
        }
        return basenameList;
    }

    //---------------------------------------------------------------

    /**
     * Resolver wildcard config.
     *
     * @param resourcePatternResolver
     *            the resource pattern resolver
     * @param basename
     *            the basename
     * @return the list< string>
     * @since 1.5.0
     */
    private static List<String> resolverWildcardConfig(ResourcePatternResolver resourcePatternResolver,String basename){
        List<String> list = newArrayList();

        try{
            Resource[] resources = resourcePatternResolver.getResources(basename);
            for (Resource resource : resources){
                list.add(BasenameBuilder.build(resource));
            }
            return list;
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

}
