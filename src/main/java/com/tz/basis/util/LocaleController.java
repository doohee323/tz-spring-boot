package com.tz.basis.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.tz.basis.user.UserInformation;
import com.google.gson.JsonObject;
import com.tz.basis.user.UserDataService;
import com.tz.basis.user.UserDetailsService;

@Controller
public class LocaleController {

	private static final Logger logger = LoggerFactory.getLogger(LocaleController.class);

	@Autowired
	private UserDataService userService;
	
//    @RequestMapping(value = "/changeLocale")
//    public String changeLocale(HttpServletRequest request, HttpServletResponse response, Authentication authentication, @RequestParam(required = false) String lang) {
//		Locale locale = new Locale(lang);
//		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
//		localeResolver.setLocale(request, response, locale);
//		
//		if (authentication != null){
//			UserInformation user = (UserInformation) authentication.getPrincipal();
//			user.setLang(locale.getLanguage());
//			userService.updateUserLanguage(user);
//		}
//		
//		String redirectURL = "redirect:" + request.getHeader("referer");
//        return redirectURL;
//    }
    
    @RequestMapping(value = "/changeLocale",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String changeLocale(HttpServletRequest request, HttpServletResponse response, Authentication authentication, @RequestParam(required = false) String lang) {
    	
    	JsonObject json = new JsonObject();
    	
		Locale locale = new Locale(lang);
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		localeResolver.setLocale(request, response, locale);
		
		if (authentication != null){
			UserInformation user = (UserInformation) authentication.getPrincipal();
			user.setLang(locale.getLanguage());
			userService.updateUserLanguage(user);
		}
		
		//String redirectURL = "redirect:" + request.getHeader("referer");
		
		json.addProperty(CommonCode.STRING_RESULT, CommonCode.INT_RESULT_SUCCESS);
        return json.toString();
    }
    
    
    
    @RequestMapping(value = "/locale",produces="application/json;charset=UTF-8")
    @ResponseBody
    public String getLocale(HttpServletRequest request, HttpServletResponse response, Authentication authentication,Locale locale) {
    	
    	JsonObject json = new JsonObject();
    	
		json.addProperty(CommonCode.STRING_RESULT, CommonCode.INT_RESULT_SUCCESS);
		json.addProperty("language",locale.getLanguage());
        return json.toString();
    }

}