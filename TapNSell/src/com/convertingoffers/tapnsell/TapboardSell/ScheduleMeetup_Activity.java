package com.convertingoffers.tapnsell.TapboardSell;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.Modal.MeetUpListModal;
import com.convertingoffers.tapnsell.Shop.ChatActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.RoundedImageView_CenterCrop;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ScheduleMeetup_Activity extends BaseActivity implements OnClickListener {
	
	ListView lv_meetup;
	ImageView ivToday,ivComing;
	Animation RightSwipe;
	Context context;
	String  id,toid,isbuyer,itemid,name,msg,image,userimage,title,latitude,longitude,address,time,date,day;
	
	LinearLayout llChoice;
	MeetUpCustomAdapter	adapter;
	String userid;
	ArrayList<MeetUpListModal> mMeetList = new ArrayList<MeetUpListModal>();
	ArrayList<MeetUpListModal> mMeetAllList = new ArrayList<MeetUpListModal>();
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	boolean ClickToday=false,ClickAll=false,Statusbtn=false;
	
	protected ImageLoader iLoader_Rounded = ImageLoader.getInstance();
	Bitmap image_bitmap=null;
	public  DisplayImageOptions options,option_item;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meetups);

		userid = pref.getString("UserID", "");
	
		ivBack.setOnClickListener(this);
		
		LoadImage();
		iLoader_Rounded.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				/*.showImageOnLoading(R.drawable.profile_user_defualtimage)
				.showImageForEmptyUri(R.drawable.profile_user_defualtimage)
				.showImageOnFail(R.drawable.profile_user_defualtimage)*/
				.resetViewBeforeLoading(true)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true)
				.build();
		
	
	
		if (cd.checkConnection()) {
			mMeetAllList.clear();
			new MeetupTodayBackTask().execute("");
		} else {
			Toast.makeText(ScheduleMeetup_Activity.this,
					"Interner connection is not available!",
					Toast.LENGTH_LONG).show();
		}	
		tvHeader.setText("Meetups");
		ivComing.setOnClickListener(this);
		ivToday.setOnClickListener(this);
		ivBack.setOnClickListener(this);
	}
	private void LoadImage() {
		 image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chat_info_default_image);
	}
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		
		llChoice= (LinearLayout) findViewById(R.id.llChoice);
		lv_meetup = (ListView) findViewById(R.id.lv_meetup);
		ivToday = (ImageView) findViewById(R.id.ivToday);
		ivComing = (ImageView) findViewById(R.id.ivComing);
		
	}

	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
	Intent i;
		switch (v.getId()) {

		case R.id.ivToday:
			buttonAnimation(ivToday);
			Statusbtn=false;
			
			if (Build.VERSION.SDK_INT >= 16)
				llChoice.setBackground(context.getResources().getDrawable(R.drawable.schedule_meetup_header_today));
			else
				llChoice.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.schedule_meetup_header_today));

			
			ClickToday=true;
		/*	if(!ClickToday){
				if (cd.checkConnection()) {
					mMeetList.clear();
					new MeetupTodayBackTask().execute("");
				} else {
					Toast.makeText(ScheduleMeetup_Activity.this,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}		
			}else{*/
		
				adapter = new MeetUpCustomAdapter(context,R.layout.schedule_meetup_list_child,mMeetList);
				lv_meetup.setAdapter(adapter);
		//	}
			
			
			break;
	
		case R.id.ivComing:
			buttonAnimation(ivComing);
			Statusbtn=true;
			
			
			if (Build.VERSION.SDK_INT >= 16)
				llChoice.setBackground(context.getResources().getDrawable(R.drawable.schedule_meetup_header_upcoming));
			else
				llChoice.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.schedule_meetup_header_upcoming));

			
			if(!ClickAll){
				
				if (cd.checkConnection()) {
					mMeetAllList.clear();
					new MeetupTodayBackTask().execute("");
				} else {
					Toast.makeText(ScheduleMeetup_Activity.this,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}		
			}else{
				adapter = new MeetUpCustomAdapter(context,R.layout.schedule_meetup_list_child, mMeetAllList);
				lv_meetup.setAdapter(adapter);
			}
			ClickAll=true;
			break;
			
		case R.id.ivBack:
			i=new Intent(context, TODOS_Activity.class);
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
	private class MeetupTodayBackTask extends
			AsyncTask<String, Void, String> {
		String errorMessage;
		JSONObject json;
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
				//userid="25";
				if(Statusbtn){
				 json = userFunction.MeetupListFunction(userid);
				}else{
				 json = userFunction.MeetupTodayFunction(userid);
				}
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONArray jmeetup=json.getJSONArray("meetup");
							for (int i = 0; i < jmeetup.length(); i++) {
								JSONObject objmeetup=jmeetup.getJSONObject(i);
								
								id=objmeetup.getString("id");
								toid=objmeetup.getString("userid");
								isbuyer=objmeetup.getString("isbuyer");
								itemid=objmeetup.getString("itemid");
								name=objmeetup.getString("name");
								
								image=UserFunctions.localImageUrl;
								
								image=image+objmeetup.getString("image");
								userimage=UserFunctions.localImageUrl;
								userimage=userimage+objmeetup.getString("userimage");
								title=objmeetup.getString("title");
								latitude=objmeetup.getString("latitude");
								longitude=objmeetup.getString("longitude");
								address=objmeetup.getString("address");
								time=objmeetup.getString("time");
								date=objmeetup.optString("date");
								day	=objmeetup.optString("day");
								
								if(Statusbtn){
									mMeetAllList.add(new MeetUpListModal(toid, userimage, isbuyer, itemid, name, image, userimage, title, latitude, longitude, address, time, date, day));
								}else{
									mMeetList.add(new MeetUpListModal(toid, userimage, isbuyer, itemid, name, image, userimage, title, latitude, longitude, address, time, date, day));	
								}
							}
							errorMessage = "true";
						} else {
							errorMessage = "false";
							msg=json.getString("message");
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
				
				if(!Statusbtn){
					adapter = new MeetUpCustomAdapter(context,R.layout.schedule_meetup_list_child1, mMeetList);
				}else{
					adapter = new MeetUpCustomAdapter(context,R.layout.schedule_meetup_list_child, mMeetAllList);
				}
				lv_meetup.setAdapter(adapter);
				
				
			} else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else {
				Custom_Dialog.dialogCode(2, null,msg,
						ScheduleMeetup_Activity.this);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(context,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
	
	public class MeetUpCustomAdapter extends ArrayAdapter<MeetUpListModal> {
		ViewHolder holder;
		private ArrayList<MeetUpListModal> listSubCategories;
		ImageLoader iLoader_item = ImageLoader.getInstance();
		DisplayImageOptions options,options_item;
		
		public MeetUpCustomAdapter(Context context, int textViewResourceId,
				ArrayList<MeetUpListModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<MeetUpListModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivUserImage,ivChat,ivUser;
			TextView tvDate,tvDayName,tvTitle,tvTime,tvAddress;
			EditText evAddress;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				if(Statusbtn){
					convertView = vi.inflate(R.layout.schedule_meetup_list_child, null);	
					
					holder.tvDate=(TextView)convertView.findViewById(R.id.tvDate);
					holder.tvDayName=(TextView)convertView.findViewById(R.id.tvDayName);
				}else{
					convertView = vi.inflate(R.layout.schedule_meetup_list_child1, null);
					
					holder.ivUser = (ImageView) convertView.findViewById(R.id.ivUser);
					holder.tvAddress=(TextView)convertView.findViewById(R.id.tvAddress);	
				}
					
				holder.ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
				holder.ivChat = (ImageView) convertView.findViewById(R.id.ivChat);
				holder.tvTitle=(TextView)convertView.findViewById(R.id.tvTitle);
				holder.tvTime=(TextView)convertView.findViewById(R.id.tvTime);
				iLoader_Rounded.init(ImageLoaderConfiguration.createDefault(context));
				options = new DisplayImageOptions.Builder()
				/*.showImageOnLoading(R.drawable.profile_user_defualtimage)
				.showImageForEmptyUri(R.drawable.profile_user_defualtimage)
				.showImageOnFail(R.drawable.profile_user_defualtimage)*/
				.resetViewBeforeLoading(true)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true)
				.build();
				
				 option_item = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.transperant)
					.showImageForEmptyUri(R.drawable.transperant)
					.showImageOnFail(R.drawable.transperant)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
				 
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MeetUpListModal m_ItemListing;
			m_ItemListing  = listSubCategories.get(position);
			
			
			String UserImage=m_ItemListing.getUserimage();
		
			

			// Image display using lazy loading 

			iLoader_item.displayImage(UserImage, holder.ivUserImage, option_item, new SimpleImageLoadingListener() {
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
			
			
			
			
			holder.tvTitle.setText(m_ItemListing.getTitle());
			holder.tvTime.setText(m_ItemListing.getTime());
			
			if(Statusbtn){
				holder.tvDate.setText(m_ItemListing.getDate());
				holder.tvDayName.setText(m_ItemListing.getDay());
			}else{
//				holder.ivUser = (ImageView) convertView.findViewById(R.id.ivUser);
				String itemimage=m_ItemListing.getImage();
			/*	if (itemimage != null) {
					imageLoader.DisplayImage(itemimage, holder.ivUser );
				} else {
					holder.ivUser .setImageResource(R.drawable.meetup_list_user_image);
				}*/
				
				if(itemimage.length()>0){
					iLoader_Rounded.displayImage(itemimage, holder.ivUser, options, loadImageListener);
				}else{
					iLoader_Rounded.displayImage("", holder.ivUser, options, loadImageListener);
				}	
				
				
				holder.tvAddress.setText(m_ItemListing.getAddress());	
			}
			
			holder.ivChat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					buttonAnimation(holder.ivChat);
					MeetUpListModal m_item = listSubCategories.get(position);
					
					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						MeetUpListModal	_list = listSubCategories.get(i);
						
						mItem_IdList.add(_list.getItemid());
					}
					Intent i = new Intent(context,ChatActivity.class);
					i.putExtra("itemid", m_item.getItemid());
					i.putExtra("from_id", m_item.getUserid());
					i.putExtra("Distance","");
					i.putExtra("ItemArray", mItem_IdList);
					i.putExtra("position", ""+position);
					startActivity(i);
					
				}
			});
			
			return convertView;

		}

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
	
}
