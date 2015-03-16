package com.convertingoffers.tapnsell.TapboardFooter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.helpshift.Helpshift;

public class Help_Activity extends BaseActivity implements OnClickListener {

	RelativeLayout rlHelp,rlContact,rlSingleFAQ,rlSingleFAQSection;
	Animation RightSwipe;
	Context context;
	String TAG = "TapnSellHelp";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

//		Helpshift.install(getApplication(), "d9c4e1c48428055292cd9b10c7145e21", "netdisruptors.helpshift.com", "netdisruptors_platform_20150131100121224-3b3eb7cfa7971f1");

		Helpshift.install(getApplication(), "d9c4e1c48428055292cd9b10c7145e21", "netdisruptors.helpshift.com", "netdisruptors_platform_20150312142727634-8589055229b3b82");
		tvHeader.setText("Help");
		ivMenu.setVisibility(View.VISIBLE);	
		ivBack.setOnClickListener(this);
		rlHelp.setOnClickListener(this);
		rlContact.setOnClickListener(this);
		rlSingleFAQ.setOnClickListener(this);
		rlSingleFAQSection.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
	
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		
	
		rlHelp = (RelativeLayout) findViewById(R.id.rlHelp);
		rlContact= (RelativeLayout) findViewById(R.id.rlContact);
		rlSingleFAQ= (RelativeLayout) findViewById(R.id.rlSingleFAQ);
		rlSingleFAQSection= (RelativeLayout) findViewById(R.id.rlSingleFAQSection);
	
	}

	
	
	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
//		,,,rlViewTerms,rlfeature
		
		case R.id.rlHelp:
			buttonAnimation(rlHelp);
			 Helpshift.showFAQs(Help_Activity.this);
			break;
	
		case R.id.rlContact:
			buttonAnimation(rlContact);
			  Helpshift.showConversation(Help_Activity.this);
			break;
			
		case R.id.ivBack:
			finish();
			break;
			
		case R.id.rlSingleFAQ:
			buttonAnimation(rlSingleFAQ);
			
			 Helpshift.showSingleFAQ(Help_Activity.this, "50");
			break;

		case R.id.rlSingleFAQSection:
			buttonAnimation(rlSingleFAQSection);
			 Helpshift.showSingleFAQ(Help_Activity.this, "50");
			 
			break;
			
		case R.id.ivMenu:
			buttonAnimation(ivMenu);
			i = new Intent(context, TapBoardActivity.class);
			startActivity(i);
		break;
		
		default:
			break;
		}
	}
	
	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(Help_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
}
