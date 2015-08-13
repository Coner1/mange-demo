package com.yzqc.support.security;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Iterator;

public abstract class AuthenticationUtil {

    private AuthenticationUtil() {
    }

    /**
     * 检测当前是否已经认证成功；
     *
     * @return
     */
    public static boolean isAuthenticated() {
        Authentication existingAuth = SecurityContextHolder.getContext()
                .getAuthentication();

        return existingAuth != null && existingAuth.isAuthenticated();
    }

    /**
     * 检测当前是否已经以第三方应用Token方式认证成功；
     *
     * @return
     */
    public static boolean isAuthenticatedByApplication() {
        Authentication existingAuth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) existingAuth.getAuthorities();
        Iterator<GrantedAuthority> authoritiesIterator = grantedAuthorities.iterator();
        while (authoritiesIterator.hasNext()) {
            GrantedAuthority authority = authoritiesIterator.next();
            if (authority instanceof GrantedApplicationAuthority) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取已经认证的应用权限；如果没有，则返回 null;
     *
     * @return
     */
    public static GrantedApplicationAuthority getGrantedApplicationAuthority() {
        Authentication existingAuth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) existingAuth.getAuthorities();
        Iterator<GrantedAuthority> authoritiesIterator = grantedAuthorities.iterator();
        while (authoritiesIterator.hasNext()) {
            GrantedAuthority authority = authoritiesIterator.next();
            if (authority instanceof GrantedApplicationAuthority) {
                return (GrantedApplicationAuthority) authority;
            }
        }
        return null;
    }

    /**
     * 检测当前是否已经以浏览器登录的用户名密码方式认证成功；
     *
     * @return
     */
    public static boolean isAuthenticatedByUserName() {
        Authentication existingAuth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (existingAuth != null
                && existingAuth instanceof UsernamePasswordAuthenticationToken
                && existingAuth.isAuthenticated()) {
            return true;
        }
        return false;
    }

}
