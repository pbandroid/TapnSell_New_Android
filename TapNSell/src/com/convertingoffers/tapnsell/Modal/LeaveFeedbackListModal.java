package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LeaveFeedbackListModal implements Serializable {

	// getter setter for store contact details display
	
	String itemid;
	String orderid;
	String userid;
	String name;
	String price;
	String username;
	String userimage;
	String distance;
	String image;
	
	
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserimage() {
		return userimage;
	}
	public void setUserimage(String userimage) {
		this.userimage = userimage;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	public LeaveFeedbackListModal() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LeaveFeedbackListModal(String itemid, String orderid, String userid,
			String name, String price, String username, String userimage,
			String distance, String image) {
		super();
		this.itemid = itemid;
		this.orderid = orderid;
		this.userid = userid;
		this.name = name;
		this.price = price;
		this.username = username;
		this.userimage = userimage;
		this.distance = distance;
		this.image = image;
	}
	
	
	
}
