package com.convertingoffers.tapnsell.Shop.checkout;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.ConnectionDetector;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class OneClickCheckout_Fragment extends Fragment implements
		OnClickListener {
	Adapter Countryadapter;
	LinearLayout llSpinnerCountry;
Custom_Dialog custom_dialog;
	TextView tvName, tvPurchasePrice, tvTransaction, tvShipping, tvTotal,
			tvSippingAdd, tvBillingAdd, tvPayment, tvMinute1, tvMinute,
			tvSecond1, tvSecond2,tvspinnerCountry;
	RelativeLayout rlConfirmBtn, rlBilling, rlShipping, rlPayment,rlWebView;
	ImageView ivItemImage;
	Boolean SameStatus=false;
	static boolean active = false;
	ArrayList<String> mcountry = new ArrayList<String>();
	String ItemId, userid, name, image, price, msg;
	Context context;
	ListView lvt;
	Dialog dialog_country;
	String  b_address_id = "", b_user_id = "", b_type = "",reserve="",
			b_first_name = "", b_last_name = "", b_mobile_no = "",
			b_address = "", b_appartment = "", b_joinaddress = "", b_city = "",
			b_state = "", b_country = "", b_zipcode = "", b_email = "",
			S_address_id = "", S_user_id = "", S_type = "", S_first_name = "",
			S_last_name = "", S_mobile_no = "", S_address = "",
			S_appartment = "", ComeFrom = "",
			S_joinaddress = "", S_city = "", orderid = "", S_state = "",
			S_country = "", S_zipcode = "", S_email = "", baddress = "",
			saddress = "", account = "",amount="",Link="",sid="";
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	RelativeLayout rlMain;
	Shipping_Fragment shipping_fragment;
	Billing_Fragment billing_fragment;
	
	Select_payment_fragment select_payment_fragment;
	Select_billing_address select_billing_address;
	// Base fragment
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	public Editor editor;
	public SharedPreferences pref;
	public ConnectionDetector cd;
	public UserFunctions userFunction;
	public ProgressDialog progressDialog;
	public String KEY_SUCCESS = "successfull", KEY_SUCCESS_STATUS = "true",
			strmsg = "", bid = "", Status_billing = "false";
	public View view;
	public TextView tvHeader;
	public ImageView ivMenu, ivBack,ivClose;
	boolean status_billing_onepage_check = false,
			status_shipping_onepage_check = false;
	OnTextviewTimerSet mSetTime;
	WebView WvPaypal;
	ProgressBar	progressBar;
	
	// Base fragment

	paymentStatusReceiver mReceiver;
	public interface OnTextviewTimerSet {
		public void SetTimeToTextView(TextView tvMinute, TextView tvMinute1,
				TextView tvSecond1, TextView tvSecond2);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.checkout_home1, container,false);
		context = getActivity().getApplicationContext();

		InitializeView();
		
		
	
		Log.e("=======", "=========");
		tvHeader.setText("Confirm Order"); 
		status_billing_onepage_check = pref.getBoolean("ComefromonePageCheck",
				false);
		MakeCountryArray();
		status_shipping_onepage_check = pref.getBoolean(
				"S_ComefromonePageCheck", false);

			if (cd.checkConnection()) {
				new AddressInfoBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed",
						Toast.LENGTH_LONG).show();
			}
		
		ItemId = pref.getString("CHKItemid", "");
		userid = pref.getString("UserID", "");
		name = pref.getString("CHKName", "");
		image = pref.getString("CHKImage", "");
		price = pref.getString("CHKPrice", "");
		bid = pref.getString("b_id_selected", "");
		sid = pref.getString("s_id_selected", "");
	
/*
		if (price.length() != 0) {

			int shipPrice = 0;
			shipPrice = Integer.parseInt(price) * 10 / 100;
			tvTransaction.setText("$ " + shipPrice);
			int total = 0;
			total = shipPrice + Integer.parseInt(price);
			tvTotal.setText(" " + total);
		}*/
		
		if(price.length()!=0){
			
			try {
				float f_price =Float.parseFloat(price);
				String p_price = String.format("%.2f", f_price);
				tvPurchasePrice.setText("$ "+p_price);
				
					float shipPrice =0;
					shipPrice= Float.parseFloat(price)*10/100;
					String s_price = String.format("%.2f", shipPrice);
					tvTransaction.setText("$ "+s_price);
					
					
					float total = 0;
					total=shipPrice+Float.parseFloat(price);
					String t_price = String.format("%.2f", total);
					tvTotal.setText(""+t_price);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			}else{
				
				tvTransaction.setText("0.0");
				tvPurchasePrice.setText("0.0");
				tvTotal.setText("0.0");
				
				Log.e("pricelength0", "pricelength0");
			}



		tvName.setText(name);
		tvName.setTag(ItemId);
	

/*		if (image != null) {
			try {
				imageLoader.DisplayImage(image, ivItemImage);
			} catch (Exception e) {
			}
		} else {
			ivItemImage.setImageResource(R.drawable.ic_launcher);
		}

		*/
		

		// Image display using lazy loading 

		iLoader_item.displayImage(image, ivItemImage, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				//holder.pbimage.setProgress(0);
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
//				holder.pbimage.setIndeterminate(true);
			}
		});
		
		// Image display using lazy loading
		
		
		rlConfirmBtn.setOnClickListener(this);
		rlBilling.setOnClickListener(this);
		rlShipping.setOnClickListener(this);
		rlPayment.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		ivClose.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		try {
			mSetTime = (OnTextviewTimerSet) activity;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

/*	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	 getActivity().finish();
	 MainCheckOutFragmentActivity activity = (MainCheckOutFragmentActivity) getActivity();
	 activity.cancelContdowwnTimer();
	}*/
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.getActivity().onKeyDown(keyCode, event);
	}
	private void InitializeView() {
		context=getActivity();
		
		iLoader_item.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.checkout_home_item_image_bg)
		.showImageForEmptyUri(R.drawable.checkout_home_item_image_bg)
		.showImageOnFail(R.drawable.checkout_home_item_image_bg)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		custom_dialog=new Custom_Dialog();
		shipping_fragment = new Shipping_Fragment();
		billing_fragment = new Billing_Fragment();
		select_payment_fragment = new Select_payment_fragment();
		select_billing_address = new Select_billing_address();

		
		rlMain = (RelativeLayout) view.findViewById(R.id.rlMain);
		ivItemImage = (ImageView) view.findViewById(R.id.ivItemImage);
		rlConfirmBtn = (RelativeLayout) view.findViewById(R.id.rlConfirmBtn);
		tvName = (TextView) view.findViewById(R.id.tvName);
		tvPurchasePrice = (TextView) view.findViewById(R.id.tvPurchasePrice);
		tvTransaction = (TextView) view.findViewById(R.id.tvTransaction);
		tvShipping = (TextView) view.findViewById(R.id.tvShipping);
		tvTotal = (TextView) view.findViewById(R.id.tvTotal);
		tvSippingAdd = (TextView) view.findViewById(R.id.tvSippingAdd);
		tvBillingAdd = (TextView) view.findViewById(R.id.tvBillingAdd);
		tvPayment = (TextView) view.findViewById(R.id.tvPayment);
		WvPaypal = (WebView) view.findViewById(R.id.WvPaypal);
		 progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
		 rlWebView= (RelativeLayout) view.findViewById(R.id.rlWebView);
		rlBilling = (RelativeLayout) view.findViewById(R.id.rlBilling);
		rlShipping = (RelativeLayout) view.findViewById(R.id.rlShipping);
		rlPayment = (RelativeLayout) view.findViewById(R.id.rlPayment);
		tvMinute = (TextView) view.findViewById(R.id.tvMinute);
		tvMinute1 = (TextView) view.findViewById(R.id.tvMinute1);
		tvSecond1 = (TextView) view.findViewById(R.id.tvSecond1);
		tvSecond2 = (TextView) view.findViewById(R.id.tvSecond2);

		// Base fragment Initialize
		ivMenu = (ImageView) view.findViewById(R.id.ivMenu);
		ivBack = (ImageView) view.findViewById(R.id.ivBack);
		ivClose = (ImageView) view.findViewById(R.id.ivClose);
		tvHeader = (TextView) view.findViewById(R.id.tvHeader);
		userFunction = new UserFunctions();
		cd = new ConnectionDetector(context);
		pref = PreferenceManager.getDefaultSharedPreferences(context);
		editor = pref.edit();
		// Base fragment Initialize
	}

	@Override
	public void onResume() {
		super.onResume();
		mSetTime.SetTimeToTextView(tvMinute, tvMinute1, tvSecond1, tvSecond2);
	}

	@Override
	public void onClick(View v) {
		
		MainCheckOutFragmentActivity activity;
		switch (v.getId()) {
		case R.id.rlConfirmBtn:
/*
			activity = (Shahil) getActivity();
			bundle = new Bundle();
			bundle.putString("orderid", orderid);
			activity.ReplaceFragmentLeftToRight(order_complete_fragment, bundle);*/
			
			
			amount=tvTotal.getText().toString();
			
			 if (cd.checkConnection()) {
				 new PlaceOrderBackTask().execute("");
			  } else {
				  Toast.makeText(context, "internet_conn_failed",
			  Toast.LENGTH_LONG).show();
			  }
			 
			break;
		case R.id.rlBilling:

		/*	editor.putBoolean("ComefromonePageCheck", true);
			editor.commit();

			bundle = new Bundle();
			bundle.putString("b_first_name", b_first_name);
			bundle.putString("b_last_name", b_last_name);
			bundle.putString("b_address", b_address);
			bundle.putString("b_appartment", b_appartment);
			bundle.putString("b_city", b_city);
			bundle.putString("b_state", b_state);
			bundle.putString("b_country", b_country);
			bundle.putString("b_zipcode", b_zipcode);
			bundle.putString("S_first_name", S_first_name);
			bundle.putString("S_last_name", S_last_name);
			bundle.putString("S_address", S_address);
			bundle.putString("S_appartment", S_appartment);
			bundle.putString("S_city", S_city);
			bundle.putString("S_state", S_state);
			bundle.putString("S_country", S_country);
			 bundle.putString("ComeFrom", "true");
			bundle.putString("S_zipcode", S_zipcode);
			bundle.putString("S_mobile_no", S_mobile_no);
			bundle.putString("S_email", S_email);
			billing_fragment.setArguments(bundle);
			activity = (MainCheckOutFragmentActivity) getActivity();
			activity.ReplaceFragmentLeftToRight(billing_fragment, bundle);
			*/
			
			DialogBillingFragment_code();
			
			
			
			break;

		case R.id.rlShipping:
//			editor.putBoolean("S_ComefromonePageCheck", true);
//			editor.commit();
//
//			Bundle b_shipping = new Bundle();
//			b_shipping.putString("b_first_name", b_first_name);
//			b_shipping.putString("b_last_name", b_last_name);
//			b_shipping.putString("b_address", b_address);
//			b_shipping.putString("b_appartment", b_appartment);
//			b_shipping.putString("b_city", b_city);
//			b_shipping.putString("b_state", b_state);
//			b_shipping.putString("b_country", b_country);
//			b_shipping.putString("b_zipcode", b_zipcode);
//			b_shipping.putString("S_first_name", S_first_name);
//			b_shipping.putString("S_last_name", S_last_name);
//			b_shipping.putString("S_address", S_address);
//			b_shipping.putString("S_appartment", S_appartment);
//			b_shipping.putString("S_city", S_city);
//			b_shipping.putString("ComeFrom", "true");
//			b_shipping.putString("S_state", S_state);
//			b_shipping.putString("S_country", S_country);
//			b_shipping.putString("S_zipcode", S_zipcode);
//			b_shipping.putString("S_mobile_no", S_mobile_no);
//			b_shipping.putString("S_email", S_email);
//			shipping_fragment.setArguments(b_shipping);
//
//			activity = (MainCheckOutFragmentActivity) getActivity();
//			activity.ReplaceFragmentLeftToRight(shipping_fragment, b_shipping);
			
			DialogShippingFragment_code();
			break;

		case R.id.rlPayment:

			editor.putBoolean("P_ComefromonePageCheck", true);
			editor.commit();
			activity = (MainCheckOutFragmentActivity) getActivity();
			Bundle b_Payment = new Bundle();
			b_Payment.putString("ComeFrom", ComeFrom);
			activity.ReplaceFragmentLeftToRight(select_payment_fragment, b_Payment);

			break;
		case R.id.ivClose:

			rlWebView.setVisibility(View.GONE);
			break;
			
		case R.id.ivBack:
			 activity = (MainCheckOutFragmentActivity) getActivity();
			if (ComeFrom.equals("true")) {
				getActivity().finish();
				activity.cancelContdowwnTimer();
			} else {
				activity.ReplaceFragmentRightToLeft();
			}

			break;

		default:
			break;
		}

	}

	@SuppressWarnings("static-access")
	public void DialogBillingFragment_code() {

		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.billing);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		final EditText evFName = (EditText) dialog.findViewById(R.id.evFName);
		final EditText evLName = (EditText) dialog.findViewById(R.id.evLName);
		final EditText evAPT = (EditText) dialog.findViewById(R.id.evAPT);
		final EditText evAddress = (EditText) dialog.findViewById(R.id.evAddress);
		final TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
		final EditText evCity = (EditText) dialog.findViewById(R.id.evCity);
		final EditText evState = (EditText) dialog.findViewById(R.id.evState);
		final EditText evZip = (EditText) dialog.findViewById(R.id.evZip);
		tvspinnerCountry=(TextView)dialog.findViewById(R.id.tvspinnerCountry);
		llSpinnerCountry=(LinearLayout)dialog.findViewById(R.id.llSpinnerCountry);
		ImageView ivNext= (ImageView) dialog.findViewById(R.id.ivNext);
		ImageView ivBack= (ImageView) dialog.findViewById(R.id.ivBack);
		evFName.setText(b_first_name);
		evLName.setText(b_last_name);
		evAPT.setText(b_appartment);
		evAddress.setText(b_address);
		tvspinnerCountry.setText(b_country);
		evCity.setText(b_city);
		evState.setText(b_state);
		evZip.setText(b_zipcode);
		tvHeader.setText("Update Billing Address");
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		llSpinnerCountry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				llSpinnerCountry.setClickable(false);
				llSpinnerCountry.setEnabled(false);
				SpinnerCountryDialog();
			}
		});
		ivNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				b_first_name=evFName.getText().toString().trim();
				b_last_name=evLName.getText().toString().trim();
				b_appartment=evAPT.getText().toString().trim();
				b_address=""+evAddress.getText().toString().trim();
				b_country=tvspinnerCountry.getText().toString().trim();
				b_city=evCity.getText().toString().trim();
				b_state=evState.getText().toString().trim();
				b_zipcode=evZip.getText().toString().trim();
				if(b_first_name.length()==0){
			    	Custom_Dialog.dialogCode(2, null, "Please enter first name.", context);
			    }else if(b_last_name.length()==0){
			    	Custom_Dialog.dialogCode(2, null, "Please enter last name.", context);
			    }else if(b_address.length()==0){
			    	Custom_Dialog.dialogCode(2, null, "Please enter address.", context);
			    }else if(b_city.length()==0){
			    	Custom_Dialog.dialogCode(2, null, "Please enter city.", context);
			    }else if(b_state.length()==0){
			    	Custom_Dialog.dialogCode(2, null, "Please enter state.", context);
			    }else if(b_country.length()==0 || b_country.equals("Select Country")){
			    	Custom_Dialog.dialogCode(2, null, "Please select country.", context);
			    }else if(b_zipcode.length()==0){
			    	Custom_Dialog.dialogCode(2, null, "Please enter zipcode.", context);
			    }else{
			    	dialog.dismiss();
//				String final_address= "";
//				final_address =b_city+b_state+b_country;
				/*if(final_address.length()!=0){
					final_address.replaceAll(null, "");	
				}*/
				
				tvBillingAdd.setText(b_city+" "+b_state+" "+b_country);
			    }
			}
		});
		
		
		dialog.show();
	}

	/**
	 * 
	 */
	@SuppressWarnings("static-access")
	public void DialogShippingFragment_code() {
		
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.shipping);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		final EditText evFName = (EditText) dialog.findViewById(R.id.evFName);
		final EditText evLName = (EditText) dialog.findViewById(R.id.evLName);
		final EditText evAPT = (EditText) dialog.findViewById(R.id.evAPT);
		final EditText evAddress = (EditText) dialog.findViewById(R.id.evAddress);
		final TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
		final EditText evMobile = (EditText) dialog.findViewById(R.id.evMobile);
		tvspinnerCountry=(TextView)dialog.findViewById(R.id.tvspinnerCountry);
		llSpinnerCountry=(LinearLayout)dialog.findViewById(R.id.llSpinnerCountry);
		final EditText evCity = (EditText) dialog.findViewById(R.id.evCity);
		final EditText evState = (EditText) dialog.findViewById(R.id.evState);
		final EditText evZip = (EditText) dialog.findViewById(R.id.evZip);
		final EditText evEmail = (EditText) dialog.findViewById(R.id.evEmail);
		RelativeLayout rlSame= (RelativeLayout) dialog.findViewById(R.id.rlSame);
		final ImageView ivSameIcon= (ImageView) dialog.findViewById(R.id.ivSameIcon);
		ImageView ivNext= (ImageView) dialog.findViewById(R.id.ivNext);
		ImageView ivBack= (ImageView) dialog.findViewById(R.id.ivBack);
		 ivSameIcon.setVisibility(View.INVISIBLE);
		evFName.setText(S_first_name);
		evLName.setText(S_last_name);
		evAPT.setText(S_appartment);
		evAddress.setText(S_address);
		tvspinnerCountry.setText(S_country);
		evCity.setText(S_city);
		evState.setText(S_state);
		evZip.setText(S_zipcode);
		evMobile.setText(S_mobile_no);
		evEmail.setText(S_email);
		tvHeader.setText("Update Shipping Address");
		
		llSpinnerCountry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				llSpinnerCountry.setClickable(false);
				llSpinnerCountry.setEnabled(false);
				SpinnerCountryDialog();
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		rlSame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				rlSameBtnCode();
			}

			private void rlSameBtnCode() {
				if(!SameStatus){
					SameStatus=true;
				 evFName.setText(b_first_name);
				 evLName.setText(b_last_name);
				 evAddress.setText(b_address);
				 evCity.setText(b_city);
				 evState.setText(b_state);
				 evZip.setText(b_zipcode);
				 ivSameIcon.setVisibility(View.VISIBLE);
				 tvspinnerCountry.setText(b_country);
				evAPT.setText(b_appartment);
				
				}else{
					SameStatus=false;
					evFName.setText(S_first_name);
					 evLName.setText(S_last_name);
					 evAddress.setText(S_address);
					 evAPT.setText(S_appartment);
					 evCity.setText(S_city);
					 evState.setText(S_state);
					 evZip.setText(S_zipcode);
					 tvspinnerCountry.setText("Select Country");
					 ivSameIcon.setVisibility(View.INVISIBLE);
				}
			}
		});
		ivNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				S_first_name=evFName.getText().toString().trim();
				S_last_name=evLName.getText().toString().trim();
				S_appartment=evAPT.getText().toString().trim();
				S_address=""+evAddress.getText().toString().trim();
				S_country=tvspinnerCountry.getText().toString().trim();
				S_city=evCity.getText().toString().trim();
				S_state=evState.getText().toString().trim();
				S_zipcode=evZip.getText().toString().trim();
				  Log.e("Shiping", "Shiping");
				    if(S_email.equals(null)|| S_email.equals("null@null.null")){
				    	S_email="";
					}
				    if(S_first_name.length()==0){
				    	Custom_Dialog.dialogCode(2, null, "Please enter Firstname.", context);
				    }else if(S_last_name.length()==0){
				    	Custom_Dialog.dialogCode(2, null, "Please enter Lastname.", context);
				    }else if(S_address.length()==0){
				    	Custom_Dialog.dialogCode(2, null, "Please enter Address.", context);
				    }else if(S_country.length()==0 || S_country.equals("Select Country")){
				    	Custom_Dialog.dialogCode(2, null, "Please select Country.", context);
				    }else if(S_mobile_no.length()==0){
				    	Custom_Dialog.dialogCode(2, null, "Please enter Mobile no.", context);
				    }else if(S_city.length()==0){
				    	Custom_Dialog.dialogCode(2, null, "Please enter City.", context);
				    }else if(S_state.length()==0){
				    	Custom_Dialog.dialogCode(2, null, "Please enter State.", context);
				    }else if(S_zipcode.length()==0){
				    	Custom_Dialog.dialogCode(2, null, "Please enter ZipCode.", context);
				    }else if(S_email.length()==0 ){
				    	Custom_Dialog.dialogCode(2, null, "Please enter EmailAddress.", context);
				    }else if(!S_email.matches(emailPattern)|| S_email.equals("null@null.com")) {
				    	Custom_Dialog.dialogCode(2, null, "Please enter valid EmailAddress.", context);
				    }else{
//				String final_address= "";
//				final_address =S_city+S_state+S_country;
//				if(final_address.length()!=0){
//					final_address.replaceAll(null, "");	
//				}
				tvSippingAdd.setText(S_city+" "+S_state+" "+S_country);
			    }
			}
		});
		
		
		dialog.show();
	}


	protected void disableHardwareAcc() {
		WvPaypal.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}
/*	public void onBackPressed() {
		
		MainCheckOutFragmentActivity activity;
		
		 activity = (MainCheckOutFragmentActivity) getActivity();
			if (ComeFrom.equals("true")) {
				getActivity().finish();
				activity.cancelContdowwnTimer();
			} else {
				activity.ReplaceFragmentRightToLeft();
			}
	}*/
	public class AddressInfoBackTask extends AsyncTask<String, Void, String> {
		String errorMessage="";

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{

				Log.e("userid", " " + userid);
				Log.e("ItemId", " " + ItemId);
				Log.e("bid", " " + bid);

				JSONObject json = userFunction.getAddressInfoFunction(userid,
						ItemId, bid,sid);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject jBilling = json.getJSONObject("Billing");

							b_address_id = jBilling.getString("address_id");
							b_user_id = jBilling.getString("user_id");
							b_type = jBilling.getString("type");
							b_first_name = jBilling.getString("first_name");
							b_last_name = jBilling.getString("last_name");
							b_mobile_no = jBilling.getString("mobile_no");
							b_address = jBilling.getString("address");
							b_appartment = jBilling.getString("appartment");
							b_joinaddress = jBilling.getString("joinaddress");
							b_city = jBilling.getString("city");
							b_state = jBilling.getString("state");
							b_country = jBilling.getString("country");
							b_zipcode = jBilling.getString("zipcode");
							b_email = jBilling.getString("email");
							JSONObject jShipping = json
									.optJSONObject("Shipping");
							if (jShipping != null) {
								S_address_id = jShipping
										.optString("address_id");
								S_user_id = jShipping.optString("user_id");
								S_type = jShipping.optString("type");
								S_first_name = jShipping
										.optString("first_name");
								S_last_name = jShipping.optString("last_name");
								S_mobile_no = jShipping.optString("mobile_no");
								S_address = jShipping.optString("address");
								S_appartment = jShipping
										.optString("appartment");
								S_joinaddress = jShipping
										.optString("joinaddress");
								S_city = jShipping.optString("city");
								S_state = jShipping.optString("state");
								S_country = jShipping.optString("country");
								S_zipcode = jShipping.optString("zipcode");
								S_email = jShipping.optString("email");
								baddress = json.optString("baddress");
								saddress = json.optString("saddress");
								account = json.optString("account");
							}
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
				tvSippingAdd.setText(S_address+" " + S_country+" "+S_zipcode);
				tvBillingAdd.setText(b_address +" "+b_country+" "+b_zipcode);
			} else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else {
				tvSippingAdd.setText("");
				tvBillingAdd.setText("");
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	public class PlaceOrderBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				Log.e("userid", " " + userid);
				Log.e("ItemId", " " + ItemId);
				Log.e("b_first_name", " " + b_first_name);
				Log.e("b_last_name", " " + b_last_name);
				Log.e("b_mobile_no", " " + b_mobile_no);
				Log.e("b_appartment", " " + b_appartment);
				Log.e("b_address", " " + b_address);
				Log.e("b_city", " " + b_city);
				Log.e("b_state", " " + b_state);
				Log.e("b_country", " " + b_country);
				Log.e("b_zipcode", " " + b_zipcode);
				Log.e("b_email", " " + b_email);
				Log.e("S_first_name", " " + S_first_name);
				Log.e("S_last_name", " " + S_last_name);
				Log.e("S_mobile_no", " " + S_mobile_no);
				Log.e("S_appartment", " " + S_appartment);
				Log.e("S_address", " " + S_address);
				Log.e("S_city", " " + S_city);
				Log.e("S_state", " " + S_state);
				Log.e("S_country", " " + S_country);
				Log.e("S_zipcode", " " + S_zipcode);
				Log.e("S_email", " " + S_email);
				Log.e("bid", " " + bid);
				Log.e("bid", " " + bid);
				Log.e("amount", " " + amount);

				//float cent_ammount = Float.parseFloat(amount);
//				cent_ammount = cent_ammount*100;
				
				
				JSONObject json = userFunction.PlaceOrderFunction(ItemId,
						userid, "",amount, b_first_name,
						b_last_name, b_mobile_no, b_appartment, b_address,
						b_city, b_state, b_country, b_zipcode, b_email,
						S_first_name, S_last_name, S_mobile_no, S_appartment,
						S_address, S_city, S_state, S_country, S_zipcode,
						S_email, bid, S_address_id);
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							Link = json.getString("url");
							
							
							orderid = json.getString("id");

							errorMessage = "true";

						} else {
							msg = json.getString("message");
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

		@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" }) @Override
		protected void onPostExecute(String result) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {
				   
				   
				   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
					   disableHardwareAcc();
				   
				   
				   
				rlWebView.setVisibility(View.VISIBLE);
				
				WvPaypal.getSettings().setJavaScriptEnabled(true);
				WvPaypal.setWebViewClient(new MyWebViewClient());
				WvPaypal.loadUrl(Link);
				WvPaypal.addJavascriptInterface(new JavaScriptInterface(), "HtmlViewer");
				
			
			} else if (result.equals("false")) {
				Custom_Dialog.dialogCode(2, null, msg, context);
			} else {
				Custom_Dialog.dialogCode(2, null, "error in posting", context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	class JavaScriptInterface {
		public void showHTML(String html) {
			if (html.contains("userid")) {
				// Log.i("My Source", html);
				try {
					Document doc = Jsoup.parse(html);
					Elements ele = doc.select("div[class=mbg]");
					Elements span = ele.select("span[class=mbg-l]");
					Elements mbgfb = span.select("a[class=mbg-fb]");
					String useridurl = mbgfb.attr("href");
					Log.e("Element",
							useridurl.substring(useridurl.indexOf("=") + 1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class MyWebViewClient extends WebViewClient {

		String LOG_TAG = "Login";

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(LOG_TAG, "onPageStarted " + url);
			/*
			 * if(!url.contains("errmsg")){ isUrlMyEbay = true; }
			 */
			// m_tv.setText("Loading page...");
			// stop button is enabled only when pages are loading
			// m_bButStop.setEnabled(true);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.d(LOG_TAG, "onPageFinished " + url);
			progressBar.setVisibility(View.GONE);
			
			}
	}
	
	private class LockItemBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(getActivity());
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
				reserve="N";
				JSONObject json = userFunction.ReserveFunction(userid,ItemId, reserve);
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							
							
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
				
			
				
			} else {
				
				Custom_Dialog.dialogCode(2, null, message, context);
				
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	@Override
	public void onStart() {
		
		super.onStart();
		mReceiver=new paymentStatusReceiver();
		getActivity().registerReceiver(mReceiver, new IntentFilter("com.tapnsell.paymentcompleted"));
	}
	
	
	@Override
	public void onDestroy() {
		
		getActivity().unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	class paymentStatusReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			rlWebView.setVisibility(View.GONE);
			
			rlBilling.setClickable(false);
			rlShipping.setClickable(false);
			rlBilling.setEnabled(false);
			rlShipping.setEnabled(false);
			rlConfirmBtn.setClickable(false);
			rlConfirmBtn.setEnabled(false);
			rlBilling.setClickable(true);
			rlShipping.setClickable(true);
			rlBilling.setEnabled(true);
			rlShipping.setEnabled(true);
			rlConfirmBtn.setClickable(true);
			rlConfirmBtn.setEnabled(true);
			
			 MainCheckOutFragmentActivity activity = (MainCheckOutFragmentActivity) getActivity();
			 activity.cancelContdowwnTimer();
			 
			 
			Intent	i_Buy = new Intent(getActivity(),OrderCompletedActivity.class);
			i_Buy.putExtra("itemid", activity.ItemId);
			i_Buy.putExtra("from_id", activity.from_id);
			i_Buy.putExtra("Distance", "0");
			i_Buy.putExtra("ItemArray",activity.mItemList);
			i_Buy.putExtra("position", ""+activity.position);
			i_Buy.putExtra("orderid", orderid);
			startActivity(i_Buy);
			getActivity().finish();
			
		/*	if(cd.checkConnection()){
				new LockItemBackTask().execute("");
			}else{
				Toast.makeText(getActivity(), "internet_conn_failed",Toast.LENGTH_LONG).show();
			}*/
		}
	}

	private void MakeCountryArray() {

		mcountry.add("Abkhazia");mcountry.add("Afghanistan");mcountry.add("Aland");mcountry.add("Albania");mcountry.add("Algeria");mcountry.add("American Samoa");mcountry.add("Andorra");mcountry.add("Angola");mcountry.add("Anguilla");mcountry.add("Antarctica");mcountry.add("Antigua & Barbuda");mcountry.add("Argentina");mcountry.add("Armenia");mcountry.add("Aruba");mcountry.add("Australia");mcountry.add("Austria");mcountry.add("Azerbaijan");mcountry.add("Bahamas");mcountry.add("Bahrain");mcountry.add("Bangladesh");mcountry.add("Barbados");mcountry.add("Belarus");mcountry.add("Belgium");mcountry.add("Belize");mcountry.add("Benin");mcountry.add("Bermuda");mcountry.add("Bhutan");mcountry.add("Bolivia");mcountry.add("Bosnia & Herzegovina");mcountry.add("Botswana");mcountry.add("Brazil");mcountry.add("British Antarctic Territory");mcountry.add("British Virgin Islands");mcountry.add("Brunei");mcountry.add("Bulgaria");mcountry.add("Burkina Faso");mcountry.add("Burundi");mcountry.add("Cambodia");mcountry.add("Cameroon");mcountry.add("Canada");mcountry.add("Cape Verde");mcountry.add("Cayman Islands");mcountry.add("Central African Republic");mcountry.add("Chad");mcountry.add("Chile");mcountry.add("China");mcountry.add("Christmas Island");mcountry.add("Cocos Keeling Islands");mcountry.add("Colombia");mcountry.add("Commonwealth");mcountry.add("Comoros");mcountry.add("Cook Islands");
		mcountry.add("Costa Rica");mcountry.add("Cote d'Ivoire");mcountry.add("Croatia");mcountry.add("Cuba");mcountry.add("Cyprus");mcountry.add("Czech Republic");mcountry.add("Democratic Republic of the Congo");mcountry.add("Denmark");mcountry.add("Djibouti");mcountry.add("Dominica");mcountry.add("Dominican Republic");mcountry.add("East Timor");mcountry.add("Ecuador");mcountry.add("Egypt");mcountry.add("El Salvador");mcountry.add("England");mcountry.add("Equatorial Guinea");mcountry.add("Eritrea");mcountry.add("Estonia");mcountry.add("Ethiopia");mcountry.add("European Union");mcountry.add("Falkland Islands");mcountry.add("Faroes");mcountry.add("Fiji");mcountry.add("Finland");mcountry.add("France");mcountry.add("Gabon");mcountry.add("Gambia");mcountry.add("Georgia");mcountry.add("Germany");mcountry.add("Ghana");mcountry.add("Gibraltar");mcountry.add("GoSquared");mcountry.add("Greece");mcountry.add("Greenland");mcountry.add("Grenada");mcountry.add("Guam");mcountry.add("Guatemala");mcountry.add("Guernsey");mcountry.add("Guinea Bissau");mcountry.add("Guinea");mcountry.add("Guyana");
		mcountry.add("Haiti");mcountry.add("Honduras");mcountry.add("Hong Kong");mcountry.add("Hungary");mcountry.add("Iceland");mcountry.add("India");mcountry.add("Indonesia");mcountry.add("Iran");mcountry.add("Iraq");mcountry.add("Ireland");mcountry.add("Isle of Man");mcountry.add("Israel");mcountry.add("Italy");mcountry.add("Jamaica");mcountry.add("Japan");mcountry.add("Jersey");
		mcountry.add("Jordan");mcountry.add("Kazakhstan");mcountry.add("Kenya");mcountry.add("Kiribati");mcountry.add("Kosovo");mcountry.add("Kuwait");mcountry.add("Kyrgyzstan");mcountry.add("Laos");mcountry.add("Latvia");mcountry.add("Lebanon");mcountry.add("Lesotho");mcountry.add("Liberia");mcountry.add("Libya");mcountry.add("Liechtenstein");mcountry.add("Lithuania");mcountry.add("Luxembourg");
		mcountry.add("Macau");mcountry.add("Macedonia");mcountry.add("Madagascar");mcountry.add("Malawi");mcountry.add("Malaysia");mcountry.add("Maldives");mcountry.add("Mali");mcountry.add("Malta");mcountry.add("Mars");mcountry.add("Marshall Islands");mcountry.add("Mauritania");mcountry.add("Mauritius");mcountry.add("Mayotte");mcountry.add("Mexico");mcountry.add("Micronesia");mcountry.add("Moldova");
		mcountry.add("Monaco");mcountry.add("Mongolia");mcountry.add("Montenegro");mcountry.add("Montserrat");mcountry.add("Morocco");mcountry.add("Mozambique");mcountry.add("Myanmar");mcountry.add("Nagorno Karabakh");mcountry.add("Namibia");mcountry.add("NATO");mcountry.add("Nauru");mcountry.add("Nepal");mcountry.add("Netherlands Antilles");mcountry.add("Netherlands");mcountry.add("New Caledonia");
		mcountry.add("New Zealand");mcountry.add("Nicaragua");mcountry.add("Niger");mcountry.add("Nigeria");mcountry.add("Niue");mcountry.add("Norfolk Island");mcountry.add("North Korea");mcountry.add("Northern Cyprus");mcountry.add("Northern Mariana Islands");mcountry.add("Norway");mcountry.add("Olympics");mcountry.add("Oman");mcountry.add("Pakistan");mcountry.add("Palau");mcountry.add("Palestine");mcountry.add("Panama");
		mcountry.add("Papua New Guinea");mcountry.add("Paraguay");mcountry.add("Peru");mcountry.add("Philippines");mcountry.add("Pitcairn Islands");mcountry.add("Poland");mcountry.add("Portugal");mcountry.add("Puerto Rico");mcountry.add("Qatar");mcountry.add("Red Cross");mcountry.add("Republic of the Congo");mcountry.add("Romania");mcountry.add("Russia");mcountry.add("Rwanda");mcountry.add("Saint Barthelemy");mcountry.add("Saint Helena");
		mcountry.add("Saint Kitts & Nevis");mcountry.add("Saint Lucia");mcountry.add("Saint Vincent & the Grenadines");mcountry.add("Samoa");mcountry.add("San Marino");mcountry.add("Sao Tome & Principe");mcountry.add("Saudi Arabia");mcountry.add("Scotland");mcountry.add("Senegal");mcountry.add("Serbia");mcountry.add("Seychelles");mcountry.add("Sierra Leone");mcountry.add("Singapore");mcountry.add("Slovakia");mcountry.add("Slovenia");mcountry.add("Solomon Islands");
		mcountry.add("Somalia");mcountry.add("Somaliland");mcountry.add("South Africa");mcountry.add("South Georgia & the South Sandwich Islands");mcountry.add("South Korea");mcountry.add("South Ossetia");mcountry.add("South Sudan");mcountry.add("Spain");mcountry.add("Sri Lanka");mcountry.add("Sudan");mcountry.add("Suriname");mcountry.add("Swaziland");mcountry.add("Sweden");mcountry.add("Switzerland");mcountry.add("Syria");mcountry.add("Taiwan");
		mcountry.add("Tajikistan");mcountry.add("Tanzania");mcountry.add("Thailand");mcountry.add("Togo");mcountry.add("Tonga");mcountry.add("Trinidad & Tobago");mcountry.add("Tunisia");mcountry.add("Turkey");mcountry.add("Turkmenistan");mcountry.add("Turks & Caicos Islands");mcountry.add("Tuvalu");mcountry.add("Uganda");mcountry.add("Ukraine");mcountry.add("United Arab Emirates");mcountry.add("United Kingdom");mcountry.add("United Nations");
		mcountry.add("United States");mcountry.add("Uruguay");mcountry.add("US Virgin Islands");mcountry.add("Uzbekistan");mcountry.add("Vanuatu");mcountry.add("Vatican City");mcountry.add("Venezuela");mcountry.add("Vietnam");mcountry.add("Wales");mcountry.add("Western Sahara");mcountry.add("Yemen");mcountry.add("Zambia");mcountry.add("Zimbabwe");
	
	}
	public void SpinnerCountryDialog()

	{
		dialog_country = new Dialog(getActivity());
		dialog_country.setContentView(R.layout.itemdetails_listview);
		dialog_country.setTitle("Select Country");
		dialog_country.show();
		dialog_country.setCanceledOnTouchOutside(true);
		dialog_country.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				llSpinnerCountry.setClickable(true);
				llSpinnerCountry.setEnabled(true);
			}
		});
		lvt = (ListView) dialog_country.findViewById(R.id.lvDialog);
		Countryadapter = new Adapter(context, mcountry);
		lvt.setAdapter(Countryadapter);
		lvClickEvent();

	}

	private void lvClickEvent() {
		
		lvt.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				llSpinnerCountry.setClickable(true);
				llSpinnerCountry.setEnabled(true);
					String Country = mcountry.get(position);
					Log.e("condition", "" + Country);
					tvspinnerCountry.setText(Country);
					dialog_country.dismiss();
			}
		});
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
			TextView tvSpinner = (TextView) rowView.findViewById(R.id.tvSpinner);
			tvSpinner.setText(val.get(position));
			System.out.println(val.get(position));
			return rowView;
		}
	}

	
	
}
