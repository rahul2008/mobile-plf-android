package com.philips.cl.di.dev.pa.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.network.Server;
import com.philips.cl.di.dev.pa.utils.Utils;



// TODO: Auto-generated Javadoc
/**
 * The Class AirPurifierController.
 */
public class AirPurifierController implements ServerResponseListener
{
	
	/** The ip address. */
	private String ipAddress ; 
	
	/** The is ignoring updates. */
	private boolean isIgnoringUpdates = false;
	
	/** The sensor data handler. */
	ServerResponseListener sensorDataHandler;
	
	/** The tag. */
	private String TAG = getClass().getName();
	
	/** The handler. */
	final Handler handler = new Handler();
	
	/** The get sensor data runnable. */
	final Runnable getSensorDataRunnable = new Runnable() {
		@Override
		public void run() {
			if (!isIgnoringUpdates) {
				// TODO
				// Parse
				//getSensorData(String.format(AppConstants.URL_CURRENT, ipAddress));
			}
			handler.postDelayed(this, AppConstants.UPDATE_INTERVAL);
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
	public AirPurifierController(ServerResponseListener sensorDataHandler,Context context) {
		this.sensorDataHandler = sensorDataHandler;
		
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
		Server statusUpdateTask = new Server(nameValuePair, this);
		statusUpdateTask.execute(String.format(AppConstants.URL, ipAddress));
	}

	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.interfaces.ServerResponseListener#receiveServerResponse(int, java.lang.String)
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		// TODO 
		Log.i(TAG, responseData);
	}

		
}