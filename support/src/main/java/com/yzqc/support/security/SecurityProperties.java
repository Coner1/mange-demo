package com.yzqc.support.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * SecurityProperties 定义了如何将安全认证相关的属性值格式为一个字符串，以及从字符串解析为SecurityProperties实例；
 * 
 * @author haiq
 *
 */
public class SecurityProperties {

	public static final String FIELD_KEY = "key";
	public static final String FIELD_TOKEN = "token";
	public static final String FIELD_EXTERNAL = "external";
	public static final String FIELD_APPCODE = "appcode";
	public static final String FIELD_TS = "ts";
	public static final String FIELD_AGENT = "user-agent";
	public static final String FIELD_REQUEST_PATH = "request-path";

	private static final String FIELD_SEPERATOR = ":";
	private static final String LINE_SEPERATOR = "\r\n";

	private String appCode;

	private String ts;

	private String userAgent;

	private String requestPath;

	private String key;

	private String token;

	private String external;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getRequestPath() {
		return requestPath;
	}

	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}

	public String toString() {
		StringBuilder formatedInfo = new StringBuilder();
		if (key != null) {
			formatedInfo.append(FIELD_KEY).append(FIELD_SEPERATOR)
					.append(key).append(LINE_SEPERATOR);
		}
		if (token != null) {
			formatedInfo.append(FIELD_TOKEN).append(FIELD_SEPERATOR)
					.append(token).append(LINE_SEPERATOR);
		}
		if (appCode != null) {
			formatedInfo.append(FIELD_APPCODE).append(FIELD_SEPERATOR)
					.append(appCode).append(LINE_SEPERATOR);
		}
		if (ts != null) {
			formatedInfo.append(FIELD_TS).append(FIELD_SEPERATOR).append(ts)
					.append(LINE_SEPERATOR);
		}
		if (userAgent != null) {
			formatedInfo.append(FIELD_AGENT).append(FIELD_SEPERATOR)
					.append(userAgent).append(LINE_SEPERATOR);
		}
		if (requestPath != null) {
			formatedInfo.append(FIELD_REQUEST_PATH).append(FIELD_SEPERATOR)
					.append(requestPath).append(LINE_SEPERATOR);
		}
		if (external != null) {
			formatedInfo.append(FIELD_EXTERNAL).append(FIELD_SEPERATOR)
					.append(external).append(LINE_SEPERATOR);
		}
		return formatedInfo.toString();
	}

	public static SecurityProperties loadFromString(String appInfo)
			throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(appInfo));
		try {
			return loadFromReader(br);
		} finally {
			br.close();
		}
	}

	public static SecurityProperties loadFromFile(File file) throws IOException {
		if (!file.isFile()) {
			return null;
		}
		FileInputStream in = new FileInputStream(file);
		InputStreamReader ir = new InputStreamReader(in,
				ApplicationTokenConsts.CHARSET);
		BufferedReader br = new BufferedReader(ir);
		try {
			return loadFromReader(br);
		} finally {
			try {
				br.close();
			} finally {
				in.close();
			}
		}
	}

	private static SecurityProperties loadFromReader(BufferedReader reader)
			throws IOException {
		SecurityProperties props = new SecurityProperties();
		String propertyLine = null;
		while ((propertyLine = reader.readLine()) != null) {
			String[] propEntry = resolvePropertyEntry(propertyLine);
			if (propEntry == null) {
				continue;
			}
			if (FIELD_KEY.equalsIgnoreCase(propEntry[0])) {
				props.key = propEntry[1];
			} else if (FIELD_TOKEN.equalsIgnoreCase(propEntry[0])) {
				props.token = propEntry[1];
			} else if (FIELD_APPCODE.equalsIgnoreCase(propEntry[0])) {
				props.appCode = propEntry[1];
			} else if (FIELD_TS.equalsIgnoreCase(propEntry[0])) {
				props.ts = propEntry[1];
			} else if (FIELD_AGENT.equalsIgnoreCase(propEntry[0])) {
				props.userAgent = propEntry[1];
			} else if (FIELD_REQUEST_PATH.equalsIgnoreCase(propEntry[0])) {
				props.requestPath = propEntry[1];
			} else if (FIELD_EXTERNAL.equalsIgnoreCase(propEntry[0])) {
				props.external = propEntry[1];
			}
		}
		return props;
	}

	private static String[] resolvePropertyEntry(String propertyLine) {
		if (propertyLine.length() == 0) {
			return null;
		}
		int sepIndex = propertyLine.indexOf(FIELD_SEPERATOR);
		if (sepIndex < 1 || sepIndex == propertyLine.length()-1) {
			return null;
		}
		String[] propEntry = new String[2];
		propEntry[0] = propertyLine.substring(0, sepIndex);
		propEntry[1] = propertyLine.substring(sepIndex+1);
		return propEntry;
	}

	public String getExternal() {
		return external;
	}

	public void setExternal(String external) {
		this.external = external;
	}
}
