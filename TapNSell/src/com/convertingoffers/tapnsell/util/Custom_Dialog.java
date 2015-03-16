package com.convertingoffers.tapnsell.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.convertingoffers.tapnsell.R;

public class Custom_Dialog {

	 static Dialog dialog;

    @SuppressWarnings("static-access")
	public static  void dialogCode(final int status_code,final Intent i,final String msg,final Context context) {
    	
    	// custom dialog
    	Log.e("testdialogCode", "testdialogCode");
    	  dialog = new Dialog(context);
    	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	dialog.setContentView(R.layout.dialog_custom);
    	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = lp.MATCH_PARENT;
        lp.height = lp.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    	ImageView ivOK = (ImageView) dialog.findViewById(R.id.ivOK);
    	TextView tvMsg= (TextView) dialog.findViewById(R.id.tvMsg);
    	tvMsg.setText(msg);
    	ivOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(status_code==0){
					dialog.dismiss();
					context.startActivity(i);
				}else if(status_code==1){
					dialog.dismiss();
					((Activity) context).finish();
				}else if(status_code==2){
					dialog.dismiss();
				}else if(status_code==3){
					dialog.dismiss();
					context.startActivity(i);
					((Activity) context).finish();
				}else if(status_code==4){
					dialog.dismiss();
					context.startActivity(i);
				}else{
					dialog.dismiss();
				}
			}
		});
    	
    }
    
}
