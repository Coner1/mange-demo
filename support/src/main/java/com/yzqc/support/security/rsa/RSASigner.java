package com.yzqc.support.security.rsa;

import com.yzqc.support.security.Signer;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

public class RSASigner extends RSACryptor implements Signer {

    public RSASigner(byte[] privateKey) throws SecurityException {
        super(RSAKeyType.PRIVATE, privateKey);
    }

    @Override
    public byte[] sign(byte[] data) throws SecurityException {
        try {
            // 用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign((PrivateKey) getKey());
            signature.update(data);

            return signature.sign();
        } catch (InvalidKeyException e) {
            throw new SecurityException(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e.getMessage(), e);
        } catch (SignatureException e) {
            throw new SecurityException(e.getMessage(), e);
        }
    }

}
