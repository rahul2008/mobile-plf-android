/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.strategy;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;

import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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
    private Gson mGson;

    @Captor
    private ArgumentCaptor<String> successStringCaptor;

    @Before
    public void setup() {
        initMocks(this);

        mGson = new GsonBuilder().serializeNulls().create();
    }

    @Given("^the BleStrategy is initialized with id '(.*?)'$")
    public void the_BleStrategy_is_initialized_with_id(String deviceId) {
        strategy = new BleStrategy(deviceId, deviceCache) {
            @Override
            protected Timer addTimeoutToRequest(BleRequest request) {
                currentRequest = request;

                return null;
            }
        };
    }

    @Then("^the BleStrategy is available$")
    public void theBleStrategyIsAvailable() {
        assertTrue(strategy.isAvailable());
    }

    @Then("^the BleStrategy is not available$")
    public void theBleStrategyIsNotAvailable() {
        assertFalse(strategy.isAvailable());
    }

    @Given("^a mock device is found with id '(.*?)'$")
    public void a_mock_device_is_found_with_id(final String deviceId) {
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

    @When("^the mock device with id '(.*?)' receives data '([0-9A-F]*?)'$")
    public void mock_device_receives_data_inline(final String id, final String data) {
        mock_device_receives_data(id, data);
    }

    @When("^the mock device with id '(.*?)' receives data '([0-9A-F]*?)' repeated '(\\d+)' times$")
    public void mock_device_receives_data_repeatedly(final String id, final String data, int times) {
        for (int i = 0; i < times; i++) {
            mock_device_receives_data(id, data);
        }
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

    @When("^doing a get-properties for productid '(\\d+)' and port '(.*?)'$")
    public void doingAGetPropertiesForProductidAndPort(int productId, String port) {
        ResponseHandler handler = mock(ResponseHandler.class);
        responseQueue.add(handler);

        strategy.getProperties(port, productId, handler);
    }

    @SuppressWarnings("unchecked")
    @When("^doing a put-properties for productid '(\\d+)' and port '(.*?)' without data$")
    public void doingAPutPropertiesForProductidAndPortWithoutData(int productId, String port) {
        doingAPutPropertiesForProductidAndPortWithData(productId, port, "{}");
    }

    @SuppressWarnings("unchecked")
    @When("^doing a put-properties for productid '(\\d+)' and port '(.*?)' with data$")
    public void doingAPutPropertiesForProductidAndPortWithData(int productId, String port, String data) {
        ResponseHandler handler = mock(ResponseHandler.class);
        responseQueue.add(handler);

        Map<String, Object> objData = new Gson().fromJson(data, HashMap.class);

        strategy.putProperties(objData, port, productId, handler);
    }

    @When("^doing a put-properties for productid '(\\d+)' and port '(.*?)' with data '(.*?)'$")
    public void doingAPutPropertiesForProductidAndPortWithDataInline(int productId, String port, String data) {
        doingAPutPropertiesForProductidAndPortWithData(productId, port, data);
    }

    @When("^doing a put-properties for productid '(\\d+)' and port '(.*?)' with any data with size '(\\d+)'$")
    public void doingAPutPropertiesForProductidAndPortWithAnyDataWithSize(int productId, String port, int jsonSize) {
        final String startString = "{\"chiquita\":\"ba";
        final String endString = "\"}";
        int remainingSize = jsonSize;

        remainingSize -= startString.length();
        remainingSize -= endString.length();

        StringBuilder b = new StringBuilder();
        b.append(startString);

        for (; remainingSize > 1; remainingSize -= 2) {
            b.append("na");
        }

        if (remainingSize == 1) {
            b.append("!");
        }
        b.append(endString);

        final String jsonData = b.toString();
        doingAPutPropertiesForProductidAndPortWithData(productId, port, jsonData);
    }

    @And("^no write occurred to mock device with id '(.*?)'$")
    public void noWriteOccurredToMockDeviceWithIdP(String deviceId) {
        CapabilityDiComm capability = getCapabilityForDevice(deviceId);

        verify(capability, times(0)).writeData((byte[]) any());
    }

    @And("^write occurred to mock device with id '(.*?)' with any data$")
    public void writeOccurredToMockDeviceWithIdP(String deviceId) {
        CapabilityDiComm capability = getCapabilityForDevice(deviceId);

        verify(capability, timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).writeData((byte[]) any());
    }

    @Then("^write occurred to mock device with id '(.*?)' with data '([0-9A-F]*?)'$")
    public void mock_device_data_written(final String deviceId, final String data) {
        writeOccurredToMockDeviceWithIdWithPattern(deviceId, data);
    }

    @Then("^write occurred to mock device with id '(.*?)' with pattern '(.*?)'$")
    public void writeOccurredToMockDeviceWithIdWithPattern(String deviceId, String pattern) {
        Matcher m = getMatcherForWrite(deviceId, pattern);
        assertTrue(m.matches());
    }

    @Then("^write occurred to mock device with id '(.*?)' with packet '(.*?)' and payload equivalent to$")
    public void writeOccurredToMockDeviceWithPacketAndPayloadEquivalentTo(String deviceId, String message, String jsonPayload) {
        writeOccurredToMockDeviceWithPacketAndPayloadEquivalentToInline(deviceId, message, jsonPayload);
    }

    @Then("^write occurred to mock device with id '(.*?)' with packet '(.*?)' and payload equivalent to '(.*?)'$")
    public void writeOccurredToMockDeviceWithPacketAndPayloadEquivalentToInline(String deviceId, String message, String expectedPayload) {
        Matcher m = getMatcherForWrite(deviceId, message);
        assertTrue(m.matches());

        String payload = m.group(1);
        String payloadString = new String(DatatypeConverter.parseHexBinary(payload), Charset.forName("UTF-8"));

        assertEqualsJson(expectedPayload, payloadString);
    }

    @And("^the request times out$")
    public void theRequestTimesOut() {
        currentRequest.cancel("Timeout occurred.");
    }

    @Then("^the result is an error '(.*?)' with data '(.*?)'$")
    public void theResultIsAnErrorWithData(String error, String data) {
        verify(responseQueue.remove(), timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).onError(eq(Error.valueOf(error)), eq(data));
    }

    @Then("^the result is an error '(.*?)' with any data$")
    public void theResultIsAnErrorWithAnyData(String error) {
        verify(responseQueue.remove(), timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).onError(eq(Error.valueOf(error)), anyString());
    }

    @Then("^the result is an error$")
    public void theResultIsAnError() {
        verify(responseQueue.remove(), timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).onError(any(Error.class), anyString());
    }

    @Then("^the result is an error '(.*?)' without data$")
    public void theResultIsAnErrorWithoutData(String error) {
        verify(responseQueue.remove(), timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).onError(eq(Error.valueOf(error)), Matchers.isNull(String.class));
    }

    @Then("^the result is success with data '(.*?)'$")
    public void theResultIsSuccessWithDataTestData(String data) {
        verify(responseQueue.remove()).onSuccess(data);
    }

    @Then("^the result is success with data$")
    public void theResultIsSuccessWithLongDataTestData(String expectedJsonString) {
        verify(responseQueue.remove()).onSuccess(successStringCaptor.capture());

        if (successStringCaptor.getAllValues().isEmpty()) fail("No result captured.");

        List<String> allValues = successStringCaptor.getAllValues();
        String actualJsonString = allValues.get(allValues.size() - 1);

        assertEqualsJson(expectedJsonString, actualJsonString);
    }

    @Then("^the result is success$")
    public void theResultIsSuccess() {
        verify(responseQueue.remove()).onSuccess(successStringCaptor.capture());
    }

    @And("^the json result contains the key '(.*?)'$")
    public void theJsonResultContainsTheKey(String key) {
        assertTrue(getObjectMapFromLastSuccessfulResult().containsKey(key));
    }

    @And("^the json value for key '(.*?)' has length '(\\d+)'$")
    public void theJsonStringValueHasLength(String key, int expectedLength) {
        Map<String, Object> objectMap = getObjectMapFromLastSuccessfulResult();

        Object object = objectMap.get(key);
        assertTrue(object instanceof String);
        assertEquals(expectedLength, ((String) object).length());
    }

    @NonNull
    private Matcher getMatcherForWrite(String deviceId, String regex) {
        Pattern pattern = Pattern.compile(regex);

        ArgumentCaptor<byte[]> argCaptor = ArgumentCaptor.forClass(byte[].class);

        CapabilityDiComm capability = getCapabilityForDevice(deviceId);
        verify(capability, timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).writeData(argCaptor.capture());

        String hexData = DatatypeConverter.printHexBinary(argCaptor.getValue());
        return pattern.matcher(hexData);
    }

    private CapabilityDiComm getCapabilityForDevice(String deviceId) {
        return (CapabilityDiComm) deviceCache
                .getDeviceMap().get(deviceId).getCapabilityForType(SHNCapabilityType.DI_COMM);
    }

    private Map<String, Object> getObjectMapFromLastSuccessfulResult() {
        if (successStringCaptor.getAllValues().isEmpty()) fail("No result captured.");

        List<String> allValues = successStringCaptor.getAllValues();
        String jsonString = allValues.get(allValues.size() - 1);

        Map<String, Object> objectMap = new HashMap<>();
        objectMap = mGson.fromJson(jsonString, objectMap.getClass());

        return objectMap;
    }

    private void assertEqualsJson(String expectedJsonString, String actualJsonString) {
        JsonParser parser = new JsonParser();

        JsonElement expected = parser.parse(expectedJsonString);
        JsonElement actual = parser.parse(actualJsonString);

        assertEquals(expected, actual);
    }
}
