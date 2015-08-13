package com.yzqc.support.security;

import java.lang.*;

public interface TokenGenerator {

	public ApplicationToken createToken(String appCode, String external) throws SecurityException;

}
