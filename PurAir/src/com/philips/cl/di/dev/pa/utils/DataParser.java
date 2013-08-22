package com.philips.cl.di.dev.pa.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONException;

import org.json.JSONObject;
import org.xml.sax.SAXException;

import android.util.Log;

import com.philips.cl.di.dev.pa.constants.ParserConstants;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.dto.FilterStatusDto;
import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
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

	@Override
	public List<OutdoorAQIEventDto> parseOutdoorAQIData() {
		List<OutdoorAQIEventDto> outdoorList = null ;
		try {
			OutdoorAQIParser parser = new OutdoorAQIParser() ;
			SAXParserFactory factory = SAXParserFactory.newInstance() ;
			SAXParser saxParser = factory.newSAXParser();
			
			ByteArrayInputStream bis = new ByteArrayInputStream(dataToParse.getBytes()) ;
			
			saxParser.parse(bis, parser) ;
			outdoorList =  parser.list ;
			
		}
		catch(NullPointerException e)
		{
			// TODO
			e.printStackTrace();
		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outdoorList ;
	}
}
