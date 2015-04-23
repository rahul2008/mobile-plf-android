package com.philips.cl.di.dicomm.port;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cl.di.dev.pa.datamodel.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public class FirmwarePort extends DICommPort {
	
	private final String FIRMWAREPORT_NAME = "firmware";
	private final int FIRMWAREPORT_PRODUCTID = 0;	
	private FirmwarePortInfo mFirmwarePortInfo;
	
	
	public FirmwarePort(NetworkNode networkNode, CommunicationStrategy communicationStrategy){
		super(networkNode,communicationStrategy);
	}

	public FirmwarePortInfo getPortInfo() {
		return mFirmwarePortInfo;
	}

	public void setPortInfo(FirmwarePortInfo firmwarePortInfo) {
		mFirmwarePortInfo = firmwarePortInfo;
	}

	@Override
	public boolean isResponseForThisPort(String response) {
		return (parseResponse(response)!=null);
	}

	@Override
	public void processResponse(String response) {
        FirmwarePortInfo firmwarePortInfo = parseResponse(response);
        if(firmwarePortInfo!=null){
        	setPortInfo(firmwarePortInfo);
        	return;
        }
        ALog.e(ALog.FIRMWAREPORT,"FirmwarePort Info should never be NULL");
	}
	
	private FirmwarePortInfo parseResponse(String response) {
        Gson gson = new GsonBuilder().create();
		
		try {
			JSONObject jsonObj = new JSONObject(response) ;
			JSONObject firmwareEventJson = jsonObj.optJSONObject("data") ;
			if( firmwareEventJson != null ) {
				jsonObj = firmwareEventJson ;
			}
			FirmwarePortInfo firmwarePortInfo = gson.fromJson(jsonObj.toString(), FirmwarePortInfo.class);
			if (firmwarePortInfo.isValid()) {
				return firmwarePortInfo;
			}
		} catch (JsonIOException e) {
			ALog.e(ALog.FIRMWAREPORT, "JsonIOException");
			return null;
		} catch (JsonSyntaxException e2) {
			ALog.e(ALog.FIRMWAREPORT, "JsonSyntaxException");
			return null;
		} catch (Exception e2) {
			ALog.e(ALog.FIRMWAREPORT, "Exception");
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
