package com.philips.cl.di.dicomm.test;
import static org.mockito.Mockito.mock;

import com.philips.cl.di.dev.pa.datamodel.AirPortProperties;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.port.AirPort;

public class AirPortTest extends MockitoTestCase {

	public void testAirPurifierInvalidData() {
		String parseData = "invalid data" ;
		assertNull( parseAirPortData(parseData)) ;
	}

	public void testAirPurifierNullData() {
		String parseData = null ;
		assertNull(parseAirPortData(parseData)) ;
	}

	public void testAirPurifierInvalidJSON() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\"" ;
		assertNull(parseAirPortData(parseData)) ;
	}

	public void testAirPurifierPowerMode() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertEquals("1",parseAirPortData(parseData).getPowerMode()) ;
	}

	public void testAirPurifierMachineMode() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(parseAirPortData(parseData).getMachineMode().equals("a")) ;
	}

	public void testAirPurifierAQILight() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(parseAirPortData(parseData).getAqiL() == 1) ;
	}

	public void testAirPurifierChildLock() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(parseAirPortData(parseData).getChildLock() == 0) ;
	}

	public void testAirPurifierAQIValue() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(parseAirPortData(parseData).getIndoorAQI() == 1) ;
	}

	public void testAirPurifierFanSpeed() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(parseAirPortData(parseData).getActualFanSpeed().equals("1")) ;
	}

	public void testAirPurifierAQIThreshold() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertTrue(parseAirPortData(parseData).getAqiThreshold() == 500) ;
	}

	public void testAirPurifierPowerModeNotEquals() {
		String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\",\"psens\":\"1\"}" ;
		assertFalse(parseAirPortData(parseData).getPowerMode().equals("0")) ;
	}

	public void testAirPurifierFirmwareData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"ready\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

		AirPortProperties result = parseAirPortData(parseData);
		assertNull(result);
	}

	public void testAirPurifierFirmwareData2() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"ready\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

		AirPortProperties result = parseAirPortData(parseData);
		assertNull(result);
	}

	public void testIndoorAQI_DCS_1() {
		String parseData =
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertNotNull(parseAirPortData(parseData)) ;
	}

	public void testIndoorAQI_DCS_2() {
		String parseData =
				"{\"product\":\"1\",\"port\":\"air\",\"no_data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertNull(parseAirPortData(parseData)) ;
	}

	public void testIndoorAQI_DCS_3() {
		String parseData =
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertEquals("a",parseAirPortData(parseData).getMachineMode()) ;
	}

	public void testIndoorAQI_DCS_4() {
		String parseData =
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertTrue(parseAirPortData(parseData).getAqiL() == 1) ;
	}

	public void testIndoorAQI_DCS_5() {
		String parseData =
				"{\"product\":\"1\",\"port\":\"air\",\"data\":{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40227\",\"psens\":\"1\"}}" ;
		assertFalse(parseAirPortData(parseData).getChildLock() == 1) ;
	}

	private AirPortProperties parseAirPortData(String parseData) {
		AirPort airPort = new AirPort(null, mock(CommunicationStrategy.class));
		airPort.processResponse(parseData);
		return airPort.getPortProperties();
	}

}
