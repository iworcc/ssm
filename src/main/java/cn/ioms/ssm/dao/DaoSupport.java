package cn.ioms.ssm.dao;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**
 * 
 *  公共dao 仅有这一个dao，所有mapper的增删改查均由该dao调用
 *
 */
@Repository("daoSupport")
public class DaoSupport implements DAO {

	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;
	
	public Object save(String str, Object obj) {
		return sqlSessionTemplate.insert(str, obj);
	}

	public Object update(String str, Object obj) {
		return sqlSessionTemplate.update(str, obj);
	}

	public Object delete(String str, Object obj) {
		return sqlSessionTemplate.delete(str, obj);
	}

	public Object findForObject(String str, Object obj) {
		return sqlSessionTemplate.selectOne(str, obj);
	}

	public Object findForList(String str, Object obj) {
		return sqlSessionTemplate.selectList(str, obj);
	}
	
	public Object findForMap(String str, Object obj, String key, String value){
		return sqlSessionTemplate.selectMap(str, obj, key);
	}
}
