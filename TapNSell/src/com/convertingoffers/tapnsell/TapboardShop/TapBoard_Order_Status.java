package com.convertingoffers.tapnsell.TapboardShop;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.Shop.ChatActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class TapBoard_Order_Status extends BaseActivity implements OnClickListener {

	ImageLoader iLoader_item = ImageLoader.getInstance();
	DisplayImageOptions options;
	TextView tvPrice,tvName,tvShippingService,tvDeliverEstimate,tvDeliverEstimateTime;
	RelativeLayout rlImage;
	ImageView ivRefund,ivFileCase,ivContactSeller,ivTrackingNo,ivProductImage,ivStatus,ivStautsText;
	
	String orderid="",itemid="",itemuserid="",trackingurl="",name="",image="",price="",orderdate="",deliveryestimate="",shippingservice="",delivereddate="",processing="",shipping="",transit="",delivered="",userid="";
	Context context;
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	Dialog dialog;
	ProgressBar progressBar;
	WebView webEbay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.tab_board_order_status);
	
//	list_item_image_frame
	userid = pref.getString("UserID", "");
	if (cd.checkConnection()) {
		new MyOrderStatusBackTask().execute("");
	
	} else {
		Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
	}
	
	Bundle bundle = getIntent().getExtras();
	if (bundle != null) {
		orderid = bundle.getString("orderid");
	}
	
	
	tvHeader.setText("Order Status");
	ivBack.setOnClickListener(this);
	ivContactSeller.setOnClickListener(this);
	ivFileCase.setOnClickListener(this);
	ivRefund.setOnClickListener(this);
	ivTrackingNo.setOnClickListener(this);
}
	
	@Override
	public void onContentChanged() {
	super.onContentChanged();
	context=this;
	 iLoader_item.init(ImageLoaderConfiguration.createDefault(context));
	 options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.list_item_image_frame)
		.showImageForEmptyUri(R.drawable.list_item_image_frame)
		.showImageOnFail(R.drawable.list_item_image_frame)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
	tvName=(TextView)findViewById(R.id.tvName);
	tvShippingService=(TextView)findViewById(R.id.tvShippingService);
	tvDeliverEstimate=(TextView)findViewById(R.id.tvDeliverEstimate);
	tvDeliverEstimateTime=(TextView)findViewById(R.id.tvDeliverEstimateTime);
	tvPrice=(TextView)findViewById(R.id.tvPrice);
	
	rlImage=(RelativeLayout)findViewById(R.id.rlImage);
	
	ivStatus=(ImageView)findViewById(R.id.ivStatus);
	ivRefund=(ImageView)findViewById(R.id.ivRefund);
	ivFileCase=(ImageView)findViewById(R.id.ivFileCase);
	ivContactSeller=(ImageView)findViewById(R.id.ivContactSeller);
	ivTrackingNo=(ImageView)findViewById(R.id.ivTrackingNo);
	ivProductImage=(ImageView)findViewById(R.id.ivProductImage);
	}
	
	private class MyOrderStatusBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

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
				JSONObject json;
				
				Log.e("userid", " "+userid);
				Log.e("orderid", " "+orderid);
					json = userFunction.OrderDetailsFunction(userid, orderid);
				
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONObject jobj=json.getJSONObject("Order");
							orderid=jobj.getString("orderid");
							itemid=jobj.getString("itemid");
							itemuserid=jobj.getString("itemuserid");
							name=jobj.getString("name");
							image = UserFunctions.localImageUrl;
							image=image+jobj.getString("image");
							
							price=jobj.getString("price");
							orderdate=jobj.getString("orderdate");
							deliveryestimate=jobj.getString("deliveryestimate");
							shippingservice=jobj.getString("shippingservice");
							delivereddate=jobj.getString("delivereddate");
							shipping=jobj.getString("shipping");
							processing=jobj.getString("processing");
							transit=jobj.getString("transit");
							delivered=jobj.getString("delivered");
							trackingurl=jobj.optString("trackingurl");
							
							errorMessage = "true";

						} else {
							errorMessage = "false";
						}
					} else {
						errorMessage = "network";
					}

				} catch (JSONException e) {
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
			
				rlImage.setTag(""+orderid);
				Log.e("image", " "+image);
				tvName.setText(""+name);
				tvShippingService.setText(""+shippingservice);
				tvDeliverEstimate.setText(""+deliveryestimate);
				tvDeliverEstimateTime.setText(""+delivereddate);
				tvPrice.setText(""+price);
				
				// Image display using lazy loading 

				iLoader_item.displayImage(image,ivProductImage, options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view, int current, int total) {
					}
				});
				
				// Image display using lazy loading
				
				
				
				
				if(processing.equals("Y")){
					ivStatus.setImageResource(R.drawable.order_status_processing_stripe);	
				}else if(shipping.equals("Y")){
					ivStatus.setImageResource(R.drawable.order_status_ship_stripe);	
				}else if(transit.equals("Y")){
					ivStatus.setImageResource(R.drawable.order_status_intransit_stripe);	
				}else  if(delivered.equals("Y")){
					ivStautsText.setImageResource(R.drawable.order_status_delivered_stripe);	
					ivStatus.setImageResource(R.drawable.order_status_delivered_stripe);	
				}else{
					ivStatus.setImageResource(R.drawable.order_status_backstripe);	
				}
				
				
			} else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else {
				Custom_Dialog.dialogCode(2, null, "No order found", context);
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

   @Override
   public void onClick(View v) {
	   Intent i;
	switch (v.getId()) {
		case R.id.ivBack:
			finish();
			break;
			
		case R.id.ivContactSeller:

			mItem_IdList.clear();
			mItem_IdList.add(itemid);
			 i = new Intent(TapBoard_Order_Status.this,ChatActivity.class);
			i.putExtra("itemid", itemid);
			i.putExtra("from_id",itemuserid);
			i.putExtra("Distance", "0");
			i.putExtra("ItemArray", mItem_IdList);
			i.putExtra("position", "0");
			startActivity(i);
			
			break;
			
		case R.id.ivFileCase:

			Two_Button_dialogCode("Are you sure you want to file the case?",true);
			
			break;
		case R.id.ivRefund:

			Two_Button_dialogCode("Are you sure you want to request for refund?",false);
			
			break;
		case R.id.ivTrackingNo:

			
			if(trackingurl.length()==0){
				Two_dialogCode("Are you sure you want to request for Tracking?");	
			}else{
				WebviewdialogCode();
			}
			
			
			break;
			
			
		default:
			break;
		}
	}
   
   
   @SuppressWarnings("static-access")
   public   void Two_Button_dialogCode(String msg, final boolean bool) {
   	
   	// custom dialog
   	Log.e("testdialogCode", "testdialogCode");
   	final Dialog  dialog = new Dialog(TapBoard_Order_Status.this);
   	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
   	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
   	dialog.setContentView(R.layout.dialog_custom_2_button);
   	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
       lp.copyFrom(dialog.getWindow().getAttributes());
       lp.width = lp.MATCH_PARENT;
       lp.height = lp.MATCH_PARENT;
       dialog.getWindow().setAttributes(lp);
       dialog.show();
   	ImageView ivNo = (ImageView) dialog.findViewById(R.id.ivNo);
   	ImageView ivYes = (ImageView) dialog.findViewById(R.id.ivYes);
   	TextView tvMsg= (TextView) dialog.findViewById(R.id.tvMsg);
   	tvMsg.setText(msg);
   	ivYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
				if(bool){
					Intent	 i = new Intent(TapBoard_Order_Status.this,NewCaseActivity.class);
					i.putExtra("orderid", orderid);
					startActivity(i);		
				}else{
					
					if (cd.checkConnection()) {
						new RequestRefundBackTask().execute("");
					
					} else {
						Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
					}
					
					
					
					
				}
			
			}
		});
   	ivNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				dialog.dismiss();
			}
		});
   	
   }
   
   
   @SuppressWarnings("static-access")
public   void Two_dialogCode(String msg) {
   	
   	// custom dialog
   	Log.e("testdialogCode", "testdialogCode");
   	final Dialog  dialog = new Dialog(TapBoard_Order_Status.this);
   	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
   	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
   	dialog.setContentView(R.layout.dialog_custom_2_button);
   	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
       lp.copyFrom(dialog.getWindow().getAttributes());
       lp.width = lp.MATCH_PARENT;
       lp.height = lp.MATCH_PARENT;
       dialog.getWindow().setAttributes(lp);
       dialog.show();
   	ImageView ivNo = (ImageView) dialog.findViewById(R.id.ivNo);
   	ImageView ivYes = (ImageView) dialog.findViewById(R.id.ivYes);
   	TextView tvMsg= (TextView) dialog.findViewById(R.id.tvMsg);
   	tvMsg.setText(msg);
   	ivYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
					
					if (cd.checkConnection()) {
						new RequestTrackingBackTask().execute("");
					
					} else {
						Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
					}
					
			}
		});
   	ivNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				dialog.dismiss();
			}
		});
   	
   }
   
   
	private class RequestRefundBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json;
				
				Log.e("userid", " "+userid);
				Log.e("orderid", " "+orderid);
					json = userFunction.RequestRefundFunction(userid, orderid);
				
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							message=json.optString("message");
							errorMessage = "true";

						} else {
							message=json.optString("message");
							errorMessage = "false";
						}
					} else {
						message=json.optString("message");
						errorMessage = "error in posting";
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return errorMessage;
		}

		@SuppressLint("NewApi") 
		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			if (result.equals("true")) {
			
				Custom_Dialog.dialogCode(2, null,message, context);
				
			} else {
				Custom_Dialog.dialogCode(2, null,message, context);
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	
   
	private class RequestTrackingBackTask extends AsyncTask<String, Void, String> {
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
				JSONObject json;
				
				Log.e("userid", " "+userid);
				Log.e("orderid", " "+orderid);
					json = userFunction.RequestTrackingFunction(userid, orderid);
				
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							message=json.optString("message");
							errorMessage = "true";

						} else {
							message=json.optString("message");
							errorMessage = "false";
						}
					} else {
						message=json.optString("message");
						errorMessage = "network";
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return errorMessage;
		}

		@SuppressLint("NewApi") 
		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			if (result.equals("true")) {
			
				Custom_Dialog.dialogCode(2, null,message, context);
				
			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		} else {
				Custom_Dialog.dialogCode(2, null,message, context);
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" }) @SuppressWarnings("static-access")
	public void WebviewdialogCode() {

		 dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_ebay_webview);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
		WebView	webViewTerms= (WebView) dialog.findViewById(R.id.webEbay);
		 progressBar=(ProgressBar)dialog.findViewById(R.id.progressBar1);
		
		webViewTerms.getSettings().setJavaScriptEnabled(true);
		webViewTerms.setWebViewClient(new MyWebViewClient());
		webViewTerms.loadUrl(trackingurl);
		webViewTerms.addJavascriptInterface(new JavaScriptInterface(), "HtmlViewer");
		
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			dialog.dismiss();	
			}
		});
		


		dialog.show();
	}


	class JavaScriptInterface {
		public void showHTML(String html) {
			if (html.contains("userid")) {
				try {
					Document doc = Jsoup.parse(html);
					Elements ele = doc.select("div[class=mbg]");
					Elements span = ele.select("span[class=mbg-l]");
					Elements mbgfb = span.select("a[class=mbg-fb]");
					String useridurl = mbgfb.attr("href");
					Log.e("Element",
							useridurl.substring(useridurl.indexOf("=") + 1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class MyWebViewClient extends WebViewClient {

		String LOG_TAG = "Login";

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(LOG_TAG, "onPageStarted " + url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.d(LOG_TAG, "onPageFinished " + url);
			progressBar.setVisibility(View.GONE);
			
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

		/*	if (url.contains("username")) {
				String[] args = url.split("&");
				String usrnmarg = args[(args.length) - 1];
				String[] usrnm = usrnmarg.split("=");
				Log.e("shouldoverride", "username  " + usrnm[1]);
				isUrlMyEbay = true;
			}*/
			view.loadUrl(url);
			return true;
		}
	}
	
	
	
}