package com.convertingoffers.tapnsell.MakeOffer;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Modal.AllOfferDisplayModalModal;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.CustomTextView;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ViewAllOfferActivity extends BaseActivity implements OnClickListener {
	/////////////////
	Context context;
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options_main_item,options_useriv;
	public boolean active = false,boolTimerSet=false,blink;

	long seconds;
	private CountDownTimer countDownTimer;//, countDownTimerValiNext,countDownTimerSenResult;
	private long totalTimeCountInMilliseconds;
	long MainTimeInMilliSecond, CurrentTimeInMilliSecond,
	finalTimeInMilliSecond;
	//private long timeBlinkInMilliseconds;
	//////////////
	ListingAllOfferCustomAdaper adapter;
	ImageView ivDefault;
	TextView tvPrice;
	
	CustomTextView tvName,tvNoItem;
	String userid="",itemid="",accept="",isbuyers,AllOffer_toid="";
	ListView lv_Offer;
	ArrayList<AllOfferDisplayModalModal> mAllOffer = new ArrayList<AllOfferDisplayModalModal>();
 	String dialog_item_isbuyer,dialog_item_offerid,dialog_item_toid,dialog_item_Itemid,dialog_item_image,dialog_item_name,dialog_item_price,item_name,item_price,image,offer_id,to_id,user_name,user_image,offer_price,time,timestamp;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("InlinedApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.all_offer);
	
	userid = pref.getString("UserID", "");
	Bundle bundle = getIntent().getExtras();
	if(bundle!=null){
	itemid = bundle.getString("AllOffer_itemid");
	AllOffer_toid= bundle.getString("AllOffer_toid");
	}
	Log.e("AllOffer_toid", " "+AllOffer_toid);
	if(cd.checkConnection()){
		

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							new  GetAllOfferBackTask().executeOnExecutor(
									AsyncTask.THREAD_POOL_EXECUTOR, "");
						} else {
							new  GetAllOfferBackTask().execute("");
						}
	}else{
		Toast.makeText(context, "internet_conn_failed",Toast.LENGTH_LONG).show();
	}
	ivMenu.setVisibility(View.VISIBLE);
	ivBack.setOnClickListener(this);
	ivMenu.setOnClickListener(this);
	tvHeader.setText("Offers");
	}

	
	
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		
		iLoader_item.init(ImageLoaderConfiguration.createDefault(context));
		options_main_item = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.list_item_image_frame)
		.showImageForEmptyUri(R.drawable.list_item_image_frame)
		.showImageOnFail(R.drawable.list_item_image_frame)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		
		options_useriv= new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.offer_item_user_bg)
		.showImageForEmptyUri(R.drawable.offer_item_user_bg)
		.showImageOnFail(R.drawable.offer_item_user_bg)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		

		
		
		ivDefault=(ImageView)findViewById(R.id.ivDefault);
		tvPrice=(TextView)findViewById(R.id.tvPrice);
		tvName=(CustomTextView)findViewById(R.id.tvName);
		tvNoItem=(CustomTextView)findViewById(R.id.tvNoItem);
		lv_Offer=(ListView)findViewById(R.id.lv_Offer);
	}
	
	
	public class GetAllOfferBackTask extends AsyncTask<String, Void, String> {
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
				Log.e("itemid", " "+itemid);
				
				JSONObject json = userFunction.GetallMyOfferFunction(AllOffer_toid, itemid);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							item_name=json.getString("item_name");
							item_price=json.getString("item_price");
							image=UserFunctions.localImageUrl;
							image=image+json.getString("image");
							JSONArray jOffer = json.getJSONArray("Offer");
							for (int i = 0; i < jOffer.length(); i++) {
								JSONObject jobj= jOffer.getJSONObject(i);
								offer_id=jobj.getString("offer_id");
								to_id=jobj.getString("user_id");
								user_name=jobj.getString("name");
								user_image=UserFunctions.localImageUrl;
								user_image=user_image+jobj.getString("user_image");
								offer_price=jobj.getString("offer");
								time=jobj.getString("time");
								timestamp=jobj.getString("timestamp");
								isbuyers=jobj.getString("isbuyer");
								mAllOffer.add(new AllOfferDisplayModalModal(offer_id, to_id, user_name, user_image
										, offer_price, time, timestamp,isbuyers));
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
				
				totalTimeCountInMilliseconds = 86400000;
				//timeBlinkInMilliseconds = 30000;
				
				
				tvPrice.setText(""+item_price);
//				setTimer("24:00:00");
				startTimer();
				
				
			
				// Image display using lazy loading 

				iLoader_item.displayImage(image, ivDefault, options_main_item, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						//holder.pbimage.setProgress(0);
//						holder.pbimage.setIndeterminate(true);
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
						//holder.pbimage.setProgress(Math.round(100.0f * current / total));
//						holder.pbimage.setIndeterminate(true);
					}
				});
				
				// Image display using lazy loading
				
				
				
				tvName.setText(item_name);
				int noitem=mAllOffer.size();
				if(noitem==0){
					tvNoItem.setText(mAllOffer.size()+" Offer");
				}else{
					tvNoItem.setText(mAllOffer.size()+" Offers");
				}

				
				adapter = new ListingAllOfferCustomAdaper(context,R.layout.all_offer_item, mAllOffer);
				lv_Offer.setAdapter(adapter);
				
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else{
				Custom_Dialog.dialogCode(1, null, message, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		cancelContdowwnTimer();
	}

	
	@SuppressLint("DefaultLocale") public class ListingAllOfferCustomAdaper extends ArrayAdapter<AllOfferDisplayModalModal> {
		ViewHolder holder;
		private ArrayList<AllOfferDisplayModalModal> listSubCategories;
		protected Object mysun;
		
		public ListingAllOfferCustomAdaper(Context context, int textViewResourceId,
				ArrayList<AllOfferDisplayModalModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<AllOfferDisplayModalModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivUserImage;
			TextView tvUserName,tvPrice,tvHour1,tvHour2,tvMinute1,tvMinute2,tvSeconds1,tvSeconds2;
			LinearLayout llMain;
			//ProgressBar pbimageUesr;
		}

		@SuppressLint("DefaultLocale") @Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
					convertView = vi.inflate(R.layout.all_offer_item, null);
				
				holder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
				holder.ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
				holder.tvHour1 = (TextView) convertView.findViewById(R.id.tvHour1);
				holder.tvHour2 = (TextView) convertView.findViewById(R.id.tvHour2);
				holder.tvMinute1 = (TextView) convertView.findViewById(R.id.tvMinute1);
				holder.tvMinute2 = (TextView) convertView.findViewById(R.id.tvMinute2);
				holder.tvSeconds1 = (TextView) convertView.findViewById(R.id.tvSeconds1);
				holder.tvSeconds2 = (TextView) convertView.findViewById(R.id.tvSeconds2);
				holder.llMain= (LinearLayout) convertView.findViewById(R.id.llMain);
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			
			final AllOfferDisplayModalModal myOffer = listSubCategories.get(position);
			String image=myOffer.getUser_image();
			Log.e("image", " "+image);
			holder.tvUserName.setText(""+myOffer.getName());
			
		/*	if (image != null) {
				imageLoader.DisplayImage(image, holder.ivUserImage );
			} else {
				holder.ivUserImage .setImageResource(R.drawable.ic_launcher);
			}
			*/
			
			iLoader_item.displayImage(image, holder.ivUserImage, options_useriv, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					//holder.pbimage.setProgress(0);
					//holder.pbimageUesr.setVisibility(View.VISIBLE);
//					holder.pbimage.setIndeterminate(true);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					//holder.pbimageUesr.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					//holder.pbimageUesr.setVisibility(View.GONE);
				}
			}, new ImageLoadingProgressListener() {
				@Override
				public void onProgressUpdate(String imageUri, View view, int current, int total) {
					//holder.pbimage.setProgress(Math.round(100.0f * current / total));
//					holder.pbimage.setIndeterminate(true);
				}
			});
			
			// Image display using lazy loading
			
			
			
			
			
			
			
			holder.tvPrice.setText("$"+myOffer.getOffer());
			long t_stamp = Long.parseLong(myOffer.getTimestamp());
			
			long s_sec =t_stamp%60;
			long m_min = t_stamp / 60;
			long h_hour = m_min / 60;
			m_min=m_min%60;
			
			holder.llMain.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AllOfferDisplayModalModal myOffer = listSubCategories.get(position);
					OfferdialogCode(myOffer);
					
				}
			});
			
		
			
			String hour =String.format("%02d", h_hour);
			String hour1 = hour.substring(0, 1);
			String hour2 = hour.substring(Math.max(hour.length() - 1, 0));
			
			String min =String.format("%02d", m_min);
			String min1 = min.substring(0, 1);
			String min2 = min.substring(Math.max(min.length() - 1, 0));
			
			String sec =String.format("%02d", s_sec);
			
			String sec1 = sec.substring(0, 1);
			String sec2 = sec.substring(Math.max(sec.length() - 1, 0));
		
			
			holder.tvSeconds1.setText(""+sec1);
			holder.tvSeconds2.setText(""+sec2);
			holder.tvMinute1.setText(""+min1);
			holder.tvMinute2.setText(""+min2);
			holder.tvHour1.setText(""+hour1);
			holder.tvHour2.setText(""+hour2);
			
			Log.e("t_stamp", " "+t_stamp);
			Log.e("m_min", " "+m_min);
			Log.e("h_hour", " "+h_hour);
			Log.e("hour1", " "+hour1);
			Log.e("hour2", " "+hour2);
			Log.e("min1", " "+min1);
			Log.e("min2", " "+min2);
			Log.e("sec1", " "+sec1);
			Log.e("sec2", " "+sec2);
		
			return convertView;

		}

	}
	
	
	
	public void startTimer() {
		countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
			// 500 means, onTick function will be called at every 500
			// milliseconds
		
			@SuppressLint("DefaultLocale")
			@Override
			public void onTick(long leftTimeInMilliseconds) {
				
				for (int i = 0; i < mAllOffer.size(); i++) {
					
					
					AllOfferDisplayModalModal my_offer=mAllOffer.get(i);
					long timestamp =Long.valueOf(my_offer.getTimestamp());
					timestamp=timestamp-1;
					my_offer.setTimestamp(""+timestamp);
					mAllOffer.set(i, my_offer);
				}
				
				adapter.notifyDataSetChanged();
			}
			@Override
			public void onFinish() {
				cancelContdowwnTimer();
			}
		}.start();
	}
	
	public void cancelContdowwnTimer() {
		
			if (countDownTimer != null) {
				countDownTimer.cancel();
			}
	}



	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.ivBack:
			cancelContdowwnTimer();
			finish();
		break;

		case R.id.ivMenu:
			cancelContdowwnTimer();
			Intent  i  = new Intent(context, TapBoardActivity.class);
			startActivity(i);
			break;
		
		default:
			break;
		}
	}
	
	@SuppressWarnings("static-access")
	private void OfferdialogCode(AllOfferDisplayModalModal myOffer) {

		
		String img;
		// custom dialog
		final Dialog dialog = new Dialog(ViewAllOfferActivity.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_offer_item);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);      
		ImageView ivDecline = (ImageView) dialog.findViewById(R.id.ivDecline);
		ImageView ivCounter = (ImageView) dialog.findViewById(R.id.ivCounter);
		ImageView ivAccept = (ImageView) dialog.findViewById(R.id.ivAccept);
		ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
		ImageView ivUserImage = (ImageView) dialog.findViewById(R.id.ivUserImage);
		TextView tvUserName = (TextView) dialog.findViewById(R.id.tvUserName);
		TextView tvPrice = (TextView) dialog.findViewById(R.id.tvPrice);
			img  =myOffer.getUser_image().toString();
			dialog_item_offerid=myOffer.getOffer_id().toString();
			
			dialog_item_price=myOffer.getOffer().toString();
			dialog_item_name=myOffer.getName().toString();
			
			dialog_item_isbuyer=myOffer.getIsbuyer().toString();
			dialog_item_toid=myOffer.getUser_id().toString();
			
			dialog_item_image=myOffer.getUser_image().toString();
			
/*		if (img != null) {
			imageLoader.DisplayImage(img, ivUserImage);
		} else {
			ivUserImage .setImageResource(R.drawable.offer_item_user_bg);
		}
		*/
//			offer_item_user_bg
		iLoader_item.displayImage(img, ivUserImage, options_useriv, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				//holder.pbimage.setProgress(0);
//				holder.pbimage.setIndeterminate(true);
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
				//holder.pbimage.setProgress(Math.round(100.0f * current / total));
//				holder.pbimage.setIndeterminate(true);
			}
		});
		
		// Image display using lazy loading
		
		
		
		
		
		
		
		
		tvUserName.setText(myOffer.getName());
		tvPrice.setText("$" +myOffer.getOffer());
		
		ivAccept.setOnClickListener(new OnClickListener() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
			public void onClick(View v) {
				dialog.dismiss();
				accept="Y";
				if(cd.checkConnection()){

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
										new  Accept_Decline_OfferBackTask().executeOnExecutor(
												AsyncTask.THREAD_POOL_EXECUTOR, "");
									} else {
										new  Accept_Decline_OfferBackTask().execute("");
									}
				}else{
					Toast.makeText(context, "internet_conn_failed",Toast.LENGTH_LONG).show();
				}
			}
		});
		
		
		ivDecline.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi") @Override
			public void onClick(View v) {
				dialog.dismiss();
				accept="N";
				if(cd.checkConnection()){
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
											new  Accept_Decline_OfferBackTask().executeOnExecutor(
													AsyncTask.THREAD_POOL_EXECUTOR, "");
										} else {
											new  Accept_Decline_OfferBackTask().execute("");
										}
				}else{
					Toast.makeText(context, "internet_conn_failed",Toast.LENGTH_LONG).show();
				}
			}
		});
		ivCounter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();

				Intent intent = new Intent(context, CounterOfferActivity.class);
				intent.putExtra("Counter_text", "");
				intent.putExtra("Counter_type", "");
				intent.putExtra("Counter_offerid", ""+dialog_item_offerid);
				intent.putExtra("Counter_price", ""+dialog_item_price);
				intent.putExtra("Counter_name", ""+dialog_item_name);
				intent.putExtra("Counter_to_id", ""+dialog_item_toid);
				intent.putExtra("Counter_itemid", ""+itemid);
				intent.putExtra("Counter_image", ""+dialog_item_image);
				intent.putExtra("Counter_isbuyer", ""+dialog_item_isbuyer);
				startActivity(intent);
				
			}
		});
		
	
		
	
		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

	

		dialog.show();
	}
	
	private class Accept_Decline_OfferBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;
		String msg;
		JSONArray jArray;
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
				
				jArray=new JSONArray();
				jArray.put(dialog_item_offerid);
				Log.e("offerid", " "+jArray);
				Log.e("userid", " "+userid);
				Log.e("accept", " "+accept);
				
				JSONObject json = userFunction.AcceptDeclineFunction(jArray.toString(), userid, accept);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							msg = json.optString("message");
							errorMessage = "true";

						} else {
							errorMessage = "false";
							msg = json.getString("message");
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

				ToastMessageDisplay(msg);
				if(dialog_item_isbuyer.equals("Y") && accept.equals("Y")){
				Intent i = new Intent(context, AcceptOffer_Buyer.class);
				i.putExtra("notification_text", msg);
				i.putExtra("notification_type", "");
				i.putExtra("notification_offerid", dialog_item_offerid);
				i.putExtra("notification_price", dialog_item_price);
				i.putExtra("notification_itemname", dialog_item_name);
				i.putExtra("notification_fromid", dialog_item_toid);
				i.putExtra("notification_itemid", itemid);
				i.putExtra("notification_itemimage", dialog_item_image);
				i.putExtra("notification_isbuyer", dialog_item_isbuyer);
				startActivity(i);
				finish();
				}else{
					finish();
				}
			
			}else if (result.equals("network")) {
				msg="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,msg, context);
//				ValidationSingleButton("Error in posting");
			} else {
				ToastMessageDisplay(msg);
			}
		}

	

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	public void ToastMessageDisplay(String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		}
		


}
