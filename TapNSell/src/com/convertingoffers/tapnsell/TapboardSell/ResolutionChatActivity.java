package com.convertingoffers.tapnsell.TapboardSell;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Modal.CaseListModal;
import com.convertingoffers.tapnsell.Modal.ConversionModal;
import com.convertingoffers.tapnsell.Shop.ProductDetails;
import com.convertingoffers.tapnsell.Shop.ChatActivity.ChatDisplayBackTask;
import com.convertingoffers.tapnsell.Shop.ChatActivity.MyReceiver;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.ImageLoaderforRounded;
import com.convertingoffers.tapnsell.util.RoundedImageView_CenterCrop;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ResolutionChatActivity extends BaseActivity implements OnClickListener {

	
	public static DisplayImageOptions options;
	public MyReceiver br_custom;
	IntentFilter filter;
	protected ImageLoader iLoader_Rounded = ImageLoader.getInstance();
	Custom_Dialog custom_dialog;
	Context con ;
	MemoryCache memoryCache ;//;//
	String itemid = "",userid="", from_id = "",is_last="",name,image="",position="",fromuser_image="",caseid,temp_time="0",to_id="", Distance="",fromuser_name="";
	ArrayList<String> dialogItem = new ArrayList<String>();
	EditText evMessage;
	RelativeLayout rlSlider;
	ImageView ivCamera,ivSend,ivClose,ivChatIcon,ivItemBack,ivProduct,ivClose_case;
	boolean scroll_status=false;
	ListView lvt,lvtDialog;
	 TextView tvSeconds,tvMinutes,tvHours;
	ArrayList<ConversionModal> mMessage = new ArrayList<ConversionModal>();
	ConversionCustomAdaper adapter;
	ImageLoaderforRounded iLoader;
	Adapter Dialogadapter;
	Integer count=0,start=0,intStartNewListing=0;
	Bitmap bitmapCamera = null,image_bitmap=null;
	public static String btnStatus="";
	ArrayList<String> mItemList = new ArrayList<String>();
	 int Device_width=0,Device_height=0;
	 CaseListModal m_item ;
	 long t_stamp=0;
	 LinearLayout llInfo;
	 
	 	long seconds;
		private CountDownTimer countDownTimer;
		long MainTimeInMilliSecond, CurrentTimeInMilliSecond,
		finalTimeInMilliSecond;
		String picturePath="";
		public static final int MEDIA_TYPE_IMAGE = 1,RESULT_LOAD_CAMERA_IMAGE = 2;
		private Uri fileUri;
		private static final String IMAGE_DIRECTORY_NAME = "ResolutionChat";
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resolution_chat_layout );

		ivMenu.setVisibility(View.VISIBLE);
		ivMenu.setOnClickListener(this);
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
		
		mItemList.clear();
		tvHeader.setText("Resolutions Chat");
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
		itemid = bundle.getString("itemid");
		to_id = bundle.getString("from_id");
		m_item= (CaseListModal) bundle.getSerializable("CaseModalObject");	
		mItemList= (ArrayList<String>) bundle.getSerializable("ItemArray");
		position=bundle.getString("position");
		caseid=m_item.getCaseid();
		}

		t_stamp=Long.parseLong(m_item.getTimestamp());
		
		
		
		long seconds =(t_stamp / 1000) % 60 ;
		long minutes =((t_stamp / (1000*60)) % 60);
		long hours   =((t_stamp / (1000*60*60)));
		tvHours.setText(""+hours);
		tvMinutes.setText(""+minutes);
		tvSeconds.setText(""+seconds);
		
		LoadImage();
		userid = pref.getString("UserID", "");
		from_id=userid;
		if (cd.checkConnection()) {
			new ChatDisplayBackTask().execute("");
		} else {
			Toast.makeText(con, "internet_conn_failed", Toast.LENGTH_LONG).show();
		}
		
		countDownTimer = new CountDownTimer(t_stamp, 1000) {
			// 500 means, onTick function will be called at every 500
			// milliseconds
		
			@SuppressLint("DefaultLocale")
			@Override
			public void onTick(long leftTimeInMilliseconds) {
			
				long seconds =(leftTimeInMilliseconds / 1000) % 60 ;
				long minutes = ((leftTimeInMilliseconds / (1000*60)) % 60);
				long hours   =((leftTimeInMilliseconds / (1000*60*60)));
				
				t_stamp=leftTimeInMilliseconds;
				//String timer = hours +":"+minutes +":"+seconds ;
				
				tvHours.setText(""+hours);
				tvMinutes.setText(""+minutes);
				tvSeconds.setText(""+seconds);
								
			}
			
			@Override
			public void onFinish() {
				cancelContdowwnTimer();
			}
		}.start();
	
		llInfo.setOnClickListener(this);
		ivCamera.setOnClickListener(this);

		ivSend.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		ivChatIcon.setOnClickListener(this);
		ivClose.setOnClickListener(this);
		ivClose_case.setOnClickListener(this);
	}
	public void cancelContdowwnTimer() {
		
			if (countDownTimer != null) {
				countDownTimer.cancel();
			}
	}
	@Override
	protected void onDestroy() {
		
		cancelContdowwnTimer();		
		super.onDestroy();
	}
	private void LoadImage() {
		
		 image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chat_info_default_image);
	}
	private class ChatDisplayBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(con);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{

				Log.e("caseid", " "+caseid);
				Log.e("userid", " "+userid);
				Log.e("start", " "+start);
		
				int t1 =0,t2=0;
				JSONObject json = userFunction.DisplayResolutionChatFunction(userid,caseid,""+start);
			
				
				
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							t1=Integer.parseInt(temp_time);
							
							temp_time =json.optString("time");
							t2=Integer.parseInt(temp_time);
							
							if(t2>t1){
								temp_time=""+t2;
							}else{
								temp_time=""+t1;
							}
							
							String message_id,msg,msg_type,msg_image,user_type,user_image,user_name,date;
							is_last=json.getString("is_last");
							JSONObject cItem =json.getJSONObject("Item");
							itemid=cItem.getString("itemid");
							name=cItem.getString("name");
							image=UserFunctions.localImageUrl;
							image=image+cItem.getString("image");
							Log.e("image", " "+image);
							JSONObject cUser =json.getJSONObject("User");
//							fromuser_image=UserFunctions.localImageUrl;
							fromuser_image=cUser.getString("fromuser_image");
							fromuser_name=cUser.getString("fromuser_name");
							
							JSONArray Message = json.getJSONArray("Message");
							for (int i = 0; i < Message.length(); i++) {
								
								JSONObject c = Message.getJSONObject(i);
								message_id=c.getString("detail_id");
								msg=c.getString("msg");
								msg_type=c.getString("msg_type");
								msg_image=c.getString("msg_image");
								user_type=c.getString("user_type");
								user_image=c.getString("touser_image");
								user_name=c.optString("user_name");
								date=c.getString("date");
								if(!scroll_status){
									mMessage.add(new ConversionModal(message_id, msg, msg_type, msg_image, user_type, user_image, user_name, date));
								}else{
									mMessage.add(i,new ConversionModal(message_id, msg, msg_type, msg_image, user_type, user_image, user_name, date));
								}
								}
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
				
		
			
			if(image.length()>0){
				iLoader_Rounded.displayImage(image, ivProduct, options, loadImageListener);
			}else{
				iLoader_Rounded.displayImage("", ivProduct, options, loadImageListener);
			}	
			if (image != null) {
				try {
					iLoader.DisplayImage(image, ivItemBack);
					
				} catch (Exception e) {
				}
			} else {
				ivProduct.setImageResource(R.drawable.ic_launcher);
			}
			
			
			
			adapter = new ConversionCustomAdaper(con,R.layout.chat_item, mMessage);
			lvt.setAdapter(adapter);
			if(!scroll_status){
				count = lvt.getCount();
				lvt.setSelectionFromTop(count, 0);	
			}else{
				scroll_status=false;
				lvt.setSelectionFromTop(20, 0);
			}
			
			lvt.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) { 
					scroll_status=true;
					int threshold = 0;
					count = 0;
					count = lvt.getCount();
					
					if(is_last.equals("N")){								
						if (scrollState == SCROLL_STATE_IDLE) {
							Log.e("lvt.getFirstVisiblePosition()", ""+lvt.getFirstVisiblePosition());
							if (lvt.getFirstVisiblePosition() == threshold) {
								// Execute LoadMoreDataTask AsyncTask
								start=start+20;
								new ChatDisplayBackTask().execute("");								
							}
						}
					}							
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					
				}

			});	
			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, ResolutionChatActivity.this);
		}else if(result.equals("false")){
				
			}else{
				Custom_Dialog.dialogCode(2, null,message,
						ResolutionChatActivity.this);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	
		
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		con=this;
		br_custom = new MyReceiver();
		filter = new IntentFilter();
		filter.addAction("Abc");
        
		custom_dialog=new Custom_Dialog();
		iLoader = new ImageLoaderforRounded(con);
		evMessage=(EditText)findViewById(R.id.evMessage);
		ivProduct=(ImageView)findViewById(R.id.ivProduct);
		ivItemBack=(ImageView)findViewById(R.id.ivItemBack);
		ivCamera=(ImageView)findViewById(R.id.ivCamera);
		ivSend=(ImageView)findViewById(R.id.ivSend);
		ivClose=(ImageView)findViewById(R.id.ivClose);
		ivChatIcon=(ImageView)findViewById(R.id.ivChatIcon);
		ivClose_case=(ImageView)findViewById(R.id.ivClose_case);
		lvt=(ListView)findViewById(R.id.lvtChat);
		rlSlider=(RelativeLayout)findViewById(R.id.rlSlider);
		tvSeconds = (TextView) findViewById(R.id.tvSeconds);
		tvMinutes = (TextView) findViewById(R.id.tvMinutes);
		tvHours = (TextView) findViewById(R.id.tvHours);
		 llInfo = (LinearLayout) findViewById(R.id.llInfo);
	}


	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		Intent i;
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		switch (v.getId()) {
		case R.id.llInfo:
			imm.hideSoftInputFromWindow(evMessage.getWindowToken(), 0);
			cancelContdowwnTimer();
		i = new Intent(ResolutionChatActivity.this,
				ProductDetails.class);
		i.putExtra("ItemArray", mItemList);
		i.putExtra("position", ""+position);
		startActivity(i);
		
		break;
		
		case R.id.ivMenu:
			imm.hideSoftInputFromWindow(evMessage.getWindowToken(), 0);
			cancelContdowwnTimer();
			 i= new Intent(ResolutionChatActivity.this , TapBoardActivity.class);
			startActivity(i);
			finish();			
		break;
		case R.id.ivCamera:
			imm.hideSoftInputFromWindow(evMessage.getWindowToken(), 0);
			dialogItem.clear();
			dialogItem.add("Camera");
			dialogItem.add("Gallery");
			
			final Dialog dialog = new Dialog(ResolutionChatActivity.this);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			dialog.setContentView(R.layout.itemdetails_listview);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		    lp.copyFrom(dialog.getWindow().getAttributes());
		    lp.width = lp.MATCH_PARENT;
		    lp.height = lp.MATCH_PARENT;
		    dialog.getWindow().setAttributes(lp);
				
				dialog.show();
				dialog.setCanceledOnTouchOutside(true);
				lvtDialog = (ListView) dialog.findViewById(R.id.lvDialog);
				Dialogadapter = new Adapter(ResolutionChatActivity.this, dialogItem);
				lvtDialog.setAdapter(Dialogadapter);
				lvtDialog.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
					
						 	String ClickIngType = dialogItem.get(position);
							if(ClickIngType.equals("Camera")){
								dialog.dismiss();
								/*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
								startActivityForResult(intent, 42); 
							      */  
								
								 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
									fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
									picturePath = fileUri.getPath();
									intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
									startActivityForResult(intent,RESULT_LOAD_CAMERA_IMAGE);
									
							       
							}else{

								dialog.dismiss();
								Intent i = new Intent(Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(i, 41);
								
							}
					}
				});
			
		break;
		
	/*	case R.id.ivInfo:
			
			 i = new Intent(ResolutionChatActivity.this,
					ProductDetails.class);
			i.putExtra("ItemArray", mItemList);
			i.putExtra("position", ""+position);
			startActivity(i);
			
		break;*/
		
		case R.id.ivSend:
			SendCode();
		break;
		
		case R.id.ivClose_case:
			imm.hideSoftInputFromWindow(evMessage.getWindowToken(), 0);
			
			Two_Button_dialogCode("Are you sure you want to close the case?");
			
			
			
			
		
		break;
		
		case R.id.ivBack:
		
			imm.hideSoftInputFromWindow(evMessage.getWindowToken(), 0);
		finish();
		break;

		case R.id.ivChatIcon:
			imm.hideSoftInputFromWindow(evMessage.getWindowToken(), 0);
			Log.e("TEST BUTTON", "TEST BUTTON");
		rlSlider.setVisibility(View.VISIBLE);
		ivChatIcon.setVisibility(View.GONE);
		
		break;
		
		case R.id.ivClose:
			
			imm.hideSoftInputFromWindow(evMessage.getWindowToken(), 0);
		rlSlider.setVisibility(View.GONE);
		ivChatIcon.setVisibility(View.VISIBLE);
		
		break;
		
		default:
			break;
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
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}
	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(Environment.
				getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
//				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile = null;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		}	
		

		return mediaFile;
	}
	@SuppressLint("NewApi") @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
	if (requestCode == 41 && resultCode == RESULT_OK && null != data) {
		bitmapCamera=null;
		 Uri selectedImage = data.getData();

		 try {
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				String path="";
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				path = cursor.getString(columnIndex);
				cursor.close();
				
				
				Runtime.getRuntime().gc();
				Log.e("Gallery", "Gallery");
				Log.e("path", " " + path);
				boolean sd=path.contains("http");
				Log.e("test check", " "+sd);
				if(!path.contains("http"))
				{
				File filenew = new File(path);
				int file_size = Integer.parseInt(String.valueOf(filenew
						.length() / 1024));
				Log.e("file_size", " " + file_size);
				
				Matrix matrix = new Matrix();
				
				if (file_size < 500) {
					
					Log.e("image size is less", "image size is less");
					bitmapCamera = BitmapFactory.decodeFile(path);
				} else {
					Log.e("image size is heavy", "image size is heavy");
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 4;
					bitmapCamera = BitmapFactory.decodeFile(path, options);
					Log.e("path", " " + path);
				}
				matrix.postRotate(0);
				bitmapCamera = Bitmap.createBitmap(bitmapCamera, 0, 0, bitmapCamera.getWidth(),
						bitmapCamera.getHeight(), matrix, true);
				}else{
					
				}
				} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	
		if(bitmapCamera!=null){
		new DoneBackTask().execute("");
		}else{
//			ValidationSingleButton("Capture image is not valid");
			Custom_Dialog.dialogCode(2, null, "Image is not valid.", con);
		}
	}else if(requestCode == 2 && resultCode == RESULT_OK){
		Runtime.getRuntime().gc();
		bitmapCamera=null;
			String path="";
			path=picturePath;
			
			Log.e("Camera", "Camera");
			Log.e("path", " " + path);

			File filenew = new File(path);
			int file_size = Integer.parseInt(String.valueOf(filenew
					.length() / 1024));
			
			Log.e("file_size", " " + file_size);
			
			if (file_size < 500) {
				
				Log.e("image size is less", "image size is less");
				bitmapCamera = BitmapFactory.decodeFile(path);
			} else {
				Log.e("image size is heavy", "image size is heavy");
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				bitmapCamera = BitmapFactory.decodeFile(path, options);
				Log.e("path", " " + path);
			}
		/*	matrix.postRotate(90);
			bitmapCamera = Bitmap.createBitmap(bitmapCamera, 0, 0, bitmapCamera.getWidth(),
					bitmapCamera.getHeight(), matrix, true);*/
	
		if(bitmapCamera!=null){
			
			if (cd.checkConnection()) {
				new DoneBackTask().execute("");
			} else {
				Toast.makeText(con, "internet_conn_failed", Toast.LENGTH_LONG).show();
			}
		
		}else{
			Custom_Dialog.dialogCode(2, null, "Capture image is not valid", con);
//			ValidationSingleButton("Capture image is not valid");
		}
		}
	}
	 
//	private String getRealPathFromURI(Uri contentURI) {
//	    String result;
//	    Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//	    if (cursor == null) { // Source is Dropbox or other similar local file path
//	        result = contentURI.getPath();
//	    } else { 
//	        cursor.moveToFirst(); 
//	        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
//	        result = cursor.getString(idx);
//	        cursor.close();
//	    }
//	    return result;
//	}
	
	private class DoneBackTask extends AsyncTask<String, Void, String> {
		String errorMessage="";

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ResolutionChatActivity.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				File myDir = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/ChatImages");
				if(myDir.exists()){
			
				}else{
					myDir.mkdirs();
				}
				count=0;
				getFileCount(myDir.toString());
				int n = count+1;
				String fname = n + ".PNG";
				File fileCreate = new File(myDir, fname);
				if (fileCreate.exists())
					fileCreate.delete();
				try {
					FileOutputStream out = new FileOutputStream(fileCreate);
					int file_size = Integer.parseInt(String.valueOf(fileCreate
							.length() / 1024));
					if (file_size < 500) {
						bitmapCamera.compress(Bitmap.CompressFormat.PNG, 100, out);
					}else if(file_size < 800){
						bitmapCamera.compress(Bitmap.CompressFormat.PNG, 80, out);
					}else if(file_size < 1000){
						bitmapCamera.compress(Bitmap.CompressFormat.PNG, 60, out);
					}else if(file_size < 2000){
						bitmapCamera.compress(Bitmap.CompressFormat.PNG, 30, out);
					}else {
						bitmapCamera.compress(Bitmap.CompressFormat.PNG, 20, out);
					}
				
					out.flush();
					out.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
				image = null;
				
				if (fileCreate.exists()) {
					
					//Bitmap bitmapOrg = decodeFile(fileCreate);
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					bitmapCamera.compress(Bitmap.CompressFormat.PNG, 100, bao);
				/*	int file_size = Integer.parseInt(String.valueOf(fileCreate
							.length() / 1024));
					if (file_size < 500) {
						
						Log.e("image size is less", "image size is less");
						bitmapCamera.compress(Bitmap.CompressFormat.PNG, 100, bao);
					}else if(file_size < 800){
						bitmapCamera.compress(Bitmap.CompressFormat.PNG, 80, bao);
					}else if(file_size < 1000){
						bitmapCamera.compress(Bitmap.CompressFormat.PNG, 60, bao);
					}else if(file_size < 2000){
						bitmapCamera.compress(Bitmap.CompressFormat.PNG, 30, bao);
					}else {
						bitmapCamera.compress(Bitmap.CompressFormat.PNG, 20, bao);
					}*/
		
					byte[] ba = bao.toByteArray();
					image = Base64.encodeToString(ba, Base64.DEFAULT);
					
				} 
		
					from_id=userid;
					Log.e("from_id", " "+from_id);
					Log.e("to_id", " "+to_id);
					Log.e("itemid", " "+itemid);
					Log.e("image", " "+image);
					JSONObject json = userFunction.SendResolutionChatImageFunction(from_id, to_id, caseid, image);
					try {
						if (json.getString(KEY_SUCCESS) != null) {
							String res = json.getString(KEY_SUCCESS);
							if (res.equals(KEY_SUCCESS_STATUS)) {
								
								errorMessage="true";
							} else {
								errorMessage = "false";
							}
						} else {
							errorMessage = "error in posting";
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
					errorMessage="true";
			}
			return errorMessage;
		}
		
		
		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			if(result.equals("true")){
				
//				bitmapCamera.recycle();
				bitmapCamera=null;
				mMessage.clear();
				if (cd.checkConnection()) {
					new ChatDisplayBackTask().execute("");
				} else {
					Toast.makeText(con, "internet_conn_failed", Toast.LENGTH_LONG).show();
				}
				}else{
					Custom_Dialog.dialogCode(2, null, "Image fail to upload please try again!", con);
//				ValidationSingleButton("Image upload fail");
				}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


	private void getFileCount(String dirPath) 
	{
	    File f = new File(dirPath);
	    File[] files  = f.listFiles();

	    if(files != null)
	    for(int i=0; i < files.length; i++)
	    {
	        File file = files[i];
	        if(file.isDirectory())
	        {
	            count ++;
	            getFileCount(file.getAbsolutePath()); 
	        }
	        else
	        {
	            count ++;
	        }
	     }
	}
	public class Adapter extends BaseAdapter {
		private Context context;
		private ArrayList<String> val;

		public Adapter(Context context, ArrayList<String> val) {
			this.context = context;
			this.val = val;
		
		}

		@Override
		public int getCount() {
			
			return val.size();
		}

		@Override
		public Object getItem(int position) {
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.itemdetails_listview_listitem, parent, false);
			TextView tvSpinner = (TextView) rowView
					.findViewById(R.id.tvSpinner);
			tvSpinner.setText(val.get(position));
			System.out.println(val.get(position));
			return rowView;
		}
	}
	
	
	@SuppressWarnings("static-access")
	public   void Two_Button_dialogCode(String msg) {
	   	
	   	// custom dialog
	   	Log.e("testdialogCode", "testdialogCode");
	   	final Dialog  dialog = new Dialog(ResolutionChatActivity.this);
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
						new CloseCaseBackTask().execute("");
					} else {
						Toast.makeText(con, "internet_conn_failed", Toast.LENGTH_LONG)
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
	
	
	private void SendCode() {

	strmsg=evMessage.getText().toString().trim();
	
	if(strmsg.length()==0){
		Toast.makeText(ResolutionChatActivity.this, "Please enter message.",Toast.LENGTH_SHORT).show();
//		ValidationSingleButton("Please enter message.");
	}else{
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(evMessage.getWindowToken(), 0);
		if (cd.checkConnection()) {
			evMessage.setText("");
			new SendChatBackTask().execute("");
		} else {
			Toast.makeText(con, "internet_conn_failed", Toast.LENGTH_LONG)
			.show();
		}
	  }
	}
	
	private class CloseCaseBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(con);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
		
				Log.e("userid", " "+userid);
				Log.e("caseid", " "+caseid);
//				userid="50";
				JSONObject json = userFunction.ClosecaseRequestFunction(userid, caseid);
				try {
					temp_time=json.optString("time");
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							message=json.optString("message");	
							errorMessage="true";
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

		@SuppressLint("InlinedApi") @Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			//mMessage.clear();
		if(result.equals("true")){
			Intent i = new Intent(ResolutionChatActivity.this, TODOS_Activity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			Custom_Dialog.dialogCode(3, i,message,
					ResolutionChatActivity.this);
		}else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
			Custom_Dialog.dialogCode(2, null,message, ResolutionChatActivity.this);
	}else{
			cancelContdowwnTimer();
			Custom_Dialog.dialogCode(1, null,message,
					ResolutionChatActivity.this);
		}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	private class SendChatBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(con);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				from_id=userid;
				
			Log.e("from_id", " "+from_id);
			Log.e("to_id", " "+to_id);
			Log.e("caseid", " "+caseid);
			Log.e("strmsg", " "+strmsg);
			Log.e("temp_time", " "+temp_time);
				JSONObject json = userFunction.SendResolutionChatFunction
				(from_id, to_id, caseid, strmsg,temp_time);
				try {
					temp_time=json.optString("time");
					if (json!=null&&json.optString(KEY_SUCCESS) != null) {
						String res = json.optString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
									String message_id,msg,msg_type,msg_image,user_type,user_image,user_name,date;
									
									JSONObject cItem =json.getJSONObject("Item");
									itemid=cItem.optString("itemid");
									name=cItem.optString("name");
									image=UserFunctions.localImageUrl;
									image=image+cItem.optString("image");
									Log.e("image", " "+image);
									JSONObject cUser =json.getJSONObject("User");
									
									fromuser_image=cUser.optString("fromuser_image");
									fromuser_name=cUser.optString("fromuser_name");
									
									JSONArray Message = json.getJSONArray("Message");
									for (int i = 0; i < Message.length(); i++) {
										
										JSONObject c = Message.getJSONObject(i);
										message_id=c.optString("message_id");
										msg=c.optString("msg");
										msg_type=c.optString("msg_type");
										msg_image=c.optString("msg_image");
										user_type=c.optString("user_type");
										user_image=c.optString("touser_image");
										user_name=c.optString("user_name");
										date=c.optString("date");
										mMessage.add(new ConversionModal(message_id, msg, msg_type, msg_image, user_type, user_image, user_name, date));
									}
							errorMessage="true";
						} else {
							message= json.optString("message");
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
				
			
			//mMessage.clear();
			if (image != null) {
				try {
					iLoader.DisplayImage(image, ivItemBack);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				ivProduct.setImageResource(R.drawable.ic_launcher);
			}
			
			if(image.length()>0){
				iLoader_Rounded.displayImage(image, ivProduct, options, loadImageListener);
			}else{
				iLoader_Rounded.displayImage("", ivProduct, options, loadImageListener);
			}	
			
			
			adapter = new ConversionCustomAdaper(con,R.layout.chat_item, mMessage);
			lvt.setAdapter(adapter);
			
			count = lvt.getCount();
			lvt.setSelectionFromTop(count, 0);	
			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, ResolutionChatActivity.this);
		}else{
				Custom_Dialog.dialogCode(2, null,message,
						ResolutionChatActivity.this);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	private class ConversionCustomAdaper extends ArrayAdapter<ConversionModal> {
		 ViewHolder holder;
		private ArrayList<ConversionModal> listSubCategories;
		ImageLoader iLoader_item = ImageLoader.getInstance();
		DisplayImageOptions options;
		public ConversionCustomAdaper(Context context, int textViewResourceId,
				ArrayList<ConversionModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<ConversionModal>();
			this.listSubCategories = listSubCategories;
		}

	public	class ViewHolder {
			ImageView ivComment,ivUserImage;
			TextView  tvComment,tvTime,tvUserName;
			RelativeLayout rlImage,rlChat;
		}

		@Override
		public View getView(final int position,  View convertView,
				ViewGroup parent) {
			String user_type;
			holder=null;
			ConversionModal myCate = listSubCategories.get(position);
			 user_type=myCate.getUser_type();
				iLoader_item.init(ImageLoaderConfiguration.createDefault(ResolutionChatActivity.this));
				options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.transperant)
				.showImageForEmptyUri(R.drawable.transperant)
				.showImageOnFail(R.drawable.transperant)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
				

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if(user_type.equals("U")){
					convertView = vi.inflate(R.layout.chat_item, parent,false);	
				}else if(user_type.equals("M")){
					convertView = vi.inflate(R.layout.chat_item1, parent,false);
				}
				holder = new ViewHolder();
				holder.ivComment = (ImageView) convertView.findViewById(R.id.ivComment);
				
				holder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
				holder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
				holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
				holder.ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
				holder.rlImage = (RelativeLayout) convertView.findViewById(R.id.rlImage);
				holder.rlChat = (RelativeLayout) convertView.findViewById(R.id.rlChat);
				
			
				
				
				if(user_type.equals("U")){
					holder.tvUserName.setText(fromuser_name);
				
					
					String to_image= myCate.getUser_image();
					Log.e("to_image", " "+to_image);
						options = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.chat_item_light_blue_bg_user)
						.showImageForEmptyUri(R.drawable.chat_item_light_blue_bg_user)
						.showImageOnFail(R.drawable.chat_item_light_blue_bg_user)
						.resetViewBeforeLoading(true)
						.cacheInMemory(true)
						.cacheOnDisk(true)
						.bitmapConfig(Bitmap.Config.RGB_565)
						.considerExifParams(true)
						.build();
						
						if(to_image.length()>0){
							iLoader_Rounded.displayImage(to_image, holder.ivUserImage, options, loadImageListener);
						}else{
							iLoader_Rounded.displayImage("",  holder.ivUserImage, options, loadImageListener);
						
						
					}
					
				}else if(user_type.equals("M")){
					
					holder.tvUserName.setText(myCate.getUser_name());
					
					options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.chat_item_blue_bg_user)
					.showImageForEmptyUri(R.drawable.chat_item_blue_bg_user)
					.showImageOnFail(R.drawable.chat_item_blue_bg_user)
					.resetViewBeforeLoading(true)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.build();
					Log.e("fromuser_image", " "+fromuser_image);
					if(fromuser_image.length()>0){
						iLoader_Rounded.displayImage(fromuser_image, holder.ivUserImage, options, loadImageListener);
					}else{
						iLoader_Rounded.displayImage("",  holder.ivUserImage, options, loadImageListener);
					}	
				}
					
			
			String message_id,msg,msg_type,msg_image,date;
			
			 message_id=myCate.getMessage_id();
			 msg=myCate.getMsg();
			 msg_type=myCate.getMsg_type();
			 msg_image=UserFunctions.localImageUrl;
			 msg_image=msg_image+myCate.getMsg_image();
			 date=myCate.getDate();
			 
			 if(msg_type.equals("T")){
				 
			 holder.rlChat.setVisibility(View.VISIBLE);
			 holder.rlImage.setVisibility(View.GONE);
			 holder.tvComment.setText(" "+msg);
			 holder.tvTime.setText(date);
			 holder.tvComment.setTag(message_id);
		
			 /*	 
			 if (image != null) {
					try {
						imageLoader.DisplayImage(image, holder.ivComment);
					} catch (Exception e) {
					}
				} else {
					holder.ivComment.setImageResource(R.drawable.ic_launcher);
				}
			 */

				// Image display using lazy loading 

				iLoader_item.displayImage(image, holder.ivComment, options, new SimpleImageLoadingListener() {
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
				
				 
			 }else{
				 holder.rlImage.setVisibility(View.VISIBLE);
				 holder.rlChat.setVisibility(View.GONE);
				 if (msg_image != null) {
					/*	try {
							imageLoader.DisplayImage(msg_image, holder.ivComment);
						} catch (Exception e) {
						}
						*/
						
						// Image display using lazy loading 

						iLoader_item.displayImage(msg_image, holder.ivComment, options, new SimpleImageLoadingListener() {
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
						
					} 
				 
				 
			 }

			 return convertView;
		}
	}
	
	public  void CallBacktastMethod(){
		
		new ChatDisplayBackTask().execute("");
	
}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
		
			if (intent.getAction().equalsIgnoreCase(
					"xyz") ) {
				mMessage.clear();
				
				if (cd.checkConnection()) {
					new ChatDisplayBackTask().execute("");
				} else {
					Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
				}

			} 

		}

	}
	
/*
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		View view = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (view instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
			}
		}
		return ret;
	}*/

}
