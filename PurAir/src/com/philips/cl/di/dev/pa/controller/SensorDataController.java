package com.philips.cl.di.dev.pa.controller;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.network.TaskGetSensorData;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.utils.DataParser;
import com.philips.cl.di.dev.pa.utils.Utils;

/**
 * This Class will get the Sensor Data every specified interval of time
 * Any number of listeners can register to this class
 * Once this gets the response from the Air Purifier, it sends it to all the listeners
 * @author 310124914
 *
 */
public class SensorDataController implements ServerResponseListener {
	
	private List<SensorEventListener> listeners ;
	
	private static Context context ;
	/** Handler for posting the runnable **/
	private final Handler handler = new Handler();
	
	private final Handler cppHandler = new Handler() ;
	
	/** Post the get sensor data every specified interval **/
	private final Runnable getSensorDataRunnable = new Runnable() {
		@Override
		public void run() {
			getSensorData(String.format(AppConstants.URL_CURRENT, Utils.getIPAddress(context)));
			handler.postDelayed(this, AppConstants.UPDATE_INTERVAL);
		}
	};
	
	public void addListener(SensorEventListener sensorEventListener) {
		listeners.add(sensorEventListener) ;
	}
	
	public void removeListener(SensorEventListener sensorEventListener) {
		listeners.remove(sensorEventListener) ;
	}
	
	/** Post the get sensor data every specified interval **/
	private final Runnable getDeviceDataFromCPP = new Runnable() {
		@Override
		public void run() {
				CPPController.getInstance(context).publishEvent(AppConstants.GETPROPS_ACTION, AppConstants.DI_COMM_REQUEST, AppConstants.GET_PROPS, Utils.getAirPurifierID(context), "", 20, 120) ;
				cppHandler.postDelayed(this, AppConstants.UPDATE_INTERVAL_CPP);
		}
	};
	
	
	private static SensorDataController sensorDataController ;
	private SensorDataController() {
		listeners = new ArrayList<SensorEventListener>() ;
	}
	
	/**
	 * Singleton constructor
	 */
	public static SensorDataController getInstance(Context appContext) {
		context = appContext ;
		if ( null == sensorDataController ) {
			sensorDataController = new SensorDataController() ;
		}
		return sensorDataController ;
	}
	
	/**
	 * Callback from AyncTask.
	 * This will in turn calls all the listeners
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		if ( responseCode == HttpURLConnection.HTTP_OK) {
				AirPurifierEventDto airpurifierEventDto = null ;
				responseData = new DISecurity(null).decryptData(responseData, AppConstants.DEVICEID) ;
				if( responseData != null ) {
					airpurifierEventDto = new DataParser(responseData).parseAirPurifierEventData() ;
				}
				//sensorListener.sensorDataReceived(airpurifierEventDto) ;
				
				for( int index = 0 ; index < listeners.size() ; index ++ ) {
					listeners.get(index).sensorDataReceived(airpurifierEventDto) ;
				}
		}
		else {
			for( int index = 0 ; index < listeners.size() ; index ++ ) {
				listeners.get(index).sensorDataReceived(null) ;
			}
		}
	}
	
	/**
	 * Starts the polling for the server
	 * At first instance, it immediately polls the server, subsequently it polls in the specified interval.
	 */
	public void startPolling() {
		Log.i("polling", "startpolling") ;
		handler.postDelayed(getSensorDataRunnable, 0);
	}
	
	/**
	 * Stops the polling
	 */
	public void stopPolling() {
		Log.i("polling", "stoppolling") ;
		handler.removeCallbacks(getSensorDataRunnable);
	}
	
	
	/**
	 * Starts the polling for the server
	 * At first instance, it immediately polls the server, subsequently it polls in the specified interval.
	 */
	public void startCPPPolling() {
		Log.i("polling", "startCPPpolling") ;
		cppHandler.postDelayed(getDeviceDataFromCPP, 0);
	}
	
	/**
	 * Stops the polling
	 */
	public void stopCPPPolling() {
		Log.i("polling", "stopCPPpolling") ;
		cppHandler.removeCallbacks(getDeviceDataFromCPP);
	}
	
	
	/**
	 * Gets the sensor Data
	 * This method calls the AsyncTask
	 * @param url
	 */
	private void getSensorData(String url) {
		Log.i("Sensor", "Get SensorData:"+url) ;
		AsyncTask<String, ?, ?> sensorDataTask = null;
		sensorDataTask = new TaskGetSensorData(this);
		sensorDataTask.execute(url);
	}
	
	public void removeAllListeners() {
		if(listeners != null) {
			listeners.clear() ;
		}
	}
}
