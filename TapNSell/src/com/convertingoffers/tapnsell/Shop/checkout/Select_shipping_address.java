package com.convertingoffers.tapnsell.Shop.checkout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.Modal.BillingModal;
import com.convertingoffers.tapnsell.util.ConnectionDetector;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;

public class Select_shipping_address extends Fragment  implements OnClickListener{
	Adapter Countryadapter;
	LinearLayout llSpinnerCountry;
	Dialog dialog_country;
	Context context;
	View view;
	String S_mobile_no="",S_email="",S_first_name="",S_last_name="",S_appartment="",S_address="",S_country="",S_city="",S_state="",S_zipcode="";
	Boolean SameStatus=false;
	String s_id,b_first_name="",b_appartment="",b_last_name="",b_address="",b_country="",b_city="",b_state="",b_zipcode="";
	OneClickCheckout_Fragment one_click_checkout;
	Shipping_Fragment shipping_fragment;
	ArrayList<String> mcountry = new ArrayList<String>();
	ArrayList<BillingModal> mShippingAddress = new ArrayList<BillingModal>();
	RelativeLayout rlAddNewAddress;
	EditText evCardNo,evCvv,evName;
	TextView tvspinnerDate,tvspinnerYear,tvspinnerCountry;
	TableLayout tblAddress;
	ImageView ivNext,iv1;
	LinearLayout llSpinnerDayMonth,llSpinnerYear;
	ArrayList<String> mMonthList = new ArrayList<String>();
	ArrayList<String> mYearList = new ArrayList<String>();
	String cardno="",cvvNo="",name="",month="",year="",address="",userid="",ItemId;
	String itemid,address_id="",user_id="",type="",first_name="",last_name="",mobile_no="",Billingaddress="",
	appartment="",joinaddress="",city="",state="",country="",zip,zipcode="",email="",APILink="";
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	String New_email="",Status_NewEmail="false";
	Dialog dialog;
	ListView lvt;
	Adapter adapter;
	boolean MonthYearstatus;
	Billing_Fragment billing_fragment;
	int Billing_Selected_pos=0;
	ArrayList<Boolean> mButtonClick = new ArrayList<Boolean>();
	Custom_Dialog custom_dialog;
	boolean status_Payment_onepage_check=false;
	// Base fragment 
	
	public Editor editor;
	public SharedPreferences pref;
	public ConnectionDetector cd;
	public UserFunctions userFunction;
	public ProgressDialog progressDialog;
	public String KEY_SUCCESS = "successfull",KEY_SUCCESS_STATUS = "true",strmsg="",bid;
	public TextView tvHeader;
	public ImageView ivMenu,ivBack;
	
	double lat=0.0, log=0.0,Distance=0.0,lat1,lat2,lng1,lng2,Dis=0.0;
	protected LocationManager locationManager;
	String strlatitude,strlongitude;
	Location location;
	boolean isCurrentLocation=false,Successfull, isGPSEnabled = false,isNetworkEnabled = false, canGetLocation = false;

	
	ProgressDialog p_dialog;
	//Card card;
	String token_id, TAG="AddPaymentMethodActivity";
	ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
//	private int day,prevMnth;
	private int selectmonth=1, selectyear=0;
	 Calendar calendar ;
	//Stripe
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.creditcard_info, container,false);
		
	
		InitializeView();
		tvHeader.setText("Select Shipping Address");
		
		userid=pref.getString("UserID", "");
		ItemId = pref.getString("CHKItemid", "");
		bid = pref.getString("b_id_selected", "");
		first_name= pref.getString("reg_fname", "");
		last_name= pref.getString("reg_lname", ""); 
		evName.setText(first_name+last_name);
		MakeCountryArray();
			
		iv1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.add_shipping_text));
		ivBack.setOnClickListener(this);
		ivNext.setOnClickListener(this);
		llSpinnerYear.setOnClickListener(this);
		llSpinnerDayMonth.setOnClickListener(this);
		rlAddNewAddress.setOnClickListener(this);
		itemid=pref.getString("CHKItemid", "");
		if(cd.checkConnection()){
			new  ShippingAddressBackTask().execute("");
		}else{
			Toast.makeText(getActivity(), "internet_conn_failed",Toast.LENGTH_LONG).show();
		}
	
		return view;
	}

	
	

	private void InitializeView() {
		custom_dialog = new Custom_Dialog();
		context=getActivity();
		
		iv1=(ImageView)view.findViewById(R.id.iv1);
		shipping_fragment=new Shipping_Fragment();
		billing_fragment=new Billing_Fragment();
		one_click_checkout =new OneClickCheckout_Fragment();
		evCardNo=(EditText)view.findViewById(R.id.evCardNo);
		evCvv=(EditText)view.findViewById(R.id.evCvv);
		evName=(EditText)view.findViewById(R.id.evName);
		tvspinnerDate=(TextView)view.findViewById(R.id.tvspinnerDate);
		tvspinnerYear=(TextView)view.findViewById(R.id.tvspinnerYear);
		tblAddress=(TableLayout)view.findViewById(R.id.tblAddress);
		ivNext=(ImageView)view.findViewById(R.id.ivNext);
		llSpinnerDayMonth=(LinearLayout)view.findViewById(R.id.llSpinnerDayMonth);
		llSpinnerYear=(LinearLayout)view.findViewById(R.id.llSpinnerYear);
		rlAddNewAddress=(RelativeLayout)view.findViewById(R.id.rlAddNewAddress);
		// Base fragment Initialize 
		ivMenu=(ImageView)view.findViewById(R.id.ivMenu);
		ivBack=(ImageView)view.findViewById(R.id.ivBack);
		tvHeader= (TextView) view.findViewById(R.id.tvHeader);
        userFunction = new UserFunctions();
    	cd = new ConnectionDetector(context);
    	pref =PreferenceManager.getDefaultSharedPreferences(context);
    	editor = pref.edit();
    	// Base fragment Initialize
	}
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.ivBack:
			
			if(cd.checkConnection()){
				new  LockItemBackTask().execute("");
			}else{
				Toast.makeText(getActivity(), "internet_conn_failed",Toast.LENGTH_LONG).show();
			}
		
			break;
		case R.id.rlAddNewAddress:
/*
			//Call 
			 activity = (MainCheckOutFragmentActivity) getActivity();
			editor.putBoolean("StatusBilling", true);
			editor.commit();
			activity.ReplaceFragmentLeftToRight(billing_fragment,null);*/
			
			
			
			DialogShippingFragment_code();
			
			
			break;
			
	
			
		case R.id.ivNext:
				
			boolean isVerify=true;
			Log.e("isVerify", ""+isVerify);
			String strCardName=evName.getText().toString().trim();
			String strCardNumber=evCardNo.getText().toString().trim();
			String strCardCVC=evCvv.getText().toString().trim();
//			String steEXPDate=etEXPDate.getText().toString().trim();
//			selectyear=Integer.parseInt(""+tvspinnerYear.getText().toString());
			Log.e("strCardName", " "+strCardName);
			Log.e("strCardNumber", " "+strCardNumber);
			Log.e("strCardCVC", " "+strCardCVC);
			Log.e("selectmonth", " "+selectmonth);
			Log.e("selectyear", " "+selectyear);
			String c_pref_card_no="";
			c_pref_card_no =pref.getString("User_credicard_no", "");
			
			Log.e("c_pref_card_no", ""+c_pref_card_no);
			Log.e("strCardNumber", ""+strCardNumber);
			
			
			BillingModal _list =mShippingAddress.get(Billing_Selected_pos);
			String s_id = _list.getAddress_id();
			editor.putString("s_id_selected", ""+s_id);
			Log.e("b_id", " "+s_id);
			editor.commit();
			
			MainCheckOutFragmentActivity activity1 = (MainCheckOutFragmentActivity) getActivity();
			activity1.ReplaceFragmentLeftToRight(one_click_checkout,null);
		/*
		 * 
		 * Temp comment Stripe Code      Temp comment Stripe Code      Temp comment Stripe Code      
		 * Temp comment Stripe Code      Temp comment Stripe Code      Temp comment Stripe Code      
		 * Temp comment Stripe Code      Temp comment Stripe Code      Temp comment Stripe Code      
		 * 
		 * 
		 * 	if(c_pref_card_no.equals(strCardNumber)){
				
				BillingModal _list =mBillingAddress.get(Billing_Selected_pos);
				String b_id = _list.getAddress_id();
				editor.putString("b_id_selected", ""+b_id);
				Log.e("b_id", " "+b_id);
				editor.commit();
				
				MainCheckOutFragmentActivity activity1 = (MainCheckOutFragmentActivity) getActivity();
				activity1.ReplaceFragmentLeftToRight(one_click_checkout,null);
        	
			}else{
				
				Log.e("in "," in");
			if(strCardNumber==null || strCardNumber.equals("")){
				isVerify=false;
//				ValidationSingleButton("Enter Valid Card Number");
				custom_dialog.dialogCode(2, null, "Enter Valid Card Number", context);
			}else if(strCardCVC==null || strCardCVC.equals("")){
				isVerify=false;
//				ValidationSingleButton("Enter Valid Card CVV Number");
				custom_dialog.dialogCode(2, null, "Enter Valid Card CVV Number", context);
			}else if(selectmonth==0){
				isVerify=false;
				custom_dialog.dialogCode(2, null, "Please select Valid month.", context);
//				ValidationSingleButton("Please select Valid month.");
			}else if(selectyear==0){
				isVerify=false;
				custom_dialog.dialogCode(2, null, "Please select Valid month.", context);
//				ValidationSingleButton("Please select Valid month.");
			}else{
				
			if(isVerify){
				 card = new Card(
						strCardNumber,
						selectmonth,	                 // Change Static EXP Month
						selectyear,                      // Change Static EXP Year
		                strCardCVC);
				 Log.e("in1 "," in1");
		        boolean validation = card.validateCard();
		        Log.e("validation "," "+validation );
		        if(validation){
		        	progressDialog = new ProgressDialog(getActivity());
					progressDialog.setMessage("Please wait");
					progressDialog.setCanceledOnTouchOutside(false);
					progressDialog.setCancelable(false);
					progressDialog.show();
		       	 Log.e("in2 "," in2");
		        	editor.putString("User_credicard_no", ""+strCardNumber);
		        	editor.commit();
		        	new Stripe().createToken(card,UserFunctions.Stripe_PUBLISHABLE_KEY,new TokenCallback() {
		                    public void onSuccess(Token token) {
		                    	
		                    	if(token.getId()!=null){
		                    		
		                    		
		                    		token_id=token.getId();
		                    		Log.e("token_id", " "+token_id);
		                    		
		                    	//if(card_id==null || card_id.equals("") ||customer_id==null || customer_id.equals("") )	{
		                    		if(cd.checkConnection()){
		                    			new CreateStripeAccountBackTask().execute("");
		                    		}else{
		                    			  Toast.makeText(getActivity(),"internet connection error",Toast.LENGTH_SHORT).show();
		                    		}
		                    }else{
		                    	if (progressDialog.isShowing()) {
	                				progressDialog.dismiss();
	                			}
		                    }
		               }
		                    
		                        
		                    public void onError(Exception error) {
		                            //handleError(error.getLocalizedMessage());
		                    	if (progressDialog.isShowing()) {
		            				progressDialog.dismiss();
		            			}
		                    	   Toast.makeText(getActivity(), error.toString(),Toast.LENGTH_SHORT).show();
		                        }
		                    });
					
		        }else{
		        	custom_dialog.dialogCode(2, null, "You did not enter a valid card.", context);
//		        	ValidationSingleButton("You did not enter a valid card");
		        }
		        }
			}
			}*/
			
			/*else{
				new AddAddressBackTask().execute("");
			}*/
			break;

		default:
			break;
		}
	}

	
	
	
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
		getLocation();
		
		 ivSameIcon.setVisibility(View.INVISIBLE);
		
		first_name= pref.getString("reg_fname", "");
		last_name= pref.getString("reg_lname", "");
		New_email= pref.getString("New_email", "");
		Status_NewEmail= pref.getString("Status_NewEmail", "false");
		String	MobileNo= pref.getString("New_mobile_no", "");
		
		
		if(Status_NewEmail.equals("true")){
			email= pref.getString("New_email", "");
			evEmail.setText(email);	
			evMobile.setText(MobileNo);	
		}else{
			email= pref.getString("reg_email", "");
			if(email.equals(null)|| email.equals("null@null.null")){
				email="";
			}else{
				evEmail.setText(email);	
			}
			
		}
		evFName.setText(first_name);
		evLName.setText(last_name);
		evState.setText(state);
		evZip.setText(zip);
		evCity.setText(city);
		tvspinnerCountry.setText(country);
		
		tvHeader.setText("Add Shipping Address");
		
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
					evFName.setText(first_name);
					 evLName.setText(last_name);
					 evCity.setText(city);
					 evState.setText(state);
					 evZip.setText(zip);
					 tvspinnerCountry.setText(country);
					 ivSameIcon.setVisibility(View.INVISIBLE);
				}
			}
		});
		ivNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				
				S_first_name=evFName.getText().toString().trim();
				S_last_name=evLName.getText().toString().trim();
				S_appartment=evAPT.getText().toString().trim();
				S_address=""+evAddress.getText().toString().trim();
				S_country=tvspinnerCountry.getText().toString().trim();
				S_city=evCity.getText().toString().trim();
				S_state=evState.getText().toString().trim();
				S_zipcode=evZip.getText().toString().trim();
				S_mobile_no=evMobile.getText().toString().trim();
				S_email=evEmail.getText().toString().trim();
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
				    	
				    	dialog.dismiss();
				    	if (cd.checkConnection()) {
							new AddAddressBackTask().execute("");
						} else {
							Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
						}	
			    }
			}
		});
		
		
		dialog.show();
	}


	
	
	/*
	private class CreateStripeAccountBackTask extends AsyncTask<String, Void, String> {
		String errorMessage="";

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
			APILink=UserFunctions.Stripe_CustomerCreate;
		}

		@Override
		protected String doInBackground(String... params) {
			
				
				nameValuePairs.clear();
                nameValuePairs.add(new BasicNameValuePair("description", "Customer for Epic Demo"));
                nameValuePairs.add(new BasicNameValuePair("card", token_id));
                CreateStripeCustomer(APILink, nameValuePairs);
                errorMessage="true";
			return errorMessage;
		}
		
		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
				
				editor.putString("UserID", strUserID); // Storing string
				editor.commit();
				
			
				BillingModal _list =mBillingAddress.get(Billing_Selected_pos);
				String b_id = _list.getAddress_id();
				editor.putString("b_id_selected", ""+b_id);
				editor.commit();
				Log.e("bid", " "+b_id);
				
				status_Payment_onepage_check= pref.getBoolean("P_ComefromonePageCheck", false);
				//if(status_Payment_onepage_check){
					MainCheckOutFragmentActivity activity = (MainCheckOutFragmentActivity) getActivity();
					activity.ReplaceFragmentLeftToRight(one_click_checkout,null);	
			
					Shahil activity = (Shahil) getActivity();
					activity.ReplaceFragmentLeftToRight(one_click_checkout,null);	
				//}
				
				//Toast.makeText(getActivity(), "Completed", Toast.LENGTH_LONG).show();
			

		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	*/	
/*		  @SuppressWarnings("unused")
		public  String CreateStripeCustomer(String ApiLink,ArrayList<NameValuePair> nameValuePairs){
		    	 com.stripe.Stripe.apiKey=""+UserFunctions.Stripe_PUBLISHABLE_KEY;
		    	 String result = "";
		    	 InputStream is = null;
		 		
		    	 try {
				      HttpClient  httpclient = new DefaultHttpClient();
				      HttpPost  httppost = new HttpPost(ApiLink);
				      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				      httppost.addHeader("Authorization", "Bearer "+UserFunctions.Stripe_Authorization_KEY);
				      HttpResponse response = httpclient.execute(httppost);
				      int statuscode=response.getStatusLine().getStatusCode();
				      
				      switch(statuscode){
				      case 200:
				    	  if( response != null ){
					    	  HttpEntity entity = response.getEntity();
					          is = entity.getContent();
					       // convert response to string
							   
					          BufferedReader   reader = new BufferedReader(new InputStreamReader(
						          is, "iso-8859-1"));
					          StringBuilder  sb = new StringBuilder();
						      String  line = null;
						      while ((line = reader.readLine()) != null) {
						        sb.append(line + "\n");
						      }
						      is.close();
						      result = sb.toString();
						      Log.e("result","result "+result);
						      JSONObject jobj=new JSONObject(result);
						      String customer_id=jobj.getString("id");
						      editor.putString("customer_id", ""+customer_id);
					    		
						      if(customer_id!=null && !customer_id.equals("")){
						    	  jobj=jobj.getJSONObject("cards");
						    	  JSONArray jArray=jobj.getJSONArray("data");
						    	  if(jArray!=null && jArray.length()>0){
						    		  jobj=jArray.getJSONObject(0);
						    		  card_id=jobj.getString("id");
						    		 editor.putString("card_id", ""+card_id);
						    		
						    		  Log.e("card_id", "card_id "+card_id);
						    		  if(card_id!=null && !card_id.equals("")){
						    			  //ChargeAccount(customer_id,card_id);
						    		  }else{
						    			  result="error"; 
						    		  }
						    	  }else{
						    		  result="error"; 
						    	  }
						      }
						      editor.commit();
					      }else{
					    	  result="error";
					      }
				    	  break;
				      case 400:
				      case 401:
				      case 402:
				      case 404:
				      case 500:
				      case 502:
				      case 503:
				      case 504:
				    	  result="error";
				    	  break;
				      default:
				    	  result="error";
				    	  break;
				      }
		    	 }catch(Exception e){
		    		 e.printStackTrace();
		    		 result="error";
		    		 return result;
		    	 }
		    	return result;
		   
		  }
	*/	
		
/*	private void ValidationSingleButton(String msg) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

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

	private class DisplayCardDetailsBackTask extends AsyncTask<String, Void, String> {
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

		    	InputStream is = null;
				String result = "";
		    	String ApiLink=UserFunctions.Stripe_CustomerCreate+"/"+customer_id;
		    	ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
			     try{
			      HttpClient  httpclient = new DefaultHttpClient();
			      HttpPost  httppost = new HttpPost(ApiLink);
			      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			      httppost.addHeader("Authorization", "Bearer "+UserFunctions.Stripe_Authorization_KEY);
			      HttpResponse response = httpclient.execute(httppost);
			      Log.e("response ", " "+response );
			      int statuscode=response.getStatusLine().getStatusCode();
			      switch(statuscode){
			      case 200:
			    	  
			    	  if( response != null ){
				    	  HttpEntity entity = response.getEntity();
				          is = entity.getContent();
				       // convert response to string
						   
				          BufferedReader   reader = new BufferedReader(new InputStreamReader(
					          is, "iso-8859-1"));
				          StringBuilder  sb = new StringBuilder();
					      String  line = null;
					      while ((line = reader.readLine()) != null) {
					        sb.append(line + "\n");
					      }
					      is.close();
					      result = sb.toString();
					      
					      Log.e("result"," "+ result);
					      JSONObject jobj=new JSONObject(result);
					      JSONArray jArray = jobj.getJSONArray("data");
					      
					      for (int i = 0; i < jArray.length(); i++) {
					    	  JSONObject c = jArray.getJSONObject(i);
					    	Log.e("exp_year", " "+ c.getString("exp_year"));  
					    	Log.e("exp_month ", " "+ c.getString("exp_month"));
							
						}
					      
					      
			    	  }
			    	  break;
			      case 400:
			      case 401:
			      case 402:
			      case 404:
			      case 500:
			      case 502:
			      case 503:
			      case 504:
//			    	  getResponse(response);
			    	  result="error";
			    	  break;
			      default:
//			    	  getResponse(response);
			    	  result="error";
			    	  break;
			      }
			    }catch(Exception e){
			    	e.printStackTrace();
			    	return result;
			    }

			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	*/

	public void getAddress(double lat, double lng) {
	    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
	    try {
	        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
	        Address obj = addresses.get(0);
	        city=obj.getLocality().toString();
	        state=obj.getAdminArea().toString();
	        country=obj.getCountryName().toString();
	        zip=obj.getPostalCode().toString();
	      
	       
	        Log.e("IGA", "Address" + addresses);
	       
	    } catch (IOException e) {
	        e.printStackTrace();
	        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
	    }
	}
	
	public void AddItem(ArrayList<BillingModal> myValueList2) {
		tblAddress.removeAllViews();
		LayoutInflater inflater;
		for (int i = 0; i < myValueList2.size(); i++) {
			inflater = getActivity().getLayoutInflater();
			View convertView = (View) inflater.inflate(
					R.layout.creditcard_info_item, null);
			tblAddress.addView(convertView);
			TextView tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
			final ImageView ivSelect = (ImageView) convertView.findViewById(R.id.ivSelect);
			// txtQuantity3
			
			boolean status = mButtonClick.get(i);
			if(status){
				ivSelect.setImageDrawable
				(getActivity().getResources().getDrawable(R.drawable.add_creditcard_address_selected_icon));
			}else{
				ivSelect.setImageDrawable
				(getActivity().getResources().getDrawable(R.drawable.add_creditcard_address_default_icon));
			}
			ivSelect.setTag(""+i);
			ivSelect.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String position="0"; 
					position=ivSelect.getTag().toString();
					int pos=Integer.parseInt(position); 
					Billing_Selected_pos=pos;
					for (int j = 0; j < tblAddress.getChildCount(); j++) {
						
						
						View row;
						row = tblAddress.getChildAt(j);
						
						 ImageView ivSelect1 = (ImageView) row.findViewById(R.id.ivSelect);
						
						 Log.e("Pos", " "+pos);
						if(j==pos){
							Log.e("Pos", "true "+pos);		
							ivSelect.setImageDrawable
							(getActivity().getResources().getDrawable(R.drawable.add_creditcard_address_selected_icon));
							mButtonClick.set(pos, true);
						}else{
							
							
							mButtonClick.set(j,false);
							 ivSelect1.setImageDrawable
								(getActivity().getResources().getDrawable(R.drawable.add_creditcard_address_default_icon));
					
						}
						
					}
					tblAddress.invalidate();
					}
			});
			
			BillingModal myValues = myValueList2.get(i);
			tvAddress.setText(myValues.getAddress());
		}
	}

	
	public class ShippingAddressBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json = userFunction.getBillingAddressFunction(userid,"S");
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							mShippingAddress.clear();
							JSONArray jArray = json.getJSONArray("Shipping");
							
							for (int i = 0; i < jArray.length(); i++) {
								JSONObject c  = jArray.getJSONObject(i);
								
								address_id =c.getString("address_id");
								user_id =c.getString("user_id");
								type =c.getString("type");
								first_name =c.getString("first_name");
								last_name =c.getString("last_name");
								mobile_no =c.getString("mobile_no");
								address =c.getString("address");
								appartment =c.getString("appartment");
								joinaddress =c.getString("joinaddress");
								city =c.getString("city");
								state =c.getString("state");
								country =c.getString("country");
								zipcode =c.getString("zipcode");
								email =c.getString("email");
								
								
								mShippingAddress.add(new BillingModal(address_id, user_id,type, first_name, last_name
										, mobile_no, address, appartment, joinaddress, city
										, state, country, zipcode, email));
							}
							
							
							errorMessage = "true";

						} else {
							message=json.optString("message");
							errorMessage = "false";
						}
					} else {
						message=json.optString("message");
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
			
			new AddressInfoBackTask().execute("");
			mButtonClick.clear();
			for (int i = 0; i < mShippingAddress.size(); i++) {
				if(i==0){
					mButtonClick.add(true);
				}else{
					mButtonClick.add(false);
				}
			}
			
			if(result.equals("true")){
				AddItem(mShippingAddress);
			}else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else{
				Custom_Dialog.dialogCode(2, null,message, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	public class LockItemBackTask extends AsyncTask<String, Void, String> {
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
				
				Log.e("userid", " "+userid);
		
				
				JSONObject json = userFunction.ReserveFunction(userid,ItemId, "N");
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							address=json.optString("address");
							
							errorMessage = "true";

						} else {
							message=json.optString("message");
							errorMessage = "false";
						}
					} else {
						message=json.optString("message");
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
				MainCheckOutFragmentActivity activity ;
				 activity = (MainCheckOutFragmentActivity) getActivity();
				activity.cancelContdowwnTimer();
				activity.finish();
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}  else {
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
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
	private class AddAddressBackTask extends AsyncTask<String, Void, String> {
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
				Log.e("type", "B");
				Log.e("first_name", " "+b_first_name);
				Log.e("last_name", " "+b_last_name);
				Log.e("address", " "+b_address);
				Log.e("city", " "+b_city);
				Log.e("state", " "+b_state);
				Log.e("country", " "+b_country);
				Log.e("zipcode", " "+b_zipcode);
				
				JSONObject json = userFunction.BillingFunction
				(userid,itemid, "S", S_first_name, S_last_name
						, S_mobile_no, S_address, S_city, S_state
						, S_country, S_zipcode, S_email,S_appartment);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							s_id=json.optString("address_id");
							errorMessage = "true";

						} else {
							errorMessage = "false";
							message=json.optString("message");
						}
					} else {
						message=json.optString("message");
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
			
			if(result.equals("true")){
				editor.putString("s_id_selected", ""+s_id);
				editor.commit();
				MainCheckOutFragmentActivity activity = (MainCheckOutFragmentActivity) getActivity();
				activity.ReplaceFragmentLeftToRight(one_click_checkout,null);
				
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else{
				Custom_Dialog.dialogCode(2, null, message, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	
	
	
	
	public class AddressInfoBackTask extends AsyncTask<String, Void, String> {
		String errorMessage="";

		protected void onPreExecute() {
	
		}

		@Override
		protected String doInBackground(String... params) {
			{

				Log.e("userid", " " + userid);
				Log.e("ItemId", " " + ItemId);
				Log.e("bid", " " + bid);

				JSONObject json = userFunction.getAddressInfoFunction(userid,
						ItemId, bid,"");
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONObject jBilling = json.getJSONObject("Billing");

							b_first_name = jBilling.getString("first_name");
							b_last_name = jBilling.getString("last_name");
							b_address = jBilling.getString("address");
							b_appartment = jBilling.getString("appartment");
							b_city = jBilling.getString("city");
							b_state = jBilling.getString("state");
							b_country = jBilling.getString("country");
							b_zipcode = jBilling.getString("zipcode");
						
							errorMessage = "true";

						} else {
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

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	public void getLocation() {
		try {
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			Log.e("enable", "isGPSEnabled " + isGPSEnabled
					+ " isNetworkEnabled " + isNetworkEnabled);
			if (!isGPSEnabled && !isNetworkEnabled) {
				Log.e("enable", "boolean " + canGetLocation);
			} else {
				this.canGetLocation = true;
				Log.e("enable", "boolean " + canGetLocation);

				if (isNetworkEnabled) {

					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						lat = location.getLatitude();
						log = location.getLongitude();
						getAddress(lat, log);
					//	getAddress(27.498928,-82.574819);
						strlatitude = "" + lat;
						strlongitude = "" + log;
						Log.i("Wifi", "Lattitude : " + lat + ", Longitude : "
								+ log);
					}
				}
				if (isGPSEnabled) {

					location = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location != null) {
						lat = location.getLatitude();
						log = location.getLongitude();
						getAddress(lat, log);
				//		getAddress(27.498928,-82.574819);
						strlatitude = "" + lat;
						strlongitude = "" + log;
						Log.i("GPS", "Lattitude : " + lat + ", Longitude : "
								+ log);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	


	
}
