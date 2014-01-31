package com.philips.cl.di.ews.test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.philips.cl.di.ews.EasyWifiSetupListener;
import com.philips.cl.di.ews.EasyWifiSetupService;


public class EasyWifiSetupStub extends EasyWifiSetupService{

	public EasyWifiSetupStub(Context aContext, EasyWifiSetupListener aListener,
			String aDeviceSsid, String anEndPoint) {
		super(aContext, aListener, aDeviceSsid, anEndPoint);
		mInfo.mCapabilities="[ESS][WPA]";
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
	}	

		@Override
		public void startScanForDeviceAp() {
			Log.i("EasyWifiSetupStub","stub startScanForDeviceAp");
		}
 
		public void sendSSidToDevice() {

		}

}