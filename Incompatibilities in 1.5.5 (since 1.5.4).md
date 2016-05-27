#从1.5.4升级到 1.5.5 不兼容

为了支持更灵活的配置以及更多的功能,feilong-spring-web jar的 browsingHistoryResolver做了相关调整和优化


解决方案


##StoreItemBrowsingHistoryInterceptor browsingHistoryResolver的配置 

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

## RecommendationEngineBuilder

原来 

```XML

	public interface RecommendationEngineBuilder extends BaseManager{
	
	    List<RecommendationEngineCommand> getRecommendationEngineCommandList(LinkedList<Long> browsingHistoryItemIds,Long currentId);
	}

```

现在 

```XML

	public interface RecommendationEngineBuilder extends BaseManager{
	
	    List<RecommendationEngineCommand> getRecommendationEngineCommandList(List<Long> browsingHistoryItemIds);
	}

```


## RecommendationEngineController

原来 


```JAVA

	@Controller
	public class RecommendationEngineController extends BaseController{
	
	 
	 
	    @ResponseBody
	    @ClientCache(value = TimeInterval.SECONDS_PER_MINUTE * 5)
	    @RequestMapping(value = { "/item/{itemId}/recommendation.json" },method = RequestMethod.POST,headers = HEADER_WITH_AJAX_SPRINGMVC)
	    public Map<RecommendationEngineType, List<RecommendationEngineCommand>> doHandler(
	                    HttpServletRequest request,
	                    @PathVariable("itemId") Long itemId){
	
	        LinkedList<Long> browsingHistoryItemIds = browsingHistoryResolver.getBrowsingHistory(request, Long.class);
	
	        List<RecommendationEngineCommand> recommendationEngineCommandList = recommendationEngineBuilder
	                        .getRecommendationEngineCommandList(browsingHistoryItemIds, itemId);
	
	             ******
	    }
	}

```



现在 


```JAVA

	@Controller
	public class RecommendationEngineController extends BaseController{
	
	 
	    @ResponseBody
	    @ClientCache(value = TimeInterval.SECONDS_PER_MINUTE * 5)
	    @RequestMapping(value = { "/item/{itemId}/recommendation.json" },method = RequestMethod.POST,headers = HEADER_WITH_AJAX_SPRINGMVC)
	    public Map<RecommendationEngineType, List<RecommendationEngineCommand>> doHandler(
	                    HttpServletRequest request,
	                    HttpServletResponse response,
	                    @PathVariable("itemId") Long itemId){
	
	        List<Long> browsingHistoryItemIds = browsingHistoryResolver.getBrowsingHistoryIdListExcludeId(itemId, request, response);
	
	        List<RecommendationEngineCommand> recommendationEngineCommandList = recommendationEngineBuilder
	                        .getRecommendationEngineCommandList(browsingHistoryItemIds);
	
	        ******
	    }
	}


```

##最后 重新发布




优点:

1.  可以在外部配置 cookie所有的信息