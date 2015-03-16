package com.convertingoffers.tapnsell.MakeOffer;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.Shop.checkout.MainCheckOutFragmentActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.CustomTextView;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class Exp_buy_OfferActivity extends BaseActivity implements OnClickListener{
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	String address="N";
	TextView tvExpireTime,tvPrice;
	ImageView ivIncrease,ivBuy,ivItemImage,ivClose;
	CustomTextView tvItemName;
	String itemid="",userid="", amount="",image="",name="",offerid="",accept=""
		,noti_msg="",type = "", price="",to_id="",isbuyer="",dialog_msg="";
	Context context;
	boolean StatusofTimer=false;
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	Intent i_Buy;
	
/////////////////
	public boolean active = false,boolTimerSet=false,blink;
	long seconds;
	private CountDownTimer countDownTimer;
//	, countDownTimerValiNext,countDownTimerSenResult;
	private long totalTimeCountInMilliseconds;
	long MainTimeInMilliSecond, CurrentTimeInMilliSecond,
	finalTimeInMilliSecond;
	private long timeBlinkInMilliseconds;
/////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.makeoffer_offer_exp);
	
	
	Bundle bundle = getIntent().getExtras();
	if(bundle!=null){
		
		noti_msg = bundle.getString("notification_text");
		type = bundle.getString("notification_type");
		offerid=bundle.getString("notification_offerid");
		price = bundle.getString("notification_price");
		name = bundle.getString("notification_itemname");
		to_id = bundle.getString("notification_fromid");
		price = bundle.getString("notification_price");
		itemid = bundle.getString("notification_itemid");
		image = bundle.getString("notification_itemimage");
		isbuyer = bundle.getString("notification_isbuyer");
	}
	userid = pref.getString("UserID", "");
		Log.e("noti_msg", " "+noti_msg);
		Log.e("type", " "+type);
		Log.e("offerid", " "+offerid);
		Log.e("price", " "+price);
		Log.e("name", " "+name);
		Log.e("to_id", " "+to_id);
		Log.e("itemid", " "+itemid);
		Log.e("image", " "+image);
		Log.e("isbuyer", " "+isbuyer);
		

	
	
	// Image display using lazy loading 

	iLoader_item.displayImage(image, ivItemImage, options, new SimpleImageLoadingListener() {
		@Override
		public void onLoadingStarted(String imageUri, View view) {
			//holder.pbimage.setProgress(0);
//			holder.pbimage.setIndeterminate(true);
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
//			holder.pbimage.setIndeterminate(true);
		}
	});
	
	// Image display using lazy loading
	
	
	tvItemName.setText(name);
	tvPrice.setText(price);
	ivClose.setOnClickListener(this);
	ivIncrease.setOnClickListener(this);
//	totalTimeCountInMilliseconds = 300000;
	totalTimeCountInMilliseconds = 900000;
	timeBlinkInMilliseconds = 30000;
	
	startTimer();
	
	}

	
	private void startTimer() {
		countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
		
			@SuppressLint("DefaultLocale")
			@Override
			public void onTick(long leftTimeInMilliseconds) {
				
				long seconds = leftTimeInMilliseconds / 1000;

				if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
					tvExpireTime.setTextAppearance(getApplicationContext(),
							R.style.blinkText);
					tvExpireTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							getResources().getDimension(R.dimen.twenty));
					if (blink) {
						tvExpireTime.setVisibility(View.VISIBLE);
					} else {
						tvExpireTime.setVisibility(View.INVISIBLE);
					}

					blink = !blink; 
				}

				tvExpireTime.setText(String.format("%02d", seconds / 60) + ":"
						+ String.format("%02d", seconds % 60));
			
			}

			@Override
			public void onFinish() {
				
				if(tvExpireTime!=null){
					tvExpireTime.setText("00:00");	
				}
				cancelContdowwnTimer();
				tvExpireTime.setVisibility(View.VISIBLE);
				StatusofTimer=true;
			
			}
		}.start();
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		if (countDownTimer != null) {
			countDownTimer.cancel();
		}
	}
	public void cancelContdowwnTimer() {
		

		if (active) {
			if (countDownTimer != null) {
				countDownTimer.cancel();
			}
		} else {
		}
	}
	@Override
	public void onStop() {
		super.onStop();
		active = false;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		active = true;
	}
	@Override
	public void onContentChanged() {
		super.onContentChanged();

		context=this;
		iLoader_item.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.counter_image_bg)
		.showImageForEmptyUri(R.drawable.counter_image_bg)
		.showImageOnFail(R.drawable.counter_image_bg)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		
		ivClose=(ImageView)findViewById(R.id.ivClose);
		ivItemImage=(ImageView)findViewById(R.id.ivItemImage);
		ivBuy=(ImageView)findViewById(R.id.ivBuy);
		ivIncrease=(ImageView)findViewById(R.id.ivIncrease);
		tvExpireTime=(TextView)findViewById(R.id.tvExpireTime);
		tvPrice=(TextView)findViewById(R.id.tvPrice);
		tvItemName=(CustomTextView)findViewById(R.id.tvItemName);
		
	}


	@Override
	public void onClick(View v) {
		
		Intent intent;
		switch (v.getId()) {
		
		case R.id.ivClose:
			finish();
			break;
			
		case R.id.ivIncrease:
			if(!StatusofTimer){
			intent = new Intent(context, MakeOfferActivity.class);
			intent.putExtra("moffer_itemid",itemid);
			intent.putExtra("moffer_image", image);
			intent.putExtra("moffer_name", name);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			startActivity(intent);
			}else{
				ToastMessageDisplay("Your offer is already expire.");
			}
			break;
			
		case R.id.ivBuy:
			if(!StatusofTimer){
				BuybtnCode();
			}else{
				ToastMessageDisplay("Your offer is already expire.");
			}
			break;
		default:
			break;
		}
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) public void BuybtnCode() {

		mItem_IdList.clear();
		mItem_IdList.add(itemid);
		
		editor.putString("CHKImage", image);
		editor.putString("CHKItemid",itemid);
		editor.putString("CHKName", name);
		editor.putString("CHKPrice",price);
		editor.commit();
		
		i_Buy = new Intent(context,MainCheckOutFragmentActivity.class);
		i_Buy.putExtra("itemid", itemid);
		i_Buy.putExtra("from_id", to_id);
		i_Buy.putExtra("Distance","0");
		i_Buy.putExtra("ItemArray", mItem_IdList);
		i_Buy.putExtra("position", "0");
		i_Buy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i_Buy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		
		if (cd.checkConnection()) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				new LockItemBackTask().executeOnExecutor(
						AsyncTask.THREAD_POOL_EXECUTOR, "");
			} else {
				new LockItemBackTask().execute("");
			}
		} else {
			Toast.makeText(context, "Interner connection is not available!",
					Toast.LENGTH_LONG).show();
		}
		
	
		
		
		
	}


	private class LockItemBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message="";

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
				
				
				JSONObject json = userFunction.ReserveFunction(userid,itemid, "Y");
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
				finish();
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else {
				Custom_Dialog.dialogCode(2, null, message, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

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

	public void ToastMessageDisplay(String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		}
	

}
