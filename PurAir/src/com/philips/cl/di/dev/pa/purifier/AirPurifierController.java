package com.philips.cl.di.dev.pa.purifier;


import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.datamodel.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;

/**
 *
 * This class acts as an interface between the server and the UI component
 * All requests from user interface will be through this interface.
 * This class will take the request from User Interface, sends it to server class, gets the response
 * parses it and sends it back to the user interface.
 *
 */
public class AirPurifierController implements ServerResponseListener, SubscriptionEventListener
{
	
	private static AirPurifierController airPurifierController ;
	
	/** The sensor data handler. */
	private AirPurifierEventListener airPurifierEventListener;
	
	private List<AirPurifierEventListener> subscriptionEventListeners ;

	private SubscriptionManager subscriptionManager ;
	

	
	/**
	 * Instantiates a new air purifier controller.
	 *
	 * @param sensorDataHandler the sensor data handler
	 * @param context the context
	 */
	private AirPurifierController() {
		subscriptionManager = SubscriptionManager.getInstance() ;
		subscriptionManager.setSubscriptionListener(this) ;
		subscriptionEventListeners = new ArrayList<AirPurifierEventListener>() ;
	}
	
	public static AirPurifierController getInstance() {
		if( null == airPurifierController ) {
			airPurifierController = new AirPurifierController() ;
		}
		return airPurifierController ;
	}
	
	
	public void setDeviceDetailsLocally(String key, String value )
	{
		String dataToUpload = JSONBuilder.getDICommBuilder(key,value) ;
		startServerTask(dataToUpload) ;
	}	
	public void setDeviceDetailsRemotely(String key, String value) {
		String eventData = JSONBuilder.getPublishEventBuilder(key, value) ;
		// Publish events
		CPPController.getInstance(PurAirApplication.getAppContext()).publishEvent(eventData,AppConstants.DI_COMM_REQUEST, AppConstants.PUT_PROPS, SessionDto.getInstance().getEui64(), "", 20, 120) ;
	}

	/**
	 * Method to call the Server task
	 * @param nameValuePair
	 */
	private void startServerTask(String dataToUpload) {
		TaskPutDeviceDetails statusUpdateTask = new TaskPutDeviceDetails(dataToUpload,String.format(AppConstants.URL_CURRENT, Utils.getIPAddress(PurAirApplication.getAppContext())),this) ;
		Thread statusUpdateTaskThread = new Thread(statusUpdateTask) ;
		statusUpdateTaskThread.start() ;
	}

	/**
	 * (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.util.ServerResponseListener#receiveServerResponse(int, java.lang.String)
	 * This is a call back to async task.
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		// TODO 
		ALog.i(ALog.AIRPURIFIER_CONTROLER, "Response: "+responseData);
		switch (responseCode) {
		case HttpsURLConnection.HTTP_OK:
				parseSensorData(responseData) ;
			break;
		default:
			if( airPurifierEventListener != null) {
				airPurifierEventListener.airPurifierEventReceived(null) ;
			}
			break;
		}
	}

	/**
	 * Parse the Sensor/Air Purifier Event data
	 * @param dataToParse
	 */
	private void parseSensorData(String dataToParse) {
		ALog.i(ALog.AIRPURIFIER_CONTROLER, "parse sensor data");
		AirPurifierEventDto airPurifierEvent = null ;
		if( dataToParse != null) {
			airPurifierEvent = new DataParser(dataToParse).parseAirPurifierEventData() ;
		}
		if ( airPurifierEventListener != null)
			airPurifierEventListener.airPurifierEventReceived(airPurifierEvent) ;
	}
	

	public void setAirPurifierEventListner(AirPurifierEventListener subscriptionEventListener) {
		subscriptionEventListeners.add(subscriptionEventListener) ;
	}
	
	public void removeSubscriptionListner(SubscriptionEventListener subscriptionEventListener) {
		subscriptionEventListeners.remove(subscriptionEventListener) ;
	}
	
	public void notifyListeners(String data) {
		AirPurifierEventDto airPurifier = new DataParser(data).parseAirPurifierEventData() ;
		if( subscriptionEventListeners != null ) {
			int listeners = subscriptionEventListeners.size() ;
			for( int index = 0 ; index < listeners ; index ++ ) {
				subscriptionEventListeners.get(index).airPurifierEventReceived(airPurifier) ;
			}
		}
	}
	
	public void subscribe(String cppID, String url, boolean isLocal) {
		ALog.i("Subscription", "cppID: "+cppID) ;
		subscriptionManager.subscribe(cppID, url,isLocal) ;
	}

	public void unSubscribe(String cppID,String url) {
		subscriptionManager.unSubscribe(cppID, url) ;
	}
	
	@Override
	public void onSubscribeEventOccurred(String data) {
		if( data != null ) {
			notifyListeners(data) ;
		}
		
	}
}