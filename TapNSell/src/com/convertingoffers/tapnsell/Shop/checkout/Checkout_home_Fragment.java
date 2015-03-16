package com.convertingoffers.tapnsell.Shop.checkout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.ConnectionDetector;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class Checkout_home_Fragment extends Fragment implements OnClickListener{

	TextView tvName,tvPurchasePrice,tvTransaction,tvShipping,tvTotal,tvMinute1,tvMinute,tvSecond1,tvSecond2;
	RelativeLayout rlConfirmBtn;
	ImageView ivItemImage;
	Billing_Fragment billing_fragment;
	String ItemId,userid,name,image,price;
	Context context;
	OnTextviewTimerSet mSetTime;
	
	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	// Base fragment 
	
	public Editor editor;
	public SharedPreferences pref;
	public ConnectionDetector cd;
	public UserFunctions userFunction;
	public ProgressDialog progressDialog;
	public String KEY_SUCCESS = "successfull",KEY_SUCCESS_STATUS = "true",strmsg="";
	public View view;
	public TextView tvHeader;
	public ImageView ivMenu,ivBack;
	static boolean active = false;
	// Base fragment
	
	public interface OnTextviewTimerSet {
		public void SetTimeToTextView(TextView tvMinute,TextView tvMinute1,TextView tvSecond1,TextView tvSecond2);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.checkout_home, container,false);
		context =getActivity().getApplicationContext();
	
		InitializeView();
		tvShipping.setText("Shipping & Handling");
		
		ItemId=pref.getString("CHKItemid", "");
		userid=pref.getString("UserID", "");
		name=pref.getString("CHKName", "");
		image=pref.getString("CHKImage", "");
		price=pref.getString("CHKPrice", "");
		
		
	if(price.length()!=0){
			
		try {
			float f_price =Float.parseFloat(price);
			String p_price = String.format("%.2f", f_price);
			tvPurchasePrice.setText("$ "+p_price);
			
				float shipPrice =0;
				shipPrice= Float.parseFloat(price)*10/100;
				String s_price = String.format("%.2f", shipPrice);
				tvTransaction.setText("$ "+s_price);
				
				
				float total = 0;
				total=shipPrice+Float.parseFloat(price);
				String t_price = String.format("%.2f", total);
				tvTotal.setText(""+t_price);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		}else{
			
			tvTransaction.setText("0.0");
			tvPurchasePrice.setText("0.0");
			tvTotal.setText("0.0");
			
			Log.e("pricelength0", "pricelength0");
		}
		tvName.setText(name);
		tvName.setTag(ItemId);
		
		tvHeader.setText("Checkout");
		
	/*	if (image != null) {
			try {
				imageLoader.DisplayImage(image, ivItemImage);
			} catch (Exception e) {
			}
		} else {
			ivItemImage.setImageResource(R.drawable.ic_launcher);
		}
		*/

		// Image display using lazy loading 

		iLoader_item.displayImage(image, ivItemImage, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				//holder.pbimage.setProgress(0);
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
		

		
		
		ivBack.setOnClickListener(this);
		rlConfirmBtn.setOnClickListener(this);
		
		return view;
	}

	private void InitializeView() {
		
		
		iLoader_item.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.checkout_home_item_image_bg)
		.showImageForEmptyUri(R.drawable.checkout_home_item_image_bg)
		.showImageOnFail(R.drawable.checkout_home_item_image_bg)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		
		ivItemImage=(ImageView)view.findViewById(R.id.ivItemImage);
		rlConfirmBtn=(RelativeLayout)view.findViewById(R.id.rlConfirmBtn);
		tvName=(TextView)view.findViewById(R.id.tvName);
		tvPurchasePrice=(TextView)view.findViewById(R.id.tvPurchasePrice);
		tvTransaction=(TextView)view.findViewById(R.id.tvTransaction);
		tvShipping=(TextView)view.findViewById(R.id.tvShipping);
		tvTotal=(TextView)view.findViewById(R.id.tvTotal);
		tvMinute=(TextView)view.findViewById(R.id.tvMinute);
		tvMinute1=(TextView)view.findViewById(R.id.tvMinute1);
		tvSecond1=(TextView)view.findViewById(R.id.tvSecond1);
		tvSecond2=(TextView)view.findViewById(R.id.tvSecond2);
		// Base fragment Initialize 
		ivMenu=(ImageView)view.findViewById(R.id.ivMenu);
		ivBack=(ImageView)view.findViewById(R.id.ivBack);
		tvHeader= (TextView) view.findViewById(R.id.tvHeader);
        userFunction = new UserFunctions();
    	cd = new ConnectionDetector(context);
    	pref =PreferenceManager.getDefaultSharedPreferences(context);
    	editor = pref.edit();
    	billing_fragment=new Billing_Fragment();
    	
    	// Base fragment Initialize
	}
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
	try {
		mSetTime=(OnTextviewTimerSet) activity;
	} catch (Exception e) {
		// TODO: handle exception
	}
	}

@Override
public void onResume() {
	super.onResume();
	mSetTime.SetTimeToTextView(tvMinute,tvMinute1, tvSecond1, tvSecond2);
}
	@Override
	public void onClick(View v) {
		
		MainCheckOutFragmentActivity activity = (MainCheckOutFragmentActivity) getActivity();
		switch (v.getId()) {
		case R.id.rlConfirmBtn:
			
			Log.e("click", "click");
						
			activity.ReplaceFragmentLeftToRight(billing_fragment,null);
			break;
		case R.id.ivBack:
			activity.cancelContdowwnTimer();
			getActivity().finish();
		break;
	
		default:
			break;
		}
	}
	
	
}
