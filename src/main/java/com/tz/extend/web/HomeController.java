package com.tz.extend.web;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.gson.JsonObject;
import com.tz.basis.user.Inquiry;
import com.tz.basis.user.InquiryService;
import com.tz.basis.user.UserDataService;
import com.tz.basis.user.UserDataStore;
import com.tz.basis.user.UserInformation;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	UserDataService userService;
	
	@Autowired
	UserDataStore userDataStore;

	@Autowired
	InquiryService inquiryService;

	@Autowired
	Environment env;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/pre_marketing", method = RequestMethod.GET)
	public String index(Locale locale, Model model) {
		return "pre_marketing";
	}

	@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Locale locale, Model model, HttpServletRequest request) {
		try {
			logger.debug("==== uiType:" + env.getProperty("uiType"));
			Object object = SecurityContextHolder.getContext().getAuthentication();
			if (object == null) {
				if (env.getProperty("uiType").equalsIgnoreCase("angular")) {
					return "redirect:/index.html#/home";
				} else {
					return "redirect:/user/home";
				}
			}
			object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (!object.toString().equalsIgnoreCase("anonymoususer")) {
				if (env.getProperty("uiType").equalsIgnoreCase("angular")) {
						return "redirect:/index.html#/home";
				} else {
					return "redirect:/user/home";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (env.getProperty("uiType").equalsIgnoreCase("angular")) {
			return "redirect:/index.html#/home";
		} else {
			return "home";
		}
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	@ResponseBody
	public String home(HttpServletRequest request) {
		JsonObject json = new JsonObject();

		json.addProperty("locale", RequestContextUtils.getLocale(request).getLanguage());
		String viewerCountry = request.getHeader("CloudFront-Viewer-Country") == null ? viewerCountry = "KR"
				: request.getHeader("CloudFront-Viewer-Country");
		json.addProperty("viewerCountry", viewerCountry);

		return json.toString();
	}	

	@RequestMapping(value = "/unsubscribe", method = RequestMethod.GET)
	public String emailRefusalPage(Locale locale, Model model, HttpServletRequest request, String type, String email) {
		model.addAttribute("type", StringUtils.isEmpty(type) ? "marketing" : type);
		model.addAttribute("email", email);
		return "etc/unsubscribe";
	}

	@RequestMapping(value = "/emailRefusal", method = RequestMethod.GET)
	@ResponseBody
	public WebResult emailRefusal(Model model, @RequestParam String type, @RequestParam String email) {
		WebResult result = new WebResult();

		try {
			UserInformation savedInfo = new UserInformation();
			savedInfo = userService.findUserByEmail(email);
			if (type.equalsIgnoreCase("fxrate")) {
				savedInfo.setFxrateEmailRefusal("Y");
			} else {
				savedInfo.setMktEmailRefusal("Y");
			}
			userService.updateUser(savedInfo);

			result.setResultCode(200);
			result.setResultMessage("OK");

			return result;

		} catch (Exception e) {
			result.setResultCode(400);
			result.setResultMessage("FAIL");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "/inquiry", method = RequestMethod.POST)
	@ResponseBody
	public WebResult InquiryJson(Model model, @RequestParam HashMap<String, String> param) {
		WebResult result = new WebResult();

		try {
			Inquiry data = new Inquiry();

			data.setEmail(param.get("email"));
			data.setContents(param.get("contents"));
			data.setUsername("N");

			Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (!object.toString().equalsIgnoreCase("anonymoususer")) {
				data.setUsername("Y");
			} else {
				if (null != userService.findUserByEmail(data.getEmail())) {
					data.setUsername("Y");
				}
			}

			inquiryService.InquiryInsert(data);

			result.setResultCode(200);
			result.setResultMessage("OK");
			return result;

		} catch (Exception e) {
			result.setResultCode(400);
			result.setResultMessage("FAIL");
			e.printStackTrace();
		}
		return result;
	}

}
