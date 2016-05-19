package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNFirmwareInfo;
import com.philips.pins.shinelib.SHNFirmwareInfoResultListener;
import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.dicommsupport.DiCommPort;
import com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CapabilityFirmwareUpdateDiCommTest {

    public static final int TEST_MAX_CHUNK_SIZE = 3;
    @Mock
    private DiCommFirmwarePort diCommPortMock;

    @Mock
    private DiCommFirmwarePortStateWaiter diCommFirmwarePortStateWaiter;

    @Mock
    private SHNCapabilityFirmwareUpdate.SHNCapabilityFirmwareUpdateListener shnCapabilityFirmwareUpdateListenerMock;

    @Mock
    private SHNFirmwareInfoResultListener shnFirmwareInfoResultListener;

    @Captor
    private ArgumentCaptor<DiCommPort.Listener> listenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<SHNMapResultListener<String, Object>> mapResultListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Map<String, Object>> mapArgumentCaptor;

    @Captor
    private ArgumentCaptor<DiCommPort.UpdateListener> updateListenerCaptor;

    @Captor
    private ArgumentCaptor<SHNResultListener> shnResultListenerCaptor;

    @Captor
    private ArgumentCaptor<DiCommFirmwarePortStateWaiter.Listener> waiterListenerArgumentCaptor;

    private CapabilityFirmwareUpdateDiComm capabilityFirmwareUpdateDiComm;

    private byte[] firmwareData = new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9};

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(diCommPortMock.isAvailable()).thenReturn(true);

        capabilityFirmwareUpdateDiComm = new CapabilityFirmwareUpdateDiComm(diCommPortMock, diCommFirmwarePortStateWaiter);

        capabilityFirmwareUpdateDiComm.setSHNCapabilityFirmwareUpdateListener(shnCapabilityFirmwareUpdateListenerMock);
    }

    @Test
    public void canCreate() throws Exception {
        new CapabilityFirmwareUpdateDiComm(diCommPortMock, diCommFirmwarePortStateWaiter);
    }

    @Test
    public void supportsUploadWithoutDeploy() throws Exception {
        assertTrue(capabilityFirmwareUpdateDiComm.supportsUploadWithoutDeploy());
    }

    @Test
    public void whenCreatedThenStateIsIdle() throws Exception {
        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenCreatedThenPortListenerIsRegistered() throws Exception {
        verify(diCommPortMock).setListener(listenerArgumentCaptor.capture());
    }

    @Test
    public void whenGetUploadedFirmwareInfoThenInfoIsReturned() throws Exception {
        String version = "version";
        when(diCommPortMock.getUploadedUpgradeVersion()).thenReturn(version);
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Idle);
        capabilityFirmwareUpdateDiComm.getUploadedFirmwareInfo(shnFirmwareInfoResultListener);

        ArgumentCaptor<SHNFirmwareInfo> shnFirmwareInfoArgumentCaptor = ArgumentCaptor.forClass(SHNFirmwareInfo.class);
        verify(shnFirmwareInfoResultListener).onActionCompleted(shnFirmwareInfoArgumentCaptor.capture(), eq(SHNResult.SHNOk));

        assertEquals(shnFirmwareInfoArgumentCaptor.getValue().getVersion(), version);
        assertEquals(shnFirmwareInfoArgumentCaptor.getValue().getState(), SHNFirmwareInfo.SHNFirmwareState.Idle);
    }

    @Test
    public void whenGetUploadedFirmwareInfoWhileUnavailableThenFailIsReported() throws Exception {
        when(diCommPortMock.isAvailable()).thenReturn(false);
        capabilityFirmwareUpdateDiComm.getUploadedFirmwareInfo(shnFirmwareInfoResultListener);

        verify(shnFirmwareInfoResultListener).onActionCompleted(null, SHNResult.SHNErrorServiceUnavailable);
    }

    @Test
    public void whenRemoteStateIsDownloadingThenFirmwareStateIsUploading() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Downloading);
        capabilityFirmwareUpdateDiComm.getUploadedFirmwareInfo(shnFirmwareInfoResultListener);

        ArgumentCaptor<SHNFirmwareInfo> shnFirmwareInfoArgumentCaptor = ArgumentCaptor.forClass(SHNFirmwareInfo.class);
        verify(shnFirmwareInfoResultListener).onActionCompleted(shnFirmwareInfoArgumentCaptor.capture(), eq(SHNResult.SHNOk));

        assertEquals(shnFirmwareInfoArgumentCaptor.getValue().getState(), SHNFirmwareInfo.SHNFirmwareState.Uploading);
    }

    @Test
    public void whenRemoteStateIsReadyThenFirmwareStateIsReadyToDeploy() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Ready);
        capabilityFirmwareUpdateDiComm.getUploadedFirmwareInfo(shnFirmwareInfoResultListener);

        ArgumentCaptor<SHNFirmwareInfo> shnFirmwareInfoArgumentCaptor = ArgumentCaptor.forClass(SHNFirmwareInfo.class);
        verify(shnFirmwareInfoResultListener).onActionCompleted(shnFirmwareInfoArgumentCaptor.capture(), eq(SHNResult.SHNOk));

        assertEquals(shnFirmwareInfoArgumentCaptor.getValue().getState(), SHNFirmwareInfo.SHNFirmwareState.ReadyToDeploy);
    }

    @Test
    public void whenUploadFirmwareIsCalledThenStateIsUploading() throws Exception {
        capabilityFirmwareUpdateDiComm.uploadFirmware(firmwareData);

        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenUploadFirmwareIsCalledWithNullDataThenStateIsUploading() throws Exception {
        capabilityFirmwareUpdateDiComm.uploadFirmware(null);

        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorInvalidParameter);
    }

    @Test
    public void whenUploadFirmwareIsCalledWithIEmptyDataThenStateIsUploading() throws Exception {
        capabilityFirmwareUpdateDiComm.uploadFirmware(new byte[]{});

        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorInvalidParameter);
    }

    @Test
    public void whenUploadIsCalledThenPropertiesAreReloaded() throws Exception {
        whenUploadFirmwareIsCalledThenStateIsUploading();

        verify(diCommPortMock).reloadProperties(mapResultListenerArgumentCaptor.capture());
    }

    private void verifyUploadFailed(SHNResult shnErrorInvalidParameter, SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState state) {
        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFailed(capabilityFirmwareUpdateDiComm, shnErrorInvalidParameter);
        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, state);
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenUploadIsCalledAndReloadOfPropertyHasFailedThenFailureIsReported() throws Exception {
        whenUploadIsCalledThenPropertiesAreReloaded();

        reset(shnCapabilityFirmwareUpdateListenerMock);
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verifyUploadFailed(SHNResult.SHNErrorConnectionLost, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenUploadIsCalledAndPortIsNotIdleThenFailureIsReported() throws Exception {
        whenUploadIsCalledThenPropertiesAreReloaded();

        reset(shnCapabilityFirmwareUpdateListenerMock);
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Downloading);
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNOk);

        verifyUploadFailed(SHNResult.SHNErrorInvalidState, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort() throws Exception {
        whenUploadIsCalledThenPropertiesAreReloaded();

        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Idle);
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNOk);

        verify(diCommPortMock).subscribe(updateListenerCaptor.capture(), shnResultListenerCaptor.capture());
    }

    @Test
    public void whenSubscriptionIsFailedThenUploadIsFailed() throws Exception {
        whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort();

        reset(shnCapabilityFirmwareUpdateListenerMock);
        shnResultListenerCaptor.getValue().onActionCompleted(SHNResult.SHNErrorConnectionLost);

        verifyUploadFailed(SHNResult.SHNErrorConnectionLost, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenSubscriptionIsSuccessfulThenDownloadingIsStarted() throws Exception {
        whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort();

        shnResultListenerCaptor.getValue().onActionCompleted(SHNResult.SHNOk);

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(2, mapArgumentCaptor.getValue().size());
        assertEquals("downloading", mapArgumentCaptor.getValue().get("state"));
        assertEquals(firmwareData.length, mapArgumentCaptor.getValue().get("size"));
    }

    @Test
    public void whenSubscriptionIsSuccessfulThenStartsWaitingForDownloading() throws Exception {
        whenSubscriptionIsSuccessfulThenDownloadingIsStarted();

        verify(diCommFirmwarePortStateWaiter).waitUntilStateIsReached(eq(DiCommFirmwarePort.State.Downloading), waiterListenerArgumentCaptor.capture());
    }

    @Test
    public void whenPutPropertiesFailsThenUploadIsFailed() throws Exception {
        whenSubscriptionIsSuccessfulThenDownloadingIsStarted();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorConnectionLost);
        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(diCommFirmwarePortStateWaiter).cancel();
    }

    private void verifyChunkWritten(int progress) {
        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        byte[] data = (byte[]) mapArgumentCaptor.getValue().get("data");
        assertEquals(TEST_MAX_CHUNK_SIZE, data.length);

        assertEquals(firmwareData[progress], data[0]);
        assertEquals(firmwareData[progress + 1], data[1]);
        assertEquals(firmwareData[progress + 2], data[2]);
        float progressIndicator = (float) progress / firmwareData.length;
        verify(shnCapabilityFirmwareUpdateListenerMock).onProgressUpdate(capabilityFirmwareUpdateDiComm, progressIndicator);
    }

    @Test
    public void whenStateSwitchesToDownloadingThenFirstChunkIsWritten() throws Exception {
        whenSubscriptionIsSuccessfulThenStartsWaitingForDownloading();
        reset(diCommPortMock);

        when(diCommPortMock.getMaxChunkSize()).thenReturn(TEST_MAX_CHUNK_SIZE);
        waiterListenerArgumentCaptor.getValue().onRequestReceived(DiCommFirmwarePort.State.Downloading, SHNResult.SHNOk);

        verifyChunkWritten(0);
    }

    @Test
    public void whenStateSwitchesToUnexpectedStateThenUploadIsFailed() throws Exception {
        whenSubscriptionIsSuccessfulThenStartsWaitingForDownloading();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        waiterListenerArgumentCaptor.getValue().onRequestReceived(DiCommFirmwarePort.State.Error, SHNResult.SHNErrorInvalidState);

        verifyUploadFailed(SHNResult.SHNErrorInvalidState, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenMaxChunkSizeIsInvalidThenUploadIsFailed() throws Exception {
        whenSubscriptionIsSuccessfulThenStartsWaitingForDownloading();
        reset(diCommPortMock, shnCapabilityFirmwareUpdateListenerMock);

        when(diCommPortMock.getMaxChunkSize()).thenReturn(Integer.MAX_VALUE);
        waiterListenerArgumentCaptor.getValue().onRequestReceived(DiCommFirmwarePort.State.Downloading, SHNResult.SHNOk);

        verifyUploadFailed(SHNResult.SHNErrorInvalidParameter, capabilityFirmwareUpdateDiComm.getState());
    }

    private void sendChunk(int progress, SHNResult result) {
        reset(shnCapabilityFirmwareUpdateListenerMock, diCommPortMock);
        when(diCommPortMock.getMaxChunkSize()).thenReturn(TEST_MAX_CHUNK_SIZE);
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Downloading);

        Map<String, Object> properties = new HashMap<>();
        properties.put("progress", progress);
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, result);
    }

    @Test
    public void whenFirstChunkIsWrittenSuccessfullyThenSecondChunkIsWritten() throws Exception {
        whenStateSwitchesToDownloadingThenFirstChunkIsWritten();

        sendChunk(TEST_MAX_CHUNK_SIZE, SHNResult.SHNOk);

        verifyChunkWritten(TEST_MAX_CHUNK_SIZE);
    }

    @Test
    public void whenFirstChunkIsNotWrittenThenUploadHasFailed() throws Exception {
        whenStateSwitchesToDownloadingThenFirstChunkIsWritten();

        sendChunk(TEST_MAX_CHUNK_SIZE, SHNResult.SHNErrorConnectionLost);

        verifyUploadFailed(SHNResult.SHNErrorConnectionLost, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenSecondChunkIsWrittenSuccessfullyThenThirdChunkIsWritten() throws Exception {
        whenFirstChunkIsWrittenSuccessfullyThenSecondChunkIsWritten();

        sendChunk(TEST_MAX_CHUNK_SIZE * 2, SHNResult.SHNOk);

        verifyChunkWritten(TEST_MAX_CHUNK_SIZE * 2);
    }

    @Test
    public void whenThirdChunkIsWrittenSuccessfullyThenLastChunkIsWritten() throws Exception {
        whenSecondChunkIsWrittenSuccessfullyThenThirdChunkIsWritten();

        sendChunk(TEST_MAX_CHUNK_SIZE * 3, SHNResult.SHNOk);

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        byte[] data = (byte[]) mapArgumentCaptor.getValue().get("data");
        assertEquals(1, data.length);

        assertEquals(firmwareData[firmwareData.length - 1], data[0]);
    }

    @Test
    public void whenProgressIsNotReportedThenUploadIsFailed() throws Exception {
        whenStateSwitchesToDownloadingThenFirstChunkIsWritten();

        reset(shnCapabilityFirmwareUpdateListenerMock, diCommPortMock);
        when(diCommPortMock.getMaxChunkSize()).thenReturn(TEST_MAX_CHUNK_SIZE);
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Downloading);

        Map<String, Object> properties = new HashMap<>();
        properties.put("state", "downloading");
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        verifyUploadFailed(SHNResult.SHNErrorInvalidParameter, capabilityFirmwareUpdateDiComm.getState());
    }

    private void sendResponseWithState(DiCommFirmwarePort.State state, int progress) {
        reset(shnCapabilityFirmwareUpdateListenerMock, diCommPortMock);
        when(diCommPortMock.getMaxChunkSize()).thenReturn(TEST_MAX_CHUNK_SIZE);
        when(diCommPortMock.getState()).thenReturn(state);

        Map<String, Object> properties = new HashMap<>();
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);
    }

    @Test
    public void whenAChunkIsWrittenWithNotDownloadingStateThenNxtChunkIsNotWritten() throws Exception {
        whenStateSwitchesToDownloadingThenFirstChunkIsWritten();

        sendResponseWithState(DiCommFirmwarePort.State.Error, 0);

        verify(diCommPortMock, never()).putProperties(anyMap(), any(SHNMapResultListener.class));
    }

    @Test
    public void whenStateSwitchesToCheckingThenNextChunkIsNotWritten() throws Exception {
        whenThirdChunkIsWrittenSuccessfullyThenLastChunkIsWritten();

        sendResponseWithState(DiCommFirmwarePort.State.Checking, firmwareData.length);

        verify(diCommPortMock, never()).putProperties(anyMap(), any(SHNMapResultListener.class));
        verify(shnCapabilityFirmwareUpdateListenerMock).onProgressUpdate(capabilityFirmwareUpdateDiComm, 1.0f);
    }

    @Test
    public void whenStateSwitchesToDownloadingThenStartsWaitingForReady() throws Exception {
        whenSubscriptionIsSuccessfulThenStartsWaitingForDownloading();
        reset(diCommPortMock);
        when(diCommPortMock.getMaxChunkSize()).thenReturn(TEST_MAX_CHUNK_SIZE);

        waiterListenerArgumentCaptor.getValue().onRequestReceived(DiCommFirmwarePort.State.Downloading, SHNResult.SHNOk);

        verify(diCommFirmwarePortStateWaiter).waitUntilStateIsReached(eq(DiCommFirmwarePort.State.Ready), waiterListenerArgumentCaptor.capture());
    }

    @Test
    public void whenStateSwitchesToReadyThenUploadIsFinished() throws Exception {
        whenStateSwitchesToDownloadingThenStartsWaitingForReady();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        waiterListenerArgumentCaptor.getValue().onRequestReceived(DiCommFirmwarePort.State.Ready, SHNResult.SHNOk);

        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFinished(capabilityFirmwareUpdateDiComm);
        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
        verify(diCommPortMock).unsubscribe(any(DiCommPort.UpdateListener.class), any(SHNResultListener.class));
    }

    @Test
    public void whenStateSwitchesToUnexpectedStateWhileWaitingForReadyThenUploadIsFailed() throws Exception {
        whenStateSwitchesToDownloadingThenStartsWaitingForReady();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        waiterListenerArgumentCaptor.getValue().onRequestReceived(DiCommFirmwarePort.State.Error, SHNResult.SHNErrorConnectionLost);

        verifyUploadFailed(SHNResult.SHNErrorConnectionLost, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenPortBecomesAvailableWithIdleStateThenStateIsNotUpdated() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Idle);
        capabilityFirmwareUpdateDiComm.onPortAvailable(diCommPortMock);

        verify(shnCapabilityFirmwareUpdateListenerMock, never()).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenPortBecomesAvailableWithPreparingStateThenStateIsUpdated() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Preparing);
        capabilityFirmwareUpdateDiComm.onPortAvailable(diCommPortMock);

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenPortBecomesAvailableAgainThenStateIsNotUpdated() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Preparing);

        capabilityFirmwareUpdateDiComm.onPortAvailable(diCommPortMock);
        capabilityFirmwareUpdateDiComm.onPortAvailable(diCommPortMock);

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenPortBecomesUnavailableWithCancellingThenStateIsUpdated() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Canceling);

        capabilityFirmwareUpdateDiComm.onPortUnavailable(diCommPortMock);

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenStateOfThePortIsUpdatedThenListenerIsNotified() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Canceling);

        capabilityFirmwareUpdateDiComm.onPropertiesChanged(new HashMap<String, Object>());

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenStateOfThePortIsUpdatedTwiceToExternalStateIdleThenListenerIsNotifiedOnce() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Downloading);
        capabilityFirmwareUpdateDiComm.onPropertiesChanged(new HashMap<String, Object>());

        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Canceling);
        capabilityFirmwareUpdateDiComm.onPropertiesChanged(new HashMap<String, Object>());

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }
}