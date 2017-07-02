package com.tz.basis.security;

import org.springframework.stereotype.Component;

import com.tz.basis.util.SecurityUtil;

@Component
public class PasswordEncryptor {

	public static void main(String[] args) {
		
		String passwd = "passwd123";
		String encrypted = SecurityUtil.encrypt(passwd);
		System.out.println(encrypted);
		System.out.println(SecurityUtil.decrypt(encrypted));
		
		// TODO Auto-generated method stub
		/*
		ApplicationContext context = new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/spring/application-security.xml");
		UserDetailsService userService = context.getBean(UserDetailsService.class);
		BCryptPasswordEncoder passwordEncoder = context.getBean(BCryptPasswordEncoder.class);
		ArrayList<UserInformation> userList = userService.getList();
		for (int i=0; i<userList.size(); i++){
			UserInformation user = userList.get(i);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userService.updateUser(user);
			System.out.println(i+1+". "+user.getFirstName()+" "+user.getLastName()+"'s password has been encrypted" );
		}
		*/
	}
}
