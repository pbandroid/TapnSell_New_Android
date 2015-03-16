package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CategoryModal implements Serializable {
	
// getter setter for brouse category 
	String id;
	String name;
	String image;
	String no_of_subcategory;

	public CategoryModal() {
		super();
		
	}

	public CategoryModal(String id, String name, String image,
			String no_of_subcategory) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.no_of_subcategory = no_of_subcategory;
	}
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public String getNo_of_subcategory() {
		return no_of_subcategory;
	}


	public void setNo_of_subcategory(String no_of_subcategory) {
		this.no_of_subcategory = no_of_subcategory;
	}

}
