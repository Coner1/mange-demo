package uap.web.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import uap.web.example.entity.DemoB;


public interface DemoBDao extends PagingAndSortingRepository<DemoB, Long> , JpaSpecificationExecutor<DemoB>{

	DemoB findByCode(String code);
	
	@Query("select d from DemoB d where code in (:codes)")
	List<DemoB> findByConditions(String[] codes);
	
	@Query("select max(demo.id)+1 from DemoB demo")
	long getNextId();
	
	@Query(value = "select * from example_demo where id = ?1", nativeQuery=true)
	DemoB getDemoByNativeSql(long id);
	
	@Modifying
	@Query(value = "delete from example_demo where id = ?1", nativeQuery=true)
	void deleteDemoByIdUseSql(long id);
}
