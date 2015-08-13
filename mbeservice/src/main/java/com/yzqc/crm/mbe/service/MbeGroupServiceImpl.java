package com.yzqc.crm.mbe.service;

import com.yzqc.crm.mbe.api.entity.MbeGroupmg;
import com.yzqc.crm.mbe.api.service.MbeGroupService;
import com.yzqc.crm.mbe.repository.MbeRepository;
import com.yzqc.support.Constants.DataExistsFlag;
import com.yzqc.support.Constants.TransactionalContanstants;
import com.yzqc.support.exception.ConfilctException;
import com.yzqc.support.utils.SpecificationBuilder;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MbeGroupServiceImpl implements MbeGroupService {

    @Autowired
    private MbeRepository mbeRepository;

    @Override
    public Page<MbeGroupmg> queryPage(Map<String, Object> queryMap, Pageable pageable) {
        queryMap.put("EQ_dr", DataExistsFlag.EXISTS);
        Specification<MbeGroupmg> spec = SpecificationBuilder.buildSpecification(queryMap, MbeGroupmg.class);
        Page<MbeGroupmg> page = mbeRepository.findAll(spec, pageable);
        return page;
    }


    @Transactional
    @Override
    public void save(MbeGroupmg mbeGroupmg) throws Throwable {
        mbeGroupmg.setCreatetime(new Date());
        mbeGroupmg.setTs(new Date());
        mbeGroupmg.setDr(DataExistsFlag.EXISTS);
        mbeRepository.save(mbeGroupmg);
    }

    @Override
    public MbeGroupmg findGrpById(Long id) throws NotFoundException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("EQ_pkMbeGroupmg", id);
        queryMap.put("EQ_dr", DataExistsFlag.EXISTS);
        Specification<MbeGroupmg> spec = SpecificationBuilder.buildSpecification(queryMap, MbeGroupmg.class);
        MbeGroupmg groupmg = mbeRepository.findOne(spec);
        if (groupmg == null) {
            throw new NotFoundException("这条数据已经不存在");
        }
        return groupmg;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    @Override
    public void update(MbeGroupmg mbeGroupmg) throws NotFoundException {
        MbeGroupmg groupmg = mbeRepository.findOne(mbeGroupmg.getPkMbeGroupmg());
        if (groupmg == null) {
            throw new NotFoundException("这条数据已经不存在");
        }
        int row = mbeRepository.update(groupmg.getName(), groupmg.getCode(), groupmg.getDescription(), new Date(),
                mbeGroupmg.getPkMbeGroupmg(), mbeGroupmg.getTs());
        if (row == TransactionalContanstants.FAILED) {
            throw new ConfilctException("这条数据已经被修改，请重新操作");
        }

    }

    @Transactional(rollbackFor = NotFoundException.class)
    @Override
    public void delete(Long id) {
        mbeRepository.update(DataExistsFlag.REOMVED, id);
    }
}
