package com.yzqc.support.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/22.
 */
public class StatelessToken implements AuthenticationToken {

    private String uname;

    private String token;

    public StatelessToken() {
    }


    public StatelessToken(String uname, String token) {
        this.uname = uname;
        this.token = token;
    }


    @Override
    public Object getPrincipal() {
        return uname;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public String toString() {
        return "StatelessToken{" +
                "uname='" + uname + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
