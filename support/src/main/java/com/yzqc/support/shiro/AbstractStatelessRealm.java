package com.yzqc.support.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2015/7/22.
 */
public abstract class AbstractStatelessRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(AbstractStatelessRealm.class);

    /**
     * 验证是否支持token类型
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken;
    }

    /**
     * 权限相关
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 身份认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws
            AuthenticationException {

        String uname = (String) authenticationToken.getPrincipal();
        try {
            StatelessToken user = genStatelessToken(uname);
            if (user == null) {
                logger.error("User [{}] not exists in System", uname);
                throw new AuthenticationException("User " + uname + " not exists in System");
            }
            return new SimpleAuthenticationInfo(user.getPrincipal(), user.getCredentials(), getName());
        } catch (Throwable e) {
            logger.error("Error occured when find User AuthenticationInfo,please check method genStatelessToken", e);
            throw new AuthenticationException(e);
        }
    }

    protected abstract StatelessToken genStatelessToken(String username) throws Throwable;


}
