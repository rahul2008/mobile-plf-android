package com.philips.cl.di.dev.pa.test;
import java.util.ArrayList;
import java.util.List;

import com.philips.cl.di.common.ssdp.controller.BaseUrlParser;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;
import com.philips.cl.di.dev.pa.datamodel.City;
import com.philips.cl.di.dev.pa.datamodel.DeviceDto;
import com.philips.cl.di.dev.pa.datamodel.DeviceWifiDto;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo.FirmwareState;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dev.pa.util.DataParser;

import junit.framework.TestCase;

public class DataParserTest extends TestCase {
	
	private String scheduleDetailJson = "{\"name\":\"16:00\",\"enabled\":true,\"time\":\"16:00\",\"days\":\"123\",\"product\":1,\"port\":\"air\",\"command\":{\"om\":\"a\"}}";
	private String allScheduleJson = "{\"2\":{\"name\":\"18:45\"},\"3\":{\"name\":\"15:45\"},\"4\":{\"name\":\"20:00\"}}";
	
	public void testAirPurifierInvalidData() {
		String parseData = "invalid data" ;
		assertNull( DataParser.parseAirPurifierEventData(parseData)) ;
	}
	
	public void testAirPurifierNullData() {
		String parseData = null ;
		assertNull(DataParser.parseAirPurifierEventData(parseData)) ;
	}
	
	public void testAirPurifierInvalidJSON() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\"" ;
		assertNull(DataParser.parseAirPurifierEventData(parseData)) ;
	}
	
	public void testAirPurifierPowerMode() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertEquals("1",DataParser.parseAirPurifierEventData(parseData).getPowerMode()) ;
	}
	
	public void testAirPurifierMachineMode() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(DataParser.parseAirPurifierEventData(parseData).getMachineMode().equals("a")) ;
	}
	
	public void testAirPurifierAQILight() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(DataParser.parseAirPurifierEventData(parseData).getAqiL() == 1) ;
	}
	
	public void testAirPurifierChildLock() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(DataParser.parseAirPurifierEventData(parseData).getChildLock() == 0) ;
	}
	
	public void testAirPurifierAQIValue() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(DataParser.parseAirPurifierEventData(parseData).getIndoorAQI() == 1) ;
	}
	
	public void testAirPurifierFanSpeed() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(DataParser.parseAirPurifierEventData(parseData).getActualFanSpeed().equals("1")) ;
	}
	
	public void testAirPurifierAQIThreshold() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(DataParser.parseAirPurifierEventData(parseData).getAqiThreshold() == 500) ;
	}
	
	public void testAirPurifierPowerModeNotEquals() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertFalse(DataParser.parseAirPurifierEventData(parseData).getPowerMode().equals("0")) ;
	}
	
	public void testIndoorAQI_DCS_1() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertNotNull(DataParser.parseAirPurifierEventDataFromCPP(parseData)) ;
	}
	
	public void testIndoorAQI_DCS_2() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"no_data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertNull(DataParser.parseAirPurifierEventDataFromCPP(parseData)) ;
	}
	
	public void testIndoorAQI_DCS_3() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertEquals("a",DataParser.parseAirPurifierEventDataFromCPP(parseData).getMachineMode()) ;
	}
	
	public void testIndoorAQI_DCS_4() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertTrue(DataParser.parseAirPurifierEventDataFromCPP(parseData).getAqiL() == 1) ;
	}
	
	public void testIndoorAQI_DCS_5() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertFalse(DataParser.parseAirPurifierEventDataFromCPP(parseData).getChildLock() == 1) ;
	}
	
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
	
	public void testParseFirmwareEventNullData() {
		String parseData = null;
		assertNull(DataParser.parseFirmwareEventData(parseData)) ;
	}
	
	public void testParseFirmwareEventEmptyData() {
		String parseData = "";
		assertNull(DataParser.parseFirmwareEventData(parseData)) ;
	}
	
	public void testParseFirmwareEventProperData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";
		
		FirmwarePortInfo result = DataParser.parseFirmwareEventData(parseData);
		
		assertNotNull(result);
		assertEquals(result.getName(), "HCN_DEVGEN");
		assertEquals(result.getVersion(), "1.1");
		assertEquals(result.getUpgrade(), "1.2");
		assertEquals(result.getState(), FirmwareState.IDLE);
		assertEquals(result.getProgress(), 0);
		assertEquals(result.getStatusmsg(), "");
		assertEquals(result.isMandatory(), false);
	}
	
	public void testParseFirmwareEventIncompleteData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgra";
		
		FirmwarePortInfo result = DataParser.parseFirmwareEventData(parseData);
		assertNull(result);
	}
	
	public void testParseFirmwareEventNoUpgradeData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";
		
		FirmwarePortInfo result = DataParser.parseFirmwareEventData(parseData);
		assertEquals(result.getUpgrade(), "");
	}
	
	public void testParseFirmwareEventNoVersionData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"upgrade\":\"1.1\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";
		
		FirmwarePortInfo result = DataParser.parseFirmwareEventData(parseData);
		assertEquals(result.getVersion(), "");
	}
	
	public void testParseFirmwareEventValidStateData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"ready\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";
		
		FirmwarePortInfo result = DataParser.parseFirmwareEventData(parseData);
		assertEquals(result.getState(), FirmwareState.READY);
	}
	
	public void testParseFirmwareEventInValidStateData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";
		
		FirmwarePortInfo result = DataParser.parseFirmwareEventData(parseData);
		assertNull(result.getState());
	}
	
	public void testParseFirmwareEventTooBigProgressData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":150,\"statusmsg\":\"\",\"mandatory\":false}";
		
		FirmwarePortInfo result = DataParser.parseFirmwareEventData(parseData);
		assertEquals(result.getProgress(), 100);
	}
	
	public void testParseFirmwareEventNegativeProgressData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":-1,\"statusmsg\":\"\",\"mandatory\":false}";
		
		FirmwarePortInfo result = DataParser.parseFirmwareEventData(parseData);
		assertEquals(result.getProgress(), 0);
	}
	
	public void testParseFirmwareEventWithAirportEvent() {
		String parseData = "{\"aqi\":\"3\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"0\",\"fs1\":\"33\",\"fs2\":\"881\",\"fs3\":\"2801\",\"fs4\":\"2801\",\"dtrs\":\"0\",\"aqit\":\"30\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"30414\",\"psens\":\"1\"}";
		
		FirmwarePortInfo result = DataParser.parseFirmwareEventData(parseData);
		assertNull(result);
	}
		
	public void testParseLocationDataNull() {
		List<City> cities = DataParser.parseLocationData(null);
		assertNull(cities);
	}
	
	public void testParseLocationDataEmpty() {
		List<City> cities = DataParser.parseLocationData("");
		assertNull(cities);
	}
	
	public void testParseLocationDataNoneFormat() {
		List<City> cities = DataParser.parseLocationData("{hello}");
		assertNull(cities);
	}
	
	public void testParseLocationData() {
		List<City> cities = DataParser.parseLocationData("{\"cities\": {\"泰安\": {\"weather_ex\": {\"wind_degrees\": 140,\"wind_kph\": 11,\"temp_c\": 7,\"high\": 13,\"weather\": \"\",\"low\": 3,\"icon\": \"clear\"},\"lon\": 117.08675,\"city_name\": \"泰安\",\"weather\": \"晴转多云 23~11°C 南风小于3级转3-4级\",\"key\": \"taian\",\"lat\": 36.200546,\"gov\": {\"co\": 776,\"idx\": 70,\"pm10\": 96,\"stations\": {\"交通技校\": {\"co\": 1090,\"idx\": 59,\"pm10\": 90,\"lon\": 117.041115,\"so2\": 12,\"no2\": 16,\"lat\": 36.201946,\"station_name\": \"交通技校\",\"o3\": 64,\"pm25\": 32,\"t\": \"2014-05-05 12:00\"}},\"so2\": 18,\"no2\": 17,\"o3\": 48,\"pm25\": 46,\"t\": \"2014-05-05 12:00\"}}}}");
		String keyStr = cities.get(0).getKey().trim();
		String iconStr = cities.get(0).getWeather().getIcon().trim();
		assertEquals("taian", keyStr);
		assertEquals("clear", iconStr);
	}
	
	public void testGetEWSDeviceWifiDetailsNull() {
		DeviceWifiDto deviceWifiDto = DataParser.getDeviceWifiDetails(null);
		assertNull(deviceWifiDto);
	}
	
	public void testGetEWSDeviceWifiDetailsEmpty() {
		DeviceWifiDto deviceWifiDto = DataParser.getDeviceWifiDetails("");
		assertNull(deviceWifiDto);
	}
	
	public void testGetEWSDeviceWifiDetailsNoneFormat() {
		DeviceWifiDto deviceWifiDto = DataParser.getDeviceWifiDetails("{hello}");
		assertNull(deviceWifiDto);
	}
	
	public void testGetEWSDeviceDetailsNull() {
		DeviceDto deviceDto  = DataParser.getDeviceDetails(null);
		assertNull(deviceDto);
	}
	
	public void testGetEWSDeviceDetailsEmpty() {
		DeviceDto deviceDto  = DataParser.getDeviceDetails("");
		assertNull(deviceDto);
	}
	
	public void testGetEWSDeviceDetailsNoneFormat() {
		DeviceDto deviceDto  = DataParser.getDeviceDetails("{hello}");
		assertNull(deviceDto);
	}
	
	public void testParseSchedulerDtoWithNullParam() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto(null);
		assertNull(schedulePortInfos);
	}
	
	public void testParseSchedulerDtoWithEmptyStringParam() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto("");
		assertNull(schedulePortInfos);
	}
	
	public void testParseSchedulerDtoWithWrongParam() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto("{\"temp\":\"1\"}");
		assertNull(schedulePortInfos);
	}
	
	public void testParseSchedulerDtoSize() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto(allScheduleJson);
		assertEquals(3, schedulePortInfos.size());
	}
	
	public void testParseSchedulerDtoKeys() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto(allScheduleJson);
		ArrayList<Integer> keys = new ArrayList<Integer>();
		keys.add(2);
		keys.add(3);
		keys.add(4);
		
		assertTrue(keys.contains(schedulePortInfos.get(0).getScheduleNumber()));
		assertTrue(keys.contains(schedulePortInfos.get(1).getScheduleNumber()));
		assertTrue(keys.contains(schedulePortInfos.get(2).getScheduleNumber()));
	}
	
	public void testParseSchedulerDtoNames() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto(allScheduleJson);
		
		ArrayList<String> names = new ArrayList<String>();
		names.add("18:45");
		names.add("15:45");
		names.add("20:00");
		
		assertTrue(names.contains(schedulePortInfos.get(0).getName()));
		assertTrue(names.contains(schedulePortInfos.get(1).getName()));
		assertTrue(names.contains(schedulePortInfos.get(2).getName()));
	}
	
	public void testParseSchedulerDtoWithWrongJsonFormat() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto("hello");
		assertNull(schedulePortInfos);
	}
	
	public void testParseScheduleListViaCPPWithNullParam() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseScheduleListViaCPP(null);
		assertNull(schedulePortInfos);
	}
	
	public void testParseScheduleListViaCPPWithEmptyStringParam() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseScheduleListViaCPP("");
		assertNull(schedulePortInfos);
	}
	
	public void testParseScheduleListViaCPPWithWrongParam() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseScheduleListViaCPP("{\"temp\":\"1\"}");
		assertNull(schedulePortInfos);
	}
	
	public void testParseScheduleListViaCPPWithWrongJsonFormat() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseScheduleListViaCPP("hello");
		assertNull(schedulePortInfos);
	}
	
	public void testParseScheduleDetailsWithNullParam() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails(null);
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsWithEmptyStringParam() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails("");
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsWithWrongParam() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails("{\"temp\":\"1\"}");
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsWithWrongJsonFormat() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails("hello");
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsScheduleName() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails(scheduleDetailJson);
		assertEquals("16:00", schedulePortInfo.getName());
	}
	
	public void testParseScheduleDetailsScheduleTime() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails(scheduleDetailJson);
		assertEquals("16:00", schedulePortInfo.getScheduleTime());
	}
	
	public void testParseScheduleDetailsScheduleDay() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails(scheduleDetailJson);
		assertEquals("123", schedulePortInfo.getDays());
	}
	
	public void testParseScheduleDetailsScheduleMode() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails(scheduleDetailJson);
		assertEquals("a", schedulePortInfo.getMode());
	}
	
	public void testParseScheduleDetailsFromCPPWithNullParam() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetailsFromCPP(null);
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsFromCPPWithEmptyStringParam() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetailsFromCPP("");
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsFromCPPWithWrongParam() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetailsFromCPP("{\"temp\":\"1\"}");
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsFromCPPWithWrongJsonFormat() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetailsFromCPP("hello");
		assertNull(schedulePortInfo);
	}
}
