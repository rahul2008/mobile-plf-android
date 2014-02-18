package com.philips.cl.di.dev.pa.customviews;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.detail.utils.GraphConst;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.dto.Weatherdto;

public class WeatherReportLayout extends  LinearLayout {
	
	private static final String TAG = WeatherReportLayout.class.getSimpleName();

	/**
	 * Constructor
	 * @param Context
	 * @param AttributeSet
	 * @param int number of view to show
	 * */
	public WeatherReportLayout(final Context context, AttributeSet attrs,  final int num) {
		super(context, attrs);
		
		SessionDto sessionDto = SessionDto.getInstance();
		List<Weatherdto> weatherDetails = null;
		if (sessionDto != null) {
			weatherDetails = sessionDto.getWeatherDetails();
			if (weatherDetails == null) {
				return;
			}
		} else {
			return;
		}
		
		String[] nextFiveDays = new String[5];
		String[] hrsDays = new String[8];
		Calendar cal = Calendar.getInstance();
		
		int dayInt = cal.get(Calendar.DAY_OF_WEEK);
		for (int j = 0; j < nextFiveDays.length; j++) {
			if (dayInt < 7) {
				dayInt++;
			} else {
				dayInt = 1;
			}
			String dayStr = new GraphConst().getDayOfWeek(context, dayInt);
			nextFiveDays[j] = dayStr;
		}
		
		int hr = cal.get(Calendar.HOUR_OF_DAY);
		for (int j = 0; j < hrsDays.length; j++) {
			String tempHr = null;
			if (hr < 10) {
				tempHr = "0" + hr + ":00";
			} else {
				tempHr = hr + ":00";
			}
			hrsDays[j] = tempHr;
			hr = hr + 3;
			if (hr > 23) {
				hr = hr - 24;
			} 
		}
		
		
		/**
		 * For weather report condition
		 * */
		if (num == 5) {
			/** Next 4 days weather report*/
			int count = 9;
			for (int i = 0; i < num; i++) {
				float sum = 0;
				float avg = 0;
				float tempInCentigrade = 0;
				float tempInFahrenheit = 0;
				String date = null;
				String time = null;
				String weatherDesc = null;
				String isdaytime = null;
				String windDirection = null;
				float windDegree = 0;
				float maxTempC = 0;
				float maxTempF = 0;
				float minTempC = 0;
				float windSpeed = 0;
				float minTempF = 0;
				float dergreeWind = 0;
				//count = count + i;
				//System.out.println("count= "+count);
				for (int j = 0; j < 8; j++) {
					tempInCentigrade = weatherDetails.get(count).getTempInCentigrade();
					tempInFahrenheit = weatherDetails.get(count).getTempInFahrenheit();
					date = weatherDetails.get(count).getDate();
					time = weatherDetails.get(count).getTime();
					//weatherDesc = weatherDetails.get(count).getWeatherDesc();
					//isdaytime = weatherDetails.get(count).getIsdaytime();
					//windDirection = weatherDetails.get(count).getWindDirection();
					maxTempC = weatherDetails.get(count).getMaxTempC();
					maxTempF = weatherDetails.get(count).getMaxTempF();
					minTempC = weatherDetails.get(count).getMinTempC();
					windSpeed = weatherDetails.get(count).getWindSpeed();
					minTempF = weatherDetails.get(count).getMinTempF();
					
					if (j == 4) {
						weatherDesc = weatherDetails.get(count).getWeatherDesc();
						isdaytime = weatherDetails.get(count).getIsdaytime();
						windDirection = weatherDetails.get(count).getWindDirection();
						windDegree = weatherDetails.get(count).getWindDegree();
					}
					
					/*Log.d(TAG, "Forcast next five  tempInCentigrade= "+tempInCentigrade
							+" tempInFahrenheit= " + tempInFahrenheit
							+" date= " +date
							+" time= " +time
							+" windSpeed= "+windSpeed
							+" windDirection= " +windDirection
							+" windDegree= " +windDegree
							+" maxTempC= " +maxTempC
							+" maxTempF= " +maxTempF
							+" minTempC= " +minTempC
							+" minTempF= " +minTempF
							+" minTempF= " +minTempF
							+" weatherDesc= "+weatherDesc
							+" isdaytime= "+isdaytime);*/
					sum = sum + windSpeed ;
					count++;
				}
				avg = sum / (float)8;
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.od_last_five_weather_forcast, null);
				v.setPadding(10, 10, 10, 10);
				
				CustomTextView dayTxt = (CustomTextView) v.findViewById(R.id.odDayWeatherForcast);
				CustomTextView maxTempTxt = (CustomTextView) v.findViewById(R.id.odWeatherForcastMaxTemp);
				CustomTextView minTempTxt = (CustomTextView) v.findViewById(R.id.odWeatherForcastMinTemp);
				CustomTextView windSpeedTxt = (CustomTextView) v.findViewById(R.id.odWeatherForcastWind);
				ImageView weatherImg = (ImageView) v.findViewById(R.id.odWeatherForcastImg);
				ImageView windDirImg = (ImageView) v.findViewById(R.id.odWeatherForcastWindImg);
				
				dayTxt.setText(nextFiveDays[i]);
				weatherImg.setImageDrawable(
						new GraphConst().getOutdoorTemperatureImage(context,weatherDesc, "yes"));
				new GraphConst().setOutdoorWeatherDirImg(context, windSpeed, windDirection, windDegree, windDirImg);
				maxTempTxt.setText(maxTempC+"\u2103");
				minTempTxt.setText(minTempC+"\u2103");
				windSpeedTxt.setText(String.format("%.1f", avg)+" km/h");
				//new GraphConst().setOutdoorWeatherDirImg(context,avg, windDirection, windDirImg);
				
				LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				this.addView(v, parentParams);
			}
		} else {
			/** Today's weather report*/
			
			for (int i = 0; i < num; i++) {
				
				float tempInCentigrade = weatherDetails.get(i+1).getTempInCentigrade();
				float tempInFahrenheit = weatherDetails.get(i+1).getTempInFahrenheit();
				String date = weatherDetails.get(i+1).getDate();
				String time = weatherDetails.get(i+1).getTime();
				String weatherDesc = weatherDetails.get(i+1).getWeatherDesc();
				String isdaytime = weatherDetails.get(i+1).getIsdaytime();
				/*Log.d(TAG, "Today Forcast  tempInCentigrade= "+tempInCentigrade
						+" tempInFahrenheit= " + tempInFahrenheit
						+" date= " +date
						+" time= " +time
						+" weatherDesc= "+weatherDesc
						+" isdaytime= "+isdaytime);*/
				
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.od_today_weather_layout, null);
				v.setPadding(10, 10, 10, 10);
				
				CustomTextView timeTxt = (CustomTextView) v.findViewById(R.id.odTodyWeatherTime);
				CustomTextView tempTxt = (CustomTextView) v.findViewById(R.id.odTodyWeatherTemp);
				ImageView weatherImg = (ImageView) v.findViewById(R.id.odTodyWeatherImg);
				
				//timeTxt.setText(hrsDays[i]);
				timeTxt.setText(splitToHr(time));
				tempTxt.setText(tempInCentigrade+"\u2103");
				weatherImg.setImageDrawable(
						new GraphConst().getOutdoorTemperatureImage(context,weatherDesc, isdaytime));
				LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				this.addView(v, parentParams);
			}
		}
		
	}
	
	private String splitToHr(String timeStr) {
		char[] strArr = timeStr.toCharArray();
		String newTime = "";
		for (int i = 0; i < strArr.length; i++) {
			newTime = String.valueOf(strArr[strArr.length - 1 - i] + newTime);
			if (i == 1) {
				newTime = ":" + newTime;
			}
		}
		return newTime;
	}
}