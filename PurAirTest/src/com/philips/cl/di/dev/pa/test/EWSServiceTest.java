package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.test.AndroidTestCase;

import com.philips.cl.di.dev.pa.ews.EWSBroadcastReceiver;
import com.philips.cl.di.dev.pa.ews.EWSListener;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public class EWSServiceTest extends AndroidTestCase {

	private EWSBroadcastReceiver ewsService;
	private EWSListener ewsListener;
	private final static String KEY = "173B7E0A9A54CB3E96A70237F6974940";
	private AirPurifier puriDevice;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ewsService = new EWSBroadcastReceiver(ewsListener, "WHF2012TEST");
		puriDevice = new AirPurifier(null, "fffgggcc05", "usn", "192.168.1.1", "name", 1, ConnectionState.CONNECTED_LOCALLY);
		puriDevice.getNetworkNode().setEncryptionKey(KEY);
	}

	public void testGetWifiPortJson() {
		String json = JSONBuilder.getWifiPortJson("Purifier2", "1234", puriDevice.getNetworkNode());
		String decryptedData = new DISecurity(null).decryptData(json, puriDevice.getNetworkNode());
		assertTrue(decryptedData.contains("ssid"));
		assertTrue(decryptedData.contains("Purifier2"));
		assertTrue(decryptedData.contains("password"));
		assertTrue(decryptedData.contains("1234"));
	}

	public void testGetWifiPortWithAdvConfigJson() {
		String json = JSONBuilder.getWifiPortWithAdvConfigJson("Purifier2", "1234", "192.168.1.1", "255.255.255.0",
		"192.168.1.1", puriDevice.getNetworkNode());
		String decryptedData = new DISecurity(null).decryptData(json, puriDevice.getNetworkNode());
		assertTrue(decryptedData.contains("ssid"));
		assertTrue(decryptedData.contains("Purifier2"));
		assertTrue(decryptedData.contains("password"));
		assertTrue(decryptedData.contains("1234"));
		assertTrue(decryptedData.contains("ipaddress"));
		assertTrue(decryptedData.contains("192.168.1.1"));
		assertTrue(decryptedData.contains("dhcp"));
		assertTrue(decryptedData.contains("false"));
		assertTrue(decryptedData.contains("netmask"));
		assertTrue(decryptedData.contains("255.255.255.0"));
		assertTrue(decryptedData.contains("gateway"));
		assertTrue(decryptedData.contains("192.168.1.1"));
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
