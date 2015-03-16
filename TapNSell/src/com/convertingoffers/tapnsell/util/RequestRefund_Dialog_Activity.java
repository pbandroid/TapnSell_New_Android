package com.convertingoffers.tapnsell.util;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapboardSell.MyListingActive_Sold_Expire_Activity;

public class RequestRefund_Dialog_Activity extends BaseActivity {
	Context context_dialog;
   	ImageView ivOK;
   	TextView tvMsg;
   	String msg,approval="N",orderid="",type="",isbuyer="",caseid="";
   	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.dialog_custom);
	
	
	userid = pref.getString("UserID", "");
	Bundle bundle = getIntent().getExtras();
	if(bundle!=null){
		
		msg = bundle.getString("notification_text");
		orderid=bundle.getString("orderid");
		type=bundle.getString("notification_type");
		isbuyer=bundle.getString("notification_isbuyer");
		caseid=bundle.getString("notification_caseid");
	}
   	
   	// custom dialog
   	tvMsg.setText(msg);
   	ivOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(RequestRefund_Dialog_Activity.this,MyListingActive_Sold_Expire_Activity.class);
				i.putExtra("come_from_refund", "true");
				startActivity(i);
			
			}
		});

   
	
	}
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		
		ivOK=(ImageView)findViewById(R.id.ivOK);
		tvMsg=(TextView)findViewById(R.id.tvMsg);
		 context_dialog=this;
	}
	
	
}
