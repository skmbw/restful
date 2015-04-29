package com.vteba.user.rest;

import java.util.Date;

import javax.inject.Named;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
@Named
@Path("/user")
public class UserRestService {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@GET
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
	
	@POST
	@Path("/add")
	public Response add(User user) {
		
		return Response.ok().build();
	}
	
	@PUT
	@Path("/update")
	public Response update() {
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/delete/{userId}")
	public Response delete(@PathParam("userId") Long userId) {
		
		return Response.ok().build();
	}
}
