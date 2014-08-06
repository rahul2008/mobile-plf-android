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
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.WeatherIcon;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.Utils;

public class WeatherReportLayout extends  LinearLayout {
	
	String[] nextFourDays;
	
	public WeatherReportLayout(Context context) {
		super(context);
	}
	
	public WeatherReportLayout(Context context, AttributeSet attr) {
		super(context, attr);
	}
	
	public WeatherReportLayout(Context context, AttributeSet attr, int defStyle) {
		super(context, attr);
	}
	
	public WeatherReportLayout(Context context, AttributeSet attrs, int defStyle, 
			List<ForecastWeatherDto> weatherDetails) {
		super(context, attrs);
		if (weatherDetails == null || weatherDetails.size() < 4 ) return;
		nextFourDays = new String[4];
		weatherForecatDays(context);
		fourDaysWeatherForecast(context, weatherDetails);
	}

	/**
	 * Constructor
	 * @param Context
	 * @param AttributeSet
	 * @param int number of view to show
	 * */
	public WeatherReportLayout(final Context context, AttributeSet attrs, List<Weatherdto> weatherDetails) {
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
			String isdaytime = weatherDetails.get(i).getIsdaytime();
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.od_today_weather_layout, null);
			v.setPadding(10, 10, 10, 10);
			
			FontTextView timeTxt = (FontTextView) v.findViewById(R.id.odTodyWeatherTime);
			TextView tempTxt = (TextView) v.findViewById(R.id.odTodyWeatherTemp);
			tempTxt.setTypeface(Fonts.getGillsansLight(context));
			ImageView weatherImg = (ImageView) v.findViewById(R.id.odTodyWeatherImg);
			
			timeTxt.setText(time);
			tempTxt.setText(tempInCentigrade + "\u00B0");
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
			TextView dayTemp = (TextView) v.findViewById(R.id.odWeatherForcastMaxTemp);
			dayTemp.setTypeface(Fonts.getGillsansLight(context));
			TextView nightTemp = (TextView) v.findViewById(R.id.odWeatherForcastMinTemp);
			nightTemp.setTypeface(Fonts.getGillsansLight(context));
			FontTextView windSpeedTxt = (FontTextView) v.findViewById(R.id.odWeatherForcastWind);
			ImageView weatherImg = (ImageView) v.findViewById(R.id.odWeatherForcastImg);
			ImageView windDirImg = (ImageView) v.findViewById(R.id.odWeatherForcastWindImg);
			
			dayTxt.setText(nextFourDays[i]);
			weatherImg.setImageResource(weatherDetails.get(i).getWeatherIcon());
			Utils.setOutdoorWeatherDirImg(context, 
					weatherDetails.get(i).getWindSpeed(), weatherDetails.get(i).getWindDirection(), windDirImg);
			
			if (weatherDetails.get(i).getTemperatureDay() != null
					&& !weatherDetails.get(i).getTemperatureDay().isEmpty()) {
				dayTemp.setText(weatherDetails.get(i).getTemperatureDay() + "\u00B0");
			} 
			
			if (weatherDetails.get(i).getTemperatureNight() != null 
					&& !weatherDetails.get(i).getTemperatureNight().isEmpty()) {
				nightTemp.setText(weatherDetails.get(i).getTemperatureNight() + "\u00B0");
			} 
			
			windSpeedTxt.setText(weatherDetails.get(i).getWindSpeed()+ context.getString(R.string.kmph));
			 
			LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			this.addView(v, parentParams);
		}
	}
	
	private void weatherForecatDays(Context context) {
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