/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.strategy;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.cdp2.datatypes.DatatypeConverter;
import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cucumber.api.java.Before;
import cucumber.api.java.en.*;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleStrategyTestSteps {
    private final BleDeviceCache deviceCache = new BleDeviceCache();
    private BleStrategy strategy;
    private final Map<String, Set<ResultListener<SHNDataRaw>>> rawDataListeners = new HashMap();

    @Before
    public void setup() {
        initMocks(this);
    }

    @Given("^a mock device is found with id \"(.*?)\"$")
    public void a_mock_device_is_found_with_id(final String deviceId) throws Throwable {
        SHNDeviceFoundInfo info = mock(SHNDeviceFoundInfo.class);
        SHNDevice device = mock(SHNDevice.class);
        rawDataListeners.put(deviceId, new HashSet<ResultListener<SHNDataRaw>>());

        CapabilityDiComm capability = spy(new CapabilityDiComm() {
            @Override
            public void writeData(@NonNull final byte[] bytes) {

            }

            @Override
            public void addDataListener(@NonNull final ResultListener<SHNDataRaw> resultListener) {
                rawDataListeners.get(deviceId).add(resultListener);
            }

            @Override
            public void removeDataListener(@NonNull final ResultListener<SHNDataRaw> resultListener) {
                rawDataListeners.get(deviceId).remove(resultListener);
            }
        });

        when(info.getShnDevice()).thenReturn(device);
        when(device.getAddress()).thenReturn(deviceId);
        when(device.getCapabilityForType(SHNCapabilityType.DI_COMM)).thenReturn(capability);

        deviceCache.deviceFound(null, info);
    }

    @When("^the mock device with id \"(.*?)\" receives data \"(.*?)\"")
    public void mock_device_receives_data(final String id, final String data) {
        final Set<ResultListener<SHNDataRaw>> listeners = rawDataListeners.get(id);

        if (listeners == null) {
            fail("Mock device '" + id + "' was not yet created");
        }

        final byte[] dataBytes = DatatypeConverter.parseHexBinary(data);

        for (ResultListener<SHNDataRaw> listener : listeners) {
            listener.onActionCompleted(new SHNDataRaw(dataBytes), SHNResult.SHNOk);
        }
    }

    @Then("^data \"(.*?)\" was written to mock device with id \"(.*?)\"")
    public void mock_device_data_written(final String data, final String deviceId) {
        CapabilityDiComm capability = (CapabilityDiComm) deviceCache
                .getDeviceMap().get(deviceId).getCapabilityForType(SHNCapabilityType.DI_COMM);

        final byte[] dataBytes = DatatypeConverter.parseHexBinary(data);

        verify(capability).writeData(dataBytes);
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
