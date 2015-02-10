package com.philips.cl.di.dev.pa.dashboard;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import android.annotation.SuppressLint;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.Utils;


public class OutdoorDetailHelper {
	private HashMap<Integer, Float> dailyAqiValueMap;
	private HashMap<Integer, Integer> dailyAqiValueCounterMap;
	private Calendar calenderGMTChinese;
	private int currentCityHourOfDay;
	private int currentCityDayOfWeek;
	private float lastDayAQIHistoricArr[];
	private float last7dayAQIHistoricArr[];
	private float last4weekAQIHistoricArr[];
	
	public OutdoorDetailHelper(float lastDayAQIHistoricArr[], 
			float last7dayAQIHistoricArr[], float last4weekAQIHistoricArr[]) {
		calenderGMTChinese = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		this.lastDayAQIHistoricArr = lastDayAQIHistoricArr;
		this.last7dayAQIHistoricArr = last7dayAQIHistoricArr;
		this.last4weekAQIHistoricArr = last4weekAQIHistoricArr;
	}
	
	public float[] getUpdateLastDayAQIHistoricArr() {
		return lastDayAQIHistoricArr;
	}
	
	public float[] getUpdateLast7DayAQIHistoricArr() {
		return last7dayAQIHistoricArr;
	}
	
	public float[] getUpdateLast4weekAQIHistoricArr() {
		return last4weekAQIHistoricArr;
	}
	
	public int getCurrentCityHourOfDay() {
		return currentCityHourOfDay;
	}
	
	public int getCurrentCityDayOfWeek() {
		return currentCityDayOfWeek;
	}
	
	@SuppressLint("UseSparseArrays")
	public void calculateCMAAQIHistoricData(List<OutdoorAQI> outdoorAQIs) {
		
		HashMap<String, Float> allHourlyAqiValueMap = new HashMap<String, Float>();
		HashMap<String, Integer> allHourlyAqiValueCounterMap = new HashMap<String, Integer>();
		
		dailyAqiValueMap = new HashMap<Integer, Float>();
		dailyAqiValueCounterMap = new HashMap<Integer, Integer>();
		
		for (int index = 0; index < outdoorAQIs.size(); index++) {
			if (outdoorAQIs.get(index) == null || outdoorAQIs.get(index).getTimeStamp() == null) return;
			String updatedDateStr = Utils.getHistoricDataUpdateDate(outdoorAQIs.get(index).getTimeStamp());
			float aqi = outdoorAQIs.get(index).getAQI();
			String dateWithHour = updatedDateStr + " " + outdoorAQIs.get(index).getTimeStamp().substring(8, 10);
			
			if (allHourlyAqiValueMap.containsKey(dateWithHour) 
					&& allHourlyAqiValueCounterMap.containsKey(dateWithHour)) {
				
				float faqi = allHourlyAqiValueMap.get(dateWithHour);
				faqi = faqi + aqi;
				allHourlyAqiValueMap.put(dateWithHour, faqi);
				int counterMap = allHourlyAqiValueCounterMap.get(dateWithHour);
				counterMap++;
				allHourlyAqiValueCounterMap.put(dateWithHour, counterMap);
			} else {
				allHourlyAqiValueMap.put(dateWithHour, aqi);
				allHourlyAqiValueCounterMap.put(dateWithHour, 1);
			}
		}
		averageHourlyAQI(allHourlyAqiValueMap, allHourlyAqiValueCounterMap);
		updateWeeklyArray(dailyAqiValueMap, dailyAqiValueCounterMap);
		
		//Clear object
		allHourlyAqiValueMap.clear();
		allHourlyAqiValueMap = null;
		allHourlyAqiValueCounterMap.clear();
		allHourlyAqiValueCounterMap = null;
		clearListObject();
	}
	
	@SuppressLint("UseSparseArrays")
	public void calculateUSEmbassyAQIHistoricData(List<OutdoorAQI> outdoorAQIs) {
		String cityNowDateStr = null;
		int cityNowAQI = 0;
		dailyAqiValueMap = new HashMap<Integer, Float>();
		dailyAqiValueCounterMap = new HashMap<Integer, Integer>();
		for (OutdoorAQI  outdoorAQI : outdoorAQIs) {
			String timeStamp = outdoorAQI.getTimeStamp();
			int aqi = outdoorAQI.getAQI();
			if (timeStamp == null) return;
			if (timeStamp.length() == 16) {// 16, date with time format yyyy-mm-dd HH:mm
				updateLastDayArray(aqi, timeStamp);
				cityNowDateStr = getDateFromTimeStamp(timeStamp);
				cityNowAQI = aqi;
			} else { 
				calulateDailyAQI(aqi, timeStamp);
			}
		}
		
		//If current day data not available, add city now data in current day date
		if (!dailyAqiValueMap.containsKey(0) && cityNowDateStr != null) {// 0, current day value
			calulateDailyAQI(cityNowAQI, cityNowDateStr);
		}
		updateWeeklyArray(dailyAqiValueMap, dailyAqiValueCounterMap);
		clearListObject();
	}
	
	
	private void averageHourlyAQI(HashMap<String, Float> allHourlyAqiValueMap,
			HashMap<String, Integer> allHourlyAqiValueCounterMap) {
		if (allHourlyAqiValueMap.isEmpty()) return;
		
		Set<String> mapKeys = allHourlyAqiValueMap.keySet();
		for (String mapKey : mapKeys) {
			if (allHourlyAqiValueMap.containsKey(mapKey) 
					&& allHourlyAqiValueCounterMap.containsKey(mapKey)) {
				float aqi = allHourlyAqiValueMap.get(mapKey) / allHourlyAqiValueCounterMap.get(mapKey);
				allHourlyAqiValueMap.put(mapKey, aqi);
				updateLastDayArray(aqi, mapKey);
				calulateDailyAQI(aqi, mapKey);
			}
		}
	}
	
	private void updateLastDayArray(float aqi, String timeStamp) {
		String currentDateStr = AppConstants.DATE_FORMAT_YYYY_MM_DD.format(Utils.getCurrentChineseDate());
		String currentDateWithHrStr = currentDateStr + " " + 
				Utils.get2DigitHr(calenderGMTChinese.get(Calendar.HOUR_OF_DAY));
		currentCityHourOfDay = calenderGMTChinese.get(Calendar.HOUR_OF_DAY);
		
		int numberOfHr = Utils.getDifferenceBetweenHrFromCurrentHr(currentDateWithHrStr, timeStamp);
		
		if (numberOfHr >= 0 && numberOfHr < 24) {
			lastDayAQIHistoricArr[23 - numberOfHr] = aqi;
		}
	}
	
	private void calulateDailyAQI(float aqi, String timeStap) {
		
		String currentDateStr = AppConstants.DATE_FORMAT_YYYY_MM_DD.format(Utils.getCurrentChineseDate());
		currentCityDayOfWeek = calenderGMTChinese.get(Calendar.DAY_OF_WEEK);
		String updatedDateStr = "";
		String [] keyArr = timeStap.split(" ");
		if (keyArr != null && keyArr.length > 0) {
			updatedDateStr = keyArr[0];
		}
		
		int numberOfDays = Utils.getDifferenceBetweenDaysFromCurrentDay(currentDateStr, updatedDateStr);
		if (numberOfDays >= 0) {
			if (dailyAqiValueMap.containsKey(numberOfDays) 
					&& dailyAqiValueCounterMap.containsKey(numberOfDays)) {
				float tempAqi = dailyAqiValueMap.get(numberOfDays);
				tempAqi = tempAqi + aqi;
				dailyAqiValueMap.put(numberOfDays, tempAqi);
				int counterMap = dailyAqiValueCounterMap.get(numberOfDays);
				counterMap++;
				dailyAqiValueCounterMap.put(numberOfDays, counterMap);

			} else {
				dailyAqiValueCounterMap.put(numberOfDays, 1);
				dailyAqiValueMap.put(numberOfDays, aqi);
			}
		}
	}
	
	private void updateWeeklyArray(HashMap<Integer, Float> dailyAqiValueMap, 
			HashMap<Integer, Integer> dailyAqiValueCounterMap) {
		if (dailyAqiValueMap.isEmpty()) return;
		for (int mapKey = 0; mapKey < dailyAqiValueMap.size(); mapKey++) {
			float avgAqi = -1F;
			if (mapKey < 28) {
				if (dailyAqiValueMap.containsKey(mapKey) 
						&& dailyAqiValueCounterMap.containsKey(mapKey)) {
					avgAqi = dailyAqiValueMap.get(mapKey) / dailyAqiValueCounterMap.get(mapKey);
				} 
				
				if (mapKey < 7) {
					last7dayAQIHistoricArr[6 - mapKey] = avgAqi;
				}
				if (mapKey < 28) {
					last4weekAQIHistoricArr[27 - mapKey] = avgAqi;
				}
			} else {
				break;
			}
		}
	}
	
	private String getDateFromTimeStamp(String timeStamp) {
		String dateStr = null;
		if (timeStamp != null && timeStamp.length() == 16) {// 16, date with time format yyyy-mm-dd HH:mm
			dateStr = timeStamp.substring(0, 10);
		}
		return dateStr;
	}
	
	private void clearListObject() {
		dailyAqiValueMap.clear();
		dailyAqiValueMap = null;
		dailyAqiValueCounterMap.clear();
		dailyAqiValueCounterMap = null;
	}
	
}
