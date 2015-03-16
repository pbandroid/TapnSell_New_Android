package com.convertingoffers.tapnsell.Shop.checkout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Meetup.MeetupRequest;
import com.convertingoffers.tapnsell.Modal.SearchModel;
import com.convertingoffers.tapnsell.Shop.ChatActivity;
import com.convertingoffers.tapnsell.Shop.ProductDetails;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.ConnectionDetector;
import com.convertingoffers.tapnsell.util.CustomTextView;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.ImageLoaderforRounded;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class OrderCompletedActivity extends BaseActivity implements OnClickListener{
	Context context;
	TextView tvOrderNo;
	ImageView ivItem,ivFb,ivMessage,ivMeetup;
	String orderid;
	int count=0;
	SearchModel myCateForfav;
	Custom_Dialog custom_dialog;
	DisplayImageOptions options;
	String FacebookImage="";
/////////////facebook start parameter /////////////////
	protected static final String TAG = "OrderComplete";
	Session mCurrentSession;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static final List<String> PERMISSIONS = Arrays.asList(
	"publish_actions");
	boolean pendingPublishReauthorization;
		// /////////////facebook end parameter /////////////////

	TableLayout tblItemList;
	ArrayList<String> mItem_IdList = new ArrayList<String>();

	// Base fragment 
	View view;
	public ImageLoaderforRounded imageLoader;
	public String is_last,itemid,struserid,name,price,price_type,distance,reserved,has_like,no_of_likes,latitude="",longitude="",intStartNewListing="0",userid="",type="P",KEY_SUCCESS = "successfull",KEY_SUCCESS_STATUS = "true",strmsg="",image="",ItemId="",from_id="",position="";
	ArrayList<SearchModel> mNewListing = new ArrayList<SearchModel>();
	public TextView tvHeader;
	public ImageView ivMenu,ivBack;
	NewListingCustomAdaper adapter;
	boolean status_billing =false,status_billing_onepage_check=false;
	// Base fragment
	ArrayList<String> mItemList = new ArrayList<String>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.order_complete);
			
			
			editor.remove("S_ComefromonePageCheck");
			editor.remove("ComefromonePageCheck");
			 editor.commit();
			
			ivBack.setVisibility(View.GONE);
			ivMenu.setVisibility(View.VISIBLE);
			
			
			
			Bundle b = getIntent().getExtras();
			if(b!=null){
				
				ItemId = b.getString("itemid");	
				from_id= b.getString("from_id");	
				mItemList= (ArrayList<String>) b.getSerializable("ItemArray");
				position=b.getString("position");	
				orderid= b.getString("orderid");
			}
			
			tvHeader.setText("Order Complete");
		
			image=pref.getString("CHKImage", "");
			tvOrderNo.setText("Order# "+orderid);
			/*if (image != null) {
				try {
					imageLoader.DisplayImage(image, ivItem);
				} catch (Exception e) {
				}
			} else {
				ivItem.setImageResource(R.drawable.ic_launcher);
			}*/
			FacebookImage=image;
			ImageLoader.getInstance()
			.displayImage(image, ivItem, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					//holder.pbimage.setProgress(0);
//					holder.pbimage.setIndeterminate(true);
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
			
			
			userid = pref.getString("UserID", "");
			latitude = pref.getString("lat", "");
			longitude = pref.getString("long", "");
			
			ivFb.setOnClickListener(this);
			ivMenu.setOnClickListener(this);
			ivMessage.setOnClickListener(this);
			ivMeetup.setOnClickListener(this);
			
			if (cd.checkConnection()) {
				new NewListingBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
			}
	}
	
	@Override
	public void onContentChanged() {
			super.onContentChanged();
			
			
			context=this;
			options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.transperant)
			.showImageForEmptyUri(R.drawable.transperant)
			.showImageOnFail(R.drawable.transperant)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
			
				
				
			tvOrderNo=(TextView)findViewById(R.id.tvOrderNo);
			ivItem=(ImageView)findViewById(R.id.ivItem);
			ivFb=(ImageView)findViewById(R.id.ivFb);
			ivMessage=(ImageView)findViewById(R.id.ivMessage);
			ivMeetup=(ImageView)findViewById(R.id.ivMeetup);
			
			tblItemList=(TableLayout)findViewById(R.id.tblItemList);
			// Base fragment Initialize 
			ivMenu=(ImageView)findViewById(R.id.ivMenu);
			ivBack=(ImageView)findViewById(R.id.ivBack);
			tvHeader= (TextView)findViewById(R.id.tvHeader);
	        imageLoader = new ImageLoaderforRounded(context);
	        userFunction = new UserFunctions();
	    	cd = new ConnectionDetector(context);
	    	pref =PreferenceManager.getDefaultSharedPreferences(context);
	    	editor = pref.edit();
	    	// Base fragment Initialize
	}
	
	
	@Override
	public void onClick(View v) {
		
		Intent i;
		switch (v.getId()) {
		case R.id.ivMenu:
			
			i = new Intent(OrderCompletedActivity.this, TapBoardActivity.class);
			startActivity(i);
			finish();
			break;
	case R.id.ivMeetup:
			Log.e("Orderid Meetup", orderid);
			i = new Intent(OrderCompletedActivity.this, MeetupRequest.class);
			i.putExtra("orderid", orderid);
			startActivity(i);
		
			break;
			
	case R.id.ivFb:
		
		ivFb.setEnabled(false);
		ivFb.setClickable(false);
		ivFb.setFocusable(false);
		ivFb.setFocusableInTouchMode(false);
		publishStory();
			break;
	case R.id.ivMessage:
		
	
		Intent iMessage = new Intent(context,
				ChatActivity.class);
		iMessage.putExtra("itemid",ItemId);
		iMessage.putExtra("from_id", from_id);
		iMessage.putExtra("Distance","0");
		iMessage.putExtra("ItemArray",  mItemList);
		iMessage.putExtra("position", ""+position);
		startActivity(iMessage);
			break;	
		default:
		}
	
	}
	
	public void publishStory() {
		Session session = Session.getActiveSession();

		if (session != null) {

			progressDialog = new ProgressDialog(OrderCompletedActivity.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
			
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
							Toast.makeText(OrderCompletedActivity.this, "Please Sing in facebook with sandbox cradentials ", Toast.LENGTH_LONG).show();
						
						}
					}
				});
				return;
			}
			Bundle postParams = new Bundle();
			postParams.putString("name", "TapnSell");
			postParams.putString("caption", "Purchase Item");
			postParams.putString("description","I just picked this up on TapNSell and saved BIG! Experience the World's Garage Sale #tapnsell \n http://play.google.com/store/apps/details?id=com.convertingoffers.tapnsell");
			postParams.putString("picture",FacebookImage);
			Log.e("image", " "+FacebookImage);
			
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
							Log.e("postId", ""+postId);
						} catch (JSONException e) {
							if (progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							Log.i(TAG, "JSON error " + e.getMessage());
						}
						FacebookRequestError error = response.getError();
						if (error != null) {
							if (progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							ivFb.setEnabled(true);
							ivFb.setClickable(true);
							ivFb.setFocusable(true);
							ivFb.setFocusableInTouchMode(true);
							Toast.makeText(OrderCompletedActivity.this,
									error.getErrorMessage(), Toast.LENGTH_LONG)
									.show();
						} else {
							if (progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							ivFb.setEnabled(true);
							ivFb.setClickable(true);
							ivFb.setFocusable(true);
							ivFb.setFocusableInTouchMode(true);
							Log.e("test", "test");
							Toast.makeText(OrderCompletedActivity.this,
									"Facebook posting successfully...",
									Toast.LENGTH_LONG).show();
						}
						 }else{
							 
							 if (progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
								ivFb.setEnabled(true);
								ivFb.setClickable(true);
								ivFb.setFocusable(true);
								ivFb.setFocusableInTouchMode(true);
							 Toast.makeText(OrderCompletedActivity.this,
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
		}else{
			
			sharewithFb();

			//Toast.makeText(getActivity(), "login fail", Toast.LENGTH_LONG).show();
		}

	}

	public void sharewithFb() {
		mCurrentSession = Session.getActiveSession();
		if (mCurrentSession == null) {
			mCurrentSession = new Session(OrderCompletedActivity.this);
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

	// /////////////////////Facebook Start/////////////////////////////
	
	private boolean isSubsetOf(Collection<String> subset,Collection<String> superset) {
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
	public void onSaveInstanceState(Bundle outState) {
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
				 publishStory();
			}

		}
	};

	Session openActiveSession(boolean allowLoginUI, StatusCallback callback,
			List<String> permissions) {
		OpenRequest openRequest = new OpenRequest(OrderCompletedActivity.this)
				.setPermissions(permissions).setCallback(callback);
		Session session = new Session.Builder(OrderCompletedActivity.this).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())
				|| allowLoginUI) {
			Session.setActiveSession(session);
			session.openForPublish(openRequest);
			return session;
		}
		return null;
	}

	// ///////////////////Facebook END/////////////////////////////

	
	
	
	private class NewListingBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json;
				
			
				
				Log.e("userid"," "+userid);
				Log.e("latitude"," "+latitude);
				Log.e("longitude"," "+longitude);
				Log.e("start"," "+intStartNewListing);
				Log.e("limit","50");
				Log.e("type"," "+type);
				
					json = userFunction.TapInspireFunction
					(userid, latitude,longitude,""+intStartNewListing,type);
				
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						is_last =json.optString("is_last"); // json.get("is_last");
						
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONArray Item = json.getJSONArray("Item");

							for (int i = 0; i < Item.length(); i++) {

								JSONObject c = Item.getJSONObject(i);
								itemid = c.getString("itemid");
								struserid = c.getString("userid");
								name = c.getString("name");
								price = c.getString("price");
								distance = c.getString("distance");
								image = UserFunctions.localImageUrl;
								image = image + c.optString("image");
								reserved= c.getString("reserved");
								has_like = c.getString("has_like");
								no_of_likes = c.getString("no_of_likes");

								mNewListing.add(new SearchModel(itemid,
										struserid, name, price, distance,
										image, has_like, no_of_likes,
										price_type,reserved));
							}

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
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			if (result.equals("true")) {

				AddSublistItem(mNewListing);
			}else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}  else {
				Custom_Dialog.dialogCode(2, null, "No items found", context);
//				ValidationSingleButton("no records found....");
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	public void AddSublistItem(ArrayList<SearchModel> myList) {
		tblItemList.removeAllViews();
		TextView tvPrice1,tvPrice2,tvName1,tvName2;
		ImageView ivItem1,ivItem2;
		
		LayoutInflater inflater;
		for (int i = 0; i < myList.size(); i=i+2) {
			inflater = getLayoutInflater();
			View convertView = (View) inflater.inflate(
					R.layout.order_complete_item, null);
			
			
			 tvPrice1 = (TextView) convertView.findViewById(R.id.tvPrice1);
			 tvPrice2 = (TextView) convertView.findViewById(R.id.tvPrice2);
			 tvName1 = (TextView) convertView.findViewById(R.id.tvName1);
			 tvName2 = (TextView) convertView.findViewById(R.id.tvName2);
			 ivItem1 = (ImageView) convertView.findViewById(R.id.ivItem1);
			 ivItem2 = (ImageView) convertView.findViewById(R.id.ivItem2);
//			 rlMain1= (RelativeLayout) convertView.findViewById(R.id.rlMain1);
//			 rlMain2= (RelativeLayout) convertView.findViewById(R.id.rlMain2);
			
		
			 
//			 if(i%2==0){
				 SearchModel myValues = myList.get(i);
				 tvPrice1.setText(myValues.getPrice());
				 tvName1.setText(myValues.getName());
				 image=myValues.getImage();
					/*if (image != null) {
						try {
							imageLoader.DisplayImage(image, ivItem1);
						} catch (Exception e) {
						}
					} else {
						ivItem1.setImageResource(R.drawable.order_complete_tap_inspire_image_bg);
					}*/
					
					
					ImageLoader.getInstance()
					.displayImage(image, ivItem1, options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							//holder.pbimage.setProgress(0);
//							holder.pbimage.setIndeterminate(true);
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
		//	 }
			 
			// if(i%2!=0){
					if(i+1<myList.size()){
						
					
				 SearchModel m_list= myList.get(i+1);
				 tvPrice2.setText(m_list.getPrice());
				 tvName2.setText(m_list.getName());
				 image=m_list.getImage();
					/*if (image != null) {
						try {
							imageLoader.DisplayImage(image, ivItem2);
						} catch (Exception e) {
						}
					} else {
						ivItem2.setImageResource(R.drawable.order_complete_tap_inspire_image_bg);
					}*/
					
					ImageLoader.getInstance()
					.displayImage(image, ivItem2, options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							//holder.pbimage.setProgress(0);
//							holder.pbimage.setIndeterminate(true);
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
					}
			// }
				tblItemList.addView(convertView);
		}
	}
	
	private class NewListingCustomAdaper extends ArrayAdapter<SearchModel> {
		ViewHolder holder;
		private ArrayList<SearchModel> listSubCategories;

		public NewListingCustomAdaper(Context context, int textViewResourceId,
				ArrayList<SearchModel> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<SearchModel>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivItem;
			TextView  tvPrice;
			RelativeLayout  rlParent;
			CustomTextView tvName;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.order_complete_item, null);

				holder = new ViewHolder();
				
				holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
				holder.ivItem = (ImageView) convertView.findViewById(R.id.ivItem);
				holder.tvName = (CustomTextView) convertView.findViewById(R.id.tvName);
				holder.rlParent = (RelativeLayout) convertView.findViewById(R.id.rlParent);
				
				
				
				
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			SearchModel myCate = listSubCategories.get(position);
			
			itemid = myCate.getItemid();
			struserid = myCate.getUserid();
			name = myCate.getName();
			price = myCate.getPrice();
			price_type = myCate.getPrice_type();
			distance = myCate.getDistance();
			image = myCate.getImage();
			has_like = myCate.getHas_like();
			no_of_likes = myCate.getNo_of_likes();
		
			holder.tvPrice.setText("" + price);
			/*if (price_type.equals("U")) {
				holder.tvPriceType.setText("€");
			} else {
				holder.tvPriceType.setText("$");
			}*/
			holder.tvName.setText("" + name);
			if (image != null) {
				try {
					imageLoader.DisplayImage(image, holder.ivItem);
				} catch (Exception e) {
				}
			} else {
				holder.ivItem.setImageResource(R.drawable.ic_launcher);
			}

			

			holder.rlParent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						SearchModel	_list = listSubCategories.get(i);
						
						mItem_IdList.add(_list.getItemid());
					
					}
					myCateForfav = listSubCategories.get(position);
					Intent iParent = new Intent(OrderCompletedActivity.this,ProductDetails.class);
					iParent.putExtra("ItemArray", mItem_IdList);
					iParent.putExtra("position", ""+position);
					startActivity(iParent);

				}
			});

		
			return convertView;

		}
	}
}
