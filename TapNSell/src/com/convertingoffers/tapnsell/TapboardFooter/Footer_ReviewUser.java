package com.convertingoffers.tapnsell.TapboardFooter;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.RoundedImageView_CenterCrop;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class Footer_ReviewUser extends BaseActivity implements OnClickListener{

	protected ImageLoader iLoader_Rounded  = ImageLoader.getInstance();;
	public static DisplayImageOptions options,option_user_img;
	Context context;
	Bitmap image_bitmap=null;
	 Dialog	 dialogpaypal;
	 String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+",HomeAddress="";
	ImageView ivProduct,ivItemBack;
	RatingBar rb_rating;
	TextView tvBuy,tvSale,tvCurrent,tvUserRatting;
	ImageView ivPhoneVerifyorNot,ivHomeVerifyorNot,ivmsgVerifyorNot,ivPayPalVerifyorNot,ivFBVerifyorNot;
	RelativeLayout rlPhoneVerifyorNot,rlHomeVerifyorNot,rlmsgVerifyorNot,rlPayPalVerifyorNot,rlFBVerifyorNot;
	String image="",twitter="",google="",facebook="",paypal="",email="",sms="",sociallogin="",name="",rating=""
	,ratingcount="",totalsell="",totalbuy="",totallisting="",userid="";
	String Fname_PayPal,lName_PayPal,PaypalEmail,msg;
	Session mCurrentSession;
	GraphUser gUser;
	
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footer_user_profile);
		
		userid = pref.getString("UserID", "");
		
		LoadImage();
		HomeAddress= pref.getString("HomeAddressMeetup", "");
		Log.e("HomeAddress","oncreate "+HomeAddress);
		if (HomeAddress.length()==0) {
			ivHomeVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_unverify));
		}else{
			ivHomeVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_verify));
		}
		
		tvHeader.setText("User Reviews");
		if(cd.checkConnection()){
			new Footer_UserProfileBackTask().execute("");
		}else{
			Toast.makeText(Footer_ReviewUser.this, "Interner connection is not available!",Toast.LENGTH_LONG).show();
		}
		
		
		
		
		ivBack.setOnClickListener(this);
		rlPhoneVerifyorNot.setOnClickListener(this);
		rlHomeVerifyorNot.setOnClickListener(this);
		rlmsgVerifyorNot.setOnClickListener(this);
		rlPayPalVerifyorNot.setOnClickListener(this);
		rlFBVerifyorNot.setOnClickListener(this);
		rlHomeVerifyorNot.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
	super.onResume();
	HomeAddress= pref.getString("HomeAddressMeetup", "");
	Log.e("HomeAddress","onresume "+HomeAddress);
	if (HomeAddress.length()==0) {
		ivHomeVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_unverify));
	}else{
		ivHomeVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_verify));
	}
	
	}
	private void LoadImage() {
		
		image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chat_item_light_blue_bg_user);

	}
	private class Footer_UserProfileBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(Footer_ReviewUser.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				JSONObject json = userFunction.FooterUserProfileFunction(userid);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS) ||res.equals("false") ) {
						
							
						
						image=json.getString("image");
						twitter=json.getString("twitter");
						google=json.getString("google");
						facebook=json.getString("facebook");
						paypal=json.getString("paypal");
						email=json.getString("email");
						sms=json.getString("sms");
						sociallogin=json.getString("sociallogin");
						name=json.getString("name");
						rating=json.getString("rating");
						ratingcount=json.getString("ratingcount");
						totalsell=json.getString("totalsell");
						totalbuy=json.getString("totalbuy");
						totallisting=json.getString("totallisting");
							errorMessage = "true";

						} 
					} else {
						errorMessage = "network";
					//	message=json.optString("message");
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

				tvHeader.setText(name);
				
				
		/*		
				if (image != null) {
					try {
						imageLoader.DisplayImage(image, ivItemBack);
					} catch (Exception e) {
					}
				} else {
					ivItemBack.setImageResource(R.drawable.chat_icon_transparent_bg);
				}*/
				
				
				
				
				
				
				// Image display using lazy loading 

				iLoader_Rounded.displayImage(image, ivItemBack, option_user_img, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						//holder.pbimage.setProgress(0);
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
//						holder.pbimage.setIndeterminate(true);
					}
				});
				
				// Image display using lazy loading
				
				
				
				
				if(image.length()>0){
					iLoader_Rounded.displayImage(image, ivProduct, options, loadImageListener);
				}else{
					iLoader_Rounded.displayImage("", ivProduct, options, loadImageListener);
				}	
				
			
				if(rating.length()==0){
					rating="0";
				}
				float rate_count=Float.parseFloat(rating);
				rb_rating.setRating(rate_count);
				tvSale.setText(totalsell);
				tvBuy.setText(totalbuy);
				tvCurrent.setText(totallisting);
				tvUserRatting.setText("("+rating+") "+" Based on "+ratingcount+" reviews");
			
				CheckVerfiOrNot();
				
				
				
				
			} else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}
		}

		public void CheckVerfiOrNot() {
			
			
			if(facebook.equals("Y")){
				ivFBVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_verify));
			}else{
				ivFBVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_unverify));
			}
			if(paypal.equals("Y")){
				ivPayPalVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_verify));
			}else{
				ivPayPalVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_unverify));
			}

			if(email.equals("Y")){
				ivmsgVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_verify));
			}else{
				ivmsgVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_unverify));
			}
			if(sms.equals("Y")){
				ivPhoneVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_verify));
			}else{
				ivPhoneVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_unverify));
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
		
		
		iLoader_Rounded.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.build();
		
		
		
		option_user_img = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.chat_icon_transparent_bg)
		.showImageForEmptyUri(R.drawable.chat_icon_transparent_bg)
		.showImageOnFail(R.drawable.chat_icon_transparent_bg)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		rb_rating=(RatingBar)findViewById(R.id.rb_rating);
		tvBuy=(TextView)findViewById(R.id.tvBuy);
		tvSale=(TextView)findViewById(R.id.tvSale);
		tvCurrent=(TextView)findViewById(R.id.tvCurrent);
		tvUserRatting=(TextView)findViewById(R.id.tvUserRatting);
		ivProduct=(ImageView )findViewById(R.id.ivProduct);
		ivItemBack=(ImageView )findViewById(R.id.ivItemBack);
		ivPhoneVerifyorNot=(ImageView)findViewById(R.id.ivPhoneVerifyorNot);
		ivHomeVerifyorNot=(ImageView)findViewById(R.id.ivHomeVerifyorNot);
		ivmsgVerifyorNot=(ImageView)findViewById(R.id.ivmsgVerifyorNot);
		ivPayPalVerifyorNot=(ImageView)findViewById(R.id.ivPayPalVerifyorNot);
		ivFBVerifyorNot=(ImageView)findViewById(R.id.ivFBVerifyorNot);
		
		
		 rlPhoneVerifyorNot=(RelativeLayout)findViewById(R.id.rlPhoneVerifyorNot);
		rlHomeVerifyorNot=(RelativeLayout)findViewById(R.id.rlHomeVerifyorNot);
		rlmsgVerifyorNot=(RelativeLayout)findViewById(R.id.rlmsgVerifyorNot);
		rlPayPalVerifyorNot=(RelativeLayout)findViewById(R.id.rlPayPalVerifyorNot);
		rlFBVerifyorNot=(RelativeLayout)findViewById(R.id.rlFBVerifyorNot);
		
	}
	
	
	
	ImageLoadingListener loadImageListener = new ImageLoadingListener() {
		
		@Override
		public void onLoadingStarted(String imageUri, View view) {
			((ImageView) view).setImageBitmap(image_bitmap);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//			Log.i(getClass().getSimpleName(), "Image Loading failed" + imageUri + " REason :" + failReason);
			((ImageView) view).setImageBitmap(image_bitmap);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			Log.i(getClass().getSimpleName(), "Image Loading " + imageUri);
			if (loadedImage != null) {
				
// 		It will Round Image
				loadedImage = RoundedImageView_CenterCrop.scaleCenterCrop(loadedImage, (int) getResources().getDimension(R.dimen.ninetydp), false);
				 
//		If You want only Rounded Corner Just give last argument as true
//				loadedImage = RoundedImageView.scaleCenterCrop(loadedImage,	(int) getResources().getDimension(R.dimen.thirty), true);
				
				((ImageView) view).setImageBitmap(loadedImage);
			} else {

				((ImageView) view).setImageBitmap(image_bitmap);
			}
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			

		}
	};
@Override
public void onClick(View v) {
	
	Intent  i ;
	switch (v.getId()) {
	case R.id.ivBack:
		finish();
		break;
		
		
		
	case R.id.rlHomeVerifyorNot:
		
		
		if(cd.checkConnection()){
			if (HomeAddress.length()==0) {
				i = new Intent(context, ProfileHome_Dialog_Activity.class);
				  startActivity(i);
			}
			
		}else{
			Toast.makeText(context, "Interner connection is not available!",Toast.LENGTH_LONG).show();
		}
	
	break;
		
	case R.id.rlFBVerifyorNot:
		
			if(cd.checkConnection()){
				if(facebook.equals("N")){
				FacebookCode();
			}}else{
				Toast.makeText(context, "Interner connection is not available!",Toast.LENGTH_LONG).show();
			}
		
		break;
	case R.id.rlmsgVerifyorNot:
	
			if(cd.checkConnection()){
				if(email.equals("N")){
				  i = new Intent(context, VerifyEmail_Activity.class);
				  startActivity(i);
				}}else{
				Toast.makeText(context, "Interner connection is not available!",Toast.LENGTH_LONG).show();
		}
		
		break;
		
		
	case R.id.rlPhoneVerifyorNot:
		
			
			if(cd.checkConnection()){
				if(sms.equals("N")){
				i = new Intent(context, VerifyPhone_Activity.class);
				  startActivity(i);
			}	
			}else{
				Toast.makeText(context, "Interner connection is not available!",Toast.LENGTH_LONG).show();
			}
		
		break;
	case R.id.rlPayPalVerifyorNot:
		
			
			if(cd.checkConnection()){
				if(paypal.equals("N")){
				PayPaldialogCode();
				}
				}else{
				Toast.makeText(context, "Interner connection is not available!",Toast.LENGTH_LONG).show();
			}
		
		
		break;
		
		
	default:
		break;
	}
}

@SuppressWarnings("static-access")
public void PayPaldialogCode() {

	// custom dialog
 dialogpaypal = new Dialog(context);
	dialogpaypal.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	
	dialogpaypal.getWindow().setBackgroundDrawable(
			new ColorDrawable(android.graphics.Color.TRANSPARENT));
	dialogpaypal.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialogpaypal.setContentView(R.layout.dialog_paypal);
	dialogpaypal.setCancelable(false);
	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	lp.copyFrom(dialogpaypal.getWindow().getAttributes());
	lp.width = lp.MATCH_PARENT;
	lp.height = lp.MATCH_PARENT;
	dialogpaypal.getWindow().setAttributes(lp);

	final EditText evPayFName, evPayLName, evPaypalEmail;
	
	ImageView ivClose = (ImageView) dialogpaypal.findViewById(R.id.ivClose);
	ImageView ivCont = (ImageView) dialogpaypal.findViewById(R.id.ivCont);
	evPayFName = (EditText) dialogpaypal.findViewById(R.id.evPayFName);
	evPayLName = (EditText) dialogpaypal.findViewById(R.id.evPayLName);
	evPaypalEmail = (EditText) dialogpaypal.findViewById(R.id.evPaypalEmail);

	
	ivClose.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					evPaypalEmail.getWindowToken(), 0);
			dialogpaypal.dismiss();
			
		}
	});
	ivCont.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			

			Fname_PayPal = evPayFName.getText().toString().trim();
			lName_PayPal = evPayLName.getText().toString().trim();
			PaypalEmail = evPaypalEmail.getText().toString().trim();

			
		
			if (Fname_PayPal.length() == 0) {
				msg = "Please enter First Name";
				Custom_Dialog.dialogCode(2, null, msg, context);
				evPayFName.requestFocus();

			} else if (lName_PayPal.length() == 0) {

				msg = "Please enter Last Name";
				Custom_Dialog.dialogCode(2, null, msg, context);
				evPayLName.requestFocus();

			} else if (PaypalEmail.length() == 0) {

				msg = "Please enter PayPal Email";
				Custom_Dialog.dialogCode(2, null, msg, context);
				evPaypalEmail.requestFocus();
			} else if (!PaypalEmail.matches(emailPattern)) {

				msg = "Please enter Valid PayPal Email";
				Custom_Dialog.dialogCode(2, null, msg, context);
				evPaypalEmail.requestFocus();
			} else {
				if (cd.checkConnection()) {

					Log.e("Fname_PayPal", " "+Fname_PayPal);
					Log.e("lName_PayPal", " "+lName_PayPal);
					Log.e("PaypalEmail", " "+PaypalEmail);
					Log.e("userid", " "+userid);
					
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							evPaypalEmail.getWindowToken(), 0);
					
					
					
					new AddPayPalInfoBackTask().execute("");
				} else {
					Toast.makeText(context, "internet_conn_failed",
							Toast.LENGTH_LONG).show();
				}
			}
		}
	});
	dialogpaypal.show();
}



public void FacebookCode() {

	if (cd.checkConnection()) {
		LoginWithFb(context);
	} else {
		Toast.makeText(context,
				"Interner connection is not available!", Toast.LENGTH_LONG)
				.show();
	}
}



public void LoginWithFb(Context context2) {
	context = context2;
	Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
	mCurrentSession = Session.getActiveSession();

	if (mCurrentSession == null) {
		mCurrentSession = new Session(Footer_ReviewUser.this);
	}
	Session.setActiveSession(mCurrentSession);
	if (mCurrentSession != null && mCurrentSession.isOpened()) {
		checkFBUser();
	} else {
		if (!mCurrentSession.isOpened() && !mCurrentSession.isClosed()) {
			mCurrentSession
					.openForRead(new Session.OpenRequest(
							Footer_ReviewUser.this)
							.setCallback(callback)
							.setPermissions(
									Arrays.asList("user_birthday", "email")));
		} else {
			Session.openActiveSession(Footer_ReviewUser.this, true, callback);
		}
	}
	//
}

private Session.StatusCallback callback = new Session.StatusCallback() {
	@Override
	public void call(Session session, SessionState state,
			Exception exception) {
		// //Log.e("Login", "state : " + state);
		onSessionStateChange(session, state, exception);
	}
};

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	mCurrentSession = Session.getActiveSession();
	if (mCurrentSession != null) {
		mCurrentSession.onActivityResult(Footer_ReviewUser.this,
				requestCode, resultCode, data);
	}

	
};

public void onSessionStateChange(Session session, SessionState state,
		Exception exception) {
	if (session != null && session.isOpened()) {
		// Log.i("FacebookFragment", "State is opened");
		mCurrentSession = session;
		checkFBUser();
	} else if (state.equals(SessionState.CLOSED_LOGIN_FAILED)) {
		Log.i("FacebookFragment",
				"State is closed" + exception.getMessage());
		Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
	}
}

@SuppressWarnings("deprecation")
public void checkFBUser() {
	progressDialog = ProgressDialog.show(Footer_ReviewUser.this, "",
			getResources().getString(R.string.Wait));
	progressDialog.setCancelable(false);
	progressDialog.show();
	Request.executeMeRequestAsync(mCurrentSession,
			new Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (progressDialog != null
							&& progressDialog.isShowing()) {
						progressDialog.dismiss();
						progressDialog = null;
					}
					Log.e("response", "" + response);
					if (user != null) {
						// strFacebookid=user.getId();
						Footer_ReviewUser.this.gUser = user;
					/*	FBEmail = (String) user.getProperty("email");
						Fb_id = user.getId();
						FBfName = user.getFirstName();
						FBlName = user.getLastName();
						FBimage = "http://graph.facebook.com/"
								+ user.getId() + "/picture?type=large";
						FBUserName = user.getUsername();

						editor.putString("reg_fname", "" + FBfName);
						editor.putString("reg_lname", "" + FBlName);
						editor.putString("reg_email", "" + FBEmail);
						editor.commit();*/
						

						new FacebookVerifyBackTask().execute("");

					}
				}
			});
}



public class FacebookVerifyBackTask extends AsyncTask<String, Void, String> {
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
			
			
			JSONObject json = userFunction.VerifyFacebookFunction(userid, "F");
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (res.equals(KEY_SUCCESS_STATUS)) {
						message=json.optString("message");
						errorMessage = "true";
					} else {
						message=json.optString("message");
						errorMessage = "false";
					}
				} else {
					message=json.optString("message");
					errorMessage = "error in posting";
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

			Custom_Dialog.dialogCode(2, null, message,context);
			facebook="Y";
			ivFBVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_verify));
			
		} else {
			ivFBVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_unverify));
			Custom_Dialog.dialogCode(2, null, message,context);
		}
}

	@Override
	protected void onProgressUpdate(Void... values) {

	}
}

private class AddPayPalInfoBackTask extends AsyncTask<String, Void, String> {
	String errorMessage, message = "";

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Please wait");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	protected String doInBackground(String... params) {

		Log.e("Fname_PayPal", " "+Fname_PayPal);
		Log.e("lName_PayPal", " "+lName_PayPal);
		Log.e("PaypalEmail", " "+PaypalEmail);
		Log.e("userid", " "+userid);
		
		
		
		JSONObject json = userFunction.PayPalFunction(userid, PaypalEmail,
				Fname_PayPal, lName_PayPal);

		try {
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if (res.equals(KEY_SUCCESS_STATUS)) {
					 userFunction.VerifyFacebookFunction(userid, "P");
					message = json.getString("message");

					errorMessage = "true";
				} else {
					
					message = json.getString("message");
					errorMessage = "false";
				}
			} else {
				errorMessage = "false";
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return errorMessage;
	}

	@Override
	protected void onPostExecute(String result) {

		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		if (result.equals("true")) {
			dialogpaypal.dismiss();
			paypal="Y";
			ivPayPalVerifyorNot.setImageDrawable(getResources().getDrawable(R.drawable.profile_unverify));
		} else {
			Custom_Dialog.dialogCode(2, null, message, context);
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}

}

}
