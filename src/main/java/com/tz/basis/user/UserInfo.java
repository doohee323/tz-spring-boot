package com.tz.basis.user;

import java.sql.Date;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfo {

	private static final Logger logger = LoggerFactory.getLogger(UserInfo.class);
	
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String mobile;
	private String address1;
	private String address2;
	private String city;
	private String country;
	private Timestamp emailconfirmdate;
	private Timestamp updated;
	private Timestamp created;
	private Timestamp lastvisited;
	private Date dob;	//Date of Birth
	private String passport;
	private String lang;
	private String referralId;
	private String hostId;
	private String timezone;
	private String username;
	private String memo;
	private String fxrateEmailRefusal;
	private String mktEmailRefusal;

	public String getReferralId() {
		return referralId;
	}

	public void setReferralId(String referralId) {
		this.referralId = referralId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userIndex) {
		this.username = userIndex;
	}

	public Timestamp getEmailconfirmdate() {
		return emailconfirmdate;
	}

	public void setEmailconfirmdate(Timestamp emailconfirmdate) {
		this.emailconfirmdate = emailconfirmdate;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}	

	public Timestamp getLastvisited() {
		return lastvisited;
	}

	public void setLastvisited(Timestamp lastvisited) {
		this.lastvisited = lastvisited;
	}	
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}	
	
	public String getTimezone() {
		return timezone;
	}
	
	public void setTimezone(String timezone) {
		this.timezone = timezone;
		
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getFxrateEmailRefusal() {
		return fxrateEmailRefusal;
	}

	public void setFxrateEmailRefusal(String fxrateEmailRefusal) {
		this.fxrateEmailRefusal = fxrateEmailRefusal;
	}

	public String getMktEmailRefusal() {
		return mktEmailRefusal;
	}

	public void setMktEmailRefusal(String mktEmailRefusal) {
		this.mktEmailRefusal = mktEmailRefusal;
	}

	@Override
	public String toString() {
		return "UserInformation2 [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", mobile=" + mobile + ", address1=" + address1 + ", address2=" + address2
				+ ", city=" + city + ", country=" + country + ", emailconfirmdate=" + emailconfirmdate + ", updated="
				+ updated + ", created=" + created + ", lastvisited=" + lastvisited + ", dob=" + dob + ", passport="
				+ passport + ", lang=" + lang + ", referralId=" + referralId + ", hostId="
				+ hostId + ", timezone=" + timezone + ", username=" + username + ", memo=" + memo + "]";
	}

}
