package com.philips.cl.di.dev.pa.outdoorlocations.test;

import java.util.List;

import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;

public class OutdoorManagerTest extends InstrumentationTestCase {
	
	private OutdoorManager outdoorManager;
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

		outdoorManager = OutdoorManager.getInstance();
		outdoorManager.saveNearbyCityData();
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
		assertEquals("haidian", name);
	}

}
