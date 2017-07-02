package com.tz.extend.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebResult {

	private static final Logger logger = LoggerFactory.getLogger(WebResult.class);
	
	public static final int RESULT_CODE_PREPARE = 100;
	public static final int RESULT_CODE_OK = 200;
	public static final int RESULT_CODE_FAIL = 400;
	public static final int RESULT_CODE_INVALID_PARAM = 401;
	
	private int resultCode;
	private String resultMessage;
	private Object data;
	
	public WebResult() {
		super();
	}
	public WebResult(int resultCode, String resultMessage, Object data) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.data = data;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
