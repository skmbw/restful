package com.vteba.user.action;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vteba.user.model.User;

/**
 * �û�������
 * 
 * @author yinlei
 * @see
 * @since 2015��4��28�� ����10:20:49
 */
@Controller
@RequestMapping("/user")
public class UserAction {
	
	@RequestMapping("/save")
	public String save() {
		User user = new User();
		user.setName("userName1");
		user.setCreateDate(new Date());
		
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