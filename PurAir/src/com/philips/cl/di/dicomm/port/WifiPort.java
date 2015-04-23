package com.philips.cl.di.dicomm.port;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cl.di.dev.pa.datamodel.WifiPortProperties;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public class WifiPort extends DICommPort<WifiPortProperties> {

	private static final String KEY_GATEWAY = "gateway";
    private static final String KEY_SUBNETMASK = "netmask";
    private static final String KEY_DHCP = "dhcp";
    private static final String KEY_IPADDRESS = "ipaddress";
    private static final String KEY_WIFIPASSWORD = "password";
    private static final String KEY_WIFISSID = "ssid";

    public WifiPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
		super(networkNode, communicationStrategy);
	}

	private final String WIFIPORT_NAME = "wifi";
	private final int WIFIPORT_PRODUCTID = 0;

	@Override
	public boolean isResponseForThisPort(String response) {
		return (parseResponse(response)!=null);
	}

	@Override
	public void processResponse(String response) {
		WifiPortProperties wifiPortProperties = parseResponse(response);
        if(wifiPortProperties!=null){
        	setPortProperties(wifiPortProperties);
        	return;
        }
        ALog.e(ALog.WIFIPORT,"Wifi port properties should never be NULL");

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
			ALog.e(ALog.WIFIPORT, "JsonSyntaxException");
		} catch (JsonIOException e) {
			ALog.e(ALog.WIFIPORT, "JsonIOException");
		} catch (Exception e2) {
			ALog.e(ALog.WIFIPORT, "Exception");
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
