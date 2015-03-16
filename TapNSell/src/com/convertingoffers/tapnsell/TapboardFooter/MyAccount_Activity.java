package com.convertingoffers.tapnsell.TapboardFooter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapboardSell.MessageBoard_Activity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class MyAccount_Activity extends BaseActivity implements OnClickListener,	ConnectionCallbacks, OnConnectionFailedListener {
	
	
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	byte[] ba;
	Bitmap bmp=null;
	String JsonString;
	Boolean successfull=false;
	static InputStream is = null;
	static String result;
	int RESULT_LOAD_CAMERA_IMAGE = 123,RESULT_LOAD_IMAGE = 456,result_code=000;
	
	ArrayList<String> mCamera = new ArrayList<String>();
	Dialog dialog;
	Animation RightSwipe;
	Context context;
	String APILink,picturePath="",type,userid,image,paypalemail,paypalfirstname,paypallastname,twitter_status,google,facebook,paypal,email,sms,sociallogin,profilecomplete="0";
	ImageView ivsms,ivUserImage,ivMessageBoard,ivCardDetail,ivUserreview,ivPasswordChange,ivtw,ivFb,ivPayPal,ivEmail,ivGplus,ivEdit;
	ProgressBar pb_userProfile_status;
	Adapter adapter_list;
	String Fname_PayPal,lName_PayPal,PaypalEmail,msg;
	// ///////////// Twitter parameter /////////////////////
	RelativeLayout rlMain;
	ListView lvt;
	// Twitter
	
	 String TWITTER_CONSUMER_KEY = "YTJGVmQRB5EyIwO6fr9wXwLbn";
	 String TWITTER_CONSUMER_SECRET = "IlbHiUQPyLXEwvdHXQXJDD0ZNMJJrLBe8IfHMja3IsTwt8QwBb";
	User twitterUser;
	String Twit_id;
	SocialAuthAdapter adapter;
	public String providerName;
	Profile profile;
	// ///////////// Twitter parameter /////////////////////
	
	Session mCurrentSession;
	GraphUser gUser;
	
	
	// google+
	boolean StatusGoogleDetails = false, gPlusclick = false;
	public static final int RC_SIGN_IN = 0;
	public GoogleApiClient mGoogleApiClient;
	public boolean mIntentInProgress;
	public boolean mSignInClicked;
	public ConnectionResult mConnectionResult;
	GoogleCloudMessaging gcm;
	// google+
	
	 Dialog	 dialogpaypal;
	 String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	 TextView Pb_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_account);

		
		
		mCamera.clear();
		mCamera.add("Camera");
		mCamera.add("Gallery");
	
		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this).addApi(Plus.API, null)
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		
		userid = pref.getString("UserID", "");
		tvHeader.setText("My Account");
		ivMenu.setVisibility(View.VISIBLE);
		ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.footer_menu));
		ivBack.setOnClickListener(this);
		ivtw.setOnClickListener(this);
		ivFb.setOnClickListener(this);
		ivGplus.setOnClickListener(this);
		ivEmail.setOnClickListener(this);
		ivsms.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
		ivPasswordChange.setOnClickListener(this);
		ivPayPal.setOnClickListener(this);
		ivEdit.setOnClickListener(this);
		ivUserreview.setOnClickListener(this);
		ivMessageBoard.setOnClickListener(this);
		adapter = new SocialAuthAdapter(new ResponseListener());
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		adapter.addCallBack(Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

		try {
			adapter.addConfig(Provider.TWITTER, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if (cd.checkConnection()) {new MyAccountBackTask().execute("");} else {Toast.makeText(context,"Interner connection is not available!",Toast.LENGTH_LONG).show();}
	}

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {

			// Variable to receive message status
			Log.d("twt Demo", "Authentication Successful");
			 providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("twt Demo", "Provider Name = " + providerName);
			profile = adapter.getUserProfile();
			type="T";
			new VerifyBackTask().execute("");
			
			boolean status = adapter.signOut(MyAccount_Activity.this, providerName);
			
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

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		iLoader_item.init(ImageLoaderConfiguration.createDefault(context));
		 options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.myaccount_user_default_image)
			.showImageForEmptyUri(R.drawable.myaccount_user_default_image)
			.showImageOnFail(R.drawable.myaccount_user_default_image)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		Pb_text=(TextView)findViewById(R.id.Pb_text);
		 pb_userProfile_status= (ProgressBar) findViewById(R.id.pb_userProfile_status);
		ivPasswordChange= (ImageView) findViewById(R.id.ivPasswordChange);
		ivMessageBoard= (ImageView) findViewById(R.id.ivMessageBoard);
		ivCardDetail= (ImageView) findViewById(R.id.ivCardDetail);
		ivUserreview= (ImageView) findViewById(R.id.ivUserreview);
		ivUserImage= (ImageView) findViewById(R.id.ivUserImage);
		ivEdit= (ImageView) findViewById(R.id.ivEdit);
		ivtw= (ImageView) findViewById(R.id.ivtw);
		ivFb= (ImageView) findViewById(R.id.ivFb);
		ivPayPal= (ImageView) findViewById(R.id.ivPayPal);
		ivEmail= (ImageView) findViewById(R.id.ivEmail);
		ivGplus= (ImageView) findViewById(R.id.ivGplus);
		ivsms= (ImageView) findViewById(R.id.ivsms);
	}
	public class MyAccountBackTask extends
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
				JSONObject json = userFunction.MyaccountFunction(userid);
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

//							image=UserFunctions.localImageUrl;
							image = json.getString("image");
							paypalemail= json.getString("paypalemail");
							paypalfirstname= json.getString("paypalfirstname");
							paypallastname= json.getString("paypallastname");
							twitter_status= json.getString("twitter");
							google= json.getString("google");
							facebook= json.getString("facebook");
							paypal= json.getString("paypal");
							email= json.getString("email");
							sms= json.getString("sms");
							sociallogin= json.getString("sociallogin");
							profilecomplete= json.getString("profilecomplete");
							
							errorMessage = "true";
						} else {
							message = json.optString("message");
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

//			shahil
				
				FillAllImages();
				
				
			} else {
				Custom_Dialog.dialogCode(2, null, message, context);
			}
		}

		public void FillAllImages() {
			
			
			try {
				pb_userProfile_status.setProgress(Integer.parseInt(profilecomplete));
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Pb_text.setText(profilecomplete + "%");
			/*if (image != null) {
				try {
					imageLoader.DisplayImage(image, ivUserImage);
				} catch (Exception e) {
				}
			} else {
				ivUserImage.setImageResource(R.drawable.myaccount_user_default_image);
			}*/
			

			
			
			
			// Image display using lazy loading 

			iLoader_item.displayImage(image, ivUserImage, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
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
				}
			});
			
			// Image display using lazy loading
			
			
			
			
			
			if(twitter_status.equals("Y")){
				ivtw.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_tw_active));
			}else{
				ivtw.setImageDrawable(getResources().getDrawable(R.drawable.tw_inactive));
			}
			
			if(paypal.equals("Y")){
				ivPayPal.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_paypal_active));
			}else{
				ivPayPal.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_paypal_inactive));
			}
			
			
			if(google.equals("Y")){
				ivGplus.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_gplus_active));
			}else{
				ivGplus.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_gplus_inactive));
			}
			
			if(facebook.equals("Y")){
				ivFb.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_fb_active));
			}else{
				ivFb.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_fb_inactive));
			}
			if(email.equals("Y")){
				ivEmail.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_email_active));
			}else{
				ivEmail.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_email_inactive));
			}
			if(sms.equals("Y")){
				ivsms.setImageDrawable(getResources().getDrawable(R.drawable.phone_active));
			}else{
				ivsms.setImageDrawable(getResources().getDrawable(R.drawable.phone_inactive));
			}
			
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	
	
	@Override
	public void onClick(View v) {
	Intent i;
		switch (v.getId()) {
			
		case R.id.ivBack:
			buttonAnimation(ivBack);
			finish();
		break;
		case R.id.ivEdit:
			buttonAnimation(ivEdit);
			if (cd.checkConnection()) {
				SpinnerCameraDialog();
			} else {
				Toast.makeText(context,
						"Interner connection is not available!",
						Toast.LENGTH_LONG).show();
			}
			
		break;
		
		case R.id.ivMenu:
			buttonAnimation(ivMenu);
			if (cd.checkConnection()) {
				i= new Intent(context , Setting_Activity.class);
				startActivity(i);
			} else {
				Toast.makeText(context,
						"Interner connection is not available!",
						Toast.LENGTH_LONG).show();
			}
			
			
			
		break;
		case R.id.ivtw:
			buttonAnimation(ivtw);
			
				if (cd.checkConnection()) {
					if(twitter_status.equals("N")){
					
						adapter.enable(ivtw,Provider.TWITTER);
						
				} }else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
			}
		break;
		
		case R.id.ivFb:
			buttonAnimation(ivFb);
			
			
			if (cd.checkConnection()) {
				if(facebook.equals("N")){
				FacebookCode();
				}
			} else {
				Toast.makeText(context,
						"Interner connection is not available!",
						Toast.LENGTH_LONG).show();
			
			
			}
			break;
		case R.id.ivPayPal:
			buttonAnimation(ivPayPal);
			
				if (cd.checkConnection()) {
					if(paypal.equals("N")){
					PayPaldialogCode();
					}
					} else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
			}
			
		break;
		case R.id.ivGplus:
			buttonAnimation(ivGplus);
				if (cd.checkConnection()) {
					if(google.equals("N")){
					GooglePlusCode();
				}
				}else {
					Toast.makeText(context,
							"Interner connection is not available!",Toast.LENGTH_LONG).show();
			}
		break;
		case R.id.ivsms:
			buttonAnimation(ivsms);
			
				if (cd.checkConnection()) {
					if(sms.equals("N")){
						editor.putString("Status_MyAccount", "true");
						editor.commit();
					 i = new Intent(context, VerifyPhone_Activity.class);
					  startActivity(i);
				}}
				  else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
			}
		break;
		
		case R.id.ivEmail:
			buttonAnimation(ivEmail);
		
			if (cd.checkConnection()) {
				if(email.equals("N")){
					editor.putString("Status_MyAccount", "true");
					editor.commit();
				  i = new Intent(context, VerifyEmail_Activity.class);
				  startActivity(i);
			} }else {
				Toast.makeText(context,
						"Interner connection is not available!",
						Toast.LENGTH_LONG).show();
		}
		break;
		
		case R.id.ivUserreview:
			buttonAnimation(ivUserreview);
			
				if (cd.checkConnection()) {
					i = new Intent(context, TapSellReviewUser.class);
					startActivity(i);
				} else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}
			break;

		case R.id.ivMessageBoard:
			buttonAnimation(ivMessageBoard);
			
				if (cd.checkConnection()) {
					i = new Intent(context, MessageBoard_Activity.class);
					startActivity(i);
				} else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}
			break;
			///
		case R.id.ivPasswordChange:
			buttonAnimation(ivPasswordChange);
			if (cd.checkConnection()) {
				
				if(sociallogin.equals("Y")){
					Custom_Dialog.dialogCode(2, null, "You are logged in via your social account.",context);
				}else{
					i = new Intent(context, UpdatePassword_Activity.class);
					startActivity(i);	
				}
				
			} else {
				Toast.makeText(context,
						"Interner connection is not available!",
						Toast.LENGTH_LONG).show();
			}
			break;
	
		default:
			break;
		}
	}

	
	

	@SuppressWarnings("static-access")
	public void SpinnerCameraDialog()
	{
		dialog = new Dialog(context);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.itemdetails_listview);
		dialog.setCancelable(false);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
	
//		dialog.setTitle("Select Country");
		dialog.show();
		dialog.setCanceledOnTouchOutside(true);
		lvt = (ListView) dialog.findViewById(R.id.lvDialog);
		adapter_list = new Adapter(context, mCamera);
		lvt.setAdapter(adapter_list);
		lvClickEvent();

	}
public void lvClickEvent() {
		
		lvt.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

					String type = mCamera.get(position);
					ivEdit.setClickable(true);
					ivEdit.setEnabled(true);
				if (type.equals("Camera")) {
					Intent intent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, RESULT_LOAD_CAMERA_IMAGE);
				} else {
					mygallerybtn();
				}
					
					dialog.dismiss();
					
			}
		});
	}

	protected void onResume() {
		super.onResume();

		ivEdit.setClickable(true);
		ivEdit.setEnabled(true);

		}
	public void mygallerybtn() {
		
		
		String	FILE_TYPE="image/*";
	
			Intent i = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				i.setType(FILE_TYPE);
			startActivityForResult(i, RESULT_LOAD_IMAGE);
		}
	public class Adapter extends BaseAdapter {
		public Context context;
		public ArrayList<String> val;
	

		public Adapter(Context context, ArrayList<String> val) {
			this.context = context;
			this.val = val;
			
		}

		@Override
		public int getCount() {
			
			return val.size();
		}

		@Override
		public Object getItem(int position) {
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.itemdetails_listview_listitem, parent, false);
			TextView tvSpinner = (TextView) rowView.findViewById(R.id.tvSpinner);
			tvSpinner.setText(val.get(position));
			System.out.println(val.get(position));
			return rowView;
		}
	}

	
	
	
	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(MyAccount_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
	
	public class VerifyBackTask extends AsyncTask<String, Void, String> {
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
				
				Log.e("userid", " "+userid);
				Log.e("type", " "+type);
				
				JSONObject json = userFunction.VerifyFacebookFunction(userid, type);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
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

				
				if(type.equals("F")){
					facebook="Y";
					ivFb.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_fb_active));	
				}else if(type.equals("T")){
					twitter_status="Y";
					ivtw.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_tw_active));	
				}else if(type.equals("G")){
					google="Y";
					ivGplus.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_gplus_active));
				}
				Custom_Dialog.dialogCode(2, null, message,context);
				
				
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else {
				if(type.equals("F")){
					facebook="N";
					ivFb.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_fb_inactive));	
				}else if(type.equals("T")){
					twitter_status="N";
					ivtw.setImageDrawable(getResources().getDrawable(R.drawable.tw_inactive));	
				}else if(type.equals("G")){
					google="N";
					ivGplus.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_gplus_inactive));
				}
				
				Custom_Dialog.dialogCode(2, null, message,context);
			}
	}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
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
			mCurrentSession = new Session(MyAccount_Activity.this);
		}
		Session.setActiveSession(mCurrentSession);
		if (mCurrentSession != null && mCurrentSession.isOpened()) {
			checkFBUser();
		} else {
			if (!mCurrentSession.isOpened() && !mCurrentSession.isClosed()) {
				mCurrentSession
						.openForRead(new Session.OpenRequest(
								MyAccount_Activity.this)
								.setCallback(callback)
								.setPermissions(
										Arrays.asList("user_birthday", "email")));
			} else {
				Session.openActiveSession(MyAccount_Activity.this, true, callback);
			}
		}
		//
	}

	public Session.StatusCallback callback = new Session.StatusCallback() {
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
			mCurrentSession.onActivityResult(MyAccount_Activity.this,
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
		
		if(requestCode==123 || requestCode==456){
			
		
		if (data != null) {
			result_code=requestCode;
			if(bmp!=null){
				if (requestCode == 123) {
					bmp = (Bitmap) data.getExtras().get("data");
					Matrix matrix = new Matrix();
					matrix.postRotate(0);
					
					bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
							bmp.getHeight(), matrix, true);
				Log.e("bmp", " "+bmp);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
					ba = stream.toByteArray();
					
					new EditUserImageBackTask().execute("");
				} else {

					Uri selectedImage = data.getData();
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getContentResolver().query(selectedImage,
							filePathColumn, null, null, null);
					cursor.moveToFirst();

					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					picturePath = cursor.getString(columnIndex);
					cursor.close();
					Log.e("picturePath", " " + picturePath);
					new EditUserImageBackTask().execute("");
				}
			}
			
		} else {
			picturePath = "";
		}
		}
		
	};

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
			 //Log.e("log_tag", "Error in http connection " + e.toString());
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
   

	public class EditUserImageBackTask extends AsyncTask<String, Void, String> {
		String message;

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
				File file = new File(picturePath);
				
				try {
					APILink = UserFunctions.localBaseUrl + "users/changeimage";
					MultipartEntity multipartcontent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
					
					multipartcontent.addPart("userid", new StringBody(userid, "text/plain", Charset.forName("UTF-8")));
					
					
						Log.e("in", "in");
						if(result_code==456){
							if (file.exists()) {
							 ba =convertFileToByteArray(file);	
							 ByteArrayBody bab = new ByteArrayBody(ba, "image/*", "ItemIage.png");
								multipartcontent.addPart("image", bab);
								multipartcontent.addPart("image", new StringBody("", "text/plain", Charset.forName("UTF-8")));
							}
							}else{
							Log.e("in1", "in1");
						//	 ba =convertFileToByteArray(file);	
							 ByteArrayBody bab = new ByteArrayBody(ba, "image/*", "ItemIage.png");
								multipartcontent.addPart("image", bab);
								multipartcontent.addPart("image", new StringBody("", "text/plain", Charset.forName("UTF-8")));
					
						}
						
						
					
					

					JsonString = getJsonStringMulitipart(APILink, multipartcontent);
					JSONObject jsonObject = new JSONObject(JsonString);
					successfull = jsonObject.optBoolean("successfull");
				
					
					if(successfull){
						message=jsonObject.optString("message");
					
					image="";
					image=UserFunctions.localImageUrl;
					image=image+jsonObject.optString("image");
					profilecomplete= jsonObject.optString("profilecomplete");
					Log.e("image", " "+image);
					Log.e("profilecomplete", " "+profilecomplete);
					
					Log.e("successfull", "t "+successfull);
					
					if (file.exists()) {	file.delete();}
					}else{
					
						message=jsonObject.optString("message");
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
			}
			return ""+successfull;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if(result.equals("true")){
				Log.e("result", " "+result);
				Custom_Dialog.dialogCode(2, null, message, context);
				try {
					pb_userProfile_status.setProgress(Integer.parseInt(profilecomplete));
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Pb_text.setText(profilecomplete + "%");
				
				// Image display using lazy loading 
				Log.e("image","postexecute "+ image);
				iLoader_item.displayImage(image, ivUserImage, options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
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
					}
				});
				
				// Image display using lazy loading
				
			}else{
				Custom_Dialog.dialogCode(2, null, message, context);
			}
			
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

    
	public static byte[] convertFileToByteArray(File f) {
		byte[] byteArray = null;
		try {
			InputStream inputStream = new FileInputStream(f);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024 * 8];
			int bytesRead = 0;

			while ((bytesRead = inputStream.read(b)) != -1) {
				bos.write(b, 0, bytesRead);
			}

			byteArray = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArray;
	}
	
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
		progressDialog = ProgressDialog.show(MyAccount_Activity.this, "",
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
							MyAccount_Activity.this.gUser = user;
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
							type="F";

							new VerifyBackTask().execute("");

						}
					}
				});
	}

	
	public void GooglePlusCode() {

		if (!StatusGoogleDetails) {
			gPlusclick = true;
			signInWithGplus();
		} else {
			type="G";
			new VerifyBackTask().execute("");
		}
	}
	

	public void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	public void updateUI(boolean isSignedIn) {
		if (isSignedIn) {
			Log.e("Detail get successfully", "Detail get successfully");
			StatusGoogleDetails = true;
			if (gPlusclick) {
				if (cd.checkConnection()) {
					type="G";
					if(google.equals("N")){
						new VerifyBackTask().execute("");
					}
				
				} else {
					Toast.makeText(context,"Interner connection is not available!",Toast.LENGTH_LONG).show();
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
			/*	Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
*/
//				google_id = currentPerson.getId();
//				google_name = currentPerson.getName().toString();
//				google_picture = currentPerson.getImage().toString();
//				google_gender = "" + currentPerson.getGender();
//				device_type = "A";

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
/*
	public boolean isMyServiceRunning(Context context) {
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
	}*/

	public void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(
						MyAccount_Activity.this, RC_SIGN_IN);
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


	@SuppressWarnings("static-access")
	@SuppressLint("InlinedApi") 
	public void PayPaldialogCode() {

		// custom dialog
	 dialogpaypal = new Dialog(context);
		dialogpaypal.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
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
				buttonAnimation(v);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						evPaypalEmail.getWindowToken(), 0);
				dialogpaypal.dismiss();
				
			}
		});
		ivCont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				buttonAnimation(v);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(evPaypalEmail.getWindowToken(), 0);
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


	public class AddPayPalInfoBackTask extends AsyncTask<String, Void, String> {
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
				ivPayPal.setImageDrawable(getResources().getDrawable(R.drawable.myaccount_paypal_active));
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

}
