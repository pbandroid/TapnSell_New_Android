package com.convertingoffers.tapnsell.sell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.convertingoffers.tapnsell.HomeActivity;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TakePictureNew extends BaseActivity implements OnClickListener{
	String ComeFromPriviw="";
	ImageView ivCamera,ivGallery,ivCnt;
	String picturePath = "",image_path="";
	Context context;
	Animation RightSwipe;
	File myDir;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private Uri fileUri;
	public int count = 0, RESULT_LOAD_CAMERA_IMAGE = 2,RESULT_LOAD_IMAGE = 1,GALLERY_KITKAT_INTENT_CALLED=20;
	Bitmap bmp=null;
	Uri ImageFileUri;
	ImageView imgprivew,takepic_text;
	 Dialog dialog;
	 public  final int REQUEST_CODE_CROP_IMAGE   = 0x3,  REQUEST_CODE_CROP_IMAGE1   = 0x4,  REQUEST_CODE_CROP_IMAGE2   = 0x5;
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.take_picture_new);
//	
	ComeFromPriviw = pref.getString("priview_image", "");
	image_path = pref.getString("image_path", "");
	ivCamera.setOnClickListener(this);
	ivGallery.setOnClickListener(this);
	ivBack.setOnClickListener(this);
	ivCnt.setOnClickListener(this);
	}
	
	@Override
	public void onContentChanged() {
	super.onContentChanged();
	context=this;
	 myDir = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraImage");
	ivCamera=(ImageView)findViewById(R.id.ivCamera);
	ivGallery =(ImageView)findViewById(R.id.ivGallery);
	imgprivew=(ImageView)findViewById(R.id.imgprivew);
	takepic_text=(ImageView)findViewById(R.id.takepic_text);
	ivCnt=(ImageView)findViewById(R.id.ivCnt);
	}

	@Override
	protected void onResume() {
		super.onResume();
		count = 0;
		getFileCount(myDir.toString());
		Log.e("cnt", " " + count);
		Changetext(count);
	}
	@SuppressLint("InlinedApi") @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivCamera:
			buttonAnimation(ivCamera);
			count=0;
			getFileCount(myDir.toString());
			if(ComeFromPriviw.equals("true")){
				
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				File file = new File(image_path);
				fileUri=Uri.fromFile(file);
				picturePath = fileUri.getPath();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(intent, RESULT_LOAD_CAMERA_IMAGE);	
				
			}else{
			
			if (count >= 4) {
				Intent i = new Intent(context, SelectPitcherActivity.class);
				startActivity(i);
				finish();
			} else {
				
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				picturePath = fileUri.getPath();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(intent, RESULT_LOAD_CAMERA_IMAGE);	
			}
			}
			break;
			
		case R.id.ivGallery:
			buttonAnimation(ivGallery);
		 	mygallerybtn();
			break;
		case R.id.ivBack:
			Intent iPriview;
			if(ComeFromPriviw.length()==0){
				Intent iSellIt = new Intent(context, HomeActivity.class);
				iSellIt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(iSellIt);
				finish();
			}else{
				 iPriview = new Intent(context, PriviewListing.class);
				startActivity(iPriview);
				finish();
			}
			
			break;
		case R.id.ivCnt:
			count=0;
			getFileCount(myDir.toString());
			if(ComeFromPriviw.equals("true")){
				Custom_Dialog.dialogCode(2, null, "You must take atleast one photo.", context);
//				ValidationDialogCancleEvent("Please select at least one image");
			}else if(count>0){
				Intent i = new Intent(context, SelectPitcherActivity.class);
				startActivity(i);
				break;
			}
		default:
			break;
		}
		
	}
	
	@SuppressLint("InlinedApi") private void mygallerybtn() {
		
		File myDir = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraImage");
		count=0;
		getFileCount(myDir.toString()); 
	String	FILE_TYPE="image/*";
		if(ComeFromPriviw.equals("true")){
			if (count >= 5) {
				Intent i = new Intent(context, SelectPitcherActivity.class);
				startActivity(i);
				finish();
			} else {
				Intent i = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					i.setType(FILE_TYPE);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		}else{
			if (count >= 4) {
				Intent i = new Intent(context, SelectPitcherActivity.class);
				startActivity(i);
				finish();
			} else {
			if (Build.VERSION.SDK_INT <19){
					Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					i.setType(FILE_TYPE);
					startActivityForResult(i, RESULT_LOAD_IMAGE);
			}/* else {
				Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				i.setType(FILE_TYPE);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(i, GALLERY_KITKAT_INTENT_CALLED);
			}*/
			else {
		         Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		         intent.addCategory(Intent.CATEGORY_OPENABLE);
		         intent.setType("image/*");
		         ((Activity) context).startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
		     }
			}
	}
	}

	
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile());
	}

	private  File getOutputMediaFile() {

		File myDir = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraImage");
		count=0;
		getFileCount(myDir.toString());
		int n = count + 1;
		String fname = n + ".PNG";
		
		File mediaFile = new File(myDir, fname);
		Log.e("CaptureImage", mediaFile.toString() + " " +mediaFile.exists());
	return mediaFile;
	}

	private void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(context, R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
	

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation changes
        ImageFileUri = getFileURI();
        Log.e("File URI onSaveInstance", "-->"+ImageFileUri);
        outState.putParcelable("file_uri", ImageFileUri);
    }
 
	
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        ImageFileUri = savedInstanceState.getParcelable("file_uri");
        Log.e("File URI onRestoreInstance", "==>"+ImageFileUri);
    }
    
    
	@SuppressLint("NewApi") 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		count=0;
		getFileCount(myDir.toString());
		Log.e("cnt"," "+count);
		Changetext(count);
			 if (resultCode == Activity.RESULT_OK) {
		  	        if (requestCode == RESULT_LOAD_CAMERA_IMAGE) {
		  	        	if(ComeFromPriviw.equals("true")){
		  	        		editor.putString("priview_image", "false");
		  	        		editor.commit();
		  	        		if(count>=4){
								Intent intent =  new Intent(context, SelectPitcherActivity.class);
								startActivity(intent);
								finish();
							}
		  	        	}else{
		  	        		if(ImageFileUri!=null && !ImageFileUri.equals("")){	  	        		
				  	        	picturePath = ImageFileUri.getPath();
				  	        	if(count>=4){
									Intent intent =  new Intent(context, SelectPitcherActivity.class);
									startActivity(intent);
									finish();
								}
			  	        	}else{
			  	        		Toast.makeText(context,"Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
			  	        	}
		  	        	}
		  	        }else if (requestCode == RESULT_LOAD_IMAGE) {
		  	            Uri selectedImageUri = data.getData();
		  	          picturePath = getPath(selectedImageUri);
		  	        dialogCode(picturePath);
		  	      
		  	            
		  	        } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
		  	        	Uri selectedImageUri = data.getData();	
		  	        		final int takeFlags = data.getFlags()
			  	                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
			  	                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			  	            getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
			  	          picturePath = getPathKitKat(selectedImageUri);
			  	        
			  	      dialogCode(picturePath);
			  	               
		  	        }
  	        		
		  	    }else if (resultCode == Activity.RESULT_CANCELED) {
		  	        Toast.makeText(context,"User cancelled image capture", Toast.LENGTH_SHORT).show();
		  	    }	 
			 Log.e("picturePath", " "+picturePath);
			
		}
	
	public void Changetext(int count2) {
		if(!ComeFromPriviw.equals("true")){
			switch (count2) {
			case 0:
				takepic_text.setImageDrawable(getResources().getDrawable(R.drawable.take_pic_text));
				break;
			case 1:
				takepic_text.setImageDrawable(getResources().getDrawable(R.drawable.take_pic_text1));
				break;
			case 2:
				takepic_text.setImageDrawable(getResources().getDrawable(R.drawable.take_pic_text2));
				break;
			case 3:
				takepic_text.setImageDrawable(getResources().getDrawable(R.drawable.take_pic_text3));
				break;
			default:
				break;
			}
			
		}
		
	}
	@SuppressLint("NewApi") public String getPathKitKat( final Uri uri) {

	     final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	     // DocumentProvider
	     if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	         // ExternalStorageProvider
	         if (isExternalStorageDocument(uri)) {
	             final String docId = DocumentsContract.getDocumentId(uri);
	             final String[] split = docId.split(":");
	             final String type = split[0];

	             if ("primary".equalsIgnoreCase(type)) {
	                 return Environment.getExternalStorageDirectory() + "/" + split[1];
	             }

	             // TODO handle non-primary volumes
	         }
	         // DownloadsProvider
	         else if (isDownloadsDocument(uri)) {

	             final String id = DocumentsContract.getDocumentId(uri);
	             final Uri contentUri = ContentUris.withAppendedId(
	                     Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	             return getDataColumn(context, contentUri, null, null);
	         }
	         // MediaProvider
	         else if (isMediaDocument(uri)) {
	             final String docId = DocumentsContract.getDocumentId(uri);
	             final String[] split = docId.split(":");
	             final String type = split[0];

	             Uri contentUri = null;
	             if ("image".equals(type)) {
	                 contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	             } else if ("video".equals(type)) {
	                 contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	             } else if ("audio".equals(type)) {
	                 contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	             }

	             final String selection = "_id=?";
	             final String[] selectionArgs = new String[] {
	                     split[1]
	             };

	             return getDataColumn(context, contentUri, selection, selectionArgs);
	         }
	     }
	     // MediaStore (and general)
	     else if ("content".equalsIgnoreCase(uri.getScheme())) {

	         // Return the remote address
	         if (isGooglePhotosUri(uri))
	             return uri.getLastPathSegment();

	         return getDataColumn(context, uri, null, null);
	     }
	     // File
	     else if ("file".equalsIgnoreCase(uri.getScheme())) {
	         return uri.getPath();
	     }

	     return null;
	 }
	@SuppressWarnings("static-access")
	public void dialogCode(final String picturePath2) {

		  dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.pitcher_priview);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		Button btnUse = (Button) dialog.findViewById(R.id.btnUse);
		Button btnRetake = (Button) dialog.findViewById(R.id.btnRetake);
		imgprivew= (ImageView) dialog.findViewById(R.id.imgprivew);
		
		new DisplayBackTask(picturePath2).execute("");
		
		btnUse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				count=0;
				getFileCount(myDir.toString());
				if(ComeFromPriviw.equals("true")){
					File sourceFile = new File(picturePath2);
					File mediaFile = new File(image_path);
					new SaveImageBackTask(sourceFile, mediaFile).execute("");
				}else{
					if(count>=4){
						Intent intent =  new Intent(context, SelectPitcherActivity.class);
						startActivity(intent);
						finish();
					}else{
					int n = count + 1;
					String fname = n + ".PNG";
					File sourceFile = new File(picturePath2);
					File mediaFile = new File(myDir, fname);
					if(mediaFile.exists()){
						mediaFile.delete();
					}
					new SaveImageBackTask(sourceFile, mediaFile).execute("");
						
				}
			
				}	
				
			}
		});
		
		btnRetake.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private class DisplayBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,pitcherPath;
		public DisplayBackTask(String picturePath2) {
			pitcherPath=picturePath2;
		}

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Fetching image");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
					
					Log.e("Camera", "Camera");
					Runtime.getRuntime().gc();
					
					File myDir = new File(pitcherPath);
					int file_size = Integer.parseInt(String.valueOf(myDir
							.length() / 1024));
					Log.e("file_size", " " + file_size);
					
					BitmapFactory.Options options = new BitmapFactory.Options();
					if (file_size < 1000) {
						
						Log.e("image size is less", "image size is less");
						bmp = BitmapFactory.decodeFile(myDir.toString());
					
					}if (file_size < 2000) {
						
						options.inSampleSize = 2;
						bmp = BitmapFactory.decodeFile(myDir.toString(), options);
					
					} else {
						Log.e("image size is heavy", "image size is heavy");
						
						options.inSampleSize = 4;
						bmp = BitmapFactory.decodeFile(myDir.toString(), options);
						Log.e("path", " " + myDir);
					}

		if(bmp!=null){
			errorMessage="true";
		}else{
			errorMessage="false";
		}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.equals("true")) {
				imgprivew.setImageBitmap(bmp);	
			} else {
				
				Toast.makeText(context, "please try again later",Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	
	private class SaveImageBackTask extends AsyncTask<String, Void, String> {
		String errorMessage="";
		File S_file,D_file;
		

		public SaveImageBackTask(File sourceFile, File mediaFile) {
			S_file=sourceFile;
			D_file=mediaFile;
		}

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Fetching image");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			Runtime.getRuntime().gc();

		    try {
				if (S_file.exists()) {
				FileChannel source = null;
				FileChannel destination = null;
				source = new FileInputStream(S_file).getChannel();
				destination = new FileOutputStream(D_file).getChannel();
				if (destination != null && source != null) {
				    destination.transferFrom(source, 0, source.size());
				}
				if (source != null) {
				    source.close();
				}
				if (destination != null) {
				    destination.close();
				}
				errorMessage="true";
				}else{
					errorMessage="false";
				}
			} catch (FileNotFoundException e) {
				errorMessage="false";
				e.printStackTrace();
			} catch (IOException e) {
				errorMessage="false";
				e.printStackTrace();
			}
			
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			count=0;
			getFileCount(myDir.toString());
			Log.e("cnt"," "+count);
			Changetext(count);
			if (result.equals("true")) {
				if(ComeFromPriviw.equals("true")){
					editor.putString("priview_image", "false");
	        		editor.commit();
	        		Intent intent =  new Intent(context, PriviewListing.class);
					startActivity(intent);
					finish();
				}else{
	        		
				count=0;
				getFileCount(myDir.toString());
				
				if(count>=4){
					Intent intent =  new Intent(context, SelectPitcherActivity.class);
					startActivity(intent);
					finish();
				}
				}
				Log.e("File is created", "File is created");
			} else {
				//Toast.makeText(TakePictureNew.this, "please try again later",Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


	public  String getPath(Uri uri) {
        // just some safety built in 
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }    
	
    public  String getPath(Uri uri, Context context) {
        // just some safety built in 
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }   


	public void getFileCount(String dirPath) {
		File f = new File(dirPath);
		File[] files = f.listFiles();

		if (files != null)
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					count++;
					getFileCount(file.getAbsolutePath());
				} else {
					count++;
				}
		}
	}
	
	
	 
	
	public static boolean isExternalStorageDocument(Uri uri) {
	     return "com.android.externalstorage.documents".equals(uri.getAuthority());
	 }

	 /**
	  * @param uri The Uri to check.
	  * @return Whether the Uri authority is DownloadsProvider.
	  */
	 public static boolean isDownloadsDocument(Uri uri) {
	     return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	 }

	 /**
	  * @param uri The Uri to check.
	  * @return Whether the Uri authority is MediaProvider.
	  */
	 public static boolean isMediaDocument(Uri uri) {
	     return "com.android.providers.media.documents".equals(uri.getAuthority());
	 }

	 /**
	  * @param uri The Uri to check.
	  * @return Whether the Uri authority is Google Photos.
	  */
	 public static boolean isGooglePhotosUri(Uri uri) {
	     return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	 }
	 
	 public Uri getFileURI(){
	  return fileUri;
	 }
	 public static String getDataColumn(Context context, Uri uri, String selection,
	         String[] selectionArgs) {

	     Cursor cursor = null;
	     final String column = "_data";
	     final String[] projection = {
	             column
	     };

	     try {
	         cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                 null);
	         if (cursor != null && cursor.moveToFirst()) {
	             final int index = cursor.getColumnIndexOrThrow(column);
	             return cursor.getString(index);
	         }
	     } finally {
	         if (cursor != null)
	             cursor.close();
	     }
	     return null;
	 }
}
