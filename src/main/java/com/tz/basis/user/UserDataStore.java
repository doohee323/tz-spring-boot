package com.tz.basis.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tz.basis.user.dao.UserInformationDAO;

@Component
@Transactional
public class UserDataStore {

	private static long ID = 0;

	private static final Logger logger = LoggerFactory.getLogger(UserDataStore.class);

	private static HashMap<String, UserInformation> data;

	@Autowired
	UserInformationDAO userInformationDAO;

	public UserInformation get(String index) {
		return userInformationDAO.get(index);
	}

	public void add(UserInformation user) {
		if (data == null) {
			data = new HashMap<String, UserInformation>();
		}
		if (user == null) {
			return;
		}
		if (user.getUsername() == null) {
			logger.debug("user index null");
			return;
		}

		UserInformation duplicatedUser = userInformationDAO.getUserById(user.getId());
		if (duplicatedUser == null) {
			userInformationDAO.add(user);
		}
	}

	public ArrayList<UserInformation> getList(String searchKeyword) {
		return userInformationDAO.getList(searchKeyword);
	}

	public long getCount(String searchKeyword) {
		return userInformationDAO.getCount(searchKeyword);
	}

	public void updateUser(UserInformation user) {
		// data.put(user.getUserIndex(), user);

		userInformationDAO.updateUser(user);
	}

	public void updateUserVisitData(UserInformation user) {
		userInformationDAO.updateUserVisitData(user);
	}

	public void updateUserLanguage(UserInformation user) {
		userInformationDAO.updateUserLanguage(user);
	}

	public boolean isEmailUnique(String email) {
		return userInformationDAO.isEmailUnique(email);
	}

	public UserInformation getUserById(String id) {
		if (userInformationDAO == null) {
			logger.debug("userInformationDAO null");
		}
		return userInformationDAO.getUserById(id);
	}

	public UserInformation getUserByEmail(String email) {
		if (userInformationDAO == null) {
			logger.debug("userInformationDAO null");
		}
		return userInformationDAO.getUserByEmail(email);
	}

	public UserInformation getUserByReferralId(String referralId) {

		if (userInformationDAO == null) {
			logger.debug("userInformationDAO null");
		}
		return userInformationDAO.getUserByReferralId(referralId);
	}

	public UserInformation getUserByMobile(String mobile) {
		return userInformationDAO.getUserByMobile(mobile);
	}
}
