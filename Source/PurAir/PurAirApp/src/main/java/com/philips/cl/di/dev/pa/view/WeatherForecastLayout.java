package com.philips.cl.di.dev.pa.view;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.WeatherIcon;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.util.Utils;

public class WeatherForecastLayout extends  LinearLayout {
	
	private String[] nextFourDays;
	
	public WeatherForecastLayout(Context context) {
		super(context);
	}
	
	public WeatherForecastLayout(Context context, AttributeSet attr) {
		super(context, attr);
	}
	
	public WeatherForecastLayout(Context context, AttributeSet attrs, int defStyle, 
			List<ForecastWeatherDto> weatherDetails) {
		super(context, attrs);
		if (weatherDetails == null || weatherDetails.size() < 4 ) return;
		nextFourDays = new String[4];
		weatherForecastDays(context);
		fourDaysWeatherForecast(context, weatherDetails);
	}

	/**
	 * Constructor
	 * @param Context
	 * @param AttributeSet
	 * @param int number of view to show
	 * */
	public WeatherForecastLayout(final Context context, AttributeSet attrs, List<Weatherdto> weatherDetails) {
		super(context, attrs);
		
		if (weatherDetails == null || weatherDetails.isEmpty()) return;
		todaysWeather(context, weatherDetails);
	}
	
	private void todaysWeather(Context context, List<Weatherdto> weatherDetails) {
		/** Today's weather report*/
		
		for (int i = 0; i < weatherDetails.size(); i++) {
			float tempInCentigrade = weatherDetails.get(i).getTempInCentigrade();
			weatherDetails.get(i).getTempInFahrenheit();
			weatherDetails.get(i).getDate();
			String time = weatherDetails.get(i).getTime().substring(10,16);
			String weatherDesc = weatherDetails.get(i).getWeatherDesc();
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.od_today_weather_layout, null);
			v.setPadding(10, 10, 10, 10);
			
			FontTextView timeTxt = (FontTextView) v.findViewById(R.id.odTodyWeatherTime);
			FontTextView tempTxt = (FontTextView) v.findViewById(R.id.odTodyWeatherTemp);
			ImageView weatherImg = (ImageView) v.findViewById(R.id.odTodyWeatherImg);
			
			timeTxt.setText(time);
			tempTxt.setText(Math.round(tempInCentigrade) + "\u00B0");
			weatherImg.setImageResource(WeatherIcon.getWeatherIconResId(weatherDesc));
			LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			this.addView(v, parentParams);
		}
	}
	
	private void fourDaysWeatherForecast(Context context, List<ForecastWeatherDto> weatherDetails) {
		/** Next 4 days weather report*/
	
		for (int i = 0; i < 4; i++) {
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.od_last_five_weather_forcast, null);
			v.setPadding(10, 10, 10, 10);
			
			FontTextView dayTxt = (FontTextView) v.findViewById(R.id.odDayWeatherForcast);
			FontTextView dayTemp = (FontTextView) v.findViewById(R.id.odWeatherForcastMaxTemp);
			FontTextView nightTemp = (FontTextView) v.findViewById(R.id.odWeatherForcastMinTemp);
			FontTextView windSpeedTxt = (FontTextView) v.findViewById(R.id.odWeatherForcastWind);
			FontTextView windSpeedTxtKm = (FontTextView) v.findViewById(R.id.odWeatherForcastWindKM);
			ImageView weatherImg = (ImageView) v.findViewById(R.id.odWeatherForcastImg);
			ImageView windDirImg = (ImageView) v.findViewById(R.id.odWeatherForcastWindImg);
			
			dayTxt.setText(nextFourDays[i]);
			weatherImg.setImageResource(weatherDetails.get(i).getWeatherIcon());
			
			int windDirectionId = weatherDetails.get(i).getWindDirection();
			int windSpeed =  weatherDetails.get(i).getWindSpeed();
			String nightTemprature = weatherDetails.get(i).getTemperatureNight();
			String dayTemprature = weatherDetails.get(i).getTemperatureDay();
			
			if (windDirectionId != - 1) {
				Utils.setOutdoorWeatherDirImg(context, windSpeed, windDirectionId, windDirImg);
			}
			
			if (dayTemprature != null && !dayTemprature.isEmpty()) {
				dayTemp.setText(Math.round(getTempratureInFloat(dayTemprature)) + "\u00B0");
			} 
			
			if (nightTemprature != null	&& !nightTemprature.isEmpty()) {
				nightTemp.setText(Math.round(getTempratureInFloat(nightTemprature)) + "\u00B0");
			} 
			
			windSpeedTxt.setText(windSpeed +" ");
			windSpeedTxtKm.setText(context.getString(R.string.kmph));
			 
			LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			this.addView(v, parentParams);
		}
	}
	
	private float getTempratureInFloat(String temprature) {
		float ftemprature = 0;
		try {
			ftemprature = Float.parseFloat(temprature);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return ftemprature;
	}
	
	private void weatherForecastDays(Context context) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		int dayInt = cal.get(Calendar.DAY_OF_WEEK);
		for (int j = 0; j < nextFourDays.length; j++) {
			if (dayInt < 7) {
				dayInt++;
			} else {
				dayInt = 1;
			}
			String dayStr = Utils.getDayOfWeek(context, dayInt);
			nextFourDays[j] = dayStr;
		}
	}
}