package com.convertingoffers.tapnsell.TapboardSell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.twitter.Twitt_Sharing;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.MomentUtil;
import com.convertingoffers.tapnsell.util.UserFunctions;
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

public class MyEarning_Activity extends BaseActivity implements
		PlusClient.ConnectionCallbacks, PlusClient.OnConnectionFailedListener,
		DialogInterface.OnCancelListener, OnClickListener {

	public ProgressDialog progressDialog;
	public String KEY_SUCCESS = "successfull", KEY_SUCCESS_STATUS = "true",
			strmsg = "";
	ImageView ivFb, ivGooglePlus, ivTwitter, ivBack;
	
	String msg,earn;
	Bitmap bitmapFromUrl=null;
	// /////////////Google plus parameter /////////////////
	
	Boolean StatusofGooglePlus = false;
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
//	
	String strMsg = "",userid="",
			strDescription = "";
	String TWITTER_CONSUMER_KEY = "YTJGVmQRB5EyIwO6fr9wXwLbn";
	String TWITTER_CONSUMER_SECRET = "IlbHiUQPyLXEwvdHXQXJDD0ZNMJJrLBe8IfHMja3IsTwt8QwBb";
	File casted_image;
	String string_img_url = null, string_msg = null;
	TextView tvEarn;
	MemoryCache memoryCache;
	Context context;
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_earnings);
		
		userid = pref.getString("UserID", "");
		mPlusClient = new PlusClient.Builder(this, this, this).setActions(
				MomentUtil.ACTIONS).build();
		mSharing = savedInstanceState != null
				&& savedInstanceState.getBoolean(STATE_SHARING, false);
		int available = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (available != ConnectionResult.SUCCESS) {
			showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
		}
		tvHeader.setText("My Earnings");
		ivMenu.setVisibility(View.VISIBLE);
		ivBack.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
		ivFb.setOnClickListener(this);
		ivGooglePlus.setOnClickListener(this);
		ivTwitter.setOnClickListener(this);
	
		if (cd.checkConnection()) {
			new MyEarnBackTask().execute("");
		} else {
			Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
	}
	
		
	}

	@Override
	public void onClick(View v) {
		Intent	i;
		switch (v.getId()) {
		case R.id.ivFb:
				
			if (cd.checkConnection()) {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Please wait");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
				sharewithFb();	
			} else {
				Toast.makeText(MyEarning_Activity.this,"Interner connection is not available!",Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.ivGooglePlus:
			StatusofGooglePlus = true;
			googleSharCode();
			break;
		case R.id.ivTwitter:
			
		//	onClickTwitt();
		
			
			if (isNetworkAvailable()) {
				twitt = new Twitt_Sharing(MyEarning_Activity.this, TWITTER_CONSUMER_KEY,
						TWITTER_CONSUMER_SECRET);
				if (earn.equals("0")) {
					msg = "I'm turning unwanted stuff into cash with TapNSell! Download Now!"
							+ "\n http://goo.gl/k7idfJ";
				} else {
					msg = "I earned $"
							+ earn
							+ " from TapNSell. Download TapnSell now to start earning!"
							+ "\n http://goo.gl/k7idfJ";
				}

				string_msg = strDescription;

			
				
				File new_image = null;
				Bitmap bmp = null;
				
					bmp = drawableToBitmap(getResources().getDrawable(R.drawable.ic_launcher));
					new_image = Bitmap_to_File(bmp);
				
				ivTwitter.setBackgroundResource(R.drawable.twitter__active);
				
		
				if(earn.equals("0")){
					msg = "I'm turning unwanted stuff into cash with TapNSell! Download Now!" +
					"\n http://goo.gl/k7idfJ";
				}else{
					msg = "I earned $"+earn+" from TapNSell. Download TapnSell now to start earning!" +
					"\n http://goo.gl/k7idfJ";
				}
			    twitt.shareToTwitter(msg, new_image);
			} else {
				showToast("No Network Connection Available !!!");
			}
			
			    
			break;
		case R.id.ivBack:
				i = new Intent(context, TapBoardActivity.class);
			startActivity(i);
			finish();
			break;
		case R.id.ivMenu:
		i = new Intent(context, TapBoardActivity.class);
			startActivity(i);
			finish();
			break;
			
		default:
			break;
		}
	}
	  private void showToast(String msg) {
			Toast.makeText(MyEarning_Activity.this, msg, Toast.LENGTH_LONG).show();

		    }
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivFb = (ImageView) findViewById(R.id.ivFb);
		ivGooglePlus = (ImageView) findViewById(R.id.ivGooglePlus);
		ivTwitter = (ImageView) findViewById(R.id.ivTwitter);
		tvEarn= (TextView) findViewById(R.id.tvEarn);
	}

	// /////////////////////Facebook Start/////////////////////////////

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
			}

		}
	};

	Session openActiveSession(boolean allowLoginUI, StatusCallback callback,
			List<String> permissions) {
		OpenRequest openRequest = new OpenRequest(MyEarning_Activity.this)
				.setPermissions(permissions).setCallback(callback);
		Session session = new Session.Builder(MyEarning_Activity.this).build();
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
					REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES, MyEarning_Activity.this);
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
			Toast.makeText(MyEarning_Activity.this,
					"Unable to sign the user in. Please try again later..",
					Toast.LENGTH_LONG).show();
		}
	}
	
	private Intent getInteractivePostIntent() {

		//bmp = BitmapFactory.decodeFile(url);
		File new_image = null;
		Uri _image=null;
		Log.e("bmp", " "+bitmapFromUrl);
		if(bitmapFromUrl!=null){
		new_image = Bitmap_to_File(bitmapFromUrl);
		 _image = Uri.fromFile(new_image);
		}
		String action = "/?view=true";
		Uri callToActionUrl = Uri
				.parse(getString(R.string.plus_example_deep_link_url) + action);
		String callToActionDeepLinkId = getString(R.string.plus_example_deep_link_id)
				+ action;

		// Create an interactive post builder.
		PlusShare.Builder builder = new PlusShare.Builder(this, mPlusClient);

		// Set call-to-action metadata.
		builder.addCallToAction(LABEL_VIEW_ITEM, callToActionUrl,
				callToActionDeepLinkId);

		// Set the target url (for desktop use).
		builder.setContentUrl(Uri.parse(UserFunctions.LogoUrl));

		// Set the target deep-link ID (for mobile use).
		builder.setContentDeepLinkId(
				getString(R.string.plus_example_deep_link_id), null, null, null);

		// Set the pre-filled message.
		//msg="I just listed this item on TapNSell. Thanks!";
		
		if (earn.equals("0")) {
			msg = "I'm turning unwanted stuff into cash with TapNSell! Download Now!"
					+ "\n http://goo.gl/k7idfJ";
		} else {
			msg = "I earned $"
					+ earn
					+ " from TapNSell. Download TapnSell now to start earning!"
					+ "\n http://goo.gl/k7idfJ";
		}
		builder.setText(msg);

		
		builder.setStream(_image);
		
		ivGooglePlus.setBackgroundResource(R.drawable.google_plus__active);
		return builder.getIntent();
	}
	
	
	public File Bitmap_to_File(Bitmap bitmapImage) {
	    	 try {
	    		 
	    		 if(bitmapImage!=null){
	    			 
	    		
	    	File rootSdDirectory = Environment.getExternalStorageDirectory();

		    casted_image = new File(rootSdDirectory, "attachment.jpg");
		    if (casted_image.exists()) {
			casted_image.delete();
		    }	   
				casted_image.createNewFile();
				FileOutputStream fos = new FileOutputStream(casted_image);
				
				bitmapImage.compress(Bitmap.CompressFormat.PNG, 40, fos);
			    fos.flush();
			    fos.close();
			    return casted_image;
	    		 }
	    		 } catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				 System.out.print(e);
			}
	    	 
	    	return casted_image;
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


	public void publishStory() {
		Session session = Session.getActiveSession();

		if (session != null) {

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
							publishStory();
						}else{
							if(progressDialog.isShowing()){
								progressDialog.dismiss();
							}
							Toast.makeText(MyEarning_Activity.this, "Please Sing in facebook with sandbox cradentials ", Toast.LENGTH_LONG).show();
							
						}
					}
				});
				return;
			}
			Bundle postParams = new Bundle();
			postParams.putString("name", "TapnSell");
			postParams.putString("caption", "My Earnings");
			if(earn.equals("0")){
				msg = "I'm turning unwanted stuff into cash with TapNSell! Download Now!" +
				"\n http://goo.gl/k7idfJ";
			}else{
				strDescription = "I earned $"+earn+" from TapNSell. Download TapnSell now to start earning!" +
						"\n http://goo.gl/k7idfJ" ;
				
			}
			postParams.putString("description",strDescription);
			postParams.putString("picture",UserFunctions.LogoUrl);
			postParams.putString("link", "http://play.google.com/store/apps/details?id=com.convertingoffers.tapnsell");
			
		
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
							Toast.makeText(MyEarning_Activity.this,
									error.getErrorMessage(), Toast.LENGTH_LONG)
									.show();
							if (progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
						} else {
							Log.e("test", "test");
							Toast.makeText(MyEarning_Activity.this,
									"Facebook posting successfully...",
									Toast.LENGTH_LONG).show();
							if (progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
						}
						 }else{
							 Toast.makeText(MyEarning_Activity.this,
									"Facebook posting failed ...",
									Toast.LENGTH_LONG).show();
							 
							 if (progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
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

	// facebook code end

	// / twitter code ////////

	
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
		//Twitter shahil
	/*public void onClickTwitt() {
		if (isNetworkAvailable()) {
			twitt = new Twitt_Sharing(MyEarning_Activity.this, consumer_key,
					secret_key);

			string_img_url = Userfunction.logourl;
			strDescription = "I earn $"+earn  +" from TapNSell.Can you help me by retweeting? Thanks!";
			string_msg = strDescription;
			String_to_File(string_img_url);
			twitt.shareOnTwitter(string_msg,);

		} else {
			showToast("No Network Connection Available !!!");
		}
	}*/

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

	/*private void showToast(String msg) {
		Toast.makeText(MyEarning_Activity.this, msg, Toast.LENGTH_LONG).show();
	}*/

	public File String_to_File(String img_url) {

		try {
			File rootSdDirectory = Environment.getExternalStorageDirectory();

			casted_image = new File(rootSdDirectory, "attachment.jpg");
			if (casted_image.exists()) {
				casted_image.delete();
			}
			casted_image.createNewFile();

			FileOutputStream fos = new FileOutputStream(casted_image);

			URL url = new URL(img_url);
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
			return casted_image;

		} catch (Exception e) {

			System.out.print(e);
		}
		return casted_image;
	}
	
	private class MyEarnBackTask extends AsyncTask<String, Void, String> {
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
			
				JSONObject json = userFunction.MyEarnFunction(userid);
				
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							earn=json.getString("amount");
							errorMessage = "true";

						} else {

							msg=json.getString("message");
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
				tvEarn.setText("$"+earn);
			} else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else {
				Custom_Dialog.dialogCode(2, null, msg, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
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
