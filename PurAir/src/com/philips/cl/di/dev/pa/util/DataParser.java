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
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.outdoorlocations.CMACityData;
import com.philips.cl.di.dev.pa.outdoorlocations.NearbyCitiesData;
import com.philips.cl.di.dev.pa.outdoorlocations.USEmbassyCityData;

/***
 * This class it used to parse data for AirPurifier event
 * @author 310124914
 *
 */
public class DataParser {

	// TODO: DIComm refactor, remove this method after refactoring request architecture
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
				airPurifierEvent.setPreFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_1))) ;
				airPurifierEvent.setMulticareFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_2))) ;
				airPurifierEvent.setActiveFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_3))) ;
				airPurifierEvent.setHepaFilterStatus(Integer.parseInt(jsonObj.getString(ParserConstants.FILTER_STATUS_4))) ;
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

	public static List<OutdoorAQI> parseLocationAQI(String dataToParse) {
		ALog.i(ALog.PARSER, "parseLocationAQI dataToParse " + dataToParse);
		if( dataToParse == null ) return null ;

		List<OutdoorAQI> aqis = new ArrayList<OutdoorAQI>();

		try {
			JSONArray responseObject = new JSONObject(dataToParse).optJSONArray("p");
			if(responseObject == null) return null;
			JSONObject observeObject = responseObject.getJSONObject(0);

			Iterator<String> areaIDIterator = observeObject.keys();
			while(areaIDIterator.hasNext()) {
				String areaID = areaIDIterator.next();
				JSONObject cityData = observeObject.getJSONObject(areaID); //Area codes
				int pm25 = cityData.optInt("p1");
				int aqi = cityData.optInt("p2");
				int pm10 = 	cityData.optInt("p3");
				int so2 = cityData.optInt("p4");
				int no2 = cityData.optInt("p5");
				String timeStamp = cityData.optString("updatetime");
				ALog.i(ALog.PARSER, "pm25 " + pm25 + " aqi " + aqi);
				aqis.add(new OutdoorAQI(pm25, aqi, pm10, so2, no2, areaID, timeStamp));
			}

			return aqis;
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
				String time = cityJsonObject.getString("l7") ;
				int temperature = 0, humidity = 0, weatherIcon = 0 , windspeedID = 0 , winddirectionID = 0;
				try {
					temperature = Integer.parseInt(cityJsonObject.getString("l1"));
					humidity = Integer.parseInt(cityJsonObject.getString("l2"));
					weatherIcon = Integer.parseInt(cityJsonObject.getString("l5"));
					windspeedID = Integer.parseInt(cityJsonObject.getString("l3"));
					winddirectionID = Integer.parseInt(cityJsonObject.getString("l4"));
					
					
					outdoorWeatherList.add(new OutdoorWeather(temperature, humidity, weatherIcon, areaID, time, windspeedID, winddirectionID));
				}catch(NumberFormatException nfe) {
					
				}
				ALog.i(ALog.PARSER, "parseLocationWeather temp : " + temperature + " humidity " + humidity + " weatherIcon " + weatherIcon);
			}
			return outdoorWeatherList;
		} catch (JSONException e) {
			ALog.e(ALog.PARSER, "ERROR parseLocationWeather");
			return null;
		}catch(Exception e) {
			return null ;
		}
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
			ALog.e(ALog.PARSER, "Parse ERROR in parseForecastCityData " + "Error: " + e.getMessage());
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

	public static List<ForecastWeatherDto> parseFourDaysForecastData(String dataToParse) {
		ALog.i(ALog.PARSER, "parseFourDaysForecastDat dataToParse: " + dataToParse);
		JSONObject cityJson = null;
		try {
			cityJson = new JSONObject(dataToParse);
		} catch (JSONException e) {
			e.printStackTrace();
			return null ;
		}
		String areaID = "";
		JSONObject temp = cityJson.optJSONObject("forecast4d");
		if(temp == null) return null;
		Iterator<String> iter = temp.keys();
		if(iter.hasNext()) {
			areaID = iter.next();
		}
		
		if(temp == null || temp.isNull(areaID)) return null;
		if(temp.optJSONObject(areaID) == null) return null;
		if(temp.optJSONObject(areaID).optJSONObject("f") == null) return null;

		JSONArray forecastArray = temp.optJSONObject(areaID).optJSONObject("f").optJSONArray("f1");
		ALog.i(ALog.PARSER, "forecastArray " + forecastArray.length());

		List<ForecastWeatherDto> weatherDtos = new ArrayList<ForecastWeatherDto>();
		Gson gson = new GsonBuilder().create();
		for (int i = 0; i < forecastArray.length(); i++) {
			try {
				ALog.i(ALog.PARSER, "Forecast for day " + i + " :: " + forecastArray.getJSONObject(i));
				ForecastWeatherDto dto = gson.fromJson(forecastArray.getJSONObject(i).toString(), ForecastWeatherDto.class);
				weatherDtos.add(dto);
				//				return weatherDtos;
			} catch (JSONException e) {
				ALog.e(ALog.PARSER, "Parse ERROR in parseFourDaysForecastData " + "Error: " + e.getMessage());
			}
		}

		ALog.i(ALog.PARSER, "ForecastCityDto weatherDtos :: " + weatherDtos);

		return weatherDtos;
	}

	public static List<OutdoorAQI> parseHistoricalAQIData(String dataToParse) {
		try {
			JSONObject historicalAQIObject = new JSONObject(dataToParse);
			JSONArray historicalAQIs = historicalAQIObject.optJSONArray("p");
			if(historicalAQIs == null) return null;
			List<OutdoorAQI> outdoorAQIs = new ArrayList<OutdoorAQI>();
			ALog.i(ALog.PARSER, "historicalAQIs length " + historicalAQIs.length());
			String areaID = "";
			Iterator<String> iter = historicalAQIs.getJSONObject(0).keys();
			if(iter != null) {
				areaID = iter.next();
			}

			for (int i = 0; i < historicalAQIs.length(); i++) {
				int pm25 = historicalAQIs.getJSONObject(i).optInt("p1");
				int aqi = historicalAQIs.getJSONObject(i).optInt("p2");
				int pm10 = historicalAQIs.getJSONObject(i).optInt("p3");
				int so2 = historicalAQIs.getJSONObject(i).optInt("p4");
				int no2 = historicalAQIs.getJSONObject(i).optInt("p5");
				String timeStamp = historicalAQIs.getJSONObject(i).optString("updatetime");
				outdoorAQIs.add(new OutdoorAQI(pm25, aqi, pm10, so2, no2, areaID, timeStamp));
			}
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

	public static List<OutdoorAQI> parseUSEmbassyLocationAQI(String dataToParse, String areaID) {
		ALog.i(ALog.PARSER, "parseUSEmbassyLocationAQI dataToParse " + dataToParse);
		
		try {
			JSONObject historicalAQIObject = new JSONObject(dataToParse);
			JSONArray historicalAQIs = historicalAQIObject.optJSONArray("result");
			if(historicalAQIs == null) return null;

			JSONObject AQIData = historicalAQIs.getJSONObject(0);
			if(AQIData == null) return null;

			List<OutdoorAQI> outdoorAQIs = new ArrayList<OutdoorAQI>();
			
			JSONObject cityNow= AQIData.getJSONObject("citynow");
			String timeStamp = cityNow.getString("date");
			JSONObject lastMoniData= AQIData.getJSONObject("lastMoniData");
			int aqi = 0;
			int pm25 = 0;
			if (lastMoniData != null) {
				for (int j = 0; j < lastMoniData.length() ; j++) {
					JSONObject individualAQIData = lastMoniData.getJSONObject(String.valueOf(j + 1));
					if(("美国领事馆".equals(individualAQIData.get("city"))) || ("美国大使馆".equals(individualAQIData.get("city")))) {
						aqi = individualAQIData.getInt("America_AQI");
						pm25 = individualAQIData.getInt("PM2.5Hour");
					}
				}
			}

			outdoorAQIs.add(new OutdoorAQI(pm25, aqi, 0, 0, 0, areaID.toLowerCase(), timeStamp));
			return outdoorAQIs;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<OutdoorAQI> parseUSEmbassyHistoricalAQIData(String dataToParse) {
		try {
			JSONObject historicalAQIObject = new JSONObject(dataToParse);
			JSONArray historicalAQIs = historicalAQIObject.optJSONArray("result");
			if(historicalAQIs == null) return null;

			JSONObject AQIData = historicalAQIs.getJSONObject(0);
			if(AQIData == null) return null;

			List<OutdoorAQI> outdoorAQIs = new ArrayList<OutdoorAQI>();

			JSONObject lastTwoWeeksData= AQIData.getJSONObject("lastTwoWeeks");			
			if (lastTwoWeeksData != null) {
				for (int j = 0; j < lastTwoWeeksData.length() ; j++) {
					JSONObject individualAQIData = lastTwoWeeksData.getJSONObject(String.valueOf(j + 1));
					outdoorAQIs.add(getOutdoorAQIValues(individualAQIData));
				}
			}

			JSONObject cityNowData = AQIData.getJSONObject("citynow");
			outdoorAQIs.add(getOutdoorAQIValues(cityNowData));
			return outdoorAQIs;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static CMACityData parseCMACityData(String data) {
		if (data == null || data.isEmpty()) {
			return null;
		}
		Gson gson = new GsonBuilder().create() ;
		CMACityData cmaCityData = null;
		try {
			cmaCityData = gson.fromJson(data, CMACityData.class) ;
		} catch (JsonSyntaxException e) {
			ALog.e(ALog.PARSER, "JsonSyntaxException");
		} catch (JsonIOException e) {
			ALog.e(ALog.PARSER, "JsonIOException");
		} catch (Exception e2) {
			ALog.e(ALog.PARSER, "Exception");
		}
		return cmaCityData;
	}
	
	public static USEmbassyCityData parseUSEmbassyCityData(String data) {
		if (data == null || data.isEmpty()) {
			return null;
		}
		Gson gson = new GsonBuilder().create() ;
		USEmbassyCityData usEmbassyCityData = null;
		try {
			usEmbassyCityData = gson.fromJson(data, USEmbassyCityData.class) ;
		} catch (JsonSyntaxException e) {
			ALog.e(ALog.PARSER, "JsonSyntaxException");
		} catch (JsonIOException e) {
			ALog.e(ALog.PARSER, "JsonIOException");
		} catch (Exception e2) {
			ALog.e(ALog.PARSER, "Exception");
		}
		return usEmbassyCityData;
	}
	
	public static NearbyCitiesData parseNearbyCitiesJson(String data) {
		if (data == null || data.isEmpty()) {
			return null;
		}
		Gson gson = new GsonBuilder().create() ;
		try{
			NearbyCitiesData nearbyCitiesData = gson.fromJson(data, NearbyCitiesData.class);
			return nearbyCitiesData;
		} catch (JsonSyntaxException e) {
			ALog.e(ALog.PARSER, "JsonSyntaxException");
			return null;
		} catch (JsonIOException e) {
			ALog.e(ALog.PARSER, "JsonIOException");
			return null;
		} catch (Exception e2) {
			ALog.e(ALog.PARSER, "Exception");
			return null;
		}

	}

	private static OutdoorAQI getOutdoorAQIValues(JSONObject jsonObject) {

		String city = jsonObject.optString("city");
		int aqi = jsonObject.optInt("AQI");
		String quality = jsonObject.optString("quality");
		String timeStamp = jsonObject.optString("date");

		return new OutdoorAQI(0, aqi, 0, 0, 0, city.toLowerCase(), timeStamp);
	}

}
