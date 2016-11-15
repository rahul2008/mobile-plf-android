package com.philips.cdp2.commlib.request;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.LocalRequestType;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.dicommsupport.DiCommMessage;
import com.philips.pins.shinelib.dicommsupport.DiCommResponse;
import com.philips.pins.shinelib.dicommsupport.MessageType;
import com.philips.pins.shinelib.dicommsupport.StatusCode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

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
        when(deviceCacheMock.getDeviceMap()).thenReturn(deviceMap);

        request = new BleRequest(deviceCacheMock, CPP_ID, PORT_NAME, PRODUCT_ID, LocalRequestType.GET, null, responseHandlerMock);
    }

    @Test
    public void whenRequestIsCancelledAfterSuccessThenNoErrorIsReported() throws Exception {

        when(mockDicommResponse.getStatus()).thenReturn(StatusCode.NoError);
        when(mockDicommResponse.getPropertiesAsString()).thenReturn("{}");
        request.execute();
        request.processDicommResponse(mockDicommResponse);

        request.cancel("timeout");

        verify(responseHandlerMock, times(0)).onError(any(Error.class), anyString());
    }
}