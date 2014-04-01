package com.philips.cl.di.dev.pa.controller;


import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.interfaces.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.network.TaskPutDeviceDetails;
import com.philips.cl.di.dev.pa.utils.DataParser;
import com.philips.cl.di.dev.pa.utils.JSONBuilder;
import com.philips.cl.di.dev.pa.utils.Utils;

/**
 *
 * This class acts as an interface between the server and the UI component
 * All requests from user interface will be through this interface.
 * This class will take the request from User Interface, sends it to server class, gets the response
 * parses it and sends it back to the user interface.
 *
 */
public class AirPurifierController implements ServerResponseListener
{
	
	/** The sensor data handler. */
	private AirPurifierEventListener airPurifierEventListener;
	
	
	/** The tag. */
	private String TAG = getClass().getName();
	
	/** request type **/
	private int requestType ;
	
	/** The handler. */
	final Handler handler = new Handler();	
	/**  **/
	private Context context ;
	
	
	
	/**
	 * The Enum DeviceMode.
	 */
	public enum DeviceMode {
		/** The auto. */
		auto,
		/** The manual. */
		manual, 
		
		silent,
		
		turbo,

		/** The test. */
		one,
		
		two,
		
		three
	};

	/**
	 * The Enum RingColor.
	 */
	public enum RingColor {

		/** The off. */
		off(0),
		/** The hazardous. */
		hazardous(1),
		/** The very_unhealthy. */
		very_unhealthy(2),
		/** The unhealthy. */
		unhealthy(3),
		/** The unhealthy_for_sensitive. */
		unhealthy_for_sensitive(4),
		/** The moderate. */
		moderate(5),
		/** The good. */
		good(6);
	 
 		/** The value. */
 		private int value;
		 
		 /**
 		 * Instantiates a new ring color.
 		 *
 		 * @param value the value
 		 */
 		private RingColor(int value) {
		   this.value = value;
		 }
		 
		 /**
 		 * Gets the value.
 		 *
 		 * @return the value
 		 */
 		public int getValue() {
		   return value;
		 }
	}
	
	/**
	 * Instantiates a new air purifier controller.
	 *
	 * @param sensorDataHandler the sensor data handler
	 * @param context the context
	 */
	public AirPurifierController(AirPurifierEventListener sensorDataListener,Context context) {
		this.airPurifierEventListener = sensorDataListener ;
		this.context = context ;
	}
	
	public AirPurifierController(Context context) {
		this.context = context ;
	}
	
	public void setDeviceDetailsLocally(String key, String value )
	{
		String dataToUpload = JSONBuilder.getDICommBuilder(key,value) ;
		startServerTask(dataToUpload) ;
	}	
	public void setDeviceDetailsRemotely(String key, String value) {
		String eventData = JSONBuilder.getPublishEventBuilder(key, value) ;
		// Publish events
		CPPController.getInstance(context).publishEvent(eventData,AppConstants.DI_COMM_REQUEST, AppConstants.PUT_PROPS, "", "", 20, 120) ;
	}

	/**
	 * Method to call the Server task
	 * @param nameValuePair
	 */
	private void startServerTask(String dataToUpload) {
		TaskPutDeviceDetails statusUpdateTask = new TaskPutDeviceDetails(dataToUpload,String.format(AppConstants.URL_CURRENT, Utils.getIPAddress(context)),this) ;
		Thread statusUpdateTaskThread = new Thread(statusUpdateTask) ;
		statusUpdateTaskThread.start() ;
	}

	/**
	 * (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.interfaces.ServerResponseListener#receiveServerResponse(int, java.lang.String)
	 * This is a call back to async task.
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		// TODO 
		Log.i(TAG, "Response: "+responseData);
		switch (responseCode) {
		case HttpsURLConnection.HTTP_OK:	
			if( requestType == AppConstants.GET_SENSOR_DATA_REQUEST_TYPE ) {
				parseSensorData(responseData) ;
			}
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
		Log.i(TAG, "parse sensor data");
		AirPurifierEventDto airPurifierEvent = null ;
		if( dataToParse != null) {
			airPurifierEvent = new DataParser(dataToParse).parseAirPurifierEventData() ;
		}
		if ( airPurifierEventListener != null)
			airPurifierEventListener.airPurifierEventReceived(airPurifierEvent) ;
	}
	
	public void getFilterStatus() {
		Log.i(TAG, "Get Filter Status") ;
//		TaskGetFilterStatus filterStatusTask = new TaskGetFilterStatus();
//		executeTask(filterStatusTask, AppConstants.URL_FILTER_STATUS);
	}	
}