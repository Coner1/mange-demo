package com.yzqc.support.security;

/**
 * 数据签名校验接口；
 * 
 * @author haiq
 *
 */
public interface Verifier {

	/**
	 * 验证数据和签名是否匹配；
	 * 
	 * @param data
	 *            数据；
	 * @param sign
	 *            签名；
	 * @return 当数据和签名是一致时，返回 true；否则，返回 false；
	 * @throws SecurityException 
	 */
	public boolean verify(byte[] data, byte[] sign) throws SecurityException;

}
