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
    public static final String CANCEL = "cancel";
    public static final String STATE = "state";
    public static final String IDLE = "idle";
    public static final String DOWNLOADING = "downloading";
    public static final String SIZE = "size";
    public static final String GO = "go";
    public static final String DATA = "data";
    public static final String PROGRESS = "progress";
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
    public void whenCreatedAndPortIsAvailableThenStateIsUpdated() throws Exception {
        when(diCommPortMock.isAvailable()).thenReturn(true);
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Downloading);

        CapabilityFirmwareUpdateDiComm capabilityFirmwareUpdateDiComm = new CapabilityFirmwareUpdateDiComm(diCommPortMock, diCommFirmwarePortStateWaiter);

        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenGetUploadedFirmwareInfoThenInfoIsReturned() throws Exception {
        when(diCommPortMock.isAvailable()).thenReturn(true);

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
        when(diCommPortMock.isAvailable()).thenReturn(true);
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Downloading);
        capabilityFirmwareUpdateDiComm.getUploadedFirmwareInfo(shnFirmwareInfoResultListener);

        ArgumentCaptor<SHNFirmwareInfo> shnFirmwareInfoArgumentCaptor = ArgumentCaptor.forClass(SHNFirmwareInfo.class);
        verify(shnFirmwareInfoResultListener).onActionCompleted(shnFirmwareInfoArgumentCaptor.capture(), eq(SHNResult.SHNOk));

        assertEquals(shnFirmwareInfoArgumentCaptor.getValue().getState(), SHNFirmwareInfo.SHNFirmwareState.Uploading);
    }

    @Test
    public void whenRemoteStateIsReadyThenFirmwareStateIsReadyToDeploy() throws Exception {
        when(diCommPortMock.isAvailable()).thenReturn(true);
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

    @Test
    public void whenUploadingThenCanNotStartAnotherUpload() throws Exception {
        capabilityFirmwareUpdateDiComm.uploadFirmware(firmwareData);

        capabilityFirmwareUpdateDiComm.uploadFirmware(firmwareData);

        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
        verify(diCommPortMock).reloadProperties(mapResultListenerArgumentCaptor.capture());
    }

    private void verifyUploadFailed(SHNResult shnErrorInvalidParameter, SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState state) {
        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFailed(capabilityFirmwareUpdateDiComm, shnErrorInvalidParameter);
        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, state);
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
        verify(diCommPortMock).unsubscribe(any(DiCommPort.UpdateListener.class), any(SHNResultListener.class));
    }

    @Test
    public void whenUploadIsCalledAndReloadOfPropertyHasFailedThenFailureIsReported() throws Exception {
        whenUploadIsCalledThenPropertiesAreReloaded();

        reset(shnCapabilityFirmwareUpdateListenerMock);
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verifyUploadFailed(SHNResult.SHNErrorConnectionLost, capabilityFirmwareUpdateDiComm.getState());
    }

    private void respondWithPortState(DiCommFirmwarePort.State state) throws Exception {
        reset(shnCapabilityFirmwareUpdateListenerMock);
        when(diCommPortMock.getState()).thenReturn(state);
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNOk);
    }

    @Test
    public void whenUploadIsCalledAndPortIsProgrammingThenFailureIsReported() throws Exception {
        whenUploadIsCalledThenPropertiesAreReloaded();

        respondWithPortState(DiCommFirmwarePort.State.Programming);

        verifyUploadFailed(SHNResult.SHNErrorProcedureAlreadyInProgress, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenUploadIsCalledAndPortIsErrorThenIdleIsSent() throws Exception {
        whenUploadIsCalledThenPropertiesAreReloaded();

        respondWithPortState(DiCommFirmwarePort.State.Error);

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(IDLE, mapArgumentCaptor.getValue().get(STATE));
    }

    @Test
    public void whenIdleHasFailedThenUploadHasFailed() throws Exception {
        whenUploadIsCalledAndPortIsErrorThenIdleIsSent();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorInvalidState);

        verifyUploadFailed(SHNResult.SHNErrorInvalidState, SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
    }

    @Test
    public void whenIdleHasSucceededThenUploadStarts() throws Exception {
        whenUploadIsCalledAndPortIsErrorThenIdleIsSent();

        reset(diCommPortMock);
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNOk);

        verify(diCommPortMock).subscribe(any(DiCommPort.UpdateListener.class), any(SHNResultListener.class));
    }

    @Test
    public void whenUploadIsCalledAndPortIsDownloadingThenCancelIsSent() throws Exception {
        whenUploadIsCalledThenPropertiesAreReloaded();

        respondWithPortState(DiCommFirmwarePort.State.Downloading);

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(CANCEL, mapArgumentCaptor.getValue().get(STATE));
    }

    @Test
    public void whenUploadIsCalledAndPortIsReadyThenCancelIsSent() throws Exception {
        whenUploadIsCalledThenPropertiesAreReloaded();

        respondWithPortState(DiCommFirmwarePort.State.Ready);

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(CANCEL, mapArgumentCaptor.getValue().get(STATE));
    }

    @Test
    public void whenCancelHasFailedThenUploadHasFailed() throws Exception {
        whenUploadIsCalledAndPortIsReadyThenCancelIsSent();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorInvalidState);

        verifyUploadFailed(SHNResult.SHNErrorInvalidState, SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
    }

    @Test
    public void whenUploadIsCalledAndPortIsReadyThenStartsWaitingForErrorState() throws Exception {
        whenUploadIsCalledThenPropertiesAreReloaded();

        respondWithPortState(DiCommFirmwarePort.State.Ready);

        verify(diCommFirmwarePortStateWaiter).waitUntilStateIsReached(eq(DiCommFirmwarePort.State.Error), waiterListenerArgumentCaptor.capture());
    }

    @Test
    public void whenTheExpectedStateIsReachedThenIdleIsSent() throws Exception {
        whenUploadIsCalledAndPortIsReadyThenStartsWaitingForErrorState();
        reset(diCommPortMock);

        waiterListenerArgumentCaptor.getValue().onRequestReceived(DiCommFirmwarePort.State.Error, SHNResult.SHNOk);

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(IDLE, mapArgumentCaptor.getValue().get(STATE));
    }

    @Test
    public void whenIdleWasNotSentSuccessfullyThenUploadHasFailed() throws Exception {
        whenTheExpectedStateIsReachedThenIdleIsSent();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verifyUploadFailed(SHNResult.SHNErrorConnectionLost, SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
    }

    @Test
    public void whenTheUnexpectedStateIsReachedThenIdleIsSent() throws Exception {
        whenUploadIsCalledAndPortIsReadyThenStartsWaitingForErrorState();
        reset(diCommPortMock);

        waiterListenerArgumentCaptor.getValue().onRequestReceived(DiCommFirmwarePort.State.Downloading, SHNResult.SHNErrorInvalidParameter);

        verifyUploadFailed(SHNResult.SHNErrorInvalidParameter, SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
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
        assertEquals(DOWNLOADING, mapArgumentCaptor.getValue().get(STATE));
        assertEquals(firmwareData.length, mapArgumentCaptor.getValue().get(SIZE));
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
        byte[] data = (byte[]) mapArgumentCaptor.getValue().get(DATA);
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
        properties.put(PROGRESS, progress);
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
        byte[] data = (byte[]) mapArgumentCaptor.getValue().get(DATA);
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
        properties.put(STATE, DOWNLOADING);
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
        verify(shnCapabilityFirmwareUpdateListenerMock).onProgressUpdate(capabilityFirmwareUpdateDiComm, 1.0f);
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
        whenCreatedThenPortListenerIsRegistered();
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Idle);

        listenerArgumentCaptor.getValue().onPortAvailable(diCommPortMock);

        verify(shnCapabilityFirmwareUpdateListenerMock, never()).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenPortBecomesAvailableWithPreparingStateThenStateIsUpdated() throws Exception {
        whenCreatedThenPortListenerIsRegistered();
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Preparing);

        listenerArgumentCaptor.getValue().onPortAvailable(diCommPortMock);

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenPortBecomesAvailableAgainThenStateIsNotUpdated() throws Exception {
        whenCreatedThenPortListenerIsRegistered();
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Preparing);

        listenerArgumentCaptor.getValue().onPortAvailable(diCommPortMock);
        listenerArgumentCaptor.getValue().onPortAvailable(diCommPortMock);

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenPortBecomesUnavailableWithCancellingThenStateIsUpdated() throws Exception {
        whenCreatedThenPortListenerIsRegistered();
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Canceling);

        listenerArgumentCaptor.getValue().onPortUnavailable(diCommPortMock);

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenStateOfThePortIsUpdatedThenListenerIsNotified() throws Exception {
        whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort();
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Canceling);

        updateListenerCaptor.getValue().onPropertiesChanged(new HashMap<String, Object>());

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenStateOfThePortIsUpdatedTwiceToExternalStateIdleThenListenerIsNotifiedOnce() throws Exception {
        whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort();

        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Downloading);
        updateListenerCaptor.getValue().onPropertiesChanged(new HashMap<String, Object>());

        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Canceling);
        updateListenerCaptor.getValue().onPropertiesChanged(new HashMap<String, Object>());

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenPortIsReadyAndDeployFirmwareIsCalledThenStateIsDeploying() throws Exception {
        whenCreatedThenPortListenerIsRegistered();

        when(diCommPortMock.getCanUpgrade()).thenReturn(true);
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Ready);
        capabilityFirmwareUpdateDiComm.deployFirmware();

        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateDeploying, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenPortIsNotReadyAndDeployFirmwareIsCalledThenStateIsDeploying() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Canceling);
        capabilityFirmwareUpdateDiComm.deployFirmware();

        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenPortCanNotUpgradeAndDeployFirmwareIsCalledThenStateIsDeploying() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Ready);
        when(diCommPortMock.getCanUpgrade()).thenReturn(false);
        capabilityFirmwareUpdateDiComm.deployFirmware();

        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenInStateDeployingThenStartWaitingForIdle() throws Exception {
        whenPortIsReadyAndDeployFirmwareIsCalledThenStateIsDeploying();

        verify(diCommFirmwarePortStateWaiter).waitUntilStateIsReached(eq(DiCommFirmwarePort.State.Idle), waiterListenerArgumentCaptor.capture());
    }

    @Test
    public void whenUnexpectedStateIsReachedThenDeployedHasFailed() throws Exception {
        whenInStateDeployingThenStartWaitingForIdle();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        waiterListenerArgumentCaptor.getValue().onRequestReceived(DiCommFirmwarePort.State.Error, SHNResult.SHNErrorInvalidParameter);

        verify(shnCapabilityFirmwareUpdateListenerMock).onDeployFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorInvalidParameter);
        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenExpectedStateIsReachedThenDeployedIsFinished() throws Exception {
        whenInStateDeployingThenStartWaitingForIdle();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        waiterListenerArgumentCaptor.getValue().onRequestReceived(DiCommFirmwarePort.State.Idle, SHNResult.SHNOk);

        verify(shnCapabilityFirmwareUpdateListenerMock).onDeployFinished(capabilityFirmwareUpdateDiComm, SHNResult.SHNOk);
        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenInStateDeployingThenGoIsSent() throws Exception {
        whenPortIsReadyAndDeployFirmwareIsCalledThenStateIsDeploying();

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(GO, mapArgumentCaptor.getValue().get(STATE));
    }

    @Test
    public void whenGoCommandWasNotSentSuccessfullyThenDeployIsFailed() throws Exception {
        whenInStateDeployingThenGoIsSent();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verify(shnCapabilityFirmwareUpdateListenerMock).onDeployFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorConnectionLost);
        assertEquals(SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenGoCommandWasNotSentSuccessfullyThenWaitSiCancelled() throws Exception {
        whenInStateDeployingThenGoIsSent();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verify(diCommFirmwarePortStateWaiter).cancel();
    }

    private void verifyCancelSent() {
        capabilityFirmwareUpdateDiComm.abortFirmwareUpload();

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(CANCEL, mapArgumentCaptor.getValue().get(STATE));
    }

    @Test
    public void whenPortIsPreparingAndDeployFirmwareIsCalledThenCancelIsSent() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Preparing);

        verifyCancelSent();
    }

    @Test
    public void whenPortIsDownloadingAndDeployFirmwareIsCalledThenCancelIsSent() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Downloading);

        verifyCancelSent();
    }

    @Test
    public void whenPortIsCheckingAndDeployFirmwareIsCalledThenCancelIsSent() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Checking);

        verifyCancelSent();
    }

    @Test
    public void whenPortIsReadyAndDeployFirmwareIsCalledThenCancelIsSent() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Ready);

        verifyCancelSent();
    }

    @Test
    public void whenPortIsIdleAndDeployFirmwareIsCalledThenCancelIsSent() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Idle);

        capabilityFirmwareUpdateDiComm.abortFirmwareUpload();

        verify(diCommPortMock, never()).putProperties(anyMap(), any(SHNMapResultListener.class));
    }

    @Test
    public void whenPortIsProgrammingAndDeployFirmwareIsCalledThenCancelIsSent() throws Exception {
        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Programming);

        capabilityFirmwareUpdateDiComm.abortFirmwareUpload();

        verify(diCommPortMock, never()).putProperties(anyMap(), any(SHNMapResultListener.class));
    }

    @Test
    public void whenInStateUploadingStateSwitchesToErrorThenUploadIsFailed() throws Exception {
        whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort();
        reset(diCommPortMock);

        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Error);
        updateListenerCaptor.getValue().onPropertiesChanged(new HashMap<String, Object>());

        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorInvalidState);
        verify(diCommPortMock).unsubscribe(any(DiCommPort.UpdateListener.class), any(SHNResultListener.class));
        verify(diCommFirmwarePortStateWaiter).cancel();
    }

    @Test
    public void whenInStateDeployingStateSwitchesToErrorThenDeployIsFailed() throws Exception {
        whenPortIsReadyAndDeployFirmwareIsCalledThenStateIsDeploying();
        reset(diCommPortMock);

        when(diCommPortMock.getState()).thenReturn(DiCommFirmwarePort.State.Error);
        listenerArgumentCaptor.getValue().onPortUnavailable(diCommPortMock);

        verify(shnCapabilityFirmwareUpdateListenerMock).onDeployFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorInvalidState);
        verify(diCommPortMock).unsubscribe(any(DiCommPort.UpdateListener.class), any(SHNResultListener.class));
        verify(diCommFirmwarePortStateWaiter).cancel();
    }

}