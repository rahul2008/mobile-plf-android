package com.philips.cl.di.dev.pa.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.os.Handler;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;

public class PurifierCurrentCityData implements ServerResponseListener {
	
	private static PurifierCurrentCityData newInstance;
	private List<Integer> currentCityGoodAQList;
	private PurifierCurrentCityPercentListener listener;
	private HashMap<String, List<Integer>> purifierCurrentCityGoodAQMap;
	
	private PurifierCurrentCityData() {
		purifierCurrentCityGoodAQMap = new HashMap<String, List<Integer>>();
	}
	
	public void setListener(PurifierCurrentCityPercentListener listener) {
		this.listener = listener;
	}
	
	public void removeListener() {
		listener = null;
	}
	
	public void startCurrentCityAreaIdTask(String latitude, String longitude) {
		if (OutdoorController.getInstance().isPhilipsSetupWifiSelected() 
				|| latitude == null || latitude.isEmpty()
				|| longitude == null || longitude.isEmpty()) return;
		
		TaskGetHttp citiesList = new TaskGetHttp("http://data.fuwu.weather.com.cn/getareaid/findId?lat=" + latitude + "&lon=" + longitude, "", PurAirApplication.getAppContext(), this);
		citiesList.start();
	}
	
	private void startPurifierCurrentLocationAQITask(String areaID) {
		long daysInMillisecs = 1000 * 60 * 60 * 24 * 30l; // 30 Days
		if (OutdoorController.getInstance().isPhilipsSetupWifiSelected()) return;
		if (areaID.isEmpty())  return;
		
		long timeInMili = Utils.getCurrentChineseDate().getTime();
		TaskGetHttp aqiHistoricTask = new TaskGetHttp(OutdoorController.getInstance().buildURL(
				OutdoorController.BASE_URL_AQI, areaID, "air_his", 
				Utils.getDate((timeInMili -  daysInMillisecs)) + "," 
				+ Utils.getDate(timeInMili), OutdoorController.APP_ID), 
				areaID, PurAirApplication.getAppContext(), this);
		aqiHistoricTask.start();
	}

	
	public static PurifierCurrentCityData getInstance() {
		if(newInstance == null) {
			newInstance = new PurifierCurrentCityData();
		}
		return newInstance;
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData, String areaID) {
		ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor percent download response: " + responseCode);
		if (responseCode == 200) {
			if (responseData != null && !areaID.isEmpty()) {
				List<OutdoorAQI> outdoorAQIs = DataParser.parseHistoricalAQIData(responseData, areaID);
				calculateAqiPercentage(outdoorAQIs);
			} else if (areaID.isEmpty() && responseData != null && !responseData.isEmpty()) {
				
				String[] areaIDResponse = responseData.split(",");
				String newAreaID = "";
				if (areaIDResponse != null && areaIDResponse.length > 0) {
					String[] areaIDSplit = areaIDResponse[0].split(":");
					if (areaIDSplit != null && areaIDSplit.length > 1) {
						newAreaID = areaIDSplit[1];
					}
				}
				if (newAreaID.isEmpty()) return;
				try {
					handler.sendEmptyMessage(Integer.parseInt(newAreaID));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			startPurifierCurrentLocationAQITask(String.valueOf(msg.what));
		};
	};
	
	@SuppressLint("SimpleDateFormat")
	private void calculateAqiPercentage(List<OutdoorAQI> outdoorAQIs) {
		if (outdoorAQIs == null || outdoorAQIs.isEmpty()) return;
		
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
//			ALog.i(ALog.OUTDOOR_DETAILS, "Percentage AQI 24hr aqi: " + aqi + " " + outdoorAQIs.get(index).getTimeStamp());
			int numberOfHr = Utils.getDifferenceBetweenHrFromCurrentHr(currentDateWithHrStr,
					upadtedDateStr + " " + outdoorAQIs.get(index).getTimeStamp().substring(8, 10));
			if (numberOfHr >= 0 && numberOfHr < 24) {
				totalAqi = totalAqi + 1;
				if (aqi <= 50) {
					goodAqi = goodAqi + 1; 
				}
//				ALog.i(ALog.OUTDOOR_DETAILS, "Percentage 24hr aqi: " + aqi 
//						+ " ; good: " + goodAqi + " ; totalAqi: " + totalAqi);
			}
			int numberOfDays = Utils.getDifferenceBetweenDaysFromCurrentDay(currentDateStr, upadtedDateStr);
			if (numberOfDays >= 0 && numberOfDays < 28) {
				
				if (numberOfDays < 7) {
					weeklyTotalAqi = weeklyTotalAqi + 1;
					if (aqi <= 50) {
						weeklyGoodAqi = weeklyGoodAqi + 1; 
					}
//					ALog.i(ALog.OUTDOOR_DETAILS, "Percentage weekly aqi: " + aqi 
//							+ " ; good: " + weeklyGoodAqi + " ; weeklyTotalAqi: " + weeklyTotalAqi);
				}
				
				monthlyTotalAqi = monthlyTotalAqi + 1;
				if (aqi <= 50) {
					monthlyGoodAqi = monthlyGoodAqi + 1; 
				}
				
//				ALog.i(ALog.OUTDOOR_DETAILS, "Percentage monthly aqi: " + aqi 
//						+ " ; good: " + monthlyGoodAqi + " ; monthlyTotalAqi: " + monthlyTotalAqi);
			}
		}
		
		currentCityGoodAQList = new ArrayList<Integer>();

		currentCityGoodAQList.add(Utils.getPercentage(goodAqi, totalAqi));
		currentCityGoodAQList.add(Utils.getPercentage(weeklyGoodAqi, weeklyTotalAqi));
		currentCityGoodAQList.add(Utils.getPercentage(monthlyGoodAqi, monthlyTotalAqi));
		
		PurAirDevice currentPuriufier = PurifierManager.getInstance().getCurrentPurifier();
		
		if (currentPuriufier != null) {
			purifierCurrentCityGoodAQMap.put(currentPuriufier.getEui64(), currentCityGoodAQList) ;
		}
		
		if (listener != null) {
			listener.onTaskComplete();
		}
	}
	
	public List<Integer> getPurifierCurrentCityGoodAQ(String purifierEui64) {
		List<Integer> currentCityGoodAQListTemp  = null;
		if (purifierCurrentCityGoodAQMap.containsKey(purifierEui64)) {
			currentCityGoodAQListTemp = purifierCurrentCityGoodAQMap.get(purifierEui64);
		}
		return currentCityGoodAQListTemp;
	}
	
	/** Listener
	 * Interface
	 * @author 310151592
	 *
	 */
	public interface PurifierCurrentCityPercentListener {
		void onTaskComplete();
	}
	
}
