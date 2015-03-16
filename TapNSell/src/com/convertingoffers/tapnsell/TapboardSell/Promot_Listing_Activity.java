
package com.convertingoffers.tapnsell.TapboardSell;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Modal.PromotListingModal;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.CustomTextView;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class Promot_Listing_Activity extends BaseActivity implements OnClickListener {
	Bitmap bitmapFromUrl=null;
	Integer count = 0, start = 0, intStartNewListing = 0;
	Animation RightSwipe;
	Context context;
	ArrayList<PromotListingModal> mList = new ArrayList<PromotListingModal>();
	String itemid, userid, name, price, reserved, distance, image, has_like,
			viewhide, no_of_likes, url, is_last,message;
	File casted_image;
	ListView lvPromote_Item;
	PromoteListingCustomAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.promot_tools_list);

		userid = pref.getString("UserID", "");
		tvHeader.setText("Promote Tools");
		ivMenu.setVisibility(View.VISIBLE);

		ivBack.setOnClickListener(this);
		ivMenu.setOnClickListener(this);

		if (cd.checkConnection()) {
			new PromotListingBackTask().execute("");
		} else {
			Toast.makeText(context, "Interner connection is not available!",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context = this;
		lvPromote_Item = (ListView) findViewById(R.id.lvPromote_Item);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {

		case R.id.ivBack:
			finish();
			break;
		case R.id.ivMenu:
			i = new Intent(context, TapBoardActivity.class);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}
	}
	private class PromotListingBackTask extends AsyncTask<String, Void, String> {
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
				if (userid.length() == 0) {
					userid = "0";
				}
				
				Log.e("userid", " " + userid);
				Log.e("start", " " + start);
				JSONObject json = userFunction.PromotListingFunction(userid, ""
						+ start);
				try {
					if (json!=null &&json.getString(KEY_SUCCESS) != null) {

						String res = json.getString(KEY_SUCCESS);
						is_last = json.getString("is_last");
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONArray Item = json.getJSONArray("Item");
							for (int i = 0; i < Item.length(); i++) {
								JSONObject Jmsg = Item.getJSONObject(i);

								itemid = Jmsg.getString("itemid");
								userid = Jmsg.getString("userid");
								name = Jmsg.getString("name");
								price = Jmsg.getString("price");
								reserved = Jmsg.getString("reserved");
								distance = Jmsg.getString("distance");
								image=UserFunctions.localImageUrl;
								image = image+Jmsg.getString("image");
								has_like = Jmsg.getString("has_like");
								viewhide = Jmsg.getString("viewhide");
								no_of_likes = Jmsg.getString("no_of_likes");
								//url=UserFunctions.localImageUrl;
								url =Jmsg.getString("url");
								mList.add(new PromotListingModal(itemid,
										userid, name, price, reserved,
										distance, image, has_like, viewhide,
										no_of_likes, url,true));

							}

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

				adapter = new PromoteListingCustomAdapter(context,
						R.layout.promote_tools_child, mList);
				lvPromote_Item.setAdapter(adapter);

				lvPromote_Item.setAdapter(adapter);
				lvPromote_Item.setSelectionFromTop(count, 0);

				lvPromote_Item.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) { 
						
						int threshold = 1;
						count = 0;
						count = lvPromote_Item.getCount();
						intStartNewListing = intStartNewListing + 50;
						if (is_last.equals("N")) {
							if (scrollState == SCROLL_STATE_IDLE) {
								if (lvPromote_Item.getLastVisiblePosition() >= count
										- threshold) {
									// Execute LoadMoreDataTask AsyncTask
									new PromotListingBackTask().execute("");
								}
							}
						}
					}

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						
					}

				});

			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		} else {
				Custom_Dialog.dialogCode(2, null, "Please try again later!",
						Promot_Listing_Activity.this);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	public class PromoteListingCustomAdapter extends
			ArrayAdapter<PromotListingModal> {
		ViewHolder holder;
		private ArrayList<PromotListingModal> listSubCategories;
		ImageLoader iLoader_item = ImageLoader.getInstance();
		DisplayImageOptions options;

		public PromoteListingCustomAdapter(Context context,
				int textViewResourceId,
				ArrayList<PromotListingModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<PromotListingModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivEmail, ivSms, ivShare, ivClose, ivAnnouces, ivDefault;
			CustomTextView tvName;
			LinearLayout llPromottool;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

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
			
			
			holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.promote_tools_child, null);

				holder.ivEmail = (ImageView) convertView.findViewById(R.id.ivEmail);
				holder.ivSms = (ImageView) convertView.findViewById(R.id.ivSms);
				holder.ivShare = (ImageView) convertView.findViewById(R.id.ivShare);
				holder.ivClose = (ImageView) convertView.findViewById(R.id.ivClose);
				holder.ivAnnouces = (ImageView) convertView.findViewById(R.id.ivAnnouces);
				holder.ivDefault = (ImageView) convertView.findViewById(R.id.ivDefault);
				holder.tvName = (CustomTextView) convertView.findViewById(R.id.tvName);
				
				holder.llPromottool = (LinearLayout) convertView
				.findViewById(R.id.llPromottool);
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PromotListingModal m_ItemListing;
			m_ItemListing = listSubCategories.get(position);

			holder.tvName.setText(m_ItemListing.getName());

			
				
				if(m_ItemListing.isStatus_btn()){
					holder.ivAnnouces.setVisibility(View.GONE);
					holder.llPromottool.setVisibility(View.VISIBLE);
				}else{
					holder.ivAnnouces.setVisibility(View.VISIBLE);
					holder.llPromottool.setVisibility(View.GONE);
				}
			
			
			String img = null;
			img=m_ItemListing.getImage();
			message="I'm selling it on TapNSell. If you know anyone that may be interested please forward to them. Thanks! "+m_ItemListing.getUrl();
		/*	if (img != null) {
				imageLoader.DisplayImage(img, holder.ivDefault);
			} else {
				holder.ivDefault
						.setImageResource(R.drawable.list_item_image_frame);
			}*/

			// Image display using lazy loading 

			iLoader_item.displayImage(img, holder.ivDefault, options, new SimpleImageLoadingListener() {
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
			
			
			
			
			holder.ivClose.setTag(m_ItemListing);
			holder.ivClose.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					buttonAnimation(v);
					PromotListingModal m_promote_item;
					m_promote_item=(PromotListingModal) v.getTag();
					m_promote_item.setStatus_btn(false);
					listSubCategories.set(position, m_promote_item);
					notifyDataSetChanged();
					
				}
			});
			holder.ivAnnouces.setTag(m_ItemListing);
			holder.ivAnnouces.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					buttonAnimation(v);
					PromotListingModal m_promote_item;
					m_promote_item = (PromotListingModal) v.getTag();
					m_promote_item.setStatus_btn(true);
					listSubCategories.set(position, m_promote_item);
					notifyDataSetChanged();
				}
			});
			
			holder.ivShare.setTag(m_ItemListing);
			holder.ivShare.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					buttonAnimation(v);
					PromotListingModal m_item = (PromotListingModal) v.getTag();
					//
					/*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
					sharingIntent.setType("text/plain");
					sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
							Html.fromHtml("Check this out:"));
					sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							Html.fromHtml(message));
					startActivity(Intent.createChooser(sharingIntent,"TapnSell"));*/
				/*	
					
					Intent emailIntent = new Intent();
				      emailIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
				      // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
				      emailIntent.setType("message/rfc822");
				      emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(message));
				      emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Check this out:");    
//				      emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				      startActivity(Intent.createChooser(emailIntent,
						"TapnSell"));*/
					
//					new
					
					Log.e("share image"," "+ m_item.getImage().toString());
				//new UrlBitmap(m_item.getImage().toString(),m_item.getUrl().toString()).execute("");
					new UrlconversionBackTask("shar",m_item.getUrl().toString()).execute("");   
				}
			});
			
			holder.ivSms.setTag(m_ItemListing);
			holder.ivSms.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PromotListingModal listing = (PromotListingModal) v.getTag();
					message ="Know anyone who might be interested in my "+listing.getName()+" item  I'm selling on TapNSell? /n"+listing.getUrl();
			
					buttonAnimation(v);
					PromotListingModal m_item = (PromotListingModal) v.getTag();
					Intent iSms = new Intent(context,Tap_sell_SellItFasterContact_sms.class);
					iSms.putExtra("url", "" + m_item.getUrl());
					iSms.putExtra("msg", "" + message);
					startActivity(iSms);
				}
			});
			holder.ivEmail.setTag(m_ItemListing);
			holder.ivEmail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					buttonAnimation(v);
					//message="Need a small favor… \n\n I’m selling this item on TapNSell. If you Know anyone who might be interested can you please forward this email.\n\n"+listing.getUrl()+"\n\nThanks! ";
					
				
					
					
					if (cd.checkConnection()) {
						new UrlconversionBackTask("email","").execute("");
					} else {
						Toast.makeText(Promot_Listing_Activity.this,
								"Interner connection is not available!", Toast.LENGTH_LONG)
								.show();
					}
					
					
				
				}
			});

			return convertView;

		}
	}

	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(Promot_Listing_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
	private class UrlconversionBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,comefrom,normalurl;
		public UrlconversionBackTask(String type, String nurl) {
			comefrom=type;
			normalurl=nurl;
		}

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();////
		}
		@Override
		protected String doInBackground(String... params) {
			{
				JSONObject json;
				if(comefrom.equals("email")){
					 json = userFunction.UrlConvertFunction(url);	
				}else{
					 json = userFunction.UrlConvertFunction(normalurl);
				}
				
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {

						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
							url=json.optString("url");
							errorMessage = "true";
						} else {
							errorMessage = "false";
						}
					} else {
						errorMessage = "error in posting";
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
			if(comefrom.equals("email")){
				
				if(url==null && url.equals("")){
					url=normalurl;
				}
			String	first_name= pref.getString("reg_fname", "");
			String	last_name= pref.getString("reg_lname", "");
			String name=first_name+"  "+last_name;
			if(name!=null && !name.equals("")){
				name.replaceAll("null","");	
			}
			message=" Need a small favor… \n\n   I’m selling this item on TapNSell. If you Know anyone who might " +
			"be interested can you please forward this email.\n\n"+url+"\n\nThanks!\n"+name+" \n"+normalurl+"\n\n  P.S. I love you. Turn unwanted stuff into cash with this cool app \n\n http://goo.gl/k7idfJ";

			Intent iSms = new Intent(context,
					Tap_sell_SellItFasterContact_email.class);
			iSms.putExtra("url", "");
			iSms.putExtra("msg", "" + message);
			startActivity(iSms);
		}else if (result.equals("network")) {
			String	message="Your internet connection is too slow";
			Custom_Dialog.dialogCode(2, null,message, context);
		}else{
			if(url==null && url.equals("")){
				url=normalurl;
			}
		share(url);
		}
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}


//	private class UrlBitmap extends AsyncTask<String, Void, String> {
//		String errorMessage,img_url,normalurl="";
//
//		public UrlBitmap(String url, String ur) {
//			img_url=url;
//			normalurl=ur;
//		}
//
//		protected void onPreExecute() {
//			progressDialog = new ProgressDialog(Promot_Listing_Activity.this);
//			progressDialog.setMessage("Please wait");
//			progressDialog.setCanceledOnTouchOutside(false);
//			progressDialog.setCancelable(false);
//			progressDialog.show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			{
//				getBitmapFromURL(img_url);
//				
//			}
//			return errorMessage;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (progressDialog.isShowing()) {
//				progressDialog.dismiss();
//			}
//			
//		
//
//		}
//	}

	public void share(String normalurl) {
	
		  //Intent share = new Intent(Intent.ACTION_SEND);
		  
		/*  ArrayList<Uri> files = new ArrayList<Uri>();
		  if(casted_image.exists()){
			  files.add(Uri.fromFile(casted_image));
			  Log.e("casted_image", " "+casted_image);
		  }*/
		  
			message="I'm selling it on TapNSell. If you know anyone that may be interested" +
					" please forward to them.    "+url+"\n\n Turn unwanted stuff into cash with this cool app    http://goo.gl/k7idfJ \n\n Thanks!";
		 
		 //  Resources resources = context.getResources();

		      Intent emailIntent = new Intent();
		      emailIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
		      // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
		      emailIntent.setType("image/*"); 
		      emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(message));
		      emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Check this out:");    
		      emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//		      emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(casted_image));
		      emailIntent.setType("message/rfc822");

		      PackageManager pm = context.getPackageManager();
		      Intent sendIntent = new Intent(Intent.ACTION_SEND);     
		      sendIntent.setType("text/plain");

		      Intent openInChooser = Intent.createChooser(emailIntent, "Share using");

		      List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
		      List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();        
		      for (int i = 0; i < resInfo.size(); i++) {
		          // Extract the label, append it, and repackage it in a LabeledIntent
		          ResolveInfo ri = resInfo.get(i);
		          String packageName = ri.activityInfo.packageName;
		          if(packageName.contains("android.email")) {
		              emailIntent.setPackage(packageName);
		          } else if(packageName.contains("plus")||packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm")) {
		              Intent intent = new Intent();
		              intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
		              intent.setAction(Intent.ACTION_SEND);
		              intent.setType("text/plain");
		              if(packageName.contains("twitter")) {
		                  intent.putExtra(Intent.EXTRA_TEXT,message);
		              } else if(packageName.contains("facebook")) {
		                  // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
		                  // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
		                  // will show the <meta content ="..."> text from that page with our link in Facebook.
		               intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		               intent.setType("image/*");               
		               intent.putExtra(Intent.EXTRA_TEXT, message);
		               intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//		               intent.putExtra(Intent.EXTRA_STREAM, files);
		            
		              } else if(packageName.contains("mms")) {
		                  intent.putExtra(Intent.EXTRA_TEXT, message);
		              } else if(packageName.contains("android.gm")) {
		               intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		               intent.setType("image/*"); 
		                  intent.putExtra(Intent.EXTRA_TEXT, message);
		                  intent.putExtra(Intent.EXTRA_SUBJECT,  "Check this out:");    
		                  intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//		                  intent.putExtra(Intent.EXTRA_STREAM, files);
		                  intent.setType("message/rfc822");
		              }

		              intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
		          }
		      }

		      // convert intentList to array
		      LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

		      openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
		      context.startActivity(openInChooser);       
		  
		 }

	public void getBitmapFromURL(String img_url) {
		
		try {
	        URL url = new URL(img_url);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        bitmapFromUrl = BitmapFactory.decodeStream(input);
	        
	        if(bitmapFromUrl!=null){
	   	 try {
	   		 
	   		 File myDir3 = new File(android.os.Environment.getExternalStorageDirectory(),"/TapNSell/ShareImages");
	     	
	 	    casted_image = new File(myDir3, "PromoteSharing.jpg");
	 	    if (casted_image.exists()) {
	 		casted_image.delete();
	 	    }	   
	 			casted_image.createNewFile();
	 			FileOutputStream fos = new FileOutputStream(casted_image);
	 			
	 			bitmapFromUrl.compress(Bitmap.CompressFormat.PNG, 50, fos);
	 		    fos.flush();
	 		    fos.close();
	 		   
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 			 System.out.print(e);
	 		}

	        }
	    } catch (IOException e) {
	    	Log.e("catch", "catch");
	        e.printStackTrace();
	        casted_image=null;
	    }
	
	}
	
}
