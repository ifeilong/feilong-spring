feilong-spring
================


[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![JDK 1.7](https://img.shields.io/badge/JDK-1.7-green.svg "JDK 1.7")

> Reduce development, Release ideas (减少开发,释放思想)

## 简介

`对spring相关类的快速封装,以便快速使用`

## 一图概述:

![one-feilong-spring](http://venusdrogon.github.io/feilong-platform/mysource/one-feilong-spring.png) 

## :dragon: Maven使用配置

feilong spring jar你可以直接在 [仓库](https://github.com/venusdrogon/feilong-platform/tree/repository/com/feilong/platform/spring "仓库") 浏览 

如果你使用 `maven`, 您可以通过以下方式来配置 `pom.xml`:

```XML
<project>

	....
	<properties>
		<version.feilong-spring>4.0.6</version.feilong-spring>
		....
	</properties>
	
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
			<version>${version.feilong-spring}</version>
		</dependency>
		<dependency>
			<groupId>com.feilong.platform.spring</groupId>
			<artifactId>feilong-spring-aop</artifactId>
			<version>${version.feilong-spring}</version>
		</dependency>
		<dependency>
			<groupId>com.feilong.platform.spring</groupId>
			<artifactId>feilong-spring-context</artifactId>
			<version>${version.feilong-spring}</version>
		</dependency>
		<dependency>
			<groupId>com.feilong.platform.spring</groupId>
			<artifactId>feilong-spring-jdbc</artifactId>
			<version>${version.feilong-spring}</version>
		</dependency>
		<dependency>
			<groupId>com.feilong.platform.spring</groupId>
			<artifactId>feilong-spring-web</artifactId>
			<version>${version.feilong-spring}</version>
		</dependency>
		....
	</dependencies>
	
	....
	
</project>
```

如果你不需要这么细致的引用feilong-spring ,你可以使用 

```XML
<project>

	....
	<properties>
		<version.feilong-spring>4.0.6</version.feilong-spring>
		....
	</properties>
	
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
			<artifactId>feilong-spring-all</artifactId>
			<version>${version.feilong-spring}</version>
		</dependency>
		....
	</dependencies>
	....
	
</project>
```

## How to install?

有些小伙伴想下载并自行install 进行研究, 你需要执行以下4个步骤:

```bat
git clone https://github.com/venusdrogon/feilong-platform.git --depth 1
mvn install -f feilong-platform/pom.xml

git clone https://github.com/venusdrogon/feilong-spring.git --depth 1
mvn install -f feilong-spring/pom.xml
```

## :memo: 说明

1. 基于 [Apache2](https://www.apache.org/licenses/LICENSE-2.0) 协议,您可以下载代码用于闭源项目,但每个修改的过的文件必须放置版权说明;
1. 1.5.0及以上版本需要`jdk1.7`及以上环境(1.5.0以下版本需要`jdk1.6`及以上环境);

## :cyclone: feilong 即时交流

|QQ 群 `243306798`
|:---------
|![](http://i.imgur.com/cIfglCa.png)

## :panda_face: About

如果您对本项目有任何建议和批评,可以使用下面的联系方式：

* iteye博客:http://feitianbenyue.iteye.com/