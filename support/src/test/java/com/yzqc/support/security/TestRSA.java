package com.yzqc.support.security;

import com.yzqc.support.security.rsa.RSAKeyPair;

import java.io.IOException;

/**
 * Created by Administrator on 2015/8/11.
 */
public class TestRSA {

    public static void main(String[] args) throws IOException, SecurityException {

        String pubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLaP2PuFRdqyMbx$Dp$wQyGM88YrYG4C3UKbNpGteXulOem5Sanh3W7PNn9L92gnOFNSFthLRtLgzv7lqxVTT9UBKJ$T5BjkwRc45bptsJUylYGGeHm_Kgw6_PwzxF7uqz3Wvqr3dyFN2FKiU8AhRp6J3k0aQw3Sv9KvYCkML00wIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIto_Y$4VF2rIxvH4On7BDIYzzxitgbgLdQps2ka15e6U56blJqeHdbs82f0v3aCc4U1IW2EtG0uDO_uWrFVNP1QEon5PkGOTBFzjlum2wlTKVgYZ4eb8qDDr8_DPEXu6rPda$qvd3IU3YUqJTwCFGnoneTRpDDdK_0q9gKQwvTTAgMBAAECgYBtDqDtAUHeVLaOUPp9j8RkfEwB7SaAknbHdifHnRvysewgM404t2CYdZykQi5VQw$jsUCJMDsE_fVY7Jk$CG3$YgY$$K0lJQ8IvwUHw884wx7QAge37zBqDkWDetgy4eXUc3PtpTuZO1yTAqNPu_Jo_2O15iu0tjvopurjKNAVwQJBANW47CvX1A6TJnKmifVWAAeZfOT94TOgFZoT92XOy8iZlg9YInwFtJKPwZ2c56qLRpkz3UlkNSOGfZ24394aJIsCQQCm_NWsbF7oQzmrNeGrA2zzeyXq2bYcs30WefECq3dXyZ_n4ZAwhZ8dVC74YLCz9lQEOwFkFwX2Pi5YbmZUXlHZAkA7HSX9znmx$lOFHLlF6Z6ie3rHH829aMbw0hr4xuUovAp8fgUzxqQ2cZq9DJwrWNCTKXxIg3YLOHTgPjV1ikeXAkB5_238MpCED9kqBIuCbVZD7Eyfb79k6J0StrQlxy_Bq0RwHJNDX9wHiWFnhPyT_fsTvCgf_9ITPQqPANkx$lbBAkBI6qr1UGqikwtz7bWyLXmMYylr7CgjxmKNdMlYIaiCDEcFOH$eojGkOGY3tpG0goQ0Hx9JTt6Y0lNi_2jOMosF";

        RSAKeyPair keyPair = new RSAKeyPair(pubkey, privateKey);
        System.out.println("====privatekey===");
        System.out.println(keyPair.getPrivateKey());
        System.out.println("====publickey===");
        System.out.println(keyPair.getPublicKey());


        Long ts = System.currentTimeMillis();
        System.out.println(ts);
        String appcode = "test";
        String url = "http://localhost:8080/CRM/api/demo/query/zxs";

        String signStr = appcode + "\r\n" + url + "\r\n" + ts;

        System.out.println(CryptUtils.encryptBASE64(CryptUtils.encryptByPublicKey(signStr.getBytes(), CryptUtils.decryptBASE64
                (keyPair.getPublicKey()))));


        String body = "{\n" +
                "      \"pkMbeGroupmg\": null,\n" +
                "      \"code\": \"123\",\n" +
                "      \"creater\": 1,\n" +
                "      \"description\": \"description99\",\n" +
                "      \"dr\": 0,\n" +
                "      \"name\": \"name99\",\n" +
                "      \"vstatus\": 1\n" +
                "    }";
        String bodyEnc = CryptUtils.encryptBASE64(CryptUtils.encryptByPublicKey(body.getBytes(), pubkey));
        System.out.println("====bodyenc");
        System.out.println(bodyEnc);
        System.out.println("====body");
        System.out.println(new String(CryptUtils.decryptByPrivateKey(CryptUtils.decryptBASE64(bodyEnc), privateKey)));
    }

}
