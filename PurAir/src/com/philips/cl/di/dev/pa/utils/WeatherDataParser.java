package com.philips.cl.di.dev.pa.utils;



import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.philips.cl.di.dev.pa.constants.ParserConstants;
import com.philips.cl.di.dev.pa.dto.Weatherdto;


public class WeatherDataParser {
	
	public List<Weatherdto> parseWeatherData(String dataToParse) {
		List<Weatherdto> weatherForecastList = null ;
		Weatherdto weatherDto = new Weatherdto()  ;
		String date = "";
		
		try {
			JSONObject jsonObj = new JSONObject(dataToParse) ;
			
			JSONObject dataObj = jsonObj.getJSONObject(ParserConstants.DATA) ;
			
			JSONArray currentCondition = dataObj.getJSONArray(ParserConstants.CURRENT_CONDITION) ;
			
			weatherForecastList = new ArrayList<Weatherdto>() ;
			
			JSONObject currentConditionObj = currentCondition.getJSONObject(0) ;
			
			weatherDto.setTempInCentigrade(Float.parseFloat(currentConditionObj.getString("temp_C"))) ;
			weatherDto.setTempInFahrenheit(Float.parseFloat(currentConditionObj.getString("temp_F"))) ;
			weatherDto.setTime(currentConditionObj.getString(ParserConstants.OBSERVATION_TIME)) ;
			weatherDto.setWeatherDesc(currentConditionObj.getJSONArray(ParserConstants.WEATHER_DESC).optJSONObject(0).getString(ParserConstants.VALUE));
			weatherDto.setIsdaytime(currentConditionObj.getString(ParserConstants.IS_DAY_TIME));
			weatherForecastList.add(weatherDto) ;
			
			JSONArray weatherArray = dataObj.getJSONArray(ParserConstants.WEATHER) ;
			
			if ( weatherArray != null && weatherArray.length() > 0 ) {				
				int length = weatherArray.length() ;
				
				for( int index = 0 ; index < length ; index ++ ) {
					JSONObject weatherJSON = weatherArray.getJSONObject(index) ;
					float maxTempC = Float.parseFloat(weatherJSON.getString(ParserConstants.MAXTEMPC)) ;
					float maxTempF = Float.parseFloat(weatherJSON.getString(ParserConstants.MAXTEMPF)) ;
					float minTempC = Float.parseFloat(weatherJSON.getString(ParserConstants.MINTEMPC)) ;
					float minTempF = Float.parseFloat(weatherJSON.getString(ParserConstants.MINTEMPF)) ;
					date = weatherJSON.getString(ParserConstants.DATE) ;
					
					JSONArray hourlyDetails = weatherJSON.getJSONArray(ParserConstants.HOURLY) ;
					
					if ( null != hourlyDetails ) {
						
						int hourlyDetailsLength = hourlyDetails.length() ;
						
						for( int i = 0 ; i < hourlyDetailsLength ; i ++ ) {
							JSONObject hourlyJSON = hourlyDetails.getJSONObject(i) ;
							weatherDto = new Weatherdto() ;
							weatherDto.setDate(date) ;
							weatherDto.setTempInCentigrade(Float.parseFloat(hourlyJSON.getString(ParserConstants.TEMP_C))) ;
							weatherDto.setTempInFahrenheit(Float.parseFloat(hourlyJSON.getString(ParserConstants.TEMP_F))) ;
							weatherDto.setTime(hourlyJSON.getString(ParserConstants.TIME)) ;
							weatherDto.setIsdaytime(hourlyJSON.getString(ParserConstants.IS_DAY_TIME));
							weatherDto.setWindSpeed(Float.parseFloat(hourlyJSON.getString(ParserConstants.WIND_SPEED))) ;
							weatherDto.setWindDirection(hourlyJSON.getString(ParserConstants.WIND_DIRECTION)) ;
							weatherDto.setWeatherDesc(hourlyJSON.getJSONArray(ParserConstants.WEATHER_DESC).optJSONObject(0).getString(ParserConstants.VALUE));
							weatherDto.setMaxTempC(maxTempC) ;
							weatherDto.setMaxTempF(maxTempF) ;
							weatherDto.setMinTempC(minTempC) ;
							weatherDto.setMinTempF(minTempF) ;
							
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
