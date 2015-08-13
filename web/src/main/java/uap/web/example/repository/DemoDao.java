package uap.web.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import uap.web.example.entity.Demo;

public interface DemoDao extends PagingAndSortingRepository<Demo, Long> , JpaSpecificationExecutor<Demo>{

	Demo findByCode(String code);
	
	@Query("select d from Demo d where code in (:codes)")
	List<Demo> findByConditions(String[] codes);
	
	@Query("select max(demo.id)+1 from Demo demo")
	long getNextId();
	
	@Query(value = "select * from example_demo where id = ?1", nativeQuery=true)
	Demo getDemoByNativeSql(long id);
	
	@Modifying
	@Query(value = "delete from example_demo where id = ?1", nativeQuery=true)
	void deleteDemoByIdUseSql(long id);
}
