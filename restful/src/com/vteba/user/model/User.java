package com.vteba.user.model;

import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 * 
 * @author yinlei
 * @see
 * @since 2015年4月28日 上午10:21:41
 */
public class User {
	private Long id;
	private String name;
	private Date createDate;
	private List<User> userList;
	private List<String> strList;
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<String> getStrList() {
		return strList;
	}

	public void setStrList(List<String> strList) {
		this.strList = strList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
