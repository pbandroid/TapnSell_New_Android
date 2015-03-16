
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.convertingoffers.tapnsell.sell.PostItemActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;

public class SignInActivity extends BaseActivity implements OnClickListener {
	
	Custom_Dialog custom_dialog;
	Context context ;
	String msg;
	EditText evEmail, evPassword;
	String strEmail, strPassword, DeviceID,strUserID,ItemDetailsStatus;
	String itemid,item_name,userid,Status="",item_description,strLogin_Dialog_Activity="",item_condition,asking_price,delevery_option,address,quantity,category_id,auto_relist,tags,latitude,longitude,price_type;
	RelativeLayout rlSignIn;
	ImageView ivRegister, ivForgotPassword,ivBack;
	int total_media=0;
	String default_img="0",image = "",APILink;
	String JsonString="";
	Boolean successfull;
	static InputStream is = null;
	static String result;
	boolean status = false;
	File file;
	
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		DeviceID = pref.getString("registrationID", "");
		default_img= pref.getString("defaultimage", "");
		rlSignIn.setOnClickListener(this);
		ivRegister.setOnClickListener(this);
		ivForgotPassword.setOnClickListener(this);
		ItemDetailsStatus=pref.getString("StatusItem", "");
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			strLogin_Dialog_Activity = bundle.getString("Login_Dialog_Activity");
		}
		
		
		ivBack.setOnClickListener(this);
		CalculateTotalMedia();
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		custom_dialog = new Custom_Dialog();
		
		
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivRegister = (ImageView) findViewById(R.id.ivRegister);
		ivForgotPassword = (ImageView) findViewById(R.id.ivForgotPassword);
		rlSignIn = (RelativeLayout) findViewById(R.id.rlSignIn);
		evEmail = (EditText) findViewById(R.id.evEmail);
		evPassword = (EditText) findViewById(R.id.evPassword);

	}

	 public void CalculateTotalMedia() {
		
		 File file;
		 for (int i = 1; i < 5; i++) {
				 file = new File(
						android.os.Environment
								.getExternalStorageDirectory(),
						"/TapNSell/CameraImage/"+i+".PNG");
				if(file.exists()){
					total_media=total_media+1;	
				}
				
				
		 }
		  file = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraVideo/TapnSellVideo.mp4");
		  if(file.exists()){
				total_media=total_media+1;	
			}
	}


	@SuppressLint({ "InlinedApi", "NewApi" }) @Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rlSignIn:

			strEmail = evEmail.getText().toString().trim();
			strPassword = evPassword.getText().toString().trim();

			 if (strPassword.length() == 0 && strEmail.length() == 0) {
				msg = "Please enter email & Password.";
				Custom_Dialog.dialogCode(2, null, msg, context);
//			ValidationSingleButton(msg);
			}else if (strEmail.length() == 0) {
				msg = "Please enter email address.";
				Custom_Dialog.dialogCode(2, null, msg, context);
			} else if(!strEmail.matches(emailPattern)) {
				 msg = "Please enter valid email address.";
				 Custom_Dialog.dialogCode(2, null, msg, context);
			}else if (strPassword.length() == 0) {
				msg = "Please enter Password.";
				Custom_Dialog.dialogCode(2, null, msg, context);
			} else {
				if(cd.checkConnection()){
						LoginBackTask task=new LoginBackTask();
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				     else
				         task.execute();
					
				}else{
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
			}
			break;
			
		case R.id.ivRegister:

			editor.putString("StatusItem", "N");
			editor.commit();
			Intent iSignIn = new Intent(context, RegisterActivity.class);
			iSignIn.putExtra("Login_Dialog_Activity", "Y");
			startActivityForResult(iSignIn, 4);
			break;
		case R.id.ivBack:
	
			if(ItemDetailsStatus.equals("Y")){
				Intent i = new Intent(context, PostItemActivity.class);
				startActivity(i);
				finish();
					
				}else{
			/*Intent i = new Intent(context, BrouseCategoryActivity.class);
			startActivity(i);*/
			finish();
					// shop screen
				}
			break;
			
		case R.id.ivForgotPassword:

			Intent iForgotPassword = new Intent(context, ForgotPasswordActivity.class);
			startActivity(iForgotPassword);
			finish();
			break;
			
		default:
			break;
		}
	}
// Login api calling 
	private class LoginBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json = userFunction.LoginFunction(strEmail,
						strPassword, DeviceID);
				try {
					if (json!=null &&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject	User = json.getJSONObject("User");
							strUserID=User.getString("userid");
							
							message=json.optString("message");
							errorMessage = "true";

						} else {
							message=json.optString("message");
							errorMessage = "false";
						}
					} else {
						message=json.optString("message");
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

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {

				editor.putString("UserID", strUserID); // Storing string
				editor.commit();
				
				if(ItemDetailsStatus.equals("Y")){
					
					userid=strUserID;
					item_name	=pref.getString("item_name", "");
					item_description=pref.getString("item_description", "");
					item_condition=pref.getString("item_condition", "");
					asking_price=pref.getString("asking_price", "");
					delevery_option=pref.getString("delevery_option", "");
					address=pref.getString("address", "");
					quantity=pref.getString("quantity", "");
					category_id=pref.getString("category_id", "");
					auto_relist=pref.getString("auto_relist", "");
					tags=pref.getString("tags", "");
					latitude=pref.getString("latitude", "");
					longitude=pref.getString("longitude", "");
					price_type=pref.getString("price_type", "");
					new	AddItemDetailBackTask().execute("");
						
					}else{
						 if(strLogin_Dialog_Activity.equals("Y")){
								
							 Intent returnIntent = new Intent();
								setResult(RESULT_OK, returnIntent);
								finish();	
								
							}else{
								Custom_Dialog.dialogCode(2, null, message, context);
					}

				}
				
			} else {
				Custom_Dialog.dialogCode(2, null, "Invalid Email or Password...!", context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 4) {
//				dialog.dismiss();
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				finish();
				
			}
	}
// soft keyboard dispatch event
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
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
		return ret;
	}

	// add item details  after login
	
	private class AddItemDetailBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			JSONObject json = userFunction.AddItemFunction(userid, item_name, item_description, item_condition,
					asking_price, quantity, delevery_option, category_id, address, latitude, longitude,""+total_media);

			try {
				if (json!=null &&json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (res.equals(KEY_SUCCESS_STATUS)) {
						
						JSONObject Item=json.getJSONObject("Item");
						itemid=Item.getString("itemid");
						
						errorMessage="true";
					} else {
						errorMessage="false";
					}
				}else{
					errorMessage="false";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

		
			if (result.equals("true")) {
				
				new BackendServiceBackTask().execute("");
			}else{
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
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

	 public class BackendServiceBackTask extends AsyncTask<String, Void, String> {
			String errorMessage;

			protected void onPreExecute() {
			}

			@Override
			protected String doInBackground(String... params) {
				{

						File file = new File(
								android.os.Environment
										.getExternalStorageDirectory(),
								"/TapNSell/CameraImage/"+default_img+".PNG");
						if (file.exists()) {
						byte[] ba =convertFileToByteArray(file);
						try {
							APILink = UserFunctions.localBaseUrl + "Items/addimage1";
							MultipartEntity multipartcontent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
							
							multipartcontent.addPart("userid", new StringBody(userid, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("itemid", new StringBody(itemid, "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("is_default", new StringBody("Y", "text/plain", Charset.forName("UTF-8")));
							multipartcontent.addPart("type", new StringBody("I", "text/plain", Charset.forName("UTF-8")));
						
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
							status = true;
						}
						}else {
							status = false;
						}
				}
				return errorMessage;
			}

			@Override
			protected void onPostExecute(String result) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				if(status){
					editor.putString("itemid", itemid); 
					editor.remove("item_name"); 
					editor.remove("item_description"); 
					editor.remove("item_condition"); 
					editor.remove("asking_price"); 
					editor.remove("delevery_option"); 
					editor.remove("address"); 
					editor.remove("quantity"); 
					editor.remove("category_id"); 
					editor.remove("latitude"); 
					editor.remove("longitude"); 
					editor.commit();
					
					Intent i = new Intent(context, PostItemActivity.class);
					startActivity(i);
					finish();
				}
			}
		}
		
	 
	}
/*// default dialog
	private void ValidationSingleButton(String msg) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);

		alertDialogBuilder.setTitle("alert");

		alertDialogBuilder.setMessage(msg).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
						
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}*/
