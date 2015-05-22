package com.philips.cl.di.dev.pa.outdoorlocations.test;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.util.OutdoorDetailsListener;

@SuppressWarnings("unchecked")
public class OutdoorManagerTest extends InstrumentationTestCase {
	
	private OutdoorManager outdoorManager;
	private OutdoorDetailsListener outdoorDetailsListener;
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

		outdoorManager = OutdoorManager.getInstance();
		outdoorManager.saveNearbyCityData();
		
		outdoorDetailsListener = mock(OutdoorDetailsListener.class);
		outdoorManager.addOutdoorDetailsListener(outdoorDetailsListener);
		
		super.setUp();
	}
	
	public void testCMACityListSize() {
		assertEquals(195, outdoorManager.getCMACities().size());
	}
	
	public void testCMACityDataFromMap1() {
		List<String> citiesId = outdoorManager.getCMACities();
		OutdoorCity outdoorCity = outdoorManager.getCityData(citiesId.get(0));
		assertNotNull(outdoorCity);
	}
	
	public void testCMACityDataFromMap2() {
		OutdoorCity outdoorCity = outdoorManager.getCityData("1101");
		assertNull(outdoorCity);
	}
	
	public void testUSEmbassyCityListSize() {
		assertEquals(5, outdoorManager.getUSEmbassyCities().size());
	}
	
	public void testGetLocalityNameFromAreaIdNullAreaId() {
		String name = outdoorManager.getLocalityNameFromAreaId(null, "101010200");
		assertNull(name);
	}
	
	public void testGetLocalityNameFromAreaIdEmptyAreaId() {
		String name = outdoorManager.getLocalityNameFromAreaId("", "101010200");
		assertNull(name);
	}
	
	public void testGetLocalityNameFromAreaIdNullLocalityId() {
		String name = outdoorManager.getLocalityNameFromAreaId("101010100", null);
		assertNull(name);
	}
	
	public void testGetLocalityNameFromAreaIdEmptyLocalityId() {
		String name = outdoorManager.getLocalityNameFromAreaId("101010100", "");
		assertNull(name);
	}
	
	public void testGetLocalityNameFromAreaIdNotExistant_1() {
		String name = outdoorManager.getLocalityNameFromAreaId("101050503", "101010200");
		assertNull(name);
	}
	
	public void testGetLocalityNameFromAreaIdNotExistant_2() {
		String name = outdoorManager.getLocalityNameFromAreaId("101055503", "101010205");
		assertNull(name);
	}
	
	public void testGetLocalityNameFromAreaId() {
		String name = outdoorManager.getLocalityNameFromAreaId("101010100", "101010200");
		assertEquals("Haidian", name);
	}
	
	public void testNearbyLocationDataReceived() {
		List<OutdoorAQI> aqis = new ArrayList<OutdoorAQI>();
		outdoorManager.nearbyLocationsAQIReceived(aqis);
		verify(outdoorDetailsListener, times(1)).onNearbyLocationsDataReceived(aqis);
		verify(outdoorDetailsListener, never()).onOneDayWeatherForecastReceived(anyList());
		verify(outdoorDetailsListener, never()).onFourDayWeatherForecastReceived(anyList());
//		verify(outdoorDetailsListener, never()).onAQIHistoricalDataReceived(anyList(), "");
	}
	
	public void testHistoricalAQIDataReceived() {
		List<OutdoorAQI> aqis = new ArrayList<OutdoorAQI>();
		outdoorManager.outdoorHistoricalAQIDataReceived(aqis, "");
		verify(outdoorDetailsListener, never()).onNearbyLocationsDataReceived(anyList());
		verify(outdoorDetailsListener, never()).onOneDayWeatherForecastReceived(anyList());
		verify(outdoorDetailsListener, never()).onFourDayWeatherForecastReceived(anyList());
//		verify(outdoorDetailsListener, times(1)).onAQIHistoricalDataReceived(aqis, "");
	}
	
	public void testFourDayWeatherForecastReceived() {
		List<ForecastWeatherDto> weatherDtos = new ArrayList<ForecastWeatherDto>();
		outdoorManager.outdoorFourDayForecastDataReceived(weatherDtos);
		verify(outdoorDetailsListener, never()).onNearbyLocationsDataReceived(anyList());
		verify(outdoorDetailsListener, never()).onOneDayWeatherForecastReceived(anyList());
		verify(outdoorDetailsListener, times(1)).onFourDayWeatherForecastReceived(weatherDtos);
//		verify(outdoorDetailsListener, never()).onAQIHistoricalDataReceived(anyList(), "");
	}
	
	public void testOneDayForecastReceived() {
		List<Weatherdto> weatherdtos = new ArrayList<Weatherdto>();
		outdoorManager.outdoorOneDayForecastDataReceived(weatherdtos);
		verify(outdoorDetailsListener, never()).onNearbyLocationsDataReceived(anyList());
		verify(outdoorDetailsListener, times(1)).onOneDayWeatherForecastReceived(weatherdtos);
		verify(outdoorDetailsListener, never()).onFourDayWeatherForecastReceived(anyList());
//		verify(outdoorDetailsListener, never()).onAQIHistoricalDataReceived(anyList(), "");
	}
	
	public void testGetCityNameFromAreaIdEmpty() {
		String name = outdoorManager.getCityNameFromAreaId("");
		assertEquals("", name);
	}
	
	public void testGetCityNameFromAreaIdNull() {
		String name = outdoorManager.getCityNameFromAreaId(null);
		assertEquals("", name);
	}
	
	public void testGetCityNameFromAreaIdName() {
		String name = outdoorManager.getCityNameFromAreaId("beijing");
		assertEquals("beijing", name);
	}
	
	public void testGetCityNameFromAreaId() {
		String name = outdoorManager.getCityNameFromAreaId("101050503");
		assertEquals("anda", name);
	}

}
