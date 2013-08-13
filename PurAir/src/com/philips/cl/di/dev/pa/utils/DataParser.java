package com.philips.cl.di.dev.pa.utils;

import org.json.JSONException;

import org.json.JSONObject;

import android.util.Log;

import com.philips.cl.di.dev.pa.constants.ParserConstants;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.dto.FilterStatusDto;
import com.philips.cl.di.dev.pa.interfaces.DataParserInterface;

/***
 * This class it used to parse data for AirPurifier event
 * @author 310124914
 *
 */
public class DataParser implements DataParserInterface {
	private static final String TAG = DataParser.class.getSimpleName() ;
	private String dataToParse ;
	
	public DataParser(String dataToParse) {
		Log.i(TAG, "DataParser") ;
		this.dataToParse = dataToParse ;
	}

	@Override
	public AirPurifierEventDto parseAirPurifierEventData() {
		AirPurifierEventDto airPurifierEvent = null ;
		try {			
			JSONObject jsonObj = new JSONObject(dataToParse) ;
			airPurifierEvent = new AirPurifierEventDto() ;
			
			airPurifierEvent.setGasSensor(Double.parseDouble((jsonObj.getString(ParserConstants.GAS_SENSOR)))) ;
			airPurifierEvent.setMachineMode(jsonObj.getString(ParserConstants.MACHINE_MODE)) ;
			airPurifierEvent.setMotorSpeed(Integer.parseInt(jsonObj.getString(ParserConstants.MOTOR_SPEED))) ;
			airPurifierEvent.setOutdoorAirQuality(Integer.parseInt(jsonObj.getString(ParserConstants.OUTDOOR_AQI))) ;
			airPurifierEvent.setPowerMode(jsonObj.getString(ParserConstants.POWER_MODE)) ;
			airPurifierEvent.setIndoorAQI(Integer.parseInt(jsonObj.getString(ParserConstants.AQI))) ;
			 
			
		} catch (JSONException e) {
			return null ;
		}
 		
		return airPurifierEvent ;
	}

	@Override
	public void parseHistoryData() {
		
	}

	/**
	 * Parse the filter Status Data
	 */
	@Override
	public FilterStatusDto parseFilterStatusData() {
		Log.i(TAG, dataToParse) ;
		FilterStatusDto filterStatusData = null ;
		try {
			JSONObject jsonObj = new JSONObject(dataToParse) ;
			filterStatusData = new FilterStatusDto() ;
			filterStatusData.setPreFilterStatus(jsonObj.optInt(ParserConstants.PRE_FILTER)) ;
			filterStatusData.setActiveCarbonFilterStatus(jsonObj.optInt(ParserConstants.ACTIVE_CARBON_FILTER)) ;
			filterStatusData.setHepaFilterStatus(jsonObj.optInt(ParserConstants.HEPA_FILTER)) ;
			filterStatusData.setMultiCareFilterStatus(jsonObj.optInt(ParserConstants.MULTI_CARE_FILTER)) ;
			
		} catch (JSONException e) {
			return null ;
		}
		return filterStatusData;
	}
}
