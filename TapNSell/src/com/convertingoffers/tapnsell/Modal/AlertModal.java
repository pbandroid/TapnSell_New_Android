package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AlertModal implements Serializable {

	// getter setter for store contact details display
	
	String id;
	String image;
	String name;
	String email;
	String push;
	String sms;
	boolean boolemail;
	boolean boolpush;
	boolean boolsms;
	
	


	public AlertModal(String id, String image, String name, String email,
			String push, String sms, boolean boolemail, boolean boolpush,
			boolean boolsms) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.email = email;
		this.push = push;
		this.sms = sms;
		this.boolemail = boolemail;
		this.boolpush = boolpush;
		this.boolsms = boolsms;
	}
	
	
	public boolean isBoolemail() {
		return boolemail;
	}


	public void setBoolemail(boolean boolemail) {
		this.boolemail = boolemail;
	}


	public boolean isBoolpush() {
		return boolpush;
	}


	public void setBoolpush(boolean boolpush) {
		this.boolpush = boolpush;
	}


	public boolean isBoolsms() {
		return boolsms;
	}


	public void setBoolsms(boolean boolsms) {
		this.boolsms = boolsms;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPush() {
		return push;
	}

	public void setPush(String push) {
		this.push = push;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	
	
	public AlertModal() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
}
