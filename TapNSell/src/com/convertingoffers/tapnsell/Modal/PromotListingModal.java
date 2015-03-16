package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PromotListingModal implements Serializable {

	String itemid;
	String userid;
	String name;
	String price;
	String reserved;
	String distance;
	String image;
	String has_like;
	String viewhide;
	String no_of_likes;
	String url;
	boolean status_btn;
	
	
	public boolean isStatus_btn() {
		return status_btn;
	}

	public void setStatus_btn(boolean status_btn) {
		this.status_btn = status_btn;
	}
	
	// getter setter for store contact details display
	
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




	public String getReserved() {
		return reserved;
	}




	public void setReserved(String reserved) {
		this.reserved = reserved;
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




	public String getViewhide() {
		return viewhide;
	}




	public void setViewhide(String viewhide) {
		this.viewhide = viewhide;
	}




	public String getNo_of_likes() {
		return no_of_likes;
	}




	public void setNo_of_likes(String no_of_likes) {
		this.no_of_likes = no_of_likes;
	}




	public String getUrl() {
		return url;
	}




	public void setUrl(String url) {
		this.url = url;
	}



	



	public PromotListingModal(String itemid, String userid, String name,
			String price, String reserved, String distance, String image,
			String has_like, String viewhide, String no_of_likes, String url,
			boolean status_btn) {
		super();
		this.itemid = itemid;
		this.userid = userid;
		this.name = name;
		this.price = price;
		this.reserved = reserved;
		this.distance = distance;
		this.image = image;
		this.has_like = has_like;
		this.viewhide = viewhide;
		this.no_of_likes = no_of_likes;
		this.url = url;
		this.status_btn = status_btn;
	}
	
	public PromotListingModal() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

	
}
