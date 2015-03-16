package com.convertingoffers.tapnsell.TapboardFooter;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Modal.AlertModal;
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

public class Alert_list_Activity extends BaseActivity implements OnClickListener {

	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	EditText evSuggetion;
	Animation RightSwipe;
	Context context;
	RelativeLayout rlSuggetion;
	String userid,comment="";
	ListView lv_Alert;
	ArrayList<AlertModal> mAlertList= new ArrayList<AlertModal>();
	String id,image,name,email,push,sms,allemail,allpush,allsms,emailcount,pushcount,smscount;
	ImageView AllEmail,AllPush,AllSms;
	AlertCustomAdaper adapter;
	boolean boolsms,boolpush,boolemail,boolAllsms=true,boolAllpush=true,boolAllemail=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_listview);

		userid = pref.getString("UserID", "");
		tvHeader.setText("Alerts");
		ivMenu.setVisibility(View.VISIBLE);
		ivBack.setOnClickListener(this);
		AllEmail.setOnClickListener(this);
		AllPush.setOnClickListener(this);
		AllSms.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
		
		if (cd.checkConnection()) {
			new AlertBackTask().execute("");
		} else {
			Toast.makeText(context, "Interner connection is not available!",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		
		
		iLoader_item.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.alert_bg)
		.showImageForEmptyUri(R.drawable.alert_bg)
		.showImageOnFail(R.drawable.alert_bg)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		
		AllEmail= (ImageView) findViewById(R.id.AllEmail);
		AllPush= (ImageView) findViewById(R.id.AllPush);
		AllSms= (ImageView) findViewById(R.id.AllSms);
		
		lv_Alert= (ListView) findViewById(R.id.lv_Alert);
		evSuggetion = (EditText) findViewById(R.id.evSuggetion);
		rlSuggetion= (RelativeLayout) findViewById(R.id.rlSuggetion);
		
	}
	private class AlertBackTask extends
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

				JSONObject json = userFunction.GetAlertFunction(userid);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONArray jalert= json.getJSONArray("alert");
							for (int i = 0; i < jalert.length(); i++) {
								
								JSONObject objAlert =jalert.getJSONObject(i);

								id=objAlert.optString("id");
								image=UserFunctions.localImageUrl;
								image=image+objAlert.optString("image");
								name=objAlert.optString("name");
								email=objAlert.optString("email");
								push=objAlert.optString("push");
								sms=objAlert.optString("sms");
								
								if(email.equals("Y")){
									boolemail=true;
								}else{
									boolemail=false;
								}
								if(push.equals("Y")){
									boolpush=true;
								}else{
									boolpush=false;
								}
								if(sms.equals("Y")){
									boolsms=true;
								}else{
									boolsms=false;
								}
								mAlertList.add(new AlertModal(id, image, name, email, push, sms,boolemail,boolpush,boolsms));
							}
							allemail=json.optString("allemail");
							allpush=json.optString("allpush");
							allsms=json.optString("allsms");
							
							emailcount=json.optString("emailcount");
							pushcount=json.optString("pushcount");
							smscount=json.optString("smscount");
							
							
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
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {
				adapter = new AlertCustomAdaper(context,R.layout.alert_item, mAlertList);
				lv_Alert.setAdapter(adapter);
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

	
	private class ChangeAlertBackTask extends AsyncTask<String, Void, String> {
		
		String userid,type,status,alertid,errorMessage;
		public ChangeAlertBackTask(String change_Alert_user_id,
				String change_Alert_type, String change_Alert_status,
				String change_Alert_id) {
			// TODO Auto-generated constructor stub
			userid=change_Alert_user_id;
			type=change_Alert_type;
			status=change_Alert_status;
			alertid=change_Alert_id;
		}

		protected void onPreExecute() {
			
		}

		@Override
		protected String doInBackground(String... params) {
			{
			
				Log.e("userid", " "+userid);
				Log.e("type", " "+type);
				Log.e("status", " "+status);
				Log.e("alertid", " "+alertid);
				
				JSONObject json = userFunction.ChangeAlertFunction(userid, type
						, status, alertid);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
						
							errorMessage = "true";
						} else {
						
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

			/*if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}*/
		
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ivMenu:
			buttonAnimation(ivMenu);
			 intent = new Intent(context, TapBoardActivity.class);
			startActivity(intent);
			finish();
		break;
		case R.id.ivBack:
			buttonAnimation(ivBack);
			finish();
		break;
		case R.id.AllEmail:
			buttonAnimation(AllEmail);
			if (cd.checkConnection()) {
			if(allemail.equals("Y")){
				String Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id;
				Change_Alert_user_id=userid;
				Change_Alert_type="1";
				Change_Alert_status="N";
				Change_Alert_id="";
				allemail="N";
				for (int i = 0; i < mAlertList.size(); i++) {
					AlertModal m_alert = mAlertList.get(i);
					m_alert.setBoolemail(false);
					adapter.notifyDataSetChanged();
				}
				
					if (cd.checkConnection()) {
						ChangeAlertBackTask task=new ChangeAlertBackTask(Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id);//.execute(msg_item.getId());
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					     else
					         task.execute();
					}
					
					
				} else{
				
				String Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id;
				Change_Alert_user_id=userid;
				Change_Alert_type="1";
				Change_Alert_status="Y";
				Change_Alert_id="";
				allemail="Y";
				for (int i = 0; i < mAlertList.size(); i++) {
					AlertModal m_alert = mAlertList.get(i);
					m_alert.setBoolemail(true);
					adapter.notifyDataSetChanged();
				}
				
				if (cd.checkConnection()) {
					ChangeAlertBackTask task=new ChangeAlertBackTask(Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id);//.execute(msg_item.getId());
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
				} else {
					Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG)
					.show();
				}
				
			}
			}else {
				Toast.makeText(context, "Interner connection is not available!",
						Toast.LENGTH_LONG).show();
			}
		
		break;
		case R.id.AllPush:
			buttonAnimation(AllPush);
			if (cd.checkConnection()) {
			if(allpush.equals("Y")){
				String Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id;
				Change_Alert_user_id=userid;
				Change_Alert_type="2";
				Change_Alert_status="N";
				Change_Alert_id="";
				allpush="N";
				for (int i = 0; i < mAlertList.size(); i++) {
					AlertModal m_alert = mAlertList.get(i);
					m_alert.setBoolpush(false);
					adapter.notifyDataSetChanged();
				}
			
					ChangeAlertBackTask task=new ChangeAlertBackTask(Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id);//.execute(msg_item.getId());
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
					 
				} 
				
				 
			else{
				String Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id;
				Change_Alert_user_id=userid;
				Change_Alert_type="2";
				Change_Alert_status="Y";
				Change_Alert_id="";
				allpush="Y";
				for (int i = 0; i < mAlertList.size(); i++) {
					AlertModal m_alert = mAlertList.get(i);
					m_alert.setBoolpush(true);
					adapter.notifyDataSetChanged();
				}
				if (cd.checkConnection()) {
					ChangeAlertBackTask task=new ChangeAlertBackTask(Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id);//.execute(msg_item.getId());
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
					 
				} else {
					Toast.makeText(context, "Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}
				
			}
			}else {
				Toast.makeText(context, "Interner connection is not available!",
						Toast.LENGTH_LONG).show();
			}
		break;
		case R.id.AllSms:
			buttonAnimation(AllSms);
			if (cd.checkConnection()) {
			if(allsms.equals("Y")){
				String Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id;
				Change_Alert_user_id=userid;
				Change_Alert_type="3";
				Change_Alert_status="N";
				Change_Alert_id="";
				allsms="N";
				for (int i = 0; i < mAlertList.size(); i++) {
					AlertModal m_alert = mAlertList.get(i);
					m_alert.setBoolsms(false);
					adapter.notifyDataSetChanged();
				}
				
					ChangeAlertBackTask task=new ChangeAlertBackTask(Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id);//.execute(msg_item.getId());
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
				
			}else{
				String Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id;
				Change_Alert_user_id=userid;
				Change_Alert_type="3";
				Change_Alert_status="Y";
				Change_Alert_id="";
				allsms="Y";
				for (int i = 0; i < mAlertList.size(); i++) {
					AlertModal m_alert = mAlertList.get(i);
					m_alert.setBoolsms(true);
					adapter.notifyDataSetChanged();
				}
				if (cd.checkConnection()) {
					ChangeAlertBackTask task=new ChangeAlertBackTask(Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id);//.execute(msg_item.getId());
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
				} else {
					Toast.makeText(context, "Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}
				
			}} else {
				Toast.makeText(context, "Interner connection is not available!",
						Toast.LENGTH_LONG).show();
			}
		break;
		default:
			break;
		}
	}
	
	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(Alert_list_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
	
	private class AlertCustomAdaper extends ArrayAdapter<AlertModal> {
		ViewHolder holder;
		private ArrayList<AlertModal> listSubCategories;

		public AlertCustomAdaper(Context context, int textViewResourceId,
				ArrayList<AlertModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<AlertModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivAlertIcon,ivEmail,ivPush,ivSms;
			CustomTextView tvType;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			holder = null;
			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.alert_item, null);

				holder = new ViewHolder();
				holder.ivAlertIcon = (ImageView) convertView.findViewById(R.id.ivAlertIcon);
				holder.ivEmail = (ImageView) convertView.findViewById(R.id.ivEmail);
				holder.ivPush = (ImageView) convertView.findViewById(R.id.ivPush);
				holder.ivSms = (ImageView) convertView.findViewById(R.id.ivSms);
				holder.tvType = (CustomTextView) convertView.findViewById(R.id.tvType);
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			AlertModal m_Alert = listSubCategories.get(position);

			holder.tvType.setText(m_Alert.getName());
			
			String icon_image=m_Alert.getImage();
			
			boolemail = m_Alert.isBoolemail();
			boolsms= m_Alert.isBoolsms();
			boolpush= m_Alert.isBoolpush();
			if(boolemail){
				holder.ivEmail.setImageDrawable(getResources().getDrawable(R.drawable.alert_click_bg));
			}else{
				holder.ivEmail.setImageDrawable(getResources().getDrawable(R.drawable.alert_blank_bg));
			}
			
			if(boolsms){
				holder.ivSms.setImageDrawable(getResources().getDrawable(R.drawable.alert_click_bg));
			}else{
				holder.ivSms.setImageDrawable(getResources().getDrawable(R.drawable.alert_blank_bg));
			}
			
			if(boolpush){
				holder.ivPush.setImageDrawable(getResources().getDrawable(R.drawable.alert_click_bg));
			}else{
				holder.ivPush.setImageDrawable(getResources().getDrawable(R.drawable.alert_blank_bg));
			}
			
			/*
			if (icon_image != null) {
				try {
					imageLoader.DisplayImage(icon_image, holder.ivAlertIcon);
				} catch (Exception e) {
				}
			} else {
				holder.ivAlertIcon.setImageResource(R.drawable.ic_launcher);
			}*/
			
			
			
			// Image display using lazy loading 

			iLoader_item.displayImage(icon_image, holder.ivAlertIcon, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					//holder.pbimage.setProgress(0);
				//	pbimage.setVisibility(View.VISIBLE);
//					holder.pbimage.setIndeterminate(true);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					//pbimage.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					//pbimage.setVisibility(View.GONE);
				}
			}, new ImageLoadingProgressListener() {
				@Override
				public void onProgressUpdate(String imageUri, View view, int current, int total) {
					//holder.pbimage.setProgress(Math.round(100.0f * current / total));
//					holder.pbimage.setIndeterminate(true);
				}
			});
			
			// Image display using lazy loading
			

			holder.ivEmail.setTag(m_Alert);
			holder.ivEmail.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi") @Override
				public void onClick(View v) {
					if (cd.checkConnection()) {
					buttonAnimation(v);
					AlertModal n_alert =null;
					
					n_alert= listSubCategories.get(position);
					
					String Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id;
				
					
					Change_Alert_id=n_alert.getId();
					Change_Alert_status= n_alert.getEmail();
				
					if (Change_Alert_status.equals("N")) {
					
						Change_Alert_status = "Y";
						n_alert.setBoolemail(true);
						n_alert.setEmail(Change_Alert_status );
						listSubCategories.set(position, n_alert);
						notifyDataSetChanged();
						
					} else {
//						holder.ivEmail.setImageDrawable(getResources().getDrawable(R.drawable.alert_blank_bg));
						Change_Alert_status = "N";
						n_alert.setBoolemail(false);
						n_alert.setEmail(Change_Alert_status );
						listSubCategories.set(position, n_alert);
						notifyDataSetChanged();
					}
					
					Change_Alert_type="1";
					Change_Alert_user_id=userid;
					
						ChangeAlertBackTask task=new ChangeAlertBackTask(Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id);//.execute(msg_item.getId());
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					     else
					         task.execute();
						 
					} else {
						Toast.makeText(context,"Interner connection is not available!",Toast.LENGTH_LONG).show();
					}
				}
			});

			holder.ivSms.setTag(m_Alert);
			holder.ivSms.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi") @Override
				public void onClick(View v) {
					if (cd.checkConnection()) {
						String Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id;
						
					buttonAnimation(v);
					AlertModal n_alert =null;
					
					n_alert= listSubCategories.get(position);
					Change_Alert_id=n_alert.getId();
					Change_Alert_status= n_alert.getSms();
					
					if (Change_Alert_status.equals("N")) {
						Change_Alert_status = "Y";
//						holder.ivSms.setImageDrawable(getResources().getDrawable(R.drawable.alert_click_bg));
						n_alert.setBoolsms(true);
						n_alert.setSms(Change_Alert_status);
						listSubCategories.set(position, n_alert);
						notifyDataSetChanged();
					} else {
						Change_Alert_status = "N";
						//holder.ivSms.setImageDrawable(getResources().getDrawable(R.drawable.alert_blank_bg));
						n_alert.setBoolsms(false);
						n_alert.setSms(Change_Alert_status);
						listSubCategories.set(position, n_alert);
						notifyDataSetChanged();
					}

					Change_Alert_type="3";
					Change_Alert_user_id=userid;
					
						ChangeAlertBackTask task=new ChangeAlertBackTask(Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id);//.execute(msg_item.getId());
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					     else
					         task.execute();
						 
					} else {
						Toast.makeText(context,"Interner connection is not available!",Toast.LENGTH_LONG).show();
					}
				}
			});
			
			holder.ivPush.setTag(m_Alert);
			holder.ivPush.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi") @Override
				public void onClick(View v) {
					if (cd.checkConnection()) {
					buttonAnimation(v);
					AlertModal n_alert =null;
					String Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id;
					n_alert= listSubCategories.get(position);
					
					Change_Alert_id=n_alert.getId();
					Change_Alert_status= n_alert.getPush();
					
					if (Change_Alert_status.equals("N")) {
						Change_Alert_status = "Y";
						n_alert.setBoolpush(true);
						n_alert.setPush(Change_Alert_status);
						listSubCategories.set(position, n_alert);
						notifyDataSetChanged();
//						holder.ivSms.setImageDrawable(getResources().getDrawable(R.drawable.alert_click_bg));
					} else {
						Change_Alert_status = "N";
						n_alert.setBoolpush(false);
						n_alert.setPush(Change_Alert_status);
						listSubCategories.set(position, n_alert);
						notifyDataSetChanged();
//						holder.ivSms.setImageDrawable(getResources().getDrawable(R.drawable.alert_blank_bg));
					}

					Change_Alert_type="2";
					Change_Alert_user_id=userid;
					
					
			
						ChangeAlertBackTask task=new ChangeAlertBackTask(Change_Alert_user_id,Change_Alert_type,Change_Alert_status,Change_Alert_id);//.execute(msg_item.getId());
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					     else
					         task.execute();
					} else {
						Toast.makeText(context,"Interner connection is not available!",Toast.LENGTH_LONG).show();
					}
				}
			});
			return convertView;
		}
	}
}