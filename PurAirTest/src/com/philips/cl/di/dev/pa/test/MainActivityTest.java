package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.List;

import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DiscoveryServiceState;
import com.philips.cl.di.dev.pa.controller.DeviceInfoController;
import com.philips.cl.di.dev.pa.dto.DeviceInfoDto;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import com.philips.cl.di.dev.pa.security.DISecurity;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private MainActivity activity;
	
	DeviceInfoController deviceInfoController;
	List<DeviceInfoDto> deviceInfoList;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		activity = getActivity();
		
		deviceInfoController = new DeviceInfoController(activity);
		deviceInfoList = deviceInfoController.getAllDeviceInfo();
	}

	public void testActivityTitle() {
		assertEquals("PHILIPS Smart Air Purifier", activity.getTitle());
	}
	
	/**
	 * SSDP
	 */
	public void testSsdpDiscoveryStart() {
		SsdpService ssdpService = SsdpService.getInstance();
		ssdpService.startDeviceDiscovery(activity);
		Field keysField;
		try {
			keysField = SsdpService.class.getDeclaredField("mServiceState");
			keysField.setAccessible(true);
			DiscoveryServiceState mServiceState = (DiscoveryServiceState) keysField.get(ssdpService);
			assertEquals(DiscoveryServiceState.STARTED, mServiceState);
			
		} catch (NoSuchFieldException e) {
			// NOP
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// NOP
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// NOP
			e.printStackTrace();
		}
		
	}
	
	public void testSsdpDiscoveryStop() {
		SsdpService ssdpService = SsdpService.getInstance();
		ssdpService.stopDeviceDiscovery();
		Field keysField;
		try {
			keysField = SsdpService.class.getDeclaredField("mServiceState");
			keysField.setAccessible(true);
			DiscoveryServiceState mServiceState = (DiscoveryServiceState) keysField.get(ssdpService);
			assertEquals(DiscoveryServiceState.STOPPED, mServiceState);
			
		} catch (NoSuchFieldException e) {
			// NOP
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// NOP
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// NOP
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void testGetAllDeviceInfo_1() {
		
		Field keysField;
		try {
			keysField = MainActivity.class.getDeclaredField("deviceInfoDtoList");
			keysField.setAccessible(true);
			List<DeviceInfoDto> deviceInfoList1 = (List<DeviceInfoDto>) keysField.get(activity);
			assertNotNull(deviceInfoList1);
			assertEquals(deviceInfoList.size(), deviceInfoList1.size());
			
		} catch (NoSuchFieldException e) {
			// NOP
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// NOP
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// NOP
			e.printStackTrace();
		}
	}
	
}
