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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwareUpdateStateDownloadingTest {

    @Mock
    FirmwareUpdatePushLocal mockFirmwareUpdate;
    private FirmwareUpdateStateDownloading stateUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        disableLogging();

        stateUnderTest = new FirmwareUpdateStateDownloading(mockFirmwareUpdate);
    }

    @Test
    public void givenStartedThenUploadFirmwareIsCalled() {
        stateUnderTest.start(null);

        verify(mockFirmwareUpdate).uploadFirmware(stateUnderTest.firmwareUploadListener);
    }

    @Test
    public void givenStartedWhenUploadFailsThenDownloadFailedIsReported() {
        Throwable t = new Throwable("Something went wrong!");

        stateUnderTest.firmwareUploadListener.onError(t.getMessage(), t);

        verify(mockFirmwareUpdate).onDownloadFailed(anyString());
    }

    @Test
    public void givenStartedWhenUploadFailsThenFinishIsReported() {
        Throwable t = new Throwable("Something went wrong!");

        stateUnderTest.firmwareUploadListener.onError(t.getMessage(), t);

        verify(mockFirmwareUpdate).finish();
    }

    @Test
    public void onSuccess_downloadProgress100() {
        stateUnderTest.firmwareUploadListener.onSuccess();

        verify(mockFirmwareUpdate).onDownloadProgress(100);
    }
}
