package com.tz.basis.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
@ControllerAdvice
public class ExceptionHandlerController {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

	//	@ExceptionHandler(Exception.class)
	//	public String exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
	//		ResponseStatus responseStatusAnnotation = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
	//		return buildModelAndViewErrorPage(request, response, ex,
	//				responseStatusAnnotation != null ? responseStatusAnnotation.value() : HttpStatus.INTERNAL_SERVER_ERROR);
	//	}

	//	@RequestMapping("*")
	//	public String fallbackHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
	//		return buildModelAndViewErrorPage(request, response, null, HttpStatus.NOT_FOUND);
	//	}

	//	@ExceptionHandler(NoHandlerFoundException.class)
	//	public ResponseEntity<Void> handle(NoHandlerFoundException ex) {
	//		logger.debug("==== ==== ==== ==== uri3: ");
	//
	//		String message = "HTTP " + ex.getHttpMethod() + " for " + ex.getRequestURL() + " is not supported.";
	//		return ResponseEntity.ok().build();
	//	}

	//	private String buildModelAndViewErrorPage(HttpServletRequest request, HttpServletResponse response, Exception ex,
	//			HttpStatus httpStatus) {
	//		response.setStatus(httpStatus.value());
	//		String uri = request.getRequestURI().toString();
	//		logger.debug("==== uri: " + uri);
	//		if (uri.indexOf("/index.html") > -1) {
	//			logger.debug("redirect:/index.html");
	//			return "redirect:/index.html";
	//		} else {
	//			uri = uri.substring(1, uri.length());
	//			logger.debug("redirect:/#" + uri);
	//			return "redirect:/#" + uri;
	//		}
	//	}

}