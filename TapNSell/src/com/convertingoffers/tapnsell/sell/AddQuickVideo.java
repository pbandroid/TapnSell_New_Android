package com.convertingoffers.tapnsell.sell;


import com.convertingoffers.tapnsell.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AddQuickVideo extends Activity implements OnClickListener {
	ImageView ivyes,ivClose,iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addquickvid);
        
        ivyes.setOnClickListener(this);
        iv.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }
    @Override
    public void onContentChanged() {
    super.onContentChanged();
    ivyes=(ImageView)findViewById(R.id.ivYesbtn);
    iv=(ImageView)findViewById(R.id.iv);
    ivClose=(ImageView)findViewById(R.id.ivClose);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
         return true;
         }
         return super.onKeyDown(keyCode, event);    
    }
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ivClose:
			 intent = new Intent(AddQuickVideo.this, ItemDetails1.class);
	        startActivity(intent);
	        finish();
	        break;

	case R.id.ivYesbtn:
		 intent = new Intent(AddQuickVideo.this, VideoScreen.class);
        startActivity(intent);
        finish();
		break;
	case R.id.iv:
		 intent = new Intent(AddQuickVideo.this, VideoScreen.class);
        startActivity(intent);
        finish();
		break;
			
		default:
			break;
		}
		
	}
    
}

