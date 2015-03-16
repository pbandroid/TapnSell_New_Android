package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ExpireModal implements Serializable {

	// getter setter for store contact details display
	
	String itemid;
	String userid;
	String name;
	String price;
	String distance;
	String image;
	String has_like;
	String	no_of_likes;
	
	public ExpireModal() {
		super();
		// TODO Auto-generated constructor stub
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

	public ExpireModal(String itemid, String userid, String name, String price,
			String distance, String image, String has_like, String no_of_likes) {
		super();
		this.itemid = itemid;
		this.userid = userid;
		this.name = name;
		this.price = price;
		this.distance = distance;
		this.image = image;
		this.has_like = has_like;
		this.no_of_likes = no_of_likes;
	}
	
}
