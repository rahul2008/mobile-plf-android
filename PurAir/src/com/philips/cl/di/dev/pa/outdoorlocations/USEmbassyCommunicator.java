package com.philips.cl.di.dev.pa.outdoorlocations;

import java.net.HttpURLConnection;
import java.util.List;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorDataListener;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.util.DataParser;

public class USEmbassyCommunicator implements DataCommunicator {

	private static final String US_EMBASSY_CITY_AQI_URL = "http://222.73.255.34/pm.php?city=";
	private static final String US_EMBASSY_CITY_HISTORICAL_AQI_URL = "http://222.73.255.34/?city=";
	
	private OutdoorDataListener outdoorDataListener;

	@Override
	public void setResponseListener(OutdoorDataListener outdoorEventListener) {
		this.outdoorDataListener = outdoorEventListener;
	}

	@Override
	public void requestAQIData(List<String> cities) {
		for (String cityName : cities) {
			TaskGetHttp requestAQITask = new TaskGetHttp(US_EMBASSY_CITY_AQI_URL + cityName, RequestType.CITIES_AQI.getRequestTypeString(), PurAirApplication.getAppContext(), this);
			requestAQITask.start();
		}
	}

	@Override
	public void requestWeatherData(List<String> cities) { /** NOP */ }

	@Override
	public void requestHistoricalAQI(String cityName) {
		TaskGetHttp requestAQITask = new TaskGetHttp(US_EMBASSY_CITY_HISTORICAL_AQI_URL + cityName, RequestType.HISTORIAL_AQI.getRequestTypeString(), PurAirApplication.getAppContext(), this);
		requestAQITask.start();
	}

	@Override
	public void requestOneDayForecast(String city) { /** NOP */ }

	@Override
	public void requestFourDayForecast(String city) { /** NOP */ }

	@Override
	public void receiveServerResponse(int responseCode, String responseData, String type) {
		if(isResponseValid(responseCode, responseData, type)) {
			notifyListeners(responseData, RequestType.valueOf(type));
		}
	}

	private boolean isResponseValid(int responseCode, String responseData, String type) {
		return responseCode == HttpURLConnection.HTTP_OK && responseData != null 
				&& !responseData.isEmpty() && type != null && !type.isEmpty();
	}

	private void notifyListeners(String responseData, RequestType type) {
		if(RequestType.CITIES_AQI.equals(type)) {
			List<OutdoorAQI> outdoorAQIList = DataParser.parseUSEmbassyLocationAQI(responseData);
			for (OutdoorAQI outdoorAQI : outdoorAQIList) {
				outdoorDataListener.outdoorAQIDataReceived(outdoorAQI, outdoorAQI.getAreaID());
			}
		} else if (RequestType.HISTORIAL_AQI.equals(type)) {
			List<OutdoorAQI> historicalOutdoorAQIs = DataParser.parseUSEmbassyHistoricalAQIData(responseData);
			if(historicalOutdoorAQIs != null) {
				outdoorDataListener.outdoorHistoricalAQIDataReceived(historicalOutdoorAQIs);
			}
		}
	}

}
