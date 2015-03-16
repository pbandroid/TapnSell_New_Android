package com.convertingoffers.tapnsell.sell;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;


public class SellItFasterActivity extends BaseActivity implements OnClickListener {

	ImageView ivNo, ivYes;
	Context context; 
	String item_id="",url,mainurl="",image="",itemname="";
	Custom_Dialog custom_dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sellitfaster);
		item_id = pref.getString("itemid", "");
		ivYes.setOnClickListener(this);
		ivNo.setOnClickListener(this);
		
		if(cd.checkConnection()){
			new UrlSharBackTask().execute("");
		}else{
			Toast.makeText(context, "internet_conn_failed",Toast.LENGTH_LONG).show();
		}
		
	}

	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	         if (keyCode == KeyEvent.KEYCODE_BACK) {
	         //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
	         return true;
	         }
	         return super.onKeyDown(keyCode, event);    
	    }
	  
	 
	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		
		case R.id.ivYes:
			
		
			 i = new Intent(SellItFasterActivity.this, SellItFasterContact.class);
			i.putExtra("msg", "");
			i.putExtra("url", url);
			i.putExtra("type", "smsa");
			startActivity(i);
		
		
			break;

		case R.id.ivNo:
			 i = new Intent(SellItFasterActivity.this,SharActivity.class);
			i.putExtra("msg", "");
			i.putExtra("url", url);
			i.putExtra("type", "smsa");
			startActivity(i);
			break;
			
		default:
			break;
		}
	}

	
	private class UrlSharBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SellItFasterActivity.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				Log.e("item_id", " "+item_id);
				Log.e("item_id", " "+item_id);
				
				JSONObject json = userFunction.SharUrlFunction(item_id, "I");
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							url=json.getString("url");
							image= UserFunctions.localImageUrl;
							image=image+json.getString("image");
							itemname=json.optString("itemname");
							mainurl=json.optString("mainurl");
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

					editor.putString("SharUrl", url);
					editor.putString("SharImage", image);
					editor.putString("itemname", itemname);
					editor.commit();	
			
			
			}else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null, message, context);
//				ValidationSingleButton("No records found....");
			}

		}
/*	
	private void ValidationSingleButton(String msg) {
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
		alertDialogBuilder.setTitle("alert");
	
			alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
	
					finish();
					}
				  });
	
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
	}*/
}
	
	
@Override
public void onContentChanged() {
	super.onContentChanged();
	context=this;
	custom_dialog =new Custom_Dialog();
	pref = PreferenceManager.getDefaultSharedPreferences(SellItFasterActivity.this);
	ivYes = (ImageView) findViewById(R.id.ivYes);
	ivNo = (ImageView) findViewById(R.id.ivNo);

	}
}

