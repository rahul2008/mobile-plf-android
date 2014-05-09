package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils;

public class IndoorDashboardUtilsTest extends TestCase {
	
	public void testGetFanSpeedTextNull() {
		int fanSpeed = IndoorDashboardUtils.getFanSpeedText(null);
		assertEquals(fanSpeed, R.string.empty_string);
	}
	
	public void testGetFanSpeedTextEmpty() {
		int fanSpeed = IndoorDashboardUtils.getFanSpeedText("");
		assertEquals(fanSpeed, R.string.empty_string);
	}
	
	public void testGetFanSpeedTextNegative() {
		int fanSpeed = IndoorDashboardUtils.getFanSpeedText("hello");
		assertEquals(fanSpeed, R.string.empty_string);
	}
	
	public void testGetFanSpeedTextCase1() {
		int fanSpeed = IndoorDashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_SILENT);
		assertEquals(R.string.silent, fanSpeed);
	}
	
	public void testGetFanSpeedTextCase2() {
		int fanSpeed = IndoorDashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_AUTO);
		assertEquals(R.string.auto, fanSpeed);
	}
	
	public void testGetFanSpeedTextCase3() {
		int fanSpeed = IndoorDashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_TURBO);
		assertEquals(R.string.turbo, fanSpeed);
	}
	
	public void testGetFanSpeedTextCase4() {
		int fanSpeed = IndoorDashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_ONE);
		assertEquals(R.string.one, fanSpeed);
	}
	
	public void testGetFanSpeedTextCase5() {
		int fanSpeed = IndoorDashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_TWO);
		assertEquals(R.string.two, fanSpeed);
	}
	
	public void testGetFanSpeedTextCase6() {
		int fanSpeed = IndoorDashboardUtils.getFanSpeedText(AppConstants.FAN_SPEED_THREE);
		assertEquals(R.string.three, fanSpeed);
	}
	
	public void testGetAqiPointerBackgroundIdZero() {
		int resourceId = IndoorDashboardUtils.getAqiPointerBackgroundId(0);
		assertEquals(R.drawable.blue_circle_with_arrow_2x, resourceId);
	}
	
	public void testGetAqiPointerBackgroundIdNegative() {
		int resourceId = IndoorDashboardUtils.getAqiPointerBackgroundId(-1);
		assertEquals(R.drawable.blue_circle_with_arrow_2x, resourceId);
	}
	
	public void testGetAqiPointerBackgroundIdCase1() {
		int resourceId = IndoorDashboardUtils.getAqiPointerBackgroundId(5);
		assertEquals(R.drawable.blue_circle_with_arrow_2x, resourceId);
	}
	
	public void testGetAqiPointerBackgroundIdCase2() {
		int resourceId = IndoorDashboardUtils.getAqiPointerBackgroundId(15);
		assertEquals(R.drawable.light_pink_circle_arrow1_2x, resourceId);
	}
	
	public void testGetAqiPointerBackgroundIdCase3() {
		int resourceId = IndoorDashboardUtils.getAqiPointerBackgroundId(25);
		assertEquals(R.drawable.red_circle_arrow_2x, resourceId);
	}
	
	public void testGetAqiPointerBackgroundIdCase4() {
		int resourceId = IndoorDashboardUtils.getAqiPointerBackgroundId(40);
		assertEquals(R.drawable.light_red_circle_arrow_2x, resourceId);
	}
	
	public void testGetAqiPointerRotationNegative() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(-1);
		assertEquals(0.0f, rotation);
	}
	
	public void testGetAqiPointerRotationZero() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(0);
		assertEquals(0.0f, rotation);
	}
	
	public void testGetAqiPointerRotationBig() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(1000);
		assertEquals(302.0f, rotation);
	}
	
	public void testGetAqiPointerRotationGood() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(10);
		assertTrue((rotation < 27.0f));
	}
	
	public void testGetAqiPointerRotationModerateBegin() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(15);
		assertTrue((rotation > 27.0f));
		assertTrue((rotation < 57.0f));
	}
	
	public void testGetAqiPointerRotationModerate() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(20);
		assertTrue((rotation > 27.0f));
		assertTrue((rotation < 57.0f));
	}
	
	public void testGetAqiPointerRotationModerateEnd() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(23);
		assertTrue((rotation > 27.0f));
		assertTrue((rotation < 57.0f));
	}
	
	public void testGetAqiPointerRotationUnhealthyBegin() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(24);
		assertTrue((rotation > 57.0f));
		assertTrue((rotation < 84.0f));
	}
	
	public void testGetAqiPointerRotationUnhealthy() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(30);
		assertTrue((rotation > 57.0f));
		assertTrue((rotation < 84.0f));
	}
	
	public void testGetAqiPointerRotationUnhealthyEnd() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(35);
		assertTrue((rotation > 57.0f));
		assertTrue((rotation < 84.0f));
	}
	
	public void testGetAqiPointerRotationVeryUnhealthyBegin() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(36);
		assertTrue((rotation > 84.0f));
		assertTrue((rotation < 302.0f));
	}
	
	public void testGetAqiPointerRotationVeryUnhealthy() {
		float rotation = IndoorDashboardUtils.getAqiPointerRotation(40);
		assertTrue((rotation > 84.0f));
		assertTrue((rotation < 302.0f));
	}
}
