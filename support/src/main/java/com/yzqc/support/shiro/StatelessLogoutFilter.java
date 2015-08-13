package com.yzqc.support.shiro;

import com.yzqc.support.shiro.CookieUtil;
import com.yzqc.support.shiro.SessionCacheContants;
import com.yzqc.support.shiro.SessionCacheManager;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2015/7/23.
 */
public class StatelessLogoutFilter extends LogoutFilter {
    private static final Logger log = LoggerFactory.getLogger(StatelessLogoutFilter.class);

    private SessionCacheManager sessionCacheManager;

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        try {
            subject.logout();

            doLogout((HttpServletRequest) request, (HttpServletResponse) response);
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }
        issueRedirect(request, response, redirectUrl);
        return false;
    }

    public void doLogout(HttpServletRequest request, HttpServletResponse response) {
        sessionCacheManager.removeSessionCacheAttribute(SessionCacheContants.USER_INFO_LOGIN,
                CookieUtil.findCookieValue(request.getCookies(), CookieConstants.USERNAME));
        response.addCookie(CookieUtil.expireCookie(CookieConstants.TOKEN));
        response.addCookie(CookieUtil.expireCookie(CookieConstants.USERNAME));
        response.addCookie(CookieUtil.expireCookie(CookieConstants.NICKNAME));
    }

    public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
        this.sessionCacheManager = sessionCacheManager;
    }
}
