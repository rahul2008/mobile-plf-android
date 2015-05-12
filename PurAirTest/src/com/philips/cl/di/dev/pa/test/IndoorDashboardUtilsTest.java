package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.DashboardAPL;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils.FanSpeed;
import com.philips.cl.di.dev.pa.datamodel.AirPortProperties;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dicomm.communication.NullStrategy;
import com.philips.cl.di.dicomm.port.AirPort;

public class IndoorDashboardUtilsTest extends TestCase {
	
	public void testDashboardAPLNegative() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(-1);
		
		assertEquals(0, apl.getPointerRotation());
		assertEquals(R.drawable.blue_circle_with_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.empty_string, apl.getTitle());
		assertEquals(R.string.empty_string, apl.getSummary());
	}
	
	public void testDashboardAPLZero() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(0);
		
		assertEquals(0, apl.getPointerRotation());
		assertEquals(R.drawable.blue_circle_with_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.good_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_good_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLGoodBegin() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(1);
		
		assertEquals(14, apl.getPointerRotation());
		assertEquals(R.drawable.blue_circle_with_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.good_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_good_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLGoodEnd() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(14);
		
		assertEquals(14, apl.getPointerRotation());
		assertEquals(R.drawable.blue_circle_with_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.good_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_good_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLModerateBegin() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(15);
		
		assertEquals(40, apl.getPointerRotation());
		assertEquals(R.drawable.light_pink_circle_arrow1_2x, apl.getPointerBackground());
		assertEquals(R.string.moderate_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_moderate_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLModerateEnd() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(23);
		
		assertEquals(40, apl.getPointerRotation());
		assertEquals(R.drawable.light_pink_circle_arrow1_2x, apl.getPointerBackground());
		assertEquals(R.string.moderate_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_moderate_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLUnhealthyBegin() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(24);
		
		assertEquals(68, apl.getPointerRotation());
		assertEquals(R.drawable.red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.unhealthy_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLUnhealthyEnd() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(35);
		
		assertEquals(68, apl.getPointerRotation());
		assertEquals(R.drawable.red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.unhealthy_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep1Begin() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(36);
		
		assertEquals(105, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep1End() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(45);
		
		assertEquals(105, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep2Begin() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(46);
		
		assertEquals(136, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep2End() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(55);
		
		assertEquals(136, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep3Begin() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(56);
		
		assertEquals(167, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep3End() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(65);
		
		assertEquals(167, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep4Begin() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(66);
		
		assertEquals(198, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep4End() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(75);
		
		assertEquals(198, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep5Begin() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(76);
		
		assertEquals(229, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep5End() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(85);
		
		assertEquals(229, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep6Begin() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(86);
		
		assertEquals(260, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep6End() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(95);
		
		assertEquals(260, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep7Begin() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(96);
		
		assertEquals(291, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryUnhealthyStep7End() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(105);
		
		assertEquals(291, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testDashboardAPLVeryHighAqi() {
		DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(1000);
		
		assertEquals(291, apl.getPointerRotation());
		assertEquals(R.drawable.light_red_circle_arrow_2x, apl.getPointerBackground());
		assertEquals(R.string.very_unhealthy_split_indoor, apl.getTitle());
		assertEquals(R.string.indoor_aqi_very_unhealthy_tip1, apl.getSummary());
	}
	
	public void testFilterStatus1() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"0\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		AirPort airPort = new AirPort(null, new NullStrategy());
		airPort.processResponse(parseData);
		AirPortProperties info = airPort.getPortProperties();

		assertEquals(PurAirApplication.getAppContext().getString(R.string.clean_now), IndoorDashboardUtils.getFilterStatus(info));
	}
	
	public void testFilterStatus2() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"110\",\"fs2\":\"120\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		AirPort airPort = new AirPort(null, new NullStrategy());
		airPort.processResponse(parseData);
		AirPortProperties info = airPort.getPortProperties();

		assertEquals(PurAirApplication.getAppContext().getString(R.string.change_now), IndoorDashboardUtils.getFilterStatus(info));
	}
	
	public void testgetFanSpeedSilent() {
		FanSpeed fanSpeed = IndoorDashboardUtils.getFanSpeed("s");
		assertEquals(R.string.silent, fanSpeed.getFanSpeedTextResID());
	}
	
	public void testgetFanSpeedAuto() {
		FanSpeed fanSpeed = IndoorDashboardUtils.getFanSpeed("a");
		assertEquals(R.string.auto, fanSpeed.getFanSpeedTextResID());
	}
	
	public void testgetFanSpeedTurbo() {
		FanSpeed fanSpeed = IndoorDashboardUtils.getFanSpeed("t");
		assertEquals(R.string.turbo, fanSpeed.getFanSpeedTextResID());
	}
	
	public void testgetFanSpeedOne() {
		FanSpeed fanSpeed = IndoorDashboardUtils.getFanSpeed("1");
		assertEquals(R.string.speed1, fanSpeed.getFanSpeedTextResID());
	}
	
	public void testgetFanSpeedTwo() {
		FanSpeed fanSpeed = IndoorDashboardUtils.getFanSpeed("2");
		assertEquals(R.string.speed2, fanSpeed.getFanSpeedTextResID());
	}
	
	public void testgetFanSpeedThree() {
		FanSpeed fanSpeed = IndoorDashboardUtils.getFanSpeed("3");
		assertEquals(R.string.speed3, fanSpeed.getFanSpeedTextResID());
	}
	
	public void testgetFanSpeedNull() {
		FanSpeed fanSpeed = IndoorDashboardUtils.getFanSpeed(null);
		assertNull(fanSpeed);
	}
	
	public void testgetFanSpeedEmpty() {
		FanSpeed fanSpeed = IndoorDashboardUtils.getFanSpeed(null);
		assertNull(fanSpeed);
	}
	
	public void testgetFanSpeedUnKnowChar() {
		FanSpeed fanSpeed = IndoorDashboardUtils.getFanSpeed("v");
		assertNull(fanSpeed);
	}
}
