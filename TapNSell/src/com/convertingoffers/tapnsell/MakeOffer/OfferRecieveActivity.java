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
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapboardSell.ViewRecieveOfferActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.CustomTextView;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class OfferRecieveActivity extends BaseActivity implements OnClickListener{
	ImageView ivItemImage,ivClose,ivDecline,ivCounter,ivAccept;
	CustomTextView tvItemName;
	TextView tvText;
	RelativeLayout rlViewOffer;
	EditText evAmmount;
	String itemid="",userid="", amount="",image="",name="",offerid="",accept=""
		,noti_msg="",type = "",user_Name="", price="",to_id="",isbuyer="",dialog_msg="";
	Context context;
	
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makeoffer_offer_recive);
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			
		noti_msg = bundle.getString("notification_text");
		type = bundle.getString("notification_type");
		offerid=bundle.getString("notification_offerid");
		price = bundle.getString("notification_price");
		name = bundle.getString("notification_itemname");
		to_id = bundle.getString("notification_fromid");
		itemid = bundle.getString("notification_itemid");
		image = bundle.getString("notification_itemimage");
		isbuyer = bundle.getString("notification_isbuyer");
		user_Name = bundle.getString("notification_username");
		
		}
		
		
		tvText.setText(user_Name+" Is Offering");
		Log.e("price", " "+price);
		userid = pref.getString("UserID", "");
		evAmmount.setText(""+price);
		/*if (image != null) {
				imageLoader.DisplayImage(image, ivItemImage);
		} else {
			ivItemImage.setImageResource(R.drawable.ic_launcher);
		}*/
		

		// Image display using lazy loading 

		iLoader_item.displayImage(image, ivItemImage, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				//holder.pbimage.setProgress(0);
//				holder.pbimage.setIndeterminate(true);
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
//				holder.pbimage.setIndeterminate(true);
			}
		});
		
		// Image display using lazy loading
		
		
		tvItemName.setText(""+name);
		ivClose.setOnClickListener(this);
		ivDecline.setOnClickListener(this);
		ivCounter.setOnClickListener(this);
		ivAccept.setOnClickListener(this);
		rlViewOffer.setOnClickListener(this);
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
	
	ivItemImage=(ImageView)findViewById(R.id.ivItemImage);
	ivClose=(ImageView)findViewById(R.id.ivClose);
	ivDecline=(ImageView)findViewById(R.id.ivDecline);
	ivCounter=(ImageView)findViewById(R.id.ivCounter);
	ivAccept=(ImageView)findViewById(R.id.ivAccept);
	rlViewOffer=(RelativeLayout)findViewById(R.id.rlViewOffer);
	tvItemName=(CustomTextView)findViewById(R.id.tvItemName);
	tvText=(TextView)findViewById(R.id.tvText);
	evAmmount=(EditText)findViewById(R.id.evAmmount);
	}

	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ivCounter:
			Intent intent = new Intent(context, CounterOfferActivity.class);
			intent.putExtra("Counter_text", ""+noti_msg);
			intent.putExtra("Counter_type", ""+type);
			intent.putExtra("Counter_offerid", ""+offerid);
			intent.putExtra("Counter_price", ""+price);
			intent.putExtra("Counter_name", ""+name);
			intent.putExtra("Counter_to_id", ""+to_id);
			intent.putExtra("Counter_itemid", ""+itemid);
			intent.putExtra("Counter_image", ""+image);
			intent.putExtra("Counter_isbuyer", ""+isbuyer);
			startActivity(intent);
		
			break;
	
			
		case R.id.ivDecline:
			accept="N";
			if(cd.checkConnection()){
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(evAmmount.getWindowToken(), 0);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						new  Accept_Decline_OfferBackTask().executeOnExecutor(
								AsyncTask.THREAD_POOL_EXECUTOR, "");
					} else {
						new  Accept_Decline_OfferBackTask().execute("");
					}
			}else{
				Toast.makeText(context, "internet_conn_failed",Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.ivAccept:
			accept="Y";
			if(cd.checkConnection()){
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(evAmmount.getWindowToken(), 0);
					
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						new  Accept_Decline_OfferBackTask().executeOnExecutor(
								AsyncTask.THREAD_POOL_EXECUTOR, "");
					} else {
						new  Accept_Decline_OfferBackTask().execute("");
					}
			}else{
				Toast.makeText(context, "internet_conn_failed",Toast.LENGTH_LONG).show();
			}
			break;
			
		case R.id.ivClose:
			finish();
			break;
		case R.id.rlViewOffer:
		
			Intent i =  new Intent(context, ViewRecieveOfferActivity.class);
			startActivity(i);
			finish();
			
			break;	
			
		default:
			break;
		}
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
				Log.e("accept", " "+accept);
				
				JSONObject json = userFunction.AcceptDeclineFunction(jArray.toString(), userid, accept);
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
				//
				if(isbuyer.equals("Y") && accept.equals("Y")){
					ToastMessageDisplay(msg);
				Intent i = new Intent(context, AcceptOffer_Buyer.class);
				i.putExtra("notification_text", msg);
				i.putExtra("notification_type", type);
				i.putExtra("notification_offerid", offerid);
				i.putExtra("notification_price", price);
				i.putExtra("notification_itemname", name);
				i.putExtra("notification_fromid", to_id);
				i.putExtra("notification_itemid", itemid);
				i.putExtra("notification_itemimage", image);
				i.putExtra("notification_isbuyer", isbuyer);
				startActivity(i);
				finish();
				}else{
					msg="Great! You’ll get notified once buyer completes order.";
					Custom_Dialog.dialogCode(1, null,msg, context);
				}
			
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


	public void ToastMessageDisplay(String msg) {
	Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
	toast.setGravity(Gravity.CENTER, 0, 0);
	toast.show();
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

}
