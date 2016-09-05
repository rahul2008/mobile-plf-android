package com.philips.cl.di.dev.pa.notification;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import cn.jpush.android.api.JPushInterface;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.listener.SendNotificationRegistrationIdListener;
import com.philips.cdp.dicommclient.cpp.listener.SignonListener;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
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
	private static final int TRY_GCM = 0;
	private static final int TRY_JPUSH = 1;
	private static String mProvider = AppConstants.PROPERTY_NOTIFICATION_PROVIDER;
	private boolean isRegistrationNeeded = true;
	private static int jPushRetryCount = 0;

	public NotificationRegisteringManager() {
		CppController.getInstance()
				.addSignOnListener(this);
		CppController.getInstance()
				.setNotificationListener(this);

//		if (!Utils.isGooglePlayServiceAvailable() || getRegitrationProvider().equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH)) {
			ALog.d(ALog.NOTIFICATION,"NO GOOGLE SERVICE");
			setRegistrationProvider(AppConstants.NOTIFICATION_PROVIDER_JPUSH);
//			if(gcm!=null){
//				try {
//					gcm.unregister();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				gcm = null;
//			}
//			JPushInterface.setDebugMode(false);
//			JPushInterface.init(PurAirApplication.getAppContext());
//			JPushInterface.resumePush(PurAirApplication.getAppContext());
//		} else {
//			ALog.i(ALog.NOTIFICATION,"GOOGLE SERVICE");
//			setRegistrationProvider(AppConstants.NOTIFICATION_PROVIDER_GOOGLE);
//			gcm = GoogleCloudMessaging.getInstance(PurAirApplication
//					.getAppContext());
//			regid = getRegistrationId();
//			ALog.i(ALog.NOTIFICATION, "NotificationRegisteringManager : regId " + regid);
//		}
	}
	
	private void startHandlerThread(){
		ALog.i(ALog.NOTIFICATION, "startHandlerThread()");
		mLooperThread = null;
		mLooperThread = new LooperThread();
		mLooperThread.start();
	}
	
	public static NotificationRegisteringManager getNotificationManager(){
		if(mNotificationManager == null){
			mNotificationManager = new NotificationRegisteringManager();
			jPushRetryCount = 0;
		}
		return mNotificationManager;	
	}
	
	public static void setNotificationManager(){
		mNotificationManager = null;
		mRegistrationDone = false;
	}
	
	public void registerAppForNotification() {
		ALog.d(ALog.NOTIFICATION, "registerAppForNotification");
		if(!isRegistrationNeeded()){
			ALog.e(ALog.NOTIFICATION, "registerAppForNotification Already registered");
			return;
		}
		if (!Utils.isGooglePlayServiceAvailable() || getRegitrationProvider().equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH)) {
			ALog.e(ALog.NOTIFICATION,
					"Google play services not supported on this device");
			setRegistrationProvider(AppConstants.NOTIFICATION_PROVIDER_JPUSH);
			regid = JPushReceiver.getRegKey();
			ALog.i(ALog.NOTIFICATION,"JPush RegID : " + regid);
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
		regid = JPushReceiver.getRegKey();
		sendRegistrationIdToBackend(regid);
		ALog.i(ALog.NOTIFICATION, "registerForJPushService regId :  " + regid);
		// Persist the regID - no need to register again.
		if(regid != null && !regid.isEmpty()){
			storeRegistrationId(regid);
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
					ALog.i(ALog.NOTIFICATION, msg);
					// send the registration ID to CPP server.
					sendRegistrationIdToBackend(regid);

					// Persist the regID - no need to register again.
					if(regid != null && !regid.isEmpty()){
						storeRegistrationId(regid);
					}
					ALog.i(ALog.NOTIFICATION, "registerForGoogleService  regid" + regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					ALog.i(ALog.NOTIFICATION, "registerForGoogleService Error : " + msg);
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
		if (!CppController.getInstance().isSignOn())
			return;
		storeRegistrationKeySendToCPP(false);
		if (regid == null || regid.isEmpty())
			return;
		// TODO:DICOMM Refactor, check getRegitrationProvider has valid value
		CppController.getInstance().sendNotificationRegistrationId(regid,  getRegitrationProvider());
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

	private static void storeRegistrationId(String registrationId) {
		final SharedPreferences prefs = getGCMPreferences();

		ALog.i(ALog.NOTIFICATION,
				"Storing registration ID for app ");

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
	
	public void storeLocale(Context ctx, String locale) {
		final SharedPreferences prefs = getGCMPreferences();

		ALog.i(ALog.NOTIFICATION,
				"Storing locale for app : " + locale);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(AppConstants.PROPERTY_APP_LOCALE, locale);
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
	
	public static SharedPreferences getGCMPreferences() {
		return PurAirApplication.getAppContext().getSharedPreferences(
				AppConstants.NOTIFICATION_PREFERENCE_FILE_NAME,
				Context.MODE_PRIVATE);
	}
	
	public static String getNotificationProvider() {
		final SharedPreferences prefs = getGCMPreferences();
		String previousProvider = prefs.getString(AppConstants.PROPERTY_NOTIFICATION_PROVIDER,
				AppConstants.PROPERTY_NOTIFICATION_PROVIDER);

		if (previousProvider.equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_GOOGLE)) {
			return AppConstants.NOTIFICATION_PROVIDER_GOOGLE;
		}
		if (previousProvider.equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH)) {
			return AppConstants.NOTIFICATION_PROVIDER_JPUSH;
		}
		else {
			return AppConstants.PROPERTY_NOTIFICATION_PROVIDER;
		}
	}

	private void storeProviderInPref(String provider) {
		final SharedPreferences prefs = getGCMPreferences();

		ALog.i(ALog.NOTIFICATION,
				"Storing Push notification provider name : " + provider);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(AppConstants.PROPERTY_NOTIFICATION_PROVIDER, provider);
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
		if(isRegistrationNeeded()){
			startHandlerThread();
		}
	}
	
	public static void setRegistrationProvider(String provider){
		mProvider = provider; 
	}
	
	public static String getRegitrationProvider(){
		return mProvider;
	}
	
	private static final class MyHandler extends Handler {
	    private final WeakReference<NotificationRegisteringManager> mReference;

	    public MyHandler(NotificationRegisteringManager activity) {
	    	mReference = new WeakReference<NotificationRegisteringManager>(activity);
	    }

	    @Override
	    public void handleMessage(Message msg) {
	    	NotificationRegisteringManager activity = mReference.get();
	    	ALog.i(ALog.NOTIFICATION,"MyHandler handleMessage activity : " +activity);

	    	switch(msg.what){
	  		case TRY_GCM:
	      		ALog.i(ALog.NOTIFICATION,"MyHandler run handleMessage TRY_GCM");
	      		mRegTryCount = 0;
	      		mChildHandler.sendEmptyMessageDelayed(TRY_GCM, 3000);
	      		break;
	  		
	  		case TRY_JPUSH: 
  				mChildHandler.sendEmptyMessage(TRY_JPUSH);
  				ALog.i(ALog.NOTIFICATION,"MyHandler run handleMessage TRY_JPUSH");
	  			break;
	  		
	  		default:
	  			break;
  		}
	    }
	  }
	
	private class LooperThread extends Thread {
	    private MyHandler mHandler = null;
	    
	    public void run() {
	        Looper.prepare();
	        ALog.i(ALog.NOTIFICATION,"LooperThread inside run");
	        
	        mHandler = new MyHandler(NotificationRegisteringManager.this);
	        
	        if(getRegitrationProvider().equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH)){
				mHandler.sendEmptyMessage(TRY_JPUSH);
				getNotificationManager().registerAppForNotification();
			}
	        else{
	        	mHandler.sendEmptyMessageDelayed(TRY_GCM, 100);
	        }
	        ALog.i(ALog.NOTIFICATION,"LooperThread mHandler : " + mHandler);
	        Looper.loop();
	    }
	}
	
	private static final Handler mChildHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case TRY_GCM:
				ALog.i(ALog.NOTIFICATION,"mChildHandler handleMessage mRegTryCount : " + mRegTryCount + " ....mRegistrationDone : " + mRegistrationDone);
				mRegTryCount ++;
				if(mChildHandler != null && !mRegistrationDone && mRegTryCount <= 2){
					ALog.i(ALog.NOTIFICATION, " regID : " + regid);
					sendRegistrationId();
					mChildHandler.sendEmptyMessageDelayed(TRY_GCM, 4000);
				}
				else if(mRegTryCount > 3){
					mRegTryCount = 0;
					creatingJpushNotificationManager();					
				}
				break;
				
			case TRY_JPUSH: 
				ALog.i(ALog.NOTIFICATION,"mChildHandler handleMessage TRY_JPUSH");
				creatingJpushNotificationManager();	
				break;
			}
		};
	};
	
	private static void creatingJpushNotificationManager(){
		ALog.i(ALog.NOTIFICATION, "creatingJpushNotificationManager now");
		regid = JPushReceiver.getRegKey();
		setRegistrationProvider(AppConstants.NOTIFICATION_PROVIDER_JPUSH);
		setNotificationManager();
//		for(int i = 0; i < 2; i++){
//			if(!mRegistrationDone){
//				getNotificationManager().registerAppForNotification();
//			}
//		}
	}
	
	private static void sendRegistrationId(){
		String previousProvider = getNotificationProvider();
		
		if((previousProvider.equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_GOOGLE) && !Utils.isGooglePlayServiceAvailable()) || 
				getRegitrationProvider().equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH)){
			regid = JPushReceiver.getRegKey();
		}			
		sendRegistrationIdToBackend(regid);
		if(regid != null && !regid.isEmpty()){
			storeRegistrationId(regid);
		}
		ALog.i(ALog.NOTIFICATION, "sendRegistrationId : regId " + regid);
	}
	
	@Override
	public void onRegistrationIdSentSuccess() {
		ALog.d(ALog.NOTIFICATION, "Registration ID successfully sent to CPP");
		mRegistrationDone = true;
		jPushRetryCount = 0;
		if(mChildHandler!=null){
			mChildHandler.removeMessages(TRY_JPUSH);
			mChildHandler.removeMessages(TRY_GCM);
		}
		
		storeProviderInPref(mProvider);
		NotificationRegisteringManager.getNotificationManager().storeVersion(PurAirApplication.getAppContext(), PurAirApplication.getAppVersion());
		String languageLocale = LanguageUtils.getLanguageForLocale(Locale.getDefault());
		NotificationRegisteringManager.getNotificationManager().storeLocale(PurAirApplication.getAppContext(), languageLocale);
		storeRegistrationKeySendToCPP(true);
	}

	@Override
	public void onRegistrationIdSentFailed() {
		jPushRetryCount ++;
		ALog.i(ALog.NOTIFICATION,
				"Failed to send Registration ID to CPP - callback");
		ALog.i(ALog.NOTIFICATION, "JPUSH retry count : = " + jPushRetryCount);
		
		/*
		 * If failed to send token to CPP then retry again.
		 */
		if(jPushRetryCount <= 2 && !mRegistrationDone){
			storeRegistrationKeySendToCPP(false);
			getNotificationManager().registerAppForNotification();
		}
	}
	
	private void initNotification() {
		setRegistrationProvider(AppConstants.PROPERTY_NOTIFICATION_PROVIDER);
		setNotificationManager();
		getNotificationManager();
		storeRegistrationKeySendToCPP(false);
		
//		if (Utils.isGooglePlayServiceAvailable()/* && !(NotificationRegisteringManager.getRegitrationProvider().
//				equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH))*/) {
//			getNotificationManager().registerAppForNotification();
//		}
	}

	
	public void getNotificationRegisteringManager() {
		jPushRetryCount = 0;
		String provider = getNotificationProvider(); 
		if(isVersionChanged()){
			isRegistrationNeeded = true ;
			ALog.i(ALog.NOTIFICATION," NotificationRegisteringManager version changed");
			initNotification();
		}
		else if(isLocaleChanged()) {
			isRegistrationNeeded = true ;
			ALog.i(ALog.NOTIFICATION," NotificationRegisteringManager locale changed");
			initNotification();
			if (!Utils.isGooglePlayServiceAvailable() || (NotificationRegisteringManager.getRegitrationProvider().
					equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH))){
				NotificationRegisteringManager.getNotificationManager().registerAppForNotification();
			}
		}
//		else if (Utils.isGooglePlayServiceAvailable() && provider.equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_GOOGLE)) {
//			isRegistered = false;
//			ALog.i(ALog.NOTIFICATION,"NotificationRegisteringManager third previsouly=GCM now = GCM");
//		}
		else if (/*!Utils.isGooglePlayServiceAvailable() && */provider.equalsIgnoreCase(AppConstants.NOTIFICATION_PROVIDER_JPUSH)) {
			isRegistrationNeeded = false;
			ALog.i(ALog.NOTIFICATION,"NotificationRegisteringManager No need to register. Its already registered with JPush.");
		}
		else {			
			isRegistrationNeeded = true;
			ALog.i(ALog.NOTIFICATION,"NotificationRegisteringManager else block");
			initNotification();
		}		
	}

	private boolean isRegistrationNeeded() {
		return isRegistrationNeeded;
	}
	
	public static boolean isVersionChanged() {
		final SharedPreferences prefs = getGCMPreferences();

		int registeredVersion = prefs.getInt(AppConstants.PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = PurAirApplication.getAppVersion();
		boolean isGCMRegistrationExpired = (registeredVersion != currentVersion);

		if (isGCMRegistrationExpired) {
			ALog.d(ALog.NOTIFICATION, "Registration ID expired - App version changed");
			return true;
		}

		return false;
	}

	public static boolean isLocaleChanged() {
		final SharedPreferences prefs = getGCMPreferences();
		String languageLocale = LanguageUtils.getLanguageForLocale(Locale.getDefault());

		String registeredLocale = prefs.getString(AppConstants.PROPERTY_APP_LOCALE,
				LanguageUtils.DEFAULT_LANGUAGE);
		boolean isLocalChanged = registeredLocale.equalsIgnoreCase(languageLocale);

		if (!isLocalChanged) {
			ALog.d(ALog.NOTIFICATION,
					"App Locale change happened");
			return true;
		}

		return false;
	}

//	private void setRegistered(boolean isRegistered) {
//		this.isRegistrationNeeded = isRegistered;
//	}
}
