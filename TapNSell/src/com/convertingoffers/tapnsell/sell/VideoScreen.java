package com.convertingoffers.tapnsell.sell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.CountDownTimerWithPause;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class VideoScreen extends Activity implements Callback, OnClickListener {

	@Override
	protected void onDestroy() {
		// stopRecording();
		super.onDestroy();
	}
	private SurfaceHolder surfaceHolder;
	private SurfaceView surfaceView;
	public MediaRecorder mrec;
	private Camera mCamera;
	long finalTimerSecond = 1 * 60 * 1000;
	private MalibuCountDownTimer countDownTimer;
	@SuppressWarnings("unused")
	private boolean timerHasStarted = false;
	File file;
	@SuppressWarnings("unused")
	private boolean timerHasPaused = false;
	long timerInterval = 1000;
	boolean recording;
	private ArrayList<String> mFiles = new ArrayList<String>();
	int currentapiVersion;
	long VideoNo;
	Button btnStart,btncenter,btnfinish,btndelete;
	String VideoQuality="low",filename,remainTimerTime,activityFinalTime;
	boolean shouldONFlash = false, StatusOfFlash;
	RelativeLayout rlFlash;
	TextView tvFlashText, textTimer;
	ImageView ivBlink, ivFlashIcon,ivclsbtn;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatusOfFlash = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		currentapiVersion = android.os.Build.VERSION.SDK_INT;
		setContentView(R.layout.take_video);
		btnStart.setOnClickListener(this);
		rlFlash.setOnClickListener(this);
		btnfinish.setOnClickListener(this);
		btndelete.setOnClickListener(this);
		btncenter.setOnClickListener(this);
		ivclsbtn.setOnClickListener(this);

		filename = Environment.getExternalStorageDirectory()+ "/TapNSell/CameraVideo" + "/TapnSellVideo.mp4";
		 file= new File(filename);
		 
		if(file.exists()) {
			btncenter.setVisibility(View.VISIBLE);
			btndelete.setEnabled(true);
			btndelete.setClickable(true);
		}else{
			btncenter.setVisibility(View.GONE);
			btndelete.setEnabled(false);
			btndelete.setClickable(false);
		}
		 
		mrec = new MediaRecorder();
		if(mCamera==null)
		mCamera = Camera.open();
		if (mCamera != null)
//			mCamera.setDisplayOrientation(90);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		rlFlash = (RelativeLayout) findViewById(R.id.rlFlash);
		btnStart = (Button) findViewById(R.id.btnStart);
		ivBlink = (ImageView) findViewById(R.id.ivBlink);
		textTimer = (TextView) findViewById(R.id.textTimer);
		ivFlashIcon = (ImageView) findViewById(R.id.ivFlashIcon);
		tvFlashText = (TextView) findViewById(R.id.tvFlashText);
		btncenter=(Button)findViewById(R.id.btncenter);
		btnfinish=(Button)findViewById(R.id.btnfinish);
		btndelete=(Button)findViewById(R.id.btndelete);
		ivclsbtn= (ImageView) findViewById(R.id.ivclsbtn);
	}

	@SuppressWarnings("unused")
	protected void startRecording() throws IOException {

		if (mCamera == null)
			mCamera = Camera.open();
		if (mCamera != null)
			//shahil ori
			mCamera.setDisplayOrientation(90);
		if (StatusOfFlash) {

			Camera.Parameters mParameters = mCamera.getParameters();

			if (mParameters != null) {
				List<String> flashModes = mParameters.getSupportedFlashModes();

				if (flashModes
						.contains(android.hardware.Camera.Parameters.FLASH_MODE_AUTO)) {
					if (shouldONFlash) {
						mParameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
					} else {
						mParameters.setFlashMode(Parameters.FLASH_MODE_OFF);
					}
				}
				mCamera.setParameters(mParameters);
			}
		}
		recording = true;
	
		String path;
		String externalStorage = Environment.getExternalStorageDirectory()+ "/TapNSell/";
		File mediaStorageDir = new File(externalStorage, "CameraVideo");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				mediaStorageDir.mkdir();
			}
		}

		filename = Environment.getExternalStorageDirectory()+ "/TapNSell/CameraVideo" + "/TapnSellVideo.mp4";
		
		 file = new File(filename);
		mFiles.add(file.getAbsolutePath().toString());
		mrec = new MediaRecorder();
		
		mCamera.lock();
		mCamera.unlock();
		mrec.setCamera(mCamera);
		mrec.setOrientationHint(90);
		mrec.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

			mrec.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			mrec.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mrec.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

			CamcorderProfile camcorderProfile;
			if (VideoQuality.equals("low")) {
				camcorderProfile = CamcorderProfile
						.get(CamcorderProfile.QUALITY_480P);
			} else {
				camcorderProfile = CamcorderProfile
						.get(CamcorderProfile.QUALITY_HIGH);
			}
			mrec.setProfile(camcorderProfile);
		} else {

			CamcorderProfile camcorderProfile;
			if (VideoQuality.equals("low")) {
				camcorderProfile = CamcorderProfile
						.get(CamcorderProfile.QUALITY_480P);
			} else {
				camcorderProfile = CamcorderProfile
						.get(CamcorderProfile.QUALITY_HIGH);
			}
			mrec.setProfile(camcorderProfile);
		}
		
		mrec.setPreviewDisplay(surfaceHolder.getSurface());
		mrec.setOutputFile(filename);
		mrec.prepare();
		mrec.start();
	
		btnStart.setClickable(false);
		try {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					btnStart.setClickable(true);
				}
			}, 3000);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void stopRecording() {
		releaseMediaRecorder();
		
	}

	private void releaseMediaRecorder() {
		if (mrec != null) {
			mrec.reset(); // clear recorder configuration
			mrec.release(); // release the recorder object
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mCamera == null) {
			mCamera = Camera.open();
		}
		try {
			mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {

			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			if (recording) {

				btndelete.setEnabled(true);
				btndelete.setClickable(true);
				if(countDownTimer!=null){
					countDownTimer.pause();	
				}
				timerHasPaused = false;
				blinkStop();

				recording = false;
				releaseMediaRecorder();
				releaseCamera();
				System.out.println("saveing value for bool key=================================");
			

			} else {
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btndelete:
			ValiDialogDeleteEvant();
			break;
		case R.id.btncenter:
			String fileName = Environment.getExternalStorageDirectory()
			+ "/TapNSell/CameraVideo" + "/TapnSellVideo.mp4";
			 file = new File(fileName);
			
			
			if(file.exists()){
			Intent i = new Intent(VideoScreen.this, VideoPreview.class);
			startActivity(i);
			finish();
			}else{
				Toast.makeText(VideoScreen.this, "Video is not available.", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnfinish:
			//countDownTimer.pause();
			//timerHasPaused = false;
		//	blinkStop();
			//recording = false;
			
			if (recording) {
				if(countDownTimer!=null){
					countDownTimer.pause();	
				}
				
				timerHasPaused = false;
				blinkStop();
				recording = false;
				btndelete.setEnabled(true);
				btndelete.setClickable(true);
				stopRecording();
				btncenter.setVisibility(View.VISIBLE);
				if (mCamera != null) {

					mCamera.setPreviewCallback(null);
					mCamera.stopPreview();
					mCamera.release();
					mCamera = null;
				}
			}
		
			Intent i = new Intent(VideoScreen.this, ItemDetails1.class);
			startActivity(i);
			finish();
			break;
		case R.id.rlFlash:
			if (!StatusOfFlash) {
				ValidationDialogCancleEvent("Flash is not available in your mobile");
			} else {
				if (mCamera != null) {
					shouldONFlash = !shouldONFlash;
					Camera.Parameters mParameters = mCamera.getParameters();

					if (mParameters != null) {
						List<String> flashModes = mParameters
								.getSupportedFlashModes();

						if (flashModes
								.contains(android.hardware.Camera.Parameters.FLASH_MODE_AUTO)) {
							if (shouldONFlash) {
								tvFlashText.setText("ON");
								ivFlashIcon.setImageDrawable(getResources()
										.getDrawable(R.drawable.flash_icon_on));
								mParameters
										.setFlashMode(Parameters.FLASH_MODE_TORCH);
							} else {
								tvFlashText.setText("OFF");
								ivFlashIcon
										.setImageDrawable(getResources()
												.getDrawable(
														R.drawable.flash_icon_off));
								mParameters
										.setFlashMode(Parameters.FLASH_MODE_OFF);
							}
						}
						mCamera.setParameters(mParameters);
					}
				}
			}

			break;
		case R.id.ivclsbtn:
			
			if (recording) {
				if(countDownTimer!=null){
					countDownTimer.pause();
				}
				
				timerHasPaused = false;
				blinkStop();
				recording = false;
				btndelete.setEnabled(true);
				btndelete.setClickable(true);
				stopRecording();
				btncenter.setVisibility(View.VISIBLE);
				if (mCamera != null) {

					mCamera.setPreviewCallback(null);
					mCamera.stopPreview();
					mCamera.release();
					mCamera = null;
				}
			}
			Intent iclose = new Intent(VideoScreen.this, ItemDetails1.class);
			startActivity(iclose);
			finish();
			
			break;
		case R.id.btnStart:
			if (recording) {
				
				if(countDownTimer!=null){
					countDownTimer.pause();	
				}
				timerHasPaused = false;
				blinkStop();
				recording = false;
				btndelete.setEnabled(true);
				btndelete.setClickable(true);
				stopRecording();
				btncenter.setVisibility(View.VISIBLE);
				if (mCamera != null) {

					mCamera.setPreviewCallback(null);
					mCamera.stopPreview();
					mCamera.release();
					mCamera = null;
				}
			} else {
				try {
					btndelete.setEnabled(false);
					btndelete.setClickable(false);
					blinkStart();
					btncenter.setVisibility(View.GONE);
					startRecording();
					countDownTimer = new MalibuCountDownTimer(finalTimerSecond,
							timerInterval, true);
					countDownTimer.create();
					timerHasStarted = true;
					timerHasPaused = true;

				} catch (Exception e) {

					String message = e.getMessage();
					Log.i(null, "Problem " + message);
					mrec.release();
				}
			}
			break;

		}
	}

	private void blinkStop() {
		ivBlink.clearAnimation();
	}

	private void blinkStart() {
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(700); // You can manage the time of the blink with this
		anim.setStartOffset(20);// parameter
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		ivBlink.startAnimation(anim);
	}

	private void ValiDialogDeleteEvant() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VideoScreen.this);
	 
	alertDialogBuilder.setTitle("alert");

	alertDialogBuilder
		.setMessage("Do you want to delete video?")
		.setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				String fileName = Environment.getExternalStorageDirectory()
				+ "/TapNSell/CameraVideo" + "/TapnSellVideo.mp4";
				File myFile = new File(fileName);
				if (myFile.exists()){
					myFile.delete();
				/*	Intent i = new Intent(VideoScreen.this, VideoScreen.class);
					startActivity(i);
					finish();*/
					btncenter.setVisibility(View.GONE);
					
				}else{
					Toast.makeText(VideoScreen.this, "There is no video file", Toast.LENGTH_LONG).show();
				}
					
			}
		  })
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {

				dialog.cancel();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	private void ValidationDialogCancleEvent(String msg) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				VideoScreen.this);

		alertDialogBuilder.setTitle("alert");

		alertDialogBuilder.setMessage(msg).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
		Intent intent = new Intent(VideoScreen.this, SelectPitcherActivity.class);
		Log.i("Hello", "This is Coomon Log");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		return;
	}

	public class MalibuCountDownTimer extends CountDownTimerWithPause {
		long minTIme, secTime;
		int vCount = 0;

		public long getLeftTime() {

			long rTime = timeLeft();
			return rTime;

		}

		public MalibuCountDownTimer(long startTime, long interval,
				boolean myStatus) {
			super(startTime, interval, true);
		}

		public void onFinish() {
			
			if(countDownTimer!=null){
				countDownTimer.pause();	
			}
			textTimer.setText("00:00");
			timerHasPaused = false;
			blinkStop();
			recording = false;
			stopRecording();
			btncenter.setVisibility(View.VISIBLE);
			if (mCamera != null) {
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}
		}

		public void onTick(long millisUntilFinished) {

			long minTIme = (millisUntilFinished / (1000 * 60)) % 60;
			long secTime = (millisUntilFinished / 1000) % 60;
			String min = String.valueOf(minTIme);
			String sec = String.valueOf(secTime);
			if (min.length() == 1) {
				min = "0" + min;
			}
			if (sec.length() == 1) {
				sec = "0" + sec;
			}
			remainTimerTime = min + ":" + sec;
			textTimer.setText(min + ":" + sec);
		}
	}
}
