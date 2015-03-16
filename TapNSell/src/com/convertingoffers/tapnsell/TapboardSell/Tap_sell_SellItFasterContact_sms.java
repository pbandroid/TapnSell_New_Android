package com.convertingoffers.tapnsell.TapboardSell;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapnSellApplication;
import com.convertingoffers.tapnsell.Modal.ContactsModal;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class Tap_sell_SellItFasterContact_sms extends BaseActivity implements OnClickListener{
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

	 ArrayList<String> mPhoneNo= new ArrayList<String>();
//	 String[] array = new String[mPhoneNo.size()];
	 ImageView ivMenuSkip,ivShar,ivAll;
	 EditText evSearch;
	 int textlength = 0;
	 TextView tv1;
	 boolean status_Check=true;
	 int 	  intStartNewListing=0,count=0,cnt = 0,totalNoContact;
	 Context context;
	 Custom_Dialog custom_dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sellitfaster_contact);
		
		mContactList.clear();
		ContentResolver cr1 =getContentResolver();
	    Cursor c = cr1.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	    totalNoContact=   c.getCount();
	    tv1.setText("Text message Listings to your freinds");
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
		url = bundle.getString("url");
		type= bundle.getString("type");
		msg= bundle.getString("msg");
		}
		ivMenuSkip.setVisibility(View.GONE);
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
				 adapter = new ContactCustomAdaper(Tap_sell_SellItFasterContact_sms.this, R.layout.sellitfaster_contact_item,mContactList);
					lvContact.setAdapter(adapter);
			}else{
				
				SearchContacList.clear();
				if(serch_text.length()!=0){
					serch_text = "%" + serch_text + "%";
				}
//	
				
				// Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, "display_name like ? ",new String[]{serch_text}, null);
				Cursor c = ((TapnSellApplication) getApplication()).db.rawQuery
				("SELECT * FROM cont WHERE name  LIKE '"+serch_text+"' and phone!=''", null);
				if (c != null && c.getCount() > 0) {
					if (c != null && c.getCount() > 0) {
						
						int id_colounm = c.getColumnIndex("id");
						int name_colounm = c.getColumnIndex("name");
						int phone_colounm = c.getColumnIndex("phone");
						for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
							String id = c.getString(id_colounm);
							String name = c.getString(name_colounm);
							String phone = c.getString(phone_colounm);
							   if(status_Check){
								   if(!mPhoneNo.contains(phone)){
									   mPhoneNo.add(phone);
								   }
								   SearchContacList.add(new ContactsModal(id, name, phone, "", "Y"));
								   
							}else{
								SearchContacList.add(new ContactsModal(id, name, phone, "", "N"));
								}
							}
						}
					}
				 adapter = new ContactCustomAdaper
				 (Tap_sell_SellItFasterContact_sms.this, R.layout.sellitfaster_contact_item,SearchContacList);
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
	public void onClick(View v) {
		
	switch (v.getId()) {
	case R.id.ivAll:
		/*String search="";
		search =evSearch.getText().toString().trim();*/
		
		
			if(!status_Check){
				status_Check=true;
				mPhoneNo.clear();		
				//if(search.length()!=0){
					for (int i = 0; i < SearchContacList.size(); i++) {
						
						ContactsModal search_cate=SearchContacList.get(i);
						search_cate.setCheck("Y");
						SearchContacList.set(i, search_cate);
						adapter.notifyDataSetChanged();
						String phone ="";
						phone=search_cate.getPhoneNumber().toString();
						if(phone.length()!=0 && !mPhoneNo.contains(phone)){
							mPhoneNo.add(phone);
						}
					
					}
				//}else{
					for (int i = 0; i < mContactList.size(); i++) {
						
						ContactsModal cate = mContactList.get(i);
						cate.setCheck("Y");
						mContactList.set(i, cate);
						adapter.notifyDataSetChanged();
					
						String phone ="";
						phone=cate.getPhoneNumber().toString();
						if(phone.length()!=0 && !mPhoneNo.contains(phone)){
							mPhoneNo.add(phone);
						}
						
					}
			//	}
		
			}else{
				
				status_Check=false;
				mPhoneNo.clear();	
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
			new SharsmsBackTask().execute("");	
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
		
		tv1=(TextView)findViewById(R.id.tv1);
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
			progressDialog = new ProgressDialog(Tap_sell_SellItFasterContact_sms.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
			
		}

		@Override
		protected String doInBackground(String... params) {
			{
				Cursor c = ((TapnSellApplication) getApplication()).db.rawQuery
				("SELECT id,name,phone FROM cont where phone != '' order by name asc LIMIT 50 OFFSET '"+intStartNewListing+"'", null);
				if (c != null && c.getCount() > 0) {
			
				int id_colounm = c.getColumnIndex("id");
				int name_colounm = c.getColumnIndex("name");
				int phone_colounm = c.getColumnIndex("phone");
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					String id = c.getString(id_colounm);
					String name = c.getString(name_colounm);
					String phone = c.getString(phone_colounm);
					   if(status_Check){
						   mPhoneNo.add(phone);
						   mContactList.add(new ContactsModal(id, name, phone, "", "Y"));
					}else{
						 mContactList.add(new ContactsModal(id, name, phone, "", "N"));
					}
					errorMessage="true";
				}
				}else{
					errorMessage="false";
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
//				Intent i = new Intent(Tap_sell_SellItFasterContact.this, SharActivity.class);
				Custom_Dialog.dialogCode(1,  null, "No phone numbers found.", context);	
			}else{
				adapter = new ContactCustomAdaper(Tap_sell_SellItFasterContact_sms.this
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

	
	private class SharsmsBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(Tap_sell_SellItFasterContact_sms.this);
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
			Toast.makeText(Tap_sell_SellItFasterContact_sms.this, "Please select contact...", Toast.LENGTH_LONG).show();
			}else{
				new TwillowSmsBackTask().execute("");	
			}
		}
	}
	
	
	private class TwillowSmsBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(Tap_sell_SellItFasterContact_sms.this);
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
			if (result.equals("true")) {
				
//				Intent i = new Intent(Tap_sell_SellItFasterContact.this, SharActivity.class);
				Custom_Dialog.dialogCode(1, null, "Message sent successfully", context);
//				ValidationSingleButton("Message sent successfully");
			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		} else {
//				ValidationSingleButton("");
				Custom_Dialog.dialogCode(1, null, "Error in posting", context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
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
							String marray_phone="";
							marray_phone=myCateForCheck.getPhoneNumber().toString();
							int index=	mPhoneNo.indexOf(marray_phone);
							mPhoneNo.remove(index);
						}else{
							holder.ivRight.setVisibility(View.VISIBLE);
							myCateForCheck.setCheck("Y");
							
							String marray_phone="";
							marray_phone=myCateForCheck.getPhoneNumber().toString();
							if(marray_phone.length()!=0 && !mPhoneNo.contains(marray_phone)){
								mPhoneNo.add(marray_phone);
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