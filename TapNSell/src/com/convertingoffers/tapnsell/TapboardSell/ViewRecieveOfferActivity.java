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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.MakeOffer.AcceptOffer_Buyer;
import com.convertingoffers.tapnsell.MakeOffer.MakeOfferActivity;
import com.convertingoffers.tapnsell.MakeOffer.ViewAllOfferActivity;
import com.convertingoffers.tapnsell.Modal.OfferDetailsModal;
import com.convertingoffers.tapnsell.Shop.checkout.MainCheckOutFragmentActivity;
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

//
public class ViewRecieveOfferActivity extends BaseActivity implements
		OnClickListener {
	String address="N";
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	Intent i_Buy;
//	
	String itemid = "", amount = "", userid = "", image = "", name = "",
			offerid = "", accept = "", noti_msg = "", type = "", price = "",
			to_id = "", isbuyer = "", dialog_msg = "", status_isbuyer,expiretime="";

	ArrayList<OfferDetailsModal> mOffer_list = new ArrayList<OfferDetailsModal>();
	ListView lv_offer;
	OfferDetailsModal myoffer_detail;
	ImageView ivRecieve, ivMade;
	Context context;
	ListingOfferCustomAdaper adapter;
	boolean Status_of_btn = false,once=false;
	
	LinearLayout llShort;
	int start = 0, intStartNewListing = 0, count = 0;;
	String item_id = "", item_name = "", Acceptofferid = "", offeramount = "",
			itemprice = "", is_last = "";

	
 	
	long seconds;
	private CountDownTimer countDownTimer;
	long MainTimeInMilliSecond, CurrentTimeInMilliSecond,totalTimeCountInMilliseconds,
	finalTimeInMilliSecond;
	String made_makeoffer,made_makeoffer_pos;
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offer_recv);

		made_makeoffer=pref.getString("MakeOfferActivity_type_offer", "N");
		made_makeoffer_pos=pref.getString("MakeOfferActivity_position", "0");
		
		editor.remove("MakeOfferActivity_type_offer");
		editor.remove("MakeOfferActivity_position");
		editor.commit();
		userid = pref.getString("UserID", "");
		

		ivMade.setOnClickListener(this);
		ivRecieve.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		tvHeader.setText("Offers");
		
		if(made_makeoffer.equals("Y")){
			Status_of_btn = true;
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(this.getResources().getDrawable(
						R.drawable.offer_made_sort_bg));
			else
				llShort.setBackgroundDrawable(this.getResources().getDrawable(
						R.drawable.offer_made_sort_bg));
		}else{
			Status_of_btn = false;
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(this.getResources().getDrawable(
						R.drawable.offer_recv_sort_bg));
			else
				llShort.setBackgroundDrawable(this.getResources().getDrawable(
						R.drawable.offer_recv_sort_bg));	
		}
		
		if (cd.checkConnection()) {
			new OfferReciveBackTask().execute("");
		} else {
			Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context = this;
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
		 
		lv_offer = (ListView) findViewById(R.id.lv_offer);
		ivRecieve = (ImageView) findViewById(R.id.ivRecieve);
		ivMade = (ImageView) findViewById(R.id.ivMade);
		llShort = (LinearLayout) findViewById(R.id.llShort);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.ivRecieve:
			cancelContdowwnTimer();
			mOffer_list.clear();
			Status_of_btn = false;
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(ViewRecieveOfferActivity.this
						.getResources().getDrawable(
								R.drawable.offer_recv_sort_bg));
			else
				llShort.setBackgroundDrawable(ViewRecieveOfferActivity.this
						.getResources().getDrawable(
								R.drawable.offer_recv_sort_bg));

			if (cd.checkConnection()) {
				new OfferReciveBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG)
						.show();
			}

			break;
		case R.id.ivMade:
			cancelContdowwnTimer();
			mOffer_list.clear();
			if (Build.VERSION.SDK_INT >= 16)
				llShort.setBackground(ViewRecieveOfferActivity.this
						.getResources().getDrawable(
								R.drawable.offer_made_sort_bg));
			else
				llShort.setBackgroundDrawable(ViewRecieveOfferActivity.this
						.getResources().getDrawable(
								R.drawable.offer_made_sort_bg));

			Status_of_btn = true;
			if (cd.checkConnection()) {
				new OfferReciveBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG)
						.show();
			}
			break;

		case R.id.ivBack:
			cancelContdowwnTimer();
			i=new Intent(context, TODOS_Activity.class);
			startActivity(i);
			finish();
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
	@Override
	protected void onDestroy() {
		
		cancelContdowwnTimer();
		super.onDestroy();
	}
//sahi
	private class OfferReciveBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@SuppressLint("SimpleDateFormat") @Override
		protected String doInBackground(String... params) {
			{
				
//				userid="175";
				Log.e("userid", " " + userid);
				Log.e("intStartNewListing", " " + intStartNewListing);
				Log.e("Status_of_btn", " " + Status_of_btn);

				
				JSONObject json = userFunction.OfferReciveFunction(userid, ""
						+ intStartNewListing, Status_of_btn);

				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						is_last = json.getString("islast");
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject offer = json.getJSONObject("offer");

							JSONArray jOffer = offer.getJSONArray("data");
							for (int i = 0; i < jOffer.length(); i++) {
								JSONObject objOffer = jOffer.getJSONObject(i);

								item_id = objOffer.getString("item_id");
								item_name = objOffer.getString("item_name");
								offerid = objOffer.getString("offerid");
								offeramount = objOffer.getString("offeramount");
								itemprice = objOffer.getString("itemprice");
								image = UserFunctions.localImageUrl;
								image = image + objOffer.getString("image");
								
								if (Status_of_btn) {
									to_id = objOffer.getString("touserid");
								}else{
									to_id = objOffer.getString("fromid");	
									
								}
							
								isbuyer = objOffer.getString("isbuyer");
								expiretime= objOffer.getString("expiretime");
								
								if(expiretime.length()==0){
									expiretime="00:00:00";
								}
								String[] split = expiretime.split(":");
								long hour,min,sec,t_stamp;
								 hour = Long.parseLong(split[0]);
								 min = Long.parseLong(split[1]);
								 sec = Long.parseLong(split[2]);
								
												
								min=hour*60+min;
								sec=min*60+sec;
								t_stamp=sec*1000;
							//	Log.e("sec", ""+t_stamp);
								
								mOffer_list.add(new OfferDetailsModal(item_id,
										item_name, offerid, offeramount,
										itemprice, image, to_id, isbuyer,""+t_stamp));

							}
							errorMessage = "true";

						} else {
							is_last = json.optString("islast");
							message = json.optString("message");
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

		@SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			
			adapter = new ListingOfferCustomAdaper(context,
					R.layout.offer_recv_child, mOffer_list);
			lv_offer.setAdapter(adapter);
			if(made_makeoffer.equals("Y")){
				int pos = 0;
				try {
					pos = Integer.valueOf(made_makeoffer_pos);
				} catch (NumberFormatException e) {
					pos = 0;
					e.printStackTrace();
				}
				lv_offer.setSelectionFromTop(pos, 0);
			}
			
			if (result.equals("true")) {
				
				totalTimeCountInMilliseconds = 86400000;
				startTime();
				
				lv_offer.setSelectionFromTop(count, 0);
				lv_offer.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) { // TODO Auto-generated method
												// stub
						int threshold = 1;
						count = 0;
						count = lv_offer.getCount();

						if (is_last.equals("N")) {
							if (scrollState == SCROLL_STATE_IDLE) {

								if (lv_offer.getLastVisiblePosition() >= count
										- threshold) {
									// Execute LoadMoreDataTask AsyncTask
									intStartNewListing = intStartNewListing + 20;
									new OfferReciveBackTask().execute("");
								}
							}
						}
					}

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						
					}

				});

				// shop screen
			} else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else if (result.equals("false")) {
				Status_of_btn=true;
				if(Status_of_btn && !once){
					
					
					if (Build.VERSION.SDK_INT >= 16)
						llShort.setBackground(ViewRecieveOfferActivity.this
								.getResources().getDrawable(
										R.drawable.offer_made_sort_bg));
					else
						llShort.setBackgroundDrawable(ViewRecieveOfferActivity.this
								.getResources().getDrawable(
										R.drawable.offer_made_sort_bg));
					new OfferReciveBackTask().execute("");	
					
					once=true;
				}else{
					Custom_Dialog.dialogCode(2, null, message, context);	
				}
				
//				
			} else {
				Custom_Dialog.dialogCode(2, null, message, context);
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	
	
	public void cancelContdowwnTimer() {
		
			if (countDownTimer != null) {
				countDownTimer.cancel();
			}
	}
	public class ListingOfferCustomAdaper extends
			ArrayAdapter<OfferDetailsModal> {
		ViewHolder holder;
		private ArrayList<OfferDetailsModal> listSubCategories;
		protected Object mysun;

		public ListingOfferCustomAdaper(Context context,
				int textViewResourceId,
				ArrayList<OfferDetailsModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<OfferDetailsModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			CustomTextView tvName;
			ImageView ivDefault, ivAllOffer, ivCounter, ivAcceptOffer;
			TextView tvPrice, tvOfferPrice,tvTime;
		}

		@SuppressLint("DefaultLocale") @Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if (Status_of_btn) {
					convertView = vi.inflate(R.layout.offer_made_child, null);
				} else {
					convertView = vi.inflate(R.layout.offer_recv_child, null);
				}

				holder.ivAllOffer = (ImageView) convertView.findViewById(R.id.ivAllOffer);
				holder.tvName = (CustomTextView) convertView.findViewById(R.id.tvName);
				holder.ivAcceptOffer = (ImageView) convertView.findViewById(R.id.ivAcceptOffer);
				holder.ivCounter = (ImageView) convertView.findViewById(R.id.ivCounter);
				holder.ivDefault = (ImageView) convertView.findViewById(R.id.ivDefault);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
				holder.tvOfferPrice = (TextView) convertView.findViewById(R.id.tvOfferPrice);
				holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			OfferDetailsModal myOffer = listSubCategories.get(position);
			String image1 = myOffer.getImage();

			holder.tvName.setText("" + myOffer.getItem_name());

			
			
			long t_stamp = Long.parseLong(myOffer.getExpiretime());
			long seconds =  (t_stamp / 1000) % 60 ;
			long minutes =  ((t_stamp / (1000*60)) % 60);
			long hours   =  ((t_stamp / (1000*60*60)));
			String timer = hours +":"+minutes +":"+seconds ;
			holder.tvTime.setText(timer);
			//Log.e("t_stamp adapter", " "+t_stamp);
			/*long s_sec =t_stamp/1000;
			long m_min = t_stamp / 60;
			long h_hour = m_min % 60;
			m_min=m_min%60;
			
			String hour =String.format("%02d", h_hour);
			String min =String.format("%02d", m_min);
			String sec =String.format("%02d", s_sec);*/
			
//			int[] split_array =	splitToComponentTimes(t_stamp);
//			startTimer(t_stamp);
			/*countDownTimer = new CountDownTimer(t_stamp, 1000) {
				// 500 means, onTick function will be called at every 500
				// milliseconds
			
				@SuppressLint("DefaultLocale")
				@Override
				public void onTick(long leftTimeInMilliseconds) {
				
					
					if(listSubCategories.size()!=0){
					OfferDetailsModal offer_item=	listSubCategories.get(position);
					offer_item.setExpiretime(""+leftTimeInMilliseconds);
					listSubCategories.set(position, offer_item);
				
					}
					notifyDataSetChanged();
				}
				
				@Override
				public void onFinish() {
					cancelContdowwnTimer();
				}
			}.start();
			
			int seconds = (int) (t_stamp / 1000) % 60 ;
			int minutes = (int) ((t_stamp / (1000*60)) % 60);
			int hours   = (int) ((t_stamp / (1000*60*60)) % 24);
			
			String timer = hours +":"+minutes +":"+seconds ;
			
			holder.tvTime.setText(timer);*/
			
			
			// Image display using lazy loading 

			iLoader_item.displayImage(image1,holder.ivDefault, options, new SimpleImageLoadingListener() {
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
			
			
			
			
			
			
			
			holder.tvPrice.setText("" + myOffer.getItemprice());
			if (Status_of_btn) {
				holder.tvOfferPrice.setText("$" + myOffer.getOfferamount());
			} else {
				holder.tvOfferPrice.setText("" + myOffer.getOfferamount());
			}

			holder.tvOfferPrice.setText("" + myOffer.getOfferamount());
			holder.ivCounter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Log.e("i am in counter", "i am in counter");
					myoffer_detail = listSubCategories.get(position);
//					Intent intent;

					
					Log.e("toid", " "+ myoffer_detail.getTo_id());
					
/*						intent = new Intent(context, CounterOfferActivity.class);
						intent.putExtra("Counter_text", "");
						intent.putExtra("Counter_type", "counter_offer");
						intent.putExtra("Counter_offerid",
								"" + myoffer_detail.getOfferid());
						intent.putExtra("Counter_price","" + myoffer_detail.getOfferamount());
						intent.putExtra("Counter_name","" + myoffer_detail.getItem_name());
						
							intent.putExtra("Counter_to_id","" + myoffer_detail.getTo_id());
						Log.e("yyyyyyyyyyyyyyyyy", " "+myoffer_detail.getTo_id());
						intent.putExtra("Counter_itemid","" + myoffer_detail.getItem_id());
						intent.putExtra("Counter_image","" + myoffer_detail.getImage());
						intent.putExtra("Counter_isbuyer", "N");
						startActivity(intent);
						*/
						
						editor.putString("MakeOfferActivity_type_offer", "Y");
						editor.putString("MakeOfferActivity_position",""+ position);
						editor.commit();
						String itemid = myoffer_detail.getItem_id();
						String image = myoffer_detail.getImage();
						String name = myoffer_detail.getItem_name();

						Intent i = new Intent(context,MakeOfferActivity.class);
						i.putExtra("moffer_itemid", "" + itemid);
						i.putExtra("moffer_image", "" + image);
						i.putExtra("moffer_name", "" + name);
						
						startActivity(i);
						
						
//					}//
				}
			});

			holder.ivAllOffer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					myoffer_detail = listSubCategories.get(position);
					Intent intent;
					intent = new Intent(context, ViewAllOfferActivity.class);
					Log.e("AllOffer_itemid", " " + myoffer_detail.getItem_id());
					intent.putExtra("AllOffer_itemid",
							"" + myoffer_detail.getItem_id());
					intent.putExtra("AllOffer_toid",
							"" + myoffer_detail.getTo_id());
					startActivity(intent);
				}
			});

			holder.ivAcceptOffer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					myoffer_detail = listSubCategories.get(position);
					Acceptofferid = myoffer_detail.getOfferid();
					status_isbuyer = myoffer_detail.getIsbuyer();
					offerid = myoffer_detail.getOfferid();
					price = myoffer_detail.getItemprice();
					name = myoffer_detail.getItem_name();
					to_id = myoffer_detail.getTo_id();
					itemid = myoffer_detail.getItem_id();
					image = myoffer_detail.getImage();
					isbuyer = myoffer_detail.getIsbuyer();

					Log.e("Acceptofferid before", " " + Acceptofferid);
					if (!Status_of_btn) {
						if (cd.checkConnection()) {
							new Accept_Decline_OfferBackTask().execute("");
						} else {
							Toast.makeText(context, "internet_conn_failed",
									Toast.LENGTH_LONG).show();
						}
					} else {

						BuybtnCode();

					}
				}
			});

			return convertView;

		}

	}
	
	private void startTime() {
		
		countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
			// 500 means, onTick function will be called at every 500
			// milliseconds
		
			@SuppressLint("DefaultLocale")
			@Override
			public void onTick(long leftTimeInMilliseconds) {
				Log.e("leftTimeInMilliseconds  ", ""+leftTimeInMilliseconds);
//				Log.e("OnTick call ", "On Tick Call "+position);
				
				
				for (int i = 0; i < mOffer_list.size(); i++) {
					OfferDetailsModal my_offer=mOffer_list.get(i);
					long timestamp =Long.valueOf(my_offer.getExpiretime());
					timestamp=timestamp-1000;
					my_offer.setExpiretime(""+timestamp);
					mOffer_list.set(i, my_offer);
				}
				
				adapter.notifyDataSetChanged();
				
				
			/*	int seconds = (int) (leftTimeInMilliseconds / 1000) % 60 ;
				int minutes = (int) ((leftTimeInMilliseconds / (1000*60)) % 60);
				int hours   = (int) ((leftTimeInMilliseconds / (1000*60*60)) % 24);
				
				CaseListModal offer_item=	listSubCategories.get(position);
				offer_item.setTimestamp(""+leftTimeInMilliseconds);
				listSubCategories.set(position, offer_item);
				
				
				holder.tvHours.setText(""+hours);
				holder.tvMinutes.setText(""+minutes);
				holder.tvSeconds.setText(""+seconds);
				
				notifyDataSetChanged();*/
			}
			
			@Override
			public void onFinish() {
				cancelContdowwnTimer();
			}
		}.start();
		
	}

/*	public static int[] splitToComponentTimes(long longVal)
	{
//	    long longVal = biggy.longValue();
	    int hours = (int) longVal / 3600;
	    int remainder = (int) longVal - hours * 3600;
	    int mins = remainder / 60;
	    remainder = remainder - mins * 60;
	    int secs = remainder;

	    int[] ints = {hours , mins , secs};
	    return ints;
	}*/
	
	public void BuybtnCode() {

		mItem_IdList.clear();
		mItem_IdList.add(itemid);

		editor.putString("CHKImage", image);
		editor.putString("CHKItemid", itemid);
		editor.putString("CHKName", name);
		editor.putString("CHKPrice", price);
		editor.commit();

		i_Buy = new Intent(context, MainCheckOutFragmentActivity.class);
		i_Buy.putExtra("itemid", itemid);
		i_Buy.putExtra("from_id", to_id);
		i_Buy.putExtra("Distance", "0");
		i_Buy.putExtra("ItemArray", mItem_IdList);
		i_Buy.putExtra("position", "0");

		new LockItemBackTask().execute("");

	}

	private class LockItemBackTask extends AsyncTask<String, Void, String> {
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

				JSONObject json = userFunction.ReserveFunction(userid, itemid,
						"Y");
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							address=json.optString("address");
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
				i_Buy.putExtra("address", address);
				startActivity(i_Buy);
			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		} else {
				Custom_Dialog.dialogCode(2, null, message, context);
				// ValidationSingleButton("Error in posting");
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	private class Accept_Decline_OfferBackTask extends
			AsyncTask<String, Void, String> {
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
				Log.e("Acceptofferid before", " " + Acceptofferid);
				jArray = new JSONArray();
				jArray.put(Acceptofferid);
				Log.e("offerid", " " + jArray);
				Log.e("userid", " " + userid);
				

				JSONObject json = userFunction.AcceptDeclineFunction(
						jArray.toString(), userid, "Y");
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							// offerid= json.getString("offerid");
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
				// custom_dialog.dialogCode(1, null, msg, con);
				ToastMessageDisplay(msg);
				if (status_isbuyer.equals("Y")) {
					Intent i = new Intent(context, AcceptOffer_Buyer.class);
					i.putExtra("notification_text", msg);
					i.putExtra("notification_type", type);
					i.putExtra("notification_offerid", offerid);
					i.putExtra("notification_price", price);
					i.putExtra("notification_itemname", name);
					i.putExtra("notification_fromid", to_id);
					i.putExtra("notification_itemid", itemid);
					i.putExtra("notification_itemimage", image);
					i.putExtra("notification_isbuyer", isbuyer);
					startActivity(i);
					finish();
				} else {
					finish();
				}

			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		} else {
				ToastMessageDisplay(msg);
				// custom_dialog.dialogCode(2, null, msg, con);
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
