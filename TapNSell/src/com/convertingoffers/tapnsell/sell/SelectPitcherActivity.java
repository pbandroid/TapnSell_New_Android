package com.convertingoffers.tapnsell.sell;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;


public class SelectPitcherActivity extends BaseActivity implements OnClickListener {
	ImageView  iv1,iv2, iv3, iv4;
	ArrayList<String> ImagePath = new ArrayList<String>(3);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_pitcher);
		
		iv1.setOnClickListener(this);
		iv2.setOnClickListener(this);
		iv3.setOnClickListener(this);
		iv4.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		tvHeader.setText("Select Cover");
		myimagepath();
	
	}

	@Override
	protected void onResume() {
			super.onResume();
			
		for (int i = 0; i < ImagePath.size(); i++) {

			String path =ImagePath.get(i);
			File f = new File(path);
			if(!f.exists() && i==0){
				iv1.setEnabled(false);
				iv1.setClickable(false);
			}
			if(!f.exists() && i==1){
				iv2.setEnabled(false);
				iv2.setClickable(false);
			}
			if(!f.exists() && i==2){
				iv3.setEnabled(false);
				iv3.setClickable(false);
			}
			if(!f.exists() && i==3){
				iv4.setEnabled(false);
				iv4.setClickable(false);
			}
		}
	}
	
	@Override
	public void onContentChanged() {
			super.onContentChanged();
			
			iv1 = (ImageView) findViewById(R.id.iv1);
			iv2 = (ImageView) findViewById(R.id.iv2);
			iv3 = (ImageView) findViewById(R.id.iv3);
			iv4 = (ImageView) findViewById(R.id.iv4);
			
	}
	private void myimagepath() {
		int widthOfscreen =0;
		int heightOfScreen = 0;
		
		DisplayMetrics dm = new DisplayMetrics();
		try {
		SelectPitcherActivity.this.getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		widthOfscreen  = dm.widthPixels/2;
		heightOfScreen  = dm.heightPixels/2;
		
		iv1.getLayoutParams().height = heightOfScreen;
		iv2.getLayoutParams().height = heightOfScreen;
		iv3.getLayoutParams().height = heightOfScreen;
		iv4.getLayoutParams().height = heightOfScreen;
		
		iv1.getLayoutParams().width = widthOfscreen;
		iv2.getLayoutParams().width = widthOfscreen;
		iv3.getLayoutParams().width = widthOfscreen;
		iv4.getLayoutParams().width = widthOfscreen;
		
		File myDir = new File(
				android.os.Environment.getExternalStorageDirectory(),
				"/TapNSell/CameraImage");
		ImagePath.add(0, myDir + "/1.PNG");
		ImagePath.add(1, myDir + "/2.PNG");
		ImagePath.add(2, myDir + "/3.PNG");
		ImagePath.add(3, myDir + "/4.PNG");
		for (int i = 0; i < ImagePath.size(); i++) {
			switch (i) {
			case 0:
				new LoadImageTask(ImagePath.get(i).toString(),iv1).execute("");
				break;
			case 1:
	
				new LoadImageTask(ImagePath.get(i).toString(),iv2).execute("");
				break;
			case 2:
		
				new LoadImageTask(ImagePath.get(i).toString(),iv3).execute("");
				break;
			case 3:

				
				new LoadImageTask(ImagePath.get(i).toString(),iv4).execute("");
				break;
				
			default:
				break;
			}
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
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.iv1:
			
			
			editor.putString("defaultimage", "1");
			editor.commit();
			
			getintent();
			break;
		case R.id.iv2:
			editor.putString("defaultimage", "2");
			editor.commit();
			getintent();
			break;
		case R.id.iv3:
			editor.putString("defaultimage", "3");
			editor.commit();
			getintent();
			break;
		case R.id.iv4:
			editor.putString("defaultimage", "4");
			editor.commit();
			getintent();
			break;
		case R.id.ivBack:
			Intent i = new Intent(SelectPitcherActivity.this, TakePictureNew
					.class);
			startActivity(i);
			finish();
			break;
		default:
			break;
		}
	}
	
	private void getintent() {
		
		Intent intent = new Intent(SelectPitcherActivity.this, AddQuickVideo.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(SelectPitcherActivity.this, TakePictureNew.class);
		startActivity(i);
		finish();
	}
}
