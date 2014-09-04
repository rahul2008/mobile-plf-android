package com.philips.cl.di.dev.pa.notification;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import cn.jpush.android.api.JPushInterface;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.SignonListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;

public class NotificationRegisteringManager implements SignonListener,
		SendNotificationRegistrationIdListener {

	private GoogleCloudMessaging gcm;
	private static String regid;
	private static NotificationRegisteringManager mNotificationManager;
	private static boolean mRegistrationDone = false;
	private static int mRegTryCount = 0;
	private LooperThread mLooperThread = null;;
//	private Handler mHandler= null;
	private static String mProvider = AppConstants.PROPERTY_NOTIFICATION_PROVIDER;

	public NotificationRegisteringManager() {
		CPPController.getInstance(PurAirApplication.getAppContext())
				.addSignOnListener(this);
		CPPController.getInstance(PurAirApplication.getAppContext())
				.setNotificationListener(this);

		if (!Utils.isGooglePlayServiceAvailable() || getRegitrationProvider().equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH)) {
			ALog.i(ALog.NOTIFICATION,"NO GOOGLE SERVICE");
			if(gcm!=null){
				try {
					gcm.unregister();
				} catch (IOException e) {
					e.printStackTrace();
				}
				gcm = null;
			}
//			JPushInterface.setDebugMode(false);
			JPushInterface.init(PurAirApplication.getAppContext());
			JPushInterface.resumePush(PurAirApplication.getAppContext());
		} else {
			ALog.i(ALog.NOTIFICATION,"GOOGLE SERVICE");
			setRegistrationProvider(AppConstants.NOTIFICATION_PROVIDER_GOOGLE);
			gcm = GoogleCloudMessaging.getInstance(PurAirApplication
					.getAppContext());
			regid = getRegistrationId();
			ALog.i(ALog.NOTIFICATION, "NotificationRegisteringManager : regId " + regid);
		}
	}
	
	private void startHandlerThread(){
		mLooperThread = null;
		mLooperThread = new LooperThread();
		mLooperThread.start();
	}
	
	public static NotificationRegisteringManager getNotificationManager(){
		if(mNotificationManager == null){
			mNotificationManager = new NotificationRegisteringManager();
		}
		return mNotificationManager;	
	}
	
	public static void setNotificationManager(){
		mNotificationManager = null;
		mRegistrationDone = false;
	}
	
	public void registerAppForNotification() {
		if (!Utils.isGooglePlayServiceAvailable() || getRegitrationProvider().equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH)) {
			ALog.e(ALog.NOTIFICATION,
					"Google play services not supported on this device");
			regid = JPushReceiver.getRegKey();
			if (regid == null || regid.isEmpty())
				return;
			
			if (isRegisteredForGCM()) {
				ALog.i(ALog.NOTIFICATION,
						"App already registered for JPUSH notifications");
			} else {
				ALog.i(ALog.NOTIFICATION,
						"App not yet registered for JPUSH notifications - registering now");
				registerForGCMInBackground();
				//return;
			}
		} else {
			if (isRegisteredForGCM()) {
				ALog.i(ALog.NOTIFICATION,
						"App already registered for Google Play push notifications");
			} else {
				ALog.i(ALog.NOTIFICATION,
						"App not yet registered for Google Play push notifications - registering now");
				registerForGCMInBackground();
		//		return;
			}
		}

//		if (getIsRegistrationKeySendToCpp()) {
//			ALog.i(ALog.NOTIFICATION, "GCM Registration ID already sent to CPP");
//		} else {
//			ALog.i(ALog.NOTIFICATION,
//					"GCM Registration ID not yet sent to CPP - Sending ID to CPP now");
//			sendRegistrationIdToBackend(regid);
//		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerForGCMInBackground() {
		if (!Utils.isGooglePlayServiceAvailable() || getRegitrationProvider().equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH)) {
			registerForJPushService();
		} else {
			registerForGoogleService();
		}
	}

	public void registerForJPushService() {
		// send the registration ID to CPP server.
		sendRegistrationIdToBackend(regid);
		// Persist the regID - no need to register again.
		if(regid != null && !regid.isEmpty()){
			storeRegistrationId(PurAirApplication.getAppContext(), regid);
		}
	}

	private void registerForGoogleService() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(PurAirApplication.getAppContext());
					}
					regid = gcm.register(AppConstants.NOTIFICATION_SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// send the registration ID to CPP server.
					sendRegistrationIdToBackend(regid);

					// Persist the regID - no need to register again.
					if(regid != null && !regid.isEmpty()){
						storeRegistrationId(PurAirApplication.getAppContext(),
								regid);
					}
					ALog.i(ALog.NOTIFICATION, "registerForGoogleService  regid" + regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					creatingJpushNotificationManager();	
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				ALog.i(ALog.NOTIFICATION, msg);
			}
		}.execute(null, null, null);
	}

	private static void sendRegistrationIdToBackend(String regid) {
		if (!CPPController.getInstance(PurAirApplication.getAppContext())
				.isSignOn())
			return;
		storeRegistrationKeySendToCPP(false);
		if (regid == null || regid.isEmpty())
			return;
		CPPController.getInstance(PurAirApplication.getAppContext())
				.sendNotificationRegistrationId(regid);
	}

	private boolean isRegisteredForGCM() {
		final SharedPreferences prefs = getGCMPreferences();
		boolean isRegisteredToGCM = !(regid == null || regid.isEmpty());

		int registeredVersion = prefs.getInt(AppConstants.PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = PurAirApplication.getAppVersion();
		boolean isGCMRegistrationExpired = (registeredVersion != currentVersion);

		if (!isRegisteredToGCM) {
			ALog.d(ALog.NOTIFICATION, "Not yet registered - No Registration ID");
			return false;
		}

		if (isGCMRegistrationExpired) {
			ALog.d(ALog.NOTIFICATION,
					"Registration ID expired - App version changed");
			return false;
		}
		
		if(!getIsRegistrationKeySendToCpp()){
			ALog.i(ALog.NOTIFICATION, "!getIsRegistrationKeySendToCpp()");
			return false;
		}

		ALog.d(ALog.NOTIFICATION, "App already registered for GCM");
		return true;
	}

	private static SharedPreferences getGCMPreferences() {
		return PurAirApplication.getAppContext().getSharedPreferences(
				AppConstants.NOTIFICATION_PREFERENCE_FILE_NAME,
				Context.MODE_PRIVATE);
	}

	private String getRegistrationId() {
		final SharedPreferences prefs = getGCMPreferences();
		String registrationId = prefs.getString(AppConstants.PROPERTY_REG_ID,
				"");

		if (registrationId.isEmpty()) {
			ALog.i(ALog.NOTIFICATION,
					"Invalid registration ID - no GCM Registration ID found.");
			return "";
		}

		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(AppConstants.PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = PurAirApplication.getAppVersion();
		if (registeredVersion != currentVersion) {
			ALog.i(ALog.NOTIFICATION,
					"Invalid registration ID - App version changed");
			return "";
		}

		ALog.i(ALog.NOTIFICATION, "Registration ID: " + registrationId);
		return registrationId;
	}

	public static void storeRegistrationId(Context ctx, String registrationId) {
		final SharedPreferences prefs = getGCMPreferences();
		int appVersion = PurAirApplication.getAppVersion();

		ALog.i(ALog.NOTIFICATION,
				"Storing GCM registration ID for app ");

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(AppConstants.PROPERTY_REG_ID, registrationId);
//		editor.putInt(AppConstants.PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}
	
	public void storeVersion(Context ctx, Integer version) {
		final SharedPreferences prefs = getGCMPreferences();

		ALog.i(ALog.NOTIFICATION,
				"Storing version ID for app : " + version);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(AppConstants.PROPERTY_APP_VERSION, version);
		editor.commit();
	}

	private boolean getIsRegistrationKeySendToCpp() {
		final SharedPreferences prefs = getGCMPreferences();
		boolean isRegistrationKeySendToCpp = prefs.getBoolean(
				AppConstants.PROPERTY_IS_REGISTRATIONKEY_SEND_TO_CPP, false);

		if (isRegistrationKeySendToCpp) {
			ALog.d(ALog.NOTIFICATION, "Registration key already sent to CPP");
		} else {
			ALog.d(ALog.NOTIFICATION, "Registration key not yet sent to CPP");
		}
		return isRegistrationKeySendToCpp;
	}

	public static void storeRegistrationKeySendToCPP(boolean registrationKeySent) {
		final SharedPreferences prefs = getGCMPreferences();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(AppConstants.PROPERTY_IS_REGISTRATIONKEY_SEND_TO_CPP,
				registrationKeySent);
		editor.commit();
	}

	@Override
	public void signonStatus(boolean signon) {
		if (!signon)
			return;
		if (getIsRegistrationKeySendToCpp())
			return;

		ALog.i(ALog.NOTIFICATION,
				"ICPCLient signed on - sending GCM Registration ID to cpp: "
						+ regid);
		
		startHandlerThread();
//		ALog.i(ALog.NOTIFICATION,"mLooperThread : " + mLooperThread);
//		ALog.i(ALog.NOTIFICATION,"mLooperThread.mHandler : " + mLooperThread.mHandler);
//		mLooperThread.mHandler.sendEmptyMessageDelayed(0, 1500);

		// Dirty: need to send registration ID after 3sec so ICPClient can
		// properly startup
		// otherwise it will fail.
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				String previousProvider = CPPController.getInstance(PurAirApplication.getAppContext()).getNotificationProvider();
//				
//				if(previousProvider.equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_GOOGLE) && !Utils.isGooglePlayServiceAvailable()){
//					regid = JPushReceiver.getRegKey();
//				}			
//				sendRegistrationIdToBackend(regid);
//				if(regid != null && !regid.isEmpty()){
//					storeRegistrationId(PurAirApplication.getAppContext(), regid);
//				}
//				ALog.i(ALog.NOTIFICATION, "new Timer().schedule : regId " + regid);
//			}
//		}, 3000);
	}
	
	private static void setRegistrationProvider(String provider){
		mProvider = provider; 
	}
	
	public static String getRegitrationProvider(){
		return mProvider;
	}
	
	public class LooperThread extends Thread {
	    public Handler mHandler;

	    public void run() {
	        Looper.prepare();
	        ALog.i(ALog.NOTIFICATION,"LooperThread inside run");
	        mHandler = new Handler() {
	        	@Override
	        	public void handleMessage(Message msg) {
	        		ALog.i(ALog.NOTIFICATION,"LooperThread run handleMessage");
	        		mRegTryCount = 0;
	        		mChildHandler.sendEmptyMessageDelayed(0, 3000);
	        	}
	        };

	        mHandler.sendEmptyMessageDelayed(0, 100);
	        ALog.i(ALog.NOTIFICATION,"LooperThread mHandler : " + mHandler);
	        
	        Looper.loop();
	    }
	}
	
	public static Handler mChildHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 0:
				ALog.i(ALog.NOTIFICATION,"mChildHandler handleMessage mRegTryCount : " + mRegTryCount + " ....mRegistrationDone : " + mRegistrationDone);
				mRegTryCount ++;
				if(mChildHandler != null && !mRegistrationDone && mRegTryCount <= 2){
					ALog.i(ALog.NOTIFICATION, " regID : " + regid);
					sendRegistrationId();
					mChildHandler.sendEmptyMessageDelayed(0, 4000);
				}
				else if(mRegTryCount > 3){
					mRegTryCount = 0;
					creatingJpushNotificationManager();					
				}
				break;
				
			case 1: 	
				break;
			}
		};
	};
	
	private static void creatingJpushNotificationManager(){
		setRegistrationProvider(AppConstants.NOTIFICATION_PROVIDER_JPUSH);
		setNotificationManager();
		getNotificationManager();
	}
	
//	private static void creatingGCMNotificationManager(){
//		setRegistrationProvider(AppConstants.NOTIFICATION_PROVIDER_GOOGLE);
//		setNotificationManager();
//		getNotificationManager();
//	}
	
	private static void sendRegistrationId(){
		String previousProvider = CPPController.getInstance(PurAirApplication.getAppContext()).getNotificationProvider();
		
		if((previousProvider.equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_GOOGLE) && !Utils.isGooglePlayServiceAvailable()) || 
				getRegitrationProvider().equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH)){
			regid = JPushReceiver.getRegKey();
		}			
		sendRegistrationIdToBackend(regid);
		if(regid != null && !regid.isEmpty()){
			storeRegistrationId(PurAirApplication.getAppContext(), regid);
		}
		ALog.i(ALog.NOTIFICATION, "sendRegistrationId : regId " + regid);
	}
	
	@Override
	public void onRegistrationIdSentSuccess() {
		ALog.i(ALog.NOTIFICATION, "Registration ID successfully sent to CPP");
		mRegistrationDone = true;
		mChildHandler.removeMessages(0);
		storeRegistrationKeySendToCPP(true);
	}

	@Override
	public void onRegistrationIdSentFailed() {
		ALog.i(ALog.NOTIFICATION,
				"Failed to send Registration ID to CPP - callback");
		storeRegistrationKeySendToCPP(false);
	}

}
