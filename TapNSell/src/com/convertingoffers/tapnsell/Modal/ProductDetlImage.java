package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductDetlImage implements Serializable {
	
// getter  setter for image path display
	String image;


	public ProductDetlImage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductDetlImage(String image) {
		super();
		this.image = image;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	
}
