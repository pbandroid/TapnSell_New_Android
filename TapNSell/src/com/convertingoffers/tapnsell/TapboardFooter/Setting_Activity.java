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

public class Setting_Activity extends BaseActivity implements OnClickListener {

	RelativeLayout rlMyProfile,rlAlert,rlSound,rlViewTerms,rlfeature;
	Animation RightSwipe;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

	
		tvHeader.setText("Settings");
		
		ivBack.setOnClickListener(this);
		ivMenu.setVisibility(View.VISIBLE);
		rlMyProfile.setOnClickListener(this);
		rlAlert.setOnClickListener(this);
		rlSound.setOnClickListener(this);
		rlViewTerms.setOnClickListener(this);
		rlfeature.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		
	
		rlMyProfile = (RelativeLayout) findViewById(R.id.rlMyProfile);
		rlAlert= (RelativeLayout) findViewById(R.id.rlAlert);
		rlSound= (RelativeLayout) findViewById(R.id.rlSound);
		rlViewTerms= (RelativeLayout) findViewById(R.id.rlViewTerms);
		rlfeature= (RelativeLayout) findViewById(R.id.rlfeature);
	
	}

	
	
	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		
		case R.id.ivMenu:
			buttonAnimation(ivMenu);
			 i = new Intent(context, TapBoardActivity.class);
			startActivity(i);
			finish();
		break;
		case R.id.rlMyProfile:
			buttonAnimation(rlMyProfile);
			i = new Intent(context, Footer_ReviewUser.class);
			startActivity(i);
		
			break;
	
		case R.id.rlAlert:
			buttonAnimation(rlAlert);
			i = new Intent(context, Alert_list_Activity.class);
			startActivity(i);
			break;
			
		case R.id.ivBack:
			finish();
			break;
			
		case R.id.rlSound:
			buttonAnimation(rlSound);
			i = new Intent(context, Sound_Setting_Activity.class);
			startActivity(i);
		
			break;

		case R.id.rlViewTerms:
			buttonAnimation(rlViewTerms);
			i = new Intent(context, View_Tearm_Activity.class);
			startActivity(i);

			break;
			
		case R.id.rlfeature:
			buttonAnimation(rlfeature);
			i = new Intent(context, Suggetion_Activity.class);
			startActivity(i);
		break;
		
		default:
			break;
		}
	}
	
	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(Setting_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
}
