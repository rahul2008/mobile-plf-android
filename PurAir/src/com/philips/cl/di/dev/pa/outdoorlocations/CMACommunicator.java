package com.philips.cl.di.dev.pa.outdoorlocations;

import java.net.HttpURLConnection;
import java.util.List;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.cma.CMAHelper;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorDataListener;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.dashboard.OutdoorWeather;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.Utils;

public class CMACommunicator implements DataCommunicator {
	
	private static final String BASE_URL_AQI = "http://api.fuwu.weather.com.cn/wis_forcastdata/data/getData.php";
	private static final String BASE_URL_WEATHER = Utils.getCMA_BaseURL() ;
	private static final String BASE_URL_HOURLY_FORECAST = "http://data.fuwu.weather.com.cn/getareaid/areaid?id=";
	
	private static final String AIR = "air";
	private static final String AIR_HISTORY = "air_his";
	private static final String OBSERVE = "observe";
	private static final String FORECAST_4_DAYS = "forecast4d";
	
	private CMAHelper cmaHelper;
	private OutdoorDataListener outdoorDataListener;
	
	public CMACommunicator() {
		cmaHelper = new CMAHelper(Utils.getCMA_AppID(), Utils.getCMA_PrivateKey());
	}
	
	@Override
	public void setResponseListener(OutdoorDataListener outdoorEventListener) {
		this.outdoorDataListener = outdoorEventListener ;
	}

	@Override
	public void requestAQIData(List<String> cities) {
		String areaIds = cities.toString().replace("[", "").replace("]", "").replace(", ", ",");

		TaskGetHttp requestAQITask = new TaskGetHttp(cmaHelper.getURL(BASE_URL_AQI, areaIds, AIR, Utils.getDate(System.currentTimeMillis())), RequestType.CITIES_AQI.getRequestTypeString(), PurAirApplication.getAppContext(), this);
		requestAQITask.start();
	}

	public void requestNearbyLocationsAQI(List<String> cities) {
		String areaIds = cities.toString().replace("[", "").replace("]", "").replace(", ", ",");

		TaskGetHttp requestAQITask = new TaskGetHttp(cmaHelper.getURL(BASE_URL_AQI, areaIds, AIR, Utils.getDate(System.currentTimeMillis())), RequestType.NEARBY_LOCATIONS_AQI.getRequestTypeString(), PurAirApplication.getAppContext(), this);
		requestAQITask.start();
	}

	@Override
	public void requestWeatherData(List<String> cities) {
		String areaIds = cities.toString().replace("[", "").replace("]", "").replace(", ", ",");
		
		TaskGetHttp requestWeatherTask = new TaskGetHttp(cmaHelper.getURL(BASE_URL_WEATHER, areaIds, OBSERVE, Utils.getDate(System.currentTimeMillis())), RequestType.CITIES_WEATHER.getRequestTypeString(), PurAirApplication.getAppContext(), this);
		requestWeatherTask.start();
	}

	@Override
	public void requestHistoricalAQI(String city) {
		long daysInMillisecs = 1000 * 60 * 60 * 24 * 30l; // 30 Days
		long currentChineseDateInMillis = Utils.getCurrentChineseDate().getTime();
		long startDateInMillis = currentChineseDateInMillis - daysInMillisecs ;
		String dateFormat = Utils.getDate(startDateInMillis) + "," + Utils.getDate(currentChineseDateInMillis) ;
		
		String url = cmaHelper.getURL(BASE_URL_AQI, city, AIR_HISTORY, dateFormat) ;
		TaskGetHttp requestHistoricalAQITask = new TaskGetHttp(url,RequestType.HISTORIAL_AQI.getRequestTypeString(), city, PurAirApplication.getAppContext(), this);
		requestHistoricalAQITask.start();
	}

	@Override
	public void requestOneDayForecast(String city) {
		ALog.i(ALog.OUTDOOR_DETAILS, "CMACommuicator$requestOneDayForecast " + city);
		TaskGetHttp requestOneDayForecastTask = new TaskGetHttp(BASE_URL_HOURLY_FORECAST + city + "&time=day", RequestType.FORECAST_ONE_DAY.getRequestTypeString(), PurAirApplication.getAppContext(), this);
		requestOneDayForecastTask.start();
	}

	@Override
	public void requestFourDayForecast(String city) {
		TaskGetHttp requestFourDayForecastTask = new TaskGetHttp(cmaHelper.getURL(BASE_URL_WEATHER, city, FORECAST_4_DAYS, Utils.getDate(System.currentTimeMillis())), RequestType.FORECAST_FOUR_DAYS.getRequestTypeString(), PurAirApplication.getAppContext(), this);
		requestFourDayForecastTask.start();
	}
	
	public void requestAllCitiesData(List<String> cities) {
		//Split requesting 194 cities data into 2 requests.
		String areaIds1 = cities.subList(0, 97).toString().replace("[", "").replace("]", "").replace(", ", ",");
		String areaIds2 = cities.subList(98, OutdoorManager.getInstance().getCMACities().size()).toString().replace("[", "").replace("]", "").replace(", ", ",");

		TaskGetHttp requestAQITask1 = new TaskGetHttp(cmaHelper.getURL(BASE_URL_AQI, areaIds1, AIR, Utils.getDate(System.currentTimeMillis())), RequestType.ALL_CITIES_AQI.getRequestTypeString(), PurAirApplication.getAppContext(), this);
		requestAQITask1.start();
		
		TaskGetHttp requestAQITask2 = new TaskGetHttp(cmaHelper.getURL(BASE_URL_AQI, areaIds2, AIR, Utils.getDate(System.currentTimeMillis())), RequestType.ALL_CITIES_AQI.getRequestTypeString(), PurAirApplication.getAppContext(), this);
		requestAQITask2.start();
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String responseData, String type, String areaId) {
		ALog.i(ALog.OUTDOOR_LOCATION, "CMACommunicator for AQIHistoric : response " + responseData);
		if(isResponseValid(responseCode, responseData, type)) {
			notifyListeners(responseData, RequestType.valueOf(type), areaId);
		}
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String responseData, String type) {
		ALog.i(ALog.OUTDOOR_LOCATION, "CMACommunicator : response " + responseData);
		if(isResponseValid(responseCode, responseData, type)) {
			notifyListeners(responseData, RequestType.valueOf(type), null);
		} else {
			outdoorDataListener.noDataReceived();
		}
	}

	private boolean isResponseValid(int responseCode, String responseData, String type) {
		return responseCode == HttpURLConnection.HTTP_OK && responseData != null 
				&& !responseData.isEmpty() && type != null && !type.isEmpty();
	}
	
	private void notifyListeners(String responseData, RequestType type, String areaId) {
		ALog.i(ALog.OUTDOOR_DETAILS, "CMACommunicator$notifyListeners " + type);
		switch(type) {
		case CITIES_AQI:
			List<OutdoorAQI> outdoorAQIs = DataParser.parseLocationAQI(responseData);
			if(outdoorAQIs == null || outdoorAQIs.isEmpty()) return;
			outdoorDataListener.allOutdoorAQIDataReceived(outdoorAQIs) ;
			break;
		case CITIES_WEATHER:
			List<OutdoorWeather> outdoorWeathers = DataParser.parseLocationWeather(responseData);
			if(outdoorWeathers == null || outdoorWeathers.isEmpty()) return;
			outdoorDataListener.outdoorWeatherDataReceived(outdoorWeathers) ;
			break;
		case FORECAST_FOUR_DAYS:
			List<ForecastWeatherDto> forecastWeatherDtos = DataParser.parseFourDaysForecastData(responseData) ;
			if(forecastWeatherDtos != null) {
				outdoorDataListener.outdoorFourDayForecastDataReceived(forecastWeatherDtos);
			}
			break;
		case FORECAST_ONE_DAY:
			List<Weatherdto> weatherdtos = DataParser.getHourlyWeatherData(responseData);
			if(weatherdtos != null) {
				outdoorDataListener.outdoorOneDayForecastDataReceived(weatherdtos);
			}
			break;
		case HISTORIAL_AQI:
			List<OutdoorAQI> historicalOutdoorAQIs = DataParser.parseHistoricalAQIData(responseData);
			if(historicalOutdoorAQIs != null) {
				outdoorDataListener.outdoorHistoricalAQIDataReceived(historicalOutdoorAQIs, areaId);
			}
			break;
		case ALL_CITIES_AQI:
			List<OutdoorAQI> allCitiesAQIs = DataParser.parseLocationAQI(responseData);
			if(allCitiesAQIs == null || allCitiesAQIs.isEmpty()) return;
			outdoorDataListener.allOutdoorAQIDataReceived(allCitiesAQIs);
			break;
		case NEARBY_LOCATIONS_AQI:
			List<OutdoorAQI> nearbyLocationAQIs = DataParser.parseLocationAQI(responseData);
			if(nearbyLocationAQIs == null || nearbyLocationAQIs.isEmpty()) return;
			outdoorDataListener.nearbyLocationsAQIReceived(nearbyLocationAQIs);
			break;
		default:
			break;
		}
	}
}
