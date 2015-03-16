
package com.convertingoffers.tapnsell;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class ForgotPasswordActivity extends BaseActivity implements OnClickListener {
	Context context ;
	EditText evEmail;
	String strEmail="";
	RelativeLayout rlFp;
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
		
		tvHeader.setText("Forgot Password");
		rlFp.setOnClickListener(this);
		ivBack.setOnClickListener(this);

	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		//Initialize variable
		context=this;
		rlFp = (RelativeLayout) findViewById(R.id.rlFp);
		evEmail = (EditText) findViewById(R.id.evEmail);
	}

	@SuppressLint("NewApi") @Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rlFp:
//Fotgot password click event
			strEmail = evEmail.getText().toString().trim();

			if (strEmail.length() == 0) {
				strmsg = "Please enter Email address";
				ValidationSingleButton(strmsg);
				evEmail.setError(strmsg);
			}  else if(!strEmail.matches(emailPattern)) {
				strmsg = "Invalid email address";
				 ValidationSingleButton(strmsg);
			}else {
				
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(evEmail.getWindowToken(), 0);
				if(cd.checkConnection()){
					
					ForgotPassBackTask task=new ForgotPassBackTask();
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
					
				}else{
					//Toast.makeText(con, "internet_conn_failed",Toast.LENGTH_LONG).show();
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
				
			}
			break;
		case R.id.ivBack:
			//back click event
			finish();
			break;
		default:
			break;
		}
	}
	private class ForgotPassBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json = userFunction.ForGotPasswordFunction(strEmail);
				try {
					if (json!=null &&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							errorMessage = "true";
							message=json.optString("message");
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

				ValidationSingleButton(message);
			}else if (result.equals("network")) {
				message="Your internet connection is slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else {
				ValidationSingleButton(message);
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
	private void ValidationSingleButton(String msg) {

		   final Dialog dialog = new Dialog(ForgotPasswordActivity.this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
           dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
           dialog.setContentView(R.layout.dialog_forgot);
           ImageView ivCont = (ImageView) dialog.findViewById(R.id.ivCont);
           TextView tvMsg= (TextView) dialog.findViewById(R.id.tvMsg);
           tvMsg.setText(msg);
           dialog.show();
           ivCont.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				editor.putString("StatusItem", "N");
				editor.commit();
				finish();
			}
		});
	}
}
