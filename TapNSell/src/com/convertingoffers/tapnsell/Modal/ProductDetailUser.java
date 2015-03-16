package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductDetailUser implements Serializable {
	
// getter  setter for image path display
	String User_user_id;
	String User_name;
	String User_image;
	
	public ProductDetailUser(String user_user_id, String user_name,
			String user_image) {
		super();
		User_user_id = user_user_id;
		User_name = user_name;
		User_image = user_image;
	}
	public ProductDetailUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUser_user_id() {
		return User_user_id;
	}

	public void setUser_user_id(String user_user_id) {
		User_user_id = user_user_id;
	}

	public String getUser_name() {
		return User_name;
	}

	public void setUser_name(String user_name) {
		User_name = user_name;
	}

	public String getUser_image() {
		return User_image;
	}

	public void setUser_image(String user_image) {
		User_image = user_image;
	}

	

	
}
