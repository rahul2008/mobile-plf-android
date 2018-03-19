/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.os.Handler;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PairingPortTest {

    private static final String CLIENT_PROVIDER = "TestClientProvider";
    private static final String CLIENT_TYPE = "TestClientType";
    private static final String CLIENT_ID = "TestClientId";
    private static final String SECRET_KEY = "TestSecretKey";
    private static final String TYPE = "TestType";
    private static final String[] PERMISSIONS = {"TestPermission"};

    private PairingPort pairingport;

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    @Mock
    private Handler handlerMock;

    @Captor
    private ArgumentCaptor<Map<String, Object>> captor;

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();
        HandlerProvider.enableMockedHandler(handlerMock);

        pairingport = new PairingPort(communicationStrategyMock);
    }

    @Test
    public void givenAPairingPort_whenPairingWithDefaults_thenPutPropertiesMustBeCalledOnCommunicationStrategy() {
        pairingport.pair(CLIENT_TYPE, CLIENT_ID, SECRET_KEY);

        verify(communicationStrategyMock).putProperties(captor.capture(), eq(pairingport.getDICommPortName()), eq(pairingport.getDICommProductId()), any(ResponseHandler.class));
        Map<String, Object> map = captor.getValue();

        assertThat(map.containsKey(PairingPort.METHOD_PAIR)).isTrue();
    }

    @Test
    public void givenAPairingPort_whenPairingWithCustomTypeAndPermissions_thenPutPropertiesMustBeCalledOnCommunicationStrategy() {
        pairingport.pair(CLIENT_PROVIDER, CLIENT_TYPE, CLIENT_ID, SECRET_KEY, TYPE, PERMISSIONS);

        verify(communicationStrategyMock).putProperties(captor.capture(), eq(pairingport.getDICommPortName()), eq(pairingport.getDICommProductId()), any(ResponseHandler.class));
        Map<String, Object> map = captor.getValue();

        assertThat(map.containsKey(PairingPort.METHOD_PAIR)).isTrue();
    }

    @Test
    public void givenAPairingPort_whenUnpairingIsTriggeredWithType_thenPutPropertiesMustBeCalledOnCommunicationStrategy() {
        pairingport.unpair(CLIENT_PROVIDER, CLIENT_TYPE, CLIENT_ID, SECRET_KEY, TYPE);

        verify(communicationStrategyMock).putProperties(captor.capture(), eq(pairingport.getDICommPortName()), eq(pairingport.getDICommProductId()), any(ResponseHandler.class));
        Map<String, Object> map = captor.getValue();

        assertThat(map.containsKey(PairingPort.METHOD_UNPAIR)).isTrue();
    }

}
