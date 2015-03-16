package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MyOrderModal implements Serializable {
	
// getter  setter for image path display
	
	String orderid;
	String itemid;
	String itemuserid;
	String name;
	String image;
	String price;
	String orderdate;
	String deliveryestimate;
	String shippingservice;
	String delivereddate;
	String processing;
	String shipping;
	String transit;
	String delivered;

	public MyOrderModal(String orderid, String itemid, String itemuserid,
		String name, String image, String price, String orderdate,
		String deliveryestimate, String shippingservice, String delivereddate,
		String processing, String shipping, String transit, String delivered) {
	super();
	this.orderid = orderid;
	this.itemid = itemid;
	this.itemuserid = itemuserid;
	this.name = name;
	this.image = image;
	this.price = price;
	this.orderdate = orderdate;
	this.deliveryestimate = deliveryestimate;
	this.shippingservice = shippingservice;
	this.delivereddate = delivereddate;
	this.processing = processing;
	this.shipping = shipping;
	this.transit = transit;
	this.delivered = delivered;
}


	public MyOrderModal() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public String getItemuserid() {
		return itemuserid;
	}

	public void setItemuserid(String itemuserid) {
		this.itemuserid = itemuserid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}

	public String getDeliveryestimate() {
		return deliveryestimate;
	}

	public void setDeliveryestimate(String deliveryestimate) {
		this.deliveryestimate = deliveryestimate;
	}

	public String getShippingservice() {
		return shippingservice;
	}

	public void setShippingservice(String shippingservice) {
		this.shippingservice = shippingservice;
	}

	public String getDelivereddate() {
		return delivereddate;
	}

	public void setDelivereddate(String delivereddate) {
		this.delivereddate = delivereddate;
	}

	public String getProcessing() {
		return processing;
	}

	public void setProcessing(String processing) {
		this.processing = processing;
	}

	public String getShipping() {
		return shipping;
	}

	public void setShipping(String shipping) {
		this.shipping = shipping;
	}

	public String getTransit() {
		return transit;
	}

	public void setTransit(String transit) {
		this.transit = transit;
	}

	public String getDelivered() {
		return delivered;
	}

	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}

	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}
	
}
