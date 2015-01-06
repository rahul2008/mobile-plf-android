package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
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
					OutdoorCityInfo cityInfo = outdoorCity.getOutdoorCityInfo();
					if (isUSEmbassyCity(key)) {
						String appendUSEmbassy = " ( "+ PurAirApplication.getAppContext().getString(R.string.us_embassy) + " )" ;
						cityInfo.setCityName(cityInfo.getCityName() + appendUSEmbassy);
						cityInfo.setCityNameCN(cityInfo.getCityNameCN() + appendUSEmbassy);
						cityInfo.setCityNameTW(cityInfo.getCityNameTW() + appendUSEmbassy);
					}
					outdoorCityInfoList.add(cityInfo) ;
				}
			}
		}
		return outdoorCityInfoList ;
	}
	
	private static boolean isCityAddedUserSelectedList(String areaId) {
		List<String> userCitiesList = OutdoorManager.getInstance().getUsersCitiesList();
		return userCitiesList.contains(areaId);
	}
	
	private static boolean isUSEmbassyCity(String areaId) {
		List<String> usEmbassyCityList = OutdoorManager.getInstance().getUSEmbassyCities();
		return usEmbassyCityList.contains(areaId);
	}
}
