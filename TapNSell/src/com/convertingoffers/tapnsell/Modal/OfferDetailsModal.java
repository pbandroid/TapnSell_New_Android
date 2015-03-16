package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OfferDetailsModal implements Serializable {
	
// getter  setter for image path display
	String item_id;
	
	String item_name;
	String offerid;
	String	offeramount;
	String	itemprice;
	String	image;
	String	to_id;
	String isbuyer;
	String expiretime;
	
	public OfferDetailsModal() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public OfferDetailsModal(String item_id, String item_name, String offerid,
			String offeramount, String itemprice, String image, String to_id,
			String isbuyer, String expiretime) {
		super();
		this.item_id = item_id;
		this.item_name = item_name;
		this.offerid = offerid;
		this.offeramount = offeramount;
		this.itemprice = itemprice;
		this.image = image;
		this.to_id = to_id;
		this.isbuyer = isbuyer;
		this.expiretime = expiretime;
	}
	
	
	public String getExpiretime() {
		return expiretime;
	}

	public void setExpiretime(String expiretime) {
		this.expiretime = expiretime;
	}
	
	public String getIsbuyer() {
		return isbuyer;
	}

	public void setIsbuyer(String isbuyer) {
		this.isbuyer = isbuyer;
	}
	public String getTo_id() {
		return to_id;
	}

	public void setTo_id(String to_id) {
		this.to_id = to_id;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getOfferid() {
		return offerid;
	}
	public void setOfferid(String offerid) {
		this.offerid = offerid;
	}
	public String getOfferamount() {
		return offeramount;
	}
	public void setOfferamount(String offeramount) {
		this.offeramount = offeramount;
	}
	public String getItemprice() {
		return itemprice;
	}
	public void setItemprice(String itemprice) {
		this.itemprice = itemprice;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	

	

	
}
