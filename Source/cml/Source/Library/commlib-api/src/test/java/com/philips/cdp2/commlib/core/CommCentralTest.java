/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cdp2.commlib.core.store.NetworkNodeDatabase;
import com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseFactory;
import com.philips.cdp2.commlib.core.util.HandlerProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.ref.WeakReference;
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
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CommCentral.class, NetworkNodeDatabaseFactory.class})
public class CommCentralTest {

    @Mock
    private Handler handlerMock;

    @Mock
    private ApplianceFactory applianceFactoryMock;

    private BarContext barTransportContextMock;
    private FooContext fooTransportContextMock;

    @Mock
    private DiscoveryStrategy someDiscoveryStrategyMock;

    @Mock
    private DiscoveryStrategy anotherDiscoveryStrategyMock;

    @Mock
    private RuntimeConfiguration runtimeConfigurationMock;

    @Mock
    private ApplianceManager applianceManagerMock;

    @Mock
    private NetworkNodeDatabase NetworkNodeDatabaseMock;

    @Mock
    private Context contextMock;

    private Set<String> emptyDeviceTypes = Collections.emptySet();
    private Set<String> emptyModelIds = Collections.emptySet();
    private Set<String> modelIds = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("1234AB", "5678CD")));

    private CommCentral commCentral;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockStatic(NetworkNodeDatabaseFactory.class);
        when(NetworkNodeDatabaseFactory.create(runtimeConfigurationMock)).thenReturn(NetworkNodeDatabaseMock);

        DICommLog.disableLogging();
        HandlerProvider.enableMockedHandler(handlerMock);

        barTransportContextMock = new BarContext();
        fooTransportContextMock = new FooContext();

        setTestingContext(contextMock);

        PowerMockito.whenNew(ApplianceManager.class).withAnyArguments().thenReturn(applianceManagerMock);

        commCentral = new CommCentral(applianceFactoryMock, runtimeConfigurationMock, barTransportContextMock, fooTransportContextMock);
    }

    @After
    public void tearDown() {
        Whitebox.setInternalState(CommCentral.class, "instanceWeakReference", new WeakReference<CommCentral>(null));
    }

    @Test
    @SuppressWarnings("unused")
    public void givenACommCentralInstance_whenASecondInstanceIsCreated_thenAnErrorMustBeThrown() {
        try {
            new CommCentral(applianceFactoryMock, runtimeConfigurationMock, barTransportContextMock, fooTransportContextMock);
            fail();
        } catch (UnsupportedOperationException ignored) {
        }
    }

    @Test
    public void givenACommCentralInstance_whenTheAppIdIsObtained_thenATemporaryAppIdMustBeReturned() {
        assertThat(CommCentral.getAppIdProvider().getAppId()).isNotEmpty();
    }

    @Test
    public void givenACommCentralInstance_whenApplianceManagerIsObtained_thenApplianceManagerMustBeReturned() {
        assertThat(commCentral.getApplianceManager()).isNotNull();
    }

    @Test
    public void givenACommCentralInstance_whenStartDiscovery_thenInvokeStartDiscoveryOnAllProvidedDiscoveryStrategies() {
        try {
            commCentral.startDiscovery();

            verify(someDiscoveryStrategyMock).start(emptyModelIds);
            verify(anotherDiscoveryStrategyMock).start(emptyModelIds);
        } catch (MissingPermissionException e) {
            fail();
        }
    }

    @Test
    public void givenACommCentralInstance_whenStartDiscoveryWithModelIds_thenInvokeStartDiscoveryOnAllProvidedDiscoveryStrategiesWithModelIds() {
        try {
            commCentral.startDiscovery(modelIds);

            verify(someDiscoveryStrategyMock).start(modelIds);
            verify(anotherDiscoveryStrategyMock).start(modelIds);
        } catch (MissingPermissionException e) {
            fail();
        }
    }

    @Test
    public void givenACommCentralInstance_whenStopDiscovery_thenInvokeStopDiscoveryOnAllProvidedDiscoveryStrategies() {
        commCentral.stopDiscovery();

        verify(someDiscoveryStrategyMock).stop();
        verify(anotherDiscoveryStrategyMock).stop();
    }

    @Test
    public void givenAnAppIdIsSet_whenTheAppIdIsObtained_thenTheSameAppIdMustBeReturned() {
        final String appId = "4pp1d3nt1f13r";

        CommCentral.getAppIdProvider().setAppId(appId);
        assertEquals("AppId must be equal.", appId, CommCentral.getAppIdProvider().getAppId());
    }

    @Test
    public void givenACommCentralInstance_whenClearingDiscoveredAppliances_thenInvokeClearDiscoveredNetworkNodesOnAllProvidedDiscoveryStrategies() {
        commCentral.clearDiscoveredAppliances();

        verify(someDiscoveryStrategyMock).clearDiscoveredNetworkNodes();
        verify(anotherDiscoveryStrategyMock).clearDiscoveredNetworkNodes();
    }

    @Test
    public void givenFooAndBarContextsAreProvidedToComCentral_whenFooIsRequested_thenItIsReturned() {

        FooContext fooFromCentral = commCentral.getTransportContext(FooContext.class);

        assertThat(fooTransportContextMock).isEqualTo(fooFromCentral);
    }

    @Test
    public void givenFooAndBarContextsAreProvidedToComCentral_whenBarIsRequested_thenItIsReturned() {

        BarContext barFromCentral = commCentral.getTransportContext(BarContext.class);

        assertThat(barTransportContextMock).isEqualTo(barFromCentral);
    }

    @Test(expected = TransportUnavailableException.class)
    public void givenFooAndBarContextsAreProvidedToComCentral_whenBazIsRequested_thenTransportUnavailableExceptionIsThrown() {

        commCentral.getTransportContext(BazContext.class);
    }

    @Test(expected = TransportUnavailableException.class)
    public void givenFooAndBarContextsAreProvidedToComCentral_whenBaseClassIsRequested_thenTransportUnavailableExceptionIsThrown() {

        commCentral.getTransportContext(TransportContext.class);
    }

    @SuppressWarnings("ConstantConditions")
    private class FooContext implements TransportContext {

        @Nullable
        @Override
        public DiscoveryStrategy getDiscoveryStrategy() {
            return someDiscoveryStrategyMock;
        }

        @NonNull
        @Override
        public CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
            return null;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private class BarContext implements TransportContext {

        @Nullable
        @Override
        public DiscoveryStrategy getDiscoveryStrategy() {
            return anotherDiscoveryStrategyMock;
        }

        @NonNull
        @Override
        public CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
            return null;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private class BazContext implements TransportContext {

        @Nullable
        @Override
        public DiscoveryStrategy getDiscoveryStrategy() {
            return null;
        }

        @NonNull
        @Override
        public CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
            return null;
        }
    }
}
