package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductDetailsCommentModal implements Serializable {
	
	// getter setter for store comment in product details 
	String comment;
	String userid;
	String image;
	String name;

	public ProductDetailsCommentModal() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProductDetailsCommentModal(String comment, String userid, String image,
			String name) {
		super();
		this.comment = comment;
		this.userid = userid;
		this.image = image;
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
}
