package com.convertingoffers.tapnsell;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class ContactBackEndService extends Service {
	 String displayName="", emailAddress="", phoneNumber="",message,MainPhoneString="",url="",type="sms",msg="";

	public void onCreate() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new ContactBackTask().executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, "");
		} else {
			new ContactBackTask().execute("");
		}
		super.onCreate();
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	private class ContactBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			
		}

		@Override
		protected String doInBackground(String... params) {
			{
		        try {
					ContentResolver cr =getContentResolver();
					Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,null);
					try {
					if(cursor!=null && cursor.getCount()>0){
					    for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
					    {
					        displayName="";emailAddress=""; phoneNumber="";
					        displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));       
							if (displayName!=null && !displayName.equals("")) {
								displayName = displayName.replaceAll("'", "");
							}
							String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					        Cursor emails = null;
							try {
								emails = cr.query(Email.CONTENT_URI,null,Email.CONTACT_ID + " = " + id, null, null);
								if(emails!=null && emails.getCount()>0){
									for(emails.moveToFirst();!emails.isAfterLast();emails.moveToNext())
										{ 
											emailAddress = emails.getString(emails.getColumnIndex(Email.DATA));
												if(emailAddress==null || emailAddress.equals("")){
													emailAddress=" ";
												}else{
													break;
												}
										}
								 }
								} catch (Exception e) {
									e.printStackTrace();
								}finally{
									if(emails!=null && emails.getCount()>0)
										emails.close();
								}
					        String phonenocount= cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
					        if(Integer.parseInt(phonenocount)>0){
					        	Cursor pCur = null;
					        	try {
					             pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{id}, null);
					             if(pCur!=null && pCur.getCount()>0){
					            	 for(pCur.moveToFirst();!pCur.isAfterLast();pCur.moveToNext())
										{ 
					            		    phoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
												if(phoneNumber==null || phoneNumber.equals("")){
													phoneNumber=" ";
												}else{
													break;
												}
										}
					             	}
					        	} catch (Exception e) {
									e.printStackTrace();
									}finally{
									 if(pCur!=null && pCur.getCount()>0)
										 pCur.close();
									}
					        	}
					       Cursor c = ((TapnSellApplication) getApplication()).db.rawQuery("SELECT * FROM cont where email ='"+emailAddress+"' And phone='"+phoneNumber+"'", null);
					      	if (c != null && c.getCount() <= 0) {
					      		Log.e("id", " "+id);
							 String QUERY1 = "INSERT INTO cont VALUES('"+ id+ "','"+ displayName+ "','"+ phoneNumber+ "','"+emailAddress+ "')";
									((TapnSellApplication)getApplication()).db.execSQL(QUERY1);
							}
					    }
					    errorMessage="true";
					}else{
						errorMessage="false";
					}
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						if(cursor!=null && cursor.getCount()>0)
							cursor.close();
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {
			
			
		}
	}

}
