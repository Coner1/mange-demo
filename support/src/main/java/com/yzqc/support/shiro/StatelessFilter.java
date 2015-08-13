package com.yzqc.support.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2015/7/22.
 */
public class StatelessFilter extends AccessControlFilter {

    private static final Logger logger = LoggerFactory.getLogger(StatelessFilter.class);


    /**
     * 只验证登陆，购物车，支付页面。
     *
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws
            Exception {
        return false;
    }

    /**
     * 验证失败之后，跳转到登陆页面
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uname = CookieUtil.findCookieValue(request.getCookies(), CookieConstants.USERNAME);
        String digest = CookieUtil.findCookieValue(request.getCookies(), CookieConstants.TOKEN);

        if (StringUtils.isNotBlank(uname) && StringUtils.isNotEmpty(digest)) {
            //生成无状态Token
            StatelessToken token = new StatelessToken(uname, digest);
            try {
                //5、委托给SecurityManager登录
                getSubject(request, servletResponse).login(token);
            } catch (Exception e) {
                logger.error("AuthenticationInfo Failed", e);

                //登录失败，跳转到登录页
                onLoginFail(request, servletResponse);
                return false;
            }
        } else {
            logger.error("No Cookie found!! redirect to login page");
            onLoginFail(request, servletResponse);
            return false;
        }

        return true;
    }

    /**
     * 跳转到登陆页面
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void onLoginFail(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);

        // 跳转到登录页
        redirectToLogin(request, httpResponse);
    }


}
