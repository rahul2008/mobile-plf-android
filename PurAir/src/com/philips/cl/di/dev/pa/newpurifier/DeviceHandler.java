package com.philips.cl.di.dev.pa.newpurifier;

import java.net.HttpURLConnection;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.os.Handler;

import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PURIFIER_EVENT;
import com.philips.cl.di.dev.pa.purifier.DeviceConnection;
import com.philips.cl.di.dev.pa.purifier.RoutingStrategy;
import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

@SuppressLint("HandlerLeak")
public class DeviceHandler implements ServerResponseListener {
	
	private SubscriptionEventListener mListener;
	private PURIFIER_EVENT mPurifierEvent ;
	private Thread statusUpdateTaskThread ;
	private PurAirDevice purifier ;
	
	private Hashtable<String, String> deviceDetailsTable ;
	private boolean stop;
	private String response ;
	
	public DeviceHandler(SubscriptionEventListener listener) {
		mListener = listener ;
		deviceDetailsTable = new Hashtable<String, String>() ;
	}
	
	public DeviceHandler(SubscriptionEventListener listener, PURIFIER_EVENT purifierEvent) {
		this(listener);
		mPurifierEvent = purifierEvent ;
	}
	
	public void setPurifierEvent(PURIFIER_EVENT purifierEvent) {
		mPurifierEvent = purifierEvent ;
	}

	public synchronized void setPurifierDetails(String key, String value, PurAirDevice purifier) {
		if (purifier == null) return;
		this.purifier = purifier;
		stop = false;
		deviceDetailsTable.put(key, value) ;
		ALog.i(ALog.DEVICEHANDLER, "Setting \"" + key + "\" to " + value + " for purifier: " + purifier.getName());
		
		if( statusUpdateTaskThread == null || !statusUpdateTaskThread.isAlive()) {
			startUpdateTask(purifier) ;
		}		
	}
	
	private void startUpdateTask(PurAirDevice purifier) {
		ALog.i(ALog.DEVICEHANDLER, "Start update task") ;
		StatusUpdateThread status = new StatusUpdateThread() ;
		statusUpdateTaskThread = new Thread(status) ;
		statusUpdateTaskThread.start() ;
	}
	
	public boolean isDeviceThreadRunning() {
		if(statusUpdateTaskThread == null || !statusUpdateTaskThread.isAlive()) 
			return false ;
		return true;
	}
	
	public void stopDeviceThread() {
		if(statusUpdateTaskThread == null || !statusUpdateTaskThread.isAlive()) return ;
		stop = true ;
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String encryptedData, String fromIp) {
		ALog.i(ALog.DEVICEHANDLER, "Receive response");
		notifyListener(responseCode,encryptedData, fromIp) ;
	}

	private void notifyListener(int responseCode, String encryptedData, String fromIp) {
		ALog.i(ALog.DEVICEHANDLER, "Response Code: "+responseCode) ;
		if (mListener == null) return;
		if(responseCode != HttpURLConnection.HTTP_OK) {
			mListener.onLocalEventLost(mPurifierEvent) ; 
		}
		else {
			mListener.onLocalEventReceived(encryptedData, fromIp) ;
		}
	}
	
	private class StatusUpdateThread implements Runnable {
		Hashtable<String, String> airPortDetailsTable = new Hashtable<String, String>();
		private DeviceConnection connection ;
		
		public void run() {
			sendRequest() ;
		}
		private void sendRequest() {
			while(deviceDetailsTable.size() != 0 && !stop) {	
				this.airPortDetailsTable.putAll(deviceDetailsTable) ;
				deviceDetailsTable.clear() ;
				connection = RoutingStrategy.getConnection(purifier, this.airPortDetailsTable) ;
				if( connection != null) {
					response = connection.setPurifierDetails() ;
				}
			}
			messageHandler.sendEmptyMessage(0) ;
		}
	}
	
	private void notifyListener() {
		if( stop == true ) return ;
		if( response != null && !response.isEmpty() ) {
			if( purifier.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) {
				mListener.onLocalEventReceived(response, purifier.getIpAddress()) ;
			}
			else if( purifier.getConnectionState() == ConnectionState.CONNECTED_REMOTELY) {
				mListener.onRemoteEventReceived(response, purifier.getEui64()) ;
			}
		}
		else {
			mListener.onLocalEventLost(mPurifierEvent) ;
		}
	}
	
	private Handler messageHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if( msg.what == 0)
				notifyListener() ;
		};
	};	
}