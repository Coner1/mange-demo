package uap.web.example.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yzqc.support.shiro.CookieConstants;
import com.yzqc.support.shiro.CookieUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import uap.web.example.entity.MgrUser;
import uap.web.example.service.account.AccountService;
import uap.web.utils.Base64Util;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * <p/>
 * 真正登录的POST请求由Filter完成,
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {

    public static final int HASH_INTERATIONS = 1024;
    // 默认一天
    public static final int COOKIES_MAXAGE = 60 * 60 * 24;
    public static final String COOKIES_PATH = "/";

    @Autowired
    protected AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(method = RequestMethod.POST, value = "formLogin")
    public String formLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("username");
        String passWord = request.getParameter("password");

        MgrUser user = accountService.findUserByLoginName(userName);
        if (passWord != null && user != null) {
            byte[] hashPassword = Digests.sha1(passWord.getBytes(), Encodes.decodeHex(user.getSalt()), HASH_INTERATIONS);
            String checkPwd = Encodes.encodeHex(hashPassword);
            if (checkPwd.equals(user.getPassword())) {
                String cookieValue = Base64Util.encode(checkPwd.getBytes());

                // 校验成功，写cookie
                response.addCookie(CookieUtil.createCookie(CookieConstants.USERNAME, user.getLoginName()));
                response.addCookie(CookieUtil.createCookie(CookieConstants.TOKEN, user.getPassword()));
            }
            return "redirect";
        } else {
            return "login";
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
        return "login";
    }

}
