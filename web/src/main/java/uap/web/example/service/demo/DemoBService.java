package uap.web.example.service.demo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.CustomSearchFilter;

import uap.web.example.entity.DemoB;
import uap.web.example.repository.DemoBDao;


@Service
public class DemoBService {

	@Autowired
	private DemoBDao dao;
	
	public DemoB getDemoById(long id){
		return dao.findOne(id);
	}
	
	public void deleteById(Long id) {
		dao.delete(id);
	}
	
	public DemoB getDemoBySql(Long id) {
		return dao.getDemoByNativeSql(id);
	}
	
	@Transactional
	public void deleteDemoByIdUseSql(Long id) {
		dao.deleteDemoByIdUseSql(id);
	}
	
	public DemoB saveEntity(DemoB entity) throws Exception{
		if(0 == entity.getTheid() ){
			entity.setTheid(dao.getNextId());
		}
		return dao.save(entity);
	}
	
	public Page<DemoB> getDemoPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		Specification<DemoB> spec = buildSpecification(searchParams);
		return dao.findAll(spec, pageRequest);
	}
	
	/**
	 * 创建动态查询条件组合.
	 */
	public Specification<DemoB> buildSpecification(Map<String, Object> searchParams) {
		Map<String, CustomSearchFilter> filters = CustomSearchFilter.parse(searchParams);
		Specification<DemoB> spec = DynamicSpecifications.bySearchFilter(filters.values(), DemoB.class);
		return spec;
	}

}
