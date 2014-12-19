package com.philips.cl.di.dev.pa.outdoorlocations.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorDataListener;
import com.philips.cl.di.dev.pa.dashboard.OutdoorWeather;
import com.philips.cl.di.dev.pa.outdoorlocations.CMACommunicator;

import android.test.InstrumentationTestCase;

@SuppressWarnings("unchecked")
public class CMACommunicatorTest extends InstrumentationTestCase {

	private OutdoorDataListener eventListener;
	private CMACommunicator cmaCommunicator;

	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		eventListener = mock(OutdoorDataListener.class);
		cmaCommunicator = new CMACommunicator();
		
		cmaCommunicator.setResponseListener(eventListener);
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		cmaCommunicator = null;
		eventListener = null;
		super.tearDown();
	}
	
	public void testNullDataReceived() {
		cmaCommunicator.receiveServerResponse(200, null, "CITIES_AQI");
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, never()).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testNullTypeReceived() {
		String data = "{\"p\":[{\"101200509\":{\"p1\":\"66\",\"p2\":\"62\",\"p3\":\"142\",\"p4\":\"5\",\"p5\":\"14\",\"updatetime\":\"201412021206\"}}],\"api_version\":\"4.0\"}";
		cmaCommunicator.receiveServerResponse(200, data, null);
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, never()).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testNoTypeReceived() {
		String data = "{\"p\":[{\"101200509\":{\"p1\":\"66\",\"p2\":\"62\",\"p3\":\"142\",\"p4\":\"5\",\"p5\":\"14\",\"updatetime\":\"201412021206\"}}],\"api_version\":\"4.0\"}";
		cmaCommunicator.receiveServerResponse(200, data, "");
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, never()).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testNoDataReceived() {
		cmaCommunicator.receiveServerResponse(200, "", "CITIES_AQI");
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, never()).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testInvalidAQIDataReceived() {
		cmaCommunicator.receiveServerResponse(200, "Data", "CITIES_AQI");
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, never()).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testAQIDataReceived() {
		String data = "{\"p\":[{\"101200509\":{\"p1\":\"66\",\"p2\":\"62\",\"p3\":\"142\",\"p4\":\"5\",\"p5\":\"14\",\"updatetime\":\"201412021206\"}}],\"api_version\":\"4.0\"}";
		cmaCommunicator.receiveServerResponse(200, data, "CITIES_AQI");
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, times(1)).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testMultipleCityAQIDataReceived() {
		String data = "{\"p\":[{\"101270201\":{\"p1\":\"40\",\"p2\":\"71\",\"p3\":\"86\",\"p4\":\"22\",\"p5\":\"33\",\"updatetime\":\"201412021207\"},"
				 + "\"101070301\":{\"p1\":\"47\",\"p2\":\"118\",\"p3\":\"174\",\"p4\":\"102\",\"p5\":\"42\",\"updatetime\":\"201412021206\"},"
				 + "\"101010100\":{\"p1\":\"14\",\"p2\":\"33\",\"p3\":\"29\",\"p4\":\"12\",\"p5\":\"46\",\"updatetime\":\"201412021205\"},"
				 + "\"101200509\":{\"p1\":\"66\",\"p2\":\"62\",\"p3\":\"142\",\"p4\":\"5\",\"p5\":\"14\",\"updatetime\":\"201412021206\"}}],\"api_version\":\"4.0\"}"; 
				
		cmaCommunicator.receiveServerResponse(200, data, "CITIES_AQI");
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, times(4)).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testInvalidWeatherDataReceived() {
		cmaCommunicator.receiveServerResponse(200, "Data", "CITIES_WEATHER");
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, never()).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, never()).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testCityWeatherDataReceived() {
		String data = "{\"observe\":{\"101010100\":{\"l\":{\"l1\":\"-1\",\"l2\":\"28\",\"l3\":\"1\",\"l4\":\"5\",\"l5\":\"00\",\"l7\":\"20:40\"}}},\"api_version\":\"4.0\"}";
		cmaCommunicator.receiveServerResponse(200, data, "CITIES_WEATHER");
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, never()).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, times(1)).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
	
	public void testMultipleCityWeatherDataReceived() {
		String data = "{\"observe\":"
				+ "{\"101010100\":{\"l\":{\"l1\":\"-1\",\"l2\":\"28\",\"l3\":\"1\",\"l4\":\"5\",\"l5\":\"00\",\"l7\":\"20:40\"}},"
				+ "\"101270201\":{\"l\":{\"l1\":\"13\",\"l2\":\"46\",\"l3\":\"1\",\"l4\":\"2\",\"l5\":\"00\",\"l7\":\"20:50\"}}},\"api_version\":\"4.0\"}";
		cmaCommunicator.receiveServerResponse(200, data, "CITIES_WEATHER");
		verify(eventListener, never()).allOutdoorAQIDataReceived(anyList());
		verify(eventListener, never()).outdoorAQIDataReceived((OutdoorAQI) any(), anyString());
		verify(eventListener, times(2)).outdoorWeatherDataReceived((OutdoorWeather) any(), anyString());
	}
}
