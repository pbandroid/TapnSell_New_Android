package com.convertingoffers.tapnsell.push;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

@SuppressLint("ParserError")
public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService(){
		super(PushUtils.GCMSenderId);
	}

	@Override
	protected void onError(Context context, String regId) {
		Log.e("", "error registration id : "+regId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		handleMessage(context, intent);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		handleRegistration(context, regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.i(TAG, "Device unregistered");
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			GCMRegistrar.unregister(context);
		} else {
			Log.i(TAG, "Ignoring unregister callback");
		}
	}
	
	@SuppressWarnings("unused")
	private void handleMessage(Context context, Intent intent) {
		try{
			String strType = "";
			String strTitle = "";
			String strText = "";
			
			//Log.e("recieve", intent.toString());
			if (intent.hasExtra("type")) {
				strType = intent.getStringExtra("type");
				//Log.i("type", strType);
			}
			
			if (intent.hasExtra("title")) {
				strTitle = intent.getStringExtra("title");
				//Log.i("title", strTitle);
			}
			
			if (intent.hasExtra("text")) {
				strText = intent.getStringExtra("text");
				Log.i("text", strText);
			}
			
			/*NotificationManager m = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
			Notification ntf = new Notification(R.drawable.ic_launcher, "A New Message!", 0);
			ntf.when = System.currentTimeMillis();
			
			RemoteViews rViews = new RemoteViews(context.getPackageName(), R.layout.notification);
			
			rViews.setTextViewText(R.id.name_textView, strPushMessage);
			ntf.contentView = rViews;
			
			Intent notifIntent = null;
			if(strType.equalsIgnoreCase("N")){
				//notifIntent = new Intent(context,FAQOrNewsActivity.class);
				notifIntent.putExtra("isFAQorNews", false);				
			} else if (strType.equalsIgnoreCase("F")) {
				//notifIntent.putExtra("isCreator",false);
			} else if (strType.equalsIgnoreCase("G")) {
				//notifIntent.putExtra("isCreator",false);
			}
		
			notifIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			PendingIntent pIntent = PendingIntent.getActivity(context, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
			Log.e("", "");
			//ntf.setLatestEventInfo(context, "TriviaQuiz", strPushMessage, pIntent);
       	
			ntf.flags |= Notification.FLAG_AUTO_CANCEL;*/
			/*if(led.equalsIgnoreCase("true"))
				ntf.defaults |= Notification.DEFAULT_LIGHTS;
			if(vibrate.equalsIgnoreCase("vibrate"))
				ntf.defaults |= Notification.DEFAULT_VIBRATE;
			if(sound.equalsIgnoreCase("sound"))
				ntf.defaults |= Notification.DEFAULT_SOUND;*/
		
			//m.notify(1, ntf);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/*@SuppressWarnings({ "deprecation", "static-access" })
	private void handleMessage(Context context, Intent intent) {
		//   Auto-generated method stub
		PushUtils.notiMsg = intent.getStringExtra("msg");
		PushUtils.notiTitle = intent.getStringExtra("title");
		PushUtils.notiType = intent.getStringExtra("type");
		PushUtils.notiUrl = intent.getStringExtra("url");

		int icon = R.drawable.ic_launcher; // icon from resources
		CharSequence tickerText = PushUtils.notiTitle;//intent.getStringExtra("me"); // ticker-text

		long when = System.currentTimeMillis(); // notification time
		CharSequence contentTitle = ""+PushUtils.notiMsg; //intent.getStringExtra("me"); // message title

		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, tickerText, when);
		Intent notificationIntent = new Intent(context,Homepage.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, "", pendingIntent);
		notification.flags|=notification.FLAG_INSISTENT|notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS;
		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(1, notification);
		PushUtils.notificationReceived=true;
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
		wl.acquire();

	}*/

	private void handleRegistration(Context context, String regId) {
		//   Auto-generated method stub
		PushUtils.registrationId = regId;
		SharedPreferences prefs = context.getSharedPreferences("SayWhat", Context.MODE_PRIVATE);
		prefs.edit().putString("registrationID", regId).commit();
	}
}


