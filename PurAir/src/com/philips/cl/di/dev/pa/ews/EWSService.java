package com.philips.cl.di.dev.pa.ews;

import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.DeviceDto;
import com.philips.cl.di.dev.pa.dto.DeviceWifiDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.security.KeyDecryptListener;
import com.philips.cl.di.dev.pa.utils.ALog;


public class EWSService extends BroadcastReceiver 
	implements KeyDecryptListener, EWSTaskListener, Runnable {

	public static final String WIFI_URI = "http://192.168.1.1/di/v1/products/0/wifi";
	public static final String DEVICE_URI = "http://192.168.1.1/di/v1/products/1/device";
	public static final String SECURITY_URI = "http://192.168.1.1/di/v1/products/0/security";
	public static final String WIFI_UI_URI = "http://192.168.1.1/di/v1/products/0/wifiui";

	private EWSListener listener ;
	private String homeSSID ;

	public static final int DEVICE_GET = 1;
	public static final int DEVICE_PUT = 2;
	public static final int WIFI_PUT = 3;
	public static final int WIFI_GET = 4;


	private int taskType ;
	private String password;
	private String deviceName ;

	private boolean isRegistered ;
	
	private int errorCodeStep2 = EWSListener.ERROR_CODE_PHILIPS_SETUP_NOT_FOUND  ;
	
	private int errorCodeStep3 = EWSListener.ERROR_CODE_COULDNOT_SEND_DATA_TO_DEVICE ;
	/**
	 * 
	 */
	private String devKey;
	/**
	 * 
	 */
	private String cppId = "";
	/**
	 * 
	 * @param listener
	 * @param context
	 * @param homeSSID
	 * @param password
	 */
	public EWSService(EWSListener listener, String homeSSID, String password) {
		this.listener = listener ;
		this.homeSSID = homeSSID ;
		this.password = password ;

	}

	private IntentFilter filter = new IntentFilter();

	public void registerListener() {
		filter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		filter.addAction(WifiManager.EXTRA_WIFI_STATE) ;
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		if( !isRegistered ) {
			PurAirApplication.getAppContext().registerReceiver(this, filter);
			isRegistered = true ;
		}
	}

	public void startScanForDeviceAp() {

		registerListener() ;

		if(stop) {
			stop = false ;
			new Thread(this).start() ;
		}

	}

	public void unRegisterListener() {
		if( isRegistered ) {
			
			stop = true ;
			PurAirApplication.getAppContext().unregisterReceiver(this) ;
			isRegistered = false ;
		}
	}

	
	@Override
	public void onReceive(Context context, Intent intent) {
		ALog.i(ALog.EWS, "On Receive:"+intent.getAction()) ;
		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			
			if (netInfo.getState() == android.net.NetworkInfo.State.CONNECTED) {
				ALog.i(ALog.EWS, "Connected in  onReceive= "+ intent.getAction());
				
				String ssid = EWSWifiManager.getSsidOfConnectedNetwork();
				if (ssid == null) {
					ALog.i(ALog.EWS, "Failed to get ssid of connected network");
					return;
				}
				
				if (ssid.contains(EWSWifiManager.DEVICE_SSID)) {
					ALog.i(ALog.EWS, "Connected to AirPurifier - Ssid= "+ ssid);
					if (homeSSID != null) {
						errorCodeStep2 = EWSListener.ERROR_CODE_COULDNOT_RECEIVE_DATA_FROM_DEVICE ;
						listener.onDeviceAPMode() ;
						initializeKey() ;
						
						isOpenNetwork = EWSWifiManager.isOpenNetwork(homeSSID);
					} else {
						// Connected to device, but homeSSID is null - Should never happen
						// TODO add an error case for this?
					}
					return;
				}
				
				ALog.i(ALog.EWS,"Connected to HomeNetwork - Ssid= "+ ssid);
				if (homeSSID == null ) {
					listener.foundHomeNetwork() ;
				}

			} else if (netInfo.getState() == NetworkInfo.State.DISCONNECTED ||
					netInfo.getState() == NetworkInfo.State.DISCONNECTING) {
				ALog.i(ALog.EWS, "Network State: "+ netInfo.getState()) ;
				listener.onWifiDisabled() ;
			}
		}
		
	}
	
	private boolean isOpenNetwork;
	
	public boolean isNoPasswordSSID() {
		return isOpenNetwork;
	}

	public void setPassword(String password) {
		this.password = password ;
	}

	public void setSSID(String ssid) {
		this.homeSSID = ssid ;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName ;
	}

	private void initializeKey() {
		ALog.i(ALog.EWS, "initiliazekey") ;
		DISecurity di = new DISecurity(this) ;
		di.initializeExchangeKeyCounter(cppId);
		di.exchangeKey(SECURITY_URI, cppId) ;
	}


	public void putWifiDetails() {
		ALog.i(ALog.EWS, "putWifiDetails");
		sendNetworkDetailsTimer.start() ;
		taskType = WIFI_PUT ;

		EWSTasks task = new EWSTasks(taskType,getWifiPortJson(),"PUT",this) ;
		task.execute(WIFI_URI);
	}

	public void putDeviceDetails() {
		ALog.i(ALog.EWS, "putDeviceDetails");
		taskType = DEVICE_PUT ;

		EWSTasks task = new EWSTasks(taskType,getDevicePortJson(),"PUT",this) ;
		task.execute(DEVICE_URI);
	}

	private void getDeviceDetails() {
		ALog.i(ALog.EWS,"device details") ;
		taskType = DEVICE_GET ;
		EWSTasks task = new EWSTasks(taskType, this) ;
		task.execute(DEVICE_URI);
	}



	private String getWifiPortJson() {
		ALog.i(ALog.EWS, "getWifiPortJson");
		JSONObject holder = new JSONObject();
		try {
			holder.put("ssid", homeSSID);
			holder.put("password", password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String js = holder.toString();

		String encryptedData = new DISecurity(null).encryptData(js, AppConstants.DEVICEID);

		return encryptedData ;
	}

	private String getDevicePortJson() {
		ALog.i(ALog.EWS, "getDevicePortJson");
		JSONObject holder = new JSONObject();
		try {
			holder.put("name", deviceName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String js = holder.toString();

		String encryptedData = new DISecurity(null).encryptData(js, AppConstants.DEVICEID);

		return encryptedData ;
	}


	public void connectToDeviceAP() {
		ALog.i(ALog.EWS, "connecttoDevice AP");
		WifiManager wifiManager = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		wifiManager.disconnect();
		EWSWifiManager.connectToPhilipsSetup();
		startScanForDeviceAp() ;
		deviceSSIDTimer.start() ;
	}
	
	@Override
	public void keyDecrypt(String key, String deviceId) {
		ALog.i(ALog.EWS, "Key: "+key) ;
		if ( key != null ) {
			setDevKey(key);
			getDeviceDetails() ;
		}
	}

	private void getWifiDetails() {
		ALog.i(ALog.EWS, "gettWifiDetails");
		taskType = WIFI_GET ;

		EWSTasks task = new EWSTasks(taskType,this) ;
		task.execute(WIFI_URI);
	}

	@Override
	public void onTaskCompleted(int responseCode, String response) {
		stop = true ;
		ALog.i(ALog.EWS, "onTaskCompleted:"+responseCode) ;
		switch (responseCode) {
		case HttpURLConnection.HTTP_OK:
			if( taskType == DEVICE_GET ) {
				String decryptedResponse = new DISecurity(null).decryptData(response, AppConstants.DEVICEID);
				if( decryptedResponse != null ) {
					ALog.i(ALog.EWS,decryptedResponse) ;
					storeDeviceDetails(decryptedResponse) ;
					getWifiDetails() ;
				}				
			}
			else if(taskType == WIFI_GET) {
				String decryptedResponse = new DISecurity(null).decryptData(response, AppConstants.DEVICEID);
				if( decryptedResponse != null ) {
					ALog.i(ALog.EWS,decryptedResponse) ;
					storeDeviceWifiDetails(decryptedResponse);
					deviceSSIDTimer.cancel() ;
					listener.onHandShakeWithDevice() ;
				}	
			}
			else if(taskType == DEVICE_PUT ) {
				String decryptedResponse = new DISecurity(null).decryptData(response, AppConstants.DEVICEID);
				ALog.i(ALog.EWS, decryptedResponse) ;
				if( decryptedResponse != null ) {
					storeDeviceDetails(decryptedResponse) ;
					listener.onHandShakeWithDevice() ;
				}	
			}
			else if(taskType == WIFI_PUT ) {
				String decryptedResponse = new DISecurity(null).decryptData(response, AppConstants.DEVICEID);
				ALog.i(ALog.EWS, decryptedResponse) ;
				if( decryptedResponse != null ) {
					EWSWifiManager.connectToHomeNetwork(homeSSID);
					listener.onDeviceConnectToHomeNetwork() ;
					errorCodeStep3 = EWSListener.ERROR_CODE_COULDNOT_FIND_DEVICE ;
				}
			}
			break;
		case HttpURLConnection.HTTP_BAD_REQUEST:
			if(taskType == WIFI_PUT ) {
				if( response != null && response.length() > 0 ) {
					if( response.contains(AppConstants.INVALID_WIFI_SETTINGS)) {
						stop = true ;
						stopNetworkDetailsTimer() ;
						listener.onErrorOccurred(EWSListener.ERROR_CODE_INVALID_PASSWORD) ;				
					}
				}
			}
			break;
		default: 
				stop = true ;
				stopNetworkDetailsTimer() ;
				stopSSIDTimer() ;
				if( taskType == WIFI_GET || taskType == DEVICE_GET) {
					listener.onErrorOccurred(EWSListener.ERROR_CODE_COULDNOT_RECEIVE_DATA_FROM_DEVICE) ;
				}
				else if( taskType == WIFI_PUT || taskType == DEVICE_PUT) {
					listener.onErrorOccurred(EWSListener.ERROR_CODE_COULDNOT_SEND_DATA_TO_DEVICE) ;
				}
			 	break;			
		}
	}

	private void storeDeviceDetails(String data) {
		Gson gson = new GsonBuilder().create() ;
		DeviceDto deviceDto = gson.fromJson(data, DeviceDto.class) ;
		SessionDto.getInstance().setDeviceDto(deviceDto) ;
	}

	private void storeDeviceWifiDetails(String data) {
		Gson gson = new GsonBuilder().create() ;
		DeviceWifiDto deviceWifiDto = gson.fromJson(data, DeviceWifiDto.class) ;
		
		SessionDto.getInstance().setDeviceWifiDto(deviceWifiDto) ;
		
		if (deviceWifiDto != null) {
			cppId = deviceWifiDto.getCppid();
		}
		
	}

	private CountDownTimer deviceSSIDTimer = new CountDownTimer(30000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			stop = true ;
			unRegisterListener() ;
			listener.onErrorOccurred(errorCodeStep2) ;
		}
	};
	
	private CountDownTimer sendNetworkDetailsTimer = new CountDownTimer(90000, 1000) {
	
		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			stop = true ;
			unRegisterListener() ;
			listener.onErrorOccurred(errorCodeStep3) ;
		}
	};
	
	public void stopNetworkDetailsTimer() {
		if( sendNetworkDetailsTimer != null)
			sendNetworkDetailsTimer.cancel() ;
	}
	
	public void stopSSIDTimer() {
		if(deviceSSIDTimer != null ) {
			deviceSSIDTimer.cancel() ;
		}
	}

	private boolean stop = true;
	private int totalTime = 10 * 1000 ;
	
	@Override
	public void run() {
		int timeElapsed = 0 ;
		while(!stop) {
			try {
				Thread.sleep(1000) ;
				timeElapsed = timeElapsed + 1000 ;
				if( timeElapsed == totalTime) {
					timeElapsed = 0 ;
					// StartScan
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setDevKey(String devKey) {
		this.devKey = devKey;
	}
	
	public String getDevKey() {
		return devKey;
	}
}
