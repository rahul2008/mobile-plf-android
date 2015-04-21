package com.philips.cl.di.dicomm.port;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public class AirPort extends DICommPort {
	
	private final String AIRPORT_NAME = "air";	
	private final int AIRPORT_PRODUCTID = 1;
	
	private AirPortInfo mAirPortInfo;
	
	public AirPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy){
		super(networkNode,communicationStrategy);
	}

	public AirPortInfo getAirPortInfo() {
		return mAirPortInfo;
	}

	public void setAirPortInfo(AirPortInfo airPortInfo) {
		mAirPortInfo = airPortInfo;
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
        AirPortInfo airPortInfo = null ;
		try {			
			if( response != null ) {
				JSONObject jsonObj = new JSONObject(response) ;
				JSONObject airPuriferJson = jsonObj.optJSONObject("data") ;
				if( airPuriferJson != null ) {
					jsonObj = airPuriferJson ;
				}
				airPortInfo = new AirPortInfo() ;			
		
				airPortInfo.setMachineMode(jsonObj.getString(ParserConstants.MACHINE_MODE)) ;
				airPortInfo.setFanSpeed(jsonObj.getString(ParserConstants.FAN_SPEED)) ;
				airPortInfo.setPowerMode(jsonObj.getString(ParserConstants.POWER_MODE)) ;
				String aqi = jsonObj.getString(ParserConstants.AQI) ;
				if(aqi != null && !aqi.equals(""))
					airPortInfo.setIndoorAQI(Integer.parseInt(aqi)) ;
		
				airPortInfo.setAqiL(Integer.parseInt(jsonObj.getString(ParserConstants.AQI_LIGHT))) ;
				airPortInfo.setAqiThreshold(Integer.parseInt(jsonObj.getString(ParserConstants.AQI_THRESHOLD))) ;
				airPortInfo.setDtrs(Integer.parseInt(jsonObj.getString(ParserConstants.DTRS))) ;
				airPortInfo.setPreFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_1))) ;
				airPortInfo.setMulticareFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_2))) ;
				airPortInfo.setActiveFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_3))) ;
				airPortInfo.setHepaFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_4))) ;
				airPortInfo.setReplaceFilter1(jsonObj.getString(ParserConstants.CLEAN_FILTER_1)) ;
				airPortInfo.setReplaceFilter2(jsonObj.getString(ParserConstants.REP_FILTER_2)) ;
				airPortInfo.setReplaceFilter3(jsonObj.getString(ParserConstants.REP_FILTER_3)) ;
				airPortInfo.setReplaceFilter4(jsonObj.getString(ParserConstants.REP_FILTER_4)) ;
				airPortInfo.setChildLock(Integer.parseInt(jsonObj.getString(ParserConstants.CHILD_LOCK))) ;
				airPortInfo.setpSensor(Integer.parseInt(jsonObj.getString(ParserConstants.PSENS))) ;
				airPortInfo.settFav(Integer.parseInt(jsonObj.getString(ParserConstants.TFAV))) ;	
				airPortInfo.setActualFanSpeed(jsonObj.getString(ParserConstants.ACTUAL_FAN_SPEED));
			}
		} catch (JSONException e) {
			ALog.e(ALog.AIRPORT, "JSONException AirPortInfo") ;
			return null;
		} catch (JsonIOException e) {
			ALog.e(ALog.AIRPORT, "JsonIOException AirPortInfo");
			return null;
		} catch (JsonSyntaxException e2) {
			ALog.e(ALog.AIRPORT, "JsonSyntaxException AirPortInfo");
			return null;
		} catch (Exception e2) {
			ALog.e(ALog.AIRPORT, "Exception AirPortInfo");
			return null;
		}
		
		return airPortInfo ;
	}

	@Override
	public String getDICommPortName() {
		return AIRPORT_NAME;
	}

	@Override
	public int getDICommProductId() {
		return AIRPORT_PRODUCTID;
	}

    @Override
    public boolean supportsSubscription() {
        return true;
    }
}
