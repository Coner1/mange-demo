package uap.web.example.web.index;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class IndexController {

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		String cuser = null;
		if (SecurityUtils.getSubject().getPrincipal() != null)
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("cusername", cuser==null?"":cuser);
		return "index";
	}
}
