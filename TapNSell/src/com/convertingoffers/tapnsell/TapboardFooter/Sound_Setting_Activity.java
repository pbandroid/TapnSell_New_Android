package com.convertingoffers.tapnsell.TapboardFooter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.TapBoardActivity;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.Custom_Dialog;

public class Sound_Setting_Activity extends BaseActivity implements
		OnClickListener {

	Animation RightSwipe;
	Context context;
	ToggleButton tb_newOffer, tb_Message, tb_MeetUpRequest,
			tb_CaseNotification, tb_ItemSold, tb_Tracking_no;
	String status = "", index = "", userid;
	boolean boolStatus_tb = false, bool_newOffer, bool_Message,
			bool_MeetUpRequest, bool_CaseNotification, bool_ItemSold,
			bool_Tracking_no;
	String str_newOffer = "N", str_Message = "N", str_MeetUpRequest = "N",
			str_CaseNotification = "N", str_ItemSold = "N",
			str_Tracking_no = "N";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sound_setting);

		userid = pref.getString("UserID", "");
		tvHeader.setText("Sound Settings");
		ivBack.setOnClickListener(this);
		ivMenu.setVisibility(View.VISIBLE);
		ivMenu.setOnClickListener(this);

		if (cd.checkConnection()) {
			new SoundSettingBackTask().execute("");
		} else {
			Toast.makeText(context, "Interner connection is not available!",
					Toast.LENGTH_LONG).show();
		}

		Log.e("boolStatus_tb", " " + boolStatus_tb);

		tb_newOffer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				index = "1";
				buttonAnimation(tb_newOffer);
				if (cd.checkConnection()) {
					if (isChecked) {
						status = "Y";
					} else {
						status = "N";
					}
					if (!boolStatus_tb) {
						if (!progressDialog.isShowing()) {

							new SoundSettingBackTask().execute("");

						}
					}
				} else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		tb_Message.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				index = "2";
				buttonAnimation(tb_Message);
				if (cd.checkConnection()) {
					if (isChecked) {
						status = "Y";
					} else {
						status = "N";
					}
					if (!boolStatus_tb) {

						if (!progressDialog.isShowing()) {
							new SoundSettingBackTask().execute("");
						}
					}
				} else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		tb_MeetUpRequest
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						index = "3";
						buttonAnimation(tb_MeetUpRequest);
						if (cd.checkConnection()) {
							if (isChecked) {
								status = "Y";
							} else {
								status = "N";
							}
							if (!boolStatus_tb) {
								if (!progressDialog.isShowing()) {
									new SoundSettingBackTask().execute("");

								}
							}
						} else {
							Toast.makeText(context,
									"Interner connection is not available!",
									Toast.LENGTH_LONG).show();
						}
					}
				});
		tb_CaseNotification
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						index = "4";
						buttonAnimation(tb_CaseNotification);
						if (cd.checkConnection()) {
							if (isChecked) {
								status = "Y";
							} else {
								status = "N";
							}
							if (!boolStatus_tb) {
								if (!progressDialog.isShowing()) {

									new SoundSettingBackTask().execute("");

								}
							}
						} else {
							Toast.makeText(context,
									"Interner connection is not available!",
									Toast.LENGTH_LONG).show();
						}
					}

				});
		tb_ItemSold.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				buttonAnimation(tb_ItemSold);

				if (cd.checkConnection()) {

					index = "5";

					if (isChecked) {
						status = "Y";
					} else {
						status = "N";
					}
					if (!boolStatus_tb) {
						if (!progressDialog.isShowing()) {

							new SoundSettingBackTask().execute("");

						}
					}
				} else {
					Toast.makeText(context,
							"Interner connection is not available!",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		tb_Tracking_no
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (cd.checkConnection()) {
							index = "6";
							buttonAnimation(tb_Tracking_no);
							if (isChecked) {
								status = "Y";
							} else {
								status = "N";
							}
							if (!boolStatus_tb) {
								if (!progressDialog.isShowing()) {

									new SoundSettingBackTask().execute("");

								}
							}
						} else {
							Toast.makeText(context,
									"Interner connection is not available!",
									Toast.LENGTH_LONG).show();
						}
					}
				});

	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		context = this;

		tb_newOffer = (ToggleButton) findViewById(R.id.tb_newOffer);
		tb_Message = (ToggleButton) findViewById(R.id.tb_Message);
		tb_MeetUpRequest = (ToggleButton) findViewById(R.id.tb_MeetUpRequest);
		tb_CaseNotification = (ToggleButton) findViewById(R.id.tb_CaseNotification);
		tb_ItemSold = (ToggleButton) findViewById(R.id.tb_ItemSold);
		tb_Tracking_no = (ToggleButton) findViewById(R.id.tb_Tracking_no);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// ,,,rlViewTerms,rlfeature
		case R.id.ivMenu:
			buttonAnimation(ivMenu);
			Intent intent = new Intent(context, TapBoardActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.ivBack:
			finish();
			break;
		default:
			break;
		}
	}

	public void buttonAnimation(View v) {

		RightSwipe = AnimationUtils.loadAnimation(Sound_Setting_Activity.this,
				R.anim.slide_left);
		v.startAnimation(RightSwipe);
	}

	private class SoundSettingBackTask extends AsyncTask<String, Void, String> {
		String errorMessage, message;

		protected void onPreExecute() {
			boolStatus_tb = true;
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			{

				Log.e("userid", " " + userid);
				Log.e("index", " " + index);
				Log.e("status", " " + status);

				JSONObject json = userFunction.SoundSettingFunction(userid,
						index, status);
				try {
					if (json!=null&&json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);
						if (res.equals(KEY_SUCCESS_STATUS)) {

							str_newOffer = json.optString("newoffer");
							str_Message = json.optString("message");
							str_MeetUpRequest = json.optString("meetups");
							str_CaseNotification = json.optString("case");
							str_ItemSold = json.optString("sold");
							str_Tracking_no = json.optString("track");

							errorMessage = "true";
						} else {
							message = json.optString("message");
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

				if (str_newOffer.equals("Y")) {
					tb_newOffer.setChecked(true);
				} else {
					tb_newOffer.setChecked(false);
				}
				if (str_Message.equals("Y")) {
					tb_Message.setChecked(true);
				} else {
					tb_Message.setChecked(false);
				}
				if (str_MeetUpRequest.equals("Y")) {
					tb_MeetUpRequest.setChecked(true);
				} else {
					tb_MeetUpRequest.setChecked(false);
				}
				if (str_CaseNotification.equals("Y")) {
					tb_CaseNotification.setChecked(true);
				} else {
					tb_CaseNotification.setChecked(false);
				}
				if (str_ItemSold.equals("Y")) {
					tb_ItemSold.setChecked(true);
				} else {
					tb_ItemSold.setChecked(false);
				}
				if (str_Tracking_no.equals("Y")) {
					tb_Tracking_no.setChecked(true);
				} else {
					tb_Tracking_no.setChecked(false);
				}
				boolStatus_tb = false;
			}else if (result.equals("network")) {
				message="Your internet connection is too slow";
				Custom_Dialog.dialogCode(2, null,message, context);
//				ValidationSingleButton("Error in posting");
			} else {
				Custom_Dialog.dialogCode(2, null, message, context);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}
	}

}
