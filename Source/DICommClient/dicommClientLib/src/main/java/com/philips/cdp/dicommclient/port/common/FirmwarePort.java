/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;

public class FirmwarePort extends DICommPort<FirmwarePortProperties> {

	private final String FIRMWAREPORT_NAME = "firmware";
	private final int FIRMWAREPORT_PRODUCTID = 0;


	public FirmwarePort(CommunicationStrategy communicationStrategy){
		super(communicationStrategy);
	}

	@Override
	public boolean isResponseForThisPort(String jsonResponse) {
		return (parseResponse(jsonResponse)!=null);
	}

	@Override
	public void processResponse(String jsonResponse) {
        FirmwarePortProperties firmwarePortInfo = parseResponse(jsonResponse);
        if(firmwarePortInfo!=null){
        	setPortProperties(firmwarePortInfo);
        	return;
        }
        DICommLog.e(DICommLog.FIRMWAREPORT,"FirmwarePort Info should never be NULL");
	}

	private FirmwarePortProperties parseResponse(String response) {
        Gson gson = new GsonBuilder().create();

		try {
			FirmwarePortProperties firmwarePortInfo = gson.fromJson(response, FirmwarePortProperties.class);
			if (firmwarePortInfo.isValid()) {
				return firmwarePortInfo;
			}
		} catch (JsonIOException e) {
			DICommLog.e(DICommLog.FIRMWAREPORT, "JsonIOException");
			return null;
		} catch (JsonSyntaxException e2) {
			DICommLog.e(DICommLog.FIRMWAREPORT, "JsonSyntaxException");
			return null;
		} catch (Exception e2) {
			DICommLog.e(DICommLog.FIRMWAREPORT, "Exception");
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
