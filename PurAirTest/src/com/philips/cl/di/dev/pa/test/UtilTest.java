package com.philips.cl.di.dev.pa.test;

import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.util.Utils;

import junit.framework.TestCase;

public class UtilTest extends TestCase {
	
	public void testGetPurifierAirPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/air";
		String actualUrl = Utils.getPortUrl(Port.AIR, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierFirmwarePortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/firmware";
		String actualUrl = Utils.getPortUrl(Port.FIRMWARE, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierWifiPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/wifi";
		String actualUrl = Utils.getPortUrl(Port.WIFI, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierWifiuiPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/wifiui";
		String actualUrl = Utils.getPortUrl(Port.WIFIUI, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierDevicePortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/device";
		String actualUrl = Utils.getPortUrl(Port.DEVICE, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierLogPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/log";
		String actualUrl = Utils.getPortUrl(Port.LOG, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierPairingPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/pairing";
		String actualUrl = Utils.getPortUrl(Port.PAIRING, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierWrongPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/air";
		String actualUrl = Utils.getPortUrl(Port.PAIRING, ipAddress);
		
		assertFalse(expectedUrl.equals(actualUrl));
	}
	
	public void testGetPurifierAirPortUrlWrongIp() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + "178.234.3.53" + "/di/v1/products/1/air";
		String actualUrl = Utils.getPortUrl(Port.AIR, ipAddress);
		
		assertFalse(expectedUrl.equals(actualUrl));
	}

}
