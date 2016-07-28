feilong-platform feilong-spring
================

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![build](https://img.shields.io/jenkins/s/https/jenkins.qa.ubuntu.com/precise-desktop-amd64_default.svg "build")
![JDK](https://img.shields.io/badge/JDK-1.7-green.svg "JDK")

Reduce development, Release ideas

#Welcome to feilong-platform feilong-spring

`对spring相关类的快速封装,以便快速使用`

# :dragon: Maven使用配置

feilong spring jar 你可以在这里 https://github.com/venusdrogon/feilong-platform/tree/repository/com/feilong/platform/spring 浏览 

在maven `pom.xml` 中,您可以通过以下方式来配置:

```XML

	<project>
	
		....
		<properties>
			<version.feilong-platform>1.8.2</version.feilong-platform>
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
				<version>${version.feilong-platform}</version>
			</dependency>
			<dependency>
				<groupId>com.feilong.platform.spring</groupId>
				<artifactId>feilong-spring-aop</artifactId>
				<version>${version.feilong-platform}</version>
			</dependency>
			<dependency>
				<groupId>com.feilong.platform.spring</groupId>
				<artifactId>feilong-spring-context</artifactId>
				<version>${version.feilong-platform}</version>
			</dependency>
			<dependency>
				<groupId>com.feilong.platform.spring</groupId>
				<artifactId>feilong-spring-jdbc</artifactId>
				<version>${version.feilong-platform}</version>
			</dependency>
			<dependency>
				<groupId>com.feilong.platform.spring</groupId>
				<artifactId>feilong-spring-web</artifactId>
				<version>${version.feilong-platform}</version>
			</dependency>
			....
		</dependencies>
		
		....
		
	</project>
```

# :memo: 说明

1. 基于`Apache2` 协议,您可以下载,代码用于闭源项目,但每个修改的过的文件必须放置版权说明;
1. 基于`maven3.3`构建;
1. 1.5.0及以上版本需要`jdk1.7`及以上环境(1.5.0以下版本需要`jdk1.6`及以上环境);

# :panda_face: About

如果您对feilong core 有任何建议和批评,可以使用下面的联系方式：

* iteye博客:http://feitianbenyue.iteye.com/