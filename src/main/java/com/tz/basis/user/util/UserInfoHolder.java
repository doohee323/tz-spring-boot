package com.tz.basis.user.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.tz.basis.user.UserInformation;

/**
 * <pre>
 * 인증된 사용자 정보에 접근하기 위한 Helper 클래스
 * <pre>
 */
public class UserInfoHolder {

	/**
	 * <pre>
	 * 로그인한 사용자 정보 조회
	 * <pre>
	 *
	 * @param <T>
	 * @param clazz - 로그인을 위해 사용된 Query 클래스
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getUserInfo(Class<? extends UserDetails> clazz)	{
		if(SecurityContextHolder.getContext().getAuthentication() == null) return null;
		try {
		    Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    if(obj.toString().equals("anonymousUser")) {
		        return (T)new UserInformation();
		    }
	        return (T) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
		}
		return (T)new UserInformation();
	}
}
