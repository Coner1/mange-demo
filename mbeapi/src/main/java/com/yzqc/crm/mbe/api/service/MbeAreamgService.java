package com.yzqc.crm.mbe.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yzqc.crm.mbe.api.entity.MbeAreamg;

public interface MbeAreamgService {
	
	Page<MbeAreamg> queryByPage(Pageable pageable) throws Exception;

	MbeAreamg save(MbeAreamg entity) throws  Exception;

	MbeAreamg getMbeAreamgById(Long id) throws  Exception;
	
	void deleteById(Long id) throws Exception;
	
}

