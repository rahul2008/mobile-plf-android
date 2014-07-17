package com.philips.cl.di.dev.pa.demo;

import java.net.HttpURLConnection;
import java.util.UUID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.datamodel.DeviceDto;
import com.philips.cl.di.dev.pa.datamodel.DeviceWifiDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.security.KeyDecryptListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;


public class DemoModeBroadcastReceiver extends BroadcastReceiver 
			implements KeyDecryptListener, Runnable, ServerResponseListener {
	
	private DemoModeListener listener;
	private boolean isRegistered;
	private int errorCode = DemoModeConstant.DEMO_MODE_ERROR_NOT_IN_PHILIPS_SETUP;
	private boolean stop = true;
	private int totalTime = 10 * 1000 ;
	private PurAirDevice tempDemoModePurifier;
	private DemoModeTask task;
	private int taskType = DemoModeConstant.DEMO_MODE_TASK_DEVICE_GET;
	private IntentFilter filter = new IntentFilter();
	private KeyInitializeState keyInitializeState = KeyInitializeState.NONE;
	
	public DemoModeBroadcastReceiver(DemoModeListener listener) {
		this.listener = listener;
		generateTempDemoModeDevice();
	}

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
				ALog.e(ALog.DEMO_MODE, "Runnable thread: " + e.getMessage());
			}
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		ALog.i(ALog.DEMO_MODE, "On Receive:"+intent.getAction()) ;
		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			
			if (netInfo.getState() == android.net.NetworkInfo.State.CONNECTED) {
				
				String ssid = EWSWifiManager.getSsidOfConnectedNetwork();
				if (ssid == null) {
					ALog.i(ALog.DEMO_MODE, "Failed to get ssid of connected network");
					return;
				}
				
				if (ssid.contains(EWSWifiManager.DEVICE_SSID)) {
					ALog.i(ALog.DEMO_MODE, "Connected to AirPurifier - Ssid= "+ ssid);
					errorCode = DemoModeConstant.DEMO_MODE_ERROR_DATA_RECIEVED_FAILED;
					initializeKey();
					return;
				}
			} 
		}
	}
	
	public void connectToDeviceAP() {
		ALog.i(ALog.DEMO_MODE, "connecttoDevice AP");
		keyInitializeState = KeyInitializeState.NONE;
		WifiManager wifiManager = (WifiManager) PurAirApplication.getAppContext().getSystemService(Context.WIFI_SERVICE);
		wifiManager.disconnect();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				EWSWifiManager.connectToPhilipsSetup();
			}
		}).start();
		
		startScanForDeviceAp() ;
		deviceSSIDTimer.start() ;
	}
	
	private void initializeKey() {
		ALog.i(ALog.DEMO_MODE, "initiliazekey") ;
		if (keyInitializeState == KeyInitializeState.NONE) {
			keyInitializeState = KeyInitializeState.START;
			DISecurity di = new DISecurity(this) ;
			di.initializeExchangeKeyCounter(tempDemoModePurifier.getEui64());
			di.exchangeKey(Utils.getPortUrl(Port.SECURITY, EWSConstant.PURIFIER_ADHOCIP), tempDemoModePurifier.getEui64()) ;
		}
	}
	public void registerListener() {
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
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
	
	private CountDownTimer deviceSSIDTimer = new CountDownTimer(60000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			stop = true ;
			unRegisterListener() ;
			if (task != null) {
				task.stopTask();
			}
			listener.onErrorOccur(errorCode) ;
		}
	};
	
	public void stopSSIDTimer() {
		if(deviceSSIDTimer != null ) {
			deviceSSIDTimer.cancel() ;
		}
	}
	
	private void generateTempDemoModeDevice() {
		String tempEui64 = UUID.randomUUID().toString();
		tempDemoModePurifier = new PurAirDevice(
				tempEui64, null, EWSConstant.PURIFIER_ADHOCIP, DemoModeConstant.DEMO, -1, ConnectionState.CONNECTED_LOCALLY);
	}
	
	private void updateTempDevice(String eui64) {
		if (tempDemoModePurifier == null) return;
		String encryptionKey = tempDemoModePurifier.getEncryptionKey();
		String purifierName = DemoModeConstant.DEMO;
		tempDemoModePurifier = new PurAirDevice(
				eui64, null, EWSConstant.PURIFIER_ADHOCIP, purifierName, -1, ConnectionState.CONNECTED_LOCALLY);
		tempDemoModePurifier.setEncryptionKey(encryptionKey);
		PurifierManager.getInstance().setCurrentPurifier(tempDemoModePurifier);
		PurAirApplication.setDemoModePurifier(eui64);
	}
	
	private void getDeviceDetails() {
		ALog.i(ALog.DEMO_MODE,"device details") ;
		taskType = DemoModeConstant.DEMO_MODE_TASK_DEVICE_GET;
		task = new DemoModeTask(this, Utils.getPortUrl(Port.DEVICE, EWSConstant.PURIFIER_ADHOCIP),"" , "GET") ;
		task.start();
	}
	
	private void getWifiDetails() {
		ALog.i(ALog.DEMO_MODE, "gettWifiDetails");
		taskType = DemoModeConstant.DEMO_MODE_TASK_WIFI_GET;
		task = new DemoModeTask(this, Utils.getPortUrl(Port.WIFI, EWSConstant.PURIFIER_ADHOCIP),"" , "GET");
		task.start();
	}

	@Override
	public void keyDecrypt(String key, String deviceEui64) {
		ALog.i(ALog.DEMO_MODE, "Key: "+key) ;
		if (tempDemoModePurifier == null) return;
		tempDemoModePurifier.setEncryptionKey(key);

		if ( key != null ) {
			getDeviceDetails() ;
		}
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String responseData, String ipStr) {
		
		stop = true ;
		ALog.i(ALog.DEMO_MODE, "onTaskCompleted:"+responseCode) ;
		switch (responseCode) {
		case HttpURLConnection.HTTP_OK:
			if( taskType == DemoModeConstant.DEMO_MODE_TASK_DEVICE_GET ) {
				String decryptedResponse = new DISecurity(null).decryptData(responseData, tempDemoModePurifier);
				if( decryptedResponse != null ) {
					ALog.i(ALog.DEMO_MODE,decryptedResponse) ;
					DeviceDto deviceDto = DataParser.getDeviceDetails(decryptedResponse) ;
					
					SessionDto.getInstance().setDeviceDto(deviceDto) ;
					if (deviceDto == null) return;
					tempDemoModePurifier.setName(DemoModeConstant.DEMO);
					getWifiDetails() ;
				}				
			}
			else if(taskType == DemoModeConstant.DEMO_MODE_TASK_WIFI_GET) {
				String decryptedResponse = new DISecurity(null).decryptData(responseData, tempDemoModePurifier);
				if( decryptedResponse != null ) {
					ALog.i(ALog.DEMO_MODE,decryptedResponse) ;
					DeviceWifiDto deviceWifiDto = DataParser.getDeviceWifiDetails(decryptedResponse);
					
					SessionDto.getInstance().setDeviceWifiDto(deviceWifiDto) ;
					
					if (deviceWifiDto != null) {
						this.updateTempDevice(deviceWifiDto.getCppid());
					}
					
					deviceSSIDTimer.cancel() ;
					listener.onHandShakeWithDevice() ;
				}	
			}
			break;
		default: 
			stop = true ;
			stopSSIDTimer() ;
			
			if( taskType == DemoModeConstant.DEMO_MODE_TASK_DEVICE_GET 
					|| taskType == DemoModeConstant.DEMO_MODE_TASK_WIFI_GET) {
				listener.onErrorOccur(DemoModeConstant.DEMO_MODE_ERROR_DATA_RECIEVED_FAILED) ;
			}
			break;			
		}
	}
}
