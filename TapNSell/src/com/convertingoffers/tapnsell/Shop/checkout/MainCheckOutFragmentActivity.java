package com.convertingoffers.tapnsell.Shop.checkout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.ConnectionDetector;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;

public class MainCheckOutFragmentActivity extends FragmentActivity implements Checkout_home_Fragment.OnTextviewTimerSet,OneClickCheckout_Fragment.OnTextviewTimerSet{

	Context context;
	FrameLayout container;
	FragmentManager myFragmentManager;
	Shipping_Fragment shipping_fragment;
	Checkout_home_Fragment checkout_home_fragment;
	OneClickCheckout_Fragment onepage_checkout;
	Select_billing_address addcred;
	
	/////////////////
	public boolean active = false,boolTimerSet=false,blink;
	TextView tvMin,tvMin1,tvSec1,tvSec2; 
	long seconds;
	private CountDownTimer countDownTimer;
	private long totalTimeCountInMilliseconds;
	long MainTimeInMilliSecond, CurrentTimeInMilliSecond,
	finalTimeInMilliSecond;
	private long timeBlinkInMilliseconds;
	String ItemId,userid,reserve="N",from_id="",address="";
	ArrayList<String> mItemList = new ArrayList<String>();
	
	
	// Base fragment 
	
	public Editor editor;
	public SharedPreferences pref;
	public ConnectionDetector cd;
	public UserFunctions userFunction;
	public ProgressDialog progressDialog;
	public String KEY_SUCCESS = "successfull",KEY_SUCCESS_STATUS = "true",strmsg="",position="";
	public TextView tvHeader;
	public ImageView ivMenu,ivBack;
	
	// Base fragment
	
	@SuppressWarnings({ "unchecked"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_out_main);
		context=this;
		container = (FrameLayout)findViewById(R.id.maincontainer);
	
		
		setTimer("04:60");
		startTimer();
		ItemId=pref.getString("CHKItemid", "");
		userid=pref.getString("UserID", "");
		
		Bundle b = getIntent().getExtras();
		if(b!=null){
			address= b.getString("address");
			ItemId = b.getString("itemid");	
			from_id= b.getString("from_id");	
			mItemList= (ArrayList<String>) b.getSerializable("ItemArray");
			position=b.getString("position");	
		}
	
		if(savedInstanceState == null){
			if(address.equals("Y")){
				FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
				fragmentTransaction.add(R.id.maincontainer, addcred, "");
				fragmentTransaction.addToBackStack(null); 
				fragmentTransaction.commit();
				
			}else{
				FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
				fragmentTransaction.add(R.id.maincontainer, checkout_home_fragment, "");
				fragmentTransaction.addToBackStack(null); 
				fragmentTransaction.commit();
			}
		}
	}
	@Override
	public void onStart() {
		super.onStart();
		active = true;
	}
	@Override
	public void onStop() {
		super.onStop();
		active = false;
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void ReplaceFragmentLeftToRight(Fragment  fragment,Bundle bundle) {
		
		myFragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		fragment.setArguments(bundle);
		fragmentTransaction.replace(R.id.maincontainer, fragment);
		fragmentTransaction.addToBackStack(null); 
		fragmentTransaction.commit();
		
	}
	public void ReplaceFragmentRightToLeft() {/*
		
		myFragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
//		fragmentTransaction.setCustomAnimations(R.anim.slide_out_right, R.anim.slide_left_right);
		fragmentTransaction.replace(R.id.maincontainer, fragment);
		fragmentTransaction.commit();
		
	*/
		
		/*FragmentManager fm = getActivity().getSupportFragmentManager();
		for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {    
		    fm.popBackStack();
		}
		myFragmentManager = getSupportFragmentManager();
		myFragmentManager.popBackStack();*/
		
		myFragmentManager = getSupportFragmentManager();
		myFragmentManager.popBackStack();
		
		
	}
	
	

	/*@SuppressLint("SimpleDateFormat")
	private void setTimerForDialog() {
		String strTime2 = "00:15";
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		Date date;
		try {
			date = sdf.parse(strTime2);

			Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendarinstance
			calendar.setTime(date); // assigns calendar to given date

			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);

			int timeinsecond = 60 * minute;
			timeinsecond = timeinsecond + second;
			totalTimeCountInMilliseconds = timeinsecond * 1000;
			timeBlinkInMilliseconds = 30 * 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	private void startTimer() {
		countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
			// 500 means, onTick function will be called at every 500
			// milliseconds
		
			@SuppressLint("DefaultLocale")
			@Override
			public void onTick(long leftTimeInMilliseconds) {
				
				
				seconds = leftTimeInMilliseconds / 1000;
//				Log.e("seconds", " "+seconds);
				if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
					if(tvMin!=null){
						tvMin.setTextAppearance(getApplicationContext(),R.style.blinkText);	
					}
					if(tvMin1!=null){
						tvMin1.setTextAppearance(getApplicationContext(),R.style.blinkText);	
					}
					if(tvSec1!=null){
						tvSec1.setTextAppearance(getApplicationContext(),R.style.blinkText);
					}
					if(tvSec2!=null){
						tvSec2.setTextAppearance(getApplicationContext(),R.style.blinkText);	
					}
					
					
					if (blink) {
						
						if(tvMin!=null){
							tvMin.setVisibility(View.VISIBLE);
						}
						if(tvMin1!=null){
							tvMin1.setVisibility(View.VISIBLE);
						}
						if(tvSec1!=null){
							tvSec1.setVisibility(View.VISIBLE);
						}
						if(tvSec2!=null){
							tvSec2.setVisibility(View.VISIBLE);	
						}
						
						// if blink is true, textview will be visible
					} else {
						
						if(tvMin!=null){
							tvMin.setText("0");	
						}
						if(tvMin1!=null){
							tvMin1.setText("0");	
						}
						if(tvSec1!=null){
							tvSec1.setText("0");	
						}
						if(tvSec2!=null){
							tvSec2.setText("0");	
						}
					}

					blink = !blink; // toggle the value of blink
				}

				
				long min = seconds / 60;
				String sec =String.format("%02d", seconds % 60);
				String sec2 = sec.substring(Math.max(sec.length() - 1, 0));
//			String sec1 = sec.charAt(0);
				String sec1 = sec.substring(0, 1);
				if(tvMin1!=null){
					tvMin1.setText(String.format("%01d", min));
				}
				if(tvSec1!=null){
					tvSec1.setText(sec1);
				}
				
				if(tvSec2!=null){
					tvSec2.setText(sec2);	
				}

			}

			@Override
			public void onFinish() {
				if(tvMin!=null){
					tvMin.setText("0");	
				}
				if(tvMin1!=null){
					tvMin1.setText("0");	
				}
				if(tvSec1!=null){
					tvSec1.setText("0");	
				}
				if(tvSec2!=null){
				tvSec2.setText("0");
				}
			/*	if (countDownTimer != null) {
					countDownTimer.cancel();
				}
*/
				
				cancelContdowwnTimer();
				
				if(cd.checkConnection()){
					new LockItemBackTask().execute("");
				}else{
					Toast.makeText(MainCheckOutFragmentActivity.this, "internet_conn_failed",Toast.LENGTH_LONG).show();
				}
			}

			
		}.start();
	}
	public void cancelContdowwnTimer() {
		

		if (active) {
			if (countDownTimer != null) {
				countDownTimer.cancel();
			}
		} 
	}
	private class LockItemBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MainCheckOutFragmentActivity.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				
				Log.e("userid", " "+userid);
				Log.e("itemid", " "+ItemId);
				Log.e("reserve", " "+reserve);
				
				JSONObject json = userFunction.ReserveFunction(userid,ItemId, reserve);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							message=json.optString("message");
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
				finish();
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else {
				Custom_Dialog.dialogCode(2, null,message, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


	@SuppressLint("SimpleDateFormat") private void setTimer(String strTime2) {

		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		Date date;
		try {
			date = sdf.parse(strTime2);

			Calendar calendar = GregorianCalendar.getInstance(); // creates a
																	// new
																	// calendar
																	// instance
			calendar.setTime(date); // assigns calendar to given date

			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);

			int timeinsecond = 60 * minute;
			timeinsecond = timeinsecond + second;
			totalTimeCountInMilliseconds = timeinsecond * 1000;
			timeBlinkInMilliseconds = 30 * 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/*@Override
	public void onStop() {
		super.onStop();
		active = false;
	}*/

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		
		// Base fragment Initialize 
	
        userFunction = new UserFunctions();
    	cd = new ConnectionDetector(MainCheckOutFragmentActivity.this);
    	pref =PreferenceManager.getDefaultSharedPreferences(MainCheckOutFragmentActivity.this);
    	editor = pref.edit();
    	
    	myFragmentManager = getSupportFragmentManager();
		addcred=new Select_billing_address();
		onepage_checkout=new OneClickCheckout_Fragment();
		checkout_home_fragment=new Checkout_home_Fragment();
		shipping_fragment= new Shipping_Fragment();
    	// Base fragment Initialize
	}

	@Override
	public void SetTimeToTextView(TextView tvMinute, TextView tvMinute1,
			TextView tvSecond1, TextView tvSecond2) {
		
		tvMin=tvMinute;
		tvMin1=tvMinute1;
		tvSec1=tvSecond1;
		tvSec2=tvSecond2;
	}
}
