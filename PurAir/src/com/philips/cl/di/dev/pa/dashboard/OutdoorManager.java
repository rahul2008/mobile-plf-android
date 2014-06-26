package com.philips.cl.di.dev.pa.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.philips.cl.di.dev.pa.util.ALog;

public class OutdoorManager implements OutdoorEventListener {
	
	private Map<String, OutdoorCity> citiesMap;
	private List<String> citiesList;
	
	private static OutdoorManager smInstance;
	
	private OutdoorDataChangeListener iListener;
	
	public static OutdoorManager getInstance() {
		if(smInstance == null) {
			smInstance = new OutdoorManager();
		}
		return smInstance;
	}
	
	public void startCitiesTask() {
		for(String areaID : citiesList) {
			if(citiesMap == null || citiesMap.get(areaID) == null || citiesMap.get(areaID).getOutdoorAQI() == null)  {
				OutdoorController.getInstance().startCitiesTask(areaID);
			}
			if(citiesMap == null || citiesMap.get(areaID) == null || citiesMap.get(areaID).getOutdoorWeather() == null) {
				OutdoorController.getInstance().startOutdoorWeatherTask(areaID);
			}
		}
	}
	
	private OutdoorManager() {
		citiesMap = new HashMap<String, OutdoorCity>();
		citiesList = new ArrayList<String>();
		
		OutdoorController.getInstance().setOutdoorEventListener(this);
		ALog.i(ALog.DASHBOARD, "OutdoorManager$startCitiesTask");
	}
	
	public void setUIChangeListener(OutdoorDataChangeListener listener){
		iListener=listener;
	}
	
	@Override
	public void outdoorAQIDataReceived(OutdoorAQI outdoorAQI, String areaID) {
		ALog.i(ALog.DASHBOARD, "outdoorAQIDataReceived " + outdoorAQI);
		if(outdoorAQI != null) {
			ALog.i(ALog.DASHBOARD, "OutdoorManager$outdoorAQIDataReceived aqi " + outdoorAQI.getPM25() + " : " + outdoorAQI.getAQI() + " : " + outdoorAQI.getPublishTime());
			addCityDataToMap(areaID, null, outdoorAQI, null);
			iListener.updateUIOnDataChange();
		}
	}

	@Override
	public void outdoorWeatherDataReceived(OutdoorWeather outdoorWeather, String areaID) {
		ALog.i(ALog.DASHBOARD, "outdoorWeatherDataReceived " + outdoorWeather);
		if(outdoorWeather != null) {
			ALog.i(ALog.DASHBOARD, "OutdoorManager$outdoorWeatherDataReceived temp " + outdoorWeather.getTemperature() + " : " + outdoorWeather.getWeatherIcon() + " : " + outdoorWeather.getHumidity());
			addCityDataToMap(areaID, null, null, outdoorWeather);
			iListener.updateUIOnDataChange();
		}
	}
	
	public List<String> getCitiesList() {
		return citiesList;
	}
	
	public void addAreaIDToList(String areaID) {
		if(!citiesList.contains(areaID)) {
			ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorManager$addToCitiesList areaID " + areaID);
			citiesList.add(areaID);
		}
	}
	
	public void addCityDataToMap(String areaID, String cityName, OutdoorAQI aqi, OutdoorWeather weather) {
		ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorManager$addCityDataToMap areaID " + areaID + " cityName " + cityName + " aqi " + aqi + " weather " + weather);
		OutdoorCity city = citiesMap.get(areaID);
		if(city == null) {
			city = new OutdoorCity();
		}
		if(cityName != null && !cityName.isEmpty())city.setCityName(cityName);
		if(aqi != null) city.setOutdoorAQI(aqi);
		if(weather != null) city.setOutdoorWeather(weather);
		citiesMap.put(areaID, city);
	}
	
	public OutdoorCity getCityData(String areaID) {
		ALog.i(ALog.DASHBOARD, "OutdoorManager$getCityData " + areaID);
		return citiesMap.get(areaID);
	}
	
}
