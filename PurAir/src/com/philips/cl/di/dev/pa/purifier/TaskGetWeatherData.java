package com.philips.cl.di.dev.pa.purifier;


import java.net.HttpURLConnection;

import com.philips.cl.di.dev.pa.datamodel.ResponseDto;
import com.philips.cl.di.dev.pa.util.NetworkUtils;

public class TaskGetWeatherData extends Thread {

	private WeatherDataListener listener ;
	private String url ;

	private ResponseDto responseObj;
	private int responseCode;

	public interface WeatherDataListener {
		void weatherDataUpdated(String weatherData);
	}

	public TaskGetWeatherData(String url,WeatherDataListener listener) {
		this.listener = listener ;
		this.url = url ;
	}

	@Override
	public void run() {
		String result = "";
		responseObj = NetworkUtils.downloadUrl(url, 10000, 15000);
		if(responseObj!=null)
		{
			result=responseObj.getResponseData();
			responseCode=responseObj.getResponseCode();
		}

		if (responseCode==HttpURLConnection.HTTP_OK && result != null) {
			listener.weatherDataUpdated(result) ;
		}

	}
}
