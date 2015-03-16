package com.convertingoffers.tapnsell;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.User;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.Shop.BrouseCategoryActivity;
import com.convertingoffers.tapnsell.push.PushUtils;
import com.convertingoffers.tapnsell.sell.TakePictureNew;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.Login_Dialog_Activity;
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
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


public class HomeActivity extends BaseActivity implements ConnectionCallbacks,OnConnectionFailedListener, OnClickListener {
	
	Animation RightSwipe;
	// google+
	boolean StatusGoogleDetails = false, gPlusclick = false;
	private static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;
	// google+

	String google_id, google_name, google_picture, google_gender, device_type;
	String FBEmail, Fb_id, FBfName, FBlName, FBimage, FBUserName, DeviceID,hasPaypal="",userid, ItemDetailsStatus = "n";
	String item_name, item_description, item_condition, asking_price,delevery_option, address, quantity, category_id, latitude,longitude;
	String img1 = "", img2 = "", img3 = "", img4 = "";
	String twitter_name, twitter_city, twitter_id, twitter_image;
	
	// Facebook
	GoogleCloudMessaging gcm;
	Session mCurrentSession;
	GraphUser gUser;
	// Facebook
	
//	static String TWITTER_CONSUMER_KEY = "YTkExAFireJ5lOJ8GHF4ag";
//	static String TWITTER_CONSUMER_SECRET = "JEWnBVwIRjZV7JfdRhtfJz21GGWTlyJAtMbo1K60";
	 String TWITTER_CONSUMER_KEY = "YTJGVmQRB5EyIwO6fr9wXwLbn";
	 String TWITTER_CONSUMER_SECRET = "IlbHiUQPyLXEwvdHXQXJDD0ZNMJJrLBe8IfHMja3IsTwt8QwBb";
	 Dialog dialog;
	User twitterUser;
	String Twit_id;
	SocialAuthAdapter adapter;
	public String providerName;
	Profile profile;
	
	ImageView ivSellIt, ivShop;
	// RelativeLayout rlSellIt, rlShop;
	ImageView ivMenuBtn;
	Context context ;
	SharedPreferences pref;
	Editor editor;
	String strUserID;
	File file;
	double lat = 0.0, log = 0.0, Distance = 0.0, lat1, lat2, lng1, lng2,
			Dis = 0.0;
	protected LocationManager locationManager;
	String strlatitude, strlongitude;
	Location location;
	boolean isCurrentLocation = false, Successfull, isGPSEnabled = false,
			isNetworkEnabled = false, canGetLocation = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		  Intent intent = new Intent(HomeActivity.this,ContactBackEndService.class);
			boolean statusBackService = isMyServiceRunning(HomeActivity.this);
			if (!statusBackService) {
				startService(intent);
			} 
			
			
		DeviceID = pref.getString("registrationID", "");
		//if (DeviceID.length() == 0) {
			registerdevice();
		//}
		PrintHashKey();
		
		//LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		ivShop.setOnClickListener(this);
		ivSellIt.setOnClickListener(this);
		ivMenuBtn.setOnClickListener(this);
		strUserID = pref.getString("UserID", "");
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, null)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		createFolder();


		userid = pref.getString("UserID", "");
		DeviceID = pref.getString("registrationID", "");
		adapter = new SocialAuthAdapter(new ResponseListener());
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

		try {
			adapter.addConfig(Provider.TWITTER, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	
		if(mCurrentSession!=null){
			mCurrentSession.removeCallback(callback);
		}
	}
	
	private boolean isMyServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (BackEndService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {

			// Variable to receive message status
			 providerName = values.getString(SocialAuthAdapter.PROVIDER);
			profile = adapter.getUserProfile();
			twitter_id = profile.getValidatedId();
			 twitter_name= profile.getDisplayName();
			 twitter_image= profile.getProfileImageURL();
			 
			
			
			new TwitterBackTask().execute("");
			
			boolean status = adapter.signOut(HomeActivity.this, providerName);
			Log.e("statu", ""+status);
			}

		@Override
		public void onError(SocialAuthError error) {
			error.printStackTrace();
			adapter.setClicked(false);
		}

		@Override
		public void onCancel() {
			adapter.setClicked(false);
		}

		@Override
		public void onBack() {
			adapter.setClicked(false);
		}
	}

	private void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(context,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}

	private void createFolder() {

		File myDir = new File(
				android.os.Environment.getExternalStorageDirectory(),
				"/TapNSell");
		File myDir1 = new File(
				android.os.Environment.getExternalStorageDirectory(),
				"TapNSell/TempImage");
		File myDir2 = new File(
				android.os.Environment.getExternalStorageDirectory(),
				"TapNSell/CameraImage");

		try {
			if (myDir2.mkdir()) {
			} else {
				myDir2.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (myDir.mkdir()) {
			} else {
				myDir.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (myDir1.mkdir()) {
			} else {
				myDir1.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 File myDir3 = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/ShareImages");
		  try {
				if (myDir3.mkdir()) {
				} else {
					myDir3.mkdirs();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	


	
	@Override
	protected void onResume() {
		super.onResume();
		strUserID = pref.getString("UserID", "");
	}

	private class CheckMediaUploadedBackTask extends
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
				if (strUserID.length() == 0) {
					strUserID = "0";
				}

				JSONObject json = userFunction
						.CheckMediaUploadFunction(strUserID);
				try {
					if (json!=null && json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							hasPaypal=json.getString("haspaypal");
							
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

				buttonAnimation(ivSellIt);
				deleteallFile();
				
				editor.putString("hasPaypal", hasPaypal);
				editor.remove("item_name");
				editor.remove("item_description");
				editor.remove("item_condition");
				editor.remove("asking_price");
				editor.remove("quantity");
				editor.remove("delevery_option");
				editor.remove("priview");
				editor.remove("image_path");
				editor.remove("item_name");
				editor.remove("item_description");
				editor.remove("item_condition");
				editor.remove("asking_price");
				editor.remove("quantity");
				editor.remove("delevery_option");
				editor.remove("category_id");
				editor.remove("Copy_Relist");
				editor.commit();

				Intent iSellIt = new Intent(context, TakePictureNew.class);
				startActivity(iSellIt);

			} else {
				Custom_Dialog.dialogCode(2, null,message,context);
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
		pref = PreferenceManager.getDefaultSharedPreferences(context);
		ivSellIt = (ImageView) findViewById(R.id.ivSellIt);
		ivShop = (ImageView) findViewById(R.id.ivShop);
		ivMenuBtn = (ImageView) findViewById(R.id.ivMenuBtn);
		editor = pref.edit();
	}

	@SuppressLint("NewApi") @Override
	public void onClick(View v) {

		switch (v.getId()) {

		// Sell click event
		case R.id.ivSellIt:

			if (cd.checkConnection()) {
				CheckMediaUploadedBackTask task=new CheckMediaUploadedBackTask();
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
			} else {
				Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
			}

			break;

		case R.id.ivShop:
			buttonAnimation(ivShop);
			/*if (strUserID.length() == 0 || strUserID.equals("0")) {
				dialogCode();
			} else {*/

				editor.remove("S_ComefromonePageCheck");
				editor.remove("ComefromonePageCheck");
				editor.commit();
				Intent iShop = new Intent(context, BrouseCategoryActivity.class);
				startActivity(iShop);
			//}
			break;

		case R.id.ivMenuBtn:
			Intent iMenuBtn;
			buttonAnimation(ivMenuBtn);
			if (strUserID.length() == 0 || strUserID.equals("0")) {
				
				 iMenuBtn = new Intent(context, Login_Dialog_Activity.class);
				 iMenuBtn.putExtra("title", "TapnSell");
				 startActivityForResult(iMenuBtn, 111);
			
			}else {
				 iMenuBtn = new Intent(context, TapBoardActivity.class);
				 startActivity(iMenuBtn);
				 finish();
				 
			}
		
			break;
		default:
			break;
		}

	}
/*
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
			
	   //   ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+ ======= Google+

		}*/

	// deleting all file from folder
	public void deleteallFile() {

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

//
//	public void getLocation() {}

	private void registerdevice() {
		//if (cd.checkConnection()) {
			DeviceID = registerDeviceInBackground(PushUtils.GCMSenderId);
		//} else {
			//Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
		//}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String registerDeviceInBackground(final String SenderID) {
		gcm = GoogleCloudMessaging.getInstance(this);
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Please wait");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);

		@SuppressWarnings("unused")
		AsyncTask<String, String, String> task = new AsyncTask() {

			protected void onPostExecute(Object result) {

				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				if (!DeviceID.equals(null)) {
					storeRegistrationId(DeviceID);
				}

			};

			protected void onPreExecute() {
				progressDialog.show();
			};

			@Override
			protected String doInBackground(Object... params) {
				

				try {
					locationManager = (LocationManager) context
							.getSystemService(Context.LOCATION_SERVICE);

					isGPSEnabled = locationManager
							.isProviderEnabled(LocationManager.GPS_PROVIDER);

					isNetworkEnabled = locationManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				
					if (!isGPSEnabled && !isNetworkEnabled) {
					} else {
						HomeActivity.this.canGetLocation = true;

						if (isNetworkEnabled) {

							location = locationManager
									.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if (location != null) {
								lat = location.getLatitude();
								log = location.getLongitude();
								strlatitude = "" + lat;
								strlongitude = "" + log;
								editor.putString("lat", strlatitude);
								editor.putString("long", strlongitude);
								editor.commit();
							
							}
						}
						if (isGPSEnabled) {

							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								lat = location.getLatitude();
								log = location.getLongitude();
								strlatitude = "" + lat;
								strlongitude = "" + log;
								editor.putString("lat", strlatitude);
								editor.putString("long", strlongitude);
								editor.commit();
							
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}
					DeviceID = gcm.register(SenderID);

					msg = "Device registered, registration ID=" + DeviceID;

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();

				} catch (Exception e) {
				}

				return msg;
			}
		}.execute(null, null, null);

		return DeviceID;

	}

	public void storeRegistrationId(String device_id) {

		editor.putString("registrationID", device_id);
		editor.commit();
	}

	public String getDeviceId() {
		String device_id = pref.getString("DEVICE_ID", "");
		if (device_id.equalsIgnoreCase("")) {
			return "";
		} else {
		}
		return device_id;
	}

/*	@SuppressWarnings("static-access")
	private void dialogCode() {

		// custom dialog
		dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_login);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		ImageView ivTwitter = (ImageView) dialog.findViewById(R.id.ivTwitter);
		ImageView ivFacebook = (ImageView) dialog.findViewById(R.id.ivFacebook);
		ImageView ivGoogle = (ImageView) dialog.findViewById(R.id.ivGoogle);
		TextView tvSignIn = (TextView) dialog.findViewById(R.id.tvSignIn);
		TextView tvRegister = (TextView) dialog.findViewById(R.id.tvRegister);
		String udata = "Sign in";
		SpannableString content = new SpannableString(udata);
		content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
		tvSignIn.setText(content);

		String udata1 = "Register";
		SpannableString content1 = new SpannableString(udata1);
		content1.setSpan(new UnderlineSpan(), 0, udata1.length(), 0);
		tvRegister.setText(content1);

		ivFacebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				FacebookCode();
			}
		});
		ivTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
				twitterCode();
			}
		});

		adapter.enable(ivTwitter,Provider.TWITTER);
		
		ivGoogle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				GooglePlusCode();
			}
		});

		tvSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
				SingInCode();
			}
		});

		tvRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
				RegisterCode();
			}
		});

		dialog.show();
	}
*/
	public void RegisterCode() {

		editor.putString("StatusItem", "N");
		editor.commit();
		Intent iSignIn = new Intent(context, RegisterActivity.class);
		iSignIn.putExtra("status", "Home");
		startActivity(iSignIn);
	}

	public void SingInCode() {

		editor.putString("StatusItem", "N");
		editor.commit();
		Intent iSignIn = new Intent(context, SignInActivity.class);
		iSignIn.putExtra("status", "Home");
		startActivity(iSignIn);

	}

	public void GooglePlusCode() {

		if (!StatusGoogleDetails) {
			gPlusclick = true;
			signInWithGplus();
		} else {
			new GoogleBackTask().execute("");
		}
	}



	public void FacebookCode() {


		if (cd.checkConnection()) {
			LoginWithFb(context);
		} else {
			Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
		}

	}

	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	private void updateUI(boolean isSignedIn) {
		if (isSignedIn) {
			StatusGoogleDetails = true;
			if (gPlusclick) {
				if (cd.checkConnection()) {

					new GoogleBackTask().execute("");
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
			}

		} else {
			StatusGoogleDetails = false;
		}
	}

	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		updateUI(false);
	}

	public void onConnected(Bundle arg0) {

		mSignInClicked = false;
		// Toast.makeText(si, "User is connected!", Toast.LENGTH_LONG).show();

		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);

				google_id = currentPerson.getId();
				google_name = currentPerson.getName().toString();
				google_picture = currentPerson.getImage().toString();
				google_gender = "" + currentPerson.getGender();
				device_type = "A";

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
				mConnectionResult.startResolutionForResult(HomeActivity.this,
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

	// Facebook Code

	public void LoginWithFb(Context context2) {
		context = context2;
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		mCurrentSession = Session.getActiveSession();

		if (mCurrentSession == null) {
			mCurrentSession = new Session(context);
		}
		Session.setActiveSession(mCurrentSession);
		if (mCurrentSession != null && mCurrentSession.isOpened()) {
			checkFBUser();
		} else {
			if (!mCurrentSession.isOpened() && !mCurrentSession.isClosed()) {
				mCurrentSession
						.openForRead(new Session.OpenRequest(HomeActivity.this)
								.setCallback(callback)
								.setPermissions(
										Arrays.asList("user_birthday", "email")));
			} else {
				Session.openActiveSession(HomeActivity.this, true, callback);
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
			mCurrentSession.onActivityResult(HomeActivity.this, requestCode,
					resultCode, data);
		}
		
	
	 if(resultCode == RESULT_OK && requestCode==111){
			Intent iMenuBtn = new Intent(context, TapBoardActivity.class);
			 startActivity(iMenuBtn);
	        }else if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        	//Toast.makeText(context, "Please try again later.", Toast.LENGTH_LONG).show();
        }
		
		
		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}
			mIntentInProgress = false;
			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	};

	public void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (session != null && session.isOpened()) {
			// Log.i("FacebookFragment", "State is opened");
			mCurrentSession = session;
			checkFBUser();
		} else if (state.equals(SessionState.CLOSED_LOGIN_FAILED)) {
			
			Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
		}
	}

	public void checkFBUser() {
		progressDialog = ProgressDialog.show(HomeActivity.this, "",
				getResources().getString(R.string.Wait));
		progressDialog.setCancelable(false);
		progressDialog.show();
		Request.newMeRequest(mCurrentSession,
				new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (progressDialog != null
								&& progressDialog.isShowing()) {
							progressDialog.dismiss();
							progressDialog = null;
						}
						if (user != null) {
							// strFacebookid=user.getId();
							HomeActivity.this.gUser = user;
							FBEmail = (String) user.getProperty("email");
							Fb_id = user.getId();
							FBfName = user.getFirstName();
							FBlName = user.getLastName();
							FBimage = "http://graph.facebook.com/"
									+ user.getId() + "/picture?type=large";
							FBUserName = user.getUsername();

							editor.putString("reg_fname", "" + FBfName);
							editor.putString("reg_lname", "" + FBlName);
							editor.putString("reg_email", "" + FBEmail);
							editor.commit();

							new FacebookBackTask().execute("");

						}
					}
				}).executeAsync();
	}

	// Facebook Code
	private class FacebookBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

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

				JSONObject json = userFunction.FBRegisterFunction(FBfName,
						FBlName, Fb_id, FBUserName, FBEmail, "", FBimage, "A",
						DeviceID);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");

							errorMessage = "true";

						} else {
							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");
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
				item_name = pref.getString("item_name", "");
				item_description = pref.getString("item_description", "");
				item_condition = pref.getString("item_condition", "");
				asking_price = pref.getString("asking_price", "");
				delevery_option = pref.getString("delevery_option", "");
				address = pref.getString("address", "");
				quantity = pref.getString("quantity", "");
				category_id = pref.getString("category_id", "");
				latitude = pref.getString("latitude", "");
				longitude = pref.getString("longitude", "");

				Intent i = new Intent(context,
						BrouseCategoryActivity.class);
				startActivity(i);

			} else if (result.equals("network")) {
				Custom_Dialog.dialogCode(2, null,"Your internet connection is too slow", context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null,"Your previous item is uploading please wait.", context);
			
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	private class GoogleBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

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
				JSONObject json = userFunction.gplusFunction(google_id,
						google_name, google_picture, google_gender,
						device_type, DeviceID);
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");
							errorMessage = "true";

						} else {
							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");
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
				item_name = pref.getString("item_name", "");
				item_description = pref.getString("item_description", "");
				item_condition = pref.getString("item_condition", "");
				asking_price = pref.getString("asking_price", "");
				delevery_option = pref.getString("delevery_option", "");
				address = pref.getString("address", "");
				quantity = pref.getString("quantity", "");
				category_id = pref.getString("category_id", "");
				latitude = pref.getString("latitude", "");
				longitude = pref.getString("longitude", "");

				Intent i = new Intent(context,
						BrouseCategoryActivity.class);
				startActivity(i);

			} else {

				Toast.makeText(context, "Please wait for some time your previous item is uploading", Toast.LENGTH_LONG)
						.show();
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	private class TwitterBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			dialog.dismiss();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				
				 
				JSONObject json = userFunction.TwitterRegisterFunction(
						twitter_id, twitter_name, twitter_city, "A", DeviceID,
						twitter_image);
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");

							errorMessage = "true";

						} else {
							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");

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
				item_name = pref.getString("item_name", "");
				item_description = pref.getString("item_description", "");
				item_condition = pref.getString("item_condition", "");
				asking_price = pref.getString("asking_price", "");
				delevery_option = pref.getString("delevery_option", "");
				address = pref.getString("address", "");
				quantity = pref.getString("quantity", "");
				category_id = pref.getString("category_id", "");
				latitude = pref.getString("latitude", "");
				longitude = pref.getString("longitude", "");

				Intent i = new Intent(context,
						BrouseCategoryActivity.class);
				startActivity(i);

			} else {

				Toast.makeText(context, result, Toast.LENGTH_LONG)
						.show();
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	private void PrintHashKey() {

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.convertingoffers.tapnsell",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}


  // print hash key for facebook 
  
 
 


/// My service is running or not

/*
private boolean isMyServiceRunning(Context context) {
	ActivityManager manager = (ActivityManager) context
			.getSystemService(Context.ACTIVITY_SERVICE);
	for (RunningServiceInfo service : manager
			.getRunningServices(Integer.MAX_VALUE)) {
		if (BackEndService.class.getName().equals(
				service.service.getClassName())) {
			return true;
		}
	}
	return false;
}


	// checkin gps is enable or not
	private void showGPSDisabledAlertToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Goto Settings Page To Enable GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
*/