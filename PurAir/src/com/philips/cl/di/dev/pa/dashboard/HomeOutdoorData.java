package com.philips.cl.di.dev.pa.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;

public class HomeOutdoorData implements ServerResponseListener {
	
	private static HomeOutdoorData newInstance;
	private List<Integer> outdoorAqiPercentList;
	
	private HomeOutdoorData() {
		outdoorAqiPercentList = new ArrayList<Integer>();
	}
	
	public void startOutdoorCurrentLocationAQITask() {
		
		if (OutdoorController.getInstance().isPhilipsSetupWifiSelected()) return;
		
		String areaID = LocationUtils.getCurrentLocationAreaId();
		if (areaID.isEmpty())  return;
		
		TimeZone timeZoneChina = TimeZone.getTimeZone("GMT+8");
		TimeZone timeZoneCurrent = Calendar.getInstance().getTimeZone();
		
		//Time difference between time zone and GMT
		int offsetChina = timeZoneChina.getOffset(Calendar.getInstance().getTimeInMillis());
		int offsetCurrent = timeZoneCurrent.getOffset(Calendar.getInstance().getTimeInMillis());
		int offset = offsetChina - offsetCurrent;
		
		Calendar cal = Calendar.getInstance(timeZoneChina);
		// cal.getTimeInMillis() selected time zone, time in milli seconds, it give same time all time zone
		// So we implemented time zone concept to get selected time zone, time in milli seconds
		
		TaskGetHttp aqiHistoricTask = new TaskGetHttp(OutdoorController.getInstance().buildURL(
				OutdoorController.BASE_URL_AQI, areaID, "air_his", 
				Utils.getDate((cal.getTimeInMillis() + offset - (1000 * 60 * 60 * 24 * 30l))) + "," 
				+ Utils.getDate(cal.getTimeInMillis() + offset), OutdoorController.APP_ID), 
				areaID, PurAirApplication.getAppContext(), this);
		aqiHistoricTask.start();
	}

	
	public static HomeOutdoorData getInstance() {
		if(newInstance == null) {
			newInstance = new HomeOutdoorData();
		}
		return newInstance;
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData, String areaID) {
		ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor percent download response: " + responseCode);
		if (responseCode == 200) {
//			SessionDto.getInstance().setOutdoorEventDto(DataParser.parseOutdoorAQIData(responseData));
//			calculateAqiPercentage();
			
			List<OutdoorAQI> outdoorAQIs = DataParser.parseHistoricalAQIData(responseData, areaID);
			calculateAqiPercentage(outdoorAQIs);
		}
		
	}
	
	@SuppressLint("SimpleDateFormat")
	private void calculateAqiPercentage(List<OutdoorAQI> outdoorAQIs) {
		if (outdoorAQIs == null || outdoorAQIs.isEmpty()) return;
		
		if (!outdoorAqiPercentList.isEmpty()) outdoorAqiPercentList.clear();

		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calenderGMTChinese = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		String currentDateStr = formatDate.format(Utils.getCurrentChineseDate());
		String currentDateWithHrStr = currentDateStr + " " + 
				Utils.get2DigitHr(calenderGMTChinese.get(Calendar.HOUR_OF_DAY)); 
		
		int totalAqi = 0;
		int goodAqi = 0;
		int monthlyTotalAqi = 0;
		int monthlyGoodAqi = 0;
		int weeklyTotalAqi = 0;
		int weeklyGoodAqi = 0;
		
		for (int index = 0; index < outdoorAQIs.size(); index++) {
			if (outdoorAQIs.get(index) == null || outdoorAQIs.get(index).getTimeStamp() == null) return;
			String upadtedDateStr = Utils.getHistoricDataUpdateDate(outdoorAQIs.get(index).getTimeStamp());
			float aqi = outdoorAQIs.get(index).getAQI();
			int numberOfHr = Utils.getDifferenceBetweenHrFromCurrentHr(currentDateWithHrStr,
					upadtedDateStr + " " + outdoorAQIs.get(index).getTimeStamp().substring(8, 10));
			if (numberOfHr >= 0 && numberOfHr < 24) {
				totalAqi = totalAqi + 1;
				if (aqi <= 50) {
					goodAqi = goodAqi + 1; 
				}
				
				ALog.i(ALog.OUTDOOR_DETAILS, "Percentage 24hr aqi: " + aqi 
						+ " ; good: " + goodAqi + " ; totalAqi: " + totalAqi);
			}
			int numberOfDays = Utils.getDifferenceBetweenDaysFromCurrentDay(currentDateStr, upadtedDateStr);
			if (numberOfDays >= 0 && numberOfDays < 28) {
				
				if (numberOfDays < 7) {
					weeklyTotalAqi = weeklyTotalAqi + 1;
					if (aqi <= 50) {
						weeklyGoodAqi = weeklyGoodAqi + 1; 
					}
					ALog.i(ALog.OUTDOOR_DETAILS, "Percentage weekly aqi: " + aqi 
							+ " ; good: " + weeklyGoodAqi + " ; weeklyTotalAqi: " + weeklyTotalAqi);
				}
				
				monthlyTotalAqi = monthlyTotalAqi + 1;
				if (aqi <= 50) {
					monthlyGoodAqi = monthlyGoodAqi + 1; 
				}
				
				ALog.i(ALog.OUTDOOR_DETAILS, "Percentage monthly aqi: " + aqi 
						+ " ; good: " + monthlyGoodAqi + " ; monthlyTotalAqi: " + monthlyTotalAqi);
			}
		}

		outdoorAqiPercentList.add(Utils.getPercentage(goodAqi, totalAqi));
		outdoorAqiPercentList.add(Utils.getPercentage(weeklyGoodAqi, weeklyTotalAqi));
		outdoorAqiPercentList.add(Utils.getPercentage(monthlyGoodAqi, monthlyTotalAqi));
	}
	
	public List<Integer> getOutdoorPercentList() {
		ALog.i(ALog.OUTDOOR_DETAILS, "Percentage list outdoorAqiPercentList: " + outdoorAqiPercentList);
		return outdoorAqiPercentList;
	}
}
