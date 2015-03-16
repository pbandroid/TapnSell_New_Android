package com.convertingoffers.tapnsell.util;
import java.util.Arrays;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.User;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.RegisterActivity;
import com.convertingoffers.tapnsell.SignInActivity;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class Login_Dialog_Activity extends BaseActivity implements ConnectionCallbacks,
OnConnectionFailedListener,OnClickListener{

	// facebook 
	Context context_;
	Session mCurrentSession;
	GraphUser gUser;
	String type,FBEmail, Fb_id, FBfName, FBlName, FBimage, FBUserName, DeviceID="",	strUserID, ItemDetailsStatus = "n";
	// facebook 

	// google+
	boolean StatusGoogleDetails = false, gPlusclick = false;
	private static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;
	String google_id, google_name, google_picture, google_gender, device_type;
	// google+
	
	// Twitter
	 String TWITTER_CONSUMER_KEY = "YTJGVmQRB5EyIwO6fr9wXwLbn";
	 String TWITTER_CONSUMER_SECRET = "IlbHiUQPyLXEwvdHXQXJDD0ZNMJJrLBe8IfHMja3IsTwt8QwBb";
	 Dialog dialog;
	User twitterUser;
	String Twit_id;
	SocialAuthAdapter adapter;
	public String providerName;
	Profile profile;
	ImageView ivClose;
	String twitter_name, twitter_city, twitter_id, twitter_image;

	// Twitter

	ImageView ivTwitter, ivFacebook,ivGoogle,ivBack;
	TextView tvSignIn,tvRegister,tvHeader,tvHeader_Dialog;
	Context context_dialog;
	String strtitle="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.dialog_login);
	
	String udata = "Sign in";
	SpannableString content = new SpannableString(udata);
	content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
	tvSignIn.setText(content);

	String udata1 = "Register";
	SpannableString content1 = new SpannableString(udata1);
	content1.setSpan(new UnderlineSpan(), 0, udata1.length(), 0);
	tvRegister.setText(content1);
	ivBack.setVisibility(View.GONE);
	ivFacebook.setOnClickListener(this);
	ivTwitter.setOnClickListener(this);
	ivGoogle.setOnClickListener(this);
	tvSignIn.setOnClickListener(this);
	tvRegister.setOnClickListener(this);
	
	Bundle bundle = getIntent().getExtras();
	if(bundle!=null){
		strtitle = bundle.getString("title");
	}else{
		strtitle="";
	}
	ivMenu.setVisibility(View.GONE);
	//tvHeader.setText("Sign in "+strtitle);
	DeviceID = pref.getString("registrationID", "");
	ivBack.setVisibility(View.GONE);
	ivClose.setOnClickListener(this);
	tvHeader_Dialog.setText("Sign in to"+strtitle);
	
	adapter = new SocialAuthAdapter(new ResponseListener());
	adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
	adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

	try {
		adapter.addConfig(Provider.TWITTER, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, null);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	
	}
	/*@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(mCurrentSession!=null){
			mCurrentSession.removeCallback(callback);
		}
	}*/
	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {

			// Variable to receive message status
			Log.d("twt Demo", "Authentication Successful");
			 providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("twt Demo", "Provider Name = " + providerName);
			profile = adapter.getUserProfile();
			twitter_id = profile.getValidatedId();
			 twitter_name= profile.getDisplayName();
			 twitter_image= profile.getProfileImageURL();
			new TwitterBackTask().execute("");
			
			boolean status = adapter.signOut(Login_Dialog_Activity.this, providerName);
			Log.e("status", " "+status);
		}

		@Override
		public void onError(SocialAuthError error) {
			error.printStackTrace();
			Log.d("Share-Bar", error.getMessage());
			adapter.setClicked(false);
		}

		@Override
		public void onCancel() {
			Log.d("Share-Bar", "Authentication Cancelled");
			adapter.setClicked(false);
		}

		@Override
		public void onBack() {
			Log.d("Share-Bar", "Dialog Closed by pressing Back Key");
			adapter.setClicked(false);
		}
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		
		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(Login_Dialog_Activity.this)
		.addOnConnectionFailedListener(Login_Dialog_Activity.this).addApi(Plus.API, null)
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		
		ivClose=(ImageView)findViewById(R.id.ivClose);
		tvHeader_Dialog= (TextView) findViewById(R.id.tvHeader_Dialog);
		 tvHeader= (TextView) findViewById(R.id.tvHeader);
		 ivBack = (ImageView) findViewById(R.id.ivBack);
		 ivTwitter = (ImageView) findViewById(R.id.ivTwitter);
		 ivFacebook = (ImageView) findViewById(R.id.ivFacebook);
		 ivGoogle = (ImageView) findViewById(R.id.ivGoogle);
		 tvSignIn = (TextView) findViewById(R.id.tvSignIn);
		 tvRegister = (TextView) findViewById(R.id.tvRegister);
		 context_dialog=this;
	}
	
//  ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook  
    
	public void FacebookCode() {

		if (cd.checkConnection()) {
			LoginWithFb();
		} else {
			Toast.makeText(context_dialog,
					"Interner connection is not available!", Toast.LENGTH_LONG)
					.show();
		}
	}
	
	public void LoginWithFb() {
		
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		mCurrentSession = Session.getActiveSession();

		if (mCurrentSession == null) {
			mCurrentSession = new Session(context_dialog);
		}
		Session.setActiveSession(mCurrentSession);
		if (mCurrentSession != null && mCurrentSession.isOpened()) {
			checkFBUser();
		} else {
			if (!mCurrentSession.isOpened() && !mCurrentSession.isClosed()) {
				mCurrentSession
						.openForRead(new Session.OpenRequest(
								(Activity) context_dialog)
								.setCallback(callback)
								.setPermissions(
										Arrays.asList("user_birthday", "email")));
			} else {
				Session.openActiveSession((Activity) context_dialog, true, callback);
			}
		}
	}

	
	public void checkFBUser() {
		progressDialog = ProgressDialog.show(context_dialog, "",
				getResources().getString(R.string.Wait));
		progressDialog.setCancelable(false);
		progressDialog.show();
		Request.newMeRequest(mCurrentSession, new Request.GraphUserCallback()  {

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
							Login_Dialog_Activity.this.gUser = user;
							FBEmail = (String) user.getProperty("email");
							Fb_id = user.getId();
							FBfName = user.getFirstName();
							FBlName = user.getLastName();
							FBimage = "http://graph.facebook.com/"+ user.getId() + "/picture?type=large";
							FBUserName = user.getUsername();

							editor.putString("reg_fname",  FBfName);
							editor.putString("reg_lname",FBlName);
							editor.putString("reg_email",  FBEmail);
							editor.commit();

							new FacebookBackTask().execute("");

						}
					}
				}).executeAsync();
			}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// //Log.e("Login", "state : " + state);
			onSessionStateChange(session, state, exception);
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
			Toast.makeText(context_dialog, "Login failed", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class FacebookBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context_dialog);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				editor.putString("reg_fname", FBfName);
				editor.putString("reg_lname", FBlName);
				editor.putString("reg_email", FBEmail);
				editor.commit();
				Log.e("deviceid"," "+ DeviceID);
				JSONObject json = userFunction.FBRegisterFunction(FBfName,
						FBlName, Fb_id, FBUserName, FBEmail, "", FBimage, "A",
						DeviceID);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");
							userFunction.VerifyFacebookFunction(strUserID, "F");
							errorMessage = "true";

						} else {
							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");
							userFunction.VerifyFacebookFunction(strUserID, "F");
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
					
					editor.putString("UserID", strUserID); // Storing string
					editor.commit();
					userid = strUserID;
					Log.e("userid fb", " " + userid);
					Intent returnIntent = new Intent();
					setResult(RESULT_OK, returnIntent);
					finish();
					
			}else if (result.equals("network")) {
				String message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, Login_Dialog_Activity.this);
//				ValidationSingleButton("Error in posting");
			} else {
				Toast.makeText(context_dialog, result, Toast.LENGTH_LONG).show();
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				finish();
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

//  ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
	//  ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook
		
		mCurrentSession = Session.getActiveSession();
		if (mCurrentSession != null) {
			mCurrentSession.onActivityResult(Login_Dialog_Activity.this,
					requestCode, resultCode, data);
		}
	//  ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook ======= Facebook

   //   ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+
		
		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}
			mIntentInProgress = false;
			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
		Log.e("requestCode", " "+requestCode);
		Log.e("resultCode"," "+ resultCode);
		
		if (requestCode == 4 ||requestCode == 7) {
			if (resultCode == RESULT_OK) {
				
//				dialog.dismiss();
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);
				finish();
				
			}else{
				//Custom_Dialog.dialogCode(2, null, "Please try agin later.", context_dialog);
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				finish();
			}
		}
		
   //   ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+
		
	}

	public void GooglePlusCode() {

		if (!StatusGoogleDetails) {
			gPlusclick = true;
			signInWithGplus();
		} else {
			new GoogleBackTask().execute("");
		}
 // ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+
	}

	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	private void updateUI(boolean isSignedIn) {
		if (isSignedIn) {
			Log.e("Detail get successfully", "Detail get successfully");
			StatusGoogleDetails = true;
			if (gPlusclick) {
				if (cd.checkConnection()) {

					new GoogleBackTask().execute("");
				} else {
					Toast.makeText(context_dialog,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}
			}

		} else {
			StatusGoogleDetails = false;
			Log.e("error", "error");
		}
	}

	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		updateUI(false);
	}

	public void onConnected(Bundle arg0) {

		mSignInClicked = false;

		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);

				google_id = currentPerson.getId();
				
			
				editor.putString("reg_fname", currentPerson.getName().getGivenName());
				editor.putString("reg_lname", currentPerson.getName().getFamilyName());
				editor.commit();
				 Log.e("reg_fname", " "+currentPerson.getName().getGivenName());
				 Log.e("reg_lname", " "+currentPerson.getName().getMiddleName());
				 Log.e("reg_lname", " "+currentPerson.getName().getFamilyName());
				 
				google_name = currentPerson.getName().getGivenName()+currentPerson.getName().getMiddleName();
			if(google_name!=null && !google_name.equals("")){
				google_name=google_name.replaceAll("null", "");
			}
				google_picture = currentPerson.getImage().getUrl();
				google_gender = "" + currentPerson.getGender();
				device_type = "A";
				
				Log.e("google_id", " "+google_id);
				Log.e("google_name", " "+google_name);
				Log.e("google_picture", " "+google_picture);
				Log.e("google_gender", " "+google_gender);

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		updateUI(true);
	}

	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			mConnectionResult = result;
			if (mSignInClicked) {
				resolveSignInError();
			}
		}

	}

	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(Login_Dialog_Activity.this,
						RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}
	
	private class GoogleBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context_dialog);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				
				Log.e("google_id", "1 "+google_id);
				Log.e("google_name", "1 "+google_name);
				Log.e("google_picture", "1 "+google_picture);
				Log.e("google_gender", "1 "+google_gender);
				Log.e("device_type", "1 "+device_type);
				
				
				JSONObject json = userFunction.gplusFunction(google_id,
						google_name, google_picture, google_gender,
						device_type, DeviceID);
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");
							 userFunction.VerifyFacebookFunction(strUserID, "G");
							errorMessage = "true";

						} else {
							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");
							 userFunction.VerifyFacebookFunction(strUserID, "G");
							errorMessage = "false";
						}
					} else {
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
				
				editor.putString("UserID", strUserID); // Storing string
				editor.commit();
				userid = strUserID;
				Log.e("userid fb", " " + userid);
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);
				finish();
				
		} else {
			Toast.makeText(context_dialog, result, Toast.LENGTH_LONG).show();
			Intent returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);
			finish();
		}

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

// ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+	

// ======= Twitter ======= Twitter ======= Twitter ======= Twitter ======= Twitter ======= Twitter ======= Twitter ======= Twitter ======= Twitter 	
	

	private class TwitterBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			
			progressDialog = new ProgressDialog(context_dialog);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				JSONObject json = userFunction.TwitterRegisterFunction(
						twitter_id, twitter_name, twitter_city, "", DeviceID,
						twitter_image);
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");
							userFunction.VerifyFacebookFunction(strUserID, "T");
							
							errorMessage = "true";

						} else {
							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");
							 userFunction.VerifyFacebookFunction(strUserID, "T");
							errorMessage = "false";
						}
					} else {
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
				
				editor.putString("UserID", strUserID); // Storing string
				editor.commit();
				userid = strUserID;
				Log.e("userid fb", " " + userid);
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);
				finish();
				
		} else {
			Toast.makeText(context_dialog, result, Toast.LENGTH_LONG).show();
			Intent returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);
			finish();
		}
			
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

// ======= Twitter ======= Twitter ======= Twitter ======= Twitter ======= Twitter ======= Twitter ======= Twitter ======= Twitter ======= Twitter

	public void SingInCode() {

		editor.putString("StatusItem", "N");
		editor.commit();
		Intent iSignIn = new Intent(context_dialog, SignInActivity.class);
		iSignIn.putExtra("Login_Dialog_Activity", "Y");
		startActivityForResult(iSignIn,7);
	}

	public void RegisterCode() {

		editor.putString("StatusItem", "N");
		editor.commit();
		Intent iSignIn = new Intent(context_dialog, RegisterActivity.class);
		iSignIn.putExtra("Login_Dialog_Activity", "Y");
		startActivityForResult(iSignIn, 4);
	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ivFacebook:
			FacebookCode();
			break;
		case R.id.ivTwitter:
			if (cd.checkConnection()) {
				adapter.enable(ivTwitter,Provider.TWITTER);
			} else {
				Toast.makeText(context_dialog,
						"Interner connection is not available!", Toast.LENGTH_LONG)
						.show();

			}
			break;
		case R.id.ivGoogle:
			GooglePlusCode();
			break;
		case R.id.ivClose:
			finish();
			break;
			
		case R.id.tvSignIn:
			SingInCode();
			break;		
		case R.id.tvRegister:
			RegisterCode();
				break;
		default:
			break;
		}
	}

}
