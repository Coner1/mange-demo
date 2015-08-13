package com.yzqc.support.security;

import java.util.HashMap;

public abstract class WhiteList<T> {
	public static final String FORMATTED_SEPERATOR = ";";
	
	private HashMap<String, T> items = new HashMap<String, T>();
	
	public WhiteList() {
	}
	
	public WhiteList(T... itemList) {
		if (itemList != null) {
			for (T t : itemList) {
				add(t);
			}
		}
	}
	
	public WhiteList(String itemList) {
		if (itemList != null) {
			String[] strItems = itemList.split(FORMATTED_SEPERATOR);
			for (String strItem : strItems) {
				T t = fromString(strItem);
				add(t);
			}
		}
	}
	
	
	protected abstract String toString(T item);
	
	protected abstract T fromString(String strItem);
	
	public void add(T item){
		String ikey = toString(item);
		items.put(ikey, item);
	}
	
	public void remove(T item){
		String ikey = toString(item);
		items.remove(ikey);
	}
	
	public boolean contains(String ip){
		return items.containsKey(ip);
	}
	
	public boolean isEmpty(){
		return items.isEmpty();
	}
}
