package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ItemDescModal implements Serializable {
	
// getter setter for brouse category 

	String userid;
	String itemid;
	String name;
	String item_description;
	String item_condition;
	String latitude;
	String longitude;
	String item_address;
	String sell_price;
	String quantity;
	String local_pickup;
	String category_id;
	String reserved;
	String distance;
	String Video;
	String no_of_likes;
	String has_like;
	ArrayList<ProductDetlImage> mImage;
	ArrayList<ProductDetailsCommentModal> mComment;
	String seller_name;
	String seller_image;
	String seller_userid;
	boolean apiCall_Status;
	String Default_image;
	String is_follow;
	String rating;
	String ratingcount;
	String shorturl;
	
	
	public String getShorturl() {
		return shorturl;
	}

	public void setShorturl(String shorturl) {
		this.shorturl = shorturl;
	}

	public ItemDescModal(String userid, String itemid, String name,
			String item_description, String item_condition, String latitude,
			String longitude, String item_address, String sell_price,
			String quantity, String local_pickup, String category_id,
			String reserved, String distance, String video, String no_of_likes,
			String has_like, ArrayList<ProductDetlImage> mImage,
			ArrayList<ProductDetailsCommentModal> mComment, String seller_name,
			String seller_image, String seller_userid, boolean apiCall_Status,
			String default_image, String is_follow,String rating,String ratingcount,String shorturl) {
		super();
		this.userid = userid;
		this.itemid = itemid;
		this.name = name;
		this.item_description = item_description;
		this.item_condition = item_condition;
		this.latitude = latitude;
		this.longitude = longitude;
		this.item_address = item_address;
		this.sell_price = sell_price;
		this.quantity = quantity;
		this.local_pickup = local_pickup;
		this.category_id = category_id;
		this.reserved = reserved;
		this.distance = distance;
		Video = video;
		this.no_of_likes = no_of_likes;
		this.has_like = has_like;
		this.mImage = mImage;
		this.mComment = mComment;
		this.seller_name = seller_name;
		this.seller_image = seller_image;
		this.seller_userid = seller_userid;
		this.apiCall_Status = apiCall_Status;
		this.Default_image = default_image;
		this.is_follow = is_follow;
		this.rating = rating;
		this.ratingcount = ratingcount;
		this.shorturl= shorturl;
	}

	public String getRating() {
		return rating;
	}



	public void setRating(String rating) {
		this.rating = rating;
	}



	public String getRatingcount() {
		return ratingcount;
	}



	public void setRatingcount(String ratingcount) {
		this.ratingcount = ratingcount;
	}

	
	public String getIs_follow() {
		return is_follow;
	}



	public void setIs_follow(String is_follow) {
		this.is_follow = is_follow;
	}




	
	public String getDefault_image() {
		return Default_image;
	}

	public void setDefault_image(String default_image) {
		Default_image = default_image;
	}

	public boolean isApiCall_Status() {
		return apiCall_Status;
	}

	public void setApiCall_Status(boolean apiCall_Status) {
		this.apiCall_Status = apiCall_Status;
	}

	public String getSeller_userid() {
		return seller_userid;
	}

	public void setSeller_userid(String seller_userid) {
		this.seller_userid = seller_userid;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getSeller_image() {
		return seller_image;
	}

	public void setSeller_image(String seller_image) {
		this.seller_image = seller_image;
	}
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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
	public String getItem_description() {
		return item_description;
	}

	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}

	public String getItem_condition() {
		return item_condition;
	}

	public void setItem_condition(String item_condition) {
		this.item_condition = item_condition;
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

	public String getItem_address() {
		return item_address;
	}

	public void setItem_address(String item_address) {
		this.item_address = item_address;
	}

	public String getSell_price() {
		return sell_price;
	}

	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getLocal_pickup() {
		return local_pickup;
	}

	public void setLocal_pickup(String local_pickup) {
		this.local_pickup = local_pickup;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getVideo() {
		return Video;
	}

	public void setVideo(String video) {
		Video = video;
	}

	public String getNo_of_likes() {
		return no_of_likes;
	}

	public void setNo_of_likes(String no_of_likes) {
		this.no_of_likes = no_of_likes;
	}

	public String getHas_like() {
		return has_like;
	}

	public void setHas_like(String has_like) {
		this.has_like = has_like;
	}

	public ArrayList<ProductDetlImage> getmImage() {
		return mImage;
	}

	public void setmImage(ArrayList<ProductDetlImage> mImage) {
		this.mImage = mImage;
	}

	public ArrayList<ProductDetailsCommentModal> getmComment() {
		return mComment;
	}

	public void setmComment(ArrayList<ProductDetailsCommentModal> mComment) {
		this.mComment = mComment;
	}


	public ItemDescModal() {
		super();
		
	}
	
	

}
