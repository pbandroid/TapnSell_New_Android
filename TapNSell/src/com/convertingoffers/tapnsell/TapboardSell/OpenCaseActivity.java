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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Modal.CaseListModal;
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

public class OpenCaseActivity extends BaseActivity implements OnClickListener {

	OpenCaseCustomAdapter adapter;
	Animation RightSwipe;
	Context context;
	ArrayList<CaseListModal> mlist = new ArrayList<CaseListModal>();
	ArrayList<String> mItem_IdList = new ArrayList<String>();
 	String 	userid,itemname,itemimage,caseid,fromuserid,fromusername,fromid,tomusername,order_no,name,reason,casenumber,orderamount,respond="",hour,minute,second,chatcount,caseimage,itemid;
// 	
 	ListView lvOpenCase;
 	int lvpading_diamen;
 	//Timer parameter 
 	
	long seconds;
	private CountDownTimer countDownTimer;
	long MainTimeInMilliSecond, CurrentTimeInMilliSecond,totalTimeCountInMilliseconds,
	finalTimeInMilliSecond;
	
	//Timer Parameter
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_board);

		userid = pref.getString("UserID", "");
		tvHeader.setText("Open Cases");
		ivMenu.setVisibility(View.VISIBLE);
		ivMenu.setOnClickListener(this);
		lvpading_diamen=(int)getResources().getDimension(R.dimen.five);
		ivBack.setOnClickListener(this);
		lvOpenCase.setDividerHeight(5);
		lvOpenCase.setPadding(lvpading_diamen, lvpading_diamen, lvpading_diamen, lvpading_diamen);
		if (cd.checkConnection()) {
			new OpenCaseBackTask().execute("");
		} else {
			Toast.makeText(OpenCaseActivity.this,
					"Interner connection is not available!",
					Toast.LENGTH_LONG).show();
		}

		
	
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		lvOpenCase=(ListView)findViewById(R.id.lvMessage);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {

	
		case R.id.ivBack:
			cancelContdowwnTimer();
			i=new Intent(context, TODOS_Activity.class);
			startActivity(i);
			finish();
			break;
			
		case R.id.ivMenu:
			cancelContdowwnTimer();
			buttonAnimation(ivMenu);
			i= new Intent(context ,TapBoardActivity.class);
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		cancelContdowwnTimer();
	}

	
	private class OpenCaseBackTask extends
			AsyncTask<String, Void, String> {
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
				if (userid.length() == 0) {
					userid = "0";
				}
				Log.e("userid", " " + userid);
//				userid="55";
				JSONObject json = userFunction.OpenCaseFunction(userid);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONArray cases = json.getJSONArray("case");
							for (int i = 0; i < cases.length(); i++) {
								JSONObject objCases= cases.getJSONObject(i);
								
								itemid=objCases.getString("itemid");
								itemname=objCases.getString("itemname");
								
								itemimage = objCases.getString("itemimage");
								caseid = objCases.getString("caseid");
								fromuserid = objCases.getString("fromuserid");
								fromusername = objCases.getString("fromusername");
								fromid = objCases.getString("userid");
								tomusername = objCases.getString("tomusername");
								order_no = objCases.getString("order_no");
								name = objCases.getString("name");
								reason = objCases.getString("reason");
								casenumber = objCases.getString("casenumber");
								orderamount = objCases.getString("orderamount");
								respond = objCases.getString("respond");
								hour = objCases.getString("hour");
								minute = objCases.getString("minute");
								second = objCases.getString("second");
								chatcount = objCases.getString("chatcount");
								caseimage = objCases.getString("caseimage");
								
								Log.e("itemid", " "+itemid);
								
								long t_stamp = 0;
								
								
//								if(respond.length()==0 || respond.equals("00:00:00")){
////									respond="24:30:40";
//								}
								String[] split = respond.split(":");
								long hour1,min1,sec1;
								 hour1 = Long.parseLong(split[0]);
								 min1 = Long.parseLong(split[1]);
								 sec1 = Long.parseLong(split[2]);
										Log.e("hour1", " "+hour1);
										Log.e("min1", " "+min1);
										Log.e("sec1", " "+sec1);
								min1=hour1*60+min1;
								sec1=min1*60+sec1;
								t_stamp=sec1*1000;
								Log.e("t_stamp1", " "+t_stamp);
							mlist.add(new CaseListModal(itemid, itemname, itemimage, caseid, fromuserid, fromusername, fromid, tomusername, order_no, name, reason, casenumber, orderamount, respond, hour, minute, second, chatcount, caseimage,""+t_stamp));
							}
							
							errorMessage = "true";
						} else {
							errorMessage = "false";
							message=json.optString("message");
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
				totalTimeCountInMilliseconds = 12864000;
				startTime();
				adapter = new OpenCaseCustomAdapter(context,R.layout.open_case, mlist);
				lvOpenCase.setAdapter(adapter);
				
			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		} else {
				Custom_Dialog.dialogCode(1, null,message,
						OpenCaseActivity.this);
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
					
					
					for (int i = 0; i < mlist.size(); i++) {
						CaseListModal my_offer=mlist.get(i);
						long timestamp =Long.valueOf(my_offer.getTimestamp());
						timestamp=timestamp-1000;
						my_offer.setTimestamp(""+timestamp);
						mlist.set(i, my_offer);
					}
					
					adapter.notifyDataSetChanged();
			
				}
				
				@Override
				public void onFinish() {
					cancelContdowwnTimer();
				}
			}.start();
			
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	public class OpenCaseCustomAdapter extends ArrayAdapter<CaseListModal> {
		ViewHolder holder;
		private ArrayList<CaseListModal> listSubCategories;
		ImageLoader iLoader_item = ImageLoader.getInstance();
		DisplayImageOptions options;
		
		public OpenCaseCustomAdapter(Context context, int textViewResourceId,
				ArrayList<CaseListModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<CaseListModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivDefault;
			RelativeLayout rlChat;
			TextView tvPrice,tvReasonForCase,tvCaseNumber,tvHours,tvMinutes,tvSeconds,tvChatCount;
			CustomTextView tvName;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

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
			
			holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.open_case, null);	
				holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
				holder.tvReasonForCase = (TextView) convertView.findViewById(R.id.tvReasonForCase);
				holder.tvCaseNumber = (TextView) convertView.findViewById(R.id.tvCaseNumber);
				holder.tvHours = (TextView) convertView.findViewById(R.id.tvHours);
				holder.tvMinutes = (TextView) convertView.findViewById(R.id.tvMinutes);
				holder.tvSeconds = (TextView) convertView.findViewById(R.id.tvSeconds);
				holder.tvChatCount = (TextView) convertView.findViewById(R.id.tvChatCount);
				holder.rlChat = (RelativeLayout) convertView.findViewById(R.id.rlChat);
				holder.tvName=(CustomTextView)convertView.findViewById(R.id.tvName);
				holder.ivDefault=(ImageView)convertView.findViewById(R.id.ivDefault);
				
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			CaseListModal m_ItemListing;
			m_ItemListing  = listSubCategories.get(position);
			
			String	CaseImage=m_ItemListing.getCaseimage();
			
			Log.e("case image in adapter"," "+ CaseImage);
			Log.e("case image in m_ItemListing.getCaseimage()"," "+ m_ItemListing.getCaseimage());
			Log.e("case image in getItemimage"," "+ m_ItemListing.getItemimage());
			if(CaseImage!=null && !CaseImage.equals("")){
				
				CaseImage=UserFunctions.localImageUrl;
				CaseImage=CaseImage+m_ItemListing.getCaseimage();
			
			}else{
				CaseImage=UserFunctions.localImageUrl;
				CaseImage=CaseImage+m_ItemListing.getItemimage();
			}
			Log.e("CaseImage ", " "+CaseImage);
			
			// Image display using lazy loading 

			iLoader_item.displayImage(CaseImage, holder.ivDefault, options, new SimpleImageLoadingListener() {
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
			
			

			holder.tvName.setText(m_ItemListing.getName());	
			holder.tvPrice.setText(m_ItemListing.getOrderamount());	
			holder.tvReasonForCase.setText(m_ItemListing.getReason());	
			holder.tvCaseNumber.setText(m_ItemListing.getCasenumber());	
			holder.tvChatCount.setText(m_ItemListing.getChatcount());	
			//// Timer
			
			long t_stamp = Long.parseLong(m_ItemListing.getTimestamp());
			
			/*long s_sec =t_stamp%60;
			long m_min = t_stamp / 60;
			long h_hour = m_min / 60;
			m_min=m_min%60;*/
			Log.e("t_stamp", " "+t_stamp);
		
			long seconds =  (t_stamp / 1000) % 60 ;
			long minutes =  ((t_stamp / (1000*60)) % 60);
			long hours   =  ((t_stamp / (1000*60*60)));
			
			Log.e("hours", " "+hours);
			Log.e("minutes", " "+minutes);
			Log.e("seconds", " "+seconds);
			holder.tvHours.setText(""+hours);
			holder.tvMinutes.setText(""+minutes);
			holder.tvSeconds.setText(""+seconds);
			/// Timer
			holder.rlChat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					buttonAnimation(v);
					CaseListModal m_item = listSubCategories.get(position);
					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						CaseListModal	_list = mlist.get(i);
						mItem_IdList.add(_list.getItemid());
					}
					Intent i = new Intent(context,ResolutionChatActivity.class);
					String toid=m_item.getFromuserid().toString();
					if(toid!=null && !toid.equals("") && ! toid.equals(userid)){
						i.putExtra("from_id", m_item.getFromuserid());
					}else{
						i.putExtra("from_id", m_item.getUserid());
					}
					
					
					i.putExtra("itemid", m_item.getItemid());
					
					i.putExtra("Distance","");
					i.putExtra("CaseModalObject",m_item);
					i.putExtra("ItemArray", mItem_IdList);
					i.putExtra("position", ""+position);
					startActivity(i);
					
				}
			});
			
			return convertView;

		}

	}
	
	
	public void cancelContdowwnTimer() {
		
			if (countDownTimer != null) {
				countDownTimer.cancel();
			}
	}

	
	
	private void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(OpenCaseActivity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
}
