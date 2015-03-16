package com.convertingoffers.tapnsell.Shop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Modal.CategoryModal;
import com.convertingoffers.tapnsell.Modal.SearchModel;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.Login_Dialog_Activity;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.facebook.LoginActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class BrouseCategoryActivity extends BaseActivity implements OnClickListener {
	Animation RightSwipe;
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options,option_item;
	Context context;
	String strUserID, strname, strlatitude="0", strlongitude="0",categoryid,reserved="";
	CategoryCustomAdaper adapter;
	ArrayList<CategoryModal> mCategory= new ArrayList<CategoryModal>();
	boolean UserIdStatus = false;

	double lat = 0.0, log = 0.0, Distance = 0.0, lat1, lat2, lng1, lng2,
			Dis = 0.0;
	protected LocationManager locationManager;
	Location location;
	boolean isCurrentLocation = false, Successfull, isGPSEnabled = false,
			isNetworkEnabled = false, canGetLocation = false;

	ImageView ivMenuBtn,ivGobtn,ivMenu;
	EditText evsearch;
	int intStartNewListing=0;
	
	GridView gvCatListing;
	String itemid, userid="", name, price, distance, image, has_like, no_of_likes;
	ArrayList<SearchModel> mSearchList = new ArrayList<SearchModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brouse_category);
		ivMenu.setVisibility(View.VISIBLE);
		userid = pref.getString("UserID", "0");
		mCategory.clear();
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
		categoryid = bundle.getString("categoryid");
		}else{
			categoryid="0";
		}
		
		gvCatListing.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
			CategoryModal cate = mCategory.get(position);
			String	no_of_subcate= cate.getNo_of_subcategory();
			String	id=cate.getId();
					
			if(no_of_subcate.equals("0")){
					
					Intent i =new Intent(context, ListingActivity.class);
					i.putExtra("categoryid", id);
					i.putExtra("ListingType", cate.getName());
					startActivity(i);
			}else{
				
					Intent icate =new Intent(context, BrouseCategoryActivity.class);
					icate.putExtra("categoryid", id);
					startActivity(icate);
					
			}
			}
		});
		
		tvHeader.setText("Browse Categories");
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(evsearch.getWindowToken(), 0);
		evsearch.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
		ivGobtn.setOnClickListener(this);
		evsearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

					strname = evsearch.getText().toString().trim();

					strUserID = pref.getString("UserID", "");

					strlatitude = pref.getString("lat", "");
					strlongitude = pref.getString("long", "");

					if (strname.length() == 0) {
						Custom_Dialog.dialogCode(2, null, "Please enter text to search.", context);
						
		 			} else if (strUserID.length() == 0) {
		 				Intent i = new Intent(BrouseCategoryActivity.this,
								LoginActivity.class);
		 				Custom_Dialog.dialogCode(3, i, "Please Login again", context);
					}  else {
						if (cd.checkConnection()) {
							new SearchBackTask().execute("");
						} else {
							Toast.makeText(BrouseCategoryActivity.this,
									"internet_conn_failed", Toast.LENGTH_LONG)
									.show();
						}
					}

				} else {
					return false;
				}
				return true;
			}
		});
		
		
		
		if (cd.checkConnection()) {
			new CategoryBackTask().execute("");
		} else {
			Toast.makeText(BrouseCategoryActivity.this,"internet_conn_failed", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		View view = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (view instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
		return ret;
	}

	private class SearchBackTask extends AsyncTask<String, Void, String> {
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
				mSearchList.clear();
				JSONObject json = userFunction.SearchFunction(strUserID,
						strname, strlatitude, strlongitude,""+intStartNewListing,"P");
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONArray Item = json.getJSONArray("Item");
							for (int i = 0; i < Item.length(); i++) {

								JSONObject c = Item.getJSONObject(i);

								itemid = c.getString("itemid");
								userid = c.getString("userid");
								name = c.getString("name");
								price = c.getString("price");
								distance = c.getString("distance");
								reserved = c.getString("reserved");
								image = c.getString("image");
								image = UserFunctions.localImageUrl + image;

								has_like = c.getString("has_like");
								no_of_likes = c.getString("no_of_likes");
								mSearchList.add(new SearchModel(itemid, userid,
										name, price, distance, image, has_like,
										no_of_likes,"",reserved));
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
				
				Intent i =new Intent(BrouseCategoryActivity.this, ListingActivity.class);
				i.putExtra("SearchData",mSearchList);
				 i.putExtra("ListingType","Search");
				startActivity(i);
			} else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
//				custom_dialog.dialogCode(2, null, "No items found.", con);
				
				dialogCode("No Item Listed Get notified once someone posts it");
				
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	@SuppressWarnings("static-access")
	public   void dialogCode(String msg) {

		final Dialog  dialog = new Dialog(BrouseCategoryActivity.this);
    	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	dialog.setContentView(R.layout.dialog_custom_2_button);
    	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = lp.MATCH_PARENT;
        lp.height = lp.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    	ImageView ivNo = (ImageView) dialog.findViewById(R.id.ivNo);
    	ImageView ivYes = (ImageView) dialog.findViewById(R.id.ivYes);
    	TextView tvMsg= (TextView) dialog.findViewById(R.id.tvMsg);
    	tvMsg.setText(msg);
    	ivYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
				
				if(cd.checkConnection()){
					new SearchNotificationBackTask().execute("");
				}else{
					Toast.makeText(BrouseCategoryActivity.this, "internet_conn_failed",Toast.LENGTH_LONG).show();
				}
			}
		});
    	ivNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				dialog.dismiss();
			}
		});
    	
    }
    
	private class CategoryBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json=null;
				if(categoryid.equals("0")){
					json = userFunction.CategoryFunction(categoryid);	
				}else{
					 json = userFunction.mycategoryListFunction(categoryid);
					 
				}
				
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONArray Category = json.getJSONArray("Category");
							
							for (int i = 0; i < Category.length(); i++) {
								JSONObject c  = Category.getJSONObject(i);
								
								String id=c.getString("id");
								String name=c.getString("name");
								String image=UserFunctions.localImageUrl;
								image=image+c.getString("image");
								String no_of_subcategory=c.getString("no_of_subcategory");
								
								mCategory.add(new CategoryModal(id, name, image, no_of_subcategory));
								
							}
							
							errorMessage = "true";
						} else {
							errorMessage = "false";
						}
					} else {
						errorMessage = "error in posting";
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
				adapter = new CategoryCustomAdaper(BrouseCategoryActivity.this,R.layout.brouse_category_item, mCategory);
				gvCatListing.setAdapter(adapter);
			} else {
				Custom_Dialog.dialogCode(2, null, "No category found.", context);
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	private class SearchNotificationBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json=null;
				
				String cat_id="";
				if(categoryid.equals("0")){
					cat_id="";
				}

				json = userFunction.SearchNotificationFunction(strUserID, strname, cat_id);	
				
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							message=json.getString("message");
							
							errorMessage = "true";
						} else {
							message=json.optString("message");
							errorMessage = "false";
						}
					} else {
						errorMessage = "error in posting";
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
				//Custom_Dialog.dialogCode(2, null, message, context);
				Toast toast;
				toast=	Toast.makeText(context, message, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	private class CategoryCustomAdaper extends ArrayAdapter<CategoryModal> {
		ViewHolder holder;
		private ArrayList<CategoryModal> listSubCategories;
		public CategoryCustomAdaper(Context context, int textViewResourceId,
				ArrayList<CategoryModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<CategoryModal>();
			this.listSubCategories = listSubCategories;
		}

		class ViewHolder {
			TextView tvCat_Name;
			ImageView ivCat;
			RelativeLayout rlCatMain;
			
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			 holder = null;
			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.brouse_category_item, null);

				holder = new ViewHolder();
				holder.rlCatMain= (RelativeLayout) convertView.findViewById(R.id.rlCatMain);
				holder.tvCat_Name = (TextView) convertView.findViewById(R.id.tvCat_Name);
				holder.ivCat = (ImageView) convertView.findViewById(R.id.ivCat);
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if(!iLoader_item.isInited()){
			iLoader_item.init(ImageLoaderConfiguration.createDefault(context));
			options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.transperant)
			.showImageForEmptyUri(R.drawable.transperant)
			.showImageOnFail(R.drawable.transperant)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
			}
				
				
			CategoryModal myCate = listSubCategories.get(position);

			if(position==0){
				if (Build.VERSION.SDK_INT >= 16)
				holder.rlCatMain.setBackground(getResources().getDrawable(R.drawable.cat_top_left));
				else
					holder.rlCatMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.cat_top_left));
					
			}else if(position==2){
				if (Build.VERSION.SDK_INT >= 16)
				holder.rlCatMain.setBackground(getResources().getDrawable(R.drawable.cat_top_right));
				else
					holder.rlCatMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.cat_top_right));
	
			}else if(position+1==listSubCategories.size()){

				if (Build.VERSION.SDK_INT >= 16)
				holder.rlCatMain.setBackground(getResources().getDrawable(R.drawable.cat_bottam_right));
				else
					holder.rlCatMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.cat_bottam_right));
	
			}else if(listSubCategories.size()%3==0 && position==listSubCategories.size()-3){
				if (Build.VERSION.SDK_INT >= 16)
				holder.rlCatMain.setBackground(getResources().getDrawable(R.drawable.cat_bottam_left));
				else
					holder.rlCatMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.cat_bottam_left));
	
			}else if(listSubCategories.size()%3==1 && position==listSubCategories.size()-2){
				if (Build.VERSION.SDK_INT >= 16)
				holder.rlCatMain.setBackground(getResources().getDrawable(R.drawable.cat_bottam_left));
				else
					holder.rlCatMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.cat_bottam_left));
			}else{
				if (Build.VERSION.SDK_INT >= 16)
				holder.rlCatMain.setBackground(getResources().getDrawable(R.drawable.cat_center));
				else
					holder.rlCatMain.setBackgroundDrawable(getResources().getDrawable(R.drawable.cat_center));
			}
			
			holder.tvCat_Name.setText(myCate.getName());
			image = myCate.getImage();
			

			// Image display using lazy loading 
			ImageLoader.getInstance()
				.displayImage(image, holder.ivCat, options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						//holder.pbimage.setProgress(0);
//						holder.pbimage.setIndeterminate(true);
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
			
				
				
				
			return convertView;
		}
 }

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		
		evsearch = (EditText) findViewById(R.id.evsearch);
		gvCatListing=(GridView)findViewById(R.id.gvCatListing);
		ivGobtn=(ImageView)findViewById(R.id.ivGobtn);
		ivMenu=(ImageView)findViewById(R.id.ivMenu);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
	
		case R.id.ivGobtn:

			strname=evsearch.getText().toString().trim();
			strUserID = pref.getString("UserID", "1");
			strlatitude = pref.getString("lat", "");
			strlongitude = pref.getString("long", "");
			 if(strlatitude.length()==0 && strlongitude.length()==0){
					strlatitude="0";
					strlongitude="0";
				}
			if(strname.length()==0){
				Custom_Dialog.dialogCode(2, null, "Please enter text to search.", context);
			}else if(strUserID.length()==0){
				Intent i = new Intent(BrouseCategoryActivity.this,
						LoginActivity.class);
				Custom_Dialog.dialogCode(3, i, "Please Login again.", context);
				UserIdStatus=true;
			}else{
				if(cd.checkConnection()){
					evsearch.setText("");
					new SearchBackTask().execute("");
				}else{
					Toast.makeText(BrouseCategoryActivity.this, "internet_conn_failed",Toast.LENGTH_LONG).show();
				}
			}
			break;

		case R.id.ivBack:
			finish();
			break;
		case R.id.ivMenu:
			Intent iMenuBtn;
			buttonAnimation(ivMenu);
			if (userid.length() == 0 || userid.equals("0")) {
				
				 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
				 iMenuBtn.putExtra("title", "TapnSell");
				 startActivityForResult(iMenuBtn, 31);
			
			}else {
				Intent i =  new Intent(context,TapBoardActivity.class);
				startActivity(i);
				
			}
			
			
			break;
		default:
			break;
		}

	}
	private void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(context,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
	public void getLocation() {
		try {
			locationManager = (LocationManager) BrouseCategoryActivity.this
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		
		
		 if(resultCode == RESULT_OK && requestCode==31){
				Intent i =  new Intent(context,TapBoardActivity.class);
				startActivity(i);
				finish();
		 }else if (resultCode == RESULT_CANCELED) {
		       //Write your code if there's no result
			  // 	Toast.makeText(context, "Please try again.", Toast.LENGTH_LONG).show();
			}
				 

	}
	@Override
	protected void onResume() {
	super.onResume();
	userid = pref.getString("UserID", "0");
	}
	
}
