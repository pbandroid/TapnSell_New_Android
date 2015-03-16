package com.convertingoffers.tapnsell.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.util.Log;

public final class UserFunctions {

	private JSONParser jsonParser;
	
//		public static String LocalViewTermsUrl = "http://192.168.1.100/apps/tapnsell-yii/site/terms";
		public static String LocalViewTermsUrl = "http://tapnsell.com/backend/site/term";
		
	/*		
	public static String localImageUrl = "http://peerdevelopment.com/apps/tapnsell/";
	public static String localBaseUrl = "http://peerdevelopment.com/apps/tapnsell/api/";*/
	
/* public static String localImageUrl ="http://192.168.1.100/apps/tapnsell-yii/"; 
	 public static String localBaseUrl = "http://192.168.1.100/apps/tapnsell-yii/api/";*/
	
	
		public static String localImageUrl = "http://tapnsell.com/backend/";
		public static String localBaseUrl = "http://tapnsell.com/backend/api/";
		public static String LogoUrl = "http://tapnsell.com/backend/img/logo.png";
	 private static String EarnUrl = localBaseUrl+ "items/earning";
		private static String LeaveFeedbackListUrl = localBaseUrl+ "userreviews/requiredfeedbacklisting";
		private static String promot_listing_url = localBaseUrl+ "items/promotelisting";
		private static String shorturl = localBaseUrl+ "items/shorturl";
		
	public static String Stripe_PUBLISHABLE_KEY = "pk_test_4aWQKMzWBM8G2Xd8HEPCJXkS";
	public static String Stripe_Authorization_KEY = "sk_test_4aWQipTM0hW5rTgS0eKVX5DN";
	public static String Stripe_CustomerCreate = "https://api.stripe.com/v1/customers";
//	private static String myCategoryurl = localBaseUrl+ "categories/categorylist";
	private static String MeetupTodayURL = localBaseUrl+ "meetups/todaysmeetups";
	private static String MeetupallUrl = localBaseUrl+ "meetups/upcomingmeetups";
	private static String myNewCategoryurl = localBaseUrl+ "categories/getcategory";
	private static String refundrequest = localBaseUrl+ "orders/refundrequest";
	private static String requesttracking = localBaseUrl+ "orders/requesttracking";
	
	private static String refund = localBaseUrl+ "orders/refund";
	private static String refundfromseller = localBaseUrl+ "orders/refundfromseller";
	
	private static String footerprofileurl = localBaseUrl + "users/myprofile";
	private static String sendtofriendurl = localBaseUrl + "Items/sendtofriend";
	private static String Getebayurl = localBaseUrl + "items/ebaylogin";
	private static String BillingAddres = localBaseUrl+ "Addresses/useraddress";
	private static String addressinfo = localBaseUrl + "Addresses/addressinfo";
	private static String placeorder = localBaseUrl + "Orders/placeorder";
	private static String GetebayCaturl = localBaseUrl + "items/ebaycat";
	private static String GetebaySharurl = localBaseUrl + "items/ebaypost";
	private static String PostImageurl = localBaseUrl + "Items/addimage1";
	private static String TwitterRegisterrurl = localBaseUrl
			+ "Users/twitterlogin";
	private static String FBRegisterurl = localBaseUrl + "Users/facebooklogin";
	private static String gplus_Registerurl = localBaseUrl
			+ "Users/googlelogin";
	private static String Registerurl = localBaseUrl + "Users/adduser";
	private static String CreateCaseurl = localBaseUrl + "lawsuits/createcase";
	
	
	private static String Shareurl = localBaseUrl + "Items/share";
	private static String Loginurl = localBaseUrl + "Users/login";
	private static String ReviewListurl = localBaseUrl
			+ "userreviews/listreviews";
	
	private static String schedulemeetups = localBaseUrl+ "meetups/schedulemeetups";
	
	private static String VerifiedSocial = localBaseUrl + "users/verifysocialaccount";
	private static String VerifiedPhoneCode = localBaseUrl + "users/verifyphone";
	private static String VerifiedemailCode = localBaseUrl + "users/verifyemail";
	

	private static String phonenumberverification = localBaseUrl + "users/phonenumberverification";
	private static String Verifiedemail = localBaseUrl + "users/emailverification";
	private static String meetupdetails = localBaseUrl + "meetups/meetupdetails";
	private static String choosemeetup = localBaseUrl + "meetups/choosemeetup";
	
	private static String CheckImageurl = localBaseUrl + "items/sellmore";
	private static String Forgoturl = localBaseUrl + "Users/forgotpassword";
	private static String Searchurl = localBaseUrl + "Items/searchitem";
	private static String ChatDisplayurl = localBaseUrl + "chats/mymessage";
	private static String ResolutionChatDisplayurl = localBaseUrl + "lawsuits/casemymessage";
	
	private static String SendChaturl = localBaseUrl + "chats/message";
	private static String SendResolutionChaturl = localBaseUrl + "lawsuits/casemessage";
	private static String CloseCaseurl = localBaseUrl + "lawsuits/closecase";
	private static String CloseCaseRequesturl = localBaseUrl + "lawsuits/closecaserequest";
	
	private static String CategoryListingurl = localBaseUrl
			+ "categories/getshopcategory";
	
	private static String SearchNotificationurl = localBaseUrl
			+ "items/saveuseralertsearch";
	private static String DeleteItemurl = localBaseUrl
	+ "items/deleteandrelistitem";
	private static String ItemCopyUrl = localBaseUrl
	+ "items/getitemdetail";

	
	private static String additem = localBaseUrl + "Items/additem";
	
	private static String TODOSURL = localBaseUrl + "users/todo";
	private static String TapBoardURl = localBaseUrl + "items/tapboard";
	private static String AlertUrl = localBaseUrl + "users/alertsetting";
	private static String ChangeAlertUrl = localBaseUrl + "users/changealert";
	
	private static String accountinfo = localBaseUrl + "users/accountinfo";
	private static String Suggesturl = localBaseUrl + "userreviews/suggestion";
	private static String ChangePasswordurl = localBaseUrl + "users/changepassword";
	
	private static String SoundSettingurl = localBaseUrl + "users/soundsetting";
	private static String OpenCaseURL = localBaseUrl + "Lawsuits/caselisting";
	private static String MessageBoardUrl = localBaseUrl + "chats/chatuserlist";
	
	private static String MyListActive_Sold_ExpireURL = localBaseUrl + "Items/myitemlisting";
	private static String ItemToshipURL = localBaseUrl + "orders/shippingitemlisting";
	private static String ShipOrderURL = localBaseUrl + "orders/shipped";
	
	private static String PayPalInfo = localBaseUrl
			+ "users/paypalverification";
	private static String Popularurl = localBaseUrl + "Items/popularitems";
	private static String TapInspireDeleteurl = localBaseUrl
			+ "likes/removeitem";
	private static String WatchListDeleteurl = localBaseUrl + "likes/itemlike";
	// private static String Nearbyitemsurl = localBaseUrl +
	// "Items/nearbyitems";
	private static String NewListurl = localBaseUrl + "Items/itemlisting";
	private static String MyOrderurl = localBaseUrl + "orders/myorders";
	private static String OrderDetailsurl = localBaseUrl
			+ "Orders/orderdetails";
	private static String TapInspireurl = localBaseUrl + "items/tapinspired";
	private static String WatchListUrl = localBaseUrl + "items/watchlistitems";
	private static String Recentlyurl = localBaseUrl + "Items/recentitems";
	private static String Categoryurl = localBaseUrl + "Items/itemsbycategory";
	private static String ProductDetailsurl = localBaseUrl + "Items/itemdetail";
//	private static String BillingUrl = localBaseUrl + "Addresses/addaddress";
	private static String PopularListurl = localBaseUrl + "Items/newitems";
	private static String MakeOfferurl = localBaseUrl + "offers/makeoffer";

	private static String Favurl = localBaseUrl + "Likes/itemlike";
	private static String Rattingurl = localBaseUrl + "userreviews/addreview";
	private static String Reserveurl = localBaseUrl
			+ "Items/changereservedstate";
	private static String Followurl = localBaseUrl + "follows/follow";
	private static String Posturl = localBaseUrl + "Comments/itemcomment";
	private static String AddBillingurl = localBaseUrl + "addresses/addaddress";
	private static String AcceptdeecLineoFFerurl = localBaseUrl
			+ "Offers/acceptoffer";
	private static String CounterOfferUrl = localBaseUrl
			+ "offers/counteroffer";
	private static String OfferReciveUrl = localBaseUrl + "offers/myoffers";
	private static String OfferMadeUrl = localBaseUrl
			+ "offers/myrequestoffers";
	private static String GetAllItemUrl = localBaseUrl + "offers/itemoffer";

	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	public JSONObject FBRegisterFunction(String first_name, String last_name,
			String facebook_id, String facebook_name, String facebook_email,
			String facebook_gender, String facebook_picture,
			String device_type, String device_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("first_name", first_name));
		params.add(new BasicNameValuePair("last_name", last_name));
		params.add(new BasicNameValuePair("facebook_id", facebook_id));
		params.add(new BasicNameValuePair("facebook_name", facebook_name));
		params.add(new BasicNameValuePair("facebook_email", facebook_email));
		params.add(new BasicNameValuePair("facebook_gender", facebook_gender));
		params.add(new BasicNameValuePair("facebook_picture", facebook_picture));
		params.add(new BasicNameValuePair("device_type", device_type));
		params.add(new BasicNameValuePair("device_id", device_id));
		params.add(new BasicNameValuePair("device_type", "A"));

		JSONObject json = jsonParser.getJSONFromUrl(FBRegisterurl, params);
		return json;
	}

	public JSONObject TwitterRegisterFunction(String twitter_id,
			String twitter_name, String twitter_city, String device_type,
			String device_id, String twitter_image) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("twitter_id", twitter_id));
		params.add(new BasicNameValuePair("twitter_name", twitter_name));
		params.add(new BasicNameValuePair("twitter_city", twitter_city));
		params.add(new BasicNameValuePair("device_type", "A"));
		params.add(new BasicNameValuePair("device_id", device_id));
		params.add(new BasicNameValuePair("twitter_image", twitter_image));

		JSONObject json = jsonParser
				.getJSONFromUrl(TwitterRegisterrurl, params);
		return json;
	}

	public JSONObject gplusFunction(String google_id, String google_name,
			String google_picture, String google_gender, String device_type,
			String device_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("google_id", google_id));
		params.add(new BasicNameValuePair("google_name", google_name));
		params.add(new BasicNameValuePair("google_picture", google_picture));
		params.add(new BasicNameValuePair("google_gender", google_gender));
		params.add(new BasicNameValuePair("device_id", device_id));
		params.add(new BasicNameValuePair("device_type", "A"));
		JSONObject json = jsonParser.getJSONFromUrl(gplus_Registerurl, params);
		return json;
	}

	public JSONObject RegisterFunction(String first_name, String last_name,
			String email, String password, String mobile, String device_type,
			String device_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("first_name", first_name));
		params.add(new BasicNameValuePair("last_name", last_name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("mobile", mobile));
		params.add(new BasicNameValuePair("device_id", device_id));
		params.add(new BasicNameValuePair("device_type", "A"));

		JSONObject json = jsonParser.getJSONFromUrl(Registerurl, params);
		return json;
	}
	public JSONObject CreateCaseFunction(String userid, String orderid,
			String issue, String description, String image) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("orderid", orderid));
		params.add(new BasicNameValuePair("issue", issue));
		params.add(new BasicNameValuePair("description", description));
		params.add(new BasicNameValuePair("image", image));

		JSONObject json = jsonParser.getJSONFromUrl(CreateCaseurl, params);
		return json;
	}

	public JSONObject GetEbayUrlFunction() {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject json = jsonParser.getJSONFromUrl(Getebayurl, params);
		return json;
	}

	public JSONObject getBillingAddressFunction(String userid,String type) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("type", type));
		JSONObject json = jsonParser.getJSONFromUrl(BillingAddres, params);
		return json;
	}

	public JSONObject getAddressInfoFunction(String userid, String itemid,
			String bid,String sid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("bid", bid));
		params.add(new BasicNameValuePair("sid", sid));
		JSONObject json = jsonParser.getJSONFromUrl(addressinfo, params);
		return json;
	}

	public JSONObject GetEbayCategoryFunction(String sessionid, String userid,
			String itemid, String ebayuser) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sessionid", sessionid));
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("ebayuser", ebayuser));

		JSONObject json = jsonParser.getJSONFromUrl(GetebayCaturl, params);
		return json;
	}

	public JSONObject SharEbayFunction(String itemid, String userid,
			String catid, String conditionid, String startprice,
			String reserveprice, String bestoffer, String type) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("catid", catid));
		params.add(new BasicNameValuePair("conditionid", conditionid));
		params.add(new BasicNameValuePair("startprice", startprice));
		params.add(new BasicNameValuePair("reserveprice", reserveprice));
		params.add(new BasicNameValuePair("bestoffer", bestoffer));
		params.add(new BasicNameValuePair("type", type));

		JSONObject json = jsonParser.getJSONFromUrl(GetebaySharurl, params);
		return json;
	}

	public JSONObject SendtofriendurlFunction(String numbers, String itemurl) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("numbers", numbers));
		params.add(new BasicNameValuePair("itemurl", itemurl));
		JSONObject json = jsonParser.getJSONFromUrl(sendtofriendurl, params);
		return json;
	}
	public JSONObject FooterUserProfileFunction(String userid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		JSONObject json = jsonParser.getJSONFromUrl(footerprofileurl, params);
		return json;
	}
	
	public JSONObject SharUrlFunction(String id, String type) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("type", type));
		JSONObject json = jsonParser.getJSONFromUrl(Shareurl, params);
		return json;
	}

	public JSONObject RecentlyViewFunction(String userid, String latitude,
			String longitude) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		JSONObject json = jsonParser.getJSONFromUrl(Recentlyurl, params);
		return json;
	}

	public JSONObject CategoryItemFunction(String userid, String latitude,
			String longitude, String categoryid) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		params.add(new BasicNameValuePair("categoryid", categoryid));
		JSONObject json = jsonParser.getJSONFromUrl(Categoryurl, params);
		return json;
	}

	public JSONObject NewListingFunction(String userid, String latitude,
			String longitude, String start) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		params.add(new BasicNameValuePair("start", start));
		JSONObject json = jsonParser.getJSONFromUrl(NewListurl, params);
		return json;
	}

	public JSONObject PopularListFunction(String userid, String latitude,
			String longitude) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		JSONObject json = jsonParser.getJSONFromUrl(Popularurl, params);
		return json;
	}

	public JSONObject DeleteItemFunction(String userid, String itemid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		JSONObject json = jsonParser
				.getJSONFromUrl(TapInspireDeleteurl, params);
		return json;
	}

	public JSONObject WatchListDeleteItemFunction(String userid, String itemid,
			String is_like) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("is_like", is_like));

		JSONObject json = jsonParser.getJSONFromUrl(WatchListDeleteurl, params);
		return json;
	}

	public JSONObject NearbyitemsFunction(String userid, String latitude,
			String longitude, String start, String type, String categoryid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		params.add(new BasicNameValuePair("start", start));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("categoryid", categoryid));

		JSONObject json = jsonParser.getJSONFromUrl(NewListurl, params);
		return json;
	}

	public JSONObject MyOrderFunction(String userid, String orderby) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("orderby", orderby));

		JSONObject json = jsonParser.getJSONFromUrl(MyOrderurl, params);
		return json;
	}

	public JSONObject OrderDetailsFunction(String userid, String orderid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("orderid", orderid));

		JSONObject json = jsonParser.getJSONFromUrl(OrderDetailsurl, params);
		return json;
	}
	public JSONObject RequestRefundFunction(String userid, String orderid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("orderid", orderid));
		
		JSONObject json = jsonParser.getJSONFromUrl(refundrequest, params);
		return json;
	}
	public JSONObject RequestTrackingFunction(String userid, String orderid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("orderid", orderid));
		
		JSONObject json = jsonParser.getJSONFromUrl(requesttracking, params);
		return json;
	}
	public JSONObject SellerconfirmRejectFunction(String userid, String orderid,String isapprove) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("orderid", orderid));
		params.add(new BasicNameValuePair("isapprove", isapprove));
		JSONObject json = jsonParser.getJSONFromUrl(refund, params);
		return json;
	}


	public JSONObject SellerRefundFunction(String userid, String orderid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("orderid", orderid));
		JSONObject json = jsonParser.getJSONFromUrl(refundfromseller, params);
		return json;
	}
	
	public JSONObject TapInspireFunction(String userid, String latitude,
			String longitude, String start, String type) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		params.add(new BasicNameValuePair("start", start));
		params.add(new BasicNameValuePair("limit", "50"));
		params.add(new BasicNameValuePair("type", type));

		JSONObject json = jsonParser.getJSONFromUrl(TapInspireurl, params);
		return json;
	}

	public JSONObject WatchListFunction(String userid, String latitude,
			String longitude, String start) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		params.add(new BasicNameValuePair("start", start));

		JSONObject json = jsonParser.getJSONFromUrl(WatchListUrl, params);
		return json;
	}

	public JSONObject ProductDetailFunction(String userid, String itemid,
			String latitude, String longitude) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));

		JSONObject json = jsonParser.getJSONFromUrl(ProductDetailsurl, params);
		return json;
	}

	public JSONObject BillingFunction(String userid, String itemid,
			String type, String first_name, String last_name, String mobile_no,
			String address, String city, String state, String country,
			String zipcode, String email, String appartment) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("first_name", first_name));
		params.add(new BasicNameValuePair("last_name", last_name));
		params.add(new BasicNameValuePair("mobile_no", mobile_no));
		params.add(new BasicNameValuePair("address", address));
		params.add(new BasicNameValuePair("city", city));
		params.add(new BasicNameValuePair("state", state));
		params.add(new BasicNameValuePair("country", country));
		params.add(new BasicNameValuePair("zipcode", zipcode));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("appartment", appartment));
		JSONObject json = jsonParser.getJSONFromUrl(AddBillingurl, params);
		return json;
	}

	public JSONObject FavFunction(String itemid, String userid, String is_like) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("is_like", is_like));

		JSONObject json = jsonParser.getJSONFromUrl(Favurl, params);
		return json;
	}

	public JSONObject ReserveFunction(String userid, String itemid,
			String is_reserved) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("is_reserved", is_reserved));

		JSONObject json = jsonParser.getJSONFromUrl(Reserveurl, params);
		return json;
	}

	public JSONObject RattingFunction(String itemid, String from_id,
			String to_id, String orderid, String review, String rating,String strRatting_orderid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("from_id", from_id));
		params.add(new BasicNameValuePair("to_id", to_id));
		params.add(new BasicNameValuePair("orderid", orderid));
		params.add(new BasicNameValuePair("review", review));
		params.add(new BasicNameValuePair("rating", rating));
		params.add(new BasicNameValuePair("orderid", strRatting_orderid));
		
		JSONObject json = jsonParser.getJSONFromUrl(Rattingurl, params);
		return json;
	}

	public JSONObject FollowFunction(String from_id, String to_id,
			String is_follow) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("from_id", from_id));
		params.add(new BasicNameValuePair("to_id", to_id));
		params.add(new BasicNameValuePair("is_follow", is_follow));
		JSONObject json = jsonParser.getJSONFromUrl(Followurl, params);
		return json;

	}

	public JSONObject FollowFunctions(String from_id, String to_id,
			String is_follow) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("from_id", from_id));
		params.add(new BasicNameValuePair("to_id", to_id));
		params.add(new BasicNameValuePair("is_follow", is_follow));
		Log.e("params", " " + params);
		JSONObject json = jsonParser.getJSONFromUrl(Followurl, params);
		return json;

	}

	public JSONObject CommentFunction(String userid, String itemid,
			String comment) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("comment", comment));
		JSONObject json = jsonParser.getJSONFromUrl(Posturl, params);
		return json;

	}

	public JSONObject PopularFunction(String userid, String latitude,
			String longitude) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		JSONObject json = jsonParser.getJSONFromUrl(PopularListurl, params);
		return json;
	}

	public JSONObject mycategoryListFunction(String categoryid) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("categoryid", categoryid));
		JSONObject json = jsonParser.getJSONFromUrl(myNewCategoryurl, params);
		return json;
	}

	public JSONObject PostDataFunction(String userid, String itemid,
			String image, String is_default, String type) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("image", image));
		params.add(new BasicNameValuePair("is_default", is_default));
		params.add(new BasicNameValuePair("type", type));

		JSONObject json = jsonParser.getJSONFromUrl(PostImageurl, params);
		return json;
	}

	public JSONObject AddItemFunction(String userid, String item_name,
			String item_description, String item_condition,
			String asking_price, String quantity, String delevery_option,
			String category_id, String address, String latitude,
			String longitude, String total_media) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("item_name", item_name));
		params.add(new BasicNameValuePair("item_description", item_description));
		params.add(new BasicNameValuePair("item_condition", item_condition));
		params.add(new BasicNameValuePair("asking_price", asking_price));
		params.add(new BasicNameValuePair("quantity", quantity));
		params.add(new BasicNameValuePair("delevery_option", delevery_option));
		params.add(new BasicNameValuePair("category_id", category_id));
		params.add(new BasicNameValuePair("address", address));
		params.add(new BasicNameValuePair("latitude", "0"));
		params.add(new BasicNameValuePair("longitude", "0"));
		params.add(new BasicNameValuePair("total_media", total_media));
		JSONObject json = jsonParser.getJSONFromUrl(additem, params);
		return json;
	}

	public JSONObject PayPalFunction(String userid, String paypal_email,
			String paypal_firstname, String paypal_lastname) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("paypal_email", paypal_email));
		params.add(new BasicNameValuePair("paypal_firstname", paypal_firstname));
		params.add(new BasicNameValuePair("paypal_lastname", paypal_lastname));

		JSONObject json = jsonParser.getJSONFromUrl(PayPalInfo, params);
		return json;
	}

	public JSONObject LoginFunction(String email_id, String password,
			String device_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("email_id", email_id));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("device_id", device_id));
		params.add(new BasicNameValuePair("devicetype", "A"));

		JSONObject json = jsonParser.getJSONFromUrl(Loginurl, params);
		return json;
	}

	public JSONObject ReviewListFunction(String userid, String start) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("start", start));

		JSONObject json = jsonParser.getJSONFromUrl(ReviewListurl, params);
		return json;
	}
	
	public JSONObject MeetupRequestFunction(String userid, String orderid, String data) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("orderid", orderid));
		params.add(new BasicNameValuePair("data", data));
		
		JSONObject json = jsonParser.getJSONFromUrl(schedulemeetups, params);
		return json;
	}


	public JSONObject CheckMediaUploadFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		JSONObject json = jsonParser.getJSONFromUrl(CheckImageurl, params);
		return json;
	}
	public JSONObject GetMeetupDetailsFunction(String userid,String orderid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("orderid", orderid));
		JSONObject json = jsonParser.getJSONFromUrl(meetupdetails, params);
		return json;
	}
	public JSONObject AcceptMeetupFunction(String userid,String meetupid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("meetupid", meetupid));
		JSONObject json = jsonParser.getJSONFromUrl(choosemeetup, params);
		return json;
		
	}
	public JSONObject getNearestStoreFunction(String url) {
		
		JSONObject json = jsonParser.getMethodUrl(url);
		return json;
	}
	public JSONObject VerifyEmailFunction(String user_id,String verifiedemail) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		params.add(new BasicNameValuePair("verifiedemail", verifiedemail));
		JSONObject json = jsonParser.getJSONFromUrl(Verifiedemail, params);
		return json;
	}
	public JSONObject VerifyPhoneFunction(String user_id,String mobileno) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		params.add(new BasicNameValuePair("mobileno", mobileno));
		JSONObject json = jsonParser.getJSONFromUrl(phonenumberverification, params);
		return json;
	}
	public JSONObject VerifyEmailCodeFunction(String user_id,String code) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		params.add(new BasicNameValuePair("code", code));
		JSONObject json = jsonParser.getJSONFromUrl(VerifiedemailCode, params);
		return json;
	}
	public JSONObject VerifyPhoneCodeFunction(String user_id,String code) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		params.add(new BasicNameValuePair("code", code));
		JSONObject json = jsonParser.getJSONFromUrl(VerifiedPhoneCode, params);
		return json;
	}
	
	public JSONObject VerifyFacebookFunction(String user_id,String type) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		params.add(new BasicNameValuePair("type", type));
		JSONObject json = jsonParser.getJSONFromUrl(VerifiedSocial, params);
		return json;
	}

	public JSONObject TODOSFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		JSONObject json = jsonParser.getJSONFromUrl(TODOSURL, params);
		return json;
	}
	public JSONObject TapBoardFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		JSONObject json = jsonParser.getJSONFromUrl(TapBoardURl, params);
		return json;
	}
	public JSONObject SuggestFunction(String user_id,String comment) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		
		params.add(new BasicNameValuePair("comment", comment));
		JSONObject json = jsonParser.getJSONFromUrl(Suggesturl, params);
		return json;
	}
	public JSONObject UpdatePasswordFunction(String user_id,String old,String new_pass) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		params.add(new BasicNameValuePair("old", old));
		params.add(new BasicNameValuePair("new", new_pass));
		JSONObject json = jsonParser.getJSONFromUrl(ChangePasswordurl, params);
		return json;
	}
	public JSONObject MyaccountFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		
		JSONObject json = jsonParser.getJSONFromUrl(accountinfo, params);
		return json;
	}
	public JSONObject GetAlertFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		JSONObject json = jsonParser.getJSONFromUrl(AlertUrl, params);
		return json;
	}
	public JSONObject ChangeAlertFunction(String user_id,String type,String status,String id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("userid", user_id));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("status", status));
		params.add(new BasicNameValuePair("id", id));
		
		JSONObject json = jsonParser.getJSONFromUrl(ChangeAlertUrl, params);
		return json;
	}
	public JSONObject SoundSettingFunction(String user_id,String index,String status) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		params.add(new BasicNameValuePair("index", index));
		params.add(new BasicNameValuePair("status", status));
		JSONObject json = jsonParser.getJSONFromUrl(SoundSettingurl, params);
		return json;
	}
	public JSONObject OpenCaseFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		JSONObject json = jsonParser.getJSONFromUrl(OpenCaseURL, params);
		return json;
	}
	public JSONObject MessageBoardFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		JSONObject json = jsonParser.getJSONFromUrl(MessageBoardUrl, params);
		return json;
	}
	public JSONObject LeaveFeedbackFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		JSONObject json = jsonParser.getJSONFromUrl(LeaveFeedbackListUrl, params);
		return json;
	}
	public JSONObject PromotListingFunction(String user_id,String start) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		params.add(new BasicNameValuePair("start", start));
		JSONObject json = jsonParser.getJSONFromUrl(promot_listing_url, params);
		return json;
	}
	public JSONObject UrlConvertFunction(String url) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("url", url));
		
		JSONObject json = jsonParser.getJSONFromUrl(shorturl, params);
		return json;
	}
	public JSONObject MyEarnFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		JSONObject json = jsonParser.getJSONFromUrl(EarnUrl , params);
		return json;
	}
	public JSONObject MeetupTodayFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		JSONObject json = jsonParser.getJSONFromUrl(MeetupTodayURL, params);
		return json;
	}
	public JSONObject MeetupListFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		JSONObject json = jsonParser.getJSONFromUrl(MeetupallUrl, params);
		return json;
	}
	public JSONObject ItemToShipFunction(String user_id) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		JSONObject json = jsonParser.getJSONFromUrl(ItemToshipURL, params);
		return json;
	}
	public JSONObject MyListActive_Sold_expire(String user_id,String latitude, String longitude) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		JSONObject json = jsonParser.getJSONFromUrl(MyListActive_Sold_ExpireURL, params);
		return json;
	}
	public JSONObject OrderShipFunction(String user_id,String orderid, String curier_service, String trackingnumber) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", user_id));
		params.add(new BasicNameValuePair("orderid", orderid));
		params.add(new BasicNameValuePair("curier_service", curier_service));
		params.add(new BasicNameValuePair("trackingnumber", trackingnumber));
		JSONObject json = jsonParser.getJSONFromUrl(ShipOrderURL, params);
		return json;
	}
	
	public JSONObject SearchFunction(String userid, String name,
			String latitude, String longitude, String start, String type) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		params.add(new BasicNameValuePair("start", start));
		params.add(new BasicNameValuePair("type", type));
		JSONObject json = jsonParser.getJSONFromUrl(Searchurl, params);
		return json;
	}

	public JSONObject DisplayChatFunction(String userid, String itemid,
			String start,String time) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("start", start));
		params.add(new BasicNameValuePair("time", time));
		JSONObject json = jsonParser.getJSONFromUrl(ChatDisplayurl, params);
		return json;
	}
	public JSONObject DisplayResolutionChatFunction(String userid, String caseid,
			String start) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("caseid", caseid));
		params.add(new BasicNameValuePair("start", start));
		JSONObject json = jsonParser.getJSONFromUrl(ResolutionChatDisplayurl, params);
		return json;
	}
	
	public JSONObject SendChatFunction(String from_id, String to_id,
			String itemid, String message, String time) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("from_id", from_id));
		params.add(new BasicNameValuePair("to_id", to_id));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("message", message));
		params.add(new BasicNameValuePair("time", time));
		JSONObject json = jsonParser.getJSONFromUrl(SendChaturl, params);
		return json;
	}
	public JSONObject SendChatImageFunction(String from_id, String to_id,
			String itemid, String image) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("from_id", from_id));
		params.add(new BasicNameValuePair("to_id", to_id));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("image", image));
		JSONObject json = jsonParser.getJSONFromUrl(SendChaturl, params);
		return json;
	}
//	=========================================
	
//	shahil
	public JSONObject SendResolutionChatFunction(String from_id, String to_id,
			String caseid, String message, String time) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("from_id", from_id));
		params.add(new BasicNameValuePair("to_id", to_id));
		params.add(new BasicNameValuePair("caseid", caseid));
		params.add(new BasicNameValuePair("message", message));
		params.add(new BasicNameValuePair("time", time));
		JSONObject json = jsonParser.getJSONFromUrl(SendResolutionChaturl, params);
		return json;
	}
	public JSONObject SendResolutionChatImageFunction(String from_id, String to_id,
			String caseid, String image) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("from_id", from_id));
		params.add(new BasicNameValuePair("to_id", to_id));
		params.add(new BasicNameValuePair("caseid", caseid));
		params.add(new BasicNameValuePair("image", image));
		JSONObject json = jsonParser.getJSONFromUrl(SendResolutionChaturl, params);
		return json;
	}
	
	
	public JSONObject ClosecaseFunction(String userid, String caseid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("caseid", caseid));
		
		JSONObject json = jsonParser.getJSONFromUrl(CloseCaseurl, params);
		return json;
	}
	public JSONObject ClosecaseRequestFunction(String userid, String caseid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("caseid", caseid));
		
		JSONObject json = jsonParser.getJSONFromUrl(CloseCaseRequesturl, params);
		return json;
	}
	
	

	public JSONObject CategoryFunction(String categoryid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("categoryid", categoryid));
		JSONObject json = jsonParser.getJSONFromUrl(CategoryListingurl, params);
		return json;
	}

	public JSONObject SearchNotificationFunction(String userid, String name,
			String categoryid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("categoryid", categoryid));

		JSONObject json = jsonParser.getJSONFromUrl(SearchNotificationurl,
				params);
		return json;
	}

	public JSONObject DeleteItemFromExpireListFunction(String userid,String itemid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("status", "D"));

		JSONObject json = jsonParser.getJSONFromUrl(DeleteItemurl, params);
		return json;
	}
	public JSONObject ItemCopyGetDataFunction(String userid,String itemid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));

		JSONObject json = jsonParser.getJSONFromUrl(ItemCopyUrl, params);
		return json;
	}
	
	
	public JSONObject ForGotPasswordFunction(String email) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));

		JSONObject json = jsonParser.getJSONFromUrl(Forgoturl, params);
		return json;
	}

	public JSONObject AddAddress(String userid, String itemid, String type,
			String first_name, String last_name, String mobile_no,
			String appartment, String address, String city, String state,
			String country, String zipcode, String email) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("first_name", first_name));
		params.add(new BasicNameValuePair("last_name", last_name));
		params.add(new BasicNameValuePair("mobile_no", mobile_no));
		params.add(new BasicNameValuePair("appartment", appartment));
		params.add(new BasicNameValuePair("address", address));
		params.add(new BasicNameValuePair("city", city));
		params.add(new BasicNameValuePair("state", state));
		params.add(new BasicNameValuePair("country", country));
		params.add(new BasicNameValuePair("zipcode", zipcode));
		params.add(new BasicNameValuePair("email", email));

		JSONObject json = jsonParser.getJSONFromUrl(ChatDisplayurl, params);
		return json;
	}

	public JSONObject PlaceOrderFunction(String itemid, String userid,
			String isaddcard, String amount, String bfirst_name,
			String blast_name, String bmobile_no, String bappartment,
			String baddress, String bcity, String bstate, String bcountry,
			String bzipcode, String bemail, String sfirst_name,
			String slast_name, String smobile_no, String sappartment,
			String saddress, String scity, String sstate, String scountry,
			String szipcode, String semail, String bid, String sid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("isaddcard", isaddcard));
		params.add(new BasicNameValuePair("amount", amount));
		params.add(new BasicNameValuePair("bfirst_name", bfirst_name));
		params.add(new BasicNameValuePair("blast_name", blast_name));
		params.add(new BasicNameValuePair("bmobile_no", bmobile_no));
		params.add(new BasicNameValuePair("bappartment", bappartment));
		params.add(new BasicNameValuePair("baddress", baddress));
		params.add(new BasicNameValuePair("bcity", bcity));
		params.add(new BasicNameValuePair("bstate", bstate));
		params.add(new BasicNameValuePair("bcountry", bcountry));
		params.add(new BasicNameValuePair("bzipcode", bzipcode));
		params.add(new BasicNameValuePair("bemail", bemail));
		params.add(new BasicNameValuePair("sfirst_name", sfirst_name));
		params.add(new BasicNameValuePair("slast_name", slast_name));
		params.add(new BasicNameValuePair("smobile_no", smobile_no));
		params.add(new BasicNameValuePair("sappartment", sappartment));
		params.add(new BasicNameValuePair("saddress", saddress));
		params.add(new BasicNameValuePair("scity", scity));
		params.add(new BasicNameValuePair("sstate", sstate));
		params.add(new BasicNameValuePair("scountry", scountry));
		params.add(new BasicNameValuePair("szipcode", szipcode));
		params.add(new BasicNameValuePair("semail", semail));
		params.add(new BasicNameValuePair("bid", bid));
		params.add(new BasicNameValuePair("sid", sid));

		JSONObject json = jsonParser.getJSONFromUrl(placeorder, params);
		return json;
	}

	public JSONObject MakeOfferFunction(String itemid, String userid,
			String amount) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("itemid", itemid));
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("amount", amount));
		JSONObject json = jsonParser.getJSONFromUrl(MakeOfferurl, params);
		return json;
	}

	public JSONObject AcceptDeclineFunction(String offerid, String userid,
			String accept) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("offerid", offerid));
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("accept", accept));
		JSONObject json = jsonParser.getJSONFromUrl(AcceptdeecLineoFFerurl,
				params);
		return json;
	}

	public JSONObject CounterOfferFunction(String offerid, String fromid,
			String toid, String amount, String itemid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("offerid", offerid));
		params.add(new BasicNameValuePair("fromid", fromid));
		params.add(new BasicNameValuePair("toid", toid));
		params.add(new BasicNameValuePair("amount", amount));
		params.add(new BasicNameValuePair("itemid", itemid));

		JSONObject json = jsonParser.getJSONFromUrl(CounterOfferUrl, params);
		return json;
	}

	public JSONObject OfferReciveFunction(String userid, String start,
			boolean statusUrl) {
		JSONObject json;
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("start", start));
		if (statusUrl) {
			json = jsonParser.getJSONFromUrl(OfferMadeUrl, params);
		} else {
			json = jsonParser.getJSONFromUrl(OfferReciveUrl, params);
		}

		return json;
	}

	public JSONObject GetallMyOfferFunction(String userid, String itemid) {
		JSONObject json;
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("itemid", itemid));
		json = jsonParser.getJSONFromUrl(GetAllItemUrl, params);
		Log.e("GetAllItemUrl", " " + GetAllItemUrl);
		return json;
	}

}