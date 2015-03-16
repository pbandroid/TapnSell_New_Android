package com.convertingoffers.tapnsell.sell;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;

public class PriviewListing extends BaseActivity implements OnClickListener {
	ImageView iv1, iv2, iv3,iv4, ivDefault,ivImageEdit,ivNameEdit;
	String default_image="0",item_name="",item_price,Copy_Relist="";
	RelativeLayout rlPost;
	TextView tvTitle,tvPrice;
	RelativeLayout rlVideo,rlPrice,rlTitle;
	ArrayList<String> ImagePath = new ArrayList<String>(3);
	boolean boolimg1=false,boolimg2=false,boolimg3=false,boolimg4=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privew_listing);
		editor.remove("priviewVid");
		editor.remove("priview_image");
		editor.remove("image_path");
		editor.remove("priviewVid");
		editor.commit();
		
		ivImageEdit.setVisibility(View.INVISIBLE);
		ivMenu.setVisibility(View.GONE);
		ivBack.setVisibility(View.GONE);
		tvHeader.setText("Preview");
		Copy_Relist=pref.getString("Copy_Relist", "");
		item_price=pref.getString("asking_price", "");
		item_name=pref.getString("item_name", "");
		tvPrice.setText(""+item_price);
		String VideoFullPath =android.os.Environment.getExternalStorageDirectory() + "/TapNSell/CameraVideo/TapnSellVideo.mp4";
		rlVideo.setTag(VideoFullPath);
		
		File f = new File(VideoFullPath);
		if(!f.exists()){
			rlVideo.setVisibility(View.INVISIBLE);
		}
		
		iv1.setOnClickListener(this);
		iv2.setOnClickListener(this);
		iv3.setOnClickListener(this);
		iv4.setOnClickListener(this);
		rlPrice.setOnClickListener(this);
		ivImageEdit.setOnClickListener(this);
		ivNameEdit.setOnClickListener(this);
		rlPost.setOnClickListener(this);
		tvTitle.setText(item_name);
	
		DisplayImageFromFolder();
		
		rlVideo.setOnClickListener(this);
	}

	@SuppressLint({ "InlinedApi", "NewApi" }) private void DisplayImageFromFolder() {
	
		default_image=pref.getString("defaultimage", "");
		File myDir = new File(
				android.os.Environment.getExternalStorageDirectory(),
				"/TapNSell/CameraImage");
		ImagePath.add(0, myDir + "/1.PNG");
		ImagePath.add(1, myDir + "/2.PNG");
		ImagePath.add(2, myDir + "/3.PNG");
		ImagePath.add(3, myDir + "/4.PNG");
		
		String path="";
		for (int i = 0; i < ImagePath.size(); i++) {
			switch (i) {
			case 0:
				
				
				path=ImagePath.get(i).toString();
				
				if (path.length()!=0 && default_image.equals("1")) {
					boolimg1=true;
					ivDefault.setTag(ImagePath.get(i));
					iv1.setTag(ImagePath.get(i));
					
					
					LoadImageTask task=new LoadImageTask(ImagePath.get(i), iv1);
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
					 
					 
					 LoadImageTask task1=new LoadImageTask(ImagePath.get(i), ivDefault);
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task1.execute();
					 
					
					
				}else if(path.length()!=0){
					boolimg1=true;
					LoadImageTask task=new LoadImageTask(ImagePath.get(i), iv1);
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
					iv1.setTag(ImagePath.get(i));
				}else{
					boolimg1=false;
					iv1.setClickable(false);
					iv1.setEnabled(false);
					iv1.setFocusable(false);
					iv1.setFocusableInTouchMode(false);
				}
				break;
			case 1:
				path=ImagePath.get(i).toString();
				if (path.length()!=0 && default_image.equals("2")) {
					boolimg2=true;
					ivDefault.setTag(ImagePath.get(i));
					iv2.setTag(ImagePath.get(i));
					
					LoadImageTask task=new LoadImageTask(ImagePath.get(i), iv2);
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
					 
					 
					 LoadImageTask task1=new LoadImageTask(ImagePath.get(i), ivDefault);
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task1.execute();
					 
					 
				}else if(path.length()!=0){
					boolimg2=true;
					iv2.setTag(ImagePath.get(i));
					
					LoadImageTask task=new LoadImageTask(ImagePath.get(i), iv2);
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
				}else{
					boolimg2=false;
					iv2.setClickable(false);
					iv2.setEnabled(false);
					iv2.setFocusable(false);
					iv2.setFocusableInTouchMode(false);
				}
				
				
				break;
			case 2:
				path=ImagePath.get(i).toString();
			
				if (path.length()!=0 && default_image.equals("3")) {
					boolimg3=true;
					ivDefault.setTag(ImagePath.get(i));
					iv3.setTag(ImagePath.get(i));
					
					
					LoadImageTask task=new LoadImageTask(ImagePath.get(i), iv3);
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
					 
					 
					 LoadImageTask task1=new LoadImageTask(ImagePath.get(i), ivDefault);
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task1.execute();
					 
					 
					 
				}else if(path.length()!=0){
					boolimg3=true;
					LoadImageTask task=new LoadImageTask(ImagePath.get(i), iv3);
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
					 iv3.setTag(ImagePath.get(i));
					 
				}else{
					boolimg3=false;
					iv3.setClickable(false);
					iv3.setEnabled(false);
					iv3.setFocusable(false);
					iv3.setFocusableInTouchMode(false);
				}
				break;
			case 3:
				path=ImagePath.get(i).toString();
//				
//			
				if (path.length()!=0 && default_image.equals("4")) {
					boolimg4=true;
//				ivDefault.setImageBitmap(bmp);
				ivDefault.setTag(ImagePath.get(i));
//				iv4.setImageBitmap(bmp);
				iv4.setTag(ImagePath.get(i));
				
				
				LoadImageTask task=new LoadImageTask(ImagePath.get(i), iv4);
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			     else
			         task.execute();
				 
				 
				 LoadImageTask task1=new LoadImageTask(ImagePath.get(i), ivDefault);
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			         task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			     else
			         task1.execute();
				 
				 
				}else if(path.length()!=0){
					boolimg4=true;
//					iv4.setImageBitmap(bmp);
					LoadImageTask task=new LoadImageTask(ImagePath.get(i), iv4);
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
					 
					iv4.setTag(ImagePath.get(i));
				}else{
					boolimg4=false;
					iv4.setClickable(false);
					iv4.setEnabled(false);
					iv4.setFocusable(false);
					iv4.setFocusableInTouchMode(false);
				}
				break;
			default:
				break;
			}
		}
	}

/*	private Bitmap decodeFile(String path) {
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(path), null, o);
			final int REQUIRED_SIZE = 300;
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(path), null,
					o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}*/
	
	@Override
	public void onContentChanged() {
		super.onContentChanged();

		iv1 = (ImageView) findViewById(R.id.iv1);
		iv2 = (ImageView) findViewById(R.id.iv2);
		iv3 = (ImageView) findViewById(R.id.iv3);
		iv4 = (ImageView) findViewById(R.id.iv4);
		
		ivDefault = (ImageView) findViewById(R.id.ivDefault);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvPrice= (TextView) findViewById(R.id.tvPrice);
		rlPost=(RelativeLayout)findViewById(R.id.rlPost);
		rlVideo=(RelativeLayout)findViewById(R.id.rlVideo);
		rlTitle=(RelativeLayout)findViewById(R.id.rlTitle);
		rlPrice=(RelativeLayout)findViewById(R.id.rlPrice);
		ivImageEdit=(ImageView)findViewById(R.id.ivImageEdit);
		ivNameEdit=(ImageView)findViewById(R.id.ivNameEdit);
	}


	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
		String strImage1="";
		switch (v.getId()) {
		case R.id.iv1:
			
			if(boolimg1){
			
			strImage1="";
			strImage1=iv1.getTag().toString();
			if(strImage1.length()!=0){
				LoadImageTask task=new LoadImageTask(strImage1, ivDefault);
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			     else
			         task.execute();
				}
			//iv1.setTag(strdefault);
			ivDefault.setTag(strImage1);
			}else{
			Toast.makeText(PriviewListing.this, "There is no image", Toast.LENGTH_LONG).show();	
			}
			break;
	
		case R.id.iv2:
			if(boolimg2){
				strImage1="";
			
			strImage1=iv2.getTag().toString();
			
			if(strImage1.length()!=0){
				LoadImageTask task=new LoadImageTask(strImage1, ivDefault);
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			     else
			         task.execute();
				}
			
			ivDefault.setTag(strImage1);
		}else{
			Toast.makeText(PriviewListing.this, "There is no image", Toast.LENGTH_LONG).show();	
			}
			break;
			
		case R.id.iv3:
			if(boolimg3){
				strImage1="";
			
			strImage1=iv3.getTag().toString();
			
			if(strImage1.length()!=0){
				LoadImageTask task=new LoadImageTask(strImage1, ivDefault);
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			     else
			         task.execute();
				}
			ivDefault.setTag(strImage1);
			}else{
				Toast.makeText(PriviewListing.this, "There is no image", Toast.LENGTH_LONG).show();	
				}
			break;
		case R.id.iv4:
			if(boolimg4){
				strImage1="";
			strImage1=iv4.getTag().toString();
			
			if(strImage1.length()!=0){
				LoadImageTask task=new LoadImageTask(strImage1, ivDefault);
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			     else
			         task.execute();
				}
			ivDefault.setTag(strImage1);
			}else{
				Toast.makeText(PriviewListing.this, "There is no image", Toast.LENGTH_LONG).show();	
				}
			break;
			
		case R.id.ivImageEdit:

			Intent i = new Intent(PriviewListing.this, TakePictureNew.class);
			editor.putString("priview_image", "true");
			editor.putString("image_path", ivDefault.getTag().toString());
			editor.commit();
			startActivity(i);
			finish();
			
			break;
		case R.id.ivNameEdit:

			Intent iname = new Intent(PriviewListing.this, ItemDetails1.class);
			iname.putExtra("Priview", "true");
			startActivity(iname);
			finish();
			
			break;
		case R.id.rlVideo:
			String path = rlVideo.getTag().toString();
			File f = new File(path);
			if(f.exists()){
				
				Intent iVideo = new Intent(PriviewListing.this, VideoPreview.class);
				editor.putString("priviewVid", "true");
				editor.commit();
				startActivity(iVideo);
				finish();	
			}else{
				Toast.makeText(PriviewListing.this, "Video is not available", Toast.LENGTH_LONG).show();
			}
			
			
			break;
			
		case R.id.rlPrice:

			Intent iPrice = new Intent(PriviewListing.this, ItemDetails2.class);
			iPrice.putExtra("Priview", "true");
			startActivity(iPrice);
			finish();
			
			break;	
		
			
		case R.id.rlPost:

			Intent iPost = new Intent(PriviewListing.this, PostItemActivity.class);
			startActivity(iPost);
			finish();
			break;	
			
		default:
			break;
		}
	}
	class LoadImageTask extends AsyncTask<String, Void, String> {

		Bitmap pickimg=null;
		String errorMessage = "",picturePath="";
		ImageView ivImage;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		

		public LoadImageTask(String path, ImageView iv1) {
			picturePath=path;
			ivImage=iv1;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			try {

				File f = new File(picturePath);
				int file_size = Integer.parseInt(String.valueOf(f.length() / 1024));
				Log.e("file_size", " " + file_size);
				ExifInterface exif1 = new ExifInterface(f.getAbsolutePath());
				int orientation = exif1.getAttributeInt(
						ExifInterface.TAG_ORIENTATION, 1);
				Log.d("EXIF", "Exif: " + orientation);
				Matrix matrix = new Matrix();
				if (orientation == 6) {
					matrix.postRotate(90);
				} else if (orientation == 3) {
					matrix.postRotate(180);
				} else if (orientation == 8) {
					matrix.postRotate(270);
				}

				try {

					BitmapFactory.Options options = new BitmapFactory.Options();
					if (file_size < 1000) {
						
						Log.e("image size is less", "image size is less");
						pickimg = BitmapFactory.decodeFile(f.toString());
					
					}if (file_size < 2000) {
						
						options.inSampleSize = 2;
						pickimg = BitmapFactory.decodeFile(f.toString(), options);
					
					} else {
						Log.e("image size is heavy", "image size is heavy");
						
						options.inSampleSize = 4;
						pickimg = BitmapFactory.decodeFile(f.toString(), options);
					}
					
					pickimg = Bitmap.createBitmap(pickimg, 0, 0,pickimg.getWidth(), pickimg.getHeight(), matrix,true); // rotating bitmap
					
					/*BitmapFactory.Options option = new BitmapFactory.Options();
					option.inSampleSize = 2;
					pickimg = BitmapFactory.decodeFile(f.getAbsolutePath(),
							option);*/
					//pickimg.compress(CompressFormat.PNG, 20, baos);
					//imagebyte = baos.toByteArray();

				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				} catch (Exception e) {

					Log.e("Bitmap Load Error", "Failed to load");
				}

			} catch (Throwable e) {
				e.printStackTrace();
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (pickimg != null) {
				ivImage.setImageBitmap(pickimg);
			}else{
				Log.e("Bitmap null", "Bitmap null");
			}

		}
	}

		     
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}