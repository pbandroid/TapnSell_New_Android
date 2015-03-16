package com.convertingoffers.tapnsell.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
	
	private Context _context;
	
	public ConnectionDetector(Context context){
		this._context = context;
	}

	public boolean checkConnection() {
		ConnectivityManager conMan = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conMan.getActiveNetworkInfo();

		final boolean connected = networkInfo != null
				&& networkInfo.isAvailable() && networkInfo.isConnected();

		if (!connected) {

			return false;
		}
		return true;
	}
}