package com.philips.cl.di.dev.pa.test;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.DashboardUtils;

import junit.framework.TestCase;

public class DashboardUtilsTest extends TestCase {
	
	public void testGetFanSpeedTextNull() {
		String fanSpeed = DashboardUtils.getFanSpeedText(null);
		assertEquals("", fanSpeed);
	}
	
	public void testGetFanSpeedTextEmpty() {
		String fanSpeed = DashboardUtils.getFanSpeedText("");
		assertEquals("", fanSpeed);
	}
	
	public void testGetFanSpeedTextNegative() {
		String fanSpeed = DashboardUtils.getFanSpeedText("hello");
		assertEquals("", fanSpeed);
	}
	
	public void testGetFanSpeedTextCase1() {
		String fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_SILENT);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.silent), fanSpeed);
	}
	
	public void testGetFanSpeedTextCase2() {
		String fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_AUTO);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.auto), fanSpeed);
	}
	
	public void testGetFanSpeedTextCase3() {
		String fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_TURBO);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.turbo), fanSpeed);
	}
	
	public void testGetFanSpeedTextCase4() {
		String fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_ONE);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.one), fanSpeed);
	}
	
	public void testGetFanSpeedTextCase5() {
		String fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_TWO);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.two), fanSpeed);
	}
	
	public void testGetFanSpeedTextCase6() {
		String fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_THREE);
		assertEquals(PurAirApplication.getAppContext().getString(R.string.three), fanSpeed);
	}
	
	public void testGetAqiPointerBackgroundIdZero() {
		int resourceId = DashboardUtils.getAqiPointerBackgroundId(0);
		assertEquals(R.drawable.blue_circle_with_arrow_2x, resourceId);
	}
	
	public void testGetAqiPointerBackgroundIdNegative() {
		int resourceId = DashboardUtils.getAqiPointerBackgroundId(-1);
		assertEquals(R.drawable.blue_circle_with_arrow_2x, resourceId);
	}
	
	public void testGetAqiPointerBackgroundIdCase1() {
		int resourceId = DashboardUtils.getAqiPointerBackgroundId(5);
		assertEquals(R.drawable.blue_circle_with_arrow_2x, resourceId);
	}
	
	public void testGetAqiPointerBackgroundIdCase2() {
		int resourceId = DashboardUtils.getAqiPointerBackgroundId(15);
		assertEquals(R.drawable.light_pink_circle_arrow1_2x, resourceId);
	}
	
	public void testGetAqiPointerBackgroundIdCase3() {
		int resourceId = DashboardUtils.getAqiPointerBackgroundId(25);
		assertEquals(R.drawable.red_circle_arrow_2x, resourceId);
	}
	
	public void testGetAqiPointerBackgroundIdCase4() {
		int resourceId = DashboardUtils.getAqiPointerBackgroundId(40);
		assertEquals(R.drawable.light_red_circle_arrow_2x, resourceId);
	}

}
