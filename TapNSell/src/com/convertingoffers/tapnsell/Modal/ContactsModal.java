package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ContactsModal implements Serializable {

	// getter setter for store contact details display
	
	String id;
	String displayName;
	String phoneNumber;
	String emailAddress;
	String check;
	
	public ContactsModal(String id, String displayName, String phoneNumber,
			String emailAddress, String check) {
		super();
		this.id = id;
		this.displayName = displayName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.check = check;
	}
	
	
	public ContactsModal() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
}
