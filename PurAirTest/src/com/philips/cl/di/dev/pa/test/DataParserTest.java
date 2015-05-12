package com.philips.cl.di.dev.pa.test;
import java.util.List;

import junit.framework.TestCase;

import com.philips.cl.di.common.ssdp.controller.BaseUrlParser;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.datamodel.DevicePortProperties;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.datamodel.WifiPortProperties;
import com.philips.cl.di.dev.pa.util.DataParser;

public class DataParserTest extends TestCase {

	public void testIndoorAQIHistory_1() {
		String parseData = "No data" ;
		assertNull(DataParser.parseHistoryData(parseData)) ;
	}

	public void testWeatherData_1() {
		String parseData = "No data" ;
		assertNull(DataParser.parseWeatherData(parseData)) ;
	}

	public void testSSDPDeviceDetailListNotNull() {
		final String xmlDescription = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>living room cl</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6341fe</UDN><cppId>1c5a6bfffe6341fe</cppId></device></root>";
		BaseUrlParser mBaseParser = new BaseUrlParser();
		mBaseParser.parse(xmlDescription);
		final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
		assertNotNull(deviceList);
	}

	public void testSSDPDeviceDetailListNotZero() {
		final String xmlDescription = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>living room cl</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6341fe</UDN><cppId>1c5a6bfffe6341fe</cppId></device></root>";
		BaseUrlParser mBaseParser = new BaseUrlParser();
		mBaseParser.parse(xmlDescription);
		final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
		assertTrue(deviceList.size() > 0);
	}

	public void testSSDPDeviceType() {
		final String xmlDescription = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>living room cl</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6341fe</UDN><cppId>1c5a6bfffe6341fe</cppId></device></root>";
		BaseUrlParser mBaseParser = new BaseUrlParser();
		mBaseParser.parse(xmlDescription);
		final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
		assertEquals("urn:philips-com:device:DiProduct:1", deviceList.get(0).getDeviceType());
	}

	public void testSSDPDeviceName() {
		final String xmlDescription = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>living room cl</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6341fe</UDN><cppId>1c5a6bfffe6341fe</cppId></device></root>";
		BaseUrlParser mBaseParser = new BaseUrlParser();
		mBaseParser.parse(xmlDescription);
		final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
		assertEquals("living room cl", deviceList.get(0).getFriendlyName());
	}

	public void testSSDPDeviceManufacturer() {
		final String xmlDescription = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>living room cl</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6341fe</UDN><cppId>1c5a6bfffe6341fe</cppId></device></root>";
		BaseUrlParser mBaseParser = new BaseUrlParser();
		mBaseParser.parse(xmlDescription);
		final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
		assertEquals("Royal Philips Electronics", deviceList.get(0).getManufacturer());
	}

	public void testSSDPDeviceModelName() {
		final String xmlDescription = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>living room cl</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6341fe</UDN><cppId>1c5a6bfffe6341fe</cppId></device></root>";
		BaseUrlParser mBaseParser = new BaseUrlParser();
		mBaseParser.parse(xmlDescription);
		final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
		assertEquals("AirPurifier", deviceList.get(0).getModelName());
	}

	public void testSSDPDeviceUDN() {
		final String xmlDescription = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>living room cl</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6341fe</UDN><cppId>1c5a6bfffe6341fe</cppId></device></root>";
		BaseUrlParser mBaseParser = new BaseUrlParser();
		mBaseParser.parse(xmlDescription);
		final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
		assertEquals("uuid:12345678-1234-1234-1234-1c5a6b6341fe", deviceList.get(0).getUdn());
	}

	public void testSSDPDeviceCPPID() {
		final String xmlDescription = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>living room cl</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6341fe</UDN><cppId>1c5a6bfffe6341fe</cppId></device></root>";
		BaseUrlParser mBaseParser = new BaseUrlParser();
		mBaseParser.parse(xmlDescription);
		final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
		assertEquals("1c5a6bfffe6341fe", deviceList.get(0).getCppId());
	}

	public void testSSDPDeviceNameError() {
		final String xmlDescription = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType1>urn:philips-com:device:DiProduct:1</deviceType1><friendlyName1>living room cl</friendlyName1><manufacturer1>Royal Philips Electronics</manufacturer1><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6341fe</UDN><cppId>1c5a6bfffe6341fe</cppId></device></root>";
		BaseUrlParser mBaseParser = new BaseUrlParser();
		mBaseParser.parse(xmlDescription);
		final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
		assertNull(deviceList.get(0).getFriendlyName());
	}

	public void testSSDPDeviceNameWithSpecialCharator() {
		final String xmlDescription = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>living room @4$/@$@-_</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6341fe</UDN><cppId>1c5a6bfffe6341fe</cppId></device></root>";
		BaseUrlParser mBaseParser = new BaseUrlParser();
		mBaseParser.parse(xmlDescription);
		final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
		assertEquals("living room @4$/@$@-_", deviceList.get(0).getFriendlyName());
	}

	public void testSSDPDeviceNameWithSpecialCharatorError() {
		final String xmlDescription = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>living room &;</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6341fe</UDN><cppId>1c5a6bfffe6341fe</cppId></device></root>";
		BaseUrlParser mBaseParser = new BaseUrlParser();
		mBaseParser.parse(xmlDescription);
		final List<SSDPdevice> deviceList = mBaseParser.getDevicesList();
		assertNull(deviceList.get(0).getFriendlyName());
	}

	public void testHourlyWeatherData() {
		String data = "<hourly><step time=\"2014-07-28 18:00:00.0\">" +
				"<temperature>33.5</temperature><feelsLike>37.2</feelsLike><relativeHumidity>28</relativeHumidity>" +
				"<windDirection_10m summary=\"S\">176</windDirection_10m><windSpeed_10m>5.5</windSpeed_10m>" +
				"<icon>clear</icon></step></hourly>" ;
		List<Weatherdto> hourlyWeatherList = DataParser.getHourlyWeatherData(data) ;
		assertNotNull(hourlyWeatherList);
	}

	public void testHourlyWeather_InvalidData() {
		String data = "<hourly>\"2014-07-28 18:00:00.0\">" +
				"<temperature>33.5</temperature><feelsLike>37.2</feelsLike><relativeHumidity>28</relativeHumidity>" +
				"<windDirection_10m summary=\"S\">176</windDirection_10m><windSpeed_10m>5.5</windSpeed_10m>" +
				"<icon>clear</icon></hourly>" ;
		List<Weatherdto> hourlyWeatherList = DataParser.getHourlyWeatherData(data) ;
		assertNull(hourlyWeatherList);
	}

	public void testHourlyWeather_NumberOfRecords() {
		String data = "<hourly><step time=\"2014-07-28 18:00:00.0\">" +
				"<temperature>33.5</temperature><feelsLike>37.2</feelsLike><relativeHumidity>28</relativeHumidity>" +
				"<windDirection_10m summary=\"S\">176</windDirection_10m><windSpeed_10m>5.5</windSpeed_10m>" +
				"<icon>clear</icon></step></hourly>" ;
		List<Weatherdto> hourlyWeatherList = DataParser.getHourlyWeatherData(data) ;
		assertEquals(1, hourlyWeatherList.size()) ;
		assertNotSame(0, hourlyWeatherList.size()) ;
	}

	public void testHourlyWeather_Temparature() {
		String data = "<hourly><step time=\"2014-07-28 18:00:00.0\">" +
				"<temperature>33.5</temperature><feelsLike>37.2</feelsLike><relativeHumidity>28</relativeHumidity>" +
				"<windDirection_10m summary=\"S\">176</windDirection_10m><windSpeed_10m>5.5</windSpeed_10m>" +
				"<icon>clear</icon></step></hourly>" ;
		List<Weatherdto> hourlyWeatherList = DataParser.getHourlyWeatherData(data) ;
		assertEquals(33.5, hourlyWeatherList.get(0).getTempInCentigrade(),0.0) ;
	}

	public void testHourlyWeather_InvalidTemparature() {
		String data = "<hourly><step time=\"2014-07-28 18:00:00.0\">" +
				"<temperature>33.s</temperature><feelsLike>37.2</feelsLike><relativeHumidity>28</relativeHumidity>" +
				"<windDirection_10m summary=\"S\">176</windDirection_10m><windSpeed_10m>5.5</windSpeed_10m>" +
				"<icon>clear</icon></step></hourly>" ;
		List<Weatherdto> hourlyWeatherList = DataParser.getHourlyWeatherData(data) ;
		assertNull(hourlyWeatherList) ;
	}

	public void testHourlyWeather_Time() {
		String data = "<hourly><step time=\"2014-07-28 18:00:00.0\">" +
				"<temperature>33.5</temperature><feelsLike>37.2</feelsLike><relativeHumidity>28</relativeHumidity>" +
				"<windDirection_10m summary=\"S\">176</windDirection_10m><windSpeed_10m>5.5</windSpeed_10m>" +
				"<icon>clear</icon></step></hourly>" ;
		List<Weatherdto> hourlyWeatherList = DataParser.getHourlyWeatherData(data) ;
		assertEquals("2014-07-28 18:00:00.0", hourlyWeatherList.get(0).getTime()) ;
	}

	public void testHourlyWeather_WindSpeed() {
		String data = "<hourly><step time=\"2014-07-28 18:00:00.0\">" +
				"<temperature>33.5</temperature><feelsLike>37.2</feelsLike><relativeHumidity>28</relativeHumidity>" +
				"<windDirection_10m summary=\"S\">176</windDirection_10m><windSpeed_10m>5.5</windSpeed_10m>" +
				"<icon>clear</icon></step></hourly>" ;
		List<Weatherdto> hourlyWeatherList = DataParser.getHourlyWeatherData(data) ;
		assertEquals(5.5,hourlyWeatherList.get(0).getWindSpeed(),0.0) ;
	}

	public void testHourlyWeather_InvalidWindSpeed() {
		String data = "<hourly><step time=\"2014-07-28 18:00:00.0\">" +
				"<temperature>33.5</temperature><feelsLike>37.2</feelsLike><relativeHumidity>28</relativeHumidity>" +
				"<windDirection_10m summary=\"S\">176</windDirection_10m><windSpeed_10m>invalidwindspeed</windSpeed_10m>" +
				"<icon>clear</icon></step></hourly>" ;
		List<Weatherdto> hourlyWeatherList = DataParser.getHourlyWeatherData(data) ;
		assertNull(hourlyWeatherList);
	}

	public void testHourlyWeather_WindDesc() {
		String data = "<hourly><step time=\"2014-07-28 18:00:00.0\">" +
				"<temperature>33.5</temperature><feelsLike>37.2</feelsLike><relativeHumidity>28</relativeHumidity>" +
				"<windDirection_10m summary=\"S\">176</windDirection_10m><windSpeed_10m>5.5</windSpeed_10m>" +
				"<icon>clear</icon></step></hourly>" ;
		List<Weatherdto> hourlyWeatherList = DataParser.getHourlyWeatherData(data) ;
		assertEquals("clear",hourlyWeatherList.get(0).getWeatherDesc()) ;
		assertNotSame("sunny", hourlyWeatherList.get(0).getWeatherDesc()) ;
	}

	//{"observe":
	//{"101010100":{"l":{"l1":"-1","l2":"21","l3":"3","l4":"5","l5":"00","l7":"12:15"}},
	//"101070301":{"l":{"l1":"-6","l2":"21","l3":"3","l4":"6","l5":"00","l7":"12:20"}},
	//"101200509":{"l":{"l1":"6","l2":"52","l3":"2","l4":"2","l5":"01","l7":"12:35"}},
	//"101270201":{"l":{"l1":"17","l2":"51","l3":"1","l4":"1","l5":"01","l7":"12:05"}}},"api_version":"4.0"}
//	//TODO : Rewrite dataparser tests for Outdoor dashboard.
//	public void testParseLocationEventWeatherWithInvalidJson() {
//		String data = "{\"observe\":{\"101270101\":{\"l\":{\",\"l2\":\"84\",\"l3\":\"1\",\"l4\":\"7\",\"l5\":\"03\",\"l7\":\"19:15\"}}}}" ;
//		OutdoorWeather outdoorWeather = DataParser.parseLocationWeather(data) ;
//		assertNull(outdoorWeather);
//	}
//
//	public void testParseLocationEventWeatherWithNullData() {
//		String data = null ;
//		OutdoorWeather outdoorWeather = DataParser.parseLocationWeather(data) ;
//		assertNull(outdoorWeather);
//	}
//
//	public void testParseLocationEventWeather() {
//		String data = "{\"observe\":{\"101270101\":{\"l\":{\"l1\":\"22\",\"l2\":\"84\",\"l3\":\"1\",\"l4\":\"7\",\"l5\":\"03\",\"l7\":\"19:15\"}}}}" ;
//		OutdoorWeather outdoorWeather = DataParser.parseLocationWeather(data) ;
//		assertNotNull(outdoorWeather);
//	}
//
//	public void testParseLocationEventWeatherTemparature() {
//		String data = "{\"observe\":{\"101270101\":{\"l\":{\"l1\":\"22\",\"l2\":\"84\",\"l3\":\"1\",\"l4\":\"7\",\"l5\":\"03\",\"l7\":\"19:15\"}}}}" ;
//		OutdoorWeather outdoorWeather = DataParser.parseLocationWeather(data) ;
//		assertEquals(22, outdoorWeather.getTemperature()) ;
//	}
//
//	public void testParseLocationEventWeatherHumidity() {
//		String data = "{\"observe\":{\"101270101\":{\"l\":{\"l1\":\"22\",\"l2\":\"84\",\"l3\":\"1\",\"l4\":\"7\",\"l5\":\"03\",\"l7\":\"19:15\"}}}}" ;
//		OutdoorWeather outdoorWeather = DataParser.parseLocationWeather(data) ;
//		assertEquals(84, outdoorWeather.getHumidity()) ;
//	}
//
//	public void testParseLocationEventAreaID() {
//		String data = "{\"observe\":{\"101270101\":{\"l\":{\"l1\":\"22\",\"l2\":\"84\",\"l3\":\"1\",\"l4\":\"7\",\"l5\":\"03\",\"l7\":\"19:15\"}}}}" ;
//		OutdoorWeather outdoorWeather = DataParser.parseLocationWeather(data) ;
//		assertEquals("101270101", outdoorWeather.getAreaID()) ;
//	}
//
//	public void testParseLocationEventTime() {
//		String data = "{\"observe\":{\"101270101\":{\"l\":{\"l1\":\"22\",\"l2\":\"84\",\"l3\":\"1\",\"l4\":\"7\",\"l5\":\"03\",\"l7\":\"19:15\"}}}}" ;
//		OutdoorWeather outdoorWeather = DataParser.parseLocationWeather(data) ;
//		assertEquals("19:15", outdoorWeather.getUpdatedTime()) ;
//	}
//

	public void testLocationOutdoorAQIWithInvalidJson( ) {
		String data = "{\"air\":{\"101270101\":{\"p\":{\"p1\":\"14\",\"p2\":\"36201406191800\"}}}}" ;
		List<OutdoorAQI> outdoorAQI = DataParser.parseLocationAQI(data) ;
		assertNull(outdoorAQI) ;
	}

	public void testLocationOutdoorAQIWithNullData() {
		String data = null;
		List<OutdoorAQI> outdoorAQI = DataParser.parseLocationAQI(data) ;
		assertNull(outdoorAQI) ;
	}

	public void testLocationOutdoorAQIWithEmptyData() {
		String data = "";
		List<OutdoorAQI> outdoorAQI = DataParser.parseLocationAQI(data) ;
		assertNull(outdoorAQI) ;
	}


	public void testLocationOutdoorAQI_PM25( ) {
		String data = "{\"p\":[{\"101200509\":{\"p1\":\"66\",\"p2\":\"62\",\"p3\":\"142\",\"p4\":\"5\",\"p5\":\"14\",\"updatetime\":\"201412021206\"}}],\"api_version\":\"4.0\"}";
		List<OutdoorAQI> outdoorAQIs = DataParser.parseLocationAQI(data) ;
		assertEquals(66, outdoorAQIs.get(0).getPM25());
	}

	public void testLocationOutdoorAQI_index( ) {
		String data = "{\"p\":[{\"101200509\":{\"p1\":\"66\",\"p2\":\"62\",\"p3\":\"142\",\"p4\":\"5\",\"p5\":\"14\",\"updatetime\":\"201412021206\"}}],\"api_version\":\"4.0\"}";
		List<OutdoorAQI> outdoorAQIs = DataParser.parseLocationAQI(data) ;
		assertEquals(62, outdoorAQIs.get(0).getAQI());
	}

	public void testLocationOutdoorAQI_AreaID( ) {
		String data = "{\"p\":[{\"101200509\":{\"p1\":\"66\",\"p2\":\"62\",\"p3\":\"142\",\"p4\":\"5\",\"p5\":\"14\",\"updatetime\":\"201412021206\"}}],\"api_version\":\"4.0\"}";
		List<OutdoorAQI> outdoorAQIs = DataParser.parseLocationAQI(data) ;
		assertEquals("101200509", outdoorAQIs.get(0).getAreaID());
	}

	public void testOutdoorAQIListSize( ) {
		String data = "{\"p\":[{\"101270201\":{\"p1\":\"40\",\"p2\":\"71\",\"p3\":\"86\",\"p4\":\"22\",\"p5\":\"33\",\"updatetime\":\"201412021207\"},"
				 + "\"101070301\":{\"p1\":\"47\",\"p2\":\"118\",\"p3\":\"174\",\"p4\":\"102\",\"p5\":\"42\",\"updatetime\":\"201412021206\"},"
				 + "\"101010100\":{\"p1\":\"14\",\"p2\":\"33\",\"p3\":\"29\",\"p4\":\"12\",\"p5\":\"46\",\"updatetime\":\"201412021205\"},"
				 + "\"101200509\":{\"p1\":\"66\",\"p2\":\"62\",\"p3\":\"142\",\"p4\":\"5\",\"p5\":\"14\",\"updatetime\":\"201412021206\"}}],\"api_version\":\"4.0\"}";

		List<OutdoorAQI> outdoorAQIs = DataParser.parseLocationAQI(data) ;
		assertEquals(4, outdoorAQIs.size()) ;
	}

	public void testParseHistoricalAQIData_invalidJson() {
		String data = "{\"p\":[{\"101270101\":{\"p1\"\",\"p2\":\"255\",\"p3\":\"403\",\"p4\":\"4\",\"p5\":\"25\",\"updatetime\":\"201407311706\"}}],\"api_version\":\"4.0\"}";
		List<OutdoorAQI> aqis = DataParser.parseHistoricalAQIData(data);
		assertNull(aqis);
	}

	public void testParseHistoricalAQIData_validJson() {
		String data1 = "{\"p\":[{\"101270101\":{\"p1\":\"205\",\"p2\":\"255\",\"p3\":\"403\",\"p4\":\"4\",\"p5\":\"25\",\"updatetime\":\"201407311706\"}}],\"api_version\":\"4.0\"}" ;
		List<OutdoorAQI> aqis = DataParser.parseHistoricalAQIData(data1);
		assertNotNull(aqis);
	}

	public void testParseFourDayForecast_invalidJson() {
		String data = "{das:adsa}";
		List<ForecastWeatherDto> dtos = DataParser.parseFourDaysForecastData(data);
		assertNull(dtos);
	}

	public void testParseFourDayForecast_validJson() {
		String data = "{\"forecast4d\":{\"101270101\":"
				+ "{\"c\":{\"c1\":\"101270101\",\"c2\":\"chengdu\",\"c3\":\"成都\",\"c4\":\"chengdu\",\"c5\":\"成都\",\"c6\":\"sichuan\",\"c7\":\"四川\",\"c8\":\"china\",\"c9\":\"中国\",\"c10\":\"1\",\"c11\":\"028\",\"c12\":\"610000\",\"c13\":\"104.066541\",\"c14\":\"30.572269\",\"c15\":\"507\",\"c16\":\"AZ9280\",\"c17\":\"+8\"},"
				+ "\"f\":{\"f1\":["
				+ "{\"fa\":\"01\",\"fb\":\"03\",\"fc\":\"33\",\"fd\":\"24\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:18|20:01\"},"
				+ "{\"fa\":\"01\",\"fb\":\"03\",\"fc\":\"32\",\"fd\":\"23\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:19|20:00\"},"
				+ "{\"fa\":\"02\",\"fb\":\"08\",\"fc\":\"31\",\"fd\":\"23\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:20|20:00\"},"
				+ "{\"fa\":\"03\",\"fb\":\"08\",\"fc\":\"30\",\"fd\":\"22\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:20|19:59\"}],"
				+ "\"f0\":\"201407281100\"}}}}";
		List<ForecastWeatherDto> dtos = DataParser.parseFourDaysForecastData(data);
		assertEquals(dtos.get(0).getTemperatureDay(), "33");
	}

	public void testParseFourDayForecast_incompeleteJson() {
		String data = "{\"forecast4d\":{\"101270101\":"
				+ "{\"c\":{\"c1\":\"101270101\",\"c2\":\"chengdu\",\"c3\":\"成都\",\"c4\":\"chengdu\",\"c5\":\"成都\",\"c6\":\"sichuan\",\"c7\":\"四川\",\"c8\":\"china\",\"c9\":\"中国\",\"c10\":\"1\",\"c11\":\"028\",\"c12\":\"610000\",\"c13\":\"104.066541\",\"c14\":\"30.572269\",\"c15\":\"507\",\"c16\":\"AZ9280\",\"c17\":\"+8\"},"
				+ "\"f\":{\"f1\":["
				+ "{\"fa\":\"01\",\"fb\":\"03\",\"fc\":\"33\",\"fd\":\"24\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:18|20:01\"},"
				+ "{\"fa\":\"01\",\"fb\":\"03\",\"fc\":\"32\",\"fd\":\"23\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:19|20:00\"},"
				+ "{\"fa\":\"02\",\"fb\":\"08\",\"fc\":\"31\",\"fd\":\"23\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:20|20:00\"},"
				+ "\"f0\":\"201407281100\"}}}}";
		List<ForecastWeatherDto> dtos = DataParser.parseFourDaysForecastData(data);
		assertNull(dtos);
	}

	public void testParseFourDayForecast_emptyField() {
		String data = "{\"forecast4d\":{\"101270101\":"
				+ "{\"c\":{\"c1\":\"101270101\",\"c2\":\"chengdu\",\"c3\":\"成都\",\"c4\":\"chengdu\",\"c5\":\"成都\",\"c6\":\"sichuan\",\"c7\":\"四川\",\"c8\":\"china\",\"c9\":\"中国\",\"c10\":\"1\",\"c11\":\"028\",\"c12\":\"610000\",\"c13\":\"104.066541\",\"c14\":\"30.572269\",\"c15\":\"507\",\"c16\":\"AZ9280\",\"c17\":\"+8\"},"
				+ "\"f\":{\"f1\":["
				+ "{\"fa\":\"01\",\"fb\":\"03\",\"fc\":\"\",\"fd\":\"24\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:18|20:01\"},"
				+ "{\"fa\":\"01\",\"fb\":\"03\",\"fc\":\"32\",\"fd\":\"23\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:19|20:00\"},"
				+ "{\"fa\":\"02\",\"fb\":\"08\",\"fc\":\"31\",\"fd\":\"23\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:20|20:00\"},"
				+ "{\"fa\":\"03\",\"fb\":\"08\",\"fc\":\"30\",\"fd\":\"22\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:20|19:59\"}],"
				+ "\"f0\":\"201407281100\"}}}}";
		List<ForecastWeatherDto> dtos = DataParser.parseFourDaysForecastData(data);
		assertEquals(dtos.get(0).getTemperatureDay(), "");
	}

	public void testUSEmbassyLocationOutdoorAQIWithInvalidJson( ) {
		String data = "{\"resultcode\":\"200\",\"reason\":\"SUCCESSED!\",\"result\":{\"city\":\"苏州\",	\"PM2.5\":\"52\",\"AQI\":\"89\",\"PM10\":\"85\",\"CO\":\"0.73\",\"NO2\":\"14\",\"time\":\"2013-08-01 13:00:00\"}}";
		List<OutdoorAQI> outdoorAQI = DataParser.parseLocationAQI(data) ;
		assertNull(outdoorAQI) ;
	}

	public void testUSEmbassyLocationOutdoorAQIWithNullData() {
		String data = null ;
		List<OutdoorAQI> outdoorAQI = DataParser.parseUSEmbassyLocationAQI(data, "101010100") ;
		assertNull(outdoorAQI) ;
	}


	public void testUSEmbassyLocationOutdoorAQI_PM25( ) {
		String data = "{\"resultcode\":\"200\",\"reason\":\"SUCCESSED!\",\"result\":[{\"city\":\"苏州\",	\"PM2.5\":\"52\",\"AQI\":\"89\",\"PM10\":\"85\",\"CO\":\"0.73\",\"NO2\":\"14\",	\"O3\":\"192\",\"SO2\":\"17\",\"time\":\"2013-08-01 13:00:00\"}]}";
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyLocationAQI(data, "101010100") ;
		assertEquals(52, outdoorAQIs.get(0).getPM25());
	}

	public void testUSEmbassyLocationOutdoorAQI_index( ) {
		String data = "{\"resultcode\":\"200\",\"reason\":\"SUCCESSED!\",\"result\":[{\"city\":\"苏州\",	\"PM2.5\":\"52\",\"AQI\":\"89\",\"PM10\":\"85\",\"CO\":\"0.73\",\"NO2\":\"14\",	\"O3\":\"192\",\"SO2\":\"17\",\"time\":\"2013-08-01 13:00:00\"}]}";
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyLocationAQI(data, "101010100") ;
		assertEquals(89, outdoorAQIs.get(0).getAQI());
	}

	public void testUSEmbassyLocationOutdoorAQI_timestamp( ) {
		String data = "{\"resultcode\":\"200\",\"reason\":\"SUCCESSED!\",\"result\":[{\"city\":\"苏州\",	\"PM2.5\":\"52\",\"AQI\":\"89\",\"PM10\":\"85\",\"CO\":\"0.73\",\"NO2\":\"14\",	\"O3\":\"192\",\"SO2\":\"17\",\"time\":\"2013-08-01 13:00:00\"}]}";
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyLocationAQI(data, "101010100") ;
		assertEquals("20130801130000", outdoorAQIs.get(0).getTimeStamp());
	}

	public void testParseUSEmbassyHistoricalAQIData_invalidJson() {
		String data = "{\"resultcode\":\"200\",\"reason\":\"SUCCESSED!\",\"error_code\":0,\"result\":[{\"citynow\":{\"city\":\"suzhou\",\"AQI\"\"\",\"quality\":\"良\",\"date\":\"2014-05-09 14:00\"},\"lastTwoWeeks\":{\"1\":{\"city\":\"suzhou\",\"AQI\":\"100\",\"quality\":\"良\",\"date\":\"2014-05-08\"},\"2\":{\"city\":\"suzhou\",\"AQI\":\"99\",\"quality\":\"良\",\"date\":\"2014-05-07\"},\"3\":{\"city\":\"suzhou\",\"AQI\":\"77\",\"quality\":\"良\",\"date\":\"2014-05-06\"},\"4\":{\"city\":\"suzhou\",\"AQI\":\"75\",\"quality\":\"良\",\"date\":\"2014-05-05\"},\"5\":{\"city\":\"suzhou\",\"AQI\":\"78\",\"quality\":\"良\",\"date\":\"2014-05-04\"}}}]}";
		List<OutdoorAQI> aqis = DataParser.parseUSEmbassyHistoricalAQIData(data);
		assertNull(aqis);
	}

	public void testParseUSEmbassyHistoricalAQIData_validJson() {
		String data = "{\"resultcode\":\"200\",\"reason\":\"SUCCESSED!\",\"error_code\":0,\"result\":[{\"citynow\":{\"city\":\"suzhou\",\"AQI\":\"77\",\"quality\":\"良\",\"date\":\"2014-05-09 14:00\"},\"lastTwoWeeks\":{\"1\":{\"city\":\"suzhou\",\"AQI\":\"100\",\"quality\":\"良\",\"date\":\"2014-05-08\"},\"2\":{\"city\":\"suzhou\",\"AQI\":\"99\",\"quality\":\"良\",\"date\":\"2014-05-07\"},\"3\":{\"city\":\"suzhou\",\"AQI\":\"77\",\"quality\":\"良\",\"date\":\"2014-05-06\"},\"4\":{\"city\":\"suzhou\",\"AQI\":\"75\",\"quality\":\"良\",\"date\":\"2014-05-05\"},\"5\":{\"city\":\"suzhou\",\"AQI\":\"78\",\"quality\":\"良\",\"date\":\"2014-05-04\"}}}]}";
		List<OutdoorAQI> aqis = DataParser.parseUSEmbassyHistoricalAQIData(data);
		assertNotNull(aqis);
	}

}
