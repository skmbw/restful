package com.vteba.user.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vteba.user.model.User;
import com.vteba.user.service.UserServiceImpl;

/**
 * 用户控制类
 * 
 * @author yinlei
 * @see
 * @since 2015年4月28日 上午10:20:49
 */
@Controller
@RequestMapping("/user")
public class UserAction {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@RequestMapping("/save")
	public String save() {
		User user = new User();
		user.setName("userName1");
		user.setCreateDate(new Date());
		userServiceImpl.save(user);
		return "user/list";
	}
	
	@RequestMapping("/ftl")
	public String ftl() {
		User user = new User();
		user.setName("userName1");
		user.setCreateDate(new Date());
		
		return "user.ftl";
	}
	
	@RequestMapping("/jsp")
	public String jsp() {
		User user = new User();
		user.setName("userName1");
		user.setCreateDate(new Date());
		
		return "user.jsp";
	}
}
