package com.convertingoffers.tapnsell.TapboardSell;

import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Shop.BrouseCategoryActivity;
import com.convertingoffers.tapnsell.Shop.ListingActivity;
import com.convertingoffers.tapnsell.TapboardFooter.MyAccount_Activity;
import com.convertingoffers.tapnsell.TapboardShop.TapInspireActivity;
import com.convertingoffers.tapnsell.sell.TakePictureNew;
import com.convertingoffers.tapnsell.util.AutoResizeTextView;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class TODOS_Activity extends BaseActivity implements OnClickListener {

	File file;
	
	AutoResizeTextView tvNewListing,tvTapInsp,tvShip,tvMeetUp,tvNewOffer,tvInbox,tvCases,tvNeedFeedBack,tvPromotListing,tvMyAccount;
	String newlist,tapinspired,toship,meetups,offers,inbox,cases,needfeedback,promote,hasPaypal="",watchlist="",mylisting="",myAccount="";
	RelativeLayout rlNewListing,rlAddPaypalAccount,rlTOShip,rlMeetUps,rlNewOffer,rlInBox,rlCases,rlNeedFeedback,rlPromotionListing
	,rlMyAccount,rltapinspire;
	Animation RightSwipe;
	Context context;
	String userid;
	
	boolean see_recomondation=false,see_newlist=false;
	ImageView ivSell,ivShop;
	 Dialog	 dialogpaypal;
	 String Fname_PayPal,lName_PayPal,PaypalEmail,msg;
	 String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todos);
		
		see_recomondation=pref.getBoolean("SeeRecomodation", false);
		see_newlist=pref.getBoolean("SeeNewList", false);
		
		userid = pref.getString("UserID", "");
		tvHeader.setText("To-Do's");
		ivBack.setOnClickListener(this);
		rlTOShip.setOnClickListener(this);
		rlMeetUps.setOnClickListener(this);
		rlNewOffer.setOnClickListener(this);
		rlInBox.setOnClickListener(this);
		rlCases.setOnClickListener(this);
		rlNeedFeedback.setOnClickListener(this);
		rlPromotionListing.setOnClickListener(this);
		ivSell.setOnClickListener(this);
		ivShop.setOnClickListener(this);
		rlMyAccount.setOnClickListener(this);
		rltapinspire.setOnClickListener(this);
		rlAddPaypalAccount.setOnClickListener(this);
		rlNewListing.setOnClickListener(this);
		
		if (cd.checkConnection()) {
			new TODOSBackTask().execute("");
		} else {
			Toast.makeText(TODOS_Activity.this,
					"Interner connection is not available!", Toast.LENGTH_LONG)
					.show();
		}
	}
	@Override
	public void onBackPressed() {
	super.onBackPressed();
	Intent i = new Intent(context, TapBoardActivity.class);
		startActivity(i);
		finish();
	}
	
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		tvTapInsp= (AutoResizeTextView) findViewById(R.id.tvTapInsp);
		tvMyAccount= (AutoResizeTextView) findViewById(R.id.tvMyAccount);
		tvShip = (AutoResizeTextView) findViewById(R.id.tvShip);
		tvMeetUp = (AutoResizeTextView) findViewById(R.id.tvMeetUp);
		tvNewOffer = (AutoResizeTextView) findViewById(R.id.tvNewOffer);
		tvInbox = (AutoResizeTextView) findViewById(R.id.tvInbox);
		tvCases = (AutoResizeTextView) findViewById(R.id.tvCases);
		tvNeedFeedBack = (AutoResizeTextView) findViewById(R.id.tvNeedFeedBack);
		tvPromotListing = (AutoResizeTextView) findViewById(R.id.tvPromotListing);
		tvNewListing = (AutoResizeTextView) findViewById(R.id.tvNewListing);
		rlTOShip = (RelativeLayout) findViewById(R.id.rlTOShip);
		rlMeetUps= (RelativeLayout) findViewById(R.id.rlMeetUps);
		rlNewOffer= (RelativeLayout) findViewById(R.id.rlNewOffer);
		rlInBox= (RelativeLayout) findViewById(R.id.rlInBox);
		rlCases= (RelativeLayout) findViewById(R.id.rlCases);
		rlNeedFeedback= (RelativeLayout) findViewById(R.id.rlNeedFeedback);
		rlPromotionListing= (RelativeLayout) findViewById(R.id.rlPromotionListing);
		ivShop= (ImageView)findViewById(R.id.ivShop);
		ivSell= (ImageView) findViewById(R.id.ivSell);
		rlAddPaypalAccount= (RelativeLayout) findViewById(R.id.rlAddPaypalAccount);
		rlMyAccount= (RelativeLayout) findViewById(R.id.rlMyAccount);
		rltapinspire= (RelativeLayout) findViewById(R.id.rltapinspire);
		rlNewListing= (RelativeLayout) findViewById(R.id.rlNewListing);
	}

	
	
	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
	
			
		case R.id.rlMyAccount:
			buttonAnimation(rlMyAccount);
			i = new Intent(context, MyAccount_Activity.class);
			startActivity(i);
			finish();
		break;
			
		case R.id.rltapinspire:
			buttonAnimation(rltapinspire);
			
			rltapinspire.setVisibility(View.GONE);
			editor.putBoolean("SeeRecomodation", false);
			  editor.commit();
			i = new Intent(context, TapInspireActivity.class);
			i.putExtra("type", "tap");
			startActivity(i);
		break;
		
		case R.id.rlTOShip:
			buttonAnimation(rlTOShip);
			i = new Intent(context, ItemToShip_Activity.class);
			startActivity(i);
			finish();
		break;
	
		case R.id.rlMeetUps:
			buttonAnimation(rlMeetUps);
			i = new Intent(context, ScheduleMeetup_Activity.class);
			startActivity(i);
			finish();
		break;
			
		case R.id.ivBack:
			buttonAnimation(ivBack);
			i = new Intent(context, TapBoardActivity.class);
			startActivity(i);
			finish();
		break;
			
		case R.id.ivSell:

			if (cd.checkConnection()) {
				new CheckMediaUploadedBackTask().execute("");
			} else {
				Toast.makeText(context,"Interner connection is not available!",
						Toast.LENGTH_LONG).show();
			}
		break;

		case R.id.ivShop:
			buttonAnimation(ivShop);
			
				editor.remove("S_ComefromonePageCheck");
				editor.remove("ComefromonePageCheck");
				editor.commit();
				Intent iShop = new Intent(context, BrouseCategoryActivity.class);
				startActivity(iShop);
				
			break;

			
		case R.id.rlNewOffer:
			buttonAnimation(rlNewOffer);
			i = new Intent(context, ViewRecieveOfferActivity.class);
			startActivity(i);
			finish();
		break;
		case R.id.rlInBox:
			buttonAnimation(rlInBox);
			i = new Intent(context, MessageBoard_ToDO_Activity.class);
			startActivity(i);
			break;
			
		case R.id.rlCases:
			buttonAnimation(rlCases);
			i = new Intent(context, OpenCaseActivity.class);
			startActivity(i);
			finish();
			break;
		case R.id.rlNeedFeedback:
			
			buttonAnimation(rlNeedFeedback);
			i = new Intent(context, LeaveFeedback_Activity.class);
			startActivity(i);
			finish();
			break;
		case R.id.rlPromotionListing:
		
			buttonAnimation(rlPromotionListing);
			i = new Intent(context, Promot_Listing_Activity.class);
			startActivity(i);
			break;
		case R.id.rlAddPaypalAccount:
			
			buttonAnimation(rlAddPaypalAccount);
			if(cd.checkConnection()){
				PayPaldialogCode();
				}else{
					Toast.makeText(context, "Interner connection is not available!",Toast.LENGTH_LONG).show();
				}
			break;
	case R.id.rlNewListing:
			buttonAnimation(rlNewListing);
			rlNewListing.setVisibility(View.GONE);
			
			  editor.putBoolean("SeeNewList", false);
			  editor.commit();
			if (cd.checkConnection()) {
				i = new Intent(context, ListingActivity.class);
				i.putExtra("categoryid", "");
				i.putExtra("ListingType","Listings");
				startActivity(i);
				} else {
					Custom_Dialog.dialogCode(2, null,"Internet connection is not available!",context);
				}
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings("static-access")
	public void PayPaldialogCode() {

	 dialogpaypal = new Dialog(context);
		dialogpaypal.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		dialogpaypal.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogpaypal.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogpaypal.setContentView(R.layout.dialog_paypal);
		dialogpaypal.setCancelable(false);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialogpaypal.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialogpaypal.getWindow().setAttributes(lp);

		final EditText evPayFName, evPayLName, evPaypalEmail;
		
		ImageView ivClose = (ImageView) dialogpaypal.findViewById(R.id.ivClose);
		ImageView ivCont = (ImageView) dialogpaypal.findViewById(R.id.ivCont);
		evPayFName = (EditText) dialogpaypal.findViewById(R.id.evPayFName);
		evPayLName = (EditText) dialogpaypal.findViewById(R.id.evPayLName);
		evPaypalEmail = (EditText) dialogpaypal.findViewById(R.id.evPaypalEmail);

		
		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						evPaypalEmail.getWindowToken(), 0);
				dialogpaypal.dismiss();
				
			}
		});
		ivCont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				

				Fname_PayPal = evPayFName.getText().toString().trim();
				lName_PayPal = evPayLName.getText().toString().trim();
				PaypalEmail = evPaypalEmail.getText().toString().trim();
			
				if (Fname_PayPal.length() == 0) {
					msg = "Please enter First Name";
					Custom_Dialog.dialogCode(2, null, msg, context);
					evPayFName.requestFocus();

				} else if (lName_PayPal.length() == 0) {

					msg = "Please enter Last Name";
					Custom_Dialog.dialogCode(2, null, msg, context);
					evPayLName.requestFocus();

				} else if (PaypalEmail.length() == 0) {

					msg = "Please enter PayPal Email";
					Custom_Dialog.dialogCode(2, null, msg, context);
					evPaypalEmail.requestFocus();
				} else if (!PaypalEmail.matches(emailPattern)) {

					msg = "Please enter Valid PayPal Email";
					Custom_Dialog.dialogCode(2, null, msg, context);
					evPaypalEmail.requestFocus();
				} else {
					if (cd.checkConnection()) {

						Log.e("Fname_PayPal", " "+Fname_PayPal);
						Log.e("lName_PayPal", " "+lName_PayPal);
						Log.e("PaypalEmail", " "+PaypalEmail);
						Log.e("userid", " "+userid);
						
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								evPaypalEmail.getWindowToken(), 0);
						
						
						
						new AddPayPalInfoBackTask().execute("");
					} else {
						Toast.makeText(context, "internet_conn_failed",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		dialogpaypal.show();
	}


	private class AddPayPalInfoBackTask extends AsyncTask<String, Void, String> {
		String errorMessage, message = "";

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

			Log.e("Fname_PayPal", " "+Fname_PayPal);
			Log.e("lName_PayPal", " "+lName_PayPal);
			Log.e("PaypalEmail", " "+PaypalEmail);
			Log.e("userid", " "+userid);
			
			JSONObject json = userFunction.PayPalFunction(userid, PaypalEmail,
					Fname_PayPal, lName_PayPal);

			try {
				if (json!=null&&json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (res.equals(KEY_SUCCESS_STATUS)) {

						message = json.getString("message");
						 userFunction.VerifyFacebookFunction(userid, "P");
						errorMessage = "true";
					} else {
						
						message = json.getString("message");
						errorMessage = "false";
					}
				} else {
					errorMessage = "network";
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
				dialogpaypal.dismiss();
				editor.putString("hasPaypal", "Y");
				editor.commit();
				Custom_Dialog.dialogCode(2, null,message,context);
				rlAddPaypalAccount.setVisibility(View.GONE);
			} else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else {
				Custom_Dialog.dialogCode(2, null, message, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

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

				Log.e("userid", " " + userid);
				JSONObject json = userFunction.CheckMediaUploadFunction(userid);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							hasPaypal = json.getString("haspaypal");

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

				buttonAnimation(ivSell);
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
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else {
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


	
	
	private class TODOSBackTask extends
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
				JSONObject json = userFunction.TODOSFunction(userid);
				try {
					if (json!=null &&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONObject JCount = json.getJSONObject("Count");
							toship=JCount.getString("toship");
							meetups=JCount.getString("meetups");
							offers=JCount.getString("offers");
							inbox=JCount.getString("inbox");
							cases=JCount.getString("cases");
							needfeedback=JCount.getString("needfeedback");
							promote=JCount.getString("promote");
							mylisting=JCount.getString("mylisting");
							watchlist=JCount.getString("watchlist");
							myAccount=json.optString("profilecomplete");
							tapinspired=JCount.optString("tapinspired");
							newlist=JCount.optString("newlist");
							hasPaypal=JCount.optString("hasPaypal");
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
				Log.e("newlist", " "+newlist);
				tvShip.setText(""+toship);
				tvMeetUp.setText(""+meetups);
				tvNewOffer.setText(""+offers);
				tvInbox.setText(""+inbox);
				tvCases.setText(""+cases);
				tvNeedFeedBack.setText(""+needfeedback);
				tvPromotListing.setText(""+promote);
				tvMyAccount.setText(""+myAccount);
				tvTapInsp.setText(""+tapinspired);
//				tvNewListing.setText(""+newlist);
				if(toship.equals("0")){
					rlTOShip.setVisibility(View.GONE);
				} if(meetups.equals("0")){
					rlMeetUps.setVisibility(View.GONE);
				} if(offers.equals("0")){
					rlNewOffer.setVisibility(View.GONE);
				} if(inbox.equals("0")){
					rlInBox.setVisibility(View.GONE);
				} if(cases.equals("0")){
					rlCases.setVisibility(View.GONE);
				} if(needfeedback.equals("0")){
					rlNeedFeedback.setVisibility(View.GONE);
				} if(promote.equals("0")){
					rlPromotionListing.setVisibility(View.GONE);
				} if(myAccount.equals("100")){
					rlMyAccount.setVisibility(View.GONE);
				}
				if(!see_recomondation){
					rltapinspire.setVisibility(View.GONE);
				}else{
					rltapinspire.setVisibility(View.VISIBLE);
				}
				if(!see_newlist){
					rlNewListing.setVisibility(View.GONE);
				}else{
					rlNewListing.setVisibility(View.VISIBLE);
				}
				
				Log.e("hasPaypal", hasPaypal);
				if(hasPaypal.length()!=0 && hasPaypal.equals("Y")){
					rlAddPaypalAccount.setVisibility(View.GONE);
				}
			
			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		} else {
				Custom_Dialog.dialogCode(2, null,"Please try again later!",
						TODOS_Activity.this);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	private void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(TODOS_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
}
