package com.tz.basis.user.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tz.basis.user.UserInformation;

/**
 */
public class UserInfo {

	private static Logger logger = LoggerFactory.getLogger(UserInfo.class);

	/**
	 * <pre>
	 *  사용자ID 조회
	 * </pre>
	 * 
	 * @return 사용자 ID
	 */
	public static String getUserId() {
		try {
			UserInformation user = UserInfoHolder.getUserInfo(UserInformation.class);
			return user.getUsername();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "";
	}

	/**
	 * <pre>
	 * </pre>
	 * 
	 * @return
	 */
	public static String getUserName() {
		UserInformation user = UserInfoHolder.getUserInfo(UserInformation.class);
		return user.getFirstName() + " " + user.getLastName();
	}

	public static UserInformation getUserInfo() {
		UserInformation user = UserInfoHolder.getUserInfo(UserInformation.class);
		if (user == null) {
			try {
				throw new Exception("error!");
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return user;
	}
	
  public static String getEmail() {
    return getUserInfo().getEmail();
}
	
}
