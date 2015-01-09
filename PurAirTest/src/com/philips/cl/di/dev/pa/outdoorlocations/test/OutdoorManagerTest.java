package com.philips.cl.di.dev.pa.outdoorlocations.test;

import java.util.List;

import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;

import junit.framework.TestCase;

public class OutdoorManagerTest extends TestCase {
	
	private OutdoorManager outdoorManager;
	
	@Override
	protected void setUp() throws Exception {
		outdoorManager = OutdoorManager.getInstance();
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

}
