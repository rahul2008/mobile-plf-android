package com.philips.cl.di.dev.pa.util;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.dashboard.ForecastCityDto;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorWeather;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.datamodel.DeviceDto;
import com.philips.cl.di.dev.pa.datamodel.DeviceWifiDto;
import com.philips.cl.di.dev.pa.datamodel.DiscoverInfo;
import com.philips.cl.di.dev.pa.datamodel.IndoorHistoryDto;
import com.philips.cl.di.dev.pa.datamodel.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;

/***
 * This class it used to parse data for AirPurifier event
 * @author 310124914
 *
 */
public class DataParser {

	private DataParser() {
	}

	public static AirPortInfo parseAirPurifierEventData(String dataToParse) {
		AirPortInfo airPurifierEvent = null ;
		try {			
			if( dataToParse != null ) {
				JSONObject jsonObj = new JSONObject(dataToParse) ;
				JSONObject airPuriferJson = jsonObj.optJSONObject("data") ;
				if( airPuriferJson != null ) {
					jsonObj = airPuriferJson ;
				}
				airPurifierEvent = new AirPortInfo() ;			

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
				airPurifierEvent.setChildLock(Integer.parseInt(jsonObj.getString(ParserConstants.CHILD_LOCK))) ;
				airPurifierEvent.setpSensor(Integer.parseInt(jsonObj.getString(ParserConstants.PSENS))) ;
				airPurifierEvent.settFav(Integer.parseInt(jsonObj.getString(ParserConstants.TFAV))) ;	
				airPurifierEvent.setActualFanSpeed(jsonObj.getString(ParserConstants.ACTUAL_FAN_SPEED));
			}
		} catch (JSONException e) {
			ALog.e(ALog.PARSER, "JSONException AirPortInfo") ;
			return null;
		} catch (JsonIOException e) {
			ALog.e(ALog.PARSER, "JsonIOException AirPortInfo");
			return null;
		} catch (JsonSyntaxException e2) {
			ALog.e(ALog.PARSER, "JsonSyntaxException AirPortInfo");
			return null;
		} catch (Exception e2) {
			ALog.e(ALog.PARSER, "Exception AirPortInfo");
			return null;
		}

		return airPurifierEvent ;
	}

	public static FirmwarePortInfo parseFirmwareEventData(String dataToParse) {
		Gson gson = new GsonBuilder().create();

		try {
			JSONObject jsonObj = new JSONObject(dataToParse) ;
			JSONObject firmwareEventJson = jsonObj.optJSONObject("data") ;
			if( firmwareEventJson != null ) {
				jsonObj = firmwareEventJson ;
			}
			FirmwarePortInfo firmwareEventDto = gson.fromJson(jsonObj.toString(), FirmwarePortInfo.class);
			if (firmwareEventDto.isValid()) {
				return firmwareEventDto;
			}
		} catch (JsonIOException e) {
			ALog.e(ALog.PARSER, "JsonIOException");
			return null;
		} catch (JsonSyntaxException e2) {
			ALog.e(ALog.PARSER, "JsonSyntaxException");
			return null;
		} catch (Exception e2) {
			ALog.e(ALog.PARSER, "Exception");
			return null;
		}
		return null;
	}

	public  static List<IndoorHistoryDto> parseHistoryData(String dataToParse) {
		//Log.i("PARSE", "Parse History Data\n"+dataToParse) ;
		List<IndoorHistoryDto> indoorHistoryList = null ;
		IndoorHistoryDto indoorAQIHistoryDto = null ;
		JSONObject jsonObject = null ;
		try {
			jsonObject = new JSONObject(dataToParse);
			JSONArray seriesJson = jsonObject.getJSONArray(ParserConstants.SERIES) ;

			if( seriesJson != null && seriesJson.length() > 0 ) {
				Log.i("PARSE", "Series: "+seriesJson.length()) ;
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
			ALog.e(ALog.PARSER, "JSONException") ;
		} catch (JsonIOException e) {
			ALog.e(ALog.PARSER, "JsonIOException");
		} catch (JsonSyntaxException e2) {
			ALog.e(ALog.PARSER, "JsonSyntaxException");
		} catch (Exception e2) {
			ALog.e(ALog.PARSER, "Exception");
			return null;
		}
		if (indoorHistoryList != null && indoorHistoryList.size() > 0 ) {
			Log.i("PARSE", "Size: "+indoorHistoryList.size()) ;
		}
		return indoorHistoryList ;
	}

	public static OutdoorAQIEventDto parseOutdoorAQIData(String dataToParse) {
		ALog.i(ALog.OUTDOOR_DETAILS, "parseOutdoorAQIData parsing");
		if (dataToParse == null) {
			return null;
		}
		try {
			Gson gson = new GsonBuilder().create() ;
			OutdoorAQIEventDto outdoorAQI = gson.fromJson(dataToParse, OutdoorAQIEventDto.class) ;
			return outdoorAQI;
		} catch (JsonSyntaxException e) {
			ALog.e(ALog.PARSER, "JsonSyntaxException");
			return null;
		} catch (JsonIOException ioe) {
			ALog.e(ALog.PARSER, "JsonIOException");
			return null;
		} catch (Exception e2) {
			ALog.e(ALog.PARSER, "Exception");
			return null;
		}
	}

	public static List<Weatherdto> parseWeatherData(String dataToParse) {
		if (dataToParse == null) {
			return null;
		}
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
							weatherDto.setWindDegree(Float.parseFloat(hourlyJSON.getString(ParserConstants.WIND_DEGREE))) ;
							weatherForecastList.add(weatherDto) ;
						}
					}
				}
			}

		} catch (JSONException e) {
			ALog.e(ALog.PARSER, "JSONException") ;
		} catch (JsonIOException e) {
			ALog.e(ALog.PARSER, "JsonIOException");
		} catch (JsonSyntaxException e2) {
			ALog.e(ALog.PARSER, "JsonSyntaxException");
		} catch (Exception e2) {
			ALog.e(ALog.PARSER, "Exception");
			return null;
		}
		return weatherForecastList ;
	}

	public static DeviceDto getDeviceDetails(String data) {
		if (data == null || data.isEmpty()) {
			return null;
		}
		Gson gson = new GsonBuilder().create() ;
		DeviceDto deviceDto = null;
		try {
			deviceDto = gson.fromJson(data, DeviceDto.class) ;
		} catch (JsonSyntaxException e) {
			ALog.e(ALog.PARSER, "JsonSyntaxException");
		} catch (JsonIOException e) {
			ALog.e(ALog.PARSER, "JsonIOException");
		} catch (Exception e2) {
			ALog.e(ALog.PARSER, "Exception");
		}
		return deviceDto;
	}

	public static DeviceWifiDto getDeviceWifiDetails(String data) {
		if (data == null || data.isEmpty()) {
			return null;
		}
		Gson gson = new GsonBuilder().create() ;
		DeviceWifiDto deviceWifiDto = null;
		try {
			deviceWifiDto = gson.fromJson(data, DeviceWifiDto.class) ;
		} catch (JsonSyntaxException e) {
			ALog.e(ALog.PARSER, "JsonSyntaxException");
		} catch (JsonIOException e) {
			ALog.e(ALog.PARSER, "JsonIOException");
		} catch (Exception e2) {
			ALog.e(ALog.PARSER, "Exception");
		}
		return deviceWifiDto;
	}

	public static List<OutdoorAQI> parseLocationAQI(String dataToParse, String areaID) {
		ALog.i(ALog.PARSER, "parseLocationAQI dataToParse " + dataToParse);
		List<OutdoorAQI> outdoorAQIList = new ArrayList<OutdoorAQI>();
		if( dataToParse == null ) return null ;
		try {
			JSONObject responseObject = new JSONObject(dataToParse);
			int pm25 = responseObject.getJSONObject("p").getInt("p1");
			int aqi = responseObject.getJSONObject("p").getInt("p2");

			ALog.i(ALog.PARSER, "pm25 " + pm25 + " aqi " + aqi);

//			outdoorAQIList.add(new OutdoorAQI(pm25, aqi, ""));
			return outdoorAQIList;
		} catch (JSONException e) {
			ALog.e(ALog.PARSER, "JSONException parseLocationAQI");
			return null;
		}
	}
	
	public static List<OutdoorWeather> parseLocationWeather(String dataToParse) {
		if( dataToParse == null ) return null ;
		
		List<OutdoorWeather> outdoorWeatherList = new ArrayList<OutdoorWeather>();
		
		try {
			JSONObject responseObject = new JSONObject(dataToParse);
			JSONObject observeObject = responseObject.getJSONObject("observe");
			
			Iterator<String> areaIDIterator = observeObject.keys();
			while(areaIDIterator.hasNext()) {
				String areaID = areaIDIterator.next();
				JSONObject cityData = observeObject.getJSONObject(areaID); //Area code

				JSONObject cityJsonObject = cityData.getJSONObject("l") ;
				int temperature = cityJsonObject.getInt("l1");
				int humidity = cityJsonObject.getInt("l2");
				int weatherIcon = cityJsonObject.getInt("l5");
				String time = cityJsonObject.getString("l7") ;

				ALog.i(ALog.PARSER, "parseLocationWeather temp : " + temperature + " humidity " + humidity + " weatherIcon " + weatherIcon);
				outdoorWeatherList.add(new OutdoorWeather(temperature, humidity, weatherIcon, areaID, time));
				
				
			}
			return outdoorWeatherList;
		} catch (JSONException e) {
			return null;
		}catch(Exception e) {
			return null ;
		}
	}

	public static List<SchedulePortInfo> parseSchedulerDto(String dataToParse) {
		if (dataToParse == null || dataToParse.isEmpty()) return null;
		ALog.i(ALog.SCHEDULER, dataToParse) ;
		List<SchedulePortInfo> schedulesList = new ArrayList<SchedulePortInfo>() ;
		JSONObject jsonObject = null ;
		try {			
			jsonObject = new JSONObject(dataToParse);
			JSONObject schedulerJsonFromCPP = jsonObject.optJSONObject("data") ;
			if( schedulerJsonFromCPP != null ) {
				jsonObject = schedulerJsonFromCPP ;
			}
			@SuppressWarnings("unchecked")
			Iterator<String> iterator = jsonObject.keys() ;
			String key = null ;
			while(iterator.hasNext()) {
				key = iterator.next() ;
				SchedulePortInfo schedules = new SchedulePortInfo() ;
				JSONObject schedule;
				schedule = jsonObject.getJSONObject(key);
				schedules.setName((String)schedule.get("name")) ;
				schedules.setScheduleNumber(Integer.parseInt(key)) ;
				schedulesList.add(schedules) ;
			}

		} catch (JSONException e) {
			schedulesList = null ;
			ALog.e(ALog.PARSER, "JsonIOException: " + e.getMessage());
		} catch(Exception e) {
			schedulesList = null ;
			ALog.e(ALog.PARSER, "JsonIOException : " + e.getMessage());
		}
		return schedulesList ;
	}
	
	public static SchedulePortInfo parseScheduleDetails(String dataToParse) {
		if (dataToParse == null || dataToParse.isEmpty()) return null;
		SchedulePortInfo schedulePortInfo = new SchedulePortInfo() ;
		try {
			JSONObject scheduleJson = new JSONObject(dataToParse) ;
			JSONObject scheduleJsonViaCPP = scheduleJson.optJSONObject("data") ;
			if(scheduleJsonViaCPP != null ) {
				scheduleJson = scheduleJsonViaCPP ;
			}
			schedulePortInfo.setName(scheduleJson.getString("name")) ;
			schedulePortInfo.setEnabled(scheduleJson.getBoolean("enabled")) ;
			schedulePortInfo.setDays(scheduleJson.getString("days")) ;
			schedulePortInfo.setMode(scheduleJson.getJSONObject("command").getString("om")) ;
			schedulePortInfo.setScheduleTime(scheduleJson.getString("time")) ;
 		} catch (JSONException e) {
			schedulePortInfo = null ;
			ALog.e(ALog.PARSER, "Exception: " + e.getMessage());
		} catch (Exception e) {
			schedulePortInfo = null ;
			ALog.e(ALog.PARSER, "Exception: " + e.getMessage());
		}
		return schedulePortInfo ;
	}
	
	public static DiscoverInfo parseDiscoverInfo(String dataToParse) {
		if (dataToParse== null || dataToParse.isEmpty()) return null;
		
		try {
			Gson gson = new GsonBuilder().create();
			DiscoverInfo info =  gson.fromJson(dataToParse, DiscoverInfo.class);
			
			if (!info.isValid()) return null;
			return info;
		} catch (JsonIOException e) {
			ALog.e(ALog.PARSER, "JsonIOException");
			return null;
		} catch (JsonSyntaxException e2) {
			ALog.e(ALog.PARSER, "JsonSyntaxException");
			return null;
		}
	}
	
	public static ForecastCityDto parseForecastCityData(String dataToParse, String areaID) {
		JSONObject cityJson = null;
		try {
			cityJson = new JSONObject(dataToParse);
		} catch (JSONException e) {
			e.printStackTrace();
			ALog.e(ALog.PARSER, "Parse ERROR in parseForecastCityData " + e.getMessage());
			return null;
		}
		JSONObject temp = cityJson.optJSONObject("forecast4d");
		if(temp == null) return null;
		
		JSONObject temp2 = temp.optJSONObject(areaID).optJSONObject("c");
		Gson gson = new GsonBuilder().create();
		ForecastCityDto cityDto =  gson.fromJson(temp2.toString(), ForecastCityDto.class);
		if(cityDto == null) return null; 

		return cityDto;
	}
	
	public static List<ForecastWeatherDto> parseFourDaysForecastData(String dataToParse, String areaID) {
		JSONObject cityJson = null;
		try {
			cityJson = new JSONObject(dataToParse);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject temp = cityJson.optJSONObject("forecast4d");
		if(temp == null) return null;
		ALog.i(ALog.PARSER, "parseFourDaysForecastData temp F :: " + temp.optJSONObject(areaID).optJSONObject("f").optJSONArray("f1"));

		JSONArray forecastArray = temp.optJSONObject(areaID).optJSONObject("f").optJSONArray("f1");
		ALog.i(ALog.PARSER, "forecastArray " + forecastArray.length());
		
		List<ForecastWeatherDto> weatherDtos = new ArrayList<ForecastWeatherDto>();
		Gson gson = new GsonBuilder().create();
		for (int i = 0; i < forecastArray.length(); i++) {
			try {
				ALog.i(ALog.PARSER, "Forecast for day " + i + " :: " + forecastArray.getJSONObject(i));
				ForecastWeatherDto dto = gson.fromJson(forecastArray.getJSONObject(i).toString(), ForecastWeatherDto.class);
				weatherDtos.add(dto);
				return weatherDtos;
			} catch (JSONException e) {
				ALog.e(ALog.PARSER, "Parse ERROR in parseFourDaysForecastData " + e.getMessage());
			}
		}
		
		ALog.i(ALog.PARSER, "ForecastCityDto weatherDtos :: " + weatherDtos);
		
		return null;
	}
	
	public static List<OutdoorAQI> parseHistoricalAQIData(String dataToParse, String areaID) {
		try {
			JSONObject historicalAQIObject = new JSONObject(dataToParse);
			ALog.i(ALog.PARSER, "historicalAQIObject " + historicalAQIObject);
			JSONArray historicalAQIs = historicalAQIObject.optJSONArray("p");
			if(historicalAQIs == null) return null;
			List<OutdoorAQI> outdoorAQIs = new ArrayList<OutdoorAQI>();
			ALog.i(ALog.PARSER, "historicalAQIs length " + historicalAQIs.length());
			
			for (int i = 0; i < historicalAQIs.length(); i++) {
				ALog.i(ALog.PARSER, "historicalAQIs.getJSONObject(i) " + historicalAQIs.getJSONObject(i));
				int pm25 = historicalAQIs.getJSONObject(i).optInt("p1");
				int aqi = historicalAQIs.getJSONObject(i).optInt("p2");
				int pm10 = historicalAQIs.getJSONObject(i).optInt("p3");
				int so2 = historicalAQIs.getJSONObject(i).optInt("p4");
				int no2 = historicalAQIs.getJSONObject(i).optInt("p5");
				
				outdoorAQIs.add(new OutdoorAQI(pm25, aqi, pm10, so2, no2, areaID));
			}
//			ALog.i(ALog.PARSER, "historicalAQIs length " + outdoorAQIs);
			return outdoorAQIs;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static List<Weatherdto> getHourlyWeatherData(String data) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = null;
		WeatherHourlyDataParser handler = null;
		List<Weatherdto> weatherList = null ;
		try {
			saxParser = saxParserFactory.newSAXParser();
			handler = new WeatherHourlyDataParser();
			saxParser.parse(new ByteArrayInputStream(data.getBytes(Charset.defaultCharset())), handler);
			weatherList = handler.getWeatherForecastHourlyList() ;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return weatherList;
	}
}
