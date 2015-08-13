package com.yzqc.common.aspect;

import com.yzqc.support.shiro.Account;
import com.yzqc.support.shiro.SessionCacheManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uap.web.example.entity.MgrUser;
import uap.web.example.service.account.AccountService;

/**
 * Created by Administrator on 2015/7/22.
 */
public class AccountAspect {

    private static final Logger logger = LoggerFactory.getLogger(AccountAspect.class);

    private SessionCacheManager cacheManager;

    private AccountService accountService;

    public void process(ProceedingJoinPoint joinPoint, Account operType) throws Throwable {
        try {
            logger.info("aspect method [{}] of object [{}] by params [{}]", joinPoint.getSignature().getName(),
                    joinPoint.getTarget(), joinPoint.getArgs());
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0) {
                logger.info("args is empty,return");
                return;
            }
            Object userObj = args[0];

            switch (operType.operationType()) {
                case UPDATE:
                    doSaveUser(userObj);
                    break;
                case REMOVE:
                    doRemoveUser(userObj);
                    break;
                default:
                    logger.info("do nothing about {}", operType.operationType());

            }
        } catch (RuntimeException e) {
            logger.error("invoke AccountAspect failed", e);
        }
        joinPoint.proceed();

    }

    private void doRemoveUser(Object userObj) {
        if (userObj instanceof Long) {
            MgrUser user = accountService.getUser((Long) userObj);
            if (user != null) {
                cacheManager.disCacheUser(user.getLoginName());
            }
        } else {
            logger.warn("Can not process remove cache with param[{}],Please put User id to the first param!!!",
                    userObj);
        }
    }

    private void doSaveUser(Object userObj) {
        if (userObj instanceof MgrUser) {
            MgrUser user = (MgrUser) userObj;
            cacheManager.refreshUser(user.getLoginName(), user);
        } else {
            logger.warn("Can not process update cache with param[{}],Please put User instance to the first param!!!",
                    userObj);
        }
    }

    public void setCacheManager(SessionCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
