package com.tz.basis.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Returns a 401 error code (Unauthorized) to the client.
 */
@Component
public class SUnauthorizedEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(SUnauthorizedEntryPoint.class);

	@Autowired
	Environment env;

	/**
	 * Always returns a 401 error code to the client.
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		
		String uri = request.getRequestURI().toString();
		logger.debug("==== uri: " + uri);
		logger.debug("==== uiType 4:" + env.getProperty("uiType"));
		
		if (env.getProperty("uiType").equalsIgnoreCase("angular")) {
			logger.debug("==== accept: " + String.valueOf(request.getHeader("accept")));
			String str = (request.getHeader("accept") == null) ? "" : request.getHeader("accept"); 
			if(str.indexOf("application/json") > -1) {
				SecurityUtils.sendError(response, exception, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
			} else {
				uri = uri.substring(1, uri.length());
				logger.debug("uiType 41: /index.html");
				response.sendRedirect("/tzUI/index.html");
			}
		} else {
			response.sendRedirect("#/login?error=logout");
		}
	}
}
