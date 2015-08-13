package com.yzqc.crm.mbe.api.service;

import com.yzqc.crm.mbe.api.entity.MbeGroupmg;
import com.yzqc.support.exception.ConfilctException;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;


public interface MbeGroupService {

    Page<MbeGroupmg> queryPage(Map<String, Object> queryMap, Pageable pageable);

    void save(MbeGroupmg mbeGroupmg) throws Throwable;

    MbeGroupmg findGrpById(Long id) throws NotFoundException;

    void update(MbeGroupmg mbeGroupmg) throws NotFoundException, ConfilctException;

    void delete(Long id);
}
