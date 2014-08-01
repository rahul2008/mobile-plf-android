package com.philips.cl.di.dev.pa.test;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.philips.cl.di.common.ssdp.controller.BaseUrlParser;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.datamodel.DeviceDto;
import com.philips.cl.di.dev.pa.datamodel.DeviceWifiDto;
import com.philips.cl.di.dev.pa.datamodel.DiscoverInfo;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo.FirmwareState;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dev.pa.util.DataParser;

public class DataParserTest extends TestCase {
	
	private String scheduleDetailJson = "{\"name\":\"16:00\",\"enabled\":true,\"time\":\"16:00\",\"days\":\"123\",\"product\":1,\"port\":\"air\",\"command\":{\"om\":\"a\"}}";
	private String allScheduleJson = "{\"2\":{\"name\":\"18:45\"},\"3\":{\"name\":\"15:45\"},\"4\":{\"name\":\"20:00\"}}";
	
	private String scheduleDetailJsonCpp = "{\"status\":0,\"data\":{\"name\":\"12:15\",\"enabled\":true,\"time\":\"12:15\",\"days\":\"123\",\"product\":1,\"port\":\"air\",\"command\":{\"om\":\"a\"}}}";
	private String allScheduleJsonCpp = "{\"status\":0,\"data\":{\"0\":{\"name\":\"16:14\"},\"1\":{\"name\":\"12:15\"}}}";
	
	
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
	
	public void testAirPurifierFirmwareData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"ready\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";
		
		AirPortInfo result = DataParser.parseAirPurifierEventData(parseData);
		assertNull(result);
	}
	
	public void testAirPurifierFirmwareData2() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"ready\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";
		
		AirPortInfo result = DataParser.parseAirPurifierEventData(parseData);
		assertNull(result);
	}
	
	public void testIndoorAQI_DCS_1() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertNotNull(DataParser.parseAirPurifierEventData(parseData)) ;
	}
	
	public void testIndoorAQI_DCS_2() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"no_data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertNull(DataParser.parseAirPurifierEventData(parseData)) ;
	}
	
	public void testIndoorAQI_DCS_3() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertEquals("a",DataParser.parseAirPurifierEventData(parseData).getMachineMode()) ;
	}
	
	public void testIndoorAQI_DCS_4() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertTrue(DataParser.parseAirPurifierEventData(parseData).getAqiL() == 1) ;
	}
	
	public void testIndoorAQI_DCS_5() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertFalse(DataParser.parseAirPurifierEventData(parseData).getChildLock() == 1) ;
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
	
	public void testparseSchedulerDtoWithNullParam() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto(null);
		assertNull(schedulePortInfos);
	}
	
	public void testparseSchedulerDtoWithEmptyStringParam() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto("");
		assertNull(schedulePortInfos);
	}
	
	public void testparseSchedulerDtoWithWrongParam() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto("{\"temp\":\"1\"}");
		assertNull(schedulePortInfos);
	}
	
	public void testparseSchedulerDtoWithWrongJsonFormat() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto("hello");
		assertNull(schedulePortInfos);
	}
	
	public void testparseSchedulerDtoSize() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto(allScheduleJsonCpp);
		assertEquals(2, schedulePortInfos.size());
	}
	
	public void testparseSchedulerDtoKeys() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto(allScheduleJsonCpp);
		ArrayList<Integer> keys = new ArrayList<Integer>();
		keys.add(0);
		keys.add(1);
		
		assertTrue(keys.contains(schedulePortInfos.get(0).getScheduleNumber()));
		assertTrue(keys.contains(schedulePortInfos.get(1).getScheduleNumber()));
	}
	
	public void testparseSchedulerDtoNames() {
		List<SchedulePortInfo> schedulePortInfos = DataParser.parseSchedulerDto(allScheduleJsonCpp);
		
		ArrayList<String> names = new ArrayList<String>();
		names.add("16:14");
		names.add("12:15");
		
		assertTrue(names.contains(schedulePortInfos.get(0).getName()));
		assertTrue(names.contains(schedulePortInfos.get(1).getName()));
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
	
	public void testparseScheduleDetailsWithNullParam() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails(null);
		assertNull(schedulePortInfo);
	}
	
	public void testparseScheduleDetailsWithEmptyStringParam() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails("");
		assertNull(schedulePortInfo);
	}
	
	public void testparseScheduleDetailsWithWrongParam() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails("{\"temp\":\"1\"}");
		assertNull(schedulePortInfo);
	}
	
	public void testparseScheduleDetailsWithWrongJsonFormat() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails("hello");
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsScheduleCppName() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails(scheduleDetailJsonCpp);
		assertEquals("12:15", schedulePortInfo.getName());
	}
	
	public void testParseScheduleDetailsScheduleCppTime() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails(scheduleDetailJsonCpp);
		assertEquals("12:15", schedulePortInfo.getScheduleTime());
	}
	
	public void testParseScheduleDetailsScheduleCppDay() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails(scheduleDetailJsonCpp);
		assertEquals("123", schedulePortInfo.getDays());
	}
	
	public void testParseScheduleDetailsScheduleCppMode() {
		SchedulePortInfo schedulePortInfo = DataParser.parseScheduleDetails(scheduleDetailJsonCpp);
		assertEquals("a", schedulePortInfo.getMode());
	}
	
	public void testParseDiscoverInfoNullParam() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo(null);
		assertNull(discoverInfo);
	}
	
	public void testParseDiscoverInfoValidEvent() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");
		assertNotNull(discoverInfo);
	}
	
	public void testParseDiscoverInfoEmptyString() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("");
		assertNull(discoverInfo);
	}
	
	public void testParseDiscoverInfoRandomString() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("fhaksjdfkljashl");
		assertNull(discoverInfo);
	}
	
	public void testParseDiscoverInfoRandomJSON() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"key\":\"value\"}");
		assertNull(discoverInfo);
	}
	
	public void testParseDiscoverInfoNoState() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("\"ClientIds\":\"\"}");
		assertNull(discoverInfo);
	}
	
	public void testParseDiscoverInfoNoClientIds() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"\"}");
		assertNull(discoverInfo);
	}
	
	public void testParseDiscoveryInfoNoClientId() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"Disconnected\"}");
		assertNull(discoverInfo);
	}
	
	public void testParseDiscoverInfoRandomState() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"Random\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");
		assertNull(discoverInfo);
	}
	
	public void testParseDiscoverInfoConnectedState() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");
		assertNotNull(discoverInfo);
	}
	
	public void testParseDiscoverInfoDisConnectedState() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"Disconnected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");
		assertNotNull(discoverInfo);
	}
	
	public void testParseDiscoverEmptyClientId() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":\"\"\"}");
		assertNull(discoverInfo);
	}

	public void testParseDiscoverEmptyClientIdArray() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":[]}");
		assertNull(discoverInfo);
	}
	
	public void testParseDiscoverSingleClientIdArray() {
		String[] expected = {"1c5a6bfffe63436c"};
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\"]}");
		
		assertNotNull(discoverInfo);
		assertEquals(expected[0], discoverInfo.getClientIds()[0]);
		assertTrue(discoverInfo.getClientIds().length == 1);
	}

	public void testParseDiscoverDoubleClientIdArray() {
		String[] expected = {"1c5a6bfffe63436c", "1c5a6bfffe634357"};
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");
		
		assertNotNull(discoverInfo);
		assertEquals(expected[0], discoverInfo.getClientIds()[0]);
		assertEquals(expected[1], discoverInfo.getClientIds()[1]);
		assertTrue(discoverInfo.getClientIds().length == 2);
	}
	
	public void testParseDiscoverConnectedEvent() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");
		
		assertNotNull(discoverInfo);
		assertTrue(discoverInfo.isConnected());
	}
	
	public void testParseDiscoverDisconnectedEvent() {
		DiscoverInfo discoverInfo = DataParser.parseDiscoverInfo("{\"State\":\"Disconnected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");
		
		assertNotNull(discoverInfo);
		assertFalse(discoverInfo.isConnected());
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
		OutdoorAQI outdoorAQI = DataParser.parseLocationAQI(data, "101270101") ;
		assertNull(outdoorAQI) ;
	}
	
	public void testLocationOutdoorAQIWithNullData() {
		String data = null ;
		OutdoorAQI outdoorAQI = DataParser.parseLocationAQI(data, "101270101") ;
		assertNull(outdoorAQI) ;
	}
	

	public void testLocationOutdoorAQI_PM25( ) {
		String data = "{\"p\":[{\"101270101\":{\"p1\":\"205\",\"p2\":\"255\",\"p3\":\"403\",\"p4\":\"4\",\"p5\":\"25\",\"updatetime\":\"201407311706\"}}],\"api_version\":\"4.0\"}" ;
		OutdoorAQI outdoorAQI = DataParser.parseLocationAQI(data, "101270101") ;
		assertEquals(205, outdoorAQI.getPM25()) ;
	}
	
	public void testLocationOutdoorAQI_index( ) {
		String data = "{\"p\":[{\"101270101\":{\"p1\":\"205\",\"p2\":\"255\",\"p3\":\"403\",\"p4\":\"4\",\"p5\":\"25\",\"updatetime\":\"201407311706\"}}],\"api_version\":\"4.0\"}" ;
		OutdoorAQI outdoorAQI = DataParser.parseLocationAQI(data, "101270101") ;
		assertEquals(255, outdoorAQI.getAQI()) ;
	}
	
//	public void testLocationOutdoorAQI_time( ) {
//		String data = "{\"p\":[{\"101010100\":{\"p1\":\"205\",\"p2\":\"255\",\"p3\":\"403\",\"p4\":\"4\",\"p5\":\"25\",\"updatetime\":\"201407311706\"}}],\"api_version\":\"4.0\"}" ;
//		OutdoorAQI outdoorAQI = DataParser.parseLocationAQI(data, "101270101") ;
//		assertEquals("201406191800", outdoorAQI.getPublishTime()) ;
//	}
	
//	public void testLocationOutdoorAQI_time_invalid( ) {
//		String data = "{\"p\":[{\"101010100\":{\"p1\":\"205\",\"p2\":\"255\",\"p3\":\"403\",\"p4\":\"4\",\"p5\":\"25\",\"updatetime\":\"201407311706\"}}],\"api_version\":\"4.0\"}" ;
//		OutdoorAQI outdoorAQI = DataParser.parseLocationAQI(data, "101270101") ;
//		assertNotSame("2014061918", outdoorAQI.getPublishTime()) ;
//	}
	
	public void testLocationOutdoorAQI_AreaID( ) {
		String data = "{\"p\":[{\"101270101\":{\"p1\":\"205\",\"p2\":\"255\",\"p3\":\"403\",\"p4\":\"4\",\"p5\":\"25\",\"updatetime\":\"201407311706\"}}],\"api_version\":\"4.0\"}" ;
		OutdoorAQI outdoorAQI = DataParser.parseLocationAQI(data, "101270101") ;
		assertEquals("101270101", outdoorAQI.getAreaID()) ;
	}
	
	public void testLocationOutdoorAQI( ) {
		String data = "{\"p\":[{\"101270101\":{\"p1\":\"205\",\"p2\":\"255\",\"p3\":\"403\",\"p4\":\"4\",\"p5\":\"25\",\"updatetime\":\"201407311706\"}}],\"api_version\":\"4.0\"}" ;
		OutdoorAQI outdoorAQI = DataParser.parseLocationAQI(data, "101270101") ;
		assertNotNull(outdoorAQI) ;
	}
	
	public void testParseHistoricalAQIData_invalidJson() {
		String data = "{\"p\":[{\"101270101\":{\"p1\"\",\"p2\":\"255\",\"p3\":\"403\",\"p4\":\"4\",\"p5\":\"25\",\"updatetime\":\"201407311706\"}}],\"api_version\":\"4.0\"}";
		List<OutdoorAQI> aqis = DataParser.parseHistoricalAQIData(data, "101270101");
		assertNull(aqis);
	}
//	{"p":
//		[
//		{"p1":"15","p2":"25","p3":"26","p4":"9","p5":"15","updatetime":"201407310705"},
//		{"p1":"14","p2":"28","p3":"30","p4":"9","p5":"15","updatetime":"201407310605"},
//		{"p1":"18","p2":"33","p3":"34","p4":"9","p5":"16","updatetime":"201407310507"},
//		{"p1":"21","p2":"41","p3":"39","p4":"29","p5":"20","updatetime":"201407310407"}]}
//	public void testParseHistoricalAQIData_validJson() {
//		String data = "{\"p\":[{\"p1\":\"205\",\"p2\":\"255\",\"p3\":\"403\",\"p4\":\"4\",\"p5\":\"25\",\"updatetime\":\"201407311706\"}}],\"api_version\":\"4.0\"}" ;
//		List<OutdoorAQI> aqis = DataParser.parseHistoricalAQIData(data, "101270101");
//		assertNotNull(aqis);
//	}
	
	private String fourDayForecastData = "{\"forecast4d\":{\"101270101\":"
			+ "{\"c\":{\"c1\":\"101270101\",\"c2\":\"chengdu\",\"c3\":\"成都\",\"c4\":\"chengdu\",\"c5\":\"成都\",\"c6\":\"sichuan\",\"c7\":\"四川\",\"c8\":\"china\",\"c9\":\"中国\",\"c10\":\"1\",\"c11\":\"028\",\"c12\":\"610000\",\"c13\":\"104.066541\",\"c14\":\"30.572269\",\"c15\":\"507\",\"c16\":\"AZ9280\",\"c17\":\"+8\"},"
			+ "\"f\":{\"f1\":["
			+ "{\"fa\":\"01\",\"fb\":\"03\",\"fc\":\"33\",\"fd\":\"24\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:18|20:01\"},"
			+ "{\"fa\":\"01\",\"fb\":\"03\",\"fc\":\"32\",\"fd\":\"23\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:19|20:00\"},"
			+ "{\"fa\":\"02\",\"fb\":\"08\",\"fc\":\"31\",\"fd\":\"23\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:20|20:00\"},"
			+ "{\"fa\":\"03\",\"fb\":\"08\",\"fc\":\"30\",\"fd\":\"22\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:20|19:59\"}],"
			+ "\"f0\":\"201407281100\"}}}}";
	
	public void testParseFourDayForecast_invalidJson() {
		String data = "{das:adsa}";
		List<ForecastWeatherDto> dtos = DataParser.parseFourDaysForecastData(data, "101270101");
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
		List<ForecastWeatherDto> dtos = DataParser.parseFourDaysForecastData(data, "101270101");
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
		List<ForecastWeatherDto> dtos = DataParser.parseFourDaysForecastData(data, "101270101");
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
		List<ForecastWeatherDto> dtos = DataParser.parseFourDaysForecastData(data, "101270101");
		assertEquals(dtos.get(0).getTemperatureDay(), "");
	}
}
