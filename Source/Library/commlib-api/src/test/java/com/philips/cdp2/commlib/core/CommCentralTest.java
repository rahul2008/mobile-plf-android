/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core;

import android.content.Context;
import android.os.Handler;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.philips.cdp2.commlib.core.util.ContextProvider.setTestingContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommCentralTest {

    @Mock
    private Handler handlerMock;

    @Mock
    private ApplianceFactory applianceFactoryMock;

    @Mock
    private TransportContext someTransportContextMock;

    @Mock
    private DiscoveryStrategy someDiscoveryStrategyMock;

    @Mock
    private TransportContext anotherTransportContextMock;

    @Mock
    private DiscoveryStrategy anotherDiscoveryStrategyMock;

    @Mock
    private Context contextMock;

    private CommCentral commCentral;
    private Set<String> emptyDeviceTypes = Collections.emptySet();
    private Set<String> emptyModelIds = Collections.emptySet();
    private Set<String> modelIds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("1234AB", "5678CD")));

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();
        HandlerProvider.enableMockedHandler(handlerMock);

        when(someTransportContextMock.getDiscoveryStrategy()).thenReturn(someDiscoveryStrategyMock);
        when(anotherTransportContextMock.getDiscoveryStrategy()).thenReturn(anotherDiscoveryStrategyMock);

        setTestingContext(contextMock);

        commCentral = new CommCentral(applianceFactoryMock, someTransportContextMock, anotherTransportContextMock);
    }

    @Test
    public void whenCommCentralConstructed_thenTemporaryAppIdMustBeSet() {
        assertThat(CommCentral.getAppIdProvider().getAppId()).isNotEmpty();
    }

    @Test
    public void whenCommCentralConstructed_thenApplianceManagerMustBeSet() {
        assertThat(commCentral.getApplianceManager()).isNotNull();
    }

    @Test
    public void whenStartDiscovery_thenInvokeStartDiscoveryOnAllProvidedDiscoveryStrategies() {
        try {
            commCentral.startDiscovery();

            verify(someDiscoveryStrategyMock).start(emptyDeviceTypes, emptyModelIds);
            verify(anotherDiscoveryStrategyMock).start(emptyDeviceTypes, emptyModelIds);
        } catch (MissingPermissionException e) {
            fail();
        }
    }

    @Test
    public void whenStartDiscoveryWithModelIds_thenInvokeStartDiscoveryOnAllProvidedDiscoveryStrategiesWithModelIds() {
        try {
            commCentral.startDiscovery(modelIds);

            verify(someDiscoveryStrategyMock).start(emptyDeviceTypes, modelIds);
            verify(anotherDiscoveryStrategyMock).start(emptyDeviceTypes, modelIds);
        } catch (MissingPermissionException e) {
            fail();
        }
    }

    @Test
    public void whenStopDiscovery_thenInvokeStopDiscoveryOnAllProvidedDiscoveryStrategies() {
        commCentral.stopDiscovery();

        verify(someDiscoveryStrategyMock).stop();
        verify(anotherDiscoveryStrategyMock).stop();
    }

    @Test
    public void whenAppIdIsSet_thenSameAppIdMustBeReturnedViaGet() {
        final String appId = "4pp1d3nt1f13r";

        CommCentral.getAppIdProvider().setAppId(appId);
        assertEquals("AppId must be equal.", appId, CommCentral.getAppIdProvider().getAppId());
    }
}
