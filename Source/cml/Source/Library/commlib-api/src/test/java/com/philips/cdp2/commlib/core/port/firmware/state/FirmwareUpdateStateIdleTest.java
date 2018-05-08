/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwareUpdateStateIdleTest {

    @Mock
    private FirmwareUpdatePushLocal mockOperation;

    private FirmwareUpdateStateIdle stateUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        disableLogging();

        stateUnderTest = new FirmwareUpdateStateIdle(mockOperation);
    }

    @Test
    public void givenStartIsCalled_whenPreviousStateIsNull_thenDownloadIsRequested() {
        stateUnderTest.start(null);

        verify(mockOperation).requestStateDownloading();
    }

    @Test
    public void givenStartIsCalled_whenPreviousStateIsProgramming_thenDeployIsFinished() {
        stateUnderTest.start(new FirmwareUpdateStateProgramming(mockOperation));

        verify(mockOperation).onDeployFinished();
    }

    @Test
    public void givenStartIsCalled_whenPreviousStateIsError_thenDownloadIsRequested() {
        stateUnderTest.start(new FirmwareUpdateStateError(mockOperation));

        verify(mockOperation).finish();
        verifyNoMoreInteractions(mockOperation);
    }

    @Test
    public void whenErrorHasOccurred_thenFailureIsReported() {
        stateUnderTest.onError("some error");

        verify(mockOperation).onDownloadFailed(anyString());
    }

    @Test
    public void whenErrorHasOccurred_thenStateIsFinished() {
        stateUnderTest.onError("some error");

        verify(mockOperation).finish();
    }
}
