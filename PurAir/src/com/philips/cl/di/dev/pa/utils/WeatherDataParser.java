package com.philips.cl.di.dev.pa.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.constants.ParserConstants;
import com.philips.cl.di.dev.pa.dto.Weatherdto;


public class WeatherDataParser {
	
	public List<Weatherdto> parseWeatherData(String dataToParse) {
		List<Weatherdto> weatherForecastList = null ;
		Weatherdto weatherDto = new Weatherdto()  ;
		String date = AppConstants.EMPTY_STRING ;
		
		try {
			JSONObject jsonObj = new JSONObject(dataToParse) ;
			
			JSONObject dataObj = jsonObj.getJSONObject(ParserConstants.DATA) ;
			
			JSONArray currentCondition = dataObj.getJSONArray(ParserConstants.CURRENT_CONDITION) ;
			
			weatherForecastList = new ArrayList<Weatherdto>() ;
			
			JSONObject currentConditionObj = currentCondition.getJSONObject(0) ;
			
			weatherDto.setTempInCentigrade(Float.parseFloat(currentConditionObj.getString("temp_C"))) ;
			weatherDto.setTempInFahrenheit(Float.parseFloat(currentConditionObj.getString("temp_F"))) ;
			weatherDto.setTime(currentConditionObj.getString(ParserConstants.OBSERVATION_TIME)) ;
			weatherForecastList.add(weatherDto) ;
			
			JSONArray weatherArray = dataObj.getJSONArray(ParserConstants.WEATHER) ;
			
			if ( weatherArray != null && weatherArray.length() > 0 ) {
				
				
				int length = weatherArray.length() ;
				
				for( int index = 0 ; index < length ; index ++ ) {
					JSONObject weatherJSON = weatherArray.getJSONObject(index) ;
					
					date = weatherJSON.getString(ParserConstants.DATE) ;
					
					JSONArray hourlyDetails = weatherJSON.getJSONArray(ParserConstants.HOURLY) ;
					
					if ( null != hourlyDetails ) {
						weatherDto = new Weatherdto() ;
						int hourlyDetailsLength = hourlyDetails.length() ;
						weatherDto.setDate(date) ;
						for( int i = 0 ; i < hourlyDetailsLength ; i ++ ) {
							JSONObject hourlyJSON = hourlyDetails.getJSONObject(i) ;
							weatherDto.setTempInCentigrade(Float.parseFloat(hourlyJSON.getString(ParserConstants.TEMP_C))) ;
							weatherDto.setTempInFahrenheit(Float.parseFloat(hourlyJSON.getString(ParserConstants.TEMP_F))) ;
							weatherDto.setTime(hourlyJSON.getString(ParserConstants.TIME)) ;
							
							weatherForecastList.add(weatherDto) ;
						}
					}
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return weatherForecastList ;
	}
}
