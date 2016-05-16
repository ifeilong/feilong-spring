feilong-platform feilong-core
================

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Reduce development, Release ideas

            .--.
           /    \
          ## a  a
          (   '._)
           |'-- |
         _.\___/_   ___feilong___
       ."\> \Y/|<'.  '._.-'
      /  \ \_\/ /  '-' /
      | --'\_/|/ |   _/
      |___.-' |  |`'`
        |     |  |
        |    / './
       /__./` | |
          \   | |
           \  | |
           ;  | |
           /  | |
     jgs  |___\_.\_
          `-"--'---'


#Welcome to feilong-platform feilong-core

`对spring相关类的快速封装,以便快速使用`

#说明

1. 基于`Apache2` 协议,您可以下载,代码用于闭源项目,但每个修改的过的文件必须放置版权说明;
1. 基于`maven3.3`构建;
1. 1.5.0及以上版本需要`jdk1.7`及以上环境(1.5.0以下版本需要`jdk1.6`及以上环境);


# Maven使用配置

```XML
	<project>
		....
		<repositories>
			<repository>
				<id>feilong-repository</id>
				<url>https://raw.github.com/venusdrogon/feilong-platform/repository</url>
			</repository>
		</repositories>
		
		....
		<dependencies>
			....
			<dependency>
				<groupId>com.feilong.platform.spring</groupId>
				<artifactId>feilong-spring-core</artifactId>
				<version>1.5.4</version>
			</dependency>
			<dependency>
				<groupId>com.feilong.platform.spring</groupId>
				<artifactId>feilong-spring-aop</artifactId>
				<version>1.5.4</version>
			</dependency>
			<dependency>
				<groupId>com.feilong.platform.spring</groupId>
				<artifactId>feilong-spring-context</artifactId>
				<version>1.5.4</version>
			</dependency>
			<dependency>
				<groupId>com.feilong.platform.spring</groupId>
				<artifactId>feilong-spring-jdbc</artifactId>
				<version>1.5.4</version>
			</dependency>
			<dependency>
				<groupId>com.feilong.platform.spring</groupId>
				<artifactId>feilong-spring-web</artifactId>
				<version>1.5.4</version>
			</dependency>
			....
		</dependencies>
		
		....
		
	</project>
```

# feilong-platform module:

Category |Name | Description | JDK编译版本
----|------------ | ---------|------------
commons |`feilong-spring-aop` | 常用aop的封装 | 1.7
commons |`feilong-spring-context` | 常用context的封装  | 1.7
commons |`feilong-spring-core` |  常用core的封装  | 1.7
commons |`feilong-spring-jdbc` |  常用jdbc的封装  | 1.7
commons |`feilong-spring-web` |  常用web的封装  | 1.7

# About

如果您对feilong platform 有任何建议，可以使用下面的联系方式：

* 新浪微博:http://weibo.com/venusdrogon
* iteye博客:http://feitianbenyue.iteye.com/