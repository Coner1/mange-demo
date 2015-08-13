package com.yzqc.support.security.rsa;


import com.yzqc.support.security.Decryptor;
import com.yzqc.support.security.StreamingDecryptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 实现了使用 RSA 私钥对数据进行分段解密；
 *
 * @author haiq
 */
public class RSAStreamingDecryptor implements StreamingDecryptor, Decryptor {

    private RSADecryptor baseDecryptor;

    private RSAKeySize keySize;

    public RSAStreamingDecryptor(int keySize, RSAKeyType keyType, byte[] key)
            throws SecurityException {
        this.baseDecryptor = new RSADecryptor(keyType, key);
        this.keySize = new RSAKeySize(keySize);
    }

    @Override
    public byte[] decrypt(byte[] data) throws SecurityException {
        return decrypt(data, 0, data.length);
    }

    @Override
    public byte[] decrypt(byte[] data, int offset) throws SecurityException {
        return decrypt(data, offset, data.length - offset);
    }

    @Override
    public byte[] decrypt(byte[] data, int offset, int length)
            throws SecurityException {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data, offset, length);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            decrypt(in, out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new SecurityException(e.getMessage(), e);
        }
    }

    @Override
    public void decrypt(InputStream in, OutputStream out) throws IOException, SecurityException {
        int len;
        //以每个加密数据片的长度创建缓冲区；
        byte[] buffer = new byte[keySize.getEncryptedDataSize()];
        while ((len = in.read(buffer)) > 0) {
            out.write(baseDecryptor.decrypt(buffer, 0, len));
        }
    }


}
