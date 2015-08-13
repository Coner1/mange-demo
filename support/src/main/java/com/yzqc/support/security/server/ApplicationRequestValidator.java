package com.yzqc.support.security.server;


import com.yzqc.support.security.ApplicationTokenConsts;
import com.yzqc.support.security.CryptUtils;
import com.yzqc.support.security.SecurityException;
import com.yzqc.support.security.SecurityProperties;

import java.io.UnsupportedEncodingException;

/**
 * ApplicationRequestValidator 校验请求的签名是否合法；
 *
 * @author haiq
 */
public class ApplicationRequestValidator {

    private String appcode;

    private byte[] pubkey;

    /**
     * 创建
     *
     * @param appcode
     * @param pubkey
     */
    public ApplicationRequestValidator(String appcode, String pubkey) {
        this.appcode = appcode;
        this.pubkey = CryptUtils.decryptBASE64(pubkey);
    }

    public boolean verify(String requestPath, String sign) throws SecurityException {
        SecurityProperties props = new SecurityProperties();
        props.setAppCode(appcode);
        props.setRequestPath(requestPath);

        try {
            byte[] origData = props.toString().getBytes(ApplicationTokenConsts.CHARSET);
            byte[] bytesSign = CryptUtils.decryptBASE64(sign);
            return CryptUtils.verify(origData, pubkey, bytesSign);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e.getMessage(), e);
        }
    }
}
