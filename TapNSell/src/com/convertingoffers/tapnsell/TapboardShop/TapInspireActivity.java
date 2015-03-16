package com.convertingoffers.tapnsell.TapboardShop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.MakeOffer.MakeOfferActivity;
import com.convertingoffers.tapnsell.Modal.SearchModel;
import com.convertingoffers.tapnsell.Shop.ChatActivity;
import com.convertingoffers.tapnsell.Shop.ProductDetails;
import com.convertingoffers.tapnsell.Shop.checkout.MainCheckOutFragmentActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.CustomTextView;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class TapInspireActivity extends BaseActivity implements OnClickListener {
	String address="N";
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	Custom_Dialog custom_dialog;
	Context context ;
	String  type="",is_like="";
	boolean statusGPS;
	ListView lvt_Product;
	RelativeLayout rlChat;
	TextView tvPrice, tvDistance, tvPopular;
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	Intent i_Buy;
	int PosForFav,intStartNewListing=0,count=0,delete_pos;
	SearchModel myCateForfav, myCateForDelete;
	boolean BoolFavClick = true;
	
	String itemid = "", struserid = "", name = "", price = "", price_type = "",
			distance = "", image = "", has_like = "", no_of_likes = "", msg,reserved="",
			 categoryid = "", DeleteItemID = "",is_last="",reserve="Y",Listing_type="";
	ArrayList<SearchModel> mNewListing = new ArrayList<SearchModel>();
	NewListingCustomAdaper adapter;
	protected LocationManager locationManager;
	String latitude = "", longitude = "", userid = "";
	Location location;
	LinearLayout llShort;
	boolean isCurrentLocation = false, Successfull, isGPSEnabled = false,
	isNetworkEnabled = false, canGetLocation = false;
	double lat = 0.0, log = 0.0, Distance = 0.0, lat1, lat2, lng1, lng2,
			Dis = 0.0;
	ImageView ivPrice,ivDistance,ivPopular;
	

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.itemlisting);
	
		userid = pref.getString("UserID", "");
		latitude = pref.getString("lat", "0");
		longitude = pref.getString("long", "0");
		
		ivMenu.setVisibility(View.VISIBLE);
//		getLocation();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			Listing_type = bundle.getString("type");
		}
		Log.e("Listing_type", " "+Listing_type);
		if(Listing_type.equals("tap")){
			tvHeader.setText("Tap Inspired");	
		}else{
			tvHeader.setText("Watch List");	
			llShort.setVisibility(View.GONE);
		}
		
		Log.e("Listing_type", "z "+Listing_type);
//		if (latitude.length() == 0) {latitude = "0";} 
//		if (longitude.length() == 0) {longitude = "0";}
//		
		type="P";
		
		if (Build.VERSION.SDK_INT >= 16)
			llShort.setBackground(TapInspireActivity.this.getResources()
					.getDrawable(R.drawable.list_item_short_bar_price));
		else
			llShort.setBackgroundDrawable(TapInspireActivity.this
					.getResources().getDrawable(
							R.drawable.list_item_short_bar_price));
		
		
	mNewListing.clear();
		if (cd.checkConnection()) {
				new NewListingBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
		}
		
		ivPrice.setOnClickListener(this);
		ivDistance.setOnClickListener(this);
		ivPopular.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
	}
///
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	
	if (resultCode == RESULT_OK) {
		if(data!=null){
			 String newLikes ="",Has_like="";
			   int pos_cat =0;
			   
				
			   Bundle buddle = data.getExtras();
			   newLikes = buddle.getString("newLikes");
			   Has_like = buddle.getString("Has_like");
			   pos_cat=buddle.getInt("postion");
			   Log.e("pos_cat", " "+pos_cat);
				Log.e("newLikes", " "+newLikes);
				Log.e("Has_like", " "+Has_like);
				if (Listing_type.equals("tap")) {

					myCateForfav.setNo_of_likes(newLikes);
					myCateForfav.setHas_like(Has_like);
					mNewListing.set(pos_cat, myCateForfav);
					adapter.notifyDataSetChanged();

				} else {

					mNewListing.remove(pos_cat);
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	private class NewListingBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			if(!progressDialog.isShowing()){
				progressDialog.show();	
			}
			
			
		}

		@Override
		protected String doInBackground(String... params) {
			{
				JSONObject json;
			
				Log.e("userid"," "+userid);
				Log.e("latitude"," "+latitude);
				Log.e("longitude"," "+longitude);
				Log.e("start"," "+intStartNewListing);
				Log.e("limit","50");
				Log.e("type"," "+type);
				
				if(Listing_type.equals("tap")){
					
					json = userFunction.TapInspireFunction
					(userid, latitude,longitude,""+intStartNewListing,type);
				}else{
					Log.e("watch", "watch");
					json = userFunction.WatchListFunction
					(userid, latitude,longitude,""+intStartNewListing);
//					items/watchlistitems
				}
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						is_last =json.optString("is_last"); // json.get("is_last");
						
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONArray Item = json.getJSONArray("Item");

							for (int i = 0; i < Item.length(); i++) {

								JSONObject c = Item.getJSONObject(i);
								itemid = c.getString("itemid");
								struserid = c.getString("userid");
								name = c.getString("name");
								price = c.getString("price");
								distance = c.getString("distance");
								image = UserFunctions.localImageUrl;
								image = image + c.optString("image");
								reserved= c.getString("reserved");
								has_like = c.getString("has_like");
								no_of_likes = c.getString("no_of_likes");

								mNewListing.add(new SearchModel(itemid,
										struserid, name, price, distance,
										image, has_like, no_of_likes,
										price_type,reserved));
							}

							errorMessage = "true";

						} else {
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

				adapter = new NewListingCustomAdaper(context,
						R.layout.itemlisting_child, mNewListing);
				lvt_Product.setAdapter(adapter);
				lvt_Product.setSelectionFromTop(count, 0);
				
				
				lvt_Product.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) { 
						int threshold = 1;
						count = 0;
						count = lvt_Product.getCount();
						
						if(is_last.equals("N")){								
							if (scrollState == SCROLL_STATE_IDLE) {
								if (lvt_Product.getLastVisiblePosition() >= count	- threshold) {
									intStartNewListing=intStartNewListing+50;
									// Execute LoadMoreDataTask AsyncTask
									
									new NewListingBackTask().execute("");								
								}
							}
						}							
					}

					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						
					}

				});		
				
			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		} else {
				if(count==0){
					Custom_Dialog.dialogCode(1, null, "No items found", context);
				}else{
					Custom_Dialog.dialogCode(2, null, "No items found", context);	
				}
				
//				ValidationSingleButton("no records found....");
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	private class NewListingShortBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
			mNewListing.clear();
		}

		@Override
		protected String doInBackground(String... params) {
			{

				JSONObject json;
				
				Log.e("userid", " "+userid);
				Log.e("latitude", " "+latitude);
				Log.e("longitude", " "+longitude);
				Log.e("intStartNewListing", " "+intStartNewListing);
				Log.e("type", " "+type);
				Log.e("categoryid", " "+categoryid);
				/*	json = userFunction.NearbyitemsFunction(userid, latitude,longitude,""+intStartNewListing
							,type,categoryid);
				*/
				
				json = userFunction.TapInspireFunction
				(userid, latitude,longitude,""+intStartNewListing,type);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						is_last = json.getString("is_last");
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONArray Item = json.getJSONArray("Item");

							for (int i = 0; i < Item.length(); i++) {

								JSONObject c = Item.getJSONObject(i);
								itemid = c.getString("itemid");
								struserid = c.getString("userid");
								name = c.getString("name");
								price = c.getString("price");
								distance = c.getString("distance");
								image = UserFunctions.localImageUrl;
								image = image + c.getString("image");
								reserved = c.getString("reserved");
								has_like = c.getString("has_like");
								no_of_likes = c.getString("no_of_likes");

								mNewListing.add(new SearchModel(itemid,
										struserid, name, price, distance,
										image, has_like, no_of_likes,
										price_type,reserved));
							}

							errorMessage = "true";

						} else {
							message=json.optString("message");
							errorMessage = "false";
						}
					} else {
						message=json.optString("message");
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

				
				tvHeader.setText("Tap Inspired");
				adapter = new NewListingCustomAdaper(context,
						R.layout.itemlisting_child, mNewListing);
				lvt_Product.setAdapter(adapter);
				lvt_Product.setSelectionFromTop(count, 0);
				lvt_Product.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) { 
						int threshold = 1;
						count = 0;
						count = lvt_Product.getCount();
						if(is_last.equals("N")){								
							if (scrollState == SCROLL_STATE_IDLE) {
								if(!progressDialog.isShowing()){
								if (lvt_Product.getLastVisiblePosition() >= count	- threshold) {
									// Execute LoadMoreDataTask AsyncTask
									intStartNewListing=intStartNewListing+50;
									new NewListingShortBackTask().execute("");								
								}
								}
							}
						}							
					}

					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						
					}

				});		
				
			} else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else {
				
				Custom_Dialog.dialogCode(2, null, message, context);
//				ValidationSingleButton("no records found....");
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	

	public void getLocation() {
		try {
			locationManager = (LocationManager) TapInspireActivity.this
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
						latitude = "" + lat;
						longitude = "" + log;
					}
				}
				if (isGPSEnabled) {

					location = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location != null) {
						lat = location.getLatitude();
						log = location.getLongitude();
						latitude = "" + lat;
						longitude = "" + log;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		
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
		 
		custom_dialog =new Custom_Dialog();
		llShort= (LinearLayout) findViewById(R.id.llShort);
		lvt_Product = (ListView) findViewById(R.id.lvt_Product);
		ivPrice= (ImageView) findViewById(R.id.ivPrice);
		ivDistance= (ImageView) findViewById(R.id.ivDistance);
		ivPopular= (ImageView) findViewById(R.id.ivPopular);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ivPrice:
			count=0;
			intStartNewListing=0;
			is_last="";
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(TapInspireActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_price));
			else
				llShort.setBackgroundDrawable(TapInspireActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_price));
	
			
			lvt_Product.smoothScrollBy(0, 0);
			type="P";
			if (cd.checkConnection()) {
				new NewListingShortBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG)
						.show();
			}

			break;

		case R.id.ivPopular:
			count=0;
			intStartNewListing=0;
			is_last="";
			
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(TapInspireActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_popular));
			else
				llShort.setBackgroundDrawable(TapInspireActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_popular));
	
//			mNewListing.clear();
			lvt_Product.smoothScrollBy(0, 0);
			type="PO";
			if (cd.checkConnection()) {
				new NewListingShortBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG)
						.show();
			}

			break;

		case R.id.ivDistance:
			count=0;
			intStartNewListing=0;
			is_last="";
			
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(TapInspireActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_distance));
			else
				llShort.setBackgroundDrawable(TapInspireActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_distance));
			lvt_Product.smoothScrollBy(0, 0);
//			mNewListing.clear();
			type="D";
			if (cd.checkConnection()) {
				new NewListingShortBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
			}
		
			break;

		case R.id.ivBack:
			finish();
			break;
			
		case R.id.ivMenu:
			finish();
			break;
			
		default:
			break;
		}
	}

	private class NewListingCustomAdaper extends ArrayAdapter<SearchModel> {
		ViewHolder holder;
		private ArrayList<SearchModel> listSubCategories;
//		protected Object mysun;

		public NewListingCustomAdaper(Context context, int textViewResourceId,
				ArrayList<SearchModel> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<SearchModel>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivDefault, ivMakeOffer, ivChat,ivDelete;
			TextView tvDistance, tvPrice, tvPriceType;
			RelativeLayout  rlParent, rlBuy;
			Button btnFav;
			CustomTextView tvName;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.tapinspire_child, null);

				holder = new ViewHolder();
				holder.ivDefault = (ImageView) convertView.findViewById(R.id.ivDefault);
				holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
				holder.btnFav = (Button) convertView.findViewById(R.id.btnFav);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
				holder.tvPriceType = (TextView) convertView.findViewById(R.id.tvPriceType);
				holder.tvName = (CustomTextView) convertView.findViewById(R.id.tvName);
				holder.ivMakeOffer = (ImageView) convertView.findViewById(R.id.ivMakeOffer);
				holder.rlParent = (RelativeLayout) convertView.findViewById(R.id.rlParent);
				holder.rlBuy = (RelativeLayout) convertView.findViewById(R.id.rlBuy);
				holder.ivChat = (ImageView) convertView.findViewById(R.id.ivChat);
				holder.ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
				
				
				
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			SearchModel myCate = listSubCategories.get(position);
			holder.btnFav.setTag(myCate);
			itemid = myCate.getItemid();
			struserid = myCate.getUserid();
			name = myCate.getName();
			price = myCate.getPrice();
			price_type = myCate.getPrice_type();
			distance = myCate.getDistance();
			image = myCate.getImage();
			has_like = myCate.getHas_like();
			no_of_likes = myCate.getNo_of_likes();
			holder.tvDistance.setText("" + distance);
			holder.btnFav.setText("" + no_of_likes);
			
			if(has_like.equals("Y")){
				Drawable drawableTop = getResources().getDrawable(R.drawable.fav_like);
				holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop , null, null);
			}else{	
				Drawable drawableTop = getResources().getDrawable(R.drawable.fav_unlike);
				holder.btnFav.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop , null, null);
				
			}
			holder.tvPrice.setText("" + price);
			if (price_type.equals("U")) {
				holder.tvPriceType.setText("€");
			} else {
				holder.tvPriceType.setText("$");
			}
			holder.tvName.setText("" + name);

			
			
			
			
			// Image display using lazy loading 

			iLoader_item.displayImage(image, holder.ivDefault, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
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
			
			
			

			holder.rlBuy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
		
					myCateForfav = listSubCategories.get(position);
					reserve =myCateForfav.getReserved();
					if(reserve.length()!=0 && reserve.equals("Y")){
						Toast.makeText(TapInspireActivity.this, "Item already reserved", Toast.LENGTH_LONG).show();
					}else{
						
						mItem_IdList.clear();
						for (int i = 0; i < listSubCategories.size(); i++) {
							SearchModel	_list = listSubCategories.get(i);
							mItem_IdList.add(_list.getItemid());
						}
					itemid=myCateForfav.getItemid();
					editor.putString("CHKImage", ""+myCateForfav.getImage());
					editor.putString("CHKItemid", ""+myCateForfav.getItemid());
					editor.putString("CHKName", ""+myCateForfav.getName());
					editor.putString("CHKPrice", ""+myCateForfav.getPrice());
					editor.commit();
					
					myCateForfav = listSubCategories.get(position);
					i_Buy = new Intent(TapInspireActivity.this,MainCheckOutFragmentActivity.class);
					i_Buy.putExtra("itemid", myCateForfav.getItemid());
					i_Buy.putExtra("from_id", myCateForfav.getUserid());
					i_Buy.putExtra("Distance", myCateForfav.getDistance());
					i_Buy.putExtra("ItemArray", mItem_IdList);
					i_Buy.putExtra("position", ""+position);
					
					new LockItemBackTask().execute("");
					
					}
				}
			});
			
			holder.ivDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					myCateForfav = listSubCategories.get(position);
					
					itemid=myCateForfav.getItemid();
					delete_pos=position;
					
					if (cd.checkConnection()) {
						new DeleteBackTask().execute("");
					} else {
						Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
					}
				}
			});
			
			holder.ivChat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						SearchModel	_list = listSubCategories.get(i);
						mItem_IdList.add(_list.getItemid());
					}
					myCateForfav = listSubCategories.get(position);
					Intent i = new Intent(TapInspireActivity.this,ChatActivity.class);
					i.putExtra("itemid", myCateForfav.getItemid());
					i.putExtra("from_id", myCateForfav.getUserid());
					i.putExtra("Distance", myCateForfav.getDistance());
					i.putExtra("ItemArray", mItem_IdList);
					i.putExtra("position", ""+position);
					startActivity(i);
				}
			});

			holder.rlParent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						SearchModel	_list = listSubCategories.get(i);
						
						mItem_IdList.add(_list.getItemid());
					
					}
					Log.e("position", " "+position);
					myCateForfav = listSubCategories.get(position);
					Intent iParent = new Intent(TapInspireActivity.this,ProductDetails.class);
					iParent.putExtra("ItemArray", mItem_IdList);
					iParent.putExtra("position", ""+position);
					startActivityForResult(iParent, 6);

				}
			});

			holder.btnFav.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myCateForfav = listSubCategories.get(position);
					has_like = myCateForfav.getHas_like();

					if (has_like.equals("Y")) {
													
							is_like="N";
							myCateForfav.setHas_like("N");
							itemid = myCateForfav.getItemid().toString();
							PosForFav = position;
							
							
							new FavBackTask().execute("");

					} else {
						is_like="Y";
						myCateForfav.setHas_like("Y");
						itemid = myCateForfav.getItemid().toString();
						PosForFav = position;
						new FavBackTask().execute("");
					}
				}
			});

			holder.ivMakeOffer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myCateForfav = listSubCategories.get(position);
					String itemid = myCateForfav.getItemid();
					String image = myCateForfav.getImage();
					String name = myCateForfav.getName();

					Intent i = new Intent(TapInspireActivity.this, MakeOfferActivity.class);
					i.putExtra("moffer_itemid", ""+itemid);
					i.putExtra("moffer_image", ""+image);
					i.putExtra("moffer_name", ""+name);
					startActivity(i);

				}
			});
			return convertView;

		}
		private class DeleteBackTask extends AsyncTask<String, Void, String> {
			String errorMessage;

			protected void onPreExecute() {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Please wait");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			protected String doInBackground(String... params) {
				{
					JSONObject json;
					Log.e("userid", " "+userid);
					Log.e("itemid", " "+itemid);
					
					
					if(Listing_type.equals("tap")){
						 json = userFunction.DeleteItemFunction(userid,itemid);
					}else{
						 json = userFunction.WatchListDeleteItemFunction(userid,itemid,"N");
					}
				
					try {
						if (json!=null&&json.getString(KEY_SUCCESS) != null) {
							String res = json.getString(KEY_SUCCESS);
							if (res.equals(KEY_SUCCESS_STATUS)) {
								errorMessage = "true";

							} else {
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
					
					mNewListing.remove(delete_pos);
					adapter.notifyDataSetChanged();
					Custom_Dialog.dialogCode(2, null, "Item removed successfully.", context);
//					ValidationSingleButton("Item Deleted sucessfully");
				} else if (result.equals("network")) {
					String	message="Your internet connection is too slow";
					Custom_Dialog.dialogCode(2, null,message, context);
			}else {
					Custom_Dialog.dialogCode(2, null, "No items found.", context);
//					ValidationSingleButton("No records found....");
				}
			}

			@Override
			protected void onProgressUpdate(Void... values) {

			}
		}

		private class FavBackTask extends AsyncTask<String, Void, String> {
			String errorMessage;

			protected void onPreExecute() {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Please wait");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			protected String doInBackground(String... params) {
				{
					JSONObject json = userFunction.FavFunction(itemid, userid,is_like);
					try {
						if (json!=null&&json.getString(KEY_SUCCESS) != null) {
							String res = json.getString(KEY_SUCCESS);
							if (res.equals(KEY_SUCCESS_STATUS)) {

								no_of_likes = json.getString("no_of_likes");
								errorMessage = "true";

							} else {
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
//				String msg="";
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				if (result.equals("true")) {

					if(Listing_type.equals("tap")){
						myCateForfav.setNo_of_likes(no_of_likes);
						mNewListing.set(PosForFav, myCateForfav);
						adapter.notifyDataSetChanged();
					}else{
						mNewListing.remove(PosForFav);
						adapter.notifyDataSetChanged();
						
						if(mNewListing.size()==0){
							Custom_Dialog.dialogCode(1, null, "No items found", context);
						}
					}
					

				} else if (result.equals("network")) {
					String	message="Your internet connection is too slow";
					Custom_Dialog.dialogCode(2, null,message, context);
			}else {
					Custom_Dialog.dialogCode(2, null, "User or Item  does not found....", context);
//					ValidationSingleButton("User or Item  does not found....");
				}
			}

			@Override
			protected void onProgressUpdate(Void... values) {

			}
		}
	
	
		private class LockItemBackTask extends AsyncTask<String, Void, String> {
			String errorMessage,message;

			protected void onPreExecute() {
				progressDialog = new ProgressDialog(context);
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
					Log.e("reserve", " "+reserve);
					
					JSONObject json = userFunction.ReserveFunction(userid,itemid, reserve);
					try {
						if (json!=null&&json.getString(KEY_SUCCESS) != null) {
							String res = json.getString(KEY_SUCCESS);
							if (res.equals(KEY_SUCCESS_STATUS)) {

								address=json.optString("address");
								errorMessage = "true";

							} else {
								message=json.optString("message");
								errorMessage = "false";
							}
						} else {
							message=json.optString("message");
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
			}else {
					Custom_Dialog.dialogCode(2, null,message, context);
				}
			}

			@Override
			protected void onProgressUpdate(Void... values) {

			}
		}
	}
}
