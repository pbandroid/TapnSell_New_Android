package com.convertingoffers.tapnsell.Shop.checkout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.ConnectionDetector;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;

public class Shipping_Fragment extends Fragment implements OnClickListener{
Custom_Dialog custom_dialog;

	EditText evFName,evLName,evAddress,evAPT,evMobile,evCity,evState,evZip,evEmail;
	TextView tvspinnerCountry,tvNext;
	LinearLayout llSpinnerCountry;
	ImageView ivNext;
	RelativeLayout rlSame;
	Dialog dialog;
	ListView lvt;
	Adapter adapter;
	Boolean SameStatus=true;
	Billing_Fragment billing_fragment;
	Select_payment_fragment select_payment_fragment;
	Checkout_home_Fragment check_fragment;
	ArrayList<String> mcountry = new ArrayList<String>();
	String itemid="",userid="",appartment="", type="",ComeFrom="", first_name="", last_name="", mobile_no="", address="", city="", state="", country="", zipcode="", email="";
	Context context;
	View view;
	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	Bundle bundle = new Bundle();
	ImageView ivSameIcon;
	OneClickCheckout_Fragment one_click_check_out;
	String New_email="",Status_NewEmail="false";
	// Base fragment 
	
	
	double lat=0.0, log=0.0,Distance=0.0,lat1,lat2,lng1,lng2,Dis=0.0;
	protected LocationManager locationManager;
	String strlatitude,strlongitude;
	Location location;
	boolean isCurrentLocation=false,Successfull, isGPSEnabled = false,isNetworkEnabled = false, canGetLocation = false;
	public Editor editor;
	public SharedPreferences pref;
	public ConnectionDetector cd;
	public UserFunctions userFunction;
	public ProgressDialog progressDialog;
	public String KEY_SUCCESS = "successfull",KEY_SUCCESS_STATUS = "true",strmsg="";
	public TextView tvHeader;
	public ImageView ivMenu,ivBack;
	String	b_first_name,b_last_name,b_address,b_city,b_state,b_country,b_zipcode,b_appartment;
	// Base fragment
	boolean status_billing =false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		

		 view = inflater.inflate(R.layout.shipping, container,false);
		context =getActivity().getApplicationContext();
		InitializeView();
		status_billing= pref.getBoolean("StatusBilling", false);
		getLocation();
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		itemid=pref.getString("CHKItemid", "");
		tvHeader.setText("Enter Shipping Address");
		ivNext.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		llSpinnerCountry.setOnClickListener(this);
		rlSame.setOnClickListener(this);
		mcountry.clear();
		MakeCountryArray();
		
		
		
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
		
		
		first_name = pref.getString("chk_first_name", "");
		last_name = pref.getString("chk_last_name", "");
		address = pref.getString("chk_address", "");
		city = pref.getString("chk_city", "");
		state = pref.getString("chk_state", "");
		country = pref.getString("chk_country", "");
		zipcode = pref.getString("chk_zipcode", "");
		appartment= pref.getString("chk_appartment", "");
		 evFName.setText(first_name);
		 evLName.setText(last_name);
		 evAddress.setText(address);
		 evCity.setText(city);
		 evState.setText(state);
		 evZip.setText(zipcode);
		 ivSameIcon.setVisibility(View.VISIBLE);
		 tvspinnerCountry.setText(country);
		 evAPT.setText(appartment);
		
		return view;
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

		
		public void getAddress(double lat, double lng) {
		    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		    try {
		        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
		        Address obj = addresses.get(0);
//		        String add = obj.getAddressLine(0);
		        evAddress.setText(obj.getAddressLine(0));
		       evCity.setText(obj.getLocality());
		       evState.setText(obj.getAdminArea());
		       tvspinnerCountry.setText(obj.getCountryName());  
		       evZip.setText(obj.getPostalCode());

		        Log.e("IGA", "Address" + addresses);
		        // Toast.makeText(this, "Address=>" + add,
		        // Toast.LENGTH_SHORT).show();

		        // TennisAppActivity.showDialog(add);
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
		    }
		}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.getActivity().onKeyDown(keyCode, event);
	}
	private void InitializeView() {
		
		custom_dialog=new Custom_Dialog();
		context=getActivity();
		select_payment_fragment=new Select_payment_fragment();
		one_click_check_out= new OneClickCheckout_Fragment();
		check_fragment=new Checkout_home_Fragment();
		billing_fragment=new Billing_Fragment();
		tvNext=(TextView)view.findViewById(R.id.tvNext);
		tvspinnerCountry=(TextView)view.findViewById(R.id.tvspinnerCountry);
		ivNext=(ImageView)view.findViewById(R.id.ivNext);
		ivSameIcon=(ImageView)view.findViewById(R.id.ivSameIcon);
		llSpinnerCountry=(LinearLayout)view.findViewById(R.id.llSpinnerCountry);
		evFName=(EditText)view.findViewById(R.id.evFName);
		evLName=(EditText)view.findViewById(R.id.evLName);
		evAddress=(EditText)view.findViewById(R.id.evAddress);
		evAPT=(EditText)view.findViewById(R.id.evAPT);
		evMobile=(EditText)view.findViewById(R.id.evMobile);
		evCity=(EditText)view.findViewById(R.id.evCity);
		evState=(EditText)view.findViewById(R.id.evState);
		evZip=(EditText)view.findViewById(R.id.evZip);
		evEmail=(EditText)view.findViewById(R.id.evEmail);
		rlSame=(RelativeLayout)view.findViewById(R.id.rlSame);
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

	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ivNext:
			
			userid=""+pref.getString("UserID", "");
			type="S";
		    first_name=""+evFName.getText().toString().trim();
		    last_name=""+evLName.getText().toString().trim();
		    mobile_no=""+evMobile.getText().toString().trim();
		    address=""+evAddress.getText().toString().trim();
		    city=""+evCity.getText().toString().trim();
		    state=""+evState.getText().toString().trim();
		    country=""+tvspinnerCountry.getText().toString().trim();
		    zipcode=""+evZip.getText().toString().trim();
		    email=""+evEmail.getText().toString().trim();
		    appartment=""+evAPT.getText().toString().trim();
		    
		    Log.e("Shiping", "Shiping");
		    if(email.equals(null)|| email.equals("null@null.null")){
				email="";
			}
		    if(first_name.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter Firstname.", context);
		    }else if(last_name.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter Lastname.", context);
		    }else if(address.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter Address.", context);
		    }else if(country.length()==0 || country.equals("Select Country")){
		    	Custom_Dialog.dialogCode(2, null, "Please select Country.", context);
		    }else if(mobile_no.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter Mobile no.", context);
		    }else if(city.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter City.", context);
		    }else if(state.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter State.", context);
		    }else if(zipcode.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter ZipCode.", context);
		    }else if(email.length()==0 ){
		    	Custom_Dialog.dialogCode(2, null, "Please enter EmailAddress.", context);
		    }else if(!email.matches(emailPattern)|| email.equals("null@null.com")) {
		    	Custom_Dialog.dialogCode(2, null, "Please enter valid EmailAddress.", context);
		    }else{
			if (cd.checkConnection()) {
				new AddAddressBackTask().execute("");
			} else {
				Toast.makeText(getActivity(), "internet_conn_failed", Toast.LENGTH_LONG).show();
			}
		    }
			break;
		case R.id.ivBack:
				
				MainCheckOutFragmentActivity activity = (MainCheckOutFragmentActivity) getActivity();
				activity.ReplaceFragmentRightToLeft();
			
			break;
			
		case R.id.rlSame:
			rlSameBtnCode();
			break;
			
		case R.id.llSpinnerCountry:
			llSpinnerCountry.setClickable(false);
			llSpinnerCountry.setEnabled(false);
			SpinnerCountryDialog();
			break;
		default:
			break;
		}
	} 
	
	private void rlSameBtnCode() {

		if(!SameStatus){
			SameStatus=true;
		 evFName.setText(first_name);
		 evLName.setText(last_name);
		 evAddress.setText(address);
		 evCity.setText(city);
		 evState.setText(state);
		 evZip.setText(zipcode);
		 ivSameIcon.setVisibility(View.VISIBLE);
		 tvspinnerCountry.setText(country);
		evAPT.setText(appartment);
		
		}else{
			SameStatus=false;
			evFName.setText("");
			 evLName.setText("");
			 evAddress.setText("");
			 evAPT.setText("");
			 evCity.setText("");
			 evState.setText("");
			 evZip.setText("");
			 tvspinnerCountry.setText("Select Country");
			 ivSameIcon.setVisibility(View.INVISIBLE);
		}
		
	}

	/*private void ValidationSingleButton(String msg) {

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
	}*/
	
	@Override
	public void onResume() {
		llSpinnerCountry.setClickable(true);
		llSpinnerCountry.setEnabled(true);
		super.onResume();
	}
	public void SpinnerCountryDialog()

	{
		dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.itemdetails_listview);
		dialog.setTitle("Select Country");
		dialog.show();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				
				llSpinnerCountry.setClickable(true);
				llSpinnerCountry.setEnabled(true);
			}
		});
		lvt = (ListView) dialog.findViewById(R.id.lvDialog);
		adapter = new Adapter(context, mcountry);
		lvt.setAdapter(adapter);
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
					dialog.dismiss();
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

	
	private class AddAddressBackTask extends AsyncTask<String, Void, String> {
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
				Log.e("userid", " "+userid);
				Log.e("type", " "+type);
				Log.e("first_name", " "+first_name);
				Log.e("last_name", " "+last_name);
				Log.e("mobile_no", " "+mobile_no);
				Log.e("address", " "+address);
				Log.e("city", " "+city);
				Log.e("state", " "+state);
				Log.e("country", " "+country);
				Log.e("zipcode", " "+zipcode);
				Log.e("email", " "+email);
				
				editor.putString("New_email", email);
				editor.putString("New_mobile_no", mobile_no);
				editor.putString("Status_NewEmail", "true");
				editor.commit();
				
				JSONObject json = userFunction.BillingFunction
				(userid,itemid, type, first_name, last_name
						, mobile_no, address, city, state
						, country, zipcode, email,appartment);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
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
			
			if(result.equals("true")){
				if(status_billing){
					MainCheckOutFragmentActivity activity = (MainCheckOutFragmentActivity) getActivity();
					activity.ReplaceFragmentLeftToRight(one_click_check_out,null);
					
				}else{
					MainCheckOutFragmentActivity activity = (MainCheckOutFragmentActivity) getActivity();
					activity.ReplaceFragmentLeftToRight(select_payment_fragment,bundle);	
				}
				
			}else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else {
				Custom_Dialog.dialogCode(2, null, "Please try again later.", context);
				
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
}
