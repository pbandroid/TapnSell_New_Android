package com.convertingoffers.tapnsell;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.convertingoffers.tapnsell.util.BaseActivity;
import com.crashlytics.android.Crashlytics;

public class SplashActivity extends BaseActivity {
	
	private long ms = 0;
	private long splashTime = 100;
	private boolean splashActive = true;
	private boolean paused = false;
	String strUserID;
	ImageView logo;
	 String regId ;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
	
		
		setContentView(R.layout.splash);
		
		  Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
		  logo.startAnimation(animation);

		  editor.putBoolean("SeeRecomodation", true);
		  editor.putBoolean("SeeNewList", true);
		  editor.commit();
		strUserID = pref.getString("UserID", "");
		Thread mythread = new Thread() {
			public void run() {
				try {
					while (splashActive && ms < splashTime) {
						if (!paused)
							ms = ms + 3000;
						sleep(ms);
					}
				} catch (Exception e) {
				} finally {

						Intent i = new Intent(SplashActivity.this, HomeActivity.class);
            			startActivity(i); 
            			 finish();
            		
				}
			}
		};
		mythread.start();
		
	}
	
	@Override
	public void onContentChanged() {
			super.onContentChanged();
			logo=(ImageView)findViewById(R.id.logo);
	}
}
