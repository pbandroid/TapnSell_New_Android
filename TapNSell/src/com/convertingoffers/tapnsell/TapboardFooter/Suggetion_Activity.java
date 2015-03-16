package com.convertingoffers.tapnsell.TapboardFooter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class Suggetion_Activity extends BaseActivity implements OnClickListener {

	EditText evSuggetion;
	Animation RightSwipe;
	Context context;
	RelativeLayout rlSuggetion;
	String userid,comment="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggest_feature);

		ivMenu.setVisibility(View.VISIBLE);
		userid = pref.getString("UserID", "");
		tvHeader.setText("Suggest a Feature");
		ivBack.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
		rlSuggetion.setOnClickListener(this);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		
		evSuggetion = (EditText) findViewById(R.id.evSuggetion);
		rlSuggetion= (RelativeLayout) findViewById(R.id.rlSuggetion);
		
	}
	private class SuggestBackTask extends
	AsyncTask<String, Void, String> {
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

				Log.e("userid", " " + userid);
				JSONObject json = userFunction.SuggestFunction(userid, comment);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							message = json.optString("message");
							errorMessage = "true";
						} else {
							message = json.optString("message");
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

				Custom_Dialog.dialogCode(1, null, message, context);

			} else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null, message, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	
	
	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		
		switch (v.getId()) {
		
		case R.id.ivMenu:
			buttonAnimation(ivMenu);
			imm.hideSoftInputFromWindow(evSuggetion.getWindowToken(), 0);
			Intent i = new Intent(context, TapBoardActivity.class);
			startActivity(i);
			finish();
		break;
		
		case R.id.ivBack:
			imm.hideSoftInputFromWindow(evSuggetion.getWindowToken(), 0);
			buttonAnimation(ivBack);
			finish();
		break;
		
		case R.id.rlSuggetion:
			buttonAnimation(rlSuggetion);
			imm.hideSoftInputFromWindow(evSuggetion.getWindowToken(), 0);

			comment = evSuggetion.getText().toString().trim();
			if (comment.length() == 0) {
				Custom_Dialog.dialogCode(2, null,
						"Pleast enter your suggestion.", context);
			} else {
				if (cd.checkConnection()) {
					new SuggestBackTask().execute("");
				} else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}
			}
						
		break;
	
		default:
			break;
		}
	}
	
	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(Suggetion_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
}
