package com.tz.basis.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tz.basis.security.SecurityUtils;
import com.tz.basis.util.CommonCode;
import com.tz.basis.util.CommonUtil;
import com.tz.extend.web.WebResult;

@Controller
@PropertySource({ "classpath:config/application.properties" })
public class UserController implements MessageSourceAware {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserDataService userService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	UserDataStore userDataStore;

	@Autowired
	Environment env;

	private MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@RequestMapping(value = "/devsignin", method = RequestMethod.POST)
	public void devsignin(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "devsignin", required = false) String logout, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if (env.getProperty("uiType").equalsIgnoreCase("postman")) {
			// allow cors
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

			UserInformation user = userDetailsService.getCurrentUser();
			SecurityUtils.sendResponse(response, HttpServletResponse.SC_OK, user);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public String insertUser(String username, String email, String password,
			String referralId, String firstName, String lastName, String facebook,
			String mobile, String dob, String timezone, String emailVerification, HttpSession session,
			HttpServletRequest request) {

		JsonObject json = new JsonObject();
		Gson gson = new Gson();
		
		try {
			UserInformation savedInfo = null;

			if (facebook.equals("Y")) { // social login user
				savedInfo = userService.getUser(username);
			} else {
				savedInfo = new UserInformation();
				savedInfo.setPassword(password);
			}
			savedInfo.setTimezone(CommonUtil.getTimezone(request, savedInfo));
			savedInfo.setEmail(email);
			savedInfo.setId(email);
			savedInfo.setFirstName(firstName);
			savedInfo.setLastName(lastName);
			savedInfo.setMobile(mobile);

			if (!dob.isEmpty()) {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date dobUtilDate;
				dobUtilDate = formatter.parse(dob);
				java.sql.Date dobSqlDate = new java.sql.Date(dobUtilDate.getTime());
				savedInfo.setDob(dobSqlDate);
			}

			String hostId = "";
			if (referralId!=null && referralId.trim() !="") {
				hostId = userService.getUserByReferralId(referralId).getUsername();
				savedInfo.setHostId(hostId);
			}
			
			if(emailVerification!=null){
				if (emailVerification.equals("true")) {
					savedInfo.setEmailconfirmdate(new Timestamp(System.currentTimeMillis()));
				}
			}
			
			if (facebook.equals("Y")) { // if user is from facebook/google connect, just update the data
				userService.updateUser(savedInfo);
			} else {
				userService.addUser(savedInfo);
			}

			Authentication auth = new UsernamePasswordAuthenticationToken(savedInfo, null, userDetailsService.loadUserByUsername(savedInfo.getEmail()).getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth); //login
			
			UserInformation user = userDetailsService.getCurrentUser();
			//UserInformation user = (UserInformation) auth.getPrincipal();

			String str = gson.toJson(user);
			JsonParser parser = new JsonParser();
			JsonObject json1 = parser.parse(str).getAsJsonObject();
			json1.addProperty("credentialsNonExpired", true);
		
			json.addProperty("user", json1.toString());
			json.addProperty(CommonCode.STRING_RESULT, CommonCode.STRING_RESULT_SUCCESS);
		} catch (Exception e) {
			logger.debug(e.toString());
			json.addProperty(CommonCode.STRING_RESULT, CommonCode.STRING_RESULT_FAIL);
		}
		return json.toString();
	}

	@RequestMapping(value = "/user", method = RequestMethod.PUT)
	@ResponseBody
	public String updateUser(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		JsonObject result = new JsonObject();
		JsonObject emailUnique = new JsonObject();

		try {
			// 1. get received JSON data from request
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String json = "";
			if (br != null) {
				json = br.readLine();
			}

			// 2. initiate jackson mapper
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //If you want ignore unknown properties globally

			// 3. Convert received JSON to Article
			UserInformation param = mapper.readValue(json, UserInformation.class);

			if (param == null) {
				result.addProperty("result", CommonCode.INT_RESULT_NOT_FOUND);
				result.addProperty("error", "no parameters retrieved");
				return result.toString();
			}

			emailUnique = userService.verifyEmail(param.getEmail(), locale);
			emailUnique = emailUnique.getAsJsonObject();
			String jsonResult = emailUnique.get("result").getAsString();
			if (!jsonResult.equals(CommonCode.STRING_VERIFY_EMAIL_SUCCESS)) {
				result.addProperty("result", CommonCode.INT_RESULT_EXIST);
				result.addProperty("errer", "The email exists.");
				return result.toString();
			}

			UserInformation savedInfo = userService.getUser(param.getUsername());
			savedInfo.setAddress2(param.getAddress2());
			savedInfo.setAddress1(param.getAddress1());
			savedInfo.setFirstName(param.getFirstName());
			savedInfo.setLastName(param.getLastName());
			savedInfo.setMobile(param.getMobile());
			savedInfo.setPassport(param.getPassport());
			savedInfo.setDob(param.getDob());

			if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("ROLE_ADMIN")) {
				savedInfo.setEmail(param.getEmail());
				savedInfo.setId(param.getEmail());
			} else {
				savedInfo.setEmail(param.getEmail()); // 관리자 아디 변경을 위한
			}

			userService.updateUser(savedInfo);

			if (savedInfo != null) {
				response.setStatus(200);
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				result.addProperty("user", gson.toJson(savedInfo));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		result.addProperty("result", CommonCode.INT_RESULT_SUCCESS);
		return result.toString();
	}

	@RequestMapping(value = "/user/home", method = RequestMethod.GET)
	@ResponseBody
	public String myAccount(HttpServletRequest request, HttpServletResponse response) {
		JsonObject json = new JsonObject();

		UserInformation userInformation = userDetailsService.getCurrentUser();
		
		if (userInformation.getLang() != null) {
			Locale locale = new Locale(userInformation.getLang());
			LocaleContextHolder.setLocale(locale);
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			localeResolver.setLocale(request, response, locale);
		}

		try {
			String transferParamAmount = "";
			String transferParamSrcCurrency = "";
			String transferParamDstCurrency = "";

			json.addProperty("param_amount", transferParamAmount);
			json.addProperty("param_dst_currency", transferParamDstCurrency);
			json.addProperty("param_src_currency", transferParamSrcCurrency);

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setStatus(200);
		return json.toString();
	}

	private String getUserIndexFromSecurityContextHoder() {
		String result = null;
		try {
			UserInformation user = userDetailsService.getCurrentUser();
			result = user.getUsername(); // user PID
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

	@RequestMapping(value = "/user/saveMobileAjax", method = RequestMethod.POST)
	@ResponseBody
	public WebResult saveMobileAjax(HttpServletRequest request, Locale locale, @RequestParam("mobile") String mobile) {
		WebResult result = new WebResult();

		UserInformation user = null;
		try {
			user = userDetailsService.getCurrentUser();
			user.setMobile(mobile);
			if (user != null) {
				userService.updateUser(user);
				result.setResultCode(200);
				result.setResultMessage("update ok");
				result.setData(user);
			}
		} catch (Exception e) {

		}
		return result;
	}

	@RequestMapping(value = "/user/mail", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyMail(HttpServletRequest request,
			@RequestParam(value = "email") String email) {

		Map<String, Object> map = new HashMap<String, Object>();
		UserInformation user = null;

		try {
			user = userDetailsService.getCurrentUser();
			String resultCode = "";
			if (user != null) {
				user.setEmail(email);
				user.setId(email);
				user.setEmailconfirmdate(new Timestamp(System.currentTimeMillis()));
				userService.updateUser(user);
				resultCode = "100";
			} else {
				resultCode = "200";
			}
			map.put(CommonCode.STRING_RESULT, resultCode);

			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			map.put(CommonCode.STRING_RESULT, "300");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/user/appid", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getAppId(HttpServletRequest request) {
			
		JsonObject json = new JsonObject();
			
		json.addProperty("id", env.getProperty("facebook.appKey"));
		json.addProperty("result", CommonCode.INT_RESULT_SUCCESS);
			
		return json.toString();
			
	}

}
