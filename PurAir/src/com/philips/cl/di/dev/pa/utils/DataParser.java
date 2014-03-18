package com.philips.cl.di.dev.pa.utils;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.philips.cl.di.dev.pa.constants.ParserConstants;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.dto.IndoorHistoryDto;
import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.dto.Weatherdto;
import com.philips.cl.di.dev.pa.interfaces.DataParserInterface;

/***
 * This class it used to parse data for AirPurifier event
 * @author 310124914
 *
 */
public class DataParser implements DataParserInterface {
	//private static final String TAG = DataParser.class.getSimpleName() ;
	private String dataToParse ;

	public DataParser(String dataToParse) {
		this.dataToParse = dataToParse ;
	}

	@Override
	public AirPurifierEventDto parseAirPurifierEventData() {
		AirPurifierEventDto airPurifierEvent = null ;
		try {			
			JSONObject jsonObj = new JSONObject(dataToParse) ;
			airPurifierEvent = new AirPurifierEventDto() ;			

			airPurifierEvent.setMachineMode(jsonObj.getString(ParserConstants.MACHINE_MODE)) ;
			airPurifierEvent.setFanSpeed(jsonObj.getString(ParserConstants.FAN_SPEED)) ;
			airPurifierEvent.setPowerMode(jsonObj.getString(ParserConstants.POWER_MODE)) ;
			String aqi = jsonObj.getString(ParserConstants.AQI) ;
			if(aqi != null && !aqi.equals(""))
				airPurifierEvent.setIndoorAQI(Integer.parseInt(aqi)) ;

			airPurifierEvent.setAqiL(Integer.parseInt(jsonObj.getString(ParserConstants.AQI_LIGHT))) ;
			airPurifierEvent.setAqiThreshold(Integer.parseInt(jsonObj.getString(ParserConstants.AQI_THRESHOLD))) ;
			airPurifierEvent.setDtrs(Integer.parseInt(jsonObj.getString(ParserConstants.DTRS))) ;
			airPurifierEvent.setFilterStatus1(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_1))) ;
			airPurifierEvent.setFilterStatus2(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_2))) ;
			airPurifierEvent.setFilterStatus3(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_3))) ;
			airPurifierEvent.setFilterStatus4(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_4))) ;
			airPurifierEvent.setReplaceFilter1(jsonObj.getString(ParserConstants.CLEAN_FILTER_1)) ;
			airPurifierEvent.setReplaceFilter2(jsonObj.getString(ParserConstants.REP_FILTER_2)) ;
			airPurifierEvent.setReplaceFilter3(jsonObj.getString(ParserConstants.REP_FILTER_3)) ;
			airPurifierEvent.setReplaceFilter4(jsonObj.getString(ParserConstants.REP_FILTER_4)) ;
			airPurifierEvent.setChildLock(Integer.parseInt(jsonObj.getString(ParserConstants.CL))) ;
			airPurifierEvent.setpSensor(Integer.parseInt(jsonObj.getString(ParserConstants.PSENS))) ;
			airPurifierEvent.settFav(Integer.parseInt(jsonObj.getString(ParserConstants.TFAV))) ;	
			airPurifierEvent.setActualFanSpeed(jsonObj.getString(ParserConstants.ACTUAL_FAN_SPEED));

		} catch (JSONException e) {
			return null ;
		}

		return airPurifierEvent ;
	}

	@Override
	public  List<IndoorHistoryDto> parseHistoryData() {
		//Log.i("PARSE", "Parse History Data\n"+dataToParse) ;
		List<IndoorHistoryDto> indoorHistoryList = null ;
		IndoorHistoryDto indoorAQIHistoryDto = null ;
		JSONObject jsonObject = null ;
			try {
				jsonObject = new JSONObject(dataToParse);
				JSONArray seriesJson = jsonObject.getJSONArray(ParserConstants.SERIES) ;
				Log.i("PARSE", "Series: "+seriesJson.length()) ;
				if( seriesJson != null && seriesJson.length() > 0 ) {
					indoorHistoryList = new ArrayList<IndoorHistoryDto>() ;
					int seriesArrayLength = seriesJson.length() ;
					for ( int index = 0 ; index < seriesArrayLength ; index ++ ) {
						JSONObject indoorJsonObj = seriesJson.getJSONObject(index) ;
						indoorAQIHistoryDto = new IndoorHistoryDto() ;
						indoorAQIHistoryDto.setTimeStamp(indoorJsonObj.getString(ParserConstants.TIMESTAMP_INDOORAQI_HISTORY)) ;
						
						JSONObject dataKeyValuePairJson = indoorJsonObj.getJSONObject(ParserConstants.DATA_VALUE_KEYPAIRS) ;
						if ( dataKeyValuePairJson != null ) {
							indoorAQIHistoryDto.setAqi(Float.parseFloat(dataKeyValuePairJson.getString(ParserConstants.AQI))) ;
							indoorAQIHistoryDto.setTfav(Integer.parseInt(dataKeyValuePairJson.getString(ParserConstants.TFAV))) ;
						}
						indoorHistoryList.add(indoorAQIHistoryDto) ;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i("PARSE", "EXCEPTION") ;
			}
			if (indoorHistoryList != null && indoorHistoryList.size() > 0 ) {
				Log.i("PARSE", "Size: "+indoorHistoryList.size()) ;
			}
			return indoorHistoryList ;
	}

	@Override
	public AirPurifierEventDto parseAirPurifierEventDataFromCPP()  {
		AirPurifierEventDto airPurifierEvent = null ;
		JSONObject jsonObject = null ;
		try {
			jsonObject = new JSONObject(dataToParse);
			JSONObject airPuriferJson = jsonObject.getJSONObject("data") ;
			airPurifierEvent = new AirPurifierEventDto() ;	

			airPurifierEvent.setMachineMode(airPuriferJson.getString(ParserConstants.MACHINE_MODE)) ;
			airPurifierEvent.setFanSpeed(airPuriferJson.getString(ParserConstants.FAN_SPEED)) ;
			airPurifierEvent.setPowerMode(airPuriferJson.getString(ParserConstants.POWER_MODE)) ;
			String aqi = airPuriferJson.getString(ParserConstants.AQI) ;
			if(aqi != null && !aqi.equals(""))
				airPurifierEvent.setIndoorAQI(Integer.parseInt(aqi)) ;

			airPurifierEvent.setAqiL(Integer.parseInt(airPuriferJson.getString(ParserConstants.AQI_LIGHT))) ;
			airPurifierEvent.setAqiThreshold(Integer.parseInt(airPuriferJson.getString(ParserConstants.AQI_THRESHOLD))) ;
			airPurifierEvent.setDtrs(Integer.parseInt(airPuriferJson.getString(ParserConstants.DTRS))) ;
			airPurifierEvent.setFilterStatus1(Integer.parseInt(airPuriferJson.getString(ParserConstants.FILTER_STATUS_1))) ;
			airPurifierEvent.setFilterStatus2(Integer.parseInt(airPuriferJson.getString(ParserConstants.FILTER_STATUS_2))) ;
			airPurifierEvent.setFilterStatus3(Integer.parseInt(airPuriferJson.getString(ParserConstants.FILTER_STATUS_3))) ;
			airPurifierEvent.setFilterStatus4(Integer.parseInt(airPuriferJson.getString(ParserConstants.FILTER_STATUS_4))) ;
			airPurifierEvent.setReplaceFilter1(airPuriferJson.getString(ParserConstants.CLEAN_FILTER_1)) ;
			airPurifierEvent.setReplaceFilter2(airPuriferJson.getString(ParserConstants.REP_FILTER_2)) ;
			airPurifierEvent.setReplaceFilter3(airPuriferJson.getString(ParserConstants.REP_FILTER_3)) ;
			airPurifierEvent.setReplaceFilter4(airPuriferJson.getString(ParserConstants.REP_FILTER_4)) ;
			airPurifierEvent.setChildLock(Integer.parseInt(airPuriferJson.getString(ParserConstants.CL))) ;
			airPurifierEvent.setpSensor(Integer.parseInt(airPuriferJson.getString(ParserConstants.PSENS))) ;
			airPurifierEvent.settFav(Integer.parseInt(airPuriferJson.getString(ParserConstants.TFAV))) ;

		} catch (JSONException e) {
			
		}catch(NumberFormatException nfe ) {
			ALog.e("Exception", "Number Format exception -- "+ nfe.getMessage()) ;
		}
		return airPurifierEvent ;
	}


	@Override
	public void parseOutdoorAQIData() {
		try {
			Gson gson = new GsonBuilder().create() ;
			OutdoorAQIEventDto outdoorAQI = gson.fromJson(dataToParse, OutdoorAQIEventDto.class) ;
			//Log.i("outdoor", "set outdoor aqi == " +outdoorAQI);
			SessionDto.getInstance().setOutdoorEventDto(outdoorAQI) ;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Weatherdto> parseWeatherData() {
		return new WeatherDataParser().parseWeatherData(dataToParse) ;
	}

}
