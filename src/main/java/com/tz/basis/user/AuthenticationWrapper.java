package com.tz.basis.user;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AuthenticationWrapper implements Authentication {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Authentication original;
	private Collection<GrantedAuthority> extraRoles;

	public AuthenticationWrapper(Authentication original, Collection<GrantedAuthority> extraRoles) {
		this.original = original;
		this.extraRoles = extraRoles;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		@SuppressWarnings("unchecked")
		Collection<GrantedAuthority> originalRoles = (Collection<GrantedAuthority>) original.getAuthorities();
		Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

		for (GrantedAuthority ori : originalRoles) {
			auths.add(ori);
		}
		for (GrantedAuthority ori : extraRoles) {
			auths.add(ori);
		}
		return auths;
	}

	public String getName() {
		return original.getName();
	}

	public Object getCredentials() {
		return original.getCredentials();
	}

	public Object getDetails() {
		return original.getDetails();
	}

	public Object getPrincipal() {
		return original.getPrincipal();
	}

	public boolean isAuthenticated() {
		return original.isAuthenticated();
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		original.setAuthenticated(isAuthenticated);
	}
}