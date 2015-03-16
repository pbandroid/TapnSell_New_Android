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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.ConnectionDetector;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;

public class Billing_Fragment extends Fragment implements OnClickListener{
	Custom_Dialog custom_dialog;
Context context;
	EditText evFName,evLName,evAddress,evAPT,evCity,evState,evZip,evEmail;
	TextView tvspinnerCountry,tvNext;
	LinearLayout llSpinnerCountry;

	ImageView ivNext;

	String userid="", type="B", first_name="",b_id="", last_name="", address="", city="", state="",itemid="",
	country="", zipcode="", appartment="";

	ArrayList<String> mcountry = new ArrayList<String>();

	String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	Dialog dialog;
	ListView lvt;
	Adapter adapter;
	 Bundle bundle;
		Billing_Fragment billing_fragment;
		Shipping_Fragment ship_fragment;
		Select_billing_address Select_billing_address;
		OneClickCheckout_Fragment one_click_check_out;
		Select_payment_fragment select_payment_fragment;
// Base fragment 
	View view;
	public Editor editor;
	public SharedPreferences pref;
	public ConnectionDetector cd;
	public UserFunctions userFunction;
	public ProgressDialog progressDialog;
	public String KEY_SUCCESS = "successfull",KEY_SUCCESS_STATUS = "true",strmsg="";
	String S_first_name="",S_last_name="",S_address="",S_appartment="",S_city="",S_state="",S_country="",S_zipcode="",S_mobile_no="",S_email="",ComeFrom="";
	public TextView tvHeader;
	public ImageView ivMenu,ivBack;
	boolean status_billing =false;
	// Base fragment
	double lat=0.0, log=0.0,Distance=0.0,lat1,lat2,lng1,lng2,Dis=0.0;
	protected LocationManager locationManager;
	String strlatitude,strlongitude;
	Location location;
	boolean isCurrentLocation=false,Successfull, isGPSEnabled = false,isNetworkEnabled = false, canGetLocation = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		context=getActivity().getApplicationContext();
		view = inflater.inflate(R.layout.billing, container,false);
		bundle = new Bundle();
		Log.e("testbilling","testbilling");
		InitializeView();
		getLocation();
		MakeCountryArray();
		
		tvHeader.setText("Enter Billing Address");
		ivNext.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		llSpinnerCountry.setOnClickListener(this);
		
		itemid=pref.getString("CHKItemid", "");
		status_billing= pref.getBoolean("StatusBilling", false);
	
		first_name= pref.getString("reg_fname", "");
		last_name= pref.getString("reg_lname", "");
		 evFName.setText(first_name);
		 evLName.setText(last_name);

		
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
						//getAddress(lat, log);
						getAddress(27.498928,-82.574819);
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
						//getAddress(lat, log);
						getAddress(27.498928,-82.574819);
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
	       evCity.setText(obj.getLocality());
	       evState.setText(obj.getAdminArea());
	       tvspinnerCountry.setText(obj.getCountryName());  
	       evZip.setText(obj.getPostalCode());
	       
	        Log.e("IGA", "Address" + addresses);
	       
	    } catch (IOException e) {
	        e.printStackTrace();
	        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
	    }
	}


	private void InitializeView() {
		
		context=getActivity();
		custom_dialog=new Custom_Dialog();
		one_click_check_out=new OneClickCheckout_Fragment();
		Select_billing_address=new Select_billing_address();
		select_payment_fragment =new Select_payment_fragment();
		ship_fragment=new Shipping_Fragment();
		billing_fragment=new Billing_Fragment();
		tvNext=(TextView)view.findViewById(R.id.tvNext);
		tvspinnerCountry=(TextView)view.findViewById(R.id.tvspinnerCountry);
		ivNext=(ImageView)view.findViewById(R.id.ivNext);
		
		llSpinnerCountry=(LinearLayout)view.findViewById(R.id.llSpinnerCountry);
		evFName=(EditText)view.findViewById(R.id.evFName);
		evLName=(EditText)view.findViewById(R.id.evLName);
		evAddress=(EditText)view.findViewById(R.id.evAddress);
		evAPT=(EditText)view.findViewById(R.id.evAPT);
		evCity=(EditText)view.findViewById(R.id.evCity);
		evState=(EditText)view.findViewById(R.id.evState);
		evZip=(EditText)view.findViewById(R.id.evZip);
		
		

		// Base fragment Initialize 
		ivMenu=(ImageView)view.findViewById(R.id.ivMenu);
		ivBack=(ImageView)view.findViewById(R.id.ivBack);
		tvHeader= (TextView) view.findViewById(R.id.tvHeader);
        userFunction = new UserFunctions();
    	cd = new ConnectionDetector(context);
    	pref =PreferenceManager.getDefaultSharedPreferences(context);
    	editor = pref.edit();
    	
	}

	
	
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ivNext:

			userid=""+pref.getString("UserID", "");
			type="B";
		    first_name=""+evFName.getText().toString().trim();
		    last_name=""+evLName.getText().toString().trim();
		    address=""+evAddress.getText().toString().trim();
		    city=""+evCity.getText().toString().trim();
		    state=""+evState.getText().toString().trim();
		    country=""+tvspinnerCountry.getText().toString().trim();
		    zipcode=""+evZip.getText().toString().trim();
		    appartment=""+evAPT.getText().toString().trim();
		    
		    if(first_name.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter first name.", context);
		    }else if(last_name.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter last name.", context);
		    }else if(address.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter address.", context);
		    }else if(city.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter city.", context);
		    }else if(state.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter state.", context);
		    }else if(country.length()==0 || country.equals("Select Country")){
		    	Custom_Dialog.dialogCode(2, null, "Please select country.", context);
		    }else if(zipcode.length()==0){
		    	Custom_Dialog.dialogCode(2, null, "Please enter zipcode.", context);
		    }else{
		    	
		    		editor.putString("chk_first_name", ""+first_name);
				    editor.putString("chk_last_name", ""+last_name);
				    editor.putString("chk_address", ""+address);
				    editor.putString("chk_city", ""+city);
				    editor.putString("chk_state", ""+state);
				    editor.putString("chk_country", ""+country);
				    editor.putString("chk_zipcode", ""+zipcode);
				    editor.putString("chk_appartment", ""+appartment);
				    editor.commit();
		    	
					if (cd.checkConnection()) {
						new AddAddressBackTask().execute("");
					} else {
						Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
					}	
		    }
			break;
		case R.id.ivBack:
//			getActivity().finish();
	
				MainCheckOutFragmentActivity activity = (MainCheckOutFragmentActivity) getActivity();
				activity.ReplaceFragmentRightToLeft();
	
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

					String Country = mcountry.get(position);
					tvspinnerCountry.setText(Country);
					llSpinnerCountry.setClickable(true);
					llSpinnerCountry.setEnabled(true);
					dialog.dismiss();
					
			}
		});
	}
	
	@Override
	public void onResume() {
		llSpinnerCountry.setClickable(true);
		llSpinnerCountry.setEnabled(true);
		super.onResume();
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

	
	
	
/*	private void rlSameBtnCode() {

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
*/
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.getActivity().onKeyDown(keyCode, event);
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
				Log.e("type", " "+type);
				Log.e("first_name", " "+first_name);
				Log.e("last_name", " "+last_name);
				Log.e("address", " "+address);
				Log.e("city", " "+city);
				Log.e("state", " "+state);
				Log.e("country", " "+country);
				Log.e("zipcode", " "+zipcode);
				
				JSONObject json = userFunction.BillingFunction
				(userid,itemid, type, first_name, last_name
						, "", address, city, state
						, country, zipcode, "",appartment);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							b_id=json.optString("address_id");
							errorMessage = "true";

						} else {
							errorMessage = "false";
							message=json.optString("message");
						}
					} else {
//						message=json.optString("message");
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
				editor.putString("b_id_selected", ""+b_id);
				editor.commit();
				MainCheckOutFragmentActivity activity = (MainCheckOutFragmentActivity) getActivity();
				activity.ReplaceFragmentLeftToRight(ship_fragment,null);
				
			} else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			}else{
				Custom_Dialog.dialogCode(2, null, message, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

}
