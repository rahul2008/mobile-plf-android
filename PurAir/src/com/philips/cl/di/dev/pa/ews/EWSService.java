package com.philips.cl.di.dev.pa.ews;

import java.net.HttpURLConnection;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.dto.DeviceDto;
import com.philips.cl.di.dev.pa.dto.DeviceWifiDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.pureairui.PurAirApplication;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.security.KeyDecryptListener;


public class EWSService extends BroadcastReceiver 
	implements KeyDecryptListener, EWSTaskListener, Runnable {

	private static final String TAG = EWSService.class.getSimpleName() ;
	public static final String WIFI_URI = "http://192.168.1.1/di/v1/products/0/wifi";
	public static final String DEVICE_URI = "http://192.168.1.1/di/v1/products/1/device";
	public static final String SECURITY_URI = "http://192.168.1.1/di/v1/products/0/security";
	public static final String WIFI_UI_URI = "http://192.168.1.1/di/v1/products/0/wifiui";

	public static final String DEVICE_SSID = "PHILIPS Setup" ;
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

	private WifiManager mWifiManager;

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

		mWifiManager = (WifiManager) PurAirApplication.getAppContext()
				.getSystemService(Context.WIFI_SERVICE);

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
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		Log.i("ews", "On Receive:"+intent.getAction()) ;
		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			
			if (netInfo.getState() == android.net.NetworkInfo.State.CONNECTED) {
				Log.i(TAG, "Connected in  onReceive= "+ intent.getAction());
				
				String ssid = getSsidOfConnectedNetwork();
				if (ssid == null) {
					Log.i("ews", "Failed to get ssid of connected network");
					return;
				}
				
				if (ssid.contains(DEVICE_SSID)) {
					Log.i("ews", "Connected to AirPurifier - Ssid= "+ ssid);
					if (homeSSID != null) {
						errorCodeStep2 = EWSListener.ERROR_CODE_COULDNOT_RECEIVE_DATA_FROM_DEVICE ;
						listener.onDeviceAPMode() ;
						initializeKey() ;
						
						List<ScanResult> results = wifiManager.getScanResults();
						processWifiList(results);
					} else {
						// Connected to device, but homeSSID is null - Should never happen
						// TODO add an error case for this?
					}
					return;
				}
				
				Log.i("ews", "Connected to HomeNetwork - Ssid= "+ ssid);
				if (homeSSID == null ) {
					listener.foundHomeNetwork() ;
				}

			} else if (netInfo.getState() == NetworkInfo.State.DISCONNECTED ||
					netInfo.getState() == NetworkInfo.State.DISCONNECTING) {
				Log.i(TAG, "Network State: "+ netInfo.getState()) ;
				listener.onWifiDisabled() ;
			}
		}
		
	}
	
	private String getSsidOfConnectedNetwork() {
		if (mWifiManager == null) {
			mWifiManager = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		}

		WifiInfo connectedWifiNetwork = mWifiManager.getConnectionInfo();
		if (connectedWifiNetwork == null ) return null;
		
		String currentSsid = connectedWifiNetwork.getSSID();
		if (currentSsid == null) return null;
		
		Log.i("ews", "Ssid of connected network: " + currentSsid);
		currentSsid = currentSsid.replace("\"", "");
		return currentSsid;
	}
	
	private static boolean isOpenNetwork;
	private void processWifiList(List<ScanResult> results) {
		for (ScanResult scanResult : results) {
			if (scanResult.SSID != null && homeSSID != null && scanResult.SSID.equals(homeSSID)) {
				if (scanResult.capabilities.contains("WPA")) {
					isOpenNetwork = false;
				}else if(scanResult.capabilities.contains("WEP")) {
					isOpenNetwork = false;
					Log.i("WEPWPA", "scanResult " + scanResult);
				}else {
					isOpenNetwork = true;
				} 
			}
		}
	}
	
	public static boolean isNoPasswordSSID() {
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
		Log.i("ews", "initiliazekey") ;
		DISecurity di = new DISecurity(this) ;
		di.exchangeKey(SECURITY_URI, cppId) ;
	}


	public void putWifiDetails() {
		sendNetworkDetailsTimer.start() ;
		taskType = WIFI_PUT ;

		EWSTasks task = new EWSTasks(taskType,getWifiPortJson(),"PUT",this) ;
		task.execute(WIFI_URI);
	}

	public void putDeviceDetails() {
		taskType = DEVICE_PUT ;

		EWSTasks task = new EWSTasks(taskType,getDevicePortJson(),"PUT",this) ;
		task.execute(DEVICE_URI);
	}

	private void getDeviceDetails() {
		Log.i("ews", "device details") ;
		taskType = DEVICE_GET ;
		EWSTasks task = new EWSTasks(taskType, this) ;
		task.execute(DEVICE_URI);
	}



	private String getWifiPortJson() {
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
		WifiManager wifiManager = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		wifiManager.disconnect();
		connectToPhilipsSetup();
		startScanForDeviceAp() ;
		deviceSSIDTimer.start() ;
	}

	private boolean connectToHomeNetwork() {
		if (homeSSID == null) {
			Log.i("ews", "Failed to connect to Home network - ssid is null");
			return false;
		}
		int networkId = getConfiguredNetworkId(homeSSID);
		if (!connectToConfiguredNetwork(homeSSID)) {
			Log.i("ews", "Failed to connect to Home network");
			return false;
		}
		
		Log.i("ews", "Successfully connected to Home network");
		return true;
	}
	
	private boolean connectToPhilipsSetup() {
		int networkId = getConfiguredNetworkId(DEVICE_SSID);
		if (networkId == -1) {
			networkId = configureOpenNetwork(DEVICE_SSID);
		}
		
		if (!connectToConfiguredNetwork(DEVICE_SSID)) {
			Log.i("ews", "Failed to connect to Philips setup");
			return false;
		}
		
		Log.i("ews", "Successfully connected to Philips setup");
		return true;
	}
	
	/**
     * Connect to a configured network.
     * @return
     */
	public boolean connectToConfiguredNetwork(String ssid) {
		WifiManager wifiManager = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		WifiConfiguration config = getWifiConfiguration(ssid);
		if (config == null) {
			Log.i("ews", "Failed to connect to network - configuration null");
			return false;
		}
		
		int oldPri = config.priority;

		// Make it the highest priority.
		int newPri = getMaxPriority(wifiManager) + 1;
		if (newPri > 99999) {
			newPri = shiftPriorityAndSave(wifiManager);
			config = getWifiConfiguration(ssid);
			if (config == null) {
				return false;
			}
		}

		// Set highest priority to this configured network
		config.priority = newPri;
		int networkId = wifiManager.updateNetwork(config);
		if (networkId == -1) {
			return false;
		}

		// Do not disable others
		if (!wifiManager.enableNetwork(networkId, false)) {
			config.priority = oldPri;
			return false;
		}

		if (!wifiManager.saveConfiguration()) {
			config.priority = oldPri;
			return false;
		}

		// We have to retrieve the WifiConfiguration after save.
		config = getWifiConfiguration(ssid);
		if (config == null) {
			return false;
		}

		// Disable others, but do not save.
		// Just to force the WifiManager to connect to it.
		if (!wifiManager.enableNetwork(config.networkId, true)) {
			return false;
		}

		final boolean connect = wifiManager.reconnect();
		if (!connect) {
			Log.i("ews", "Failed to connect to network - reconnect failed");
			return false;
		}

		Log.i("ews", "Successfully connected to network");
		return true;
	}
    
    private static int getMaxPriority(final WifiManager wifiManager) {
        final List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        int pri = 0;
        for(final WifiConfiguration config : configurations) {
                if(config.priority > pri) {
                        pri = config.priority;
                }
        }
        return pri;
    }
    
    private static int shiftPriorityAndSave(final WifiManager wifiMgr) {
        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
        sortByPriority(configurations);
        final int size = configurations.size();
        for(int i = 0; i < size; i++) {
                final WifiConfiguration config = configurations.get(i);
                config.priority = i;
                wifiMgr.updateNetwork(config);
        }
        wifiMgr.saveConfiguration();
        return size;
    }
    
    private static void sortByPriority(final List<WifiConfiguration> configurations) {
        java.util.Collections.sort(configurations, new Comparator<WifiConfiguration>() {

                @Override
                public int compare(WifiConfiguration object1,
                                WifiConfiguration object2) {
                        return object1.priority - object2.priority;
                }
        });
    }
	
	/**
	 * @return -1 if not yet configured, else network id
	 */
	private int getConfiguredNetworkId(String ssid) {
		WifiConfiguration config = getWifiConfiguration(ssid);
		if (config == null) return -1; 
		
		return config.networkId;
	}
	
	private WifiConfiguration getWifiConfiguration(String ssid) {
		ssid = ssid.replace("\"", "");
		
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		List<WifiConfiguration> configuredNetworks = wifiMan.getConfiguredNetworks();
		
		for (WifiConfiguration config : configuredNetworks) {
			String configSsid = config.SSID.replace("\"", "");
			if (configSsid.equals(ssid)) {
				return config;			
			}
		}
		return null;
	}
	
	private int configureOpenNetwork(String ssid) {
		WifiConfiguration wfc = new WifiConfiguration();
		 
		wfc.SSID = "\"".concat(ssid).concat("\"");
		wfc.status = WifiConfiguration.Status.DISABLED;
		wfc.priority = 40;
		
		wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		wfc.allowedAuthAlgorithms.clear();
		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		
		WifiManager wifiMan = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		int networkId = wifiMan.addNetwork(wfc);
		
		if (networkId != -1) {
			Log.i("ews", "Successfully configured open network: " + ssid);
		} else {
			Log.i("ews", "Failed to configure open network: " + ssid);
		}
		return networkId;
	}

	@Override
	public void keyDecrypt(String key, String deviceId) {
		Log.i("ews", "Key: "+key) ;
		if ( key != null ) {
			setDevKey(key);
			getDeviceDetails() ;
		}
	}

	private void getWifiDetails() {
		taskType = WIFI_GET ;

		EWSTasks task = new EWSTasks(taskType,this) ;
		task.execute(WIFI_URI);
	}

	@Override
	public void onTaskCompleted(int responseCode, String response) {
		stop = true ;
		Log.i("ews", "onTaskCompleted:"+responseCode) ;
		switch (responseCode) {
		case HttpURLConnection.HTTP_OK:
			if( taskType == DEVICE_GET ) {
				String decryptedResponse = new DISecurity(null).decryptData(response, AppConstants.DEVICEID);
				if( decryptedResponse != null ) {
					Log.i("ews", decryptedResponse) ;
					storeDeviceDetails(decryptedResponse) ;
					getWifiDetails() ;
				}				
			}
			else if(taskType == WIFI_GET) {
				String decryptedResponse = new DISecurity(null).decryptData(response, AppConstants.DEVICEID);
				if( decryptedResponse != null ) {
					storeDeviceWifiDetails(decryptedResponse);
					deviceSSIDTimer.cancel() ;
					listener.onHandShakeWithDevice() ;
				}	
			}
			else if(taskType == DEVICE_PUT ) {
				String decryptedResponse = new DISecurity(null).decryptData(response, AppConstants.DEVICEID);
				Log.i("ews", decryptedResponse) ;
				if( decryptedResponse != null ) {
					storeDeviceDetails(decryptedResponse) ;
					listener.onHandShakeWithDevice() ;
				}	
			}
			else if(taskType == WIFI_PUT ) {
				String decryptedResponse = new DISecurity(null).decryptData(response, AppConstants.DEVICEID);
				Log.i("ews", decryptedResponse) ;
				if( decryptedResponse != null ) {
					connectToHomeNetwork();
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
