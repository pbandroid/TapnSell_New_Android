package com.convertingoffers.tapnsell.TapboardFooter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
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
import com.convertingoffers.tapnsell.util.AutoResizeTextView;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class VerifyPhone_Activity extends BaseActivity implements OnClickListener {

	
	Animation RightSwipe;
	Context context;
	EditText evEmailPhone;
	RelativeLayout rlVerify;
	String strEmail="",message="",userid,defaultcountry,currentcountrycode;
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	TextView tvPrivatePin;
	ImageView ivBtnText,ivTextImage;
	
	double lat = 0.0, log = 0.0, Distance = 0.0, lat1, lat2, lng1, lng2,
			Dis = 0.0;
	protected LocationManager locationManager;
	String strlatitude, strlongitude;
	Location location;
	boolean isCurrentLocation = false, Successfull, isGPSEnabled = false,
			isNetworkEnabled = false, canGetLocation = false;
	String[] countrydata,countryname,countrycode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footer_verify_email_sms);

		userid = pref.getString("UserID", "");
		tvHeader.setText("Verify Number");
		evEmailPhone.setHint("Enter Mobile Number");
		evEmailPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
		ivBack.setOnClickListener(this);
		rlVerify.setOnClickListener(this);
		tvPrivatePin.setVisibility(View.VISIBLE);
		ivTextImage.setImageDrawable(getResources().getDrawable(R.drawable.verify_no));
		ivBtnText.setImageDrawable(getResources().getDrawable(R.drawable.footer_send_pin));
//		getLocation();
		Log.e("i amd in verify", "i amd in verify");
//		defaultcountry = getResources().getConfiguration().locale.getCountry();
		 TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		 defaultcountry = manager.getNetworkCountryIso();
		 loadcountries();
		
	}

	
	
	private void loadcountries()
	{
		String[] split;
		
		countrycode =getResources().getStringArray(R.array.CountryCodes); 
		
		for(int i = 0 ; i<countrycode.length ;i++)
		{
			split= countrycode[i].split(",");
			Log.e("split[1]", ""+split[1]);
			if(defaultcountry.equalsIgnoreCase(split[1]))
			{
				currentcountrycode=split[0];
				Log.e("currentcountrycode", ""+currentcountrycode);
			}
		}
		
	}
	
	
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		countrydata =getResources().getStringArray(R.array.CountryArray);
		ivTextImage=(ImageView)findViewById(R.id.ivTextImage);
		ivBtnText=(ImageView)findViewById(R.id.ivBtnText);
		evEmailPhone=(EditText)findViewById(R.id.evEmailPhone);
		rlVerify = (RelativeLayout) findViewById(R.id.rlVerify);
		tvPrivatePin=(TextView)findViewById(R.id.tvPrivatePin);
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
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
//		,,,rlViewTerms,rlfeature
		case R.id.rlVerify:
			
			buttonAnimation(rlVerify);
			strEmail=evEmailPhone.getText().toString().trim();
		
			if(strEmail.length()==0){
				message = "Please enter valid phone no.";
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
		AutoResizeTextView tvPhoneEmail= (AutoResizeTextView) dialog.findViewById(R.id.tvPhoneEmail);
		AutoResizeTextView tvText1= (AutoResizeTextView) dialog.findViewById(R.id.tvText1);
		AutoResizeTextView tvText2= (AutoResizeTextView) dialog.findViewById(R.id.tvText2);
		
		final String DOUBLE_BYTE_SPACE = "\u3000";
		String fixString = "";
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1
		   && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {  
		    fixString = DOUBLE_BYTE_SPACE;
		}
		
		tvText1.setText(fixString+"An sms was sent to"+fixString);
		tvText2.setText(fixString+"click the link to verify phoneno."+fixString);
		tvPhoneEmail.setText(fixString+strEmail+fixString);

		ivContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent i = new Intent(context, CodePhone_Activity.class);
				startActivity(i);
				finish();
			}
		});

		dialog.show();
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
				String email="";
				 
				email=	currentcountrycode+strEmail;
				Log.e("userid", " "+userid);
				Log.e("strEmail", " "+strEmail);
				JSONObject json = userFunction.VerifyPhoneFunction(userid, email);
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

			
			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		} else {
				Custom_Dialog.dialogCode(2, null,message, context);
			}
	}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(VerifyPhone_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
}
