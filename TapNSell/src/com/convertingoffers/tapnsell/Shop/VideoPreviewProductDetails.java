package com.convertingoffers.tapnsell.Shop;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;

public class VideoPreviewProductDetails extends BaseActivity implements OnClickListener {
    private VideoView videoView = null;
    String Status="";
    ProgressDialog pDialog;
    VideoView videoview;
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
       WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
       setContentView(R.layout.video_preview);
       ivBack.setOnClickListener(this);
       tvHeader.setText("Preview video");
       String SrcPath="";
       Bundle bundle = getIntent().getExtras();
       if(bundle!=null){
        SrcPath = bundle.getString("url");
       }

       // Create a progressbar
       pDialog = new ProgressDialog(VideoPreviewProductDetails.this);
       // Set progressbar title
       pDialog.setTitle("Video is Streaming");
       // Set progressbar message
       pDialog.setMessage("Buffering...");
       pDialog.setIndeterminate(false);
       pDialog.setCancelable(true);
       // Show progressbar
       pDialog.show();

       try {
           // Start the MediaController
           MediaController mediacontroller = new MediaController(
        		   VideoPreviewProductDetails.this);
           mediacontroller.setAnchorView(videoview);
           // Get the URL from String VideoURL
           Uri video = Uri.parse(SrcPath);
           videoview.setMediaController(mediacontroller);
           videoview.setVideoURI(video);
//           videoview.getHolder().lockCanvas().rotate(90);
       } catch (Exception e) {
//           Log.e("Error", e.getMessage());
           e.printStackTrace();
       }

       videoview.requestFocus();
       videoview.setOnPreparedListener(new OnPreparedListener() {
           // Close the progress bar and play the video
           public void onPrepared(MediaPlayer mp) {
               pDialog.dismiss();
               videoview.start();
           }
       });
    }

   	@Override
   	public void onContentChanged() {
	super.onContentChanged();
	
	videoview = (VideoView) findViewById(R.id.VideoView);
  
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
		
			finish();
			
			break;
	
		default:
			break;
		}
	  }
	}
