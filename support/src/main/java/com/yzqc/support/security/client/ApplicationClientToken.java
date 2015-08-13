package com.yzqc.support.security.client;

public interface ApplicationClientToken {
	
	/**
	 * 分配给应用用于对请求进行签名的KEY；
	 * 
	 * @return
	 */
	public String secretKey();
	
	/**
	 * 应用编码；
	 * 
	 * @return
	 */
	public String code();
	
	/**
	 * 时间戳；
	 * 
	 * @return
	 */
	public long ts();
	
	
}
