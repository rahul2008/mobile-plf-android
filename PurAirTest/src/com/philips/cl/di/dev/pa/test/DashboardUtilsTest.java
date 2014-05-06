package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.DashboardUtils;

public class DashboardUtilsTest extends TestCase {
	
	public void testGetFanSpeedTextNull() {
		int fanSpeed = DashboardUtils.getFanSpeedText(null);
		assertEquals(fanSpeed, R.string.empty_string);
	}
	
	public void testGetFanSpeedTextEmpty() {
		int fanSpeed = DashboardUtils.getFanSpeedText("");
		assertEquals(fanSpeed, R.string.empty_string);
	}
	
	public void testGetFanSpeedTextNegative() {
		int fanSpeed = DashboardUtils.getFanSpeedText("hello");
		assertEquals(fanSpeed, R.string.empty_string);
	}
	
	public void testGetFanSpeedTextCase1() {
		int fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_SILENT);
		assertEquals(R.string.silent, fanSpeed);
	}
	
	public void testGetFanSpeedTextCase2() {
		int fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_AUTO);
		assertEquals(R.string.auto, fanSpeed);
	}
	
	public void testGetFanSpeedTextCase3() {
		int fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_TURBO);
		assertEquals(R.string.turbo, fanSpeed);
	}
	
	public void testGetFanSpeedTextCase4() {
		int fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_ONE);
		assertEquals(R.string.one, fanSpeed);
	}
	
	public void testGetFanSpeedTextCase5() {
		int fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_TWO);
		assertEquals(R.string.two, fanSpeed);
	}
	
	public void testGetFanSpeedTextCase6() {
		int fanSpeed = DashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_THREE);
		assertEquals(R.string.three, fanSpeed);
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
