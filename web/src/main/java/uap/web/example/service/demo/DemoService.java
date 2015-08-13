package uap.web.example.service.demo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.CustomSearchFilter;

import uap.web.example.entity.Demo;
import uap.web.example.repository.DemoDao;

@Service
public class DemoService {
	
	@Autowired
	private JdbcTemplate jt;

	@Autowired
	private DemoDao dao;
	
	public Demo getDemoById(long id){
		return dao.findOne(id);
	}
	
	public void deleteById(Long id) {
		dao.delete(id);
	}
	
	public Demo getDemoBySql(Long id) {
		return dao.getDemoByNativeSql(id);
	}
	
	@Transactional
	public void deleteDemoByIdUseSql(Long id) {
		dao.deleteDemoByIdUseSql(id);
	}
	
	public Demo saveEntity(Demo entity) throws Exception{
		if(0 == entity.getId()){
			entity.setId(dao.getNextId());
		}
		return dao.save(entity);
	}
	
	public Page<Demo> getDemoPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		//jt.queryForList("select * from emall_demo;");
		//jt.queryForList("select count(demo0_.id) as col_0_0_ from example_demo demo0_ where 2=1;");
		//jt.queryForList("select demo0_.id as id1_0_, demo0_.code as code2_0_, demo0_.isdefault as isdefaul3_0_, demo0_.memo as memo4_0_, demo0_.name as name5_0_ from example_demo demo0_ where 1=1 order by demo0_.id desc limit 15;");
		Specification<Demo> spec = buildSpecification(searchParams);
		return dao.findAll(spec, pageRequest);
	}
	
	/**
	 * 创建动态查询条件组合.
	 */
	public Specification<Demo> buildSpecification(Map<String, Object> searchParams) {
		Map<String, CustomSearchFilter> filters = CustomSearchFilter.parse(searchParams);
		Specification<Demo> spec = DynamicSpecifications.bySearchFilter(filters.values(), Demo.class);
		return spec;
	}

}
