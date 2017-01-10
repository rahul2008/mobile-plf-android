package com.philips.cdp2.commlib.ble.request;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.dicommsupport.DiCommResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.philips.cdp.dicommclient.request.Error.TIMED_OUT;
import static com.philips.pins.shinelib.SHNDevice.State.Connected;
import static com.philips.pins.shinelib.dicommsupport.StatusCode.NoError;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BleRequestTest {
    private static final String CPP_ID = "Sinterklaas";
    private static final String PORT_NAME = "PoliticallyCorrectPiet";
    private static final int PRODUCT_ID = 1337;

    private BleRequest request;

    private Map<String, SHNDevice> deviceMap = new HashMap<>();

    @Mock
    private BleDeviceCache deviceCacheMock;

    @Mock
    private ResponseHandler responseHandlerMock;

    @Mock
    private DiCommResponse mockDicommResponse;

    @Mock
    private SHNDevice mockDevice;

    @Mock
    private CapabilityDiComm mockCapability;

    @Mock
    CountDownLatch mockInProgressLatch;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        deviceMap.put(CPP_ID, mockDevice);
        when(mockDevice.getCapabilityForType(SHNCapabilityType.DI_COMM)).thenReturn(mockCapability);
        when(mockDevice.getState()).thenReturn(Connected);
        when(deviceCacheMock.getDeviceMap()).thenReturn(deviceMap);
        doAnswer(new Answer() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                request.processDiCommResponse(mockDicommResponse);
                return null;
            }
        }).when(mockInProgressLatch).await();

        request = new BleGetRequest(deviceCacheMock, CPP_ID, PORT_NAME, PRODUCT_ID, responseHandlerMock);
        request.inProgressLatch = mockInProgressLatch;
    }

    @Test
    public void whenRequestIsCancelledAfterSuccessThenNoErrorIsReported() throws Exception {
        when(mockDicommResponse.getStatus()).thenReturn(NoError);
        when(mockDicommResponse.getPropertiesAsString()).thenReturn("{}");
        request.run();

        request.cancel("timeout");

        verify(responseHandlerMock, times(0)).onError(any(Error.class), anyString());
    }

    @Test
    public void whenTimeoutOccursBeforeRequestIsExecutedThenErrorIsReported() throws Exception {
        when(mockDicommResponse.getStatus()).thenReturn(NoError);
        when(mockDicommResponse.getPropertiesAsString()).thenReturn("{}");

        request.cancel("timeout");

        verify(responseHandlerMock).onError(eq(TIMED_OUT), anyString());
    }

    @Test
    public void whenTimeoutOccursBeforeRequestIsExecutedThenRequestIsNeverExecuted() throws Exception {
        when(mockDicommResponse.getStatus()).thenReturn(NoError);
        when(mockDicommResponse.getPropertiesAsString()).thenReturn("{}");
        request.cancel("timeout");

        request.run();

        verify(responseHandlerMock, times(0)).onSuccess(anyString());
    }

    @Test
    public void callsDisconnectAfterOnSuccess(){
        when(mockDicommResponse.getStatus()).thenReturn(NoError);
        when(mockDicommResponse.getPropertiesAsString()).thenReturn("{}");

        request.run();

        InOrder inOrder = inOrder(responseHandlerMock, mockDevice);
        inOrder.verify(responseHandlerMock).onSuccess(anyString());
        inOrder.verify(mockDevice).disconnect();
    }
}
