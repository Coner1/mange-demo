package uap.web.example.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import uap.web.core.jdbc.BaseJdbcDao;
import uap.web.example.entity.MgrFunction;

@Component
public class MgrFunctionJdbcDao extends BaseJdbcDao<MgrFunction> {

	public List<MgrFunction> findAllFuncsByUserId(long userId) {
		String sql = "select * from mgr_function where isactive='Y' and id in (select func_id from mgr_role_func where role_id in (select role_id from mgr_role_user where user_id = ?))";
		List<MgrFunction> result = (List<MgrFunction>) this.getJdbcTemplate().query(sql, new Object[] { userId }, BeanPropertyRowMapper.newInstance(MgrFunction.class));
		return result;
	}

	// 待完善
	public void addRoleUser(long userId){
		long id = getNextId();
		String sql = "insert into mgr_role_user(id, role_id, user_id) values("+ id +", '2', '"+ userId +"')";
		this.getJdbcTemplate().execute(sql);
	}
	
	public long getNextId(){
		String sql = "select max(id) + 1 from mgr_role_user";
		return this.getJdbcTemplate().queryForObject(sql, Long.class);
	}
}
