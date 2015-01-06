package com.philips.cl.di.dev.pa.outdoorlocations.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.outdoorlocations.AddOutdoorLocationHelper;

public class AddOutdoorLocationHelperTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private List<OutdoorCityInfo> outdoorCityInfoList ;
	
	public AddOutdoorLocationHelperTest() {
		super(MainActivity.class);
	}
	
	
	@Override
	protected void setUp() throws Exception {
		
		setActivityInitialTouchMode(false);
		getActivity();
		
		outdoorCityInfoList = new ArrayList<OutdoorCityInfo>() ;
		outdoorCityInfoList.add(new OutdoorCityInfo("Shanghai", "", "", 10.1f, 11.0f,"1111", 0)) ;
		outdoorCityInfoList.add(new OutdoorCityInfo("Beijing", "", "", 10.1f, 11.0f,"1111", 0)) ;
		outdoorCityInfoList.add(new OutdoorCityInfo("Chengdu", "", "", 10.1f, 11.0f,"1111", 0)) ;
		outdoorCityInfoList.add(new OutdoorCityInfo("Bangalore", "", "", 10.1f, 11.0f,"1111", 0)) ;
		outdoorCityInfoList.add(new OutdoorCityInfo("Anda", "", "", 10.1f, 11.0f,"1111", 0)) ;
		Collections.sort(outdoorCityInfoList) ;
		super.setUp();
	}
	
	public void testOutdoorLocationCitySortbyEnglishName() {
		assertEquals("Anda", outdoorCityInfoList.get(0).getCityName()) ;
		assertEquals("Bangalore", outdoorCityInfoList.get(1).getCityName()) ;
	}
	
	public void testOutdoorLocationSortbyEnglishNameFail() {
		assertNotSame("Beijing", outdoorCityInfoList.get(1).getCityName()) ;
	}
	
	public void testMaptoList() {
		Map<String, OutdoorCity> outdoorCityMap = OutdoorManager.getInstance().getCityMap() ;
		
		List<OutdoorCityInfo> outdoorCityInfoList = AddOutdoorLocationHelper.getAllCityInfoList(outdoorCityMap) ;
		List<String> userCitiesList = OutdoorManager.getInstance().getUsersCitiesList();
		int userCitiesListSize = userCitiesList.size();
		
		assertEquals(outdoorCityMap.size() - userCitiesListSize, outdoorCityInfoList.size()) ;
	}
	
}
