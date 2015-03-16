package com.convertingoffers.tapnsell.sell;


import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.Shop.BrouseCategoryActivity;
import com.convertingoffers.tapnsell.TapboardSell.MyListingActive_Sold_Expire_Activity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class nice extends BaseActivity implements OnClickListener {
	ImageView ivShop, ivSell, ivClose;
	File file;
	Context context;
	String strUserID=""; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nice);
		
		ivShop.setOnClickListener(this);
		ivSell.setOnClickListener(this);
		ivClose.setOnClickListener(this);
		
		strUserID = pref.getString("UserID", "");
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		ivShop = (ImageView) findViewById(R.id.ivShop);
		ivSell = (ImageView) findViewById(R.id.ivSell);
		ivClose = (ImageView) findViewById(R.id.ivClose);
	}

	@SuppressLint("InlinedApi") @Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ivSell:
			if(cd.checkConnection()){
				new CheckMediaUploadedBackTask().execute("");
			}else{
				Toast.makeText(nice.this, "Interner connection is not available!",Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.ivShop:
			Intent iShop = new Intent(nice.this, BrouseCategoryActivity.class);
			iShop.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(iShop);
			finish();
			
			break;
		case R.id.ivClose:
			Intent  i = new Intent(nice.this, MyListingActive_Sold_Expire_Activity.class);
			startActivity(i);
			break;
		default:
			break;
		}
	}
		@Override
		public void onBackPressed() {
			
			super.onBackPressed();
		}
	// deleting all file from folder
	private void deleteallFile() {
		

		file = new File(android.os.Environment.getExternalStorageDirectory(),
				"/TapNSell/CameraImage/1.PNG");
		if (file.exists()) {
			file.delete();
		}

		file = new File(android.os.Environment.getExternalStorageDirectory(),
				"/TapNSell/CameraImage/2.PNG");
		if (file.exists()) {
			file.delete();
		}

		file = new File(android.os.Environment.getExternalStorageDirectory(),
				"/TapNSell/CameraImage/3.PNG");
		if (file.exists()) {
			file.delete();
		}

		file = new File(android.os.Environment.getExternalStorageDirectory(),
				"/TapNSell/CameraImage/4.PNG");
		if (file.exists()) {
			file.delete();
		}

		file = new File(android.os.Environment.getExternalStorageDirectory(),
				"/TapNSell/CameraVideo/TapnSellVideo.mp4");
		if (file.exists()) {
			file.delete();
		}

	}
	
	

	private class CheckMediaUploadedBackTask extends AsyncTask<String, Void, String> {
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
				if(strUserID.length()==0){
					strUserID="0";
				}
				
				JSONObject json = userFunction.CheckMediaUploadFunction(strUserID);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							errorMessage = "true";
						} else {
							errorMessage = "false";
							message=json.optString("message");
						}
					} else {
						errorMessage = "network";
						message=json.optString("message");
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

				deleteallFile();

				editor.remove("priview");
				editor.remove("image_path");
				editor.remove("item_name");
				editor.remove("item_description");
				editor.remove("item_condition");
				editor.remove("asking_price");
				editor.remove("quantity");
				editor.remove("delevery_option");
				editor.remove("category_id");
				editor.commit();

				Intent iSellIt = new Intent(nice.this, TakePictureNew.class);
				iSellIt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(iSellIt);
				finish();
				
			} else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null, message, nice.this);
			}
	}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


}
