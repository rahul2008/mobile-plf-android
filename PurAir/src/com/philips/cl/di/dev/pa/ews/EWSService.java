package com.philips.cl.di.dev.pa.ews;

import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.cl.di.dev.pa.dto.DeviceDto;
import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.disecurity.DISecurity;
import com.philips.cl.disecurity.KeyDecryptListener;

public class EWSService extends BroadcastReceiver implements KeyDecryptListener, EWSTaskListener {

	private static final String TAG = EWSService.class.getSimpleName() ;
	public static final String WIFI_URI = "http://192.168.1.1/di/v1/products/1/wifi";
	public static final String DEVICE_URI = "http://192.168.1.1/di/v1/products/1/device";
	public static final String SECURITY_URI = "http://192.168.1.1/di/v1/products/0/security";

	public static final String DEVICE_SSID = "PHILIPS Setup" ;
	private EWSListener listener ;
	private Context context ;
	private String homeSSID ;

	public static final int DEVICE_GET = 1;
	public static final int DEVICE_PUT = 2;
	public static final int WIFI = 3;


	private int taskType ;
	private String password;
	private String deviceName ;
	
	private static final String deviceID = "dev001" ;

	public EWSService(EWSListener listener, Context context, String homeSSID, String password) {
		this.listener = listener ;
		this.context = context ;
		this.homeSSID = homeSSID ;
		this.password = password ;
		
	}
	
	private WifiManager mWifiManager;
	
	public void startScanForDeviceAp() {
		
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		context.registerReceiver(this, filter);
		timer = new Timer();
		timer.schedule(updateTask, 0, 10000);
		 
	}
	
	private Timer timer ;
	private TimerTask updateTask = new TimerTask() {
		@Override
	    public void run() {
			if(mWifiManager == null){
				mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			}
			mWifiManager.startScan();
	    }
   };

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("ews", "On Receive") ;
		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			android.net.NetworkInfo aNetwork = intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (aNetwork.getState() == android.net.NetworkInfo.State.CONNECTED) {
				Log.i(TAG, "Connected in  onReceive= "+ intent.getAction());
				WifiInfo connectionInfo = intent
						.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);

				String ssid = connectionInfo.getSSID();
				Log.i(TAG, "Connected ssid in  onReceive= "+ ssid);
				if (ssid.contains(homeSSID)) {
				}
				else if (ssid.contains(DEVICE_SSID)) {
					Log.i("ews", "AP Mode") ;
					listener.onDeviceAPMode() ;
					initializeKey() ;
					// Handshake with the Air Purifier
				}
			}
		}
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
		di.exchangeKey(SECURITY_URI, "dev001") ;
	}

	
	public void putWifiDetails() {
		taskType = WIFI ;

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
		Log.i("ews", "HomeSSID: "+homeSSID+":"+password) ;
		JSONObject holder = new JSONObject();
		try {
			holder.put("ssid", homeSSID);
			holder.put("password", password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*if (mPassword != null && mPassword.equals("") == false) {
			String s = Base64.encodeToString(encrypt(mPassword.getBytes()),
					Base64.DEFAULT);
			holder.put("password", s);
		}*/
		String js = holder.toString();
		Log.i("ews", js) ;
		
		String encryptedData = new DISecurity(null).encryptData(js, deviceID);
		
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

		/*if (mPassword != null && mPassword.equals("") == false) {
			String s = Base64.encodeToString(encrypt(mPassword.getBytes()),
					Base64.DEFAULT);
			holder.put("password", s);
		}*/
		String js = holder.toString();

		String encryptedData = new DISecurity(null).encryptData(js, deviceID);

		return encryptedData ;
	}

	
	public void connectToDeviceAP() {
		connectTo(DEVICE_SSID, "") ;
		startScanForDeviceAp() ;
	}

	public void connectToHomeNetwork(String ssid, String password) {
		connectTo(ssid, password) ;
	}

	private void connectTo(String ssid, String password) {

		WifiConfiguration config= new WifiConfiguration();
		config.SSID = "\"" + ssid.replace("\"", "") + "\"";
		config.status=WifiConfiguration.Status.ENABLED;
		config.priority = 1;
		if(password.equals("")){
			config.allowedKeyManagement.set(KeyMgmt.NONE);
		}else{
			config.preSharedKey ="\""+password+"\"";
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
		}
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

		WifiManager wifiManager = (WifiManager) 
				context.getSystemService(Context.WIFI_SERVICE);
		int netId = wifiManager.addNetwork(config);
		if (netId >= 0) {
			wifiManager.enableNetwork(netId, true);
		} else {
			Toast.makeText(context, "Please enable network", Toast.LENGTH_LONG).show() ;
		}
	}

	@Override
	public void keyDecrypt(String key) {
		Log.i("ews", "Key: "+key) ;
		if ( key != null ) {
			getDeviceDetails() ;
		}
	}

	@Override
	public void onTaskCompleted(int responseCode, String response) {
		Log.i("ews", "onTaskCompleted") ;
		switch (responseCode) {
		case HttpURLConnection.HTTP_OK:
			if( taskType == DEVICE_GET ) {
				String decryptedResponse = new DISecurity(null).decryptData(response, deviceID);
				Log.i("ews", decryptedResponse) ;
				if( decryptedResponse != null ) {
					storeDeviceDetails(decryptedResponse) ;
					listener.onHandShakeWithDevice() ;
				}				
			}
			else if(taskType == DEVICE_PUT ) {
				String decryptedResponse = new DISecurity(null).decryptData(response, deviceID);
				Log.i("ews", decryptedResponse) ;
				if( decryptedResponse != null ) {
					storeDeviceDetails(decryptedResponse) ;
					listener.onHandShakeWithDevice() ;
				}	
			}
			else if(taskType == WIFI ) {
				String decryptedResponse = new DISecurity(null).decryptData(response, deviceID);
				Log.i("ews", decryptedResponse) ;
				if( decryptedResponse != null ) {
					connectTo(homeSSID, password) ;
					listener.onDeviceConnectToHomeNetwork() ;
					
				}
			}
			break;
		}
	}
	
	private void storeDeviceDetails(String data) {
		Gson gson = new GsonBuilder().create() ;
		DeviceDto deviceDto = gson.fromJson(data, DeviceDto.class) ;
		SessionDto.getInstance().setDeviceDto(deviceDto) ;
	}
}
