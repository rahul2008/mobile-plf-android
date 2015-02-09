package com.philips.cl.di.dev.pa.outdoorlocations.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.outdoorlocations.NearbyCitiesData;
import com.philips.cl.di.dev.pa.outdoorlocations.NearbyCitiesData.LocalityInfo;
import com.philips.cl.di.dev.pa.util.DataParser;

import junit.framework.TestCase;

public class NearbyCitiesDataTest extends TestCase {
	
	private String json;
	
	@Override
	protected void setUp() throws Exception {
		try {
			InputStream is = PurAirApplication.getAppContext().getAssets().open("nearby_cities_list.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.setUp();
	}
	
	public void testReadJson() {
		assertNotNull(json);
	}
	
	public void testParseNull() {
		NearbyCitiesData citiesData = DataParser.parseNearbyCitiesJson(null);
		assertNull(citiesData);
	}
	
	public void testParseEmptyJson() {
		NearbyCitiesData citiesData = DataParser.parseNearbyCitiesJson("");
		assertNull(citiesData);
	}
	
	//Properly formatted Json doesn't return null, but the data structure will be empty.
	public void testParseIncorrectInput() {
		NearbyCitiesData citiesData = DataParser.parseNearbyCitiesJson("{\"balance\":1000.21,\"num\":100,\"nickname\":null,\"is_vip\":true,\"name\":\"foo\"}");
		assertEquals(0, citiesData.getNearbyCitiesMap().size());
	}
	
	public void testParseValidJson() {
		NearbyCitiesData citiesData = DataParser.parseNearbyCitiesJson(json);
		assertNotNull(citiesData);
	}
	
	public void testMapSize() {
		NearbyCitiesData citiesData = DataParser.parseNearbyCitiesJson(json);
		Map<String, List<LocalityInfo>> nearbyCitiesMap = citiesData.getNearbyCitiesMap();
		assertEquals(3, nearbyCitiesMap.size());
	}
	
	public void testListSize() {
		NearbyCitiesData citiesData = DataParser.parseNearbyCitiesJson(json);
		Map<String, List<LocalityInfo>> nearbyCitiesMap = citiesData.getNearbyCitiesMap();
		List<LocalityInfo> localityInfos = nearbyCitiesMap.get("101010100");
		assertEquals(14, localityInfos.size());
	}
	
	public void testListLocalityItemName() {
		NearbyCitiesData citiesData = DataParser.parseNearbyCitiesJson(json);
		Map<String, List<LocalityInfo>> nearbyCitiesMap = citiesData.getNearbyCitiesMap();
		List<LocalityInfo> localityInfos = nearbyCitiesMap.get("101010100");
		assertEquals("haidian", localityInfos.get(0).getLocalityEN());
	}
	
	public void testListLocalityItemAreaID() {
		NearbyCitiesData citiesData = DataParser.parseNearbyCitiesJson(json);
		Map<String, List<LocalityInfo>> nearbyCitiesMap = citiesData.getNearbyCitiesMap();
		List<LocalityInfo> localityInfos = nearbyCitiesMap.get("101020100");
		assertEquals("101020200", localityInfos.get(0).getAreaID());
	}
	
	public void testListLocalityItemNameCN() {
		NearbyCitiesData citiesData = DataParser.parseNearbyCitiesJson(json);
		Map<String, List<LocalityInfo>> nearbyCitiesMap = citiesData.getNearbyCitiesMap();
		List<LocalityInfo> localityInfos = nearbyCitiesMap.get("101280101");
		assertEquals("番禺", localityInfos.get(0).getLocalityCN());
	}

	public void testListLocalityItemLong() {
		NearbyCitiesData citiesData = DataParser.parseNearbyCitiesJson(json);
		Map<String, List<LocalityInfo>> nearbyCitiesMap = citiesData.getNearbyCitiesMap();
		List<LocalityInfo> localityInfos = nearbyCitiesMap.get("101010100");
		assertEquals("116.378", localityInfos.get(0).getLongitude());
	}
	
	public void testListLocalityItemLat() {
		NearbyCitiesData citiesData = DataParser.parseNearbyCitiesJson(json);
		Map<String, List<LocalityInfo>> nearbyCitiesMap = citiesData.getNearbyCitiesMap();
		List<LocalityInfo> localityInfos = nearbyCitiesMap.get("101010100");
		assertEquals("39.913", localityInfos.get(0).getLatitude());
	}
}
