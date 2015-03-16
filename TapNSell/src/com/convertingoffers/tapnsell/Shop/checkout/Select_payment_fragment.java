package com.convertingoffers.tapnsell.Shop.checkout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.ConnectionDetector;
import com.convertingoffers.tapnsell.util.UserFunctions;

public class Select_payment_fragment extends Fragment implements OnClickListener{
	RelativeLayout rlPayment;
	Animation RightSwipe;
	View view;
	 OneClickCheckout_Fragment one_click_check_out;
	Context context;
	Select_billing_address add_card_info;
Billing_Fragment billing_fragment;
	// Base fragment 
	
	public Editor editor;
	public SharedPreferences pref;
	public ConnectionDetector cd;
	public UserFunctions userFunction;
	public ProgressDialog progressDialog;
	public String KEY_SUCCESS = "successfull",KEY_SUCCESS_STATUS = "true",strmsg="";
	public TextView tvHeader;
	public ImageView ivMenu,ivBack;
	
	// Base fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		context = getActivity().getApplicationContext();
		view = inflater.inflate(R.layout.payment_method, container,false);
		InitializeView();
		
	
		 
		tvHeader.setText("Select Payment Method");
		rlPayment.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		return view;
	}

    private void buttonAnimation(View v) {
		
    	RightSwipe = AnimationUtils.loadAnimation(context,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
    
	private void InitializeView() {
		
		add_card_info= new Select_billing_address();
		one_click_check_out=new OneClickCheckout_Fragment();
		billing_fragment = new Billing_Fragment();
		rlPayment=(RelativeLayout)view.findViewById(R.id.rlPayment);
		
		// Base fragment Initialize 
		ivMenu=(ImageView)view.findViewById(R.id.ivMenu);
		ivBack=(ImageView)view.findViewById(R.id.ivBack);
		tvHeader= (TextView) view.findViewById(R.id.tvHeader);
        userFunction = new UserFunctions();
    	cd = new ConnectionDetector(context);
    	pref =PreferenceManager.getDefaultSharedPreferences(context);
    	editor = pref.edit();
    	// Base fragment Initialize
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		
		case R.id.rlPayment:
			buttonAnimation(rlPayment);
			
			MainCheckOutFragmentActivity activity = (MainCheckOutFragmentActivity) getActivity();
			activity.ReplaceFragmentLeftToRight(one_click_check_out,null);
			
		break;
		case R.id.ivBack:
		
				MainCheckOutFragmentActivity act= (MainCheckOutFragmentActivity) getActivity();
				act.ReplaceFragmentRightToLeft();	
		
			
		break;
		default:
			break;
		}
	}

}
