/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.state;

import org.junit.Before;
import org.junit.Test;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwareUpdateStateDownloadingTest extends WaitingFirmwareUpdateState {

    @Before
    public void setUp() {
        initMocks(this);
        disableLogging();

        stateUnderTest = new FirmwareUpdateStateDownloading(mockOperation);
    }

    @Test
    public void onFinish_downloadProgress100() {
        stateUnderTest.finish();

        verify(mockOperation).onDownloadProgress(100);
    }
}