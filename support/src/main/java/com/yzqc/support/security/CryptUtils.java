package com.yzqc.support.security;

import com.yzqc.support.security.rsa.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class CryptUtils {

    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    public static final String KEY_ALGORTHM = "RSA";//
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    public static final String PUBLIC_KEY = "RSAPublicKey";// 公钥
    public static final String PRIVATE_KEY = "RSAPrivateKey";// 私钥

    public static final int DEFAULT_RSA_KEY_SIZE = 1024;// 生成默认的RSA密钥长度；

    /**
     * 以默认的 Key 长度生成一个密钥；
     *
     * @return
     * @throws SecurityException
     */
    public static RSAKeyPair generateKey() throws SecurityException {
        return generateKey(DEFAULT_RSA_KEY_SIZE);
    }

    /**
     * 使用默认长度生成一个密钥；
     * <p/>
     * 注：密钥的长度决定了能够加密的数据长度：数据长度 = 密钥长度/8 - 11；对于 1024 的密钥，只能对最长 117 的数据进行加密;
     *
     * @param keySize
     * @return
     * @throws SecurityException
     */
    public static RSAKeyPair generateKey(int keySize) throws SecurityException {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e.getMessage(), e);
        }
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        String pubKey = Base64Utils.toBase64(publicKey.getEncoded());
        String privKey = Base64Utils.toBase64(privateKey.getEncoded());

        return new RSAKeyPair(pubKey, privKey);
    }

    /**
     * 用私钥加密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws SecurityException
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key)
            throws SecurityException {
        // 解密密钥
        byte[] keyBytes = decryptBASE64(key);
        return encryptByPrivateKey(data, 0, data.length, keyBytes);
    }

    /**
     * 用私钥加密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws SecurityException
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key)
            throws SecurityException {
        return encryptByPrivateKey(data, 0, data.length, key);
    }

    /**
     * 用私钥加密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws SecurityException
     */
    public static byte[] encryptByPrivateKey(byte[] data, int offset,
                                             int length, byte[] key) throws SecurityException {

        return encryptByPrivateKey(data, offset, length, key,
                DEFAULT_RSA_KEY_SIZE);

        // // 取私钥
        // PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new
        // PKCS8EncodedKeySpec(key);
        // try {
        // KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        // Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //
        // // 对数据加密
        // Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        // cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        //
        // return cipher.doFinal(data, startIndex, length);
        // } catch (Exception e) {
        // if (e instanceof RuntimeException) {
        // throw (RuntimeException) e;
        // }
        // throw new SecurityException(e.getMessage(), e);
        // }
    }

    /**
     * 用私钥加密
     *
     * @param data    要加密的原始数据；
     * @param offset  起始位置；
     * @param length  数据长度；
     * @param key     密钥
     * @param keySize 生成密钥时指定的密钥长度；
     * @return
     * @throws SecurityException
     */
    public static byte[] encryptByPrivateKey(byte[] data, int offset,
                                             int length, byte[] key, int keySize) throws SecurityException {

        RSAStreamingEncryptor encryptor = new RSAStreamingEncryptor(keySize,
                RSAKeyType.PRIVATE, key);
        return encryptor.encrypt(data, offset, length);
    }

    /**
     * 用公钥加密
     *
     * @param data 加密数据
     * @param key  base64格式的密钥文本
     * @return
     * @throws SecurityException
     */
    public static byte[] encryptByPublicKey(byte[] data, String key)
            throws SecurityException {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        return encryptByPublicKey(data, 0, data.length, keyBytes);
    }

    /**
     * 用公钥加密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws SecurityException
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key)
            throws SecurityException {
        return encryptByPublicKey(data, 0, data.length, key);
    }

    /**
     * 用公钥加密
     *
     * @param data
     * @param offset
     * @param length
     * @param key
     * @return
     * @throws SecurityException
     */
    public static byte[] encryptByPublicKey(byte[] data, int offset,
                                            int length, byte[] key) throws SecurityException {

        return encryptByPublicKey(data, offset, length, key,
                DEFAULT_RSA_KEY_SIZE);

        // // 取公钥
        // X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        // try {
        // KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        // Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        //
        // // 对数据解密
        // Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        // cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        //
        // return cipher.doFinal(data, startIndex, length);
        // } catch (Exception e) {
        // if (e instanceof RuntimeException) {
        // throw (RuntimeException) e;
        // }
        // throw new SecurityException(e.getMessage(), e);
        // }
    }

    /**
     * 用公钥加密
     *
     * @param data
     * @param offset
     * @param length
     * @param key
     * @param keySize
     * @return
     * @throws SecurityException
     */
    public static byte[] encryptByPublicKey(byte[] data, int offset,
                                            int length, byte[] key, int keySize) throws SecurityException {
        RSAStreamingEncryptor encryptor = new RSAStreamingEncryptor(keySize,
                RSAKeyType.PUBLIC, key);
        return encryptor.encrypt(data, offset, length);
    }

    /**
     * 用私钥解密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws SecurityException
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key)
            throws SecurityException {
        // 对私钥解密
        byte[] keyBytes = decryptBASE64(key);
        return decryptByPrivateKey(data, keyBytes);

//		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
//				keyBytes);
//		try {
//			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
//			Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
//			// 对数据解密
//			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//			cipher.init(Cipher.DECRYPT_MODE, privateKey);
//
//			return cipher.doFinal(data);
//		} catch (Exception e) {
//			if (e instanceof RuntimeException) {
//				throw (RuntimeException) e;
//			}
//			throw new SecurityException(e.getMessage(), e);
//		}
    }

    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws SecurityException {
        return decryptByPrivateKey(data, 0, data.length, key, DEFAULT_RSA_KEY_SIZE);
    }

    public static byte[] decryptByPrivateKey(byte[] data, int offset,
                                             int length, byte[] key) throws SecurityException {
        return decryptByPrivateKey(data, offset, length, key, DEFAULT_RSA_KEY_SIZE);
    }

    public static byte[] decryptByPrivateKey(byte[] data, int offset,
                                             int length, byte[] key, int keySize)
            throws SecurityException {
        RSAStreamingDecryptor encryptor = new RSAStreamingDecryptor(keySize,
                RSAKeyType.PRIVATE, key);
        return encryptor.decrypt(data, offset, length);
    }

    /**
     * 用公钥解密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return
     * @throws SecurityException
     */
    public static byte[] decryptByPublicKey(byte[] data, String key)
            throws SecurityException {
        // 对私钥解密
        byte[] keyBytes = decryptBASE64(key);

        return decryptByPublicKey(data, keyBytes);

//		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
//		try {
//			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
//			Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
//
//			// 对数据解密
//			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//			cipher.init(Cipher.DECRYPT_MODE, publicKey);
//
//			return cipher.doFinal(data);
//		} catch (Exception e) {
//			if (e instanceof RuntimeException) {
//				throw (RuntimeException) e;
//			}
//			throw new SecurityException(e.getMessage(), e);
//		}
    }

    public static byte[] decryptByPublicKey(byte[] data, byte[] key)
            throws SecurityException {
        return decryptByPublicKey(data, 0, data.length, key, DEFAULT_RSA_KEY_SIZE);
    }

    public static byte[] decryptByPublicKey(byte[] data, int offset,
                                            int length, byte[] key)
            throws SecurityException {
        return decryptByPublicKey(data, offset, length, key, DEFAULT_RSA_KEY_SIZE);
    }

    public static byte[] decryptByPublicKey(byte[] data, int offset,
                                            int length, byte[] key, int keySize)
            throws SecurityException {
        RSAStreamingDecryptor encryptor = new RSAStreamingDecryptor(keySize,
                RSAKeyType.PUBLIC, key);
        return encryptor.decrypt(data, offset, length);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws SecurityException
     */
    public static byte[] sign(byte[] data, String privateKey)
            throws SecurityException {
        // 解密私钥
        byte[] keyBytes = decryptBASE64(privateKey);

        return sign(data, keyBytes);

//		// 构造PKCS8EncodedKeySpec对象
//		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
//				keyBytes);
//		try {
//			// 指定加密算法
//			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
//			// 取私钥匙对象
//			PrivateKey privateKey2 = keyFactory
//					.generatePrivate(pkcs8EncodedKeySpec);
//			// 用私钥对信息生成数字签名
//			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
//			signature.initSign(privateKey2);
//			signature.update(data);
//
//			return signature.sign();
//		} catch (Exception e) {
//			if (e instanceof RuntimeException) {
//				throw (RuntimeException) e;
//			}
//			throw new SecurityException(e.getMessage(), e);
//		}
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws SecurityException
     */
    public static byte[] sign(byte[] data, byte[] privateKey)
            throws SecurityException {
        RSASigner signer = new RSASigner(privateKey);
        return signer.sign(data);
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey Base64编码的公钥
     * @param sign      数字签名
     * @return
     * @throws SecurityException
     */
    public static boolean verify(byte[] data, String publicKey, byte[] sign)
            throws SecurityException {
        // 解密公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        return verify(data, keyBytes, sign);
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return
     * @throws SecurityException
     */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign)
            throws SecurityException {

        RSAVerifier verifier = new RSAVerifier(publicKey);
        return verifier.verify(data, sign);

//		// 构造X509EncodedKeySpec对象
//		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
//				publicKey);
//		try {
//			// 指定加密算法
//			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
//			// 取公钥匙对象
//			PublicKey publicKey2 = keyFactory
//					.generatePublic(x509EncodedKeySpec);
//
//			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
//			signature.initVerify(publicKey2);
//			signature.update(data);
//			// 验证签名是否正常
//			return signature.verify(sign);
//		} catch (Exception e) {
//			if (e instanceof RuntimeException) {
//				throw (RuntimeException) e;
//			}
//			throw new SecurityException(e.getMessage(), e);
//		}
    }


    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws SecurityException
     */
    public static byte[] decryptBASE64(String key) {
        return Base64Utils.fromBase64(key);
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     */
    public static String encryptBASE64(byte[] key) {
        return Base64Utils.toBase64(key);
    }

    /**
     * MD5加密
     *
     * @param data
     * @return
     * @throws SecurityException
     */
    public static byte[] encryptMD5(byte[] data) throws SecurityException {
        try {
            MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
            md5.update(data);
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e.getMessage(), e);
        }
    }

    /**
     * SHA加密
     *
     * @param data
     * @return
     * @throws SecurityException
     */
    public static byte[] encryptSHA(byte[] data) throws SecurityException {
        try {
            MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
            sha.update(data);
            return sha.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e.getMessage(), e);
        }
    }
}
