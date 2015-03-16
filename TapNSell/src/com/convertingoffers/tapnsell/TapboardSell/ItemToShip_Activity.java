package com.convertingoffers.tapnsell.TapboardSell;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.Modal.ItemToShipModal;
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

public class ItemToShip_Activity extends BaseActivity implements OnClickListener {
	String Carrier="",TrackingNo="";
	ListView lv_Item;
	Animation RightSwipe;
	Context context;
	ArrayList<String> mType = new ArrayList<String>();
	ArrayList<ItemToShipModal> mItemToShip = new ArrayList<ItemToShipModal>();
	ArrayList<String> mItem_IdList = new ArrayList<String>();
	String orderid="",itemid="",toid="",msg="",Ship_orderid="",categoryid="",userid="",
	name="",price="",address="",distance="",image="",is_shipped="";
	ItemTOShipCustomAdapter Shipadapter;
	ListView lvt;
	Adapter adapter;
	Dialog	dialogSpinner,dialog ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_to_ship);

		userid = pref.getString("UserID", "");
		tvHeader.setText("Item To Ship");
		ivMenu.setVisibility(View.VISIBLE);
		ivBack.setOnClickListener(this);
		ivMenu.setOnClickListener(this);
		MakeSpinnerArray();
		
		if (cd.checkConnection()) {
			new ItemToShipBackTask().execute("");
		} else {
			Toast.makeText(context,
					"Interner connection is not available!",
					Toast.LENGTH_LONG).show();
		}
		
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context=this;
		lv_Item = (ListView) findViewById(R.id.lv_Item);
	}

	@Override
	public void onClick(View v) {
		Intent  i;
		switch (v.getId()) {
		case R.id.ivMenu:
		
			i=new Intent(context, TapBoardActivity.class);
			startActivity(i);
			finish();
			
			break;
	
		case R.id.ivBack:
			i=new Intent(context, TODOS_Activity.class);
			startActivity(i);
			finish();
			break;
	
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
	super.onBackPressed();
	Intent i=new Intent(context, TODOS_Activity.class);
	startActivity(i);
	finish();
	}
	
	
	private class ItemToShipBackTask extends
			AsyncTask<String, Void, String> {
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
				if (userid.length() == 0) {
					userid = "0";
				}
				
				Log.e("userid", " " + userid);
				JSONObject json = userFunction.ItemToShipFunction(userid);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							JSONArray Item = json.getJSONArray("Item");
							for (int i = 0; i < Item.length(); i++) {
								JSONObject objItem = Item.getJSONObject(i);
								is_shipped=objItem.getString("is_shipped");
								orderid=objItem.getString("orderid");
								itemid=objItem.getString("itemid");
								toid=objItem.getString("userid");
								categoryid=objItem.getString("categoryid");
								name=objItem.getString("name");
								price=objItem.getString("price");
								address=objItem.getString("address");
								distance=objItem.getString("distance");
								image = UserFunctions.localImageUrl;
								image=image+objItem.getString("image");
								mItemToShip.add(new ItemToShipModal(orderid, itemid, userid, categoryid, name, price, address, distance, image,is_shipped));
								message=json.optString("message");
							}
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

		@Override
		protected void onPostExecute(String result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result.equals("true")) {


				Shipadapter = new ItemTOShipCustomAdapter(context,R.layout.item_to_ship_child, mItemToShip);
				lv_Item.setAdapter(Shipadapter);

			}else if (result.equals("network")) {
				String	message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
		} else {
				Custom_Dialog.dialogCode(2, null,message,
						ItemToShip_Activity.this);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}
	
	
	private class ShipedOrderBackTask extends
	AsyncTask<String, Void, String> {
		LinearLayout llBtnbg1;
String errorMessage;

public ShipedOrderBackTask(ItemToShipModal m_item, LinearLayout llBtnbg) {
	// TODO Auto-generated constructor stub
	llBtnbg1=llBtnbg;
}

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
		JSONObject json = userFunction.OrderShipFunction(userid, Ship_orderid,Carrier,TrackingNo);
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
		}
	}
	return errorMessage;
}

@SuppressLint("NewApi") @SuppressWarnings("deprecation")
@Override
protected void onPostExecute(String result) {

	if (progressDialog.isShowing()) {
		progressDialog.dismiss();
	}
	if (result.equals("true")) {
		if (Build.VERSION.SDK_INT >= 16)
			llBtnbg1.setBackground(ItemToShip_Activity.this.getResources().getDrawable(R.drawable.item_ship_chat_ship2));
		else
			llBtnbg1.setBackgroundDrawable(ItemToShip_Activity.this.getResources().getDrawable(R.drawable.item_ship_chat_ship2));
		
		
		Custom_Dialog.dialogCode(2, null,msg,ItemToShip_Activity.this);
	//code for meetup

	} else {
		Custom_Dialog.dialogCode(2, null,msg,
				ItemToShip_Activity.this);
	}
}

@Override
protected void onProgressUpdate(Void... values) {

}
}
	
	
	
	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(ItemToShip_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}
	
	public class ItemTOShipCustomAdapter extends ArrayAdapter<ItemToShipModal> {
		ViewHolder holder;
		private ArrayList<ItemToShipModal> listSubCategories;
		protected Object mysun;
		ImageLoader iLoader_item = ImageLoader.getInstance();
		DisplayImageOptions options;
	
		
		public ItemTOShipCustomAdapter(Context context, int textViewResourceId,
				ArrayList<ItemToShipModal> listSubCategories) {
			super(context, textViewResourceId, listSubCategories);
			this.listSubCategories = new ArrayList<ItemToShipModal>();
			this.listSubCategories = listSubCategories;
		}

		public class ViewHolder {
			ImageView ivChat,ivShipped,ivItemImage;
			TextView tvName;
			EditText evAddress;
			LinearLayout llBtnbg;
		}

		@SuppressLint("NewApi") @SuppressWarnings("deprecation")
		@Override
		public View getView( final int position, View convertView,
				ViewGroup parent) {

			holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				convertView = vi.inflate(R.layout.item_to_ship_child, null);
					
				holder.ivChat = (ImageView) convertView.findViewById(R.id.ivChat);
				holder.ivShipped = (ImageView) convertView.findViewById(R.id.ivShipped);
				holder.ivItemImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
				holder.evAddress = (EditText) convertView.findViewById(R.id.evAddress);
				holder.llBtnbg = (LinearLayout) convertView.findViewById(R.id.llBtnbg);
				
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
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
			
			
			final ItemToShipModal m_ItemListing  = listSubCategories.get(position);
			String image=m_ItemListing.getImage();
			
			String is_shipped_Y= "N";
			is_shipped_Y=	m_ItemListing.getIs_shipped();
			if(is_shipped_Y.equals("N")){
				if (Build.VERSION.SDK_INT >= 16)
					holder.llBtnbg .setBackground(ItemToShip_Activity.this.getResources().getDrawable(R.drawable.item_ship_chat_ship1));
				else
					holder.llBtnbg .setBackgroundDrawable(ItemToShip_Activity.this.getResources().getDrawable(R.drawable.item_ship_chat_ship1));
				
			}else{
				if (Build.VERSION.SDK_INT >= 16)
					holder.llBtnbg .setBackground(ItemToShip_Activity.this.getResources().getDrawable(R.drawable.item_ship_chat_ship2));
				else
					holder.llBtnbg .setBackgroundDrawable(ItemToShip_Activity.this.getResources().getDrawable(R.drawable.item_ship_chat_ship2));
				
			}
			Log.e("image", " "+image);
			holder.tvName.setText(m_ItemListing.getName());
			holder.evAddress.setText(m_ItemListing.getAddress());
			holder.evAddress.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
				
					int sdk = android.os.Build.VERSION.SDK_INT;
					if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
					    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					    clipboard.setText(m_ItemListing.getAddress());
					} else {
					    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE); 
					    android.content.ClipData clip = android.content.ClipData.newPlainText("item to ship",m_ItemListing.getAddress());
					    clipboard.setPrimaryClip(clip);
					}
					Toast.makeText(context,"Copy to clipboard", Toast.LENGTH_LONG).show();
					return false;
				}
			});
			
			
		
			
		/*	
			if (image != null) {
				imageLoader.DisplayImage(image, holder.ivItemImage );
			} else {
				holder.ivItemImage .setImageResource(R.drawable.ic_launcher);
			}*/
			
			
			

			// Image display using lazy loading 

			iLoader_item.displayImage(image, holder.ivItemImage , options, new SimpleImageLoadingListener() {
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
			
			
			
			
			holder.ivShipped.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					buttonAnimation(v);
					
					ItemToShipModal m_item = listSubCategories.get(position);
					Ship_orderid=m_item.getOrderid();
					dialogCode(m_item,	holder.llBtnbg);
				}
			});
			
			holder.ivChat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					buttonAnimation(v);
					ItemToShipModal m_item = listSubCategories.get(position);
					
					mItem_IdList.clear();
					for (int i = 0; i < listSubCategories.size(); i++) {
						ItemToShipModal	_list = listSubCategories.get(i);
						
						mItem_IdList.add(_list.getItemid());
					}
					Intent i = new Intent(context,ChatActivity.class);
					i.putExtra("itemid", m_item.getItemid());
					i.putExtra("from_id", m_item.getUserid());
					i.putExtra("Distance", m_item.getDistance());
					i.putExtra("ItemArray", mItem_IdList);
					i.putExtra("position", ""+position);
					startActivity(i);
					
				}
			});
			
		
			return convertView;

		}

	}

	
	
	@SuppressWarnings("static-access")
	public void SpinnerTypeDialog(TextView tvCarrier)

	{
		dialogSpinner = new Dialog(context);
		dialogSpinner.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogSpinner.setCancelable(false);
		dialogSpinner.setContentView(R.layout.itemdetails_listview);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialogSpinner.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialogSpinner.getWindow().setAttributes(lp);
		dialogSpinner.show();
		dialogSpinner.setCanceledOnTouchOutside(true);
		lvt = (ListView) dialogSpinner.findViewById(R.id.lvDialog);
		adapter = new Adapter(context, mType);
		lvt.setAdapter(adapter);
		lvClickEvent(tvCarrier);
	}
	private void lvClickEvent(final TextView tvCarrier) {
		
		lvt.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
					
					String Country = mType.get(position);
					tvCarrier.setText(Country);
					dialogSpinner.dismiss();
			}
		});
	}
	private void MakeSpinnerArray() {
		mType.add("USPS");
		mType.add("FEDEX");
		mType.add("UPS");
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

	@SuppressWarnings("static-access")
	public void dialogCode(final ItemToShipModal m_item, final LinearLayout llBtnbg) {

		 dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_tracking_no);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = lp.MATCH_PARENT;
		lp.height = lp.MATCH_PARENT;
		dialog.getWindow().setAttributes(lp);
		ImageView ivComplete = (ImageView) dialog.findViewById(R.id.ivComplete);
		ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
		final TextView tvCarrier = (TextView) dialog.findViewById(R.id.tvCarrier);
		final TextView evTrackingNo = (TextView) dialog.findViewById(R.id.evTrackingNo);
		RelativeLayout	rlCarrier= (RelativeLayout) dialog.findViewById(R.id.rlCarrier);
		
		rlCarrier.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SpinnerTypeDialog(tvCarrier);
			}
		});
		ivComplete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			dialog.dismiss();
			
			TrackingNo= evTrackingNo.getText().toString().trim();
			Carrier = tvCarrier.getText().toString().trim();
			if(TrackingNo.length()==0){
				Custom_Dialog.dialogCode(2, null,"Please enter tracking no.",context);
			}else if(Carrier.length()==0){
				Custom_Dialog.dialogCode(2, null,"Please select carrier",context);	
			}else{
				if (cd.checkConnection()) {
					new ShipedOrderBackTask(m_item,llBtnbg).execute("");
				} else {
					Toast.makeText(context,"Interner connection is not available!",Toast.LENGTH_LONG).show();
				}
			}
			
			}
		});
		
		ivClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});


		dialog.show();
	}

}
