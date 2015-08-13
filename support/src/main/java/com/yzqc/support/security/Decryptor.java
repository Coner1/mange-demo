package com.yzqc.support.security;

/**
 * Decryptor 定义数据解密接口；
 * 
 * @author haiq
 *
 */
public interface Decryptor {
	
	/**
	 * 解密数据；
	 * 
	 * @param data 被加密过的数据；
	 * @return
	 * @throws SecurityException
	 */
	public byte[] decrypt(byte[] data) throws SecurityException;
	
	/**
	 * 
	 * @param data 被加密过的数据；
	 * @param offset 数据的起始位置
	 * @return
	 * @throws SecurityException
	 */
	public byte[] decrypt(byte[] data, int offset) throws SecurityException;
	
	/**
	 * 解密数据；
	 * 
	 * @param data 被加密过的数据；
	 * @param offset 数据的起始位置
	 * @param length 数据长度；
	 * @return
	 * @throws SecurityException
	 */
	public byte[] decrypt(byte[] data, int offset, int length) throws SecurityException;
	
}
