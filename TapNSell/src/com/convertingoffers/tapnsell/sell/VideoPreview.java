package com.convertingoffers.tapnsell.sell;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.Shop.ProductDetails;
import com.convertingoffers.tapnsell.util.BaseActivity;

public class VideoPreview extends BaseActivity implements OnClickListener {
    private VideoView videoView = null;
    String Status="";
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
       WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.video_preview);
       ivBack.setOnClickListener(this);
       tvHeader.setText("Preview video");
       Status=pref.getString("priviewVid", "");
       String SrcPath =Environment.getExternalStorageDirectory()+ "/TapNSell/CameraVideo" + "/TapnSellVideo.mp4";

       videoView.setVideoPath(SrcPath);
       videoView.setMediaController(new MediaController(this));
       videoView.requestFocus();
       videoView.start();
   }

   	@Override
   	public void onContentChanged() {
	super.onContentChanged();
	
	
      videoView = (VideoView) findViewById(R.id.VideoView);
  
   	}
   
   @Override
   protected void onDestroy() {
         try {
                 videoView.stopPlayback();
         } catch (Exception e) {
                 //
         }
         super.onDestroy();
   }

@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.ivBack:
		
		
		if(Status.length()==0){
			
			Intent i = new Intent(VideoPreview.this, VideoScreen.class);
			startActivity(i);
			finish();
			
		}else if(Status.equals("Ptrue")){

			editor.remove("priviewVid");
			editor.commit();
			Intent i = new Intent(VideoPreview.this, ProductDetails.class);
			startActivity(i);
			finish();
		}else{
			editor.remove("priviewVid");
			editor.commit();
			Intent i = new Intent(VideoPreview.this, PriviewListing.class);
			startActivity(i);
			finish();
		}

		editor.remove("priviewVid");
		editor.commit();
		
		break;

	default:
		break;
	}
}
}
