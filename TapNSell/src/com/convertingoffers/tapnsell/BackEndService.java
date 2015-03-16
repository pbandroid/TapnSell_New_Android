package com.convertingoffers.tapnsell;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import com.convertingoffers.tapnsell.util.ConnectionDetector;
import com.convertingoffers.tapnsell.util.UserFunctions;

public class BackEndService extends Service {
	boolean status = false;
	File file;
	public String KEY_SUCCESS = "successfull",KEY_SUCCESS_STATUS = "true",strmsg="";
	String default_img="0", userid = "", itemid = "",is_default= "N",image = "",APILink,type = "I";

	public ConnectionDetector cd;
	String JsonString="";
	Boolean successfull=false;
	static InputStream is = null;
	static String result;
	public UserFunctions userFunction;
	public Editor editor;
	public SharedPreferences pref;
	@SuppressLint("NewApi") @Override
	public void onCreate() {
		
		super.onCreate();
		
		pref =PreferenceManager.getDefaultSharedPreferences(BackEndService.this);
		editor = pref.edit();
	    
		userid = pref.getString("UserID", "");
		itemid = pref.getString("itemid", "");
		default_img= pref.getString("defaultimage", "");
		userFunction = new UserFunctions();
		cd = new ConnectionDetector(BackEndService.this);
		
		if (cd.checkConnection()) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				new BackendServiceBackTask().executeOnExecutor(
						AsyncTask.THREAD_POOL_EXECUTOR, "");
			} else {
				new BackendServiceBackTask().execute("");
			}
		} else {

		}
	}
	
	
	private class BackendServiceBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			
			Bitmap	pickimg=null ;
			Matrix matrix = null;
			byte[] ba = null ;
			BitmapFactory.Options option = new BitmapFactory.Options();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			{
				for (int i = 1; i < 6; i++) {

					if (i == 1) {

						if(default_img.equals("1")){
							is_default="Y";
						}else{
							is_default="N";
						}

					type="I";
					File file = new File(
							android.os.Environment
									.getExternalStorageDirectory(),
							"/TapNSell/CameraImage/1.PNG");
					if (file.exists()) {
						int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
						
						try {
							ExifInterface exif1 = new ExifInterface(file.getAbsolutePath());
							int orientation = exif1.getAttributeInt(
									ExifInterface.TAG_ORIENTATION, 1);
							 matrix = new Matrix();
							if (orientation == 6) {
								matrix.postRotate(90);
							} else if (orientation == 3) {
								matrix.postRotate(180);
							} else if (orientation == 8) {
								matrix.postRotate(270);
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
							
							
						
							
							
							if (file_size < 1000) {
								pickimg = BitmapFactory.decodeFile(file.getAbsolutePath());
							}else if (file_size < 2000) {
								option.inSampleSize = 2;
								pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
							
							} else {
								option.inSampleSize = 4;
								pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
							}
							
							pickimg = Bitmap.createBitmap(pickimg, 0, 0,
									pickimg.getWidth(), pickimg.getHeight(), matrix,
									true); // rotating bitmap
							pickimg.compress(CompressFormat.PNG, 100, baos);
							
							ba 	 = baos.toByteArray();
							
							

						} catch (OutOfMemoryError e) {
							e.printStackTrace();
						} catch (Exception e) {

						}
							
							
					try {
						APILink = UserFunctions.localBaseUrl + "Items/addimage1";
						MultipartEntity multipartcontent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
						
						multipartcontent.addPart("userid", new StringBody(userid, "text/plain", Charset.forName("UTF-8")));
						multipartcontent.addPart("itemid", new StringBody(itemid, "text/plain", Charset.forName("UTF-8")));
						multipartcontent.addPart("is_default", new StringBody(is_default, "text/plain", Charset.forName("UTF-8")));
						multipartcontent.addPart("type", new StringBody(type, "text/plain", Charset.forName("UTF-8")));
						ByteArrayBody bab = new ByteArrayBody(ba, "image/*", "ItemIage.png");
						multipartcontent.addPart("data[image]", bab);
					
						JsonString = getJsonStringMulitipart(APILink, multipartcontent);
						if(JsonString.length()!=0){
						JSONObject jsonObject = new JSONObject(JsonString);
						successfull = jsonObject.optBoolean("successfull");
						file.delete();
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(successfull){
					}
					}else {
						status = false;
					}
				
			
				
					
					
					} else if (i == 2) {
						
						if(default_img.equals("2")){
							is_default="Y";
						}else{
							is_default="N";
						}
						type="I";
						File file = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraImage/2.PNG");
						if (file.exists()) {
							
							
//							BitmapFactory.Options options = new BitmapFactory.Options();
//							options.inJustDecodeBounds = true;
//							BitmapFactory.decodeFile(file.toString(), options);
//							int width = options.outWidth;
//							int height = options.outHeight;
//							byte[] ba =convertFileToByteArray(file);
//							ba=rotateYUV420Degree90(ba, width, height);
							int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
							
							try {
								ExifInterface exif1 = new ExifInterface(file.getAbsolutePath());
								int orientation = exif1.getAttributeInt(
										ExifInterface.TAG_ORIENTATION, 1);
								 matrix = new Matrix();
								if (orientation == 6) {
									matrix.postRotate(90);
								} else if (orientation == 3) {
									matrix.postRotate(180);
								} else if (orientation == 8) {
									matrix.postRotate(270);
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							try {
								
//								BitmapFactory.Options option = new BitmapFactory.Options();
//								option.inSampleSize = 2;
//									pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(),
//										option);
								
								
								if (file_size < 1000) {
									
									pickimg = BitmapFactory.decodeFile(file.getAbsolutePath());
								
								}else if (file_size < 2000) {
									
									
									option.inSampleSize = 2;
									pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
								
								} else {
									
									option.inSampleSize = 4;
									pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
								}
								
								pickimg = Bitmap.createBitmap(pickimg, 0, 0,
										pickimg.getWidth(), pickimg.getHeight(), matrix,
										true); // rotating bitmap
								pickimg.compress(CompressFormat.PNG, 100, baos);
								
								ba 	 = baos.toByteArray();
								
								

							} catch (OutOfMemoryError e) {
								e.printStackTrace();
							} catch (Exception e) {

							}
						
						try {
							APILink = UserFunctions.localBaseUrl + "Items/addimage1";
							MultipartEntity multipartcontent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
							
							multipartcontent.addPart("userid", new StringBody(userid, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("itemid", new StringBody(itemid, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("is_default", new StringBody(is_default, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("type", new StringBody(type, "text/plain", Charset.forName("UTF-8")));
							ByteArrayBody bab = new ByteArrayBody(ba, "image/*", "ItemIage.png");
							multipartcontent.addPart("data[image]", bab);
						if(JsonString.length()!=0){
							JsonString = getJsonStringMulitipart(APILink, multipartcontent);
							JSONObject jsonObject = new JSONObject(JsonString);
							successfull = jsonObject.optBoolean("successfull");
							file.delete();
						}
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(successfull){
						}
						}else {
							status = false;
						}
					

					} else if (i == 3) {

						if(default_img.equals("3")){
							is_default="Y";
						}else{
							is_default="N";
						}
						
						File file = new File(
								android.os.Environment
										.getExternalStorageDirectory(),
								"/TapNSell/CameraImage/3.PNG");
						if (file.exists()) {
							
							
							int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
							
							try {
								ExifInterface exif1 = new ExifInterface(file.getAbsolutePath());
								int orientation = exif1.getAttributeInt(
										ExifInterface.TAG_ORIENTATION, 1);
								 matrix = new Matrix();
								if (orientation == 6) {
									matrix.postRotate(90);
								} else if (orientation == 3) {
									matrix.postRotate(180);
								} else if (orientation == 8) {
									matrix.postRotate(270);
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							try {
								
//								BitmapFactory.Options option = new BitmapFactory.Options();
//								option.inSampleSize = 2;
//									pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(),
//										option);
//								
								
								if (file_size < 1000) {
									pickimg = BitmapFactory.decodeFile(file.getAbsolutePath());
								}else if (file_size < 2000) {
									option.inSampleSize = 2;
									pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
								
								} else {
									option.inSampleSize = 4;
									pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
								}
								
								pickimg = Bitmap.createBitmap(pickimg, 0, 0,
										pickimg.getWidth(), pickimg.getHeight(), matrix,
										true); // rotating bitmap
								pickimg.compress(CompressFormat.PNG, 100, baos);
								
								ba 	 = baos.toByteArray();
								
								

							} catch (OutOfMemoryError e) {
								e.printStackTrace();
							} catch (Exception e) {
							}
						
						
						try {
							APILink = UserFunctions.localBaseUrl + "Items/addimage1";
							MultipartEntity multipartcontent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
							
							multipartcontent.addPart("userid", new StringBody(userid, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("itemid", new StringBody(itemid, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("is_default", new StringBody(is_default, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("type", new StringBody(type, "text/plain", Charset.forName("UTF-8")));
							ByteArrayBody bab = new ByteArrayBody(ba, "image/*", "ItemIage.png");
							multipartcontent.addPart("data[image]", bab);
						
							JsonString = getJsonStringMulitipart(APILink, multipartcontent);
							if(JsonString.length()!=0){
							JSONObject jsonObject = new JSONObject(JsonString);
							successfull = jsonObject.optBoolean("successfull");
							file.delete();
							}
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(successfull){
						}
						}else {
							status = false;
						}
					

					} else if (i == 4) {

						if(default_img.equals("4")){
							is_default="Y";
						}else{
							is_default="N";
						}
						
						File file = new File(
								android.os.Environment
										.getExternalStorageDirectory(),
								"/TapNSell/CameraImage/4.PNG");
						image = null;

						if (file.exists()) {
//							BitmapFactory.Options options = new BitmapFactory.Options();
//							options.inJustDecodeBounds = true;
//							BitmapFactory.decodeFile(file.toString(), options);
//							int width = options.outWidth;
//							int height = options.outHeight;
//							byte[] ba =convertFileToByteArray(file);
//							ba=rotateYUV420Degree90(ba, width, height);
							int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
							
							try {
								ExifInterface exif1 = new ExifInterface(file.getAbsolutePath());
								int orientation = exif1.getAttributeInt(
										ExifInterface.TAG_ORIENTATION, 1);
								 matrix = new Matrix();
								if (orientation == 6) {
									matrix.postRotate(90);
								} else if (orientation == 3) {
									matrix.postRotate(180);
								} else if (orientation == 8) {
									matrix.postRotate(270);
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							try {
								
//								BitmapFactory.Options option = new BitmapFactory.Options();
							
								
								
								if (file_size < 1000) {
									pickimg = BitmapFactory.decodeFile(file.getAbsolutePath());
								}else if (file_size < 2000) {
									option.inSampleSize = 2;
									pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
								
								} else {
									option.inSampleSize = 4;
									pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
								}
								
								pickimg = Bitmap.createBitmap(pickimg, 0, 0,
										pickimg.getWidth(), pickimg.getHeight(), matrix,
										true); // rotating bitmap
								pickimg.compress(CompressFormat.PNG, 100, baos);
								
								ba 	 = baos.toByteArray();
								
								

							} catch (OutOfMemoryError e) {
								e.printStackTrace();
							} catch (Exception e) {
							}
						
					
						
						try {
							APILink = UserFunctions.localBaseUrl + "Items/addimage1";
							MultipartEntity multipartcontent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
							
							multipartcontent.addPart("userid", new StringBody(userid, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("itemid", new StringBody(itemid, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("is_default", new StringBody(is_default, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("type", new StringBody(type, "text/plain", Charset.forName("UTF-8")));
						
							ByteArrayBody bab = new ByteArrayBody(ba, "image/*", "ItemIage.png");
							multipartcontent.addPart("data[image]", bab);
						
							JsonString = getJsonStringMulitipart(APILink, multipartcontent);
							if(JsonString.length()!=0){
							JSONObject jsonObject = new JSONObject(JsonString);
							successfull = jsonObject.optBoolean("successfull");
							file.delete();}
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(successfull){
						}
						}else {
							status = false;
						}
					

					} else if (i == 5){
						type="V";
						File file = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraVideo/TapnSellVideo.mp4");
						if (file.exists()) {
								 ba =convertFileToByteArray(file);
						try {
							APILink = UserFunctions.localBaseUrl + "Items/addimage1";
							MultipartEntity multipartcontent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
							
							multipartcontent.addPart("userid", new StringBody(userid, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("itemid", new StringBody(itemid, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("is_default", new StringBody(is_default, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("type", new StringBody(type, "text/plain", Charset.forName("UTF-8")));
							ByteArrayBody bab = new ByteArrayBody(ba, "video/mp4", "20130615_193547.mp4");
							multipartcontent.addPart("data[video]", bab);
							if(JsonString.length()!=0){
							JsonString = getJsonStringMulitipart(APILink, multipartcontent);
							JSONObject jsonObject = new JSONObject(JsonString);
							successfull = jsonObject.optBoolean("successfull");
							file.delete();
							}
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(successfull){
						}
						}
					}
				}
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {
		}
	}
	
	 public static String getJsonStringMulitipart(String ApiLink, MultipartEntity multipartentity){
		 try{
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
		     HttpPost httppost = new HttpPost(ApiLink);
		     httppost.setEntity(multipartentity);
		     
		     HttpResponse response = httpclient.execute(httppost,localContext);
		     if( response == null ){
			     
		      }else{
		    	  HttpEntity entity = response.getEntity();
		          is = entity.getContent();
		          
		      //    //Log.e("log_tag", ""+is.toString());
		      }
		 } catch(Exception e){
		     return "";
		 }
		 
		 // convert response to string
		 try {
			 BufferedReader reader = new BufferedReader(new InputStreamReader(
					 is, "iso-8859-1"), 8);
		     StringBuilder sb = new StringBuilder();
		     String line = null;
		     while ((line = reader.readLine()) != null) {
		    	 sb.append(line + "\n");
		     }
		     is.close();
		     result = sb.toString();
		 } catch (Exception e) {
			 e.printStackTrace();
			 return "";
		 }
		 return result;
	 }
    
    
	public static byte[] convertFileToByteArray(File f)
	 {
	 byte[] byteArray = null;
	 try
	 {
	 InputStream inputStream = new FileInputStream(f);
	 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 byte[] b = new byte[1024*8];
	 int bytesRead =0;
	 
	 while ((bytesRead = inputStream.read(b)) != -1)
	 {
    	bos.write(b, 0, bytesRead);
	 }

	 byteArray = bos.toByteArray();
	 }
	 catch (IOException e)
	 {
	 e.printStackTrace();
	 }
	 return byteArray;
	 }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	

}

/*
private void deleteallFile() {
	
	 file = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraImage/1.PNG");
	if(file.exists()){
		file.delete();
	}
	
	 file = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraImage/2.PNG");
	if(file.exists()){
		file.delete();
	}
	
	 file = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraImage/3.PNG");
	if(file.exists()){
		file.delete();
	}
	
	 file = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraImage/4.PNG");
		if(file.exists()){
			file.delete();
	}
		
	file = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraVideo/TapnSellVideo.mp4");
		if(file.exists()){
			file.delete();
	}
}

*/