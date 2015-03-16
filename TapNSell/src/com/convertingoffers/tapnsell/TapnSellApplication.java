package com.convertingoffers.tapnsell;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;


public class TapnSellApplication extends Application{
	
	
	public DatabaseHandler mydatabaseAdapter;
	public SQLiteDatabase db;
	@Override
	public void onCreate() {		
		super.onCreate();
		// Enable Crash Reporting
//		ParseCrashReporting.enable(this);
//		 
//		// Setup Parse
//		Parse.initialize(this, "4FrPaUDdAPPfRdJm59928XINwX5g1XfuqNrtCGiW", "ryVZF4nLwlJnL1oQC7fmfQuzt2HCOwNOipjiwgub");
		db = null;
		mydatabaseAdapter = new DatabaseHandler(this);
		try {
			mydatabaseAdapter.createDatabase();
			if(!mydatabaseAdapter.isOpen()){
	
				db=mydatabaseAdapter.openDatabase();
			}else{
			
			}
			
		} catch (Exception e) {
		}
		
	}
	
	@Override
	public void onTerminate() {		
		super.onTerminate();
		 if(db!=null)
				db.close();
	    if(mydatabaseAdapter!=null)
		mydatabaseAdapter.close();
		mydatabaseAdapter = null;
		db=null;
	}
	
	@Override
	public void onLowMemory() {		
		super.onLowMemory();
	}
	
	
}
