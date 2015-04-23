package com.philips.cl.di.dicomm.test;
import static org.mockito.Mockito.mock;

import com.philips.cl.di.dev.pa.datamodel.FirmwarePortProperties;
import com.philips.cl.di.dev.pa.datamodel.FirmwarePortProperties.FirmwareState;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.port.FirmwarePort;

public class FirmwarePortTest extends MockitoTestCase {

	public void testParseFirmwareEventNullData() {
		String parseData = null;
		assertNull(parseFirmwarePortData(parseData)) ;
	}

	public void testParseFirmwareEventEmptyData() {
		String parseData = "";
		assertNull(parseFirmwarePortData(parseData)) ;
	}

	public void testParseFirmwareEventProperData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

		FirmwarePortProperties result = parseFirmwarePortData(parseData);

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

		FirmwarePortProperties result = parseFirmwarePortData(parseData);
		assertNull(result);
	}

	public void testParseFirmwareEventNoUpgradeData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

		FirmwarePortProperties result = parseFirmwarePortData(parseData);
		assertEquals(result.getUpgrade(), "");
	}

	public void testParseFirmwareEventNoVersionData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"upgrade\":\"1.1\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

		FirmwarePortProperties result = parseFirmwarePortData(parseData);
		assertEquals(result.getVersion(), "");
	}

	public void testParseFirmwareEventValidStateData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"ready\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

		FirmwarePortProperties result = parseFirmwarePortData(parseData);
		assertEquals(result.getState(), FirmwareState.READY);
	}

	public void testParseFirmwareEventInValidStateData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

		FirmwarePortProperties result = parseFirmwarePortData(parseData);
		assertNull(result.getState());
	}

	public void testParseFirmwareEventTooBigProgressData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":150,\"statusmsg\":\"\",\"mandatory\":false}";

		FirmwarePortProperties result = parseFirmwarePortData(parseData);
		assertEquals(result.getProgress(), 100);
	}

	public void testParseFirmwareEventNegativeProgressData() {
		String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":-1,\"statusmsg\":\"\",\"mandatory\":false}";

		FirmwarePortProperties result = parseFirmwarePortData(parseData);
		assertEquals(result.getProgress(), 0);
	}

	public void testParseFirmwareEventWithAirportEvent() {
		String parseData = "{\"aqi\":\"3\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"0\",\"fs1\":\"33\",\"fs2\":\"881\",\"fs3\":\"2801\",\"fs4\":\"2801\",\"dtrs\":\"0\",\"aqit\":\"30\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"30414\",\"psens\":\"1\"}";

		FirmwarePortProperties result = parseFirmwarePortData(parseData);
		assertNull(result);
	}


	private FirmwarePortProperties parseFirmwarePortData(String parseData) {
		FirmwarePort firmwarePort = new FirmwarePort(null, mock(CommunicationStrategy.class));
		firmwarePort.processResponse(parseData);
		return firmwarePort.getPortProperties();
	}

}
