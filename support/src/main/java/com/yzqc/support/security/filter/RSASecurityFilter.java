package com.yzqc.support.security.filter;

import com.yzqc.support.security.CryptUtils;
import com.yzqc.support.security.SecurityException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/8/11.
 */
public class RSASecurityFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RSASecurityFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String appCode = request.getHeader("appcode");
        String sign = request.getHeader("sign");
        String ts = request.getHeader("ts");
        String bodyEncryption = request.getHeader("bodyEncryption");
        String url = request.getRequestURL().toString();

        if (StringUtils.isBlank(appCode) || StringUtils.isBlank(ts) || StringUtils.isEmpty(sign)) {
            forbiddenReq(servletResponse);
            return;
        }

        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIto_Y$4VF2rIxvH4On7BDIYzzxitgbgLdQps2ka15e6U56blJqeHdbs82f0v3aCc4U1IW2EtG0uDO_uWrFVNP1QEon5PkGOTBFzjlum2wlTKVgYZ4eb8qDDr8_DPEXu6rPda$qvd3IU3YUqJTwCFGnoneTRpDDdK_0q9gKQwvTTAgMBAAECgYBtDqDtAUHeVLaOUPp9j8RkfEwB7SaAknbHdifHnRvysewgM404t2CYdZykQi5VQw$jsUCJMDsE_fVY7Jk$CG3$YgY$$K0lJQ8IvwUHw884wx7QAge37zBqDkWDetgy4eXUc3PtpTuZO1yTAqNPu_Jo_2O15iu0tjvopurjKNAVwQJBANW47CvX1A6TJnKmifVWAAeZfOT94TOgFZoT92XOy8iZlg9YInwFtJKPwZ2c56qLRpkz3UlkNSOGfZ24394aJIsCQQCm_NWsbF7oQzmrNeGrA2zzeyXq2bYcs30WefECq3dXyZ_n4ZAwhZ8dVC74YLCz9lQEOwFkFwX2Pi5YbmZUXlHZAkA7HSX9znmx$lOFHLlF6Z6ie3rHH829aMbw0hr4xuUovAp8fgUzxqQ2cZq9DJwrWNCTKXxIg3YLOHTgPjV1ikeXAkB5_238MpCED9kqBIuCbVZD7Eyfb79k6J0StrQlxy_Bq0RwHJNDX9wHiWFnhPyT_fsTvCgf_9ITPQqPANkx$lbBAkBI6qr1UGqikwtz7bWyLXmMYylr7CgjxmKNdMlYIaiCDEcFOH$eojGkOGY3tpG0goQ0Hx9JTt6Y0lNi_2jOMosF";

        boolean pass = false;

        try {
            String signStr = createSign(appCode, url, ts);
            pass = signStr.equals(new String(CryptUtils.decryptByPrivateKey(CryptUtils.decryptBASE64(sign), privateKey)));
        } catch (SecurityException e) {
            logger.error("解密出错!!", e);
        }
        if (pass) {
            if (StringUtils.equalsIgnoreCase("true", bodyEncryption)) {
                filterChain.doFilter(new RSARequestWrapper(request, privateKey), servletResponse);
            } else {
                filterChain.doFilter(request, servletResponse);
            }

        } else {
            forbiddenReq(servletResponse);
        }

    }

    private void forbiddenReq(ServletResponse servletResponse) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setStatus(HttpStatus.SC_FORBIDDEN);
        response.getWriter().print("Forbbiden");
        response.getWriter().flush();
    }

    private String createSign(String appcode, String url, String ts) {
        return appcode + "\r\n" + url + "\r\n" + ts;
    }


    @Override
    public void destroy() {

    }

    private class RSARequestWrapper extends HttpServletRequestWrapper {

        private final String privatekey;

        public RSARequestWrapper(HttpServletRequest request, String privateKey) {
            super(request);
            this.privatekey = privateKey;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            ServletInputStream in = super.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len;
            while ((len = in.read(data)) != -1) {
                out.write(data, 0, len);
            }
            String inStr = new String(out.toByteArray());
            byte[] decByBase64 = CryptUtils.decryptBASE64(inStr);

            try {
                byte[] decryptData = CryptUtils.decryptByPrivateKey(decByBase64, privatekey);
                return new CustomServletInputStream(new ByteArrayInputStream(decryptData));
            } catch (SecurityException e) {
                throw new IOException("数据解密出错！！");
            }
        }
    }

    private class CustomServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream in;

        public CustomServletInputStream(ByteArrayInputStream in) {
            this.in = in;
        }

        @Override
        public int read() throws IOException {
            return in.read();
        }
    }
}
