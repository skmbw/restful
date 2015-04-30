package com.vteba.user.rest;

import java.util.Date;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vteba.user.model.User;
import com.vteba.user.service.UserServiceImpl;

/**
 * resteasy实现的restful webservice服务
 * 
 * @author yinlei
 * @see
 * @since 2015年4月29日 上午10:16:58
 */
@Named// 表示是一个bean，受spring管理
@Path("/user")// 映射路径
public class UserRestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestService.class);
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@GET// 用于查询
	@Path("/detail/{userId}")// 包含路径参数
	@Produces(value = { MediaType.APPLICATION_JSON })// 返回Json格式的数据
	public User get(@PathParam("userId") Long userId) {// 获取路径参数作为入参
		User user = new User();
		user.setId(11L);
		user.setName("yinlei");
		user.setCreateDate(new Date());
		
		userServiceImpl.save(user);
		
		return user;
	}
	
	@POST// 一般用于添加
	@Path("/add")
	public Response add(User user) {
		LOGGER.info("收到参数user.id=[{}], user.name=[{}]", user.getId(), user.getName());
		return Response.ok().build();
	}
	
	@PUT// 一般用于修改，浏览器没有提供put方法，所以要用jquery，提供的模拟方法
	@Path("/update")
	@Consumes(value = { MediaType.APPLICATION_JSON })// 接受json类型的数据
	public Response update(User user) {
		LOGGER.info("收到参数user.id=[{}], user.name=[{}]", user.getId(), user.getName());
		return Response.ok().build();
	}
	
	@DELETE// 一般用于删除
	@Path("/delete/{userId}")
	public Response delete(@PathParam("userId") Long userId) {
		
		return Response.ok().build();
	}
}
