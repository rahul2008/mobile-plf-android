/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.port.brighteyes;

import android.os.Handler;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Map;

import static com.philips.cdp2.demouapp.port.brighteyes.WakeUpAlarmPortProperties.KEY_ENABLED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class WakeUpAlarmPortTest {

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    @Mock
    private Handler handlerMock;

    private WakeUpAlarmPort wakeUpAlarmPort;

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();
        HandlerProvider.enableMockedHandler(handlerMock);

        wakeUpAlarmPort = new WakeUpAlarmPort(communicationStrategyMock);
    }
    
    @Test
    public void givenAPortIsConstructed_whenSupportsSubscriptionIsInvoked_thenResultShouldBeTrue() {
        assertThat(wakeUpAlarmPort.supportsSubscription()).isTrue();
    }

    @Test
    public void givenAPortIsConstructed_whenSetEnabledIsInvoked_thenPutPropsShouldBeInvokedOnTheCommunicationStrategy() {
        wakeUpAlarmPort.setEnabled(true);
        final Map<String, Object> map = Collections.<String, Object>singletonMap(KEY_ENABLED, true);

        verify(communicationStrategyMock).putProperties(eq(map), eq(WakeUpAlarmPort.NAME), eq(WakeUpAlarmPort.PRODUCTID), any(ResponseHandler.class));
    }

}
