package com.yzqc.crm.mbe.repository;

import com.yzqc.crm.mbe.api.entity.MbeGroupmg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Administrator on 2015/8/5.
 */
public interface MbeRepository extends PagingAndSortingRepository<MbeGroupmg, Long>, JpaSpecificationExecutor<MbeGroupmg> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update MbeGroupmg m set m.name=:name,m.code=:code,m.description=:desc,m.ts=:curTs where m.pkMbeGroupmg=:pkMbeGroupmg and m.ts=:ts and m.dr=1")
    int update(@Param("name") String name, @Param("code") String code, @Param("desc") String desc,
               @Param("curTs") Date curTs, @Param("pkMbeGroupmg") Long pkMbeGroupmg, @Param("ts") Date ts);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update MbeGroupmg m set m.dr=?1 where m.pkMbeGroupmg=?2")
    void update(int reomved, Long id);
}
