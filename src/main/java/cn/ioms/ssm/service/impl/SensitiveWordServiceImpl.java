package cn.ioms.ssm.service.impl;

import cn.ioms.ssm.dao.DAO;
import cn.ioms.ssm.entity.SensitiveWord;
import cn.ioms.ssm.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {


    @Autowired
    private DAO dao;


    @Override
    public List<SensitiveWord> queryAll() {


        return (List<SensitiveWord>) dao.findForList("SensitiveWordMapper.queryAll",null);
    }
}
