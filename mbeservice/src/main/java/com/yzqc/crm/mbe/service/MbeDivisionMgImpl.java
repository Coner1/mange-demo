package com.yzqc.crm.mbe.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yzqc.crm.mbe.api.entity.MbeDivisionmg;
import com.yzqc.crm.mbe.api.service.MbeDivisionmgService;
import com.yzqc.crm.mbe.repository.MbeDivisionMgRepository;
import com.yzqc.support.Constants.DataExistsFlag;
import com.yzqc.support.Constants.TransactionalContanstants;
import com.yzqc.support.exception.ConfilctException;
import com.yzqc.support.utils.SpecificationBuilder;


@Service
public class MbeDivisionMgImpl implements MbeDivisionmgService {
	
	@Autowired
	private MbeDivisionMgRepository dao;

	@Override
	public Page<MbeDivisionmg> queryPage(Map<String, Object> queryMap,Pageable pageable) {
		// TODO 自动生成的方法存根
		 queryMap.put("EQ_dr", 1);
	        Specification<MbeDivisionmg> spec = SpecificationBuilder.buildSpecification(queryMap, MbeDivisionmg.class);
	        Page<MbeDivisionmg> page = dao.findAll(spec, pageable);
	        return page;
	}
	
	@Transactional
	@Override
	public void save(MbeDivisionmg entity) throws Exception {
		entity.setCreatetime(new Date());
	
		entity.setDr(1);
		dao.save(entity);
		
	}

	@Override
	public MbeDivisionmg findDivisionmgById(Long id) throws NotFoundException {
		// TODO 自动生成的方法存根
		 Map<String, Object> queryMap = new HashMap<String, Object>();
	        queryMap.put("EQ_pkDivisionmg", id);
	        queryMap.put("EQ_dr", 1);
	        Specification<MbeDivisionmg> spec = SpecificationBuilder.buildSpecification(queryMap, MbeDivisionmg.class);
	        MbeDivisionmg divisionmg = dao.findOne(spec);
	        if (divisionmg == null) {
	            throw new NotFoundException("这条数据已经不存在");
	        }
	        return divisionmg;
	}
	
	 @Transactional(rollbackFor = NotFoundException.class)
	    @Override
	    public void update(MbeDivisionmg mbeDivisionmg) throws NotFoundException {
		 MbeDivisionmg divisionmg = dao.findOne(mbeDivisionmg.getPkDivisionmg());
	        if (divisionmg == null) {
	            throw new NotFoundException("这条数据已经不存在");
	        }
	        int row = dao.update(divisionmg.getName(), divisionmg.getCode(), divisionmg.getDescription(), new Date(),
	        		divisionmg.getPkDivisionmg(), divisionmg.getTs());
	        if (row == TransactionalContanstants.FAILED) {
	            throw new ConfilctException("这条数据已经被修改，请重新操作");
	        }

	    }

	@Override
	public void deleteById(Long id) {
		// TODO 自动生成的方法存根
		dao.update(DataExistsFlag.REOMVED, id);
	}
	

}

