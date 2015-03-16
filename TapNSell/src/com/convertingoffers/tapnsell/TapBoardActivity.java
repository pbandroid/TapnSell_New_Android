package com.convertingoffers.tapnsell;
import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.convertingoffers.tapnsell.Modal.SearchModel;
import com.convertingoffers.tapnsell.Shop.BrouseCategoryActivity;
import com.convertingoffers.tapnsell.Shop.ListingActivity;
import com.convertingoffers.tapnsell.TapboardFooter.Help_Activity;
import com.convertingoffers.tapnsell.TapboardFooter.MyAccount_Activity;
import com.convertingoffers.tapnsell.TapboardFooter.Setting_Activity;
import com.convertingoffers.tapnsell.TapboardSell.MessageBoard_Activity;
import com.convertingoffers.tapnsell.TapboardSell.MyEarning_Activity;
import com.convertingoffers.tapnsell.TapboardSell.MyListingActive_Sold_Expire_Activity;
import com.convertingoffers.tapnsell.TapboardSell.OpenCaseActivity;
import com.convertingoffers.tapnsell.TapboardSell.TODOS_Activity;
import com.convertingoffers.tapnsell.TapboardShop.TapBoardMyOrderList;
import com.convertingoffers.tapnsell.TapboardShop.TapInspireActivity;
import com.convertingoffers.tapnsell.sell.TakePictureNew;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;

public class TapBoardActivity extends BaseActivity implements OnClickListener {

	Animation RightSwipe;
		ImageView ivWatchList, ivToDos, ivTapInspire, ivNewListing, ivEarning,
			ivMyOrder, ivMyListing, ivShop, ivSellIt, ivGobtn;
	TextView tvTodos, tvMessage,tvCaseNo;
	EditText evsearch;
	String strname, strlatitude="0", strlongitude="0",categoryid,reserved="",hasPaypal="";
	String itemid, userid, name, price, distance,case_no="", image, has_like, no_of_likes,todo,message_count;
	int intStartNewListing=0;
	ArrayList<SearchModel> mSearchList = new ArrayList<SearchModel>();
	boolean UserIdStatus = false;
	Context context;
	File file;
	RelativeLayout  rlMessage,rlMyAccount,rlSetting,rlCases,rlHelp;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tapboard);

		userid = pref.getString("UserID", "");
		tvHeader.setText("Tap Board");
		ivTapInspire.setOnClickListener(this);
		ivWatchList.setOnClickListener(this);
		ivNewListing.setOnClickListener(this);
		ivMyOrder.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		ivGobtn.setOnClickListener(this);
		ivSellIt.setOnClickListener(this);
		ivShop.setOnClickListener(this);
		ivToDos.setOnClickListener(this);
		rlMessage.setOnClickListener(this);
		ivEarning.setOnClickListener(this);
		ivMyListing.setOnClickListener(this);
		rlMyAccount.setOnClickListener(this);
		rlSetting.setOnClickListener(this);
		rlCases.setOnClickListener(this);
		rlHelp.setOnClickListener(this);
		
		
		if (cd.checkConnection()) {
			
			TODOSBackTask task=new TODOSBackTask();
			 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		     else
		         task.execute();
			
		} else {
			Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
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

				JSONObject json = userFunction.TapBoardFunction(userid);
				try {
					if (json!=null &&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONObject JCount = json.getJSONObject("Count");
							todo=JCount.getString("todo");
							message_count=JCount.getString("message");
							case_no=JCount.getString("case");
							errorMessage = "true";
						} else {
							message=json.optString("message");
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

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {
				
				tvTodos.setText(todo);
				tvMessage.setText(message_count);
				tvCaseNo.setText(case_no);
			} else {
				Custom_Dialog.dialogCode(2, null,message,
						context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		
		context=this;
		ivWatchList = (ImageView) findViewById(R.id.ivWatchList);
		ivToDos = (ImageView) findViewById(R.id.ivToDos);
		ivTapInspire = (ImageView) findViewById(R.id.ivTapInspire);
		ivNewListing = (ImageView) findViewById(R.id.ivNewListing);
		ivEarning = (ImageView) findViewById(R.id.ivEarning);
		ivMyOrder = (ImageView) findViewById(R.id.ivMyOrder);
		ivMyListing = (ImageView) findViewById(R.id.ivMyListing);
		ivShop = (ImageView) findViewById(R.id.ivShop);
		ivSellIt = (ImageView) findViewById(R.id.ivSell);
		rlMessage = (RelativeLayout) findViewById(R.id.rlMessage);
		ivGobtn = (ImageView) findViewById(R.id.ivGobtn);
		tvTodos = (TextView) findViewById(R.id.tvTodos);
		tvMessage = (TextView) findViewById(R.id.tvMessage);
		tvCaseNo = (TextView) findViewById(R.id.tvCaseNo);
		evsearch= (EditText) findViewById(R.id.evsearch);
		
		rlMyAccount= (RelativeLayout) findViewById(R.id.rlMyAccount);
		rlSetting= (RelativeLayout) findViewById(R.id.rlSetting);
		rlCases= (RelativeLayout) findViewById(R.id.rlCases);
		rlHelp= (RelativeLayout) findViewById(R.id.rlHelp);
	}

	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {

		case R.id.ivSell:

			if (cd.checkConnection()) {
				CheckMediaUploadedBackTask task=new CheckMediaUploadedBackTask();
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			     else
			         task.execute();
				
				
			} else {
				Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
			}

			break;

		case R.id.ivShop:
			buttonAnimation(ivShop);
			if (cd.checkConnection()) {
				editor.remove("S_ComefromonePageCheck");
				editor.remove("ComefromonePageCheck");
				editor.commit();
				Intent iShop = new Intent(context, BrouseCategoryActivity.class);
				startActivity(iShop);
			} else {
				Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
			}

				
			
			break;
			
		case R.id.ivBack:
			buttonAnimation(ivBack);
			 i = new Intent(context, HomeActivity.class);
			startActivity(i);
			finish();
		break;
		case R.id.ivGobtn:
			if (cd.checkConnection()) {
				buttonAnimation(ivGobtn);
				strname=evsearch.getText().toString().trim();
				strlatitude = pref.getString("lat", "");
				strlongitude = pref.getString("long", "");
				 if(strlatitude.length()==0 && strlongitude.length()==0){
						strlatitude="0";
						strlongitude="0";
					}
				if(strname.length()==0){
					Custom_Dialog.dialogCode(2, null, "Please enter text to search.", context);
					//ValidationSingleButton("Please enter text to search.",false);
				}else if(userid.length()==0){
					 i = new Intent(context,
							SignInActivity.class);
					 Custom_Dialog.dialogCode(3, i, "Please Login again.", context);
					//ValidationSingleButton("Please Login again",UserIdStatus);
					UserIdStatus=true;
				}else{
					if(cd.checkConnection()){
						evsearch.setText("");
						
						SearchBackTask task=new SearchBackTask();
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					     else
					         task.execute();
						
					}else{
						Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
					}
				}
			} else {
				Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
			}

		
		break;
		case R.id.ivTapInspire:
			buttonAnimation(ivTapInspire);
			if (cd.checkConnection()) {
			i =new  Intent(context, TapInspireActivity.class);
			i.putExtra("type", "tap");
			startActivity(i);
			} else {
				Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
			}
			
		break;
		
		case R.id.ivWatchList:
			buttonAnimation(ivWatchList);
			if (cd.checkConnection()) {
				i = new Intent(context, TapInspireActivity.class);
				i.putExtra("type", "watch");
				startActivity(i);
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
				
			
			
		break;
		
		case R.id.ivNewListing:
			
			
			buttonAnimation(ivNewListing);
			if (cd.checkConnection()) {
				i = new Intent(context, ListingActivity.class);
				i.putExtra("categoryid", "");
				i.putExtra("ListingType","Listings");
				startActivity(i);
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
				
			
			
			
		break;
		
		case R.id.ivMyOrder:
			buttonAnimation(ivMyOrder);
			
			if (cd.checkConnection()) {
				i = new Intent(context, TapBoardMyOrderList.class);
				i.putExtra("type", "Listings");
				startActivity(i);
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
			
		break;
		case R.id.ivToDos:
			buttonAnimation(ivToDos);
			if (cd.checkConnection()) {
				i = new Intent(context, TODOS_Activity.class);
				startActivity(i);
				finish();
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
		
		break;
	/*	case R.id.ivItem:
			buttonAnimation(ivToDos);
			i = new Intent(context, TODOS_Activity.class);
			startActivity(i);
		break;*/
		case R.id.ivEarning:
			buttonAnimation(ivEarning);
			if (cd.checkConnection()) {
				i = new Intent(context, MyEarning_Activity.class);
				startActivity(i);
				finish();
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
		
			
		break;
		case R.id.rlMessage:
			buttonAnimation(rlMessage);
			if (cd.checkConnection()) {
				i = new Intent(context, MessageBoard_Activity.class);
				startActivity(i);
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
			
		break;
		case R.id.rlMyAccount:
			buttonAnimation(rlMyAccount);
			if (cd.checkConnection()) {
				i = new Intent(context, MyAccount_Activity.class);
				startActivity(i);
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
			
		break;
		
		case R.id.rlSetting:
			buttonAnimation(rlSetting);
			if (cd.checkConnection()) {
				i = new Intent(context, Setting_Activity.class);
				startActivity(i);
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
			
			
		break;
		
		case R.id.rlCases:
			buttonAnimation(rlCases);
			if (cd.checkConnection()) {
				i = new Intent(context, OpenCaseActivity.class);
				startActivity(i);
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
		
		break;
		
		case R.id.rlHelp:
			buttonAnimation(rlHelp);
			if (cd.checkConnection()) {
				i = new Intent(context, Help_Activity.class);
				startActivity(i);
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
		
			
		break;
		
		case R.id.ivMyListing:
			buttonAnimation(ivMyListing);
			if (cd.checkConnection()) {
				i = new Intent(context,
						MyListingActive_Sold_Expire_Activity.class);
				startActivity(i);
			} else {
				Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
			}
		
		break;
		
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
	super.onBackPressed();
	Intent i = new Intent(context, HomeActivity.class);
		startActivity(i);
		finish();
	}
	private class CheckMediaUploadedBackTask extends
	AsyncTask<String, Void, String> {
String errorMessage;

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

		JSONObject json = userFunction
				.CheckMediaUploadFunction(userid);
		try {
			if (json!=null && json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if (res.equals(KEY_SUCCESS_STATUS)) {
					
					hasPaypal=json.getString("haspaypal");
					
					errorMessage = "true";
				} else {
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

		buttonAnimation(ivSellIt);
		deleteallFile();
		
		editor.putString("hasPaypal", hasPaypal);
		editor.remove("item_name");
		editor.remove("item_description");
		editor.remove("item_condition");
		editor.remove("asking_price");
		editor.remove("quantity");
		editor.remove("delevery_option");
		editor.remove("priview");
		editor.remove("image_path");
		editor.remove("item_name");
		editor.remove("item_description");
		editor.remove("item_condition");
		editor.remove("asking_price");
		editor.remove("quantity");
		editor.remove("delevery_option");
		editor.remove("category_id");
		editor.commit();

		Intent iSellIt = new Intent(context, TakePictureNew.class);
		startActivity(iSellIt);

	} else if (result.equals("network")) {
		Custom_Dialog.dialogCode(2, null,"Your internet connection is too slow", context);
//		ValidationSingleButton("Error in posting");
	} else {
		Custom_Dialog.dialogCode(2, null,
				"Please wait your privious item is uploading...!",
				context);
	}
}



// deleting all file from folder
private void deleteallFile() {

	file = new File(android.os.Environment.getExternalStorageDirectory(),
			"/TapNSell/CameraImage/1.PNG");
	if (file.exists()) {
		file.delete();
	}

	file = new File(android.os.Environment.getExternalStorageDirectory(),
			"/TapNSell/CameraImage/2.PNG");
	if (file.exists()) {
		file.delete();
	}

	file = new File(android.os.Environment.getExternalStorageDirectory(),
			"/TapNSell/CameraImage/3.PNG");
	if (file.exists()) {
		file.delete();
	}

	file = new File(android.os.Environment.getExternalStorageDirectory(),
			"/TapNSell/CameraImage/4.PNG");
	if (file.exists()) {
		file.delete();
	}

	file = new File(android.os.Environment.getExternalStorageDirectory(),
			"/TapNSell/CameraVideo/TapnSellVideo.mp4");
	if (file.exists()) {
		file.delete();
	}

}


@Override
protected void onProgressUpdate(Void... values) {

}
}


	
	private class SearchBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json = userFunction.SearchFunction(userid,
						strname, strlatitude, strlongitude,""+intStartNewListing,"P");
				try {
					if (json!=null &&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONArray Item = json.getJSONArray("Item");
							for (int i = 0; i < Item.length(); i++) {

								JSONObject c = Item.getJSONObject(i);

								itemid = c.getString("itemid");
								userid = c.getString("userid");
								name = c.getString("name");
								price = c.getString("price");
								distance = c.getString("distance");
								reserved = c.getString("reserved");
								image = c.getString("image");
								image = UserFunctions.localImageUrl + image;

								has_like = c.getString("has_like");
								no_of_likes = c.getString("no_of_likes");
								mSearchList.add(new SearchModel(itemid, userid,
										name, price, distance, image, has_like,
										no_of_likes,"",reserved));
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
			if (result.equals("true")) {
				
				Intent i =new Intent(context, ListingActivity.class);
				i.putExtra("SearchData",mSearchList);
				 i.putExtra("ListingType","Search");
				startActivity(i);
			} else if (result.equals("network")) {
				Custom_Dialog.dialogCode(2, null,"Your internet connection is too slow", context);
//				ValidationSingleButton("Error in posting");
			} else {
				
				Custom_Dialog.dialogCode(2, null,message, context);
				
				
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	private void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(context,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}

}
