package com.philips.cl.di.dev.pa.test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;

public class FirmwareUpdateTest2 extends ActivityInstrumentationTestCase2<MainActivity>  {
	
	private MainActivity activity;
	Lock sequential = new ReentrantLock();

	public FirmwareUpdateTest2() {
	    super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();	
		setActivityInitialTouchMode(false);
		activity = getActivity();
		sequential.lock();
	}
	
	@Override
	protected void tearDown() throws Exception {
	    sequential.unlock();
	    super.tearDown();
	}
	
	public void onPause() {     
		this.onPause();  
	} 
	
	//********************************************** Test Cases for Entry Criteria ************************************************//
	
	/*  
	 * Class Name  : NA
	 * TestCase ID :
	 */
	/*
	//Test Case ID: 1
	@UiThreadTest
	public void testNewFWAvailable() {
		final LinearLayout llfirmwareUpdAvailable = (LinearLayout) activity.findViewById(R.id.firmware_update_available);
		String data = "{'name':'Jaguar','version':'19','upgrade':'21','state':'idle','progress':'0','statusmsg':'' ,'is_mandatory_upgrade':'false'}";
		
		assertEquals(LinearLayout.GONE, llfirmwareUpdAvailable.getVisibility());
		activity.firmwareDataRecieved(data);
		assertEquals(LinearLayout.VISIBLE, llfirmwareUpdAvailable.getVisibility());
	}
	
	@UiThreadTest
	public void testNoFWAvailble() {
		final LinearLayout llfirmwareUpdAvailable = (LinearLayout) activity.findViewById(R.id.firmware_update_available);
		String data = "{'name':'Jaguar','version':'19','upgrade':'','state':'idle','progress':'0','statusmsg':'' ,'is_mandatory_upgrade':'false'}";
		
		activity.firmwareDataRecieved(data);
		assertEquals(LinearLayout.GONE, llfirmwareUpdAvailable.getVisibility());
	}
	
	@UiThreadTest
	public void testPropertyMissing() {
		final LinearLayout llfirmwareUpdAvailable = (LinearLayout) activity.findViewById(R.id.firmware_update_available);
		String data = "{'name':'Jaguar','state':'idle','progress':'0','statusmsg':'' ,'is_mandatory_upgrade':'false'}";
		
		activity.firmwareDataRecieved(data);
		assertEquals(LinearLayout.GONE, llfirmwareUpdAvailable.getVisibility());
	}
	
	@UiThreadTest
	public void testNullResponse() {
		final LinearLayout llfirmwareUpdAvailable = (LinearLayout) activity.findViewById(R.id.firmware_update_available);
		String data = null;
		
		activity.firmwareDataRecieved(data);
		assertEquals(LinearLayout.GONE, llfirmwareUpdAvailable.getVisibility());
	}
	
	@UiThreadTest
	public void testNoJsonResponse() {
		final LinearLayout llfirmwareUpdAvailable = (LinearLayout) activity.findViewById(R.id.firmware_update_available);
		String data = "jdfaljfgladjglad";
		
		activity.firmwareDataRecieved(data);
		assertEquals(LinearLayout.GONE, llfirmwareUpdAvailable.getVisibility());
	}
	
	@UiThreadTest
	public void testEmptyResponse() {
		final LinearLayout llfirmwareUpdAvailable = (LinearLayout) activity.findViewById(R.id.firmware_update_available);
		String data = "";
		
		activity.firmwareDataRecieved(data);
		assertEquals(LinearLayout.GONE, llfirmwareUpdAvailable.getVisibility());
	}
	*/
	
	
}
