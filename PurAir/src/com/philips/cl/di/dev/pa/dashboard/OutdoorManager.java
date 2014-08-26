package com.philips.cl.di.dev.pa.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.database.Cursor;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorLocationAbstractFillAsyncTask;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorLocationAbstractGetAsyncTask;
import com.philips.cl.di.dev.pa.util.ALog;

public class OutdoorManager implements OutdoorEventListener {

	private Map<String, OutdoorCity> citiesMap;
	private Map<String, OutdoorCity> citiesMapAll; // This is for entire city list
	private List<String> userCitiesList;
	private List<String> allCitiesList;
	private List<String> allMatchingCitiesList;

	private static OutdoorManager smInstance;

	private OutdoorDataChangeListener iListener;
	private OutdoorLocationAbstractGetAsyncTask mOutdoorLocationGetAsyncTask;
	private OutdoorLocationAbstractFillAsyncTask mOutdoorLocationFillAsyncTask; // Suppressed as this is a false positive.

	public synchronized static OutdoorManager getInstance() {
		if(smInstance == null) {
			smInstance = new OutdoorManager();
		}
		return smInstance;
	}
	
	public void startAllCitiesTask() {
		OutdoorController.getInstance().startAllCitiesAQITask();
	}
	
	public void startCitiesTask() {
		for(String areaID : userCitiesList) {
			System.out.println("manzer: check city data : " + areaID);
			if(citiesMap == null || citiesMap.get(areaID) == null || citiesMap.get(areaID).getOutdoorAQI() == null)  {
				OutdoorController.getInstance().startCityAQITask(areaID);
			}
			if(citiesMap == null || citiesMap.get(areaID) == null || citiesMap.get(areaID).getOutdoorWeather() == null) {
				OutdoorController.getInstance().startCityWeatherTask(areaID);
			}
		}
	}

	private OutdoorManager() {
		
		insertDataAndGetShortListCities();

		citiesMap = new HashMap<String, OutdoorCity>();
		citiesMapAll = new HashMap<String, OutdoorCity>();
		userCitiesList = new ArrayList<String>();
		allCitiesList = new ArrayList<String>();
		allMatchingCitiesList = new ArrayList<String>();
		
		WeatherIcon.populateWeatherIconMap();
		OutdoorController.getInstance().setOutdoorEventListener(this);
		ALog.i(ALog.DASHBOARD, "OutdoorManager$startCitiesTask: " + mOutdoorLocationFillAsyncTask);
	}

	private void insertDataAndGetShortListCities() {
		mOutdoorLocationGetAsyncTask = (OutdoorLocationAbstractGetAsyncTask) new OutdoorLocationAbstractGetAsyncTask() {

			@Override
			protected void onPostExecute(Cursor result) {
				fillCitiesListFromDatabase(result);
			}
		};

		mOutdoorLocationFillAsyncTask = (OutdoorLocationAbstractFillAsyncTask) new OutdoorLocationAbstractFillAsyncTask() {

			@Override
			protected void onPostExecute(Void result) {
				mOutdoorLocationGetAsyncTask.execute(new String[]{AppConstants.SQL_SELECTION_GET_SHORTLIST_ITEMS});
			}
		}.execute(new String[]{});
		
	}

	private void fillCitiesListFromDatabase(Cursor cursor) {

		if (cursor != null && cursor.getCount() > 0) {
			ALog.i(ALog.OUTDOOR_LOCATION, "Fetch list of cities already short listed from DB " + cursor.getCount());
			cursor.moveToFirst();
			do {
				String city = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY));
				String cityCN = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY_CN));
				String cityTW = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY_TW));
				String areaID = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AREA_ID));
				float longitude = cursor.getFloat(cursor.getColumnIndex(AppConstants.KEY_LONGITUDE));
				float latitude = cursor.getFloat(cursor.getColumnIndex(AppConstants.KEY_LATITUDE));

				ALog.i(ALog.OUTDOOR_LOCATION, "Add cities from DB to outdoor dashboard city " + city + " areaID " + areaID);
				OutdoorCityInfo info = new OutdoorCityInfo(city, cityCN, cityTW, longitude, latitude, areaID);
				OutdoorManager.getInstance().addAreaIDToUsersList(areaID);
				OutdoorManager.getInstance().addCityDataToMap(info, null, null, areaID);
			} while (cursor.moveToNext());
			OutdoorManager.getInstance().startCitiesTask();
		}
	}

	public void setUIChangeListener(OutdoorDataChangeListener listener){
		iListener=listener;
	}

	@Override
	public void outdoorAQIDataReceived(OutdoorAQI outdoorAQI, String areaID) {
		ALog.i(ALog.DASHBOARD, "outdoorAQIDataReceived " + outdoorAQI);
		if(outdoorAQI != null) {
			ALog.i(ALog.DASHBOARD, "OutdoorManager$outdoorAQIDataReceived aqi " + outdoorAQI.getPM25() + " : " + outdoorAQI.getAQI());
			addCityDataToMap(null, outdoorAQI, null, areaID);
			if (iListener != null) {
				iListener.updateUIOnDataChange();
			}
		}
	}

	@Override
	public void outdoorWeatherDataReceived(OutdoorWeather outdoorWeather, String areaID) {
		ALog.i(ALog.DASHBOARD, "outdoorWeatherDataReceived " + outdoorWeather);
		if(outdoorWeather != null) {
			ALog.i(ALog.DASHBOARD, "OutdoorManager$outdoorWeatherDataReceived temp " + outdoorWeather.getTemperature() + " : " + outdoorWeather.getWeatherIcon() + " : " + outdoorWeather.getHumidity());
			addCityDataToMap(null, null, outdoorWeather, areaID);
			if (iListener != null) {
				iListener.updateUIOnDataChange();
			}
		}
	}
	
	public List<String> getAllMatchingCitiesList(float latitudePlus, float latitudeMinus, float longitudePlus, float longitudeMinus){
		for (int i = 0; i < getAllCitiesList().size(); i++) {
			OutdoorCity outdoorCity = OutdoorManager.getInstance()
					.getCityDataAll(getAllCitiesList().get(i));
			float latitude = outdoorCity.getOutdoorCityInfo().getLatitude();
			
			if((latitude <  longitudePlus && latitude  >
				longitudeMinus)  &&  (latitude <
				latitudePlus &&  latitude  >
				latitudeMinus)){				
					allMatchingCitiesList.add(getAllCitiesList().get(i));
				}
		}
		
		return allMatchingCitiesList;
	}
	

	public synchronized List<String> getAllCitiesList() {
		return allCitiesList;
	}
	
	public synchronized List<String> getUsersCitiesList() {
		return userCitiesList;
	}
	
	public synchronized void clearCitiesList() {
		if (userCitiesList != null && !userCitiesList.isEmpty()) userCitiesList.clear();
	}

	public synchronized void addAreaIdToAllCitiesList(String areaID) {
		if(!allCitiesList.contains(areaID)) {
			ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorManager$addToAllCitiesList areaID " + areaID);
			allCitiesList.add(areaID);
		}
	}
	
	public synchronized void addAreaIDToUsersList(String areaID) {
		if(!userCitiesList.contains(areaID)) {
			ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorManager$addToUserCitiesList areaID " + areaID);
			//Add current location first position in the list
			if (areaID.equals(OutdoorController.getCurrentLocationAreaId())) {
				userCitiesList.add(0, areaID);
			} else {
				userCitiesList.add(areaID);
			}
		}
	}

	public void removeAreaIDFromUsersList(String areaID) {
		if(userCitiesList.contains(areaID)) {
			ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorManager$removeAreaIDFromList areaID " + areaID);
			userCitiesList.remove(areaID);
		}
	}

	public void addCityDataToMap(OutdoorCityInfo info, OutdoorAQI aqi, OutdoorWeather weather, String areaID) {
		ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorManager$addCityDataToMap areaID " + areaID);
		OutdoorCity city = citiesMap.get(areaID);
		System.out.println("manzer addCityDataToMap1: " + city);
		if(city == null) {
			city = new OutdoorCity();
		}
		if(info != null) city.setOutdoorCityInfo(info);
		if(aqi != null) city.setOutdoorAQI(aqi);
		if(weather != null) city.setOutdoorWeather(weather);
		citiesMap.put(areaID, city);
	}
	
	public void addAllCityDataToMap(OutdoorCityInfo info, OutdoorAQI aqi, OutdoorWeather weather, String areaID) {
		ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorManager$addAllCityDataToMap areaID " + areaID);
		OutdoorCity city = citiesMap.get(areaID);
		if(city == null) {
			city = new OutdoorCity();
		}
		if(info != null) city.setOutdoorCityInfo(info);
		if(aqi != null) city.setOutdoorAQI(aqi);
		if(weather != null) city.setOutdoorWeather(weather);
		citiesMapAll.put(areaID, city);
	}

	public void removeCityDataFromMap(String areaID) {
		if(citiesMap != null && citiesMap.containsKey(areaID)) {
			ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorManager$removeCityDataFromMap areaID " + areaID);
			citiesMap.remove(areaID);
		}
	}

	public OutdoorCity getCityData(String areaID) {
		ALog.i(ALog.DASHBOARD, "OutdoorManager$getCityData " + areaID);
		return citiesMap.get(areaID);
	}

	public OutdoorCity getCityDataAll(String areaID) {
		ALog.i(ALog.DASHBOARD, "OutdoorManager$getCityDataAll " + areaID);
		return citiesMapAll.get(areaID);
	}

	public void clearCityOutdoorInfo() {
		if(citiesMap != null && !citiesMap.isEmpty()) {
			Set<String> keys = citiesMap.keySet() ;
			Iterator<String> i = keys.iterator();
			while(i.hasNext()) {
				String areadID = i.next() ;
				citiesMap.get(areadID).setOutdoorAQI(null) ;
				citiesMap.get(areadID).setOutdoorWeather(null) ;
			}
			
		}
	}

	@Override
	public void allOutdoorAQIDataReceived(List<OutdoorAQI> aqis) {
		if(aqis == null || aqis.isEmpty()) return;
		for (OutdoorAQI outdoorAQI : aqis) {
			addAllCityDataToMap(null, outdoorAQI, null, outdoorAQI.getAreaID());
		}
	}
}
