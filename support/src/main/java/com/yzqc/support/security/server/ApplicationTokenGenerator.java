package com.yzqc.support.security.server;

import com.yzqc.support.security.*;
import com.yzqc.support.security.SecurityException;
import com.yzqc.support.security.rsa.RSAKeyPair;

import java.io.UnsupportedEncodingException;


public class ApplicationTokenGenerator implements TokenGenerator {

    private static final int KEY_SIZE = 1024;

    @Override
    public ApplicationToken createToken(String appCode, String external) throws SecurityException {
        long ts = System.currentTimeMillis();
        String origAppInfo = generateApplicationInfo(appCode, ts, external);
        RSAKeyPair keyPair = CryptUtils.generateKey(KEY_SIZE);
        byte[] appInfoBytes;
        try {
            appInfoBytes = origAppInfo.getBytes(ApplicationTokenConsts.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e.getMessage(), e);
        }

        byte[] appEncrptedInfo = CryptUtils.encryptByPublicKey(appInfoBytes, keyPair.getPublicKey());
        String appInfo = CryptUtils.encryptBASE64(appEncrptedInfo);

        String clientToken = generateClientToken(keyPair.getPrivateKey(), appInfo);
        return new ApplicationTokenEntry(appCode, ts, clientToken, keyPair.getPublicKey());
    }


    private String generateClientToken(String privateKey, String token) {
        SecurityProperties props = new SecurityProperties();
        props.setKey(privateKey);
        props.setToken(token);
        return props.toString();
    }

    private String generateApplicationInfo(String code, long ts, String external) {
        SecurityProperties props = new SecurityProperties();
        props.setAppCode(code);
        props.setTs(ts + "");
        props.setExternal(external);
        return props.toString();
    }

    private static class ApplicationTokenEntry implements ApplicationToken {

        private final String code;

        private final long ts;

        private final String clientToken;

        private final String serverCredential;

        public ApplicationTokenEntry(String code, long ts, String clientToken,
                                     String serverCredential) {
            this.code = code;
            this.ts = ts;
            this.clientToken = clientToken;
            this.serverCredential = serverCredential;
        }

        @Override
        public String code() {
            return code;
        }

        @Override
        public long ts() {
            return ts;
        }

        @Override
        public String clientToken() {
            return clientToken;
        }

        @Override
        public String serverCredential() {
            return serverCredential;
        }

    }
}
