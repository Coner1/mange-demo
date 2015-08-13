package com.yzqc.support.security.client;

import com.yzqc.support.security.ApplicationTokenConsts;
import com.yzqc.support.security.CryptUtils;
import com.yzqc.support.security.SecurityException;
import com.yzqc.support.security.SecurityProperties;

import java.io.UnsupportedEncodingException;

/**
 * ApplicationRequestSigner 实现用 Token 对请求信息进行签名；
 *
 * @author haiq
 */
public class ApplicationRequestSigner {

    private ApplicationClientToken clientToken;

    public ApplicationRequestSigner(ApplicationClientToken clientToken) {
        this.clientToken = clientToken;
    }

    /**
     * 对请求信息进行签名；
     *
     * @param requestPath 请求的服务路径；
     * @return 签名的 Base64 编码文本；
     * @throws SecurityException
     */
    public String sign(String requestPath) throws SecurityException {
        SecurityProperties props = new SecurityProperties();
        props.setAppCode(clientToken.code());
        props.setRequestPath(requestPath);

        try {
            byte[] origData = props.toString().getBytes(ApplicationTokenConsts.CHARSET);
            byte[] sign = CryptUtils.sign(origData, clientToken.secretKey());
            return CryptUtils.encryptBASE64(sign);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e.getMessage(), e);
        }

    }
}
