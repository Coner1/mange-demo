package com.yzqc.support.shiro;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2015/7/22.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Account {

    public enum OperationType {
        UPDATE, REMOVE;
    }

    /**
     * 操作类型
     *
     * @return
     */
    OperationType operationType();

}
