package com.tz.basis.user;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "user_information")
public class UserInformation implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UserInformation.class);

	private String id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private String email;

	private String password;

	private String mobile;

	private String address1;

	private String address2;

	private String city;

	private String country;

	@Column(name = "email_confirm_date")
	private Timestamp emailconfirmdate;

	@Column(name = "lastvisited_at")
	private Timestamp lastvisited;

	private Date dob; // Date of Birth
	private String passport;

	@Column(name = "visit_cnt")
	private long visitcnt;
	private String lang;

	@Column(name = "referral_id")
	private String referralId;

	@Column(name = "host_id")
	private String hostId;

	private String timezone;

	@Column(name = "fxrate_email_refusal")
	private String fxrateEmailRefusal;

	@Column(name = "mkt_email_refusal")
	private String mktEmailRefusal;

	@Id
	@Column(name = "username")
	private String username;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

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

	public UserInformation() {
		super();
	}

	public UserInformation(String id, String password) {
		super();
		this.id = id;
		this.password = password;
	}

	public UserInformation(UserInformation user) {
		this.address1 = user.getAddress1();
		this.address2 = user.getAddress2();
		this.city = user.getCity();
		this.country = user.country;
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.id = user.getId();
		this.username = user.getUsername();
		this.lastName = user.getLastName();
		this.mobile = user.getMobile();
		this.password = user.getPassword();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String userIndex) {
		this.username = userIndex;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (id != null && id.equals("admin")) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Timestamp getEmailconfirmdate() {
		return emailconfirmdate;
	}

	public void setEmailconfirmdate(Timestamp emailconfirmdate) {
		this.emailconfirmdate = emailconfirmdate;
	}

	public Timestamp getUpdated() {
		return updatedAt;
	}

	public void setUpdated(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Timestamp getCreated() {
		return createdAt;
	}

	public void setCreated(Timestamp createdAt) {
		this.createdAt = createdAt;
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

	public long getVisitcnt() {
		return visitcnt;
	}

	public void setVisitcnt(long visitcnt) {
		this.visitcnt = visitcnt;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UserInformation user = (UserInformation) obj;
		if (!email.equals(user.email)) {
			return false;
		}
		return true;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
