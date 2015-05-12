package com.philips.cl.di.dicomm.port;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.datamodel.AirPortProperties;
import com.philips.cl.di.dev.pa.util.ALog;

public class AirPort extends DICommPort<AirPortProperties> {

	private final String AIRPORT_NAME = "air";
	private final int AIRPORT_PRODUCTID = 1;

	public AirPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy){
		super(networkNode,communicationStrategy);
	}

	@Override
	public boolean isResponseForThisPort(String response) {
		return (parseResponse(response)!=null);
	}

	@Override
	public void processResponse(String response) {
        AirPortProperties airPortInfo = parseResponse(response);
        if(airPortInfo!=null){
        	setPortProperties(airPortInfo);
        	return;
        }
        ALog.e(ALog.AIRPORT,"AirPort Info should never be NULL");
	}

	private AirPortProperties parseResponse(String response) {
        AirPortProperties airPortProperties = null ;
		try {
			if( response != null ) {
				JSONObject jsonObj = new JSONObject(response) ;
				JSONObject airPuriferJson = jsonObj.optJSONObject("data") ;
				if( airPuriferJson != null ) {
					jsonObj = airPuriferJson ;
				}
				airPortProperties = new AirPortProperties() ;

				airPortProperties.setMachineMode(jsonObj.getString(ParserConstants.MACHINE_MODE)) ;
				airPortProperties.setFanSpeed(jsonObj.getString(ParserConstants.FAN_SPEED)) ;
				airPortProperties.setPowerMode(jsonObj.getString(ParserConstants.POWER_MODE)) ;
				String aqi = jsonObj.getString(ParserConstants.AQI) ;
				if(aqi != null && !aqi.equals(""))
					airPortProperties.setIndoorAQI(Integer.parseInt(aqi)) ;

				airPortProperties.setAqiL(Integer.parseInt(jsonObj.getString(ParserConstants.AQI_LIGHT))) ;
				airPortProperties.setAqiThreshold(Integer.parseInt(jsonObj.getString(ParserConstants.AQI_THRESHOLD))) ;
				airPortProperties.setDtrs(Integer.parseInt(jsonObj.getString(ParserConstants.DTRS))) ;
				airPortProperties.setPreFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_1))) ;
				airPortProperties.setMulticareFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_2))) ;
				airPortProperties.setActiveFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_3))) ;
				airPortProperties.setHepaFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_4))) ;
				airPortProperties.setReplaceFilter1(jsonObj.getString(ParserConstants.CLEAN_FILTER_1)) ;
				airPortProperties.setReplaceFilter2(jsonObj.getString(ParserConstants.REP_FILTER_2)) ;
				airPortProperties.setReplaceFilter3(jsonObj.getString(ParserConstants.REP_FILTER_3)) ;
				airPortProperties.setReplaceFilter4(jsonObj.getString(ParserConstants.REP_FILTER_4)) ;
				airPortProperties.setChildLock(Integer.parseInt(jsonObj.getString(ParserConstants.CHILD_LOCK))) ;
				airPortProperties.setpSensor(Integer.parseInt(jsonObj.getString(ParserConstants.PSENS))) ;
				airPortProperties.settFav(Integer.parseInt(jsonObj.getString(ParserConstants.TFAV))) ;
				airPortProperties.setActualFanSpeed(jsonObj.getString(ParserConstants.ACTUAL_FAN_SPEED));
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

		return airPortProperties ;
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
    
    public void setDefaultPortProperties(AirPortProperties defaultAirPortProperties) {
        setPortProperties(defaultAirPortProperties);
    }
}
