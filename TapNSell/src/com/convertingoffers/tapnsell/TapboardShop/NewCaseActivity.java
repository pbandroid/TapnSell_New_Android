package com.convertingoffers.tapnsell.TapboardShop;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapboardSell.OpenCaseActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;

public class NewCaseActivity extends BaseActivity implements OnClickListener {
	private static final String IMAGE_DIRECTORY_NAME = "Cases";
	ImageView ivAttach, ivCamera, ivOpenCase;
	EditText evDec;
	TextView tvspinnerType;
	LinearLayout llSpinnerType;
	Context context;
	String  description,issue="Never Recieved",orderid,userid,image,msg,APILink;
	ArrayList<String> mType = new ArrayList<String>();
	String JsonString;
	Boolean successfull =false;
	static InputStream is = null;
	static String result;
	Dialog dialog;
	ListView lvt;
	Adapter adapter;
	int RESULT_LOAD_CAMERA_IMAGE = 2,RESULT_LOAD_IMAGE = 1;;
	Animation RightSwipe;
	String picturePath="";
	public static final int MEDIA_TYPE_IMAGE = 1;
	private Uri fileUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_case);
		
		MakeSpinnerArray();
		tvHeader.setText("Create a Case");
		userid = pref.getString("UserID", "");
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
		orderid = bundle.getString("orderid");
		}
		ivAttach.setOnClickListener(this);
		ivCamera.setOnClickListener(this);
		ivOpenCase.setOnClickListener(this);
		llSpinnerType.setOnClickListener(this);
		ivBack.setOnClickListener(this);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		ivAttach = (ImageView) findViewById(R.id.ivAttach);
		ivCamera = (ImageView) findViewById(R.id.ivCamera);
		ivOpenCase = (ImageView) findViewById(R.id.ivOpenCase);
		evDec = (EditText) findViewById(R.id.evDec);
		llSpinnerType = (LinearLayout) findViewById(R.id.llSpinnerType);
		tvspinnerType= (TextView) findViewById(R.id.tvspinnerType);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ivAttach:
			
			buttonAnimation(ivAttach);
			ivAttach.setClickable(false);
			ivAttach.setEnabled(false);
			mygallerybtn();
			
			break;
	case R.id.ivCamera:
		
		  buttonAnimation(ivCamera);
	      
	      
	      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
			picturePath = fileUri.getPath();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(intent,RESULT_LOAD_CAMERA_IMAGE);
			
			
			break;
	case R.id.ivOpenCase:
		
		issue=tvspinnerType.getText().toString().trim();
		description=evDec.getText().toString().trim();
		if(description.length()==0){
			Custom_Dialog.dialogCode(2, null, "Please enter Case Details", NewCaseActivity.this);
		}else{
			if (cd.checkConnection()) {
				new CreateCaseBackTask().execute("");
			} else {
				Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
		}
		}
		
		break;
	case R.id.llSpinnerType:
		
		llSpinnerType.setEnabled(false);
		llSpinnerType.setEnabled(false);
		SpinnerTypeDialog();
		
		break;

	case R.id.ivBack:
	
		finish();
		
	break;
		default:
			break;
		}
	}
	

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}
	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(Environment.
				getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
//				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile = null;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		}	
		

		return mediaFile;
	}


		public void mygallerybtn() {
		
		
			String	FILE_TYPE="image/*";
		
				Intent i = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					i.setType(FILE_TYPE);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
	
	
	private class CreateCaseBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
			
				
				/*Log.e("userid", " "+ userid);
				Log.e("orderid", " "+ orderid);
				Log.e("issue", " "+ issue);
				Log.e("description", " "+ description);
				Log.e("image", " "+ image);
				
				JSONObject json = userFunction.CreateCaseFunction(userid, orderid, issue, description, image);
				
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							msg=json.getString("message");
							errorMessage = "true";

						} else {

							msg=json.getString("message");
							errorMessage = "false";
						}
					} else {
						errorMessage = "error in posting";
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}*/
				
				
				
				File file = new File(picturePath);
				
				
				
				try {
					APILink = UserFunctions.localBaseUrl + "lawsuits/createcase";
					MultipartEntity multipartcontent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
					
					multipartcontent.addPart("userid", new StringBody(userid, "text/plain", Charset.forName("UTF-8")));
					multipartcontent.addPart("orderid", new StringBody(orderid, "text/plain", Charset.forName("UTF-8")));
					multipartcontent.addPart("issue", new StringBody(issue, "text/plain", Charset.forName("UTF-8")));
					multipartcontent.addPart("description", new StringBody(description, "text/plain", Charset.forName("UTF-8")));
					
					if (file.exists()) {
						byte[] ba =convertFileToByteArray(file);
						Log.e("userid", " "+userid);
						Log.e("orderid", " "+orderid);
						Log.e("issue", " "+issue);
						Log.e("description", " "+description);
						Log.e("ba", " "+ba);
						ByteArrayBody bab = new ByteArrayBody(ba, "image/*", "ItemIage.png");
						multipartcontent.addPart("image", bab);
					}else{
						multipartcontent.addPart("image", new StringBody("", "text/plain", Charset.forName("UTF-8")));
					}
					

					JsonString = getJsonStringMulitipart(APILink, multipartcontent);
					
					
					JSONObject jsonObject = null;
					try {
						jsonObject = new JSONObject(JsonString);
						successfull = jsonObject.optBoolean("successfull");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (successfull) {
						
						message=jsonObject.optString("message");
						errorMessage = "true";

					} else {

						message=jsonObject.optString("message");
						errorMessage = "false";
					}
					Log.e("successfull", "t "+successfull);
					
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
			}
			return errorMessage;
		}

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {
				Custom_Dialog.dialogCode(1, null, message, context);
			} else {
				Intent i = new Intent(context, OpenCaseActivity.class);
				Custom_Dialog.dialogCode(3, i, "Case has already Been Created.", context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	public static byte[] convertFileToByteArray(File f) {
		byte[] byteArray = null;
		try {
			InputStream inputStream = new FileInputStream(f);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024 * 8];
			int bytesRead = 0;

			while ((bytesRead = inputStream.read(b)) != -1) {
				bos.write(b, 0, bytesRead);
			}

			byteArray = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArray;
	}
	
	@SuppressWarnings("static-access")
	public void SpinnerTypeDialog()

	{
		dialog = new Dialog(NewCaseActivity.this);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.itemdetails_listview);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		dialog.show();
		dialog.setCanceledOnTouchOutside(true);
		lvt = (ListView) dialog.findViewById(R.id.lvDialog);
		adapter = new Adapter(NewCaseActivity.this, mType);
		lvt.setAdapter(adapter);
		lvClickEvent();
	}
	private void lvClickEvent() {
		
		lvt.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

					llSpinnerType.setEnabled(true);
					llSpinnerType.setEnabled(true);
					String Country = mType.get(position);
					tvspinnerType.setText(Country);
					llSpinnerType.setClickable(true);
					llSpinnerType.setEnabled(true);
					dialog.dismiss();
			}
		});
	}
	private void MakeSpinnerArray() {
		mType.add("Never Received");
		mType.add("Defective item");
		mType.add("not same item");
	}
	@Override
	protected void onResume() {
	super.onResume();

	llSpinnerType.setClickable(true);
	llSpinnerType.setEnabled(true);

	}
	public class Adapter extends BaseAdapter {
		private Context context;
		private ArrayList<String> val;
	
		public Adapter(Context context, ArrayList<String> val) {
			this.context = context;
			this.val = val;
		}

		@Override
		public int getCount() {
			
			return val.size();
		}

		@Override
		public Object getItem(int position) {
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.itemdetails_listview_listitem, parent, false);
			TextView tvSpinner = (TextView) rowView.findViewById(R.id.tvSpinner);
			tvSpinner.setText(val.get(position));
			System.out.println(val.get(position));
			return rowView;
		}
	}

    private void buttonAnimation(View v) {
		
    	RightSwipe = AnimationUtils.loadAnimation(NewCaseActivity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ivAttach.setEnabled(true);
		ivAttach.setClickable(true);
	/*	if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			bmp= (Bitmap) data.getExtras().get("data");
		}else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			bmp= (Bitmap) data.getExtras().get("data");
		}*/
		
		if (requestCode==RESULT_LOAD_CAMERA_IMAGE && resultCode==RESULT_OK) {
			Log.e("picturePath", " " + picturePath);
		}else if (requestCode==RESULT_LOAD_IMAGE && data != null && resultCode==RESULT_OK) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
		}else {
			picturePath = "";
		}
	}
	
    public static String getJsonStringMulitipart(String ApiLink, MultipartEntity multipartentity){
		 try{
			 HttpClient httpclient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
		     HttpPost httppost = new HttpPost(ApiLink);
		     httppost.setEntity(multipartentity);
		     
		     HttpResponse response = httpclient.execute(httppost,localContext);
		     if( response == null ){
			     
		      }else{
		    	  HttpEntity entity = response.getEntity();
		          is = entity.getContent();
		          
		      //    //Log.e("log_tag", ""+is.toString());
		      }
		 } catch(Exception e){
			 //Log.e("log_tag", "Error in http connection " + e.toString());
		     return "";
		 }
		 
		 // convert response to string
		 try {
			 BufferedReader reader = new BufferedReader(new InputStreamReader(
					 is, "iso-8859-1"), 8);
		     StringBuilder sb = new StringBuilder();
		     String line = null;
		     while ((line = reader.readLine()) != null) {
		    	 sb.append(line + "\n");
		     }
		     is.close();
		     result = sb.toString();
		     Log.e("res",result);
		 } catch (Exception e) {
			 e.printStackTrace();
			 return "";
		 }
		 return result;
	 }
   
  }
