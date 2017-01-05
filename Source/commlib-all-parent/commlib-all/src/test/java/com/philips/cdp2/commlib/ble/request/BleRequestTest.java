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
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static com.philips.pins.shinelib.SHNDevice.State.Connected;
import static com.philips.pins.shinelib.dicommsupport.StatusCode.NoError;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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

    @Before
    public void setUp() {
        initMocks(this);

        deviceMap.put(CPP_ID, mockDevice);
        when(mockDevice.getCapabilityForType(SHNCapabilityType.DI_COMM)).thenReturn(mockCapability);
        when(mockDevice.getState()).thenReturn(Connected);
        when(deviceCacheMock.getDeviceMap()).thenReturn(deviceMap);

        request = new BleGetRequest(deviceCacheMock, CPP_ID, PORT_NAME, PRODUCT_ID, responseHandlerMock);
    }

    @Test
    public void whenRequestIsCancelledAfterSuccessThenNoErrorIsReported() throws Exception {

        when(mockDicommResponse.getStatus()).thenReturn(NoError);
        when(mockDicommResponse.getPropertiesAsString()).thenReturn("{}");
        request.execute();
        request.processDicommResponse(mockDicommResponse);
        request.cleanup();

        request.cancel("timeout");

        verify(responseHandlerMock, times(0)).onError(any(Error.class), anyString());
    }
}
