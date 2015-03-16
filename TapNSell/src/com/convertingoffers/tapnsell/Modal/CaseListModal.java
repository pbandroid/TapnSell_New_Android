package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CaseListModal implements Serializable {

	
	// getter setter for store contact details display
	String itemname;
	String itemimage;
	String caseid;
	String fromuserid;
	String fromusername;
	String userid;
	String tomusername;
	String order_no;
	String name;
	String reason;
	String casenumber;
	String orderamount;
	String respond;
	String hour;
	String minute;
	String second;
	String chatcount;
	String caseimage;
	String itemid;
	String	Timestamp;
	
	
	public CaseListModal() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CaseListModal(String itemid,String itemname, String itemimage, String caseid,
			String fromuserid, String fromusername, String userid,
			String tomusername, String order_no, String name, String reason,
			String casenumber, String orderamount, String respond, String hour,
			String minute, String second, String chatcount, String caseimage,
			 String timestamp) {
		super();
		this.itemname = itemname;
		this.itemimage = itemimage;
		this.caseid = caseid;
		this.fromuserid = fromuserid;
		this.fromusername = fromusername;
		this.userid = userid;
		this.tomusername = tomusername;
		this.order_no = order_no;
		this.name = name;
		this.reason = reason;
		this.casenumber = casenumber;
		this.orderamount = orderamount;
		this.respond = respond;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.chatcount = chatcount;
		this.caseimage = caseimage;
		this.itemid = itemid;
		Timestamp = timestamp;
	}

	public String getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}

	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getItemimage() {
		return itemimage;
	}
	public void setItemimage(String itemimage) {
		this.itemimage = itemimage;
	}
	public String getCaseid() {
		return caseid;
	}
	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}
	public String getFromuserid() {
		return fromuserid;
	}
	public void setFromuserid(String fromuserid) {
		this.fromuserid = fromuserid;
	}
	public String getFromusername() {
		return fromusername;
	}
	public void setFromusername(String fromusername) {
		this.fromusername = fromusername;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getTomusername() {
		return tomusername;
	}
	public void setTomusername(String tomusername) {
		this.tomusername = tomusername;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getCasenumber() {
		return casenumber;
	}
	public void setCasenumber(String casenumber) {
		this.casenumber = casenumber;
	}
	public String getOrderamount() {
		return orderamount;
	}
	public void setOrderamount(String orderamount) {
		this.orderamount = orderamount;
	}
	public String getRespond() {
		return respond;
	}
	public void setRespond(String respond) {
		this.respond = respond;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
	public String getChatcount() {
		return chatcount;
	}
	public void setChatcount(String chatcount) {
		this.chatcount = chatcount;
	}
	public String getCaseimage() {
		return caseimage;
	}
	public void setCaseimage(String caseimage) {
		this.caseimage = caseimage;
	}
	
	
	
	
	

	
}
