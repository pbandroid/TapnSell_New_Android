package com.convertingoffers.tapnsell.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.convertingoffers.tapnsell.R;

public class BaseActivity extends Activity {

	public Editor editor;
	public SharedPreferences pref;
	public ConnectionDetector cd;
	public UserFunctions userFunction;
	public ProgressDialog progressDialog;
	public String KEY_SUCCESS = "successfull",KEY_SUCCESS_STATUS = "true",strmsg="";
	
	public TextView tvHeader;
	public ImageView ivMenu,ivBack;
	
	// Dialog code parameter

	String userid = "", itemid = "", regId = "";
	Dialog dialog;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
    }
    @Override
    public void onContentChanged() {
    super.onContentChanged();
    
    ivMenu=(ImageView)findViewById(R.id.ivMenu);
    ivBack=(ImageView)findViewById(R.id.ivBack);
    tvHeader= (TextView) findViewById(R.id.tvHeader);
    userFunction = new UserFunctions();
	cd = new ConnectionDetector(BaseActivity.this);
	pref =PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
	editor = pref.edit();
    }
  }

