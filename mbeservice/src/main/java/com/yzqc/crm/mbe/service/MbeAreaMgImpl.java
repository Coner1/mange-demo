package com.yzqc.crm.mbe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yzqc.crm.mbe.api.entity.MbeAreamg;
import com.yzqc.crm.mbe.api.service.MbeAreamgService;
import com.yzqc.crm.mbe.repository.MbeAreaMgRepository;

@Service
public class MbeAreaMgImpl implements MbeAreamgService {
	
	@Autowired
	private MbeAreaMgRepository dao;

	@Override
	public Page<MbeAreamg> queryByPage(Pageable pageable) throws Exception {
		Page<MbeAreamg> demo1 = dao.queryPage("3",pageable);
		List<MbeAreamg> list = new ArrayList<MbeAreamg>();
		Iterable<MbeAreamg> demo = dao.findAll(pageable);
		for(;demo.iterator().hasNext();){
			list.add(demo.iterator().next());
		}
		Page<MbeAreamg> page = new PageImpl<MbeAreamg>(list, pageable, 10);
		return page;
	}

	@Override
	public MbeAreamg save(MbeAreamg entity) throws Exception {
		if(null == entity.getPkAreamg()){
//			entity.setPkAreamg("111");
		}
		return dao.save(entity);
	}

	@Override
	public MbeAreamg getMbeAreamgById(Long id) throws Exception {
		return dao.findOne(id);
	}

	@Override
	public void deleteById(Long id) throws Exception {
		dao.delete(id);
	}

}
