/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;

public class WifiPort extends DICommPort<WifiPortProperties> {

	private static final String KEY_GATEWAY = "gateway";
    private static final String KEY_SUBNETMASK = "netmask";
    private static final String KEY_DHCP = "dhcp";
    private static final String KEY_IPADDRESS = "ipaddress";
    private static final String KEY_WIFIPASSWORD = "password";
    private static final String KEY_WIFISSID = "ssid";

    public WifiPort(CommunicationStrategy communicationStrategy) {
		super(communicationStrategy);
	}

	private final String WIFIPORT_NAME = "wifi";
	private final int WIFIPORT_PRODUCTID = 0;

	@Override
	public boolean isResponseForThisPort(String jsonResponse) {
		return (parseResponse(jsonResponse)!=null);
	}

	@Override
	public void processResponse(String jsonResponse) {
		WifiPortProperties wifiPortProperties = parseResponse(jsonResponse);
        if(wifiPortProperties!=null){
        	setPortProperties(wifiPortProperties);
        	return;
        }
        DICommLog.e(DICommLog.WIFIPORT,"Wifi port properties should never be NULL");

	}

	@Override
	public String getDICommPortName() {
		return WIFIPORT_NAME;
	}

	@Override
	public int getDICommProductId() {
		return WIFIPORT_PRODUCTID;
	}

	@Override
	public boolean supportsSubscription() {
		// TODO DIComm Refactor check if subscription to deviceport is necessary
		return false;
	}

	private WifiPortProperties parseResponse(String response) {
        if (response == null || response.isEmpty()) {
			return null;
		}
		Gson gson = new GsonBuilder().create() ;
		WifiPortProperties wifiPortProperties = null;
		try {
			wifiPortProperties = gson.fromJson(response, WifiPortProperties.class) ;
		} catch (JsonSyntaxException e) {
			DICommLog.e(DICommLog.WIFIPORT, "JsonSyntaxException");
		} catch (JsonIOException e) {
			DICommLog.e(DICommLog.WIFIPORT, "JsonIOException");
		} catch (Exception e2) {
			DICommLog.e(DICommLog.WIFIPORT, "Exception");
		}
		return wifiPortProperties;
	}

	public void setWifiNetworkDetails(String ssid, String password) {
	    Map<String, Object> dataMap = new HashMap<String, Object>();
	    dataMap.put(KEY_WIFISSID, ssid);
        dataMap.put(KEY_WIFIPASSWORD, password);
        putProperties(dataMap);
	}

	public void setWifiNetworkDetails(String ssid, String password, String ipAddress, boolean dhcp, String subnetMask, String gateWay) {
	    Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put(KEY_WIFISSID, ssid);
        dataMap.put(KEY_WIFIPASSWORD, password);
        dataMap.put(KEY_IPADDRESS, ipAddress);
        dataMap.put(KEY_DHCP, dhcp);
        dataMap.put(KEY_SUBNETMASK, subnetMask);
        dataMap.put(KEY_GATEWAY, gateWay);
        putProperties(dataMap);
	}
}
