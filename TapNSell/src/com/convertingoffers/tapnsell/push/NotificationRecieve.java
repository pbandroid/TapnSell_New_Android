package com.convertingoffers.tapnsell.push;


import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.MakeOffer.AcceptOffer_Buyer;
import com.convertingoffers.tapnsell.MakeOffer.ChaChingActivity;
import com.convertingoffers.tapnsell.MakeOffer.DeclineOffer_Buyer;
import com.convertingoffers.tapnsell.MakeOffer.DeclineOffer_Seller;
import com.convertingoffers.tapnsell.MakeOffer.Exp_buy_OfferActivity;
import com.convertingoffers.tapnsell.MakeOffer.Exp_sell_OfferActivity;
import com.convertingoffers.tapnsell.MakeOffer.OfferRecieveActivity;
import com.convertingoffers.tapnsell.Meetup.ChouseMeetupActivity;
import com.convertingoffers.tapnsell.Meetup.SuggestMeetupActivity;
import com.convertingoffers.tapnsell.Shop.ChatActivity;
import com.convertingoffers.tapnsell.TapboardFooter.Footer_ReviewUser;
import com.convertingoffers.tapnsell.TapboardSell.ItemToShip_Activity;
import com.convertingoffers.tapnsell.TapboardSell.OpenCaseActivity;
import com.convertingoffers.tapnsell.TapboardSell.ResolutionChatActivity;
import com.convertingoffers.tapnsell.TapboardShop.TapBoard_Order_Status;
import com.convertingoffers.tapnsell.util.ConnectionDetector;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.Dialog_inapp_Activity;
import com.convertingoffers.tapnsell.util.RequestRefund_Dialog_Activity;
import com.convertingoffers.tapnsell.util.UserFunctions;

public class NotificationRecieve extends BroadcastReceiver {
	public String KEY_SUCCESS = "successfull",KEY_SUCCESS_STATUS = "true",strmsg="";
	SharedPreferences pref;
	Editor editor;
	int cnt=0;
	ArrayList<String> mItemList = new ArrayList<String>();
	String userid="",orderid="",approval="N",caseid="",sound="Y",image,strcnt="",username="",msg="",type = "",item_price="",name="", offerid="",price="",itemname="",fromid="",itemid="",itemimage="",isbuyer="";
	Custom_Dialog custom_dialog;
	public ConnectionDetector cd;
	public ProgressDialog progressDialog;
	Context con;
	boolean IsappOpen=false;
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.i("C2DM", "action " + action);
		if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
			final String registrationId = intent.getStringExtra("registration_id");
			final String error = intent.getStringExtra("error");
			Log.i("C2DM", "Received registration ID " + registrationId + " with error " + error);
/////
			//save the registration ID in shared prefs
			Log.e("Notification get", " "+registrationId);
			Log.e("Shahil", " "+registrationId);
			pref =PreferenceManager.getDefaultSharedPreferences(context);
			editor = pref.edit();
			cd = new ConnectionDetector(context);
			editor.putString("registrationID", ""+registrationId);
			editor.commit();
			custom_dialog=new Custom_Dialog();
			con=context;
			pref.edit().putString("registrationID", registrationId).commit();
			 
		
			String receiver = context.getPackageName() + ".push.GCMIntentService";
			Log.i("C2DM", "Received registration ID " + registrationId + " with error " + receiver);
			intent.setClassName(context, receiver);
			intent.putExtra("registrationID", registrationId);
			context.startService(intent);
		} else if("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
			handleMessage(context, intent);
		}
	}
	
	private void handleMessage(Context context, Intent intent) {
		try{
		
			IsappOpen=isAppRunning(context);
			
			msg=intent.getStringExtra("text");
			type =intent.getStringExtra("type");
			offerid=intent.getStringExtra("offerid");
			price=intent.getStringExtra("price");
			itemid=intent.getStringExtra("itemid");
			itemname =intent.getStringExtra("itemname");
			item_price =intent.getStringExtra("item_price");
			itemimage=UserFunctions.localImageUrl;
			itemimage =itemimage+intent.getStringExtra("itemimage");
			fromid =intent.getStringExtra("fromid");
			isbuyer =intent.getStringExtra("isbuyer");
			username =intent.getStringExtra("username");
			orderid =intent.getStringExtra("orderid");
			name=intent.getStringExtra("name");
			image=intent.getStringExtra("image");
			caseid=intent.getStringExtra("caseid");
			sound=intent.getStringExtra("sound");
			custom_dialog=new Custom_Dialog();
			Log.e("cnt ", " "+cnt);
			Log.e("msg", msg);
			Log.e("type"," "+ type);
			Log.e("offerid noti"," "+ offerid);
			Log.e("price"," "+ price);
			Log.e("itemname"," "+ itemname);
			Log.e("fromid"," "+ fromid);
			Log.e("itemid"," "+ itemid);
			Log.e("itemimage"," "+ itemimage);
			Log.e("isbuyer"," "+ isbuyer);
			Log.e("username"," "+ username);
			Log.e("orderid"," "+ orderid);
			Log.e("name"," "+ name);
			Log.e("image"," "+ image);
			Log.e("caseid"," "+ caseid);
			
			if(type.length()!=0 && type.equals("offer_remainder")){   
				Intent intent_exp;
	        	if(isbuyer.equals("Y")){
	        	intent_exp =new Intent(context,Exp_buy_OfferActivity.class);	
         		}else{
	        	intent_exp =new Intent(context,Exp_sell_OfferActivity.class);	
	        	}
	        	intent_exp.putExtra("notification_text", msg);
	        	intent_exp.putExtra("notification_type", type);
	        	intent_exp.putExtra("notification_offerid",offerid );
	        	intent_exp.putExtra("notification_price", price);
    	        intent_exp.putExtra("notification_itemname", itemname);
    	        intent_exp.putExtra("notification_fromid", fromid);
    	        intent_exp.putExtra("notification_item_price", item_price);
    	        intent_exp.putExtra("notification_itemid", itemid);
    	        intent_exp.putExtra("notification_itemimage",itemimage );
    	        intent_exp.putExtra("notification_isbuyer", isbuyer);
    	        context.startActivity(intent_exp);
	        }else if(type.length()!=0 && type.equals("payment_success")){   
	        	Intent i = new Intent("com.tapnsell.paymentcompleted");
	            context.sendBroadcast(i);
	        }else if(type.length()!=0 && type.equals("accept_offer") &&isbuyer.equals("N")){
	        	notificationCodeWithoutIntent(context, msg);
	        }else if(type.length()!=0 && type.equals("general")){
	        	notificationCodeWithoutIntent(context, msg);
	        }else if(type.length()!=0 && type.equals("confirm_meetup")){
	        	notificationCodeWithoutIntent(context, msg);
	        }else{
	        	pref =PreferenceManager.getDefaultSharedPreferences(context);
				editor = pref.edit();
				strcnt= pref.getString("cnt", "0");
				cnt=Integer.parseInt(strcnt);
				cnt=cnt+1;
				editor.putString("cnt", ""+cnt);
				editor.commit();
				notificationCode(context,msg);
	        
	        }

			
			
			//}
			/*
			String strType = "";
			String strTitle = "";
			String strLngId = "";
			String strLngName = "";
			String strCatId = "";
			String strCatName = "";
			String strUsrType = "";
			String strClassId = "";
			String strClassName = "";
			Log.e("recieve", intent.toString());
			if (intent.hasExtra("type")) {
				strType = intent.getStringExtra("type");
				Log.i("type", strType);
			}
			if (intent.hasExtra("title")) {
				strTitle = intent.getStringExtra("title");
				Log.i("title", strTitle);
			}
			if(intent.hasExtra("language_id")){
				strLngId = intent.getStringExtra("language_id");
				Log.i("Language Id", strLngId);
			}
			if(intent.hasExtra("language_name")){
				strLngName = intent.getStringExtra("language_name");
				Log.i("Language Name", strLngName);
			}
			if(intent.hasExtra("category_id")){
				strCatId = intent.getStringExtra("category_id");
				Log.i("category id", strCatId);
			}
			if(intent.hasExtra("category_name")){
				strCatName = intent.getStringExtra("category_name");
				Log.i("category name", strCatName);
			}
			if(intent.hasExtra("user_type")){
				strUsrType = intent.getStringExtra("user_type");
				Log.i("usertype", strUsrType);
			}
			if(intent.hasExtra("class_id")){
				strClassId = intent.getStringExtra("class_id");
				Log.i("classid", strClassId);
			}
			if(intent.hasExtra("class_name")){
				strClassName = intent.getStringExtra("class_name");
				Log.i("classname", strClassName);
			}
			if(isAppRunning(context)){
				Intent ntfintnt = new Intent(context, NotificationDialog.class);
				ntfintnt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ntfintnt.putExtra("msg", strTitle);
				context.startActivity(ntfintnt);
			} else {
				SharedPreferences prefere = context.getSharedPreferences(UserFunctions.PREFS_NAME, 0);
				SharedPreferences.Editor editor = prefere.edit();
				if(!strType.equalsIgnoreCase("friend_request")){
					editor.putString("USERTYPE", strUsrType);
					editor.putString("LANGUAGEID", strLngId);
					editor.putString("CATEGORYID", strCatId);
					editor.putString("LANGUAGE", strLngName);
					editor.putString("CATEGORY", strCatName);
					editor.commit();
				}
			}
		*/
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
/*	  @SuppressWarnings("static-access")
	public   void InappButtondialogCode(String msg,final Context context, final Intent i) {
		   	
		   	// custom dialog
		   	final Dialog  dialog = new Dialog(context);
		   	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		   	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		   	dialog.setContentView(R.layout.dialog_inapp);
		   	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		       lp.copyFrom(dialog.getWindow().getAttributes());
		       lp.width = lp.MATCH_PARENT;
		       lp.height = lp.MATCH_PARENT;
		       dialog.getWindow().setAttributes(lp);
		       dialog.show();
		 
		   	tvmsg.setText(msg);
		   	rlBottam.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					context.startActivity(i);
				}
			});
		   	
		   	
		   	ivClose.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					dialog.dismiss();
				}
			});
		   }
		   
	*/
	@SuppressWarnings({ "deprecation", "static-access", "null" })
	@SuppressLint("NewApi") 
	
	private void notificationCode(Context context2, String msg) {
		
		NotificationManager nm = 
	          (NotificationManager) context2.getSystemService(context2.NOTIFICATION_SERVICE);
	 
	        int icon = R.drawable.ic_launcher;
	        CharSequence tickerText = "TapNSell";
	        long when = System.currentTimeMillis();
	        Notification notification = null; 
	        NotificationCompat.Builder builder = new NotificationCompat.Builder(context2);
	        
	        if(Build.VERSION.SDK_INT < 11 ){
	            new Notification( icon, tickerText, when);
	        }

	        Context context = context2.getApplicationContext();
	        CharSequence contentTitle = "TapNSell";
	        CharSequence contentText = msg;
	        Intent notificationIntent = null,inappIntent=null; 
	        
	    	inappIntent =new Intent(context2,Dialog_inapp_Activity.class);
	        inappIntent.putExtra("Inapp_text", ""+msg);
	        inappIntent.putExtra("Inapp_type", type);
	        inappIntent.putExtra("Inapp_offerid",offerid );
	        inappIntent.putExtra("Inapp_price", price);
	        inappIntent.putExtra("Inapp_itemname", itemname);
	        inappIntent.putExtra("Inapp_fromid", fromid);
	        inappIntent.putExtra("Inapp_itemid", itemid);
	        inappIntent.putExtra("Inapp_itemimage",itemimage );
	        inappIntent.putExtra("Inapp_isbuyer", isbuyer);
	        inappIntent.putExtra("Inapp_item_price", item_price);
	        inappIntent.putExtra("Inapp_username", username);
	        inappIntent.putExtra("Inapp_orderid", orderid);
	        inappIntent.putExtra("Inapp_notification_caseid", caseid);
	        inappIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        
	       Log.e("type", "type "+type);
	        if(type!=null && !type.equals("") && type.equals("make_offer")){
	        	if(IsappOpen){
	        		inappIntent.putExtra("NotificationNo", "1");
	        		context.startActivity(inappIntent);
	        	}
	        	notificationIntent =new Intent(context2,OfferRecieveActivity.class);   	
	        }else if(type!=null && !type.equals("") && type.equals("accept_offer")){
	         	if(isbuyer.equals("Y")){
	         		if(IsappOpen){
		        		inappIntent.putExtra("NotificationNo", "2");
		        		context.startActivity(inappIntent);
		        	}
	         	notificationIntent =new Intent(context2,AcceptOffer_Buyer.class);	
	         	}
	        }else if(type!=null && !type.equals("") && type.equals("counter_offer")){
	        	if(IsappOpen){
	        		inappIntent.putExtra("NotificationNo", "3");
	        		context.startActivity(inappIntent);
	        	}
	        	
	        	notificationIntent =new Intent(context2,OfferRecieveActivity.class);  
	        	
	        }else if(type!=null && !type.equals("") && type.equals("shipped")){
	        	if(IsappOpen){
	        		inappIntent.putExtra("NotificationNo", "4");
	        		context.startActivity(inappIntent);
	        	}
	        	
	        	notificationIntent =new Intent(context2,TapBoard_Order_Status.class);   
	        }else if(type!=null && !type.equals("") && type.equals("createcase")){

	        	if(IsappOpen){
	        		inappIntent.putExtra("NotificationNo", "5");
	        		context.startActivity(inappIntent);
	        	}
	        	notificationIntent =new Intent(context2,OpenCaseActivity.class);   
	        }else if(type!=null && !type.equals("") && type.equals("requesttracking")){
	        	if(IsappOpen){inappIntent.putExtra("NotificationNo", "6");context.startActivity(inappIntent);}
	        	
	        	notificationIntent =new Intent(context2,ItemToShip_Activity.class);   
	        }else if(type!=null && !type.equals("") && type.equals("decline_offer")){   
	        	if(isbuyer.equals("N")){
	        		
	        	if(IsappOpen){inappIntent.putExtra("NotificationNo", "7");context.startActivity(inappIntent);}
		        	
         		notificationIntent =new Intent(context2,DeclineOffer_Seller.class);	
         	}else{
         		if(IsappOpen){inappIntent.putExtra("NotificationNo", "8");context.startActivity(inappIntent);}
  		      
         		notificationIntent =new Intent(context2,DeclineOffer_Buyer.class);	
         	}
	        }else if(type!=null && !type.equals("") && type.equals("offer_remainder")){   
	        	if(isbuyer.equals("Y")){
	        		
	        		if(IsappOpen){inappIntent.putExtra("NotificationNo", "9");context.startActivity(inappIntent);}
	    		      
         		notificationIntent =new Intent(context2,Exp_buy_OfferActivity.class);	
	        	}else{
	        	if(IsappOpen){inappIntent.putExtra("NotificationNo", "10");context.startActivity(inappIntent);}
		    		  
	        	notificationIntent =new Intent(context2,Exp_sell_OfferActivity.class);	
	        	}
			} else if (type!=null && !type.equals("") && type.equals("place_order")) {
				if(IsappOpen){inappIntent.putExtra("NotificationNo", "11");context.startActivity(inappIntent);}
			    
				notificationIntent = new Intent(context2, ChaChingActivity.class);
			}else if (type!=null && !type.equals("") && type.equals("closecase")) {
				if(IsappOpen){inappIntent.putExtra("NotificationNo", "12");context.startActivity(inappIntent);}
				   
				notificationIntent = new Intent(context2, OpenCaseActivity.class);
			}else if (type!=null && !type.equals("") && type.equals("meetup_request")) {
				if(IsappOpen){inappIntent.putExtra("NotificationNo", "13");context.startActivity(inappIntent);}
				
				notificationIntent = new Intent(context2, ChouseMeetupActivity.class);
			}else if (type!=null && !type.equals("") && type.equals("meetup_request_buyer")) {
				if(IsappOpen){inappIntent.putExtra("NotificationNo", "14");context.startActivity(inappIntent);}
				
				notificationIntent = new Intent(context2, SuggestMeetupActivity.class);
			}else if(type!=null && !type.equals("") && type.equals("refund_request")){
				if(IsappOpen){inappIntent.putExtra("NotificationNo", "15");context.startActivity(inappIntent);}
				
	        	notificationIntent = new Intent(context2, RequestRefund_Dialog_Activity.class);
	        }else if(type!=null && !type.equals("") && type.equals("case_close_request")){
	        	if(IsappOpen){inappIntent.putExtra("NotificationNo", "16");context.startActivity(inappIntent);}
				
	        	notificationIntent = new Intent(context2, RequestRefund_Dialog_Activity.class);
	        }else if(type!=null && !type.equals("") && type.equals("addreview")){
	        	if(IsappOpen){inappIntent.putExtra("NotificationNo", "17");context.startActivity(inappIntent);}
				
	        	notificationIntent = new Intent(context2, Footer_ReviewUser.class);
	        }else if (type!=null && !type.equals("") && type.equals("meetup_request_seller")) {
	        	if(IsappOpen){inappIntent.putExtra("NotificationNo", "18");context.startActivity(inappIntent);}
				
				notificationIntent = new Intent(context2, SuggestMeetupActivity.class);
			}else if (type!=null && !type.equals("") && type.equals("new_message")) {
			
				ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
  			  	List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
  			  	String activityname = taskInfo.get(0).topActivity.getClassName();
            	
            	if(activityname.equalsIgnoreCase("com.convertingoffers.tapnsell.Shop.ChatActivity")){
            		Intent	updateintent = new Intent();
					updateintent.setAction("Abc");
					context.sendBroadcast(updateintent);
			}else if (type!=null && !type.equals("") && type.equals("new_messagecase")) {
			
				ActivityManager am_case = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
  			  	List<RunningTaskInfo> taskInfo_case = am_case.getRunningTasks(1);
  			  	String activityname_case = taskInfo_case.get(0).topActivity.getClassName();
            	
            	if(activityname_case.equalsIgnoreCase("com.convertingoffers.tapnsell.TapboardSell.ResolutionChatActivity")){
            		Intent	updateintent = new Intent();
					updateintent.setAction("xyz");
					context.sendBroadcast(updateintent);
			}else{
				if(IsappOpen){inappIntent.putExtra("NotificationNo", "20");context.startActivity(inappIntent);}
				
				notificationIntent = new Intent(context2, ResolutionChatActivity.class);
	        }
				
				
			}else{
	        Log.e("type is null", "type is null");	
	        
	        }
	        
	        
	    
	        
	        
	        notificationIntent.putExtra("notification_text", ""+msg);
	        notificationIntent.putExtra("notification_type", type);
	        notificationIntent.putExtra("notification_offerid",offerid );
	        notificationIntent.putExtra("notification_price", price);
	        Log.e("price", ""+price);
	        notificationIntent.putExtra("notification_itemname", itemname);
	        notificationIntent.putExtra("notification_fromid", fromid);
	        notificationIntent.putExtra("notification_itemid", itemid);
	        notificationIntent.putExtra("notification_itemimage",itemimage );
	        Log.e("itemimage", " "+itemimage);
	        notificationIntent.putExtra("notification_isbuyer", isbuyer);
	        notificationIntent.putExtra("notification_item_price", item_price);
	        notificationIntent.putExtra("notification_username", username);
	        notificationIntent.putExtra("orderid", orderid);
	        notificationIntent.putExtra("notification_caseid", caseid);
	        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	       // notificationIntent.putExtra("name", nname);
	       
			
	        PendingIntent contentIntent = 
	            PendingIntent.getActivity(context2, cnt, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_CANCEL_CURRENT);
	        if(Build.VERSION.SDK_INT >= 11 ){
	        	if(sound.equals("Y")){
	        		notification= builder.setAutoCancel(true)
		             .setDefaults(Notification.DEFAULT_ALL)
		             .setWhen(System.currentTimeMillis())         
		             .setSmallIcon(icon)
		             .setTicker(tickerText)            
		             .setContentTitle(contentTitle)
		             .setContentText(contentText)
		             .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND)
		             .setContentIntent(contentIntent).build();
		        		
	        	}else{
	        		notification= builder.setAutoCancel(true)
		             .setWhen(System.currentTimeMillis())         
		             .setSmallIcon(icon)
		             .setTicker(tickerText)            
		             .setContentTitle(contentTitle)
		             .setContentText(contentText)
		             .setContentIntent(contentIntent).build();
	        	}
	        	Log.e("test 11", "tset11");
	        	
	             //.setContentInfo("Info").build();
	        }else{
	        notification.setLatestEventInfo(
	            context, 
	            contentTitle, 
	            contentText, 
	            contentIntent);
	        }
	        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;
	        nm.notify(cnt, notification );
	}
	}
	

	 
	
	private void notificationCodeWithoutIntent(Context context2, String msg) {
		
		 @SuppressWarnings("static-access")
		NotificationManager nm = 
	          (NotificationManager) context2.getSystemService(context2.NOTIFICATION_SERVICE);
	 
	        int icon = R.drawable.ic_launcher;
	        CharSequence tickerText = "TapNSell";
	        long when = System.currentTimeMillis();
	        Notification notification = null; 
	        NotificationCompat.Builder builder = new NotificationCompat.Builder(context2);

	        if(Build.VERSION.SDK_INT < 11 ){
	            new Notification( icon, tickerText, when);
	        }
/*	        try {
	            Uri notifications = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	            Ringtone r = RingtoneManager.getRingtone(context2.getApplicationContext(), notifications);
	            r.play();
	        } catch (Exception e) {}*/
	        Context context = context2.getApplicationContext();
	        CharSequence contentTitle = "TapNSell";
	        CharSequence contentText = msg;
	        Intent notificationIntent =  new Intent(); 
	      
	        PendingIntent contentIntent = 
	            PendingIntent.getActivity(context2, cnt, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_CANCEL_CURRENT);
	        if(Build.VERSION.SDK_INT >= 11 ){	
	        	if(sound.equals("Y")){
        		notification= builder.setAutoCancel(true)
	             .setDefaults(Notification.DEFAULT_ALL)
	             .setWhen(System.currentTimeMillis())         
	             .setSmallIcon(icon)
	             .setTicker(tickerText)            
	             .setContentTitle(contentTitle)
	             .setContentText(contentText)
	             .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE| Notification.DEFAULT_SOUND)
	             .setContentIntent(contentIntent).build();
	        		
       	}else{
       		notification= builder.setAutoCancel(true)
	             .setWhen(System.currentTimeMillis())         
	             .setSmallIcon(icon)
	             .setTicker(tickerText)            
	             .setContentTitle(contentTitle)
	             .setContentText(contentText)
	             .setContentIntent(contentIntent).build();
       	}
	             //.setContentInfo("Info").build();
	        }else{
	        notification.setLatestEventInfo(
	            context, 
	            contentTitle, 
	            contentText, 
	            contentIntent);
	        }
	        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;
	        nm.notify(cnt, notification );
	}

	
	public boolean isAppRunning(Context context){
		boolean isAppRunning = false;
		ActivityManager activityManager = (ActivityManager) context.getSystemService( Context.ACTIVITY_SERVICE );
	    List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
	    if(services.size()>0){
	    	if (services.get(0).topActivity.getPackageName().toString()
		            .equalsIgnoreCase(context.getPackageName().toString())) {
		    	isAppRunning = true;
		    }
	    }
	    	    
	    return isAppRunning;
	}

}
