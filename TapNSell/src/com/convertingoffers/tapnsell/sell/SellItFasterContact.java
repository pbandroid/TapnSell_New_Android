package com.convertingoffers.tapnsell.sell;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapnSellApplication;
import com.convertingoffers.tapnsell.Modal.ContactsModal;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class SellItFasterContact extends BaseActivity implements OnClickListener{
	 int mMessageSentParts;
	 int mMessageSentTotalParts;
	 int mMessageSentCount;
	 ContactsModal	myCateForCheck;
	 ContactCustomAdaper adapter;
	 String displayName="", emailAddress="", phoneNumber="",message,MainPhoneString="",url="",type="sms",msg="";
	 ListView lvContact;
	 JSONArray jPhoneNo = new JSONArray();
	 ArrayList<ContactsModal> mContactList = new ArrayList<ContactsModal>();
	 ArrayList<ContactsModal> SearchContacList = new ArrayList<ContactsModal>();
	 String[] email_array;
	 String[] own_email_array;
		ArrayList<String> ownEmail= new ArrayList<String>();
	 ArrayList<String> mEmail= new ArrayList<String>();
//	 String[] array = new String[mPhoneNo.size()];
	 ImageView ivMenuSkip,ivShar,ivAll;
	 EditText evSearch;
	 int textlength = 0;
	 boolean status_Check=true;
	 int 	  intStartNewListing=0,count=0,cnt = 0,totalNoContact;
	 Context context;
	 Custom_Dialog custom_dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sellitfaster_contact);
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(context).getAccounts();
		for (Account account : accounts) {
		    if (emailPattern.matcher(account.name).matches()) {
		        String possibleEmail = account.name;
		        ownEmail.add(possibleEmail);
		    }
		    break;
		}
		own_email_array = ownEmail.toArray(new String[ownEmail.size()]);
		mContactList.clear();
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
		url = bundle.getString("url");
		type= bundle.getString("type");
		msg= bundle.getString("msg");
		}
		Log.e("url"," "+ url);
		mContactList.clear();
		new ContactBackTask().execute(""); 
		ivAll.setOnClickListener(this);
		ivShar.setOnClickListener(this);
		ivMenuSkip.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		evSearch.addTextChangedListener(new TextWatcher() {

		public void afterTextChanged(Editable s) {

		}

		public void beforeTextChanged(CharSequence s, int start, int count,
		int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
		int count) {
			
			String serch_text="";
			serch_text=evSearch.getText().toString();
			if(serch_text.length()==0){
				SearchContacList.clear();
				 adapter = new ContactCustomAdaper(SellItFasterContact.this, R.layout.sellitfaster_contact_item,mContactList);
					lvContact.setAdapter(adapter);
			}else{
				
				SearchContacList.clear();
				if(serch_text.length()!=0){
					serch_text = "%" + serch_text + "%";
				}
//	
				
				// Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, "display_name like ? ",new String[]{serch_text}, null);
				Cursor c = ((TapnSellApplication) getApplication()).db.rawQuery
				("SELECT * FROM cont WHERE name  LIKE '"+serch_text+"'", null);
				if (c != null && c.getCount() > 0) {
					if (c != null && c.getCount() > 0) {
						
						int id_colounm = c.getColumnIndex("id");
						int name_colounm = c.getColumnIndex("name");
						int phone_colounm = c.getColumnIndex("phone");
						int email_colounm = c.getColumnIndex("email");
						for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
							String id = c.getString(id_colounm);
							String name = c.getString(name_colounm);
							String phone = c.getString(phone_colounm);
							String email = c.getString(email_colounm);
							   if(status_Check){
								   if(!mEmail.contains(email)){
									   mEmail.add(phone);
								   }
								   SearchContacList.add(new ContactsModal(id, name, phone, email, "Y"));
								   
							}else{
								SearchContacList.add(new ContactsModal(id, name, phone, email, "N"));
								}
							}
						}
					}
				 adapter = new ContactCustomAdaper
				 (SellItFasterContact.this, R.layout.sellitfaster_contact_item,SearchContacList);
					lvContact.setAdapter(adapter);
			}
		        
			/*
		text_sort.clear();
		textlength = evSearch.getText().length();
					String serch_text="";
					serch_text=evSearch.getText().toString().toLowerCase();
		for (int i = 0; i < mContactList.size(); i++) {
						
		ContactsModal mycate = mContactList.get(i);
		String name =mycate.getDisplayName();
		if (textlength <= name.length()) {
		if(name.toLowerCase().contains(evSearch.getText().toString().toLowerCase())){
		text_sort.add(mycate);
		}else{

	        ContentResolver cr =getContentResolver();
	        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" = ? ",new String[]{serch_text}, null);
	        
	        Log.e("Cursorlength", " "+cursor.getCount());
		}
		}
		}
		adapter = new ContactCustomAdaper(SellItFasterContact.this, R.layout.sellitfaster_contact_item,text_sort);
		lvContact.setAdapter(adapter);
		*/}
		});
		}
	
	@Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	         if (keyCode == KeyEvent.KEYCODE_BACK) {
	      return true;
	         }
	         return super.onKeyDown(keyCode, event);    
    }
	 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		Intent  i =  new Intent(context, SharActivity.class);
		startActivity(i);
		 
	}
	
	@Override
	public void onClick(View v) {
		
	switch (v.getId()) {
	case R.id.ivAll:
		/*String search="";
		search =evSearch.getText().toString().trim();*/
		
		
			if(!status_Check){
				status_Check=true;
				mEmail.clear();		
				//if(search.length()!=0){
					for (int i = 0; i < SearchContacList.size(); i++) {
						
						ContactsModal search_cate=SearchContacList.get(i);
						search_cate.setCheck("Y");
						SearchContacList.set(i, search_cate);
						adapter.notifyDataSetChanged();
						String email ="";
						email=search_cate.getEmailAddress().toString();
						if(email.length()!=0 && !mEmail.contains(email)){
							mEmail.add(email);
						}
					
					}
				//}else{
					for (int i = 0; i < mContactList.size(); i++) {
						
						ContactsModal cate = mContactList.get(i);
						cate.setCheck("Y");
						mContactList.set(i, cate);
						adapter.notifyDataSetChanged();
						
						String email ="";
						email=cate.getEmailAddress().toString();
						if(email.length()!=0 && !mEmail.contains(email)){
							mEmail.add(email);
						}
						
					}
			//	}
		
			}else{
				
				status_Check=false;
				mEmail.clear();	
				//if(search.length()!=0){
					for (int i = 0; i < SearchContacList.size(); i++) {
						
						ContactsModal search_cate=SearchContacList.get(i);
						search_cate.setCheck("N");
						SearchContacList.set(i, search_cate);
						adapter.notifyDataSetChanged();
						
					}
				//}else{
					for (int i = 0; i < mContactList.size(); i++) {
						
						ContactsModal cate = mContactList.get(i);
						cate.setCheck("N");
						mContactList.set(i, cate);
						adapter.notifyDataSetChanged();
						
					}
				//}
			
				
			}
	break;
	case R.id.ivShar:
//		shahil send email
			new SharemailBackTask().execute("");	
	break;
	case R.id.ivMenuSkip:
			
		Intent i = new Intent(SellItFasterContact.this, SharActivity.class);
		startActivity(i);
		finish();
	break;
	case R.id.ivBack:
		finish();
	break;
	
	default:
	break;
		}
	}

	  
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		ivAll=(ImageView)findViewById(R.id.ivAll);
		lvContact=(ListView)findViewById(R.id.lvContact);
		ivShar=(ImageView)findViewById(R.id.ivShar);
		evSearch=(EditText)findViewById(R.id.evSearch);
		ivMenuSkip=(ImageView)findViewById(R.id.ivMenuSkip);
		custom_dialog=new Custom_Dialog();
		context=this;
	}
	
	private class ContactBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SellItFasterContact.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
			
		}

		@Override
		protected String doInBackground(String... params) {
			{
				
				Cursor c = ((TapnSellApplication) getApplication()).db.rawQuery
				("SELECT email,id,name FROM cont where email != '' order by name asc LIMIT 50 OFFSET '"+intStartNewListing+"'", null);
				if (c != null && c.getCount() >= 0) {
			
				if (c != null && c.getCount() > 0) {
				int id_colounm = c.getColumnIndex("id");
				int name_colounm = c.getColumnIndex("name");
				int email_colounm = c.getColumnIndex("email");
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					String id = c.getString(id_colounm);
					String name = c.getString(name_colounm);
					String email = c.getString(email_colounm);
					   if(status_Check){
						   mEmail.add(email);
						   mContactList.add(new ContactsModal(id, name, "", email, "Y"));
					}else{
						 mContactList.add(new ContactsModal(id, name, "", email, "N"));
					}
					errorMessage="true";
				}
				}else{
					errorMessage="false";
				}
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
			
            	
			if(mContactList.size()==0){
				Intent i = new Intent(SellItFasterContact.this, SharActivity.class);
				Custom_Dialog.dialogCode(3,  i, "No email address found.", context);	
			}else{
				adapter = new ContactCustomAdaper(SellItFasterContact.this
						, R.layout.sellitfaster_contact_item,mContactList);
				lvContact.setAdapter(adapter);
			}
				
				lvContact.setSelectionFromTop(count, 0);
				lvContact.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) { 
						
						
//						lvContact.setScrollContainer(false);
						String search_content="";
						search_content=evSearch.getText().toString();
						if(search_content.length()==0){
						int threshold = 1;
						count = 0;
						count = lvContact.getCount();
						
						if(totalNoContact>cnt){							
							if (scrollState == SCROLL_STATE_IDLE) {
								if (lvContact.getLastVisiblePosition() >= count	- threshold) {
									// Execute LoadMoreDataTask AsyncTask
									
									if (!progressDialog.isShowing()) {
										intStartNewListing=intStartNewListing+50;
										new ContactBackTask().execute("");
									}
									
								}
							}
						}							
					}
					}
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						
					}

				});		
				
				
				///	position= lvContact.getLastVisiblePosition();
				
				/* lvContact.setOnScrollListener(new OnScrollListener() {
					
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						
						String search_content="";
						search_content=evSearch.getText().toString();
						if(search_content.length()==0){
						Log.e("totalNoContact", " "+totalNoContact);
						int threshold = 1;
						cnt = lvContact.getCount();
						if(totalNoContact>cnt){
						
							if(scrollState==SCROLL_STATE_IDLE){
								if (lvContact.getLastVisiblePosition() >= cnt	- threshold) {
									ScrollCnt=ScrollCnt+10;
									new ContactBackTask().execute("");
									lvContact.setSelectionFromTop(cnt-10, 0);
								}
							}	
						}
					}
					}
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						
					}
				});*/
				
			} 
		}
	}

	private class SharemailBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SellItFasterContact.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				
				email_array = mEmail.toArray(new String[mEmail.size()]);
		
		 }
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			
			Log.e("email_array", " "+email_array.length);
		/*	 Intent isensms = new Intent(android.content.Intent.ACTION_VIEW);
			  isensms.putExtra("address",MainPhoneString);
			  isensms.putExtra("sms_body", "Greetings!");
			  isensms.setType("vnd.android-dir/mms-sms");
			  if(type.equals("smsa")){
				  startActivityForResult(isensms,11);				  
			  }else{
				  startActivityForResult(isensms,13);
				  }*/
			//share();
			/*Intent emailLauncher = new Intent(Intent.ACTION_SEND_MULTIPLE);

//			emailLauncher.setType("message/rfc822");
			emailLauncher.setType("application/octet-stream");
			emailLauncher.putExtra(android.content.Intent.EXTRA_BCC,email_array);
			//		emailLauncher.putExtra(Intent.EXTRA_EMAIL,new String[] { "abc@gmail.com" , "test@gmail.com", "xyz@test.com"});
			emailLauncher.putExtra(Intent.EXTRA_SUBJECT, "Check this out:");    
			emailLauncher.putExtra(Intent.EXTRA_TEXT,msg);
			
			       try {
					startActivityForResult(emailLauncher, 1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			
			String	first_name= pref.getString("reg_fname", "");
			String	last_name= pref.getString("reg_lname", "");
			String name ="";
			name=first_name+"  "+last_name;
			if(name.length()!=0){
				name.replaceAll("null","");	
			}
			
			message="Need a small favor… \n\n I’m selling this item on TapNSell. If you Know anyone who might " +
					"be interested can you please forward this email.\n\n"+url+"\n\nThanks!\n"+name+" \n \n P.S. I love you. Turn unwanted stuff into cash with this cool app \n\n http://goo.gl/k7idfJ";
			initShareIntent("com.google.android.gm", message);
			
		}
	}
	
	private void initShareIntent(String type,String msg) {
	    boolean found = false;
	    Intent share = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
	    share.setType("vnd.android.cursor.item/email");

	    // gets the list of intents that can be loaded.
	    List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
	    if (!resInfo.isEmpty()){
	        for (ResolveInfo info : resInfo) {
	            if (info.activityInfo.packageName.toLowerCase().contains(type) || info.activityInfo.name.toLowerCase().contains(type)) {
	            
	            	share.putExtra(Intent.EXTRA_SUBJECT, "Can you help me out?");   
	            	 share.putExtra(Intent.EXTRA_EMAIL,own_email_array);
	                share.putExtra(Intent.EXTRA_TEXT,msg);
	                share.putExtra(android.content.Intent.EXTRA_BCC,email_array);
	                share.setPackage(info.activityInfo.packageName);
	                found = true;
	                
	                break;
	            }
	        }
	        if (!found)
	            return;
	        try {
	        	startActivityForResult(Intent.createChooser(share, "Select"), 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }
	}
	
/*	private class SharsmsBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SellItFasterContact.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
			
			for (int i = 0; i < mPhoneNo.size(); i++) {
				
					String phoneNo;
					phoneNo=mPhoneNo.get(i).toString();
					
					if(phoneNo.length()!=0){
						if(MainPhoneString.length()==0){
						MainPhoneString=phoneNo;
						}else{
							MainPhoneString=MainPhoneString+", "+phoneNo;	
						}
						jPhoneNo.put(phoneNo);
					}
			}
			Log.e("jPhoneNo", " "+jPhoneNo);
		
		 }
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			Log.e("PhoneNo", " "+MainPhoneString);
			mPhoneNo.clear();
			
			if(jPhoneNo.length()==0){
			Toast.makeText(SellItFasterContact.this, "Please select contact...", Toast.LENGTH_LONG).show();
			}else{
				new TwillowSmsBackTask().execute("");	
			}
		}
	}
	
	
	private class TwillowSmsBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SellItFasterContact.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				JSONObject json = userFunction.SendtofriendurlFunction(jPhoneNo.toString(), url);
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

						
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

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {
				
				Intent i = new Intent(SellItFasterContact.this, SharActivity.class);
				Custom_Dialog.dialogCode(3, i, "Message sent successfully", context);
//				ValidationSingleButton("Message sent successfully");
			} else {
//				ValidationSingleButton("");
				Custom_Dialog.dialogCode(1, null, "Error in posting", context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	*/
/*
	private void ValidationSingleButton(String msg) {
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				SellItFasterContact.this);

		alertDialogBuilder.setTitle("alert");
 
			alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						dialog.dismiss();
						
						startActivity(i);
						finish();
					
					}
				  });
 
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
	}
*/
	private class ContactCustomAdaper extends ArrayAdapter<ContactsModal> {
		 	ViewHolder holder;
			private ArrayList<ContactsModal> listSubCategories;
			public ContactCustomAdaper(Context context, int textViewResourceId,
					ArrayList<ContactsModal> listSubCategories) {
				super(context, textViewResourceId, listSubCategories);
				this.listSubCategories = new ArrayList<ContactsModal>();
				this.listSubCategories = listSubCategories;
			}

			class ViewHolder {
			
				TextView tvContactName;
				RelativeLayout rlClick,rl1;
				ImageView ivRight;
			}

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {

				holder = null;

				if (convertView == null) {

					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

					convertView = vi.inflate(R.layout.sellitfaster_contact_item, null);

					holder = new ViewHolder();

					holder.tvContactName = (TextView) convertView.findViewById(R.id.tvContactName);
					holder.rlClick = (RelativeLayout) convertView.findViewById(R.id.rlClick);
					holder.rl1= (RelativeLayout) convertView.findViewById(R.id.rl1);
					holder.ivRight= (ImageView) convertView.findViewById(R.id.ivRight);
					convertView.setTag(holder);

				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				ContactsModal myCate = listSubCategories.get(position);
				holder.tvContactName.setText(myCate.getDisplayName());
				holder.tvContactName.setTag(myCate.getId());
				holder.rlClick.setTag(myCate.getPhoneNumber());
				holder.ivRight.setTag(myCate.getEmailAddress());
				holder.rl1.setTag(myCate);
				String chk=myCate.getCheck().toString();
				
				if(chk.equals("Y")){
					holder.ivRight.setVisibility(View.VISIBLE);
				}else{
					holder.ivRight.setVisibility(View.GONE);
				}

				holder.rlClick.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						myCateForCheck=listSubCategories.get(position);
						String chk= myCateForCheck.getCheck();
						if(chk.equals("Y")){
							holder.ivRight.setVisibility(View.GONE);
							myCateForCheck.setCheck("N");
							String marray_email="";
							marray_email=myCateForCheck.getEmailAddress().toString();
							int index=	mEmail.indexOf(marray_email);
							mEmail.remove(index);
						}else{
							holder.ivRight.setVisibility(View.VISIBLE);
							myCateForCheck.setCheck("Y");
							
							String marray_email="";
							marray_email=myCateForCheck.getEmailAddress().toString();
							if(marray_email.length()!=0 && !mEmail.contains(marray_email)){
								mEmail.add(marray_email);
							}
						}
						listSubCategories.set(position, myCateForCheck);
						adapter.notifyDataSetChanged();
					}
				});	
			return convertView;
		}
	}

	
}