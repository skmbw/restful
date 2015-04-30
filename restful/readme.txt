1、restful的入口是UserRestServcie.java

2、对应的测试方法分别是
http://localhost:8090/restful/add.jsp，测试添加的(@POST)
http://localhost:8090/restful/index.jsp
页面中的 更新和删除按钮测试  更新(@UPDATE)和删除（@DELETE）的

之所以写前面三个方法，是因为 浏览器，一般没有实现update和delete方法，post也要表单提交。
所以通用的做法是 使用jquery等js框架模拟。或者直接使用 Apache的 httpclient等来操作。
restful webservice，是提供业务的

查询（@GET），直接在浏览器中http://localhost:8090/restful/rest/user/detail/2

3、和spring mvc还有spring 结合后在web.xml中配置稍有变化
原来的listener变了，用下面的代替
<!-- resteasy启动初始化监听器 -->
	<listener>
      	<listener-class>org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap</listener-class> 
   	</listener>
   	<!-- resteasy和spring整合，有了这个，ContextLoaderListener就不要了  -->
	<listener>
		<listener-class>com.vteba.service.context.listener.SpringContextLoaderListener</listener-class>
  	</listener>

4、这个是配置resteasy 拦截 url的，为了和spring mvc区分，一般要添加一个前缀
	
<!-- 要指定前缀否则和spring mvc的url-pattern冲突。还有一种解决办法就是将spring mvc和reseasy整合在一起
		tomcat中需要这个，jboss中就要注释掉了
	 -->
	<servlet>
      	<servlet-name>resteasy</servlet-name>
      	<servlet-class>org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher</servlet-class>
      	<init-param>
      		<param-name>resteasy.servlet.mapping.prefix</param-name>
        	<param-value>/rest</param-value>
      	</init-param>
    </servlet>
  
   	<servlet-mapping>
      	<servlet-name>resteasy</servlet-name>
      	<url-pattern>/rest/*</url-pattern>t
   	</servlet-mapping>
   	
5、和spring mvc整合，还要再 spring的主配置文件application-context.xml中引入 
<!-- Import basic SpringMVC Resteasy integration -->
    <import resource="classpath:springmvc-resteasy.xml"/>
这个配置文件，包含在resteasy-spring-3.0.11.Final.jar中

6、我这里返回和接受json的数据，使用fastjson来实现，
在application-context.xml中，有配置
<bean id="fastjsonProvider" class="com.vteba.service.json.jaxrs.FastJsonProvider"></bean>

一般，resteasy会使用jackson的提供者。我做了替换。性能好一些。

7、将项目导入到eclipse中，启动tomcat可以调试了。