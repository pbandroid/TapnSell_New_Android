package com.convertingoffers.tapnsell.Meetup;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;

import com.convertingoffers.tapnsell.HomeActivity;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.Modal.MapStoreModal;
import com.convertingoffers.tapnsell.util.ConnectionDetector;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class MeetupRequestdialogActivity extends FragmentActivity implements OnClickListener  {
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	Context context ;
	Animation RightSwipe;
	ImageView ivStoreIcon,ivSelect,ivClose;
	
	public ProgressDialog progressDialog;
	public String KEY_SUCCESS = "successfull",KEY_SUCCESS_STATUS = "true",strmsg="";
	int maptype = 0;
	TextView tvAddress,tvDistance,tvStoreName,tvDate1,tvDate2,tvDate3,tvTime1,tvTime2,tvTime3;
	EditText evHomeAddress;
	RelativeLayout rlDate1,rlDate2,rlDate3,rlTime1,rlTime2,rlTime3;
	String strLat = "", strLog = "", id = "", icon = "", strSmallName = "",
	userid, data, orderid, strformatted_address = "", Serchvalue = "",
	HomeAddress = "";
	boolean booldate1=false,booldate2=false,booldate3=false,booltime1=false,booltime2=false,booltime3=false; 
	boolean status=false;
	
	public Editor editor;
	public SharedPreferences pref;
	public ConnectionDetector cd;
	public UserFunctions userFunction;
	MapStoreModal store;
	int year, month, day, hour, minute, second;
	ArrayList<String> mdate =new ArrayList<String>(3);
	ArrayList<String> mtime =new ArrayList<String>(3);
	Double lat,log;
	 GoogleMap mMap;
		protected static final String TAG_ERROR_DIALOG_FRAGMENT = "errorDialog";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_mdt_home);
		userid = pref.getString("UserID", "");
		mdate.add("");
		mdate.add("");
		mdate.add("");
		mtime.add("");
		mtime.add("");
		mtime.add("");
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			status = bundle.getBoolean("Stauts");
			store= (MapStoreModal) bundle.getSerializable("store");
			orderid= bundle.getString("orderid");
		}
		
		
		lat=Double.valueOf(store.getLat());
		log=Double.valueOf(store.getLng());
		
		
		rlDate1.setOnClickListener(this);
		rlDate2.setOnClickListener(this);
		rlDate3.setOnClickListener(this);
		rlTime1.setOnClickListener(this);
		rlTime2.setOnClickListener(this);
		rlTime3.setOnClickListener(this);
		ivSelect.setOnClickListener(this);
		ivClose.setOnClickListener(this);
		
		if (readyToGo()) {
			setUpMapIfNeeded();
		}
		
		
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
									
									tvAddress.setText("" + Serchvalue);
									tvAddress.setVisibility(View.VISIBLE);
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
			String image = store.getIcon();

			
			// Image display using lazy loading 

			iLoader_item.displayImage(image, ivStoreIcon, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					//holder.pbimage.setProgress(0);
//					holder.pbimage.setIndeterminate(true);
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
			
			
			
			tvAddress.setText(store.getAddress());
			tvStoreName.setText(store.getName());
			tvStoreName.setTag(store.getLat());
			tvAddress.setTag(store.getLng());
		}
		

		 
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		//Initialize variable
		context=this;
		
		iLoader_item.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.change_meetup_location_logo)
		.showImageForEmptyUri(R.drawable.change_meetup_location_logo)
		.showImageOnFail(R.drawable.change_meetup_location_logo)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		ivStoreIcon = (ImageView) findViewById(R.id.ivStoreIcon);
		ivSelect = (ImageView) findViewById(R.id.ivSelect);
		ivClose = (ImageView) findViewById(R.id.ivClose);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		evHomeAddress = (EditText) findViewById(R.id.evHomeAddress);
		tvDistance = (TextView) findViewById(R.id.tvDistance);
		tvStoreName = (TextView) findViewById(R.id.tvStoreName);
		tvDate1 = (TextView) findViewById(R.id.tvDate1);
		tvDate2 = (TextView) findViewById(R.id.tvDate2);
		tvDate3 = (TextView) findViewById(R.id.tvDate3);
		tvTime1 = (TextView) findViewById(R.id.tvTime1);
		tvTime2 = (TextView) findViewById(R.id.tvTime2);
		tvTime3 = (TextView) findViewById(R.id.tvTime3);
		rlDate1 = (RelativeLayout) findViewById(R.id.rlDate1);
		rlDate2 = (RelativeLayout) findViewById(R.id.rlDate2);
		rlDate3 = (RelativeLayout) findViewById(R.id.rlDate3);
		rlTime1 = (RelativeLayout) findViewById(R.id.rlTime1);
		rlTime2 = (RelativeLayout) findViewById(R.id.rlTime2);
		rlTime3 = (RelativeLayout) findViewById(R.id.rlTime3);
		pref = PreferenceManager
				.getDefaultSharedPreferences(MeetupRequestdialogActivity.this);
		editor = pref.edit();
		userFunction = new UserFunctions();
		cd = new ConnectionDetector(MeetupRequestdialogActivity.this);
	   
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.rlDate1:
			buttonAnimation(v);
			DatePickerdialogCode(tvDate1,1);
			break;
		case R.id.rlDate2:
			buttonAnimation(v);
			DatePickerdialogCode(tvDate2,2);
			break;
		case R.id.rlDate3:
			buttonAnimation(v);
			DatePickerdialogCode(tvDate3,3);
			break;
		case R.id.rlTime1:
			buttonAnimation(v);
			TimePickerdialogCode(tvTime1,11);
			break;
		case R.id.rlTime2:
			buttonAnimation(v);
			TimePickerdialogCode(tvTime2,22);
			break;
		case R.id.rlTime3:
			buttonAnimation(v);
			TimePickerdialogCode(tvTime3,33);
			break;
		case R.id.ivClose:
			buttonAnimation(v);
			finish();
			break;
		case R.id.ivSelect:
			buttonAnimation(v);
	
String Address= tvAddress.getText().toString().trim();
			
	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(evHomeAddress.getWindowToken(), 0);
	JSONArray dataArray = new JSONArray();
	try {
		JSONObject obj;
		for (int i = 0; i < 3; i++) {
			obj =new JSONObject() ;
			
			if (i == 0) {
				if(!(mdate.get(0).equals(null)||mdate.get(0).equals(""))&&!(mtime.get(0).equals(null)||mtime.get(0).equals(""))){
					obj.put("place", tvStoreName.getText().toString());
					obj.put("address", tvAddress.getText().toString());
					obj.put("lat", tvStoreName.getTag().toString());
					obj.put("lng", tvAddress.getTag().toString());
					obj.put("date", mdate.get(0).toString());
					obj.put("time", mtime.get(0).toString());	
					dataArray.put(obj);
				}
				
			} else if (i == 1) {
				if(!(mdate.get(1).equals(null)||mdate.get(1).equals(""))&&!(mtime.get(1).equals(null)||mtime.get(1).equals(""))){
					
				obj.put("place", tvStoreName.getText().toString());
				obj.put("address", tvAddress.getText().toString());
				obj.put("lat", tvStoreName.getTag().toString());
				obj.put("lng", tvAddress.getTag().toString());
			
				obj.put("date", mdate.get(1).toString());
				obj.put("time", mtime.get(1).toString());
				dataArray.put(obj);

				}
			} else if (i == 2) {
				if(!(mdate.get(2).equals(null)||mdate.get(2).equals(""))&&!(mtime.get(2).equals(null)||mtime.get(2).equals(""))){
					
					obj.put("place", tvStoreName.getText().toString());
					obj.put("address", tvAddress.getText().toString());
					obj.put("lat", tvStoreName.getTag().toString());
					obj.put("lng", tvAddress.getTag().toString());
					obj.put("date", mdate.get(2).toString());
					obj.put("time", mtime.get(2).toString());
					dataArray.put(obj);
			}
			}
			
			data = dataArray.toString();
			Log.e("Store", " " + data);
		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	if(Address==null || Address.equals("")){
		Custom_Dialog.dialogCode(2, null, "Please enter home address.", context);	
	}else if((mdate.get(0).equals(null)||mdate.get(0).equals(""))&&(mdate.get(1).equals(null)||mdate.get(1).equals(""))&&(mdate.get(2).equals(null)||mdate.get(2).equals(""))){
		Custom_Dialog.dialogCode(2, null, "Please select atleast one date.", context);	
	}else if((mtime.get(0).equals(null)||mtime.get(0).equals(""))&&(mtime.get(1).equals(null)||mtime.get(1).equals(""))&&(mtime.get(2).equals(null)||mtime.get(2).equals(""))){
		Custom_Dialog.dialogCode(2, null, "Please select atleast one time.", context);	
	}else if(dataArray.length()==0){
		Custom_Dialog.dialogCode(2, null, "Please select proper date time.", context);
	}else{
		if (cd.checkConnection()) {
			new RequestMeetupBackTask().execute("");
		}
	}
	/*else if((mtime.get(0).equals(null)||mtime.get(0).equals(""))&&(!mdate.get(0).equals(null)||!mdate.get(0).equals(""))){
		Custom_Dialog.dialogCode(2, null, "Please select proper date time", context);	
	}else if(!(mtime.get(0).equals(null)||!mtime.get(0).equals(""))&&(mdate.get(0).equals(null)||mdate.get(0).equals(""))){
		Custom_Dialog.dialogCode(2, null, "Please select proper date time", context);	
	}else if((mtime.get(1).equals(null)||mtime.get(1).equals(""))&&(!mdate.get(1).equals(null)||!mdate.get(1).equals(""))){
		Custom_Dialog.dialogCode(2, null, "Please select proper date time", context);	
	}else if(!(mtime.get(1).equals(null)||!mtime.get(1).equals(""))&&(mdate.get(1).equals(null)||mdate.get(1).equals(""))){
		Custom_Dialog.dialogCode(2, null, "Please select proper date time", context);	
	}else if((mtime.get(2).equals(null)||mtime.get(2).equals(""))&&(!mdate.get(2).equals(null)||!mdate.get(2).equals(""))){
		Custom_Dialog.dialogCode(2, null, "Please select proper date time", context);	
	}else if(!(mtime.get(2).equals(null)||!mtime.get(2).equals(""))&&(mdate.get(2).equals(null)||mdate.get(2).equals(""))){
		Custom_Dialog.dialogCode(2, null, "Please select proper date time", context);	
	}else{*/
	
	//}

	break;
	
		default:
			break;
		}
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
//				userid="55";
//				orderid="66";
				Log.e("userid", " "+userid);
				Log.e("orderid", ""+orderid);
				Log.e("data", " "+data);
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
	@SuppressWarnings("static-access")
	public void DatePickerdialogCode(final TextView tvDate, final int i) {
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
		ImageView ivClose = (ImageView) datepicker_dialog
		.findViewById(R.id.ivClose);
		datepicker = (DatePicker) datepicker_dialog
				.findViewById(R.id.datepicker);
		// / Set current date on view
		String temp_date="";
		temp_date=tvDate.getHint().toString();
		
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
				day = datepicker.getDayOfMonth();
				month = datepicker.getMonth();
				year = datepicker.getYear();
				int m = month+1;
				if(i==1){
					mdate.set(0, day + "/" + m + "/" + year);
				}else if(i==2){
					mdate.set(1, day + "/" + m + "/" + year);
				}else if(i==3){
					mdate.set(2, day + "/" + m + "/" + year);
				}
				datepicker_dialog.dismiss();
				
			String formatedDate=	formatDate(year, month, day);
				tvDate.setText(formatedDate);
			}
		});
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				if(i==1){
					mdate.set(0, "");
				}else if(i==2){
					mdate.set(1, "");
				}else if(i==3){
					mdate.set(2, "");
				}
				datepicker_dialog.dismiss();
				tvDate.setText("");
				tvDate.setHint("Date");
			}
		});

		datepicker_dialog.show();
	}

	@SuppressWarnings("static-access")
	public void TimePickerdialogCode(final TextView tvTime, final int i) {
		// /////////// /// // / // // / /
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
		ImageView ivClose = (ImageView) time_picker_dialog
		.findViewById(R.id.ivClose);
		timepicker = (TimePicker) time_picker_dialog
				.findViewById(R.id.Timepicker);
		// / Set current date on view

		String temp_time="";
		temp_time=tvTime.getHint().toString();
		if(temp_time.equals("Time")){
		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR);
		minute = c.get(Calendar.MINUTE);
		}
	
		timepicker.setCurrentHour(hour);
		timepicker.setCurrentMinute(minute);

		// Set current date on view
ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				time_picker_dialog.dismiss();
				if(i==11){
					mtime.set(0, "");
				}else if(i==22){
					mtime.set(1, "");
				}else if(i==33){
					mtime.set(2, "");
				}
				tvTime.setHint("Time");
				tvTime.setText("");
				
			}
		});
		ivCont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				
				
				time_picker_dialog.dismiss();

				hour = timepicker.getCurrentHour();
				minute = timepicker.getCurrentMinute();
				
				
			    String mytime="";
				if(hour>12){
					
					int h = hour-12;  
					 mytime=h + ":" + minute+ " PM";
				}else{
					 mytime=hour + ":" + minute+ " AM";
				}
				tvTime.setText(mytime);
				if(i==11){
					mtime.set(0, mytime);
				}else if(i==22){
					mtime.set(1, mytime);
				}else if(i==33){
					mtime.set(2, mytime);
				}
			}
		});

		time_picker_dialog.show();
	}

	protected boolean readyToGo() {
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(MeetupRequestdialogActivity.this);

		if (status == ConnectionResult.SUCCESS) {
			return (true);
		} else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
			ErrorDialogFragment.newInstance(status).show(
					MeetupRequestdialogActivity.this.getSupportFragmentManager(),
					TAG_ERROR_DIALOG_FRAGMENT);
		} else {
			AlertDialog dialog = new AlertDialog.Builder(MeetupRequestdialogActivity.this)
					.create();
			dialog.setMessage("install_latest_google_map");
			dialog.setButton(AlertDialog.BUTTON_POSITIVE,
					"Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							MeetupRequestdialogActivity.this.finish();
						}
					});
			dialog.show();
		}
		return (false);
	}

	

	public static class ErrorDialogFragment extends DialogFragment {
		static final String ARG_STATUS = "status";

		static ErrorDialogFragment newInstance(int status) {
			Bundle args = new Bundle();

			args.putInt(ARG_STATUS, status);

			ErrorDialogFragment result = new ErrorDialogFragment();

			result.setArguments(args);

			return (result);
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Bundle args = getArguments();

			return GooglePlayServicesUtil.getErrorDialog(
					args.getInt(ARG_STATUS), getActivity(), 0);
		}

		@Override
		public void onDismiss(DialogInterface dlg) {
			if (getActivity() != null) {
				getActivity().finish();
			}
		}
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.ftMap)).getMap();

			switch (maptype) {
			case 0:
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			case 1:
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case 2:
				mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			}
		/*	
			lat = 23.026529;
			log = 72.5713277;*/

			LatLng latLng = new LatLng(lat, log);
			mMap.addMarker(new MarkerOptions()
					.position(new LatLng(lat, log))
					.title("current_location")
					.snippet(Locale.getDefault().toString())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_action_location)));
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(latLng) // Sets the center of the map to
					// Mountain View
					.zoom(14) // Sets the zoom
					.tilt(30) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder

			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

		}
	}

	@SuppressLint("SimpleDateFormat") private static String formatDate(int year, int month, int day) {

	    Calendar cal = Calendar.getInstance();
	    cal.clear();
	    cal.set(year, month, day);
	    Date date = cal.getTime();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");

	    return sdf.format(date);
	}
}
