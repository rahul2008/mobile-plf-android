package com.philips.cl.di.dev.pa.controller;

import java.net.HttpURLConnection;

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
import com.philips.icpinterface.CallbackHandler;
import com.philips.icpinterface.EventPublisher;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.data.Commands;
/**
 * This Class will get the Sensor Data every specified interval of time
 * Any number of listeners can register to this class
 * Once this gets the response from the Air Purifier, it sends it to all the listeners
 * @author 310124914
 *
 */
public class SensorDataController implements ServerResponseListener, CallbackHandler {
	
	private static final String TAG = "SensorDataController" ;
	
	private SensorEventListener sensorListener ;
	
	private Context context ;
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
	
	/** Post the get sensor data every specified interval **/
	private final Runnable getDeviceDataFromCPP = new Runnable() {
		@Override
		public void run() {
			publishEvent() ;
			cppHandler.postDelayed(this, AppConstants.UPDATE_INTERVAL_CPP);
		}
	};
	
	private void publishEvent() {
		EventPublisher eventPublisher = new EventPublisher(this);		
		eventPublisher.setEventInformation("DICOMM-REQUEST", "GETPROPS", "", "", 20, 120);
		//eventPublisher.setEventData(JSONBuilder.getPublishEventBuilder("", ));
		eventPublisher.setTargets(new String[] {"1c5a6bfffe634141"});
		eventPublisher.setEventCommand(Commands.PUBLISH_EVENT);
		eventPublisher.executeCommand();
	}
	
	/**
	 * Singleton constructor
	 */
	public SensorDataController(SensorEventListener sensorListener, Context context) {
		this.sensorListener = sensorListener ;
		this.context = context ;
	}
	
	/**
	 * Callback from AyncTask.
	 * This will in turn calls all the listeners
	 */
	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		if ( responseCode == HttpURLConnection.HTTP_OK) {	
				sensorListener.sensorDataReceived(new DataParser(responseData).parseAirPurifierEventData()) ;
		}
	}
	
	/**
	 * Starts the polling for the server
	 * At first instance, it immediately polls the server, subsequently it polls in the specified interval.
	 */
	public void startPolling() {
		Log.i(TAG, "Startpolling") ;
		handler.postDelayed(getSensorDataRunnable, 0);
	}
	
	/**
	 * Stops the polling
	 */
	public void stopPolling() {
		handler.removeCallbacks(getSensorDataRunnable);
	}
	
	
	/**
	 * Starts the polling for the server
	 * At first instance, it immediately polls the server, subsequently it polls in the specified interval.
	 */
	public void startCPPPolling() {
		cppHandler.postDelayed(getDeviceDataFromCPP, 0);
	}
	
	/**
	 * Stops the polling
	 */
	public void stopCPPPolling() {
		cppHandler.removeCallbacks(getDeviceDataFromCPP);
	}
	
	
	/**
	 * Gets the sensor Data
	 * This method calls the AsyncTask
	 * @param url
	 */
	private void getSensorData(String url) {
		Log.i(TAG, "Get SensorData:"+url) ;
		AsyncTask<String, ?, ?> sensorDataTask = null;
		sensorDataTask = new TaskGetSensorData(this);
		sensorDataTask.execute(url);
	}

	@Override
	public void callback(int arg0, int arg1, ICPClient arg2) {
		
	}

	@Override
	public void setHandler(Handler arg0) {
		// TODO Auto-generated method stub
		
	}
		
}
