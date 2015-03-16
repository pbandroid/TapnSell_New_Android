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
import com.convertingoffers.tapnsell.Modal.MessageBoardModal;
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

public class MessageBoard_ToDO_Activity extends BaseActivity implements OnClickListener {

	protected ImageLoader iLoader_Rounded = ImageLoader.getInstance();
	Bitmap image_bitmap=null;
	public static DisplayImageOptions options;
	
	Animation RightSwipe;
	Context context;
	String userid;
	String username="",userimage="",message="",itemid="",image="",name="",toid="";
	ArrayList<MessageBoardModal> mList= new ArrayList<MessageBoardModal>();
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	
	ListView lvMessage;
	MessageListCustomAdapter	adapter;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_board);

		userid = pref.getString("UserID", "");
		tvHeader.setText("Message Board");
		ivMenu.setVisibility(View.VISIBLE);

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
		
		LoadImage();
		
		ivBack.setOnClickListener(this);
		ivMenu.setOnClickListener(this);

		if (cd.checkConnection()) {
			TODOSBackTask task=new TODOSBackTask();
			 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		     else
		         task.execute();
		} else {
			Toast.makeText(MessageBoard_ToDO_Activity.this,
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
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {

		case R.id.ivBack:
			 i = new Intent(context, TODOS_Activity.class);
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
	
	private class TODOSBackTask extends
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
				//userid="25";
				Log.e("userid", " " + userid);
				JSONObject json = userFunction.MessageBoardFunction(userid);
				try {
					if (json!=null && json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONArray Item= json.getJSONArray("Item");
							for (int i = 0; i < Item.length(); i++) {
								JSONObject Jmsg= Item.getJSONObject(i);
								
								
								username=Jmsg.getString("username");
								userimage=Jmsg.getString("userimage");
								message=Jmsg.getString("message");
								itemid=Jmsg.getString("itemid");
								image=UserFunctions.localImageUrl;
								image=image+Jmsg.getString("image");
								name=Jmsg.getString("name");
								toid=Jmsg.getString("userid");
								
								mList.add(new MessageBoardModal(username, userimage, message, itemid, image, name, toid));
								
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
				
				adapter = new MessageListCustomAdapter(context,R.layout.message_board_item, mList);
				lvMessage.setAdapter(adapter);
			
			
				
			} else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else {
				Custom_Dialog.dialogCode(2, null,message,
						MessageBoard_ToDO_Activity.this);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	@Override
	protected void onResume() {
	super.onResume();
	
	}
	public class MessageListCustomAdapter extends ArrayAdapter<MessageBoardModal> {
		ViewHolder holder;
		private ArrayList<MessageBoardModal> listSubCategories;
		protected Object mysun;
		
		public MessageListCustomAdapter(Context context, int textViewResourceId,
				ArrayList<MessageBoardModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<MessageBoardModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivUser;
			RelativeLayout rlMain;
			TextView tvName,tvMessage;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.message_board_item, null);	
				
				holder.rlMain = (RelativeLayout) convertView.findViewById(R.id.rlMain);
				holder.ivUser = (ImageView) convertView.findViewById(R.id.ivUser);
				holder.tvName=(TextView)convertView.findViewById(R.id.tvName);
				holder.tvMessage=(TextView)convertView.findViewById(R.id.tvMessage);
				
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		
			MessageBoardModal m_ItemListing;
			m_ItemListing  = listSubCategories.get(position);
			
			String UserImage= "";
			UserImage=m_ItemListing.getUserimage();
				if(UserImage.length()>0){
					iLoader_Rounded.displayImage(UserImage, holder.ivUser, options, loadImageListener);
				}else{
					iLoader_Rounded.displayImage("", holder.ivUser, options, loadImageListener);
				}	
				
				
				holder.tvName.setText(m_ItemListing.getUsername());	
				holder.tvMessage.setText(m_ItemListing.getMessage());	
			
			
			holder.rlMain.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					buttonAnimation(v);
					MessageBoardModal m_item = listSubCategories.get(position);
					
					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						MessageBoardModal	_list = listSubCategories.get(i);
						mItem_IdList.add(_list.getItemid());
					}
					mList.remove(position);
					adapter.notifyDataSetChanged();
					Intent i = new Intent(context,ChatActivity.class);
					i.putExtra("itemid", m_item.getItemid());
					i.putExtra("from_id", m_item.getTo_id());
					i.putExtra("Distance","");
					i.putExtra("ItemArray", mItem_IdList);
					i.putExtra("position", ""+position);
					startActivity(i);
					
				}
			});
			
			return convertView;
		}
	}
	
	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(MessageBoard_ToDO_Activity.this,
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
	
}
