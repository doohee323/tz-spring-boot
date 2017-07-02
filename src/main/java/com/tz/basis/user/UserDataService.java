package com.tz.basis.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.tz.basis.user.dao.UserInformationDAO;
import com.tz.basis.util.CommonCode;

@Service
@Transactional
public class UserDataService implements MessageSourceAware {

	private static final Logger logger = LoggerFactory.getLogger(UserDataService.class);

	private static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private final static char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private static final int MAX_REFERRAL_ID_LENGTH = 6;

	@Autowired
	private UserDataStore userDataStore;
	
	@Autowired
	private UserDetailsService userDetailsService;

	private MessageSource messageSource;
	
	@Autowired
	UserInformationDAO userInformationDAO;
	

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public boolean isEmailValid(String id) {
		boolean result = false;

		if (id.matches(EMAIL_PATTERN)) {
			result = true;
		}
		return result;

	}

	public boolean isEmailUnique(String email) {

		Authentication auth = userDetailsService.getCurrentAuth();
		if (!auth.getPrincipal().equals("anonymousUser")) { //for membership info modification
			UserInformation user = userDetailsService.getCurrentUser();
			if (auth.getAuthorities().toString().contains("ROLE_ADMIN")) {
				return true;
			} else {
				if (user.getId().equals(email)) {
      					return true;
				}
			}

		}
		return userDataStore.isEmailUnique(email);
	}

	public boolean isPasswordValid(String pass) {
		if (pass.trim().length() < 6) {
			return false;
		}
		return true;
	}

	public void addUser(UserInformation user) {
		user.setReferralId(generateReferralId());
		user.setLang(LocaleContextHolder.getLocale().getLanguage());
		if (user.getUsername() == null) {
			user.setUsername(generateUserIndex());
		}
		userDataStore.add(user);
	}

	/**
	 * 
	 * @param id
	 * @param pass
	 * @param hostId
	 * @return 1: 성공 11: 잘못된 아이디 형식 12: 아이디 중복 21: 잘못된 패스워드 형식
	 */
	public int emailPasswordCheck(UserInformation user) {

		if (!isEmailValid(user.getId())) {
			return 11;
		}
		if (!isEmailUnique(user.getId())) {
			return 12;
		}
		if (!isPasswordValid(user.getPassword())) {
			return 21;
		}
		return 1;
	}

	/**
	 * 
	 * @param id
	 * @param pass
	 * @param hostId
	 * @return 1: 성공 11: 잘못된 아이디 형식 12: 아이디 중복 21: 잘못된 패스워드 형식
	 */
	public int addUserInfo(UserInformation user) {

		int result = emailPasswordCheck(user);
		if (result > 1) {
			return result;
		}
		addUser(user);

		return result;
	}

	public UserInformation getUser(String index) {
		return userDataStore.get(index);
	}

	public UserInformation getUserById(String id) {
		return userDataStore.getUserById(id);
	}

	public UserInformation getUserByReferralId(String referralId) {
		return userDataStore.getUserByReferralId(referralId);
	}

	public ArrayList<UserInformation> getList(String searchKeyword) {
		return userDataStore.getList(searchKeyword);
	}

	public ArrayList<UserInformation> getList() {
		return userDataStore.getList(null);
	}

	private String generateUserIndex() {
		return UUID.randomUUID().toString();
	}

	public boolean updateUser(UserInformation user) {
		userDataStore.updateUser(user);
		return true;
	}

	public boolean updateUserVisitData(UserInformation user) {
		userDataStore.updateUserVisitData(user);
		return true;
	}

	public boolean updateUserLanguage(UserInformation user) {
		userDataStore.updateUserLanguage(user);
		return true;
	}

	private String generateToken(String email) {
		String token = UUID.randomUUID().toString();
		MessageDigest md1;
		try {
			md1 = MessageDigest.getInstance("MD5");
			md1.update(email.getBytes());
			byte[] bd1 = md1.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < bd1.length; i++) {
				String hex = Integer.toHexString(0xff & bd1[i]);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			token = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return token;
	}

	public String getUserConfirmationToken(String id) {
		String token = generateToken(id + System.currentTimeMillis());
		tmpConfirmMap.remove(id);
		tmpConfirmMap.put(id, token);
		return token;
	}

	HashMap<String, String> tmpConfirmMap = new HashMap<String, String>();

	public UserInformation findUserByEmail(String userEmail) {
		return userDataStore.getUserByEmail(userEmail);
	}

	Random random = new Random(System.currentTimeMillis());

	public synchronized String generateReferralId() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < MAX_REFERRAL_ID_LENGTH; i++) {
			sb.append(DIGITS[random.nextInt(DIGITS.length)]);
		}
		return sb.toString();
	}

	public UserInformation getUserByMobile(String mobile) {
		return userDataStore.getUserByMobile(mobile);
	}
	
	// check whether user has approved email/mobile information
	public Map<String, Object> checkUserCertification() {

		Map<String, Object> map = new HashMap<>();
		UserInformation user = userDetailsService.getCurrentUser();;
		UserInformation userTemp = getUser(user.getUsername());

		if (userTemp.getMobile() == null || userTemp.getMobile().equals("")) {
			map.put("mobile", "false");
		} else {
			map.put("mobile", "true");
		}

		if (userTemp.getEmailconfirmdate() == null || userTemp.getEmailconfirmdate().equals("")) {
			map.put("email", "false");
		} else {
			map.put("email", "true");
		}

		return map;
	}

	public List<Boolean> verifyMember(List<String> emailList) {

		List<Boolean> result = new ArrayList<Boolean>();
		Boolean bool;

		for (int i = 0; i < emailList.size(); i++) {

			bool = true;

			if (getUserById(emailList.get(i)) != null) {
				bool = false;
			}

			result.add(i, bool);
		}

		return result;

	}

	/**
	 * 
	 * @param userEmail
	 * @return result : 200 = SUCCESSS ,400 = FAIL,500 = EXCEPTION / resultMessage
	 *         = message
	 */
	public JsonObject verifyEmail(String userEmail, Locale locale) {

		JsonObject result = new JsonObject();
		try {
			if (!isEmailValid(userEmail)) {
				result.addProperty(CommonCode.STRING_RESULT, CommonCode.STRING_VERIFY_EMAIL_FAIL);
				result.addProperty(CommonCode.STRING_RESULT_MESSAGE,
						messageSource.getMessage("signin.alert.invalid.email", null, locale));
				return result;
			}

			if (!isEmailUnique(userEmail)) {
				result.addProperty(CommonCode.STRING_RESULT, CommonCode.STRING_VERIFY_EMAIL_FAIL);
				result.addProperty(CommonCode.STRING_RESULT_MESSAGE,
						messageSource.getMessage("signin.alert.existing.email", null, locale));
				return result;
			}
			result.addProperty(CommonCode.STRING_RESULT, CommonCode.STRING_VERIFY_EMAIL_SUCCESS);

		} catch (Exception e) {
			logger.debug("Error => " + e);
			result.addProperty(CommonCode.STRING_RESULT, CommonCode.STRING_VERIFY_EMAIL_EXCEPTION);
			result.addProperty(CommonCode.STRING_RESULT_MESSAGE, "error => " + e);
			return result;
		}

		return result;
	}
	
}
