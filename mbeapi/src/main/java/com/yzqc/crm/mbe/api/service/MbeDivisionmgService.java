package com.yzqc.crm.mbe.api.service;

import java.util.Map;

import javassist.NotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yzqc.crm.mbe.api.entity.MbeDivisionmg;
import com.yzqc.support.exception.ConfilctException;

public interface MbeDivisionmgService {

	Page<MbeDivisionmg> queryPage(Map<String, Object> queryMap, Pageable pageable);

	void save(MbeDivisionmg mbeDivisionmg)throws Throwable;

	MbeDivisionmg findDivisionmgById(Long id) throws NotFoundException;
	
	void update(MbeDivisionmg mbeDivisionmg)throws  NotFoundException, ConfilctException;
	
	void deleteById(Long id);

}
