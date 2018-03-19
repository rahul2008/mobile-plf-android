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
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PairingPortTest {

    private static final String CLIENT_PROVIDER = "TODO";
    private static final String CLIENT_TYPE = "TODO";
    private static final String CLIENT_ID = "TODO";
    private static final String SECRET_KEY = "TODO";
    private static final String TYPE = "TODO";
    private static final String[] PERMISSIONS = {"TODO"};

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
    public void givenAPairingPort_whenPairingIsTriggered_thenPutPropertiesMustBeCalledOnCommunicationStrategy() {
        pairingport.triggerPairing(CLIENT_TYPE, CLIENT_ID, SECRET_KEY);

        verify(communicationStrategyMock).putProperties(ArgumentMatchers.<String, Object>anyMap(), eq(pairingport.getDICommPortName()), eq(pairingport.getDICommProductId()), any(ResponseHandler.class));
    }

    @Test
    public void givenAPairingPort_whenPairingIsTriggeredWithTypeAndPermissions_thenPutPropertiesMustBeCalledOnCommunicationStrategy() {
        pairingport.triggerPairing(CLIENT_PROVIDER, CLIENT_TYPE, CLIENT_ID, SECRET_KEY, TYPE, PERMISSIONS);

        verify(communicationStrategyMock).putProperties(ArgumentMatchers.<String, Object>anyMap(), eq(pairingport.getDICommPortName()), eq(pairingport.getDICommProductId()), any(ResponseHandler.class));
    }

}
