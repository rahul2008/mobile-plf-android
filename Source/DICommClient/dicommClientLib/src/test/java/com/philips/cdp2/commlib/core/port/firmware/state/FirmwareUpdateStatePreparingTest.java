/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.state;

import org.junit.Before;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirmwareUpdateStatePreparingTest extends WaitingFirmwareUpdateState {

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        disableLogging();

        stateUnderTest = new FirmwareUpdateStatePreparing(mockFirmwareUpdate);
    }
}
