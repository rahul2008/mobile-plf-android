package com.philips.cl.di.dev.pa.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.interfaces.ICPDeviceDetailsListener;
import com.philips.cl.di.dev.pa.interfaces.ICPDownloadListener;
import com.philips.cl.di.dev.pa.interfaces.ICPEventListener;
import com.philips.cl.di.dev.pa.interfaces.SignonListener;
import com.philips.cl.di.dev.pa.utils.DataParser;
import com.philips.cl.di.dev.pa.utils.Utils;
import com.philips.icpinterface.DownloadData;
import com.philips.icpinterface.EventPublisher;
import com.philips.icpinterface.EventSubscription;
import com.philips.icpinterface.GlobalStore;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.ICPClientToAppInterface;
import com.philips.icpinterface.SignOn;
import com.philips.icpinterface.configuration.Params;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;

public class CPPController implements ICPClientToAppInterface, ICPEventListener {

	private static final String TAG = CPPController.class.getName() ;

	private static CPPController icpStateInstance ;

	private static final String certificateExtension = ".cer";

	private SignOn signon ;
	private boolean isSignOn ;
	private SignonListener signOnListener ;

	private static ICPCallbackHandler callbackHandler =  new ICPCallbackHandler();

	private static Params configParams;

	private Context context ;

	private EventSubscription eventSubscription ;
	private List<ICPDeviceDetailsListener> listeners ;
	private boolean isDCSRunning ;

	private EventPublisher eventPublisher ;

	private DownloadData downloadData;
	private ICPDownloadListener downloadDataListener ;
	private StringBuilder downloadDataBuilder ;

	private CPPController(Context context) {
		this.context = context ;
		listeners = new ArrayList<ICPDeviceDetailsListener>() ;
		eventPublisher = new EventPublisher(callbackHandler) ;
		callbackHandler.setHandler(this) ;		
	}

	/**
	 * 
	 * @param appContext
	 * @return
	 */
	public static CPPController getInstance(Context appContext) {

		if ( null == icpStateInstance ) {
			icpStateInstance = new CPPController(appContext) ;
			//icpStateInstance.init() ;
			// init and signon
		}
		return icpStateInstance ;
	}

	public boolean isSignOn() {
		return isSignOn;
	}


	/**
	 * This method will not be there in the final step.
	 */
	private void setConfigParameters() 
	{
		try
		{
			InputStream in = context.getAssets().open("nvmfile.cfg");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			if(configParams instanceof DemoAppConfigurationParametersForProvisioned)
				((DemoAppConfigurationParametersForProvisioned)configParams).setNVMConfigParams(br);
			else if(configParams instanceof DemoAppConfigurationParametersForKeyProvisioning)
				((DemoAppConfigurationParametersForKeyProvisioning)configParams).setNVMConfigParams(br);

			in.close();
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception Raised While reading");
		}
	}

	/**
	 * Method to inialize
	 */
	public void init() {
		Log.i(TAG, "init SignOn.isKPSEnabled() " + SignOn.isKPSEnabled() + " SignOn.isTLSEnabled() " + SignOn.isTLSEnabled());
		if(SignOn.isKPSEnabled() == true)
		{
			configParams = new DemoAppConfigurationParametersForKeyProvisioning();
			setConfigParameters();
		}
		else if(SignOn.isTLSEnabled() == true)
		{
			configParams = new DemoAppConfigurationParametersForProvisioned(context);
			setConfigParameters();
		}
		else {
			configParams = new DemoAppConfigurationParametersForProvisioned(context);
		}


		String property = System.getProperty("java.library.path"); 
		StringTokenizer parser = new StringTokenizer(property, ":"); 
		while (parser.hasMoreTokens()) 
		{ 
			System.err.println(parser.nextToken()); 
		}
		int rv = 0;

		SignOn.create(callbackHandler, configParams);

		signon = SignOn.getInstance();
		//For TLS/KPS enabled case to load-certificates/chek network & other information
		//Need android context
		if(SignOn.isTLSEnabled() == true || SignOn.isKPSEnabled() == true )
			signon.setInterfaceObject(this);  //Important part
		rv = signon.init();

		if ( rv == Errors.SUCCESS) {
			onSignon() ;
		}
	}

	/**
	 * This method will be used for signon.
	 */
	private void signon() {	      
		Log.i(TAG, "signon");
		signon = SignOn.getInstance();
		signon.setIsFirstTime(true) ;
		signon.executeCommand() ;        
	}

	/**
	 * Sets the signon parameter
	 * @param signon
	 */
	public void setSignon(boolean signon) {
		this.isSignOn = signon ;
	}

	/**
	 * This method will call the signon 
	 * On Callback the status of the signon is known.
	 */
	private void onSignon() {
		Log.i(TAG, "onSignOn");
		ICPCallbackHandler callbackHandler = new ICPCallbackHandler();
		callbackHandler.setHandler(this) ;
		DemoAppConfigurationParametersForProvisioned configParams = new DemoAppConfigurationParametersForProvisioned(context);
		SignOn.create(callbackHandler, configParams) ;
		signon() ;
	}

	/**
	 * This methodn will be used to set the listener of DataDownload
	 * @param downloadDataListener
	 */
	public void setDownloadDataListener(ICPDownloadListener downloadDataListener) {
		this.downloadDataListener = downloadDataListener ;
	}

	/**
	 * Method to add the listeners
	 * @param deviceDetailsListener
	 */
	public void addDeviceDetailsListener(ICPDeviceDetailsListener deviceDetailsListener) {
		if ( listeners != null ) {
			listeners.add(deviceDetailsListener) ;
		}
	}

	/**
	 * Method to remove the listeners
	 * @param deviceDetailsListener
	 */
	public void removeDeviceDetailsListener(ICPDeviceDetailsListener deviceDetailsListener) {
		if ( listeners != null ) {
			listeners.remove(deviceDetailsListener) ;
		}
	}


	/** Subcribe event methods **/
	/**
	 * This method will subscribe to events
	 */
	public void startDCSService() {
		Log.i(TAG, "Start DCS: "+isDCSRunning + " isSIgnOn" + isSignOn) ;
		if ( !isDCSRunning ) {
			if ( isSignOn ) {
				Log.i(TAG, "Signon Success") ;
				int numberOfEvents = 20 ;
				eventSubscription = EventSubscription.create(callbackHandler,numberOfEvents) ;
				eventSubscription.setFilter("") ;
				eventSubscription.setServiceTag("") ;

				eventSubscription.executeCommand() ;

				isDCSRunning = true ;
			}
			else {
				Log.i(TAG, "Not signed on") ;
				init() ;
			}
		}
	}

	/**
	 * Stop the DCS service
	 */
	public void stopDCSService()
	{
		Log.i(TAG, "Stop DCS service") ;
		if(eventSubscription != null) {
			if( isDCSRunning ) {
				Log.i(TAG, "Stopped") ;
				eventSubscription.stopCommand();
				isDCSRunning = false ;
			}
		}
	}

	/**
	 * This method will notify all the listeners for DCS events
	 * @param icpClientObj
	 */
	private void notifyListeners(String dataReceived) {
		Log.i(TAG, "notifyListeners()= "+dataReceived) ;
		if ( dataReceived.contains(AppConstants.PRODUCT)) {
			AirPurifierEventDto airPurifierDetails = new DataParser(dataReceived).parseAirPurifierEventDataFromCPP();
			int numberOfListerners = listeners.size() ;
			for( int index = 0 ; index < numberOfListerners ; index ++ ) {
				listeners.get(index).onReceivedDeviceDetails(airPurifierDetails);
			}
		}
	}

	/**
	 * This method will be used to publish the events from App to Air Purifier via CPP
	 * 
	 * @param eventData
	 * @param eventType
	 * @param actionName
	 * @param replyTo
	 * @param conversationId
	 * @param priority
	 * @param ttl
	 */
	public void publishEvent(String eventData,String eventType, String actionName, String replyTo, String conversationId, int priority, int ttl) {
		Log.i(TAG, "publishEvent eventData " + eventData + " eventType " + eventType + " isSignOn " + isSignOn);
		if( isSignOn ) {
			String airPurifierID = Utils.getAirPurifierID(context) ;

//			if( airPurifierID  == null ) {
//				airPurifierID = AppConstants.AIRPURIFIER_ID ;
//			}
			eventPublisher.setEventInformation(eventType, actionName, airPurifierID, conversationId, priority, ttl);
			eventPublisher.setEventData(eventData);
			eventPublisher.setTargets(new String [] {airPurifierID});
			eventPublisher.setEventCommand(Commands.PUBLISH_EVENT);

			eventPublisher.executeCommand();
		}
	}

	/**
	 * This method will download the data from the cpp given the query and the buffer size
	 * callback from the download will happen in onICPCallbackEventOccurred
	 * @param query
	 * @param bufferSize
	 */
	public void downloadDataFromCPP(String query, int bufferSize) {
		try {
			if ( isSignOn ) {
				downloadData = new DownloadData(callbackHandler) ;
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
		stopDCSService() ;
		startDCSService() ;
	}

	public void addSignonListener(SignonListener signOnListener) {
		this.signOnListener = signOnListener ;
	}


	/***
	 * This is the callback method for all the CPP related events.
	 *  (Signon, Publish Events, Subscription, etc..)
	 * 
	 */
	@Override
	public void onICPCallbackEventOccurred(int eventType, int status, ICPClient obj) {
		Log.i(TAG, "onICPCallbackEventOccurred eventType " + eventType + " status " + status);
		if ( eventType == Commands.SIGNON ) {
			if ( status == Errors.SUCCESS ) {
				isSignOn = true ;
				if ( signOnListener != null) {
					signOnListener.signonStatus(true) ;
				}
				startDCSService() ;
			}
			else {
				if ( signOnListener != null) {
					signOnListener.signonStatus(false) ;
				}
			}
			signOnListener = null ;
		}
		else if ( eventType == Commands.SUBSCRIBE_EVENTS ) {
			String dcsEvents = "" ;
			if ( status == Errors.SUCCESS) {
				int NoOfEvents = 0;

				NoOfEvents = eventSubscription.getNumberOfEventsReturned();

				for (int i = 0; i < NoOfEvents; i++)
				{
					dcsEvents = eventSubscription.getData(i);
					if ( eventSubscription.getState() == EventSubscription.SUBSCRIBE_EVENTS_RECEIVED) {
						notifyListeners(dcsEvents) ;
					}
				}
			}
		}
		else if ( eventType == Commands.DOWNLOAD_DATA ) {

			byte[] bufferOriginal =((DownloadData)obj).getBuffer().array();
			byte[] buffer = bufferOriginal.clone();

			if( downloadDataBuilder == null ) {
				downloadDataBuilder = new StringBuilder() ;	
			}

			downloadDataBuilder.append(new String(buffer)) ;

			if(((DownloadData)obj).getIsDownloadComplete() == true)
			{
				Log.i(TAG, "Download complete") ;
				if( downloadDataListener != null ) {
					String downloadedData = downloadDataBuilder.toString() ;
					downloadDataBuilder.setLength(0) ;
					downloadDataListener.onDataDownload(status, downloadedData ) ;
				}
			}
		} 
	}

	@Override
	public boolean loadCertificates() 
	{
		GlobalStore gs = GlobalStore.getInstance();

		//Read certificates
		byte[] buffer = new byte[1024];
		try {
			String assetFiles[] = context.getAssets().list("");
			InputStream in;

			for(String asset : assetFiles)
			{
				if(asset.contains(certificateExtension))
				{
					in = context.getAssets().open(asset);
					int read = 0;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((read = in.read(buffer, 0, buffer.length)) != -1) 
					{
						baos.write(buffer, 0, read);
					}
					baos.flush();
					in.close();
					gs.setCertificateByteArray(baos.toByteArray());
					baos.close();
				}
			}
		} 
		catch (IOException e) 
		{
			
		}
		if(gs.getNumberOfCertificates() > 0)
		{
			return true;
		}
		return false;
	}


	/*
	 * Description: Function throws exception if network not exist. If network exist return from function.
	 */
	@Override
	public void checkNetworkSate() throws Exception 
	{
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnectedOrConnecting() == true)
		{
			//Network exist
			return;
		}
		throw new Exception("No Network Exist");

	}
	
	/*
	 * Returns ICPClient version
	 */
	public String getICPClientVersion()
	{
		if(signon==null) {
			SignOn.create(callbackHandler, configParams) ;
			signon = SignOn.getInstance();
		}
			
		
		return signon.clientVersion();
	}

}
