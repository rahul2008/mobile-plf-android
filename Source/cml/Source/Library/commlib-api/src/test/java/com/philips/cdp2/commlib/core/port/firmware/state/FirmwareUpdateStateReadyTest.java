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
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwareUpdateStateReadyTest {

    @Mock
    private FirmwareUpdatePushLocal mockOperation;
    @Mock
    private FirmwareUpdateState mockState;

    private FirmwareUpdateStateReady stateUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        disableLogging();

        stateUnderTest = new FirmwareUpdateStateReady(mockOperation);
    }

    @Test
    public void onStart_DownloadFinished() {

        stateUnderTest.start(mockState);

        verify(mockOperation).onDownloadFinished();
    }
}
