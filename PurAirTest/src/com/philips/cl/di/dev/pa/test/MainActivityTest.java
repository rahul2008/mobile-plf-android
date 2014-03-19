package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.List;

import com.philips.cl.di.dev.pa.controller.DeviceInfoController;
import com.philips.cl.di.dev.pa.dto.DeviceInfoDto;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import com.philips.cl.di.dev.pa.security.DISecurity;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private MainActivity activity;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		activity = getActivity();
	}

	public void testActivityTitle() {
		assertEquals("PHILIPS Smart Air Purifier", activity.getTitle());
	}
	
	/**
	 * SSDP
	 */
	@SuppressWarnings("unchecked")
	public void testGetAllDeviceInfo_1() {
		DeviceInfoController deviceInfoController = new DeviceInfoController(activity);
		List<DeviceInfoDto> deviceInfoList = deviceInfoController.getAllDeviceInfo();
		
		Field keysField;
		try {
			keysField = MainActivity.class.getDeclaredField("deviceInfoDtoList");
			keysField.setAccessible(true);
			List<DeviceInfoDto> deviceInfoList1 = (List<DeviceInfoDto>) keysField.get(activity);
			
			if (deviceInfoList == null) {
				assertNull(deviceInfoList1);
			}
			
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
	public void testGetAllDeviceInfo_2() {
		DeviceInfoController deviceInfoController = new DeviceInfoController(activity);
		List<DeviceInfoDto> deviceInfoList = deviceInfoController.getAllDeviceInfo();
		
		Field keysField;
		try {
			keysField = MainActivity.class.getDeclaredField("deviceInfoDtoList");
			keysField.setAccessible(true);
			List<DeviceInfoDto> deviceInfoList1 = (List<DeviceInfoDto>) keysField.get(activity);
			
			if (deviceInfoList != null) {
				assertNotNull(deviceInfoList1);
				assertEquals(deviceInfoList.size(), deviceInfoList1.size());
			}
			
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
	
//	public void testGetIdUsnExistsInTable() {
//		
//	}
	
}
