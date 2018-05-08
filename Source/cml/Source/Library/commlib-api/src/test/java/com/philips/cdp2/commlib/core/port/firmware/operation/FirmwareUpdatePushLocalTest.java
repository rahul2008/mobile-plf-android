/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.operation;

import android.support.annotation.NonNull;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortListener;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateDownloading;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateError;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateIdle;
import com.philips.cdp2.commlib.core.port.firmware.util.FirmwarePortStateWaiter;
import com.philips.cdp2.commlib.core.port.firmware.util.FirmwareUpdateException;
import com.philips.cdp2.commlib.core.port.firmware.util.FirmwareUploader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CANCELING;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.DOWNLOADING;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.IDLE;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.PROGRAMMING;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirmwareUpdatePushLocal.class})
public class FirmwareUpdatePushLocalTest {

    @Mock
    private FirmwarePort mockFirmwarePort;

    @Mock
    private CommunicationStrategy mockCommunicationStrategy;

    @Mock
    private FirmwarePortListener mockListener;

    @Mock
    private FirmwarePortProperties mockPortProperties;

    @Mock
    private FirmwareUploader.UploadListener mockUploadListener;

    @Mock
    private FirmwareUpdateStateIdle mockIdleState;

    @Mock
    private FirmwareUpdateStateDownloading mockDownloadingState;

    @Mock
    private FirmwareUpdateStateError mockErrorState;

    @Mock
    private FirmwarePortStateWaiter mockFirmwarePortStateWaiter;

    @Mock
    private FirmwareUploader mockFirmwareUploader;

    @Captor
    private ArgumentCaptor<FirmwarePortListener.FirmwarePortException> exceptionArgumentCaptor;

    @Captor
    private ArgumentCaptor<FirmwarePortStateWaiter.WaiterListener> waiterListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Map<String, Object>> portPropertiesArgumentCaptor;

    private byte[] firmwaredata = {0xC, 0x0, 0xF, 0xF, 0xE, 0xE};

    private FirmwareUpdatePushLocal firmwareUpdateUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DICommLog.disableLogging();

        when(mockPortProperties.getState()).thenReturn(IDLE);
        when(mockFirmwarePort.getPortProperties()).thenReturn(mockPortProperties);

        PowerMockito.whenNew(FirmwareUpdateStateIdle.class).withAnyArguments().thenReturn(mockIdleState);
        PowerMockito.whenNew(FirmwareUpdateStateDownloading.class).withAnyArguments().thenReturn(mockDownloadingState);
        PowerMockito.whenNew(FirmwareUpdateStateError.class).withAnyArguments().thenReturn(mockErrorState);
        PowerMockito.whenNew(FirmwarePortStateWaiter.class).withAnyArguments().thenReturn(mockFirmwarePortStateWaiter);

        firmwareUpdateUnderTest = new FirmwareUpdatePushLocal(mockFirmwarePort, mockCommunicationStrategy, mockListener, firmwaredata) {
            @NonNull
            @Override
            FirmwareUploader createFirmwareUploader(FirmwareUploader.UploadListener firmwareUploadListener) {
                return mockFirmwareUploader;
            }
        };
    }

    @Test
    public void givenPushLocalIsInitialized_whenRemoteIsInIdleState_thenStateIsIdle() {
        assertTrue(firmwareUpdateUnderTest.getState() instanceof FirmwareUpdateStateIdle);
    }

    @Test
    public void givenPushLocalIsInitialized_whenCreateFirmwareUploaderIsCalled_thenARealInstanceIsCreated() {
        firmwareUpdateUnderTest = new FirmwareUpdatePushLocal(mockFirmwarePort, mockCommunicationStrategy, mockListener, firmwaredata);

        FirmwareUploader firmwareUploader = firmwareUpdateUnderTest.createFirmwareUploader(mockUploadListener);

        assertNotNull(firmwareUploader);
    }

    @Test
    public void givenPushLocalIsInitialized_whenRemoteIsInUnknownState_thenStateIsIdle() {
        when(mockFirmwarePort.getPortProperties()).thenReturn(null);

        firmwareUpdateUnderTest = new FirmwareUpdatePushLocal(mockFirmwarePort, mockCommunicationStrategy, mockListener, firmwaredata);

        assertTrue(firmwareUpdateUnderTest.getState() instanceof FirmwareUpdateStateIdle);
    }

    @Test
    public void givenPushLocalIsInitialized_whenRemoteIsInDownloadingState_thenStateIsDownloading() {
        when(mockPortProperties.getState()).thenReturn(DOWNLOADING);

        firmwareUpdateUnderTest = new FirmwareUpdatePushLocal(mockFirmwarePort, mockCommunicationStrategy, mockListener, firmwaredata);

        assertTrue(firmwareUpdateUnderTest.getState() instanceof FirmwareUpdateStateDownloading);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenPushLocalIsInitialized_whenFirmwareDataIsEmpty_thenExceptionIsThrown() {
        firmwareUpdateUnderTest = new FirmwareUpdatePushLocal(mockFirmwarePort, mockCommunicationStrategy, mockListener, new byte[0]);
    }

    @Test
    public void givenPushLocalIsInitialized_whenFirmwareUpdateIsStarted_thenCancelIsPerformed() throws FirmwareUpdateException {
        firmwareUpdateUnderTest.start(1000);

        verify(mockIdleState).cancel();
    }

    @Test
    public void givenCancelIsNotSupportedByCurrentState_whenFirmwareUpdateIsStarted_thenStartIsPerformed() throws FirmwareUpdateException {
        doThrow(new FirmwareUpdateException("")).when(mockIdleState).cancel();

        firmwareUpdateUnderTest.start(1000);

        verify(mockIdleState).start(null);
    }

    @Test
    public void givenPushLocalIsWaitingForNextState_whenRemoteSwitchesToDownloading_thenDownloadIsStarted() throws Exception {
        firmwareUpdateUnderTest.waitForNextState();

        PowerMockito.verifyNew(FirmwarePortStateWaiter.class).withArguments(eq(mockFirmwarePort), eq(mockCommunicationStrategy), eq(IDLE), waiterListenerArgumentCaptor.capture());
        waiterListenerArgumentCaptor.getValue().onNewState(DOWNLOADING);

        verify(mockDownloadingState).start(mockIdleState);
    }

    @Test
    public void givenPushLocalIsWaitingForNextState_whenRemoteReportsError_thenErrorIsReported() throws Exception {
        firmwareUpdateUnderTest.waitForNextState();

        PowerMockito.verifyNew(FirmwarePortStateWaiter.class).withArguments(eq(mockFirmwarePort), eq(mockCommunicationStrategy), eq(IDLE), waiterListenerArgumentCaptor.capture());
        String wtf = "What a terrible failure";
        waiterListenerArgumentCaptor.getValue().onError(wtf);

        verify(mockIdleState).onError(wtf);
    }

    @Test
    public void whenDeployIsCalled_thenDeployIsCalledOnCurrentState() throws FirmwareUpdateException {
        firmwareUpdateUnderTest.deploy(1000);

        verify(mockIdleState).deploy();
    }

    @Test
    public void whenCancelIsCalled_thenCancelIsCalledOnCurrentState() throws FirmwareUpdateException {
        firmwareUpdateUnderTest.cancel(1000);

        verify(mockIdleState).cancel();
    }

    @Test
    public void whenFinishIsCalled_thenFinishIsCalledOnPort() {
        firmwareUpdateUnderTest.finish();

        verify(mockFirmwarePort).finishFirmwareUpdate();
    }

    @Test
    public void whenUploadFirmwareIsCalled_thenUploadIsStarted() {
        firmwareUpdateUnderTest.uploadFirmware(mockUploadListener);

        verify(mockFirmwareUploader).start();
        verifyNoMoreInteractions(mockFirmwareUploader);
    }

    @Test
    public void givenFirmwareUploadWasStarted_whenStopUploadIsCalled_thenUploaderStops() {
        firmwareUpdateUnderTest.uploadFirmware(mockUploadListener);
        verify(mockFirmwareUploader).start();

        firmwareUpdateUnderTest.stopUploading();

        verify(mockFirmwareUploader).stop();
        verifyNoMoreInteractions(mockFirmwareUploader);
    }

    @Test
    public void givenFirmwareUploadIsNotStartedYet_whenStopUploadIsCalled_then() {
        firmwareUpdateUnderTest.stopUploading();

        verifyZeroInteractions(mockFirmwareUploader);
    }

    @Test
    public void givenRemoteReportsDeployFinished_thenFinishIsReported() {
        firmwareUpdateUnderTest.onDeployFinished();

        verify(mockListener).onDeployFinished();
    }

    @Test
    public void givenRemoteReportsAnErrorForDownload_whenDownloadHasFailed_thenFailureIsReportedWithMessage() {
        when(mockFirmwarePort.getPortProperties()).thenReturn(mockPortProperties);
        when(mockPortProperties.getStatusMessage()).thenReturn("TestError");

        firmwareUpdateUnderTest.onDownloadFailed();

        verify(mockListener).onDownloadFailed(exceptionArgumentCaptor.capture());
        assertEquals("TestError", exceptionArgumentCaptor.getValue().getMessage());
    }

    @Test
    public void givenRemoteReportsAnErrorForDeploy_whenDownloadHasFailed_thenFailureIsReportedWithMessage() {
        when(mockFirmwarePort.getPortProperties()).thenReturn(mockPortProperties);
        when(mockPortProperties.getStatusMessage()).thenReturn("TestError");

        firmwareUpdateUnderTest.onDeployFailed();

        verify(mockListener).onDeployFailed(exceptionArgumentCaptor.capture());
        assertEquals("TestError", exceptionArgumentCaptor.getValue().getMessage());
    }

    @Test
    public void givenRemoteReportsDownloadFinished_thenFinishIsReported() {
        firmwareUpdateUnderTest.onDownloadFinished();

        verify(mockListener).onDownloadFinished();
    }

    @Test
    public void givenRemoteReportsDownloadProgress_thenDownloadIsReported() {
        firmwareUpdateUnderTest.onDownloadProgress(42);

        verify(mockListener).onDownloadProgress(42);
    }

    @Test
    public void givenRemoteReportsCheckingProgress_thenProgressIsReported() {
        firmwareUpdateUnderTest.onCheckingProgress(42);

        verify(mockListener).onCheckingProgress(42);
    }

    @Test
    public void givenCancelingStateIsRequested_thenPropertiesAreUpdated() {
        firmwareUpdateUnderTest.requestState(CANCELING);

        verify(mockFirmwarePort).putProperties("state", "cancel");
    }

    @Test
    public void givenProgrammingStateIsRequested_thenPropertiesAreUpdated() {
        firmwareUpdateUnderTest.requestState(PROGRAMMING);

        verify(mockFirmwarePort).putProperties("state", "go");
    }

    @Test
    public void givenDownloadingStateIsRequested_thenPropertiesAreUpdated() {
        firmwareUpdateUnderTest.requestStateDownloading();

        verify(mockFirmwarePort).putProperties(portPropertiesArgumentCaptor.capture());
        assertTrue(portPropertiesArgumentCaptor.getValue().containsKey("size"));
        assertEquals(portPropertiesArgumentCaptor.getValue().get("size"), firmwaredata.length);
        assertTrue(portPropertiesArgumentCaptor.getValue().containsKey("state"));
        assertEquals(portPropertiesArgumentCaptor.getValue().get("state"), "downloading");
    }
}
