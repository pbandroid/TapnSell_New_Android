package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ItemToShipModal implements Serializable {

	// getter setter for store contact details display
	
	String orderid;
	

	String itemid;
	String userid;
	String categoryid;
	String name;
	String price;
	String address;
	String distance;
	String image;
	String is_shipped;
	
	public String getIs_shipped() {
		return is_shipped;
	}
	public void setIs_shipped(String is_shipped) {
		this.is_shipped = is_shipped;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	
	public ItemToShipModal(String orderid, String itemid, String userid,
			String categoryid, String name, String price, String address,
			String distance, String image, String is_shipped) {
		super();
		this.orderid = orderid;
		this.itemid = itemid;
		this.userid = userid;
		this.categoryid = categoryid;
		this.name = name;
		this.price = price;
		this.address = address;
		this.distance = distance;
		this.image = image;
		this.is_shipped = is_shipped;
	}
	
	public ItemToShipModal() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	

	
}
