/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.core.port.firmware;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;

import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.IDLE;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.READY;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwarePortTest extends RobolectricTest {
    private FirmwarePort firmwarePort;

    @Mock
    FirmwarePortListener mockFirmwarePortListener;

    @Captor
    ArgumentCaptor<Integer> progressCaptor;

    @Override
    @Before
    public void setUp() {
        initMocks(this);

        firmwarePort = new FirmwarePort(mock(CommunicationStrategy.class));
        firmwarePort.addFirmwarePortListener(mockFirmwarePortListener);
    }

    @Override
    @After
    public void tearDown() {
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
    @Ignore
    public void testParseFirmwareEventInValidStateData() {
        String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";

        FirmwarePortProperties result = parseFirmwarePortData(parseData);
        assertNull(result.getState());
    }

    @Test
    public void testParseFirmwareEventTooBigProgressData() {
        String parseData = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":150,\"statusmsg\":\"\",\"mandatory\":false}";

        FirmwarePortProperties result = parseFirmwarePortData(parseData);
        assertEquals(result.getProgress(), 150);
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
    public void whenInCheckingState_ProgressUpdateShoudBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "", "checking", 0, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "checking", 10, "", 100));

        verify(mockFirmwarePortListener).onCheckingProgress(0);
        verify(mockFirmwarePortListener).onCheckingProgress(10);
    }

    @Test
    public void whenInCheckingState_ProgressUpdateShoudNotBeCalledWithSameProgress() {
        parseFirmwarePortData(createPropertiesJson("1", "", "checking", 12, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "checking", 12, "", 100));

        verify(mockFirmwarePortListener, times(1)).onCheckingProgress(12);
    }

    @Test
    public void whenInCheckingState_ProgressUpdateShoudBeCalledWithCorrectProgress() {
        parseFirmwarePortData(createPropertiesJson("1", "", "checking", 2, "", 50));
        parseFirmwarePortData(createPropertiesJson("1", "", "checking", 5, "", 25));

        InOrder inOrder = inOrder(mockFirmwarePortListener);
        inOrder.verify(mockFirmwarePortListener).onCheckingProgress(4);
        inOrder.verify(mockFirmwarePortListener).onCheckingProgress(20);
    }

    @Test
    public void whenFirmwareIsAvailable_FirmwareAvailableShouldBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "2", "idle", 0, "", 0));

        verify(mockFirmwarePortListener).onFirmwareAvailable("2");
    }

    @Test
    public void givenDeviceHasNoCanUpgradePropertyThenUpgradeIsSupportedByDefault() {
        String json = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":150,\"statusmsg\":\"\",\"mandatory\":false}";
        firmwarePort.processResponse(json);

        assertTrue("canUpgrade() should return true by default if not supported by device.", firmwarePort.canUpgrade());
    }

    @Test
    public void givenDeviceHasCanUpgradePropertyAndUpgradeIsSupportedThenCanUpgradeValueIsTrue() {
        String json = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":150,\"statusmsg\":\"\",\"mandatory\":false,\"canupgrade\":true}";
        firmwarePort.processResponse(json);

        assertTrue("canUpgrade() should return true if supported by device and set to 'true'.", firmwarePort.canUpgrade());
    }

    @Test
    public void givenDeviceHasCanUpgradePropertyAndUpgradeIsNotSupportedThenCanUpgradeValueIsFalse() {
        String json = "{\"name\":\"HCN_DEVGEN\",\"version\":\"1.1\",\"upgrade\":\"1.2\",\"state\":\"wrong\",\"progress\":150,\"statusmsg\":\"\",\"mandatory\":false,\"canupgrade\":false}";
        firmwarePort.processResponse(json);

        assertFalse("canUpgrade() should return true if supported by device and set to 'false'.", firmwarePort.canUpgrade());
    }

    @Test
    public void givenFirmwarePortPropertiesNotLoadedBeforeThenCanUpgradeValueIsFalse() {
        assertFalse("canUpgrade() should return false if port properties not loaded yet.", firmwarePort.canUpgrade());
    }

    private String createPropertiesJson(String version, String upgrade, String state, int progress, String statusMsg, int size) {
        String propertiesJson = "{\"name\": \"test-firmware\", \"version\": \"" + version + "\", \"upgrade\": \"" + upgrade + "\", \"state\": \"" + state + "\", \"progress\": \"" + progress + "\", \"statusmsg\": \"" + statusMsg + "\", \"size\": \"" + size + "\"}";
        return propertiesJson;
    }
}
