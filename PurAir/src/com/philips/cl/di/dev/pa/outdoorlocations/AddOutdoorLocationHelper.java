package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;

public class AddOutdoorLocationHelper {
	
	public static List<OutdoorCityInfo> getAllCityInfoList(Map<String, OutdoorCity> cityMap) {
		
		List<OutdoorCityInfo> outdoorCityInfoList = new ArrayList<OutdoorCityInfo>() ;
		if(!cityMap.isEmpty())  {
			Iterator<String> keySetIterator = cityMap.keySet().iterator() ;
			while(keySetIterator.hasNext()) {
				String key = keySetIterator.next() ;
				
				OutdoorCity outdoorCity = cityMap.get(key) ;				
				if( outdoorCity != null && !isCityAddedUserSelectedList(key)) {					
					outdoorCityInfoList.add(outdoorCity.getOutdoorCityInfo()) ;
				}
			}
			Collections.sort(outdoorCityInfoList) ;
		}
		return outdoorCityInfoList ;
	}
	
	private static boolean isCityAddedUserSelectedList(String areaId) {
		List<String> userCitiesList = OutdoorManager.getInstance().getUsersCitiesList();
		return userCitiesList.contains(areaId);
	}
}
