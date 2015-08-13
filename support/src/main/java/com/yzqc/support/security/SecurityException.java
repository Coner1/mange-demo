package com.yzqc.support.security;

public class SecurityException extends Exception{

	private static final long serialVersionUID = 4381527702112182774L;

	
	public SecurityException() {
	}
	
	
	
	public SecurityException(String message) {
		super(message);
	}
	
	
	
	public SecurityException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}
