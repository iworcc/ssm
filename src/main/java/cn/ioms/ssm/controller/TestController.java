package cn.ioms.ssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.ioms.ssm.service.TestService;

/**
 * @RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
 */
@RestController
public class TestController {

	
	@Autowired
	private TestService testService;
	
	@RequestMapping(value = "/index")
	public String  index() {
		return "Hello World!";
	}
	
	
	@RequestMapping(value = "/msg")
	public String msg() {
		return testService.msg();
	}
}
