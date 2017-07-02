package com.tz.basis.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomGenericException extends RuntimeException {

	private static final long serialVersionUID = 1L;

  private static final Logger logger = LoggerFactory.getLogger(CustomGenericException.class);
  
	private String errCode;
	private String errMsg;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public CustomGenericException(String errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

}
