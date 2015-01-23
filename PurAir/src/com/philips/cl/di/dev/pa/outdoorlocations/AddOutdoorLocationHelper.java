package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.util.LocationUtils;

public class AddOutdoorLocationHelper {
	private static List<String> userCitiesList;
	public static List<OutdoorCityInfo> getAllCityInfoList(Map<String, OutdoorCity> cityMap) {
		userCitiesList = new UserCitiesDatabase().getAllCities();
		List<OutdoorCityInfo> outdoorCityInfoList = new ArrayList<OutdoorCityInfo>() ;
		if(!cityMap.isEmpty())  {
			Iterator<String> keySetIterator = cityMap.keySet().iterator() ;
			while(keySetIterator.hasNext()) {
				String key = keySetIterator.next() ;
				OutdoorCity outdoorCity = cityMap.get(key) ;	
				if( outdoorCity != null && !isCityAddedUserSelectedList(key) && outdoorCity.getOutdoorCityInfo() != null) {		
					outdoorCityInfoList.add(outdoorCity.getOutdoorCityInfo()) ;
				}
			}
			Collections.sort(outdoorCityInfoList) ;
		}
		return outdoorCityInfoList ;
	}
	
	private static boolean isCityAddedUserSelectedList(String areaId) {
		return userCitiesList.contains(areaId);
	}
	
	public static List<OutdoorCityInfo> getSortedUserSelectedCitiesInfo(List<String> userCities) {
		List<OutdoorCityInfo> outdoorCityInfoList = new ArrayList<OutdoorCityInfo>() ;
		for (String areaId : userCities) {
			OutdoorCity outdoorCity = OutdoorManager.getInstance().getCityData(areaId);	
			if( outdoorCity != null) {		
				outdoorCityInfoList.add(outdoorCity.getOutdoorCityInfo()) ;
			}
		}
		
		Collections.sort(outdoorCityInfoList) ;
		String currentLocationAreaId = LocationUtils.getCurrentLocationAreaId();
		if (!currentLocationAreaId.isEmpty()) {
			OutdoorCity currentoutdoorCity = OutdoorManager.getInstance().getCityData(currentLocationAreaId);	
			if( currentoutdoorCity != null) {		
				outdoorCityInfoList.add(0, currentoutdoorCity.getOutdoorCityInfo()) ;
			}
		}
		return outdoorCityInfoList ;
	}
	
	public static String getCityKeyWithRespectDataProvider(OutdoorCityInfo outdoorCityInfo) {
		String key = outdoorCityInfo.getAreaID();
		if (outdoorCityInfo.getDataProvider() == OutdoorDataProvider.US_EMBASSY.ordinal()) {
			key = outdoorCityInfo.getCityName();
		}
		return key;
	}
	
	@SuppressLint("DefaultLocale")
	public static String getFirstWordCapitalInSentence(String cityName) {
		StringBuilder builder = new StringBuilder() ;
		builder.append(cityName.substring(0,1).toUpperCase()).append(cityName.substring(1)) ;
		
		return builder.toString() ;
	}
	
}
