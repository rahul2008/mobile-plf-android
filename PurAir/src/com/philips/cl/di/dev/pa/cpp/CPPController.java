package com.philips.cl.di.dev.pa.cpp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.icpinterface.DownloadData;
import com.philips.icpinterface.EventPublisher;
import com.philips.icpinterface.EventSubscription;
import com.philips.icpinterface.GlobalStore;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.ICPClientToAppInterface;
import com.philips.icpinterface.Provision;
import com.philips.icpinterface.SignOn;
import com.philips.icpinterface.configuration.Params;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;
import com.philips.icpinterface.data.PeripheralDevice;

public class CPPController implements ICPClientToAppInterface, ICPEventListener {
	
	private static final String TAG = CPPController.class.getName();

	private static CPPController icpStateInstance;

	private static final String CERTIFICATE_EXTENSION = ".cer";

	private SignOn signon;
	private boolean isSignOn;
	private SignonListener signOnListener;

	private ICPCallbackHandler callbackHandler;

	private Params configParams;

	private Context context;

	private EventSubscription eventSubscription; 
	private List<ICPDeviceDetailsListener> listeners;
	private boolean isDCSRunning;

	private EventPublisher eventPublisher; 

	private DownloadData downloadData;
	private ICPDownloadListener downloadDataListener;
	private StringBuilder downloadDataBuilder;
	ArrayList<PeripheralDevice> periPheralDevices = new ArrayList<PeripheralDevice>();
	
	private enum KEY_PROVISION {
		NOT_PROVISIONED,
		PROVISIONING,
		PROVISIONED
	}
	
	private KEY_PROVISION keyProvisioningState = KEY_PROVISION.NOT_PROVISIONED ;
	
	private CPPController(Context context) {
		this.context = context;
		listeners = new ArrayList<ICPDeviceDetailsListener>();
		callbackHandler = new ICPCallbackHandler();
		callbackHandler.setHandler(this);
		eventPublisher = new EventPublisher(callbackHandler);
		
		init() ;
	}

	/**
	 * 
	 * @param appContext
	 * @return
	 */
	public static CPPController getInstance(Context appContext) {
		ALog.i(ALog.ICPCLIENT, "GetInstance: " + icpStateInstance);
		if (null == icpStateInstance) {
			icpStateInstance = new CPPController(appContext);
			// init and signon
		}
		return icpStateInstance;
	}
	
	public void signOnWithProvisioning()
	{
		if( getKeyProvisioningState() == KEY_PROVISION.NOT_PROVISIONED)
		{
			ALog.i(ALog.ICPCLIENT, "startprovisioning on network change if not provisioned") ;
			startKeyProvisioning();
		}
		else if(getKeyProvisioningState() == KEY_PROVISION.PROVISIONED
				&& !isSignOn()) {
			ALog.i(ALog.ICPCLIENT, "startsignon on network change if not signed on") ;
			
			onSignon();
		}
	}

	private void startKeyProvisioning() {
		ALog.i(ALog.KPS, "Start provision");
		
			keyProvisioningState = KEY_PROVISION.PROVISIONING ;
			String appID = null;
			String appVersion = null;
			int rv = 0;
	
			// IF KPS not enabled exist from the activity.
			if (!SignOn.isKPSEnabled()) {
				return;
			}	
			
			// set Peripheral Information
			Provision prv = new Provision(callbackHandler, configParams,
					null, context);
	
			// Set Application Info
			PackageManager pm = context.getPackageManager();
			appID = context.getPackageName();
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
		return isSignOn;
	}

	private KEY_PROVISION getKeyProvisioningState() {
		return keyProvisioningState ;
	}
	
	/**
	 * This method will not be there in the final step.
	 */
	private void setConfigParameters() {
		try {
			InputStream in = context.getAssets().open("nvmfile.cfg");
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					Charset.defaultCharset()));
			if (configParams instanceof DemoAppConfigurationParametersForProvisioned)
				((DemoAppConfigurationParametersForProvisioned) configParams)
				.setNVMConfigParams(br);
			else if (configParams instanceof DemoAppConfigurationParametersForKeyProvisioning)
				((DemoAppConfigurationParametersForKeyProvisioning) configParams)
				.setNVMConfigParams(br);

			in.close();
			br.close();
		} catch (Exception e) {
			System.out.println("Exception Raised While reading");
		}
	}

	/**
	 * Method to inialize
	 */
	private void init() {
		Log.i("cpp", "init SignOn.isKPSEnabled() " + SignOn.isKPSEnabled()
				+ " SignOn.isTLSEnabled() " + SignOn.isTLSEnabled());
		if (SignOn.isKPSEnabled()) {
			// Provision
			configParams = new DemoAppConfigurationParametersForKeyProvisioning();
			setConfigParameters();
		}

		String property = System.getProperty("java.library.path");
		StringTokenizer parser = new StringTokenizer(property, ":");
		while (parser.hasMoreTokens()) {
			System.err.println(parser.nextToken());
		}
		int rv = 0;

		if(signon == null)
		{
			signon = SignOn.getInstance(callbackHandler, configParams);
		}

		// For TLS/KPS enabled case to load-certificates/chek network & other
		// information
		// Need android context
		if (SignOn.isTLSEnabled() == true || SignOn.isKPSEnabled() == true) {
			signon.setInterfaceAndContextObject(this, context);
		}
		rv = signon.init();

		if (rv == Errors.SUCCESS) {
			startKeyProvisioning();
		}

	}
	
	/**
	 * This method will be used for signon.
	 */
	private void signon() {
		Log.i(TAG, "signon");
		signon.setIsFirstTime(true);
		int rv = signon.executeCommand();
		if( rv != Errors.SUCCESS ) {
			isSignOn = false ;
		}
	}

	/**
	 * Sets the signon parameter
	 * 
	 * @param signon
	 */
	public void setSignon(boolean signon) {
		this.isSignOn = signon;
	}

	/**
	 * This method will call the signon On Callback the status of the signon is
	 * known.
	 */
	private void onSignon() {
		ALog.i(ALog.ICPCLIENT, "onSignOn");
		if(! isSignOn ) {
			isSignOn = true ;
			ICPCallbackHandler callbackHandler = new ICPCallbackHandler();
			callbackHandler.setHandler(this);
//			DemoAppConfigurationParametersForProvisioned configParams = new DemoAppConfigurationParametersForProvisioned(context);
			signon();
		}
	}

	/**
	 * This methodn will be used to set the listener of DataDownload
	 * 
	 * @param downloadDataListener
	 */
	public void setDownloadDataListener(ICPDownloadListener downloadDataListener) {
		ALog.i(ALog.INDOOR_RDCP, "setDownloadDataListener");
		this.downloadDataListener = downloadDataListener;
	}

	/**
	 * Method to add the listeners
	 * 
	 * @param deviceDetailsListener
	 */
	public void addDeviceDetailsListener(
			ICPDeviceDetailsListener deviceDetailsListener) {
		if (listeners != null) {
			listeners.add(deviceDetailsListener);
		}
	}

	/**
	 * Method to remove the listeners
	 * 
	 * @param deviceDetailsListener
	 */
	public void removeDeviceDetailsListener(
			ICPDeviceDetailsListener deviceDetailsListener) {
		if (listeners != null) {
			listeners.remove(deviceDetailsListener);
		}
	}

	/** Subcribe event methods **/
	/**
	 * This method will subscribe to events
	 */
	public void startDCSService() {
		Log.i("connect", "Start DCS: " + isDCSRunning + " isSIgnOn" + isSignOn);

		if (!isDCSRunning) {
			if (isSignOn) {
				Log.i(TAG, "Signon Success");
				int numberOfEvents = 20;
				eventSubscription = EventSubscription.getInstance(callbackHandler,
						numberOfEvents);
				eventSubscription.setFilter("");
				eventSubscription.setServiceTag("");

				eventSubscription.executeCommand();

				isDCSRunning = true;
			} else {
				Log.i(TAG, "Not signed on");
				signOnWithProvisioning() ;
			}
		}
	}

	/**
	 * Stop the DCS service
	 */
	public void stopDCSService() {
		Log.i(TAG, "Stop DCS service");
		if (eventSubscription != null) {
			if (isDCSRunning) {
				Log.i(TAG, "Stopped");
				eventSubscription.stopCommand();
				isDCSRunning = false;
			}
		}
	}

	/**
	 * This method will notify all the listeners for DCS events
	 * 
	 * @param icpClientObj
	 */
	private void notifyListeners(String dataReceived) {
		ALog.i(ALog.ICPCLIENT, "notifyListeners()= " + dataReceived);
		if (dataReceived.contains(AppConstants.PRODUCT)) {
			AirPurifierEventDto airPurifierDetails = DataParser.parseAirPurifierEventDataFromCPP(dataReceived);
			int numberOfListerners = listeners.size();
			ALog.i(ALog.ICPCLIENT, "Air Purifier Details: "+airPurifierDetails) ;
			for (int index = 0; index < numberOfListerners; index++) {
				listeners.get(index)
				.onReceivedDeviceDetails(airPurifierDetails);
			}
		}
	}
	/**
	 * This method will be used to publish the events from App to Air Purifier
	 * via CPP
	 * 
	 * @param eventData
	 * @param eventType
	 * @param actionName
	 * @param replyTo
	 * @param conversationId
	 * @param priority
	 * @param ttl
	 */
	public void publishEvent(String eventData, String eventType,
			String actionName, String replyTo, String conversationId,
			int priority, int ttl) {
		ALog.i(ALog.ICPCLIENT, "publishEvent eventData " + eventData + " eventType "
				+ eventType + " Action Name: " +actionName +
				" replyTo: " +replyTo +" + isSignOn "+isSignOn);
		if (isSignOn) {
			String airPurifierID = Utils.getAirPurifierID(context);
			eventPublisher.setEventInformation(eventType, actionName,
					replyTo, conversationId, priority, ttl);
			eventPublisher.setEventData(eventData);
			eventPublisher.setTargets(new String[] { airPurifierID });
			eventPublisher.setEventCommand(Commands.PUBLISH_EVENT);

			eventPublisher.executeCommand();
		}
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
		ALog.i(ALog.INDOOR_RDCP, "downloadDataFromCPP query: " + query +", isSignOn: " + isSignOn);
		try {
			if (isSignOn) {
				downloadData = new DownloadData(callbackHandler);
				downloadData.setDownloadDataDetails(query, 2048, 0, 0);
				downloadData.executeCommand();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}

	public void restartDCSService() {
		stopDCSService();
		startDCSService();
	}

	public void addSignonListener(SignonListener signOnListener) {
		this.signOnListener = signOnListener;
	}

	/***
	 * This is the callback method for all the CPP related events. (Signon,
	 * Publish Events, Subscription, etc..)
	 * 
	 */

	@Override
	public void onICPCallbackEventOccurred(int eventType, int status,
			ICPClient obj) {
		ALog.i(ALog.ICPCLIENT, "onICPCallbackEventOccurred eventType "
				+ eventType + " status " + status);
		if (eventType == Commands.SIGNON) {
			if (status == Errors.SUCCESS) {
				ALog.i(ALog.ICPCLIENT, "SIGNON-SUCCESSFUL") ;
				isSignOn = true;
				if (signOnListener != null) {
					signOnListener.signonStatus(true);
				}				
				// startDCSService();
			} else {
				ALog.e(ALog.ICPCLIENT, "SIGNON-FAILED") ;
				isSignOn = false ;
				if (signOnListener != null) {
					signOnListener.signonStatus(false);
				}
			}
			signOnListener = null;
		} else if (eventType == Commands.KEY_PROVISION) {
			if (status == Errors.SUCCESS) {
				ALog.i(ALog.KPS, "PROVISION-SUCCESS");
				keyProvisioningState = KEY_PROVISION.PROVISIONED ;
				Provision provision = (Provision) obj;
				ALog.i(ALog.KPS, "EUI64(APP-KEY): "+provision.getEUI64());
				SessionDto.getInstance().setAppEui64(provision.getEUI64());
				onSignon();
			}
			else {
				ALog.e(ALog.KPS, "PROVISION-FAILED");
				keyProvisioningState = KEY_PROVISION.NOT_PROVISIONED ;
			}
		} else if (eventType == Commands.SUBSCRIBE_EVENTS) {
			String dcsEvents = "";
			if (status == Errors.SUCCESS) {
				int noOfEvents = 0;

				noOfEvents = eventSubscription.getNumberOfEventsReturned();

				for (int i = 0; i < noOfEvents; i++) {
					dcsEvents = eventSubscription.getData(i);
					if (eventSubscription.getState() == EventSubscription.SUBSCRIBE_EVENTS_RECEIVED) {
						notifyListeners(dcsEvents);
					}
				}
			}
		} else if (eventType == Commands.DOWNLOAD_DATA) {
			ALog.i(ALog.INDOOR_RDCP, "ICP client callbacked");
//			byte[] bufferOriginal = ((DownloadData) obj).getBuffer().array();
			
			byte[] bufferOriginal = new byte[((DownloadData)obj).getBuffer().capacity()];
			for(int i = 0 ;i < ((DownloadData)obj).getBuffer().capacity(); i++)
			{
				bufferOriginal[i] = ((DownloadData)obj).getBuffer().get(i);
			}
			
			byte[] buffer = bufferOriginal.clone();

			if (downloadDataBuilder == null) {
				downloadDataBuilder = new StringBuilder();
			}

			downloadDataBuilder.append(new String(buffer, Charset
					.defaultCharset()));
			ALog.i(ALog.INDOOR_RDCP, "ICP client download: " + downloadDataBuilder.toString());
			ALog.i(ALog.INDOOR_RDCP, "ICP client download: " + ((DownloadData) obj).getIsDownloadComplete());

			if (((DownloadData) obj).getIsDownloadComplete()) {
				Log.i(TAG, "Download complete");
				if (downloadDataListener != null) {
					String downloadedData = downloadDataBuilder.toString();
					downloadDataBuilder.setLength(0);
					downloadDataListener.onDataDownload(status, downloadedData);
				}
			}
		}
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
			ALog.e(ALog.CPPCONTROLLER, e.getMessage());
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

	public static void reset() {
		icpStateInstance = null;		
	}

}
