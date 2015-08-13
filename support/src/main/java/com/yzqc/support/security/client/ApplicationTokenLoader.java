package com.yzqc.support.security.client;

import com.yzqc.support.security.ApplicationTokenConsts;
import com.yzqc.support.security.CryptUtils;
import com.yzqc.support.security.SecurityException;
import com.yzqc.support.security.SecurityProperties;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 应用令牌加载器；
 *
 * @author haiq
 */
public class ApplicationTokenLoader {

    private static final Object mutex = new Object();

    private static volatile ApplicationClientToken token;

    private static final ApplicationClientToken NULL_TOKEN = new NullApplicationClientTokenEntry();

    private ApplicationTokenLoader() {
    }

    /**
     * 获取当前应用中的令牌；
     * <p/>
     * 首次调用时从
     *
     * @return
     */
    public static ApplicationClientToken get() {
        if (token == null) {
            synchronized (mutex) {
                if (token == null) {
                    token = load();
                    if (token == null) {
                        //避免加载出错后仍然多次重复加载，避免产生性能瓶颈；
                        token = NULL_TOKEN;
                    }
                }
            }
        }
        if (token == NULL_TOKEN) {
            return null;
        }
        return token;
    }

    private static ApplicationClientToken load() {
        //从类路径中加载；
        InputStream in = ApplicationTokenLoader.class.getResourceAsStream("/" + ApplicationTokenConsts.TOKEN_FILE_NAME);

        try {
            return resolve(in);
        } catch (Exception e) {
            System.out.println("加载应用令牌时发生错误！--异常类型：" + e.getClass().getName() + "; 异常信息：" + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static ApplicationClientToken resolve(InputStream in) throws IOException, SecurityException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try {
            if (in == null) {
                return null;
            }
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) > 0) {
                bo.write(buf, 0, len);
            }
            byte[] bytesToken = bo.toByteArray();
            String strToken = new String(bytesToken, "utf-8");

            return resolve(strToken);
        } finally {
            try {
                bo.close();
            } catch (IOException e) {
            }
        }
    }

    public static ApplicationClientToken resolve(String tokenString) throws IOException, SecurityException {
        SecurityProperties securtyProps = SecurityProperties.loadFromString(tokenString);
        ApplicationClientTokenEntry token = new ApplicationClientTokenEntry();
        if (securtyProps.getKey() == null || securtyProps.getToken() == null) {
            return null;
        }
        token.securityKey = securtyProps.getKey();
        byte[] encrptInfo = CryptUtils.decryptBASE64(securtyProps.getToken());
        byte[] appInfo = CryptUtils.decryptByPrivateKey(encrptInfo, token.securityKey);

        SecurityProperties appInfoProps = SecurityProperties.loadFromString(new String(appInfo, ApplicationTokenConsts.CHARSET));
        if (appInfoProps.getAppCode() == null || appInfoProps.getTs() == null) {
            return null;
        }
        token.code = appInfoProps.getAppCode();
        token.ts = Long.parseLong(appInfoProps.getTs());

        return token;
    }


    private static class ApplicationClientTokenEntry implements ApplicationClientToken {

        private String securityKey;

        private String code;

        private long ts;

        @Override
        public String secretKey() {
            return securityKey;
        }

        @Override
        public String code() {
            return code;
        }

        @Override
        public long ts() {
            return ts;
        }

    }

    private static class NullApplicationClientTokenEntry implements ApplicationClientToken {

        @Override
        public String secretKey() {
            return null;
        }

        @Override
        public String code() {
            return null;
        }

        @Override
        public long ts() {
            return 0;
        }

    }
}
