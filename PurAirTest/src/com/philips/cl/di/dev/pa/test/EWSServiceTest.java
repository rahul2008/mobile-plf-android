package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.net.wifi.WifiConfiguration;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import com.philips.cl.di.dev.pa.ews.EWSListener;
import com.philips.cl.di.dev.pa.ews.EWSService;

public class EWSServiceTest extends AndroidTestCase {
	
	private EWSService ewsService;
	private MockContext mockContext;
	private EWSListener ewsListener;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ewsService = new EWSService(ewsListener, mockContext, "WHF2012TEST", "");
		mockContext = new MockContext();
		
	}
	
//	public void testRegisterListener() {
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
//		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
//		filter.addAction(WifiManager.EXTRA_WIFI_STATE) ;
//		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
//		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
//		mContext.registerReceiver(ewsService, filter);
////		ewsService.onReceive(mockContext, new Intent());
//		ewsService.getResultCode();
//	}
	
	public void testGetWifiPortJson() {
		String data1 = "", data2 = "";
		
		try {
			Method getWifiPort = EWSService.class.getDeclaredMethod("getWifiPortJson", (Class<?>[]) null);
			getWifiPort.setAccessible(true);
			data1 = (String) getWifiPort.invoke(ewsService, (Object[]) null);
			data2 = (String) getWifiPort.invoke(ewsService, (Object[]) null);
			
			assertEquals(data1, data2);
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetDevicePortJson() {
		String data1 = "";
		String data2 = "";
		
		try {
			Method devicePort = EWSService.class.getDeclaredMethod("getDevicePortJson", (Class<?>[]) null);
			devicePort.setAccessible(true);
			data1 = (String) devicePort.invoke(ewsService, (Object[]) null);
			data2 = (String) devicePort.invoke(ewsService, (Object[]) null);
			
			assertEquals(data1, data2);
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
