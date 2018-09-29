package cn.ioms.ssm.service.impl;

import org.springframework.stereotype.Service;

import cn.ioms.ssm.service.TestService;

@Service
public class TestServiceImpl implements TestService{

	@Override
	public String msg() {
		// TODO Auto-generated method stub
		return "一切正常~";
	}

}
