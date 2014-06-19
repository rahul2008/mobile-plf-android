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
			if(citiesMap == null || citiesMap.get(areaID) == null )  {
				OutdoorController.getInstance().startCitiesTask(areaID);
				OutdoorController.getInstance().startOutdoorWeatherTask(areaID);
			}
		}
	}
	
	private OutdoorManager() {
		citiesMap = new HashMap<String, OutdoorCity>();
		citiesList = new ArrayList<String>();
		//Hard coded city area codes for UT3
		//TODO : Remove hard coding
		citiesList.add("101010100"); //Beijing
		citiesList.add("101020100"); //Shanghai
		citiesList.add("101270101"); //Cheng Du
		
		startCitiesTask();
		
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
			OutdoorCity city = citiesMap.get(areaID);
			if(city == null) {
				city = new OutdoorCity();
			}
			city.setOutdoorAQI(outdoorAQI);
			citiesMap.put(areaID, city);
			iListener.updateUIOnDataChange();
		}
	}

	@Override
	public void outdoorWeatherDataReceived(OutdoorWeather outdoorWeather, String areaID) {
		ALog.i(ALog.DASHBOARD, "outdoorWeatherDataReceived " + outdoorWeather);
		if(outdoorWeather != null) {
			ALog.i(ALog.DASHBOARD, "OutdoorManager$outdoorWeatherDataReceived temp " + outdoorWeather.getTemperature() + " : " + outdoorWeather.getWeatherIcon() + " : " + outdoorWeather.getHumidity());
			OutdoorCity city = citiesMap.get(areaID);
			if(city == null) {
				city = new OutdoorCity();
			}
			city.setOutdoorWeather(outdoorWeather);
			citiesMap.put(areaID, city);
			iListener.updateUIOnDataChange();
		}
	}
	
	public List<String> getCitiesList() {
		return citiesList;
	}
	
	public OutdoorCity getCityData(String areaID) {
		ALog.i(ALog.DASHBOARD, "OutdoorManager$getCityData " + areaID);
		return citiesMap.get(areaID);
	}
	
}
