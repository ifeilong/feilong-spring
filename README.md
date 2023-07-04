feilong-spring
================


[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-green.svg "JDK 1.8")

> Reduce development, Release ideas (减少开发,释放思想)

## 简介

`对spring相关类的快速封装,以便快速使用`

## 一图概述:

![one-feilong-spring](http://venusdrogon.github.io/feilong-platform/mysource/one-feilong-spring.png) 


## :dragon: Maven使用配置

feilong-spring 自从5.0.0开始,发布中央仓库 https://search.maven.org/artifact/com.github.ifeilong/feilong-spring

### `maven 配置` 


```XML
<dependency>
  <groupId>com.github.ifeilong</groupId>
  <artifactId>feilong-spring</artifactId>
  <version>5.1.0</version>
  
  <!-- 如果你的项目环境不是 spring 5.2系列,或者spring jar有冲突 ,可以排除spring 的间接依赖 -->
  <exclusions>
	<exclusion>
		<groupId>org.springframework</groupId>
		<artifactId>*</artifactId>
	</exclusion>
	<exclusion>
		<groupId>org.aspectj</groupId>
		<artifactId>aspectjweaver</artifactId>
	</exclusion>
  </exclusions>
</dependency>
```

### `Gradle 配置` 

```
com.github.ifeilong:feilong-spring:5.1.0
```

### `非Maven项目`

点击 https://repo1.maven.org/maven2/com/github/ifeilong/feilong-spring/ 链接，下载 feilong-spring.jar即可：

**注意:**
- feilong-spring 5 需要 JDK8+，对Android平台没有测试，不能保证所有工具类或工具方法可用。 
- 如果你的项目使用 JDK7，请使用 feilong-spring 4.2.0 版本


## 使用案例

### 启动显示 requestmapping

项目老项目,想看看里面有哪些接口, 可以通过以下方式来做

#### 第一步 配置 ContextRefreshedHandlerMethodInfoEventListener

XML格式 配置

```XML
<!-- 启动的时候,显示 路径 method等 信息 -->
<bean id="contextRefreshedHandlerMethodInfoEventListener" class="com.feilong.spring.web.event.ContextRefreshedHandlerMethodInfoEventListener">
    <property name="annotationAndAnnotationToStringBuilderMap">
        <map>
            <entry key="com.feilong.spring.web.servlet.interceptor.clientcache.ClientCache">
                <bean class="com.feilong.spring.web.servlet.interceptor.clientcache.ClientCacheToStringBuilder" />
            </entry>
        </map>
    </property>
</bean>
 ```
 
 spring-boot 场景配置
 
 ```JAVA
 /**
 * 启动的时候,显示 路径 method等 信息
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
@Configuration
public class ContextRefreshedHandlerMethodInfoEventListenerIniter{

    @Bean("contextRefreshedHandlerMethodInfoEventListener")
    public ContextRefreshedHandlerMethodInfoEventListener init(){
        ContextRefreshedHandlerMethodInfoEventListener bean = new ContextRefreshedHandlerMethodInfoEventListener();
        bean.setAnnotationAndAnnotationToStringBuilderMap(emptyMap());
        return bean;
    }
}
 
 ```
 
#### 第二步 启动输出即可

输出

```
17:02:34 INFO  (AbstractContextRefreshedHandlerMethodLogginEventListener.java:148) render() - handler method ,size:[74],info:
url                                                get post put head patch delete options trace isAjax header Controller                           Method                       
-------------------------------------------------- --- ---- --- ---- ----- ------ ------- ----- ------ ------ ------------------------------------ ---------------------------- 
/b/allLibrary                                      √                                                          LibraryController                    getAllLibrary                
/b/deleteIbeaconById                                   √                                                      LibraryController                    deleteIbeaconById            
/b/handleAccessLimit/{libId}                       √                                                          LibraryController                    handleAccessLimit            
/b/insertIbeacon                                       √                                                      LibraryController                    insertIbeacon                
/b/libId/{libId}/QRCodes                           √                                                          LibQrCodeController                  getQRCodes                   
/b/libId/{libId}/bind                                       √                                                 OrderController                      bindOrder                    
/b/libId/{libId}/orders                            √                                                          OrderController                      getLibraryOrders             
/b/libId/{libId}/propCode:bind                              √                                                 LibQrCodeController                  bindPropCode                 
/b/libId/{libId}/propCode:unbind                            √                                                 LibQrCodeController                  unbindPropCode               
/b/libId/{libId}/propCodes                         √                                                          LibQrCodeController                  getPropCodes                 
/b/libId/{libId}/propCodes/info                    √                                                          LibQrCodeController                  getPropCodesInfo             
/b/libId/{libId}/qrCodeId/{qrCodeId}/state/{state}          √                                                 LibQrCodeController                  updateQRCodeState            
/b/libId/{libId}/unbind                                     √                                                 OrderController                      unbindOrder                  
/b/menu                                            √                                                          MenuController      
 ```
 
#### 我想要生成cvs文件,怎么做?

你可以设置 writeCvs 属性,来下载成cvs文件 

writeCvs 属性
 
 
 ```XML
<bean id="contextRefreshedHandlerMethodInfoEventListener" class="com.feilong.spring.web.event.ContextRefreshedHandlerMethodInfoEventListener">
    <property name="annotationAndAnnotationToStringBuilderMap">
        <map>
            <entry key="com.feilong.spring.web.servlet.interceptor.clientcache.ClientCache">
                <bean class="com.feilong.spring.web.servlet.interceptor.clientcache.ClientCacheToStringBuilder" />
            </entry>
        </map>
    </property>
    <property name="writeCvs" value="true"/>
</bean>
```

#### 我想要修改输出cvs文件路径,怎么做?

`writeCvsFilePath` 属性

默认文件输出地址  `{USER_HOME}/feilong/RequestMappingInfo/RequestMappingInfo-{time}.csv` , 如 `/Users/feilong/feilong/RequestMappingInfo/RequestMappingInfo-20220919184547.csv`

你可以通过设置 `writeCvsFilePath` 属性来改变这个输出地址


## :memo: 说明

1. 基于 [Apache2](https://www.apache.org/licenses/LICENSE-2.0) 协议,您可以下载代码用于闭源项目,但每个修改的过的文件必须放置版权说明;
1. 5.0.0及以上版本需要 `jdk1.8`及以上环境
1. 1.5.0及以上版本需要 `jdk1.7`及以上环境
1. 1.5.0以下版本需要 `jdk1.6`及以上环境

## :cyclone: feilong 即时交流

|QQ 群 `243306798`
|:---------
|![](http://i.imgur.com/cIfglCa.png)

## :panda_face: About

如果您对本项目有任何建议和批评,可以使用下面的联系方式：

* iteye博客:http://feitianbenyue.iteye.com/
