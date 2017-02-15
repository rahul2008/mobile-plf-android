/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.commlib.core.port.firmware;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.commlib.core.communication.CommunicationStrategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;

import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.IDLE;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.READY;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
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

    @Ignore
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

    // onProgressUpdated

    @Test
    public void whenGoingFromIdleStateToDownloadState_ProgressUpdateShouldBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "", "idle", 0, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 0, "", 100));

        verify(mockFirmwarePortListener, times(1)).onDownloadProgress(0);
    }

    @Test
    public void whenInDownloadingState_ProgressUpdateShoudBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 0, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 10, "", 100));

        verify(mockFirmwarePortListener).onDownloadProgress(0);
        verify(mockFirmwarePortListener).onDownloadProgress(10);
    }

    @Test
    public void whenInDownloadingState_ProgressUpdateShoudNotBeCalledWithSameProgress() {
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 12, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 12, "", 100));

        verify(mockFirmwarePortListener, times(1)).onDownloadProgress(12);
    }

    @Test
    public void whenInDownloadingState_ProgressUpdateShoudBeCalledWithCorrectProgress() {
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 2, "", 50));
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 5, "", 25));

        InOrder inOrder = inOrder(mockFirmwarePortListener);
        inOrder.verify(mockFirmwarePortListener).onDownloadProgress(4);
        inOrder.verify(mockFirmwarePortListener).onDownloadProgress(20);
    }

    @Test
    public void whenGoingFromDownloadingStateToCheckingState_ProgressUpdateShouldBeCalledWith100Percent() {
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 99, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "checking", 0, "", 100));

        verify(mockFirmwarePortListener).onDownloadProgress(100);
        verify(mockFirmwarePortListener).onCheckingProgress(0);
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
    public void whenGoingFromCheckingStateToReadyState_ProgressUpdateShouldBeCalledWith100Percent() {
        parseFirmwarePortData(createPropertiesJson("1", "", "checking", 99, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "ready", 0, "", 100));

        verify(mockFirmwarePortListener).onCheckingProgress(100);
    }

    @Test
    public void whenGoingFromDownloadingStateToReadyState_AndCheckingStateIsSkipped_ProgressUpdateShouldBeCalledWith100Percent() {
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 99, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "ready", 0, "", 100));

        //verify(mockFirmwarePortListener).onProgressUpdated(CHECKING, 100);
    }

    @Test
    public void whenGoingFromDownloadingStateToIdleState_AndCheckingStateAndReadyStateIsSkipped_ProgressUpdateShouldBeCalledWith100Percent() {
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 99, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "idle", 0, "", 100));

        //verify(mockFirmwarePortListener).onProgressUpdated(CHECKING, 100);
    }

    // onDownloadFailed

    @Test
    public void whenGoingFromPreparingStateToErrorState_DownloadFailedShouldBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "", "preparing", 2, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "error", 0, "Prepare failed", 100));

        ArgumentCaptor<FirmwarePortListener.FirmwarePortException> firmwarePortExceptionArgumentCaptor = ArgumentCaptor.forClass(FirmwarePortListener.FirmwarePortException.class);
        verify(mockFirmwarePortListener).onDownloadFailed(firmwarePortExceptionArgumentCaptor.capture());
        assertEquals("Prepare failed", firmwarePortExceptionArgumentCaptor.getValue().getMessage());
    }

    @Test
    public void whenGoingFromDownloadingStateToErrorState_DownloadFailedShouldBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 2, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "error", 0, "Error downloading", 100));

        ArgumentCaptor<FirmwarePortListener.FirmwarePortException> firmwarePortExceptionArgumentCaptor = ArgumentCaptor.forClass(FirmwarePortListener.FirmwarePortException.class);
        verify(mockFirmwarePortListener).onDownloadFailed(firmwarePortExceptionArgumentCaptor.capture());
        assertEquals("Error downloading", firmwarePortExceptionArgumentCaptor.getValue().getMessage());
    }

    // onDownloadFinished

    @Test
    public void whenGoingFromCheckingStateToReadyState_DownloadFinishedShouldBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "", "checking", 100, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "ready", 0, "", 100));

        verify(mockFirmwarePortListener).onDownloadFinished();
    }

    @Test
    public void whenGoingFromDownloadingStateToReadyState_AndCheckingStateIsSkipped_DownloadFinishedShouldBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 80, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "ready", 0, "", 100));

        verify(mockFirmwarePortListener).onDownloadFinished();
    }

    // onFirmwareAvailable

    @Test
    public void whenFirmwareIsAvailable_FirmwareAvailableShouldBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "2", "idle", 0, "", 0));

        verify(mockFirmwarePortListener).onFirmwareAvailable("2");
    }

    // onDeployFailed

    @Test
    public void whenGoingFromReadyStateToErrorStateWithBecauseOfCancel_DeployFailedShouldBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "2", "ready", 0, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "2", "error", 0, "Upgrade canceled", 100));

        ArgumentCaptor<FirmwarePortListener.FirmwarePortException> firmwarePortExceptionArgumentCaptor = ArgumentCaptor.forClass(FirmwarePortListener.FirmwarePortException.class);
        verify(mockFirmwarePortListener).onDeployFailed(firmwarePortExceptionArgumentCaptor.capture());
        assertEquals("Upgrade canceled", firmwarePortExceptionArgumentCaptor.getValue().getMessage());
    }

    @Test
    public void whenGoingFromReadyStateToErrorStateBecauseOfProgrammingError_DeployFailedShouldBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "2", "ready", 0, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "2", "error", 0, "Error programming", 100));

        ArgumentCaptor<FirmwarePortListener.FirmwarePortException> firmwarePortExceptionArgumentCaptor = ArgumentCaptor.forClass(FirmwarePortListener.FirmwarePortException.class);
        verify(mockFirmwarePortListener).onDeployFailed(firmwarePortExceptionArgumentCaptor.capture());
        assertEquals("Error programming", firmwarePortExceptionArgumentCaptor.getValue().getMessage());
    }

    // onDeployFinished

    @Test
    public void whenGoingFromReadyStateToErrorStateBecauseOfProgrammingError_DeployFinishedShouldBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "2", "ready", 0, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "2", "idle", 0, "", 0));

        verify(mockFirmwarePortListener).onDeployFinished();
    }

    @Test
    public void whenGoingFromDownloadingStateToIdleState_AndCheckingStateAndReadyStateIsSkipped_DeployFinishedShouldBeCalled() {
        parseFirmwarePortData(createPropertiesJson("1", "", "downloading", 99, "", 100));
        parseFirmwarePortData(createPropertiesJson("1", "", "idle", 0, "", 100));

        verify(mockFirmwarePortListener).onDeployFinished();
    }

    private String createPropertiesJson(String version, String upgrade, String state, int progress, String statusMsg, int size) {
        String propertiesJson = "{\"name\": \"test-firmware\", \"version\": \"" + version + "\", \"upgrade\": \"" + upgrade + "\", \"state\": \"" + state + "\", \"progress\": \"" + progress + "\", \"statusmsg\": \"" + statusMsg + "\", \"size\": \"" + size + "\"}";
        return propertiesJson;
    }
}
