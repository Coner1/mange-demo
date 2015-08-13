package com.yzqc.support.security;

/**
 * 数据签名接口；
 * 
 * @author haiq
 *
 */
public interface Signer {

	/**
	 * 对数据进行签名；
	 * 
	 * @param data
	 *            要签名的数据；
	 * @return 签名；
	 * @throws SecurityException
	 */
	public byte[] sign(byte[] data) throws SecurityException;

}
