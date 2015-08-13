package com.yzqc.crm.controller.mbe;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.web.Servlets;

import uap.web.example.entity.MgrUser;

import com.yzqc.crm.mbe.api.entity.MbeDivisionmg;
import com.yzqc.crm.mbe.api.service.MbeDivisionmgService;
import com.yzqc.support.shiro.SessionCacheManager;

@Controller
@RequestMapping("mbe_divisionmg")
public class MbeDivisionmgController {
	
	private static final Logger logger = LoggerFactory.getLogger(MbeDivisionmgController.class);
	
	@Autowired
	private MbeDivisionmgService service;
	
	@Autowired
    private SessionCacheManager cacheManager;

    @Autowired
    private Validator validator;

	
	@RequestMapping(value = "page", method = RequestMethod.GET)
	 public ModelAndView list(
	            @RequestParam(value = "page", defaultValue = "1") int pageno,
	            @RequestParam(value = "page.size", defaultValue = "20") int pageSize,
	            @RequestParam(value = "sortType", defaultValue = "auto") String sortType,
	        Model model, HttpServletRequest request) throws Exception {
	        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
	        PageRequest pageRequest = buildPageRequest(pageno, pageSize, sortType);
	        Page<MbeDivisionmg> page = service.queryPage(searchParams, pageRequest);
	        return new ModelAndView("", "page", page);
	    }
	

	/**
	 * 进入新增页面
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("division/add", "value", new MbeDivisionmg());
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
    public ModelAndView add(@RequestBody MbeDivisionmg mbeDivisionmg) throws Throwable {
		
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        //验证
        BeanValidators.validateWithException(validator, mbeDivisionmg);
        ModelAndView modelAndView = new ModelAndView("aaa");

        //保存数据
        MgrUser user = cacheManager.getCurUser((String) SecurityUtils.getSubject().getPrincipal());
        mbeDivisionmg.setCreater(user.getName());
        service.save(mbeDivisionmg);
        return modelAndView;
    }


    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public ModelAndView toUpdate(@PathVariable("id") Long id) throws Exception {
        MbeDivisionmg divisionmg = service.findDivisionmgById(id);
        return new ModelAndView("", "value", divisionmg);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ModelAndView update(@RequestBody MbeDivisionmg mbeDivisionmg) throws Exception {

        //验证
        BeanValidators.validateWithException(validator, mbeDivisionmg);
        ModelAndView modelAndView = new ModelAndView("aaa");
        //保存数据
        service.update(mbeDivisionmg);
        return modelAndView;
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public ModelAndView delete(@PathVariable("id") Long id) throws Exception {
        ModelAndView modelAndView = new ModelAndView("aaa");
        //保存数据
        service.deleteById(id);
        return modelAndView;
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pageSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, "pkDivisionmg");
        } else if ("code".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "code");
        }
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }
}
