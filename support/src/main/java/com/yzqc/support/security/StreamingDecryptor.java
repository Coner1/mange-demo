package com.yzqc.support.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamingDecryptor {
	
	/**
	 * 解密输入流中的全部数据，并将解密数据写入到输出流；
	 * 
	 * @param in
	 *            被解密数据的输入流；
	 * @param out
	 *            解密后的数据的输出流；
	 * @throws IOException 
	 * @throws SecurityException
	 */
	public void decrypt(InputStream in, OutputStream out) throws IOException, SecurityException;

}
