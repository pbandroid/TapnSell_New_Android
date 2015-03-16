package com.convertingoffers.tapnsell.MakeOffer;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapboardSell.ItemToShip_Activity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.CustomTextView;
import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ChaChingActivity extends BaseActivity implements OnClickListener {
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;

	ImageView ivFacebookShar, ivShippingInfo, ivItemImage;
	CustomTextView tvItemName;
	String itemid = "", userid = "", amount = "", image = "", name = "",offerid = "", accept = "", noti_msg = "", type = "", price = "",to_id = "", isbuyer = "", dialog_msg = "", orderid = "";
	Context context;
	Animation RightSwipe;
	Session mCurrentSession;
	public Session.StatusCallback statusCallback = new SessionStatusCallback();
	public static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cha_ching);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {

			noti_msg = bundle.getString("notification_text");
			type = bundle.getString("notification_type");
			offerid = bundle.getString("notification_offerid");
			price = bundle.getString("notification_price");
			name = bundle.getString("notification_itemname");
			to_id = bundle.getString("notification_fromid");
			itemid = bundle.getString("notification_itemid");
			image = bundle.getString("notification_itemimage");
			isbuyer = bundle.getString("notification_isbuyer");
			orderid = bundle.getString("orderid");
			name = bundle.getString("name");
		}

		
		tvItemName.setText(name);
		tvHeader.setText("Cha Ching !");
		
		iLoader_item.displayImage(image, ivItemImage, options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,Bitmap loadedImage) {
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,int current, int total) {
				}
			});
		
		ivBack.setOnClickListener(this);
		ivFacebookShar.setOnClickListener(this);
		ivShippingInfo.setOnClickListener(this);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context = this;

		iLoader_item.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.cha_ching_image_bg)
				.showImageForEmptyUri(R.drawable.cha_ching_image_bg)
				.showImageOnFail(R.drawable.cha_ching_image_bg)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		tvItemName = (CustomTextView) findViewById(R.id.tvItemName);
		ivFacebookShar = (ImageView) findViewById(R.id.ivFacebookShar);
		ivShippingInfo = (ImageView) findViewById(R.id.ivShippingInfo);
		ivItemImage = (ImageView) findViewById(R.id.ivItemImage);

	}

	@Override
	public void onClick(View v) {

		Intent i;
		switch (v.getId()) {

		case R.id.ivFacebookShar:

			buttonAnimation(ivFacebookShar);
			sharewithFb();

			break;

		case R.id.ivBack:
			buttonAnimation(ivBack);
			finish();
			break;
/*		case R.id.ivMeetUpInfo:
			buttonAnimation(ivMeetUpInfo);
			i = new Intent(context, ChouseMeetupActivity.class);
			i.putExtra("orderid", "" + orderid);
			startActivity(i);

			break;*/

		case R.id.ivShippingInfo:
			buttonAnimation(ivShippingInfo);
			i = new Intent(context, ItemToShip_Activity.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}

	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(context, R.anim.slide_left);
		v.startAnimation(RightSwipe);
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
			publishStory(mCurrentSession, "", "");
		}

	}

	boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mCurrentSession != null)
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mCurrentSession != null) {
			Session session = Session.getActiveSession();
			Session.saveSession(session, outState);
		}
	}

	public class SessionStatusCallback implements Session.StatusCallback {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
		}
	}

	public void publishStory(Session session, String string, String string2) {
		List<String> permissions = session.getPermissions();
		if (!isSubsetOf(PERMISSIONS, permissions)) {
			Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
					ChaChingActivity.this, PERMISSIONS);
			session.requestNewPublishPermissions(newPermissionsRequest);
			// return;
			
			session.addCallback(new StatusCallback() {
				
				@Override
				public void call(Session session, SessionState state, Exception exception) {

					if(session!=null && session.getPermissions().contains("publish_actions")){
						publishStory(session, "", "");
					}else{
						if(progressDialog.isShowing()){
							progressDialog.dismiss();
						}
						Toast.makeText(ChaChingActivity.this, "Please Sing in facebook with sandbox cradentials ", Toast.LENGTH_LONG).show();
						
					}
				}
			});
		}

	
		
		Bundle postParams = new Bundle();
		postParams.putString("name", "TapnSell");
		postParams.putString("caption", "Item Sold");
		postParams.putString("description","I just picked this up on TapNSell and saved BIG! Experience the World's Garage Sale #tapnsell \n http://goo.gl/k7idfJ");
//		postParams.putString("link", image);
		postParams.putString("picture",image);

		
		
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
				ChaChingActivity.this, session, postParams))
				.setOnCompleteListener(new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {

						if (error == null) {
							String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(ChaChingActivity.this,"Successfully posted on facebook",Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(ChaChingActivity.this,"Failed to share app",Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(ChaChingActivity.this,"Failed to share app", Toast.LENGTH_SHORT).show();
						}
					}
				}).build();
		feedDialog.show();
	}

	Session.StatusCallback statuscallback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.equals(SessionState.OPENED)) {
				Session.setActiveSession(session);
				publishStory(session, "", "");
			}
		}
	};

	Session openActiveSession(boolean allowLoginUI, StatusCallback callback,
			List<String> permissions) {
		OpenRequest openRequest = new OpenRequest(ChaChingActivity.this)
				.setPermissions(permissions).setCallback(callback);
		Session session = new Session.Builder(ChaChingActivity.this).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())
				|| allowLoginUI) {
			Session.setActiveSession(session);
			session.openForPublish(openRequest);
			return session;
		}
		return null;
	}

	// ///////////////////Facebook END/////////////////////////////

	/*
	 * public void publishStory() { Session session =
	 * Session.getActiveSession();
	 * 
	 * if (session != null) {
	 * 
	 * progressDialog = new ProgressDialog(context);
	 * progressDialog.setMessage("Please wait");
	 * progressDialog.setCanceledOnTouchOutside(false);
	 * progressDialog.setCancelable(false); progressDialog.show();
	 * 
	 * 
	 * Bundle postParams = new Bundle(); postParams.putString("name",
	 * "TapNsell Android"); postParams.putString("caption", "test tapnsell");
	 * postParams.putString("description",
	 * "I just listed this item on #TapNSell. Thanks!");
	 * postParams.putString("link", image);
	 * 
	 * 
	 * 
	 * Request.Callback callback = new Request.Callback() {
	 * 
	 * @SuppressWarnings("unused") public void onCompleted(Response response) {
	 * if (response != null) { GraphObject graphObject =
	 * response.getGraphObject(); if (graphObject != null) { Log.e("response",
	 * " " + response); JSONObject graphResponse = response.getGraphObject()
	 * .getInnerJSONObject(); String postId = null; try { postId =
	 * graphResponse.getString("id"); } catch (JSONException e) { if
	 * (progressDialog.isShowing()) { progressDialog.dismiss(); } Log.i(TAG,
	 * "JSON error " + e.getMessage()); } FacebookRequestError error =
	 * response.getError(); if (error != null) { if (progressDialog.isShowing())
	 * { progressDialog.dismiss(); } ivFacebookShar.setEnabled(true);
	 * ivFacebookShar.setClickable(true); ivFacebookShar.setFocusable(true);
	 * ivFacebookShar.setFocusableInTouchMode(true); Toast.makeText(context,
	 * error.getErrorMessage(), Toast.LENGTH_LONG) .show(); } else { if
	 * (progressDialog.isShowing()) { progressDialog.dismiss(); }
	 * ivFacebookShar.setEnabled(true); ivFacebookShar.setClickable(true);
	 * ivFacebookShar.setFocusable(true);
	 * ivFacebookShar.setFocusableInTouchMode(true); Log.e("test", "test");
	 * Toast.makeText(context, "Facebook posting successfully...",
	 * Toast.LENGTH_LONG).show(); } }else{
	 * 
	 * if (progressDialog.isShowing()) { progressDialog.dismiss(); }
	 * ivFacebookShar.setEnabled(true); ivFacebookShar.setClickable(true);
	 * ivFacebookShar.setFocusable(true);
	 * ivFacebookShar.setFocusableInTouchMode(true); Toast.makeText(context,
	 * "Facebook posting failed ...", Toast.LENGTH_LONG).show();
	 * 
	 * }
	 * 
	 * } else { if (progressDialog.isShowing()) { progressDialog.dismiss(); }
	 * Log.e("object null", "object null"); } } };
	 * 
	 * Request request = new Request(session, "me/feed", postParams,
	 * HttpMethod.POST, callback);
	 * 
	 * RequestAsyncTask task = new RequestAsyncTask(request); task.execute();
	 * }else{
	 * 
	 * sharewithFb();
	 * 
	 * 
	 * }
	 * 
	 * }
	 */
}
