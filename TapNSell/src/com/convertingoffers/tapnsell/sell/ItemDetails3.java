package com.convertingoffers.tapnsell.sell;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.Modal.CategoryModal;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class ItemDetails3 extends BaseActivity implements OnClickListener {
	Context con;
	CateCustomAdaper adapter;
	String cate_id="0",userid="";
	//ListView lvtItemDetails;
	GridView gvCat;
	ArrayList<CategoryModal> myCateList = new ArrayList<CategoryModal>();
	Custom_Dialog custom_dialog;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_detail3);
		con=this;
		userid = pref.getString("UserID", "");
		tvHeader.setText("Item details");
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			cate_id = bundle.getString("CATEID");
			tvHeader.setText("Sub-Category");
		}else{
			cate_id="0";
			tvHeader.setText("Select Category");
		}
	
		myCateList.clear();
		if(cd.checkConnection()){
			new CategoryListBackTask().execute("");
		}else{
			Toast.makeText(ItemDetails3.this, "internet_conn_failed",Toast.LENGTH_LONG).show();
		}
		ivBack.setOnClickListener(this);

	
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		custom_dialog = new Custom_Dialog();
		context = this;
		gvCat = (GridView) findViewById(R.id.gvCat);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ivBack:
			finish();
		break;
		

		default:
			break;
		}

	}
	
	
	private class CategoryListBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ItemDetails3.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			JSONObject json = userFunction.mycategoryListFunction(cate_id);

			try {
				if (json!=null&&json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (res.equals(KEY_SUCCESS_STATUS)) {
						
						JSONArray jCategory = json.getJSONArray("Category");
						
						for (int i = 0; i < jCategory.length(); i++) {
						
							JSONObject c = jCategory.getJSONObject(i);
							
							String id=c.getString("id");
							String name=c.getString("name");
							String image=c.getString("image");
							String no_of_subcategory=c.getString("no_of_subcategory");
							
							int lenth_array=0;
							lenth_array=jCategory.length();
							Log.e("condition", "lenth_array%2!=0 && lenth_array!=i+1");
							if(lenth_array%2!=0 && lenth_array==i+1){
								myCateList.add(new CategoryModal("11111", "", "", ""));
								myCateList.add(new CategoryModal(id, name, image, no_of_subcategory));
							}else{
								myCateList.add(new CategoryModal(id, name, image, no_of_subcategory));
							}
							/*if(i==jCategory.length()){
								myCateList.add(new CategoryModal("1111", "", "", ""));
								myCateList.add(new CategoryModal(id, name, image, no_of_subcategory));
							}else{
								myCateList.add(new CategoryModal(id, name, image, no_of_subcategory));	
							}*/
							//myCateList.add(new CategoryModal(id, name, image, no_of_subcategory));
							
						}						
					errorMessage="true";
					}  else {
						message=json.optString("message");
						errorMessage = "false";
					}
				} else {
					errorMessage = "network";
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {
				
				Log.e("myCateList", " "+myCateList.size());
				adapter = new CateCustomAdaper(ItemDetails3.this,R.layout.item_detail3_item, myCateList);
				gvCat.setAdapter(adapter);
			} else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else {
				// ValidationSingleButton("No records found...!");
				Custom_Dialog.dialogCode(2, null, message, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
/*	private void ValidationSingleButton(String msg) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemDetails3.this);

		alertDialogBuilder.setTitle("alert");

		alertDialogBuilder.setMessage(msg).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}*/
	
	
	private class CateCustomAdaper extends
	ArrayAdapter<CategoryModal> {
		ViewHolder holder = null;
		private ArrayList<CategoryModal> listSubCategories;

		public CateCustomAdaper(Context context, int textViewResourceId,
				ArrayList<CategoryModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<CategoryModal>();
			this.listSubCategories = listSubCategories;
		}

class ViewHolder {
	TextView tvCat1;//,tvCat2;
	ImageView iv1;
}

@Override
public View getView(int position, View convertView,
		ViewGroup parent) {
		holder=null;
	
	if (convertView == null) {

		LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		convertView = vi.inflate(R.layout.item_detail3_item, null);

		holder = new ViewHolder();
		holder.tvCat1 = (TextView) convertView.findViewById(R.id.tvCat1);
	//	holder.tvCat2 = (TextView) convertView.findViewById(R.id.tvCat2);
		 holder.iv1=(ImageView)convertView.findViewById(R.id.iv1);
		convertView.setTag(holder);

	} else {
		holder = (ViewHolder) convertView.getTag();
	}
	CategoryModal myCate = listSubCategories.get(position);
	//CategoryModal myCate1 = listSubCategories.get(position+1);
	holder.tvCat1.setText(""+myCate.getName().toString());
	holder.tvCat1.setTag(myCate);
	if(position%2==0){
		holder.iv1.setVisibility(View.VISIBLE);
	}else{
		holder.iv1.setVisibility(View.GONE);
	}
///if(position%2!=0)
	holder.tvCat1.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			CategoryModal _cate = (CategoryModal) v.getTag();//listSubCategories.get(position);
			String cateid = _cate.getId();
			
			if(!cateid.equals("11111")){
				
			
			String no_of_SubCate = _cate.getNo_of_subcategory();
			if(no_of_SubCate.equals("0")){
				
			editor.putString("category_id",cateid ); // Storing string
			editor.commit();
			Intent i =new Intent(ItemDetails3.this, PriviewListing.class);
			//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
			finish();
			}else{
				
			
				Intent  i=new Intent(ItemDetails3.this, ItemDetails3.class);
				Bundle b = new Bundle();
				b.putString("CATEID", cateid);
				i.putExtras(b);
				startActivity(i);	
			}
			}
		}
	});
	
/*	holder.tvCat2.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			CategoryModal _cate = (CategoryModal) v.getTag();//listSubCategories.get(position);
			String cateid = _cate.getId();
			String no_of_SubCate = _cate.getNo_of_subcategory();
			if(no_of_SubCate.equals("0")){
				editor.putString("category_id",cateid ); // Storing string
				editor.commit();
				
				Intent i =new Intent(ItemDetails3.this, PriviewListing.class);
				startActivity(i);
			}else{
				
				
			 Intent  i=new Intent(ItemDetails3.this, ItemDetails3.class);
				Bundle b = new Bundle();
				b.putString("CATEID", cateid);
				i.putExtras(b);
				startActivity(i);
		}
		}
	});*/
	
	return convertView;
}

@Override
public int getCount() {
	
/*	if(listSubCategories.size()>0){
		return ;
	}*/
	return listSubCategories.size();
}
}
	
		
	

}
