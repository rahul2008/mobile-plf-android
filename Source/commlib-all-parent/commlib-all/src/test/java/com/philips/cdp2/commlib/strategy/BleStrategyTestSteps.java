/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.strategy;

import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleStrategyTestSteps {
    private final BleDeviceCache deviceCache = new BleDeviceCache();
    private BleStrategy strategy;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Given("^a mock device is found with id \"(.*?)\"$")
    public void a_mock_device_is_found_with_id(String deviceId) throws Throwable {
        SHNDeviceFoundInfo info = mock(SHNDeviceFoundInfo.class);
        SHNDevice device = mock(SHNDevice.class);

        when(info.getShnDevice()).thenReturn(device);
        when(device.getAddress()).thenReturn(deviceId);

        deviceCache.deviceFound(null, info);
    }

    @Given("^the BleStrategy is initialized with id \"(.*?)\"$")
    public void the_BleStrategy_is_initialized_with_id(String deviceId) throws Throwable {
        strategy = new BleStrategy(deviceId, deviceCache);
    }

    @Then("^the BleStrategy is available$")
    public void theBleStrategyIsAvailable() throws Throwable {
        assertTrue(strategy.isAvailable());
    }

    @Then("^the BleStrategy is not available$")
    public void theBleStrategyIsNotAvailable() throws Throwable {
        assertFalse(strategy.isAvailable());
    }
}
