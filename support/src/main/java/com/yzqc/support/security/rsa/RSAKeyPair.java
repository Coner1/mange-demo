package com.yzqc.support.security.rsa;

public class RSAKeyPair{
	
	private String publicKey;
	
	private String privateKey;
	
	public RSAKeyPair(String publicKey, String privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	/**
	 * Base64 编码的私钥；
	 * @return
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * Base64编码格式的公钥；
	 * @return
	 */
	public String getPublicKey() {
		return publicKey;
	}

}