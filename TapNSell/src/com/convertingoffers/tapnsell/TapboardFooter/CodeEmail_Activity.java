package com.convertingoffers.tapnsell.TapboardFooter;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class CodeEmail_Activity extends BaseActivity implements OnClickListener {

	Animation RightSwipe;
	Context context;
	EditText evEmailPhone;
	RelativeLayout rlVerify;
	String strVaridicationCode = "", message = "", userid,Status_MyAccount="false";
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	ImageView ivTextImage, ivVerifyIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footer_verify_email_sms);
		Status_MyAccount=pref.getString("Status_MyAccount", "false");
		userid = pref.getString("UserID", "");
		tvHeader.setText("Verify Pin");
		evEmailPhone.setHint("Verify Pin");
		evEmailPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
		ivTextImage.setImageDrawable(getResources().getDrawable(
				R.drawable.footer_enter_digit));
		ivVerifyIcon.setImageDrawable(getResources().getDrawable(
				R.drawable.verify_right));
		ivBack.setOnClickListener(this);
		rlVerify.setOnClickListener(this);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context = this;

		evEmailPhone = (EditText) findViewById(R.id.evEmailPhone);
		rlVerify = (RelativeLayout) findViewById(R.id.rlVerify);
		ivTextImage = (ImageView) findViewById(R.id.ivTextImage);
		ivVerifyIcon = (ImageView) findViewById(R.id.ivVerifyIcon);
	}

	@SuppressLint({ "InlinedApi", "NewApi" }) @Override
	public void onClick(View v) {
	
		switch (v.getId()) {
		// ,,,rlViewTerms,rlfeature
		case R.id.rlVerify:
			buttonAnimation(rlVerify);
			strVaridicationCode = evEmailPhone.getText().toString().trim();

			if (strVaridicationCode.length() == 0) {
				message = "Please enter pin number";
				Custom_Dialog.dialogCode(2, null, message, context);
			} else {

				if (cd.checkConnection()) {
					PinVarificationBackTask task=new PinVarificationBackTask();//.execute(msg_item.getId());
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
				} else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
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

	/**
	 * 
	 */
	@SuppressWarnings("static-access")
	public void StatusdialogCode() {

		final Dialog dialog = new Dialog(context);
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
		ImageView ivVerifyIcon = (ImageView) dialog
				.findViewById(R.id.ivVerifyIcon);

		TextView tvPhoneEmail = (TextView) dialog
				.findViewById(R.id.tvPhoneEmail);
		TextView tvText1 = (TextView) dialog.findViewById(R.id.tvText1);
		TextView tvText2 = (TextView) dialog.findViewById(R.id.tvText2);
		tvText1.setVisibility(View.GONE);
		tvText2.setVisibility(View.GONE);
		ivVerifyIcon.setVisibility(View.VISIBLE);

		tvPhoneEmail.setText("Great! your email \n has been verified.");
		ivContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent i;
				if(Status_MyAccount.equals("true")){
					 i = new Intent(context,MyAccount_Activity.class);	
				}else{
					 i = new Intent(context,  Footer_ReviewUser.class);
				}
				editor.remove("Status_MyAccount");
				editor.commit();
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

	private class PinVarificationBackTask extends
			AsyncTask<String, Void, String> {
		String errorMessage, message;

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

				JSONObject json = userFunction.VerifyEmailCodeFunction(userid,
						strVaridicationCode);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

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

				StatusdialogCode();

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

	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(CodeEmail_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
}
