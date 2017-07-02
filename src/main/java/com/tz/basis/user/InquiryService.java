package com.tz.basis.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tz.basis.user.dao.InquiryDAO;

@Service
@Transactional
public class InquiryService {

	private static final Logger logger = LoggerFactory.getLogger(InquiryService.class);
	
	@Autowired
	private InquiryDAO inquiryDAO;

	public InquiryDAO getInquiryDAO() {
		return inquiryDAO;
	}

	public void setInquiryDAO(InquiryDAO inquiryDAO) {
		this.inquiryDAO = inquiryDAO;
	}

	public void InquiryInsert(Inquiry data){
		inquiryDAO.add(data);
	}
	
	public Inquiry getInquiry(String id){
		return (Inquiry) inquiryDAO.get(id);
	}

	public List<Inquiry> getInquiryList(String searchKeyword){
		return (List<Inquiry>) inquiryDAO.getList(searchKeyword);
	}
}
