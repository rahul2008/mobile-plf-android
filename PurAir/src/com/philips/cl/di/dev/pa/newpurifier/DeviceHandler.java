package com.philips.cl.di.dev.pa.newpurifier;

import java.net.HttpURLConnection;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import com.philips.cl.di.dev.pa.purifier.RoutingStrategy;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dicomm.communication.Request;
import com.philips.cl.di.dicomm.communication.Response;
import com.philips.cl.di.dicomm.communication.ResponseHandler;
import com.philips.cl.di.dicomm.communication.Error;

@SuppressLint("HandlerLeak")
public class DeviceHandler implements ServerResponseListener {
	
	private ResponseHandler mListener;
	private Error mPurifierEventError ;
	private Thread statusUpdateTaskThread ;
	private NetworkNode mNetworkNode ;
	
	private Hashtable<String, String> deviceDetailsTable ;
	private boolean stop;
	private String response ;
	
	public DeviceHandler(ResponseHandler listener) {
		mListener = listener ;
		deviceDetailsTable = new Hashtable<String, String>() ;
	}
	
	public DeviceHandler(ResponseHandler listener, Error purifierEvent) {
		this(listener);
		mPurifierEventError = purifierEvent ;
	}
	
	public void setPurifierEvent(Error purifierEvent) {
		mPurifierEventError = purifierEvent ;
	}

	public synchronized void setPurifierDetails(String key, String value, NetworkNode networkNode) {
		if (networkNode == null) return;
		this.mNetworkNode = networkNode;
		stop = false;
		deviceDetailsTable.put(key, value) ;
		ALog.i(ALog.DEVICEHANDLER, "Setting \"" + key + "\" to " + value + " for appliance: " + networkNode.getName());
		
		if( statusUpdateTaskThread == null || !statusUpdateTaskThread.isAlive()) {
			startUpdateTask(networkNode) ;
		}		
	}
	
	private void startUpdateTask(NetworkNode networkNode) {
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
	public void receiveServerResponse(int responseCode, String responseData, String type, String areaId) {/**NOP*/}
	
	@Override
	public void receiveServerResponse(int responseCode, String encryptedData, String fromIp) {
		ALog.i(ALog.DEVICEHANDLER, "Receive response");
		notifyListener(responseCode,encryptedData, fromIp) ;
	}

	private void notifyListener(int responseCode, String encryptedData, String fromIp) {
		ALog.i(ALog.DEVICEHANDLER, "Response Code: "+responseCode) ;
		if (mListener == null) return;
		if(responseCode != HttpURLConnection.HTTP_OK) {
			mListener.onError(mPurifierEventError) ; 
		}
		else {
			mListener.onSuccess(encryptedData) ;
		}
	}
	
	private class StatusUpdateThread implements Runnable {
		Hashtable<String, String> airPortDetailsTable = new Hashtable<String, String>();
		private Request connection ;
		
		public void run() {
			sendRequest() ;
		}
		private void sendRequest() {
			while(deviceDetailsTable.size() != 0 && !stop) {	
				this.airPortDetailsTable.putAll(deviceDetailsTable) ;
				deviceDetailsTable.clear() ;
				connection = RoutingStrategy.getConnection(mNetworkNode, this.airPortDetailsTable) ;
				if( connection != null) {
					Response responseObj = connection.execute();
					response = responseObj.getResponseMessage() ;
				}
			}
			messageHandler.sendEmptyMessage(0) ;
		}
	}
	
	private void notifyListener() {
		if( stop == true ) return ;
		if( response != null && !response.isEmpty() ) {
			DISecurity diSecurity = new DISecurity(null);
			
			if( mNetworkNode.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) {
				String decryptedData = diSecurity.decryptData(response, mNetworkNode) ;
				if (decryptedData == null ) {
					ALog.d(ALog.DEVICEHANDLER, "Unable to decrypt data for : " + mNetworkNode.getIpAddress());
					return;
				}
				mListener.onSuccess(decryptedData) ;
			}
			else if( mNetworkNode.getConnectionState() == ConnectionState.CONNECTED_REMOTELY) {
				mListener.onSuccess(response) ;
			}
		}
		else {
			mListener.onError(mPurifierEventError) ;
		}
	}
	
	private Handler messageHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(android.os.Message msg) {
			if( msg.what == 0)
				notifyListener() ;
		};
	};	
}