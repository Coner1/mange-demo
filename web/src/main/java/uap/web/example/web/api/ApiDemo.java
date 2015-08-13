package uap.web.example.web.api;

import com.yzqc.crm.mbe.api.entity.MbeGroupmg;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uap.web.example.entity.MgrUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2015/8/12.
 */
@Controller
@RequestMapping("/api/demo")
public class ApiDemo {

    @RequestMapping("query/{uname}")
    @ResponseBody
    public MgrUser queryUser(@PathVariable("uname") String username, @RequestBody MbeGroupmg groupmg, HttpServletResponse response) {
        response.addHeader("bodyEncrypt", "true");
        System.out.println(groupmg);
        MgrUser user = new MgrUser();
        user.setName(username);
        user.setLoginName(username);
        return user;
    }
}
