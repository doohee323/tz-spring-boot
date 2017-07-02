package com.tz.basis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleNameValuePair {

	private static final Logger logger = LoggerFactory.getLogger(SimpleNameValuePair.class);
	
	private String name;
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public SimpleNameValuePair(){
		super();
	}
	public SimpleNameValuePair(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	
	
	
}
