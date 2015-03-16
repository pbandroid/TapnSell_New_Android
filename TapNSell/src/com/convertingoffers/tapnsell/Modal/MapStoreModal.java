package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MapStoreModal implements Serializable {

	// getter setter for store contact details display
	
	String id;
	String name;
	String icon;
	String lat;
	String lng;
	String address;
	String distance;
	
	public MapStoreModal(String id, String name, String icon, String lat,
			String lng, String address, String distance) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.lat = lat;
		this.lng = lng;
		this.address = address;
		this.distance = distance;
	}


	


	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	public MapStoreModal() {
		super();
		// TODO Auto-generated constructor stub
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


	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public String getLat() {
		return lat;
	}


	public void setLat(String lat) {
		this.lat = lat;
	}


	public String getLng() {
		return lng;
	}


	public void setLng(String lng) {
		this.lng = lng;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}



	
}
