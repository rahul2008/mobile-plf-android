package com.philips.cl.di.dev.pa.purifier;


import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import android.os.AsyncTask;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.security.DISecurity;
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
	
	
	public void setDeviceDetailsLocally(String key, String value, PurAirDevice purifier)
	{
		String dataToUpload = JSONBuilder.getDICommBuilder(key, value, purifier) ;
		if(dataToUpload != null && !dataToUpload.isEmpty()) {
			startServerTask(dataToUpload, purifier.getIpAddress()) ;
		}
	}	
	public void setDeviceDetailsRemotely(String key, String value, PurAirDevice purifier) {
		String eventData = JSONBuilder.getPublishEventBuilder(key, value) ;
		// Publish events
		CPPController.getInstance(PurAirApplication.getAppContext()).publishEvent(eventData,AppConstants.DI_COMM_REQUEST, AppConstants.PUT_PROPS, SessionDto.getInstance().getEui64(), "", 20, 120, purifier.getEui64()) ;
	}

	/**
	 * Method to call the Server task
	 * @param nameValuePair
	 */
	private void startServerTask(String dataToUpload, String purifierIp) {
		ALog.i(ALog.AIRPURIFIER_CONTROLER, "Start the server task for subscribe") ;
		TaskPutDeviceDetails statusUpdateTask = new TaskPutDeviceDetails(dataToUpload, Utils.getPortUrl(Port.AIR, purifierIp),this) ;
		Thread statusUpdateTaskThread = new Thread(statusUpdateTask) ;
		statusUpdateTaskThread.start() ;
	}

	public void getPurifierDetails(String url) {
		ALog.i(ALog.AIRPURIFIER_CONTROLER, "Get Purifier details") ;
		AsyncTask<String, ?, ?> sensorDataTask = null;
		sensorDataTask = new TaskGetSensorData(this);
		sensorDataTask.execute(url);
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
				PurAirDevice purifier = PurifierManager.getInstance().getCurrentPurifier();
				String decryptedData = new DISecurity(null).decryptData(responseData, purifier) ;
				parseSensorData(decryptedData) ;
			break;
		default:			
			break;
		}
	}

	/**
	 * Parse the Sensor/Air Purifier Event data
	 * @param dataToParse
	 */
	private void parseSensorData(String dataToParse) {
		ALog.i(ALog.AIRPURIFIER_CONTROLER, "parse sensor data: \n"+dataToParse);
		notifyListeners(dataToParse) ;
	}
	

	public void addAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		subscriptionEventListeners.add(airPurifierEventListener) ;
	}
	
	public void removeAirPurifierEventListener(AirPurifierEventListener airPurifierEventListener) {
		subscriptionEventListeners.remove(airPurifierEventListener) ;
	}
	
	public void notifyListeners(String data) {
		ALog.d(ALog.SUBSCRIPTION, "AirPurifierController$notifyListeners data " + data);
		AirPortInfo airPurifier = DataParser.parseAirPurifierEventData(data) ;
		FirmwarePortInfo firmwareEventDto = DataParser.parseFirmwareEventData(data);
		if( subscriptionEventListeners == null ) return;
		
		int listeners = subscriptionEventListeners.size() ;
		for( int index = 0 ; index < listeners ; index ++ ) {
			if(airPurifier != null) {
				subscriptionEventListeners.get(index).airPurifierEventReceived(airPurifier);
				continue;
			} 
			if(firmwareEventDto != null) {
				subscriptionEventListeners.get(index).firmwareEventReceived(firmwareEventDto);
			}
		}
	}
	
	public void subscribeToAllEvents(PurAirDevice purifier) {
		ALog.i(ALog.AIRPURIFIER_CONTROLER, "Subscribe to all events for purifier: " + purifier) ;
		subscriptionManager.subscribeToPurifierEvents(purifier);
		subscriptionManager.subscribeToFirmwareEvents(purifier);
	}

	public void unSubscribeFromAllEvents(PurAirDevice purifier) {
		ALog.i(ALog.AIRPURIFIER_CONTROLER, "UnSubscribe from all events from purifier: " + purifier) ;
		subscriptionManager.unSubscribeFromPurifierEvents(purifier);
		subscriptionManager.unSubscribeFromFirmwareEvents(purifier);
	}
	
	@Override
	public void onSubscribeEventOccurred(String encryptedData) {
		PurAirDevice purifier = PurifierManager.getInstance().getCurrentPurifier();
		if (purifier == null) return;
		
		String decryptedData = new DISecurity(null).decryptData(encryptedData, purifier) ;
		if (decryptedData == null ) return;

		notifyListeners(decryptedData) ;
	}
}