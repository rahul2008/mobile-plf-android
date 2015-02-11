package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.ArrayList;
import java.util.List;

import com.philips.cl.di.dev.pa.dashboard.OutdoorDataListener;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.outdoorlocations.DataCommunicator.RequestType;

public class DataCommunicatorStrategy {
	
	private CMACommunicator cmaCommunicator;
	private USEmbassyCommunicator usEmbassyCommunicator;
	
	public DataCommunicatorStrategy(OutdoorDataListener outdoorDataListener) {
		cmaCommunicator = new CMACommunicator();
		cmaCommunicator.setResponseListener(outdoorDataListener);
		usEmbassyCommunicator = new USEmbassyCommunicator();
		usEmbassyCommunicator.setResponseListener(outdoorDataListener);
	}
	
	public void requestCityAQIData(List<String> userCities) {
		requestCMAData(getCMAUserList(userCities), RequestType.CITIES_AQI);
		requestUSEmbassyData(getUSEmbassyUserList(userCities), RequestType.CITIES_AQI);
	}
	
	public void requestNearbyLocationsAQIData(List<String> locationAreaIds) {
		requestCMAData(locationAreaIds, RequestType.NEARBY_LOCATIONS_AQI);
	}
	
	public void requestCityWeatherData(List<String> userCities) {
		requestCMAData(getCMACitiesFromCityNames(userCities), RequestType.CITIES_WEATHER);
	}
	
	public void requestHistoricalAQIData(List<String> userCity) {
		requestCMAData(userCity, RequestType.HISTORIAL_AQI);
		requestUSEmbassyData(userCity, RequestType.HISTORIAL_AQI);
	}
	
	public void requestOneDayWeatherForecastData(List<String> userCity) {
		requestCMAData(userCity, RequestType.FORECAST_ONE_DAY);
	}
	
	public void requestFourDayWeatherForecastData(List<String> userCity) {
		requestCMAData(userCity, RequestType.FORECAST_FOUR_DAYS);
	}
	
	public void requestAllCityData(List<String> allCMACities) {
		requestCMAData(allCMACities, RequestType.ALL_CITIES_AQI);
	}

	private List<String> getCMACitiesFromCityNames(List<String> userCities) {
		List<String> cmaCities = getCMAUserList(userCities);
		List<String> usEmbassyCities = getUSEmbassyUserList(userCities);
		for (String cityName : usEmbassyCities) {
			String areaId = OutdoorManager.getInstance().getAreaIdFromCityName(cityName);
			if(!cmaCities.contains(areaId)) {
				cmaCities.add(areaId);
			}
		}
		return cmaCities;
	}
	
	private void requestCMAData(List<String> userCities, RequestType type) {
		if(userCities == null || userCities.isEmpty()) return;
		switch (type) {
		case CITIES_AQI:
			cmaCommunicator.requestAQIData(userCities);
			break;
		case CITIES_WEATHER:
			cmaCommunicator.requestWeatherData(userCities);
			break;
		case FORECAST_ONE_DAY:
			cmaCommunicator.requestOneDayForecast(userCities.get(0));
			break;
		case FORECAST_FOUR_DAYS:
			cmaCommunicator.requestFourDayForecast(userCities.get(0));
			break;
		case HISTORIAL_AQI:
			cmaCommunicator.requestHistoricalAQI(userCities.get(0));
			break;
		case ALL_CITIES_AQI:
			cmaCommunicator.requestAllCitiesData(userCities);
			break;
		case NEARBY_LOCATIONS_AQI:
			cmaCommunicator.requestNearbyLocationsAQI(userCities);
		default:
			new IllegalArgumentException("Outdoor data request type not Found");
			break;
		}
	}
	
	private void requestUSEmbassyData(List<String> userCities, RequestType type) {
		if(userCities == null || userCities.isEmpty()) return;
		switch (type) {
		case CITIES_AQI:
			usEmbassyCommunicator.requestAQIData(userCities);
			break;
		case CITIES_WEATHER: /** NOP */
			break;
		case FORECAST_ONE_DAY: /** NOP */
			break;
		case FORECAST_FOUR_DAYS: /** NOP */
			break;
		case HISTORIAL_AQI:
			usEmbassyCommunicator.requestHistoricalAQI(userCities.get(0));
			break;
		case ALL_CITIES_AQI: /** NOP */
			break;
		default:
			new IllegalArgumentException("Outdoor data request type not Found");
			break;
		}
	}
	
	private List<String> getCMAUserList(List<String> userCities) {
		return intersection(userCities, OutdoorManager.getInstance().getCMACities());
	}
	
	private List<String> getUSEmbassyUserList(List<String> userCities) {
		return intersection(userCities, OutdoorManager.getInstance().getUSEmbassyCities());
	}
	
	private <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();
        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
}
