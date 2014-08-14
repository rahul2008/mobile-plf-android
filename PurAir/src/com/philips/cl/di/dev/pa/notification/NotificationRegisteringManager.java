package com.philips.cl.di.dev.pa.notification;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import cn.jpush.android.api.JPushInterface;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.SignonListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;

public class NotificationRegisteringManager implements SignonListener, SendNotificationRegistrationIdListener {
	
	private GoogleCloudMessaging gcm;
	private String regid;

	public NotificationRegisteringManager() {
		CPPController.getInstance(PurAirApplication.getAppContext()).addSignOnListener(this);
		CPPController.getInstance(PurAirApplication.getAppContext()).setNotificationListener(this);
		
		if (!Utils.isGooglePlayServiceAvailable()) {
			JPushInterface.setDebugMode(false); 
			JPushInterface.init(PurAirApplication.getAppContext());  
		}
		else{
			gcm = GoogleCloudMessaging.getInstance(PurAirApplication.getAppContext());
			regid = getRegistrationId();
		}
	}

	public void registerAppForNotification() {
		if (!Utils.isGooglePlayServiceAvailable()) {
			ALog.e(ALog.NOTIFICATION, "Google play services not supported on this device");
			
			regid = JPushReceiver.getRegKey();
			
			if(regid == null || regid.isEmpty()) return;
			
			registerForGCMInBackground();
		}
		else{
			if (isRegisteredForGCM()) {
				ALog.i(ALog.NOTIFICATION, "App already registered for Google Play push notifications");
			} else {
				ALog.i(ALog.NOTIFICATION, "App not yet registered for Google Play push notifications - registering now");
				registerForGCMInBackground();
				return;
			}
		}

		if (getIsRegistrationKeySendToCpp()) {
			ALog.i(ALog.NOTIFICATION, "GCM Registration ID already sent to CPP");
		} else {
			ALog.i(ALog.NOTIFICATION, "GCM Registration ID not yet sent to CPP - Sending ID to CPP now");
			sendRegistrationIdToBackend(regid);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerForGCMInBackground() {
		if (Utils.isGooglePlayServiceAvailable()) {
			registerForGoogleService();
		}
		else{
			registerForJPushService();
		}
	}
	
	public void registerForJPushService() {
		// send the registration ID to CPP server.
		sendRegistrationIdToBackend(regid);
		// Persist the regID - no need to register again.
		storeRegistrationId(PurAirApplication.getAppContext(), regid);
	}

	private void registerForGoogleService(){
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(PurAirApplication.getAppContext());
					}
					regid = gcm.register(AppConstants.NOTIFICATION_SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// send the registration ID to CPP server.
					sendRegistrationIdToBackend(regid);

					// Persist the regID - no need to register again.
					storeRegistrationId(PurAirApplication.getAppContext(), regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				ALog.i(ALog.NOTIFICATION, msg);
			}
		}.execute(null, null, null);
	}
	
	private void sendRegistrationIdToBackend(String regid) {
		if(!CPPController.getInstance(PurAirApplication.getAppContext()).isSignOn())return;
		storeRegistrationKeySendToCPP(false);
		if(regid == null || regid.isEmpty()) return;
		CPPController.getInstance(PurAirApplication.getAppContext()).sendNotificationRegistrationId(regid);
	}
	
	private boolean isRegisteredForGCM() {
		final SharedPreferences prefs = getGCMPreferences();
		boolean isRegisteredToGCM = !(regid == null || regid.isEmpty());
		
		int registeredVersion = prefs.getInt(AppConstants.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = PurAirApplication.getAppVersion();
		boolean isGCMRegistrationExpired = (registeredVersion != currentVersion);
		
		if (!isRegisteredToGCM) {
			ALog.d(ALog.NOTIFICATION, "Not yet registered - No Registration ID");
			return false;
		}
		
		if (isGCMRegistrationExpired) {
			ALog.d(ALog.NOTIFICATION, "Registration ID expired - App version changed");
			return false;
		}

		ALog.d(ALog.NOTIFICATION, "App already registered for GCM");
		return true;
	}

	private SharedPreferences getGCMPreferences() {
		return PurAirApplication.getAppContext().getSharedPreferences(AppConstants.NOTIFICATION_PREFERENCE_FILE_NAME,
				Context.MODE_PRIVATE);
	}
	
	private String getRegistrationId() {
		final SharedPreferences prefs = getGCMPreferences();
		String registrationId = prefs.getString(AppConstants.PROPERTY_REG_ID, "");

		if (registrationId.isEmpty()) {
			ALog.i(ALog.NOTIFICATION, "Invalid registration ID - no GCM Registration ID found.");
			return "";
		}
		
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(AppConstants.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = PurAirApplication.getAppVersion();
		if (registeredVersion != currentVersion) {
			ALog.i(ALog.NOTIFICATION, "Invalid registration ID - App version changed");
			return "";
		}
		
		ALog.i(ALog.NOTIFICATION, "Registration ID: " + registrationId);
		return registrationId;
	}
	
	private void storeRegistrationId(Context ctx, String registrationId) {
		final SharedPreferences prefs = getGCMPreferences();
		int appVersion = PurAirApplication.getAppVersion();
		
		ALog.i(ALog.NOTIFICATION, "Storing GCM registration ID for app version: " + appVersion);
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(AppConstants.PROPERTY_REG_ID, registrationId);
		editor.putInt(AppConstants.PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}
	
	private boolean getIsRegistrationKeySendToCpp() {
		final SharedPreferences prefs = getGCMPreferences();
		boolean isRegistrationKeySendToCpp = prefs.getBoolean(AppConstants.PROPERTY_IS_REGISTRATIONKEY_SEND_TO_CPP, false);
		
		if (isRegistrationKeySendToCpp) {
			ALog.d(ALog.NOTIFICATION, "Registration key already sent to CPP");
		} else {
			ALog.d(ALog.NOTIFICATION, "Registration key not yet sent to CPP");
		}
		return isRegistrationKeySendToCpp;
	}
	
	private void storeRegistrationKeySendToCPP(boolean registrationKeySent) {
		final SharedPreferences prefs = getGCMPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(AppConstants.PROPERTY_IS_REGISTRATIONKEY_SEND_TO_CPP, registrationKeySent);
		editor.commit();
	}

	
	@Override
	public void signonStatus(boolean signon) {
		if (!signon) return;
		if (getIsRegistrationKeySendToCpp()) return;
		
		ALog.i(ALog.NOTIFICATION, "ICPCLient signed on - sending GCM Registration ID to cpp: " + regid);

		// Dirty: need to send registration ID after 3sec so ICPClient can properly startup
		// otherwise it will fail.
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				sendRegistrationIdToBackend(regid);
			}
		}, 3000);
	}

	@Override
	public void onRegistrationIdSentSuccess() {
		ALog.i(ALog.NOTIFICATION, "Registration ID successfully sent to CPP");
		storeRegistrationKeySendToCPP(true);
	}

	@Override
	public void onRegistrationIdSentFailed() {
		ALog.i(ALog.NOTIFICATION, "Failed to send Registration ID to CPP - callback");
		storeRegistrationKeySendToCPP(false);
	}

}
