package com.tz.basis.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tz.basis.user.UserDataService;
import com.tz.basis.user.UserInformation;
import com.tz.basis.util.CommonUtil;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
@Transactional
public class SAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(SAuthenticationSuccessHandler.class);

	@Autowired
	private UserDataService userService;

	@Autowired
	Environment env;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		UserInformation user = (UserInformation) authentication.getPrincipal();

		boolean isUser = false;
		boolean isAdmin = false;
		String targetUrl = "/user/home";
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
				isUser = true;
			} else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			}
		}
		HttpSession session = request.getSession();

		if (isAdmin) {
			session.setMaxInactiveInterval(15 * 60); // session remains 15 minutes
			targetUrl = "/admin";
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		} else if (isUser) {
			session.setMaxInactiveInterval(3600); // session remains an hour
			UserInformation dbUser = userService.getUser(user.getUsername());
			if (dbUser.getFirstName() == null || dbUser.getFirstName().trim().length() == 0 || dbUser.getLastName() == null
					|| dbUser.getLastName().trim().length() == 0) {
				targetUrl = "/login_process/edit/profile";
				getRedirectStrategy().sendRedirect(request, response, targetUrl);
			} else {
				if (session != null) {
					String referralUrl = (String) session.getAttribute("url_prior_login");
					String siteUrl = CommonUtil.getURLWithContextPath(request) + "/";

					if (env.getProperty("uiType").equalsIgnoreCase("angular")) {
						// allow cors
						response.setHeader("Access-Control-Allow-Origin", "*");
						response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
						response.setHeader("Access-Control-Max-Age", "3600");
						response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

						SecurityUtils.sendResponse(response, HttpServletResponse.SC_OK, user);
					} else {
						if (referralUrl == null) {
							super.onAuthenticationSuccess(request, response, authentication);
						} else {
							if (!referralUrl.equalsIgnoreCase(siteUrl) && !referralUrl.contains("login")
									&& !referralUrl.toLowerCase().contains("password")) {
								targetUrl = referralUrl;
							}
							// then we redirect
							getRedirectStrategy().sendRedirect(request, response, targetUrl);
						}
					}
				}
			}
		} else {
			throw new IllegalStateException();
		}

		if (env.getProperty("uiType").equalsIgnoreCase("angular")) {
			session.removeAttribute("url_prior_login");
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}

	}
}
