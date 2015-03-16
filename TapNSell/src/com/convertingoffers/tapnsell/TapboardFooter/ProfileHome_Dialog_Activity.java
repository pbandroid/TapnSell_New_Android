package com.convertingoffers.tapnsell.TapboardFooter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class ProfileHome_Dialog_Activity extends BaseActivity {
	Context context_dialog;
   	ImageView ivComplete,ivClose;
   	EditText  evAddress,evCity,evState,evZipCode;
   	String Address="",city="",state="",zip="";
	double lat=0.0, log=0.0,Distance=0.0,lat1,lat2,lng1,lng2,Dis=0.0;
	protected LocationManager locationManager;
	String strlatitude,strlongitude;
	Location location;
	boolean isCurrentLocation=false,Successfull, isGPSEnabled = false,isNetworkEnabled = false, canGetLocation = false;

	
   	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.dialog_verify_home);
	new GetAddressBackTask().execute("");
	ivComplete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(evAddress.getWindowToken(), 0);
				Address =evAddress.getText().toString().trim();
				city=evCity.getText().toString().trim();
				state=evState.getText().toString().trim();
				zip=evZipCode.getText().toString().trim();
				
				if(Address.length()==0){
					Custom_Dialog.dialogCode(2, null, "Please enter address.", context_dialog);
				}else if(city.length()==0){
					Custom_Dialog.dialogCode(2, null, "Please enter city.", context_dialog);
				}else if(state.length()==0){
					Custom_Dialog.dialogCode(2, null, "Please enter state.", context_dialog);
				}else if(zip.length()==0){
					Custom_Dialog.dialogCode(2, null, "Please enter zipcode.", context_dialog);
				}else{
					String finalAddress="";
					finalAddress= evAddress.getText().toString() + " " +evCity.getText().toString().trim() + " " +
					evState.getText().toString().trim()+" "+evZipCode.getText().toString().trim();
					finalAddress=finalAddress.replaceAll("null", "");
					editor.putString("HomeAddressMeetup", ""+ finalAddress);
					editor.commit();	
					finish();
				}
				
				
			}
		});
  
   ivClose.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(evAddress.getWindowToken(), 0);
	finish();	
	}
});
	
	}
   	
   	private class GetAddressBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context_dialog);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			try {
				locationManager = (LocationManager) context_dialog
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
					ProfileHome_Dialog_Activity.this.canGetLocation = true;
					Log.e("enable", "boolean " + canGetLocation);

					if (isNetworkEnabled) {

						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							lat = location.getLatitude();
							log = location.getLongitude();
							
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
		
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			  Geocoder geocoder = new Geocoder(context_dialog, Locale.getDefault());
			    try {
			        List<Address> addresses = geocoder.getFromLocation(27.498928, -82.574819, 1);
			        Address obj = addresses.get(0);

			     //   evAddress.setText(obj.getAddressLine(0));
			        evZipCode.setText(obj.getPostalCode());
			        evCity.setText(obj.getLocality());
				       evState.setText(obj.getAdminArea());
			        Log.e("IGA", "Address" + obj);
			        // Toast.makeText(this, "Address=>" + add,
			        // Toast.LENGTH_SHORT).show();

			        // TennisAppActivity.showDialog(add);
			    } catch (IOException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			        Toast.makeText(context_dialog, e.getMessage(), Toast.LENGTH_SHORT).show();
			    }
		}


		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	
   	
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		
		evCity=(EditText)findViewById(R.id.evCity);
		evState=(EditText)findViewById(R.id.evState);
		evZipCode=(EditText)findViewById(R.id.evZipCode);
		evAddress=(EditText)findViewById(R.id.evAddress);
		ivComplete=(ImageView)findViewById(R.id.ivComplete);
		ivClose=(ImageView)findViewById(R.id.ivClose);
		context_dialog=this;
	}
	
}
