package com.convertingoffers.tapnsell.TapboardFooter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.AutoResizeTextView;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class VerifyEmail_Activity extends BaseActivity implements OnClickListener {

	 Dialog dialog;
	Animation RightSwipe;
	Context context;
	EditText evEmailPhone;
	RelativeLayout rlVerify;
	String strEmail="",message="",userid;
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footer_verify_email_sms);

		userid = pref.getString("UserID", "");
		tvHeader.setText("Verify Email");
		ivBack.setOnClickListener(this);
		rlVerify.setOnClickListener(this);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;

		evEmailPhone=(EditText)findViewById(R.id.evEmailPhone);
		rlVerify = (RelativeLayout) findViewById(R.id.rlVerify);
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
//		,,,rlViewTerms,rlfeature
		case R.id.rlVerify:
			buttonAnimation(rlVerify);
			strEmail=evEmailPhone.getText().toString().trim();
		
			if(strEmail.length()==0){
				message = "Please enter Email id.";
				Custom_Dialog.dialogCode(2, null, message, context);
			}else if(!strEmail.matches(emailPattern)) {
				message = "Invalid email address.";
					Custom_Dialog.dialogCode(2, null, message, context);
			}else{
				
				if(cd.checkConnection()){
					new EmailVerificationBackTask().execute("");
				}else{
					Toast.makeText(context, "Interner connection is not available!",Toast.LENGTH_LONG).show();
				}
			}
		
			break;
		case R.id.ivBack:
			finish();
			break;
		default:
			break;
		}
	}
	@SuppressWarnings("static-access")
	public void StatusdialogCode() {

		dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_verify);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		
		ImageView ivContinue = (ImageView) dialog.findViewById(R.id.ivContinue);
		AutoResizeTextView tvPhoneEmail= (AutoResizeTextView) dialog.findViewById(R.id.tvPhoneEmail);
		AutoResizeTextView tvText1= (AutoResizeTextView) dialog.findViewById(R.id.tvText1);
		AutoResizeTextView tvText2= (AutoResizeTextView) dialog.findViewById(R.id.tvText2);
		
		final String DOUBLE_BYTE_SPACE = "\u3000";
		String fixString = "";
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1
		   && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {  
		    fixString = DOUBLE_BYTE_SPACE;
		}
		
		tvText1.setText(fixString +"An e-mail was sent to"+fixString );
		tvText2.setText(fixString +"click the link to verify e-mail"+fixString );
		tvPhoneEmail.setText(fixString+strEmail+fixString);

		ivContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent i = new Intent(context, CodeEmail_Activity.class);
				startActivity(i);
				finish();
			}
		});

		dialog.show();
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
	private class EmailVerificationBackTask extends AsyncTask<String, Void, String> {
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
				
				
				JSONObject json = userFunction.VerifyEmailFunction(userid, strEmail);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

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

				StatusdialogCode();

			
			} else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else {
				Custom_Dialog.dialogCode(2, null,message, context);
			}
	}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


	
	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(VerifyEmail_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
}
