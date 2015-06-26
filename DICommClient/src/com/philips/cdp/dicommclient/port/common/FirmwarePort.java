/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DLog;

public class FirmwarePort extends DICommPort<FirmwarePortProperties> {

	private final String FIRMWAREPORT_NAME = "firmware";
	private final int FIRMWAREPORT_PRODUCTID = 0;


	public FirmwarePort(NetworkNode networkNode, CommunicationStrategy communicationStrategy){
		super(networkNode,communicationStrategy);
	}

	@Override
	public boolean isResponseForThisPort(String response) {
		return (parseResponse(response)!=null);
	}

	@Override
	public void processResponse(String response) {
        FirmwarePortProperties firmwarePortInfo = parseResponse(response);
        if(firmwarePortInfo!=null){
        	setPortProperties(firmwarePortInfo);
        	return;
        }
        DLog.e(DLog.FIRMWAREPORT,"FirmwarePort Info should never be NULL");
	}

	private FirmwarePortProperties parseResponse(String response) {
        Gson gson = new GsonBuilder().create();

		try {
			JSONObject jsonObj = new JSONObject(response) ;
			JSONObject firmwareEventJson = jsonObj.optJSONObject("data") ;
			if( firmwareEventJson != null ) {
				jsonObj = firmwareEventJson ;
			}
			FirmwarePortProperties firmwarePortInfo = gson.fromJson(jsonObj.toString(), FirmwarePortProperties.class);
			if (firmwarePortInfo.isValid()) {
				return firmwarePortInfo;
			}
		} catch (JsonIOException e) {
			DLog.e(DLog.FIRMWAREPORT, "JsonIOException");
			return null;
		} catch (JsonSyntaxException e2) {
			DLog.e(DLog.FIRMWAREPORT, "JsonSyntaxException");
			return null;
		} catch (Exception e2) {
			DLog.e(DLog.FIRMWAREPORT, "Exception");
			return null;
		}
		return null;
	}

	@Override
	public String getDICommPortName() {
		return FIRMWAREPORT_NAME;
	}

	@Override
	public int getDICommProductId() {
		return FIRMWAREPORT_PRODUCTID;
	}

    @Override
    public boolean supportsSubscription() {
        return true;
    }
}
