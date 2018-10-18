/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNFirmwareInfo;
import com.philips.pins.shinelib.SHNFirmwareInfoResultListener;
import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState;
import com.philips.pins.shinelib.dicommsupport.DiCommPort;
import com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.philips.pins.shinelib.SHNResult.SHNErrorInvalidState;
import static com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateDeploying;
import static com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle;
import static com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate.SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading;
import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.State;
import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.getMaxChunkSize;
import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.getStateFromProps;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CapabilityFirmwareUpdateDiCommTest {

    private static final int TEST_MAX_CHUNK_SIZE = 3;
    private static final String CANCEL = "cancel";
    private static final String STATE = "state";
    private static final String IDLE = "idle";
    private static final String DOWNLOADING = "downloading";
    private static final String SIZE = "size";
    private static final String GO = "go";
    private static final String DATA = "data";
    private static final String PROGRESS = "progress";
    private static final String MAX_CHUNK_SIZE = "maxchunksize";

    @Mock
    private DiCommFirmwarePort diCommPortMock;

    @Mock
    private DiCommFirmwarePortStateWaiter diCommFirmwarePortStateWaiterMock;

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

    private CapabilityFirmwareUpdateDiCommForTest capabilityFirmwareUpdateDiComm;

    private byte[] firmwareData = new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9};

    private Map<String, Object> latestReceivedProperties = new HashMap<>();

    @Before
    public void setUp() {
        initMocks(this);

        when(diCommPortMock.getProperties()).thenReturn(latestReceivedProperties);
        when(diCommPortMock.getState()).thenReturn(getStateFromProps(latestReceivedProperties));
        when(diCommPortMock.getMaxChunkSize()).thenReturn(getMaxChunkSize(latestReceivedProperties));

        capabilityFirmwareUpdateDiComm = new CapabilityFirmwareUpdateDiCommForTest(diCommPortMock, null);

        capabilityFirmwareUpdateDiComm.setSHNCapabilityFirmwareUpdateListener(shnCapabilityFirmwareUpdateListenerMock);
    }

    @After
    public void tearDown() throws Exception {
        latestReceivedProperties.clear();
    }

    @Test
    public void canCreate() {
        new CapabilityFirmwareUpdateDiCommForTest(diCommPortMock, null);
    }

    @Test
    public void supportsUploadWithoutDeploy() {
        assertTrue(capabilityFirmwareUpdateDiComm.supportsUploadWithoutDeploy());
    }

    @Test
    public void whenCreatedThenStateIsIdle() {
        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenCreatedThenPortListenerIsRegistered() {
        verify(diCommPortMock).setListener(listenerArgumentCaptor.capture());
    }

    @Test
    public void whenGetUploadedFirmwareInfoThenInfoIsReturned() {
        when(diCommPortMock.isAvailable()).thenReturn(true);

        String version = "version";
        when(diCommPortMock.getUploadedUpgradeVersion()).thenReturn(version);
        when(diCommPortMock.getState()).thenReturn(State.Idle);
        capabilityFirmwareUpdateDiComm.getUploadedFirmwareInfo(shnFirmwareInfoResultListener);

        ArgumentCaptor<SHNFirmwareInfo> shnFirmwareInfoArgumentCaptor = ArgumentCaptor.forClass(SHNFirmwareInfo.class);
        verify(shnFirmwareInfoResultListener).onActionCompleted(shnFirmwareInfoArgumentCaptor.capture(), eq(SHNResult.SHNOk));

        assertEquals(shnFirmwareInfoArgumentCaptor.getValue().getVersion(), version);
        assertEquals(shnFirmwareInfoArgumentCaptor.getValue().getState(), SHNFirmwareInfo.SHNFirmwareState.Idle);
    }

    @Test
    public void whenGetUploadedFirmwareInfoWhileUnavailableThenFailIsReported() {
        when(diCommPortMock.isAvailable()).thenReturn(false);
        capabilityFirmwareUpdateDiComm.getUploadedFirmwareInfo(shnFirmwareInfoResultListener);

        verify(shnFirmwareInfoResultListener).onActionCompleted(null, SHNResult.SHNErrorServiceUnavailable);
    }

    @Test
    public void whenRemoteStateIsDownloadingThenFirmwareStateIsUploading() {
        when(diCommPortMock.isAvailable()).thenReturn(true);
        when(diCommPortMock.getState()).thenReturn(State.Downloading);
        capabilityFirmwareUpdateDiComm.getUploadedFirmwareInfo(shnFirmwareInfoResultListener);

        ArgumentCaptor<SHNFirmwareInfo> shnFirmwareInfoArgumentCaptor = ArgumentCaptor.forClass(SHNFirmwareInfo.class);
        verify(shnFirmwareInfoResultListener).onActionCompleted(shnFirmwareInfoArgumentCaptor.capture(), eq(SHNResult.SHNOk));

        assertEquals(shnFirmwareInfoArgumentCaptor.getValue().getState(), SHNFirmwareInfo.SHNFirmwareState.Uploading);
    }

    @Test
    public void whenRemoteStateIsReadyThenFirmwareStateIsReadyToDeploy() {
        when(diCommPortMock.isAvailable()).thenReturn(true);
        when(diCommPortMock.getState()).thenReturn(State.Ready);
        capabilityFirmwareUpdateDiComm.getUploadedFirmwareInfo(shnFirmwareInfoResultListener);

        ArgumentCaptor<SHNFirmwareInfo> shnFirmwareInfoArgumentCaptor = ArgumentCaptor.forClass(SHNFirmwareInfo.class);
        verify(shnFirmwareInfoResultListener).onActionCompleted(shnFirmwareInfoArgumentCaptor.capture(), eq(SHNResult.SHNOk));

        assertEquals(shnFirmwareInfoArgumentCaptor.getValue().getState(), SHNFirmwareInfo.SHNFirmwareState.ReadyToDeploy);
    }

    @Test
    public void whenUploadFirmwareIsCalledThenStateIsUploading() {
        capabilityFirmwareUpdateDiComm.uploadFirmware(firmwareData, false);

        assertEquals(SHNFirmwareUpdateStateUploading, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenUploadFirmwareIsCalledWithNullDataThenListenerIsNotifiedOfFailureAndStateIsIdle() {
        capabilityFirmwareUpdateDiComm.uploadFirmware(null, false);

        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorInvalidParameter);
    }

    @Test
    public void whenUploadFirmwareIsCalledWithEmptyDataThenListenerIsNotifiedOfFailureAndStateIsIdle() {
        capabilityFirmwareUpdateDiComm.uploadFirmware(new byte[]{}, false);

        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorInvalidParameter);
    }

    @Test
    public void whenUploadIsCalledWithoutResumeThenCurrentValuesAreFetchedFromDevice() {
        capabilityFirmwareUpdateDiComm.uploadFirmware(firmwareData, false);

        verify(diCommPortMock).reloadProperties(mapResultListenerArgumentCaptor.capture());
    }

    @Test
    public void givenStateIsUploading_whenUploadIsRequestedAgain_thenUploadIsNotInterrupted() {
        capabilityFirmwareUpdateDiComm.uploadFirmware(firmwareData, false);
        assertEquals(SHNFirmwareUpdateStateUploading, capabilityFirmwareUpdateDiComm.getState());

        final byte[] newBytes = "bananen zijn gezond".getBytes();
        capabilityFirmwareUpdateDiComm.uploadFirmware(newBytes, false);

        assertEquals(SHNFirmwareUpdateStateUploading, capabilityFirmwareUpdateDiComm.getState());
        verify(diCommPortMock, times(1)).reloadProperties(mapResultListenerArgumentCaptor.capture());
        assertNotEquals(capabilityFirmwareUpdateDiComm.getFirmwareData(), newBytes);
    }

    @Test
    public void whenUploadIsCalledAndFetchOfCurrentValuesFromDeviceHasFailedThenFailureIsReported() {
        whenUploadIsCalledWithoutResumeThenCurrentValuesAreFetchedFromDevice();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNResult.SHNErrorConnectionLost, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenUploadIsCalledAndPortIsErrorThenIdleIsSent() {
        whenUploadIsCalledWithoutResumeThenCurrentValuesAreFetchedFromDevice();

        respondWith(State.Error);

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(IDLE, mapArgumentCaptor.getValue().get(STATE));
    }

    @Test
    public void givenPortIsInErrorStateWhileUploadIsStarted_whenSettingItToIdleFails_thenUploadFails() {
        whenUploadIsCalledAndPortIsErrorThenIdleIsSent();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNErrorInvalidState);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNErrorInvalidState, SHNFirmwareUpdateStateIdle);
    }

    @Test
    public void whenIdleHasSucceededThenUploadStarts() {
        whenUploadIsCalledAndPortIsErrorThenIdleIsSent();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(latestReceivedProperties, SHNResult.SHNOk);

        verify(diCommPortMock).subscribe(any(DiCommPort.UpdateListener.class), any(SHNResultListener.class));
    }

    @Test
    public void givenPortIsDownloading_whenUploadIsCalledWithoutResume_thenCancelIsSent() {
        whenUploadIsCalledWithoutResumeThenCurrentValuesAreFetchedFromDevice();

        respondWith(State.Downloading, 3);

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(CANCEL, mapArgumentCaptor.getValue().get(STATE));
    }

    @Test
    public void whenUploadIsCalledAndPortIsReadyThenCancelIsSentAndStateWaiterWaitsForError() {
        whenUploadIsCalledWithoutResumeThenCurrentValuesAreFetchedFromDevice();

        respondWith(State.Ready);

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(CANCEL, mapArgumentCaptor.getValue().get(STATE));

        verify(diCommFirmwarePortStateWaiterMock).waitUntilStateIsReached(eq(State.Error), waiterListenerArgumentCaptor.capture());
    }

    @Test
    public void whenCancelHasFailedThenUploadHasFailed() {
        whenUploadIsCalledAndPortIsReadyThenCancelIsSentAndStateWaiterWaitsForError();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNErrorInvalidState);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNErrorInvalidState, SHNFirmwareUpdateStateIdle);
    }

    @Test
    public void givenErrorStateIsExpectedToBeReached_whenItIsReached_thenIdleIsSent() {
        whenUploadIsCalledAndPortIsReadyThenCancelIsSentAndStateWaiterWaitsForError();

        waiterListenerArgumentCaptor.getValue().onStateUpdated(State.Error, SHNResult.SHNOk);

        verify(diCommPortMock, atLeastOnce()).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(IDLE, mapArgumentCaptor.getValue().get(STATE));
    }

    @Test
    public void whenIdleWasNotSentSuccessfullyThenUploadHasFailed() {
        givenErrorStateIsExpectedToBeReached_whenItIsReached_thenIdleIsSent();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNResult.SHNErrorConnectionLost, SHNFirmwareUpdateStateIdle);
    }

    @Test
    public void givenStateIsReadyAndCancelIsSent_whenAnUnexpectedStateIsReached_thenIdleIsSent() {
        whenUploadIsCalledAndPortIsReadyThenCancelIsSentAndStateWaiterWaitsForError();

        waiterListenerArgumentCaptor.getValue().onStateUpdated(State.Downloading, SHNResult.SHNOk);

        verify(diCommPortMock, atLeastOnce()).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertThat(getStateFromProps(mapArgumentCaptor.getValue())).isEqualTo(State.Idle);
    }

    @Test
    public void whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort() {
        whenUploadIsCalledWithoutResumeThenCurrentValuesAreFetchedFromDevice();

        respondWith(State.Idle);

        verify(diCommPortMock).subscribe(updateListenerCaptor.capture(), shnResultListenerCaptor.capture());
    }

    @Test
    public void whenSubscriptionIsFailedThenUploadIsFailed() {
        whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort();

        reset(shnCapabilityFirmwareUpdateListenerMock);
        shnResultListenerCaptor.getValue().onActionCompleted(SHNResult.SHNErrorConnectionLost);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNResult.SHNErrorConnectionLost, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenSubscriptionIsSuccessfulThenDownloadingIsStarted() {
        whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort();

        shnResultListenerCaptor.getValue().onActionCompleted(SHNResult.SHNOk);

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(2, mapArgumentCaptor.getValue().size());
        assertEquals(DOWNLOADING, mapArgumentCaptor.getValue().get(STATE));
        assertEquals(firmwareData.length, mapArgumentCaptor.getValue().get(SIZE));
    }

    @Test
    public void whenSubscriptionIsSuccessfulThenStartsWaitingForDownloading() {
        whenSubscriptionIsSuccessfulThenDownloadingIsStarted();

        verify(diCommFirmwarePortStateWaiterMock).waitUntilStateIsReached(eq(State.Downloading), waiterListenerArgumentCaptor.capture());
    }

    @Test
    public void whenPutPropertiesFailsThenUploadIsFailed() {
        whenSubscriptionIsSuccessfulThenDownloadingIsStarted();

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorConnectionLost);
        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(diCommFirmwarePortStateWaiterMock).cancel();
    }

    @Test
    public void whenStateSwitchesToDownloadingThenFirstChunkIsWritten() {
        whenSubscriptionIsSuccessfulThenStartsWaitingForDownloading();

        waiterListenerArgumentCaptor.getValue().onStateUpdated(State.Downloading, SHNResult.SHNOk);

        verifyChunkWritten(0);
    }

    @Test
    public void whenStateSwitchesToUnexpectedStateThenUploadIsFailed() {
        whenSubscriptionIsSuccessfulThenStartsWaitingForDownloading();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        waiterListenerArgumentCaptor.getValue().onStateUpdated(State.Error, SHNErrorInvalidState);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNErrorInvalidState, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenMaxChunkSizeIsInvalidThenUploadIsFailed() {
        whenSubscriptionIsSuccessfulThenStartsWaitingForDownloading();
        reset(diCommPortMock, shnCapabilityFirmwareUpdateListenerMock);

        when(diCommPortMock.getMaxChunkSize()).thenReturn(Integer.MAX_VALUE);
        waiterListenerArgumentCaptor.getValue().onStateUpdated(State.Downloading, SHNResult.SHNOk);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNResult.SHNErrorInvalidParameter, capabilityFirmwareUpdateDiComm.getState());
    }

    private void sendChunk(Object progress, SHNResult result) {
        reset(shnCapabilityFirmwareUpdateListenerMock, diCommPortMock);
        when(diCommPortMock.getMaxChunkSize()).thenReturn(TEST_MAX_CHUNK_SIZE);
        when(diCommPortMock.getState()).thenReturn(State.Downloading);

        Map<String, Object> properties = new HashMap<>();
        properties.put(PROGRESS, progress);
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, result);
    }

    @Test
    public void whenFirstChunkIsWrittenSuccessfullyThenSecondChunkIsWritten() {
        whenStateSwitchesToDownloadingThenFirstChunkIsWritten();

        sendChunk(TEST_MAX_CHUNK_SIZE, SHNResult.SHNOk);

        verifyChunkWritten(TEST_MAX_CHUNK_SIZE);
    }

    @Test
    public void whenFirstChunkIsWrittenSuccessfullyWithDoubleProgressThenSecondChunkIsWritten() {
        whenStateSwitchesToDownloadingThenFirstChunkIsWritten();

        sendChunk((double) TEST_MAX_CHUNK_SIZE, SHNResult.SHNOk);

        verifyChunkWritten(TEST_MAX_CHUNK_SIZE);
    }

    @Test
    public void whenFirstChunkIsNotWrittenThenUploadHasFailed() {
        whenStateSwitchesToDownloadingThenFirstChunkIsWritten();

        sendChunk(TEST_MAX_CHUNK_SIZE, SHNResult.SHNErrorConnectionLost);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNResult.SHNErrorConnectionLost, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenSecondChunkIsWrittenSuccessfullyThenThirdChunkIsWritten() {
        whenFirstChunkIsWrittenSuccessfullyThenSecondChunkIsWritten();

        sendChunk(TEST_MAX_CHUNK_SIZE * 2, SHNResult.SHNOk);

        verifyChunkWritten(TEST_MAX_CHUNK_SIZE * 2);
    }

    @Test
    public void whenThirdChunkIsWrittenSuccessfullyThenLastChunkIsWritten() {
        whenSecondChunkIsWrittenSuccessfullyThenThirdChunkIsWritten();

        sendChunk(TEST_MAX_CHUNK_SIZE * 3, SHNResult.SHNOk);

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        byte[] data = (byte[]) mapArgumentCaptor.getValue().get(DATA);
        assertEquals(1, data.length);

        assertEquals(firmwareData[firmwareData.length - 1], data[0]);
    }

    @Test
    public void whenProgressIsNotReportedThenUploadIsFailed() {
        whenStateSwitchesToDownloadingThenFirstChunkIsWritten();

        reset(shnCapabilityFirmwareUpdateListenerMock, diCommPortMock);
        when(diCommPortMock.getMaxChunkSize()).thenReturn(TEST_MAX_CHUNK_SIZE);
        when(diCommPortMock.getState()).thenReturn(State.Downloading);

        Map<String, Object> properties = new HashMap<>();
        properties.put(STATE, DOWNLOADING);
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(properties, SHNResult.SHNOk);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNResult.SHNErrorInvalidParameter, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenAChunkIsWrittenWithNotDownloadingStateThenNextChunkIsNotWritten() {
        whenStateSwitchesToDownloadingThenFirstChunkIsWritten();
        reset(diCommPortMock);

        respondWith(State.Error, 0);

        verify(diCommPortMock, never()).putProperties(anyMap(), any());
    }

    @Test
    public void whenStateSwitchesToCheckingThenNextChunkIsNotWritten() {
        whenThirdChunkIsWrittenSuccessfullyThenLastChunkIsWritten();
        reset(diCommPortMock);

        respondWith(State.Checking, firmwareData.length);

        verify(diCommPortMock, never()).putProperties(anyMap(), any());
    }

    @Test
    public void whenStateSwitchesToDownloadingThenStartsWaitingForReady() {
        whenSubscriptionIsSuccessfulThenStartsWaitingForDownloading();
        reset(diCommFirmwarePortStateWaiterMock);

        waiterListenerArgumentCaptor.getValue().onStateUpdated(State.Downloading, SHNResult.SHNOk);

        verify(diCommFirmwarePortStateWaiterMock).waitUntilStateIsReached(eq(State.Ready), waiterListenerArgumentCaptor.capture());
    }

    @Test
    public void whenStateSwitchesToReadyThenUploadIsFinished() {
        whenStateSwitchesToDownloadingThenStartsWaitingForReady();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        waiterListenerArgumentCaptor.getValue().onStateUpdated(State.Ready, SHNResult.SHNOk);

        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFinished(capabilityFirmwareUpdateDiComm);
        verify(shnCapabilityFirmwareUpdateListenerMock).onProgressUpdate(capabilityFirmwareUpdateDiComm, 1.0f);
        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
        verify(diCommPortMock).unsubscribe(any(DiCommPort.UpdateListener.class), (SHNResultListener) any());
    }

    @Test
    public void whenStateSwitchesToUnexpectedStateWhileWaitingForReadyThenUploadIsFailed() {
        whenStateSwitchesToDownloadingThenStartsWaitingForReady();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        waiterListenerArgumentCaptor.getValue().onStateUpdated(State.Error, SHNResult.SHNErrorConnectionLost);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNResult.SHNErrorConnectionLost, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenPortBecomesAvailableWithIdleStateThenStateIsNotUpdated() {
        whenCreatedThenPortListenerIsRegistered();
        when(diCommPortMock.getState()).thenReturn(State.Idle);

        listenerArgumentCaptor.getValue().onPortAvailable(diCommPortMock);

        verify(shnCapabilityFirmwareUpdateListenerMock, never()).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void givenCapabilityInNonIdleState_whenPortBecomesUnavailable_thenStateBecomesIdle() {
        whenCreatedThenPortListenerIsRegistered();
        capabilityFirmwareUpdateDiComm.overrideState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading);

        listenerArgumentCaptor.getValue().onPortUnavailable(diCommPortMock);

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
        assertThat(capabilityFirmwareUpdateDiComm.getState()).isEqualTo(SHNFirmwareUpdateStateIdle);
    }

    @Test
    public void whenStateOfThePortIsUpdatedThenListenerIsNotified() {
        whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort();
        when(diCommPortMock.getState()).thenReturn(State.Canceling);

        updateListenerCaptor.getValue().onPropertiesChanged(new HashMap<String, Object>());

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenStateOfThePortIsUpdatedTwiceToExternalStateIdleThenListenerIsNotifiedOnce() {
        whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort();

        when(diCommPortMock.getState()).thenReturn(State.Downloading);
        updateListenerCaptor.getValue().onPropertiesChanged(new HashMap<String, Object>());

        when(diCommPortMock.getState()).thenReturn(State.Canceling);
        updateListenerCaptor.getValue().onPropertiesChanged(new HashMap<String, Object>());

        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenPortIsReadyAndDeployFirmwareIsCalledThenStateIsDeploying() {
        whenCreatedThenPortListenerIsRegistered();

        when(diCommPortMock.getCanUpgrade()).thenReturn(true);
        when(diCommPortMock.getState()).thenReturn(State.Ready);
        capabilityFirmwareUpdateDiComm.deployFirmware();

        assertEquals(SHNFirmwareUpdateStateDeploying, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenPortIsNotReadyAndDeployFirmwareIsCalledThenStateIsDeploying() {
        when(diCommPortMock.getState()).thenReturn(State.Canceling);
        capabilityFirmwareUpdateDiComm.deployFirmware();

        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenPortCanNotUpgradeAndDeployFirmwareIsCalledThenStateIsDeploying() {
        when(diCommPortMock.getState()).thenReturn(State.Ready);
        when(diCommPortMock.getCanUpgrade()).thenReturn(false);
        capabilityFirmwareUpdateDiComm.deployFirmware();

        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenInStateDeployingThenStartWaitingForIdle() {
        whenPortIsReadyAndDeployFirmwareIsCalledThenStateIsDeploying();

        verify(diCommFirmwarePortStateWaiterMock).waitUntilStateIsReached(eq(State.Idle), waiterListenerArgumentCaptor.capture());
    }

    @Test
    public void whenUnexpectedStateIsReachedThenDeployedHasFailed() {
        whenInStateDeployingThenStartWaitingForIdle();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        waiterListenerArgumentCaptor.getValue().onStateUpdated(State.Error, SHNResult.SHNErrorInvalidParameter);

        verify(shnCapabilityFirmwareUpdateListenerMock).onDeployFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorInvalidParameter);
        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenExpectedStateIsReachedThenDeployedIsFinished() {
        whenInStateDeployingThenStartWaitingForIdle();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        waiterListenerArgumentCaptor.getValue().onStateUpdated(State.Idle, SHNResult.SHNOk);

        verify(shnCapabilityFirmwareUpdateListenerMock).onDeployFinished(capabilityFirmwareUpdateDiComm, SHNResult.SHNOk);
        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenInStateDeployingThenGoIsSent() {
        whenPortIsReadyAndDeployFirmwareIsCalledThenStateIsDeploying();

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(GO, mapArgumentCaptor.getValue().get(STATE));
    }

    @Test
    public void whenGoCommandWasNotSentSuccessfullyThenDeployIsFailed() {
        whenInStateDeployingThenGoIsSent();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verify(shnCapabilityFirmwareUpdateListenerMock).onDeployFailed(capabilityFirmwareUpdateDiComm, SHNResult.SHNErrorConnectionLost);
        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
        verify(shnCapabilityFirmwareUpdateListenerMock).onStateChanged(capabilityFirmwareUpdateDiComm);
    }

    @Test
    public void whenGoCommandWasNotSentSuccessfullyThenWaitIsCancelled() {
        whenInStateDeployingThenGoIsSent();
        reset(shnCapabilityFirmwareUpdateListenerMock);

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNErrorConnectionLost);

        verify(diCommFirmwarePortStateWaiterMock).cancel();
    }

    private void givenPortInState(State state) {
        when(diCommPortMock.getState()).thenReturn(state);
        capabilityFirmwareUpdateDiComm.updateState();
    }

    @Test
    public void givenPortIsPreparing_whenAbortFirmwareIsCalled_andCancelIsSentSuccessfully_thenStateIsIdle() {
        givenPortInState(State.Preparing);

        capabilityFirmwareUpdateDiComm.abortFirmwareUpload();

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(CANCEL, mapArgumentCaptor.getValue().get(STATE));
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNOk);

        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void givenPortIsDownloading_whenAbortFirmwareIsCalled_andCancelIsSentSuccessfully_thenStateIsIdle() {
        givenPortInState(State.Downloading);

        capabilityFirmwareUpdateDiComm.abortFirmwareUpload();

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(CANCEL, mapArgumentCaptor.getValue().get(STATE));
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNOk);

        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void givenPortIsChecking_whenAbortFirmwareIsCalled_andCancelIsSentSuccessfully_thenStateIsIdle() {
        givenPortInState(State.Checking);

        capabilityFirmwareUpdateDiComm.abortFirmwareUpload();

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(CANCEL, mapArgumentCaptor.getValue().get(STATE));
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNOk);

        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void givenPortIsReady_whenAbortFirmwareIsCalled_andCancelIsSentSuccessfully_thenStateIsIdle() {
        givenPortInState(State.Ready);

        capabilityFirmwareUpdateDiComm.abortFirmwareUpload();

        verify(diCommPortMock).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertEquals(CANCEL, mapArgumentCaptor.getValue().get(STATE));
        mapResultListenerArgumentCaptor.getValue().onActionCompleted(null, SHNResult.SHNOk);

        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void givenPortIsIdle_whenAbortFirmwareIsCalled_andCancelIsNotSentSuccessfully_thenStateStaysIdle() {
        givenPortInState(State.Idle);

        capabilityFirmwareUpdateDiComm.abortFirmwareUpload();

        verify(diCommPortMock, never()).putProperties(ArgumentMatchers.<String, Object>anyMap(), any(SHNMapResultListener.class));

        assertEquals(SHNFirmwareUpdateStateIdle, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void givenPortIsProgramming_whenAbortFirmwareIsCalled_andCancelIsNotSentSuccessfully_thenStateStaysDeploying() {
        givenPortInState(State.Programming);

        capabilityFirmwareUpdateDiComm.abortFirmwareUpload();

        verify(diCommPortMock, never()).putProperties(ArgumentMatchers.<String, Object>anyMap(), any(SHNMapResultListener.class));

        assertEquals(SHNFirmwareUpdateStateDeploying, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenInStateUploadingStateSwitchesToErrorThenUploadIsFailed() {
        whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort();
        when(diCommPortMock.getState()).thenReturn(State.Error);

        updateListenerCaptor.getValue().onPropertiesChanged(new HashMap<>());

        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFailed(capabilityFirmwareUpdateDiComm, SHNErrorInvalidState);
        verify(diCommPortMock).unsubscribe(any(DiCommPort.UpdateListener.class), any());
        verify(diCommFirmwarePortStateWaiterMock).cancel();
    }

    @Test
    public void whenInStateDeployingStateSwitchesToErrorThenDeployIsFailed() {
        whenUploadIsCalledAndPortIsIdleThenCapabilityIsSubscribedToThePort();
        reset(shnCapabilityFirmwareUpdateListenerMock);
        whenPortIsReadyAndDeployFirmwareIsCalledThenStateIsDeploying();

        listenerArgumentCaptor.getValue().onPortUnavailable(diCommPortMock);
        when(diCommPortMock.getState()).thenReturn(State.Error);
        listenerArgumentCaptor.getValue().onPortAvailable(diCommPortMock);
        updateListenerCaptor.getValue().onPropertiesChanged(Collections.emptyMap());

        verify(shnCapabilityFirmwareUpdateListenerMock).onDeployFailed(capabilityFirmwareUpdateDiComm, SHNErrorInvalidState);
        verify(diCommPortMock).unsubscribe(any(DiCommPort.UpdateListener.class), any());
        verify(diCommFirmwarePortStateWaiterMock).cancel();
    }

    @Test
    public void givenDeviceInDownloadingState_whenResumingUpload_thenUploadStartsFromExistingProgress() {
        capabilityFirmwareUpdateDiComm.uploadFirmware(firmwareData, true);
        verify(diCommPortMock).reloadProperties(mapResultListenerArgumentCaptor.capture());

        respondWith(State.Downloading, 1);

        verifyChunkWritten(1);
    }

    @Test
    public void givenDeviceInDownloadingState_whenItReportsProgressWhichIsTooLarge_thenUploadFailedIsReported() throws Exception {
        capabilityFirmwareUpdateDiComm.uploadFirmware(firmwareData, true);
        verify(diCommPortMock).reloadProperties(mapResultListenerArgumentCaptor.capture());

        respondWith(State.Downloading, firmwareData.length + 1);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNErrorInvalidState, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void givenDeviceInDownloadingState_whenItReportsProgressWhichIsNegative_thenUploadFailedIsReported() throws Exception {
        capabilityFirmwareUpdateDiComm.uploadFirmware(firmwareData, true);
        verify(diCommPortMock).reloadProperties(mapResultListenerArgumentCaptor.capture());

        respondWith(State.Downloading, -1);

        verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNErrorInvalidState, capabilityFirmwareUpdateDiComm.getState());
    }

    @Test
    public void whenStartingUploadOfFirmwareWithoutProvidingResumeFlag_thenFirmwareUploadShouldNotResume() throws Exception {

        capabilityFirmwareUpdateDiComm.uploadFirmware(firmwareData);

        assertThat(capabilityFirmwareUpdateDiComm.uploadFirmwareWithResumeCalled).isTrue();
    }

    private class CapabilityFirmwareUpdateDiCommForTest extends CapabilityFirmwareUpdateDiComm {

        boolean uploadFirmwareWithResumeCalled = false;

        CapabilityFirmwareUpdateDiCommForTest(@NonNull DiCommFirmwarePort diCommPort, Handler internalHandler) {
            super(diCommPort, internalHandler);
        }

        @Override
        protected DiCommFirmwarePortStateWaiter createDiCommFirmwarePortStateWaiter(@NonNull DiCommFirmwarePort diCommPort, @NonNull Handler internalHandler) {
            return diCommFirmwarePortStateWaiterMock;
        }

        byte[] getFirmwareData() {
            return firmwareData;
        }

        void overrideState(SHNFirmwareUpdateState state) {
            this.state = state;
        }

        @Override
        public void uploadFirmware(final byte[] firmwareData, final boolean shouldResume) {
            uploadFirmwareWithResumeCalled = true;
            super.uploadFirmware(firmwareData, shouldResume);
        }
    }

    private void respondWith(State state) {
        respondWith(state, -1);
    }

    private void respondWith(State state, double progress) {
        latestReceivedProperties.clear();
        latestReceivedProperties.put(PROGRESS, progress);
        latestReceivedProperties.put(STATE, state.toString());
        latestReceivedProperties.put(MAX_CHUNK_SIZE, TEST_MAX_CHUNK_SIZE * 4 / 3);

        mapResultListenerArgumentCaptor.getValue().onActionCompleted(latestReceivedProperties, SHNResult.SHNOk);
    }

    private void verifyUploadFailedGotReportedWithCorrectError_andStateBecameIdle_andStateChangeGotReported_andUnsubscribeWasCalled(SHNResult error, SHNFirmwareUpdateState state) {
        verify(shnCapabilityFirmwareUpdateListenerMock).onUploadFailed(capabilityFirmwareUpdateDiComm, error);
        verify(shnCapabilityFirmwareUpdateListenerMock, atLeastOnce()).onStateChanged(capabilityFirmwareUpdateDiComm);
        assertThat(state).isEqualTo(SHNFirmwareUpdateStateIdle);
        verify(diCommPortMock).unsubscribe(any(), any());
    }

    private void verifyChunkWritten(int progress) {
        verify(diCommPortMock, atLeastOnce()).putProperties(mapArgumentCaptor.capture(), mapResultListenerArgumentCaptor.capture());
        assertEquals(1, mapArgumentCaptor.getValue().size());
        assertThat(mapArgumentCaptor.getValue().containsKey(DATA)).isTrue();
        byte[] chunk = (byte[]) mapArgumentCaptor.getValue().get(DATA);
        assertEquals(TEST_MAX_CHUNK_SIZE, chunk.length);

        assertEquals(firmwareData[progress], chunk[0]);
        assertEquals(firmwareData[progress + 1], chunk[1]);
        assertEquals(firmwareData[progress + 2], chunk[2]);
        float progressIndicator = (float) progress / firmwareData.length;
        verify(shnCapabilityFirmwareUpdateListenerMock).onProgressUpdate(capabilityFirmwareUpdateDiComm, progressIndicator);
    }
}