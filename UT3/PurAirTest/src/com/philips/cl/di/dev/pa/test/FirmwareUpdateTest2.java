package com.philips.cl.di.dev.pa.test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateActivity;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;

public class FirmwareUpdateTest2 extends ActivityInstrumentationTestCase2<FirmwareUpdateActivity>  {
	
	private FirmwareUpdateActivity activity;

	public FirmwareUpdateTest2() {
		
	    super(FirmwareUpdateActivity.class);
	    
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();	
		setActivityInitialTouchMode(false);
		activity = getActivity();
	}
	
	public void testGetCurrentPurifier() {
		PurAirDevice currentPurAirDevice = PurifierManager.getInstance().getCurrentPurifier();
		PurAirDevice purAirDevice = activity.getCurrentPurifier();
		if (currentPurAirDevice == null) {
			assertNull(purAirDevice);
		} else {
			assertNotNull(purAirDevice);
		}
		
	}
	
	public void testGetCurrentPurifierName() {
		PurAirDevice currentPurAirDevice = PurifierManager.getInstance().getCurrentPurifier();
		String purAirName = activity.getPurifierName();
		if (currentPurAirDevice == null) {
			assertNull(purAirName);
		} else {
			assertNotNull(purAirName);
		}
		
	}
	
	public void testGetFirmwareURL() {
		PurAirDevice currentPurAirDevice = PurifierManager.getInstance().getCurrentPurifier();
		String url = activity.getFirmwareURL();
		if (currentPurAirDevice == null) {
			assertNull(url);
		} else {
			assertNotNull(url);
		}
	}
}
