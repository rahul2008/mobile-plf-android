package com.philips.cl.di.dev.pa.notification;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.ICPCallbackHandler;
import com.philips.cl.di.dev.pa.cpp.ICPEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.ThirdPartyNotification;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;

public class NotificationRegisteringManager implements ICPEventListener{
	private final String PREFERENCE_FILE_NAME = "GCMRegistrion";
	private static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String SENDER_ID = "654182650566";
	private static final String IS_REGISTERED_FOR_NOTIFICATION="is_registered_notification";
	private ICPCallbackHandler callbackHandler;
	private GoogleCloudMessaging gcm;
	private String regid;

	public NotificationRegisteringManager() {
		callbackHandler = new ICPCallbackHandler();
		callbackHandler.setHandler(this);
	}

	public void registerAppForNotification(){
		if(!Utils.isGooglePlayServiceAvailable()) return;

		gcm = GoogleCloudMessaging.getInstance(PurAirApplication.getAppContext());
		regid = getRegistrationId(PurAirApplication.getAppContext());

		if (regid.isEmpty()) {
			registerInBackground();
		}
	}

	private String getRegistrationId(Context ctx)
	{
		final SharedPreferences prefs = getGCMPreferences(ctx);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");

		if (registrationId.isEmpty()) {
			ALog.i(ALog.NOTIFICATION, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(ctx);
		if (registeredVersion != currentVersion) {
			ALog.i(ALog.NOTIFICATION, "App version changed.");
			return "";
		}
		return registrationId;

	}

	private SharedPreferences getGCMPreferences(Context context) {
		//persists the registration ID in shared preferences
		return PurAirApplication.getAppContext().getSharedPreferences(PREFERENCE_FILE_NAME,
				Context.MODE_PRIVATE);
	}

	private int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(PurAirApplication.getAppContext());
					}
					regid = gcm.register(SENDER_ID);
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

	private void storeRegistrationId(PurAirApplication appContext,
			String regid2) {
		final SharedPreferences prefs = getGCMPreferences(appContext);
		int appVersion = getAppVersion(appContext);
		ALog.i(ALog.NOTIFICATION, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regid);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private void sendRegistrationIdToBackend(String regid) {

		ThirdPartyNotification thirdParty = new ThirdPartyNotification(callbackHandler,AppConstants.SERVICE_TAG);
		thirdParty.setProtocolDetails(AppConstants.PROTOCOL, AppConstants.PROVIDER, regid);
		int retStatus =  thirdParty.executeCommand();
		if(Errors.SUCCESS != retStatus)
		{

		}
	}

	@Override
	public void onICPCallbackEventOccurred(int eventType, int status,
			ICPClient obj) {
		if(eventType== Commands.THIRDPARTY_REGISTER_PROTOCOLADDRS)
		{
			ALog.i(ALog.NOTIFICATION, "Notification registration. Status: " + status);
			final SharedPreferences prefs = getGCMPreferences(PurAirApplication.getAppContext());
			ALog.i(ALog.NOTIFICATION, "Saving registration status");
			SharedPreferences.Editor editor = prefs.edit();
			if(status==Errors.SUCCESS){			
				editor.putBoolean(IS_REGISTERED_FOR_NOTIFICATION, true);		    
			}
			else{
				editor.putBoolean(IS_REGISTERED_FOR_NOTIFICATION, false);
			}
			editor.commit();
		}
	}
	
	public boolean getNotificationRegistrationStatus(){
		final SharedPreferences prefs = getGCMPreferences(PurAirApplication.getAppContext());
		return prefs.getBoolean(IS_REGISTERED_FOR_NOTIFICATION, false);
	}
}
