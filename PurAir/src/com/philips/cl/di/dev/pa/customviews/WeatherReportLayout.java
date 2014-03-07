package com.philips.cl.di.dev.pa.customviews;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dto.Weatherdto;
import com.philips.cl.di.dev.pa.pureairui.fragments.HomeFragment;
import com.philips.cl.di.dev.pa.utils.Utils;

public class WeatherReportLayout extends  LinearLayout {
	
	private static final String TAG = WeatherReportLayout.class.getSimpleName();

	public WeatherReportLayout(Context context) {
		super(context);
	}
	
	public WeatherReportLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public WeatherReportLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}
	/**
	 * Constructor
	 * @param Context
	 * @param AttributeSet
	 * @param int number of view to show
	 * */
	public WeatherReportLayout(final Context context, AttributeSet attrs,  final int num, String timeStr) {
		super(context, attrs);
		
		List<Weatherdto> weatherDetails = HomeFragment.getWeatherDetails();
		
		if (weatherDetails == null) {
			return;
		}
		String[] nextFiveDays = new String[5];
		String[] hrsDays = new String[8];
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
		if (timeStr != null && timeStr.length() > 0) {
			try {
				hourOfDay = Integer.parseInt(timeStr.substring(11, 13));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		int dayInt = cal.get(Calendar.DAY_OF_WEEK);
		for (int j = 0; j < nextFiveDays.length; j++) {
			if (dayInt < 7) {
				dayInt++;
			} else {
				dayInt = 1;
			}
			String dayStr = Utils.getDayOfWeek(context, dayInt);
			nextFiveDays[j] = dayStr;
		}
		
		for (int j = 0; j < hrsDays.length; j++) {
			String tempHr = null;
			if (hourOfDay < 10) {
				tempHr = "0" + hourOfDay + ":00";
			} else {
				tempHr = hourOfDay + ":00";
			}
			hrsDays[j] = tempHr;
			hourOfDay = hourOfDay + 3;
			if (hourOfDay > 23) {
				hourOfDay = hourOfDay - 24;
			} 
		}
		
		
		/**
		 * For weather report condition
		 * */
		if (num == 5) {
			Log.i(TAG, "Weatherdto NExt 4== "+ weatherDetails.size());
			/** Next 4 days weather report*/
			int count = 9;
			for (int i = 0; i < num; i++) {
				float windSpeedTemp = 0;
				String weatherDesc = null;
				String windDirection = null;
				float windDegree = 0;
				float maxTempC = 0;
				float minTempC = 0;
				float windSpeed = 0;
				//count = count + i;
				//System.out.println("count= "+count);
				for (int j = 0; j < 8; j++) {
					weatherDetails.get(count).getTempInFahrenheit();
					weatherDetails.get(count).getDate();
					weatherDetails.get(count).getTime();
					//weatherDesc = weatherDetails.get(count).getWeatherDesc();
					//isdaytime = weatherDetails.get(count).getIsdaytime();
					//windDirection = weatherDetails.get(count).getWindDirection();
					maxTempC = weatherDetails.get(count).getMaxTempC();
					weatherDetails.get(count).getMaxTempF();
					minTempC = weatherDetails.get(count).getMinTempC();
					windSpeed = weatherDetails.get(count).getWindSpeed();
					weatherDetails.get(count).getMinTempF();
					
					if (j == 4) {
						weatherDesc = weatherDetails.get(count).getWeatherDesc();
						weatherDetails.get(count).getIsdaytime();
						windDirection = weatherDetails.get(count).getWindDirection();
						windDegree = weatherDetails.get(count).getWindDegree();
					}
					
					if (windSpeed > windSpeedTemp) {
						windSpeedTemp = windSpeed ;
					} 
					
					count++;
				}
				//avg = sum / (float)8;
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
						Utils.getOutdoorTemperatureImage(context, weatherDesc, "yes"));
				Utils.setOutdoorWeatherDirImg(context, windSpeedTemp, windDirection, windDegree, windDirImg);
				maxTempTxt.setText(maxTempC+"\u2103");
				minTempTxt.setText(minTempC+"\u2103");
				windSpeedTxt.setText(String.format("%.1f", windSpeedTemp)+" km/h");
				//new GraphConst().setOutdoorWeatherDirImg(context,avg, windDirection, windDirImg);
				
				LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				this.addView(v, parentParams);
			}
		} else {
			/** Today's weather report*/
			
			for (int i = 0; i < num; i++) {
				Log.i("Weatherdto", "Weatherdto current days== "+ weatherDetails.size());
				float tempInCentigrade = weatherDetails.get(i+1).getTempInCentigrade();
				weatherDetails.get(i+1).getTempInFahrenheit();
				weatherDetails.get(i+1).getDate();
				String time = weatherDetails.get(i+1).getTime();
				String weatherDesc = weatherDetails.get(i+1).getWeatherDesc();
				String isdaytime = weatherDetails.get(i+1).getIsdaytime();
				
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.od_today_weather_layout, null);
				v.setPadding(10, 10, 10, 10);
				
				CustomTextView timeTxt = (CustomTextView) v.findViewById(R.id.odTodyWeatherTime);
				CustomTextView tempTxt = (CustomTextView) v.findViewById(R.id.odTodyWeatherTemp);
				ImageView weatherImg = (ImageView) v.findViewById(R.id.odTodyWeatherImg);
				
				//timeTxt.setText(hrsDays[i]);
				timeTxt.setText(Utils.splitToHr(time));
				tempTxt.setText(tempInCentigrade+"\u2103");
				weatherImg.setImageDrawable(
						Utils.getOutdoorTemperatureImage(context,weatherDesc, isdaytime));
				LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				this.addView(v, parentParams);
			}
		}
		
	}
	
	
}