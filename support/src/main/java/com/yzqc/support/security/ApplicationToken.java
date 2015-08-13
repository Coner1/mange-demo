package com.yzqc.support.security;

/**
 * 第三方应用令牌；
 * 
 * @author haiq
 *
 */
public interface ApplicationToken {
	
	
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

	/**
	 * 客户端令牌；
	 * 
	 * 第三方应用使用平台发布的 SDK，并携带分配的客户端令牌才能通过服务器的认证；
	 * @return
	 */
	public String clientToken();
	
	/**
	 * 服务端凭证；
	 * 
	 * 用于校验第三方应用的请求；
	 * @return
	 */
	public String serverCredential();
	
}
