package com.philips.cl.di.dev.pa.test;

import java.util.Calendar;

import junit.framework.TestCase;
import android.graphics.drawable.Drawable;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.datamodel.IndoorTrendDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.util.Utils;

public class UtilTest extends TestCase {
	
	public void testGetPurifierAirPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/air";
		String actualUrl = Utils.getPortUrl(Port.AIR, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierFirmwarePortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/0/firmware";
		String actualUrl = Utils.getPortUrl(Port.FIRMWARE, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierWifiPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/0/wifi";
		String actualUrl = Utils.getPortUrl(Port.WIFI, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierWifiuiPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/wifiui";
		String actualUrl = Utils.getPortUrl(Port.WIFIUI, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierDevicePortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/device";
		String actualUrl = Utils.getPortUrl(Port.DEVICE, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierLogPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/0/log";
		String actualUrl = Utils.getPortUrl(Port.LOG, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierPairingPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/0/pairing";
		String actualUrl = Utils.getPortUrl(Port.PAIRING, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierSecurityPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/0/security";
		String actualUrl = Utils.getPortUrl(Port.SECURITY, ipAddress);
		
		assertEquals(expectedUrl, actualUrl);
	}
	
	public void testGetPurifierWrongPortUrl() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + ipAddress + "/di/v1/products/1/air";
		String actualUrl = Utils.getPortUrl(Port.PAIRING, ipAddress);
		
		assertFalse(expectedUrl.equals(actualUrl));
	}
	
	public void testGetPurifierAirPortUrlWrongIp() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + "178.234.3.53" + "/di/v1/products/1/air";
		String actualUrl = Utils.getPortUrl(Port.AIR, ipAddress);
		
		assertFalse(expectedUrl.equals(actualUrl));
	}
	
	public void testGetPurifierAirPortUrlNullIp() {
		String ipAddress = null;
		String expectedUrl = "http://" + null + "/di/v1/products/1/air";
		String actualUrl = Utils.getPortUrl(Port.AIR, ipAddress);
		
		assertTrue(expectedUrl.equals(actualUrl));
	}
	
	public void testGetPurifierAirPortUrlNullPort() {
		String ipAddress = "192.168.1.23";
		String expectedUrl = "http://" + "192.168.1.23" + "/di/v1/products/1/invalidport";
		String actualUrl = Utils.getPortUrl(null, ipAddress);
		
		assertTrue(expectedUrl.equals(actualUrl));
	}
	
	public void testGetLastDayHoursNull() {
		int hr = Utils.getLastDayHours(null);
		assertEquals(0, hr);
	}
	
	public void testGetLastDayHoursEmpty() {
		int hr = Utils.getLastDayHours("");
		assertEquals(0, hr);
	}
	
	public void testGetLastDayHoursWrongFormat() {
		int hr = Utils.getLastDayHours("hello");
		assertEquals(0, hr);
	}
	
	public void testGetDiffInDays() {
		long rl = Utils.getDiffInDays(0L);
		assertTrue(rl != 0);
	}
	
	public void testGetPercentageBothZero() {
		int percent = Utils.getPercentage(0, 0);
		assertEquals(0, percent);
	}
	
	public void testGetPercentageFirstZero() {
		int percent = Utils.getPercentage(0, 4);
		assertEquals(0, percent);
	}
	
	public void testGetPercentageLastZero() {
		int percent = Utils.getPercentage(4, 0);
		assertEquals(0, percent);
	}
	
	public void testGetDayOfWeekNine() {
		String day = Utils.getDayOfWeek(PurAirApplication.getAppContext(), 9);
		assertNull(day);
	}
	
	public void testGetDayOfWeekZero() {
		String day = Utils.getDayOfWeek(PurAirApplication.getAppContext(), 0);
		assertNull(day);
	}
	
	public void testGetDayOfWeekOne() {
		String day = Utils.getDayOfWeek(PurAirApplication.getAppContext(), 1);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.sun), day);
	}
	
	public void testGetDayOfWeekTwo() {
		String day = Utils.getDayOfWeek(PurAirApplication.getAppContext(), 2);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.mon), day);
	}
	
	public void testGetDayOfWeekThree() {
		String day = Utils.getDayOfWeek(PurAirApplication.getAppContext(), 3);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.tue), day);
	}
	
	public void testGetDayOfWeekFour() {
		String day = Utils.getDayOfWeek(PurAirApplication.getAppContext(), 4);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.wed), day);
	}
	
	public void testGetDayOfWeekFive() {
		String day = Utils.getDayOfWeek(PurAirApplication.getAppContext(), 5);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.thu), day);
	}
	
	public void testGetDayOfWeekSix() {
		String day = Utils.getDayOfWeek(PurAirApplication.getAppContext(), 6);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.fri), day);
	}
	
	public void testGetDayOfWeekSeven() {
		String day = Utils.getDayOfWeek(PurAirApplication.getAppContext(), 7);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.sat), day);
	}
	
	public void testGetAQIStatusAndSummaryNegativeCase() {
		String[] statusSummaries = Utils.getAQIStatusAndSummary(-1);
		assertNull(statusSummaries[0]);
		assertNull(statusSummaries[1]);
	}
	
	public void testGetAQIStatusAndSummaryCase1() {
		String[] statusSummaries = Utils.getAQIStatusAndSummary(5);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.good), statusSummaries[0]);
	}
	
	public void testGetAQIStatusAndSummaryCase2() {
		String[] statusSummaries = Utils.getAQIStatusAndSummary(15);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.moderate), statusSummaries[0]);
	}
	
	public void testGetAQIStatusAndSummaryCase3() {
		String[] statusSummaries = Utils.getAQIStatusAndSummary(25);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.unhealthy), statusSummaries[0]);
	}
	
	public void testGetAQIStatusAndSummaryCase4() {
		String[] statusSummaries = Utils.getAQIStatusAndSummary(40);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.very_unhealthy_split), statusSummaries[0]);
	}
	
	public void testGetOutdoorTemperatureImageNull() {
		Drawable weatherDrawable = 
				Utils.getOutdoorTemperatureImage(PurAirApplication.getAppContext(), null, "yes");
		assertNull(weatherDrawable);
	}
	
	public void testGetOutdoorTemperatureImageEmpty() {
		Drawable weatherDrawable = 
				Utils.getOutdoorTemperatureImage(PurAirApplication.getAppContext(), "", "yes");
		assertNull(weatherDrawable);
	}
	
	public void testGetOutdoorTemperatureImageNullDay() {
		Drawable weatherDrawable = 
				Utils.getOutdoorTemperatureImage(PurAirApplication.getAppContext(), "Partly Cloudy", null);
		assertNotNull(weatherDrawable);
	}
	
	public void testGetOutdoorTemperatureImageEmptyDay() {
		Drawable weatherDrawable = 
				Utils.getOutdoorTemperatureImage(PurAirApplication.getAppContext(), "Partly Cloudy", "");
		assertNotNull(weatherDrawable);
	}

	public void testGetTimeDiffInMinite() {
		long timeCurr = Calendar.getInstance().getTimeInMillis() - 3*60*1000;
		IndoorTrendDto indoorTrendDto = SessionDto.getInstance().getIndoorTrendDto();
		if (indoorTrendDto != null) {
			indoorTrendDto.setTimeMin(timeCurr);
			int min = Utils.getTimeDiffInMinite();
			assertTrue(min >= 3);
		} else {
			assertNull(indoorTrendDto);
		}
	}
	
	public void testGetTimeDiffInMinitePlus() {
		long timeCurr = Calendar.getInstance().getTimeInMillis() + 3*60*1000;
		IndoorTrendDto indoorTrendDto = SessionDto.getInstance().getIndoorTrendDto();
		if (indoorTrendDto != null) {
			indoorTrendDto.setTimeMin(timeCurr);
			int min = Utils.getTimeDiffInMinite();
			assertTrue(min < 0);
		} else {
			assertNull(indoorTrendDto);
		}
	}
		
}
