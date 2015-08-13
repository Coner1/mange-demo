package com.yzqc.support.security;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPWhiteList extends WhiteList<InetAddress>{

	@Override
	protected String toString(InetAddress item) {
		return item.toString();
	}

	@Override
	protected InetAddress fromString(String strItem) {
		try {
			return InetAddress.getByName(strItem);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(strItem);
		}
	}
	
	private IPWhiteList(String items) {
		super(items);
	}
	
	private IPWhiteList(InetAddress...addresses) {
		super(addresses);
	}
	
	public static IPWhiteList create(){
		return new IPWhiteList();
	}
	
	public static IPWhiteList createFromString(String strIpAddrList){
		return new IPWhiteList(strIpAddrList);
	}
	
	public static IPWhiteList createFromAddress(InetAddress... addresses){
		return new IPWhiteList(addresses);
	}
}
