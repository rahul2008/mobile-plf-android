/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.strategy;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.cdp2.commlib.request.BleRequest;
import com.philips.cdp2.datatypes.DatatypeConverter;
import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleStrategyTestSteps {
    private static final int TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS = 100;

    private final BleDeviceCache deviceCache = new BleDeviceCache();
    private BleStrategy strategy;
    private final Map<String, Set<ResultListener<SHNDataRaw>>> rawDataListeners = new HashMap();
    private final Queue<ResponseHandler> responseQueue = new ArrayDeque<>();
    private BleRequest currentRequest;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Given("^a mock device is found with id '(.*?)'$")
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

    @When("^the mock device with id '(.*?)' receives data '([0-9A-F]*?)'")
    public void mock_device_receives_data_inline(final String id, final String data) {
        mock_device_receives_data(id, data);
    }

    @When("^the mock device with id '(.*?)' receives data$")
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

    @Then("^write occurred to mock device with id '(.*?)' with data '([0-9A-F]*?)'$")
    public void mock_device_data_written(final String deviceId, final String data) {
        CapabilityDiComm capability = (CapabilityDiComm) deviceCache
                .getDeviceMap().get(deviceId).getCapabilityForType(SHNCapabilityType.DI_COMM);

        final byte[] dataBytes = DatatypeConverter.parseHexBinary(data);

        verify(capability, timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).writeData(dataBytes);
    }

    @Given("^the BleStrategy is initialized with id '(.*?)'$")
    public void the_BleStrategy_is_initialized_with_id(String deviceId) throws Throwable {
        strategy = new BleStrategy(deviceId, deviceCache) {
            @Override
            protected void addTimeoutToRequest(BleRequest request) {
                currentRequest = request;
            }
        };
    }

    @Then("^the BleStrategy is available$")
    public void theBleStrategyIsAvailable() throws Throwable {
        assertTrue(strategy.isAvailable());
    }

    @Then("^the BleStrategy is not available$")
    public void theBleStrategyIsNotAvailable() throws Throwable {
        assertFalse(strategy.isAvailable());
    }

    @SuppressWarnings("unchecked")
    @When("^doing a put-properties for productid '(\\d+)' and port '(.*?)' without data$")
    public void doingAPutPropertiesForProductidAndPortWithoutData(int productId, String port) throws Throwable {
        doingAPutPropertiesForProductidAndPortWithData(productId, port, "{}");
    }

    @SuppressWarnings("unchecked")
    @When("^doing a put-properties for productid '(\\d+)' and port '(.*?)' with data$")
    public void doingAPutPropertiesForProductidAndPortWithData(int productId, String port, String data) throws Throwable {
        ResponseHandler handler = mock(ResponseHandler.class);
        responseQueue.add(handler);

        Map<String, Object> objData = new Gson().fromJson(data, HashMap.class);

        strategy.putProperties(objData, port, productId, handler);
    }

    @When("^doing a get-properties for productid '(\\d+)' and port '(.*?)'")
    public void doingAGetPropertiesForProductidAndPort(int productId, String port) {
        ResponseHandler handler = mock(ResponseHandler.class);
        responseQueue.add(handler);

        strategy.getProperties(port, productId, handler);
    }

    @Then("^the result is an error '(.*?)' with data '(.*?)'$")
    public void theResultIsAnErrorThisErrorWithDataTestData(String error, String data) throws Throwable {
        verify(responseQueue.remove()).onError(Error.valueOf(error), data);
    }

    @Then("^the result is an error '(.*?) with any data'$")
    public void theResultIsAnErrorThisError(String error) throws Throwable {
        verify(responseQueue.remove()).onError(Error.valueOf(error), anyString());
    }

    @Then("^the result is an error$")
    public void theResultIsAnError() throws Throwable {
        verify(responseQueue.remove()).onError(any(Error.class), anyString());
    }

    @Then("^the result is an error '(.*?)' without data$")
    public void theResultIsAnErrorWithoutData(String error) throws Throwable {
        verify(responseQueue.remove()).onError(Error.valueOf(error), null);
    }

    @Then("^the result is success with data '(.*?)'$")
    public void theResultIsSuccessWithDataTestData(String data) throws Throwable {
        verify(responseQueue.remove()).onSuccess(data);
    }

    @Then("^the result is success with data$")
    public void theResultIsSuccessWithLongDataTestData(String data) throws Throwable {
        verify(responseQueue.remove()).onSuccess(data);
    }

    @Then("^the result is success$")
    public void theResultIsSuccess() throws Throwable {
        verify(responseQueue.remove()).onSuccess(anyString());
    }

    @And("^no write occurred to mock device with id '(.*?)'$")
    public void noWriteOccurredToMockDeviceWithIdP(String deviceId) throws Throwable {
        CapabilityDiComm capability = (CapabilityDiComm) deviceCache
                .getDeviceMap().get(deviceId).getCapabilityForType(SHNCapabilityType.DI_COMM);

        verify(capability, times(0)).writeData((byte[]) any());
    }

    @And("^write occurred to mock device with id '(.*?)' with any data$")
    public void writeOccurredToMockDeviceWithIdP(String deviceId) throws Throwable {
        CapabilityDiComm capability = (CapabilityDiComm) deviceCache
                .getDeviceMap().get(deviceId).getCapabilityForType(SHNCapabilityType.DI_COMM);

        verify(capability, timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).writeData((byte[]) any());
    }

    @And("^the request times out$")
    public void theRequestTimesOut() throws Throwable {
        currentRequest.cancel("Timeout occurred.");
    }

}
