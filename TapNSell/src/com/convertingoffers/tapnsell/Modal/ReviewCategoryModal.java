package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ReviewCategoryModal implements Serializable {
	
// getter setter for brouse category 
	
	String username;
	String image;
	String comment;
	String rating;
	String date;
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ReviewCategoryModal() {
		super();
		
	}

	public ReviewCategoryModal(String username, String image, String comment,
			String rating, String date) {
		super();
		this.username = username;
		this.image = image;
		this.comment = comment;
		this.rating = rating;
		this.date = date;
	}

}
