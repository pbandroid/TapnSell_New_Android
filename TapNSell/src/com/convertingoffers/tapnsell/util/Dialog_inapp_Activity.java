package com.convertingoffers.tapnsell.util;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class Dialog_inapp_Activity extends Activity implements OnClickListener{
	ImageView ivClose;
	TextView tvmsg;
	RelativeLayout rlBottam;
	Context context2;
	String msg="",type="",offerid="",itemname="",to_id="",notification_item_price="",notification_price="",
	itemid="",image="",isbuyer="",user_Name="",OrderID="",Caseid="",notification_no;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.dialog_inapp);
	
	
	Bundle bundle = getIntent().getExtras();
	if(bundle!=null){
		
	msg = bundle.getString("Inapp_text");
	type = bundle.getString("Inapp_type");
	offerid=bundle.getString("Inapp_offerid");
	notification_price = bundle.getString("Inapp_price");
	itemname = bundle.getString("Inapp_itemname");
	to_id = bundle.getString("Inapp_fromid");
	itemid = bundle.getString("Inapp_itemid");
	image = bundle.getString("Inapp_itemimage");
	isbuyer = bundle.getString("Inapp_isbuyer");
	user_Name = bundle.getString("Inapp_username");
	notification_item_price = bundle.getString("Inapp_item_price");
	OrderID = bundle.getString("Inapp_orderid");
	Caseid = bundle.getString("Inapp_notification_caseid");
	notification_no = bundle.getString("NotificationNo");
	}
	
	tvmsg.setText(msg);
	ivClose.setOnClickListener(this);
	rlBottam.setOnClickListener(this);
	}
	@Override
	public void onContentChanged() {
	super.onContentChanged();
	context2=this;
	 ivClose = (ImageView)findViewById(R.id.ivClose);
   	 tvmsg= (TextView) findViewById(R.id.tvmsg);
   	 rlBottam= (RelativeLayout)findViewById(R.id.rlBottam);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivClose:
			finish();
			break;
	case R.id.rlBottam:
			int no_number=0;
			Intent notificationIntent = null ;
			if(notification_no!=null && !notification_no.equals("")){
			try {
				no_number=Integer.parseInt(notification_no);
			} catch (NumberFormatException e) {
				no_number=0;
				e.printStackTrace();
			}
			}
			
			switch (no_number) {
			case 0:
				finish();
				break;
				
			case 1:
					notificationIntent =new Intent(context2,OfferRecieveActivity.class);  
			break;
			
			case 2:
				notificationIntent =new Intent(context2,AcceptOffer_Buyer.class);  
			break;
			
			case 3:
				notificationIntent =new Intent(context2,OfferRecieveActivity.class);  
			break;
			
			case 4:
				notificationIntent =new Intent(context2,TapBoard_Order_Status.class);  
			break;
			
			case 5:
				notificationIntent =new Intent(context2,OpenCaseActivity.class);  
			break;
			
			case 6:
				notificationIntent =new Intent(context2,ItemToShip_Activity.class);  
			break;
			
			case 7:
				notificationIntent =new Intent(context2,DeclineOffer_Seller.class);  
			break;
			
			case 8:
				notificationIntent =new Intent(context2,DeclineOffer_Buyer.class);  
			break;
			
			case 9:
				notificationIntent =new Intent(context2,Exp_buy_OfferActivity.class);  
			break;
			
			case 10:
				notificationIntent =new Intent(context2,Exp_sell_OfferActivity.class);  
			break;
			
			case 11:
				notificationIntent =new Intent(context2,ChaChingActivity.class);  
			break;
			
			case 12:
				notificationIntent =new Intent(context2,OpenCaseActivity.class);  
			break;

			case 13:
				notificationIntent =new Intent(context2,ChouseMeetupActivity.class);  
			break;
			
			case 14:
				notificationIntent =new Intent(context2,SuggestMeetupActivity.class);  
			break;
			
			case 15:
				notificationIntent =new Intent(context2,RequestRefund_Dialog_Activity.class);  
			break;
			
			case 16:
				notificationIntent =new Intent(context2,RequestRefund_Dialog_Activity.class);  
			break;
			
			case 17:
				notificationIntent =new Intent(context2,Footer_ReviewUser.class);  
			break;
			
			case 18:
				notificationIntent =new Intent(context2,SuggestMeetupActivity.class);  
			break;
			
			case 19:
				notificationIntent =new Intent(context2,ChatActivity.class);  
			break;
			
			case 20:
				notificationIntent =new Intent(context2,ResolutionChatActivity.class);  
			break;
			
			default:
				finish();
				break;
			}
			
			if(notificationIntent!=null){
		
			notificationIntent.putExtra("notification_text", ""+msg);
	        notificationIntent.putExtra("notification_type", type);
	        notificationIntent.putExtra("notification_offerid",offerid );
	        notificationIntent.putExtra("notification_price", notification_price);
	        notificationIntent.putExtra("notification_itemname", itemname);
	        notificationIntent.putExtra("notification_fromid", to_id);
	        notificationIntent.putExtra("notification_itemid", itemid);
	        notificationIntent.putExtra("notification_itemimage",image );
	        notificationIntent.putExtra("notification_isbuyer", isbuyer);
	        notificationIntent.putExtra("notification_item_price", notification_item_price);
	        notificationIntent.putExtra("notification_username",user_Name);
	        notificationIntent.putExtra("orderid", OrderID);
	        notificationIntent.putExtra("notification_caseid", Caseid);
	        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context2.startActivity(notificationIntent);
			finish();
			
			}
		
		}
	}
	
}
