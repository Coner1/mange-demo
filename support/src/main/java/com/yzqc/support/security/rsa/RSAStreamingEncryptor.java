package com.yzqc.support.security.rsa;


import com.yzqc.support.security.Encryptor;
import com.yzqc.support.security.StreamingEncryptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 实现了使用 RSA 公钥对数据进行分段加密；
 *
 * @author haiq
 */
public class RSAStreamingEncryptor implements StreamingEncryptor, Encryptor {

    private RSAEncryptor baseEncryptor;

    private RSAKeySize keySize;

    public RSAStreamingEncryptor(int keySize, RSAKeyType keyType, byte[] key)
            throws SecurityException {
        this.baseEncryptor = new RSAEncryptor(keyType, key);
        this.keySize = new RSAKeySize(keySize);
    }

    @Override
    public byte[] encrypt(byte[] data) throws SecurityException {
        return encrypt(data, 0, data.length);
    }

    @Override
    public byte[] encrypt(byte[] data, int offset) throws SecurityException {
        return encrypt(data, offset, data.length - offset);
    }

    @Override
    public byte[] encrypt(byte[] data, int offset, int length)
            throws SecurityException {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data, offset,
                    length);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            encrypt(in, out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new SecurityException(e.getMessage(), e);
        }
    }

    @Override
    public void encrypt(InputStream in, OutputStream out) throws IOException,
            SecurityException {
        int len;
        // 按能够进行加密的数据的最大长度创建缓冲区；
        byte[] buffer = new byte[keySize.getMaxEncryptingDataSize()];
        while ((len = in.read(buffer)) > 0) {
            out.write(baseEncryptor.encrypt(buffer, 0, len));
        }
    }

}
