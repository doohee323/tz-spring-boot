package com.tz.extend.web;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {

	private static final Logger logger = LoggerFactory
			.getLogger(ErrorController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/errors/404", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "error/404";
	}

	@RequestMapping(value = "/errors/400", method = RequestMethod.GET)
	public String error400(Locale locale, Model model) {
		return "error/400";
	}
	@RequestMapping(value = "/errors/405", method = RequestMethod.GET)
	public String error405(Locale locale, Model model) {
		return "error/405";
	}
	@RequestMapping(value = "/errors/403", method = RequestMethod.GET)
	public String error403(Locale locale, Model model) {
		return "error/403";
	}

	@RequestMapping(value = "/errors", method = RequestMethod.GET)
	public String error(Locale locale, Model model) {
		return "error/error";
	}

	@RequestMapping(value = "/accessdenied", method = RequestMethod.GET)
	public String accessdenied(Locale locale, Model model, HttpSession session) {

		return "error/accessdenied";
	}
	
	@RequestMapping(value = "/error/loggedIn", method = RequestMethod.GET)
	public String failByLogin(Model model) {
		model.addAttribute("error", "You are log in now");
		return "error/error_goback";
	}
	
}
