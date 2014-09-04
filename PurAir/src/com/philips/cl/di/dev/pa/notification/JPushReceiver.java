package com.philips.cl.di.dev.pa.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.activity.MainActivity;

/*
 * This class will receives registration ID from JPush server.
 * Author : Ritesh.jha@philips.com
 * Date : 8 Aug 2014
 */

public class JPushReceiver extends BroadcastReceiver {
	public static final String KEY_APP_KEY = "JPUSH_APPKEY";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public static final String TAG = "JPushReceiver";

	private static SharedPreferences preferences = null;

	public static String getRegKey() {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(PurAirApplication.getAppContext());
		return preferences.getString("regKey", "");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));
		
		JPushInterface.setDebugMode(false); 
		
//		if (Utils.isGooglePlayServiceAvailable()) {
//			return;  
//		}

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			if(!MainActivity.registrationNeededNow()){
				return;
			}
			String regKey = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] Receive Registration Id : " + regKey);
			preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("regKey", regKey);
			editor.apply();
			
//			PurAirApplication.getAppContext()
//					.getNotificationRegisteringManager()
//					.registerAppForNotification();
			
			MainActivity.setRegistrationNeededNow(false);
			NotificationRegisteringManager.getNotificationManager().registerAppForNotification();
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.i(TAG, "[MyReceiver] Push down received a custom message: "
					+ bundle.getString(JPushInterface.EXTRA_MESSAGE));

//			if (JPushActivity.isForeground) {
//				String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//				String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//				Intent msgIntent = new Intent(
//						JPushInterface.ACTION_MESSAGE_RECEIVED);
//				msgIntent.putExtra(JPushActivity.KEY_MESSAGE, message);
//				if (!isEmpty(extras)) {
//					try {
//						JSONObject extraJson = new JSONObject(extras);
//						if (null != extraJson && extraJson.length() > 0) {
//							msgIntent
//									.putExtra(JPushActivity.KEY_EXTRAS, extras);
//						}
//					} catch (JSONException e) {
//					}
//				}
//				context.sendBroadcast(msgIntent);
//			}
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] Push down the notifications received");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] Push down the received notification ID: "
					+ notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] Users click to open the notification : Commented");
			// JPushInterface.reportNotificationOpened(context,
			// bundle.getString(JPushInterface.EXTRA_MSG_ID));
			//
			// // Open the Custom Activity
			// Intent i = new Intent(context, TestActivity.class);
			// i.putExtras(bundle);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(i);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] The user receives the RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// according to JPushInterface.EXTRA_EXTRA content processing code,
			// such as opening a new Activity, open a web page, etc. ..

		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// Print all intent extra data
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	private static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}
}