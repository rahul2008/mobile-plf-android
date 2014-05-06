package com.philips.cl.di.dev.pa.test;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.OutdoorDto;

import junit.framework.TestCase;

public class OutdoorDtoTest extends TestCase {
	
	public void testGetAqiTitleCaseNull() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi(null);
		assertEquals("", outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCaseEmpty() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("");
		assertEquals("", outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCaseNegative1() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("-1");
		assertEquals("", outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCaseNegative2() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("hello");
		assertEquals("", outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase1() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("25");
		assertEquals(PurAirApplication.getAppContext().getString(R.string.good), outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase2() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("75");
		assertEquals(PurAirApplication.getAppContext().getString(R.string.moderate), outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase3() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("125");
		assertEquals(PurAirApplication.getAppContext().getString(R.string.healthy), outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase4() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("175");
		assertEquals(PurAirApplication.getAppContext().getString(R.string.unhealthy), outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase5() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("225");
		assertEquals(PurAirApplication.getAppContext().getString(R.string.very_unhealthy_split), outdoorDto.getAqiTitle());
	}
	
	public void testGetAqiTitleCase6() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("400");
		assertEquals(PurAirApplication.getAppContext().getString(R.string.hazardous), outdoorDto.getAqiTitle());
	}
	/**
	 * GetAqiSummary
	 */
	public void testGetAqiSummaryCaseNull() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi(null);
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertNull(aqiSummaries[0]);
		assertNull(aqiSummaries[1]);
	}
	
	public void testGetAqiSummaryCaseEmpty() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("");
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertNull(aqiSummaries[0]);
		assertNull(aqiSummaries[1]);
	}
	
	public void testGetAqiSummaryCaseNegative1() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("-1");
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertNull(aqiSummaries[0]);
		assertNull(aqiSummaries[1]);
	}
	
	public void testGetAqiSummaryCaseNegative2() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("hello");
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertNull(aqiSummaries[0]);
		assertNull(aqiSummaries[1]);	
	}
	
	public void testGetAqiSummaryCase1() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("25");
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_good_tip1), aqiSummaries[0]);
	}
	
	public void testGetAqiSummaryCase2() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("75");
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_moderate_tip1), aqiSummaries[0]);
	}
	
	public void testGetAqiSummaryCase3() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("125");
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_unhealthy_sensitive_group_tip1), aqiSummaries[0]);
	}
	
	public void testGetAqiSummaryCase4() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("175");
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_unhealthy_tip1), aqiSummaries[0]);
	}
	
	public void testGetAqiSummaryCase5() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("225");
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_very_unhealthy_tip1), aqiSummaries[0]);
	}
	
	public void testGetAqiSummaryCase6() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("400");
		String[] aqiSummaries = outdoorDto.getAqiSummary();
		assertEquals(PurAirApplication.getAppContext().getString(
				R.string.outdoor_aqi_hazardous_tip1), aqiSummaries[0]);
	}

	/**
	 * getAqiPointerRotaion
	 */
	public void testGetAqiPointerRotaionCaseNull() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi(null);
		float fdegree = outdoorDto.getAqiPointerRotaion();
		assertEquals(0.0f, fdegree, 0.00f);
	}
	
	public void testGetAqiPointerRotaionCaseEmpty() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("");
		float fdegree = outdoorDto.getAqiPointerRotaion();
		assertEquals(0.0f, fdegree, 0.00f);
	}
	
	public void testGetAqiPointerRotaionCaseNegative1() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("-1");
		float fdegree = outdoorDto.getAqiPointerRotaion();
		assertEquals(0.0f, fdegree, 0.00f);
	}
	
	public void testGetAqiPointerRotaionCaseNegative2() {
		OutdoorDto outdoorDto = new OutdoorDto();
		outdoorDto.setAqi("hello");
		float fdegree = outdoorDto.getAqiPointerRotaion();
		assertEquals(0.0f, fdegree, 0.00f);
	}
}
