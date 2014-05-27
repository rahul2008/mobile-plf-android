package com.philips.cl.di.dev.pa.notification;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.ICPCallbackHandler;
import com.philips.cl.di.dev.pa.cpp.ICPEventListener;
import com.philips.cl.di.dev.pa.cpp.SignonListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.ThirdPartyNotification;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;

public class NotificationRegisteringManager implements ICPEventListener, SignonListener {
	
	private ICPCallbackHandler callbackHandler;
	private GoogleCloudMessaging gcm;
	private String regid;

	public NotificationRegisteringManager() {
		callbackHandler = new ICPCallbackHandler();
		callbackHandler.setHandler(this);
		CPPController.getInstance(PurAirApplication.getAppContext()).addSignOnListener(this);
		
		gcm = GoogleCloudMessaging.getInstance(PurAirApplication.getAppContext());
		regid = getRegistrationId(PurAirApplication.getAppContext());
	}

	public void registerAppForNotification() {
		if (!Utils.isGooglePlayServiceAvailable()) {
			ALog.e(ALog.NOTIFICATION, "Google play services not supported on this device");
			return;
		}

		if (isRegisteredForGCM()) {
			ALog.i(ALog.NOTIFICATION, "App already registered for Google Play push notifications");
		} else {
			ALog.i(ALog.NOTIFICATION, "App not yet registered for Google Play push notifications - registering now");
			registerForGCMInBackground();
			return;
		}

		if (isRegistrationKeySendToCpp()) {
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
		SharedPreferences.Editor editor = getGCMPreferences(PurAirApplication.getAppContext()).edit();
		editor.putBoolean(AppConstants.PROPERTY_IS_REGISTRATIONKEY_SEND_TO_CPP, false).commit();

		if (!CPPController.getInstance(PurAirApplication.getAppContext()).isSignOn()) {
			ALog.e(ALog.NOTIFICATION, "Failed to send registration ID to CPP - not signed on");
			return;
		}
		
		ThirdPartyNotification thirdParty = new ThirdPartyNotification(callbackHandler,AppConstants.NOTIFICATION_SERVICE_TAG);
		thirdParty.setProtocolDetails(AppConstants.NOTIFICATION_PROTOCOL, AppConstants.NOTIFICATION_PROVIDER, regid);
		int retStatus =  thirdParty.executeCommand();
		if (Errors.SUCCESS != retStatus)	{
			ALog.e(ALog.NOTIFICATION, "Failed to send registration ID to CPP - immediate");
		}
	}

	private SharedPreferences getGCMPreferences(Context context) {
		//persists the registration ID in shared preferences
		return PurAirApplication.getAppContext().getSharedPreferences(AppConstants.NOTIFICATION_PREFERENCE_FILE_NAME,
				Context.MODE_PRIVATE);
	}
	
	private String getRegistrationId(Context ctx) {
		final SharedPreferences prefs = getGCMPreferences(ctx);
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
		final SharedPreferences prefs = getGCMPreferences(ctx);
		int appVersion = PurAirApplication.getAppVersion();
		
		ALog.i(ALog.NOTIFICATION, "Storing GCM registration ID for app version: " + appVersion);
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(AppConstants.PROPERTY_REG_ID, registrationId);
		editor.putInt(AppConstants.PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private boolean isRegisteredForGCM() {
		final SharedPreferences prefs = getGCMPreferences(PurAirApplication.getAppContext());
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
	
	private boolean isRegistrationKeySendToCpp() {
		final SharedPreferences prefs = getGCMPreferences(PurAirApplication.getAppContext());
		boolean isRegistrationKeySendToCpp = prefs.getBoolean(AppConstants.PROPERTY_IS_REGISTRATIONKEY_SEND_TO_CPP, false);
		
		if (isRegistrationKeySendToCpp) {
			ALog.d(ALog.NOTIFICATION, "Registration key already sent to CPP");
		} else {
			ALog.d(ALog.NOTIFICATION, "Registration key not yet sent to CPP");
		}
		return isRegistrationKeySendToCpp;
	}

	@Override
	public void onICPCallbackEventOccurred(int eventType, int status,
			ICPClient obj) {
		if (eventType== Commands.THIRDPARTY_REGISTER_PROTOCOLADDRS) {
			
			ALog.d(ALog.NOTIFICATION, "Received registration callback from CPP");

			final SharedPreferences prefs = getGCMPreferences(PurAirApplication.getAppContext());
			SharedPreferences.Editor editor = prefs.edit();
			
			if (status==Errors.SUCCESS) {	
				ALog.i(ALog.NOTIFICATION, "Registration ID successfully sent to CPP");
				editor.putBoolean(AppConstants.PROPERTY_IS_REGISTRATIONKEY_SEND_TO_CPP, true);		    
			} else {
				ALog.i(ALog.NOTIFICATION, "Failed to send Registration ID to CPP - callback");
				editor.putBoolean(AppConstants.PROPERTY_IS_REGISTRATIONKEY_SEND_TO_CPP, false);
			}
			editor.commit();
		}
	}

	@Override
	public void signonStatus(boolean signon) {
		if (!signon) return;
		if (isRegistrationKeySendToCpp()) return;
		
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
}
