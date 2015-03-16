package com.convertingoffers.tapnsell.sell;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.TouchImageView;

public class PicturePreviewActivity extends BaseActivity implements
		OnClickListener {
	ProgressDialog progressDialog;
	String ComeFromPriviw = "", image_path;
	byte[] br;
	TouchImageView imgViewer;
	Button btnUse, btnRetake;
	public int count = 0;
	Bitmap bMap = null;
	File dialogfile;
//	TouchImageView ivDisplayImageDialog;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pitcher_priview);
		ComeFromPriviw = pref.getString("priview_image", "");
		image_path = pref.getString("image_path", "");
		btnUse = (Button) findViewById(R.id.btnUse);
		btnRetake = (Button) findViewById(R.id.btnRetake);
		btnUse.setOnClickListener(this);
		btnRetake.setOnClickListener(this);
	
		new DisplayBackTask().execute("");
		
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnUse:
			// codebtnUse();
			new DoneBackTask().execute("");
			break;
		case R.id.btnRetake:
			codebtnRetake();
			break;
		default:
			break;
		}

	}

	private class DisplayBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(PicturePreviewActivity.this);
			progressDialog.setMessage("Fetching image");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				if (bundle.containsKey("Camera")) {
					
					Log.e("Camera", "Camera");
					Runtime.getRuntime().gc();
					File myDir = new File(
							android.os.Environment.getExternalStorageDirectory(),
							"/TapNSell/TempImage/temp.png");

					dialogfile=myDir;
					/*
					 * if(myDir.exists()){ Bitmap myBitmap =
					 * BitmapFactory.decodeFile(myDir.getAbsolutePath());
					 * imgViewer.setImageBitmap(myBitmap); }
					 */

					int file_size = Integer.parseInt(String.valueOf(myDir.length() / 1024));
					Log.e("file_size", " " + file_size);
					
				
					BitmapFactory.Options options = new BitmapFactory.Options();
					if (file_size < 1000) {
						
						Log.e("image size is less", "image size is less");
						bMap = BitmapFactory.decodeFile(myDir.toString());
					
					}else if (file_size < 2000) {
						
						Log.e("image size is heavy", "image size is heavy");
						
						options.inSampleSize = 2;
						bMap = BitmapFactory.decodeFile(myDir.toString(), options);
						Log.e("path", " " + myDir);
					
					} else {
						Log.e("image size is heavy", "image size is heavy");
						
						options.inSampleSize = 4;
						bMap = BitmapFactory.decodeFile(myDir.toString(), options);
						Log.e("path", " " + myDir);
					}
					/*
					bMap = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(),
							bMap.getHeight(), matrix, true);*/

				} else if (bundle.containsKey("picturePath")) {
					Runtime.getRuntime().gc();
					Log.e("Gallery", "Gallery");
					String path = bundle.getString("picturePath");
					
					Log.e("path", " " + path);

					File filenew = new File(path);
					dialogfile=filenew;
					int file_size = Integer.parseInt(String.valueOf(filenew
							.length() / 1024));
					Log.e("file_size", " " + file_size);
					
					Matrix matrix = new Matrix();
					
					if (file_size < 1000) {
						
						Log.e("image size is less", "image size is less");
						bMap = BitmapFactory.decodeFile(path);
					} else if (file_size < 2000) {
						
						Log.e("image size is heavy", "image size is heavy");
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 2;
						bMap = BitmapFactory.decodeFile(path, options);
						Log.e("path", " " + path);
					}else {
						Log.e("image size is heavy", "image size is heavy");
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 4;
						bMap = BitmapFactory.decodeFile(path, options);
						Log.e("path", " " + path);
					}
					
					matrix.postRotate(0);
					bMap = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(),
							bMap.getHeight(), matrix, true);
/*					imgViewer.setImageBitmap(bMap);*/
				}
			}
		if(bMap!=null){
			errorMessage="true";
		}else{
			errorMessage="false";
		}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.equals("true")) {
		
					imgViewer.setImageBitmap(bMap);	
				
				
			} else {
				Toast.makeText(PicturePreviewActivity.this, "please try again later",Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	
	public class SaveImageBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;
		File S_file,D_file;
		

		public SaveImageBackTask(File sourceFile, File mediaFile) {
			S_file=sourceFile;
			D_file=mediaFile;
		}

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Fetching image");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			Runtime.getRuntime().gc();

	
			return errorMessage;
		}

		
		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


	
	
	private class DoneBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(PicturePreviewActivity.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				File myDir = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/CameraImage");
				getFileCount(myDir.toString());

				if (ComeFromPriviw.equals("true")) {
					/*imgViewer.buildDrawingCache();
					Bitmap bmap = imgViewer.getDrawingCache();*/
					myDir.mkdirs();
					errorMessage = "10";
					Log.e("image_path ", " " + image_path);
					File file = new File(image_path);
					if (file.exists())
						file.delete();
					m_copyfile(dialogfile, file);
				} else {
					if (count == 3) {
						errorMessage = "3";
						myDir.mkdirs();
						int n = count + 1;
						String fname = n + ".PNG";
						File file = new File(myDir, fname);
						if (file.exists())
							file.delete();
						m_copyfile(dialogfile, file);
						
						
						
					} else {
						errorMessage = "0";
//						imgViewer.buildDrawingCache();
//						Bitmap bmap = imgViewer.getDrawingCache();

						myDir.mkdirs();
						int n = count + 1;
						String fname = n + ".PNG";
						File file = new File(myDir, fname);
						if (file.exists())
							file.delete();
						m_copyfile(dialogfile, file);
					}
				}
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.equals("3")) {

				Intent intent = new Intent(PicturePreviewActivity.this,
						SelectPitcherActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			} else if (result.equals("10")) {
				editor.remove("priview_image");
				editor.remove("image_path");
				editor.commit();

				Intent intent = new Intent(PicturePreviewActivity.this,
						PriviewListing.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(PicturePreviewActivity.this,
						TakePictureNew.class);
				startActivity(intent);
				finish();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


	private void codebtnRetake() {

		Intent intent = new Intent(PicturePreviewActivity.this,
				TakePictureNew.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context = this;
		imgViewer = (TouchImageView) findViewById(R.id.imgprivew);
	}
	public void m_copyfile(File S_file,File D_file) {
		 try {
				if (S_file.exists()) {
				FileChannel source = null;
				FileChannel destination = null;
				source = new FileInputStream(S_file).getChannel();
				destination = new FileOutputStream(D_file).getChannel();
				if (destination != null && source != null) {
				    destination.transferFrom(source, 0, source.size());
				}
				if (source != null) {
				    source.close();
				}
				if (destination != null) {
				    destination.close();
				}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	public Bitmap decodeFile(Bitmap bitmap) {// you can provide file path here
		int orientation;
		try {
			orientation = ExifInterface.ORIENTATION_ROTATE_90;
			Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();
			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), m, true);
				return bitmap;
			} else if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				m.postRotate(180);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), m, true);
				return bitmap;
			}
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	private void getFileCount(String dirPath) {
		File f = new File(dirPath);
		File[] files = f.listFiles();

		if (files != null)
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					count++;
					getFileCount(file.getAbsolutePath());
				} else {
					count++;
				}
			}
	}
	
}
/*
 * private void codebtnUse() { File myDir = new
 * File(android.os.Environment.getExternalStorageDirectory
 * (),"/TapNSell/CameraImage"); getFileCount(myDir.toString()); if(count==3){
 * Intent intent = new Intent(PicturePreviewActivity.this,
 * SelectPitcherActivity.class); intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); startActivity(intent);
 * 
 * imgViewer.buildDrawingCache(); Bitmap bmap = imgViewer.getDrawingCache();
 * myDir.mkdirs(); int n = count+1; String fname = n + ".PNG"; File file = new
 * File(myDir, fname); if (file.exists()) file.delete(); try { FileOutputStream
 * out = new FileOutputStream(file); bmap.compress(Bitmap.CompressFormat.PNG,
 * 90, out); out.flush(); out.close();
 * 
 * } catch (Exception e) { e.printStackTrace(); } } else{
 * imgViewer.buildDrawingCache(); Bitmap bmap = imgViewer.getDrawingCache();
 * 
 * myDir.mkdirs(); int n = count+1; String fname = n + ".PNG"; File file = new
 * File(myDir, fname); if (file.exists()) file.delete(); try { FileOutputStream
 * out = new FileOutputStream(file); bmap.compress(Bitmap.CompressFormat.PNG,
 * 90, out); out.flush(); out.close();
 * 
 * } catch (Exception e) { e.printStackTrace(); } Intent intent = new
 * Intent(PicturePreviewActivity.this, TakePictureActivity.class);
 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); startActivity(intent); } }
 */
