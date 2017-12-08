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

public class FirmwareUpdateStateErrorTest {

    @Mock
    private FirmwareUpdatePushLocal mockOperation;

    private FirmwareUpdateStateError stateUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        disableLogging();

        stateUnderTest = new FirmwareUpdateStateError(mockOperation);
    }

    @Test
    public void onStartFromChecking_DownloadFailed() {

        stateUnderTest.start(new FirmwareUpdateStateChecking(mockOperation));

        verify(mockOperation).onDownloadFailed();
    }

    @Test
    public void onStartFromDownloading_DownloadFailed() {

        stateUnderTest.start(new FirmwareUpdateStateDownloading(mockOperation));

        verify(mockOperation).onDownloadFailed();
    }

    @Test
    public void onStartFromPreparing_DownloadFailed() {

        stateUnderTest.start(new FirmwareUpdateStatePreparing(mockOperation));

        verify(mockOperation).onDownloadFailed();
    }

    @Test
    public void onStartFromReady_DeployFailed() {

        stateUnderTest.start(new FirmwareUpdateStateReady(mockOperation));

        verify(mockOperation).onDeployFailed();
    }
}
