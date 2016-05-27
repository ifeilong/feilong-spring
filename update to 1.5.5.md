feilong-spring-web browsingHistoryResolver的配置 从1.5.4升级到 1.5.5 不兼容

改变如下:

原来的配置

```XML

	<bean id="browsingHistoryResolver" class="com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryCookieResolver">
		<!--
			<property name="cookieName"></property>
			<property name="cookieMaxAge"></property>
			<property name="maxCount"></property>
		-->
		<property name="symmetricEncryption">
			<bean class="com.feilong.tools.security.symmetric.SymmetricEncryption" lazy-init="true">
				<!-- 第1个参数是加密解密方式 可选值 参见 com.feilong.tools.security.symmetric.SymmetricType -->
				<constructor-arg index="0" value="AES" />
				<!-- 第2个参数是密钥字符串 一旦设定了就不要随便改了 -->
				<constructor-arg index="1" value="a1b2store88qwert" />
			</bean>
		</property>
	</bean>

	<!-- 自定义的拦截器 -->
	<mvc:interceptors>
		<mvc:interceptor>
			<mvc:mapping path="/item/*" />
			<mvc:mapping path="/product/*/detail.htm" />

			<bean class="com.baozun.store.web.interceptor.StoreItemBrowsingHistoryInterceptor">
				<property name="browsingHistoryResolver" ref="browsingHistoryResolver" />
			</bean>
		</mvc:interceptor>
	</mvc:interceptors>

```



现在的配置



```XML

	<bean id="browsingHistoryResolver" class="com.feilong.spring.web.servlet.interceptor.browsinghistory.BrowsingHistoryCookieResolver">
		<constructor-arg index="0" value="com.feilong.spring.web.servlet.interceptor.browsinghistory.command.DefaultBrowsingHistoryCommand"></constructor-arg>

		<property name="cookieAccessor">
			<bean class="com.feilong.framework.accessor.cookie.CookieAccessor">
				<property name="cookieEntity">
					<bean class="com.feilong.servlet.http.entity.CookieEntity">
						<property name="name" value="f_b_h_n" /><!-- feilong_browsing_History_new -->
						<property name="maxAge" value="#{T(com.feilong.core.TimeInterval).SECONDS_PER_MONTH*3}"></property>
						<property name="httpOnly" value="true"></property>
					</bean>
				</property>
			</bean>
		</property>

		<property name="maxCount" value="10" />

		<property name="symmetricEncryptionCharsetName" value="UTF-8" />
		<property name="symmetricEncryption">
			<bean class="com.feilong.tools.security.symmetric.SymmetricEncryption" lazy-init="true">
				<!-- 第1个参数是加密解密方式 可选值 参见 com.feilong.tools.security.symmetric.SymmetricType -->
				<constructor-arg index="0" value="AES" />
				<!-- 第2个参数是密钥字符串 一旦设定了就不要随便改了 -->
				<constructor-arg index="1" value="a1b2store88qwert" />
			</bean>
		</property>
	</bean>

	<!-- 自定义的拦截器 -->
	<mvc:interceptors>
		<mvc:interceptor>
			<mvc:mapping path="/item/*" />
			<mvc:mapping path="/product/*/detail.htm" />

			<bean class="com.baozun.store.web.interceptor.StoreItemBrowsingHistoryInterceptor">
				<property name="browsingHistoryResolver" ref="browsingHistoryResolver" />
			</bean>
		</mvc:interceptor>
	</mvc:interceptors>


```


优点:

1.  可以在外部配置 cookie所有的信息
