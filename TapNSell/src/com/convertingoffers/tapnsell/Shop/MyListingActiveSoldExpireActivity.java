package com.convertingoffers.tapnsell.Shop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Modal.ActiveModal;
import com.convertingoffers.tapnsell.Modal.Active_Sold_ExpireModal;
import com.convertingoffers.tapnsell.Modal.ExpireModal;
import com.convertingoffers.tapnsell.Modal.SoldModal;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.CustomTextView;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.ImageLoaderTopBottamRounded;
import com.convertingoffers.tapnsell.util.RoundedImageView_CenterCrop;
import com.convertingoffers.tapnsell.util.TouchImageView;
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

public class MyListingActiveSoldExpireActivity extends BaseActivity implements OnClickListener {

	String rattingname="";
	
	protected static final String TAG = "ShareActivity";
	Session mCurrentSession;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static final List<String> PERMISSIONS = Arrays.asList(
			 "publish_actions");
	boolean pendingPublishReauthorization;
	String strRatting_UserName, strRatting_Image, strRatting_Review="",
	strRatting_ItemId, strRatting_Fromid, strRatting_toid,hasrefundrequest="",refunded="",
	strRatting_Ratting,orderid="";
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options,options_rounded;
	
	Bitmap image_bitmap=null;
	
	ImageView ivActive,ivSold,ivExpire;
	ListView lv_Item;
	Animation RightSwipe;
	Context context;
	
	ActiveCustomAdapter Activeadapter;
	SoldCustomAdapter Soldadapter;
	ExpireCustomAdapter Expireadapter;
	int positiondelete;
	Bitmap bitmap;
	ArrayList<ActiveModal> mActiveList = new ArrayList<ActiveModal>();
	ArrayList<SoldModal> mSoldList = new ArrayList<SoldModal>();
	ArrayList<ExpireModal> mExpireList = new ArrayList<ExpireModal>();
	ArrayList<Active_Sold_ExpireModal> mAllList = new ArrayList<Active_Sold_ExpireModal>();
	String	itemid,toid,name,price,distance,image,has_like,no_of_likes;
	String sold_orderid,sold_buyerid,sold_username,sold_userimage,sold_itemid,sold_toid,sold_name,
	sold_price,sold_distance,sold_image,sold_has_like,sold_no_of_likes;
	String Active_itemid,Active_toid,Active_name,Active_price,Active_distance,
	Active_image,Active_has_like,Active_no_of_likes,SelectionType="Active",delete_itemid,Copy_itemid;
	String copy_image="",copy_defaultimage="",copy_userid="",copy_itemid="",copy_name="",copy_item_description=""
		,copy_item_condition="",copy_latitude="",copy_longitude="",copy_item_address="",copy_sell_price="",copy_quantity="",copy_delevery_option="",
		copy_condition="",copy_local_pickup="",copy_category_id="",copy_reserved="",copy_distance="",copy_Video="";	
	String latitude,longitude,userid;
	boolean status=false;
	LinearLayout llShort;
	ImageLoaderTopBottamRounded imageloader;
	
	TouchImageView ivDisplayImageDialog;
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemlisting);

		userid = pref.getString("UserID", "");
		latitude = pref.getString("lat", "");
		longitude= pref.getString("long", "");

		editor.remove("Copy_Relist");
		editor.commit();
		tvHeader.setText("My Listings");
		ivMenu.setVisibility(View.GONE);
		ivBack.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
		ivActive.setOnClickListener(this);
		ivSold.setOnClickListener(this);
		ivExpire.setOnClickListener(this);
		lv_Item.setPadding(5,5,5,0);
		LoadImage();
		if (Build.VERSION.SDK_INT >= 16)
			llShort.setBackground(context.getResources().getDrawable(R.drawable.mylisting_top_btn_short_active));
		else
			llShort.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mylisting_top_btn_short_active));
		

		if (cd.checkConnection()) {
			new ActiveSoldExpireBackTask().execute("");
		} else {
			Toast.makeText(context,"Interner connection is not available!",Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		
		iLoader_item.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.list_item_image_frame)
		.showImageForEmptyUri(R.drawable.list_item_image_frame)
		.showImageOnFail(R.drawable.list_item_image_frame)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		options_rounded = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.feedback_user_default)
		.showImageForEmptyUri(R.drawable.feedback_user_default)
		.showImageOnFail(R.drawable.feedback_user_default)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		
		lv_Item = (ListView) findViewById(R.id.lvt_Product);
		llShort= (LinearLayout) findViewById(R.id.llShort);
		ivActive= (ImageView) findViewById(R.id.ivPrice);
		ivSold= (ImageView) findViewById(R.id.ivDistance);
		ivExpire= (ImageView) findViewById(R.id.ivPopular);
	}

	@SuppressLint("NewApi") @SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		Intent  i;
		switch (v.getId()) {
		case R.id.ivMenu:
		
			i=new Intent(context, TapBoardActivity.class);
			startActivity(i);
			finish();
			
			break;
	
		case R.id.ivBack:
			finish();
			break;
	
		case R.id.ivPrice:
			tvHeader.setText("My Listings");
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(context.getResources().getDrawable(R.drawable.mylisting_top_btn_short_active));
			else
				llShort.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mylisting_top_btn_short_active));

			if(mActiveList.size()==0){
				Custom_Dialog.dialogCode(2, null,"No items Listed.",
						MyListingActiveSoldExpireActivity.this);
				Activeadapter = new ActiveCustomAdapter(context,R.layout.mylisting_active_child, mActiveList);	
				lv_Item.setAdapter(Activeadapter);
			}else{
			Activeadapter = new ActiveCustomAdapter(context,R.layout.mylisting_active_child, mActiveList);	
			lv_Item.setAdapter(Activeadapter);
			
			}
			
			break;
			
		case R.id.ivDistance:
			tvHeader.setText("Items Sold");
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(context.getResources().getDrawable(R.drawable.mylisting_top_btn_short_sold));
			else
				llShort.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mylisting_top_btn_short_sold));

			if(mSoldList.size()==0){
				Custom_Dialog.dialogCode(2, null,"No items Listed.",
						MyListingActiveSoldExpireActivity.this);
				Soldadapter = new SoldCustomAdapter(context,R.layout.mylisting_sold_child, mSoldList);	
				lv_Item.setAdapter(Soldadapter);
			}else{
				
			Soldadapter = new SoldCustomAdapter(context,R.layout.mylisting_sold_child, mSoldList);	
			lv_Item.setAdapter(Soldadapter);

			}
			break;
			
		case R.id.ivPopular:
			tvHeader.setText("Expired");
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(context.getResources().getDrawable(R.drawable.mylisting_top_btn_short_expire));
			else
				llShort.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mylisting_top_btn_short_expire));

			if (mExpireList.size() == 0) {
				Custom_Dialog.dialogCode(2, null, "No items Listed.",
						MyListingActiveSoldExpireActivity.this);
				Expireadapter = new ExpireCustomAdapter(context,
						R.layout.mylisting_expire_child, mExpireList);
				lv_Item.setAdapter(Expireadapter);
			} else {
				Expireadapter = new ExpireCustomAdapter(context,
						R.layout.mylisting_expire_child, mExpireList);
				lv_Item.setAdapter(Expireadapter);

			}
			
			
			break;
		default:
			break;
		}
	}
	public void LoadImage() {
		
		image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.feedback_user_default);

	}
	private class ActiveSoldExpireBackTask extends
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
				
//				userid="25";
				Log.e("userid", " " + userid);
				Log.e("latitude", " " + latitude);
				Log.e("longitude", " " + longitude);
				JSONObject json = userFunction.MyListActive_Sold_expire(userid, latitude, longitude);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject Item= json.getJSONObject("Item");
							
							JSONArray Expire=Item.getJSONArray("Expire");
							JSONArray Sold=Item.getJSONArray("Sold");
							JSONArray Active=Item.getJSONArray("Active");
							
							
							for (int i = 0; i < Expire.length(); i++) {
								JSONObject objE = Expire.getJSONObject(i);

								itemid	=objE.getString("itemid");
								toid	=objE.getString("userid");
								name	=objE.getString("name");
								price	=objE.getString("price");
								distance	=objE.getString("distance");
								image=UserFunctions.localImageUrl;
								image	=image+objE.getString("image");
								has_like	=objE.getString("has_like");
								no_of_likes	=objE.getString("no_of_likes");
								mExpireList.add(new ExpireModal(itemid, userid, name, price, distance, image, has_like, no_of_likes));
								
							}
							for (int j = 0; j < Sold.length(); j++) {
								
								JSONObject objS = Sold.getJSONObject(j);
								
								sold_orderid=objS.getString("orderid");
								 sold_buyerid=objS.getString("buyerid");
								 sold_username=objS.getString("username");
								 sold_userimage=objS.getString("userimage");
								 sold_itemid=objS.getString("itemid");
								 sold_toid=objS.getString("userid");
								 sold_name=objS.getString("name");
								 sold_price=objS.getString("price");
								 sold_distance=objS.getString("distance");
								 sold_image=UserFunctions.localImageUrl;
								 sold_image=sold_image+objS.getString("image");
								 sold_has_like=objS.getString("has_like");
								 sold_no_of_likes=objS.getString("no_of_likes");
								 hasrefundrequest=objS.optString("hasrefundrequest");
								 refunded=objS.optString("refunded");
								 mSoldList.add(new SoldModal(sold_orderid, sold_buyerid, sold_username, 
										 sold_userimage, sold_itemid, sold_toid, sold_name, sold_price, 
										 sold_distance, sold_image, sold_has_like, sold_no_of_likes,hasrefundrequest,refunded));
								 
							}
							for (int k = 0; k < Active.length(); k++) {
								JSONObject objA = Active.getJSONObject(k);

								Active_itemid=objA.getString("itemid");
								Active_toid=objA.getString("userid");
								Active_name=objA.getString("name");
								Active_price=objA.getString("price");
								Active_distance=objA.getString("distance");
								Active_image=UserFunctions.localImageUrl;
								Active_image=Active_image+objA.getString("image");
								Active_has_like=objA.getString("has_like");
								Active_no_of_likes=objA.getString("no_of_likes");
								mActiveList.add(new ActiveModal(Active_itemid, Active_toid, Active_name
										,Active_price, Active_distance, Active_image, Active_has_like, Active_no_of_likes));
							}
							
							mAllList.add(new Active_Sold_ExpireModal(mActiveList, mSoldList, mExpireList));
							
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
				if(mActiveList.size()==0){
					Custom_Dialog.dialogCode(2, null,"No items Listed.",
							MyListingActiveSoldExpireActivity.this);
				}else{
					Activeadapter = new ActiveCustomAdapter(context,R.layout.mylisting_active_child, mActiveList);	
					lv_Item.setAdapter(Activeadapter);
				}
				

			} else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null,message,
						MyListingActiveSoldExpireActivity.this);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(MyListingActiveSoldExpireActivity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
	
	public class ActiveCustomAdapter extends ArrayAdapter<ActiveModal> {
		ViewHolder holder;
		private ArrayList<ActiveModal> listSubCategories;
	
		
		
		public ActiveCustomAdapter(Context context, int textViewResourceId,
				ArrayList<ActiveModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<ActiveModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			//Active_item
			TextView tvPrice;
			ImageView ivDefault;
			CustomTextView tvName;
			
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			
		
			holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
					convertView = vi.inflate(R.layout.mylisting_active_child_productdetail, null);

					holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
					holder.ivDefault = (ImageView) convertView.findViewById(R.id.ivDefault);
					holder.tvName = (CustomTextView) convertView.findViewById(R.id.tvName);
					
					
			/*	}else if(SelectionType.equals("Sold")){
					convertView = vi.inflate(R.layout.mylisting_sold_child, null);
					
					holder.iv1 = (ImageView) convertView.findViewById(R.id.iv1);
					holder.iv2 = (ImageView) convertView.findViewById(R.id.iv2);
					holder.ivCopy = (ImageView) convertView.findViewById(R.id.ivCopy);
					
					
				}else{
					convertView = vi.inflate(R.layout.mylisting_expire_child, null);
					
					holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
					holder.rlDelete = (RelativeLayout) convertView.findViewById(R.id.rlDelete);
					holder.rlRelist = (RelativeLayout) convertView.findViewById(R.id.rlRelist);
				
					
				}*/
									
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			ActiveModal m_ActiveItemListing  = listSubCategories.get(position);
			
			String image="";
			holder.tvName.setText(m_ActiveItemListing.getName());
			holder.tvPrice.setText(m_ActiveItemListing.getPrice());
			image=m_ActiveItemListing.getImage().toString();
			holder.ivDefault.setTag(m_ActiveItemListing);
			holder.ivDefault.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ActiveModal m_AListing =(ActiveModal) v.getTag();
					DisplayImagedialogCode(m_AListing.getImage().toString());
				}
			});
			
			Log.e("image", " "+image);
		/*	
			if (image != null) {
				imageLoader.DisplayImage(image, holder.ivDefault );
			} else {
				holder.ivDefault .setImageResource(R.drawable.list_item_image_frame);
			}
				*/	
			// Image display using lazy loading 

			iLoader_item.displayImage(image, holder.ivDefault, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					
					Bitmap bmp=	scaleToActualAspectRatio(loadedImage);
					Log.e("bmp", "i got bmp "+bmp);
					holder.ivDefault.setImageBitmap(bmp);
					
				}
			}, new ImageLoadingProgressListener() {
				@Override
				public void onProgressUpdate(String imageUri, View view, int current, int total) {
				}
			});
			
			// Image display using lazy loading
			
			
			
			
			
			
			
	/*	holder.ivChat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					buttonAnimation(holder.ivChat);
					ItemToShipModal m_item = listSubCategories.get(position);
					
					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						ItemToShipModal	_list = listSubCategories.get(i);
						
						mItem_IdList.add(_list.getItemid());
					}
					Intent i = new Intent(context,ChatActivity.class);
					i.putExtra("itemid", m_item.getItemid());
					i.putExtra("from_id", m_item.getUserid());
					i.putExtra("Distance", m_item.getDistance());
					i.putExtra("ItemArray", mItem_IdList);
					i.putExtra("position", ""+position);
					startActivity(i);
					
				}
			});*/
			
		
			return convertView;

		}

	}

	public class SoldCustomAdapter extends ArrayAdapter<SoldModal> {
		ViewHolder holder;
		private ArrayList<SoldModal> listSubCategories;
		
		public SoldCustomAdapter(Context context, int textViewResourceId,
				ArrayList<SoldModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<SoldModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			
			
			ImageView ivDefault;
			CustomTextView tvName;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
					convertView = vi.inflate(R.layout.mylisting_sold_child_productdetail, null);
				
					holder.ivDefault = (ImageView) convertView.findViewById(R.id.ivDefault);
					holder.tvName = (CustomTextView) convertView.findViewById(R.id.tvName);
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			SoldModal m_ActiveItemListing  = listSubCategories.get(position);
			
			String image="";
			holder.tvName.setText(m_ActiveItemListing.getName());
			image=m_ActiveItemListing.getImage().toString();
			
			Log.e("image", " "+image);
			
			
			// Image display using lazy loading 

			iLoader_item.displayImage(image, holder.ivDefault, options, new SimpleImageLoadingListener() {
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
			
			
			
			
			
	/*
			holder.ivChat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					buttonAnimation(holder.ivChat);
					ItemToShipModal m_item = listSubCategories.get(position);
					
					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						ItemToShipModal	_list = listSubCategories.get(i);
						
						mItem_IdList.add(_list.getItemid());
					}
					Intent i = new Intent(context,ChatActivity.class);
					i.putExtra("itemid", m_item.getItemid());
					i.putExtra("from_id", m_item.getUserid());
					i.putExtra("Distance", m_item.getDistance());
					i.putExtra("ItemArray", mItem_IdList);
					i.putExtra("position", ""+position);
					startActivity(i);
					
				}
			});*/
			
		
			return convertView;

		}

	}
	@SuppressWarnings("static-access")
	public   void Two_Button_dialogCode(String msg) {
	   	
	   	// custom dialog
	   	Log.e("testdialogCode", "testdialogCode");
	   	final Dialog  dialog = new Dialog(context);
	   	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	   	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	   	dialog.setContentView(R.layout.dialog_custom_2_button);
	   	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	       lp.copyFrom(dialog.getWindow().getAttributes());
	       lp.width = lp.MATCH_PARENT;
	       lp.height = lp.MATCH_PARENT;
	       dialog.getWindow().setAttributes(lp);
	       dialog.show();
	   	ImageView ivNo = (ImageView) dialog.findViewById(R.id.ivNo);
	   	ImageView ivYes = (ImageView) dialog.findViewById(R.id.ivYes);
	   	TextView tvMsg= (TextView) dialog.findViewById(R.id.tvMsg);
	   	tvMsg.setText(msg);
	   	ivYes.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					dialog.dismiss();
					if (cd.checkConnection()) {
						new RequestRefundBackTask().execute("");
					} else {
						Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG)
								.show();
					}
				}
			});
	   	ivNo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					dialog.dismiss();
				}
			});
	   	
	   }
	
	

	public class ExpireCustomAdapter extends ArrayAdapter<ExpireModal> {
		ViewHolder holder;
		private ArrayList<ExpireModal> listSubCategories;
		
		public ExpireCustomAdapter(Context context, int textViewResourceId,
				ArrayList<ExpireModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<ExpireModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			//Active_item
			TextView tvPrice;
			ImageView ivDefault;
			CustomTextView tvName;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				
					convertView = vi.inflate(R.layout.mylisting_expire_child_productdetail, null);
					
					holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
					holder.ivDefault = (ImageView) convertView.findViewById(R.id.ivDefault);
					holder.tvName = (CustomTextView) convertView.findViewById(R.id.tvName);
					
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			ExpireModal m_ActiveItemListing  = listSubCategories.get(position);
			
			String image="";
			holder.tvName.setText(m_ActiveItemListing.getName());
			holder.tvPrice.setText(m_ActiveItemListing.getPrice());
			image=m_ActiveItemListing.getImage().toString();
			
			Log.e("image", " "+image);
		/*	
			if (image != null) {
				imageLoader.DisplayImage(image, holder.ivDefault );
			} else {
				holder.ivDefault .setImageResource(R.drawable.list_item_image_frame);
			}
				*/	
			

			// Image display using lazy loading 

			iLoader_item.displayImage(image, holder.ivDefault, options, new SimpleImageLoadingListener() {
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
			
			
			
			
			
	/*
			holder.ivChat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					buttonAnimation(holder.ivChat);
					ItemToShipModal m_item = listSubCategories.get(position);
					
					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						ItemToShipModal	_list = listSubCategories.get(i);
						
						mItem_IdList.add(_list.getItemid());
					}
					Intent i = new Intent(context,ChatActivity.class);
					i.putExtra("itemid", m_item.getItemid());
					i.putExtra("from_id", m_item.getUserid());
					i.putExtra("Distance", m_item.getDistance());
					i.putExtra("ItemArray", mItem_IdList);
					i.putExtra("position", ""+position);
					startActivity(i);
					
				}
			});*/
			
		
			return convertView;

		}

	}
	
	@SuppressWarnings("static-access")
	public   void DeletedialogCode(String msg) {
    	
    	// custom dialog
    	Log.e("testdialogCode", "testdialogCode");
    	final Dialog  dialog = new Dialog(context);
    	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	dialog.setContentView(R.layout.dialog_custom_2_button);
    	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = lp.MATCH_PARENT;
        lp.height = lp.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    	ImageView ivNo = (ImageView) dialog.findViewById(R.id.ivNo);
    	ImageView ivYes = (ImageView) dialog.findViewById(R.id.ivYes);
    	TextView tvMsg= (TextView) dialog.findViewById(R.id.tvMsg);
    	tvMsg.setText(msg);
    	ivYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
				
				if(cd.checkConnection()){
					new DeleteItemBackTask().execute("");
				}else{
					Toast.makeText(context, "internet_conn_failed",Toast.LENGTH_LONG).show();
				}
			}
		});
    	ivNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				dialog.dismiss();
			}
		});
    	
    }
	
	private class DeleteItemBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json=null;
//				userid="50";
				Log.e("userid", ""+userid);
				Log.e("delete_itemid", ""+delete_itemid);
					json = userFunction.DeleteItemFromExpireListFunction(userid, delete_itemid);	
				
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							message=json.getString("message");
							
							errorMessage = "true";
						} else {
							message=json.optString("message");
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
			if(result.equals("true")){
				mExpireList.remove(positiondelete);
				Expireadapter.notifyDataSetChanged();
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else{
				Custom_Dialog.dialogCode(2, null, message, context);	
			}
				
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	public void DownloadFile(String fileURL, File fileName) {
        try {
          
            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(fileName);
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;

            while ((len1 = in.read(buffer)) > 0) {                          
                f.write(buffer, 0, len1);               
            }       
            f.close();


        } catch (Exception e) {

            Log.d("Error....", e.toString());
        }

    }
	
	public Bitmap getBitmapFromURL(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
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
		OpenRequest openRequest = new OpenRequest(MyListingActiveSoldExpireActivity.this)
				.setPermissions(permissions).setCallback(callback);
		Session session = new Session.Builder(MyListingActiveSoldExpireActivity.this).build();
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
							Toast.makeText(MyListingActiveSoldExpireActivity.this, "Please Sing in facebook with sandbox cradentials ", Toast.LENGTH_LONG).show();
							
						}
					}
				});
				return;
			}
			//Shahil facebook
			Bundle postParams = new Bundle();
			postParams.putString("name", "TapnSell");
			postParams.putString("caption", "Ratting Item");
			postParams.putString("description","I just liked this on TapNSell. Experience the World's Garage Sale #tapnsell");
			postParams.putString("picture",UserFunctions.LogoUrl);
			Log.e("strRatting_Image ", " "+strRatting_Image);
			
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
						Log.e("response", " "+response);
						GraphObject graphObject = response.getGraphObject();
						if (graphObject != null) {
							Log.e("response", " " + response);
							JSONObject graphResponse = response
									.getGraphObject().getInnerJSONObject();
							String postId = null;
							try {
								postId = graphResponse.getString("id");
							} catch (JSONException e) {
								Log.i(TAG, "JSON error " +postId+ e.getMessage());
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
							
							Toast.makeText(context,"Facebook posting failed ...",Toast.LENGTH_LONG).show();

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (mCurrentSession != null)
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);

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
	
	

	private class RequestRefundBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json;
				
				Log.e("userid", " "+userid);
				Log.e("orderid", " "+orderid);
					json = userFunction.SellerconfirmRejectFunction(userid, orderid,"Y");
				
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
						errorMessage = "network";
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return errorMessage;
		}

		@SuppressLint("NewApi") 
		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			if (result.equals("true")) {
			
				Custom_Dialog.dialogCode(2, null,message, context);
				
			} else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null,message, context);
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	@SuppressWarnings("static-access")
	private void DisplayImagedialogCode(String image) {

		// custom dialog
		final Dialog dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_image);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;

		dialog.getWindow().setAttributes(lp);
		ivDisplayImageDialog = (TouchImageView) dialog.findViewById(R.id.ivmsgImage);
		//videoview = (VideoView) dialog.findViewById(R.id.videoview);
		
			ivDisplayImageDialog.setVisibility(View.VISIBLE);
	
		if(!dialog.isShowing()){
			iLoader_item.displayImage(image, ivDisplayImageDialog, options, new SimpleImageLoadingListener() {
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
			dialog.show();
			}
	}
	
	@SuppressLint("NewApi") 
	public Bitmap scaleToActualAspectRatio(Bitmap bitmap) {
		 if (bitmap != null) {
		 boolean flag = true;
		 
		 
		 int deviceWidth=0,deviceHeight=0;
	        Display display = getWindowManager().getDefaultDisplay();
       	Point size = new Point();
	        if (Build.VERSION.SDK_INT >= 13){
	        	
	        	display.getSize(size);
	        	deviceWidth = size.x;
	        	deviceHeight = size.y;
	        }else{
	         DisplayMetrics displaymetrics = new DisplayMetrics();
	   	     getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	   	  deviceWidth = displaymetrics.heightPixels;
	   	  deviceHeight   = displaymetrics.widthPixels;
	        }
	        
		/*int deviceWidth = getWindowManager().getDefaultDisplay().getWidth();
		 int deviceHeight = getWindowManager().getDefaultDisplay().getHeight();*/
		 
		int bitmapHeight = bitmap.getHeight(); // 563
		 int bitmapWidth = bitmap.getWidth(); // 900
		 
		// aSCPECT rATIO IS Always WIDTH x HEIGHT rEMEMMBER 1024 x 768
		 
		if (bitmapWidth > deviceWidth) {
		 flag = false;
		 
		// scale According to WIDTH
		 int scaledWidth = deviceWidth;
		 int scaledHeight = (scaledWidth * bitmapHeight) / bitmapWidth;
		 
		try {
		 if (scaledHeight > deviceHeight)
		 scaledHeight = deviceHeight;
		 
		bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
		 scaledHeight, true);
		 } catch (Exception e) {
		 e.printStackTrace();
		 }
		 }
		 
		if (flag) {
		 if (bitmapHeight > deviceHeight) {
		 // scale According to HEIGHT
		 int scaledHeight = deviceHeight;
		 int scaledWidth = (scaledHeight * bitmapWidth)
		 / bitmapHeight;
		 
		try {
		 if (scaledWidth > deviceWidth)
		 scaledWidth = deviceWidth;
		 
		bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
		 scaledHeight, true);
		 } catch (Exception e) {
		 e.printStackTrace();
		 }
		 }
		 }
		 }
		 return bitmap;
		 }
	
}
