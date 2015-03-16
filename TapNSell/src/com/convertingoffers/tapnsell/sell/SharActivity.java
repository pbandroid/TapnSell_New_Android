package com.convertingoffers.tapnsell.sell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.User;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapboardSell.MyListingActive_Sold_Expire_Activity;
import com.convertingoffers.tapnsell.twitter.Twitt_Sharing;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.MomentUtil;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusShare;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;

public class SharActivity extends BaseActivity implements
		PlusClient.ConnectionCallbacks, PlusClient.OnConnectionFailedListener,
		DialogInterface.OnCancelListener, OnClickListener {

	public ProgressDialog progressDialog;
	public String KEY_SUCCESS = "successfull", KEY_SUCCESS_STATUS = "true",userid,
			strmsg = "";
	ImageView ivFb, ivGooglePlus, ivTwitter, ivBack,ivCnt;
	RelativeLayout rlNext;
	String msg;
	Bitmap bitmapFromUrl=null;
	// /////////////Google plus parameter /////////////////
	Boolean StatusofGooglePlus = false,boolfb=false;
	protected static final String TAG = "ShareActivity";
	private static final String STATE_SHARING = "state_sharing";
	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
	private static final int REQUEST_CODE_SIGN_IN = 1;
	private static final int REQUEST_CODE_INTERACTIVE_POST = 2;
	private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 3;
	private static final String LABEL_VIEW_ITEM = "VIEW_ITEM";
	private boolean mSharing;
	private PlusClient mPlusClient;
	boolean onceSharBtnClick = false;
	// /////////////Google plus parameter ///////////////////

	// /////////////facebook start parameter /////////////////

	Session mCurrentSession;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static final List<String> PERMISSIONS = Arrays.asList(
			 "publish_actions");
	boolean pendingPublishReauthorization;
	// /////////////facebook end parameter /////////////////

	// ///////////// Twitter parameter /////////////////////
	Twitt_Sharing twitt;
	public boolean TwitterLoginStatus= false;;
	String ItemName="",strMsg = "",Copy_Relist,RemoveID="",strDescription = "";
	
	String TWITTER_CONSUMER_KEY = "YTJGVmQRB5EyIwO6fr9wXwLbn";
	String TWITTER_CONSUMER_SECRET = "IlbHiUQPyLXEwvdHXQXJDD0ZNMJJrLBe8IfHMja3IsTwt8QwBb";
	public static User twitterUser;
	String Twit_id,SharUrl,SharImage;
	SocialAuthAdapter adapter;
	public String providerName;
	Profile profile;
	
	
	File casted_image;
	String string_img_url = "", string_msg = "";
	MemoryCache memoryCache;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
		
		userid = pref.getString("UserID", "");
		SharUrl = pref.getString("SharUrl", "");
		SharImage = pref.getString("SharImage", "");
		ItemName= pref.getString("itemname", "");
		Log.e("share url", " "+SharUrl);
		mPlusClient = new PlusClient.Builder(this, this, this).setActions(
				MomentUtil.ACTIONS).build();
		mSharing = savedInstanceState != null
				&& savedInstanceState.getBoolean(STATE_SHARING, false);
		int available = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (available != ConnectionResult.SUCCESS) {
			showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
		}

		ivBack.setOnClickListener(this);
		ivFb.setOnClickListener(this);
		ivGooglePlus.setOnClickListener(this);
		ivTwitter.setOnClickListener(this);
		ivCnt.setOnClickListener(this);
		
		Copy_Relist=pref.getString("Copy_Relist", "");
		RemoveID=pref.getString("RemoveID", "");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new  UrlBitmap().executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, "");
		} else {
			new  UrlBitmap().execute("");
		}
		
	
	// twitter code 
		
	
		
	
		// twitter code 
		
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ivFb:
			
			if(boolfb){
				Custom_Dialog.dialogCode(2, null, "Item already posted on facebook", SharActivity.this);
			}else{
			if (cd.checkConnection()) {
				sharewithFb();	
			} else {
				Toast.makeText(SharActivity.this,"Interner connection is not available!",Toast.LENGTH_LONG).show();
			}
			}
			break;
		case R.id.ivGooglePlus:
			StatusofGooglePlus = true;
			googleSharCode();
			
//			share_image_text_GPLUS();
			break;
		case R.id.ivTwitter:
			
			TwitterLoginStatus=false;
			ivTwitter.setBackgroundResource(R.drawable.twitter_inactive);
			
			if (isNetworkAvailable()) {
				twitt = new Twitt_Sharing(SharActivity.this, TWITTER_CONSUMER_KEY,
						TWITTER_CONSUMER_SECRET);

				strDescription = " I just listed this item on #TapNSell. " +
						"Can you help me by retweeting? Thanks! \n"+SharUrl;
				string_msg = strDescription;
				Log.e("SharImage", "'SharImage "+SharImage);
			

			} else {
				showToast("No Network Connection Available !!!");
			}
				
				File new_image = null;
				Bitmap bmp = null;
				if(SharImage.length()>0){
					string_img_url = SharImage;	
					new_image = Bitmap_to_File(bitmapFromUrl);
				}else{
					bmp = drawableToBitmap(getResources().getDrawable(R.drawable.ic_launcher));
					new_image = Bitmap_to_File(bmp);
				}
				ivTwitter.setBackgroundResource(R.drawable.twitter__active);
			    twitt.shareToTwitter(strDescription, new_image);
		
			
			break;
			
		case R.id.ivBack:
			finish();
			break;
		case R.id.ivCnt:
			if(Copy_Relist.equals("Y")){
				Intent i = new Intent(SharActivity.this, MyListingActive_Sold_Expire_Activity.class);
				startActivity(i);
				finish();	
			}else if(Copy_Relist.equals("N")){
				
				if(cd.checkConnection()){
					new DeleteItemBackTask().execute("");
				}else{
					Toast.makeText(SharActivity.this, "internet_conn_failed",Toast.LENGTH_LONG).show();
				}
			}else{
				Intent i = new Intent(SharActivity.this, nice.class);
				startActivity(i);
				finish();
			}
			break;
			
	/*	case R.id.ivShar:
			Log.e("boolFacebook", " " + boolFacebook);
		
			
				onceSharBtnClick = true;

				if (boolFacebook) {
				//	twitt.shar(this); // new FacebookBackTask().execute("");
					//Log.e("adapter.getCurrentProvider()","provider "+adapter.getCurrentProvider());
					//adapter.updateStatus("TapnSell Shar on Twitter", new MessageListener(), false);
					publishStory();
				} else if (!TwitterLoginStatus || !boolFacebook) {
					
					if(Copy_Relist.equals("Y")){
						Intent i = new Intent(SharActivity.this, MyListingActive_Sold_Expire_Activity.class);
						startActivity(i);
						finish();	
					}else if(Copy_Relist.equals("N")){
						
						if(cd.checkConnection()){
							new DeleteItemBackTask().execute("");
						}else{
							Toast.makeText(SharActivity.this, "internet_conn_failed",Toast.LENGTH_LONG).show();
						}
					}else{
						Intent i = new Intent(SharActivity.this, nice.class);
						startActivity(i);
						finish();
					}
					
				} else {
					Toast.makeText(
							SharActivity.this,
							"Facebook sharing failed please try again later",
							Toast.LENGTH_LONG).show();
				}
			
			break;*/
		default:
			break;
		}
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivFb = (ImageView) findViewById(R.id.ivFb);
		ivGooglePlus = (ImageView) findViewById(R.id.ivGooglePlus);
		ivTwitter = (ImageView) findViewById(R.id.ivTwitter);
		ivCnt= (ImageView) findViewById(R.id.ivCnt);
//		ivShar = (ImageView) findViewById(R.id.ivShar);
	}

	// /////////////////////Facebook Start/////////////////////////////

	private class DeleteItemBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SharActivity.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				JSONObject json=null;
			
					json = userFunction.DeleteItemFromExpireListFunction(userid, RemoveID);	
				
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
//							message=json.getString("message");
							
							errorMessage = "true";
						} else {
//							message=json.optString("message");
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
			Intent i = new Intent(SharActivity.this, MyListingActive_Sold_Expire_Activity.class);
			startActivity(i);
			finish();	
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	
	public void sharewithFb() {
		mCurrentSession = Session.getActiveSession();
		if (mCurrentSession == null) {
			mCurrentSession = new Session(this);
			Session.setActiveSession(mCurrentSession);
			if (!mCurrentSession.isOpened() && !mCurrentSession.isClosed()) {
				mCurrentSession.openForRead(new Session.OpenRequest(this)
						.setCallback(statuscallback));
			}
		} else if (!mCurrentSession.isOpened()) {
			openActiveSession(true, statuscallback, PERMISSIONS);
		} else {
			// publishStory(mCurrentSession,"","");
			publishStory();
			ivFb.setBackgroundResource(R.drawable.facebook_active);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mCurrentSession != null)
			Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mCurrentSession != null)
			Session.getActiveSession().removeCallback(statusCallback);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mCurrentSession != null) {
			Session session = Session.getActiveSession();
			Session.saveSession(session, outState);
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

		}
	}

	Session.StatusCallback statuscallback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.equals(SessionState.OPENED)) {
				Session.setActiveSession(session);
				// publishStory(session,"","");
				publishStory();
				ivFb.setBackgroundResource(R.drawable.facebook_active);
			}

		}
	};

	Session openActiveSession(boolean allowLoginUI, StatusCallback callback,
			List<String> permissions) {
		OpenRequest openRequest = new OpenRequest(SharActivity.this)
				.setPermissions(permissions).setCallback(callback);
		Session session = new Session.Builder(SharActivity.this).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())
				|| allowLoginUI) {
			Session.setActiveSession(session);
			session.openForPublish(openRequest);
			return session;
		}
		return null;
	}

	// ///////////////////Facebook END/////////////////////////////

	// /////////////////////// Google plus sharing code
	// Start//////////////////////////////////

	private void googleSharCode() {

		if (!mPlusClient.isConnected()) {
			// Set sharing so that the share is started in onConnected.
			mSharing = true;

			if (!mPlusClient.isConnecting()) {
				mPlusClient.connect();
			}
		} else {
			startActivityForResult(getInteractivePostIntent(),
					REQUEST_CODE_INTERACTIVE_POST);
		}
	}

	@SuppressWarnings("deprecation")
	protected Dialog onCreateDialog(int id) {
		if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES) {
			return super.onCreateDialog(id);
		}

		int available = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (available == ConnectionResult.SUCCESS) {

			return null;
		}
		if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
			return GooglePlayServicesUtil.getErrorDialog(available, this,
					REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES, SharActivity.this);
		}
		return new AlertDialog.Builder(this)
				.setMessage("Sign in with Google is not available.")
				.setCancelable(true).setOnCancelListener(this).create();
	}

	private void handleResult(int resultCode) {
		if (resultCode == RESULT_OK) {
			// onActivityResult is called after onStart (but onStart is not
			// guaranteed to be called while signing in), so we should make
			// sure we're not already connecting before we call connect again.
			if (!mPlusClient.isConnecting() && !mPlusClient.isConnected()) {
				mPlusClient.connect();
			}
		} else {
			Log.e(TAG, "Unable to sign the user in.");
			// finish();
			Toast.makeText(SharActivity.this,
					"Unable to sign the user in. Please try again later..",
					Toast.LENGTH_LONG).show();
		}
	}
	
	private Intent getInteractivePostIntent() {

		//bmp = BitmapFactory.decodeFile(url);
		File new_image = null;
		Uri _image=null;
		
		if(bitmapFromUrl!=null){
		new_image = Bitmap_to_File(bitmapFromUrl);
		 _image = Uri.fromFile(new_image);
		}else{
			Log.e("bitmapFromUrl", "bitmapFromUrl is null");
		}
		
		String action = "/?view=true";
		Uri callToActionUrl = Uri
				.parse(getString(R.string.plus_example_deep_link_url) + action);
		String callToActionDeepLinkId = getString(R.string.plus_example_deep_link_id)
				+ action;

		// Create an interactive post builder.
		PlusShare.Builder builder = new PlusShare.Builder(this, mPlusClient);
		// Set call-to-action metadata.
		builder.addCallToAction(LABEL_VIEW_ITEM, callToActionUrl,callToActionDeepLinkId);

		// Set the target url (for desktop use).
		builder.setContentUrl(Uri.parse(SharImage));

		// Set the target deep-link ID (for mobile use).
		builder.setContentDeepLinkId(getString(R.string.plus_example_deep_link_id), null, null, null);

		// Set the pre-filled message.
		msg="I just listed this on TapNSell. Can you help me by sharing? Thanks! \n"+SharUrl;
		builder.setText(msg);

		builder.setStream(_image);
		
		ivGooglePlus.setBackgroundResource(R.drawable.google_plus__active);
		return builder.getIntent();
	}
	
	
	
	public void onConnected(Bundle connectionHint) {
		if (!mSharing) {
			// The share button hasn't been clicked yet.
			return;
		}

		mSharing = false;
		startActivityForResult(getInteractivePostIntent(),
				REQUEST_CODE_INTERACTIVE_POST);
	}

	public void onDisconnected() {
		// Do nothing.
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mSharing) {
			return;
		}

		try {
			result.startResolutionForResult(this, REQUEST_CODE_SIGN_IN);
		} catch (IntentSender.SendIntentException e) {
			// Try to connect again and get another intent to start.
			mPlusClient.connect();
		}
	}
	
	public void share_image_text_GPLUS() {
	    File pictureFile;

	    try {
	        File rootSdDirectory = Environment.getExternalStorageDirectory();

	        pictureFile = new File(rootSdDirectory, "attachment.jpg");
	        if (pictureFile.exists()) {
	            pictureFile.delete();
	        }
	        pictureFile.createNewFile();

	        FileOutputStream fos = new FileOutputStream(pictureFile);

	        URL url = new URL(SharImage);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        connection.setDoOutput(true);
	        connection.connect();
	        InputStream in = connection.getInputStream();

	        byte[] buffer = new byte[1024];
	        int size = 0;
	        while ((size = in.read(buffer)) > 0) {
	            fos.write(buffer, 0, size);
	        }
	        fos.close();

	    } catch (Exception e) {

	        System.out.print(e);
	        // e.printStackTrace();
	        return;
	    }

	    Uri pictureUri = Uri.fromFile(pictureFile);

	    Intent shareIntent = ShareCompat.IntentBuilder.from(this)
	            .setText("Hello from Google+!").setType("image/jpeg")
	            .setStream(pictureUri).getIntent()
	            .setPackage("com.google.android.apps.plus");
	    startActivity(shareIntent);
	}

	@Override
	public void onCancel(DialogInterface dialogInterface) {
		Log.e(TAG, "Unable to sign the user in.");
		finish();
	}

	// ////////////////////////////// Google plus sharing code	// end///////////////////////////

	// ////////////////////////////// facebook code start	// ///////////////////////////////////

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (mCurrentSession != null)
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);

		if (StatusofGooglePlus) {
			switch (requestCode) {
			case REQUEST_CODE_SIGN_IN:
			case REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES:
				handleResult(resultCode);
				break;

			case REQUEST_CODE_INTERACTIVE_POST:
				mSharing = false;
				if (resultCode != RESULT_OK) {
					Log.e(TAG, "Failed to create interactive post");
				}
				break;
			}
		}
	}

	/*
	 * public void onPostExecute(String result) {
	 * 
	 * if (progressDialog.isShowing()) { progressDialog.dismiss(); }
	 * 
	 * if(result.equals("true")){ Toast.makeText(SharActivity.this,
	 * "Facebook posted success fully",Toast.LENGTH_LONG).show(); Intent i = new
	 * Intent(SharActivity.this, nice.class); startActivity(i); finish(); }else{
	 * Toast.makeText(SharActivity.this,
	 * "facebook posting failed",Toast.LENGTH_LONG).show(); }
	 * 
	 * }
	 */

	public void publishStory() {
		Session session = Session.getActiveSession();
		progressDialog = new ProgressDialog(SharActivity.this);
		progressDialog.setMessage("Please wait");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		if(!progressDialog.isShowing()){
			progressDialog.show();	
		}
		
		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				session.addCallback(new StatusCallback() {
					
					@Override
					public void call(Session session, SessionState state, Exception exception) {
 
						if(session!=null && session.getPermissions().contains("publish_actions")){
						//	publishStory();
						}else{
							if(progressDialog.isShowing()){
								progressDialog.dismiss();
							}
							Toast.makeText(SharActivity.this, "Please Sing in facebook with sandbox cradentials ", Toast.LENGTH_LONG).show();
							Intent i = new Intent(SharActivity.this, nice.class);
							startActivity(i);
							finish();
						}
					}
				});
				return;
			}
			Bundle postParams = new Bundle();
			postParams.putString("name", "TapnSell");
			postParams.putString("caption",ItemName);
			postParams.putString("description","I just listed this on TapNSell." +
					" Can you help me by liking this post? Thanks! \n"+SharUrl);
			postParams.putString("link", SharUrl);
			postParams.putString("picture",SharImage);

			/*
			 * postParams.putString("name", "Facebook SDK for Android");
			 * postParams.putString("caption",
			 * "Build great social apps and get more installs.");
			 * postParams.putString("description",
			 * "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps."
			 * ); postParams.putString("link",
			 * "https://developers.facebook.com/android");
			 * postParams.putString("picture",
			 * "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png"
			 * );
			 */
			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					if (response != null) {
						 GraphObject graphObject = response.getGraphObject();
						 if (graphObject != null) {
						Log.e("response", " " + response);
						JSONObject graphResponse = response.getGraphObject()
								.getInnerJSONObject();
						String postId = null;
						try {
							postId = graphResponse.getString("id");
						} catch (JSONException e) {
							Log.i(TAG, "JSON error "+postId + e.getMessage());
						}
						FacebookRequestError error = response.getError();
						if (error != null) {
							if(progressDialog.isShowing()){
								progressDialog.dismiss();
							}
							Toast.makeText(SharActivity.this,
									error.getErrorMessage(), Toast.LENGTH_LONG)
									.show();
						} else {
							if(progressDialog.isShowing()){
								progressDialog.dismiss();
							}
							Log.e("test", "test");
							boolfb=true;
							Toast.makeText(SharActivity.this,
									"Facebook posting successfully...",
									Toast.LENGTH_LONG).show();
						}
						 }else{
							 if(progressDialog.isShowing()){
									progressDialog.dismiss();
								}
							 Toast.makeText(SharActivity.this,
									"Facebook posting failed ...",
									Toast.LENGTH_LONG).show();
						 }
						 
					} else {
						if (progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						
						Log.e("object null", "object null");
					}
				}
			};

			Request request = new Request(session, "me/feed", postParams,
					HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}

	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	// com shahil
	/*
	 * public void publishStory() {
	 * 
	 * //new FacebookBackTask().execute("");
	 * 
	 * 
	 * 
	 * // publishStory();
	 * 
	 * if (boolFacebook) { Session session = Session.getActiveSession();
	 * 
	 * if (session != null) {
	 * 
	 * Bundle postParams = new Bundle(); postParams.putString("name",
	 * "   testShahil tapnsell "); postParams.putString("caption",
	 * "  testShahil tapnsell "); postParams .putString( "description",
	 * "  testShahil tapnsell"); postParams.putString("link",
	 * "https://developers.facebook.com/android"); postParams
	 * .putString("picture",
	 * "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png"
	 * );
	 * 
	 * Request.Callback callback = new Request.Callback() { public void
	 * onCompleted(Response response) { if (response != null) {
	 * 
	 * GraphObject graphObject = response.getGraphObject(); if (graphObject !=
	 * null) { Log.e("response ", " " + response); JSONObject graphResponse =
	 * response .getGraphObject().getInnerJSONObject();
	 * 
	 * String postId = null; try { postId = graphResponse.getString("id"); }
	 * catch (JSONException e) { Log.i("json error", "JSON error " +
	 * e.getMessage());
	 * 
	 * } FacebookRequestError error = response .getError(); if (error != null) {
	 * Toast.makeText(SharActivity.this, error.getErrorMessage(),
	 * Toast.LENGTH_LONG).show();
	 * 
	 * } else { Toast.makeText(SharActivity.this, postId,
	 * Toast.LENGTH_LONG).show();
	 * 
	 * } } } else {
	 * 
	 * }
	 * 
	 * mCurrentSession = null; } };
	 * 
	 * Request request = new Request(session, "me/feed", postParams,
	 * HttpMethod.POST, callback);
	 * 
	 * RequestAsyncTask task = new RequestAsyncTask(request); task.execute(); }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * }
	 */// com shahil
	/*
	 * protected void publishStory(Session session, String string, String
	 * string2) { List<String> permissions = session.getPermissions(); if
	 * (!isSubsetOf(PERMISSIONS, permissions)) { Session.NewPermissionsRequest
	 * newPermissionsRequest = new Session
	 * .NewPermissionsRequest(SharActivity.this, PERMISSIONS);
	 * session.requestNewPublishPermissions(newPermissionsRequest); //return; }
	 * callback strDescription="test sharing facebook"; Bundle postParams = new
	 * Bundle(); postParams.putString("name", "TapNSell App");
	 * postParams.putString("description",strDescription);
	 * postParams.putString("link", url);
	 * 
	 * WebDialog feedDialog = (new
	 * WebDialog.FeedDialogBuilder(SharActivity.this, session,
	 * postParams)).setOnCompleteListener(new OnCompleteListener() {
	 * 
	 * @Override public void onComplete(Bundle values, FacebookException error)
	 * {
	 * 
	 * 
	 * 
	 * if (error == null) { String postId = values.getString("post_id"); if
	 * (postId != null) { Toast.makeText(SharActivity.this,
	 * "Successfully posted on facebook", Toast.LENGTH_SHORT).show(); } else {
	 * Toast.makeText(SharActivity.this, "Failed to share app",
	 * Toast.LENGTH_SHORT).show(); } } else { Toast.makeText(SharActivity.this,
	 * "Failed to share app", Toast.LENGTH_SHORT).show(); } } }).build();
	 * feedDialog.show();
	 * 
	 * 
	 * }
	 * 
	 * Session openActiveSession(boolean allowLoginUI, StatusCallback callback,
	 * List<String> permissions) { OpenRequest openRequest = new
	 * OpenRequest(HomeActivity
	 * .this).setPermissions(permissions).setCallback(callback); Session session
	 * = new Session.Builder(HomeActivity.this).build(); if
	 * (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) ||
	 * allowLoginUI) { Session.setActiveSession(session);
	 * session.openForPublish(openRequest); return session; } return null; }
	 */
	// facebook code end

	// / twitter code ////////

	private class UrlBitmap extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SharActivity.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
			Log.e("SharUrl SharUrl SharUrl", " "+SharUrl);
				getBitmapFromURL(SharImage);
			}
			return errorMessage;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
		

		}
	}
	
	 public File Bitmap_to_File(Bitmap bitmapImage) {
    	 try {
    		 
    	File rootSdDirectory = Environment.getExternalStorageDirectory();

	    casted_image = new File(rootSdDirectory, "attachment.PNG");
	    if (casted_image.exists()) {
		casted_image.delete();
	    }	   
			casted_image.createNewFile();
			FileOutputStream fos = new FileOutputStream(casted_image);
			if(bitmapImage!=null){
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 50, fos);
			}
		    fos.flush();
		    fos.close();
		    return casted_image;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 System.out.print(e);
		}

    	return casted_image;
    }
	public  Bitmap getBitmapFromURL(String src) {
		
		try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        bitmapFromUrl = BitmapFactory.decodeStream(input);
	        return bitmapFromUrl;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
/*	public void onClickTwitt() {
		if (isNetworkAvailable()) {
			twitt = new Twitt_Sharing(SharActivity.this, consumer_key,
					secret_key);

			string_img_url = "http://3.bp.blogspot.com/_Y8u09A7q7DU/S-o0pf4EqwI/AAAAAAAAFHI/PdRKv8iaq70/s1600/id-do-anything-logo.jpg";
			strDescription = "test sharing";
			string_msg = strDescription;
			String_to_File(string_img_url);
			twitt.shareToTwitter(string_msg);

		} else {
			showToast("No Network Connection Available !!!");
		}
	}*/



public void LoginInTwitter() {
		if(TwitterLoginStatus){
			TwitterLoginStatus=false;
			ivTwitter.setBackgroundResource(R.drawable.twitter_inactive);
		}else{
			TwitterLoginStatus = true;
			ivTwitter.setBackgroundResource(R.drawable.twitter__active);
		}
		
	}


	public void onClickTwitt() {
		if (isNetworkAvailable()) {
			twitt = new Twitt_Sharing(SharActivity.this, TWITTER_CONSUMER_KEY,
					TWITTER_CONSUMER_SECRET);

			strDescription = "I just listed this item on TapnSell. Thanks!";
			string_msg = strDescription;
			Log.e("SharImage", "'SharImage "+SharImage);
			//String_to_File(SharImage);

		} else {
			showToast("No Network Connection Available !!!");
		}
	}
/*	public void onClickTwitt() {
		if (isNetworkAvailable()) {
		    Twitt_Sharing twitt = new Twitt_Sharing(SharActivity.this,
		    		TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
		    
		     // string_img_url = "http://3.bp.blogspot.com/_Y8u09A7q7DU/S-o0pf4EqwI/AAAAAAAAFHI/PdRKv8iaq70/s1600/id-do-anything-logo.jpg";
		     //  string_img_url = "http://www.peerdevelopment.com/apps/fightapp/images/ic_launcher.png";

		    strDescription="I just listed this item on TapnSell. Thanks!"; 
		    //  strDescription="sadsadsad asdsadsad"; 
		    string_msg = strDescription;
		    // here we have web url image so we have to make it as file to

		    String_to_File(SharImage);
		    // Now share both message & image to sharing activity
		    twitt.shareToTwitter(strDescription);
		    TwitterLoginStatus = true;
			ivTwitter.setBackgroundResource(R.drawable.twitter__active);

		} else {
		    showToast("No Network Connection Available !!!");
		}
	    }
*/
	    // when user will click on twitte then first that will check that is
	    // internet exist or not
	    public boolean isNetworkAvailable() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
		    return false;
		} else {
		    NetworkInfo[] info = connectivity.getAllNetworkInfo();
		    if (info != null) {
			for (int i = 0; i < info.length; i++) {
			    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
				return true;
			    }
			}
		    }
		}
		return false;
	    }

	    private void showToast(String msg) {
		Toast.makeText(SharActivity.this, msg, Toast.LENGTH_LONG).show();

	    }

	    // this function will make your image to file
	 //   public File String_to_File(String img_url) {}


		public Bitmap drawableToBitmap (Drawable drawable) {
	        if (drawable instanceof BitmapDrawable) {
	            return ((BitmapDrawable)drawable).getBitmap();
	        }

	        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	        Canvas canvas = new Canvas(bitmap); 
	        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	        drawable.draw(canvas);

	        return bitmap;
	    }
	    
}
