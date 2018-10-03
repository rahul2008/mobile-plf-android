/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.context;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.bluelib.plugindefinition.ReferenceNodeDeviceDefinitionInfo;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNCentral.SHNCentralListener;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.tagging.SHNTagger;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.utility.SHNLogger.LoggerImplementation;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@PrepareForTest({SHNLogger.class, SHNTagger.class})
@RunWith(PowerMockRunner.class)
public class BleTransportContextTest {

    @Mock
    private Context contextMock;

    @Mock
    private AppInfraInterface appInfraInterfaceMock;

    @Mock
    private AppTaggingInterface appTaggingInterfaceMock;

    @Mock
    private SHNCentral shnCentralMock;

    @Mock
    private RuntimeConfiguration runtimeConfigurationMock;

    @Mock
    private DeviceCache deviceCacheMock;

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    @Mock
    private DiscoveryStrategy discoveryStrategyMock;

    @Captor
    private ArgumentCaptor<SHNCentralListener> shnCentralListenerArgumentCaptor;

    @SuppressWarnings("unused, FieldCanBeLocal")
    private BleTransportContext bleTransportContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(SHNLogger.class);
        mockStatic(SHNTagger.class);

        when(runtimeConfigurationMock.getContext()).thenReturn(contextMock);
        when(runtimeConfigurationMock.getAppInfraInterface()).thenReturn(appInfraInterfaceMock);
        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterfaceMock);
        when(shnCentralMock.getShnCentralState()).thenReturn(SHNCentral.State.SHNCentralStateReady);
    }

    @Test
    public void givenBleTransportContextIsConstructed_whenLogIsEnabledInRuntimeConfiguration_thenALoggerIsRegisteredOnBluelib() {
        when(runtimeConfigurationMock.isLogEnabled()).thenReturn(true);

        constructBleTransportContext();

        verifyStatic(SHNLogger.class, times(1));
        SHNLogger.registerLogger(any(LoggerImplementation.class));
    }

    @Test
    public void givenBleTransportContextIsConstructed_whenLogIsDisabledInRuntimeConfiguration_thenNoLoggerIsRegisteredOnBluelib() {
        when(runtimeConfigurationMock.isLogEnabled()).thenReturn(false);

        constructBleTransportContext();

        verifyStatic(SHNLogger.class, never());
        SHNLogger.registerLogger(any(LoggerImplementation.class));
    }

    @Test
    public void givenTaggingIsEnabledInRuntimeConfiguration_whenBleTransportContextIsConstructed_thenATaggerIsRegisteredOnBluelib() {
        when(runtimeConfigurationMock.isTaggingEnabled()).thenReturn(true);

        constructBleTransportContext();

        verifyStatic(SHNTagger.class, times(1));
        SHNTagger.registerTagger(any(SHNTagger.Tagger.class));
    }

    @Test
    public void givenTaggingIsDisabledInRuntimeConfiguration_whenBleTransportContextIsConstructed_thenNoTaggerIsRegisteredOnBluelib() {
        when(runtimeConfigurationMock.isTaggingEnabled()).thenReturn(false);

        constructBleTransportContext();

        verifyStatic(SHNTagger.class, never());
        SHNTagger.registerTagger(any(SHNTagger.Tagger.class));
    }


    @Test(expected = TransportUnavailableException.class)
    public void givenBluetoothHardwareIsUnavailable_whenBleTransportContextIsConstructed_thenThrowAnException() {
        new BleTransportContext(runtimeConfigurationMock) {
            @NonNull
            @Override
            SHNCentral createCentral(final RuntimeConfiguration runtimeConfiguration, final boolean showPopupIfBLEIsTurnedOff) throws SHNBluetoothHardwareUnavailableException {
                throw new SHNBluetoothHardwareUnavailableException();
            }
        };
    }

    @Test
    public void givenBleTransportContextIsConstructed_whenSHNCentralBecomesNotReady_thenDiscoveredNodesAreCleared() {
        constructBleTransportContext();

        shnCentralListenerArgumentCaptor.getValue().onStateUpdated(shnCentralMock, SHNCentral.State.SHNCentralStateNotReady);

        verify(discoveryStrategyMock, times(1)).clearDiscoveredNetworkNodes();
    }

    private void constructBleTransportContext() {
        bleTransportContext = new BleTransportContext(runtimeConfigurationMock) {
            @NonNull
            @Override
            DeviceCache createDeviceCache() {
                return deviceCacheMock;
            }

            @NonNull
            @Override
            SHNCentral createCentral(final RuntimeConfiguration runtimeConfiguration, final boolean showPopupIfBLEIsTurnedOff) throws SHNBluetoothHardwareUnavailableException {
                return shnCentralMock;
            }

            @NonNull
            @Override
            DiscoveryStrategy createDiscoveryStrategy(@NonNull RuntimeConfiguration runtimeConfiguration) {
                return discoveryStrategyMock;
            }

            @NonNull
            @Override
            public CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
                return communicationStrategyMock;
            }
        };

        verify(shnCentralMock).registerDeviceDefinition(any(ReferenceNodeDeviceDefinitionInfo.class));
        verify(shnCentralMock).registerShnCentralListener(shnCentralListenerArgumentCaptor.capture());
    }
}
