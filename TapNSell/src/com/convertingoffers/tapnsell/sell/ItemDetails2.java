package com.convertingoffers.tapnsell.sell;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class ItemDetails2 extends BaseActivity implements OnClickListener {
	LinearLayout llDelivery;
	EditText evAskingPrice, evQty;
	TextView tvNext;
	RelativeLayout rlNext;
	ImageView ivItemdetails_arrow_up, ivItemdetails_arrow_down;
	String strAskingPrice="",strQty="",strDelivary="",strStatus="",d_option1="1";
	ImageView ivBoth,ivShip,ivPick;
	Custom_Dialog custom_dialog;
	Context context;
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_detail2);
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
		
		strAskingPrice=pref.getString("asking_price", "");
		strQty=pref.getString("quantity", "");
		
		d_option1=pref.getString("delevery_option", "1");
		
		if(d_option1.equals("1")){
			if (Build.VERSION.SDK_INT >= 16)
				llDelivery.setBackground(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_1));
			else
				llDelivery.setBackgroundDrawable(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_1));
	
		}else if(d_option1.equals("2")){
	
			if (Build.VERSION.SDK_INT >= 16)
				llDelivery.setBackground(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_2));
			else
				llDelivery.setBackgroundDrawable(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_2));
	
		}else{
			if (Build.VERSION.SDK_INT >= 16)
				llDelivery.setBackground(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_3));
			else
				llDelivery.setBackgroundDrawable(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_3));
	
		}
	
		
		evAskingPrice.setText(""+strAskingPrice);
		if(strQty.length()==0){
			evQty.setText("1");
		}else{
		evQty.setText(""+strQty);
		}
	
		
		ivBack.setOnClickListener(this);
		rlNext.setOnClickListener(this);
		ivShip.setOnClickListener(this);
		ivPick.setOnClickListener(this);
		ivBoth.setOnClickListener(this);
		ivItemdetails_arrow_down.setOnClickListener(this);
		ivItemdetails_arrow_up.setOnClickListener(this);

	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		custom_dialog=new Custom_Dialog();
		ivItemdetails_arrow_up = (ImageView) findViewById(R.id.ivItemdetails_arrow_up);
		ivItemdetails_arrow_down = (ImageView) findViewById(R.id.ivItemdetails_arrow_down);
		evAskingPrice = (EditText) findViewById(R.id.evAskingPrice);
		evQty = (EditText) findViewById(R.id.evQty);
		rlNext = (RelativeLayout) findViewById(R.id.rlNext);
		ivShip = (ImageView) findViewById(R.id.ivShip);
		ivPick = (ImageView) findViewById(R.id.ivPick);
		ivBoth = (ImageView) findViewById(R.id.ivBoth);
		tvNext= (TextView) findViewById(R.id.tvNext);
		llDelivery= (LinearLayout) findViewById(R.id.llDelivery);
	}
	// soft keyboard dispatch event

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
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rlNext:
		
			strAskingPrice = evAskingPrice.getText().toString().trim();
			strQty = evQty.getText().toString().trim();
			if(strQty.equals("0") || strQty.length()==0 ||strQty.equals("00") ||strQty.equals("000")){
				evQty.setText("1");
				strQty="1";
			}
			if (strAskingPrice.length() == 0) {
//				ValidationSingleButton("Please enter valid asking price");
				Custom_Dialog.dialogCode(2, null, "Please enter valid asking price.", context);
			} else {
				String d_option="1";
				if(strDelivary.equals("Ship")){
					d_option="1";
				}else if(strDelivary.equals("Pick")){
					d_option="2";
				}else{
					d_option="1,2";
				}
				editor.putString("asking_price", strAskingPrice); // Storing string
				editor.putString("quantity", strQty);// Storing string
				editor.putString("delevery_option", d_option);// Storing
				editor.commit();
				if(strStatus.length()==0){
					Intent i  = new Intent(ItemDetails2.this, ItemDetails3.class);
					startActivity(i);	
				}else{
					Intent i  = new Intent(ItemDetails2.this, PriviewListing.class);
					startActivity(i);	
					finish();
				}
			}
			break;
		case R.id.ivShip:
			strDelivary="Ship";
			if (Build.VERSION.SDK_INT >= 16)
				llDelivery.setBackground(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_1));
			else
				llDelivery.setBackgroundDrawable(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_1));
	
			break;
	
		case R.id.ivPick:
			strDelivary="Pick";
			if (Build.VERSION.SDK_INT >= 16)
				llDelivery.setBackground(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_2));
			else
				llDelivery.setBackgroundDrawable(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_2));
	
			
			
			break;
		case R.id.ivBoth:
			strDelivary="Both";
			
			if (Build.VERSION.SDK_INT >= 16)
				llDelivery.setBackground(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_3));
			else
				llDelivery.setBackgroundDrawable(ItemDetails2.this.getResources().getDrawable(R.drawable.id2_3));
	
			
			
			break;
		case R.id.ivBack:
			if(strStatus.length()==0){
				finish();
			}else{
				Intent i  = new Intent(ItemDetails2.this, PriviewListing.class);
				startActivity(i);
				finish();
			}
			break;
		case R.id.ivItemdetails_arrow_up:

			String qty = evQty.getText().toString().trim();
			if (qty.length() == 0) {
				evQty.setText("1");
			} else {
				try {
					int intQty = Integer.parseInt(qty);
					intQty = intQty + 1;
					evQty.setText("" + intQty);
				} catch (NumberFormatException e) {
					evQty.setText("1");
					e.printStackTrace();
				}

			}

			break;
		case R.id.ivItemdetails_arrow_down:
			String qtydown = evQty.getText().toString().trim();

			if (qtydown.length() == 0 || qtydown.equals("1")) {
				evQty.setText("1");
			} else {
				try {
					int intQty = Integer.parseInt(qtydown);
					intQty = intQty - 1;
					evQty.setText("" + intQty);
				} catch (NumberFormatException e) {
					evQty.setText("1");
					e.printStackTrace();
				}
			}

			break;

		default:
			break;
		}

	}
	
/*	private void ValidationSingleButton(String msg) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemDetails2.this);

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
