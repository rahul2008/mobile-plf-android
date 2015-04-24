package com.philips.cl.di.dev.pa.test;

import android.test.AndroidTestCase;

import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dicomm.security.DISecurity;

public class EWSServiceTest extends AndroidTestCase {

	private final static String KEY = "173B7E0A9A54CB3E96A70237F6974940";

	private NetworkNode mNetworkNode;
    
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		mNetworkNode = new NetworkNode();
        mNetworkNode.setBootId(1);
        mNetworkNode.setCppId("fffgggcc05");
        mNetworkNode.setIpAddress("192.168.1.1");
        mNetworkNode.setName("name");
        mNetworkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
        mNetworkNode.setEncryptionKey(KEY);
	}

	public void testGetWifiPortJson() {
		String json = JSONBuilder.getWifiPortJson("Purifier2", "1234", mNetworkNode);
		String decryptedData = new DISecurity().decryptData(json, mNetworkNode);
		assertTrue(decryptedData.contains("ssid"));
		assertTrue(decryptedData.contains("Purifier2"));
		assertTrue(decryptedData.contains("password"));
		assertTrue(decryptedData.contains("1234"));
	}

	public void testGetWifiPortWithAdvConfigJson() {
		String json = JSONBuilder.getWifiPortWithAdvConfigJson("Purifier2", "1234", "192.168.1.1", "255.255.255.0",
		"192.168.1.1", mNetworkNode);
		String decryptedData = new DISecurity().decryptData(json, mNetworkNode);
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
}
