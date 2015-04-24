package com.philips.cl.di.dicomm.port;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cl.di.dev.pa.datamodel.WifiUIPortProperties;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public class WifiUIPort extends DICommPort<WifiUIPortProperties> {

    private final String WIFIUIPORT_NAME = "wifiui";
    private final int WIFIUIPORT_PRODUCTID = 1;

    private static final String KEY_SETUP = "setup";
    private static final String KEY_CONNECTION = "connection";

    public WifiUIPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
		super(networkNode, communicationStrategy);
	}

	@Override
	public boolean isResponseForThisPort(String response) {
		return (parseResponse(response)!=null);
	}

	@Override
	public void processResponse(String response) {
	    WifiUIPortProperties properties = parseResponse(response);
        if(properties!=null){
        	setPortProperties(properties);
        	return;
        }
        ALog.e(ALog.WIFIUIPORT,"Wifi port properties should never be NULL");

	}

	@Override
	public String getDICommPortName() {
		return WIFIUIPORT_NAME;
	}

	@Override
	public int getDICommProductId() {
		return WIFIUIPORT_PRODUCTID;
	}

	@Override
	public boolean supportsSubscription() {
		return false;
	}

	private WifiUIPortProperties parseResponse(String response) {
        if (response == null || response.isEmpty()) {
			return null;
		}
		Gson gson = new GsonBuilder().create() ;
		WifiUIPortProperties properties = null;
		try {
		    properties = gson.fromJson(response, WifiUIPortProperties.class) ;
		} catch (JsonSyntaxException e) {
			ALog.e(ALog.WIFIUIPORT, "JsonSyntaxException");
		} catch (JsonIOException e) {
			ALog.e(ALog.WIFIUIPORT, "JsonIOException");
		} catch (Exception e2) {
			ALog.e(ALog.WIFIUIPORT, "Exception");
		}
		return properties;
	}
    
    public void disableDemoMode() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put(KEY_SETUP, "inactive");
        dataMap.put(KEY_CONNECTION, "disconnected");
        putProperties(dataMap);
    }
}
