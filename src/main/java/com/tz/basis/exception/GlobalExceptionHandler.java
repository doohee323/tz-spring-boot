package com.tz.basis.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<Void> handle(NoHandlerFoundException ex){
//  		logger.debug("==== ==== ==== ==== uri2: ");
//        String message = "HTTP " + ex.getHttpMethod() + " for " + ex.getRequestURL() + " is not supported.";
//        return ResponseEntity.ok().build();
//    }

}
