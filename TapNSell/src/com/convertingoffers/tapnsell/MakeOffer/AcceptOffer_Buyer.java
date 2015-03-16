package com.convertingoffers.tapnsell.MakeOffer;


import java.util.ArrayList;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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

public class AcceptOffer_Buyer extends BaseActivity  implements OnClickListener{
	String address="N";
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	ImageView ivItemImage,ivContinue,ivClose;
	CustomTextView tvItemName;
	String itemid="",userid="", amount="",image="",name="",offerid="",accept=""
		,noti_msg="",type = "", price="",to_id="",isbuyer="",dialog_msg="";

	Context context;
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	Intent i_Buy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makeoffer_offer_accept);
		
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
		ivContinue.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ivContinue:
			// call Buyer Screen
			BuybtnCode();
			break;
			
		case R.id.ivClose:
			finish();
			break;
		default:
			break;
		}
	}
	
	
	
	@SuppressLint("NewApi") public void BuybtnCode() {

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
				
				
				JSONObject json = userFunction.ReserveFunction(userid,itemid, "Y");
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							address=json.optString("address");
							errorMessage = "true";

						} else {
							msg=json.optString("message");
							errorMessage = "false";
						}
					} else {
						msg=json.optString("message");
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
				i_Buy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i_Buy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i_Buy.putExtra("address", address);
				startActivity(i_Buy);
				finish();
				
			} else if (result.equals("network")) {
				msg="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,msg, context);
//				ValidationSingleButton("Error in posting");
			}else{
				Custom_Dialog.dialogCode(2, null,msg, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
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
		ivContinue=(ImageView)findViewById(R.id.ivContinue);
		ivClose=(ImageView)findViewById(R.id.ivClose);
		tvItemName=(CustomTextView)findViewById(R.id.tvItemName);
		ivItemImage=(ImageView)findViewById(R.id.ivItemImage);
		ivClose=(ImageView)findViewById(R.id.ivClose);
		tvItemName=(CustomTextView)findViewById(R.id.tvItemName);
	}
}
