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
import com.philips.cl.di.dev.pa.outdoorlocations.UserCitiesDatabase;
import com.philips.cl.di.dev.pa.util.LocationUtils;

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
		Map<String, OutdoorCity> outdoorCityMap = OutdoorManager.getInstance().getCitiesMap() ;
		
		List<OutdoorCityInfo> outdoorCityInfoList = AddOutdoorLocationHelper.getAllCityInfoList(outdoorCityMap) ;
		List<String> userCitiesList = new UserCitiesDatabase().getAllCities();
		
		int userCitiesListSize = userCitiesList.size();
		
		assertEquals(outdoorCityMap.size() - userCitiesListSize, outdoorCityInfoList.size()) ;
	}
	
	public void testGetFirstWordCapitalInSentence() {
		String cityName = AddOutdoorLocationHelper.getFirstWordCapitalInSentence("beijing");
		assertEquals("Beijing", cityName) ;
	}
	
	public void testGetCityKeyWithRespectDataProviderUSEmbassy() {
		OutdoorCityInfo outdoorCityInfo = new OutdoorCityInfo("beijing", "beijing", "beijing", 1F, 2F, "1011010", 1);
		String key = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(outdoorCityInfo);
		assertEquals("beijing", key);
	}
	
	public void testGetCityKeyWithRespectDataProviderCMA() {
		OutdoorCityInfo outdoorCityInfo = new OutdoorCityInfo("beijing", "beijing", "beijing", 1F, 2F, "1011010", 0);
		String key = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(outdoorCityInfo);
		assertEquals("1011010", key);
	}
	
	public void testGetSortedUserSelectedCitiesInfo1() {
		List<String> ids = new ArrayList<String>();
		ids.add("101090701");//Cangzhou
		ids.add("101010100");//Beijing
		ids.add("101161301");//Baiyin
		ids.add("chengdu");//chengdu
		ids.add("shanghai");//shanghai
		ids.add("101050503");//Anda
		List<OutdoorCityInfo> outdoorCityInfos = AddOutdoorLocationHelper.getSortedUserSelectedCitiesInfo(ids);
		assertEquals("101050503", outdoorCityInfos.get(0).getAreaID());
	}
	
	public void testGetSortedUserSelectedCitiesInfo2() {
		List<String> ids = new ArrayList<String>();
		ids.add("101090701");//Cangzhou
		ids.add("101010100");//Beijing
		ids.add("101161301");//Baiyin
		ids.add("chengdu");//chengdu
		ids.add("shanghai");//shanghai
		ids.add("101050503");//Anda
		List<OutdoorCityInfo> outdoorCityInfos = AddOutdoorLocationHelper.getSortedUserSelectedCitiesInfo(ids);
		assertEquals("101020100", outdoorCityInfos.get(5).getAreaID());
	}
	
}
