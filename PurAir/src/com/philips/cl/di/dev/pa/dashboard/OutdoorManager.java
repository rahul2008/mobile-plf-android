package com.philips.cl.di.dev.pa.dashboard;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.outdoorlocations.CMACityData;
import com.philips.cl.di.dev.pa.outdoorlocations.CMACityData.CMACityDetail;
import com.philips.cl.di.dev.pa.outdoorlocations.DataCommunicatorStrategy;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorJsonReader;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorLocationAbstractFillAsyncTask;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorLocationAbstractGetAsyncTask;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorLocationDatabase;
import com.philips.cl.di.dev.pa.outdoorlocations.USEmbassyCityData;
import com.philips.cl.di.dev.pa.outdoorlocations.USEmbassyCityData.USEmbassyCityDetail;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.OutdoorDetailsListener;

public class OutdoorManager implements OutdoorDataListener {

	private Map<String, OutdoorCity> citiesMap;
	private List<String> cmaCities;
	private List<String> usEmbassyCities;
	private List<String> userCitiesList;
	private List<String> nearbyCities;
	
	private OutdoorJsonReader outdoorJsonReader;
	private DataCommunicatorStrategy dataCommunicatorStrategy;

	private static OutdoorManager smInstance;

	private OutdoorDataChangeListener outdoorDataChangeListener;
	private AllOutdoorDataListener allOutdoorDataListener;
	private OutdoorDetailsListener outdoorDetailsListener ;

	private OutdoorLocationAbstractGetAsyncTask mOutdoorLocationGetAsyncTask; //TODO : Remove in next version.
	private OutdoorLocationAbstractFillAsyncTask mOutdoorLocationFillAsyncTask; //TODO : Remove in next version.
	private long lastUpdatedTime ;

	private static final int UPDATE_INTERVAL = 10 ; // in mins

	public synchronized static OutdoorManager getInstance() {
		if (smInstance == null) {
			smInstance = new OutdoorManager();
		}
		return smInstance;
	}

	public void startAllCitiesTask() {
		dataCommunicatorStrategy.requestAllCityData(cmaCities);
	}

	public synchronized void startCitiesTask() {
		if (userCitiesList.isEmpty()) return;
		boolean isUpdated = false;
		if (lastUpdatedTime == 0 || getDiffInTimeInMins(lastUpdatedTime)  >= UPDATE_INTERVAL) {
			isUpdated = true;
			dataCommunicatorStrategy.requestCityAQIData(userCitiesList);
		}

		if (lastUpdatedTime == 0 || getDiffInTimeInMins(lastUpdatedTime)  >= UPDATE_INTERVAL) {
			isUpdated = true;
			dataCommunicatorStrategy.requestCityWeatherData(userCitiesList);
		}			
		if(isUpdated) {
			lastUpdatedTime = System.currentTimeMillis();
		}
	}
	
	public void startHistoricalAQITask(String areaId) {
		List<String> city = new ArrayList<String>();
		city.add(areaId);
		dataCommunicatorStrategy.requestHistoricalAQIData(city);
	}
	
	public void startOneDayWeatherForecastTask(String areaId) {
		List<String> city = new ArrayList<String>();
		city.add(areaId);
		dataCommunicatorStrategy.requestOneDayWeatherForecastData(city);
	}
	
	public void startCityFourDayForecastTask(String areaId) {
		List<String> city = new ArrayList<String>();
		city.add(areaId);
		dataCommunicatorStrategy.requestFourDayWeatherForecastData(city);
	}
	
	public void startFourDayWeatherForecastTask(String areaId) {
		List<String> city = new ArrayList<String>();
		city.add(areaId);
		dataCommunicatorStrategy.requestFourDayWeatherForecastData(city);
	}
	
	public void resetUpdatedTime() {
		lastUpdatedTime = 0;
	}

	private int getDiffInTimeInMins(long time) {
		int timeDiffInMins = (int) ((System.currentTimeMillis() - time) / (1000 * 60)); 
		return timeDiffInMins ;
	}

	
	private OutdoorManager() {
		
		insertDataAndGetShortListCities();
		outdoorJsonReader = new OutdoorJsonReader();
		dataCommunicatorStrategy = new DataCommunicatorStrategy(this);
		citiesMap = new HashMap<String, OutdoorCity>();
		userCitiesList = new ArrayList<String>();
		cmaCities = new ArrayList<String>();
		usEmbassyCities = new ArrayList<String>();
		saveCMACitiesInMapAndList(outdoorJsonReader.readCMACityJsonAsString());
		saveUSEmbassyCitiesInMapAndList(outdoorJsonReader.readUSEmbassyCityJsonAsString());
		
		nearbyCities = new ArrayList<String>();

		WeatherIcon.populateWeatherIconMap();
		
		ALog.i(ALog.DASHBOARD, "OutdoorManager$startCitiesTask: " + mOutdoorLocationFillAsyncTask);
	}

	private void saveCMACitiesInMapAndList(String cmaCitiesJsonAsString) {
		CMACityData cmaCityData = DataParser.parseCMACityData(cmaCitiesJsonAsString);
		List<CMACityDetail> cmList = cmaCityData.getCmaCitiesData();
		for (CMACityDetail cmaCityDetail : cmList) {
			OutdoorCityInfo info = new OutdoorCityInfo(cmaCityDetail.getNameEN(), cmaCityDetail.getNameCN(), cmaCityDetail.getNameTW(), Float.parseFloat(cmaCityDetail.getLongitude()), Float.parseFloat(cmaCityDetail.getLatitude()), cmaCityDetail.getAreaID());
			String areaId = cmaCityDetail.getAreaID();
			cmaCities.add(areaId);
			addCityDataToMap(info, null, null, areaId);
		}
	}
	
	private void saveUSEmbassyCitiesInMapAndList(String usEmbassyCityJsonAsString) {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData(usEmbassyCityJsonAsString);
		List<USEmbassyCityDetail> usEmbassyList = usEmbassyCityData.getUSEmbassyCitiesData();
		for (USEmbassyCityDetail embassyCityDetail : usEmbassyList) {
			OutdoorCityInfo info = new OutdoorCityInfo(embassyCityDetail.getNameEN(), embassyCityDetail.getNameCN(), embassyCityDetail.getNameTW(), Float.parseFloat(embassyCityDetail.getLongitude()), Float.parseFloat(embassyCityDetail.getLatitude()), embassyCityDetail.getAreaID());
			String areaId = embassyCityDetail.getNameEN();
			usEmbassyCities.add(areaId);
			addCityDataToMap(info, null, null, areaId);
		}
	}

	public List<String> getCMACities() {
		return cmaCities;
	}
	
	public List<String> getUSEmbassyCities() {
		return usEmbassyCities;
	}
	
	public String getAreaIdFromCityName(String cityName) {
		OutdoorCity city = citiesMap.get(cityName);
		if(city != null) {
			OutdoorCityInfo info = city.getOutdoorCityInfo();
			if(info != null) {
				return info.getAreaID();
			}
		}
		return "";
	}
	
	public String getCityNameFromAreaId(String areaId) {
		OutdoorCity city = citiesMap.get(areaId);
		if(city != null) {
			OutdoorCityInfo info = city.getOutdoorCityInfo();
			if(info != null) {
				return info.getCityName().toLowerCase();
			}
		}
		return "";
	}
	
	private void insertDataAndGetShortListCities() {
		mOutdoorLocationGetAsyncTask = (OutdoorLocationAbstractGetAsyncTask) new OutdoorLocationAbstractGetAsyncTask() {

			@Override
			protected void onPostExecute(Cursor result) {
				processDataBaseInfo(result);
			}
		};

		mOutdoorLocationFillAsyncTask = (OutdoorLocationAbstractFillAsyncTask) new OutdoorLocationAbstractFillAsyncTask() {

			@Override
			protected void onPostExecute(Void result) {
				mOutdoorLocationGetAsyncTask.execute(new String[] { AppConstants.SQL_SELECTION_GET_SHORTLIST_ITEMS });
			}
		}.execute(new String[] {});
	}

	/** Added to fetch selected city information before updating dashboard **/
	public void fetchSelectedCityInfo(){
		OutdoorLocationDatabase database = new OutdoorLocationDatabase();
		Cursor cursor = null;
		try {
			database.open();
			cursor = database.getDataFromOutdoorLoacation(AppConstants.SQL_SELECTION_GET_SHORTLIST_ITEMS);
			database.close();
			processDataBaseInfo(cursor);
		} catch (SQLiteException e) {
			ALog.e(ALog.OUTDOOR_LOCATION,
				"OutdoorLocationAbstractGetAsyncTask failed to retive data from DB: " + e.getMessage());
		}
	}

	private void processDataBaseInfo(Cursor cursor) {
		if (cursor != null && cursor.getCount() > 0) {
			ALog.i(ALog.OUTDOOR_LOCATION, "Fetch list of cities already short listed from DB " + cursor.getCount());
			cursor.moveToFirst();
			do {
				OutdoorCityInfo info = getOutdoorCityInfo(cursor); 
				addAreaIDToUsersList(info.getAreaID());
				addCityDataToMap(info, null, null, info.getAreaID());
			} while (cursor.moveToNext());
		} 
		
		// Added outside if condition, some time user may delete all outdoor city except current city
		// Current city information does not contain in cursor, that why adding current city info.
		addCurrentCityInfo();
		startCitiesTask();
	}
	
	public void addCurrentCityInfo() {
		String currentCityAreaId = LocationUtils.getCurrentLocationAreaId();
		if (LocationUtils.isCurrentLocationEnabled() && !currentCityAreaId.isEmpty()) {
			addCurrentCityAreaIDToUsersList(currentCityAreaId);
			OutdoorController.getInstance().addMyLocationToMap(currentCityAreaId);
		}
	}

	private OutdoorCityInfo getOutdoorCityInfo(Cursor cursor) {
		String city = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY));
		String cityCN = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY_CN));
		String cityTW = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY_TW));
		String areaID = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AREA_ID));
		float longitude = cursor.getFloat(cursor.getColumnIndex(AppConstants.KEY_LONGITUDE));
		float latitude = cursor.getFloat(cursor.getColumnIndex(AppConstants.KEY_LATITUDE));

		return new OutdoorCityInfo(city, cityCN, cityTW, longitude, latitude, areaID); 
	}

	public void removeUIChangeListener(OutdoorDataChangeListener listener) {
		outdoorDataChangeListener = null;
	}

	public void setUIChangeListener(OutdoorDataChangeListener listener) {
		outdoorDataChangeListener = listener;
	}
	
	public void removeOutdoorDetailsListener(OutdoorDetailsListener listener) {
		outdoorDetailsListener = null;
	}
	
	public void setOutdoorDetailsListener(OutdoorDetailsListener listener) {
		outdoorDetailsListener = listener;
	}
	
	public void removeAllOutdoorDataListener(AllOutdoorDataListener listener) {
		allOutdoorDataListener = null;
	}
	
	public void setAllOutdoorDataListener(AllOutdoorDataListener listener) {
		allOutdoorDataListener = listener;
	}
	
	@Override
	public void outdoorAQIDataReceived(OutdoorAQI outdoorAQI, String areaID) {
		if (outdoorAQI != null) {
			addCityDataToMap(null, outdoorAQI, null, areaID);
			if(userCitiesList.contains(areaID)) {
				if (outdoorDataChangeListener != null) {
					outdoorDataChangeListener.updateUIOnDataChange();
				}
			}
		}
	}

	@Override
	public void outdoorWeatherDataReceived(OutdoorWeather outdoorWeather, String areaID) {
		if (outdoorWeather != null) {
			if(userCitiesList.contains(areaID)) {
				addCityDataToMap(null, null, outdoorWeather, areaID);
			}
			String cityName = getCityNameFromAreaId(areaID);
			if(userCitiesList.contains(cityName)) {
				addCityDataToMap(null, null, outdoorWeather, cityName.toLowerCase());
			}
			if (outdoorDataChangeListener != null) {
				outdoorDataChangeListener.updateUIOnDataChange();
			}
		}
	}
	
	@Override
	public void outdoorHistoricalAQIDataReceived(List<OutdoorAQI> aqis) {
		outdoorDetailsListener.onAQIHistoricalDataReceived(aqis);
	}
	
	@Override
	public void outdoorOneDayForecastDataReceived(List<Weatherdto> weatherdtos) {
		outdoorDetailsListener.onOneDayWeatherForecastReceived(weatherdtos);
	}

	@Override
	public void outdoorFourDayForecastDataReceived(List<ForecastWeatherDto> weatherDtos) {
		outdoorDetailsListener.onFourDayWeatherForecastReceived(weatherDtos);
	}

	public List<String> getNearbyCitiesList(float latitudePlus,
			float latitudeMinus, float longitudePlus, float longitudeMinus) {
		nearbyCities.clear();
		for (int i = 0; i < cmaCities.size(); i++) {
			OutdoorCity outdoorCity = OutdoorManager.getInstance().getCityData(cmaCities.get(i));
			float latitude = outdoorCity.getOutdoorCityInfo().getLatitude();
			float longitude = outdoorCity.getOutdoorCityInfo().getLongitude();

			if ((longitude <= longitudePlus && longitude >= longitudeMinus)
					&& (latitude <= latitudePlus && latitude >= latitudeMinus)) {
				nearbyCities.add(cmaCities.get(i));
			}
		}

		return nearbyCities;
	}

	public synchronized List<String> getUsersCitiesList() {
		return userCitiesList;
	}

	public synchronized void clearCitiesList() {
		if (userCitiesList != null && !userCitiesList.isEmpty())
			userCitiesList.clear();
	}

	public synchronized void addAreaIDToUsersList(String areaID) {
		if (!userCitiesList.contains(areaID)) {
			// Add current location first position in the list
			if (LocationUtils.getCurrentLocationAreaId().equals(areaID) && LocationUtils.isCurrentLocationEnabled()) {
				userCitiesList.add(0, areaID);
			} else {
				userCitiesList.add(areaID);
			}
		}
	}
	
	public void addCurrentCityAreaIDToUsersList(String areaID) {
		if (LocationUtils.getCurrentLocationAreaId().equals(areaID)) {
			removeAreaIDFromUsersList(areaID);
			userCitiesList.add(0, areaID);
		}
	}

	public void removeAreaIDFromUsersList(String areaID) {
		if (userCitiesList.contains(areaID)) {
			ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorManager$removeAreaIDFromList areaID " + areaID);
			userCitiesList.remove(areaID);
		}
	}

	public void addCityDataToMap(OutdoorCityInfo info, OutdoorAQI aqi,
			OutdoorWeather weather, String areaID) {
		ALog.i(ALog.OUTDOOR_LOCATION, "OutdoorManager$addCityDataToMap areaID "
				+ areaID);
		OutdoorCity city = citiesMap.get(areaID);
		if (city == null) {
			city = new OutdoorCity();
		}
		if (info != null) {
			city.setOutdoorCityInfo(info);
		}
		if (aqi != null) {
			city.setOutdoorAQI(aqi);
		}
		if (weather != null) {
			city.setOutdoorWeather(weather);
		}
		citiesMap.put(areaID, city);
	}

	public void removeCityDataFromMap(String areaID) {
		if (citiesMap != null && citiesMap.containsKey(areaID)) {
			ALog.i(ALog.OUTDOOR_LOCATION,
					"OutdoorManager$removeCityDataFromMap areaID " + areaID);
			citiesMap.remove(areaID);
		}
	}

	public OutdoorCity getCityData(String areaID) {
		ALog.i(ALog.DASHBOARD, "OutdoorManager$getCityData " + areaID);
		return citiesMap.get(areaID);
	}

	public void clearCityOutdoorInfo() {
		resetUpdatedTime();
		if (citiesMap != null && !citiesMap.isEmpty()) {
			Set<String> keys = citiesMap.keySet();
			Iterator<String> i = keys.iterator();
			while (i.hasNext()) {
				String areadID = i.next();
				citiesMap.get(areadID).setOutdoorAQI(null);
				citiesMap.get(areadID).setOutdoorWeather(null);
			}
		}
	}

	@Override
	public void allOutdoorAQIDataReceived(List<OutdoorAQI> aqis) {
		if (aqis == null || aqis.isEmpty())
			return;
		for (OutdoorAQI outdoorAQI : aqis) {
			addCityDataToMap(null, outdoorAQI, null, outdoorAQI.getAreaID());
		}
		if (outdoorDataChangeListener != null) {
			outdoorDataChangeListener.updateUIOnDataChange();
		}
		if(allOutdoorDataListener != null) {
			allOutdoorDataListener.updateUIOnAllDataReceived();
		}
	}

}
