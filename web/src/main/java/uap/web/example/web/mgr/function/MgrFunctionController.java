package uap.web.example.web.mgr.function;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import com.yzqc.support.shiro.SessionCacheManager;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.web.Servlets;

import uap.web.example.entity.MgrFunction;
import uap.web.example.entity.MgrUser;
import uap.web.example.service.mgr.MgrFunctionService;

@Controller
@RequestMapping(value = "/mgr/function")
public class MgrFunctionController {
private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MgrFunctionService service;

	@Autowired
	private SessionCacheManager sessionCacheManager;

	@Autowired
	private Validator validator;

	@RequestMapping(value = "page", method = RequestMethod.GET)
	public @ResponseBody Page<MgrFunction> page(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = "20") int pageSize, @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
	    searchParams = Servlets.getParametersStartingWith(request, "search_");

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);

		Page<MgrFunction> categoryPage = service.getDemoPage(searchParams, pageRequest);
		return categoryPage;
	}

	/** 进入新增 */
	@RequestMapping(value="create", method=RequestMethod.GET)
	public @ResponseBody MgrFunction add() {
		MgrFunction entity = new MgrFunction();
		return entity;
	}

	/** 保存新增 */
    @RequestMapping(value="create", method=RequestMethod.POST)
    public @ResponseBody MgrFunction create(@RequestBody MgrFunction entity, HttpServletRequest resq) {

    	BeanValidators.validateWithException(validator, entity);
    	try {
    		entity = service.saveEntity(entity);
		} catch (Exception e) {
			//记录日志
			logger.error("保存出错!",e);
		}
        return entity;
    }

	/**
	 * 进入更新界面
	 *
	 * @param id
	 * @param model
	 * @return 需要更新的实体的json结构
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public @ResponseBody MgrFunction updateForm(@PathVariable("id") Long id, Model model) {
		MgrFunction entity = service.getFuncById(id);
		return entity;
	}

	/** 保存更新 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public @ResponseBody MgrFunction update(@RequestBody MgrFunction entity) {
    	try {
    		entity = service.saveEntity(entity);
		} catch (Exception e) {
			logger.error("更新出错!",e);
		}
		return entity;
	}

	/**
	 * 删除实体
	 *
	 * @param id 删除的标识
	 * @return 是否删除成功
	 */
	@RequestMapping(value = "delete/{id}",method = RequestMethod.DELETE)
	public @ResponseBody boolean delete(@PathVariable("id") Long id) {
		service.deleteById(id);
		return true;
	}


	@RequestMapping(value = "rootmenu", method = RequestMethod.GET)
	public @ResponseBody MgrFunction getRootMenu(Model model, HttpServletRequest request) {
		MgrUser cuser = null;
		if(SecurityUtils.getSubject().getPrincipal()!=null)
			cuser = sessionCacheManager.getCurUser(request);
		if(cuser == null){
			return null;
		}

		return service.getFuncRootByUser(cuser.getId());
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "title");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
}
