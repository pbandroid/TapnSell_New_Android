package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MeetUpListModal implements Serializable {

	// getter setter for store contact details display
	String id;
	String userid;
	String isbuyer;
	String itemid;
	String name;
	String image;
	String userimage;
	String title;
	String latitude;
	String longitude;
	String address;
	String time;
	String date;
	String day;
	
	public MeetUpListModal() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MeetUpListModal(String id, String userid, String isbuyer,
			String itemid, String name, String image, String userimage,
			String title, String latitude, String longitude, String address,
			String time, String date, String day) {
		super();
		this.id = id;
		this.userid = userid;
		this.isbuyer = isbuyer;
		this.itemid = itemid;
		this.name = name;
		this.image = image;
		this.userimage = userimage;
		this.title = title;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.time = time;
		this.date = date;
		this.day = day;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getIsbuyer() {
		return isbuyer;
	}
	public void setIsbuyer(String isbuyer) {
		this.isbuyer = isbuyer;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
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
	public String getUserimage() {
		return userimage;
	}
	public void setUserimage(String userimage) {
		this.userimage = userimage;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	
	
}
