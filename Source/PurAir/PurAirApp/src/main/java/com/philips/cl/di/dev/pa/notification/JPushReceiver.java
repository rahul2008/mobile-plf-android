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
		
		JPushInterface.setDebugMode(true); 
		
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regKey = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] Receive Registration Id : " + regKey);
			preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("regKey", regKey);
			editor.apply();
			
			NotificationRegisteringManager.getNotificationManager().registerAppForNotification();
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.i(TAG, "[MyReceiver] Push down received a custom message: "
					+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] Push down the notifications received");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] Push down the received notification ID: "
					+ notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] Users click to open the notification : Commented");
			 JPushInterface.reportNotificationOpened(context, bundle.getString(JPushInterface.EXTRA_MSG_ID));
			 // Open the Custom Activity
			Intent intentNew = new Intent(context, MainActivity.class);
			intentNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intentNew.setAction(Intent.ACTION_MAIN);
			intentNew.addCategory(Intent.CATEGORY_LAUNCHER);
			intentNew.putExtras(bundle);
			context.startActivity(intentNew);

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
}