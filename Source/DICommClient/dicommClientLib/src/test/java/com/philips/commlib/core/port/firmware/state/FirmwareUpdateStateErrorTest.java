/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwareUpdateStateErrorTest {

    @Mock
    private FirmwareUpdatePushLocal mockOperation;

    FirmwareUpdateStateError stateUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        DICommLog.disableLogging();

        stateUnderTest = new FirmwareUpdateStateError(mockOperation);
    }

    @Test
    public void whenComingFromCheckingStateDownloadFailedShouldBeCalled() {

        stateUnderTest.start(new FirmwareUpdateStateChecking(mockOperation));

        verify(mockOperation).onDownloadFailed();
    }
}