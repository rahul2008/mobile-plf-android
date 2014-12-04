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
		String data = "{\"p\":[{\"101200509\":{\"p1\":\"66\",\"p2\":\"62\",\"p3\":\"142\",\"p4\":\"5\",\"p5\":\"14\",\"updatetime\":\"201412021206\"}}],\"api_version\":\"4.0\"}";
		String areaId = "101200509";
		
		controller.receiveServerResponse(200, data, areaId);
		
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, times(1)).outdoorAQIDataReceived((OutdoorAQI) any(), eq(areaId));
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testReceivedMultipleCityAQI() {
		String data = "{\"p\":[{\"101270201\":{\"p1\":\"40\",\"p2\":\"71\",\"p3\":\"86\",\"p4\":\"22\",\"p5\":\"33\",\"updatetime\":\"201412021207\"},"
				 + "\"101070301\":{\"p1\":\"47\",\"p2\":\"118\",\"p3\":\"174\",\"p4\":\"102\",\"p5\":\"42\",\"updatetime\":\"201412021206\"},"
				 + "\"101010100\":{\"p1\":\"14\",\"p2\":\"33\",\"p3\":\"29\",\"p4\":\"12\",\"p5\":\"46\",\"updatetime\":\"201412021205\"},"
				 + "\"101200509\":{\"p1\":\"66\",\"p2\":\"62\",\"p3\":\"142\",\"p4\":\"5\",\"p5\":\"14\",\"updatetime\":\"201412021206\"}}],\"api_version\":\"4.0\"}"; 
		
		controller.receiveServerResponse(200, data, "user_cities");
		
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, times(4)).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
}
