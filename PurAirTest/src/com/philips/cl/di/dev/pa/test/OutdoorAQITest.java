package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;

public class OutdoorAQITest extends TestCase{
	
	public void testGetAqiTitleCaseNegative1() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, -1, null);
		assertEquals("", outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase1() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 25, null);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.good), outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase2() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 75, null);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.moderate), outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase3() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 125, null);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.unhealthy_for_sensitive_groups), outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase4() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 175, null);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.unhealthy), outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase5() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 225, null);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.very_unhealthy_split), outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase6() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 400, null);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.hazardous), outdoorDto.getAqiTitle());
	}
	/**
	 * GetAqiSummary
	 */
	public void testGetAqiSummaryCaseNegative1() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, -1, null);
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertNull(aqiSummaries[0]);
		assertNull(aqiSummaries[1]);
	}
	
	public void testGetAqiSummaryCase1() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 25, null);
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_good_tip1), aqiSummaries[0]);
	}
	
	public void testGetAqiSummaryCase2() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 75, null);
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_moderate_tip1), aqiSummaries[0]);
	}
	
	public void testGetAqiSummaryCase3() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 125, null);
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_unhealthy_sensitive_group_tip1), aqiSummaries[0]);
	}
	
	public void testGetAqiSummaryCase4() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 175, null);
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_unhealthy_tip1), aqiSummaries[0]);
	}
	
	public void testGetAqiSummaryCase5() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 225, null);
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_very_unhealthy_tip1), aqiSummaries[0]);
	}
	
	public void testGetAqiSummaryCase6() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 400, null);
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_hazardous_tip1), aqiSummaries[0]);
	}

	/**
	 * getAqiPointerRotaion
	 */
	public void testGetAqiPointerRotaionCaseNull() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 0, null);
		float fdegree = outdoorDto.getAqiPointerRotaion();
		assertEquals(0.0f, fdegree, 0.00f);
	}
	
	public void testGetAqiPointerRotaionCaseEmpty() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 0, null);
		float fdegree = outdoorDto.getAqiPointerRotaion();
		assertEquals(0.0f, fdegree, 0.00f);
	}
	
	public void testGetAqiPointerRotaionCaseNegative1() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, -1, null);
		float fdegree = outdoorDto.getAqiPointerRotaion();
		assertEquals(0.0f, fdegree, 0.00f);
	}
	
	public void testGetAqiPointerRotaionCaseNegative2() {
		OutdoorAQI outdoorDto = new OutdoorAQI(10, 0, null);
		float fdegree = outdoorDto.getAqiPointerRotaion();
		assertEquals(0.0f, fdegree, 0.00f);
	}

}
