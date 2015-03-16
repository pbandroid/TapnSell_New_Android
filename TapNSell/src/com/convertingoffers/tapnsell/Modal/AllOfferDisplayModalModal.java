package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AllOfferDisplayModalModal implements Serializable {

	// getter setter for store contact details display
	
	String offer_id;
	String user_id;
	String name;
	String user_image;
	String offer;
	String time;
	String timestamp;
	String isbuyer;
	
	public AllOfferDisplayModalModal(String offer_id, String user_id,
			String name, String user_image, String offer, String time,
			String timestamp, String isbuyer) {
		super();
		this.offer_id = offer_id;
		this.user_id = user_id;
		this.name = name;
		this.user_image = user_image;
		this.offer = offer;
		this.time = time;
		this.timestamp = timestamp;
		this.isbuyer = isbuyer;
	}
	
	public String getIsbuyer() {
		return isbuyer;
	}

	public void setIsbuyer(String isbuyer) {
		this.isbuyer = isbuyer;
	}
	public AllOfferDisplayModalModal() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getOffer_id() {
		return offer_id;
	}
	public void setOffer_id(String offer_id) {
		this.offer_id = offer_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUser_image() {
		return user_image;
	}
	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}
	public String getOffer() {
		return offer;
	}
	public void setOffer(String offer) {
		this.offer = offer;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
