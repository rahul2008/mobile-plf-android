package com.philips.cl.di.dev.pa.controller;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.network.TaskGetSensorData;
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
	
	private static final String TAG = "SensorDataController" ;
	private static SensorDataController controller ;
	
	private static List<SensorEventListener> sensorEventListeners;
	
	private static Context context ;
	/** Handler for posting the runnable **/
	private final Handler handler = new Handler();
	
	/** Post the get sensor data every specified interval **/
	private final Runnable getSensorDataRunnable = new Runnable() {
		@Override
		public void run() {
			Log.i(TAG, "Get Sensor Data") ;
			getSensorData(String.format(AppConstants.URL_CURRENT, Utils.getIPAddress(context)));			
			handler.postDelayed(this, AppConstants.UPDATE_INTERVAL);
		}
	};
	
	/**
	 * Singleton constructor
	 */
	private SensorDataController() {
		if( sensorEventListeners == null ) {
			sensorEventListeners = new ArrayList<SensorEventListener>() ;
		}
	}
	
	/**
	 * Returns the Singleton instance of this class
	 * @param appContext
	 * @return
	 */
	public static SensorDataController getInstance( Context appContext) {
		context =  appContext;
		if( null == controller ) {
			controller = new SensorDataController() ;
		}
		return controller ;
	}
	
	/**
	 * Register the listener
	 * @param sensorEventListener
	 */
	public void registerListener(SensorEventListener sensorEventListener) {
		if( null != sensorEventListeners) {
			sensorEventListeners.add(sensorEventListener) ;
		}
	}
	
	/**
	 * UnRegister the listener
	 * @param sensorEventListener
	 */
	public void unRegisterListener(SensorEventListener sensorEventListener) {
		if( null != sensorEventListeners) {
			sensorEventListeners.remove(sensorEventListener) ;
		}
	}
	
	/**
	 * Remove all listeners
	 */
	public void removeAllListeners() {
		if( sensorEventListeners != null ) {
			for ( int index = 0 ; index < sensorEventListeners.size() ; index ++ ) {
				sensorEventListeners.remove(index) ;
			}
		}
	}
	
	/**
	 * Reset the controller
	 */
	public void resetController() {
		controller = null ;
	}

	/**
	 * Callback from AyncTask.
	 * This will in turn calls all the listeners
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		if ( responseCode == HttpURLConnection.HTTP_OK) {	
			for( int index = 0 ; index < sensorEventListeners.size() ; index ++ ) {
				sensorEventListeners.get(index).sensorDataReceived(new DataParser(responseData).parseAirPurifierEventData()) ;
			}
		}		
	}
	
	/**
	 * Starts the polling for the server
	 * At first instance, it immediately polls the server, subsequently it polls in the specified interval.
	 */
	public void startPolling() {
		handler.postDelayed(getSensorDataRunnable, 0);
	}
	
	/**
	 * Stops the polling
	 */
	public void stopPolling() {
		handler.removeCallbacks(getSensorDataRunnable);
	}
	
	/**
	 * Gets the sensor Data
	 * This method calls the AsyncTask
	 * @param url
	 */
	private void getSensorData(String url) {
		AsyncTask<String, ?, ?> sensorDataTask = null;		
		sensorDataTask = new TaskGetSensorData(this);
		sensorDataTask.execute(url);
	}
		
}
