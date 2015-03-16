package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SoldModal implements Serializable {

	// getter setter for store contact details display
	
	String orderid;
	






	String buyerid;
	String username;
	String userimage;
	String itemid;
	String userid;
	String name;
	String	price;
	String	distance;
	String	image;
	String	has_like;
	String	no_of_likes;
	String hasrefundrequest;
	String refunded;
	
	public String getHasrefundrequest() {
		return hasrefundrequest;
	}




	public void setHasrefundrequest(String hasrefundrequest) {
		this.hasrefundrequest = hasrefundrequest;
	}




	public String getRefunded() {
		return refunded;
	}




	public void setRefunded(String refunded) {
		this.refunded = refunded;
	}






	
	public String getOrderid() {
		return orderid;
	}




	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}




	public String getBuyerid() {
		return buyerid;
	}




	public void setBuyerid(String buyerid) {
		this.buyerid = buyerid;
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




	public String getHas_like() {
		return has_like;
	}




	public void setHas_like(String has_like) {
		this.has_like = has_like;
	}




	public String getNo_of_likes() {
		return no_of_likes;
	}




	public void setNo_of_likes(String no_of_likes) {
		this.no_of_likes = no_of_likes;
	}



	
	public SoldModal(String orderid, String buyerid, String username,
			String userimage, String itemid, String userid, String name,
			String price, String distance, String image, String has_like,
			String no_of_likes, String hasrefundrequest, String refunded) {
		super();
		this.orderid = orderid;
		this.buyerid = buyerid;
		this.username = username;
		this.userimage = userimage;
		this.itemid = itemid;
		this.userid = userid;
		this.name = name;
		this.price = price;
		this.distance = distance;
		this.image = image;
		this.has_like = has_like;
		this.no_of_likes = no_of_likes;
		this.hasrefundrequest = hasrefundrequest;
		this.refunded = refunded;
	}
	
	
	public SoldModal() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
}
