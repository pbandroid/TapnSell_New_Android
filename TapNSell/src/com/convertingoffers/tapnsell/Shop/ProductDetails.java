package com.convertingoffers.tapnsell.Shop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.HomeActivity;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.MakeOffer.MakeOfferActivity;
import com.convertingoffers.tapnsell.Modal.ItemDescModal;
import com.convertingoffers.tapnsell.Modal.ProductDetailsCommentModal;
import com.convertingoffers.tapnsell.Modal.ProductDetlImage;
import com.convertingoffers.tapnsell.Modal.SearchModel;
import com.convertingoffers.tapnsell.Shop.checkout.MainCheckOutFragmentActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.ImageLoaderTopBottamRounded;
import com.convertingoffers.tapnsell.util.Login_Dialog_Activity;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ProductDetails extends BaseActivity {
	Bitmap bitmapFromUrl=null;
	File casted_image;
	String address="N" ,FacebookSharurl="";
	Animation RightSwipe;
	protected static final String TAG = "ShareActivity";
	Session mCurrentSession;
	RatingBar rb_Rating2;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	boolean pendingPublishReauthorization;
	String rattingname="";
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options,option_user_seller,option_comment;
	String 
			strRatting_UserName, strRatting_Image, strRatting_Review,
			strRatting_ItemId, strRatting_Fromid, strRatting_toid,
			strRatting_Ratting;
	String item_description, item_condition, latitude, longitude, item_address,
			sell_price, quantity, local_pickup, category_id, reserved,
			distance, Video, no_of_likes, to_id, name, from_id, comment,
			pageState = "0", default_image = "", seller_is_follow = "N",
			default2, image3, image4, default4, is_follow = "", default3,
			reserve = "Y", seller_userid, seller_name, seller_image = "",
			has_like = "", is_like = "", itemid = "", userid = "", image1 = "",
			default1 = "", image2 = "",newLikes="",Has_like="",short_url="";
	boolean boolimg1 = false, boolimg2 = false, boolimg3 = false,
			boolimg4 = false;
	int cnt = 0, TotalArraySize = 0, PosForFav = 0,temp_pos;
	ViewPager myPager;
	ArrayList<ProductDetailsCommentModal> mComment;
	ArrayList<ProductDetailsCommentModal> mtemp_Comment=new ArrayList<ProductDetailsCommentModal>();
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	ArrayList<ProductDetlImage> mProductImage;
	ArrayList<String> mItemList = new ArrayList<String>();
	SearchModel myCateForfav;
	int postion = 0, pos = 0;
	Intent i_Buy;
	ArrayList<ItemDescModal> mProductDecs;
	double lat = 0.0, log = 0.0, Distance = 0.0, lat1, lat2, lng1, lng2,
			Dis = 0.0;
	protected LocationManager locationManager;
	String strlatitude, strlongitude;
	Location location;
	View V;
	ArrayList<ItemDescModal> mPDetails = new ArrayList<ItemDescModal>();
	boolean isCurrentLocation = false, Successfull, isGPSEnabled = false,
			isNetworkEnabled = false, canGetLocation = false;
	MyPagerAdapter adapter;
	ItemDescModal _mlist = null;
	
	Custom_Dialog custom_dialog;
	Context context;
//	ImageLoaderTopBottamRounded imageloader;
	boolean FavStatus=false;
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail_viewpager);
		mItemList.clear();
		userid = pref.getString("UserID", "0");
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			getLocation();
		} else {
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Toast.makeText(this, "GPS is Enabled in your device",
						Toast.LENGTH_SHORT).show();
				getLocation();
			}
		}

		Bundle b = getIntent().getExtras();
		if (b != null) {
			mItemList = (ArrayList<String>) b.getSerializable("ItemArray");
			pageState = b.getString("position");
			try {
				postion = Integer.parseInt(pageState);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			itemid = mItemList.get(postion);
		}

		TotalArraySize = mItemList.size();

		mProductDecs = new ArrayList<ItemDescModal>(TotalArraySize);
		mProductImage = new ArrayList<ProductDetlImage>();
		mComment = new ArrayList<ProductDetailsCommentModal>();

		for (int i = 0; i < TotalArraySize; i++) {
			mProductDecs.add(i, new ItemDescModal("", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", mProductImage,
					mComment, seller_name, seller_image, seller_userid, false,
					"", "", "", "",""));
		}
		if (cd.checkConnection()) {
			new ProductDetailsBackTask().execute("");
		} else {
			Toast.makeText(ProductDetails.this, "internet_conn_failed",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context = this;
		custom_dialog = new Custom_Dialog();
		myPager = (ViewPager) findViewById(R.id.myViewPager);
	}

	private class ProductDetailsBackTask extends
			AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ProductDetails.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{

				Log.e("userid", " "+userid);
				Log.e("itemid", " "+itemid);
				Log.e("strlatitude", " "+strlatitude);
				Log.e("strlongitude", " "+strlongitude);
				JSONObject json = userFunction.ProductDetailFunction(userid,
						itemid, strlatitude, strlongitude);
				try {
					if (json!=null &&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject jobj = json.getJSONObject("Item");

							userid = jobj.getString("userid");
							itemid = jobj.getString("itemid");
							name = jobj.getString("name");
							item_description = jobj.getString("item_description");
							item_condition = jobj.getString("item_condition");
							latitude = jobj.getString("latitude");
							longitude = jobj.getString("longitude");
							item_address = jobj.getString("item_address");
							sell_price = jobj.getString("sell_price");
							quantity = jobj.getString("quantity");
							local_pickup = jobj.getString("local_pickup");
							category_id = jobj.getString("category_id");
							reserved = jobj.getString("reserved");
							distance = jobj.getString("distance");
							String vid= "";
							vid=jobj.getString("Video");
							if(vid.length()==0){
								Video="";
							}else{
								
								Video = UserFunctions.localImageUrl;
								Video = Video + jobj.getString("Video");
							}
							
							no_of_likes = jobj.getString("no_of_likes");
							has_like = json.optString("has_like");
							is_follow = json.optString("is_follow");
							short_url= jobj.optString("url");
							mComment = new ArrayList<ProductDetailsCommentModal>() ;
							mComment.clear();
							JSONArray jcomment = jobj.getJSONArray("Comment");
							for (int j = 0; j < jcomment.length(); j++) {

								JSONObject c = jcomment.getJSONObject(j);
								String comment = c.getString("comment");
								String userid = c.getString("userid");
								String	image =  c.getString("image");
								String name = c.getString("name");

								mComment.add(new ProductDetailsCommentModal(comment, userid, image, name));
							}
							mProductImage.clear();
							JSONArray Image = jobj.getJSONArray("Image");
							cnt = Image.length();
							for (int i = 0; i < Image.length(); i++) {

								JSONObject c = Image.getJSONObject(i);
								image1 = UserFunctions.localImageUrl;
								image1 = image1 + c.getString("image");
								default1 = c.getString("default");
								if (default1.equals("Y")) {
									default_image = image1;
								}

								mProductImage.add(new ProductDetlImage(image1));

							}
							JSONObject jReview = json.getJSONObject("Review");

							String rating = jReview.getString("rating");
							String ratingcount = jReview
									.getString("ratingcount");

							JSONObject jUser = json.getJSONObject("User");
							seller_userid = jUser.getString("userid");
							seller_name = jUser.getString("name");
//							seller_image = UserFunctions.localImageUrl;
							seller_image = jUser.getString("image");
							errorMessage = "true";
							mProductDecs.set(postion, new ItemDescModal(userid,
									itemid, name, item_description,
									item_condition, latitude, longitude,
									item_address, sell_price, quantity,
									local_pickup, category_id, reserved,
									distance, Video, no_of_likes, has_like,
									mProductImage, mComment, seller_name,
									seller_image, seller_userid, true,
									default_image, is_follow, rating,
									ratingcount,short_url));
						
						} else {
							errorMessage = "false";
							message=json.optString("message");
						}
					} else {
						errorMessage = "network";
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {

				adapter = new MyPagerAdapter(TotalArraySize, mProductDecs);
				myPager.setOnPageChangeListener(null);
				myPager.setAdapter(adapter);
				myPager.setCurrentItem(postion);
				myPager.setOnPageChangeListener(new OnPageChangeListener() {
					public void onPageScrollStateChanged(int state) {
					}

					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
					}

					public void onPageSelected(int pos) {
						// Check if this is the page you want.
						postion = pos;
						itemid = mItemList.get(pos);

						ItemDescModal _cate = mProductDecs.get(pos);
						boolean status = _cate.isApiCall_Status();
						if (!status) {
							if (cd.checkConnection()) {
								new ProductDetailsBackTask().execute("");
							} else {
								Toast.makeText(ProductDetails.this,
										"internet_conn_failed",
										Toast.LENGTH_LONG).show();
							}
						}
					}
				});
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else {
				Custom_Dialog.dialogCode(2, null,message,
						context);
				
				// ValidationSingleButton("no records found.");
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	public void getLocation() {
		try {
			locationManager = (LocationManager) ProductDetails.this
					.getSystemService(Context.LOCATION_SERVICE);

			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
			} else {
				this.canGetLocation = true;

				if (isNetworkEnabled) {

					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						lat = location.getLatitude();
						log = location.getLongitude();
						strlatitude = "" + lat;
						strlongitude = "" + log;
						
					}
				}
				if (isGPSEnabled) {

					location = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location != null) {
						lat = location.getLatitude();
						log = location.getLongitude();
						strlatitude = "" + lat;
						strlongitude = "" + log;
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class MyPagerAdapter extends PagerAdapter {
		int size;
		
		View layout;
		ArrayList<ItemDescModal> mProducDetails = new ArrayList<ItemDescModal>();
		TextView tv_Name, tvDecs, tvCondition,  tvLocalPickUp,
				tvPrice, tvHeaders;
		ImageView ivChat, ivBuyNow, ivMakeOffer, ivFollowing, ivComment,
				ivUserSeller, ivPost, iv1, iv2, iv3, iv4, ivDefault, ivBack,ivShar,ivMenu;
		EditText evComment;
		ListView lvComment;
		Button btnDistance, btnFav;
		String strImage1 = "", strdefault = "";
		Bitmap bmp1, bmpdefault;
		RelativeLayout rlVideo, rlBuyNowbtn, rlMakeOffer, llRating, llReview;
		LinearLayout llReviewItem;
		RatingBar rb_User_review_Rating;
		TableLayout tblComment;

		public MyPagerAdapter(int noofsize,
				ArrayList<ItemDescModal> mProducDetails) {
			size = noofsize;
			this.mProducDetails = mProducDetails;
		}

		public int getCount() {
			// Log.e("getCount Size", ""+myMatchineList.size());
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public Object instantiateItem(View collection, int position) {
			pos = position;
			LayoutInflater inflater = (LayoutInflater) ProductDetails.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.product_details_item, null);

			btnDistance = (Button) layout.findViewById(R.id.btnDistance);
			tvPrice = (TextView) layout.findViewById(R.id.tvPrice);
			ivChat = (ImageView) layout.findViewById(R.id.ivChat);
			ivDefault = (ImageView) layout.findViewById(R.id.ivDefault);
			iv1 = (ImageView) layout.findViewById(R.id.iv1);
			iv2 = (ImageView) layout.findViewById(R.id.iv2);
			iv3 = (ImageView) layout.findViewById(R.id.iv3);
			iv4 = (ImageView) layout.findViewById(R.id.iv4);
			ivMenu = (ImageView) layout.findViewById(R.id.ivMenu);
			ivShar = (ImageView) layout.findViewById(R.id.ivShar);
			tblComment = (TableLayout) layout.findViewById(R.id.tblComment);
			tv_Name = (TextView) layout.findViewById(R.id.tv_Name);
			tvDecs = (TextView) layout.findViewById(R.id.tvDecs);
			tvCondition = (TextView) layout.findViewById(R.id.tvCondition);
			tvLocalPickUp = (TextView) layout.findViewById(R.id.tvLocalPickUp);
			ivBuyNow = (ImageView) layout.findViewById(R.id.ivBuyNow);
			ivMakeOffer = (ImageView) layout.findViewById(R.id.ivMakeOffer);
			ivFollowing = (ImageView) layout.findViewById(R.id.ivFollowing);
			ivComment = (ImageView) layout.findViewById(R.id.ivComment);
			ivUserSeller = (ImageView) layout.findViewById(R.id.ivUserSeller);
			ivPost = (ImageView) layout.findViewById(R.id.ivPost);
			evComment = (EditText) layout.findViewById(R.id.evcomment);
			rlVideo = (RelativeLayout) layout.findViewById(R.id.rlVideo);
			rlBuyNowbtn = (RelativeLayout) layout.findViewById(R.id.rlBuyNowbtn);
			btnDistance = (Button) layout.findViewById(R.id.btnDistance);
			btnFav = (Button) layout.findViewById(R.id.btnFav);
			tvHeaders = (TextView) layout.findViewById(R.id.tvHeader);
			ivBack = (ImageView) layout.findViewById(R.id.ivBack);
			rlMakeOffer = (RelativeLayout) layout.findViewById(R.id.rlMakeOffer);
			llReview = (RelativeLayout) layout.findViewById(R.id.llReview);
			llRating = (RelativeLayout) layout.findViewById(R.id.llRating);
			
			iLoader_item.init(ImageLoaderConfiguration.createDefault(context));
			options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.list_item_image_frame)
			.showImageForEmptyUri(R.drawable.list_item_image_frame)
			.showImageOnFail(R.drawable.list_item_image_frame)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
			
			option_user_seller= new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.transperant)
			.showImageForEmptyUri(R.drawable.transperant)
			.showImageOnFail(R.drawable.transperant)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
			
			
			ivMenu.setVisibility(View.VISIBLE);
			rb_User_review_Rating = (RatingBar) layout.findViewById(R.id.rb_User_review_Rating);
			final ItemDescModal _list = mProducDetails.get(position);

			String following = "";
			following = _list.getIs_follow();
			if (following.equals("Y")) {
				// seller_is_follow = "N";
				ivFollowing.setImageResource(R.drawable.product_details_following_green);
			} else {
				ivFollowing.setImageResource(R.drawable.product_details_following);
			}

			
			String path = "";
			path = _list.getVideo();
			if(path.length()==0){
				rlVideo.setVisibility(View.INVISIBLE);
			}
			
			
			tvHeaders.setText("" + _list.getName());
			cnt = _list.getmImage().size();
			if (cnt == 0) {

				ivDefault.setEnabled(false);
				ivDefault.setClickable(false);
				iv1.setEnabled(false);
				iv1.setClickable(false);
				iv2.setEnabled(false);
				iv2.setClickable(false);
				iv3.setEnabled(false);
				iv3.setClickable(false);

			} else if (cnt == 1) {

				ivDefault.setEnabled(true);
				ivDefault.setClickable(true);
				iv1.setEnabled(false);
				iv1.setClickable(false);
				iv2.setEnabled(false);
				iv2.setClickable(false);
				iv3.setEnabled(false);
				iv3.setClickable(false);

			} else if (cnt == 2) {

				ivDefault.setEnabled(true);
				ivDefault.setClickable(true);
				iv1.setEnabled(true);
				iv1.setClickable(true);
				iv2.setEnabled(false);
				iv2.setClickable(false);
				iv3.setEnabled(false);
				iv3.setClickable(false);

			} else if (cnt == 3) {

				ivDefault.setEnabled(true);
				ivDefault.setClickable(true);
				iv1.setEnabled(true);
				iv1.setClickable(true);
				iv2.setEnabled(true);
				iv2.setClickable(true);
				iv3.setEnabled(false);
				iv3.setClickable(false);

			} else if (cnt == 4) {

				ivDefault.setEnabled(true);
				ivDefault.setClickable(true);
				iv1.setEnabled(true);
				iv1.setClickable(true);
				iv2.setEnabled(true);
				iv2.setClickable(true);
				iv3.setEnabled(true);
				iv3.setClickable(true);
			}
			ArrayList<ProductDetailsCommentModal> mcomment_poswis= new ArrayList<ProductDetailsCommentModal>();
			mcomment_poswis.clear();
			mcomment_poswis=_list.getmComment();
			Log.e("comment size while getting"," "+ mcomment_poswis.size());
		//	AddCommentTBl(mcomment_poswis,tblComment);
			tblComment.removeAllViews();
			LayoutInflater minflater;
			for (int i = 0; i < mcomment_poswis.size(); i++) {
				minflater = getLayoutInflater();
				View convertView = (View) minflater.inflate(
						R.layout.product_details_comment, null);

				tblComment.addView(convertView);
				ImageView ivComment = (ImageView) convertView
						.findViewById(R.id.ivComment);
				TextView tvCommentName = (TextView) convertView
						.findViewById(R.id.tvCommentName);
				TextView tvComment = (TextView) convertView
						.findViewById(R.id.tvComment);
				
				ProductDetailsCommentModal myValues = mcomment_poswis.get(i);
				String imgComment = myValues.getImage();
				
				option_comment= new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.product_details_comment_user_bg)
				.showImageForEmptyUri(R.drawable.product_details_comment_user_bg)
				.showImageOnFail(R.drawable.product_details_comment_user_bg)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
				//asdasd
				
			/*	if (imgComment != null) {
					try {
						imageLoader.DisplayImage(imgComment, ivComment);
					} catch (Exception e) {
					}
				} else {
					ivComment.setImageResource(R.drawable.ic_launcher);
				}
	*/
				
				
				// Image display using lazy loading 

				iLoader_item.displayImage(imgComment, ivComment, option_comment, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						//holder.pbimage.setProgress(0);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view, int current, int total) {
//						holder.pbimage.setIndeterminate(true);
					}
				});
				
				// Image display using lazy loading

				
				tvCommentName.setText(myValues.getName());
				tvCommentName.setTag(myValues.getUserid());
				tvComment.setText(myValues.getComment());
			}
			
			tv_Name.setText(_list.getName());
			tv_Name.setTag(_list.getItemid());
			tvDecs.setText(_list.getItem_description());
			tvPrice.setText(_list.getSell_price());
			tvCondition.setText(_list.getItem_condition());
			tvLocalPickUp.setText(_list.getLocal_pickup());
			btnDistance.setText(_list.getDistance());
			// btnFav.settext
			rlVideo.setTag(_list.getVideo());
			btnFav.setText(_list.getNo_of_likes());
			seller_image = _list.getSeller_image();
			
			
			//pbimage1,pbimage2,pbimage3,pbimage4,pbimage
			
			
		/*	if (seller_image.length() != 0) {
				try {
					imageLoader.DisplayImage(seller_image, ivUserSeller);
				} catch (Exception e) {
				}
			}
			*/
			

			// Image display using lazy loading 

			iLoader_item.displayImage(seller_image, ivUserSeller, option_user_seller, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					//holder.pbimage.setProgress(0);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				}
			}, new ImageLoadingProgressListener() {
				@Override
				public void onProgressUpdate(String imageUri, View view, int current, int total) {
					//holder.pbimage.setProgress(Math.round(100.0f * current / total));
//					holder.pbimage.setIndeterminate(true);
				}
			});
			
			// Image display using lazy loading
			
			
			
			String liske = "";
			liske = _list.getRating();

			if (liske.length() == 0) {
				liske = "0";
			}
			float like = 0;
			try {
				like = Float.parseFloat(liske);
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			rb_User_review_Rating.setRating(like);
			ivBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
 
				
					Intent returnIntent = new Intent();
					returnIntent.putExtra("postion", postion);
					returnIntent.putExtra("newLikes", newLikes);
					returnIntent.putExtra("Has_like", Has_like);
					if(FavStatus){
						setResult(RESULT_OK, returnIntent);
					}else{
						setResult(RESULT_CANCELED, returnIntent);
					}
					finish();	
				}
			});

			ArrayList<ProductDetlImage> m_image = new ArrayList<ProductDetlImage>();
			m_image.clear();
			m_image = _list.getmImage();
			default_image = _list.getDefault_image();

			
		/*	try {
				imageLoader.DisplayImage(_list.getDefault_image(), ivDefault);
			} catch (Exception e) {
			}*/

			
			// Image display using lazy loading 

			iLoader_item.displayImage(_list.getDefault_image(), ivDefault, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					//holder.pbimage.setProgress(0);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				}
			}, new ImageLoadingProgressListener() {
				@Override
				public void onProgressUpdate(String imageUri, View view, int current, int total) {
					//holder.pbimage.setProgress(Math.round(100.0f * current / total));
//					holder.pbimage.setIndeterminate(true);
				}
			});
			
			// Image display using lazy loading
			
			
			
			
			
			ProductDetlImage _image = null;

			for (int i = 0; i < m_image.size(); i++) {
				_image = m_image.get(i);

				switch (i) {
				case 0:
					_image = null;
					_image = m_image.get(i);
					if (_image.getImage().length() != 0) {
						try {
							boolimg1 = true;
						//	imageLoader.DisplayImage(_image.getImage(), iv1);
							
							
							// Image display using lazy loading 

							iLoader_item.displayImage(_image.getImage(), iv1, options, new SimpleImageLoadingListener() {
								@Override
								public void onLoadingStarted(String imageUri, View view) {
									//holder.pbimage.setProgress(0);
//									holder.pbimage.setIndeterminate(true);
								}

								@Override
								public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
								}

								@Override
								public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								}
							}, new ImageLoadingProgressListener() {
								@Override
								public void onProgressUpdate(String imageUri, View view, int current, int total) {
//									holder.pbimage.setIndeterminate(true);
								}
							});
							
							// Image display using lazy loading
							
							
							
							
							
							iv1.setTag(_image.getImage());
						} catch (Exception e) {
						}
					} else {
						boolimg1 = false;
						iv1.setClickable(false);
						iv1.setEnabled(false);
						iv1.setFocusable(false);
						iv1.setFocusableInTouchMode(false);
					}


					break;
				case 1:

					_image = null;
					_image = m_image.get(i);
					if (_image.getImage().length() != 0) {
						boolimg2 = true;
						try {

							// Image display using lazy loading 

							iLoader_item.displayImage(_image.getImage(), iv2, options, new SimpleImageLoadingListener() {
								@Override
								public void onLoadingStarted(String imageUri, View view) {
									//holder.pbimage.setProgress(0);
								}

								@Override
								public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
								}

								@Override
								public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								}
							}, new ImageLoadingProgressListener() {
								@Override
								public void onProgressUpdate(String imageUri, View view, int current, int total) {
//									holder.pbimage.setIndeterminate(true);
								}
							});
							
							// Image display using lazy loading
							
							
							
							
							iv2.setTag(_image.getImage());
						} catch (Exception e) {
						}
					} else {
						boolimg2 = false;
						iv2.setClickable(false);
						iv2.setEnabled(false);
						iv2.setFocusable(false);
						iv2.setFocusableInTouchMode(false);
					}


					break;
				case 2:

					_image = null;
					_image = m_image.get(i);
					if (_image.getImage().length() != 0) {
						boolimg3 = true;
						try {
							//imageLoader.DisplayImage(_image.getImage(), iv3);
							
							// Image display using lazy loading 

							iLoader_item.displayImage(_image.getImage(), iv3, options, new SimpleImageLoadingListener() {
								@Override
								public void onLoadingStarted(String imageUri, View view) {
									//holder.pbimage.setProgress(0);
								}

								@Override
								public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
								}

								@Override
								public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								}
							}, new ImageLoadingProgressListener() {
								@Override
								public void onProgressUpdate(String imageUri, View view, int current, int total) {
//									holder.pbimage.setIndeterminate(true);
								}
							});
							
							// Image display using lazy loading
							
							
							
							iv3.setTag(_image.getImage());
						} catch (Exception e) {
						}
					} else {
						boolimg3 = false;
						iv3.setClickable(false);
						iv3.setEnabled(false);
						iv3.setFocusable(false);
						iv3.setFocusableInTouchMode(false);
					}


					break;
				case 3:

					_image = null;
					_image = m_image.get(i);
					if (_image.getImage().length() != 0) {
						boolimg4 = true;
						try {
							
							// Image display using lazy loading 

							iLoader_item.displayImage(_image.getImage(), iv4, options, new SimpleImageLoadingListener() {
								@Override
								public void onLoadingStarted(String imageUri, View view) {
									//holder.pbimage.setProgress(0);
								}

								@Override
								public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
								}

								@Override
								public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								}
							}, new ImageLoadingProgressListener() {
								@Override
								public void onProgressUpdate(String imageUri, View view, int current, int total) {
								}
							});
							
							// Image display using lazy loading

							iv4.setTag(_image.getImage());
						} catch (Exception e) {
						}
					} else {
						boolimg4 = false;
						iv4.setClickable(false);
						iv4.setEnabled(false);
						iv4.setFocusable(false);
						iv4.setFocusableInTouchMode(false);
					}

					break;
				default:
					break;
				}

			}
			ivFollowing.setTag(_list);
			ivFollowing.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					
					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();
					seller_is_follow = _mlist.getIs_follow();
					ivFollowing.requestFocus();
					PosForFav = mProducDetails.indexOf(_mlist);
					V=v;
					
					mPDetails=new ArrayList<ItemDescModal>();
					mPDetails=mProducDetails;
					Intent iMenuBtn;
					buttonAnimation(v);
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " Follow");
						 startActivityForResult(iMenuBtn, 21);
					
					}else {
						
					
						if (cd.checkConnection()) {
							new FollowBackTask(v,mProducDetails).execute("");
						} else {
							Toast.makeText(ProductDetails.this,
									"internet_conn_failed", Toast.LENGTH_LONG)
									.show();
						}
						
					}
				
				}
			});
			
			ivShar.setTag(_list);
			ivShar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();
					
//				String	message="I Love this app";
//					
//				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//				sharingIntent.setType("text/html");
//				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
//						message);
//				startActivity(Intent.createChooser(sharingIntent,"TapnSell"));
					
//					String image=_mlist.getDefault_image().toString();
					String short_url=_mlist.getShorturl().toString();
					share(short_url);
				
				}
			});
			
			ivMenu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent iMenuBtn;
					buttonAnimation(v);
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " Follow");
						 startActivityForResult(iMenuBtn, 22);
					
					}else {
						Intent i =  new Intent(context,TapBoardActivity.class);
						startActivity(i);
						finish();
					}
					
				}
			});
			ivComment.setTag(_list);
			ivComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();
					Intent iMenuBtn;
					buttonAnimation(v);
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " Comment");
						 startActivityForResult(iMenuBtn, 23);
					
					}else {
						
						temp_pos=myPager.getCurrentItem();
						Intent i = new Intent(ProductDetails.this,
								ChatActivity.class);
						i.putExtra("itemid", _mlist.getItemid());
						i.putExtra("from_id", _mlist.getSeller_userid());
						i.putExtra("Distance", _mlist.getDistance());
						i.putExtra("ItemArray", mItemList);
						i.putExtra("position", "" + myPager.getCurrentItem());
						startActivity(i);
					}
					

				}
			});
			btnDistance.setTag(_list);
			btnDistance.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					

					
					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();

					Intent i = new Intent(ProductDetails.this,
							MapStoreActivity.class);
					i.putExtra("lat", "" + _mlist.getLatitude());
					i.putExtra("log", "" + _mlist.getLongitude());
					startActivity(i);

				}
			});

			ivMakeOffer.setTag(_list);
			ivMakeOffer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();
					Intent iMenuBtn;
					buttonAnimation(v);
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " MakeOffer");
						 startActivityForResult(iMenuBtn, 24);
					
					}else {
						String itemid = _mlist.getItemid();
						String image = _mlist.getDefault_image();
						String name = _mlist.getName();

						Intent i = new Intent(ProductDetails.this,
								MakeOfferActivity.class);
						i.putExtra("moffer_itemid", "" + itemid);
						i.putExtra("moffer_image", "" + image);
						i.putExtra("moffer_name", "" + name);
						startActivity(i);
					}
				

				}
			});
			rlMakeOffer.setTag(_list);
			rlMakeOffer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();

					Intent iMenuBtn;
					buttonAnimation(v);
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " MakeOffer");
						 startActivityForResult(iMenuBtn, 25);
					
					}else {
						String itemid = _mlist.getItemid();
						String image = _mlist.getDefault_image();
						String name = _mlist.getName();

						Intent i = new Intent(ProductDetails.this,
								MakeOfferActivity.class);
						i.putExtra("moffer_itemid", "" + itemid);
						i.putExtra("moffer_image", "" + image);
						i.putExtra("moffer_name", "" + name);
						startActivity(i);
					}
					

				}
			});
			ivChat.setTag(_list);
			ivChat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();
					temp_pos=myPager.getCurrentItem();
					Intent iMenuBtn;
					buttonAnimation(v);
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " Chat");
						 startActivityForResult(iMenuBtn, 26);
					
					}else {
						Intent i = new Intent(ProductDetails.this,ChatActivity.class);
						
						i.putExtra("itemid", _mlist.getItemid());
						i.putExtra("from_id", _mlist.getSeller_userid());
						i.putExtra("Distance", _mlist.getDistance());
						i.putExtra("ItemArray", mItemList);
						i.putExtra("position", "" + myPager.getCurrentItem());
						startActivity(i);
					}
				
				}
			});

			ivBuyNow.setTag(_list);
			ivBuyNow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();
					reserve = _mlist.getReserved();
					Intent iMenuBtn;
					buttonAnimation(v);
					
					if (reserve.length() != 0 && reserve.equals("Y")) {
						Toast.makeText(ProductDetails.this,
								"Item already reserved", Toast.LENGTH_LONG)
								.show();
					} else {
						temp_pos= myPager.getCurrentItem();
						mItem_IdList.clear();
						for (int i = 0; i < size; i++) {
							ItemDescModal _list = mProducDetails.get(i);

							mItem_IdList.add(_list.getItemid());
						}
						itemid = _mlist.getItemid();
						editor.putString("CHKImage",
								"" + _mlist.getDefault_image());
						editor.putString("CHKItemid", "" + _mlist.getItemid());
						editor.putString("CHKName", "" + _mlist.getName());
						editor.putString("CHKPrice",
								"" + _mlist.getSell_price());
						editor.commit();
						
						if (userid.length() == 0 || userid.equals("0")) {
							
							 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
							 iMenuBtn.putExtra("title", " Buy");
							 startActivityForResult(iMenuBtn, 27);
						
						}else {
							i_Buy = new Intent(ProductDetails.this,
									MainCheckOutFragmentActivity.class);
							i_Buy.putExtra("itemid", _mlist.getItemid());
							i_Buy.putExtra("from_id", _mlist.getUserid());
							i_Buy.putExtra("Distance", _mlist.getDistance());
							i_Buy.putExtra("ItemArray", mItem_IdList);
							i_Buy.putExtra("position",
									"" + myPager.getCurrentItem());
							reserve="Y";
							new LockItemBackTask().execute("");
						}
					

					}
				}
			});

			rlBuyNowbtn.setTag(_list);
			rlBuyNowbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();
					reserve = _mlist.getReserved();
					
					if (reserve.length() != 0 && reserve.equals("Y")) {
						Toast.makeText(ProductDetails.this,
								"Item already reserved", Toast.LENGTH_LONG)
								.show();
					} else {

						mItem_IdList.clear();
						for (int i = 0; i < size; i++) {
							ItemDescModal _list = mProducDetails.get(i);

							mItem_IdList.add(_list.getItemid());
						}
						temp_pos=myPager.getCurrentItem();
						itemid = _mlist.getItemid();
						editor.putString("CHKImage",
								"" + _mlist.getDefault_image());
						editor.putString("CHKItemid", "" + _mlist.getItemid());
						editor.putString("CHKName", "" + _mlist.getName());
						editor.putString("CHKPrice",
								"" + _mlist.getSell_price());
						editor.commit();
						Intent iMenuBtn;
						buttonAnimation(v);
						if (userid.length() == 0 || userid.equals("0")) {
							
							 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
							 iMenuBtn.putExtra("title", " BuyNow");
							 startActivityForResult(iMenuBtn, 28);
						
						}else {
							i_Buy = new Intent(ProductDetails.this,
									MainCheckOutFragmentActivity.class);
							i_Buy.putExtra("itemid", _mlist.getItemid());
							i_Buy.putExtra("from_id", _mlist.getUserid());
							i_Buy.putExtra("Distance", _mlist.getDistance());
							i_Buy.putExtra("ItemArray", mItem_IdList);
							i_Buy.putExtra("position",
									"" + myPager.getCurrentItem());
							reserve="Y";
							new LockItemBackTask().execute("");
						}
						

					}
				}
			});
			rlVideo.setTag(_list);
			rlVideo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();
					String path = "";
					path = _mlist.getVideo();
					
					if (path.length() != 0) {

						Intent iVideo = new Intent(ProductDetails.this,
								VideoPreviewProductDetails.class);
						iVideo.putExtra("url", path);
						startActivity(iVideo);
						finish();
					} else {
						Custom_Dialog.dialogCode(2, null, "Video is not available",
								context);
					}
				}
			});

			iv1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (boolimg1) {

						strImage1 = "";

						strImage1 = iv1.getTag().toString();

						if (strImage1.length() != 0) {
							try {

								int current_item = myPager.getCurrentItem();
								ItemDescModal _mlist = mProducDetails
										.get(current_item);
								_mlist.setDefault_image(strImage1);
								mProducDetails.set(current_item, _mlist);
								notifyDataSetChanged();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						Toast.makeText(ProductDetails.this,
								"There is no image", Toast.LENGTH_LONG).show();
					}

				}
			});
			iv2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (boolimg2) {

						strImage1 = iv2.getTag().toString();

						int current_item = myPager.getCurrentItem();
						ItemDescModal _mlist = mProducDetails.get(current_item);
						_mlist.setDefault_image(strImage1);
						mProducDetails.set(current_item, _mlist);
						notifyDataSetChanged();

					} else {
						Toast.makeText(ProductDetails.this,
								"There is no image", Toast.LENGTH_LONG).show();
					}
				}
			});

			iv3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (boolimg3) {

						strImage1 = iv3.getTag().toString();

						int current_item = myPager.getCurrentItem();
						ItemDescModal _mlist = mProducDetails.get(current_item);
						_mlist.setDefault_image(strImage1);
						mProducDetails.set(current_item, _mlist);
						notifyDataSetChanged();

					} else {
						Toast.makeText(ProductDetails.this,
								"There is no image", Toast.LENGTH_LONG).show();
					}
				}
			});

			iv4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (boolimg4) {

						strImage1 = iv4.getTag().toString();
						int current_item = myPager.getCurrentItem();
						ItemDescModal _mlist = mProducDetails.get(current_item);
						_mlist.setDefault_image(strImage1);
						mProducDetails.set(current_item, _mlist);
						notifyDataSetChanged();

					} else {
						Toast.makeText(ProductDetails.this,
								"There is no image", Toast.LENGTH_LONG).show();
					}

				}
			});
			btnFav.setTag(_list);
			btnFav.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(!FavStatus){
						FavStatus=true;
					}else{
						FavStatus=false;
					}
					V=v;
					mPDetails=new ArrayList<ItemDescModal>();
					mPDetails=mProducDetails;
					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();

					has_like = _mlist.getHas_like();
					
					Intent iMenuBtn;
					buttonAnimation(v);
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " favourite");
						 startActivityForResult(iMenuBtn, 29);
					
					}else {
						if (has_like.equals("Y")) {
							Drawable drawableTop = getResources().getDrawable(
									R.drawable.fav_unlike);
							btnFav.setCompoundDrawablesWithIntrinsicBounds(null,
									drawableTop, null, null);
							is_like = "N";
							Has_like= "N";
							_mlist.setHas_like("N");
							itemid = _mlist.getItemid().toString();
							PosForFav = mProducDetails.indexOf(_mlist);
							new FavBackTask(v,mProducDetails).execute("");

						} else {
							Drawable drawableTop1 = getResources().getDrawable(
									R.drawable.fav_like);
							btnFav.setCompoundDrawablesWithIntrinsicBounds(null,
									drawableTop1, null, null);
							//
							is_like = "Y";
							Has_like= "Y";
							_mlist.setHas_like("Y");
							itemid = _mlist.getItemid().toString();
							PosForFav = mProducDetails.indexOf(_mlist);
							new FavBackTask(v,mProducDetails).execute("");
						}
					}
				
				}
			});
			llReview.setTag(_list);
			llReview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					_mlist = new ItemDescModal();
					_mlist = (ItemDescModal) v.getTag();
					buttonAnimation(v);
						
						Intent i = new Intent(ProductDetails.this, ReviewUser.class);
						i.putExtra("Seller_userid", "" + _mlist.getSeller_userid());
						startActivity(i);
				
				}
			});

			llRating.setTag(_list);
			llRating.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					_mlist = new ItemDescModal();

					_mlist = (ItemDescModal) v.getTag();
					Intent iMenuBtn;
					buttonAnimation(v);
					rb_Rating2=rb_User_review_Rating;
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " UserReview");
						 startActivityForResult(iMenuBtn, 31);
					
					}else {
						strRatting_ItemId = _mlist.getItemid();
						strRatting_toid = _mlist.getSeller_userid();
						strRatting_UserName = _mlist.getSeller_name();
						strRatting_Image = _mlist.getSeller_image();
						strRatting_Fromid = userid;
						FacebookSharurl=_mlist.getSeller_image();
						rattingname=_mlist.getSeller_name().toString();
						
						
						NiceReviewdialogCode(rb_User_review_Rating,pos,_mlist);
					}
					

				}
			});
			ivPost.setTag(_list);
			ivPost.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent iMenuBtn;
					buttonAnimation(v);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(evComment.getWindowToken(), 0);
//shail
					if (cd.checkConnection()) {
						_mlist = new ItemDescModal();
						_mlist = (ItemDescModal) v.getTag();
						itemid = _mlist.getItemid();
//						PosForFav = mProducDetails.indexOf(_mlist);
						PosForFav=myPager.getCurrentItem();
						View row;
						row = myPager.getChildAt(PosForFav);
						evComment = (EditText) row.findViewById(R.id.evcomment);
						comment = evComment.getText().toString();
						mPDetails=new ArrayList<ItemDescModal>();
						mPDetails=mProducDetails;
						if (comment.length() == 0) {
							Custom_Dialog.dialogCode(2, null,
									"Please enter valid text!",
									ProductDetails.this);
						} else {
							if (userid.length() == 0 || userid.equals("0")) {
								mtemp_Comment=_mlist.getmComment();
								 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
								 iMenuBtn.putExtra("title", " Post Comment");
								 startActivityForResult(iMenuBtn, 32);
							
							}else {
								new PostBackTask(_mlist.getmComment(),PosForFav).execute("");
							}
						
						}

					} else {
						Toast.makeText(ProductDetails.this,
								"internet_conn_failed", Toast.LENGTH_LONG)
								.show();
					}

				}
			});

			((ViewPager) collection).addView(layout, ((ViewPager) collection)
					.getChildCount() > position ? position
					: ((ViewPager) collection).getChildCount());

			return layout;
		}

	public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		public boolean isViewFromObject(View view, Object object) {
			// return view == ((ImageView) object);
			return view == ((View) object);
		}

		public void finishUpdate(View arg0) {
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View arg0) {
		}
	}
public void getBitmapFromURL(String img_url) {
		
		try {
	        URL url = new URL(img_url);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        bitmapFromUrl = BitmapFactory.decodeStream(input);
	        
	        if(bitmapFromUrl!=null){
	   	 try {
	   		 
	   		 File myDir3 = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/ShareImages");
	     	
	 	    casted_image = new File(myDir3, "PromoteSharing.jpg");
	 	    if (casted_image.exists()) {
	 		casted_image.delete();
	 	    }	   
	 			casted_image.createNewFile();
	 			FileOutputStream fos = new FileOutputStream(casted_image);
	 			
	 			bitmapFromUrl.compress(Bitmap.CompressFormat.PNG, 50, fos);
	 		    fos.flush();
	 		    fos.close();
	 		   
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 			 System.out.print(e);
	 		}

	        }
	    } catch (IOException e) {
	    	Log.e("catch", "catch");
	        e.printStackTrace();
	        casted_image=null;
	    }
	
	}
	
/*	private class UrlBitmap extends AsyncTask<String, Void, String> {
		String errorMessage,img_url,normalurl="";

		public UrlBitmap(String url, String ur) {
			img_url=url;
			normalurl=ur;
		}

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ProductDetails.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				getBitmapFromURL(img_url);
				
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			Log.e("Shorturl", " "+normalurl);
			
		}
	}
	
*/	
	public void share(String normalurl) {
		
		  //Intent share = new Intent(Intent.ACTION_SEND);
		  
		
			
		  
		String	message="Just saw this item on #TapNSell. Thought you might be interested?"+" \n "+normalurl+"\n\nThanks!";
		 
		 //  Resources resources = context.getResources();

		      Intent emailIntent = new Intent();
		      emailIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
		      // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
		      emailIntent.setType("image/*"); 
		      emailIntent.putExtra(Intent.EXTRA_TEXT, message);
		      emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Check this out:");    
		      emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		      emailIntent.setType("message/rfc822");

		      PackageManager pm = context.getPackageManager();
		      Intent sendIntent = new Intent(Intent.ACTION_SEND);     
		      sendIntent.setType("text/plain");

		      Intent openInChooser = Intent.createChooser(emailIntent, "Share using");

		      List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
		      List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();        
		      for (int i = 0; i < resInfo.size(); i++) {
		          // Extract the label, append it, and repackage it in a LabeledIntent
		          ResolveInfo ri = resInfo.get(i);
		          String packageName = ri.activityInfo.packageName;
		          if(packageName.contains("android.email")) {
		              emailIntent.setPackage(packageName);
		          } else if(packageName.contains("plus")||packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm")) {
		            
		        	  if(packageName.contains("plus")){
		        		  message="Just saw this on TapNSell and thought you might be interested? #tapnsell"+" \n "+normalurl+"\n\nThanks!";
		        	  }else if(packageName.contains("twitter")){
		        		  message="Just saw this item on #TapNSell. Thought you might be interested?"+" \n "+normalurl+"\n\nThanks!";
		        	  }else if(packageName.contains("facebook")){
		        		  message="Just saw this item on #TapNSell. Thought you might be interested?"+" \n "+normalurl+"\n\nThanks!";
		        	  }else if(packageName.contains("android.gm")){
		          		  message="Just saw this on TapNSell and thought you might be interested? #tapnsell"+" \n "+normalurl+"\n\nThanks!";
		        	  }
		        	  
		        	  Intent intent = new Intent();
		              intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
		              intent.setAction(Intent.ACTION_SEND);
		              intent.setType("text/plain");
		              if(packageName.contains("twitter")) {
		                  intent.putExtra(Intent.EXTRA_TEXT,message);
		              } else if(packageName.contains("facebook")) {
		                  // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
		                  // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
		                  // will show the <meta content ="..."> text from that page with our link in Facebook.
		               intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		               intent.setType("image/*");               
		               intent.putExtra(Intent.EXTRA_TEXT, message);
		               intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		            
		              } else if(packageName.contains("mms")) {
		                  intent.putExtra(Intent.EXTRA_TEXT, message);
		              } else if(packageName.contains("android.gm")) {
		               intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		               intent.setType("image/*"); 
		                  intent.putExtra(Intent.EXTRA_TEXT, message);
		                  intent.putExtra(Intent.EXTRA_SUBJECT,  "Check this out:");    
		                  intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		                  intent.setType("message/rfc822");
		              }

		              intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
		          }
		      }

		      // convert intentList to array
		      LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

		      openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
		      context.startActivity(openInChooser);       
		  
		 }

	
	public class PostBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;
		int pos;
		ArrayList<ProductDetailsCommentModal> m_newcomment;
		public PostBackTask(ArrayList<ProductDetailsCommentModal> getmComment, int posForFav) {
			m_newcomment=getmComment;
			pos=posForFav;
		}

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ProductDetails.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{

				
				mComment.clear();
				JSONObject json = userFunction.CommentFunction(userid,
						itemid, comment);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONArray jComment = json
									.getJSONArray("Comment");
							m_newcomment.clear();
							for (int i = 0; i < jComment.length(); i++) {
								JSONObject c = jComment.getJSONObject(i);
								String comment = c.getString("comment");
								String userid = c.getString("userid");
								String	image = c.getString("image");
								String name = c.getString("name");
								m_newcomment.add(new ProductDetailsCommentModal(
										comment, userid, image, name));
							}

							errorMessage = "true";

						} else {
							message=json.optString("message");
							errorMessage = "false";
						}
					} else {
						errorMessage = "network";
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {

				_mlist.setmComment(m_newcomment);
				
				mPDetails.set(pos, _mlist);
				adapter.notifyDataSetChanged();
				Custom_Dialog.dialogCode(2, null,
						"Comment posted successfully", context);


			} else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null,
						message, context);

			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


	public void AddCommentTBl(ArrayList<ProductDetailsCommentModal> myValueList2, TableLayout tblComment) {

		tblComment.removeAllViews();
		LayoutInflater inflater;
		for (int i = 0; i < myValueList2.size(); i++) {
			inflater = getLayoutInflater();
			View convertView = (View) inflater.inflate(
					R.layout.product_details_comment, null);

			tblComment.addView(convertView);
			ImageView ivComment = (ImageView) convertView
					.findViewById(R.id.ivComment);
			TextView tvCommentName = (TextView) convertView
					.findViewById(R.id.tvCommentName);
			TextView tvComment = (TextView) convertView
					.findViewById(R.id.tvComment);
			
			ProductDetailsCommentModal myValues = myValueList2.get(i);
			String imgComment = myValues.getImage();
			
			option_comment= new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.product_details_comment_user_bg)
			.showImageForEmptyUri(R.drawable.product_details_comment_user_bg)
			.showImageOnFail(R.drawable.product_details_comment_user_bg)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
			//asdasd
			
		/*	if (imgComment != null) {
				try {
					imageLoader.DisplayImage(imgComment, ivComment);
				} catch (Exception e) {
				}
			} else {
				ivComment.setImageResource(R.drawable.ic_launcher);
			}
*/
			
			
			// Image display using lazy loading 

			iLoader_item.displayImage(imgComment, ivComment, option_comment, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					//holder.pbimage.setProgress(0);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				}
			}, new ImageLoadingProgressListener() {
				@Override
				public void onProgressUpdate(String imageUri, View view, int current, int total) {
//					holder.pbimage.setIndeterminate(true);
				}
			});
			
			// Image display using lazy loading

			
			tvCommentName.setText(myValues.getName());
			tvCommentName.setTag(myValues.getUserid());
			tvComment.setText(myValues.getComment());
		}
		// evComment.setText("");
	}

	private class LockItemBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ProductDetails.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{

				
				JSONObject json = userFunction.ReserveFunction(userid, itemid,
						reserve);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							address=json.optString("address");
							errorMessage = "true";

						} else {
							errorMessage = "false";
							message=json.optString("message");
						}
					} else {
						errorMessage = "network";
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {
				i_Buy.putExtra("address", address);
				startActivity(i_Buy);
			} else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null, message, context);

				// ValidationSingleButton("Error in posting");
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	private class RattingReviewBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,ratting,message;
		RatingBar rbRatting;
		int position5;
		ItemDescModal mlist;
		
		public RattingReviewBackTask(RatingBar rb_User_review_Rating, int pos2, ItemDescModal _mlist2) {
			rbRatting=rb_User_review_Rating;
			position5=pos2;
			mlist=_mlist2;
		}

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ProductDetails.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{

				

				JSONObject json = userFunction.RattingFunction(
						strRatting_ItemId, strRatting_Fromid, strRatting_toid,
						"", strRatting_Review, strRatting_Ratting,"");
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							message=json.optString("message");
							
							JSONObject jReview=json.optJSONObject("Review");
							ratting=jReview.optString("rating");
							errorMessage = "true";

						} else {
							message=json.optString("message");
							errorMessage = "false";
						}
					} else {
						errorMessage = "network";
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {
				Custom_Dialog.dialogCode(2, null,
						message, context);
				float like = 0;
				try {
					like = Float.parseFloat(ratting);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
				mlist.setRating(ratting);
				mProductDecs.set(position5, mlist);
				rbRatting.setRating(like);
				adapter.notifyDataSetChanged();
				NiceReviewFaceBookShardialogCode();
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else {
				Custom_Dialog.dialogCode(2, null, message, context);
				// ValidationSingleButton("Error in posting");
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	/*
	 * private void dialogCode() {
	 * 
	 * // custom dialog final Dialog dialog = new Dialog(ProductDetails.this);
	 * dialog.getWindow().setBackgroundDrawable(new
	 * ColorDrawable(android.graphics.Color.TRANSPARENT));
	 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * dialog.setContentView(R.layout.feedback_dialog);
	 * WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	 * lp.copyFrom(dialog.getWindow().getAttributes()); lp.width =
	 * lp.MATCH_PARENT; lp.height = lp.MATCH_PARENT;
	 * dialog.getWindow().setAttributes(lp); ImageView ivClose = (ImageView)
	 * dialog.findViewById(R.id.ivClose); ImageView ivUserIcon = (ImageView)
	 * dialog.findViewById(R.id.ivUserIcon); TextView tvUserName= (TextView)
	 * dialog.findViewById(R.id.tvUserName); EditText evFeedback_share_exp=
	 * (EditText) dialog.findViewById(R.id.evFeedback_share_exp); ImageView
	 * ivRateNow = (ImageView) dialog.findViewById(R.id.ivRateNow);
	 * 
	 * ivFacebook.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { dialog.dismiss(); FacebookCode();
	 * } }); ivTwitter.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * dialog.dismiss(); twitterCode(); } });
	 * 
	 * 
	 * ivGoogle.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { dialog.dismiss();
	 * GooglePlusCode(); } });
	 * 
	 * tvSignIn.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * dialog.dismiss(); SingInCode(); } });
	 * 
	 * tvRegister.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * dialog.dismiss(); RegisterCode(); } });
	 * 
	 * dialog.show(); }
	 */

	
	
	
	@SuppressWarnings("static-access")
	private void NiceReviewdialogCode(final RatingBar rb_User_review_Rating, final int pos2, final ItemDescModal _mlist2) {
		// custom dialog
		final Dialog dialog = new Dialog(ProductDetails.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.feedback_dialog);
		dialog.setCancelable(false);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
		ImageView ivRateNow = (ImageView) dialog.findViewById(R.id.ivRateNow);
		ImageView ivUserIcon = (ImageView) dialog.findViewById(R.id.ivUserIcon);
		TextView tvUserName = (TextView) dialog.findViewById(R.id.tvUserName);
		final RatingBar rb_User_review_Item = (RatingBar) dialog
				.findViewById(R.id.rb_User_review_Item);
		final EditText evFeedback_share_exp = (EditText) dialog
				.findViewById(R.id.evFeedback_share_exp);

		tvUserName.setText(strRatting_UserName);

		
		ImageLoaderTopBottamRounded	imageloader = new ImageLoaderTopBottamRounded(context);

		if (strRatting_Image != null) {
			try {
				imageloader.DisplayImage(strRatting_Image, ivUserIcon);
			} catch (Exception e) {
			}
		} else {
			ivUserIcon.setImageResource(R.drawable.ic_launcher);
		}
		
		
		

		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		ivRateNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				strRatting_Review = evFeedback_share_exp.getText().toString()
						.trim();
				strRatting_Ratting = "" + rb_User_review_Item.getRating();
			float rat=	rb_User_review_Item.getRating();
			
			if(rat<=0){
				Custom_Dialog.dialogCode(2, null, "Please give ratting to user",context);
			}else if(strRatting_Review.length()==0){
				Custom_Dialog.dialogCode(2, null, "Please give review to user",context);
			}else{
				dialog.dismiss();
				if (cd.checkConnection()) {
					new RattingReviewBackTask(rb_User_review_Rating,pos2,_mlist2).execute("");
				} else {
					Toast.makeText(ProductDetails.this, "internet_conn_failed",
							Toast.LENGTH_LONG).show();
				}
			}
			}
		});

		dialog.show();
	}

	@SuppressWarnings("static-access")
	private void NiceReviewFaceBookShardialogCode() {
		// custom dialog
		final Dialog dialog = new Dialog(ProductDetails.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_nice_review);
		dialog.setCancelable(false);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
		ImageView ivFacebookShar = (ImageView) dialog
				.findViewById(R.id.ivFacebookShar);

		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		ivFacebookShar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
				progressDialog = new ProgressDialog(ProductDetails.this);
				progressDialog.setMessage("Please wait");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
				
				if (cd.checkConnection()) {
					sharewithFb();
				} else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		dialog.show();
	}

	// /////////////////////Facebook Start/////////////////////////////

	public void sharewithFb() {
		mCurrentSession = Session.getActiveSession();
		if (mCurrentSession == null) {
			mCurrentSession = new Session(this);
			Session.setActiveSession(mCurrentSession);
			if (!mCurrentSession.isOpened() && !mCurrentSession.isClosed()) {
				mCurrentSession.openForRead(new Session.OpenRequest(this)
						.setCallback(statuscallback));
			}
		} else if (!mCurrentSession.isOpened()) {
			openActiveSession(true, statuscallback, PERMISSIONS);
		} else {
			// publishStory(mCurrentSession,"","");
			
			publishStory();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mCurrentSession != null)
			Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mCurrentSession != null)
			Session.getActiveSession().removeCallback(statusCallback);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mCurrentSession != null) {
			Session session = Session.getActiveSession();
			Session.saveSession(session, outState);
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

		}
	}

	Session.StatusCallback statuscallback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.equals(SessionState.OPENED)) {
				Session.setActiveSession(session);
				// publishStory(session,"","");
				publishStory();
			}

		}
	};

	Session openActiveSession(boolean allowLoginUI, StatusCallback callback,
			List<String> permissions) {
		OpenRequest openRequest = new OpenRequest(ProductDetails.this)
				.setPermissions(permissions).setCallback(callback);
		Session session = new Session.Builder(ProductDetails.this).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())
				|| allowLoginUI) {
			Session.setActiveSession(session);
			session.openForPublish(openRequest);
			return session;
		}
		return null;
	}

	public void publishStory() {
		Session session = Session.getActiveSession();

		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				
				
				session.addCallback(new StatusCallback() {
					
					@Override
					public void call(Session session, SessionState state, Exception exception) {
 
						if(session!=null && session.getPermissions().contains("publish_actions")){
							publishStory();
						}else{
							if(progressDialog.isShowing()){
								progressDialog.dismiss();
							}
							Toast.makeText(ProductDetails.this, "Please Sing in facebook with sandbox cradentials ", Toast.LENGTH_LONG).show();
							
						}
					}
				});
				return;
			}
			
			Bundle postParams = new Bundle();
			postParams.putString("name", "TapnSell");
			postParams.putString("caption", "Ratting Item");
			postParams.putString("description","I just liked this on TapNSell. Experience the World's Garage Sale #tapnsell");
			postParams.putString("picture",UserFunctions.LogoUrl);
//			postParams.putString("link", url);
			
			/*
			 * postParams.putString("name", "Facebook SDK for Android");
			 * postParams.putString("caption",
			 * "Build great social apps and get more installs.");
			 * postParams.putString("description",
			 * "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps."
			 * ); postParams.putString("link",
			 * "https://developers.facebook.com/android");
			 * postParams.putString("picture",
			 * "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png"
			 * );
			 */
			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					if (response != null) {
						GraphObject graphObject = response.getGraphObject();
						if (graphObject != null) {
							JSONObject graphResponse = response
									.getGraphObject().getInnerJSONObject();
							String postId = "";
							try {
								postId = graphResponse.getString("id");
								Log.e("postid", " "+postId);
							} catch (JSONException e) {
							}
							FacebookRequestError error = response.getError();
							if (error != null) {
								
								if (progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
								
								Toast.makeText(context,error.getErrorMessage(),Toast.LENGTH_LONG).show();
							} else {
								
								if (progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
								
								Toast.makeText(context,"Facebook posting successfully...",Toast.LENGTH_LONG).show();
							}
						} else {
							
							
							if (progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							
							Toast.makeText(context,"Facebook posting failed ...",Toast.LENGTH_LONG).show();

						}

					} else {
						if (progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}
				}
			};

			Request request = new Request(session, "me/feed", postParams,
					HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}
	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	public class FollowBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;
		ImageView ivFollowing;
		ArrayList<ItemDescModal> mProducDetail;
		public FollowBackTask(View v, ArrayList<ItemDescModal> mProducDetails) {
			ivFollowing=(ImageView) v;
			mProducDetail=mProducDetails;
		}

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ProductDetails.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				to_id = seller_userid;
				from_id = userid;
				if (seller_is_follow.equals("Y")) {
					seller_is_follow = "N";
				} else {
					seller_is_follow = "Y";
				}

				
				// Log.e(BasicNameValuePair,
				// userFunction.BasicNameValuePair)
				JSONObject json = userFunction.FollowFunctions(from_id,
						to_id, seller_is_follow);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							errorMessage = "true";

						} else {
							message=json.optString("message");
							errorMessage = "false";
						}
					} else {
						errorMessage = "network";
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {

				if (seller_is_follow.equals("Y")) {
					ivFollowing
							.setImageResource(R.drawable.product_details_following_green);
				} else {
					ivFollowing
							.setImageResource(R.drawable.product_details_following);
				}

				_mlist.setIs_follow(seller_is_follow);
				mProducDetail.set(PosForFav, _mlist);
				adapter.notifyDataSetChanged();

			} else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {

				/*
				 * AlertDialog.Builder alertDialogBuilder = new
				 * AlertDialog.Builder( ProductDetails.this);
				 * 
				 * alertDialogBuilder.setTitle("alert");
				 * 
				 * alertDialogBuilder .setMessage( "User  does not exist.")
				 * .setCancelable(false) .setPositiveButton("Ok", new
				 * DialogInterface.OnClickListener() { public void
				 * onClick(DialogInterface dialog, int id) {
				 * dialog.cancel(); } });
				 * 
				 * AlertDialog alertDialog = alertDialogBuilder.create();
				 * alertDialog.show();
				 */
				Custom_Dialog.dialogCode(2, null, "User  does not exist.",
						context);

			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	private class FavBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;
		
		ArrayList<ItemDescModal>  mProducDetail;
		public FavBackTask(View v, ArrayList<ItemDescModal> mProducDetails) {
			mProducDetail=mProducDetails;
		}

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ProductDetails.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{

				
				JSONObject json = userFunction.FavFunction(itemid, userid,
						is_like);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							no_of_likes = json.getString("no_of_likes");
							errorMessage = "true";

						} else {
							message=json.optString("message");
							errorMessage = "false";
						}
					} else {
						errorMessage = "network";
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {
//			String msg = "";
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {
				newLikes=no_of_likes;
				_mlist.setNo_of_likes(no_of_likes);
				mProducDetail.set(PosForFav, _mlist);
				adapter.notifyDataSetChanged();

				if (is_like.equals("Y")) {
				}

			} else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null,
						message, context);
				/*
				 * AlertDialog.Builder alertDialogBuilder = new
				 * AlertDialog.Builder( ProductDetails.this);
				 * 
				 * alertDialogBuilder.setTitle("alert");
				 * 
				 * alertDialogBuilder
				 * 
				 * .setMessage("User or Item does not found.")
				 * .setCancelable(false) .setPositiveButton("Ok", new
				 * DialogInterface.OnClickListener() { public void onClick(
				 * DialogInterface dialog, int id) { dialog.dismiss(); } });
				 * 
				 * AlertDialog alertDialog = alertDialogBuilder.create();
				 * alertDialog.show();
				 */

			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		userid = pref.getString("UserID", "");
		if (mCurrentSession != null)
			Session.getActiveSession().onActivityResult(this, requestCode,resultCode, data);
		if(userid.equals(seller_userid)){
			Intent  i = new Intent(ProductDetails.this, HomeActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
			finish();
		}else{
	
		 if(resultCode == RESULT_OK && requestCode==21){

				if (cd.checkConnection()) {
					new FollowBackTask(V,mPDetails).execute("");
				} else {
					Toast.makeText(ProductDetails.this,
							"internet_conn_failed", Toast.LENGTH_LONG)
							.show();
				}
		 }else if(resultCode == RESULT_OK && requestCode==22){
				Intent i =  new Intent(context,TapBoardActivity.class);
				startActivity(i);
				finish();
			 } else if(resultCode == RESULT_OK && requestCode==23){
				 Intent i = new Intent(ProductDetails.this,ChatActivity.class);
					i.putExtra("itemid", _mlist.getItemid());
					i.putExtra("from_id", _mlist.getUserid());
					i.putExtra("Distance", _mlist.getDistance());
					i.putExtra("ItemArray", mItemList);
					i.putExtra("position", "" + temp_pos);
					startActivity(i);
			 }else if(resultCode == RESULT_OK && requestCode==24){
				 String itemid = _mlist.getItemid();
					String image = _mlist.getDefault_image();
					String name = _mlist.getName();

					Intent i = new Intent(ProductDetails.this,
							MakeOfferActivity.class);
					i.putExtra("moffer_itemid", "" + itemid);
					i.putExtra("moffer_image", "" + image);
					i.putExtra("moffer_name", "" + name);
					startActivity(i);
				 
			 }else if(resultCode == RESULT_OK && requestCode==25){
				 	
				 String itemid = _mlist.getItemid();
					String image = _mlist.getDefault_image();
					String name = _mlist.getName();

					Intent i = new Intent(ProductDetails.this,
							MakeOfferActivity.class);
					i.putExtra("moffer_itemid", "" + itemid);
					i.putExtra("moffer_image", "" + image);
					i.putExtra("moffer_name", "" + name);
					startActivity(i);
				 
			 }else if(resultCode == RESULT_OK && requestCode==26){
				 	
				 Intent i = new Intent(ProductDetails.this,ChatActivity.class);
					i.putExtra("itemid", _mlist.getItemid());
					i.putExtra("from_id", _mlist.getSeller_userid());
					i.putExtra("Distance", _mlist.getDistance());
					i.putExtra("ItemArray", mItemList);
					i.putExtra("position", "" +temp_pos);
					startActivity(i);
				 
			 }else if(resultCode == RESULT_OK && requestCode==27){
				 	
					i_Buy = new Intent(ProductDetails.this,MainCheckOutFragmentActivity.class);
					i_Buy.putExtra("itemid", _mlist.getItemid());
					i_Buy.putExtra("from_id", _mlist.getUserid());
					i_Buy.putExtra("Distance", _mlist.getDistance());
					i_Buy.putExtra("ItemArray", mItem_IdList);
					i_Buy.putExtra("position",
							"" + temp_pos);
					reserve="Y";
					new LockItemBackTask().execute("");
				 
			 }else if(resultCode == RESULT_OK && requestCode==28){
				 	
					i_Buy = new Intent(ProductDetails.this,
							MainCheckOutFragmentActivity.class);
					i_Buy.putExtra("itemid", _mlist.getItemid());
					i_Buy.putExtra("from_id", _mlist.getUserid());
					i_Buy.putExtra("Distance", _mlist.getDistance());
					i_Buy.putExtra("ItemArray", mItem_IdList);
					i_Buy.putExtra("position",
							"" + temp_pos);
					reserve="Y";
					new LockItemBackTask().execute("");
				 
					
			 }else if(resultCode == RESULT_OK && requestCode==30){
					Intent i = new Intent(ProductDetails.this, ReviewUser.class);
					i.putExtra("Seller_userid", "" + _mlist.getSeller_userid());
					startActivity(i);
			 }else if(resultCode == RESULT_OK && requestCode==29){

					if (has_like.equals("Y")) {
						Drawable drawableTop = getResources().getDrawable(
								R.drawable.fav_unlike);
						TextView iv= (TextView) V;
						 iv.setCompoundDrawablesWithIntrinsicBounds(null,
								drawableTop, null, null);
						is_like = "N";
						Has_like= "N";
						_mlist.setHas_like("N");
						itemid = _mlist.getItemid().toString();
						PosForFav = mPDetails.indexOf(_mlist);
						new FavBackTask(V,mPDetails).execute("");

					} else {
						Drawable drawableTop1 = getResources().getDrawable(
								R.drawable.fav_like);
						TextView iv= (TextView) V;
						iv.setCompoundDrawablesWithIntrinsicBounds(null,
								drawableTop1, null, null);
						//
						is_like = "Y";
						Has_like= "Y";
						_mlist.setHas_like("Y");
						itemid = _mlist.getItemid().toString();
						PosForFav = mPDetails.indexOf(_mlist);
						new FavBackTask(V,mPDetails).execute("");
					}
				
				 
			 }else if(resultCode == RESULT_OK && requestCode==30){
					Intent i = new Intent(ProductDetails.this, ReviewUser.class);
					i.putExtra("Seller_userid", "" + _mlist.getSeller_userid());
					startActivity(i);
					
			 }else if(resultCode == RESULT_OK && requestCode==31){
					
					strRatting_ItemId = _mlist.getItemid();
					strRatting_toid = _mlist.getSeller_userid();
					strRatting_UserName = _mlist.getSeller_name();
					strRatting_Image = _mlist.getSeller_image();
					strRatting_Fromid = userid;
					NiceReviewdialogCode(rb_Rating2,pos,_mlist);
			 }else if(resultCode == RESULT_OK && requestCode==32){
					new PostBackTask(mtemp_Comment,myPager.getCurrentItem()).execute("");
			 }else if (resultCode == RESULT_CANCELED) {
		       //Write your code if there's no result
			  // 	Toast.makeText(context, "Please try again.", Toast.LENGTH_LONG).show();
			}
		}	 

	}
	
	private void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(context,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
}
