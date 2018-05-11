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

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;
import static org.springframework.util.ResourceUtils.JAR_URL_SEPARATOR;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import com.feilong.core.lang.StringUtil;

/**
 * The Class BasenameBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://github.com/venusdrogon/feilong-spring/issues/1">PathMatchingReloadableResourceBundleMessageSource 不支持
 *      help_message_en_GB.properties 格式文件</a>
 * @since 1.11.0
 */
class BasenameBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BasenameBuilder.class);

    //---------------------------------------------------------------

    /** <code>{@value}</code>. */
    public static final String  REGEX  = "(_\\w+){0,3}\\.(properties|xml)";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private BasenameBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Resolver base name.
     *
     * @param resource
     *            the resource
     * @return 如果 <code>resource</code> 是null,抛出 {@link NullPointerException}<br>
     * @throws IOException
     *             the IO exception
     * @see <a href="https://searchcode.com/codesearch/view/17495983/#">https://searchcode.com/codesearch/view/17495983/#</a>
     * @see <a href=
     *      "https://github.com/asual/summer/blob/8635d0799db7588c7ebf7e989a11e99294951b83/modules/core/src/main/java/com/asual/summer/core/resource/MessageResource.java">
     *      MessageResource</a>
     */
    static String build(Resource resource) throws IOException{
        Validate.notNull(resource, "resource can't be null!");

        //---------------------------------------------------------------

        URL url = resource.getURL();
        String fileName = url.getFile();

        String afterParseUrl = parseUrl(url, fileName);

        String baseName = parseBasename(afterParseUrl);
        LOGGER.debug("file:[{}],baseName is:[{}]", fileName, baseName);
        return baseName;
    }

    //---------------------------------------------------------------

    /**
     * Parses the url.
     *
     * @param url
     *            the url
     * @param fileName
     *            the file name
     * @return the string
     */
    private static String parseUrl(URL url,String fileName){
        boolean isJar = ResourceUtils.isJarURL(url);
        String replaceFirst = fileName.replaceFirst(isJar ? "^.*" + JAR_URL_SEPARATOR : "^(.*/test-classes|.*/classes|.*/resources)/", "");

        LOGGER.debug("file:[{}],replaceFirst:[{}]", fileName, replaceFirst);
        return replaceFirst;
    }

    //---------------------------------------------------------------

    /**
     * Parses the basename.
     *
     * @param replaceFirst
     *            the replace first
     * @return 如果 <code>replaceFirst</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>replaceFirst</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    private static String parseBasename(String replaceFirst){
        Validate.notBlank(replaceFirst, "replaceFirst can't be blank!");
        return CLASSPATH_URL_PREFIX + StringUtil.replaceAll(replaceFirst, REGEX, EMPTY);
    }

}
