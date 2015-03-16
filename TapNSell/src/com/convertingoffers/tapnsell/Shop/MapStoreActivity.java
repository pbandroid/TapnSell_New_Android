package com.convertingoffers.tapnsell.Shop;

import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.convertingoffers.tapnsell.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapStoreActivity extends FragmentActivity {
	
	
	protected static final String TAG_ERROR_DIALOG_FRAGMENT = "errorDialog";
	int maptype = 0;
	private GoogleMap mMap;
	double lat=0,log=0;
	String latitude,longitude;
	ImageView ivBack;
	TextView tvHeader;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_display);
		
		tvHeader=(TextView)findViewById(R.id.tvHeader);
		ivBack=(ImageView)findViewById(R.id.ivBack);
		
		tvHeader.setText("Map");
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
		public void onClick(View v) {
			finish();	
			}
		});
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			latitude = bundle.getString("lat");
			longitude = bundle.getString("log");
		}else{
			latitude="0";
			longitude="0";
		}
		
		lat=Double.valueOf(latitude);
		log=Double.valueOf(longitude);
	
		Log.e("lat", " "+lat);
		Log.e("log", " "+log);
		if (readyToGo()) {
			setUpMapIfNeeded();
		}
		
	}

	protected boolean readyToGo() {
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(MapStoreActivity.this);

		if (status == ConnectionResult.SUCCESS) {
			return (true);
		} else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
			ErrorDialogFragment.newInstance(status).show(
					MapStoreActivity.this.getSupportFragmentManager(),
					TAG_ERROR_DIALOG_FRAGMENT);
		} else {
			AlertDialog dialog = new AlertDialog.Builder(MapStoreActivity.this)
					.create();
			dialog.setMessage("install_latest_google_map");
			dialog.setButton(AlertDialog.BUTTON_POSITIVE,
					"Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							MapStoreActivity.this.finish();
						}
					});
			dialog.show();
		}
		return (false);
	}

	public static class ErrorDialogFragment extends DialogFragment {
		static final String ARG_STATUS = "status";

		static ErrorDialogFragment newInstance(int status) {
			Bundle args = new Bundle();

			args.putInt(ARG_STATUS, status);

			ErrorDialogFragment result = new ErrorDialogFragment();

			result.setArguments(args);

			return (result);
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Bundle args = getArguments();

			return GooglePlayServicesUtil.getErrorDialog(
					args.getInt(ARG_STATUS), getActivity(), 0);
		}

		@Override
		public void onDismiss(DialogInterface dlg) {
			if (getActivity() != null) {
				getActivity().finish();
			}
		}
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

			switch (maptype) {
			case 0:
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			case 1:
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case 2:
				mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			}
		/*	
			lat = 23.026529;
			log = 72.5713277;*/

			LatLng latLng = new LatLng(lat, log);
			mMap.addMarker(new MarkerOptions()
					.position(new LatLng(lat, log))
					.title("current_location")
					.snippet(Locale.getDefault().toString())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_action_location)));
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(latLng) // Sets the center of the map to
					// Mountain View
					.zoom(14) // Sets the zoom
					.tilt(30) // Sets the tilt of the camera to 30 degrees
					.build(); // Creates a CameraPosition from the builder

			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

		}
	}
}