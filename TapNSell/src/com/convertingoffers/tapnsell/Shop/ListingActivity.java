package com.convertingoffers.tapnsell.Shop;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import com.convertingoffers.tapnsell.HomeActivity;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.MakeOffer.MakeOfferActivity;
import com.convertingoffers.tapnsell.Modal.SearchModel;
import com.convertingoffers.tapnsell.Shop.checkout.MainCheckOutFragmentActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.CustomTextView;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.Login_Dialog_Activity;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ListingActivity extends BaseActivity implements OnClickListener {
	

	String address="N";
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	Animation RightSwipe;
	Custom_Dialog custom_dialog;
	Context context ;
	String  type="",is_like="";
	boolean statusGPS;
	ListView lvt_Product;
	TextView tvPrice, tvDistance, tvPopular;
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	Intent i_Buy;
	int PosForFav,intStartNewListing=0,count=0,temp_pos=0;
	SearchModel myCateForfav, myCateForDelete;
	boolean BoolFavClick = true;
	
	String itemid = "", struserid = "", name = "", price = "", price_type = "",
			distance = "", image = "", has_like = "", no_of_likes = "", msg,reserved="",
			ProductType = "", categoryid = "", DeleteItemID = "",is_last="",reserve="Y";
	ArrayList<SearchModel> mNewListing = new ArrayList<SearchModel>();
	
	NewListingCustomAdaper adapter;
	protected LocationManager locationManager;
	String latitude = "0", longitude = "0", userid = "";
	Location location;
	LinearLayout llShort;
	boolean isCurrentLocation = false, Successfull, isGPSEnabled = false,
	isNetworkEnabled = false, canGetLocation = false;
	double lat = 0.0, log = 0.0, Distance = 0.0, lat1, lat2, lng1, lng2,
			Dis = 0.0;
	ImageView ivPrice,ivDistance,ivPopular;
	

	
	@SuppressLint("NewApi")@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemlisting);
		
		userid = pref.getString("UserID", "0");
		latitude = pref.getString("lat", "0");
		longitude = pref.getString("long", "0");
		//getLocation();
				lvt_Product.setPadding(5, 5, 5, 0);
		ivMenu.setVisibility(View.VISIBLE);
	
		
		Log.e("latitude", " "+latitude);
		Log.e("longitude", " "+longitude);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			ProductType = bundle.getString("ListingType");
			categoryid = bundle.getString("categoryid");
		}
		if (ProductType.equals("Nearby")) {
			 type="D";
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_distance));
			else
				llShort.setBackgroundDrawable(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_distance));
			
		}else if (ProductType.equals("Listings")) {
			type="";
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_normal));
			else
				llShort.setBackgroundDrawable(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_normal));
		} else if (ProductType.equals("Popular")) {

			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_popular));
			else
				llShort.setBackgroundDrawable(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_popular));

		} else if (ProductType.equals("Category")) {
			
		} else {
			type="P";
			
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(ListingActivity.this.getResources()
						.getDrawable(R.drawable.list_item_short_bar_price));
			else
				llShort.setBackgroundDrawable(ListingActivity.this
						.getResources().getDrawable(
								R.drawable.list_item_short_bar_price));

		}

		mNewListing.clear();

		if (ProductType.equals("Search")) {

			tvHeader.setText("Browse Listing");
			mNewListing = (ArrayList<SearchModel>) bundle
					.getSerializable("SearchData");

			if (mNewListing.size() == 0) {
//				ValidationSingleButton("No record found...");
				Custom_Dialog.dialogCode(2, null, "No items found", context);
			} else {

				adapter = new NewListingCustomAdaper(context,
						R.layout.itemlisting_child, mNewListing);
				lvt_Product.setAdapter(adapter);
			}
		} else {
			if (cd.checkConnection()) {
				new NewListingBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
			}
		}
		ivPrice.setOnClickListener(this);
		ivDistance.setOnClickListener(this);
		ivPopular.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		userid = pref.getString("UserID", "0");
	}
	private class NewListingBackTask extends AsyncTask<String, Void, String> {
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
				Log.e("latitude final check", " "+latitude);
				Log.e("longitude final chcek", " "+longitude);
				Log.e("userid final chcek", " "+userid);
				
				
				JSONObject json;
				   if (ProductType.equals("Recently")) {
					json = userFunction.RecentlyViewFunction(userid, latitude,
							longitude);
				}  else {

					Log.e("latitude final userid", " "+userid);
					Log.e("longitude final latitude", " "+latitude);
					Log.e("userid final longitude", " "+longitude);
					Log.e("latitude final intStartNewListing", " "+intStartNewListing);
					Log.e("longitude final type", " "+type);
					Log.e("userid final categoryid", " "+categoryid);


					json = userFunction.NearbyitemsFunction(userid, latitude,longitude,""+intStartNewListing,type,categoryid);
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
								image = image + c.getString("image");
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
			if(ProductType.equals("Short")){
			}
			if (result.equals("true")) {

				mSetHeaderText();
			

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
						intStartNewListing=intStartNewListing+50;
						if(is_last.equals("N")){								
							if (scrollState == SCROLL_STATE_IDLE) {
								if (lvt_Product.getLastVisiblePosition() >= count	- threshold) {
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
//				ValidationSingleButton("Error in posting");
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
					json = userFunction.NearbyitemsFunction(userid, latitude,longitude,""+intStartNewListing
							,type,categoryid);
				
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
			mSetHeaderText();
			if(ProductType.equals("Short")){
			}
			if (result.equals("true")) {

				
				mSetHeaderText();
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
						intStartNewListing=intStartNewListing+50;
						
						if(is_last.equals("N")){								
							if (scrollState == SCROLL_STATE_IDLE) {
								if (lvt_Product.getLastVisiblePosition() >= count	- threshold) {
									// Execute LoadMoreDataTask AsyncTask
									new NewListingShortBackTask().execute("");								
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
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null, message, context);
//				ValidationSingleButton("no records found....");
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	
	private void mSetHeaderText() {
		
		if (ProductType.equals("Listings")) {
			type="";
			tvHeader.setText("New Listings");
		} else if (ProductType.equals("Nearby")) {
			 type="D";
			tvHeader.setText("NearBy Listings");
		} else if (ProductType.equals("Recently")) {
			tvHeader.setText("Recently viewed");
		}  else if (ProductType.equals("Category")) {
			tvHeader.setText("Browse Listings");
		} else if (ProductType.equals("TapInspire")) {
			tvHeader.setText("Tap Inspire");
		} else {
			tvHeader.setText("Browse Listings");
		}
	}
	public void getLocation() {
		try {
			locationManager = (LocationManager) ListingActivity.this
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
						editor.putString("lat", ""+latitude);
						editor.putString("log", ""+longitude);
						editor.commit();
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
						editor.putString("lat", ""+latitude);
						editor.putString("log", ""+longitude);
						editor.commit();
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
		custom_dialog =new Custom_Dialog();
	
		llShort= (LinearLayout) findViewById(R.id.llShort);
		lvt_Product = (ListView) findViewById(R.id.lvt_Product);
		ivPrice= (ImageView) findViewById(R.id.ivPrice);
		ivDistance= (ImageView) findViewById(R.id.ivDistance);
		ivPopular= (ImageView) findViewById(R.id.ivPopular);
		
		iLoader_item.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.list_item_image_frame)
		.showImageForEmptyUri(R.drawable.list_item_image_frame)
		.showImageOnFail(R.drawable.list_item_image_frame)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		String temp_ProductType="";
		switch (v.getId()) {
		case R.id.ivPrice:
			count=0;
			intStartNewListing=0;
			is_last="";
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_price));
			else
				llShort.setBackgroundDrawable(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_price));
	
			
			
			 temp_ProductType=ProductType;
				lvt_Product.smoothScrollBy(0, 0);
			ProductType="Short";
			type="P";
			if (cd.checkConnection()) {
				new NewListingShortBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG)
						.show();
			}
			ProductType=temp_ProductType;
			
			
			break;

		case R.id.ivPopular:
			count=0;
			intStartNewListing=0;
			is_last="";
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_popular));
			else
				llShort.setBackgroundDrawable(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_popular));
	
		
			 temp_ProductType=ProductType;
				lvt_Product.smoothScrollBy(0, 0);
			ProductType="Short";
			type="PO";
			if (cd.checkConnection()) {
				new NewListingShortBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG)
						.show();
			}
			ProductType=temp_ProductType;
		
			break;

		case R.id.ivDistance:
			count=0;
			intStartNewListing=0;
			is_last="";
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_distance));
			else
				llShort.setBackgroundDrawable(ListingActivity.this.getResources().getDrawable(R.drawable.list_item_short_bar_distance));
			lvt_Product.smoothScrollBy(0, 0);
//			mNewListing.clear();
			type="D";
			 temp_ProductType=ProductType;
			
			ProductType="Short";
			
			if (cd.checkConnection()) {
				new NewListingShortBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG)
						.show();
			}
			ProductType=temp_ProductType;
		
			break;

		case R.id.ivBack:
			finish();
			break;
		case R.id.ivMenu:
			Intent iMenuBtn;
			buttonAnimation(ivMenu);
			if (userid.length() == 0 || userid.equals("0")) {
				
				 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
				 iMenuBtn.putExtra("title", " Tapboard");
				 startActivityForResult(iMenuBtn, 15);
			
			}else {
				Intent i=new Intent(ListingActivity.this, TapBoardActivity.class);
				startActivity(i);
				finish();
			}
			
			break;
			
		default:
			break;
		}
	}

	public class NewListingCustomAdaper extends ArrayAdapter<SearchModel> {
		ViewHolder holder;
		private ArrayList<SearchModel> listSubCategories;
		protected Object mysun;

		public NewListingCustomAdaper(Context context, int textViewResourceId,
				ArrayList<SearchModel> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<SearchModel>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivDefault, ivMakeOffer, ivChat;
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
				convertView = vi.inflate(R.layout.itemlisting_child, null);

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
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (ProductType.equals("TapInspire")) {
				
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
			holder.tvDistance.setText(""+distance);
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
		/*	if (image != null) {
				try {
					imageLoader.DisplayImage(image, holder.ivDefault);
				} catch (Exception e) {
				}
			} else {
				holder.ivDefault.setImageResource(R.drawable.ic_launcher);
			}*/

			
			// Image display using lazy loading 
		
			
			iLoader_item.displayImage(image, holder.ivDefault, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					//holder.pbimage.setProgress(0);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					//scaleImage(holder.ivDefault);
				}
			}, new ImageLoadingProgressListener() {
				@Override
				public void onProgressUpdate(String imageUri, View view, int current, int total) {
					//holder.pbimage.setProgress(Math.round(100.0f * current / total));
//					holder.pbimage.setIndeterminate(true);
					
					
				}
			});
			
			// Image display using lazy loading
			
			
			holder.rlBuy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					temp_pos=position;
					myCateForfav = listSubCategories.get(position);
					reserve =myCateForfav.getReserved();
					if(reserve.length()!=0 && reserve.equals("Y")){
						Toast.makeText(ListingActivity.this, "Item already reserved", Toast.LENGTH_LONG).show();
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
					Intent iMenuBtn;
					buttonAnimation(v);
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " Buy");
						 startActivityForResult(iMenuBtn, 14);
					
					}else {
						i_Buy = new Intent(ListingActivity.this,MainCheckOutFragmentActivity.class);
						i_Buy.putExtra("itemid", myCateForfav.getItemid());
						i_Buy.putExtra("from_id", myCateForfav.getUserid());
						i_Buy.putExtra("Distance", myCateForfav.getDistance());
						i_Buy.putExtra("ItemArray", mItem_IdList);
						i_Buy.putExtra("position", ""+position);
						reserve="Y";
						new LockItemBackTask().execute("");
					}
					
					
					}
				}
			});
			
			holder.ivChat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					mItem_IdList.clear();
					temp_pos=position;
					for (int i = 0; i < listSubCategories.size(); i++) {
						SearchModel	_list = listSubCategories.get(i);
						
						mItem_IdList.add(_list.getItemid());
					}
					myCateForfav = listSubCategories.get(position);
					Intent iMenuBtn;
					buttonAnimation(v);
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " Chat");
						 startActivityForResult(iMenuBtn, 13);
					
					}else {
						Intent i = new Intent(ListingActivity.this,ChatActivity.class);
						i.putExtra("itemid", myCateForfav.getItemid());
						i.putExtra("from_id", myCateForfav.getUserid());
						i.putExtra("Distance", myCateForfav.getDistance());
						i.putExtra("ItemArray", mItem_IdList);
						i.putExtra("position", ""+position);
						startActivity(i);
					}
					
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
						myCateForfav = listSubCategories.get(position);
						buttonAnimation(v);
						Intent iParent = new Intent(ListingActivity.this,ProductDetails.class);
						iParent.putExtra("ItemArray", mItem_IdList);
						iParent.putExtra("position", ""+position);
						startActivity(iParent);
				}
			});
		
			holder.btnFav.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Intent iMenuBtn;
					buttonAnimation(v);
					myCateForfav = listSubCategories.get(position);
					PosForFav = position;
					has_like = myCateForfav.getHas_like();
					Log.e("has_like", " "+has_like);
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " favourite");
						 startActivityForResult(iMenuBtn, 11);
					
					}else {

						
						if (has_like.equals("Y")) {
							
							is_like="N";
							myCateForfav.setHas_like("N");
							itemid = myCateForfav.getItemid().toString();
							new FavBackTask().execute("");

						} else {
							is_like = "Y";
							myCateForfav.setHas_like("Y");
							itemid = myCateForfav.getItemid().toString();
							new FavBackTask().execute("");
						}
					}
				}

			
			});

			holder.ivMakeOffer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent iMenuBtn;
					buttonAnimation(v);
					myCateForfav = listSubCategories.get(position);
					if (userid.length() == 0 || userid.equals("0")) {
						
						 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
						 iMenuBtn.putExtra("title", " MakeOffer");
						 startActivityForResult(iMenuBtn, 12);
					
					}else {
						
						String itemid = myCateForfav.getItemid();
						String image = myCateForfav.getImage();
						String name = myCateForfav.getName();
						Intent i = new Intent(ListingActivity.this, MakeOfferActivity.class);
						i.putExtra("moffer_itemid", ""+itemid);
						i.putExtra("moffer_image", ""+image);
						i.putExtra("moffer_name", ""+name);
						startActivity(i);

					}
					
				
				}
			});

			return convertView;

		}

	}
	
	public class FavBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json = userFunction.FavFunction(itemid, userid,is_like);
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
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {

				myCateForfav.setNo_of_likes(no_of_likes);
				mNewListing.set(PosForFav, myCateForfav);
				adapter.notifyDataSetChanged();

			} else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null, message, context);
//				ValidationSingleButton("User or Item  does not found....");
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	public class LockItemBackTask extends AsyncTask<String, Void, String> {
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
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null, message, context);
//				ValidationSingleButton("Error in posting");
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	private void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(context,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
	@SuppressLint("InlinedApi") @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		userid = pref.getString("UserID", "");
		String to_id="";
		to_id=myCateForfav.getUserid();
		if(userid.equals(to_id)){
			Intent  i = new Intent(ListingActivity.this, HomeActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
			finish();
		}else{
			
	 if(resultCode == RESULT_OK && requestCode==11){
		 Log.e("has_like"," "+ has_like);
		 if (has_like.equals("Y")) {
				is_like="N";
				myCateForfav.setHas_like("N");
				itemid = myCateForfav.getItemid().toString();
				new FavBackTask().execute("");

			} else {
				is_like = "Y";
				myCateForfav.setHas_like("Y");
				itemid = myCateForfav.getItemid().toString();
				new FavBackTask().execute("");
			}
	        }else  if(resultCode == RESULT_OK && requestCode==12){
	   		
	        	String itemid = myCateForfav.getItemid();
	        	String image = myCateForfav.getImage();
	        	String name = myCateForfav.getName();

	        	Intent i = new Intent(ListingActivity.this, MakeOfferActivity.class);
	        	i.putExtra("moffer_itemid", ""+itemid);
	        	i.putExtra("moffer_image", ""+image);
	        	i.putExtra("moffer_name", ""+name);
	        	startActivity(i);
	        	
		        }else if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        	Toast.makeText(context, "Please try again.", Toast.LENGTH_LONG).show();
        }else  if(resultCode == RESULT_OK && requestCode==13){
	   		
        	Intent i = new Intent(ListingActivity.this,ChatActivity.class);
			i.putExtra("itemid", myCateForfav.getItemid());
			i.putExtra("from_id", myCateForfav.getUserid());
			i.putExtra("Distance", myCateForfav.getDistance());
			i.putExtra("ItemArray", mItem_IdList);
			i.putExtra("position", ""+temp_pos);
			startActivity(i);
        	
	        }else if (resultCode == RESULT_CANCELED) {
            //Write your code if there's no result
        	Toast.makeText(context, "Please try again.", Toast.LENGTH_LONG).show();
    }else  if(resultCode == RESULT_OK && requestCode==14){
   		
    	i_Buy = new Intent(ListingActivity.this,MainCheckOutFragmentActivity.class);
		i_Buy.putExtra("itemid", myCateForfav.getItemid());
		i_Buy.putExtra("from_id", myCateForfav.getUserid());
		i_Buy.putExtra("Distance", myCateForfav.getDistance());
		i_Buy.putExtra("ItemArray", mItem_IdList);
		i_Buy.putExtra("position", ""+temp_pos);
		reserve="Y";
		new LockItemBackTask().execute("");
       }else  if(resultCode == RESULT_OK && requestCode==15){
    		Intent i=new Intent(ListingActivity.this, TapBoardActivity.class);
			startActivity(i);
			finish();
          }else if (resultCode == RESULT_CANCELED) {
       //Write your code if there's no result
        	//  Toast.makeText(context, "Please try again.", Toast.LENGTH_LONG).show();
          }
		}
	 
	
	};
	
	
	
/*	private void scaleImage(ImageView view)
	{
	    // Get the ImageView and its bitmap
	  
	    Drawable drawing = view.getDrawable();
	    if (drawing == null) {
	        return; // Checking for null & return, as suggested in comments
	    }
	    Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

	    // Get current dimensions AND the desired bounding box
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    int bounding = dpToPx(250);
	    Log.i("Test", "original width = " + Integer.toString(width));
	    Log.i("Test", "original height = " + Integer.toString(height));
	    Log.i("Test", "bounding = " + Integer.toString(bounding));

	    // Determine how much to scale: the dimension requiring less scaling is
	    // closer to the its side. This way the image always stays inside your
	    // bounding box AND either x/y axis touches it.  
	    float xScale = ((float) bounding) / width;
	    float yScale = ((float) bounding) / height;
	    float scale = (xScale <= yScale) ? xScale : yScale;
	    Log.i("Test", "xScale = " + Float.toString(xScale));
	    Log.i("Test", "yScale = " + Float.toString(yScale));
	    Log.i("Test", "scale = " + Float.toString(scale));

	    // Create a matrix for the scaling and add the scaling data
	    Matrix matrix = new Matrix();
	    matrix.postScale(scale, scale);

	    // Create a new bitmap and convert it to a format understood by the ImageView 
	    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	    width = scaledBitmap.getWidth(); // re-use
	    height = scaledBitmap.getHeight(); // re-use
	    BitmapDrawable result = new BitmapDrawable(scaledBitmap);
	    Log.i("Test", "scaled width = " + Integer.toString(width));
	    Log.i("Test", "scaled height = " + Integer.toString(height));

	    // Apply the scaled bitmap
	    view.setImageBitmap(scaledBitmap);

	    // Now change ImageView's dimensions to match the scaled image
	    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams(); 
	    params.width = width;
	    params.height = height;
	    view.setLayoutParams(params);

	    Log.i("Test", "done");
	}

	private int dpToPx(int dp)
	{
	    float density = getApplicationContext().getResources().getDisplayMetrics().density;
	    return Math.round((float)dp * density);
	}*/
/*	
	
*/
	
	}
