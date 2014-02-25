package com.philips.cl.di.dev.pa.network;


import java.net.HttpURLConnection;

import com.philips.cl.di.dev.pa.dto.ResponseDto;
import com.philips.cl.di.dev.pa.utils.NetworkUtils;

public class TaskGetWeatherData extends Thread {

	private WeatherDataListener listener ;
	private String url ;

	private ResponseDto responseObj;
	private int responseCode;

	public interface WeatherDataListener {
		public void weatherDataUpdated(String weatherData);
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
