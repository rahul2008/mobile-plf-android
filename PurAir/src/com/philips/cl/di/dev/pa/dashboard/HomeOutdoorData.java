package com.philips.cl.di.dev.pa.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;

public class HomeOutdoorData implements ServerResponseListener {
	
	private static HomeOutdoorData newInstance;
	private List<Integer> outdoorAqiPercentList;
	
	private HomeOutdoorData() {
		outdoorAqiPercentList = new ArrayList<Integer>();
	}
	
	public void startOutdoorAQITask() {
		TaskGetHttp shanghaiAQI = new TaskGetHttp(
				AppConstants.SHANGHAI_OUTDOOR_AQI_URL, PurAirApplication.getAppContext(), this);
		shanghaiAQI.start();

	}

	
	public static HomeOutdoorData getInstance() {
		if(newInstance == null) {
			newInstance = new HomeOutdoorData();
		}
		return newInstance;
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor percent download response: " + responseCode);
		if (responseCode == 200) {
			SessionDto.getInstance().setOutdoorEventDto(DataParser.parseOutdoorAQIData(responseData));
			calculateAqiPercentage();
		}
	}
	
	private void calculateAqiPercentage() {
		OutdoorAQIEventDto aqiEventDto = SessionDto.getInstance().getOutdoorEventDto();
		if (aqiEventDto == null) {
			return;
		}
		String currentCityTimeHr = "0";
		if (aqiEventDto.getT() != null) {
			currentCityTimeHr =  aqiEventDto.getT().substring(11, 13);
		}
		int hr = Utils.getLastDayHours(currentCityTimeHr);
		int idx[] = aqiEventDto.getIdx();
		if (idx == null ||idx.length == 0) {
			return;
		}
		int last7dayHrs = 6 * 24 + hr;
		int last4WeekHrs = 3 * 7 * 24 + 6 * 24 + hr;
		if (idx.length < last4WeekHrs) {
			return;
		}
		
		if (outdoorAqiPercentList.size() > 0) {
			outdoorAqiPercentList.clear();
		}
		
		int goodAirCount = 0;
		for (int index = 0; index < last4WeekHrs; index++) {
			if (index == 0 && idx[index] == 0) {
				idx[index] = idx[index + 1];
			}
			if (idx[index] <= 50) {
				goodAirCount++;
			}
			
			if (index == 23) {
				outdoorAqiPercentList.add(Utils.getPercentage(goodAirCount, 24));
			}
			
			if (index == last7dayHrs - 1) {
				outdoorAqiPercentList.add(Utils.getPercentage(goodAirCount, last7dayHrs));
			}
			
			if (index == last4WeekHrs - 1) {
				outdoorAqiPercentList.add(Utils.getPercentage(goodAirCount, last4WeekHrs));
			}
		}
	}
	
	public List<Integer> getOutdoorPercentList() {
		ALog.i(ALog.OUTDOOR_DETAILS, "Percentage list outdoorAqiPercentList: " + outdoorAqiPercentList);
		return outdoorAqiPercentList;
	}
}
