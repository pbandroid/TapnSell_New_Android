package com.convertingoffers.tapnsell.TapboardFooter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class UpdatePaypal_Activity extends BaseActivity implements OnClickListener {

	Animation RightSwipe;
	Context context;
	String userid;
	EditText ev1,ev2,ev3;
	RelativeLayout rlUpdate,rlCancel;
	String old,newpass,confirm_pass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footer_update_paypal);

		userid = pref.getString("UserID", "");
		tvHeader.setText("Update PayPal");
		ivBack.setOnClickListener(this);
		
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		
		
		rlUpdate = (RelativeLayout) findViewById(R.id.rlUpdate);
		rlCancel = (RelativeLayout) findViewById(R.id.rlCancel);
		ev1 = (EditText) findViewById(R.id.ev1);
		ev2 = (EditText) findViewById(R.id.ev2);
		ev3 = (EditText) findViewById(R.id.ev3);
	}
	private class ChangePasswordBackTask extends
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
				JSONObject json = userFunction.UpdatePasswordFunction(userid, old, newpass);
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
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
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
	
		switch (v.getId()) {
			
		case R.id.ivBack:
			buttonAnimation(ivBack);
			finish();
		break;
		
		case R.id.rlUpdate:
			buttonAnimation(rlUpdate);
			
			old=ev1.getText().toString().trim();
			newpass=ev2.getText().toString().trim();
			confirm_pass=ev3.getText().toString().trim();
			
			
			if(old.length()==0){
			Custom_Dialog.dialogCode(1, null,"Pleast enter original password",context);
			}else if(newpass.length()==0){
			Custom_Dialog.dialogCode(1, null,"Pleast enter new password",context);
			}else if(!confirm_pass.equals(newpass)){
				Custom_Dialog.dialogCode(2, null, "password does not match", context);
			}else{
				if (cd.checkConnection()) {new ChangePasswordBackTask().execute("");} else {Toast.makeText(context,"Interner connection is not available!",Toast.LENGTH_LONG).show();}	
			}
						
		break;
	
		default:
			break;
		}
	}
	
	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(UpdatePaypal_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
}
