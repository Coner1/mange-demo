package com.yzqc.crm.mbe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yzqc.crm.mbe.api.entity.MbeAreamg;

public interface MbeAreaMgRepository extends PagingAndSortingRepository<MbeAreamg, Long>, JpaSpecificationExecutor<MbeAreamg> {

	@Query("select max(demo.id)+1 from MbeAreamg demo")
	long getNextId();
	
	@Query(value = "select m from MbeAreamg m where code =?1")
	Page<MbeAreamg> queryPage(String code, Pageable pageable);
	
}
