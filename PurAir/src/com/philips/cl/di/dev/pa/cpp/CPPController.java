package com.philips.cl.di.dev.pa.cpp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.RemoteViews;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.notification.NotificationRegisteringManager;
import com.philips.cl.di.dev.pa.notification.SendNotificationRegistrationIdListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.icpinterface.ComponentDetails;
import com.philips.icpinterface.DownloadData;
import com.philips.icpinterface.EventPublisher;
import com.philips.icpinterface.EventSubscription;
import com.philips.icpinterface.FileDownload;
import com.philips.icpinterface.GlobalStore;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.ICPClientToAppInterface;
import com.philips.icpinterface.Provision;
import com.philips.icpinterface.SignOn;
import com.philips.icpinterface.ThirdPartyNotification;
import com.philips.icpinterface.configuration.Params;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.ComponentInfo;
import com.philips.icpinterface.data.Errors;

public class CPPController implements ICPClientToAppInterface, ICPEventListener {

	private static CPPController icpStateInstance;
	private static final int APP_UPDATE_NOTIFICATION_BUILDER_ID = 128272;
	private static final String CERTIFICATE_EXTENSION = ".cer";
	public static final String BOOT_STRAP_ID_1 = "MDAwMD";

	private static SignOn signon;
	private boolean isSignOn;
	private List<SignonListener> signOnListeners;

	private SendNotificationRegistrationIdListener notificationListener;
	private AppUpdateNotificationListener appUpdateNotificationListener ; 

	private ICPCallbackHandler callbackHandler;
	private Params configParams;
	private Context context;

	private EventSubscription eventSubscription; 
    private HashMap<String,DCSEventListener> mDcsEventListenersMap = new HashMap<String, DCSEventListener>(); 
	private DCSResponseListener dcsResponseListener ;
	private CppDiscoverEventListener mCppDiscoverEventListener;
	private boolean isDCSRunning;

	//DCS client state
	private enum ICP_CLIENT_DCS_STATE { STARTED, STARTING, STOPPED, STOPPING } ;
	// App Requested State
	//This is required if the callback has delay in Starting and Stopping
	private enum APP_REQUESTED_STATE { NONE, START, STOP };

	private ICP_CLIENT_DCS_STATE dcsState = ICP_CLIENT_DCS_STATE.STOPPED;
	private APP_REQUESTED_STATE appDcsRequestState = APP_REQUESTED_STATE.NONE ;


	private DownloadData downloadData;
	private ICPDownloadListener downloadDataListener;
	private StringBuilder downloadDataBuilder;
	private List<PublishEventListener> publishEventListeners ;
	private String provider = null;
	private int cntOffset  = 0;
	private int fileSize = 0;
	private int percentage ;
	private FileOutputStream fos = null;
	private int byteOffset = 0;
	private NotificationManager mNotifyManager;
	private Notification notification;
	private String filePath = "";
	private EventPublisher eventPublisher ;
	
	private enum KEY_PROVISION {
		NOT_PROVISIONED,
		PROVISIONING,
		PROVISIONED
	}

	private KEY_PROVISION keyProvisioningState = KEY_PROVISION.NOT_PROVISIONED ;
	private boolean appUpdateAlertShown;
	private String mAppCppId;

	private CPPController(Context context) {
		this.context = context;
		callbackHandler = new ICPCallbackHandler();
		callbackHandler.setHandler(this);
		
		signOnListeners = new ArrayList<SignonListener>();
		publishEventListeners = new ArrayList<PublishEventListener>() ;

		appUpdateAlertShown=false;
		init() ;
	}

	private CPPController() {
		// Only used for testing
	}

	/**
	 * 
	 * @param appContext
	 * @return
	 */
	public static synchronized CPPController getInstance(Context appContext) {
		ALog.i(ALog.ICPCLIENT, "GetInstance: " + icpStateInstance);
		if (null == icpStateInstance) {
			icpStateInstance = new CPPController(appContext);
			// init and signon
		}
		setLocale();
		return icpStateInstance;
	}

	public void signOnWithProvisioning() {
		if (getKeyProvisioningState() == KEY_PROVISION.NOT_PROVISIONED) {
			ALog.i(ALog.ICPCLIENT, "startprovisioning on network change if not provisioned");
			startKeyProvisioning();
		} else if (getKeyProvisioningState() == KEY_PROVISION.PROVISIONED && !isSignOn()) {
			ALog.i(ALog.ICPCLIENT, "startsignon on network change if not signed on");
			signOn();
		}
				
	}

	private void startKeyProvisioning() {
			ALog.i(ALog.KPS, "Start provision");
			keyProvisioningState = KEY_PROVISION.PROVISIONING ;
			String appID = null;
			String appVersion = null;
			int rv = 0;
	
			// set Peripheral Information
			Provision prv = new Provision(callbackHandler, configParams,
					null, context);
	
			// Set Application Info
			PackageManager pm = context.getPackageManager();
			appID = "1_com.philips.cl.di.air";
			try {
				appVersion = ""
						+ pm.getPackageInfo(context.getPackageName(), 0).versionCode;
			} catch (Exception e) {
				e.printStackTrace();
			}
			ALog.i(ALog.KPS, appID + ":" + AppConstants.APP_TYPE + ":" + appVersion);
			prv.setApplicationInfo(appID, AppConstants.APP_TYPE, appVersion);
	
			rv = prv.executeCommand();
			if (rv != Errors.SUCCESS) {
				ALog.i(ALog.KPS, "PROVISION-FAILED");
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				rv = prv.executeCommand();
				if(rv != Errors.SUCCESS ) {
					keyProvisioningState = KEY_PROVISION.NOT_PROVISIONED ;
				}
		}
	}

	public boolean isSignOn() {
		if (signon == null) {
			signon = SignOn.getInstance(callbackHandler, configParams);			
		}
		signon.getSignOnStatus();
		return signon.getSignOnStatus();
	}

	private KEY_PROVISION getKeyProvisioningState() {
		return keyProvisioningState ;
	}

	/**
	 * Method to inialize
	 */
	private void init() {
		// Provision
		configParams = new PurAirKPSConfiguration();
		((PurAirKPSConfiguration) configParams).setNVMConfigParams();

		int rv = 0;

		if (signon == null) {
			signon = SignOn.getInstance(callbackHandler, configParams);
		}
		
		// For TLS/KPS enabled case to load-certificates/chek network & other
		// information
		// Need android context
		// if (SignOn.isTLSEnabled() || SignOn.isKPSEnabled()) {
		signon.setInterfaceAndContextObject(this, context);
		// }

		rv = signon.init();

		if (rv == Errors.SUCCESS) {
			startKeyProvisioning();
		}
	}

	/**
	 * This method will call the signon On Callback the status of the signon is
	 * known.
	 */
	private void signOn() {
		if(! isSignOn ) {
			ALog.i(ALog.ICPCLIENT, "onSignOn");
			isSignOn = true ;

			if( callbackHandler == null) {
				callbackHandler = new ICPCallbackHandler();
				callbackHandler.setHandler(this);
			}

			signon.setIsFirstTime(true);
			ALog.i(ALog.ICPCLIENT,"Version: "+signon.clientVersion()) ;
			int rv = signon.executeCommand();
			if( rv != Errors.SUCCESS ) {
				isSignOn = false ;
			}
		}
	}

	public void addSignOnListener(SignonListener signOnListener) {
		synchronized (signOnListeners) {
			if (!signOnListeners.contains(signOnListener)) {
				signOnListeners.add(signOnListener);
				ALog.v(ALog.CPPCONTROLLER, "Added signOn listener - " +signOnListener.hashCode());
			}
		}
	}

	public void removeSignOnListener(SignonListener signOnListener) {
		synchronized (signOnListeners) {
			if (signOnListeners.contains(signOnListener)) {
				signOnListeners.remove(signOnListener);
				ALog.v(ALog.CPPCONTROLLER, "Removed signOn listener - " +signOnListener.hashCode());
			}
		}
	}

	public void setNotificationListener(SendNotificationRegistrationIdListener listener) {
		notificationListener = listener;
	}

	public void setDownloadDataListener(ICPDownloadListener downloadDataListener) {
		ALog.i(ALog.INDOOR_RDCP, "setDownloadDataListener");
		this.downloadDataListener = downloadDataListener;
	}
	
	public void removeDownloadDataListener() {
		ALog.i(ALog.INDOOR_RDCP, "setDownloadDataListener");
		this.downloadDataListener = null;
	}

	public void addDCSEventListener(String cppId, DCSEventListener dcsEventListener) {
		mDcsEventListenersMap.put(cppId, dcsEventListener);
	}
	
	private DCSEventListener getDCSEventListener(String cppId) {
		return mDcsEventListenersMap.get(cppId);
	}

	public void setCppDiscoverEventListener(CppDiscoverEventListener mCppDiscoverEventListener) {
		this.mCppDiscoverEventListener = mCppDiscoverEventListener;
	}

	public void setDCSResponseListener(DCSResponseListener dcsResponseListener) {
		this.dcsResponseListener = dcsResponseListener ;
	}

	public void addPublishEventListener(PublishEventListener publishEventListener) {
		synchronized (publishEventListeners) {
			if (!publishEventListeners.contains(publishEventListener)) {
				this.publishEventListeners.add(publishEventListener) ;
			}
		}
	}
	
	public void removePublishEventListener(PublishEventListener publishEventListener) {
		synchronized (publishEventListeners) {
			if (publishEventListeners.contains(publishEventListener)) {
				publishEventListeners.remove(publishEventListener);
			}
		}
	}
	
	/** Subcribe event methods **/
	/**
	 * This method will subscribe to events
	 */
	public void startDCSService() {
		ALog.d(ALog.CPPCONTROLLER, "Start DCS: " + isDCSRunning + " isSIgnOn" + isSignOn +"DCS state: " +dcsState);
		
			if( dcsState == ICP_CLIENT_DCS_STATE.STOPPED) {
				dcsState = ICP_CLIENT_DCS_STATE.STARTING ;
				appDcsRequestState = APP_REQUESTED_STATE.NONE ;
				if (isSignOn) {
					ALog.i(ALog.CPPCONTROLLER, "Starting DCS - Already Signed On");
					int numberOfEvents = 1;
					eventSubscription = EventSubscription.getInstance(callbackHandler,
							numberOfEvents);
					eventSubscription.setFilter("");
					eventSubscription.setServiceTag("");
	
					eventSubscription.executeCommand();
				} else {
					ALog.i(ALog.CPPCONTROLLER, "Failed to start DCS - not signed on");
					signOnWithProvisioning() ;
				}
			}
			else {
				appDcsRequestState = APP_REQUESTED_STATE.START ;
			}
	
	}		

	/**
	 * Stop the DCS service
	 */
	public void stopDCSService() {
		if(! isSignOn()) return ;
		if (eventSubscription == null) return;
		if( dcsState == ICP_CLIENT_DCS_STATE.STARTED) {
			dcsState = ICP_CLIENT_DCS_STATE.STOPPING ;
			appDcsRequestState = APP_REQUESTED_STATE.NONE ;
			ALog.i(ALog.SUBSCRIPTION, "Stop DCS service");
			eventSubscription.stopCommand();
		}
		else {
			appDcsRequestState = APP_REQUESTED_STATE.STOP ;
		}
	}


	private void notifySignOnListeners(boolean signOnStatus) {
		synchronized (signOnListeners) {
			for (SignonListener listener : signOnListeners) {
				listener.signonStatus(signOnStatus);
			}
		}
	}

	private void notifyNotificationListener(boolean success) {
		if (notificationListener == null) return;
		if (success) {
			notificationListener.onRegistrationIdSentSuccess();
		} else {
			notificationListener.onRegistrationIdSentFailed();
		}
	}

	public void notifyDCSListener(String data, String fromEui64, String action) {
		if( action == null ) return ;
		if( action.equalsIgnoreCase("RESPONSE") && dcsResponseListener != null) {
			dcsResponseListener.onDCSResponseReceived(data) ;
		}
		if (data == null) return;	
		
		if (DataParser.parseDiscoverInfo(data) != null) {
			ALog.i(ALog.SUBSCRIPTION, "Discovery event received - " + action);
			boolean isResponseToRequest = false;
			if (action != null
					&& action.toUpperCase().trim().equals(AppConstants.DISCOVER)) {
				isResponseToRequest = true;
			}
			if (mCppDiscoverEventListener != null) {
				mCppDiscoverEventListener.onDiscoverEventReceived(data, isResponseToRequest);
			}
			return;
		}
		
		if (getDCSEventListener(fromEui64) != null) {
			getDCSEventListener(fromEui64).onDCSEventReceived(data, fromEui64, action);
		}
	}

	/**
	 * This method will be used to publish the events from App to Air Purifier
	 * via CPP
	 * 
	 * @param eventData
	 * @param eventType
	 * @param actionName
	 * @param conversationId
	 * @param priority
	 * @param ttl
	 */
	public int publishEvent(String eventData, String eventType,
			String actionName, String conversationId, int priority,
			int ttl, String purifierEui64) {
		eventPublisher = new EventPublisher(callbackHandler);
		int messageID = -1 ;
		ALog.i(ALog.ICPCLIENT, "publishEvent eventData " + eventData + " eventType "
				+ eventType + " Action Name: " +actionName +
				" replyTo: " +mAppCppId +" + isSignOn "+isSignOn);
		if (isSignOn) {
			eventPublisher.setEventInformation(eventType, actionName,
					mAppCppId, conversationId, priority, ttl);
			eventPublisher.setEventData(eventData);
			if (purifierEui64 != null) {
				eventPublisher.setTargets(new String[] { purifierEui64 });
			} else {
				eventPublisher.setTargets(new String[0]);
			}
			eventPublisher.setEventCommand(Commands.PUBLISH_EVENT);
			eventPublisher.executeCommand();
			messageID = eventPublisher.getMessageId() ;
		}
		return messageID ;
	}

	public boolean sendNotificationRegistrationId(String gcmRegistrationId) {
		if (!CPPController.getInstance(PurAirApplication.getAppContext()).isSignOn()) {
			ALog.e(ALog.CPPCONTROLLER, "Failed to send registration ID to CPP - not signed on");
			return false;
		}

		NotificationRegisteringManager.getNotificationManager();
		if(NotificationRegisteringManager.getRegitrationProvider().equalsIgnoreCase(
				AppConstants.NOTIFICATION_PROVIDER_JPUSH) || !Utils.isGooglePlayServiceAvailable()){
			provider = AppConstants.NOTIFICATION_PROVIDER_JPUSH;
		}
		else{
			provider = AppConstants.NOTIFICATION_PROVIDER_GOOGLE;
		}

		ALog.i(ALog.NOTIFICATION, "CPPController sendNotificationRegistrationId provider : " + provider 
				+"------------RegId : " + gcmRegistrationId);

		ThirdPartyNotification thirdParty = new ThirdPartyNotification(
				callbackHandler, AppConstants.NOTIFICATION_SERVICE_TAG);
		thirdParty.setProtocolDetails(AppConstants.NOTIFICATION_PROTOCOL, provider/* = 
				Utils.isGooglePlayServiceAvailable() ? AppConstants.NOTIFICATION_PROVIDER_GOOGLE : 
					AppConstants.NOTIFICATION_PROVIDER_JPUSH*/, gcmRegistrationId);

		int retStatus =  thirdParty.executeCommand();
		if (Errors.SUCCESS != retStatus)	{
			ALog.e(ALog.CPPCONTROLLER, "Failed to send registration ID to CPP - immediate");
			return false;
		}
		return true;
	}

	public SharedPreferences getGCMPreferences() {
		return PurAirApplication.getAppContext().getSharedPreferences(
				AppConstants.NOTIFICATION_PREFERENCE_FILE_NAME,
				Context.MODE_PRIVATE);
	}

	public String getNotificationProvider() {
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


	/**
	 * This method will download the data from the cpp given the query and the
	 * buffer size callback from the download will happen in
	 * onICPCallbackEventOccurred
	 * 
	 * @param query
	 * @param bufferSize
	 */
	public void downloadDataFromCPP(String query, int bufferSize) {
		//If purifier in demo mode, skip download data
		if (PurAirApplication.isDemoModeEnable()) {
			notifyDownloadDataListener(Errors.GENERAL_ERROR , null);
			return;
		}
		ALog.i(ALog.INDOOR_RDCP, "downloadDataFromCPP query: " + query +", isSignOn: " + isSignOn);
		try {
			if (isSignOn) {
				downloadData = new DownloadData(callbackHandler);
				downloadData.setDownloadDataDetails(query, 2048, 0, 0);
				downloadData.executeCommand();
			} else {
				notifyDownloadDataListener(Errors.GENERAL_ERROR , null);
			}
		} catch (IllegalArgumentException e) {
			notifyDownloadDataListener(Errors.GENERAL_ERROR , null);
			e.printStackTrace();
		} catch (Exception e) {
			notifyDownloadDataListener(Errors.GENERAL_ERROR , null);
			e.printStackTrace();
		} catch (Error e) {
			notifyDownloadDataListener(Errors.GENERAL_ERROR , null);
			e.printStackTrace();
		} 
	}

	public void setDefaultDcsState() {
		dcsState = ICP_CLIENT_DCS_STATE.STOPPED ;
		appDcsRequestState = APP_REQUESTED_STATE.NONE ;
	}

	public void setAppUpdateNotificationListener(AppUpdateNotificationListener listener) {
		this.appUpdateNotificationListener = listener ;
	}

	/***
	 * This is the callback method for all the CPP related events. (Signon,
	 * Publish Events, Subscription, etc..)
	 * 
	 */

	@Override
	public void onICPCallbackEventOccurred(int eventType, int status,
			ICPClient obj) {
		ALog.i(ALog.ICPCLIENT, "onICPCallbackEventOccurred eventType " + eventType + " status " + status);
		switch (eventType) {

		case Commands.SIGNON:
			if (status == Errors.SUCCESS) {
				ALog.i(ALog.ICPCLIENT, "SIGNON-SUCCESSFUL") ;
				isSignOn = true;
				notifySignOnListeners(true);
			} else {
				ALog.e(ALog.ICPCLIENT, "SIGNON-FAILED") ;
				isSignOn = false ;
				notifySignOnListeners(false);
			}
			break;
		case Commands.PUBLISH_EVENT:
			EventPublisher eventPublisher = (EventPublisher) obj;
			for(PublishEventListener listener: publishEventListeners) {
				listener.onPublishEventReceived(status, eventPublisher.getMessageId()) ;
			}
			break;
		case Commands.KEY_PROVISION:
			keyProvisionEvent(status, obj);
			break;
		case Commands.EVENT_NOTIFICATION:
			ALog.i(ALog.ICPCLIENT, "Event Notification: "+status) ;
			if( status == Errors.SUCCESS ) {
				if( appUpdateNotificationListener != null && !appUpdateAlertShown) {
					appUpdateNotificationListener.onAppUpdate() ;
					appUpdateAlertShown=true;
				}
			}
			break;
		case Commands.GET_COMPONENT_DETAILS:
			componentDetailsEvent(status, obj);
			break;
		case Commands.SUBSCRIBE_EVENTS:
			subscribeEvents(status, obj);
			break;
		case Commands.THIRDPARTY_REGISTER_PROTOCOLADDRS :
			thirdPartyRegisterProtocolAddressEvent(status, obj);
			break;
		case Commands.DOWNLOAD_DATA:
			rdcpDownloadEvent(status, obj);
			break;
		case Commands.DOWNLOAD_FILE:
			fileDownloadEvent(status, obj);
			break;
		default:
			break;
		}
	}
	
	private void keyProvisionEvent(int status, ICPClient obj) {
		if (status == Errors.SUCCESS) {
			ALog.i(ALog.KPS, "PROVISION-SUCCESS");
			keyProvisioningState = KEY_PROVISION.PROVISIONED ;
			Provision provision = (Provision) obj;
			ALog.i(ALog.KPS, "EUI64(APP-KEY): "+provision.getEUI64());
			mAppCppId = provision.getEUI64();
			signOn();
		}
		else {
			ALog.e(ALog.KPS, "PROVISION-FAILED");
			keyProvisioningState = KEY_PROVISION.NOT_PROVISIONED ;
		}
	}
	
	private void componentDetailsEvent(int status, ICPClient obj) {
		if (status == Errors.SUCCESS) {
			ALog.i(ALog.CPPCONTROLLER, "ICPCallback FetchComponentDetails success" );
			ComponentDetails componentDetails = (ComponentDetails) obj;
			int numberOfComponents = componentDetails.getNumberOfComponentReturned() ;
			ALog.i(ALog.ICPCLIENT, "Number of components: "+numberOfComponents) ;
			for( int index = 0 ; index < numberOfComponents; index ++ ) {
				if( componentDetails.getComponentInfo(index).id.equals(AppConstants.COMPONENT_ID)) {
					// Start software download
					if(isUpgradeAvailable(componentDetails.getComponentInfo(index).versionNumber)) {
						downloadNewApplication(componentDetails.getComponentInfo(index));
						break;
					}
				}
			}
		}
		else {
			downloadFailed() ;
			ALog.e(ALog.CPPCONTROLLER, "ICPCallback FetchComponentDetails failed: " + status);
		}
	}
	
	private void subscribeEvents(int status, ICPClient obj) {
		String dcsEvents = "";
		String fromEui64 = "";
		String action = "";
		//TODO : Handle SUBSCRIBE_EVENTS_STOPPED and SUBSCRIBE_EVENTS_DISCONNECTED 
		if (status == Errors.SUCCESS) {
			ALog.i(ALog.ICPCLIENT,"State :"+eventSubscription.getState())  ;
			dcsState = ICP_CLIENT_DCS_STATE.STARTED;
			if(eventSubscription.getState() == EventSubscription.SUBSCRIBE_EVENTS_STOPPED) {
				dcsState = ICP_CLIENT_DCS_STATE.STOPPED ;
				if( appDcsRequestState == APP_REQUESTED_STATE.START) {
					startDCSService() ;
				}
				return ;
			}
			else if (eventSubscription.getState() == EventSubscription.SUBSCRIBE_EVENTS_RECEIVED) {
				int noOfEvents = 0;
				noOfEvents = eventSubscription.getNumberOfEventsReturned();
				for (int i = 0; i < noOfEvents; i++) {
					dcsEvents = eventSubscription.getData(i);
					fromEui64 = eventSubscription.getReplyTo(i);
					action = eventSubscription.getAction(i);

					ALog.d(ALog.ICPCLIENT, "DCS event received from: " +fromEui64 + "    action: " + action);
					ALog.d(ALog.ICPCLIENT, "DCS event received: " +dcsEvents);
					notifyDCSListener(dcsEvents, fromEui64, action);
				}
			}
			else if( eventSubscription.getState() != EventSubscription.SUBSCRIBE_EVENTS_STOPPED) {
				int noOfEvents = 0;
				noOfEvents = eventSubscription.getNumberOfEventsReturned();
				for (int i = 0; i < noOfEvents; i++) {
					dcsEvents = eventSubscription.getData(i);
					fromEui64 = eventSubscription.getReplyTo(i);
					action = eventSubscription.getAction(i);

					ALog.d(ALog.ICPCLIENT, "DCS event received from: " +fromEui64 + "    action: " + action);
					ALog.d(ALog.ICPCLIENT, "DCS event received: " +dcsEvents);
				}
			}
			if (appDcsRequestState == APP_REQUESTED_STATE.STOP) {
				stopDCSService();
			}
		}
	}
	
	private void thirdPartyRegisterProtocolAddressEvent(int status, ICPClient obj) {
		ThirdPartyNotification tpns = (ThirdPartyNotification) obj;			
		if (status == Errors.SUCCESS && tpns.getRegistrationStatus()) {
			ALog.i(ALog.CPPCONTROLLER, "Successfully registered with CPP");
			ALog.i(ALog.NOTIFICATION, "Successfully registered with CPP");
			storeProviderInPref(provider);
			NotificationRegisteringManager.getNotificationManager().storeVersion(context, PurAirApplication.getAppVersion());
			String languageLocale = LanguageUtils.getLanguageForLocale(Locale.getDefault());
			NotificationRegisteringManager.getNotificationManager().storeLocale(context, languageLocale);
			notifyNotificationListener(true);
		} else {
			ALog.i(ALog.CPPCONTROLLER, "Failed to send registration ID to CPP - errorCode: " + status);
			notifyNotificationListener(false);
		}
	}
	
	private void rdcpDownloadEvent(int status, ICPClient obj) {
		if (status == Errors.SUCCESS) {
			byte[] bufferOriginal = new byte[((DownloadData) obj)
			                                 .getBuffer().capacity()];
			for (int i = 0; i < ((DownloadData) obj).getBuffer().capacity(); i++) {
				bufferOriginal[i] = ((DownloadData) obj).getBuffer().get(i);
			}

			byte[] buffer = bufferOriginal.clone();

			if (downloadDataBuilder == null) {
				downloadDataBuilder = new StringBuilder();
			}

			downloadDataBuilder.append(new String(buffer, Charset.defaultCharset()));

			if (((DownloadData) obj).getIsDownloadComplete()) {
				ALog.d(ALog.CPPCONTROLLER, "Download complete");
				if (downloadDataListener != null) {
					String downloadedData = downloadDataBuilder.toString();
					downloadDataBuilder.setLength(0);
					notifyDownloadDataListener(status, downloadedData);
				}
			}
		} else {
			notifyDownloadDataListener(status, null);
		}
	}
	
	private void notifyDownloadDataListener(int status, String downloadedData) {
		if (downloadDataListener != null) {
			downloadDataListener.onDataDownload(status, downloadedData);
		}
	}
	
	private void fileDownloadEvent(int status, ICPClient obj) {
		if(status == Errors.SUCCESS) {

			if (fos == null) {
				createFileOutputStream(obj);
			}
			if (fos != null) {
				try {
					byte[] bufferOriginal = new byte[((FileDownload)obj).getBuffer().capacity()];
					if (bufferOriginal != null) {
						for(int i = 0; i < ((FileDownload)obj).getBuffer().capacity(); i++)	{
							bufferOriginal[i] = ((FileDownload)obj).getBuffer().get(i);
						}

						byte[] buffer = bufferOriginal.clone();

						fos.write(buffer);
						byteOffset += buffer.length;
						float currentPercentage = (byteOffset / (float) fileSize) * 100;
						if(percentage != (int)currentPercentage) {
							percentage = (int)currentPercentage;
							notification.contentView.setProgressBar(R.id.notification_progressbar, 100, percentage, false);        
					        notification.contentView.setTextViewText(R.id.notification_progressbar_percent, percentage + "%");    
					        mNotifyManager.notify(APP_UPDATE_NOTIFICATION_BUILDER_ID, notification);
						}
					}
					
					if(((FileDownload)obj).getDownloadStatus() == true) {
						//In downloading time, if Internet disconnect, then we are getting getDownloadStatus() true.
						// So we added double check as file size.
						if (((FileDownload)obj).getDownloadProgress() == fileSize) {
							fileDownloadCompleted();
						} else {
							downloadFailed();
						}
					}
				} catch (IOException e) {
					downloadFailed() ;
					e.printStackTrace();
				}
			}
		}
		else {
			downloadFailed() ;
		}
	}
	
	private void createFileOutputStream(ICPClient obj) {
		try {
			createAppNotificationBuilder() ;
			File sdcardWithDirFile = Utils.getExternalStorageDirectory(AppConstants.APP_UPDATE_DIRECTORY);
			if(sdcardWithDirFile==null){
				fos=null;
				return;
			}
			File outFile = new File(sdcardWithDirFile, AppConstants.APP_UPDATE_FILE_NAME);
			if (outFile != null) filePath = outFile.toString(); 
			fos = new FileOutputStream(outFile);
			byteOffset = 0;
			fileSize = ((FileDownload)obj).getFileSize();
		} catch (FileNotFoundException e) {
			downloadFailed() ;
			e.printStackTrace();
			fos = null;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void createAppNotificationBuilder() {
		mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification(R.drawable.purair_icon, 
        		context.getString(R.string.app_update_notif_title), System.currentTimeMillis());
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);
        contentView.setProgressBar(R.id.notification_progressbar, 100, percentage, false);        
        contentView.setTextViewText(R.id.notification_progressbar_percent, percentage + "%");        
        notification.contentView = contentView;

        PendingIntent dummyIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
        notification.contentIntent = dummyIntent;
        mNotifyManager.notify(APP_UPDATE_NOTIFICATION_BUILDER_ID, notification);
	}
	
	private void fileDownloadCompleted() {
		
		notification.contentView.setProgressBar(R.id.notification_progressbar, 100, 100, false);        
        notification.contentView.setTextViewText(R.id.notification_progressbar_percent, percentage + "%");   
		
        mNotifyManager.notify(APP_UPDATE_NOTIFICATION_BUILDER_ID, notification);
        mNotifyManager.cancel(APP_UPDATE_NOTIFICATION_BUILDER_ID);
        // Reset the offset
        percentage = 0 ;
        byteOffset = 0;
		closeFileOutputStream() ;
		showPackageInstaller();
	}
	
	private void showPackageInstaller() {
		Intent intentInstaller = new Intent(Intent.ACTION_VIEW);
		intentInstaller.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(filePath));
		intentInstaller.setDataAndType(uri, "application/vnd.android.package-archive");
		context.startActivity(intentInstaller);
	}

	private void downloadNewApplication(ComponentInfo componentInfo) {
		File sdcardWithDirFile = Utils.getExternalStorageDirectory(AppConstants.APP_UPDATE_DIRECTORY);
		
		if (sdcardWithDirFile == null && appUpdateNotificationListener != null) {
			appUpdateNotificationListener.onAppUpdateFailed("External storage not available");
			return;
		}
		
		FileDownload fileDownload = new FileDownload(callbackHandler);

		fileSize = componentInfo.size;

		fileDownload.setURL(componentInfo.url);
		fileDownload.setSecurityKey(componentInfo.secuirtyKey);
		fileDownload.setCRC(componentInfo.crc);
		fileDownload.setChunkSize(10240);
		fileDownload.setSize(componentInfo.size);
		fileDownload.setOffset(cntOffset);

		int rv = fileDownload.executeCommand();
		if (rv == Errors.SUCCESS) {
			ALog.i(ALog.ICPCLIENT, "File download parameters are correct");
		}

	}

	private boolean isUpgradeAvailable(int versionAvailableInCPP) {
		// TODO Auto-generated method stub
		ALog.i(ALog.ICPCLIENT, "Version at CPP:"+versionAvailableInCPP) ;
		if( PurAirApplication.getAppVersion() < versionAvailableInCPP) {
			ALog.i(ALog.ICPCLIENT, "Version:"+PurAirApplication.getAppVersion()) ;
			return true;
		}
		return false;
	}

	@Override
	public boolean loadCertificates() {
		GlobalStore gs = GlobalStore.getInstance();

		// Read certificates
		byte[] buffer = new byte[1024];
		try {
			String assetFiles[] = context.getAssets().list("");
			InputStream in;

			for (String asset : assetFiles) {
				if (asset.contains(CERTIFICATE_EXTENSION)) {
					in = context.getAssets().open(asset);
					int read = 0;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((read = in.read(buffer, 0, buffer.length)) != -1) {
						baos.write(buffer, 0, read);
					}
					baos.flush();
					in.close();
					gs.setCertificateByteArray(baos.toByteArray());
					baos.close();
				}
			}
		} catch (IOException e) {
			ALog.e(ALog.CPPCONTROLLER, "Error: " + e.getMessage());
		}
		if (gs.getNumberOfCertificates() > 0) {
			return true;
		}
		return false;
	}

	/*
	 * Description: Function throws exception if network not exist. If network
	 * exist return from function.
	 */
	@Override
	public void checkNetworkSate() throws Exception {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting() == true) {
			// Network exist
			return;
		}
		throw new Exception("No Network Exist");
	}

	/*
	 * Returns ICPClient version
	 */
	public String getICPClientVersion() {
		if (signon == null) {
			signon = SignOn.getInstance(callbackHandler, configParams);
		}
		return signon.clientVersion();
	}

	public static final CPPController getCppControllerForTesting() {
		return new CPPController();
	}

	public void fetchICPComponents() {
		ComponentInfo[] componentInfo = new ComponentInfo[1];

		componentInfo[0] = new ComponentInfo();
		componentInfo[0].id = AppConstants.COMPONENT_ID;
		componentInfo[0].versionNumber = PurAirApplication.getAppVersion();

		ComponentDetails componentDetails = new ComponentDetails(callbackHandler, componentInfo);

		int responseCode = componentDetails.executeCommand();
		if (responseCode == Errors.SUCCESS) {
			ALog.i(ALog.CPPCONTROLLER, "fetchICPComponentDetails success");
		} else {
			downloadFailed() ;
			ALog.e(ALog.CPPCONTROLLER, "fetchICPComponentDetails failed");
		}
	}
	
	private void downloadFailed() {
		if( mNotifyManager != null ) {
			mNotifyManager.cancel(APP_UPDATE_NOTIFICATION_BUILDER_ID) ;
		}
		if( appUpdateNotificationListener != null ) {
			appUpdateNotificationListener.onAppUpdateFailed(AppConstants.EMPTY_STRING) ;
		}
	}
	
	private void closeFileOutputStream() {
		try {
			if (fos != null) {
				fos.close();
				fos = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void setLocale(){
		if (signon == null) return;
		
		signon.setNewLocale(Utils.getCountryCode(), Utils.getCountryLocale());
	}
	
	public void setAppUpdateStatus(boolean shown){
		appUpdateAlertShown=shown;
	}

	public String getAppCppId() {
		return mAppCppId;
	}
}
