package com.philips.cl.di.dev.pa.outdoorlocations.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.outdoorlocations.CMACityData;
import com.philips.cl.di.dev.pa.outdoorlocations.CMACityData.CMACityDetail;
import com.philips.cl.di.dev.pa.util.DataParser;

import junit.framework.TestCase;

//Save this file save as UTF-8

public class CMACityDataTest extends TestCase {
	
	private String json;
	
	@Override
	protected void setUp() throws Exception {
		try {
			InputStream is = PurAirApplication.getAppContext().getAssets().open("city_list_cma.json");
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
	
	public void testparseCMACityData() {
		CMACityData cmaCityData = DataParser.parseCMACityData(json);
		assertNotNull(cmaCityData);
	}
	
	public void testparseCMACityDataError() {
		CMACityData cmaCityData = DataParser.parseCMACityData("json");
		assertNull(cmaCityData);
	}
	
	public void testparseCMACityDataNull() {
		CMACityData cmaCityData = DataParser.parseCMACityData(null);
		assertNull(cmaCityData);
	}
	
	public void testparseCMACityDataEmpty() {
		CMACityData cmaCityData = DataParser.parseCMACityData("");
		assertNull(cmaCityData);
	}
	
	public void testparseCMACityDataDetail() {
		CMACityData cmaCityData = DataParser.parseCMACityData(json);
		List<CMACityDetail> cmList = cmaCityData.getCmaCitiesData();
		assertNotNull(cmList);
	}
	
	public void testparseCMACityDataDetailCityNameEN() {
		CMACityData cmaCityData = DataParser.parseCMACityData(json);
		List<CMACityDetail> cmList = cmaCityData.getCmaCitiesData();
		String cityNameEN = cmList.get(0).getNameEN();
		assertEquals("Anda", cityNameEN);
	}
	
	public void testparseCMACityDataDetailCityNameCN() {
		CMACityData cmaCityData = DataParser.parseCMACityData(json);
		List<CMACityDetail> cmList = cmaCityData.getCmaCitiesData();
		String cityNameCN = cmList.get(0).getNameCN();
		assertEquals("安达", cityNameCN);
	}
	
	public void testparseCMACityDataDetailCityNameTW() {
		CMACityData cmaCityData = DataParser.parseCMACityData(json);
		List<CMACityDetail> cmList = cmaCityData.getCmaCitiesData();
		String cityNameTW = cmList.get(0).getNameTW();
		assertEquals("安達", cityNameTW);
	}
	
	public void testparseCMACityDataDetailCityAreaID() {
		CMACityData cmaCityData = DataParser.parseCMACityData(json);
		List<CMACityDetail> cmList = cmaCityData.getCmaCitiesData();
		String areaID = cmList.get(0).getAreaID();
		assertEquals("101050503", areaID);
	}
	
	public void testparseCMACityDataDetailCityLat() {
		CMACityData cmaCityData = DataParser.parseCMACityData(json);
		List<CMACityDetail> cmList = cmaCityData.getCmaCitiesData();
		String lat = cmList.get(0).getLatitude();
		assertEquals("46.52", lat);
	}
	
	public void testparseCMACityDataDetailCityLon() {
		CMACityData cmaCityData = DataParser.parseCMACityData(json);
		List<CMACityDetail> cmList = cmaCityData.getCmaCitiesData();
		String lon = cmList.get(0).getLongitude();
		assertEquals("125.393", lon);
	}
	
	public void testCMACityMapProfileTime() {
		CMACityData cmaCityData = DataParser.parseCMACityData(json);
		List<CMACityDetail> cmList = cmaCityData.getCmaCitiesData();
		Map<String, OutdoorCityInfo> cmaMap = new HashMap<String, OutdoorCityInfo>(); 
		for (CMACityDetail cmaCityDetail : cmList) {
			cmaMap.put(cmaCityDetail.getAreaID(), new OutdoorCityInfo(cmaCityDetail.getNameEN(), cmaCityDetail.getNameCN(), cmaCityDetail.getNameTW(), Float.parseFloat(cmaCityDetail.getLongitude()), Float.parseFloat(cmaCityDetail.getLatitude()), cmaCityDetail.getAreaID()));
		}
	}
}
