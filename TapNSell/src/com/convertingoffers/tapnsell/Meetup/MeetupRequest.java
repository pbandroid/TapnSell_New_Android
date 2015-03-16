package com.convertingoffers.tapnsell.Meetup;


import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.convertingoffers.tapnsell.HomeActivity;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Modal.MapStoreModal;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.RoundedImageView_CenterCrop;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class MeetupRequest extends BaseActivity implements OnClickListener {
	 ImageLoader iLoader_Rounded = ImageLoader.getInstance();
	 DisplayImageOptions options;
	Animation RightSwipe;
	Context context;
	Bitmap image_bitmap=null;
	double lat = 0.0, log = 0.0, Distance = 0.0, lat1, lat2, lng1, lng2,
			Dis = 0.0;
	protected LocationManager locationManager;
	String strlatitude, strlongitude, MyUrl;
	Location location;
	boolean isCurrentLocation = false, Successfull, isGPSEnabled = false,
			isNetworkEnabled = false, canGetLocation = false;

	String strLat = "", strLog = "", id = "", icon = "", strSmallName = "",
			userid, data, orderid, strformatted_address = "", Serchvalue = "",
			HomeAddress = "";
	ArrayList<MapStoreModal> mMapList = new ArrayList<MapStoreModal>();
	 ArrayList<MapStoreModal> text_sort  = new ArrayList<MapStoreModal>();
	MeetupCustomAdaper adapter;
	ListView lvStore;
	ImageView ivArrow_chouse_meetup;
	// EditText evHomeAddress;
	TextView tvHomeAddress;
	RelativeLayout rlHome;
	int year, month, day, hour, minute, second;
	 EditText evSearch;
	 int textlength = 0;
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meetup_request);
		LoadImage();
		ivMenu.setVisibility(View.VISIBLE);
		userid = pref.getString("UserID", "");
		Bundle b = getIntent().getExtras();
		if (b != null) {
			orderid = b.getString("orderid");
		}
		
		text_sort.clear();
		mMapList.clear();
		HomeAddress = pref.getString("HomeAddressMeetup", "");
		
		ivMenu.setVisibility(View.VISIBLE);
		
		
		tvHeader.setText("Meet Up Request");
		if (HomeAddress.length() == 0) {
			ivArrow_chouse_meetup.setImageDrawable(getResources().getDrawable(
					R.drawable.meetup_request_plus_icon));
			tvHomeAddress.setText("Enter Home Address");
		} else {
			tvHomeAddress.setText(HomeAddress);
			ivArrow_chouse_meetup.setImageDrawable(getResources().getDrawable(
					R.drawable.order_arrow));

		}
		
		Log.e("orderid bundle",""+orderid);
		userid = pref.getString("UserID", "");

		ivMenu.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		rlHome.setOnClickListener(this);
		// PrintHashKey();

		strlatitude = pref.getString("lat", "0");
		strlongitude = pref.getString("long", "0");
		
		if (cd.checkConnection()) {
			new getlatlang().execute("");
		} else {
			Toast.makeText(context, "Interner connection is not available!",
					Toast.LENGTH_LONG).show();
		}
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		evSearch.addTextChangedListener(new TextWatcher() {

		public void afterTextChanged(Editable s) {

		}

		public void beforeTextChanged(CharSequence s, int start, int count,
		int after) {

		}

		@SuppressLint("DefaultLocale") public void onTextChanged(CharSequence s, int start, int before,
		int count) {
		text_sort.clear();
		textlength = evSearch.getText().length();
//		new getlatlang().execute("");	
		for (int i = 0; i < mMapList.size(); i++) {
						
			MapStoreModal mycate = mMapList.get(i);
		String name =mycate.getName();
		String address =mycate.getAddress();
		if (textlength <= name.length()) {
		if(name.toLowerCase().contains(evSearch.getText().toString().toLowerCase()) ||address.toLowerCase().contains(evSearch.getText().toString().toLowerCase())){
		text_sort.add(mycate);
		}
		}
		}
		adapter = new MeetupCustomAdaper(context,
				R.layout.meetup_request_item, text_sort);
		lvStore.setAdapter(adapter);
		}
		});
		
	}
	private void LoadImage() {
		 image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.change_meetup_location_logo);
	}
	private void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(context, R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}

	private class RequestMeetupBackTask extends AsyncTask<String, Void, String> {
		String errorMessage, message;

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
//				orderid="66";
				Log.e("userids"," "+userid);
				Log.e("orderid"," "+orderid);
				Log.e("data"," "+data);
				
				try {
					JSONObject json = userFunction.MeetupRequestFunction(userid,
							orderid, data);
					
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
						
						
							message = json.optString("message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
			}
			return errorMessage;
		}

		@SuppressLint("InlinedApi") @Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if(result.equals("true")){
				Intent i = new Intent(context, HomeActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				Custom_Dialog.dialogCode(3, i, message, context);
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else{
				Custom_Dialog.dialogCode(2, null, message, context);	
			}
			
		}

	}
	private class GetSerchValueIDBackTask extends
			AsyncTask<String, Void, String> {
		String errorMessage;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			try {
				String StrStausOfJson = "";
			
				
		MyUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/"
				+ "json?location="
				+ strlatitude
				+ ","
				+ strlongitude
				+ "&radius=1000&"
				+ "types=food%7Ccafe%7Cpark%7Cbar%7Cmuseum%7Cpost_office%7Cschool%7Cshopping_mall"
				+ "&key=" + getResources().getString(R.string.mapkey);// AIzaSyATzZUwu8oHwGAJqw-dOJn95e3ExkdQFfM";
//		+ "types=food|cafe|park|bar|museum|post_office|school|shopping_mall"
		MyUrl= MyUrl.replaceAll(" ","%20");
		Log.e("MyUrl", " " + MyUrl);
		JSONObject json = userFunction.getNearestStoreFunction(MyUrl);
		StrStausOfJson = json.optString("status");
		errorMessage = "";

		if (StrStausOfJson.equals("OK")) {
			JSONArray JarrayStore;
			JarrayStore = json.getJSONArray("results");
			for (int i = 0; i < JarrayStore.length(); i++) {
				JSONObject cStore;
				cStore = JarrayStore.getJSONObject(i);

				JSONObject cgeometry = cStore.getJSONObject("geometry");
				JSONObject clocation = cgeometry
						.getJSONObject("location");

				id = cStore.getString("id");
				icon = cStore.getString("icon");
				strLat = clocation.getString("lat");
				strLog = clocation.getString("lng");
				strSmallName = cStore.getString("name");
				strformatted_address = cStore.getString("vicinity");

			/*	Log.e("id", " "+id);
				Log.e("icon", " "+icon);
				Log.e("strLat", " "+strLat);
				Log.e("strLog", " "+strLog);
				Log.e("strSmallName", " "+strSmallName);
				Log.e("strformatted_address", " "+strformatted_address);*/
				
				mMapList.add(new MapStoreModal(id, strSmallName,
						StrStausOfJson, strLat, strLog, strformatted_address,
						""));
			}

			errorMessage = "ok";
		} else {
			errorMessage = "zero";
		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return errorMessage;
}

@Override
protected void onPostExecute(String result) {
	if (progressDialog.isShowing()) {
		progressDialog.dismiss();
	}

	if (result.equals("ok")) {
		
		
		text_sort.clear();
		for (int i = 0; i < mMapList.size(); i++) {
			MapStoreModal cate =mMapList.get(i);
			text_sort.add(cate);
		}
		adapter = new MeetupCustomAdaper(context,
				R.layout.meetup_request_item, text_sort);
		lvStore.setAdapter(adapter);
		
	} else if (result.equals("zero")) {
		Custom_Dialog.dialogCode(2, null, "no nearest location found.",context);
	}

}

@Override
protected void onProgressUpdate(Void... values) {

}
}

	
	
	private class getlatlang extends
			AsyncTask<String, Void, String> {
		String errorMessage;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			try {
				locationManager = (LocationManager) context
						.getSystemService(Context.LOCATION_SERVICE);

				isGPSEnabled = locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER);

				isNetworkEnabled = locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				Log.e("enable", "isGPSEnabled " + isGPSEnabled
						+ " isNetworkEnabled " + isNetworkEnabled);
				if (!isGPSEnabled && !isNetworkEnabled) {
					Log.e("enable", "boolean " + canGetLocation);
				} else {
					MeetupRequest.this.canGetLocation = true;
					Log.e("enable", "boolean " + canGetLocation);

					if (isNetworkEnabled) {

						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							lat = location.getLatitude();
							log = location.getLongitude();
							strlatitude = "" + lat;
							strlongitude = "" + log;
							Log.i("Wifi", "Lattitude : " + lat + ", Longitude : "
									+ log);
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
							Log.i("GPS", "Lattitude : " + lat + ", Longitude : "
									+ log);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return errorMessage;
		}

	
		@Override
		protected void onPostExecute(String result) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			
			Log.e("strlatitude", " "+strlatitude);
			Log.e("strlongitude", " "+strlongitude);
			
			if (cd.checkConnection()) {
				new GetSerchValueIDBackTask().execute("");
			} else {
				Toast.makeText(context, "Interner connection is not available!",
						Toast.LENGTH_LONG).show();
			}
			
			/*if (result.equals("ok")) {
				
				
				text_sort.clear();
				for (int i = 0; i < mMapList.size(); i++) {
					MapStoreModal cate =mMapList.get(i);
					text_sort.add(cate);
				}
				adapter = new MeetupCustomAdaper(context,
						R.layout.meetup_request_item, text_sort);
				lvStore.setAdapter(adapter);
				
			} else if (result.equals("zero")) {
				custom_dialog.dialogCode(2, null, "no nearest location found.",context);
			}*/

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	// initialize variable
	@Override
	public void onContentChanged() {
		super.onContentChanged();

		context = this;
		iLoader_Rounded.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.meetup_list_user_image)
				.showImageForEmptyUri(R.drawable.meetup_list_user_image)
				.showImageOnFail(R.drawable.meetup_list_user_image)
				.resetViewBeforeLoading(true)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true)
				.build();
		ivArrow_chouse_meetup= (ImageView) findViewById(R.id.ivArrow_chouse_meetup);
		tvHomeAddress = (TextView) findViewById(R.id.tvHomeAddress);
		lvStore = (ListView) findViewById(R.id.lvStore);
		rlHome = (RelativeLayout) findViewById(R.id.rlHome);
		tvHomeAddress=(TextView)findViewById(R.id.tvHomeAddress);
		evSearch=(EditText)findViewById(R.id.evsearch);
	}

	
	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.ivBack:
			buttonAnimation(ivBack);
			finish();
			break;
		case R.id.ivMenu:
			buttonAnimation(ivMenu);
			i = new Intent(context, TapBoardActivity.class);
			startActivity(i);	
			break;
			
		case R.id.rlHome:
			buttonAnimation(rlHome);
			HomeAddress = pref.getString("HomeAddressMeetup", "");
			MapStoreModal store = new MapStoreModal();

			store.setAddress("");
			store.setIcon("");
			store.setId("");
			store.setLat(strlatitude);
			store.setLng(strlongitude);
			store.setName("Home");
			store.setDistance("0");
			 i = new Intent(context,MeetupRequestdialogActivity.class);
			i.putExtra("store", store);
			i.putExtra("Stauts", true);
			i.putExtra("orderid", orderid);
			
			startActivity(i);

			break;

		default:
			break;
		}
	}

	@SuppressWarnings("static-access")
	public void MeetupdialogCode(MapStoreModal store, boolean status) {

		final Dialog dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_mdt_home);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);

		ImageView ivStoreIcon = (ImageView) dialog
				.findViewById(R.id.ivStoreIcon);
		ImageView ivSelect = (ImageView) dialog.findViewById(R.id.ivSelect);
		ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
		final TextView tvAddress = (TextView) dialog
				.findViewById(R.id.tvAddress);
		final EditText evHomeAddress = (EditText) dialog
				.findViewById(R.id.evHomeAddress);
		TextView tvDistance = (TextView) dialog.findViewById(R.id.tvDistance);
		final TextView tvStoreName = (TextView) dialog
				.findViewById(R.id.tvStoreName);

		final TextView tvDate1 = (TextView) dialog.findViewById(R.id.tvDate1);
		final TextView tvDate2 = (TextView) dialog.findViewById(R.id.tvDate2);
		final TextView tvDate3 = (TextView) dialog.findViewById(R.id.tvDate3);
		final TextView tvTime1 = (TextView) dialog.findViewById(R.id.tvTime1);
		final TextView tvTime2 = (TextView) dialog.findViewById(R.id.tvTime2);
		final TextView tvTime3 = (TextView) dialog.findViewById(R.id.tvTime3);
		RelativeLayout rlDate1 = (RelativeLayout) dialog.findViewById(R.id.rlDate1);
		RelativeLayout rlDate2 = (RelativeLayout) dialog.findViewById(R.id.rlDate2);
		RelativeLayout rlDate3 = (RelativeLayout) dialog.findViewById(R.id.rlDate3);
		RelativeLayout rlTime1 = (RelativeLayout) dialog.findViewById(R.id.rlTime1);
		RelativeLayout rlTime2 = (RelativeLayout) dialog.findViewById(R.id.rlTime2);
		RelativeLayout rlTime3 = (RelativeLayout) dialog.findViewById(R.id.rlTime3);

		
		
		
		if (status) {
			HomeAddress = pref.getString("HomeAddressMeetup", "");
			if (HomeAddress.length() == 0) {

				tvAddress.setVisibility(View.GONE);
				evHomeAddress.setVisibility(View.VISIBLE);
			} else {
				tvAddress.setVisibility(View.VISIBLE);
				evHomeAddress.setVisibility(View.GONE);
				tvAddress.setText(HomeAddress);
			}
			tvStoreName.setText("Home");
			tvStoreName.setTag(store.getLat());
			tvAddress.setTag(store.getLng());
			tvDistance.setText("0.0 m");
			ivStoreIcon.setImageDrawable(getResources().getDrawable(
					R.drawable.meetup_request_home));

			evHomeAddress
					.setOnEditorActionListener(new OnEditorActionListener() {

						@Override
						public boolean onEditorAction(TextView v, int actionId,
								KeyEvent event) {

							if (actionId == EditorInfo.IME_ACTION_DONE
									|| actionId == EditorInfo.IME_ACTION_NEXT) {

								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										evHomeAddress.getWindowToken(), 0);

								Serchvalue = evHomeAddress.getText().toString();

								if (Serchvalue.equals("")) {
									Custom_Dialog.dialogCode(2, null,
											"Please enter home address.",
											context);
								} else {

									editor.putString("HomeAddressMeetup", ""
											+ Serchvalue);
									editor.commit();
									ivArrow_chouse_meetup
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.order_arrow));
									tvHomeAddress.setText("" + Serchvalue);
									tvHomeAddress.setVisibility(View.VISIBLE);
									evHomeAddress.setVisibility(View.GONE);

								}
							} else {
								return false;
							}

							return true;
						}
					});

		} else {
			tvAddress.setVisibility(View.VISIBLE);
			evHomeAddress.setVisibility(View.GONE);
			tvDistance.setText(store.getDistance());
			tvDistance.setText("" + store.getDistance() + " m");
			ivStoreIcon.setImageDrawable(getResources().getDrawable(
					R.drawable.order_arrow));
			tvAddress.setText(store.getAddress());
			tvStoreName.setText(store.getName());
			tvStoreName.setTag(store.getLat());
			tvAddress.setTag(store.getLng());
		}
		rlDate1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerdialogCode(tvDate1);
			}
		});

		rlDate2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerdialogCode(tvDate2);
			}
		});

		rlDate3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerdialogCode(tvDate3);
			}
		});

		rlTime1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerdialogCode(tvTime1);
			}
		});

		rlTime2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerdialogCode(tvTime2);
			}
		});

		rlTime3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerdialogCode(tvTime3);
			}
		});

		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(evHomeAddress.getWindowToken(), 0);

				buttonAnimation(v);
			}
		});

		ivSelect.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("null")
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				buttonAnimation(v);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(evHomeAddress.getWindowToken(), 0);
				JSONArray dataArray = new JSONArray();
				try {
					JSONObject obj = null;
					for (int i = 0; i < 3; i++) {
						obj.put("place", tvStoreName.getText().toString());
						obj.put("address", tvAddress.getText().toString());
						obj.put("lat", tvStoreName.getTag().toString());
						obj.put("lng", tvAddress.getTag().toString());
						if (i == 0) {
							obj.put("date", tvDate1.getText().toString());
							obj.put("time", tvTime1.getText().toString());
						} else if (i == 1) {
							obj.put("date", tvDate2.getText().toString());
							obj.put("time", tvTime2.getText().toString());

						} else if (i == 2) {
							obj.put("date", tvDate3.getText().toString());
							obj.put("time", tvTime3.getText().toString());

						}
						dataArray.put(obj);
						data = dataArray.toString();
						Log.e("Store", " " + data);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (cd.checkConnection()) {
					new RequestMeetupBackTask().execute("");
				}

			}
		});

		dialog.show();
	}


	@SuppressWarnings("static-access")
	public void DatePickerdialogCode(final TextView tvDate) {
		// /
		final DatePicker datepicker;
		final Dialog datepicker_dialog = new Dialog(context);
		datepicker_dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		datepicker_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		datepicker_dialog.setCancelable(false);
		datepicker_dialog.setContentView(R.layout.dialog_datepicker);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(datepicker_dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		datepicker_dialog.getWindow().setAttributes(lp);

		ImageView ivCont = (ImageView) datepicker_dialog
				.findViewById(R.id.ivCont);
		datepicker = (DatePicker) datepicker_dialog
				.findViewById(R.id.datepicker);
		// / Set current date on view

		
		String temp_date="";
		temp_date=tvDate.getText().toString();
		if(temp_date.equals("Date")){
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);	
		}
		datepicker.init(year, month, day, null);
		// Set current date on view

		ivCont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				datepicker_dialog.dismiss();
				int day = datepicker.getDayOfMonth();
				int month = datepicker.getMonth()+1;
				int year = datepicker.getYear();

				tvDate.setText(day + "/" + month + "/" + year);
			}
		});

		datepicker_dialog.show();
	}

	@SuppressWarnings("static-access")
	public void TimePickerdialogCode(final TextView tvTime) {
		// /
		final TimePicker timepicker;
		final Dialog time_picker_dialog = new Dialog(context);
		time_picker_dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		time_picker_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		time_picker_dialog.setCancelable(false);
		time_picker_dialog.setContentView(R.layout.dialog_timepicker);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(time_picker_dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		time_picker_dialog.getWindow().setAttributes(lp);

		ImageView ivCont = (ImageView) time_picker_dialog
				.findViewById(R.id.ivCont);
		timepicker = (TimePicker) time_picker_dialog
				.findViewById(R.id.Timepicker);
		// / Set current date on view

		String temp_time="";
		temp_time=tvTime.getText().toString();
		final Calendar c = Calendar.getInstance();
		if (temp_time.equals("Time")) {
			hour = c.get(Calendar.HOUR);
			minute = c.get(Calendar.MINUTE);
		}

//		tvTime.setText(new StringBuilder().append(hour).append(":")
//				.append(minute));
		timepicker.setCurrentHour(hour);
		timepicker.setCurrentMinute(minute);

		// Set current date on view

		ivCont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				time_picker_dialog.dismiss();

				hour = timepicker.getCurrentHour();
				minute = timepicker.getCurrentMinute();

				tvTime.setText(day + "/" + month + "/" + year);
			}
		});

		time_picker_dialog.show();
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();

		HomeAddress = pref.getString("HomeAddressMeetup", "");

		if (HomeAddress.length() == 0) {
			ivArrow_chouse_meetup.setImageDrawable(getResources().getDrawable(
					R.drawable.meetup_request_plus_icon));
			tvHomeAddress.setText("Enter Home Address");
		} else {
			tvHomeAddress.setText(HomeAddress);
			ivArrow_chouse_meetup.setImageDrawable(getResources().getDrawable(
					R.drawable.order_arrow));

		}

	}

	public class MeetupCustomAdaper extends ArrayAdapter<MapStoreModal> {
		
		private ArrayList<MapStoreModal> listSubCategories;
		protected Object mysun;

		public MeetupCustomAdaper(Context context, int textViewResourceId,
				ArrayList<MapStoreModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<MapStoreModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivLocationIcon;
			TextView tvLocationName, tvAddress, tvDistance;
			RelativeLayout rlMain;
			
		}
//
		@Override
		public View getView(int position, View convertView,
				ViewGroup parent) {

			ViewHolder 	holder = null;
			final MapStoreModal store = listSubCategories.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = vi.inflate(R.layout.meetup_request_item, null);

				holder.ivLocationIcon = (ImageView) convertView
						.findViewById(R.id.ivLocationIcon);
				holder.tvLocationName = (TextView) convertView
						.findViewById(R.id.tvLocationName);
				holder.tvAddress = (TextView) convertView
						.findViewById(R.id.tvAddress);
				holder.tvDistance = (TextView) convertView
						.findViewById(R.id.tvDistance);
				holder.rlMain = (RelativeLayout) convertView
						.findViewById(R.id.rlMain);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			
			String image = store.getIcon();

			if(image.length()>0){
				iLoader_Rounded.displayImage(image, holder.ivLocationIcon, options, loadImageListener);
			}else{
				iLoader_Rounded.displayImage("", holder.ivLocationIcon, options, loadImageListener);
			}	

			Log.e("store.getName()", ""+store.getName());
			Log.e("getAddress()", ""+store.getAddress());
			
			holder.tvLocationName.setText(store.getName());
			holder.tvAddress.setText(store.getAddress());

			float la1, la2, lo1, lo2, dis;
			try {
				la1 = Float.parseFloat(strlatitude);
				lo1 = Float.parseFloat(strlongitude);
				la2 = Float.parseFloat(store.getLat());
				lo2 = Float.parseFloat(store.getLng());
				dis = distFrom(la1, lo1, la2, lo2);
				dis=dis/1000;
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				dis = (float) 0.0;
				e.printStackTrace();
			}
			String distan = String.format("%.2f", dis);
			holder.tvDistance.setText("" + distan + "K");
			store.setDistance(distan);
			//holder.rlMain.setTag(store);
			holder.rlMain.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					buttonAnimation(v);
					
					Intent i = new Intent(context,MeetupRequestdialogActivity.class);
					i.putExtra("store", store);
					i.putExtra("Stauts", false);
					i.putExtra("orderid", orderid);
					startActivity(i);
			//		MeetupdialogCode(store, false);
				}
			});
			return convertView;
		}
	}

	public float distFrom(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 3958.75;
		float Distance;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		Distance = Float.valueOf("" + dist * meterConversion);
		return Distance;
	}

	ImageLoadingListener loadImageListener = new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				((ImageView) view).setImageBitmap(image_bitmap);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//				Log.i(getClass().getSimpleName(), "Image Loading failed" + imageUri + " REason :" + failReason);
				((ImageView) view).setImageBitmap(image_bitmap);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				Log.i(getClass().getSimpleName(), "Image Loading " + imageUri);
				if (loadedImage != null) {
					
//	 		It will Round Image
					loadedImage = RoundedImageView_CenterCrop.scaleCenterCrop(loadedImage, (int) getResources().getDimension(R.dimen.ninetydp), false);
					 
//			If You want only Rounded Corner Just give last argument as true
//					loadedImage = RoundedImageView.scaleCenterCrop(loadedImage,	(int) getResources().getDimension(R.dimen.thirty), true);
					
					((ImageView) view).setImageBitmap(loadedImage);
				} else {

					((ImageView) view).setImageBitmap(image_bitmap);
				}
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
				
			}
		};

}

/*

//checkin gps is enable or not
private void showGPSDisabledAlertToUser() {
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	alertDialogBuilder
			.setMessage(
					"GPS is disabled in your device. Would you like to enable it?")
			.setCancelable(false)
			.setPositiveButton("Goto Settings Page To Enable GPS",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent callGPSSettingIntent = new Intent(
									android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(callGPSSettingIntent);
						}
					});
	alertDialogBuilder.setNegativeButton("Cancel",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
	AlertDialog alert = alertDialogBuilder.create();
	alert.show();
}*/