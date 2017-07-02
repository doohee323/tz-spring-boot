package com.tz.basis.user.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tz.basis.user.Inquiry;

@Component
public class InquiryDAO {

	private static final Logger logger = LoggerFactory.getLogger(InquiryDAO.class);

	@PersistenceContext
	private EntityManager em;

	public Inquiry get(String id) {
		Inquiry result = null;
		result = (Inquiry) em.find(Inquiry.class, id);
		return result;
	}

	public boolean add(Inquiry info) {
		boolean result = false;
		info.setCreated(new Timestamp(System.currentTimeMillis()));
		em.persist(info);
		result = true;
		return result;

	}

	@SuppressWarnings("unchecked")
	public List<Inquiry> getList(String searchKeyword) {
		List<Inquiry> list = null;
		String where = "";
		if (searchKeyword.isEmpty()) {
			where = " where id = :ids ";
		}

		Query query = em.createNativeQuery("SELECT * FROM inquiry " + where + " order by created_at DESC");
		if (searchKeyword.isEmpty()) {
			query.setParameter("ids", searchKeyword);
		}
		list = query.getResultList();
		return list;
	}

}
