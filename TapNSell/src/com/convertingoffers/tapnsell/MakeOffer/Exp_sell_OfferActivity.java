package com.convertingoffers.tapnsell.MakeOffer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import com.convertingoffers.tapnsell.HomeActivity;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.CustomTextView;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class Exp_sell_OfferActivity extends BaseActivity implements OnClickListener{
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	TextView tvExpireTime,tvPrice;
	ImageView ivCounter,ivAccept,ivItemImage,ivClose;
	CustomTextView tvItemName;
	String itemid="",userid="", amount="",image="",name="",offerid="",accept=""
		,noti_msg="",type = "", price="",to_id="",isbuyer="",dialog_msg="";
	Context context;
	boolean StatusofTimer=false;
/////////////////
	public boolean active = false,boolTimerSet=false,blink;
	long seconds;
	private CountDownTimer countDownTimer;//, countDownTimerValiNext,countDownTimerSenResult;
	private long totalTimeCountInMilliseconds;
	long MainTimeInMilliSecond, CurrentTimeInMilliSecond,
	finalTimeInMilliSecond;
	private long timeBlinkInMilliseconds;
/////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.makeoffer_offer_exp_sell);
	
	
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
		
	/*if(image != null) {
			imageLoader.DisplayImage(image, ivItemImage);
	} else {
		ivItemImage.setImageResource(R.drawable.ic_launcher);
	}
	*/

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
	ivCounter.setOnClickListener(this);
	ivAccept.setOnClickListener(this);
	totalTimeCountInMilliseconds = 300000;
//	totalTimeCountInMilliseconds = 40000;
	timeBlinkInMilliseconds = 30000;
	
	startTimer();
	
	}

	
	private void startTimer() {
		countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
			// 500 means, onTick function will be called at every 500
			// milliseconds
		
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
						// if blink is true, textview will be visible
					} else {
						tvExpireTime.setVisibility(View.INVISIBLE);
					}

					blink = !blink; // toggle the value of blink
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
		ivCounter=(ImageView)findViewById(R.id.ivCounter);
		ivAccept=(ImageView)findViewById(R.id.ivAccept);
		tvExpireTime=(TextView)findViewById(R.id.tvExpireTime);
		tvPrice=(TextView)findViewById(R.id.tvPrice);
		tvItemName=(CustomTextView)findViewById(R.id.tvItemName);
		
	}


	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
		
		Intent intent;
		switch (v.getId()) {
		
		case R.id.ivClose:
			finish();
			break;
			
		case R.id.ivCounter:
			if(!StatusofTimer){
				
					intent = new Intent(context, CounterOfferActivity.class);
					intent.putExtra("Counter_text", "");
					intent.putExtra("Counter_type", "counter_offer");
					intent.putExtra("Counter_offerid", ""+offerid);
					intent.putExtra("Counter_price", ""+price);
					intent.putExtra("Counter_name", ""+name);
					intent.putExtra("Counter_to_id", ""+to_id);
					intent.putExtra("Counter_itemid", ""+itemid);
					intent.putExtra("Counter_image", ""+image);
					intent.putExtra("Counter_isbuyer", "N");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					
			}else{
				ToastMessageDisplay("Your offer is already expire.");
			}
			break;
			
		case R.id.ivAccept:
			if(!StatusofTimer){
			
				
				
				if (cd.checkConnection()) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						new Accept_Decline_OfferBackTask().executeOnExecutor(
								AsyncTask.THREAD_POOL_EXECUTOR, "");
					} else {
						new Accept_Decline_OfferBackTask().execute("");
					}
				} else {
					Toast.makeText(context, "Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}
			}else{
				ToastMessageDisplay("Your offer is already expire.");
			}
			break;
		default:
			break;
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
	
	
	private class Accept_Decline_OfferBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;
		String msg;
		JSONArray jArray;
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
				
				jArray=new JSONArray();
				jArray.put(offerid);
				Log.e("offerid", " "+jArray);
				Log.e("userid", " "+userid);
				
				
				JSONObject json = userFunction.AcceptDeclineFunction(jArray.toString(), userid, "Y");
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
//							offerid= json.getString("offerid");
							msg = json.optString("message");
							errorMessage = "true";

						} else {
							errorMessage = "false";
							msg = json.getString("message");
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
				//custom_dialog.dialogCode(1, null, msg, con);
				ToastMessageDisplay(msg);
			Intent i = new Intent(context, HomeActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
			
			}else if (result.equals("network")) {
				msg="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,msg, context);
//				ValidationSingleButton("Error in posting");
			}  else {
				ToastMessageDisplay(msg);
				//custom_dialog.dialogCode(2, null, msg, con);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	

}
