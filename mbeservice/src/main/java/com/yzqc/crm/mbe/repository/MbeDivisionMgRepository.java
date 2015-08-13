package com.yzqc.crm.mbe.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.yzqc.crm.mbe.api.entity.MbeDivisionmg;



public interface MbeDivisionMgRepository extends PagingAndSortingRepository<MbeDivisionmg, Long>, JpaSpecificationExecutor<MbeDivisionmg> {

	 @Transactional
	    @Modifying(clearAutomatically = true)
	    @Query("update MbeDivisionmg m set m.name=:name,m.code=:code,m.description=:desc,m.ts=:curTs where m.pkDivisionmg=:pkDivisionmg and m.ts=:ts and m.dr=1")
	    int update(@Param("name") String name, @Param("code") String code, @Param("desc") String desc,
	               @Param("curTs") Date curTs, @Param("pkDivisionmg") Long pkDivisionmg, @Param("ts") Date ts);

	    @Transactional
	    @Modifying(clearAutomatically = true)
	    @Query("update MbeDivisionmg m set m.dr=?1 where m.pkDivisionmg=?2")
	    void update(int reomved, Long id);
	
}
