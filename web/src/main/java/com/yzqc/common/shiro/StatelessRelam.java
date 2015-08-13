package com.yzqc.common.shiro;

import com.yzqc.support.shiro.AbstractStatelessRealm;
import com.yzqc.support.shiro.SessionCacheManager;
import com.yzqc.support.shiro.StatelessToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import uap.web.example.entity.MgrUser;
import uap.web.example.service.account.AccountService;

/**
 * Created by Administrator on 2015/8/6.
 */
public class StatelessRelam extends AbstractStatelessRealm {

    private SessionCacheManager sessionCacheManager;

    private AccountService accountService;


    //TODO 完善业务逻辑
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return super.doGetAuthorizationInfo(principalCollection);
    }

    @Override
    protected StatelessToken genStatelessToken(String username) throws Throwable {
        MgrUser mgrUser = sessionCacheManager.getCurUser(username);
        if (mgrUser == null) {
            mgrUser = accountService.findUserByLoginName(username);
            sessionCacheManager.cacheUser(username, mgrUser);
        }
        return new StatelessToken(mgrUser.getLoginName(), mgrUser.getPassword());
    }


    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
        this.sessionCacheManager = sessionCacheManager;
    }
}
