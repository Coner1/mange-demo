package com.yzqc.crm.controller.mbe;

import com.yzqc.crm.mbe.api.entity.MbeGroupmg;
import com.yzqc.crm.mbe.api.service.MbeGroupService;
import com.yzqc.support.exception.ValidateException;
import com.yzqc.support.shiro.SessionCacheManager;
import javassist.NotFoundException;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuqp
 */
@Controller
@RequestMapping("mbe_grpmg")
public class MbeGroupController {

    private static final Logger logger = LoggerFactory.getLogger(MbeGroupController.class);

    @Autowired
    private MbeGroupService mbeService;

    @Autowired
    private SessionCacheManager cacheManager;

    @Autowired
    private Validator validator;

    @RequestMapping(value = "page", method = RequestMethod.GET)
    public ModelAndView list(
            @RequestParam(value = "page", defaultValue = "1") int pageno,
            @RequestParam(value = "page.size", defaultValue = "20") int pageSize,
            @RequestParam(value = "sortType", defaultValue = "auto") String sortType,
            Model model, HttpServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        PageRequest pageRequest = buildPageRequest(pageno, pageSize, sortType);
        Page<MbeGroupmg> page = mbeService.queryPage(searchParams, pageRequest);
        return new ModelAndView("member/list", "page", page);
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView toAdd(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("member/add", "value", new MbeGroupmg());
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ModelAndView add(@RequestBody MbeGroupmg mbeGroupmg) throws Throwable {

        //验证
        BeanValidators.validateWithException(validator, mbeGroupmg);
        ModelAndView modelAndView = new ModelAndView("aaa");

        //保存数据
        MgrUser user = cacheManager.getCurUser((String) SecurityUtils.getSubject().getPrincipal());
        mbeGroupmg.setCreater(BigInteger.valueOf(user.getId()));
        try {
            mbeService.save(mbeGroupmg);
        } catch (Throwable e) {
            logger.error("Fail to save " + mbeGroupmg, e);
            throw new ValidateException("code", "编码" + mbeGroupmg.getCode() + "已经被使用");
        }
        return modelAndView;
    }


    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public ModelAndView toUpdate(@PathVariable("id") Long id) throws NotFoundException {
        MbeGroupmg groupmg = mbeService.findGrpById(id);
        return new ModelAndView("", "value", groupmg);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ModelAndView update(@RequestBody MbeGroupmg mbeGroupmg) throws NotFoundException {

        //验证
        BeanValidators.validateWithException(validator, mbeGroupmg);
        ModelAndView modelAndView = new ModelAndView("aaa");
        //保存数据
        mbeService.update(mbeGroupmg);
        return modelAndView;
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public ModelAndView delete(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("aaa");
        //保存数据
        mbeService.delete(id);
        return modelAndView;
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pageSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, "pkMbeGroupmg");
        } else if ("code".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "code");
        }
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }
}
