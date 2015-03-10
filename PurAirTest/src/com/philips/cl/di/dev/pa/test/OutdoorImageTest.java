package com.philips.cl.di.dev.pa.test;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.OutdoorImage;

import junit.framework.TestCase;

public class OutdoorImageTest extends TestCase {
	
	private String shanghaiAreaId = "101020100";
	private String beijingAreaId = "101010100";
	private String chongqingAreaId = "101040100";
	private String guangzhouAreaId = "101280101";
	private String genericAreaId = "101220601";
	
	private int goodAqi = 35;
	private int moderateAqi = 75;
	private int unhealthySensativeGroupAqi = 125;
	private int unhealthyAqi = 175;
	private int veryUnhealthyAqi = 225;
	private int hazardousAqi = 400;
	
	//Shanghai
	public void testShanghaiGood() {
		int resourceId = OutdoorImage.valueOf(shanghaiAreaId, goodAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_shanghai_50, resourceId);
	}
	
	public void testShanghaiModerate() {
		int resourceId = OutdoorImage.valueOf(shanghaiAreaId, moderateAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_shanghai_100, resourceId);
	}
	
	public void testShanghaiUnhealthySensativeGroup() {
		int resourceId = OutdoorImage.valueOf(shanghaiAreaId, unhealthySensativeGroupAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_shanghai_150, resourceId);
	}
	
	public void testShanghaiUnhealthy() {
		int resourceId = OutdoorImage.valueOf(shanghaiAreaId, unhealthyAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_shanghai_200, resourceId);
	}
	
	public void testShanghaiVeryUnhealthy() {
		int resourceId = OutdoorImage.valueOf(shanghaiAreaId, veryUnhealthyAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_shanghai_300, resourceId);
	}
	
	public void testShanghaiHazardous() {
		int resourceId = OutdoorImage.valueOf(shanghaiAreaId, hazardousAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_shanghai_500, resourceId);
	}
	
	public void testShanghaiAQIMinus() {
		int resourceId = OutdoorImage.valueOf(shanghaiAreaId, -10);
		assertEquals(R.drawable.air_dashboard_outdoor_shanghai_50, resourceId);
	}
	
	//Beijing
	public void testBeijingGood() {
		int resourceId = OutdoorImage.valueOf(beijingAreaId, goodAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_beijing_50, resourceId);
	}
	
	public void testBeijingModerate() {
		int resourceId = OutdoorImage.valueOf(beijingAreaId, moderateAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_beijing_100, resourceId);
	}
	
	public void testBeijingUnhealthySensativeGroup() {
		int resourceId = OutdoorImage.valueOf(beijingAreaId, unhealthySensativeGroupAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_beijing_150, resourceId);
	}
	
	public void testBeijingUnhealthy() {
		int resourceId = OutdoorImage.valueOf(beijingAreaId, unhealthyAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_beijing_200, resourceId);
	}
	
	public void testBeijingVeryUnhealthy() {
		int resourceId = OutdoorImage.valueOf(beijingAreaId, veryUnhealthyAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_beijing_300, resourceId);
	}
	
	public void testBeijingHazardous() {
		int resourceId = OutdoorImage.valueOf(beijingAreaId, hazardousAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_beijing_500, resourceId);
	}
	
	public void testBeijingAQIMinus() {
		int resourceId = OutdoorImage.valueOf(beijingAreaId, -20);
		assertEquals(R.drawable.air_dashboard_outdoor_beijing_50, resourceId);
	}
	
	//Guangzhou
	public void testGuangzhouGood() {
		int resourceId = OutdoorImage.valueOf(guangzhouAreaId, goodAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_guangzhou_50, resourceId);
	}
	
	public void testGuangzhouModerate() {
		int resourceId = OutdoorImage.valueOf(guangzhouAreaId, moderateAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_guangzhou_100, resourceId);
	}
	
	public void testGuangzhouUnhealthySensativeGroup() {
		int resourceId = OutdoorImage.valueOf(guangzhouAreaId, unhealthySensativeGroupAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_guangzhou_150, resourceId);
	}
	
	public void testGuangzhouUnhealthy() {
		int resourceId = OutdoorImage.valueOf(guangzhouAreaId, unhealthyAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_guangzhou_200, resourceId);
	}
	
	public void testGuangzhouVeryUnhealthy() {
		int resourceId = OutdoorImage.valueOf(guangzhouAreaId, veryUnhealthyAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_guangzhou_300, resourceId);
	}
	
	public void testGuangzhouHazardous() {
		int resourceId = OutdoorImage.valueOf(guangzhouAreaId, hazardousAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_guangzhou_500, resourceId);
	}
	
	public void testGuangzhouAQIMinus() {
		int resourceId = OutdoorImage.valueOf(guangzhouAreaId, -20);
		assertEquals(R.drawable.air_dashboard_outdoor_guangzhou_50, resourceId);
	}
	
	//Chongqing
	public void testChongqingGood() {
		int resourceId = OutdoorImage.valueOf(chongqingAreaId, goodAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_chongqing_50, resourceId);
	}
	
	public void testChongqingModerate() {
		int resourceId = OutdoorImage.valueOf(chongqingAreaId, moderateAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_chongqing_100, resourceId);
	}
	
	public void testChongqingUnhealthySensativeGroup() {
		int resourceId = OutdoorImage.valueOf(chongqingAreaId, unhealthySensativeGroupAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_chongqing_150, resourceId);
	}
	
	public void testChongqingUnhealthy() {
		int resourceId = OutdoorImage.valueOf(chongqingAreaId, unhealthyAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_chongqing_200, resourceId);
	}
	
	public void testChongqingVeryUnhealthy() {
		int resourceId = OutdoorImage.valueOf(chongqingAreaId, veryUnhealthyAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_chongqing_300, resourceId);
	}
	
	public void testChongqingHazardous() {
		int resourceId = OutdoorImage.valueOf(chongqingAreaId, hazardousAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_chongqing_500, resourceId);
	}
	
	public void testChongqingAQIminus() {
		int resourceId = OutdoorImage.valueOf(chongqingAreaId, -30);
		assertEquals(R.drawable.air_dashboard_outdoor_chongqing_50, resourceId);
	}
	
	//Generic
	public void testGenericGood() {
		int resourceId = OutdoorImage.valueOf(genericAreaId, goodAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_generic_city_50, resourceId);
	}
	
	public void testGenericModerate() {
		int resourceId = OutdoorImage.valueOf(genericAreaId, moderateAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_generic_city_100, resourceId);
	}
	
	public void testGenericUnhealthySensativeGroup() {
		int resourceId = OutdoorImage.valueOf(genericAreaId, unhealthySensativeGroupAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_generic_city_150, resourceId);
	}
	
	public void testGenericUnhealthy() {
		int resourceId = OutdoorImage.valueOf(genericAreaId, unhealthyAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_generic_city_200, resourceId);
	}
	
	public void testGenericVeryUnhealthy() {
		int resourceId = OutdoorImage.valueOf(genericAreaId, veryUnhealthyAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_generic_city_300, resourceId);
	}
	
	public void testGenericHazardous() {
		int resourceId = OutdoorImage.valueOf(genericAreaId, hazardousAqi);
		assertEquals(R.drawable.air_dashboard_outdoor_generic_city_500, resourceId);
	}
	
	public void testGenericAQIMinus() {
		int resourceId = OutdoorImage.valueOf(genericAreaId, -10);
		assertEquals(R.drawable.air_dashboard_outdoor_generic_city_50, resourceId);
	}

}
