package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.test.AndroidTestCase;

import com.philips.cl.di.dev.pa.ews.EWSBroadcastReceiver;
import com.philips.cl.di.dev.pa.ews.EWSListener;

public class EWSServiceTest extends AndroidTestCase {
	
	private EWSBroadcastReceiver ewsService;
	private EWSListener ewsListener;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ewsService = new EWSBroadcastReceiver(ewsListener, "WHF2012TEST", "");
		
	}
	
	public void testGetWifiPortJson() {
		String data1 = "", data2 = "";
		
		try {
			Method getWifiPort = EWSBroadcastReceiver.class.getDeclaredMethod("getWifiPortJson", (Class<?>[]) null);
			getWifiPort.setAccessible(true);
			data1 = (String) getWifiPort.invoke(ewsService, (Object[]) null);
			data2 = (String) getWifiPort.invoke(ewsService, (Object[]) null);
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(data1, data2);
	}
	
	public void testGetDevicePortJson() {
		String data1 = "";
		String data2 = "";
		
		try {
			Method devicePort = EWSBroadcastReceiver.class.getDeclaredMethod("getDevicePortJson", (Class<?>[]) null);
			devicePort.setAccessible(true);
			data1 = (String) devicePort.invoke(ewsService, (Object[]) null);
			data2 = (String) devicePort.invoke(ewsService, (Object[]) null);
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertEquals(data1, data2);
	}
	
}
