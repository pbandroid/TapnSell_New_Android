package com.convertingoffers.tapnsell.MakeOffer;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.CustomTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DeclineOffer_Seller extends BaseActivity  implements OnClickListener{
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	ImageView ivItemImage,ivClose,ivCounterOffer;
	CustomTextView tvItemName,tvDeclineText;
	String itemid="",userid="", amount="",image="",name="",offerid="",accept=""
		,noti_msg="",type = "", price="",to_id="",isbuyer="",dialog_msg="";
	Context context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makeoffer_offer_decline_seller);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			
		noti_msg = bundle.getString("notification_text");
		type = bundle.getString("notification_type");
		offerid=bundle.getString("notification_offerid");
		price = bundle.getString("notification_price");
		name = bundle.getString("notification_itemname");
		to_id = bundle.getString("notification_fromid");
		price = bundle.getString("notification_price");
		itemid = bundle.getString("notification_itemid");
		image = bundle.getString("notification_itemimage");
		isbuyer = bundle.getString("notification_isbuyer");
		
		}
		userid = pref.getString("UserID", "");
		tvDeclineText.setText("Your Offer For $"+ price +" Was Declined.");
	/*	if (image != null) {
				imageLoader.DisplayImage(image, ivItemImage);
		} else {
			ivItemImage.setImageResource(R.drawable.ic_launcher);
		}*/
		

		// Image display using lazy loading 

		iLoader_item.displayImage(image, ivItemImage, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				//holder.pbimage.setProgress(0);
//				holder.pbimage.setIndeterminate(true);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			}
		}, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				//holder.pbimage.setProgress(Math.round(100.0f * current / total));
//				holder.pbimage.setIndeterminate(true);
			}
		});
		
		// Image display using lazy loading
	
		tvItemName.setText(""+name);
		ivClose.setOnClickListener(this);
		ivCounterOffer.setOnClickListener(this);
	}
	
	public void onClick(View v) {

		Intent intent;
		switch (v.getId()) {

		case R.id.ivCounterOffer:
			
			intent = new Intent(context, CounterOfferActivity.class);
			intent.putExtra("Counter_text", ""+noti_msg);
			intent.putExtra("Counter_type", ""+type);
			Log.e("Decline offerid", " "+offerid);
			intent.putExtra("Counter_offerid", ""+offerid);
			intent.putExtra("Counter_price", ""+price);
			intent.putExtra("Counter_name", ""+name);
			intent.putExtra("Counter_to_id", ""+to_id);
			intent.putExtra("Counter_itemid", ""+itemid);
			intent.putExtra("Counter_image", ""+image);
			intent.putExtra("Counter_isbuyer", ""+isbuyer);
			startActivity(intent);

			break;
			
		case R.id.ivClose:
			finish();
			break;
		default:
			break;
		}
	}
	
	
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		
		context=this;
		
		iLoader_item.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.counter_image_bg)
		.showImageForEmptyUri(R.drawable.counter_image_bg)
		.showImageOnFail(R.drawable.counter_image_bg)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		tvDeclineText=(CustomTextView)findViewById(R.id.tvDeclineText);
		ivItemImage=(ImageView)findViewById(R.id.ivItemImage);
		tvItemName=(CustomTextView)findViewById(R.id.tvItemName);
		ivClose=(ImageView)findViewById(R.id.ivClose);
		tvItemName=(CustomTextView)findViewById(R.id.tvItemName);
		ivCounterOffer=(ImageView)findViewById(R.id.ivCounterOffer);
	}
}
