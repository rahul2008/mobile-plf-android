package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.dashboard.OutdoorEventListener;
import com.philips.cl.di.dev.pa.dashboard.OutdoorWeather;
import com.philips.cl.di.dev.pa.util.OutdoorDetailsListener;

@SuppressWarnings("unchecked")
public class OutdoorControllerTest extends InstrumentationTestCase{
	
	private OutdoorController controller;
	private OutdoorDetailsListener detailsListener;
	private OutdoorEventListener eventListener;
	

	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		OutdoorController.setDummyOutdoorControllerForTesting(null);
		controller = OutdoorController.getInstance();
		detailsListener = mock(OutdoorDetailsListener.class);
		eventListener = mock(OutdoorEventListener.class);

		controller.setOutdoorDetailsListener(detailsListener);
		controller.setOutdoorEventListener(eventListener);
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		OutdoorController.setDummyOutdoorControllerForTesting(null);
		super.tearDown();
	}
	
	public void testRecievedAllCityData() {
		controller.receiveServerResponse(200, "Data", "all_cities");
		verify(eventListener, times(1)).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, never()).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testRecievedAllCityData2() {
		controller.receiveServerResponse(200, "", "all_cities");
		verify(eventListener, times(1)).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, never()).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testReceivedCityAQI1() {
		String data = "{\"p\":[{\"101270101\":{\"p1\":\"205\",\"p2\":\"255\",\"p3\":\"403\",\"p4\":\"4\",\"p5\":\"25\",\"updatetime\":\"201407311706\"}}],\"api_version\":\"4.0\"}";
		String areaId = "101270101";
		
		controller.receiveServerResponse(200, data, areaId);
		
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, times(1)).outdoorAQIDataReceived((OutdoorAQI) any(), eq(areaId));
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
}
