package com.tz.basis.user;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

	private static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

	@Autowired
	private UserDataService userDataService;

	@Autowired
	Environment env;

	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		UserInformation user = null;
		try {
			user = userDataService.getUserById(arg0);
			if (user == null && arg0.matches(UUID_PATTERN)) {
				user = userDataService.getUser(arg0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		} else {
		}
		return user;
	}

	/**
	 * Get the login of the current user.
	 */
	public UserInformation getCurrentUser() {
		UserInformation user = null;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null) {
				throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
			}
			user = (UserInformation) authentication.getPrincipal();
		} catch (Exception e) {
			if (env.getProperty("uiType").equalsIgnoreCase("postman")) {
				user = (UserInformation) loadUserByUsername(env.getProperty("testUser"));
			} else {
				throw new IllegalStateException("Unable to get user");
			}
		}
		return user;
	}

	/**
	 * Get the login of the current user's Authority.
	 */
	public Authentication getCurrentAuth() {
		Authentication authentication = null;
		try {
			authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null) {
				throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
			}
			if (env.getProperty("uiType").equalsIgnoreCase("postman")) {
				UserInformation user = (UserInformation) loadUserByUsername(env.getProperty("testUser"));
				authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			throw new IllegalStateException("Unable to get user's authority");
		}
		return authentication;
	}	
	
	/**
	 * Get the login of the current user's Authority.
	 */
	@SuppressWarnings("unchecked")
	public Collection<? extends GrantedAuthority> getCurrentAuths() {
		Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null) {
				throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
			}
			if (env.getProperty("uiType").equalsIgnoreCase("postman")) {
				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
				auths.add(grantedAuthority);
			} else {
				auths = (Collection<GrantedAuthority>) authentication.getAuthorities();
			}
		} catch (Exception e) {
			throw new IllegalStateException("Unable to get user's authority");
		}
		return auths;
	}

}
