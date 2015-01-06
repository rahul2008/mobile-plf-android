package com.philips.cl.di.dev.pa.outdoorlocations.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorDataProvider;
import com.philips.cl.di.dev.pa.outdoorlocations.USEmbassyCityData;
import com.philips.cl.di.dev.pa.outdoorlocations.USEmbassyCityData.USEmbassyCityDetail;
import com.philips.cl.di.dev.pa.util.DataParser;

public class USEmbassyCityDataTest extends TestCase {

	private String json;
	
	@Override
	protected void setUp() throws Exception {
		try {
			InputStream is = PurAirApplication.getAppContext().getAssets().open("city_list_us_embassy.json");
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
	
	public void testReadJsonFileFromAssest() {
		assertNotNull(json);
	}
	
	public void testparseUSEmbassyCityData() {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData(json);
		assertNotNull(usEmbassyCityData);
	}
	
	public void testparseUSEmbassyCityDataError() {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData("json");
		assertNull(usEmbassyCityData);
	}
	
	public void testparseUSEmbassyCityDataNull() {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData(null);
		assertNull(usEmbassyCityData);
	}
	
	public void testparseUSEmbassyCityDataEmpty() {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData("");
		assertNull(usEmbassyCityData);
	}
	
	public void testParseUSEmbassyCityAreaId() {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData(json);
		USEmbassyCityDetail embassyCityDetail = usEmbassyCityData.getUSEmbassyCitiesData().get(0);
		assertEquals("101010100", embassyCityDetail.getAreaID());
	}
	
	public void testParseUSEmbassyCityNameEN() {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData(json);
		USEmbassyCityDetail embassyCityDetail = usEmbassyCityData.getUSEmbassyCitiesData().get(0);
		assertEquals("beijing", embassyCityDetail.getNameEN());
	}
	
	public void testParseUSEmbassyCityNameCN() {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData(json);
		USEmbassyCityDetail embassyCityDetail = usEmbassyCityData.getUSEmbassyCitiesData().get(0);
		assertEquals("北京", embassyCityDetail.getNameCN());
	}
	
	public void testParseUSEmbassyCityNameTW() {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData(json);
		USEmbassyCityDetail embassyCityDetail = usEmbassyCityData.getUSEmbassyCitiesData().get(0);
		assertEquals("北京", embassyCityDetail.getNameTW());
	}
	
	public void testParseUSEmbassyCityLat() {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData(json);
		USEmbassyCityDetail embassyCityDetail = usEmbassyCityData.getUSEmbassyCitiesData().get(0);
		assertEquals("39.904", embassyCityDetail.getLatitude());
	}
	
	public void testParseUSEmbassyCityLong() {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData(json);
		USEmbassyCityDetail embassyCityDetail = usEmbassyCityData.getUSEmbassyCitiesData().get(0);
		assertEquals("116.391", embassyCityDetail.getLongitude());
	}
	
	public void testUSEmbassyCityMapProfileTime() {
		USEmbassyCityData usEmbassyCityData = DataParser.parseUSEmbassyCityData(json);
		List<USEmbassyCityDetail> usEmbassyList = usEmbassyCityData.getUSEmbassyCitiesData();
		Map<String, OutdoorCityInfo> usEmbassyMap = new HashMap<String, OutdoorCityInfo>(); 
		for (USEmbassyCityDetail embassyCityDetail : usEmbassyList) {
			usEmbassyMap.put(embassyCityDetail.getAreaID(), new OutdoorCityInfo(embassyCityDetail.getNameEN(), embassyCityDetail.getNameCN(), embassyCityDetail.getNameTW(), Float.parseFloat(embassyCityDetail.getLongitude()), Float.parseFloat(embassyCityDetail.getLatitude()), embassyCityDetail.getAreaID(), OutdoorDataProvider.US_EMBASSY.ordinal()));
		}
	}
}
