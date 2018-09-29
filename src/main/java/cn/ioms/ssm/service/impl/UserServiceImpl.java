package cn.ioms.ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ioms.ssm.dao.DAO;
import cn.ioms.ssm.entity.User;
import cn.ioms.ssm.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private DAO dao;
	

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(User record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(User record) {
		
		return (int) dao.save("UserMapper.insertSelective", record);
	}

	@Override
	public User selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(User record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(User record) {
		// TODO Auto-generated method stub
		return 0;
	}

}
