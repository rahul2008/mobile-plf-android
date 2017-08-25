/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.communication;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.request.BleRequest;
import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.philips.cdp2.commlib.ble.discovery.BleDiscoveryStrategy.SCAN_WINDOW_MILLIS;
import static com.philips.pins.shinelib.SHNCapabilityType.DI_COMM;
import static com.philips.pins.shinelib.SHNDevice.State.Connected;
import static com.philips.pins.shinelib.SHNDevice.State.Disconnected;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleCommunicationStrategyTestSteps {
    private static final long TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS = TimeUnit.SECONDS.toMillis(10);

    private class QueuedRequest {
        BleRequest request;
        ResponseHandler handler;

        QueuedRequest(final ResponseHandler handler) {
            this.handler = handler;
        }
    }

    private BleDeviceCache mDeviceCache;
    private BleCommunicationStrategy mStrategy;

    private Map<String, Set<ResultListener<SHNDataRaw>>> mRawDataListeners;
    private Deque<QueuedRequest> mRequestQueue;
    private Gson mGson;
    private Map<String, Integer> writtenBytes;

    private Map<String, SHNDevice.SHNDeviceListener> deviceListenerMap;

    @Mock
    private NetworkNode mockNetworkNode;

    @Mock
    CapabilityDiComm capability;

    @Captor
    private ArgumentCaptor<String> successStringCaptor;

    @Mock
    private Handler callbackHandlerMock;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Before
    public void setup() {
        initMocks(this);

        mRawDataListeners = new ConcurrentHashMap<>();
        writtenBytes = new ConcurrentHashMap<>();
        mDeviceCache = new BleDeviceCache(Executors.newSingleThreadScheduledExecutor());
        mRequestQueue = new ArrayDeque<>();
        mGson = new GsonBuilder().serializeNulls().create();
        deviceListenerMap = new ConcurrentHashMap<>();

        when(callbackHandlerMock.post(runnableCaptor.capture())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                runnableCaptor.getValue().run();
                return null;
            }
        });
    }

    @After
    public void tearDown() {
        validateMockitoUsage();
    }

    @Given("^the BLE communication strategy is initialized with id '(.*?)'$")
    public void theBLECommunicationStrategyIsInitializedWithId(String deviceId) {
        mStrategy = new BleCommunicationStrategy(deviceId, mDeviceCache, callbackHandlerMock) {

            @Override
            protected void dispatchRequest(final BleRequest request) {
                mRequestQueue.peekLast().request = request;
                super.dispatchRequest(request);
            }
        };
    }

    @Then("^the BLE communication strategy is available$")
    public void theBLECommunicationStrategyIsAvailable() {
        assertTrue("BLE communication strategy was not available", mStrategy.isAvailable());
    }

    @Then("^the BLE communication strategy is not available$")
    public void theBLECommunicationStrategyIsNotAvailable() {
        assertFalse("BLE communication strategy was available", mStrategy.isAvailable());
    }

    @Then("^the BLE communication strategy becomes unavailable$")
    public void theBLECommunicationStrategyBecomesUnavailable() {
        mDeviceCache.clear();
    }

    @Given("^a mock device is found with id '(.*?)'$")
    public void a_mock_device_is_found_with_id(final String deviceId) {
        final SHNDeviceFoundInfo info = mock(SHNDeviceFoundInfo.class);
        final SHNDevice device = mock(SHNDevice.class);
        mRawDataListeners.put(deviceId, new CopyOnWriteArraySet<ResultListener<SHNDataRaw>>());
        writtenBytes.put(deviceId, 0);

        resetCapability(deviceId);

        when(info.getShnDevice()).thenReturn(device);
        when(device.getAddress()).thenReturn(deviceId);
        when(device.getCapabilityForType(DI_COMM)).thenReturn(capability);
        when(device.getState()).thenReturn(Disconnected);

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                SHNDevice.SHNDeviceListener listener = (SHNDevice.SHNDeviceListener) invocation.getArguments()[0];
                deviceListenerMap.put(deviceId, listener);
                return null;
            }
        }).when(device).registerSHNDeviceListener(any(SHNDevice.SHNDeviceListener.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                when(device.getState()).thenReturn(Connected);
                if (deviceListenerMap.containsKey(deviceId)) {
                    deviceListenerMap.get(deviceId).onStateUpdated(device);
                }
                return null;
            }
        }).when(device).connect(30000L);

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                when(device.getState()).thenReturn(Disconnected);
                if (deviceListenerMap.containsKey(deviceId)) {
                    deviceListenerMap.get(deviceId).onStateUpdated(device);
                }
                return null;
            }
        }).when(device).disconnect();

        when(mockNetworkNode.getCppId()).thenReturn(deviceId);

        mDeviceCache.addDevice(device, mockNetworkNode, new BleDeviceCache.ExpirationCallback() {
            @Override
            public void onCacheExpired(NetworkNode networkNode) {
                // Ignored
            }
        }, SCAN_WINDOW_MILLIS);
    }

    private void resetCapability(final String deviceId) {
        reset(capability);

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                mRawDataListeners.get(deviceId).add((ResultListener<SHNDataRaw>) invocation.getArguments()[0]);
                return null;
            }
        }).when(capability).addDataListener(any(ResultListener.class));

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                mRawDataListeners.get(deviceId).remove((ResultListener<SHNDataRaw>) invocation.getArguments()[0]);
                return null;
            }
        }).when(capability).removeDataListener(any(ResultListener.class));
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
        final Set<ResultListener<SHNDataRaw>> listeners = Collections.unmodifiableSet(mRawDataListeners.get(id));

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
        mRequestQueue.add(new QueuedRequest(handler));

        mStrategy.getProperties(port, productId, handler);
    }

    @SuppressWarnings("unchecked")
    @When("^doing a put-properties for productid '(\\d+)' and port '(.*?)' with null data$")
    public void doingAPutPropertiesForProductidAndPortWithNullData(int productId, String port) {
        doingAPutPropertiesForProductidAndPortWithData(productId, port, null);
    }

    @SuppressWarnings("unchecked")
    @When("^doing a put-properties for productid '(\\d+)' and port '(.*?)' with empty data$")
    public void doingAPutPropertiesForProductidAndPortWithEmptyData(int productId, String port) {
        doingAPutPropertiesForProductidAndPortWithData(productId, port, "{}");
    }

    @SuppressWarnings("unchecked")
    @When("^doing a put-properties for productid '(\\d+)' and port '(.*?)' with data$")
    public void doingAPutPropertiesForProductidAndPortWithData(int productId, String port, String data) {
        ResponseHandler handler = mock(ResponseHandler.class);
        mRequestQueue.add(new QueuedRequest(handler));

        Map<String, Object> objData = new Gson().fromJson(data, HashMap.class);

        mStrategy.putProperties(objData, port, productId, handler);
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

    @Then("^no write occurred to mock device with id '(.*?)'$")
    public void noWriteOccurredToMockDeviceWithIdP(String deviceId) {

        verify(capability, times(0)).writeData((byte[]) any());
        resetCapability(deviceId);
    }

    @Then("^write occurred to mock device with id '(.*?)' with any data$")
    public void writeOccurredToMockDeviceWithIdP(String deviceId) {
        getWrittenBytesForDevice(deviceId);
    }

    @Then("^write occurred to mock device with id '(.*?)' with data '([0-9A-F]*?)'$")
    public void mock_device_data_written(final String deviceId, final String data) {
        writeOccurredToMockDeviceWithIdWithPattern(deviceId, data);
    }

    @Then("^write occurred to mock device with id '(.*?)' with pattern '(.*?)'$")
    public void writeOccurredToMockDeviceWithIdWithPattern(String deviceId, String pattern) {
        Matcher m = getMatcherForWrite(deviceId, pattern);
        assertTrue("pattern '" + pattern + "' was not found", m.matches());
    }

    @Then("^write occurred to mock device with id '(.*?)' with packet '(.*?)' and payload equivalent to$")
    public void writeOccurredToMockDeviceWithPacketAndPayloadEquivalentTo(String deviceId, String message, String jsonPayload) {
        writeOccurredToMockDeviceWithPacketAndPayloadEquivalentToInline(deviceId, message, jsonPayload);
    }

    @Then("^write occurred to mock device with id '(.*?)' with packet '(.*?)' and payload equivalent to '(.*?)'$")
    public void writeOccurredToMockDeviceWithPacketAndPayloadEquivalentToInline(String deviceId, String packet, String expectedPayload) {
        Matcher m = getMatcherForWrite(deviceId, packet);
        assertTrue("packet '" + packet + "' was not found", m.matches());

        String payload = m.group(1);
        String payloadString = new String(DatatypeConverter.parseHexBinary(payload), Charset.forName("UTF-8"));

        assertEqualsJson(expectedPayload, payloadString);
    }

    @Then("^total bytes written to id '(.*?)' is '(\\d+)'$")
    public void totalBytesWrittenToIdPIs(String deviceId, int expectedBytes) throws Throwable {
        assertEquals(expectedBytes, writtenBytes.get(deviceId).intValue());
    }

    @Then("^the request times out$")
    public void theRequestTimesOut() {
        mRequestQueue.peek().request.cancel("Timeout occurred.");
    }

    @Then("^the result is an error '(.*?)' with data '(.*?)'$")
    public void theResultIsAnErrorWithData(String error, String data) {
        verify(mRequestQueue.remove().handler, timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).onError(eq(Error.valueOf(error)), eq(data));
    }

    @Then("^the result is an error '(.*?)' with any data$")
    public void theResultIsAnErrorWithAnyData(String error) {
        verify(mRequestQueue.remove().handler, timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).onError(eq(Error.valueOf(error)), anyString());
    }

    @Then("^the result is an error$")
    public void theResultIsAnError() {
        verify(mRequestQueue.remove().handler, timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).onError(any(Error.class), or(anyString(), (String) isNull()));
    }

    @Then("^the result is an error '(.*?)' without data$")
    public void theResultIsAnErrorWithoutData(String error) {
        verify(mRequestQueue.remove().handler, timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).onError(eq(Error.valueOf(error)), (String) isNull());
    }

    @Then("^the result for request nr '(\\d+)' is an error '(.*?)'$")
    public void theResultForRequestNrIsAnError(int requestNumber, String error) {
        theResultIsAnErrorWithAnyData(error);
    }

    @Then("^the result is success with data '(.*?)'$")
    public void theResultIsSuccessWithDataTestData(String data) {
        verify(mRequestQueue.remove().handler).onSuccess(data);
    }

    @Then("^the result is success with data$")
    public void theResultIsSuccessWithLongDataTestData(String expectedJsonString) {
        verify(mRequestQueue.remove().handler).onSuccess(successStringCaptor.capture());

        if (successStringCaptor.getAllValues().isEmpty()) fail("No result captured.");

        List<String> allValues = successStringCaptor.getAllValues();
        String actualJsonString = allValues.get(allValues.size() - 1);

        assertEqualsJson(expectedJsonString, actualJsonString);
    }

    @Then("^the result is success without data$")
    public void theResultIsSuccessWithoutData() {
        verify(mRequestQueue.remove().handler).onSuccess(null);
    }

    @Then("^the result is success$")
    public void theResultIsSuccess() {
        verify(mRequestQueue.remove().handler).onSuccess(successStringCaptor.capture());
    }

    @Then("^the json result contains the key '(.*?)'$")
    public void theJsonResultContainsTheKey(String key) {
        assertTrue("key '" + key + "' not found in: " + getObjectMapFromLastSuccessfulResult(), getObjectMapFromLastSuccessfulResult().containsKey(key));
    }

    @Then("^the json value for key '(.*?)' has length '(\\d+)'$")
    public void theJsonStringValueHasLength(String key, int expectedLength) {
        Map<String, Object> objectMap = getObjectMapFromLastSuccessfulResult();

        Object object = objectMap.get(key);
        assertTrue(object.getClass() + " is not an instance of String ", object instanceof String);
        assertEquals(expectedLength, ((String) object).length());
    }

    @Given("^there is an appliance$")
    public void thereIsAnAppliance() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("^a port has been added with name '(.*?)' and productID '(\\d+)'$")
    public void aPortHasBeenAddedWithNameXAndProductID(String port, int productId) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^application subscribes to port '(.*?)' with productID '(\\d+)'$")
    public void applicationSubscribesToPortXWithProductID(String port, int productId) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^subscription is successful$")
    public void subscriptionIsSuccessful() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^application unsubscribes from port '(.*?)' with productID '(\\d+)'$")
    public void applicationUnsubscribesFromPortXWithProductID(String port, int productId) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^cancelling subscription is successful$")
    public void cancellingSubscriptionIsSuccessful() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^port '(.*?)' with productID '(\\d+)' sends a notification$")
    public void portXWithProductIDSendsANotification(String port, int productId) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^application receives a notification from port '(.*?)' with productID '(\\d+)'$")
    public void applicationReceivesANotificationFromPortXWithProductID(String port, int productId) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @NonNull
    private byte[] getWrittenBytesForDevice(String deviceId) {
        ArgumentCaptor<byte[]> argCaptor = ArgumentCaptor.forClass(byte[].class);

        verify(capability, timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS)).writeData(argCaptor.capture());

        byte[] bytes = argCaptor.getValue();
        writtenBytes.put(deviceId, writtenBytes.get(deviceId) + bytes.length);
        resetCapability(deviceId);

        return bytes;
    }

    @NonNull
    private Matcher getMatcherForWrite(String deviceId, String regex) {
        Pattern pattern = Pattern.compile(regex);

        String hexData = DatatypeConverter.printHexBinary(getWrittenBytesForDevice(deviceId));
        return pattern.matcher(hexData);
    }

    private Map<String, Object> getObjectMapFromLastSuccessfulResult() {
        if (successStringCaptor.getAllValues().isEmpty()) fail("No result captured.");

        List<String> allValues = successStringCaptor.getAllValues();
        String jsonString = allValues.get(allValues.size() - 1);

        Map<String, Object> objectMap = new ConcurrentHashMap<>();
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
