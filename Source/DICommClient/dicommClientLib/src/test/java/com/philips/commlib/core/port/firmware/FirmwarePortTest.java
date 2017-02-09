/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.commlib.core.port.firmware;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.commlib.core.communication.CommunicationStrategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.IDLE;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.READY;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwarePortTest extends RobolectricTest {
    private FirmwarePort firmwarePort;

    @Mock
    FirmwarePortListener mockFirmwarePortListener;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        initMocks(this);

        firmwarePort = new FirmwarePort(mock(CommunicationStrategy.class));
        firmwarePort.addFirmwarePortListener(mockFirmwarePortListener);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        firmwarePort.removeFirmwarePortListener(mockFirmwarePortListener);
    }

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
        assertEquals(result.getState(), IDLE);
        assertEquals(result.getProgress(), 0);
        assertEquals(result.getStatusMessage(), "");
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
        assertEquals(result.getState(), READY);
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
        firmwarePort.processResponse(parseData);
        return firmwarePort.getPortProperties();
    }

    @Test
    public void whenGoingFromIdleStateToDownloadState_ProgressUpdateShouldBeCalled() throws Exception {
        parseFirmwarePortData(createPropertiesJson("1", "", "idle", 0, "", 0));

        verify(mockFirmwarePortListener, never()).onProgressUpdated(any(FirmwarePortListener.FirmwarePortProgressType.class), eq(0));

        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 0, "", 0));

        verify(mockFirmwarePortListener).onProgressUpdated(any(FirmwarePortListener.FirmwarePortProgressType.class), eq(0));
    }

    private String createPropertiesJson(String version, String upgrade, String state, int progress, String statusMsg, int size) {
        String propertiesJson = "{\"name\": \"test-firmware\", \"version\": \"" + version + "\", \"upgrade\": \"" + upgrade + "\", \"state\": \"" + state + "\", \"progress\": \"" + progress + "\", \"statusMsg\": \"" + statusMsg + "\", \"size\": \"" + size + "\"}";
        return propertiesJson;
    }
}
