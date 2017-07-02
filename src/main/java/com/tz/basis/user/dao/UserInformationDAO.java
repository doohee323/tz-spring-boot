package com.tz.basis.user.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.tz.basis.user.UserInformation;

@Repository
@SuppressWarnings("unchecked")
public class UserInformationDAO {

	private static final Logger logger = LoggerFactory.getLogger(UserInformationDAO.class);

	@PersistenceContext
	private EntityManager em;

	private Gson gson = new Gson();
	
	private static final String FROM_TABLE = "SELECT * FROM user_information";

	public UserInformation get(String userIndex) {
		UserInformation result = (UserInformation) em.find(UserInformation.class, userIndex);
		return result;
	}

	public void add(UserInformation user) {
		user.setCreated(new Timestamp(System.currentTimeMillis()));
		user.setUpdated(user.getCreated());
		user.setLastvisited(user.getCreated());
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		logger.debug(gson.toJson(user));
		
		em.persist(user);
	}

	public long getCount(String searchKeyword) {
		String where = "";
		long result = 0;
		if (StringUtils.isNotEmpty(searchKeyword)) {
			where = " upper(email) like :searchKeyword " + " or upper(first_name) like :searchKeyword "
					+ " or upper(last_name) like :searchKeyword "
					+ " or replace(upper(CONCAT(first_name, last_name)),' ','') like :searchKeyword "
					+ " or replace(upper(CONCAT(last_name, first_name)), ' ', '') like :searchKeyword ";
		}

		if (where.length() > 0) {
			where = " where " + where;
		}
		Query query = em.createNativeQuery("select count(*) from user_information " + where + " order by created_at DESC ");
		if (StringUtils.isNotEmpty(searchKeyword)) {
			query.setParameter("searchKeyword", "%" + searchKeyword.toUpperCase().replaceAll(" ", "") + "%");
		}
		result = ((BigInteger) query.getSingleResult()).longValue();
		return result;
	}

	public ArrayList<UserInformation> getList(String searchKeyword) {
		List<UserInformation> userList = null;
		String where = "";
		if (StringUtils.isNotEmpty(searchKeyword)) {
			where = " upper(email) like :searchKeyword " + " or upper(first_name) like :searchKeyword "
					+ " or upper(last_name) like :searchKeyword "
					+ " or replace(upper(CONCAT(first_name,last_name)),' ','') like :searchKeyword "
					+ " or replace(upper(CONCAT(last_name,first_name)), ' ', '') like :searchKeyword ";
		}

		if (where.length() > 0) {
			where = " where " + where;
		}

		Query query = em.createNativeQuery(FROM_TABLE + where + " order by created DESC", UserInformation.class);
		if (StringUtils.isNotEmpty(searchKeyword)) {
			query.setParameter("searchKeyword", "%" + searchKeyword.toUpperCase().replaceAll(" ", "") + "%");
		}
		userList = query.getResultList();

		ArrayList<UserInformation> result = new ArrayList<UserInformation>();
		if (userList != null && userList.size() > 0) {
			result.addAll(userList);
		}
		return result;
	}

	public void updateUser(UserInformation user) {
		user.setUpdated(new Timestamp(System.currentTimeMillis()));
		em.merge(user);
	}

	public void updateUserVisitData(UserInformation user) {
		user.setLastvisited(new Timestamp(System.currentTimeMillis()));
		em.merge(user);
	}

	public void updateUserLanguage(UserInformation user) {
		String hqlUpdate = "update user_information set lang = :lang where username = :username";
		int updatedEntities = em.createNativeQuery(hqlUpdate).setParameter("lang", user.getLang())
				.setParameter("username", user.getUsername()).executeUpdate();
	}

	public boolean isEmailUnique(String email) {
		Query query = em.createNativeQuery(FROM_TABLE + " where email = :email ", UserInformation.class).setParameter("email", email);
		List<UserInformation> results = query.getResultList();
		if (results.isEmpty()) {
			return true;
		}
		return false;
	}

	public UserInformation getUserById(String id) {
		UserInformation result = null;
		Query query = em.createNativeQuery(FROM_TABLE + " where id = :id ", UserInformation.class).setParameter("id", id);
		List<UserInformation> results = query.getResultList();
		if (!results.isEmpty()) {
			result = (UserInformation) results.get(0);
		}
		return result;
	}

	public UserInformation getUserByEmail(String email) {
		UserInformation result = null;
		Query query = em.createNativeQuery(FROM_TABLE + " where email = :email ", UserInformation.class)
				.setParameter("email", email);
		List<UserInformation> results = query.getResultList();
		if (!results.isEmpty()) {
			result = (UserInformation) results.get(0);
		}
		return result;
	}

	public UserInformation getUserByReferralId(String referralId) {
		UserInformation result = null;
		Query query = em.createNativeQuery(FROM_TABLE + " where referral_id = :referralId ", UserInformation.class)
				.setParameter("referralId", referralId);
		List<UserInformation> results = query.getResultList();
		if (!results.isEmpty()) {
			result = (UserInformation) results.get(0);
		}
		return result;
	}

	public UserInformation getUserByMobile(String mobile) {
		UserInformation result = null;
		Query query = em.createNativeQuery(FROM_TABLE + " where mobile = :mobile ", UserInformation.class)
				.setParameter("mobile", mobile);
		List<UserInformation> results = query.getResultList();
		if (!results.isEmpty()) {
			result = (UserInformation) results.get(0);
		}
		return result;
	}
	

}
