/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import org.junit.Before;
import org.junit.Test;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwareUpdateStateCheckingTest extends WaitingFirmwareUpdateState {

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        disableLogging();

        stateUnderTest = new FirmwareUpdateStateChecking(mockFirmwareUpdate);
    }

    @Test
    public void onFinish_checkingProgress100() {
        stateUnderTest.finish();

        verify(mockFirmwareUpdate).onCheckingProgress(100);
    }
}
