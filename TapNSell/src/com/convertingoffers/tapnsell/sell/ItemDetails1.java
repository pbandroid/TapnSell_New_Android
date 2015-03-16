package com.convertingoffers.tapnsell.sell;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.convertingoffers.tapnsell.HomeActivity;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class ItemDetails1 extends BaseActivity implements OnClickListener {
	EditText evitem_details1_evname, item_details1_evdec;
	TextView tvNext;
	RelativeLayout rlNext;
	String strName="",strDecs="",strCondition="New",strStatus="";
	ImageView ivNew,ivUsed;
	LinearLayout llCondition;
	String cond1="1";
	Custom_Dialog custom_dialog;
	Context context;
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_detail1);
		context=this;
		tvHeader.setText("Item details");
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
		strStatus = bundle.getString("Priview");
		}
		if(strStatus.length()!=0){
			tvNext.setText("Update");
		}else{
			tvNext.setText("Next");
		}
		strName=pref.getString("item_name", "");
		strDecs=pref.getString("item_description", "");
		cond1=pref.getString("item_condition", "2");
		evitem_details1_evname.setText(""+strName);
		item_details1_evdec.setText(""+strDecs);
		
		evitem_details1_evname.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
	

		if(cond1.equals("1")){
			if (Build.VERSION.SDK_INT >= 16)
				llCondition.setBackground(getResources().getDrawable(R.drawable.id1_2));
			else
				llCondition.setBackgroundDrawable(getResources().getDrawable(R.drawable.id1_2));
			
		}else {
			if (Build.VERSION.SDK_INT >= 16)
				llCondition.setBackground(getResources().getDrawable(R.drawable.id1_1));
			else
				llCondition.setBackgroundDrawable(getResources().getDrawable(R.drawable.id1_1));
			
		}
		ivBack.setOnClickListener(this);
		rlNext.setOnClickListener(this);
		ivNew.setOnClickListener(this);
		ivUsed.setOnClickListener(this);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		
		custom_dialog= new Custom_Dialog();
		tvNext= (TextView) findViewById(R.id.tvNext);
		evitem_details1_evname = (EditText) findViewById(R.id.evitem_details1_evname);
		item_details1_evdec = (EditText) findViewById(R.id.item_details1_evdec);
		rlNext = (RelativeLayout) findViewById(R.id.rlNext);
		ivNew = (ImageView) findViewById(R.id.ivNew);
		ivUsed = (ImageView) findViewById(R.id.ivUsed);
		llCondition= (LinearLayout) findViewById(R.id.llCondition);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rlNext:
			strName=evitem_details1_evname.getText().toString().trim();
			strDecs=item_details1_evdec.getText().toString().trim();
			
			if(strName.length()==0 && strDecs.length()==0){
			//	ValidationSingleButton("Please enter Item name and description.");
				Custom_Dialog.dialogCode(2, null, "Please enter Item name and description.", context);
			}else if(strName.length()==0){
				Custom_Dialog.dialogCode(2, null, "Please enter Item name.", context);
				//ValidationSingleButton("Please enter Item name.");
			}else if(strDecs.length()==0){
//				ValidationSingleButton("Please enter description.");
				Custom_Dialog.dialogCode(2, null, "Please enter description.", context);
			} else{
				String cond="1";
				if(strCondition.equals("New")){
					 cond="1";	
				}else{
					 cond="2";
				}
				
				editor.putString("item_name", strName); // Storing string
				editor.putString("item_description", strDecs);// Storing string
				editor.putString("item_condition", cond);// Storing string
				editor.commit();	
				if(strStatus.length()==0){
				Intent i  = new Intent(ItemDetails1.this, ItemDetails2.class);
				startActivity(i);
				}else{
					Intent i  = new Intent(ItemDetails1.this, PriviewListing.class);
					startActivity(i);
					finish();
				}
				
			}
			break;
		case R.id.ivNew:
			strCondition="New";
			if (Build.VERSION.SDK_INT >= 16)
				llCondition.setBackground(getResources().getDrawable(R.drawable.id1_2));
			else
				llCondition.setBackgroundDrawable(getResources().getDrawable(R.drawable.id1_2));
			
			break;
		case R.id.ivBack:
			if(strStatus.length()==0){
				Intent i  = new Intent(ItemDetails1.this, HomeActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				finish();
			}else{
				Intent i  = new Intent(ItemDetails1.this, PriviewListing.class);
				startActivity(i);
				finish();
			}
			break;
		case R.id.ivUsed:
			strCondition="Used";
			if (Build.VERSION.SDK_INT >= 16)
				llCondition.setBackground(getResources().getDrawable(R.drawable.id1_1));
			else
				llCondition.setBackgroundDrawable(getResources().getDrawable(R.drawable.id1_1));
			
			break;
		default:
			break;
		}
	}
	// soft keyboard dispatch event

	@SuppressLint("InlinedApi") @Override
	public void onBackPressed() {
	super.onBackPressed();
	if(strStatus.length()==0){
		Intent i  = new Intent(ItemDetails1.this, HomeActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
		finish();
	}else{
		Intent i  = new Intent(ItemDetails1.this, PriviewListing.class);
		startActivity(i);
		finish();
	}
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		View view = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (view instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
		return ret;
	}
/*	private void ValidationSingleButton(String msg) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemDetails1.this);

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

}
