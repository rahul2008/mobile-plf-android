/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.port.common.FirmwarePortProperties.FirmwareState;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class FirmwarePortTest extends RobolectricTest {

    @Test
    public void testParseFirmwareEventNullData() {
        String parseData = null;
        assertNull(parseFirmwarePortData(parseData));
    }

    @Test
    public void testParseFirmwareEventEmptyData() {
        String parseData = "";
        assertNull(parseFirmwarePortData(parseData));
    }

    @Test
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

    @Test
    public void testParseFirmwareEventIncompleteData() {
        String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgra";

        FirmwarePortProperties result = parseFirmwarePortData(parseData);
        assertNull(result);
    }

    @Test
    public void testParseFirmwareEventNoUpgradeData() {
        String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

        FirmwarePortProperties result = parseFirmwarePortData(parseData);
        assertEquals(result.getUpgrade(), "");
    }

    @Test
    public void testParseFirmwareEventNoVersionData() {
        String parseData = "{\"name\":\"HCN_DEVGEN\",\"upgrade\":\"1.1\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

        FirmwarePortProperties result = parseFirmwarePortData(parseData);
        assertNull(result);
    }

    @Test
    public void testParseFirmwareEventValidStateData() {
        String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"ready\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

        FirmwarePortProperties result = parseFirmwarePortData(parseData);
        assertEquals(result.getState(), FirmwareState.READY);
    }

    @Test
    public void testParseFirmwareEventInValidStateData() {
        String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

        FirmwarePortProperties result = parseFirmwarePortData(parseData);
        assertNull(result.getState());
    }

    @Test
    public void testParseFirmwareEventTooBigProgressData() {
        String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":150,\"statusmsg\":\"\",\"mandatory\":false}";

        FirmwarePortProperties result = parseFirmwarePortData(parseData);
        assertEquals(result.getProgress(), 100);
    }

    @Test
    public void testParseFirmwareEventNegativeProgressData() {
        String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":-1,\"statusmsg\":\"\",\"mandatory\":false}";

        FirmwarePortProperties result = parseFirmwarePortData(parseData);
        assertEquals(result.getProgress(), 0);
    }

    @Test
    public void testParseFirmwareEventWithAirportEvent() {
        String parseData = "{\"aqi\":\"3\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"0\",\"fs1\":\"33\",\"fs2\":\"881\",\"fs3\":\"2801\",\"fs4\":\"2801\",\"dtrs\":\"0\",\"aqit\":\"30\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"30414\",\"psens\":\"1\"}";

        FirmwarePortProperties result = parseFirmwarePortData(parseData);
        assertNull(result);
    }

    private FirmwarePortProperties parseFirmwarePortData(String parseData) {
        FirmwarePort firmwarePort = new FirmwarePort(mock(CommunicationStrategy.class));
        firmwarePort.processResponse(parseData);
        return firmwarePort.getPortProperties();
    }
}
