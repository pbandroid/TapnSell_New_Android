package com.convertingoffers.tapnsell.Meetup;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.convertingoffers.tapnsell.HomeActivity;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Shop.MapStoreActivity;
import com.convertingoffers.tapnsell.TapboardFooter.Footer_ReviewUser;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.RoundedImageView_CenterCrop;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ChouseMeetupActivity extends BaseActivity implements OnClickListener{

	ArrayList<String> mdate =new ArrayList<String>(3);
	ArrayList<String> mtime =new ArrayList<String>(3);
	  DisplayImageOptions options,option_user_image;
	 ImageLoader iLoader_Rounded = ImageLoader.getInstance();
	ImageLoader iLoader_item = ImageLoader.getInstance();
	Animation RightSwipe;
	ImageView ivProductImage,ivUser,ivLocationIcon,ivch3,ivch2,ivch1,ivAccept,ivSuggest,ivd1,ivd2,ivd3,ivt1,ivt2,ivt3;
	TextView tvName,tvPrice,tvUserName,tvLocationName,tvAddress,tvDistance,tvDate1,tvDate2,tvDate3,tvTime1,tvTime2,tvTime3;
	LinearLayout lldate1,lldate2,lldate3;
	RelativeLayout rldate1,rldate2,rldate3,rltime1,rltime2,rltime3,rlTop,rlMap;
	
	boolean boolch1=false,boolch2=false,boolch3=false, dialog_meetup_boolch1=false,dialog_meetup_boolch2=false,dialog_meetup_boolch3=false;
	Context context;
	String data,userid,orderid, itemid,name,price,image,meetupplace,meetupaddress,latitude,longitude,isbuyer,toid,username,userimage;
	ArrayList<String> arrayDate= new ArrayList<String>();
	ArrayList<String> arrayTime= new ArrayList<String>();
	ArrayList<String> arrayid= new ArrayList<String>();
	Bitmap image_bitmap=null;
	String meetupid="0";
	int year=0, month=0, day=0, hour=0, minute=0, second;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.change_meetup_buyer);
	LoadImage();
	userid = pref.getString("UserID", "");
	Bundle bundle = getIntent().getExtras();
	if(bundle!=null){
		orderid= bundle.getString("orderid");
	}
	Log.e("orderid bundle",""+orderid);
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
	
	option_user_image = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.list_item_image_frame)
	.showImageForEmptyUri(R.drawable.list_item_image_frame)
	.showImageOnFail(R.drawable.list_item_image_frame)
	.resetViewBeforeLoading(true)
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.considerExifParams(true)
	.build();
	
	mdate.add("");
	mdate.add("");
	mdate.add("");
	mtime.add("");
	mtime.add("");
	mtime.add("");
	ivch1.setOnClickListener(this);
	ivch2.setOnClickListener(this);
	ivch3.setOnClickListener(this);
	ivAccept.setOnClickListener(this);
	ivSuggest.setOnClickListener(this);
	rlTop.setOnClickListener(this);
	ivBack.setOnClickListener(this);
	ivMenu.setOnClickListener(this);
	rlMap.setOnClickListener(this);
	tvHeader.setText("Choose Meetup");
	ivMenu.setVisibility(View.VISIBLE);
	if(cd.checkConnection()){
		new GetMeetupDetailsBackTask().execute("");
	}else{
		Toast.makeText(context, "Interner connection is not available!",Toast.LENGTH_LONG).show();
	}
	
	
	
	}
	
	@Override
	public void onContentChanged() {
	super.onContentChanged();
	
	context=this;
	ivProductImage=(ImageView)findViewById(R.id.ivProductImage);
	ivUser=(ImageView)findViewById(R.id.ivUser);
	ivLocationIcon=(ImageView)findViewById(R.id.ivLocationIcon);
	ivAccept=(ImageView)findViewById(R.id.ivAccept);
	ivSuggest=(ImageView)findViewById(R.id.ivSuggest);
	rlTop=(RelativeLayout)findViewById(R.id.rlTop);
	ivt3=(ImageView)findViewById(R.id.ivt3);
	tvName=(TextView)findViewById(R.id.tvName);
	tvPrice=(TextView)findViewById(R.id.tvPrice);
	tvUserName=(TextView)findViewById(R.id.tvUserName);
	tvLocationName=(TextView)findViewById(R.id.tvLocationName);
	tvAddress=(TextView)findViewById(R.id.tvAddress);
	tvDistance=(TextView)findViewById(R.id.tvDistance);
	tvDate1=(TextView)findViewById(R.id.tvDate1);
	tvDate2=(TextView)findViewById(R.id.tvDate2);
	tvDate3=(TextView)findViewById(R.id.tvDate3);
	tvTime1=(TextView)findViewById(R.id.tvTime1);
	tvTime2=(TextView)findViewById(R.id.tvTime2);
	tvTime3=(TextView)findViewById(R.id.tvTime3);
	lldate1=(LinearLayout)findViewById(R.id.ll3);
	lldate2=(LinearLayout)findViewById(R.id.ll4);
	lldate3=(LinearLayout)findViewById(R.id.ll5);
	ivch3=(ImageView)findViewById(R.id.ivch3);
	ivch2=(ImageView)findViewById(R.id.ivch2);
	ivch1=(ImageView)findViewById(R.id.ivch1);
	ivd1=(ImageView)findViewById(R.id.ivd1);
	ivd2=(ImageView)findViewById(R.id.ivd2);
	ivd3=(ImageView)findViewById(R.id.ivd3);
	ivt1=(ImageView)findViewById(R.id.ivt1);
	ivt2=(ImageView)findViewById(R.id.ivt2);
	 rldate1=(RelativeLayout)findViewById(R.id.rlDate1);
	 rldate2=(RelativeLayout)findViewById(R.id.rlDate2);
	 rldate3=(RelativeLayout)findViewById(R.id.rlDate3);
	 rltime1=(RelativeLayout)findViewById(R.id.rlTime1);
	 rltime2=(RelativeLayout)findViewById(R.id.rlTime2);
	 rltime3=(RelativeLayout)findViewById(R.id.rlTime3);
	 rlMap=(RelativeLayout)findViewById(R.id.rlMap);
	}

	private void LoadImage() {
		
		 image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.meetup_list_user_image);
	}
	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.ivch1:
			boolch3=false;
			boolch2=false;
			if(boolch1){boolch1=false;}else{boolch1=true;}
			OnclickChangeBackGroundImage(rldate1,rltime1,lldate1,ivch1,boolch1,tvDate1,tvTime1,ivd1,ivt1);
			OnclickChangeBackGroundImage(rldate2,rltime2,lldate2,ivch2,false,tvDate2,tvTime2,ivd2,ivt2);
			OnclickChangeBackGroundImage(rldate3,rltime3,lldate3,ivch3,false,tvDate3,tvTime3,ivd3,ivt3);
			
			
			break;
		case R.id.ivMenu:
			buttonAnimation(ivMenu);
		 	i = new Intent(context, TapBoardActivity.class);
			startActivity(i);	
			break;
			
		case R.id.ivch2:
			boolch1=false;
			boolch3=false;
			if(boolch2){boolch2=false;}else{boolch2=true;}
			OnclickChangeBackGroundImage(rldate2,rltime2,lldate2,ivch2,boolch2,tvDate2,tvTime2,ivd2,ivt2);
			OnclickChangeBackGroundImage(rldate1,rltime1,lldate1,ivch1,false,tvDate1,tvTime1,ivd1,ivt1);
			OnclickChangeBackGroundImage(rldate3,rltime3,lldate3,ivch3,false,tvDate3,tvTime3,ivd3,ivt3);
			
			break;
		case R.id.ivch3:
			boolch1=false;
			boolch2=false;
			if(boolch3){boolch3=false;}else{boolch3=true;}
			OnclickChangeBackGroundImage(rldate3,rltime3,lldate3,ivch3,boolch3,tvDate3,tvTime3,ivd3,ivt3);
			OnclickChangeBackGroundImage(rldate1,rltime1,lldate1,ivch1,false,tvDate1,tvTime1,ivd1,ivt1);
			OnclickChangeBackGroundImage(rldate2,rltime2,lldate2,ivch2,false,tvDate2,tvTime2,ivd2,ivt2);
			
			break;
		case R.id.ivAccept:

			if(!boolch3 && !boolch1 && !boolch2){
				Custom_Dialog.dialogCode(1, null,"Please select meeetup date.",context);
			}else{
				if(boolch1){
					meetupid=arrayid.get(0);
				}else if(boolch2){
					meetupid=arrayid.get(1);
				}else if(boolch3){
					meetupid=arrayid.get(2);
				}
				
				if(cd.checkConnection()){
					new AcceptMeetupBackTask().execute("");
				}else{
					Toast.makeText(context, "Interner connection is not available!",Toast.LENGTH_LONG).show();
				}
			}
			
			break;
		case R.id.ivSuggest:
			ChangeMeetupdialogCode();
			break;
		case R.id.ivBack:
			finish();
			break;
		case R.id.rlTop:
			 i = new Intent(context, Footer_ReviewUser.class);
			startActivity(i);
			
			break;
		case R.id.rlMap:
			 i = new Intent(context, MapStoreActivity.class);
			 i.putExtra("lat", latitude);
			 i.putExtra("log", longitude);
			startActivity(i);
			
			break;
			
		default:
			break;
		}
	}

	

	
	private class GetMeetupDetailsBackTask extends AsyncTask<String, Void, String> {
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
				
				/*userid="55";
				orderid="66";*/
				Log.e("userid", " "+userid);
				Log.e("orderid", " "+orderid);
				JSONObject json = userFunction.GetMeetupDetailsFunction(userid, orderid);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							
								isbuyer=json.optString("isbuyer");
							JSONObject jItem = json.getJSONObject("item");
							itemid= jItem.optString("itemid");
							name= jItem.optString("name");
							price= jItem.optString("price");
							image=UserFunctions.localImageUrl;
							image= image+jItem.optString("image");
							meetupplace= jItem.optString("meetupplace");
							meetupaddress= jItem.optString("meetupaddress");
							latitude= jItem.optString("latitude");
							longitude= jItem.optString("longitude");
							JSONObject juser = json.getJSONObject("user");
							toid= juser.optString("userid");
							username= juser.optString("username");
							userimage=UserFunctions.localImageUrl;
							userimage= userimage+juser.optString("userimage");
							JSONArray meetup= json.getJSONArray("meetup");
							
							
							arrayDate.clear();
							arrayTime.clear();
							arrayid.clear();
							
							for (int i = 0; i < meetup.length(); i++) {
								JSONObject obj = meetup.getJSONObject(i);
								arrayid.add(obj.optString("id"));
								arrayDate.add(obj.optString("date"));
								arrayTime.add(obj.optString("time"));
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

				tvPrice.setText(price);
				tvName.setText(name);
				tvName.setTag(itemid);
				tvUserName.setText(username);
				tvLocationName.setText(meetupplace);
				tvAddress.setText(meetupaddress);
				tvLocationName.setTag(latitude);
				tvAddress.setTag(longitude);
				tvDistance.setText("");
				
				SetDateAndTime();
				
				if(userimage.length()>0){
					iLoader_Rounded.displayImage(userimage, ivUser, options, loadImageListener);
				}else{
					iLoader_Rounded.displayImage("", ivUser, options, loadImageListener);
				}	
				
				if(meetupplace!=null&&meetupplace.equals("")&&meetupplace.equals("Home")){
					ivLocationIcon.setImageDrawable(getResources().getDrawable(R.drawable.meetup_request_home));
				}else{
					ivLocationIcon.setImageDrawable(getResources().getDrawable(R.drawable.change_meetup_location_logo));
		
				}
			
				
				/*
				if (image != null) {
					try {
						imageLoader.DisplayImage(image, ivProductImage);
					} catch (Exception e) {
					}
				} else {
					ivProductImage.setImageResource(R.drawable.list_item_image_frame);
				}*/
				
				
				// Image display using lazy loading 

				iLoader_item.displayImage(image, ivProductImage, option_user_image, new SimpleImageLoadingListener() {
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
						//holder.pbimage.setProgress(Math.round(100.0f * current / total));
//						holder.pbimage.setIndeterminate(true);
					}
				});
				
				// Image display using lazy loading
				
				
				
				
				
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else {
				Custom_Dialog.dialogCode(1, null,message,context);
			}
	}

		private void SetDateAndTime() {
			
			for (int i = 0; i < arrayDate.size(); i++) {
				if(arrayDate.size()==1){
					
					lldate1.setVisibility(View.VISIBLE);
					lldate2.setVisibility(View.GONE);
					lldate3.setVisibility(View.GONE);
					
					tvDate1.setText(arrayDate.get(0));
					tvTime1.setText(arrayTime.get(0));
					tvDate1.setTag(arrayid.get(0));
				}else if(arrayTime.size()==2){
					
					lldate1.setVisibility(View.VISIBLE);
					lldate2.setVisibility(View.VISIBLE);
					lldate3.setVisibility(View.GONE);
					
					tvDate1.setText(arrayDate.get(0));
					tvTime1.setText(arrayTime.get(0));
					tvDate1.setTag(arrayid.get(0));
					tvDate2.setText(arrayDate.get(1));
					tvTime2.setText(arrayTime.get(1));
					tvDate2.setTag(arrayid.get(1));
					
				}else{
					lldate1.setVisibility(View.VISIBLE);
					lldate2.setVisibility(View.VISIBLE);
					lldate3.setVisibility(View.VISIBLE);
					
					tvDate1.setText(arrayDate.get(0));
					tvTime1.setText(arrayTime.get(0));
					tvDate1.setTag(arrayid.get(0));
					tvDate2.setText(arrayDate.get(1));
					tvTime2.setText(arrayTime.get(1));
					tvDate2.setTag(arrayid.get(1));

					tvDate3.setText(arrayDate.get(2));
					tvTime3.setText(arrayTime.get(2));
					tvDate3.setTag(arrayid.get(2));
					
				}
			}
			
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	
	private class AcceptMeetupBackTask extends AsyncTask<String, Void, String> {
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
				
//				userid="55";
				Log.e("userid", " "+userid);
				Log.e("meetupid", " "+meetupid);
				JSONObject json = userFunction.AcceptMeetupFunction(userid, meetupid);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							message=json.optString("message");
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

		@SuppressLint("InlinedApi") @Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {
				Intent i = new Intent(context, HomeActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				Custom_Dialog.dialogCode(3, i, message, context);
			} else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(1, null,message,context);
			}
	}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

ImageLoadingListener loadImageListener = new ImageLoadingListener() {
		
		@Override
		public void onLoadingStarted(String imageUri, View view) {
			((ImageView) view).setImageBitmap(image_bitmap);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//			Log.i(getClass().getSimpleName(), "Image Loading failed" + imageUri + " REason :" + failReason);
			((ImageView) view).setImageBitmap(image_bitmap);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			Log.i(getClass().getSimpleName(), "Image Loading " + imageUri);
			if (loadedImage != null) {
				
// 		It will Round Image
				loadedImage = RoundedImageView_CenterCrop.scaleCenterCrop(loadedImage, (int) getResources().getDimension(R.dimen.ninetydp), false);
				 
//		If You want only Rounded Corner Just give last argument as true
//				loadedImage = RoundedImageView.scaleCenterCrop(loadedImage,	(int) getResources().getDimension(R.dimen.thirty), true);
				
				((ImageView) view).setImageBitmap(loadedImage);
			} else {

				((ImageView) view).setImageBitmap(image_bitmap);
			}
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			
			
		}
	};
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") private void OnclickChangeBackGroundImage
	(RelativeLayout rldate12,
			RelativeLayout rlTime12, LinearLayout lldate12, ImageView ivch12, boolean boolch12,
			TextView tvDate12, TextView tvTime12, ImageView ivd12, ImageView ivt12) {
		
		
		if(boolch12){
			
			tvDate12.setTextColor(Color.parseColor("#ffffff"));
			tvTime12.setTextColor(Color.parseColor("#ffffff"));
			
			ivd12.setImageDrawable(getResources().getDrawable(R.drawable.change_meetup_date_icon_select));
			ivt12.setImageDrawable(getResources().getDrawable(R.drawable.change_meetup_time_icon_select));
			
			
			if (Build.VERSION.SDK_INT >= 16)
				rldate12.setBackground(context.getResources().getDrawable(R.drawable.change_meetup_date_select_bg));
			else
				rldate12.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.change_meetup_date_select_bg));
		
			if (Build.VERSION.SDK_INT >= 16)
				rlTime12.setBackground(context.getResources().getDrawable(R.drawable.change_meetup_time_select_bg));
			else
				rlTime12.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.change_meetup_time_select_bg));
			
			if (Build.VERSION.SDK_INT >= 16)
				lldate12.setBackground(context.getResources().getDrawable(R.drawable.change_meetup_blue_bg));
			else
				lldate12.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.change_meetup_blue_bg));

			ivch12.setImageDrawable(getResources().getDrawable(R.drawable.change_meetup_select_icon));
		
		}else{
			
			
			tvDate12.setTextColor(Color.parseColor("#000000"));
			tvTime12.setTextColor(Color.parseColor("#000000"));
		
			ivd12.setImageDrawable(getResources().getDrawable(R.drawable.change_meetup_date_icon));
			ivt12.setImageDrawable(getResources().getDrawable(R.drawable.change_meetup_time_icon));
			
			
			
			if (Build.VERSION.SDK_INT >= 16)
				rldate12.setBackground(context.getResources().getDrawable(R.drawable.change_meetup_date_bg));
			else
				rldate12.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.change_meetup_date_bg));
		
			
			if (Build.VERSION.SDK_INT >= 16)
				rlTime12.setBackground(context.getResources().getDrawable(R.drawable.change_meetup_time_bg));
			else
				rlTime12.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.change_meetup_time_bg));
			
			if (Build.VERSION.SDK_INT >= 16)
				lldate12.setBackground(context.getResources().getDrawable(R.drawable.change_meetup_date_and_time_unselect_bg));
			else
				lldate12.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.change_meetup_date_and_time_unselect_bg));

			ivch12.setImageDrawable(getResources().getDrawable(R.drawable.change_meetup_unselect_icon));
		
		}
	}
	@SuppressWarnings("static-access")
	public void ChangeMeetupdialogCode() {
		// /
		
		final Dialog dialog_meetup = new Dialog(context);
		dialog_meetup.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog_meetup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_meetup.setCancelable(false);
		dialog_meetup.setContentView(R.layout.dialog_request_change);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog_meetup.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog_meetup.getWindow().setAttributes(lp);
		 
	
		final TextView dialog_meetup_tvDate1 = (TextView) dialog_meetup.findViewById(R.id.tvDate1);
		final TextView dialog_meetup_tvDate2 = (TextView) dialog_meetup.findViewById(R.id.tvDate2);
		final TextView dialog_meetup_tvDate3 = (TextView) dialog_meetup.findViewById(R.id.tvDate3);
		final TextView dialog_meetup_tvTime1 = (TextView) dialog_meetup.findViewById(R.id.tvTime1);
		final TextView dialog_meetup_tvTime2 = (TextView) dialog_meetup.findViewById(R.id.tvTime2);
		final TextView dialog_meetup_tvTime3 = (TextView) dialog_meetup.findViewById(R.id.tvTime3);
		final ImageView dialog_ivRequestChange=(ImageView)dialog_meetup.findViewById(R.id.ivRequestChange);
		final ImageView dialog_ivClose=(ImageView)dialog_meetup.findViewById(R.id.ivClose);
		
		final RelativeLayout	 dialog_meetup_rldate1=(RelativeLayout)dialog_meetup.findViewById(R.id.rlDate1);
		final RelativeLayout dialog_meetup_rldate2=(RelativeLayout)dialog_meetup.findViewById(R.id.rlDate2);
		final RelativeLayout dialog_meetup_rldate3=(RelativeLayout)dialog_meetup.findViewById(R.id.rlDate3);
		final RelativeLayout dialog_meetup_rltime1=(RelativeLayout)dialog_meetup.findViewById(R.id.rlTime1);
		final RelativeLayout dialog_meetup_rltime2=(RelativeLayout)dialog_meetup.findViewById(R.id.rlTime2);
		final RelativeLayout dialog_meetup_rltime3=(RelativeLayout)dialog_meetup.findViewById(R.id.rlTime3);
		 
	
		
		
	
		dialog_meetup_rldate1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonAnimation(v);
				DatePickerdialogCode(dialog_meetup_tvDate1,1);
			}
		});
		

		dialog_meetup_rldate2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				buttonAnimation(v);
				DatePickerdialogCode(dialog_meetup_tvDate2,2);
			}
		});
		dialog_meetup_rldate3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonAnimation(v);
				DatePickerdialogCode(dialog_meetup_tvDate3,3);
			}
		});
		
		dialog_meetup_rltime1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonAnimation(v);
			
				TimePickerdialogCode(dialog_meetup_tvTime1,11);
			}
		});
		dialog_meetup_rltime2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				buttonAnimation(v);
				TimePickerdialogCode(dialog_meetup_tvTime2,22);
			}
		});

		dialog_meetup_rltime3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				buttonAnimation(v);
				TimePickerdialogCode(dialog_meetup_tvTime3,33);
			}
		});
		dialog_ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dialog_meetup.dismiss();
			}
		});
		
		dialog_ivRequestChange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				buttonAnimation(v);
			/*	
				JSONArray dataArray = new JSONArray();
				try {
					JSONObject obj = new JSONObject();
					for (int i = 0; i < 3; i++) {
						 
						obj.put("place", meetupplace);
						obj.put("address", meetupaddress);
						obj.put("lat",latitude);
						obj.put("lng", longitude);
						if (i == 0) {
							obj.put("date", dialog_meetup_tvDate1.getText().toString());
							obj.put("time", dialog_meetup_tvTime1.getText().toString());
						} else if (i == 1) {
							obj.put("date", dialog_meetup_tvDate2.getText().toString());
							obj.put("time", dialog_meetup_tvTime2.getText().toString());

						} else if (i == 2) {
							obj.put("date", dialog_meetup_tvDate3.getText().toString());
							obj.put("time", dialog_meetup_tvTime3.getText().toString());

						}
						dataArray.put(obj);
						data = dataArray.toString();
						Log.e("Store", " " + data);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				
				
				JSONArray dataArray = new JSONArray();
				try {
					JSONObject obj;
					for (int i = 0; i < 3; i++) {
						obj =new JSONObject() ;
						
						if (i == 0) {
							if(!(mdate.get(0).equals(null)||mdate.get(0).equals(""))&&!(mtime.get(0).equals(null)||mtime.get(0).equals(""))){
								obj.put("place", meetupplace);
								obj.put("address", meetupaddress);
								obj.put("lat", latitude);
								obj.put("lng",longitude);
								obj.put("date", mdate.get(0).toString());
								obj.put("time", mtime.get(0).toString());	
								dataArray.put(obj);
							}
							
						} else if (i == 1) {
							if(!(mdate.get(1).equals(null)||mdate.get(1).equals(""))&&!(mtime.get(1).equals(null)||mtime.get(1).equals(""))){
								
								obj.put("place", meetupplace);
								obj.put("address", meetupaddress);
								obj.put("lat", latitude);
								obj.put("lng",longitude);
							obj.put("date", mdate.get(1).toString());
							obj.put("time", mtime.get(1).toString());
							dataArray.put(obj);

							}
						} else if (i == 2) {
							if(!(mdate.get(2).equals(null)||mdate.get(2).equals(""))&&!(mtime.get(2).equals(null)||mtime.get(2).equals(""))){
								
								obj.put("place", meetupplace);
								obj.put("address", meetupaddress);
								obj.put("lat", latitude);
								obj.put("lng",longitude);
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

				if((mdate.get(0).equals(null)||mdate.get(0).equals(""))&&(mdate.get(1).equals(null)||mdate.get(1).equals(""))&&(mdate.get(2).equals(null)||mdate.get(2).equals(""))){
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
				
				dialog_meetup.dismiss();
			}
		});
		/**/
		if (!dialog_meetup.isShowing()) {
			dialog_meetup.show();
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

	
	
/*	
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
		ImageView ivClose = (ImageView) datepicker_dialog
		.findViewById(R.id.ivClose);
		datepicker = (DatePicker) datepicker_dialog
				.findViewById(R.id.datepicker);
		// / Set current date on view

		String temp_date="";
		temp_date=tvDate.getText().toString();
		if(temp_date.length()==0){
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);	
		}
		
		tvDate.setText(new StringBuilder().append(day).append("/")
				.append(month+1).append("/").append(year).append(" "));
		datepicker.init(year, month, day, null);
		// Set current date on view

		ivCont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				datepicker_dialog.dismiss();
				day = datepicker.getDayOfMonth();
				month = datepicker.getMonth()+1;
				year = datepicker.getYear();

				tvDate.setText(day + "/" + month + "/" + year);
			}
		});
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				datepicker_dialog.dismiss();
				
				tvDate.setText("Date");
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
		ImageView ivClose = (ImageView) time_picker_dialog
		.findViewById(R.id.ivClose);
		timepicker = (TimePicker) time_picker_dialog
				.findViewById(R.id.Timepicker);
		// / Set current date on view
		String temp_time="";
		temp_time=tvTime.getText().toString();
		final Calendar c = Calendar.getInstance();
		if (temp_time.length() == 0) {
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

				tvTime.setText("Time");
			}
		});
		ivCont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				time_picker_dialog.dismiss();

				hour = timepicker.getCurrentHour();
				minute = timepicker.getCurrentMinute();

				tvTime.setText(hour + ":" + minute);
			}
		});

		time_picker_dialog.show();
	}

	*/

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
//				userid = "55";
//				orderid = "67";
				Log.e("userid", " "+userid);
				Log.e("orderid", " "+orderid);
				Log.e("data", " "+data);
				
				try {
					JSONObject json = userFunction.MeetupRequestFunction(
							userid, orderid, data);

					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);

						if (res.equals(KEY_SUCCESS_STATUS)) {

							errorMessage = "true";
						} else {
							errorMessage = "false";

						}
					} else {
						errorMessage = "error in posting";
					}

					message = json.optString("message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return errorMessage;
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {
				Intent i = new Intent(context, HomeActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);
				Custom_Dialog.dialogCode(3, i, message, context);
			} else {
				Custom_Dialog.dialogCode(2, null, message, context);
			}

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