package com.philips.cl.di.dev.pa.controller;

import java.util.ArrayList;



import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.interfaces.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.network.Server;
import com.philips.cl.di.dev.pa.utils.DataParser;
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
	
	/** The ip address. */
	private String ipAddress ;
	
	
	/** The sensor data handler. */
	private AirPurifierEventListener airPurifierEventListener;
	
	/** The tag. */
	private String TAG = getClass().getName();
	
	/** request type **/
	private int requestType ;
	
	/** server error message **/
	public String errorMessage ;
	
	/** The handler. */
	final Handler handler = new Handler();	
	/**  **/
	private Context context ;
	
	/** The get sensor data runnable. */
	final Runnable getSensorDataRunnable = new Runnable() {
		@Override
		public void run() {
		/**	getSensorData(String.format(URL_CURRENT, ipAddress));
			
			handler.postDelayed(this, AppConstants.UPDATE_INTERVAL);**/
		}
	};
	
	/**
	 * The Enum DeviceMode.
	 */
	public enum DeviceMode {
		/** The auto. */
		auto,
		/** The manual. */
		manual, 
		/** The test. */
		test
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
	public AirPurifierController(AirPurifierEventListener sensorDataListener,Context context,int requestType) {
		this.airPurifierEventListener = sensorDataListener;
		this.context = context ;
		this.requestType = requestType ;
		
		// Taking it from Shared Preferences
		this.ipAddress = Utils.getIPAddress(context);
		
		handler.postDelayed(getSensorDataRunnable, 0);
	}
	
	/**
	 * Sets the device power state.
	 *
	 * @param deviceState the new device power state
	 */
	public void setDevicePowerState(boolean deviceState) {
		Log.d(TAG, "Send device power state : " + deviceState);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);        
		nameValuePair.add(new BasicNameValuePair("status[power_mode]", deviceState ? "on" : "off"));
		nameValuePair.add(new BasicNameValuePair("status[machine_mode]", DeviceMode.manual.name()));
		if (deviceState) {
			nameValuePair.add(new BasicNameValuePair("status[motor_speed]", ""+2));
			nameValuePair.add(new BasicNameValuePair("status[ring_color]", ""+RingColor.good.getValue()));
		} else {
			nameValuePair.add(new BasicNameValuePair("status[motor_speed]", ""+0));
			nameValuePair.add(new BasicNameValuePair("status[ring_color]", ""+RingColor.off.getValue()));
		}
		
		startServerTask(nameValuePair) ;
	}
	
	
	/**
	 * Sets the device motor speed.
	 *
	 * @param deviceState the new device power state
	 */
	public void setDeviceMotorSpeed(int motorSpeed) {
		Log.d(TAG, "Send device motor speed : " + motorSpeed);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
		nameValuePair.add(new BasicNameValuePair("status[machine_mode]", DeviceMode.manual.name()));
		nameValuePair.add(new BasicNameValuePair("status[motor_speed]", ""+motorSpeed));
		
		startServerTask(nameValuePair) ;
	}
	
	/**
	 * Sets the device mode (auto or manual)
	 *
	 * @param machinemode/devicemode
	 */
	public void setDeviceMode(DeviceMode deviceMode) {
		Log.d(TAG, "Send device mode : " + deviceMode.name());
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);        
		nameValuePair.add(new BasicNameValuePair("status[machine_mode]", deviceMode.name())); 
		
		startServerTask(nameValuePair) ;
	}

	/**
	 * Method to call the Server task
	 * @param nameValuePair
	 */
	private void startServerTask(List<NameValuePair> nameValuePair) {
		Server statusUpdateTask = new Server(nameValuePair, this);
		statusUpdateTask.execute(String.format(AppConstants.URL, ipAddress));
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
			//Handle the error scenarios
		case HttpsURLConnection.HTTP_BAD_REQUEST:
		case HttpsURLConnection.HTTP_NOT_FOUND:
		default:
			errorMessage = context.getResources().getString(R.string.network_error) ;
			airPurifierEventListener.sensorDataReceived(null) ;
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
		airPurifierEventListener.sensorDataReceived(airPurifierEvent) ;
	}
}