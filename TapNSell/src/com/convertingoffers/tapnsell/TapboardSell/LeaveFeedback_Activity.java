package com.convertingoffers.tapnsell.TapboardSell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Modal.LeaveFeedbackListModal;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.ImageLoaderTopBottamRounded;
import com.convertingoffers.tapnsell.util.RoundedImageView_CenterCrop;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class LeaveFeedback_Activity extends BaseActivity implements OnClickListener {

	protected ImageLoader iLoader_Rounded = ImageLoader.getInstance();
	Bitmap image_bitmap=null;
	public static DisplayImageOptions options_rounded,option;
	boolean pendingPublishReauthorization;
	String rattingname="",strUserImage="";
	Animation RightSwipe;
	Context context;
	String userid;
	ArrayList<LeaveFeedbackListModal> mList= new ArrayList<LeaveFeedbackListModal>();
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	
	String  itemid,orderid,toid,name,price,username,userimage,distance,image,	strRatting_UserName, strRatting_Image, strRatting_Review="",
	strRatting_ItemId, strRatting_Fromid, strRatting_toid,strRatting_orderid="",
	strRatting_Ratting;
	ImageLoaderTopBottamRounded 	imageloader_top_bottam_round;
	ListView lvMessage;
	LeaveFeedbackListCustomAdapter 	adapter;
	
	protected static final String TAG = "ShareActivity";
	Session mCurrentSession;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static final List<String> PERMISSIONS = Arrays.asList(
			 "publish_actions");
	
	int temp_pos=0;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_board);

		userid = pref.getString("UserID", "");
		tvHeader.setText("Leave Feedback");
		ivMenu.setVisibility(View.VISIBLE);

		iLoader_Rounded.init(ImageLoaderConfiguration.createDefault(this));
		options_rounded = new DisplayImageOptions.Builder()
				/*.showImageOnLoading(R.drawable.profile_user_defualtimage)
				.showImageForEmptyUri(R.drawable.profile_user_defualtimage)
				.showImageOnFail(R.drawable.profile_user_defualtimage)*/
				.resetViewBeforeLoading(true)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true)
				.build();
		
		option = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.needfeedback_default_bg)
		.showImageForEmptyUri(R.drawable.needfeedback_default_bg)
		.showImageOnFail(R.drawable.needfeedback_default_bg)
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.build();
		
		LoadImage();
		
		
		
		ivBack.setOnClickListener(this);
		ivMenu.setOnClickListener(this);

		
		
		
		 
		 
		 
		if (cd.checkConnection()) {
			LeaveFeedbackBackTask task=new LeaveFeedbackBackTask();
			 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		     else
		         task.execute();
		} else {
			Toast.makeText(LeaveFeedback_Activity.this,
					"Interner connection is not available!",
					Toast.LENGTH_LONG).show();
		}

	
	}

	private void LoadImage() {
		
		image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.message_user_icon);
	}
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		lvMessage=(ListView)findViewById(R.id.lvMessage);
		imageloader_top_bottam_round = new ImageLoaderTopBottamRounded(context);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {

		case R.id.ivBack:
			i=new Intent(context, TODOS_Activity.class);
			startActivity(i);
			finish();
			break;
		case R.id.ivMenu:
			 i =  new Intent(context, TapBoardActivity.class);
			startActivity(i);
			finish();
			break;
			
	
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
	super.onBackPressed();
	Intent i=new Intent(context, TODOS_Activity.class);
	startActivity(i);
	finish();
	}
	private class LeaveFeedbackBackTask extends
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
				if (userid.length() == 0) {
					userid = "0";
				}
//				userid="50";
				Log.e("userid", " " + userid);
				JSONObject json = userFunction.LeaveFeedbackFunction(userid);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONArray Item= json.getJSONArray("Item");
							for (int i = 0; i < Item.length(); i++) {
								JSONObject Jmsg= Item.getJSONObject(i);
								
								
								itemid=Jmsg.getString("itemid");
								//userimage=UserFunctions.localImageUrl;
								userimage=Jmsg.getString("userimage");
								orderid=Jmsg.getString("orderid");
								toid=Jmsg.getString("userid");
								name=Jmsg.getString("name");
								price=Jmsg.getString("price");
								username=Jmsg.getString("username");
								distance=Jmsg.getString("distance");
								image=UserFunctions.localImageUrl;
								image=image+Jmsg.getString("image");
								
								mList.add(new LeaveFeedbackListModal(itemid, orderid, toid, name, price, username, userimage, distance, image));
								
							}
							
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
				
				adapter = new LeaveFeedbackListCustomAdapter (context,R.layout.leave_feedback, mList);
				lvMessage.setAdapter(adapter);
			
				
				
			} else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else {
				Custom_Dialog.dialogCode(2, null,message,
						LeaveFeedback_Activity.this);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	public class LeaveFeedbackListCustomAdapter extends ArrayAdapter<LeaveFeedbackListModal> {
		ViewHolder holder;
		private ArrayList<LeaveFeedbackListModal> listSubCategories;
		protected Object mysun;
		
		public LeaveFeedbackListCustomAdapter(Context context, int textViewResourceId,
				ArrayList<LeaveFeedbackListModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<LeaveFeedbackListModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivMyOrderImage,ivUserImage;
			RelativeLayout rlRate;
			TextView tvMyOrderItemName,tvName;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.leave_feedback, null);	
				
				holder.rlRate = (RelativeLayout) convertView.findViewById(R.id.rlRate);
				holder.ivMyOrderImage = (ImageView) convertView.findViewById(R.id.ivMyOrderImage);
				holder.ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
				holder.tvMyOrderItemName=(TextView)convertView.findViewById(R.id.tvMyOrderItemName);
				holder.tvName=(TextView)convertView.findViewById(R.id.tvName);
				
				
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		
			LeaveFeedbackListModal m_ItemListing;
			m_ItemListing  = listSubCategories.get(position);
			
			String UserImage= "";
		
			UserImage=m_ItemListing.getUserimage();
			
			
			Log.e("UserImage", ""+UserImage);
				if(UserImage.length()>0){
					
					iLoader_Rounded.displayImage(UserImage, holder.ivMyOrderImage, options_rounded, loadImageListener);
				}else{
				
					iLoader_Rounded.displayImage("", holder.ivMyOrderImage, options_rounded, loadImageListener);
				}	
				holder.tvMyOrderItemName.setText(m_ItemListing.getName());	
				
				holder.tvName.setText("Rate "+m_ItemListing.getUsername());	
				rattingname=m_ItemListing.getUsername();
				Log.e("UserImage", ""+UserImage);
			String img = "";
			img=m_ItemListing.getImage();
			/*	if (img != null) {
					try {
						imageLoader.DisplayImage(img, holder.ivUserImage);
					} catch (Exception e) {
					}
				} else {
					holder.ivUserImage.setImageResource(R.drawable.needfeedback_default_bg);
				}*/
			
				
				// Image display using lazy loading 
			
			
				iLoader_Rounded.displayImage(img, holder.ivUserImage , option, new SimpleImageLoadingListener() {
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
				
				
				
				
				
			holder.rlRate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
//					buttonAnimation(holder.rlRate);
					
					
					temp_pos=position;
					
					LeaveFeedbackListModal m_item = listSubCategories.get(position);
					strRatting_ItemId = m_item.getItemid();
					strRatting_toid = m_item.getUserid();
					strRatting_UserName = m_item.getUsername();
					strRatting_Image = m_item.getUserimage();
					strRatting_orderid= m_item.getOrderid();
					strRatting_Fromid = userid;
					String image="";
						image=m_item.getImage().toString();
					if(image.length()==0){
						strUserImage=UserFunctions.LogoUrl;
					}else{
						strUserImage=m_item.getImage();	
					}
					NiceReviewdialogCode();
					//NiceReviewFaceBookShardialogCode();
					
				
					
				}
			});
			
			return convertView;

		}

	}
	
	@SuppressWarnings("static-access")
	private void NiceReviewdialogCode() {
		// custom dialog
		final Dialog dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.feedback_dialog);
		dialog.setCancelable(false);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
		ImageView ivRateNow = (ImageView) dialog.findViewById(R.id.ivRateNow);
		ImageView ivUserIcon = (ImageView) dialog.findViewById(R.id.ivUserIcon);
		TextView tvUserName = (TextView) dialog.findViewById(R.id.tvUserName);
		final RatingBar rb_User_review_Item = (RatingBar) dialog
				.findViewById(R.id.rb_User_review_Item);
		final EditText evFeedback_share_exp = (EditText) dialog
				.findViewById(R.id.evFeedback_share_exp);

		tvUserName.setText(strRatting_UserName);

		imageloader_top_bottam_round = new ImageLoaderTopBottamRounded(context);

		
		
		if(strRatting_Image.length()>0){
			iLoader_Rounded.displayImage(strRatting_Image, ivUserIcon, options_rounded, loadImageListener);
		}else{
			iLoader_Rounded.displayImage("", ivUserIcon, options_rounded, loadImageListener);
		}	
		
		

		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(evFeedback_share_exp.getWindowToken(), 0);
		
				dialog.dismiss();
			}
		});

		ivRateNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				
				
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(evFeedback_share_exp.getWindowToken(), 0);
		
				strRatting_Review = evFeedback_share_exp.getText().toString()
						.trim();
				strRatting_Ratting = "" + rb_User_review_Item.getRating();
			
				if(strRatting_Review.length()==0){
					Custom_Dialog.dialogCode(2, null,"Please share your experience.",
							LeaveFeedback_Activity.this);
				}else{
					dialog.dismiss();
					
					if (cd.checkConnection()) {
						new RattingReviewBackTask().execute("");
					} else {
						Toast.makeText(context, "internet_conn_failed",
								Toast.LENGTH_LONG).show();
					}	
				}
					
			}
			});

		dialog.show();
	}

	
	private class RattingReviewBackTask extends AsyncTask<String, Void, String> {
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

				Log.e("strRatting_ItemId", " " + strRatting_ItemId);
				Log.e("strRatting_Fromid", " " + strRatting_Fromid);
				Log.e("strRatting_toid", " " + strRatting_toid);
				Log.e("strRatting_Review", " " + strRatting_Review);
				Log.e("strRatting_Ratting", " " + strRatting_Ratting);

				JSONObject json = userFunction.RattingFunction(
						strRatting_ItemId, strRatting_Fromid, strRatting_toid,
						"", strRatting_Review, strRatting_Ratting,strRatting_orderid);
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
//				Custom_Dialog.dialogCode(2, null,
//						"Ratting Posted successfully.", context);
				
				mList.remove(temp_pos);
			adapter.notifyDataSetChanged();
				NiceReviewFaceBookShardialogCode();
			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		} else {
				Custom_Dialog.dialogCode(2, null, message, context);
				//NiceReviewFaceBookShardialogCode();
				// ValidationSingleButton("Error in posting");
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	@SuppressWarnings("static-access")
	private void NiceReviewFaceBookShardialogCode() {
		// custom dialog
		final Dialog dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_nice_review);
		dialog.setCancelable(false);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
		ImageView ivFacebookShar = (ImageView) dialog
				.findViewById(R.id.ivFacebookShar);

		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		ivFacebookShar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Please wait");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(false);
				progressDialog.show();
				
				if (cd.checkConnection()) {
					sharewithFb();
				} else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		dialog.show();
	}

	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(LeaveFeedback_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
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
		OpenRequest openRequest = new OpenRequest(LeaveFeedback_Activity.this)
				.setPermissions(permissions).setCallback(callback);
		Session session = new Session.Builder(LeaveFeedback_Activity.this).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())
				|| allowLoginUI) {
			Session.setActiveSession(session);
			session.openForPublish(openRequest);
			return session;
		}
		return null;
	}

	public void publishStory() {
		Session session = Session.getActiveSession();
		/*progressDialog = new ProgressDialog(LeaveFeedback_Activity.this);
		progressDialog.setMessage("Please wait");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		progressDialog.show();*/
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
							publishStory();
						}else{
							if(progressDialog.isShowing()){
								progressDialog.dismiss();
							}
							Toast.makeText(LeaveFeedback_Activity.this, "Please Sing in facebook with sandbox cradentials ", Toast.LENGTH_LONG).show();
							
						}
					}
				});
				return;
			}
			
			Bundle postParams = new Bundle();
			postParams.putString("name", "TapnSell");
			postParams.putString("caption", "Ratting Item");
			postParams.putString("description","I just liked this on TapNSell. Experience the World's Garage Sale #tapnsell");
			postParams.putString("picture","https://peerdevelopment.com/apps/tapnsell/img/logo.png");
//			postParams.putString("link", strUserImage);
			Log.e("strUserImage", " "+strUserImage);
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
					Log.e("response", " "+response);
					if (response != null) {
						
						GraphObject graphObject = response.getGraphObject();
						Log.e("graphObject", " "+graphObject);
						if (graphObject != null) {
//							Log.e("response", " " + response);
							JSONObject graphResponse = response
									.getGraphObject().getInnerJSONObject();
							String postId = null;
							try {
								postId = graphResponse.getString("id");
							} catch (JSONException e) {
								Log.i(TAG, "JSON error "+postId + e.getMessage());
							}
							FacebookRequestError error = response.getError();
							if (error != null) {
								
								if (progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
								
								Toast.makeText(context,error.getErrorMessage(),Toast.LENGTH_LONG).show();
							} else {
								
								if (progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
								
								Log.e("test", "test");
								Toast.makeText(context,"Facebook posting successfully...",Toast.LENGTH_LONG).show();
							}
						} else {
							
							if (progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							publishStory();
							Toast.makeText(context,"Facebook posting failed ...",Toast.LENGTH_LONG).show();

						}
						

					} else {
						if(progressDialog.isShowing()){
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (mCurrentSession != null)
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);

	}

}
