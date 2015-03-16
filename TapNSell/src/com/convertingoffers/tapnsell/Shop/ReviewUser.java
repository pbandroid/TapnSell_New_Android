package com.convertingoffers.tapnsell.Shop;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.Modal.ReviewCategoryModal;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;
import com.convertingoffers.tapnsell.util.ExpandableTextView;
import com.convertingoffers.tapnsell.util.RoundedImageView_CenterCrop;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ReviewUser extends BaseActivity implements OnClickListener{

	protected ImageLoader iLoader_Rounded  = ImageLoader.getInstance();;
	public static DisplayImageOptions options;
	Context con;
	ReviewListingCustomAdaper adapter;
	int intStartNewListing=0,count=0;
	TextView tvUserName,tvUserRatting,tvBuy,tvSale,tvCurrent;
	ImageView ivProduct;
	ListView lvtListReview;
	String is_last,name,image,rating,ratingcount,totalsell,totalbuy,totallisting,userid="";
	Bitmap image_bitmap=null;
	RatingBar rb_rating;
	LinearLayout llCurretListing;
	ArrayList<ReviewCategoryModal> m_Review_List= new ArrayList<ReviewCategoryModal>();
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_review);
		
		
		userid = pref.getString("UserID", "");
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
		userid = bundle.getString("Seller_userid");
		}else{
			userid="0";	
		}
		
		Log.e("userid", " "+userid);
		LoadImage();
		iLoader_Rounded.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.build();
		ivBack.setOnClickListener(this);
		llCurretListing.setOnClickListener(this);
		tvHeader.setText("User Reviews");
		if(cd.checkConnection()){
			new ReviewBackTask().execute("");
		}else{
			Toast.makeText(ReviewUser.this, "Interner connection is not available!",Toast.LENGTH_LONG).show();
		}
	}
	
	private void LoadImage() {
		
		image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chat_item_light_blue_bg_user);

	}
	private class ReviewBackTask extends AsyncTask<String, Void, String> {
		String errorMessage,message;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ReviewUser.this);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{
				JSONObject json = userFunction.ReviewListFunction(userid,""+intStartNewListing);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {
								  is_last =json.getString("is_last");
								
							JSONObject j_User=json.getJSONObject("User");
													
							name=j_User.getString("name");
//							image =UserFunctions.localImageUrl;
							image=j_User.getString("image");
							rating=j_User.getString("rating");
							ratingcount=j_User.getString("ratingcount");
							totalsell=j_User.getString("totalsell");
							totalbuy=j_User.getString("totalbuy");
							totallisting=j_User.getString("totallisting");
							
							JSONArray j_array =json.getJSONArray("reviews");
							
							for (int i = 0; i < j_array.length(); i++) {
								String username,User_image,comment,rate,date;
								JSONObject c = j_array.getJSONObject(i);
								username=c.getString("username");
								User_image=c.getString("image");
								comment=c.getString("comment");
								rate=c.getString("rating");
								date=c.getString("date");
								
								m_Review_List.add(new ReviewCategoryModal(username, User_image, comment, rate, date));
							}
							
							errorMessage = "true";
						}else{
							message=json.optString("message");
							JSONObject j_User=json.getJSONObject("User");
							
							name=j_User.getString("name");
//							image =UserFunctions.localImageUrl;
							image=j_User.getString("image");
							rating=j_User.getString("rating");
							ratingcount=j_User.getString("ratingcount");
							totalsell=j_User.getString("totalsell");
							totalbuy=j_User.getString("totalbuy");
							totallisting=j_User.getString("totallisting");
							
							errorMessage = "flase";
							
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
			
			 if (result.equals("network")) {
				String	message="Your internet connection is too slow";
					Custom_Dialog.dialogCode(2, null,message, ReviewUser.this);
//					ValidationSingleButton("Error in posting");
				}else{
				if(image.length()>0){
					iLoader_Rounded.displayImage(image, ivProduct, options, loadImageListener);
				}else{
					iLoader_Rounded.displayImage("", ivProduct, options, loadImageListener);
				}	
				
				tvUserName.setText(""+name);
				tvSale.setText(""+totalsell);
				tvBuy.setText(""+totalbuy);
				tvCurrent.setText(""+totallisting);
				Log.e("ratingcount", " "+rating);
				tvUserRatting.setText("("+rating+") "+" Based on "+ratingcount+" reviews");
				if(rating.length()==0){
					rating="0";
				}
				
				float rate_count=Float.parseFloat(rating);
				
				rb_rating.setRating(rate_count);
				if (result.equals("true")) {
				adapter = new ReviewListingCustomAdaper(con,
						R.layout.itemlisting_child, m_Review_List);
				lvtListReview.setAdapter(adapter);
				lvtListReview.setSelectionFromTop(count, 0);
				
				
				lvtListReview.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) { 
						int threshold = 1;
						count = 0;
						count = lvtListReview.getCount();
						intStartNewListing=intStartNewListing+50;
						if(is_last.equals("N")){								
							if (scrollState == SCROLL_STATE_IDLE) {
								if (lvtListReview.getLastVisiblePosition() >= count	- threshold) {
									// Execute LoadMoreDataTask AsyncTask
									new ReviewBackTask().execute("");								
								}
							}
						}							
					}

					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						
					}
				});		
			
				}
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

	

	private class ReviewListingCustomAdaper extends ArrayAdapter<ReviewCategoryModal> {
		ViewHolder holder;
		private ArrayList<ReviewCategoryModal> listSubCategories;

		public ReviewListingCustomAdaper (Context context, int textViewResourceId,
				ArrayList<ReviewCategoryModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<ReviewCategoryModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivUserImage;
			TextView tvUserName,tvDate;
			ExpandableTextView ETReview;
			RatingBar rb_User_review_Item;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.user_review_item, null);

				holder = new ViewHolder();
				holder.ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
				holder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
				holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
				holder.ETReview = (ExpandableTextView) convertView.findViewById(R.id.ETReview);
				holder.rb_User_review_Item= (RatingBar) convertView.findViewById(R.id.rb_User_review_Item);
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			

			ReviewCategoryModal myCate = listSubCategories.get(position);
		
			
			holder.tvUserName.setText(myCate.getUsername());
			 holder.tvDate.setText(myCate.getDate());
			 holder.ETReview.setText(myCate.getComment());
			 String iv="";
			 iv=myCate.getImage();
				if(image.length()>0){
					iLoader_Rounded.displayImage(iv, holder.ivUserImage, options, loadImageListener);
				}else{
					iLoader_Rounded.displayImage("", holder.ivUserImage, options, loadImageListener);
				}
				String Rating =myCate.getRating();
				if(Rating.length()==0){
					Rating="0";
				}
				float rate = Float.parseFloat(Rating);
				holder.rb_User_review_Item.setRating(rate);
				
			return convertView;

		}
	}

	@Override
	public void onContentChanged() {
		
		super.onContentChanged();
		
		con=this;
		llCurretListing=(LinearLayout)findViewById(R.id.llCurretListing);
		rb_rating=(RatingBar)findViewById(R.id.rb_rating);
		tvBuy=(TextView)findViewById(R.id.tvBuy);
		tvSale=(TextView)findViewById(R.id.tvSale);
		tvCurrent=(TextView)findViewById(R.id.tvCurrent);
		tvUserName=(TextView)findViewById(R.id.tvUserName);
		tvUserRatting=(TextView)findViewById(R.id.tvUserRatting);
		ivProduct=(ImageView )findViewById(R.id.ivProduct);
		lvtListReview=(ListView)findViewById(R.id.lvtListReview);
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
@Override
public void onClick(View v) {
	
	switch (v.getId()) {
	case R.id.ivBack:
	
		finish();
		
		break;
	case R.id.llCurretListing:
		
		Intent i =new Intent(con, MyListingActiveSoldExpireActivity.class);
		startActivity(i);
	
		
		break;
		
	default:
		break;
	}
}
}
