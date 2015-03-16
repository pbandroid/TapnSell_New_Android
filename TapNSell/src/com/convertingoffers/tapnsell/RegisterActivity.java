
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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.convertingoffers.tapnsell.Shop.BrouseCategoryActivity;
import com.convertingoffers.tapnsell.sell.PostItemActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	Context context;
	Custom_Dialog custom_dialog;
	String msg; 
	EditText evFName, evLName,evEmail, evMobileNo, evPassword, evCnfPassword;
	String strFName="", strLName="",hasPaypal="", Fname_PayPal="",lName_PayPal="",PaypalEmail="", strEmail="", strMobileNo="", strPassword="", strCnfPassword="",DeviceID,strUserID="",ItemDetailsStatus="n";
	String Status="",itemid,item_name,userid,strLogin_Dialog_Activity="N",item_description,item_condition,asking_price,delevery_option,address,quantity,category_id,auto_relist,tags,latitude,longitude,price_type;
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	Button btnRegister;
	TextView tvHeader;
	// Facebook
	Session mCurrentSession;
	GraphUser gUser;
	// Facebook
	ImageView ivMenu,ivBack;
	int total_media=0;

	String default_img="0",image = "",APILink;
	String JsonString="";
	Boolean successfull;
	static InputStream is = null;
	static String result;
	boolean status = false;
	File file;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
	
		
		DeviceID = pref.getString("registrationID", "");
		default_img= pref.getString("defaultimage", "");
		Log.e("DeviceID", " "+DeviceID);
		btnRegister.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle.containsKey("Login_Dialog_Activity")) {
			strLogin_Dialog_Activity = bundle.getString("Login_Dialog_Activity");
		}else{
			strLogin_Dialog_Activity="N";
		}
		
		ItemDetailsStatus=pref.getString("StatusItem", "");
		CalculateTotalMedia();
		
		hasPaypal=pref.getString("hasPaypal", "");
		
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		custom_dialog = new Custom_Dialog();
		ivBack=(ImageView)findViewById(R.id.ivBack);
		tvHeader=(TextView)findViewById(R.id.tvHeader);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		evFName = (EditText) findViewById(R.id.evFName);
		evLName = (EditText) findViewById(R.id.evLName);
		evEmail = (EditText) findViewById(R.id.evEmail);
		evMobileNo = (EditText) findViewById(R.id.evMobileNo);
		evPassword = (EditText) findViewById(R.id.evPassword);
		evCnfPassword = (EditText) findViewById(R.id.evCnfPassword);
		ivMenu = (ImageView) findViewById(R.id.ivMenu);
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
			Log.e("total_media", " "+total_media);
	}
	@SuppressLint({ "InlinedApi", "NewApi" }) @Override
	public void onClick(View v) {

	switch (v.getId()) {
	case R.id.btnRegister:
		strFName=evFName.getText().toString().trim();
		strLName=evLName.getText().toString().trim();
		strEmail=evEmail.getText().toString().trim();
		strMobileNo=evMobileNo.getText().toString().trim();
		strPassword=evPassword.getText().toString().trim();
		strCnfPassword=evCnfPassword.getText().toString().trim();
		
		
		 if(strFName.length()==0){
			 msg = "Please enter firstname";
//			ValidationSingleButton(msg);
			 Log.e("test", "test");
			Custom_Dialog.dialogCode(2, null, msg, context);
			evFName.requestFocus();
		}else if(strLName.length()==0){
			msg = "Please enter lastname";
			Custom_Dialog.dialogCode(2, null, msg, context);
			evLName.requestFocus();
		}else if(strEmail.length()==0){
			msg = "Please enter Email id";
			Custom_Dialog.dialogCode(2, null, msg, context);
			evEmail.requestFocus();
		}else if(!strEmail.matches(emailPattern)) {
			 msg = "Invalid email address";
				Custom_Dialog.dialogCode(2, null, msg, context);
				evEmail.requestFocus();
		}else if(strMobileNo.length()==0){
			msg = "Please enter mobile";
			Custom_Dialog.dialogCode(2, null, msg, context);
			evMobileNo.requestFocus();
		}else if(strPassword.length()==0){
			msg = "Please enter password";
			Custom_Dialog.dialogCode(2, null, msg, context);
			evPassword.requestFocus();
		}else if(strCnfPassword.length()==0){
			
				msg = "Password does not match.";
				Custom_Dialog.dialogCode(2, null, msg, context);
				evCnfPassword.requestFocus();
			
		}else if(!strCnfPassword.equals(strPassword)){
			
				msg = "password does not match";
				Custom_Dialog.dialogCode(2, null, msg, context);
				evCnfPassword.requestFocus();
			
		}else{
			if(cd.checkConnection()){
				
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(evCnfPassword.getWindowToken(), 0);
			//	new RegisterBackTask().execute("");
				
					RegisterBackTask task=new RegisterBackTask();
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			     else
			         task.execute();
			}else{
				Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
			}
		
		}
		break;
	case R.id.ivBack:
		
		if(ItemDetailsStatus.equals("Y")){
			Intent i = new Intent(context, PostItemActivity.class);
			startActivity(i);
			
			finish();
			}else{
//		Intent i = new Intent(context, BrouseCategoryActivity.class);
//		startActivity(i);
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				finish();
				// shop screen
	}
		
		
	break;
	default:
		break;
	}
	}
// register api calling
	private class RegisterBackTask extends AsyncTask<String, Void, String> {
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
				
				Log.e("strFName", " "+strFName);
				Log.e("strLName", " "+strLName);
				Log.e("strEmail", " "+strEmail);
				Log.e("strPassword", " "+strPassword);
				Log.e("strMobileNo", " "+strMobileNo);
				Log.e("A", "A");
				Log.e("DeviceID", " "+DeviceID);
				
				JSONObject json = userFunction.RegisterFunction
				(strFName, strLName, strEmail, strPassword, strMobileNo, "A", DeviceID);
				try {
					if (json!=null &&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject	User = json.getJSONObject("User");
							strUserID=User.getString("userid");
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
				
			
			
			
					
					editor.putString("UserID", strUserID); 
					editor.putString("reg_fname", ""+strFName);
					editor.putString("reg_lname", ""+strLName);
					editor.putString("reg_email", ""+strEmail);
					
					Log.e("strFName", " "+strFName);
					Log.e("strLName", " "+strLName);
					Log.e("strEmail", " "+strEmail);
					// Storing string
					editor.commit();
					userid=strUserID;
					if(ItemDetailsStatus.equals("Y")){

						if(hasPaypal.equals("Y")){
							
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
						
						Log.e("item_name", "reg "+item_name);
						Log.e("item_description", "reg "+item_description);
						Log.e("item_condition", "reg "+item_condition);
						Log.e("asking_price", "reg "+asking_price);
						Log.e("delevery_option", "reg "+delevery_option);
						Log.e("address", "reg "+address);
						Log.e("quantity", "reg "+quantity);
						Log.e("category_id", "reg "+category_id);
						Log.e("auto_relist", "reg "+auto_relist);
						Log.e("tags", "reg "+tags);
						Log.e("latitude", "reg "+latitude);
						Log.e("longitude", "reg "+longitude);
						Log.e("price_type", "reg "+price_type);
						
						new	AddItemDetailBackTask().execute("");
						}else{
							dialogCode();
						}
						}else if(strLogin_Dialog_Activity.equals("Y")){
							///
							Intent returnIntent = new Intent();
							setResult(RESULT_OK, returnIntent);
							finish();	
						}else{
					Intent i = new Intent(context, BrouseCategoryActivity.class);
					startActivity(i);
					finish();
							// shop screen
				}
				
			
			
			} else {
				Custom_Dialog.dialogCode(2, null, message, context);
			}

		}


		@Override
		protected void onProgressUpdate(Void... values) {

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

// add item details after normal registration 
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
			userid=strUserID;
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
			Log.e("true", "true");
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
	
	
	private class AddPayPalInfoBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message="";

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

			JSONObject json = userFunction.PayPalFunction(strUserID,PaypalEmail , Fname_PayPal, lName_PayPal);

			try {
				if (json!=null &&json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (res.equals(KEY_SUCCESS_STATUS)) {
						
						message=json.optString("message");
						 userFunction.VerifyFacebookFunction(userid, "P");
						errorMessage="true";
					} else {
						message=json.optString("message");
						errorMessage="false";
					}
				}else{
					message=json.optString("message");
					errorMessage="false";
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {

				
				editor.putString("UserID", strUserID); 
				editor.putString("reg_fname", ""+strFName);
				editor.putString("reg_lname", ""+strLName);
				editor.putString("reg_email", ""+strEmail);
				editor.putString("hasPaypal", "Y");
				editor.commit();
				Log.e("strFName", " "+strFName);
				Log.e("strLName", " "+strLName);
				Log.e("strEmail", " "+strEmail);
				// Storing string
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
					
					Log.e("item_name", "reg "+item_name);
					Log.e("item_description", "reg "+item_description);
					Log.e("item_condition", "reg "+item_condition);
					Log.e("asking_price", "reg "+asking_price);
					Log.e("delevery_option", "reg "+delevery_option);
					Log.e("address", "reg "+address);
					Log.e("quantity", "reg "+quantity);
					Log.e("category_id", "reg "+category_id);
					Log.e("auto_relist", "reg "+auto_relist);
					Log.e("tags", " "+tags);
					Log.e("latitude", " "+latitude);
					Log.e("longitude", " "+longitude);
					Log.e("price_type", " "+price_type);
					
					new	AddItemDetailBackTask().execute("");
					
					}else{
						 if(strLogin_Dialog_Activity.equals("Y")){
								
								Intent returnIntent = new Intent();
								setResult(RESULT_OK, returnIntent);
								
								Custom_Dialog.dialogCode(1, null, message, context);	
								
							}else{
								Custom_Dialog.dialogCode(2, null, message, context);
					}

				}

			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
		
	}
	
	
	@SuppressWarnings("static-access")
	public void dialogCode() {

		// custom dialog
		final Dialog dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_paypal);
		dialog.setCancelable(false);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		
		final EditText evPayFName,evPayLName,evPaypalEmail;
		
		ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
		ImageView ivCont = (ImageView) dialog.findViewById(R.id.ivCont);
		evPayFName = (EditText) dialog.findViewById(R.id.evPayFName);
		 evPayLName = (EditText) dialog.findViewById(R.id.evPayLName);
		 evPaypalEmail = (EditText) dialog.findViewById(R.id.evPaypalEmail);
		
		 ivClose.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							evPaypalEmail.getWindowToken(), 0);
					dialog.dismiss();
					
					
					editor.putString("UserID", strUserID); // Storing string
					editor.commit();

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
					
					Log.e("item_name", "reg "+item_name);
					Log.e("item_description", "reg "+item_description);
					Log.e("item_condition", "reg "+item_condition);
					Log.e("asking_price", "reg "+asking_price);
					Log.e("delevery_option", "reg "+delevery_option);
					Log.e("address", "reg "+address);
					Log.e("quantity", "reg "+quantity);
					Log.e("category_id", "reg "+category_id);
					Log.e("auto_relist", "reg "+auto_relist);
					Log.e("tags", " "+tags);
					Log.e("latitude", " "+latitude);
					Log.e("longitude", " "+longitude);
					Log.e("price_type", " "+price_type);
					
					new	AddItemDetailBackTask().execute("");
				}
			});
		ivCont.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			
				 Fname_PayPal=evPayFName.getText().toString().trim();
				 lName_PayPal=evPayLName.getText().toString().trim();
				 PaypalEmail=evPaypalEmail.getText().toString().trim();
				
				 Log.e("PaypalEmail ", " "+PaypalEmail);
				if(Fname_PayPal.length()==0){
					msg = "Please enter First Name";
					Custom_Dialog.dialogCode(2, null, msg, context);
					evPayFName.requestFocus();
					
				}else if(lName_PayPal.length()==0){
					
					msg = "Please enter Last Name";
					Custom_Dialog.dialogCode(2, null, msg, context);
					evPayLName.requestFocus();
					
					
				}else if(PaypalEmail.length()==0){
					
					msg = "Please enter PayPal Email";
					Custom_Dialog.dialogCode(2, null, msg, context);
					evPaypalEmail.requestFocus();
				}else if(!PaypalEmail.matches(emailPattern)) {
					
					msg = "Please enter Valid PayPal Email";
					Custom_Dialog.dialogCode(2, null, msg, context);
					evPaypalEmail.requestFocus();
				}else{
					if(cd.checkConnection()){
						
						Log.e("===", "====");
						Log.e("Fname_PayPal", " "+Fname_PayPal);
						Log.e("lName_PayPal", " "+lName_PayPal);
						Log.e("Email", " "+PaypalEmail);
						Log.e("userid", " "+strUserID);
						dialog.dismiss();
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(evPaypalEmail.getWindowToken(), 0);
						new AddPayPalInfoBackTask().execute("");
					}else{
						Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
					}
				}
			}
		});
	dialog.show();
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
		     Log.e("res",result);
		 } catch (Exception e) {
			 e.printStackTrace();
			 return "";
		 }
		 return result;
	 }
    
    

}