package com.tz.basis.util;

public class CommonCode {
	
	public static final String STRING_RESULT="result";
	public static final String STRING_RESULT_SUCCESS="200";
	public static final String STRING_RESULT_FAIL="400";
	public static final String STRING_RESULT_NOT_FOUND="404";
	public static final String STRING_EXCEPTION="500";
	
	public static final String STRING_RESULT_MESSAGE="resultMessage";
	public static final String STRING_RESULT_LIST="resultList";
	public static final String STRING_RESULT_EXCEPTION="1000";
	
	//request Mail
	public static final String STRING_RESULT_FORM_SUCCESS="100";
	public static final String STRING_RESULT_FORM_FAIL="200";
	public static final String STRING_RESULT_FORM_EXIST="300";
	public static final String STRING_RESULT_FORM_EXCEPTION="400";
		
	//confirm codeMail
	public static final String STRING_RESULT_CODE_SUCCESS="100";
	public static final String STRING_RESULT_CODE_FAIL="200";
	public static final String STRING_RESULT_CODE_NOT_EXIST="300";
	public static final String STRING_RESULT_CODE_EXCEPTION="400";
	public static final String STRING_RESULT_CODE_EXPIRE="500";
	
	//request SMSCODE
	public static final String STRING_REQUEST_SMS_SUCCESS="200";
	public static final String STRING_REQUEST_SMS_PHONE_EXIST="100";
	public static final String STRING_REQUEST_SMS_NO_PHONE="300";
	public static final String STRING_REQUEST_SMS_EXCEPTION="500";
	
	//confirm SMSCODE
	public static final String STRING_CONFRIM_SMS_SUCCESS="200";
	public static final String STRING_CONFRIM_SMS_FAIL="100";
	public static final String STRING_CONFRIM_SMS_NO_CODE="300";
	public static final String STRING_CONFRIM_SMS_EXCEPTION="500";
	public static final String STRING_CONFRIM_SMS_EXPIRE="400";
	
	//check account existence
	public static final String STRING_CHECK_ACCOUNT_EXISTENCE_EXIST="200";
	public static final String STRING_CHECK_ACCOUNT_EXISTENCE_NOT_EXIST="201";
	public static final String STRING_CHECK_ACCOUNT_EXISTENCE_EXCEPITION="400";
	
	
	/**
	 * /user/invitation/gmail<br>
	 * send invitation gmail
	 * @return business logic result
	 */
	public static final String STRING_SEND_INVITATION_GMAIL_EXIST="100";
	public static final String STRING_SEND_INVITATION_GMAIL_SUCCESS="200";
	public static final String STRING_SEND_INVITATION_GMAIL_FAIL="400";
	public static final String STRING_SEND_INVITATION_GMAIL_EXCEPTION="500";
	
	
	/**
	 * /user/invitation/gmail<br>
	 * verify member
	 * @return business logic result
	 */
	
	public static final String STRING_VERIFY_MEMBER_SUCCESS="200";
	public static final String STRING_VERIFY_MEMBER_FAIL="400";
	public static final String STRING_VERIFY_MEMBER_EXCEPTION="500";
	
	/**
	 * /user/emails/{userEmail}<br>
	 * verify member
	 * @return business logic result
	 */
	public static final String STRING_VERIFY_EMAIL_SUCCESS="200";
	public static final String STRING_VERIFY_EMAIL_FAIL="400";
	public static final String STRING_VERIFY_EMAIL_EXCEPTION="500";
	
	/**
	 * /transfers/{id}<br>
	 * update transfer
	 * @return business logic result
	 */
	public static final int UPDATE_SUCCESS_INT=200;
	public static final int UPDATE_ALREADY_TRANSFERRING_INT=400;
	public static final int UPDATE_ALREADY_COMPLETED_INT=401;
	
	/**
	 * 동사보다는 명사를 사용 
	 * 
	 * @return result,data,resultMessage
	 * 
	 * 200 성공
	 * 400 field validation 실패
	 * 401 인가실패
	 * 404 not found
	 * 500 서버내부적에
	 */
	
	public static final int INT_RESULT_SUCCESS=200;
	public static final int INT_RESULT_FAIL=400;
	public static final int INT_RESULT_NOT_FOUND=404;
	public static final int INT_RESULT_SERVER_ERROR=500;
	public static final int INT_RESULT_EXPIRED=700;
	public static final int INT_RESULT_EXIST=800;

	public static final String METHOD_KEY="_method";
	public static final String HTTPCODE_KEY="_errorCode";
	public static final String URL_KEY="_url";
	public static final String RESULT_KEY="_result";
	public static final String BODY_KEY="_body";

}
