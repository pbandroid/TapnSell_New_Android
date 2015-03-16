package com.convertingoffers.tapnsell.TapboardShop;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Modal.MyOrderModal;
import com.convertingoffers.tapnsell.Modal.SearchModel;
import com.convertingoffers.tapnsell.Shop.ProductDetails;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.RoundedImageView_CenterCrop;
import com.convertingoffers.tapnsell.util.UserFunctions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class TapBoardMyOrderList extends BaseActivity implements OnClickListener {

	ImageLoader iLoader_item = ImageLoader.getInstance();
	protected ImageLoader iLoader_Rounded = ImageLoader.getInstance();
	Bitmap image_bitmap=null;
	public static DisplayImageOptions options,options_item;
	Animation RightSwipe;
	ListView lvOrder,lvTap;
	EditText evSearch;
	MyOrderCustomAdaper adapter;
	NewListingCustomAdaper	Tapadapter;
	SearchModel myCateForfav;
	int textlength = 0;
	ArrayList<MyOrderModal> text_sort = new ArrayList<MyOrderModal>();
	ArrayList<MyOrderModal> myOrderArray = new ArrayList<MyOrderModal>();
	ArrayList<SearchModel> mNewListing = new ArrayList<SearchModel>();
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	
	Context context;	
	String distance="",reserved="",has_like="",no_of_likes="",latitude="",longitude="",is_last="",orderid="",itemid="",itemuserid="",name="",price="",image="",orderdate="",deliveryestimate="",type="p",shippingservice="",delivereddate="",processing="",price_type = "",shipping="",transit="",delivered="",userid = "",OrderType="D";
	int intStartNewListing=0,count=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_board_order);

		
		iLoader_Rounded.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				/*.showImageOnLoading(R.drawable.profile_user_defualtimage)
				.showImageForEmptyUri(R.drawable.profile_user_defualtimage)
				.showImageOnFail(R.drawable.profile_user_defualtimage)*/
				.resetViewBeforeLoading(true)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true)
				.build();
		
		LoadImage();
		ivMenu.setVisibility(View.VISIBLE);
		ivMenu.setOnClickListener(this);
		tvHeader.setText("My Orders");
		userid = pref.getString("UserID", "");
		if (cd.checkConnection()) {
			myOrderArray.clear();
			text_sort.clear();
			new MyOrderListBackTask().execute("");
			new TapInspireListingBackTask().execute("");
		} else {
			Toast.makeText(context, "internet_conn_failed", Toast.LENGTH_LONG).show();
		}
		
		
		evSearch.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				text_sort.clear();
				textlength = evSearch.getText().length();

				for (int i = 0; i < myOrderArray.size(); i++) {

					MyOrderModal mycate = myOrderArray.get(i);
					String name = mycate.getName();
					String price = mycate.getPrice();
					if (textlength <= name.length()) {
						if(name.toLowerCase().contains(evSearch.getText().toString().toLowerCase()) || price.toLowerCase().contains(evSearch.getText().toString().toLowerCase())){
							text_sort.add(mycate);
						}
					}
				}
				adapter = new MyOrderCustomAdaper(TapBoardMyOrderList.this,
						R.layout.tab_board_myorder_top_list, text_sort);
				lvOrder.setAdapter(adapter);
				// lvt.setAdapter(new CustomAdapter(text_sort));
			}
		});

		ivBack.setOnClickListener(this);
	}
	
	private void LoadImage() {
		image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.message_user_icon);
	}
	@Override
	public void onContentChanged() {
	super.onContentChanged();
	
	context=this;
	lvTap=(ListView)findViewById(R.id.lvTap);
	lvOrder=(ListView)findViewById(R.id.lvOrder);
	evSearch=(EditText)findViewById(R.id.evSearch);
	
	}
	

	private class TapInspireListingBackTask extends AsyncTask<String, Void, String> {
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
				/*
				userid="55";
				latitude="23.0268691";
				longitude="72.5713304";
				intStartNewListing=0;
				*/
				Log.e("userid"," "+userid);
				Log.e("latitude"," "+latitude);
				Log.e("longitude"," "+longitude);
				Log.e("start"," "+intStartNewListing);
				Log.e("limit","50");
				
					json = userFunction.TapInspireFunction
					(userid, latitude,longitude,""+intStartNewListing,type);
				
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						is_last =json.optString("is_last"); // json.get("is_last");
						
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONArray Item = json.getJSONArray("Item");

							for (int i = 0; i < Item.length(); i++) {

								JSONObject c = Item.getJSONObject(i);
								itemid = c.getString("itemid");
								userid = c.getString("userid");
								name = c.getString("name");
								price = c.getString("price");
								distance = c.getString("distance");
								image = UserFunctions.localImageUrl;
								image = image + c.optString("image");
								reserved= c.getString("reserved");
								has_like = c.getString("has_like");
								no_of_likes = c.getString("no_of_likes");

								mNewListing.add(new SearchModel(itemid,
										userid, name, price, distance,
										image, has_like, no_of_likes,
										price_type,reserved));
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
				
				
				Tapadapter = new NewListingCustomAdaper(context,
						R.layout.tab_board_myorder_bottam_list, mNewListing);
				lvTap.setAdapter(Tapadapter);
				lvTap.setSelectionFromTop(count, 0);
				
				
				lvTap.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) { 
						int threshold = 1;
						count = 0;
						count = lvTap.getCount();
						intStartNewListing=intStartNewListing+50;
						if(is_last.equals("N")){								
							if (scrollState == SCROLL_STATE_IDLE) {
								if (lvTap.getLastVisiblePosition() >= count	- threshold) {
									// Execute LoadMoreDataTask AsyncTask
									if (!progressDialog.isShowing()) {
										new TapInspireListingBackTask().execute("");		
									}
															
								}
							}
						}							
					}

					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						
					}

				});		
			} else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else {
//				custom_dialog.dialogCode(2, null, "No TapInspire Item found", con);
				Toast.makeText(context, "No TapInspire Items found", Toast.LENGTH_LONG).show();
//				ValidationSingleButton("no records found....");
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	private class NewListingCustomAdaper extends ArrayAdapter<SearchModel> {
		ViewHolder holder;
		private ArrayList<SearchModel> listSubCategories;

		public NewListingCustomAdaper(Context context, int textViewResourceId,
				ArrayList<SearchModel> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<SearchModel>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivProductImage;
			TextView Tap_tvPrice, tvProductName;
			RelativeLayout rlMainTapInspire;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			holder = null;
			
			
			
			 options_item= new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.transperant)
				.showImageForEmptyUri(R.drawable.transperant)
				.showImageOnFail(R.drawable.transperant)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
			 
			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.tab_board_myorder_bottam_list, null);

				holder = new ViewHolder();
				holder.ivProductImage = (ImageView) convertView.findViewById(R.id.ivProductImage);
				holder.Tap_tvPrice = (TextView) convertView.findViewById(R.id.Tap_tvPrice);
				holder.rlMainTapInspire = (RelativeLayout) convertView.findViewById(R.id.rlMainTapInspire);
				holder.tvProductName = (TextView) convertView.findViewById(R.id.tvProductName);
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			SearchModel myCate = listSubCategories.get(position);

			holder.Tap_tvPrice.setText("$"+ myCate.getPrice());
			holder.tvProductName.setText(myCate.getName());
			
			image=myCate.getImage();
			

			
			
			
			// Image display using lazy loading 

			iLoader_item.displayImage(image,holder.ivProductImage, options_item, new SimpleImageLoadingListener() {
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
			
			
			
				
				
			holder.rlMainTapInspire.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						SearchModel	_list = listSubCategories.get(i);
						
						mItem_IdList.add(_list.getItemid());
					
					}
					
					myCateForfav = listSubCategories.get(position);
					Intent iParent = new Intent(TapBoardMyOrderList.this,ProductDetails.class);
					iParent.putExtra("ItemArray", mItem_IdList);
					iParent.putExtra("position", ""+position);
					startActivity(iParent);

				}
			});

			return convertView;

		}
	}

	
	private class MyOrderListBackTask extends AsyncTask<String, Void, String> {
		String errorMessage;

		protected void onPreExecute() {
			
		}

		@Override
		protected String doInBackground(String... params) {
			{
				JSONObject json;
				//shahil
//				userid="25";
				OrderType="D";
			
					json = userFunction.MyOrderFunction(userid, OrderType);
				
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						is_last =json.optString("is_last"); // json.get("is_last");
						
						if (res.equals(KEY_SUCCESS_STATUS)) {
							
							JSONArray Item = json.getJSONArray("Order");
							for (int i = 0; i < Item.length(); i++) {
								JSONObject c = Item.getJSONObject(i);
								orderid = c.getString("orderid");
								itemid = c.getString("itemid");
								itemuserid = c.getString("itemuserid");
								name = c.getString("name");
								image = UserFunctions.localImageUrl;
								image = image + c.getString("image");
								price = c.getString("price");
								orderdate= c.getString("orderdate");
								
								deliveryestimate= c.getString("deliveryestimate");
								shippingservice= c.getString("shippingservice");
								delivereddate= c.getString("delivereddate");
								processing= c.getString("processing");
								shipping= c.getString("shipping");
								transit= c.getString("transit");
								delivered= c.getString("delivered");
								
								
								myOrderArray.add(new MyOrderModal(orderid, itemid, itemuserid, 
										name, image, price, orderdate, deliveryestimate
									, shippingservice, delivereddate, processing, 
									shipping, transit, delivered));
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
				
				
				for (int i = 0; i < myOrderArray.size(); i++) {
					MyOrderModal cate = myOrderArray.get(i);
					text_sort.add(cate);
				}
				
				adapter = new MyOrderCustomAdaper(context,
						R.layout.itemlisting_child, text_sort);
				lvOrder.setAdapter(adapter);
				
			} else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		}else {
				Custom_Dialog.dialogCode(2, null, "No items found", context);
//				ValidationSingleButton("no records found....");
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
ImageLoadingListener loadImageListener = new ImageLoadingListener() {
		
		@Override
		public void onLoadingStarted(String imageUri, View view) {
			((ImageView) view).setImageBitmap(image_bitmap);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//			Log.i(getClass().getSimpleName(), "Image Loading failed" + imageUri + " REason :" + failReason);
			((ImageView) view).setImageBitmap(image_bitmap);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			Log.i(getClass().getSimpleName(), "Image Loading " + imageUri);
			if (loadedImage != null) {
				
// 		It will Round Image
				loadedImage = RoundedImageView_CenterCrop.scaleCenterCrop(loadedImage, (int) getResources().getDimension(R.dimen.ninetydp), false);
				 
//		If You want only Rounded Corner Just give last argument as true
//				loadedImage = RoundedImageView.scaleCenterCrop(loadedImage,	(int) getResources().getDimension(R.dimen.thirty), true);
				
				((ImageView) view).setImageBitmap(loadedImage);
			} else {

				((ImageView) view).setImageBitmap(image_bitmap);
			}
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			
			
		}
	};
	
	
	private class MyOrderCustomAdaper extends ArrayAdapter<MyOrderModal> {
		ViewHolder holder;
		private ArrayList<MyOrderModal> listSubCategories;

		public MyOrderCustomAdaper(Context context, int textViewResourceId,
				ArrayList<MyOrderModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<MyOrderModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivMyOrderImage;
			TextView tvMyOrderItemName, tvMyOrderDate, tvMyOrderPrice;
			RelativeLayout rlMyorderMain;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.tab_board_myorder_top_list, null);

				holder = new ViewHolder();
				holder.rlMyorderMain = (RelativeLayout) convertView.findViewById(R.id.rlMyorderMain);
				holder.ivMyOrderImage = (ImageView) convertView.findViewById(R.id.ivMyOrderImage);
				holder.tvMyOrderItemName = (TextView) convertView.findViewById(R.id.tvMyOrderItemName);
				holder.tvMyOrderDate = (TextView) convertView.findViewById(R.id.tvMyOrderDate);
				holder.tvMyOrderPrice = (TextView) convertView.findViewById(R.id.tvMyOrderPrice);
				
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			MyOrderModal myCate = listSubCategories.get(position);
		
			holder.tvMyOrderItemName.setText(" "+ myCate.getName());
			holder.tvMyOrderPrice.setText("$ "+ myCate.getPrice());
			holder.tvMyOrderDate.setText(" "+ myCate.getDelivereddate());
						
			
			String UserImage= "";
			UserImage=myCate.getImage();
				if(UserImage.length()>0){
					iLoader_Rounded.displayImage(UserImage,holder.ivMyOrderImage, options, loadImageListener);
				}else{
					iLoader_Rounded.displayImage("", holder.ivMyOrderImage, options, loadImageListener);
				}	
				
			
			holder.rlMyorderMain.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						MyOrderModal	_list = listSubCategories.get(i);
						
						mItem_IdList.add(_list.getItemid());
					}
				MyOrderModal	_order_list= listSubCategories.get(position);
					Intent i = new Intent(TapBoardMyOrderList.this,
							TapBoard_Order_Status.class);
					i.putExtra("orderid", _order_list.getOrderid());
					startActivity(i);
				}
			});
			return convertView;
		}
	}


	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ivBack:
			buttonAnimation(ivBack);
			finish();
			break;
			
		case R.id.ivMenu:
			buttonAnimation(ivMenu);
			Intent i = new Intent(TapBoardMyOrderList.this, TapBoardActivity.class);
			startActivity(i);
			finish();
			break;
			
		default:
			break;
		}
	}
	
	private void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(TapBoardMyOrderList.this,R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
   
}
