package cn.ioms.ssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.ioms.ssm.entity.User;
import cn.ioms.ssm.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value = "insertUser")
	public String insertUser() {
		User us = new User(Long.valueOf("123"),"s123","张三");
		int count = userService.insertSelective(us);
		return "受影响行数："+count;
	}
}
