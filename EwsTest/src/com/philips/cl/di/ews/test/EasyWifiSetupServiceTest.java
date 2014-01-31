package com.philips.cl.di.ews.test;

import android.content.Intent;
import android.test.AndroidTestCase;

import com.philips.cl.di.ews.EasyWifiSetupListener;
import com.philips.cl.di.ews.EasyWifiSetupService;

public class EasyWifiSetupServiceTest extends AndroidTestCase implements
		EasyWifiSetupListener {

	EasyWifiSetupService mService;
	static private String expected = "Philips Setup";
	static private String mConnecetAp;
	static private String mFoundAp;
	static private String mResultFromDevice;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mService = new EasyWifiSetupService(this.getContext(), this, expected, "http://192.168.1.1");
	}

	public void test00onReceive() {
		Intent intent = new Intent();
		intent.setAction("dummy");
		mService.onReceive(getContext(), intent);
	}
/*
	public void test01startScanForDeviceAp() {
		mService.startScanForDeviceAp();
		int counter = 0;
		while(mFoundAp ==null && counter <100){
			waitForResult(300);
			counter++;
		}
		assertEquals(expected, mFoundAp);
		
	}

	public void test02connectTo() {
		EasyWifiSetupService.connectTo(getContext(), expected ,"" );

		int counter = 0;
		while(mConnecetAp ==null && counter <100){
			waitForResult(300);
			counter++;
		}
	assertEquals(expected, mConnecetAp.replace("\"", ""));
	}

	public void test03sendSSidToDevice() {
		mService.sendSSidToDevice();
		int counter = 0;
		while(mResultFromDevice ==null && counter <100){
			waitForResult(300);
			counter++;
		}
		assertNotNull(mResultFromDevice);

	}

	public void waitForResult(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

*/

	@Override
	public void connectedToDeviceAp(String actual) {
		mConnecetAp = actual;
	}

	@Override
	public void foundDeviceAp(String actual) {
		mFoundAp = actual;
	}

	@Override
	public void sentSsidToDevice(String actual) {
		mResultFromDevice = actual;
	}

	@Override
	public void connectedToHomeNetwork(String url_base) {
		// TODO Auto-generated method stub
		
	}
}
