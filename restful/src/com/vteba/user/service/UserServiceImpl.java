package com.vteba.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.vteba.user.model.User;

@Service
public class UserServiceImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	public Long save(User user) {
		LOGGER.debug("±£´æuser=[{}].", user);
		return 21L;
	}
}
