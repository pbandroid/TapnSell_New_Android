package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BillingModal implements Serializable {
	
// getter setter for brouse category 
	String address_id;
	String user_id;
	String type;
	String first_name;
	String last_name;
	String mobile_no;
	String address;
	String appartment;
	String joinaddress;
	String city;
	String state;
	String country;
	String zipcode;
	String email;
	
	public BillingModal(String address_id, String user_id, String type,
			String first_name, String last_name, String mobile_no, String address,
			String appartment, String joinaddress, String city, String state,
			String country, String zipcode, String email) {
		super();
		this.address_id = address_id;
		this.user_id = user_id;
		this.type = type;
		this.first_name = first_name;
		this.last_name = last_name;
		this.mobile_no = mobile_no;
		this.address = address;
		this.appartment = appartment;
		this.joinaddress = joinaddress;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipcode = zipcode;
		this.email = email;
	}

	
	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAppartment() {
		return appartment;
	}

	public void setAppartment(String appartment) {
		this.appartment = appartment;
	}

	public String getJoinaddress() {
		return joinaddress;
	}

	public void setJoinaddress(String joinaddress) {
		this.joinaddress = joinaddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public BillingModal() {
		super();
		
	}

	



}
