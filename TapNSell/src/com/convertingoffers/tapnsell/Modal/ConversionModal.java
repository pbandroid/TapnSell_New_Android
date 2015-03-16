package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ConversionModal implements Serializable {
	
	// getter setter for chat modal

	String message_id;
	String msg;
	String msg_type;
	String msg_image;
	String user_type;
	String user_image;
	String user_name;
	String date;

	public ConversionModal(String message_id, String msg, String msg_type,
			String msg_image, String user_type, String user_image,
			String user_name, String date) {
		super();
		this.message_id = message_id;
		this.msg = msg;
		this.msg_type = msg_type;
		this.msg_image = msg_image;
		this.user_type = user_type;
		this.user_image = user_image;
		this.user_name = user_name;
		this.date = date;
	}
	public ConversionModal() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}
	public String getMsg_image() {
		return msg_image;
	}
	public void setMsg_image(String msg_image) {
		this.msg_image = msg_image;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getUser_image() {
		return user_image;
	}
	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	

	
}
