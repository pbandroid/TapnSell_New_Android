package com.convertingoffers.tapnsell.sell;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.User;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.BackEndService;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.RegisterActivity;
import com.convertingoffers.tapnsell.push.PushUtils;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.CircularSeekBar;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.facebook.LoggingBehavior;
import com.facebook.LoginActivity;
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

public class PostItemActivity extends BaseActivity implements
		ConnectionCallbacks, OnConnectionFailedListener {

	
	Dialog dialog;
	// google+
	boolean StatusGoogleDetails = false, gPlusclick = false;
	private static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;
	GoogleCloudMessaging gcm;
	// google+
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	String google_id, google_name, google_picture, google_gender, device_type;
	String FBEmail, Fb_id, FBfName, FBlName, FBimage, FBUserName, DeviceID,
			strUserID, ItemDetailsStatus = "n";
	String item_name, item_description, item_condition, asking_price,
			delevery_option, address, quantity, category_id, latitude,
			longitude;
	String img1 = "", img2 = "", img3 = "", img4 = "";
	String twitter_name, twitter_city, twitter_id, twitter_image;

	// Facebook
	Context context;
	Session mCurrentSession;
	GraphUser gUser;
	// Facebook
	
	Dialog dialogpaypal; 
	String hasPaypal = "", Fname_PayPal = "", lName_PayPal = "",
			PaypalEmail = "", msg = "";

	RelativeLayout rlMain;
	
	String TWITTER_CONSUMER_KEY = "YTJGVmQRB5EyIwO6fr9wXwLbn";
	String TWITTER_CONSUMER_SECRET = "IlbHiUQPyLXEwvdHXQXJDD0ZNMJJrLBe8IfHMja3IsTwt8QwBb";
	public static User twitterUser;
	String Twit_id;
	SocialAuthAdapter adapter;
	public String providerName;
	Profile profile;
	// Twitter

	/*private static Twitter twitter;
	private static RequestToken requestToken;
	private static twitter4j.auth.AccessToken accessToken;*/
//	private Twitter_Handler_Login mTwitter;
	
	TextView tvPlsWait;

	// int progress=0;

	int myProgress = 0, cnt = 1, total_media = 0;
	String userid = "", itemid = "", regId = "";
	String s_item = "";
	CircularSeekBar circularSeekbar;
	ImageView ivPostItem;

	double lat = 0.0, log = 0.0, Distance = 0.0, lat1, lat2, lng1, lng2,
	Dis = 0.0;
	protected LocationManager locationManager;
	String strlatitude, strlongitude;
	Location location;
	boolean isCurrentLocation = false, Successfull, isGPSEnabled = false,
	isNetworkEnabled = false, canGetLocation = false;

	String default_img="0",image = "",APILink;
	String JsonString;
	Boolean successfull=false;
	static InputStream is = null;
	static String result;
	boolean status = false;
	File file;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_item);

		DeviceID = pref.getString("registrationID", "");
		hasPaypal = pref.getString("hasPaypal", "");
		default_img= pref.getString("defaultimage", "");
		//if (DeviceID.length() == 0) {
			registerdevice();
		//}
		//getLocation();

		context = this;
		userid = pref.getString("UserID", "");
		strUserID=userid;
		DeviceID = pref.getString("registrationID", "");
		circularSeekbar.setEnabled(false);
		circularSeekbar.setFocusable(false);
		circularSeekbar.setFocusableInTouchMode(false);
		circularSeekbar.setMaxProgress(110);
		circularSeekbar.setProgress(0);
		circularSeekbar.setBackGroundColor(getResources().getColor(
				android.R.color.white));
		circularSeekbar.invalidate();
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, null)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		s_item = pref.getString("StatusItem", "N");

		CalculateTotalMedia();

		// twitter code 
		
		adapter = new SocialAuthAdapter(new ResponseListener());
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

		try {
			adapter.addConfig(Provider.TWITTER, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		// twitter code 
		
		
		
	}
	public void getLocation() {
		try {
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			Log.e("enable", "isGPSEnabled " + isGPSEnabled
					+ " isNetworkEnabled " + isNetworkEnabled);
			if (!isGPSEnabled && !isNetworkEnabled) {
				Log.e("enable", "boolean " + canGetLocation);
			} else {
				this.canGetLocation = true;
				Log.e("enable", "boolean " + canGetLocation);

				if (isNetworkEnabled) {

					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						lat = location.getLatitude();
						log = location.getLongitude();
						strlatitude = "" + lat;
						strlongitude = "" + log;
						Log.i("Wifi", "Lattitude : " + lat + ", Longitude : "
								+ log);
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
						Log.i("GPS", "Lattitude : " + lat + ", Longitude : "
								+ log);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {

			// Variable to receive message status
			Log.d("twt Demo", "Authentication Successful");

			// Get name of provider after authentication
			 providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("twt Demo", "Provider Name = " + providerName);
			//Toast.makeText(LoginActivity.this, providerName + " connected", Toast.LENGTH_SHORT).show();
			
			
			profile = adapter.getUserProfile();
			twitter_id = profile.getValidatedId();
			 twitter_name= profile.getDisplayName();
			 twitter_image= profile.getProfileImageURL();
			 
			
			 Log.e("twitter_id", ""+twitter_id);
			 Log.e("twitter_name", ""+twitter_name);
			 Log.e("twitter_image", ""+twitter_image);
			 Log.e("twitter_Email", ""+profile.getEmail());
			new TwitterBackTask().execute("");
			
			boolean status = adapter.signOut(PostItemActivity.this, providerName);
			
			Log.e("status", ":"+status);
			//btnLogout.setVisibility(View.VISIBLE);
			//btnUserInfo.setVisibility(View.VISIBLE);
			//btnLogin.setVisibility(View.GONE);
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

	public void CalculateTotalMedia() {
		
		File file;
		for (int i = 1; i < 5; i++) {
			file = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"/TapNSell/CameraImage/" + i + ".PNG");
			if (file.exists()) {
				total_media = total_media + 1;
			}

		}
		file = new File(android.os.Environment.getExternalStorageDirectory(),
				"/TapNSell/CameraVideo/TapnSellVideo.mp4");
		if (file.exists()) {
			total_media = total_media + 1;
		}
		Log.e("total_media", " " + total_media);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void registerdevice() {
		if (cd.checkConnection()) {
			DeviceID = registerDeviceInBackground(PushUtils.GCMSenderId);
		} else {
			Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG)
					.show();
		}
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
				
				if (s_item.equals("N")) {
					if (userid.length() == 0) {
						dialogCode();
					} else if (hasPaypal.equals("N")) {
						PayPaldialogCode();
					}else {

						Log.e("userid", " " + userid);
						item_name = pref.getString("item_name", "");
						item_description = pref.getString("item_description", "");
						item_condition = pref.getString("item_condition", "");
						asking_price = pref.getString("asking_price", "");
						delevery_option = pref.getString("delevery_option", "");
						address = pref.getString("address", "");
						quantity = pref.getString("quantity", "");
						category_id = pref.getString("category_id", "");
						/*latitude = pref.getString("latitude", "");
						longitude = pref.getString("longitude", "");
		*/
						new AddItemDetailBackTask().execute("");
					}
				} else {
					if (userid.length() == 0) {
						dialogCode();
					}else if (hasPaypal.equals("N")) {
						PayPaldialogCode();
					} else {
						editor.remove("StatusItem");
						editor.commit();
						Intent intent = new Intent(PostItemActivity.this,
								BackEndService.class);
						
						boolean statusBackService = isMyServiceRunning(PostItemActivity.this);
						if (statusBackService) {
							stopService(intent);
							startService(intent);
						} else {
							startService(intent);
						}
						new ShowCustomProgressBarAsyncTask().execute();
					}
				}
			};

			protected void onPreExecute() {
				progressDialog.show();
			};

			@Override
			protected String doInBackground(Object... params) {
				
				Log.v("Register", "I am in do in Background");


				try {
					locationManager = (LocationManager) context
							.getSystemService(Context.LOCATION_SERVICE);

					isGPSEnabled = locationManager
							.isProviderEnabled(LocationManager.GPS_PROVIDER);

					isNetworkEnabled = locationManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
					Log.e("enable", "isGPSEnabled " + isGPSEnabled
							+ " isNetworkEnabled " + isNetworkEnabled);
					if (!isGPSEnabled && !isNetworkEnabled) {
						Log.e("enable", "boolean " + canGetLocation);
					} else {
						PostItemActivity.this.canGetLocation = true;
						Log.e("enable", "boolean " + canGetLocation);

						if (isNetworkEnabled) {

							location = locationManager
									.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if (location != null) {
								lat = location.getLatitude();
								log = location.getLongitude();
								strlatitude = "" + lat;
								strlongitude = "" + log;
								editor.putString("lat", ""+strlongitude);
								editor.putString("log", ""+strlongitude);
								editor.commit();
								Log.i("Wifi", "Lattitude : " + lat + ", Longitude : "
										+ log);
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
								editor.putString("lat", ""+strlongitude);
								editor.putString("log", ""+strlongitude);
								editor.commit();
								Log.i("GPS", "Lattitude : " + lat + ", Longitude : "
										+ log);
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
					Log.i("Sender ID", SenderID);
					DeviceID = gcm.register(SenderID);
					Log.v("Device id", "=================>>>>>" + DeviceID);

					msg = "Device registered, registration ID=" + DeviceID;

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();

				} catch (Exception e) {
					Log.v("Device id", "=================>>>>>Not get");
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
@Override
protected void onDestroy() {
	super.onDestroy();
	
	if(mCurrentSession!=null){
		mCurrentSession.removeCallback(callback);
	}
}
	public String getDeviceId() {
		String device_id = pref.getString("DEVICE_ID", "");
		if (device_id.equalsIgnoreCase("")) {
			Log.i("Device_id_Registration", "Registration not found.");
			return "";
		} else {
			Log.i("Device_id_Registration", "Registration Found." + device_id);
		}
		return device_id;
	}

	@SuppressWarnings("static-access")
	public void dialogCode() {

		 dialog = new Dialog(PostItemActivity.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_login);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		ImageView ivTwitter = (ImageView) dialog.findViewById(R.id.ivTwitter);
		ImageView ivFacebook = (ImageView) dialog.findViewById(R.id.ivFacebook);
		ImageView ivGoogle = (ImageView) dialog.findViewById(R.id.ivGoogle);
		ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
		TextView tvSignIn = (TextView) dialog.findViewById(R.id.tvSignIn);
		TextView tvRegister = (TextView) dialog.findViewById(R.id.tvRegister);
		TextView tvHeader_Dialog = (TextView) dialog.findViewById(R.id.tvHeader_Dialog);
		
		tvHeader_Dialog.setText("Sign in to Post");
		String udata = "Sign in";
		SpannableString content = new SpannableString(udata);
		content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
		tvSignIn.setText(content);

		String udata1 = "Register";
		SpannableString content1 = new SpannableString(udata1);
		content1.setSpan(new UnderlineSpan(), 0, udata1.length(), 0);
		tvRegister.setText(content1);
		adapter.enable(ivTwitter,Provider.TWITTER);
		ivFacebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				FacebookCode();
			}
		});
		
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
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
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

	public void RegisterCode() {

		editor.putString("StatusItem", "Y");
		editor.commit();
		Intent iSignIn = new Intent(PostItemActivity.this,
				RegisterActivity.class);
		iSignIn.putExtra("status", "Home");
		startActivity(iSignIn);
		finish();
	}

	public void SingInCode() {

		editor.putString("StatusItem", "Y");
		editor.commit();

		Log.e("in preview Screen", " " + pref.getString("StatusItem", ""));
		Intent iSignIn = new Intent(PostItemActivity.this, LoginActivity.class);
		iSignIn.putExtra("status", "Home");
		startActivity(iSignIn);
		finish();

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
			LoginWithFb(PostItemActivity.this);
		} else {
			Toast.makeText(PostItemActivity.this,
					"Interner connection is not available!", Toast.LENGTH_LONG)
					.show();
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
			Log.e("Detail get successfully", "Detail get successfully");
			StatusGoogleDetails = true;
			if (gPlusclick) {
				if (cd.checkConnection()) {
					new GoogleBackTask().execute("");
				} else {
					Toast.makeText(PostItemActivity.this,
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
		// Toast.makeText(si, "User is connected!", Toast.LENGTH_LONG).show();

		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);

				google_id = currentPerson.getId();
				google_name = currentPerson.getName().getGivenName()+currentPerson.getName().getMiddleName();
				if(google_name!=null && !google_name.equals("")){
					google_name=google_name.replaceAll("null", "");
				}
				google_picture = currentPerson.getImage().getUrl().toString();
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

	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(
						PostItemActivity.this, RC_SIGN_IN);
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
			mCurrentSession = new Session(PostItemActivity.this);
		}
		Session.setActiveSession(mCurrentSession);
		if (mCurrentSession != null && mCurrentSession.isOpened()) {
			checkFBUser();
		} else {
			if (!mCurrentSession.isOpened() && !mCurrentSession.isClosed()) {
				mCurrentSession
						.openForRead(new Session.OpenRequest(
								PostItemActivity.this)
								.setCallback(callback)
								.setPermissions(
										Arrays.asList("user_birthday", "email")));
			} else {
				Session.openActiveSession(PostItemActivity.this, true, callback);
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
			mCurrentSession.onActivityResult(PostItemActivity.this,
					requestCode, resultCode, data);
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
			Log.i("FacebookFragment",
					"State is closed" + exception.getMessage());
			Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
		}
	}


	public void checkFBUser() {
		progressDialog = ProgressDialog.show(PostItemActivity.this, "",
				getResources().getString(R.string.Wait));
		progressDialog.setCancelable(false);
		progressDialog.show();
		/*Request.executeMeRequestAsync(mCurrentSession,
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
							PostItemActivity.this.gUser = user;
							FBEmail = (String) user.getProperty("email");
							Fb_id = user.getId();
							FBfName = user.getFirstName();
							FBlName = user.getLastName();
							FBimage = "http://graph.facebook.com/"
									+ user.getId() + "/picture?type=large";
							FBUserName = user.getUsername();

							
							Log.e("Email in facebook"," "+FBEmail);
							editor.putString("reg_fname",FBfName);
							editor.putString("reg_lname",FBlName);
							editor.putString("reg_email",FBEmail);
							editor.commit();

							new FacebookBackTask().execute("");

						}
					}
				});*/
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
				
					PostItemActivity.this.gUser = user;
					FBEmail = (String) user.getProperty("email");
					Fb_id = user.getId();
					FBfName = user.getFirstName();
					FBlName = user.getLastName();
					FBimage = "http://graph.facebook.com/"
							+ user.getId() + "/picture?type=large";
					FBUserName = user.getUsername();

					
					Log.e("Email in facebook"," "+FBEmail);
					editor.putString("reg_fname",FBfName);
					editor.putString("reg_lname",FBlName);
					editor.putString("reg_email",FBEmail);
					editor.commit();

					new FacebookBackTask().execute("");

				}
			}
		}).executeAsync();
		
	}

	// Facebook Code

	private class FacebookBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(PostItemActivity.this);
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
							 userFunction.VerifyFacebookFunction(strUserID, "F");
							errorMessage = "true";

						} else {
							JSONObject User = json.getJSONObject("User");
							strUserID = User.getString("userid");
							 userFunction.VerifyFacebookFunction(strUserID, "F");
							errorMessage = "false";
							message=json.optString("message");
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
				
				
				if (hasPaypal.equals("Y")) {
					editor.putString("UserID", strUserID); // Storing string
					editor.commit();
					userid = strUserID;
					Log.e("userid fb", " " + userid);
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

					new AddItemDetailBackTask().execute("");

				} else {
					userid = strUserID;
					PayPaldialogCode();
				}

			} else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {

				Toast.makeText(PostItemActivity.this, result, Toast.LENGTH_LONG)
						.show();
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	private class TwitterBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			dialog.dismiss();
			progressDialog = new ProgressDialog(PostItemActivity.this);
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
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
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

				if (hasPaypal.equals("Y")) {

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

					new AddItemDetailBackTask().execute("");
				} else {
					userid = strUserID;
					PayPaldialogCode();
				}
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else {

				Toast.makeText(PostItemActivity.this, result, Toast.LENGTH_LONG)
						.show();
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	private class GoogleBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(PostItemActivity.this);
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
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
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
							message=json.optString("message");
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

				if (hasPaypal.equals("Y")) {

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
					new AddItemDetailBackTask().execute("");

				} else {
					userid = strUserID;
					PayPaldialogCode();
				}
			} else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {

				Toast.makeText(PostItemActivity.this, message, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

/*
	private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {

		@Override
		public void onError(String value) {
			showToast("Login Failed");
			mTwitter.resetAccessToken();
		}

		@Override
		public void onComplete(String value) {
			// showTwittDialog();
			Log.i("DataTwit123", "" + user.getName() + " " + user.getLocation()
					+ " " + user.getProfileImageURL() + " " + user.getId());
			twitter_name = user.getName();

			twitter_city = user.getLocation();

			twitter_id = "" + user.getId();
			twitter_image = user.getProfileImageURL();
			new TwitterBackTask().execute("");
		}
	};

	private void registerToTwitter() {

		mTwitter = new Twitter_Handler_Login(PostItemActivity.this,
				TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
		mTwitter.setListener(mTwLoginDialogListener);

		if (mTwitter.hasAccessToken()) {
			// this will post data in asyn background thread
			// showTwittDialog();
		} else {
			mTwitter.authorize(PostItemActivity.this);
		}

	}
*/
	public class ShowCustomProgressBarAsyncTask extends
			AsyncTask<Void, Integer, Void> {

		int myProgress;

		@Override
		protected void onPostExecute(Void result) {

			tvPlsWait.setVisibility(View.INVISIBLE);
			ivPostItem.setVisibility(View.VISIBLE);
			circularSeekbar.setRingCircleColor();
			circularSeekbar.invalidate();
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent i = new Intent(PostItemActivity.this,
							SellItFasterActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}
			}, 3000);

		}

		@Override
		protected void onPreExecute() {

			// textview.setText("Start your work");
			myProgress = 10;
			circularSeekbar.setProgress(myProgress);
			circularSeekbar.invalidate();
		}

		@Override
		protected Void doInBackground(Void... params) {

			int progressStatus = 0;
			while (progressStatus < 110) {
				progressStatus++;
				publishProgress(progressStatus);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			circularSeekbar.setProgress(values[0]);
			circularSeekbar.invalidate();
		}
	}

	private class AddItemDetailBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(PostItemActivity.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			Log.e("userid", " " + userid);
			Log.e("item_name", " " + item_name);
			Log.e("item_description", " " + item_description);
			Log.e("item_condition", " " + item_condition);
			Log.e("asking_price", " " + asking_price);
			Log.e("quantity", " " + quantity);
			Log.e("delevery_option", " " + delevery_option);
			Log.e("category_id", " " + category_id);
			Log.e("address", " " + address);
			Log.e("latitude", " " + strlatitude);
			Log.e("longitude", " " + strlongitude);

			JSONObject json = userFunction.AddItemFunction(userid, item_name,
					item_description, item_condition, asking_price, quantity,
					delevery_option, category_id, address, strlatitude, strlongitude,
					"" + total_media);

			try {
				if (json!=null&&json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (res.equals(KEY_SUCCESS_STATUS)) {

						JSONObject Item = json.getJSONObject("Item");
						itemid = Item.getString("itemid");

						errorMessage = "true";
					} else {
						errorMessage = "false";
					}
				} else {
					errorMessage = "network";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

		
			if (result.equals("true")) {

			new BackendServiceBackTask().execute("");
			
			}else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else{
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}

			
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context = this;
		rlMain= (RelativeLayout) findViewById(R.id.rlMain);
		ivPostItem = (ImageView) findViewById(R.id.ivPostItem);
		circularSeekbar = (CircularSeekBar) findViewById(R.id.csBar);
		tvPlsWait = (TextView) findViewById(R.id.tvPlsWait);
	}

	@SuppressWarnings("static-access")
	public void PayPaldialogCode() {

		// custom dialog
		 dialogpaypal = new Dialog(PostItemActivity.this);
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
				
				
				editor.putString("UserID", strUserID); // Storing string
				editor.commit();

				userid = strUserID;
				Log.e("userid fb", " " + userid);
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
				new AddItemDetailBackTask().execute("");
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
						imm.hideSoftInputFromWindow(evPaypalEmail.getWindowToken(), 0);
						
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
				editor.putString("UserID", strUserID); // Storing string
				editor.putString("hasPaypal", "Y");
				hasPaypal="Y";
				editor.commit();
				userid = strUserID;
				Log.e("userid fb", " " + userid);
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

				new AddItemDetailBackTask().execute("");

			} else {
				Custom_Dialog.dialogCode(2, null, message, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

	}
	public class BackendServiceBackTask extends AsyncTask<String, Void, String> {
		String errorMessage="";
		Bitmap	pickimg=null ;
		Matrix matrix;
		byte[] ba ;
		BitmapFactory.Options option = new BitmapFactory.Options();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		protected void onPreExecute() {
			
		}

		@Override
		protected String doInBackground(String... params) {
			{

					File file = new File(
							android.os.Environment
									.getExternalStorageDirectory(),
							"/TapNSell/CameraImage/"+default_img+".PNG");
					
					Log.e("file", " "+file);
					Log.e("default_img", " "+default_img);
					if (file.exists()) {
						
						int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
						
						try {
							ExifInterface exif1 = new ExifInterface(file.getAbsolutePath());
							int orientation = exif1.getAttributeInt(
									ExifInterface.TAG_ORIENTATION, 1);
							Log.d("EXIF", "Exif: " + orientation);
							 matrix = new Matrix();
							if (orientation == 6) {
								matrix.postRotate(90);
							} else if (orientation == 3) {
								matrix.postRotate(180);
							} else if (orientation == 8) {
								matrix.postRotate(270);
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
//							
//							BitmapFactory.Options option = new BitmapFactory.Options();
//							option.inSampleSize = 2;
//								pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(),
//									option);
							
							if (file_size < 1000) {
								
								Log.e("image size is less", "image size is less");
								pickimg = BitmapFactory.decodeFile(file.getAbsolutePath());
							
							}else if (file_size < 2000) {
								
								Log.e("image size is heavy", "image size is heavy");
								
								option.inSampleSize = 2;
								pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
								Log.e("path", " " + file);
							
							} else {
								Log.e("image size is heavy", "image size is heavy");
								
								option.inSampleSize = 4;
								pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
								Log.e("path", " " + file);
							}
							
							pickimg = Bitmap.createBitmap(pickimg, 0, 0,
									pickimg.getWidth(), pickimg.getHeight(), matrix,
									true); // rotating bitmap
							pickimg.compress(CompressFormat.PNG, 100, baos);
							
							ba 	 = baos.toByteArray();
							
							

						} catch (OutOfMemoryError e) {
							e.printStackTrace();
						} catch (Exception e) {

							Log.e("Bitmap Load Error", "Failed to load");
						}
						
//				ba	=convertFileToByteArray(file);
					try {
						APILink = UserFunctions.localBaseUrl + "Items/addimage1";
						MultipartEntity multipartcontent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
						
						multipartcontent.addPart("userid", new StringBody(userid, "text/plain", Charset.forName("UTF-8")));
						multipartcontent.addPart("itemid", new StringBody(itemid, "text/plain", Charset.forName("UTF-8")));
						multipartcontent.addPart("is_default", new StringBody("Y", "text/plain", Charset.forName("UTF-8")));
						multipartcontent.addPart("type", new StringBody("I", "text/plain", Charset.forName("UTF-8")));
					
						Log.e("BA"," "+ba);
						ByteArrayBody bab = new ByteArrayBody(ba, "image/*", "ItemIage.png");
						multipartcontent.addPart("data[image]", bab);
					
						JsonString = getJsonStringMulitipart(APILink, multipartcontent);
						JSONObject jsonObject = new JSONObject(JsonString);
						successfull = jsonObject.optBoolean("successfull");
						
						
						//ByteArrayOutputStream stream =//activity.getByteArrayOutputStream();
						/*if(baos!=null){
							byte[] byteArray = baos.toByteArray();
							multipartcontent.addPart("data[image]", new ByteArrayBody(byteArray, "image/*", "driverimage.png"));
						}else{
							Log.e("image null","image null");
						}
*/
						file.delete();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(successfull){
						status = true;
					}
					}else {
						status = false;
					}
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.e("true", "true");
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if(status){
				editor.putString("itemid", itemid);
				editor.remove("item_name");
				editor.remove("item_description");
				editor.remove("item_condition");
				editor.remove("asking_price");
				editor.remove("delevery_option");
				editor.remove("address");
				editor.remove("quantity");
				editor.remove("category_id");
				editor.remove("latitude");
				editor.remove("longitude");
				editor.commit();
			}
			
			Intent intent = new Intent(PostItemActivity.this,
					BackEndService.class);
			boolean statusBackService = isMyServiceRunning(PostItemActivity.this);
			if (statusBackService) {
				stopService(intent);
				startService(intent);
			} else {
				startService(intent);
			}
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);//(RelativeLayout.LayoutParams)rlMain.getLayoutParams();
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			circularSeekbar.setLayoutParams(params);
			
			new ShowCustomProgressBarAsyncTask().execute();
		}
	}
	


	public static byte[] convertFileToByteArray(File f)
	 {
	 byte[] byteArray = null;
	 try
	 {
	 InputStream inputStream = new FileInputStream(f);
	 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 byte[] b = new byte[1024*8];
	 int bytesRead =0;
	 
	 while ((bytesRead = inputStream.read(b)) != -1)
	 {
   	bos.write(b, 0, bytesRead);
	 }

	 byteArray = bos.toByteArray();
	 }
	 catch (IOException e)
	 {
	 e.printStackTrace();
	 }
	 return byteArray;
	 }
	 public static String getJsonStringMulitipart(String ApiLink, MultipartEntity multipartentity){
		 try{
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
		     HttpPost httppost = new HttpPost(ApiLink);
		     httppost.setEntity(multipartentity);
		     
		     HttpResponse response = httpclient.execute(httppost,localContext);
		     if( response == null ){
			     
		      }else{
		    	  HttpEntity entity = response.getEntity();
		          is = entity.getContent();
		          
		      //    //Log.e("log_tag", ""+is.toString());
		      }
		 } catch(Exception e){
		     return "";
		 }
		 
		 // convert response to string
		 try {
			 BufferedReader reader = new BufferedReader(new InputStreamReader(
					 is, "iso-8859-1"), 8);
		     StringBuilder sb = new StringBuilder();
		     String line = null;
		     while ((line = reader.readLine()) != null) {
		    	 sb.append(line + "\n");
		     }
		     is.close();
		     result = sb.toString();
		     Log.e("res",result);
		 } catch (Exception e) {
			 e.printStackTrace();
			 return "";
		 }
		 return result;
	 }
    
    
}
