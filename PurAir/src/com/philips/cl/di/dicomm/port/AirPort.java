package com.philips.cl.di.dicomm.port;

import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.newpurifier.DeviceHandler;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PurifierEvent;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;

public class AirPort extends DICommPort {
	
	private final DeviceHandler mDeviceHandler;
	
	private AirPortInfo mAirPortInfo;
	
	public AirPort(NetworkNode networkNode, DeviceHandler deviceHandler){
		super(networkNode);
		mDeviceHandler = deviceHandler;
	}

	public AirPortInfo getAirPortInfo() {
		return mAirPortInfo;
	}

	public void setAirPortInfo(AirPortInfo mAirPortInfo) {
		this.mAirPortInfo = mAirPortInfo;
	}

	@Override
	public boolean isResponseForThisPort(String response) {
		return (parseResponse(response)!=null);
	}

	@Override
	public void processResponse(String response) {
        AirPortInfo airPortInfo = parseResponse(response);
        if(airPortInfo!=null){
        	setAirPortInfo(airPortInfo);
        	return;
        }
        ALog.e(ALog.AIRPORT,"AirPort Info should never be NULL");
	}
	
	private AirPortInfo parseResponse(String response) {
        return DataParser.parseAirPurifierEventData(response);
	}
	
	// TODO refactor into new architecture, rename method
	public void setPurifierDetails(String key, String value, PurifierEvent purifierEvent) {
		ALog.i(ALog.AIRPORT, "Set Appliance details: " + key +" = " + value) ;
		mDeviceHandler.setPurifierEvent(purifierEvent) ;
		mDeviceHandler.setPurifierDetails(key, value, mNetworkNode);
	}

}
