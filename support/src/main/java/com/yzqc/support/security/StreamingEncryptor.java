package com.yzqc.support.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 针对流式数据的加密接口；
 * 
 * @author haiq
 *
 */
public interface StreamingEncryptor {

	/**
	 * 加密输入流的全部数据，并将加密数据写入到输出流；
	 * 
	 * @param in
	 *            要加密的原始数据的输入流；
	 * @param out
	 *            加密后的数据的输出流；
	 * @throws SecurityException
	 * @throws IOException 
	 */
	public void encrypt(InputStream in, OutputStream out) throws IOException, SecurityException;

}
