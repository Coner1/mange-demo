package com.yzqc.support.shiro;

import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2015/7/23.
 */
public class CookieUtil {

    private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);


    /**
     * 创建cookie，不过期，js不可操作。base64编码
     *
     * @param key
     * @param value
     * @return
     */
    public static Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, Base64.encodeToString(value.getBytes(Charset.forName("UTF-8"))));
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        return cookie;
    }


    /**
     * 查找cookie内容
     *
     * @param cookies
     * @param key
     * @return
     */
    public static String findCookieValue(Cookie[] cookies, String key) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return Base64.decodeToString(cookie.getValue().getBytes(Charset.forName("UTF-8")));
                }
            }
        }
        return null;

    }


    /**
     * 将cookie过期
     *
     * @param key
     * @return
     */
    public static Cookie expireCookie(String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

}
