package com.yzqc.crm.controller.mbe;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.apache.shiro.SecurityUtils;
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

import com.yzqc.crm.mbe.api.entity.MbeAreamg;
import com.yzqc.crm.mbe.api.service.MbeAreamgService;
import com.yzqc.support.shiro.SessionCacheManager;

@Controller
@RequestMapping("mbe_areamg")
public class MbeAreamgController {
	
	@Autowired
	private MbeAreamgService areamgService;
	
//	private OperResult<MbeAreamg> result = null;
	
	@Autowired
	private SessionCacheManager cacheManager;
	
	@Autowired
	private Validator validator;
	
	

	@RequestMapping(value = "page",method=RequestMethod.GET)
	public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") int pageno,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "sortType", defaultValue = "auto") String sortType,
            Model model, HttpServletRequest request) {
		 Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
         PageRequest pageRequest = buildPageRequest(pageno, pageSize, sortType);
         Page<MbeAreamg> page = null;//areamgService.queryPage(searchParams, pageRequest);
         return new ModelAndView("", "page", page);
	}
	
	 
	/**
	 * 进入新增页面
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response){
		MbeAreamg areamg = new MbeAreamg();
		areamg.setCreater("admin");
		areamg.setCteatetime(new Date());
		return new ModelAndView("","value",areamg);
	}
	
	/**
	 * 进入更新页面
	 * @throws Exception 
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public ModelAndView toUpdate(@PathVariable("id") Long id, Model model) throws Exception {
		MbeAreamg areamg = null;
		areamg = areamgService.getMbeAreamgById(id);
        return new ModelAndView("", "value", areamg);
    }

	@RequestMapping(value = "update",method = RequestMethod.POST)
	public ModelAndView update(@RequestBody MbeAreamg mbeAreamg) throws NotFoundException{
		//验证
        BeanValidators.validateWithException(validator, mbeAreamg);
		//areamgService.update(mbeAreamg);
        return new ModelAndView();
	}
	
	@RequestMapping(value = "add",method = RequestMethod.POST)
	public ModelAndView add(@RequestBody MbeAreamg mbeAreamg) throws Throwable{
		
		BeanValidators.validateWithException(validator, mbeAreamg);
		MgrUser user = cacheManager.getCurUser((String) SecurityUtils.getSubject().getPrincipal());
		mbeAreamg.setCreater(BigInteger.valueOf(user.getId())+"");
		areamgService.save(mbeAreamg);
        return new ModelAndView();
	}
	
	@RequestMapping(value = "del/{id}",method = RequestMethod.DELETE)
	public ModelAndView del(@PathVariable("id") Long id) throws Exception{
		areamgService.deleteById(id);
        return new ModelAndView();
	}
	
	/**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pageSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, "pkAreamg");
        } else if ("code".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "code");
        }
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }
	
}
