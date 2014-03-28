package com.philips.cl.di.dev.pa.test;
import java.util.List;

import com.philips.cl.di.common.ssdp.controller.BaseUrlParser;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;
import com.philips.cl.di.dev.pa.utils.DataParser;

import junit.framework.TestCase;

public class DataParserTest extends TestCase {
	
	public void testIndoorAQI_1() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertNotNull(new DataParser(parseData)) ;
	}

	public void testIndoorAQI_2() {
		String parseData = "No response" ;
		assertNull(new DataParser(parseData).parseAirPurifierEventData()) ;
	}
	
	public void testIndoorAQI_4() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertEquals("1",new DataParser(parseData).parseAirPurifierEventData().getPowerMode()) ;
	}
	
	public void testIndoorAQI_5() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(new DataParser(parseData).parseAirPurifierEventData().getMachineMode().equals("a")) ;
	}
	
	public void testIndoorAQI_6() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertFalse(new DataParser(parseData).parseAirPurifierEventData().getPowerMode().equals("0")) ;
	}
	
	public void testIndoorAQI_DCS_1() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertNotNull(new DataParser(parseData).parseAirPurifierEventDataFromCPP()) ;
	}
	
	public void testIndoorAQI_DCS_2() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"no_data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertNull(new DataParser(parseData).parseAirPurifierEventDataFromCPP()) ;
	}
	
	public void testIndoorAQI_DCS_3() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertEquals("a",new DataParser(parseData).parseAirPurifierEventDataFromCPP().getMachineMode()) ;
	}
	
	public void testIndoorAQI_DCS_4() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertTrue(new DataParser(parseData).parseAirPurifierEventDataFromCPP().getAqiL() == 1) ;
	}
	
	public void testIndoorAQI_DCS_5() {
		String parseData = 
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertFalse(new DataParser(parseData).parseAirPurifierEventDataFromCPP().getChildLock() == 1) ;
	}
	
	public void testIndoorAQIHistory_1() {
		String parseData = "No data" ;
		assertNull(new DataParser(parseData).parseHistoryData()) ;
	}
	
	public void testWeatherData_1() {
		String parseData = "No data" ;
		assertNull(new DataParser(parseData).parseWeatherData()) ;
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
}
