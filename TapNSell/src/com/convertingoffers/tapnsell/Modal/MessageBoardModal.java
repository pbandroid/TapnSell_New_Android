package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MessageBoardModal implements Serializable {

	String username;
	String userimage;
	String message;
	String itemid;
	String image;
	String name;
	String toid;
	
	// getter setter for store contact details display
	public MessageBoardModal(String username, String userimage, String message,
			String itemid, String image, String name, String to_id) {
		super();
		this.username = username;
		this.userimage = userimage;
		this.message = message;
		this.itemid = itemid;
		this.image = image;
		this.name = name;
		this.toid = to_id;
	}
	public MessageBoardModal() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTo_id() {
		return toid;
	}
	public void setTo_id(String toid) {
		this.toid = toid;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
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
