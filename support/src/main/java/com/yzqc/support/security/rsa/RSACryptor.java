package com.yzqc.support.security.rsa;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 公钥密码器；
 * 
 * @author haiq
 *
 */
public class RSACryptor {
	
	public static final String RSA_ALGORTHM = "RSA";
	
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	
	private static final Object mutex = new Object();

	private static KeyFactory keyFactory;

	private Key key;

	

	/**
	 * 创建 RSACryptor 实例；
	 * 
	 * @param keyType
	 *            传入参数 key 的密钥类型，枚举值：公钥或者私钥；
	 * @param key
	 *            密钥
	 * @throws SecurityException
	 */
	public RSACryptor(RSAKeyType keyType, byte[] key)
			throws SecurityException {
		initializeKey(keyType, key);
	}

	protected static KeyFactory getKeyFactory() throws NoSuchAlgorithmException {
		if (keyFactory == null) {
			synchronized (mutex) {
				if (keyFactory == null) {
					keyFactory = KeyFactory.getInstance(RSA_ALGORTHM);
				}
			}
		}
		return keyFactory;
	}

	private void initializeKey(RSAKeyType keyType, byte[] keyBytes)
			throws SecurityException {
		try {
			switch (keyType) {
			case PRIVATE:
				PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
						keyBytes);
				key = getKeyFactory().generatePrivate(pkcs8EncodedKeySpec);
				break;
			case PUBLIC:
				X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
				key = getKeyFactory().generatePublic(x509EncodedKeySpec);
				break;
			default:
				throw new IllegalArgumentException("Invalid RSAKeyType!");
			}

		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException(e.getMessage(), e);
		} catch (InvalidKeySpecException e) {
			throw new SecurityException(e.getMessage(), e);
		}
	}


	protected Key getKey() {
		return key;
	}

	

}
