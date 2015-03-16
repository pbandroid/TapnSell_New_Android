package com.convertingoffers.tapnsell.MakeOffer;


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
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CounterOfferActivity extends BaseActivity implements OnClickListener{
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	ImageView ivItemImage,ivCounterOffer,ivClose;
	CustomTextView tvItemName;
	EditText evAmmount;
	String itemid="",userid="", amount="",image="",name="",offerid="",accept=""
		,noti_msg="",type = "", price="",to_id="",isbuyer="",dialog_msg="";
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.counteroffer);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			
			noti_msg = bundle.getString("Counter_text");
			type = bundle.getString("Counter_type");
			offerid=bundle.getString("Counter_offerid");
			price = bundle.getString("Counter_price");
			name = bundle.getString("Counter_name");
			to_id = bundle.getString("Counter_to_id");
			itemid = bundle.getString("Counter_itemid");
			image = bundle.getString("Counter_image");
			isbuyer = bundle.getString("Counter_isbuyer");
		}
		userid = pref.getString("UserID", "");
			
	/*	if(image != null) {
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
		ivCounterOffer.setOnClickListener(this);
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
	ivCounterOffer=(ImageView)findViewById(R.id.ivCounterOffer);
	tvItemName=(CustomTextView)findViewById(R.id.tvItemName);
	evAmmount=(EditText)findViewById(R.id.evAmmount);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("DefaultLocale") @Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ivCounterOffer:
			amount=evAmmount.getText().toString();
			

			boolean Status_Ammount=false;
			if(amount.length()!=0){
			float total = 0;
			total=Float.parseFloat(amount);
			String t_price = String.format("%.2f", total);
			Log.e("value", " "+t_price);
			if(t_price.equals("0.00")){
				Status_Ammount=false;
			}else{
				Status_Ammount=true;
			}
			}
			
			if(amount.length()==0){
				Custom_Dialog.dialogCode(2, null, "Please enter amount.", context);
			}else if(!Status_Ammount){
				Custom_Dialog.dialogCode(2, null, "Please enter valid amount.", context);
			}else{
				
				if(cd.checkConnection()){
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(evAmmount.getWindowToken(), 0);
						
						if (cd.checkConnection()) {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
								new CounterOfferBackTask().executeOnExecutor(
										AsyncTask.THREAD_POOL_EXECUTOR, "");
							} else {
								new CounterOfferBackTask().execute("");
							}
						} else {
							Toast.makeText(context, "Interner connection is not available!",
									Toast.LENGTH_LONG).show();
						}
						
					
				}else{
					Toast.makeText(context, "internet_conn_failed",Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.ivClose:
			finish();
			break;
			
		default:
			break;
		}
	}
	private class CounterOfferBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;
		String msg;
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
				
				Log.e("offerid", " "+offerid);
				Log.e("userid", " "+userid);
				Log.e("to_id", " "+to_id);
				Log.e("amount", " "+amount);
				Log.e("itemid", " "+itemid);
				
				JSONObject json = userFunction.CounterOfferFunction(offerid, userid, to_id, amount, itemid);
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
//				ToastMessageDisplay(msg);
				Intent  i = new Intent(context, HomeActivity.class);
				Custom_Dialog.dialogCode(3, i, msg, context);
				
			} else if (result.equals("network")) {
				msg="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,msg, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(1, null, msg, context);
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
